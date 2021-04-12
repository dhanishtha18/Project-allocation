package org.projectapp;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore.Images.Media;
import android.text.format.DateFormat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.webkit.URLUtil;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.SpinnerAdapter;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog.Builder;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;
import androidx.appcompat.widget.AppCompatEditText;
import androidx.appcompat.widget.AppCompatSpinner;
import androidx.appcompat.widget.AppCompatTextView;
import androidx.core.view.ViewCompat;
import androidx.lifecycle.Lifecycle.State;
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
import java.io.IOException;
import java.util.ArrayList;
import java.util.Calendar;
import org.apache.poi.openxml4j.opc.ContentTypes;
import org.projectapp.APIs.APIService;
import org.projectapp.Model.Hod;
import org.projectapp.Model.Staff;
import org.projectapp.Model.Student;
import org.projectapp.Model.StudyResources;
import org.projectapp.Model.Subject;
import org.projectapp.Notifications.Client;
import org.projectapp.Notifications.Data;
import org.projectapp.Notifications.MyResponse;
import org.projectapp.Notifications.Sender;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import uk.co.senab.photoview.PhotoView;

public class UploadStudyResourceActivity extends AppCompatActivity {
    static final int PICK_IMAGE_REQUEST = 234;
    APIService apiService;
    View customview;
    String department;
    Uri filePath;
    String id;
    AppCompatButton img;
    LinearLayout layout;
    LayoutInflater layoutInflater;
    DatabaseReference mDatabase;
    String name;
    AppCompatButton pdf;
    PopupWindow popupWindow;
    AppCompatEditText resource_description;
    PhotoView resource_image;
    AppCompatEditText resource_link;
    AppCompatSpinner resource_semester;
    AppCompatSpinner resource_subject;
    AppCompatButton select_file;
    String[] semArray;
    String semester;
    StorageReference storageReference;
    String subject;
    ArrayList<String> subjects;
    AppCompatTextView title;
    String type;
    String uploadId;
    AppCompatButton upload_resource_btn;
    AppCompatButton url;

    /* access modifiers changed from: protected */
    public void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        setContentView((int) R.layout.activity_upload_study_resource);
        this.department = getIntent().getStringExtra("department");
        this.name = getIntent().getStringExtra("name");
        this.id = getIntent().getStringExtra("id");
        this.resource_semester = (AppCompatSpinner) findViewById(R.id.resource_semester);
        this.resource_subject = (AppCompatSpinner) findViewById(R.id.resource_subject);
        this.resource_description = (AppCompatEditText) findViewById(R.id.resource_description);
        this.resource_image = (PhotoView) findViewById(R.id.resource_image);
        this.select_file = (AppCompatButton) findViewById(R.id.select_file);
        this.upload_resource_btn = (AppCompatButton) findViewById(R.id.upload_resource_btn);
        this.resource_link = (AppCompatEditText) findViewById(R.id.resource_link);
        this.title = (AppCompatTextView) findViewById(R.id.title);
        this.subjects = new ArrayList<>();
        this.title.setText("Upload Study Resources");
        this.apiService = (APIService) Client.getClient("https://fcm.googleapis.com/").create(APIService.class);
        this.layout = new LinearLayout(this);
        this.layout.setOrientation(0);
        this.layout.setGravity(17);
        this.layout.setBackgroundColor(ViewCompat.MEASURED_STATE_MASK);
        this.layoutInflater = (LayoutInflater) getSystemService("layout_inflater");
        this.customview = this.layoutInflater.inflate(R.layout.dialog_upload_study_resource, null);
        this.img = (AppCompatButton) this.customview.findViewById(R.id.img);
        this.pdf = (AppCompatButton) this.customview.findViewById(R.id.pdf);
        this.url = (AppCompatButton) this.customview.findViewById(R.id.url);
        this.popupWindow = new PopupWindow(this.customview, -1, -1);
        Calendar instance = Calendar.getInstance();
        String str = "Select Semester";
        if (this.department.equals("Computer Department")) {
            if (instance.get(2) < 5 || instance.get(2) == 11) {
                this.semArray = new String[]{str, "CO2I", "CO4I", "CO6I"};
            } else {
                this.semArray = new String[]{str, "CO1I", "CO3I", "CO5I"};
            }
        } else if (!this.department.equals("IT Department")) {
            String str2 = "CE5I";
            String str3 = "CE3I";
            String str4 = "CE1I";
            String str5 = "CE6I";
            String str6 = "CE4I";
            String str7 = "CE2I";
            if (this.department.equals("Civil I Shift Department")) {
                if (instance.get(2) < 5 || instance.get(2) == 11) {
                    this.semArray = new String[]{str, str7, str6, str5};
                } else {
                    this.semArray = new String[]{str, str4, str3, str2};
                }
            } else if (!this.department.equals("Civil II Shift Department")) {
                String str8 = "ME1I";
                String str9 = "ME6I";
                String str10 = "ME4I";
                String str11 = "ME2I";
                if (this.department.equals("Mechanical I Shift Department")) {
                    if (instance.get(2) < 5 || instance.get(2) == 11) {
                        this.semArray = new String[]{str, str11, str10, str9};
                    } else {
                        this.semArray = new String[]{str, str8, "ME3I", "ME5I"};
                    }
                } else if (this.department.equals("Mechanical II Shift Department")) {
                    if (instance.get(2) < 5 || instance.get(2) == 11) {
                        this.semArray = new String[]{str, str11, str10, str9};
                    } else {
                        this.semArray = new String[]{str, str8, "ME3I", "ME5I"};
                    }
                } else if (this.department.equals("Chemical Department")) {
                    if (instance.get(2) < 5 || instance.get(2) == 11) {
                        this.semArray = new String[]{str, "CH2I", "CH4I", "CH6I"};
                    } else {
                        this.semArray = new String[]{str, "CH1I", "CH3I", "CH5I"};
                    }
                } else if (this.department.equals("TR Department")) {
                    if (instance.get(2) < 5 || instance.get(2) == 11) {
                        this.semArray = new String[]{str, "TR2G", "TR4G", "TR5G"};
                    } else {
                        this.semArray = new String[]{str, "TR1G", "TR3G", "TR5G"};
                    }
                }
            } else if (instance.get(2) < 5 || instance.get(2) == 11) {
                this.semArray = new String[]{str, str7, str6, str5};
            } else {
                this.semArray = new String[]{str, str4, str3, str2};
            }
        } else if (instance.get(2) < 5 || instance.get(2) == 11) {
            this.semArray = new String[]{str, "IF2I", "IF4I", "IF6I"};
        } else {
            this.semArray = new String[]{str, "IF1I", "IF3I", "IF5I"};
        }
        this.resource_semester.setAdapter((SpinnerAdapter) new ArrayAdapter(this, R.layout.support_simple_spinner_dropdown_item, this.semArray));
        this.resource_semester.setOnItemSelectedListener(new OnItemSelectedListener() {
            public void onNothingSelected(AdapterView<?> adapterView) {
            }

            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long j) {
                if (adapterView.getItemAtPosition(i).toString().equals("Select Semester")) {
                    UploadStudyResourceActivity.this.semester = null;
                    return;
                }
                UploadStudyResourceActivity.this.semester = adapterView.getItemAtPosition(i).toString().trim();
                FirebaseDatabase instance = FirebaseDatabase.getInstance();
                StringBuilder sb = new StringBuilder();
                sb.append(UploadStudyResourceActivity.this.department);
                sb.append("/subjects/");
                sb.append(UploadStudyResourceActivity.this.semester);
                instance.getReference(sb.toString()).addListenerForSingleValueEvent(new ValueEventListener() {
                    public void onCancelled(@NonNull DatabaseError databaseError) {
                    }

                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        UploadStudyResourceActivity.this.subjects.clear();
                        if (dataSnapshot.getChildrenCount() == 0) {
                            UploadStudyResourceActivity.this.resource_subject.setAdapter((SpinnerAdapter) new ArrayAdapter(UploadStudyResourceActivity.this, R.layout.spinner_dialog, UploadStudyResourceActivity.this.subjects));
                            return;
                        }
                        for (DataSnapshot value : dataSnapshot.getChildren()) {
                            UploadStudyResourceActivity.this.subjects.add(((Subject) value.getValue(Subject.class)).getName());
                        }
                        UploadStudyResourceActivity.this.resource_subject.setAdapter((SpinnerAdapter) new ArrayAdapter(UploadStudyResourceActivity.this, R.layout.spinner_dialog, UploadStudyResourceActivity.this.subjects));
                    }
                });
            }
        });
        this.resource_subject.setOnItemSelectedListener(new OnItemSelectedListener() {
            public void onNothingSelected(AdapterView<?> adapterView) {
            }

            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long j) {
                UploadStudyResourceActivity.this.select_file.setVisibility(View.VISIBLE);
                UploadStudyResourceActivity.this.upload_resource_btn.setVisibility(View.VISIBLE);
                UploadStudyResourceActivity.this.subject = adapterView.getItemAtPosition(i).toString();
                UploadStudyResourceActivity uploadStudyResourceActivity = UploadStudyResourceActivity.this;
                FirebaseStorage instance = FirebaseStorage.getInstance();
                StringBuilder sb = new StringBuilder();
                sb.append(UploadStudyResourceActivity.this.department);
                String str = "/Notes/";
                sb.append(str);
                sb.append(UploadStudyResourceActivity.this.semester);
                String str2 = "/";
                sb.append(str2);
                sb.append(UploadStudyResourceActivity.this.subject);
                uploadStudyResourceActivity.storageReference = instance.getReference(sb.toString());
                UploadStudyResourceActivity uploadStudyResourceActivity2 = UploadStudyResourceActivity.this;
                FirebaseDatabase instance2 = FirebaseDatabase.getInstance();
                StringBuilder sb2 = new StringBuilder();
                sb2.append(UploadStudyResourceActivity.this.department);
                sb2.append(str);
                sb2.append(UploadStudyResourceActivity.this.semester);
                sb2.append(str2);
                sb2.append(UploadStudyResourceActivity.this.subject);
                uploadStudyResourceActivity2.mDatabase = instance2.getReference(sb2.toString());
            }
        });
        this.select_file.setOnClickListener(new OnClickListener() {
            public void onClick(View view) {
                UploadStudyResourceActivity.this.showOptions();
            }
        });
        this.upload_resource_btn.setOnClickListener(new OnClickListener() {
            public void onClick(View view) {
                if (UploadStudyResourceActivity.this.resource_description.getText().toString().equals("")) {
                    Toast.makeText(UploadStudyResourceActivity.this, "Enter Study Resource Description", Toast.LENGTH_SHORT).show();
                } else {
                    UploadStudyResourceActivity.this.uploadFile();
                }
            }
        });
    }

    /* access modifiers changed from: private */
    public void showOptions() {
        this.popupWindow.showAtLocation(this.layout, 17, 0, 0);
        this.customview.findViewById(R.id.dialog).setOnClickListener(new OnClickListener() {
            public void onClick(View view) {
                UploadStudyResourceActivity.this.popupWindow.dismiss();
            }
        });
        this.pdf.setOnClickListener(new OnClickListener() {
            public void onClick(View view) {
                UploadStudyResourceActivity uploadStudyResourceActivity = UploadStudyResourceActivity.this;
                uploadStudyResourceActivity.type = "pdf";
                uploadStudyResourceActivity.resource_link.setVisibility(View.GONE);
                UploadStudyResourceActivity.this.showPdfChooser();
                UploadStudyResourceActivity.this.popupWindow.dismiss();
            }
        });
        this.img.setOnClickListener(new OnClickListener() {
            public void onClick(View view) {
                UploadStudyResourceActivity uploadStudyResourceActivity = UploadStudyResourceActivity.this;
                uploadStudyResourceActivity.type = ContentTypes.EXTENSION_JPG_1;
                uploadStudyResourceActivity.resource_link.setVisibility(View.GONE);
                UploadStudyResourceActivity.this.showImageChooser();
                UploadStudyResourceActivity.this.popupWindow.dismiss();
            }
        });
        this.url.setOnClickListener(new OnClickListener() {
            public void onClick(View view) {
                UploadStudyResourceActivity uploadStudyResourceActivity = UploadStudyResourceActivity.this;
                uploadStudyResourceActivity.type = "link";
                uploadStudyResourceActivity.resource_link.setVisibility(View.VISIBLE);
                UploadStudyResourceActivity.this.popupWindow.dismiss();
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
        startActivityForResult(Intent.createChooser(intent, "Select Pdf"), PICK_IMAGE_REQUEST);
    }

    /* access modifiers changed from: protected */
    public void onActivityResult(int i, int i2, Intent intent) {
        super.onActivityResult(i, i2, intent);
        if (i == PICK_IMAGE_REQUEST && i2 == -1 && intent != null && intent.getData() != null) {
            this.filePath = intent.getData();
            if (this.type.equals(ContentTypes.EXTENSION_JPG_1)) {
                try {
                    this.resource_image.setImageBitmap(Media.getBitmap(getContentResolver(), this.filePath));
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            if (this.type.equals("pdf")) {
                this.resource_image.setImageResource(R.drawable.preview);
            }
        }
    }

    /* access modifiers changed from: private */
    public void uploadFile() {
        final ProgressDialog progressDialog = new ProgressDialog(this);
        progressDialog.setCancelable(false);
        progressDialog.setMessage("Uploading...");
        progressDialog.show();
        String str = "Students";
        String str2 = "HODs";
        String str3 = "Staff";
        if (this.filePath != null) {
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
                            StudyResources studyResources = new StudyResources(UploadStudyResourceActivity.this.resource_description.getText().toString().trim(), uri.toString(), UploadStudyResourceActivity.this.name, UploadStudyResourceActivity.this.id, DateFormat.format("dd-MM-yyyy (HH:mm:ss)", System.currentTimeMillis()).toString(), UploadStudyResourceActivity.this.type, UploadStudyResourceActivity.this.uploadId);
                            UploadStudyResourceActivity.this.mDatabase.child(UploadStudyResourceActivity.this.uploadId).setValue(studyResources).addOnSuccessListener(new OnSuccessListener<Void>() {
                                public void onSuccess(Void voidR) {
                                    UploadStudyResourceActivity.this.resource_description.setText("");
                                    UploadStudyResourceActivity.this.resource_image.setImageResource(0);
                                    UploadStudyResourceActivity.this.resource_semester.setSelection(0);
                                    UploadStudyResourceActivity.this.resource_subject.setAdapter((SpinnerAdapter) null);
                                    UploadStudyResourceActivity.this.filePath = null;
                                }
                            });
                        }
                    });
                    progressDialog.dismiss();
                    Toast.makeText(UploadStudyResourceActivity.this, "Resource Uploaded ", Toast.LENGTH_SHORT).show();
                }
            }).addOnFailureListener((OnFailureListener) new OnFailureListener() {
                public void onFailure(Exception exc) {
                    progressDialog.dismiss();
                    Toast.makeText(UploadStudyResourceActivity.this, exc.getMessage(), Toast.LENGTH_SHORT).show();
                }
            });
            final String trim = this.resource_description.getText().toString().trim();
            FirebaseDatabase.getInstance().getReference(str3).child(this.department).addListenerForSingleValueEvent(new ValueEventListener() {
                public void onCancelled(@NonNull DatabaseError databaseError) {
                }

                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    for (DataSnapshot value : dataSnapshot.getChildren()) {
                        Staff staff = (Staff) value.getValue(Staff.class);
                        if (!staff.getId().equals(FirebaseAuth.getInstance().getCurrentUser().getUid())) {
                            UploadStudyResourceActivity.this.sendNotification(staff.getToken(), staff.getId(), UploadStudyResourceActivity.this.name, trim);
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
                            UploadStudyResourceActivity.this.sendNotification(hod.getToken(), hod.getId(), UploadStudyResourceActivity.this.name, trim);
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
                                UploadStudyResourceActivity.this.sendNotification(student.getToken(), student.getId(), UploadStudyResourceActivity.this.name, trim);
                            }
                        }
                    }
                }
            });
            return;
        }
        String str4 = "";
        if (this.resource_link.getText().toString().trim().equals(str4)) {
            Toast.makeText(this, "No File Selected.", Toast.LENGTH_SHORT).show();
            progressDialog.dismiss();
        } else if (URLUtil.isValidUrl(this.resource_link.getText().toString())) {
            this.uploadId = this.mDatabase.push().getKey();
            StudyResources studyResources = new StudyResources(this.resource_description.getText().toString().trim(), this.resource_link.getText().toString().trim(), this.name, this.id, DateFormat.format("dd-MM-yyyy (HH:mm:ss)", System.currentTimeMillis()).toString(), this.type, this.uploadId);
            this.mDatabase.child(this.uploadId).setValue(studyResources).addOnSuccessListener(new OnSuccessListener<Void>() {
                public void onSuccess(Void voidR) {
                    UploadStudyResourceActivity.this.resource_link.setText("");
                    UploadStudyResourceActivity.this.resource_link.setVisibility(View.GONE);
                    UploadStudyResourceActivity.this.resource_semester.setSelection(0);
                    UploadStudyResourceActivity.this.resource_subject.setAdapter((SpinnerAdapter) null);
                }
            });
            final String trim2 = this.resource_description.getText().toString().trim();
            this.resource_description.setText(str4);
            FirebaseDatabase.getInstance().getReference(str3).child(this.department).addListenerForSingleValueEvent(new ValueEventListener() {
                public void onCancelled(@NonNull DatabaseError databaseError) {
                }

                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    for (DataSnapshot value : dataSnapshot.getChildren()) {
                        Staff staff = (Staff) value.getValue(Staff.class);
                        if (!staff.getId().equals(FirebaseAuth.getInstance().getCurrentUser().getUid())) {
                            UploadStudyResourceActivity.this.sendNotification(staff.getToken(), staff.getId(), UploadStudyResourceActivity.this.name, trim2);
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
                            UploadStudyResourceActivity.this.sendNotification(hod.getToken(), hod.getId(), UploadStudyResourceActivity.this.name, trim2);
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
                                UploadStudyResourceActivity.this.sendNotification(student.getToken(), student.getId(), UploadStudyResourceActivity.this.name, trim2);
                            }
                        }
                    }
                }
            });
            progressDialog.dismiss();
            Toast.makeText(this, "Resource Uploaded ", Toast.LENGTH_SHORT).show();
        } else {
            progressDialog.dismiss();
            Builder builder = new Builder(this);
            builder.setTitle((CharSequence) "Alert");
            builder.setMessage((CharSequence) "Please enter valid URL!");
            builder.setPositiveButton((CharSequence) "Ok", (DialogInterface.OnClickListener) null);
            builder.create();
            if (getLifecycle().getCurrentState().isAtLeast(State.CREATED)) {
                builder.show();
            }
        }
    }

    /* access modifiers changed from: private */
    public void sendNotification(String str, String str2, String str3, String str4) {
        this.apiService.sendNotification(new Sender(new Data(FirebaseAuth.getInstance().getCurrentUser().getUid(), str4, "New Study Resource", str2), str)).enqueue(new Callback<MyResponse>() {
            public void onFailure(Call<MyResponse> call, Throwable th) {
            }

            public void onResponse(Call<MyResponse> call, Response<MyResponse> response) {
                if (response.code() == 200) {
                    int i = ((MyResponse) response.body()).success;
                }
            }
        });
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
