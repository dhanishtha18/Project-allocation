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
import androidx.appcompat.widget.AppCompatImageButton;
import androidx.appcompat.widget.AppCompatTextView;
import androidx.core.content.FileProvider;
import androidx.recyclerview.widget.RecyclerView.Adapter;
import com.bumptech.glide.Glide;
import com.google.android.gms.common.internal.ImagesContract;
import com.google.android.gms.tasks.OnSuccessListener;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FileDownloadTask;
import com.google.firebase.storage.FirebaseStorage;

import java.io.File;
import java.io.IOException;
import java.util.List;
import org.apache.poi.openxml4j.opc.ContentTypes;
import org.projectapp.Model.Complaint;
import org.projectapp.Model.Vote;
import org.projectapp.R;
import org.projectapp.ViewImageActivity;

public class ComplaintAdapter extends Adapter<ComplaintAdapter.ViewHolder> {
    public static final int MSG_TYPE_LEFT = 0;
    public static final int MSG_TYPE_RIGHT = 1;
    private List<Complaint> complaints;
    File file;
    FirebaseUser fuser;
    /* access modifiers changed from: private */
    public Context mContext;
    int viewType;

    class ViewHolder extends androidx.recyclerview.widget.RecyclerView.ViewHolder {
        public AppCompatTextView name;
        public AppCompatImageButton negative;
        public AppCompatTextView negative_vote;
        public AppCompatImageButton positive;
        public AppCompatTextView positive_vote;
        public ImageView show_image;
        public AppCompatTextView show_message;
        public AppCompatTextView time;

        public ViewHolder(@NonNull View view) {
            super(view);
            this.show_message = (AppCompatTextView) view.findViewById(R.id.message);
            this.positive_vote = (AppCompatTextView) view.findViewById(R.id.positive_vote);
            this.negative_vote = (AppCompatTextView) view.findViewById(R.id.negative_vote);
            this.positive = (AppCompatImageButton) view.findViewById(R.id.positive);
            this.time = (AppCompatTextView) view.findViewById(R.id.time);
            this.negative = (AppCompatImageButton) view.findViewById(R.id.negative);
            this.show_image = (ImageView) view.findViewById(R.id.show_image);
            this.name = (AppCompatTextView) view.findViewById(R.id.name);
        }
    }

    public ComplaintAdapter(Context context, List<Complaint> list) {
        this.complaints = list;
        this.mContext = context;
    }

    @NonNull
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        this.viewType = i;
        if (i == 1) {
            return new ViewHolder(LayoutInflater.from(this.mContext).inflate(R.layout.layout_complaint_right, viewGroup, false));
        }
        return new ViewHolder(LayoutInflater.from(this.mContext).inflate(R.layout.layout_complaint_left, viewGroup, false));
    }

    public void onBindViewHolder(@NonNull final ViewHolder viewHolder, int i) {
        final Complaint complaint = (Complaint) this.complaints.get(i);
        if (complaint.getMsgtype() == null || !complaint.getMsgtype().equals("complaint")) {
            viewHolder.positive.setVisibility(View.GONE);
            viewHolder.positive_vote.setVisibility(View.GONE);
            viewHolder.negative.setVisibility(View.GONE);
            viewHolder.negative_vote.setVisibility(View.GONE);
        } else {
            viewHolder.positive.setVisibility(View.VISIBLE);
            viewHolder.positive_vote.setVisibility(View.VISIBLE);
            viewHolder.negative.setVisibility(View.VISIBLE);
            viewHolder.negative_vote.setVisibility(View.VISIBLE);
        }
        if (complaint.getName().equals("") || this.viewType == 1) {
            viewHolder.name.setVisibility(View.GONE);
        } else {
            viewHolder.name.setVisibility(View.VISIBLE);
            viewHolder.name.setText(complaint.getName());
        }
        viewHolder.show_message.setText(complaint.getMessage());
        viewHolder.time.setText(complaint.getTime());
        if (complaint.getUrl() != null) {
            String str = "/rait/Complaint/";
            String str2 = "/rait/Complaint";
            if (complaint.getType().equals(ContentTypes.EXTENSION_JPG_1)) {
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
                    sb2.append(complaint.getMessage());
                    sb2.append(complaint.getId());
                    sb2.append(".jpg");
                    final File file3 = new File(sb2.toString());
                    if (!file3.exists()) {
                        file3.createNewFile();
                        FirebaseStorage.getInstance().getReferenceFromUrl(complaint.getUrl()).getFile(file3).addOnSuccessListener((OnSuccessListener) new OnSuccessListener<FileDownloadTask.TaskSnapshot>() {
                            public void onSuccess(FileDownloadTask.TaskSnapshot taskSnapshot) {
                                if (ComplaintAdapter.this.mContext != null) {
                                    Glide.with(ComplaintAdapter.this.mContext).load(file3).into(viewHolder.show_image);
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
                    sb4.append(complaint.getMessage());
                    sb4.append(complaint.getId());
                    sb4.append(".pdf");
                    this.file = new File(sb4.toString());
                    if (this.file.exists()) {
                        try {
                            Page openPage = new PdfRenderer(ParcelFileDescriptor.open(this.file, 268435456)).openPage(View.VISIBLE);
                            Bitmap createBitmap = Bitmap.createBitmap(openPage.getWidth(), openPage.getHeight(), Config.ARGB_8888);
                            openPage.render(createBitmap, null, null, Page.RENDER_MODE_FOR_DISPLAY);
                            viewHolder.show_image.setImageBitmap(createBitmap);
                        } catch (IOException e2) {
                            e2.printStackTrace();
                        }
                    } else {
                        this.file.createNewFile();
                        FirebaseStorage.getInstance().getReferenceFromUrl(complaint.getUrl()).getFile(this.file).addOnSuccessListener((OnSuccessListener) new OnSuccessListener<FileDownloadTask.TaskSnapshot>() {
                            public void onSuccess(FileDownloadTask.TaskSnapshot taskSnapshot) {
                                try {
                                    Page openPage = new PdfRenderer(ParcelFileDescriptor.open(ComplaintAdapter.this.file, 268435456)).openPage(View.VISIBLE);
                                    Bitmap createBitmap = Bitmap.createBitmap(openPage.getWidth(), openPage.getHeight(), Config.ARGB_8888);
                                    openPage.render(createBitmap, null, null, Page.RENDER_MODE_FOR_DISPLAY);
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
        FirebaseDatabase.getInstance().getReference("Votes").child(complaint.getId()).addValueEventListener(new ValueEventListener() {
            public void onCancelled(@NonNull DatabaseError databaseError) {
            }

            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                int i = 0;
                int i2 = 0;
                for (DataSnapshot dataSnapshot2 : dataSnapshot.getChildren()) {
                    if (dataSnapshot2.getChildrenCount() != 0) {
                        Vote vote = (Vote) dataSnapshot2.getValue(Vote.class);
                        if (vote.getPositive().booleanValue()) {
                            i++;
                            if (FirebaseAuth.getInstance().getCurrentUser().getUid().equals(dataSnapshot2.getKey())) {
                                viewHolder.positive.setImageResource(R.drawable.positive_blue);
                                viewHolder.negative.setImageResource(R.drawable.negative_black);
                            }
                        }
                        if (vote.getNegative().booleanValue()) {
                            i2++;
                            if (FirebaseAuth.getInstance().getCurrentUser().getUid().equals(dataSnapshot2.getKey())) {
                                viewHolder.positive.setImageResource(R.drawable.positive_black);
                                viewHolder.negative.setImageResource(R.drawable.negative_blue);
                            }
                        }
                        viewHolder.positive_vote.setText(String.valueOf(i));
                        viewHolder.negative_vote.setText(String.valueOf(i2));
                    }
                }
            }
        });
        viewHolder.positive.setOnClickListener(new OnClickListener() {
            public void onClick(View view) {
                FirebaseDatabase.getInstance().getReference("Votes").child(complaint.getId()).child(FirebaseAuth.getInstance().getCurrentUser().getUid()).setValue(new Vote(Boolean.valueOf(true), Boolean.valueOf(false)));
            }
        });
        viewHolder.negative.setOnClickListener(new OnClickListener() {
            public void onClick(View view) {
                FirebaseDatabase.getInstance().getReference("Votes").child(complaint.getId()).child(FirebaseAuth.getInstance().getCurrentUser().getUid()).setValue(new Vote(Boolean.valueOf(false), Boolean.valueOf(true)));
            }
        });
        viewHolder.show_image.setOnClickListener(new OnClickListener() {
            public void onClick(View view) {
                if (complaint.getType().equals(ContentTypes.EXTENSION_JPG_1)) {
                    Intent intent = new Intent(ComplaintAdapter.this.mContext, ViewImageActivity.class);
                    String str = "";
                    intent.putExtra(ImagesContract.URL, str);
                    intent.putExtra("title", str);
                    intent.putExtra("id", str);
                    intent.putExtra("path", str);
                    StringBuilder sb = new StringBuilder();
                    sb.append(Environment.getExternalStorageDirectory().getPath());
                    sb.append("/rait/Complaint/");
                    sb.append(complaint.getMessage());
                    sb.append(complaint.getId());
                    sb.append(".jpg");
                    intent.putExtra("fullpath", sb.toString());
                    ComplaintAdapter.this.mContext.startActivity(intent);
                    return;
                }
                ComplaintAdapter.this.mContext.startActivity(new Intent("android.intent.action.VIEW", FileProvider.getUriForFile(ComplaintAdapter.this.mContext, "org.rait.provider", ComplaintAdapter.this.file)));
            }
        });
    }

    public int getItemCount() {
        return this.complaints.size();
    }

    public int getItemViewType(int i) {
        this.fuser = FirebaseAuth.getInstance().getCurrentUser();
        return ((Complaint) this.complaints.get(i)).getSender().equals(this.fuser.getUid()) ? 1 : 0;
    }
}
