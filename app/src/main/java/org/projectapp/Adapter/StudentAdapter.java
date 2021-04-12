package org.projectapp.Adapter;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.AppCompatButton;
import androidx.appcompat.widget.AppCompatTextView;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;

import org.projectapp.Model.Student;
import org.projectapp.R;
import org.projectapp.ViewStudentProfileActivity;

import java.util.List;

public class StudentAdapter extends RecyclerView.Adapter<StudentAdapter.ViewHolder> {

    private Context context;
    private List<Student> uploads;
    String loginas, admin_login, admin_password;
    Boolean unactive;
    ProgressDialog dialog;

    public StudentAdapter(Context context, List<Student> uploads, Boolean unactive, String loginas, String admin_login, String admin_password) {
        this.uploads = uploads;
        this.context = context;
        this.loginas = loginas;
        this.admin_login = admin_login;
        this.admin_password = admin_password;
        this.unactive = unactive;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.layout_unactive, parent, false);
        ViewHolder viewHolder = new ViewHolder(v);
        return viewHolder;
    }


    public void onBindViewHolder(ViewHolder holder, final int position) {
        final Student upload = uploads.get(position);

        if (loginas.equals("lecturer")) {
            holder.viewh.setVisibility(View.GONE);
            holder.delete.setVisibility(View.GONE);
        }

        if (loginas.equals("principal")) {
            holder.hod_login_id.setVisibility(View.GONE);
            holder.hod_name.setText("Name: " + upload.getName());
            holder.hod_password.setText("Department: " + upload.getDepartment());
            holder.hod_department.setText("Skills: " + upload.getSkills());
        } else {
            if (unactive) {
                holder.hod_name.setText("Name: " + upload.getName());
                holder.hod_password.setText("Temporary Password: " + upload.getPassword());
                holder.hod_login_id.setText("Login ID: " + upload.getEnrollment());
                holder.hod_department.setText("Skills: " + upload.getSkills());
            } else {
                holder.hod_login_id.setVisibility(View.GONE);
                holder.hod_password.setVisibility(View.GONE);
                holder.hod_name.setText("Name: " + upload.getName());
                holder.hod_department.setText("Skills: " + upload.getSkills());
            }
        }

        holder.viewh.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                context.startActivity(new Intent(context, ViewStudentProfileActivity.class)
                        .putExtra("department", upload.getDepartment())
                        .putExtra("id", upload.getId())
                        .putExtra("semester", upload.getSemester())
                        .putExtra("loginas", loginas));
            }
        });

        holder.delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder alert=new AlertDialog.Builder(context);
                alert.setTitle("Confirm");
                alert.setMessage("Do you really want to delete Student "+upload.getName());
                alert.setPositiveButton("Confirm", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int which) {
                        dialog = new ProgressDialog(context);
                        dialog.setMessage("Deleting...");
                        dialog.setCancelable(false);
                        dialog.show();
                        FirebaseAuth.getInstance().signOut();
                        FirebaseAuth.getInstance().signInWithEmailAndPassword(upload.getEmail(), upload.getPassword())
                        .addOnSuccessListener(new OnSuccessListener<AuthResult>() {
                            @Override
                            public void onSuccess(AuthResult authResult) {
                                FirebaseAuth.getInstance().getCurrentUser().delete().addOnFailureListener(new OnFailureListener() {
                                    @Override
                                    public void onFailure(@NonNull Exception e) {
                                        FirebaseAuth.getInstance().signInWithEmailAndPassword(admin_login,admin_password);
                                        dialog.dismiss();
                                    }
                                }).addOnSuccessListener(new OnSuccessListener<Void>() {
                                    @Override
                                    public void onSuccess(Void aVoid) {
                                        FirebaseAuth.getInstance().signInWithEmailAndPassword(admin_login, admin_password)
                                                .addOnSuccessListener(new OnSuccessListener<AuthResult>() {
                                                    @Override
                                                    public void onSuccess(AuthResult authResult) {
                                                        FirebaseDatabase.getInstance().getReference("Students").child(upload.getDepartment()).child(upload.getSemester()).child(upload.getId())
                                                                .removeValue().addOnSuccessListener(new OnSuccessListener<Void>() {
                                                            @Override
                                                            public void onSuccess(Void aVoid) {
                                                                if (!upload.getImage_url().equals("default")) {
                                                                    FirebaseStorage.getInstance().getReferenceFromUrl(upload.getImage_url()).delete();
                                                                }
                                                                dialog.dismiss();
                                                                uploads.clear();
                                                            }
                                                        }).addOnFailureListener(new OnFailureListener() {
                                                            @Override
                                                            public void onFailure(@NonNull Exception e) {
                                                                dialog.dismiss();
                                                            }
                                                        });
                                                    }
                                                });

                                    }
                                });
                            }
                        }).addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                FirebaseAuth.getInstance().signInWithEmailAndPassword(admin_login,admin_password);
                                dialog.dismiss();
                            }
                        });

                    }
                });
                alert.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                });
                alert.show();
            }
        });
    }

    @Override
    public int getItemCount() {
        return uploads.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {

        public AppCompatTextView hod_name, hod_password, hod_login_id, hod_department;
        public AppCompatButton viewh, delete;

        public ViewHolder(View itemView) {
            super(itemView);

            hod_name = itemView.findViewById(R.id.hod_name);
            hod_password = itemView.findViewById(R.id.hod_password);
            hod_login_id = itemView.findViewById(R.id.hod_login_id);
            hod_department = itemView.findViewById(R.id.hod_department);
            viewh = itemView.findViewById(R.id.view);
            delete = itemView.findViewById(R.id.delete);
        }
    }
}
