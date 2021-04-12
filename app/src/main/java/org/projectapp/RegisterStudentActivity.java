package org.projectapp;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.SpinnerAdapter;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;
import androidx.appcompat.widget.AppCompatSpinner;
import androidx.appcompat.widget.AppCompatTextView;
import androidx.lifecycle.Lifecycle;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FileDownloadTask;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Calendar;
import java.util.Random;
import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.projectapp.Model.Document;
import org.projectapp.Model.Student;

public class RegisterStudentActivity extends AppCompatActivity {
    private static final int PICK_IMAGE_REQUEST = 234;
    String admin_login;
    String admin_password;
    String department;
    Uri filePath;
    AppCompatSpinner select_semester;
    String[] semArray;
    String semester;
    AppCompatTextView title;
    AppCompatButton upload_document;
    String year;

    /* access modifiers changed from: protected */
    public void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        setContentView((int) R.layout.activity_register_student);
        this.department = getIntent().getStringExtra("department");
        this.admin_login = getIntent().getStringExtra("admin_login");
        this.admin_password = getIntent().getStringExtra("admin_password");
        this.select_semester = (AppCompatSpinner) findViewById(R.id.select_semester);
        this.upload_document = (AppCompatButton) findViewById(R.id.upload_document);
        this.title = (AppCompatTextView) findViewById(R.id.title);
        this.title.setText("Register Student");
        Calendar instance = Calendar.getInstance();
        if (this.department.equals("Computer Department")) {
            if (instance.get(2) < 5 || instance.get(2) == 11) {
                this.semArray = new String[]{"Select Semester", "CO2I", "CO4I", "CO6I"};
            } else {
                this.semArray = new String[]{"Select Semester", "CO1I", "CO3I", "CO5I"};
            }
        } else if (this.department.equals("IT Department")) {
            if (instance.get(2) < 5 || instance.get(2) == 11) {
                this.semArray = new String[]{"Select Semester", "IF2I", "IF4I", "IF6I"};
            } else {
                this.semArray = new String[]{"Select Semester", "IF1I", "IF3I", "IF5I"};
            }
        } else if (this.department.equals("Civil I Shift Department")) {
            if (instance.get(2) < 5 || instance.get(2) == 11) {
                this.semArray = new String[]{"Select Semester", "CE2I", "CE4I", "CE6I"};
            } else {
                this.semArray = new String[]{"Select Semester", "CE1I", "CE3I", "CE5I"};
            }
        } else if (this.department.equals("Civil II Shift Department")) {
            if (instance.get(2) < 5 || instance.get(2) == 11) {
                this.semArray = new String[]{"Select Semester", "CE2I", "CE4I", "CE6I"};
            } else {
                this.semArray = new String[]{"Select Semester", "CE1I", "CE3I", "CE5I"};
            }
        } else if (this.department.equals("Mechanical I Shift Department")) {
            if (instance.get(2) < 5 || instance.get(2) == 11) {
                this.semArray = new String[]{"Select Semester", "ME2I", "ME4I", "ME6I"};
            } else {
                this.semArray = new String[]{"Select Semester", "ME1I", "ME3I", "ME5I"};
            }
        } else if (this.department.equals("Mechanical II Shift Department")) {
            if (instance.get(2) < 5 || instance.get(2) == 11) {
                this.semArray = new String[]{"Select Semester", "ME2I", "ME4I", "ME6I"};
            } else {
                this.semArray = new String[]{"Select Semester", "ME1I", "ME3I", "ME5I"};
            }
        } else if (this.department.equals("Chemical Department")) {
            if (instance.get(2) < 5 || instance.get(2) == 11) {
                this.semArray = new String[]{"Select Semester", "CH2I", "CH4I", "CH6I"};
            } else {
                this.semArray = new String[]{"Select Semester", "CH1I", "CH3I", "CH5I"};
            }
        } else if (this.department.equals("TR Department")) {
            if (instance.get(2) < 5 || instance.get(2) == 11) {
                this.semArray = new String[]{"Select Semester", "TR2G", "TR4G"};
            } else {
                this.semArray = new String[]{"Select Semester", "TR1G", "TR3G", "TR5G"};
            }
        }
        this.select_semester.setAdapter((SpinnerAdapter) new ArrayAdapter(this, R.layout.spinner_dialog, this.semArray));
        this.select_semester.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            public void onNothingSelected(AdapterView<?> adapterView) {
            }

            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long j) {
                if (adapterView.getItemAtPosition(i).toString().equals("Select Semester")) {
                    RegisterStudentActivity.this.semester = null;
                    return;
                }
                RegisterStudentActivity.this.semester = adapterView.getItemAtPosition(i).toString();
            }
        });
        this.upload_document.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                if (RegisterStudentActivity.this.semester == null) {
                    AlertDialog.Builder builder = new AlertDialog.Builder(RegisterStudentActivity.this);
                    builder.setTitle((CharSequence) "Alert");
                    builder.setMessage((CharSequence) "Select Semester");
                    builder.setPositiveButton((CharSequence) "Ok", (DialogInterface.OnClickListener) new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialogInterface, int i) {
                            RegisterStudentActivity.this.select_semester.requestFocus();
                        }
                    });
                    builder.create();
                    if (RegisterStudentActivity.this.getLifecycle().getCurrentState().isAtLeast(Lifecycle.State.CREATED)) {
                        builder.show();
                        return;
                    }
                    return;
                }
                Calendar instance = Calendar.getInstance();
                if (instance.get(2) < instance.get(5)) {
                    RegisterStudentActivity registerStudentActivity = RegisterStudentActivity.this;
                    registerStudentActivity.year = (instance.get(1) - 1) + "-" + instance.get(1);
                } else if (instance.get(2) == 11) {
                    RegisterStudentActivity registerStudentActivity2 = RegisterStudentActivity.this;
                    registerStudentActivity2.year = instance.get(1) + "-" + (instance.get(1) + 1);
                } else {
                    RegisterStudentActivity registerStudentActivity3 = RegisterStudentActivity.this;
                    registerStudentActivity3.year = instance.get(1) + "-" + (instance.get(1) + 1);
                }
                RegisterStudentActivity.this.UploadFile();
            }
        });
    }

    /* access modifiers changed from: private */
    public void UploadFile() {
        Intent intent = new Intent("android.intent.action.GET_CONTENT");
        intent.setType("application/vnd.ms-excel");
        startActivityForResult(Intent.createChooser(intent, "Select Document"), PICK_IMAGE_REQUEST);
    }

    /* access modifiers changed from: protected */
    public void onActivityResult(int i, int i2, Intent intent) {
        super.onActivityResult(i, i2, intent);
        if (i == PICK_IMAGE_REQUEST && i2 == RESULT_OK && intent != null && intent.getData() != null) {
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
            progressDialog.setMessage("Registering Students...");
            progressDialog.show();
            FirebaseDatabase instance = FirebaseDatabase.getInstance();
            final DatabaseReference reference = instance.getReference(this.department + "/Students List/" + this.semester + " " + this.year);
            FirebaseStorage instance2 = FirebaseStorage.getInstance();
            final StorageReference reference2 = instance2.getReference(this.department + "/Students List/" + this.semester + " " + this.year + ".xls");
            final ProgressDialog progressDialog2 = progressDialog;
            final Uri uri2 = uri;
            reference2.putFile(uri).addOnSuccessListener((OnSuccessListener) new OnSuccessListener<UploadTask.TaskSnapshot>() {
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                    reference2.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                        public void onSuccess(Uri uri) {
                            try {
                                String str = Environment.getExternalStorageDirectory().getPath() + "/rait/Document";
                                File file = new File(str);
                                if (!file.exists()) {
                                    file.mkdirs();
                                }
                                final File file2 = new File(str, RegisterStudentActivity.this.semester + " Students List " + RegisterStudentActivity.this.year + ".xls");
                                file2.createNewFile();
                                FirebaseStorage.getInstance().getReferenceFromUrl(String.valueOf(uri)).getFile(file2).addOnSuccessListener((OnSuccessListener) new OnSuccessListener<FileDownloadTask.TaskSnapshot>() {
                                    public void onSuccess(FileDownloadTask.TaskSnapshot taskSnapshot) {
                                        int i;
                                        int i2;
                                        int i3;
                                        try {
                                            HSSFWorkbook hSSFWorkbook = new HSSFWorkbook((InputStream) new FileInputStream(file2));
                                            HSSFSheet sheet = hSSFWorkbook.getSheet(RegisterStudentActivity.this.semester + " Students");
                                            int i4 = 0;
                                            HSSFRow row = sheet.getRow(0);
                                            int i5 = 6;
                                            row.createCell(6).setCellValue("Login Id");
                                            int i6 = 7;
                                            row.createCell(7).setCellValue("Temporary Password");
                                            int i7 = 1;
                                            int i8 = 1;
                                            while (i8 <= sheet.getLastRowNum()) {
                                                HSSFRow row2 = sheet.getRow(i8);
                                                if (row2 != null) {
                                                    final HSSFCell cell = row2.getCell(i4);
                                                    final HSSFCell cell2 = row2.getCell(i7);
                                                    final HSSFCell cell3 = row2.getCell(2);
                                                    final HSSFCell cell4 = row2.getCell(3);
                                                    final HSSFCell cell5 = row2.getCell(4);
                                                    HSSFCell cell6 = row2.getCell(5);
                                                    HSSFCell createCell = row2.createCell(i5);
                                                    HSSFCell createCell2 = row2.createCell(i6);
                                                    if (!cell4.toString().equals("") && !cell2.toString().equals("")) {
                                                        String access$100 = RegisterStudentActivity.this.generatePassword();
                                                        i2 = 0;
                                                        createCell.setCellValue(String.format("%.0f", (double)cell2.getNumericCellValue()));
                                                        createCell2.setCellValue(access$100);
                                                        FirebaseAuth.getInstance().signOut();
                                                        Task<AuthResult> createUserWithEmailAndPassword = FirebaseAuth.getInstance().createUserWithEmailAndPassword(cell4.toString().trim(), access$100);
                                                        i = i8;
                                                        final String str = access$100;
                                                        i3 = i7;
                                                        final HSSFCell hSSFCell = cell6;
                                                        createUserWithEmailAndPassword.addOnSuccessListener(new OnSuccessListener<AuthResult>() {
                                                            public void onSuccess(AuthResult authResult) {
                                                                FirebaseDatabase instance = FirebaseDatabase.getInstance();
                                                                DatabaseReference child = instance.getReference("Students/" + RegisterStudentActivity.this.department + "/" + RegisterStudentActivity.this.semester).child(FirebaseAuth.getInstance().getCurrentUser().getUid());
                                                                String uid = FirebaseAuth.getInstance().getCurrentUser().getUid();
                                                                String str = RegisterStudentActivity.this.department;
                                                                String str2 = RegisterStudentActivity.this.semester;
                                                                String format = String.format("%.0f", (double)cell2.getNumericCellValue());
                                                                String format2 = String.format("%.0f", (double)cell5.getNumericCellValue());
                                                                String format3 = String.format("%.0f", (double)cell.getNumericCellValue());
                                                                String str3 = str;
                                                                Student student = new Student(uid, cell3.getStringCellValue(), str, str2, format, format2, cell4.getStringCellValue(), format3, str3, "default", "deactivated", (String) null, String.format("%.0f", (double)cell3.getNumericCellValue()), (String) null, (String) null, (String) null, (String) null, (String) null, (String) null, (String) null);
                                                                child.setValue(student).addOnFailureListener(new OnFailureListener() {
                                                                    public void onFailure(@NonNull Exception exc) {
                                                                        FirebaseAuth.getInstance().getCurrentUser().delete();
                                                                        RegisterStudentActivity registerStudentActivity = RegisterStudentActivity.this;
                                                                        Toast.makeText(registerStudentActivity, cell3.getStringCellValue() + " Not Registered/\nEnter All Details.", Toast.LENGTH_LONG).show();
                                                                    }
                                                                });
                                                            }
                                                        }).addOnFailureListener(new OnFailureListener() {
                                                            public void onFailure(@NonNull Exception exc) {
                                                                Toast.makeText(RegisterStudentActivity.this, exc.getMessage(), Toast.LENGTH_SHORT).show();
                                                            }
                                                        });
                                                        i8 = i + 1;
                                                        i7 = i3;
                                                        i4 = i2;
                                                        i5 = 6;
                                                        i6 = 7;
                                                    }
                                                }
                                                i = i8;
                                                i3 = i7;
                                                i2 = i4;
                                                i8 = i + 1;
                                                i7 = i3;
                                                i4 = i2;
                                                i5 = 6;
                                                i6 = 7;
                                            }
                                            FileOutputStream fileOutputStream = new FileOutputStream(file2);
                                            hSSFWorkbook.write((OutputStream) fileOutputStream);
                                            fileOutputStream.flush();
                                            fileOutputStream.close();
                                        } catch (FileNotFoundException e) {
                                            e.printStackTrace();
                                        } catch (IOException e2) {
                                            e2.printStackTrace();
                                        }
                                        FirebaseAuth.getInstance().signOut();
                                        FirebaseAuth.getInstance().signInWithEmailAndPassword(RegisterStudentActivity.this.admin_login, RegisterStudentActivity.this.admin_password);
                                        reference2.putFile(Uri.fromFile(file2)).addOnSuccessListener((OnSuccessListener) new OnSuccessListener<UploadTask.TaskSnapshot>() {
                                            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                                                reference2.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                                                    public void onSuccess(Uri uri) {
                                                        reference.setValue(new Document(uri.toString()));
                                                        progressDialog2.dismiss();
                                                        RegisterStudentActivity registerStudentActivity = RegisterStudentActivity.this;
                                                        Toast.makeText(registerStudentActivity, "Students Registered Successfully and File Stored to " + uri2.getPath(), Toast.LENGTH_SHORT).show();
                                                    }
                                                });
                                            }
                                        }).addOnFailureListener((OnFailureListener) new OnFailureListener() {
                                            public void onFailure(@NonNull Exception exc) {
                                                progressDialog2.dismiss();
                                            }
                                        });
                                    }
                                }).addOnFailureListener((OnFailureListener) new OnFailureListener() {
                                    public void onFailure(@NonNull Exception exc) {
                                        progressDialog2.dismiss();
                                    }
                                });
                            } catch (IOException e) {
                                progressDialog2.dismiss();
                                e.printStackTrace();
                            }
                        }
                    });
                }
            }).addOnFailureListener((OnFailureListener) new OnFailureListener() {
                public void onFailure(Exception exc) {
                    progressDialog.dismiss();
                    Toast.makeText(RegisterStudentActivity.this, exc.getMessage(), Toast.LENGTH_SHORT).show();
                }
            });
            return;
        }
        Toast.makeText(this, "No File Selected.", Toast.LENGTH_SHORT).show();
    }

    /* access modifiers changed from: private */
    public String generatePassword() {
        char[] cArr = new char[8];
        Random random = new Random();
        for (int i = 0; i < 8; i++) {
            cArr[i] = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz1234567890!@#$%^&*()_+-=./<>".charAt(random.nextInt(80));
        }
        return cArr.toString();
    }

    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }
}
