package org.projectapp;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore.Images.Media;
import android.text.format.DateFormat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;
import androidx.appcompat.widget.AppCompatEditText;
import androidx.appcompat.widget.AppCompatTextView;
import androidx.core.view.ViewCompat;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask.TaskSnapshot;
import de.hdodenhof.circleimageview.CircleImageView;
import java.io.IOException;
import org.apache.poi.openxml4j.opc.ContentTypes;
import org.projectapp.APIs.APIService;
import org.projectapp.Model.Hod;
import org.projectapp.Model.Notice;
import org.projectapp.Model.Principal;
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

public class UploadNoticeActivity extends AppCompatActivity {
    private static final int PICK_IMAGE_REQUEST = 234;
    APIService apiService;
    AppCompatTextView cancel;
    View customview;
    String department;
    Uri filePath;
    CircleImageView icon;
    PhotoView image_notice;
    AppCompatButton img;
    LinearLayout layout;
    LayoutInflater layoutInflater;
    DatabaseReference mDatabase;
    String name = null;
    AppCompatEditText notice_title;
    AppCompatButton pdf;
    PopupWindow popupWindow;
    AppCompatButton select_file;
    StorageReference storageReference;
    AppCompatTextView title;
    String type = null;
    String uploadId;
    AppCompatButton upload_notice_btn;

    /* access modifiers changed from: protected */
    @SuppressLint("WrongConstant")
    public void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        setContentView((int) R.layout.activity_upload_notice);
        this.title = (AppCompatTextView) findViewById(R.id.title);
        this.icon = (CircleImageView) findViewById(R.id.icon);
        this.notice_title = (AppCompatEditText) findViewById(R.id.notice_title);
        this.image_notice = (PhotoView) findViewById(R.id.image_notice);
        this.select_file = (AppCompatButton) findViewById(R.id.select_file);
        this.upload_notice_btn = (AppCompatButton) findViewById(R.id.upload_notice_btn);
        this.apiService = (APIService) Client.getClient("https://fcm.googleapis.com/").create(APIService.class);
        this.name = getIntent().getStringExtra("name");
        this.department = getIntent().getStringExtra("department");
        this.title.setText(getIntent().getStringExtra("notice"));
        String str = "path";
        this.storageReference = FirebaseStorage.getInstance().getReference(getIntent().getStringExtra(str));
        this.mDatabase = FirebaseDatabase.getInstance().getReference(getIntent().getStringExtra(str));
        this.layout = new LinearLayout(this);
        this.layout.setOrientation(0);
        this.layout.setGravity(17);
        this.layout.setBackgroundColor(ViewCompat.MEASURED_STATE_MASK);
        this.layoutInflater = (LayoutInflater) getSystemService("layout_inflater");
        this.customview = this.layoutInflater.inflate(R.layout.img_doc_dialog, null);
        this.img = (AppCompatButton) this.customview.findViewById(R.id.img);
        this.pdf = (AppCompatButton) this.customview.findViewById(R.id.pdf);
        this.cancel = (AppCompatTextView) this.customview.findViewById(R.id.cancel);
        this.popupWindow = new PopupWindow(this.customview, -1, -1);
        this.select_file.setOnClickListener(new OnClickListener() {
            public void onClick(View view) {
                UploadNoticeActivity.this.showOptions();
            }
        });
        this.upload_notice_btn.setOnClickListener(new OnClickListener() {
            public void onClick(View view) {
                if (UploadNoticeActivity.this.notice_title.getText().toString().equals("")) {
                    Toast.makeText(UploadNoticeActivity.this, "Enter Notice Title", Toast.LENGTH_SHORT).show();
                } else {
                    UploadNoticeActivity.this.uploadFile();
                }
            }
        });
    }

    /* access modifiers changed from: private */
    public void uploadFile() {
        if (this.filePath != null) {
            final ProgressDialog progressDialog = new ProgressDialog(this);
            progressDialog.setCancelable(false);
            progressDialog.setMessage("Uploading...");
            progressDialog.show();
            this.uploadId = this.mDatabase.push().getKey();
            StorageReference storageReference2 = this.storageReference;
            StringBuilder sb = new StringBuilder();
            sb.append(this.uploadId);
            sb.append(".");
            sb.append(this.type);
            final StorageReference child = storageReference2.child(sb.toString());
            child.putFile(this.filePath).addOnSuccessListener((OnSuccessListener) new OnSuccessListener<TaskSnapshot>() {
                public void onSuccess(TaskSnapshot taskSnapshot) {
                    child.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                        public void onSuccess(Uri uri) {
                            Notice notice = new Notice(UploadNoticeActivity.this.notice_title.getText().toString().trim(), uri.toString(), UploadNoticeActivity.this.name, DateFormat.format("dd-MM-yyyy (HH:mm:ss)", System.currentTimeMillis()).toString(), UploadNoticeActivity.this.type, UploadNoticeActivity.this.uploadId);
                            UploadNoticeActivity.this.mDatabase.child(UploadNoticeActivity.this.uploadId).setValue(notice);
                            UploadNoticeActivity.this.image_notice.setImageResource(0);
                            UploadNoticeActivity.this.notice_title.setText("");
                            UploadNoticeActivity.this.filePath = null;
                        }
                    });
                    progressDialog.dismiss();
                    Toast.makeText(UploadNoticeActivity.this, "Notice Uploaded ", Toast.LENGTH_SHORT).show();
                }
            }).addOnFailureListener((OnFailureListener) new OnFailureListener() {
                public void onFailure(Exception exc) {
                    progressDialog.dismiss();
                    Toast.makeText(UploadNoticeActivity.this, exc.getMessage(), Toast.LENGTH_SHORT).show();
                }
            });
            String str = "Students";
            String str2 = "HODs";
            String str3 = "Staff";
            if (this.department.equals("")) {
                final String trim = this.notice_title.getText().toString().trim();
                FirebaseDatabase.getInstance().getReference(str3).addListenerForSingleValueEvent(new ValueEventListener() {
                    public void onCancelled(@NonNull DatabaseError databaseError) {
                    }

                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        for (DataSnapshot children : dataSnapshot.getChildren()) {
                            for (DataSnapshot value : children.getChildren()) {
                                Staff staff = (Staff) value.getValue(Staff.class);
                                if (!staff.getId().equals(FirebaseAuth.getInstance().getCurrentUser().getUid())) {
                                    UploadNoticeActivity.this.sendNotification(staff.getToken(), staff.getId(), UploadNoticeActivity.this.name, trim);
                                }
                            }
                        }
                    }
                });
                FirebaseDatabase.getInstance().getReference(str2).addListenerForSingleValueEvent(new ValueEventListener() {
                    public void onCancelled(@NonNull DatabaseError databaseError) {
                    }

                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        for (DataSnapshot children : dataSnapshot.getChildren()) {
                            for (DataSnapshot value : children.getChildren()) {
                                Hod hod = (Hod) value.getValue(Hod.class);
                                if (!hod.getId().equals(FirebaseAuth.getInstance().getCurrentUser().getUid())) {
                                    UploadNoticeActivity.this.sendNotification(hod.getToken(), hod.getId(), UploadNoticeActivity.this.name, trim);
                                }
                            }
                        }
                    }
                });
                FirebaseDatabase.getInstance().getReference(str).child(this.department).addListenerForSingleValueEvent(new ValueEventListener() {
                    public void onCancelled(@NonNull DatabaseError databaseError) {
                    }

                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        for (DataSnapshot children : dataSnapshot.getChildren()) {
                            for (DataSnapshot children2 : children.getChildren()) {
                                for (DataSnapshot value : children2.getChildren()) {
                                    Student student = (Student) value.getValue(Student.class);
                                    if (!student.getId().equals(FirebaseAuth.getInstance().getCurrentUser().getUid())) {
                                        UploadNoticeActivity.this.sendNotification(student.getToken(), student.getId(), UploadNoticeActivity.this.name, trim);
                                    }
                                }
                            }
                        }
                    }
                });
                FirebaseDatabase.getInstance().getReference("Principal").addListenerForSingleValueEvent(new ValueEventListener() {
                    public void onCancelled(@NonNull DatabaseError databaseError) {
                    }

                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        for (DataSnapshot value : dataSnapshot.getChildren()) {
                            Principal principal = (Principal) value.getValue(Principal.class);
                            if (!principal.getId().equals(FirebaseAuth.getInstance().getCurrentUser().getUid())) {
                                UploadNoticeActivity.this.sendNotification(principal.getToken(), principal.getId(), UploadNoticeActivity.this.name, trim);
                            }
                        }
                    }
                });
                return;
            }
            final String trim2 = this.notice_title.getText().toString().trim();
            FirebaseDatabase.getInstance().getReference(str3).child(this.department).addListenerForSingleValueEvent(new ValueEventListener() {
                public void onCancelled(@NonNull DatabaseError databaseError) {
                }

                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    for (DataSnapshot value : dataSnapshot.getChildren()) {
                        Staff staff = (Staff) value.getValue(Staff.class);
                        if (!staff.getId().equals(FirebaseAuth.getInstance().getCurrentUser().getUid())) {
                            UploadNoticeActivity.this.sendNotification(staff.getToken(), staff.getId(), UploadNoticeActivity.this.name, trim2);
                        }
                    }
                }
            });
            FirebaseDatabase.getInstance().getReference(str2).child(this.department).addListenerForSingleValueEvent(new ValueEventListener() {
                public void onCancelled(@NonNull DatabaseError databaseError) {
                }

                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    for (DataSnapshot value : dataSnapshot.getChildren()) {
                        Hod hod = (Hod) value.getValue(Hod.class);
                        if (!hod.getId().equals(FirebaseAuth.getInstance().getCurrentUser().getUid())) {
                            UploadNoticeActivity.this.sendNotification(hod.getToken(), hod.getId(), UploadNoticeActivity.this.name, trim2);
                        }
                    }
                }
            });
            FirebaseDatabase.getInstance().getReference(str).child(this.department).addListenerForSingleValueEvent(new ValueEventListener() {
                public void onCancelled(@NonNull DatabaseError databaseError) {
                }

                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    for (DataSnapshot children : dataSnapshot.getChildren()) {
                        for (DataSnapshot value : children.getChildren()) {
                            Student student = (Student) value.getValue(Student.class);
                            if (!student.getId().equals(FirebaseAuth.getInstance().getCurrentUser().getUid())) {
                                UploadNoticeActivity.this.sendNotification(student.getToken(), student.getId(), UploadNoticeActivity.this.name, trim2);
                            }
                        }
                    }
                }
            });
            return;
        }
        Toast.makeText(this, "No File Selected.", Toast.LENGTH_SHORT).show();
    }

    /* access modifiers changed from: private */
    public void sendNotification(String str, String str2, String str3, String str4) {
        String uid = FirebaseAuth.getInstance().getCurrentUser().getUid();
        StringBuilder sb = new StringBuilder();
        sb.append("New ");
        sb.append(getIntent().getStringExtra("notice"));
        this.apiService.sendNotification(new Sender(new Data(uid, str4, sb.toString(), str2), str)).enqueue(new Callback<MyResponse>() {
            public void onFailure(Call<MyResponse> call, Throwable th) {
            }

            public void onResponse(Call<MyResponse> call, Response<MyResponse> response) {
                if (response.code() == 200) {
                    int i = ((MyResponse) response.body()).success;
                }
            }
        });
    }

    /* access modifiers changed from: private */
    public void showOptions() {
        this.popupWindow.showAtLocation(this.layout, 17, 0, 0);
        this.customview.findViewById(R.id.dialog).setOnClickListener(new OnClickListener() {
            public void onClick(View view) {
                UploadNoticeActivity.this.popupWindow.dismiss();
            }
        });
        this.pdf.setOnClickListener(new OnClickListener() {
            public void onClick(View view) {
                UploadNoticeActivity uploadNoticeActivity = UploadNoticeActivity.this;
                uploadNoticeActivity.type = "pdf";
                uploadNoticeActivity.showPdfChooser();
                UploadNoticeActivity.this.popupWindow.dismiss();
            }
        });
        this.img.setOnClickListener(new OnClickListener() {
            public void onClick(View view) {
                UploadNoticeActivity uploadNoticeActivity = UploadNoticeActivity.this;
                uploadNoticeActivity.type = ContentTypes.EXTENSION_JPG_1;
                uploadNoticeActivity.showImageChooser();
                UploadNoticeActivity.this.popupWindow.dismiss();
            }
        });
        this.cancel.setOnClickListener(new OnClickListener() {
            public void onClick(View view) {
                UploadNoticeActivity.this.popupWindow.dismiss();
            }
        });
    }

    /* access modifiers changed from: private */
    public void showImageChooser() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction("android.intent.action.GET_CONTENT");
        startActivityForResult(Intent.createChooser(intent, "Select Picture"), PICK_IMAGE_REQUEST);
    }

    /* access modifiers changed from: private */
    public void showPdfChooser() {
        Intent intent = new Intent();
        intent.setType("application/pdf");
        intent.setAction("android.intent.action.GET_CONTENT");
        startActivityForResult(Intent.createChooser(intent, "Select Document"), PICK_IMAGE_REQUEST);
    }

    /* access modifiers changed from: protected */
    public void onActivityResult(int i, int i2, Intent intent) {
        super.onActivityResult(i, i2, intent);
        if (i == PICK_IMAGE_REQUEST && i2 == -1 && intent != null && intent.getData() != null) {
            this.filePath = intent.getData();
            if (this.type.equals(ContentTypes.EXTENSION_JPG_1)) {
                try {
                    this.image_notice.setImageBitmap(Media.getBitmap(getContentResolver(), this.filePath));
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            if (this.type.equals("pdf")) {
                this.image_notice.setImageResource(R.drawable.preview);
            }
        }
    }

    public void onBackPressed() {
        if (this.popupWindow.isShowing()) {
            this.popupWindow.dismiss();
            return;
        }
        super.onBackPressed();
        finish();
    }
}
