package org.projectapp.Adapter;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Environment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.widget.AppCompatButton;
import androidx.appcompat.widget.AppCompatTextView;
import androidx.core.content.FileProvider;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FileDownloadTask;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import org.projectapp.Model.Notice;
import org.projectapp.R;
import org.projectapp.ViewImageActivity;

import java.io.File;
import java.util.List;


public class NoticeAdapter extends RecyclerView.Adapter<NoticeAdapter.ViewHolder> {

    private Context context;
    private List<Notice> uploads;
    private String loginas, path, notice;

    public NoticeAdapter(Context context, List<Notice> uploads, String loginas, String path, String notice) {
        this.uploads = uploads;
        this.context = context;
        this.loginas = loginas;
        this.path=path;
        this.notice=notice;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.layout_notice, parent, false);
        ViewHolder viewHolder = new ViewHolder(v);
        return viewHolder;
    }


    public void onBindViewHolder(ViewHolder holder, final int position) {
        final Notice upload = uploads.get(position);


        holder.notice_title.setText("Title: "+upload.getTitle());
        holder.uploadedby.setText("Uploaded By: "+upload.getUploadedby());
        holder.date.setText("Date & Time: "+upload.getDate());

        if (loginas.equals("student")) {
            holder.notice_delete.setVisibility(View.GONE);
        } else {
            holder.notice_delete.setVisibility(View.VISIBLE);
        }

        if (notice.contains("staff")){
            if (loginas.equals("lecturer")) {
                holder.notice_delete.setVisibility(View.GONE);
            } else if (loginas.equals("hod")){
                holder.notice_delete.setVisibility(View.VISIBLE);
            }
        }else if (notice.contains("HOD")){
            if (loginas.equals("hod")) {
                holder.notice_delete.setVisibility(View.GONE);
            } else if (loginas.equals("principal")){
                holder.notice_delete.setVisibility(View.VISIBLE);
            }
        }

        holder.notice_view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(upload.getType().equals("jpg")) {
                    Intent i = new Intent(context, ViewImageActivity.class);
                    i.putExtra("url", upload.getUrl());
                    i.putExtra("title", upload.getTitle());
                    i.putExtra("id", upload.getId());
                    i.putExtra("path", Environment.getExternalStorageDirectory().getPath()+"/rait/"+notice);
                    i.putExtra("fullpath",Environment.getExternalStorageDirectory().getPath()+"/rait/"+notice+"/"+upload.getTitle()+upload.getId()+".jpg");
                    context.startActivity(i);
                }else{
                    String fullPath = Environment.getExternalStorageDirectory().getPath()+"/rait/"+notice;
                    File dir1 = new File(fullPath);
                    if (!dir1.exists()) {
                        dir1.mkdirs();
                    }
                    String path = Environment.getExternalStorageDirectory().getPath()+"/rait/"+notice+"/"+upload.getTitle()+upload.getId()+".pdf";
                    final File dir = new File(path);
                    if(dir.exists()){
                        context.startActivity(new Intent(Intent.ACTION_VIEW, FileProvider.getUriForFile(context, "org.projectapp" + ".provider", dir)).addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION));
                    } else {
                        final ProgressDialog dialog=new ProgressDialog(context);
                        dialog.setMessage("Opening...");
                        dialog.setCancelable(false);
                        dialog.show();
                        FirebaseStorage.getInstance().getReferenceFromUrl(upload.getUrl()).getFile(dir)
                                .addOnSuccessListener(new OnSuccessListener<FileDownloadTask.TaskSnapshot>() {
                                    @Override
                                    public void onSuccess(FileDownloadTask.TaskSnapshot taskSnapshot) {
                                        context.startActivity(new Intent(Intent.ACTION_VIEW, FileProvider.getUriForFile(context, "org.projectapp" + ".provider", dir)).addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION));
                                        dialog.dismiss();
                                    }
                                }).addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                context.startActivity(new Intent(Intent.ACTION_VIEW,Uri.parse(upload.getUrl())));
                                dialog.dismiss();
                            }
                        });
                    }
                }
            }
        });

        holder.notice_delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder alert=new AlertDialog.Builder(context);
                alert.setMessage("Do you really want to delete "+upload.getTitle()+"?");
                alert.setTitle("Delete notice");
                alert.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        StorageReference storageReference= FirebaseStorage.getInstance().getReferenceFromUrl(upload.getUrl());
                        storageReference.delete();
                        DatabaseReference databaseReference=FirebaseDatabase.getInstance().getReference(path).child(upload.getId());
                        databaseReference.removeValue();
                        Toast.makeText(context, "Notice Deleted Successfully.", Toast.LENGTH_SHORT).show();
                        uploads.clear();
                    }
                });
                alert.setNegativeButton("No", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                });
                alert.create();
                if (context!=null) {
                    alert.show();
                }

            }
        });
    }

    @Override
    public int getItemCount() {
        return uploads.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {

        public AppCompatTextView notice_title, uploadedby, date;
        AppCompatButton notice_delete, notice_view;

        public ViewHolder(View itemView) {
            super(itemView);

            notice_title = itemView.findViewById(R.id.notice_title);
            uploadedby =  itemView.findViewById(R.id.uploadedby);
            date = itemView.findViewById(R.id.upload_date);
            notice_delete = itemView.findViewById(R.id.notice_delete);
            notice_view = itemView.findViewById(R.id.notice_view);
        }
    }

}
