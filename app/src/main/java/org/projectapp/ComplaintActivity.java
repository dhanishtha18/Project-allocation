package org.projectapp;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.text.format.DateFormat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;
import androidx.appcompat.widget.AppCompatEditText;
import androidx.appcompat.widget.AppCompatImageButton;
import androidx.appcompat.widget.AppCompatTextView;
import androidx.lifecycle.Lifecycle;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.github.clans.fab.FloatingActionButton;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import org.apache.poi.openxml4j.opc.ContentTypes;
import org.projectapp.APIs.APIService;
import org.projectapp.Adapter.ComplaintAdapter;
import org.projectapp.Model.Complaint;
import org.projectapp.Model.Hod;
import org.projectapp.Model.Staff;
import org.projectapp.Model.Student;
import org.projectapp.Notifications.Client;
import org.projectapp.Notifications.Data;
import org.projectapp.Notifications.MyResponse;
import org.projectapp.Notifications.Sender;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import uk.co.senab.photoview.PhotoView;

public class ComplaintActivity extends AppCompatActivity {
    private static final int PICK_IMAGE_REQUEST = 234;
    APIService apiService;
    AppCompatImageButton btn_doc;
    AppCompatImageButton btn_send;
    AppCompatTextView cancel;
    List<Complaint> complaints;
    View customview;
    String dbname;
    String department;
    FloatingActionButton fab;
    Uri filePath;
    FirebaseUser fuser;
    AppCompatButton img;
    PhotoView img_view;
    Intent intent;
    LinearLayout layout;
    LayoutInflater layoutInflater;
    String loginas;
    ComplaintAdapter messageAdapter;
    String name;
    AppCompatButton pdf;
    PopupWindow popupWindow;
    RecyclerView recyclerView;
    DatabaseReference reference;
    AppCompatEditText text_send;
    String type;
    String uploadId;
    AppCompatTextView username;

    /* access modifiers changed from: protected */
    public void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        setContentView(R.layout.activity_complaint);
        this.recyclerView = (RecyclerView) findViewById(R.id.recycler_view);
        this.recyclerView.setHasFixedSize(true);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        linearLayoutManager.setStackFromEnd(true);
        this.recyclerView.setLayoutManager(linearLayoutManager);
        this.username = (AppCompatTextView) findViewById(R.id.title);
        this.btn_send = (AppCompatImageButton) findViewById(R.id.btn_send);
        this.text_send = (AppCompatEditText) findViewById(R.id.text_send);
        this.img_view = (PhotoView) findViewById(R.id.img_preview);
        this.btn_doc = (AppCompatImageButton) findViewById(R.id.btn_doc);
        this.fab = (FloatingActionButton) findViewById(R.id.fab);
        this.apiService = (APIService) Client.getClient("https://fcm.googleapis.com/").create(APIService.class);
        this.layout = new LinearLayout(this);
        this.layout.setOrientation(LinearLayout.VERTICAL);
        this.layout.setGravity(17);
        this.layoutInflater = (LayoutInflater) getSystemService(LAYOUT_INFLATER_SERVICE);
        this.customview = this.layoutInflater.inflate(R.layout.img_doc_dialog, (ViewGroup) null);
        this.img = (AppCompatButton) this.customview.findViewById(R.id.img);
        this.pdf = (AppCompatButton) this.customview.findViewById(R.id.pdf);
        this.cancel = (AppCompatTextView) this.customview.findViewById(R.id.cancel);
        this.popupWindow = new PopupWindow(this.customview, -1, -1);
        this.recyclerView.setVisibility(View.VISIBLE);
        this.img_view.setVisibility(View.GONE);
        this.intent = getIntent();
        this.dbname = this.intent.getStringExtra("path");
        this.department = this.intent.getStringExtra("department");
        this.name = this.intent.getStringExtra("cname");
        this.loginas = this.intent.getStringExtra("loginas");
        this.fuser = FirebaseAuth.getInstance().getCurrentUser();
        this.username.setText(getIntent().getStringExtra("name"));
        this.btn_send.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                ComplaintActivity.this.recyclerView.setVisibility(View.VISIBLE);
                ComplaintActivity.this.img_view.setVisibility(View.GONE);
                String obj = ComplaintActivity.this.text_send.getText().toString();
                if (!obj.equals("")) {
                    ComplaintActivity complaintActivity = ComplaintActivity.this;
                    complaintActivity.sendMessage(complaintActivity.fuser.getUid(), obj, "message");
                } else {
                    Toast.makeText(ComplaintActivity.this, "You Can't send empty message.", Toast.LENGTH_SHORT).show();
                }
                ComplaintActivity.this.text_send.setText("");
            }
        });
        this.btn_doc.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                ComplaintActivity.this.showOptions();
            }
        });
        readMessage(this.fuser.getUid());
        this.fab.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                AlertDialog.Builder builder = new AlertDialog.Builder(ComplaintActivity.this);
                builder.setTitle((CharSequence) "Raise Complaint");
                View inflate = View.inflate(ComplaintActivity.this, R.layout.layout_update_password, (ViewGroup) null);
                final AppCompatEditText appCompatEditText = (AppCompatEditText) inflate.findViewById(R.id.password);
                if (inflate.getParent() != null) {
                    ((ViewGroup) inflate.getParent()).removeView(inflate);
                }
                builder.setView(inflate);
                appCompatEditText.setHint("Enter Complaint");
                appCompatEditText.setBackgroundResource(R.drawable.edittext);
                builder.setPositiveButton((CharSequence) "Raise", (DialogInterface.OnClickListener) new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialogInterface, int i) {
                        ComplaintActivity.this.sendMessage(ComplaintActivity.this.fuser.getUid(), appCompatEditText.getText().toString(), "complaint");
                    }
                });
                builder.setNegativeButton((CharSequence) "Cancel", (DialogInterface.OnClickListener) new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialogInterface, int i) {
                    }
                });
                builder.create();
                if (ComplaintActivity.this.getLifecycle().getCurrentState().isAtLeast(Lifecycle.State.CREATED)) {
                    builder.show();
                }
            }
        });
    }

    /* access modifiers changed from: private */
    public void sendMessage(String str, String str2, String str3) {
        final String str4;
        ProgressDialog progressDialog;
        String str5 = str2;
        final ProgressDialog progressDialog2 = new ProgressDialog(this);
        progressDialog2.setCancelable(false);
        progressDialog2.setMessage("Sending...");
        progressDialog2.show();
        final DatabaseReference reference2 = FirebaseDatabase.getInstance().getReference(this.dbname);
        this.uploadId = reference2.push().getKey();
        if (this.filePath != null) {
            final StorageReference child = FirebaseStorage.getInstance().getReference(this.dbname).child(this.uploadId);
            final String str6 = str;
            final String str7 = str2;
            final String str8 = str3;
            final ProgressDialog progressDialog3 = progressDialog2;
            child.putFile(this.filePath).addOnSuccessListener((OnSuccessListener) new OnSuccessListener<UploadTask.TaskSnapshot>() {
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                    child.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                        public void onSuccess(Uri uri) {
                            if (ComplaintActivity.this.loginas.equals("hod")) {
                                reference2.child(ComplaintActivity.this.uploadId).setValue(new Complaint(str6, str7, DateFormat.format("dd-MM-yyyy (HH:mm:ss)", System.currentTimeMillis()).toString(), uri.toString(), ComplaintActivity.this.type, ComplaintActivity.this.uploadId, str8, ComplaintActivity.this.name));
                            } else {
                                reference2.child(ComplaintActivity.this.uploadId).setValue(new Complaint(str6, str7, DateFormat.format("dd-MM-yyyy (HH:mm:ss)", System.currentTimeMillis()).toString(), uri.toString(), ComplaintActivity.this.type, ComplaintActivity.this.uploadId, str8, ""));
                            }
                            ComplaintActivity.this.filePath = null;
                            progressDialog3.dismiss();
                        }
                    });
                }
            }).addOnFailureListener((OnFailureListener) new OnFailureListener() {
                public void onFailure(Exception exc) {
                    progressDialog2.dismiss();
                    Toast.makeText(ComplaintActivity.this, exc.getMessage(), Toast.LENGTH_SHORT).show();
                }
            });
            str4 = str5;
        } else {
            if (this.loginas.equals("hod")) {
                progressDialog = progressDialog2;
                str4 = str5;
                reference2.child(this.uploadId).setValue(new Complaint(str, str2, DateFormat.format("dd-MM-yyyy (HH:mm:ss)", System.currentTimeMillis()).toString(), (String) null, (String) null, this.uploadId, str3, this.name));
            } else {
                progressDialog = progressDialog2;
                str4 = str5;
                reference2.child(this.uploadId).setValue(new Complaint(str, str2, DateFormat.format("dd-MM-yyyy (HH:mm:ss)", System.currentTimeMillis()).toString(), (String) null, (String) null, this.uploadId, str3, ""));
            }
            progressDialog.dismiss();
        }
        String str9 = this.dbname;
        if (str9.equals(this.department + "/Student Complaints")) {
            FirebaseDatabase.getInstance().getReference("HODs").addListenerForSingleValueEvent(new ValueEventListener() {
                public void onCancelled(@NonNull DatabaseError databaseError) {
                }

                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    for (DataSnapshot children : dataSnapshot.getChildren()) {
                        for (DataSnapshot next : children.getChildren()) {
                            if (next.getChildrenCount() != 0) {
                                Hod hod = (Hod) next.getValue(Hod.class);
                                if (!hod.getId().equals(FirebaseAuth.getInstance().getCurrentUser().getUid())) {
                                    ComplaintActivity.this.sendNotification(hod.getToken(), hod.getId(), "Complaint", str4);
                                }
                            }
                        }
                    }
                }
            });
            FirebaseDatabase.getInstance().getReference("Students").child(this.department).addListenerForSingleValueEvent(new ValueEventListener() {
                public void onCancelled(@NonNull DatabaseError databaseError) {
                }

                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    for (DataSnapshot children : dataSnapshot.getChildren()) {
                        for (DataSnapshot next : children.getChildren()) {
                            if (next.getChildrenCount() != 0) {
                                Student student = (Student) next.getValue(Student.class);
                                if (!student.getId().equals(FirebaseAuth.getInstance().getCurrentUser().getUid())) {
                                    ComplaintActivity.this.sendNotification(student.getToken(), student.getId(), "Complaint", str4);
                                }
                            }
                        }
                    }
                }
            });
            return;
        }
        FirebaseDatabase.getInstance().getReference("Staff").child(this.department).addListenerForSingleValueEvent(new ValueEventListener() {
            public void onCancelled(@NonNull DatabaseError databaseError) {
            }

            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot next : dataSnapshot.getChildren()) {
                    if (next.getChildrenCount() != 0) {
                        Staff staff = (Staff) next.getValue(Staff.class);
                        if (!staff.getId().equals(FirebaseAuth.getInstance().getCurrentUser().getUid())) {
                            ComplaintActivity.this.sendNotification(staff.getToken(), staff.getId(), "Complaint", str4);
                        }
                    }
                }
            }
        });
        FirebaseDatabase.getInstance().getReference("HODs").child(this.department).addListenerForSingleValueEvent(new ValueEventListener() {
            public void onCancelled(@NonNull DatabaseError databaseError) {
            }

            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot next : dataSnapshot.getChildren()) {
                    if (next.getChildrenCount() != 0) {
                        Hod hod = (Hod) next.getValue(Hod.class);
                        if (!hod.getId().equals(FirebaseAuth.getInstance().getCurrentUser().getUid())) {
                            ComplaintActivity.this.sendNotification(hod.getToken(), hod.getId(), "Complaint", str4);
                        }
                    }
                }
            }
        });
    }

    /* access modifiers changed from: private */
    public void sendNotification(String str, String str2, String str3, String str4) {
        this.apiService.sendNotification(new Sender(new Data(FirebaseAuth.getInstance().getCurrentUser().getUid(), str3 + ":" + str4, "New " + getIntent().getStringExtra("noti"), str2), str)).enqueue(new Callback<MyResponse>() {
            public void onFailure(Call<MyResponse> call, Throwable th) {
            }

            public void onResponse(Call<MyResponse> call, Response<MyResponse> response) {
                if (response.code() == 200) {
                    int i = response.body().success;
                }
            }
        });
    }

    private void readMessage(String str) {
        this.complaints = new ArrayList();
        this.reference = FirebaseDatabase.getInstance().getReference(this.dbname);
        this.reference.addValueEventListener(new ValueEventListener() {
            public void onCancelled(@NonNull DatabaseError databaseError) {
            }

            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                ComplaintActivity.this.complaints.clear();
                for (DataSnapshot value : dataSnapshot.getChildren()) {
                    ComplaintActivity.this.complaints.add((Complaint) value.getValue(Complaint.class));
                }
                if (ComplaintActivity.this.getLifecycle().getCurrentState().isAtLeast(Lifecycle.State.INITIALIZED)) {
                    ComplaintActivity complaintActivity = ComplaintActivity.this;
                    complaintActivity.messageAdapter = new ComplaintAdapter(complaintActivity, complaintActivity.complaints);
                    ComplaintActivity.this.recyclerView.setAdapter(ComplaintActivity.this.messageAdapter);
                }
            }
        });
    }

    /* access modifiers changed from: private */
    public void showOptions() {
        this.popupWindow.showAtLocation(this.layout, 17, 0, 0);
        this.customview.findViewById(R.id.dialog).setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                ComplaintActivity.this.popupWindow.dismiss();
            }
        });
        this.pdf.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                ComplaintActivity complaintActivity = ComplaintActivity.this;
                complaintActivity.type = "pdf";
                complaintActivity.showPdfChooser();
                ComplaintActivity.this.popupWindow.dismiss();
            }
        });
        this.img.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                ComplaintActivity complaintActivity = ComplaintActivity.this;
                complaintActivity.type = ContentTypes.EXTENSION_JPG_1;
                complaintActivity.showImageChooser();
                ComplaintActivity.this.popupWindow.dismiss();
            }
        });
        this.cancel.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                ComplaintActivity.this.popupWindow.dismiss();
            }
        });
    }

    /* access modifiers changed from: private */
    public void showImageChooser() {
        Intent intent2 = new Intent();
        intent2.setType("image/*");
        intent2.setAction("android.intent.action.GET_CONTENT");
        startActivityForResult(Intent.createChooser(intent2, "Select Picture"), PICK_IMAGE_REQUEST);
    }

    /* access modifiers changed from: private */
    public void showPdfChooser() {
        Intent intent2 = new Intent();
        intent2.setType("application/pdf");
        intent2.setAction("android.intent.action.GET_CONTENT");
        startActivityForResult(Intent.createChooser(intent2, "Select Document"), PICK_IMAGE_REQUEST);
    }

    /* access modifiers changed from: protected */
    public void onActivityResult(int i, int i2, Intent intent2) {
        super.onActivityResult(i, i2, intent2);
        if (i == PICK_IMAGE_REQUEST && i2 == -1 && intent2 != null && intent2.getData() != null) {
            this.filePath = intent2.getData();
            if (this.type.equals(ContentTypes.EXTENSION_JPG_1)) {
                this.recyclerView.setVisibility(View.GONE);
                this.img_view.setVisibility(View.VISIBLE);
                try {
                    this.img_view.setImageBitmap(MediaStore.Images.Media.getBitmap(getContentResolver(), this.filePath));
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            if (this.type.equals("pdf")) {
                this.recyclerView.setVisibility(View.GONE);
                this.img_view.setVisibility(View.VISIBLE);
                this.img_view.setImageResource(R.drawable.preview);
            }
        }
    }

    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }
}
