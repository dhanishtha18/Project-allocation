package org.projectapp.Adapter;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;
import android.graphics.pdf.PdfRenderer;
import android.graphics.pdf.PdfRenderer.Page;
import android.os.Environment;
import android.os.ParcelFileDescriptor;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ImageView;
import androidx.annotation.NonNull;
import androidx.appcompat.widget.AppCompatTextView;
import androidx.core.content.FileProvider;
import androidx.recyclerview.widget.RecyclerView.Adapter;
import com.bumptech.glide.Glide;
import com.google.android.gms.common.internal.ImagesContract;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.storage.FileDownloadTask.TaskSnapshot;
import com.google.firebase.storage.FirebaseStorage;
import java.io.File;
import java.io.IOException;
import java.util.List;
import org.apache.poi.openxml4j.opc.ContentTypes;
import org.projectapp.Model.Chat;
import org.projectapp.R;
import org.projectapp.ViewImageActivity;

public class DiscussionAdapter extends Adapter<DiscussionAdapter.ViewHolder> {
    public static final int MSG_TYPE_LEFT = 0;
    public static final int MSG_TYPE_RIGHT = 1;
    File file;
    FirebaseUser fuser;
    private List<Chat> mChat;
    /* access modifiers changed from: private */
    public Context mContext;
    int viewType;

    public class ViewHolder extends androidx.recyclerview.widget.RecyclerView.ViewHolder {
        public AppCompatTextView name;
        public ImageView profile_image;
        public ImageView show_image;
        public AppCompatTextView show_message;
        public AppCompatTextView time;
        public AppCompatTextView txt_seen;

        public ViewHolder(@NonNull View view) {
            super(view);
            this.show_message = (AppCompatTextView) view.findViewById(R.id.show_message);
            this.profile_image = (ImageView) view.findViewById(R.id.profile_image);
            this.txt_seen = (AppCompatTextView) view.findViewById(R.id.txt_seen);
            this.show_image = (ImageView) view.findViewById(R.id.show_image);
            this.time = (AppCompatTextView) view.findViewById(R.id.time);
            this.name = (AppCompatTextView) view.findViewById(R.id.name);
        }
    }

    public DiscussionAdapter(Context context, List<Chat> list) {
        this.mChat = list;
        this.mContext = context;
    }

    @NonNull
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        this.viewType = i;
        if (i == 1) {
            return new ViewHolder(LayoutInflater.from(this.mContext).inflate(R.layout.chat_item_right, viewGroup, false));
        }
        return new ViewHolder(LayoutInflater.from(this.mContext).inflate(R.layout.chat_item_left, viewGroup, false));
    }

    public void onBindViewHolder(@NonNull final ViewHolder viewHolder, int i) {
        final Chat chat = (Chat) this.mChat.get(i);
        if (this.viewType == 0) {
            viewHolder.name.setVisibility(View.VISIBLE);
            viewHolder.name.setText(chat.getReceiver());
        } else {
            viewHolder.name.setVisibility(View.GONE);
        }
        viewHolder.show_message.setText(chat.getMessage());
        viewHolder.time.setText(chat.getTime());
        if (chat.getProfile().equals("default")) {
            viewHolder.profile_image.setImageResource(R.drawable.profile);
        } else {
            Context context = this.mContext;
            if (context != null) {
                Glide.with(context).load(chat.getProfile()).into(viewHolder.profile_image);
            }
        }
        if (chat.getUrl() != null) {
            String str = "//Discuss/";
            String str2 = "//Discuss";
            if (chat.getType().equals(ContentTypes.EXTENSION_JPG_1)) {
                viewHolder.show_image.setVisibility(View.VISIBLE);
                try {
                    StringBuilder sb = new StringBuilder();
                    sb.append(Environment.getExternalStorageDirectory().getPath());
                    sb.append(str2);
                    File file2 = new File(sb.toString());
                    if (!file2.exists()) {
                        file2.mkdirs();
                    }
                    StringBuilder sb2 = new StringBuilder();
                    sb2.append(Environment.getExternalStorageDirectory().getPath());
                    sb2.append(str);
                    sb2.append(chat.getMessage());
                    sb2.append(chat.getSender());
                    sb2.append(".jpg");
                    final File file3 = new File(sb2.toString());
                    if (!file3.exists()) {
                        file3.createNewFile();
                        FirebaseStorage.getInstance().getReferenceFromUrl(chat.getUrl()).getFile(file3).addOnSuccessListener((OnSuccessListener) new OnSuccessListener<TaskSnapshot>() {
                            public void onSuccess(TaskSnapshot taskSnapshot) {
                                if (DiscussionAdapter.this.mContext != null) {
                                    Glide.with(DiscussionAdapter.this.mContext).load(file3).into(viewHolder.show_image);
                                }
                            }
                        });
                    } else if (this.mContext != null) {
                        Glide.with(this.mContext).load(file3).into(viewHolder.show_image);
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            } else {
                viewHolder.show_image.setVisibility(View.VISIBLE);
                try {
                    StringBuilder sb3 = new StringBuilder();
                    sb3.append(Environment.getExternalStorageDirectory().getPath());
                    sb3.append(str2);
                    File file4 = new File(sb3.toString());
                    if (!file4.exists()) {
                        file4.mkdirs();
                    }
                    StringBuilder sb4 = new StringBuilder();
                    sb4.append(Environment.getExternalStorageDirectory().getPath());
                    sb4.append(str);
                    sb4.append(chat.getMessage());
                    sb4.append(chat.getSender());
                    sb4.append(".pdf");
                    this.file = new File(sb4.toString());
                    if (this.file.exists()) {
                        try {
                            Page openPage = new PdfRenderer(ParcelFileDescriptor.open(this.file, 268435456)).openPage(View.VISIBLE);
                            Bitmap createBitmap = Bitmap.createBitmap(openPage.getWidth(), openPage.getHeight(), Config.ARGB_8888);
                            openPage.render(createBitmap, null, null, Page.RENDER_MODE_FOR_PRINT);
                            viewHolder.show_image.setImageBitmap(createBitmap);
                        } catch (IOException e2) {
                            e2.printStackTrace();
                        }
                    } else {
                        this.file.createNewFile();
                        FirebaseStorage.getInstance().getReferenceFromUrl(chat.getUrl()).getFile(this.file).addOnSuccessListener((OnSuccessListener) new OnSuccessListener<TaskSnapshot>() {
                            public void onSuccess(TaskSnapshot taskSnapshot) {
                                try {
                                    Page openPage = new PdfRenderer(ParcelFileDescriptor.open(DiscussionAdapter.this.file, 268435456)).openPage(View.VISIBLE);
                                    Bitmap createBitmap = Bitmap.createBitmap(openPage.getWidth(), openPage.getHeight(), Config.ARGB_8888);
                                    openPage.render(createBitmap, null, null, Page.RENDER_MODE_FOR_PRINT);
                                    viewHolder.show_image.setImageBitmap(createBitmap);
                                } catch (IOException e) {
                                    e.printStackTrace();
                                }
                            }
                        });
                    }
                } catch (IOException e3) {
                    e3.printStackTrace();
                }
            }
        }
        viewHolder.show_image.setOnClickListener(new OnClickListener() {
            public void onClick(View view) {
                if (chat.getType().equals(ContentTypes.EXTENSION_JPG_1)) {
                    Intent intent = new Intent(DiscussionAdapter.this.mContext, ViewImageActivity.class);
                    String str = "";
                    intent.putExtra(ImagesContract.URL, str);
                    intent.putExtra("title", str);
                    intent.putExtra("id", str);
                    intent.putExtra("path", str);
                    StringBuilder sb = new StringBuilder();
                    sb.append(Environment.getExternalStorageDirectory().getPath());
                    sb.append("//Discuss/");
                    sb.append(chat.getMessage());
                    sb.append(chat.getSender());
                    sb.append(".jpg");
                    intent.putExtra("fullpath", sb.toString());
                    DiscussionAdapter.this.mContext.startActivity(intent);
                    return;
                }
                DiscussionAdapter.this.mContext.startActivity(new Intent("android.intent.action.VIEW", FileProvider.getUriForFile(DiscussionAdapter.this.mContext, "rait.provider", DiscussionAdapter.this.file)));
            }
        });
        viewHolder.txt_seen.setVisibility(View.GONE);
    }

    public int getItemCount() {
        return this.mChat.size();
    }

    public int getItemViewType(int i) {
        this.fuser = FirebaseAuth.getInstance().getCurrentUser();
        return ((Chat) this.mChat.get(i)).getSender().equals(this.fuser.getUid()) ? 1 : 0;
    }
}
