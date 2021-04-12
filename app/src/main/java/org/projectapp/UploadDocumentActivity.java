package org.projectapp;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.SpinnerAdapter;
import android.widget.Toast;
import androidx.appcompat.app.AlertDialog.Builder;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;
import androidx.appcompat.widget.AppCompatSpinner;
import androidx.appcompat.widget.AppCompatTextView;
import androidx.lifecycle.Lifecycle.State;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask.TaskSnapshot;
import java.util.Calendar;
import org.projectapp.Model.Document;

public class UploadDocumentActivity extends AppCompatActivity {
    private static final int PICK_IMAGE_REQUEST = 234;
    String department;
    Uri filePath;
    String filetype;
    String path;
    AppCompatSpinner select_document;
    AppCompatSpinner select_semester;
    String[] semArray;
    String semester;
    AppCompatTextView title;
    String uploadId;
    AppCompatButton upload_document;
    String year;

    /* access modifiers changed from: protected */
    public void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        setContentView((int) R.layout.activity_upload_document);
        this.department = getIntent().getStringExtra("department");
        this.select_document = (AppCompatSpinner) findViewById(R.id.select_document);
        this.select_semester = (AppCompatSpinner) findViewById(R.id.select_semester);
        this.upload_document = (AppCompatButton) findViewById(R.id.upload_document);
        this.title = (AppCompatTextView) findViewById(R.id.title);
        this.title.setText("Upload Documents");
        this.select_document.setAdapter((SpinnerAdapter) new ArrayAdapter(this, R.layout.spinner_dialog, new String[]{"Select File", "Theory Attendence File", "Unit Test File", "Practical Attendence File", "Manual Marks File"}));
        this.select_document.setOnItemSelectedListener(new OnItemSelectedListener() {
            public void onNothingSelected(AdapterView<?> adapterView) {
            }

            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long j) {
                if (adapterView.getItemAtPosition(i).toString().equals("Select File")) {
                    UploadDocumentActivity.this.filetype = null;
                    return;
                }
                UploadDocumentActivity.this.filetype = adapterView.getItemAtPosition(i).toString();
            }
        });
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
                        this.semArray = new String[]{str, "TR2G", "TR4G"};
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
        this.select_semester.setAdapter((SpinnerAdapter) new ArrayAdapter(this, R.layout.spinner_dialog, this.semArray));
        this.select_semester.setOnItemSelectedListener(new OnItemSelectedListener() {
            public void onNothingSelected(AdapterView<?> adapterView) {
            }

            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long j) {
                if (adapterView.getItemAtPosition(i).toString().equals("Select Semester")) {
                    UploadDocumentActivity.this.semester = null;
                    return;
                }
                UploadDocumentActivity.this.semester = adapterView.getItemAtPosition(i).toString();
            }
        });
        this.upload_document.setOnClickListener(new OnClickListener() {
            public void onClick(View view) {
                String str = "Ok";
                String str2 = "Alert";
                if (UploadDocumentActivity.this.filetype == null) {
                    Builder builder = new Builder(UploadDocumentActivity.this);
                    builder.setTitle((CharSequence) str2);
                    builder.setMessage((CharSequence) "Select Document");
                    builder.setPositiveButton((CharSequence) str, (DialogInterface.OnClickListener) new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialogInterface, int i) {
                            UploadDocumentActivity.this.select_document.requestFocus();
                        }
                    });
                    builder.create();
                    if (UploadDocumentActivity.this.getLifecycle().getCurrentState().isAtLeast(State.CREATED)) {
                        builder.show();
                    }
                } else if (UploadDocumentActivity.this.semester == null) {
                    Builder builder2 = new Builder(UploadDocumentActivity.this);
                    builder2.setTitle((CharSequence) str2);
                    builder2.setMessage((CharSequence) "Select Semester");
                    builder2.setPositiveButton((CharSequence) str, (DialogInterface.OnClickListener) new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialogInterface, int i) {
                            UploadDocumentActivity.this.select_semester.requestFocus();
                        }
                    });
                    builder2.create();
                    if (UploadDocumentActivity.this.getLifecycle().getCurrentState().isAtLeast(State.CREATED)) {
                        builder2.show();
                    }
                } else {
                    Calendar instance = Calendar.getInstance();
                    String str3 = "-";
                    if (instance.get(2) < instance.get(5)) {
                        UploadDocumentActivity uploadDocumentActivity = UploadDocumentActivity.this;
                        StringBuilder sb = new StringBuilder();
                        sb.append(instance.get(1) - 1);
                        sb.append(str3);
                        sb.append(instance.get(1));
                        uploadDocumentActivity.year = sb.toString();
                    } else if (instance.get(2) == 11) {
                        UploadDocumentActivity uploadDocumentActivity2 = UploadDocumentActivity.this;
                        StringBuilder sb2 = new StringBuilder();
                        sb2.append(instance.get(1));
                        sb2.append(str3);
                        sb2.append(instance.get(1) + 1);
                        uploadDocumentActivity2.year = sb2.toString();
                    } else {
                        UploadDocumentActivity uploadDocumentActivity3 = UploadDocumentActivity.this;
                        StringBuilder sb3 = new StringBuilder();
                        sb3.append(instance.get(1));
                        sb3.append(str3);
                        sb3.append(instance.get(1) + 1);
                        uploadDocumentActivity3.year = sb3.toString();
                    }
                    String str4 = " ";
                    if (UploadDocumentActivity.this.filetype.equals("Theory Attendence File")) {
                        UploadDocumentActivity uploadDocumentActivity4 = UploadDocumentActivity.this;
                        StringBuilder sb4 = new StringBuilder();
                        sb4.append(UploadDocumentActivity.this.department);
                        sb4.append("/Theory Attendence/");
                        sb4.append(UploadDocumentActivity.this.semester);
                        sb4.append(str4);
                        sb4.append(UploadDocumentActivity.this.year);
                        uploadDocumentActivity4.path = sb4.toString();
                        UploadDocumentActivity.this.UploadFile();
                    } else if (UploadDocumentActivity.this.filetype.equals("Unit Test File")) {
                        UploadDocumentActivity uploadDocumentActivity5 = UploadDocumentActivity.this;
                        StringBuilder sb5 = new StringBuilder();
                        sb5.append(UploadDocumentActivity.this.department);
                        sb5.append("/Unit Test/");
                        sb5.append(UploadDocumentActivity.this.semester);
                        sb5.append(str4);
                        sb5.append(UploadDocumentActivity.this.year);
                        uploadDocumentActivity5.path = sb5.toString();
                        UploadDocumentActivity.this.UploadFile();
                    } else if (UploadDocumentActivity.this.filetype.equals("Practical Attendence File")) {
                        UploadDocumentActivity uploadDocumentActivity6 = UploadDocumentActivity.this;
                        StringBuilder sb6 = new StringBuilder();
                        sb6.append(UploadDocumentActivity.this.department);
                        sb6.append("/Practical Attendence/");
                        sb6.append(UploadDocumentActivity.this.semester);
                        sb6.append(str4);
                        sb6.append(UploadDocumentActivity.this.year);
                        uploadDocumentActivity6.path = sb6.toString();
                        UploadDocumentActivity.this.UploadFile();
                    } else if (UploadDocumentActivity.this.filetype.equals("Manual Marks File")) {
                        UploadDocumentActivity uploadDocumentActivity7 = UploadDocumentActivity.this;
                        StringBuilder sb7 = new StringBuilder();
                        sb7.append(UploadDocumentActivity.this.department);
                        sb7.append("/Manual Marks/");
                        sb7.append(UploadDocumentActivity.this.semester);
                        sb7.append(str4);
                        sb7.append(UploadDocumentActivity.this.year);
                        uploadDocumentActivity7.path = sb7.toString();
                        UploadDocumentActivity.this.UploadFile();
                    }
                }
            }
        });
    }

    /* access modifiers changed from: private */
    public void UploadFile() {
        Intent intent = new Intent("android.intent.action.GET_CONTENT");
        intent.setType("application/pdf");
        startActivityForResult(Intent.createChooser(intent, "Select Document"), PICK_IMAGE_REQUEST);
    }

    /* access modifiers changed from: protected */
    public void onActivityResult(int i, int i2, Intent intent) {
        super.onActivityResult(i, i2, intent);
        if (i == PICK_IMAGE_REQUEST && i2 == -1 && intent != null && intent.getData() != null) {
            this.filePath = intent.getData();
            Uri uri = this.filePath;
            if (uri != null) {
                uploadFile(uri);
            }
        }
    }

    private void uploadFile(Uri uri) {
        if (uri != null) {
            final ProgressDialog progressDialog = new ProgressDialog(this);
            progressDialog.setCancelable(false);
            progressDialog.setMessage("Uploading...");
            progressDialog.show();
            final DatabaseReference reference = FirebaseDatabase.getInstance().getReference(this.path);
            final StorageReference reference2 = FirebaseStorage.getInstance().getReference(this.path);
            reference2.putFile(uri).addOnSuccessListener((OnSuccessListener) new OnSuccessListener<TaskSnapshot>() {
                public void onSuccess(TaskSnapshot taskSnapshot) {
                    reference2.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                        public void onSuccess(Uri uri) {
                            reference.setValue(new Document(uri.toString()));
                        }
                    });
                    progressDialog.dismiss();
                    Toast.makeText(UploadDocumentActivity.this, "File Uploaded ", Toast.LENGTH_SHORT).show();
                }
            }).addOnFailureListener((OnFailureListener) new OnFailureListener() {
                public void onFailure(Exception exc) {
                    progressDialog.dismiss();
                    Toast.makeText(UploadDocumentActivity.this, exc.getMessage(), Toast.LENGTH_SHORT).show();
                }
            });
            return;
        }
        Toast.makeText(this, "No File Selected.", Toast.LENGTH_SHORT).show();
    }

    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }
}
