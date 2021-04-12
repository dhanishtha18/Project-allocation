package org.projectapp.Adapter;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.pdf.PdfRenderer;
import android.os.Environment;
import android.os.ParcelFileDescriptor;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.AppCompatTextView;
import androidx.core.content.FileProvider;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.storage.FileDownloadTask;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;


import org.projectapp.Model.Chat;
import org.projectapp.R;
import org.projectapp.ViewImageActivity;

import java.io.File;
import java.io.IOException;
import java.util.List;


public class MessageAdapter extends RecyclerView.Adapter<MessageAdapter.ViewHolder> {

    public static final int MSG_TYPE_LEFT = 0;
    public static final int MSG_TYPE_RIGHT = 1;

    private Context mContext;
    private List<Chat> mChat;
    private String imageurl;
    File file;

    FirebaseUser fuser;

    public MessageAdapter(Context mContext, List<Chat> mChat, String imageurl) {
        this.mChat = mChat;
        this.mContext = mContext;
        this.imageurl = imageurl;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        if (viewType == MSG_TYPE_RIGHT) {
            View view = LayoutInflater.from(mContext).inflate(R.layout.chat_item_right, parent, false);
            return new MessageAdapter.ViewHolder(view);
        } else {
            View view = LayoutInflater.from(mContext).inflate(R.layout.chat_item_left, parent, false);
            return new MessageAdapter.ViewHolder(view);
        }
    }

    @Override
    public void onBindViewHolder(@NonNull final ViewHolder holder, int position) {

        final Chat chat = mChat.get(position);

        holder.show_message.setText(chat.getMessage());
        holder.time.setText(chat.getTime());

        holder.profile_image.setImageResource(R.drawable.profile);


        if (chat.getUrl() != null) {
            if (chat.getType().equals("jpg")) {
                holder.show_image.setVisibility(View.VISIBLE);
                try{
                    final File dirl = new File(Environment.getExternalStorageDirectory().getPath() + "//Discuss");
                    if (!dirl.exists()) {
                        dirl.mkdirs();
                    }
                    final File file = new File(Environment.getExternalStorageDirectory().getPath() + "//Discuss/" + chat.getMessage()+chat.getSender() + ".jpg");
                    if (file.exists()){
                        if (mContext!=null) {
                            Glide.with(mContext).load(file).into(holder.show_image);
                        }
                    }else {
                        file.createNewFile();
                        StorageReference storageReference = FirebaseStorage.getInstance().getReferenceFromUrl(chat.getUrl());
                        storageReference.getFile(file).addOnSuccessListener(new OnSuccessListener<FileDownloadTask.TaskSnapshot>() {
                            @Override
                            public void onSuccess(FileDownloadTask.TaskSnapshot taskSnapshot) {
                                if (mContext!=null) {
                                    Glide.with(mContext).load(file).into(holder.show_image);
                                }
                            }
                        });
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            } else {
                holder.show_image.setVisibility(View.VISIBLE);
                try {

                    final File dirl = new File(Environment.getExternalStorageDirectory().getPath() + "//Discuss");
                    if (!dirl.exists()) {
                        dirl.mkdirs();
                    }
                    file = new File(Environment.getExternalStorageDirectory().getPath() + "//Discuss/" + chat.getMessage()+chat.getSender() + ".pdf");
                    if (file.exists()){
                        try {
                            ParcelFileDescriptor fileDescriptor = ParcelFileDescriptor.open(file, ParcelFileDescriptor.MODE_READ_ONLY);
                            PdfRenderer mPdfRenderer = new PdfRenderer(fileDescriptor);

                            PdfRenderer.Page mPdfPage = mPdfRenderer.openPage(0);
                            Bitmap bitmap = Bitmap.createBitmap(mPdfPage.getWidth(),
                                    mPdfPage.getHeight(),
                                    Bitmap.Config.ARGB_8888);
                            mPdfPage.render(bitmap, null, null, PdfRenderer.Page.RENDER_MODE_FOR_PRINT);
                            holder.show_image.setImageBitmap(bitmap);

                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }else {
                        file.createNewFile();
                        StorageReference storageReference = FirebaseStorage.getInstance().getReferenceFromUrl(chat.getUrl());
                        storageReference.getFile(file).addOnSuccessListener(new OnSuccessListener<FileDownloadTask.TaskSnapshot>() {
                            @Override
                            public void onSuccess(FileDownloadTask.TaskSnapshot taskSnapshot) {
                                try {
                                    ParcelFileDescriptor fileDescriptor = ParcelFileDescriptor.open(file, ParcelFileDescriptor.MODE_READ_ONLY);
                                    PdfRenderer mPdfRenderer = new PdfRenderer(fileDescriptor);

                                    PdfRenderer.Page mPdfPage = mPdfRenderer.openPage(0);
                                    Bitmap bitmap = Bitmap.createBitmap(mPdfPage.getWidth(),
                                            mPdfPage.getHeight(),
                                            Bitmap.Config.ARGB_8888);
                                    mPdfPage.render(bitmap, null, null, PdfRenderer.Page.RENDER_MODE_FOR_PRINT);
                                    holder.show_image.setImageBitmap(bitmap);

                                } catch (IOException e) {
                                    e.printStackTrace();
                                }
                            }
                        });
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }


        holder.show_image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (chat.getType().equals("jpg")) {
                    Intent i = new Intent(mContext, ViewImageActivity.class);
                    i.putExtra("url", "");
                    i.putExtra("title", "");
                    i.putExtra("id", "");
                    i.putExtra("path", "");
                    i.putExtra("fullpath", Environment.getExternalStorageDirectory().getPath() + "//Discuss/" + chat.getMessage() +chat.getSender()+ ".jpg");
                    mContext.startActivity(i);
                } else {
                    mContext.startActivity(new Intent(Intent.ACTION_VIEW, FileProvider.getUriForFile(mContext, "projectapp" + ".provider", file)).addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION));

                }
            }
        });

        if (position == mChat.size() - 1) {
            if (chat.isIsseen()) {
                holder.txt_seen.setText("Seen");
            } else {
                holder.txt_seen.setText("Delivered");
            }
        } else {
            holder.txt_seen.setVisibility(View.GONE);
        }
    }

    @Override
    public int getItemCount() {
        return mChat.size();
    }


    public class ViewHolder extends RecyclerView.ViewHolder {

        public AppCompatTextView show_message;
        public AppCompatTextView txt_seen, time;
        public ImageView profile_image;
        public ImageView show_image;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            show_message = itemView.findViewById(R.id.show_message);
            profile_image = itemView.findViewById(R.id.profile_image);
            txt_seen = itemView.findViewById(R.id.txt_seen);
            show_image = itemView.findViewById(R.id.show_image);
            time = itemView.findViewById(R.id.time);
        }
    }

    @Override
    public int getItemViewType(int position) {
        fuser = FirebaseAuth.getInstance().getCurrentUser();
        if (mChat.get(position).getSender().equals(fuser.getUid())) {
            return MSG_TYPE_RIGHT;
        } else {
            return MSG_TYPE_LEFT;
        }
    }

}
