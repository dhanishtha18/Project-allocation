package org.projectapp.Adapter;

import android.app.AlertDialog.Builder;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.appcompat.widget.AppCompatButton;
import androidx.appcompat.widget.AppCompatTextView;
import androidx.recyclerview.widget.RecyclerView.Adapter;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import java.util.List;
import org.projectapp.Model.Hod;
import org.projectapp.R;
import org.projectapp.ViewHodProfileActivity;

public class HodAdapter extends Adapter<HodAdapter.ViewHolder> {
    /* access modifiers changed from: private */
    public String admin_login;
    /* access modifiers changed from: private */
    public String admin_password;
    /* access modifiers changed from: private */
    public Context context;
    Boolean isavailable = Boolean.valueOf(false);
    private String loginas;
    Boolean unactive;
    /* access modifiers changed from: private */
    public List<Hod> uploads;

    class ViewHolder extends androidx.recyclerview.widget.RecyclerView.ViewHolder {
        public AppCompatButton delete;
        public AppCompatTextView hod_department;
        public AppCompatTextView hod_login_id;
        public AppCompatTextView hod_name;
        public AppCompatTextView hod_password;
        public AppCompatButton viewh;

        public ViewHolder(View view) {
            super(view);
            this.hod_name = (AppCompatTextView) view.findViewById(R.id.hod_name);
            this.hod_password = (AppCompatTextView) view.findViewById(R.id.hod_password);
            this.hod_login_id = (AppCompatTextView) view.findViewById(R.id.hod_login_id);
            this.hod_department = (AppCompatTextView) view.findViewById(R.id.hod_department);
            this.viewh = (AppCompatButton) view.findViewById(R.id.view);
            this.delete = (AppCompatButton) view.findViewById(R.id.delete);
        }
    }

    public HodAdapter(Context context2, List<Hod> list, Boolean bool, String str, String str2, String str3) {
        this.uploads = list;
        this.context = context2;
        this.admin_login = str;
        this.admin_password = str2;
        this.unactive = bool;
        this.loginas = str3;
    }

    public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        return new ViewHolder(LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.layout_unactive, viewGroup, false));
    }

    public void onBindViewHolder(ViewHolder viewHolder, int i) {
        final Hod hod = (Hod) this.uploads.get(i);
        String str = "Department: ";
        String str2 = "Name: ";
        if (!this.loginas.equals("principal")) {
            viewHolder.hod_login_id.setVisibility(View.GONE);
            viewHolder.hod_password.setVisibility(View.GONE);
            viewHolder.viewh.setVisibility(View.VISIBLE);
            viewHolder.delete.setVisibility(View.GONE);
            AppCompatTextView appCompatTextView = viewHolder.hod_name;
            StringBuilder sb = new StringBuilder();
            sb.append(str2);
            sb.append(hod.getName());
            appCompatTextView.setText(sb.toString());
            AppCompatTextView appCompatTextView2 = viewHolder.hod_department;
            StringBuilder sb2 = new StringBuilder();
            sb2.append(str);
            sb2.append(hod.getDepartment());
            appCompatTextView2.setText(sb2.toString());
        } else if (this.unactive.booleanValue()) {
            AppCompatTextView appCompatTextView3 = viewHolder.hod_name;
            StringBuilder sb3 = new StringBuilder();
            sb3.append(str2);
            sb3.append(hod.getName());
            appCompatTextView3.setText(sb3.toString());
            AppCompatTextView appCompatTextView4 = viewHolder.hod_password;
            StringBuilder sb4 = new StringBuilder();
            sb4.append("Temporary Password: ");
            sb4.append(hod.getPassword());
            appCompatTextView4.setText(sb4.toString());
            AppCompatTextView appCompatTextView5 = viewHolder.hod_login_id;
            StringBuilder sb5 = new StringBuilder();
            sb5.append("Login ID: ");
            sb5.append(hod.getLogin_id());
            appCompatTextView5.setText(sb5.toString());
            AppCompatTextView appCompatTextView6 = viewHolder.hod_department;
            StringBuilder sb6 = new StringBuilder();
            sb6.append(str);
            sb6.append(hod.getDepartment());
            appCompatTextView6.setText(sb6.toString());
        } else {
            viewHolder.hod_login_id.setVisibility(View.GONE);
            viewHolder.hod_password.setVisibility(View.GONE);
            AppCompatTextView appCompatTextView7 = viewHolder.hod_name;
            StringBuilder sb7 = new StringBuilder();
            sb7.append(str2);
            sb7.append(hod.getName());
            appCompatTextView7.setText(sb7.toString());
            AppCompatTextView appCompatTextView8 = viewHolder.hod_department;
            StringBuilder sb8 = new StringBuilder();
            sb8.append(str);
            sb8.append(hod.getDepartment());
            appCompatTextView8.setText(sb8.toString());
        }
        viewHolder.viewh.setOnClickListener(new OnClickListener() {
            public void onClick(View view) {
                String str = "id";
                HodAdapter.this.context.startActivity(new Intent(HodAdapter.this.context, ViewHodProfileActivity.class).putExtra("department", hod.getDepartment()).putExtra(str, hod.getId()).putExtra("loginas", "principal"));
            }
        });
        viewHolder.delete.setOnClickListener(new OnClickListener() {
            public void onClick(View view) {
                Builder builder = new Builder(HodAdapter.this.context);
                String str = "Confirm";
                builder.setTitle(str);
                StringBuilder sb = new StringBuilder();
                sb.append("Do you really want to delete H.O.D ");
                sb.append(hod.getName());
                builder.setMessage(sb.toString());
                builder.setPositiveButton(str, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialogInterface, int i) {
                        final ProgressDialog progressDialog = new ProgressDialog(HodAdapter.this.context);
                        progressDialog.setMessage("Deleting...");
                        progressDialog.setCancelable(false);
                        progressDialog.show();
                        FirebaseDatabase.getInstance().getReference("HODs").addListenerForSingleValueEvent(new ValueEventListener() {
                            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                for (DataSnapshot children : dataSnapshot.getChildren()) {
                                    for (DataSnapshot dataSnapshot2 : children.getChildren()) {
                                        if (dataSnapshot2.getChildrenCount() != 0) {
                                            Hod hod = (Hod) dataSnapshot2.getValue(Hod.class);
                                            if (!hod.getDepartment().equals(hod.getDepartment()) && hod.getId().equals(hod.getId())) {
                                                HodAdapter.this.isavailable = Boolean.valueOf(true);
                                            }
                                        }
                                    }
                                }
                                if (HodAdapter.this.isavailable.booleanValue()) {
                                    HodAdapter.this.isavailable = Boolean.valueOf(false);
                                    FirebaseDatabase.getInstance().getReference("HODs").child(hod.getDepartment()).child(hod.getId()).removeValue().addOnSuccessListener(new OnSuccessListener<Void>() {
                                        public void onSuccess(Void voidR) {
                                            HodAdapter.this.uploads.clear();
                                            FirebaseAuth.getInstance().signInWithEmailAndPassword(HodAdapter.this.admin_login, HodAdapter.this.admin_password);
                                            progressDialog.dismiss();
                                        }
                                    }).addOnFailureListener(new OnFailureListener() {
                                        public void onFailure(@NonNull Exception exc) {
                                            progressDialog.dismiss();
                                            FirebaseAuth.getInstance().signInWithEmailAndPassword(HodAdapter.this.admin_login, HodAdapter.this.admin_password);
                                            Toast.makeText(HodAdapter.this.context, "Technical error occured.", Toast.LENGTH_SHORT).show();
                                        }
                                    });
                                    return;
                                }
                                FirebaseAuth.getInstance().signInWithEmailAndPassword(hod.getEmail(), hod.getPassword()).addOnSuccessListener(new OnSuccessListener<AuthResult>() {
                                    public void onSuccess(AuthResult authResult) {
                                        FirebaseAuth.getInstance().getCurrentUser().delete().addOnSuccessListener(new OnSuccessListener<Void>() {
                                            public void onSuccess(Void voidR) {
                                                FirebaseAuth.getInstance().signInWithEmailAndPassword(HodAdapter.this.admin_login, HodAdapter.this.admin_password).addOnSuccessListener(new OnSuccessListener<AuthResult>() {
                                                    public void onSuccess(AuthResult authResult) {
                                                        FirebaseDatabase.getInstance().getReference("HODs").child(hod.getDepartment()).child(hod.getId()).removeValue().addOnSuccessListener(new OnSuccessListener<Void>() {
                                                            public void onSuccess(Void voidR) {
                                                                HodAdapter.this.uploads.clear();
                                                                progressDialog.dismiss();
                                                                FirebaseAuth.getInstance().signInWithEmailAndPassword(HodAdapter.this.admin_login, HodAdapter.this.admin_password);
                                                            }
                                                        }).addOnFailureListener(new OnFailureListener() {
                                                            public void onFailure(@NonNull Exception exc) {
                                                                FirebaseAuth.getInstance().signInWithEmailAndPassword(HodAdapter.this.admin_login, HodAdapter.this.admin_password);
                                                                Toast.makeText(HodAdapter.this.context, "Technical error occured.", Toast.LENGTH_SHORT).show();
                                                                progressDialog.dismiss();
                                                            }
                                                        });
                                                    }
                                                }).addOnFailureListener(new OnFailureListener() {
                                                    public void onFailure(@NonNull Exception exc) {
                                                        FirebaseAuth.getInstance().signInWithEmailAndPassword(HodAdapter.this.admin_login, HodAdapter.this.admin_password);
                                                        Toast.makeText(HodAdapter.this.context, "Technical error occured.", Toast.LENGTH_SHORT).show();
                                                        progressDialog.dismiss();
                                                    }
                                                });
                                            }
                                        }).addOnFailureListener(new OnFailureListener() {
                                            public void onFailure(@NonNull Exception exc) {
                                                FirebaseAuth.getInstance().signInWithEmailAndPassword(HodAdapter.this.admin_login, HodAdapter.this.admin_password);
                                                progressDialog.dismiss();
                                            }
                                        });
                                    }
                                }).addOnFailureListener(new OnFailureListener() {
                                    public void onFailure(@NonNull Exception exc) {
                                        FirebaseAuth.getInstance().signInWithEmailAndPassword(HodAdapter.this.admin_login, HodAdapter.this.admin_password);
                                        progressDialog.dismiss();
                                    }
                                });
                            }

                            public void onCancelled(@NonNull DatabaseError databaseError) {
                                FirebaseAuth.getInstance().signInWithEmailAndPassword(HodAdapter.this.admin_login, HodAdapter.this.admin_password);
                            }
                        });
                    }
                });
                builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialogInterface, int i) {
                    }
                });
                builder.show();
            }
        });
    }

    public int getItemCount() {
        return this.uploads.size();
    }
}
