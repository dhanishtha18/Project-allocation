package org.projectapp;

import android.app.ProgressDialog;
import android.content.DialogInterface;
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
import androidx.appcompat.widget.AppCompatEditText;
import androidx.appcompat.widget.AppCompatSpinner;
import androidx.appcompat.widget.AppCompatTextView;
import androidx.lifecycle.Lifecycle;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
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
import java.util.ArrayList;
import java.util.Calendar;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.projectapp.Model.Document;
import org.projectapp.Model.Subject;

public class StudentTestMarksActivity extends AppCompatActivity {
    AppCompatEditText avg_ut;
    String department;
    Document document = null;
    AppCompatButton edit;
    String enrollment;
    File file = null;
    AppCompatEditText group;
    AppCompatEditText individual;
    AppCompatEditText micro;
    int rowno;
    AppCompatEditText seat;
    String semester;
    AppCompatSpinner student_subject;
    String subject;
    ArrayList<String> subjects;
    AppCompatTextView title;
    AppCompatEditText total;
    AppCompatEditText ut1;
    AppCompatEditText ut2;
    String year;

    /* access modifiers changed from: protected */
    public void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        setContentView((int) R.layout.activity_student_test_marks);
        this.seat = (AppCompatEditText) findViewById(R.id.seat);
        this.title = (AppCompatTextView) findViewById(R.id.title);
        this.ut1 = (AppCompatEditText) findViewById(R.id.ut1);
        this.ut2 = (AppCompatEditText) findViewById(R.id.ut2);
        this.avg_ut = (AppCompatEditText) findViewById(R.id.avg_ut);
        this.group = (AppCompatEditText) findViewById(R.id.group);
        this.individual = (AppCompatEditText) findViewById(R.id.individual);
        this.micro = (AppCompatEditText) findViewById(R.id.micro);
        this.total = (AppCompatEditText) findViewById(R.id.total);
        this.student_subject = (AppCompatSpinner) findViewById(R.id.student_subject);
        this.edit = (AppCompatButton) findViewById(R.id.edit);
        this.title.setText("Test Marks");
        this.subjects = new ArrayList<>();
        this.subjects.add("Select Subject");
        this.department = getIntent().getStringExtra("department");
        this.semester = getIntent().getStringExtra("semester");
        this.enrollment = getIntent().getStringExtra("enrollment");
        FirebaseDatabase instance = FirebaseDatabase.getInstance();
        instance.getReference(this.department + "/subjects/" + this.semester).addListenerForSingleValueEvent(new ValueEventListener() {
            public void onCancelled(@NonNull DatabaseError databaseError) {
            }

            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot value : dataSnapshot.getChildren()) {
                    Subject subject = (Subject) value.getValue(Subject.class);
                    if (subject.getName().contains("TH")) {
                        StudentTestMarksActivity.this.subjects.add(subject.getName());
                    }
                }
                AppCompatSpinner appCompatSpinner = StudentTestMarksActivity.this.student_subject;
                StudentTestMarksActivity studentTestMarksActivity = StudentTestMarksActivity.this;
                appCompatSpinner.setAdapter((SpinnerAdapter) new ArrayAdapter(studentTestMarksActivity, R.layout.spinner_dialog, studentTestMarksActivity.subjects));
            }
        });
        this.student_subject.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            public void onNothingSelected(AdapterView<?> adapterView) {
            }

            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long j) {
                if (!adapterView.getItemAtPosition(i).toString().equals("Select Subject")) {
                    StudentTestMarksActivity.this.ut1.setVisibility(View.VISIBLE);
                    StudentTestMarksActivity.this.ut2.setVisibility(View.VISIBLE);
                    StudentTestMarksActivity.this.avg_ut.setVisibility(View.VISIBLE);
                    StudentTestMarksActivity.this.group.setVisibility(View.VISIBLE);
                    StudentTestMarksActivity.this.individual.setVisibility(View.VISIBLE);
                    StudentTestMarksActivity.this.micro.setVisibility(View.VISIBLE);
                    StudentTestMarksActivity.this.total.setVisibility(View.VISIBLE);
                    StudentTestMarksActivity.this.seat.setVisibility(View.VISIBLE);
                    StudentTestMarksActivity.this.edit.setVisibility(View.VISIBLE);
                    Calendar instance = Calendar.getInstance();
                    if (instance.get(2) < instance.get(5)) {
                        StudentTestMarksActivity studentTestMarksActivity = StudentTestMarksActivity.this;
                        studentTestMarksActivity.year = (instance.get(1) - 1) + "-" + instance.get(1);
                    } else if (instance.get(2) == 11) {
                        StudentTestMarksActivity studentTestMarksActivity2 = StudentTestMarksActivity.this;
                        studentTestMarksActivity2.year = instance.get(1) + "-" + (instance.get(1) + 1);
                    } else {
                        StudentTestMarksActivity studentTestMarksActivity3 = StudentTestMarksActivity.this;
                        studentTestMarksActivity3.year = instance.get(1) + "-" + (instance.get(1) + 1);
                    }
                    StudentTestMarksActivity.this.subject = adapterView.getItemAtPosition(i).toString();
                    StudentTestMarksActivity studentTestMarksActivity4 = StudentTestMarksActivity.this;
                    studentTestMarksActivity4.startTest(studentTestMarksActivity4.semester, StudentTestMarksActivity.this.subject, StudentTestMarksActivity.this.year, StudentTestMarksActivity.this.department, StudentTestMarksActivity.this.enrollment);
                    return;
                }
                StudentTestMarksActivity.this.ut1.setVisibility(View.GONE);
                StudentTestMarksActivity.this.ut2.setVisibility(View.GONE);
                StudentTestMarksActivity.this.avg_ut.setVisibility(View.GONE);
                StudentTestMarksActivity.this.group.setVisibility(View.GONE);
                StudentTestMarksActivity.this.individual.setVisibility(View.GONE);
                StudentTestMarksActivity.this.micro.setVisibility(View.GONE);
                StudentTestMarksActivity.this.total.setVisibility(View.GONE);
                StudentTestMarksActivity.this.seat.setVisibility(View.GONE);
                StudentTestMarksActivity.this.edit.setVisibility(View.GONE);
            }
        });
        this.edit.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                if (StudentTestMarksActivity.this.seat.isEnabled()) {
                    StudentTestMarksActivity.this.edit.setCompoundDrawablesRelativeWithIntrinsicBounds(R.drawable.edit, 0, 0, 0);
                    StudentTestMarksActivity.this.edit.setText("Edit");
                    if (StudentTestMarksActivity.this.seat.getText().toString().trim().equals("")) {
                        AlertDialog.Builder builder = new AlertDialog.Builder(StudentTestMarksActivity.this);
                        builder.setTitle((CharSequence) "Alert");
                        builder.setMessage((CharSequence) "Enter Seat No.");
                        builder.setPositiveButton((CharSequence) "Ok", (DialogInterface.OnClickListener) new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialogInterface, int i) {
                            }
                        });
                        builder.create();
                        if (StudentTestMarksActivity.this.getLifecycle().getCurrentState().isAtLeast(Lifecycle.State.CREATED)) {
                            builder.show();
                        }
                    } else if (StudentTestMarksActivity.this.ut1.getText().toString().trim().equals("")) {
                        AlertDialog.Builder builder2 = new AlertDialog.Builder(StudentTestMarksActivity.this);
                        builder2.setTitle((CharSequence) "Alert");
                        builder2.setMessage((CharSequence) "Enter U.T.1 Marks");
                        builder2.setPositiveButton((CharSequence) "Ok", (DialogInterface.OnClickListener) new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialogInterface, int i) {
                            }
                        });
                        builder2.create();
                        if (StudentTestMarksActivity.this.getLifecycle().getCurrentState().isAtLeast(Lifecycle.State.CREATED)) {
                            builder2.show();
                        }
                    } else if (StudentTestMarksActivity.this.ut2.getText().toString().trim().equals("")) {
                        AlertDialog.Builder builder3 = new AlertDialog.Builder(StudentTestMarksActivity.this);
                        builder3.setTitle((CharSequence) "Alert");
                        builder3.setMessage((CharSequence) "Enter U.T.2 Marks");
                        builder3.setPositiveButton((CharSequence) "Ok", (DialogInterface.OnClickListener) new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialogInterface, int i) {
                            }
                        });
                        builder3.create();
                        if (StudentTestMarksActivity.this.getLifecycle().getCurrentState().isAtLeast(Lifecycle.State.CREATED)) {
                            builder3.show();
                        }
                    } else if (StudentTestMarksActivity.this.group.getText().toString().trim().equals("")) {
                        AlertDialog.Builder builder4 = new AlertDialog.Builder(StudentTestMarksActivity.this);
                        builder4.setTitle((CharSequence) "Alert");
                        builder4.setMessage((CharSequence) "Enter Group Marks.");
                        builder4.setPositiveButton((CharSequence) "Ok", (DialogInterface.OnClickListener) new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialogInterface, int i) {
                            }
                        });
                        builder4.create();
                        if (StudentTestMarksActivity.this.getLifecycle().getCurrentState().isAtLeast(Lifecycle.State.CREATED)) {
                            builder4.show();
                        }
                    } else if (StudentTestMarksActivity.this.individual.getText().toString().trim().equals("")) {
                        AlertDialog.Builder builder5 = new AlertDialog.Builder(StudentTestMarksActivity.this);
                        builder5.setTitle((CharSequence) "Alert");
                        builder5.setMessage((CharSequence) "Enter Individual Marks");
                        builder5.setPositiveButton((CharSequence) "Ok", (DialogInterface.OnClickListener) new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialogInterface, int i) {
                            }
                        });
                        builder5.create();
                        if (StudentTestMarksActivity.this.getLifecycle().getCurrentState().isAtLeast(Lifecycle.State.CREATED)) {
                            builder5.show();
                        }
                    } else {
                        ProgressDialog progressDialog = new ProgressDialog(StudentTestMarksActivity.this);
                        progressDialog.setCancelable(false);
                        progressDialog.setMessage("Uploading Marks...");
                        progressDialog.show();
                        try {
                            HSSFWorkbook hSSFWorkbook = new HSSFWorkbook((InputStream) new FileInputStream(StudentTestMarksActivity.this.file));
                            HSSFRow row = hSSFWorkbook.getSheet(StudentTestMarksActivity.this.subject).getRow(StudentTestMarksActivity.this.rowno);
                            row.createCell(3).setCellValue(StudentTestMarksActivity.this.seat.getText().toString().trim());
                            final float parseFloat = Float.parseFloat(StudentTestMarksActivity.this.ut1.getText().toString().trim());
                            final float parseFloat2 = Float.parseFloat(StudentTestMarksActivity.this.ut2.getText().toString().trim());
                            row.createCell(4).setCellValue(StudentTestMarksActivity.this.ut1.getText().toString().trim());
                            row.createCell(5).setCellValue(StudentTestMarksActivity.this.ut2.getText().toString().trim());
                            double d = (double) ((parseFloat + parseFloat2) / 2.0f);
                            row.createCell(6).setCellValue(Math.ceil(d));
                            row.createCell(7).setCellValue(StudentTestMarksActivity.this.group.getText().toString().trim());
                            row.createCell(View.GONE).setCellValue(StudentTestMarksActivity.this.individual.getText().toString().trim());
                            float parseFloat3 = Float.parseFloat(StudentTestMarksActivity.this.individual.getText().toString().trim());
                            float parseFloat4 = Float.parseFloat(StudentTestMarksActivity.this.group.getText().toString().trim());
                            float ceil = (float) (Math.ceil(d) + Math.ceil((double) (parseFloat3 + parseFloat4)));
                            row.createCell(9).setCellValue(String.valueOf(ceil));
                            FileOutputStream fileOutputStream = new FileOutputStream(StudentTestMarksActivity.this.file);
                            hSSFWorkbook.write((OutputStream) fileOutputStream);
                            fileOutputStream.flush();
                            fileOutputStream.close();
                            final StorageReference referenceFromUrl = FirebaseStorage.getInstance().getReferenceFromUrl(StudentTestMarksActivity.this.document.getUrl());
                            final float f = parseFloat3;
                            final float f2 = parseFloat4;
                            final float f3 = ceil;
                            final ProgressDialog progressDialog2 = progressDialog;
                            referenceFromUrl.putFile(Uri.fromFile(StudentTestMarksActivity.this.file)).addOnSuccessListener((OnSuccessListener) new OnSuccessListener<UploadTask.TaskSnapshot>() {
                                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                                    referenceFromUrl.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                                        public void onSuccess(Uri uri) {
                                            Document document = new Document(uri.toString());
                                            FirebaseDatabase instance = FirebaseDatabase.getInstance();
                                            instance.getReference(StudentTestMarksActivity.this.department + "/Unit Test/" + StudentTestMarksActivity.this.semester + " " + StudentTestMarksActivity.this.year).setValue(document);
                                            StudentTestMarksActivity.this.avg_ut.setText(String.valueOf((float) Math.ceil((double) ((parseFloat + parseFloat2) / 2.0f))));
                                            StudentTestMarksActivity.this.micro.setText(String.valueOf(f + f2));
                                            StudentTestMarksActivity.this.total.setText(String.valueOf(f3));
                                            progressDialog2.dismiss();
                                            Toast.makeText(StudentTestMarksActivity.this, "Marks Updated", Toast.LENGTH_SHORT).show();
                                        }
                                    });
                                }
                            });
                        } catch (FileNotFoundException e) {
                            progressDialog.dismiss();
                            e.printStackTrace();
                        } catch (IOException e2) {
                            progressDialog.dismiss();
                            e2.printStackTrace();
                        }
                        StudentTestMarksActivity.this.seat.setEnabled(false);
                        StudentTestMarksActivity.this.ut1.setEnabled(false);
                        StudentTestMarksActivity.this.ut2.setEnabled(false);
                        StudentTestMarksActivity.this.group.setEnabled(false);
                        StudentTestMarksActivity.this.individual.setEnabled(false);
                    }
                } else {
                    StudentTestMarksActivity.this.seat.setEnabled(true);
                    StudentTestMarksActivity.this.ut1.setEnabled(true);
                    StudentTestMarksActivity.this.ut2.setEnabled(true);
                    StudentTestMarksActivity.this.group.setEnabled(true);
                    StudentTestMarksActivity.this.individual.setEnabled(true);
                    StudentTestMarksActivity.this.edit.setCompoundDrawablesRelativeWithIntrinsicBounds(R.drawable.done, 0, 0, 0);
                    StudentTestMarksActivity.this.edit.setText("Done");
                }
            }
        });
    }

    /* access modifiers changed from: private */
    public void startTest(String str, String str2, String str3, String str4, String str5) {
        FirebaseDatabase instance = FirebaseDatabase.getInstance();
        final String str6 = str;
        final String str7 = str3;
        final String str8 = str2;
        final String str9 = str5;
        instance.getReference(str4 + "/Unit Test/" + str + " " + str3).addListenerForSingleValueEvent(new ValueEventListener() {
            public void onCancelled(@NonNull DatabaseError databaseError) {
            }

            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                StudentTestMarksActivity.this.document = (Document) dataSnapshot.getValue(Document.class);
                if (StudentTestMarksActivity.this.document.getUrl() != null) {
                    try {
                        File file = new File(Environment.getExternalStorageDirectory().getPath() + "/rait/Document");
                        if (!file.exists()) {
                            file.mkdirs();
                        }
                        StudentTestMarksActivity.this.file = new File(Environment.getExternalStorageDirectory().getPath() + "/rait/Document", str6 + " Unit Test " + str7 + ".xls");
                        StudentTestMarksActivity.this.file.createNewFile();
                        final ProgressDialog progressDialog = new ProgressDialog(StudentTestMarksActivity.this);
                        progressDialog.setCancelable(false);
                        progressDialog.setMessage("Fetching Marks");
                        progressDialog.show();
                        FirebaseStorage.getInstance().getReferenceFromUrl(StudentTestMarksActivity.this.document.getUrl()).getFile(StudentTestMarksActivity.this.file).addOnSuccessListener((OnSuccessListener) new OnSuccessListener<FileDownloadTask.TaskSnapshot>() {
                            /* JADX WARNING: Code restructure failed: missing block: B:13:0x0053, code lost:
                                r8.this$1.this$0.rowno = r1;
                             */
                            /* JADX WARNING: Code restructure failed: missing block: B:14:0x005d, code lost:
                                if (r2.getCell(3) != null) goto L_0x007a;
                             */
                            /* JADX WARNING: Code restructure failed: missing block: B:15:0x005f, code lost:
                                r2.createCell(3).setCellValue("-");
                                r8.this$1.this$0.seat.setText(r2.getCell(3).toString());
                             */
                            /* JADX WARNING: Code restructure failed: missing block: B:16:0x007a, code lost:
                                r8.this$1.this$0.seat.setText(r2.getCell(3).toString());
                             */
                            /* JADX WARNING: Code restructure failed: missing block: B:19:0x0092, code lost:
                                if (r2.getCell(4) != null) goto L_0x00a5;
                             */
                            /* JADX WARNING: Code restructure failed: missing block: B:21:?, code lost:
                                r8.this$1.this$0.ut1.setText("0");
                                r2.createCell(4).setCellValue("0");
                             */
                            /* JADX WARNING: Code restructure failed: missing block: B:22:0x00a5, code lost:
                                r8.this$1.this$0.ut1.setText(r2.getCell(4).toString());
                             */
                            /* JADX WARNING: Code restructure failed: missing block: B:24:0x00bb, code lost:
                                if (r2.getCell(5) != null) goto L_0x00ce;
                             */
                            /* JADX WARNING: Code restructure failed: missing block: B:25:0x00bd, code lost:
                                r8.this$1.this$0.ut2.setText("0");
                                r2.createCell(5).setCellValue("0");
                             */
                            /* JADX WARNING: Code restructure failed: missing block: B:26:0x00ce, code lost:
                                r8.this$1.this$0.ut2.setText(r2.getCell(5).toString());
                             */
                            /* JADX WARNING: Code restructure failed: missing block: B:28:0x00e4, code lost:
                                if (r2.getCell(6) != null) goto L_0x00f7;
                             */
                            /* JADX WARNING: Code restructure failed: missing block: B:29:0x00e6, code lost:
                                r8.this$1.this$0.avg_ut.setText("0");
                                r2.createCell(6).setCellValue("0");
                             */
                            /* JADX WARNING: Code restructure failed: missing block: B:30:0x00f7, code lost:
                                r8.this$1.this$0.avg_ut.setText(r2.getCell(6).toString());
                             */
                            /* JADX WARNING: Code restructure failed: missing block: B:32:0x010d, code lost:
                                if (r2.getCell(7) != null) goto L_0x0120;
                             */
                            /* JADX WARNING: Code restructure failed: missing block: B:33:0x010f, code lost:
                                r8.this$1.this$0.group.setText("0");
                                r2.createCell(7).setCellValue("0");
                             */
                            /* JADX WARNING: Code restructure failed: missing block: B:34:0x0120, code lost:
                                r8.this$1.this$0.group.setText(r2.getCell(7).toString());
                             */
                            /* JADX WARNING: Code restructure failed: missing block: B:36:0x0137, code lost:
                                if (r2.getCell(View.GONE) != null) goto L_0x014a;
                             */
                            /* JADX WARNING: Code restructure failed: missing block: B:37:0x0139, code lost:
                                r8.this$1.this$0.individual.setText("0");
                                r2.createCell(View.GONE).setCellValue("0");
                             */
                            /* JADX WARNING: Code restructure failed: missing block: B:38:0x014a, code lost:
                                r8.this$1.this$0.individual.setText(r2.getCell(View.GONE).toString());
                             */
                            /* JADX WARNING: Code restructure failed: missing block: B:40:0x0161, code lost:
                                if (r2.getCell(9) != null) goto L_0x0174;
                             */
                            /* JADX WARNING: Code restructure failed: missing block: B:41:0x0163, code lost:
                                r8.this$1.this$0.total.setText("0");
                                r2.createCell(9).setCellValue("0");
                             */
                            /* JADX WARNING: Code restructure failed: missing block: B:42:0x0174, code lost:
                                r8.this$1.this$0.total.setText(r2.getCell(9).toString());
                             */
                            /* JADX WARNING: Code restructure failed: missing block: B:43:0x0185, code lost:
                                r8.this$1.this$0.micro.setText(java.lang.String.valueOf(java.lang.Float.parseFloat(r8.this$1.this$0.group.getText().toString()) + java.lang.Float.parseFloat(r8.this$1.this$0.individual.getText().toString())));
                             */
                            /* Code decompiled incorrectly, please refer to instructions dump. */
                            public void onSuccess(com.google.firebase.storage.FileDownloadTask.TaskSnapshot r9) {
                                /*
                                    r8 = this;
                                    org.apache.poi.hssf.usermodel.HSSFWorkbook r9 = new org.apache.poi.hssf.usermodel.HSSFWorkbook     // Catch:{ IOException -> 0x01f5 }
                                    java.io.FileInputStream r0 = new java.io.FileInputStream     // Catch:{ IOException -> 0x01f5 }
                                    projectapp.StudentTestMarksActivity$4 r1 = projectapp.StudentTestMarksActivity.C19674.this     // Catch:{ IOException -> 0x01f5 }
                                    projectapp.StudentTestMarksActivity r1 = projectapp.StudentTestMarksActivity.this     // Catch:{ IOException -> 0x01f5 }
                                    java.io.File r1 = r1.file     // Catch:{ IOException -> 0x01f5 }
                                    r0.<init>(r1)     // Catch:{ IOException -> 0x01f5 }
                                    r9.<init>((java.io.InputStream) r0)     // Catch:{ IOException -> 0x01f5 }
                                    projectapp.StudentTestMarksActivity$4 r0 = projectapp.StudentTestMarksActivity.C19674.this     // Catch:{ IOException -> 0x01f5 }
                                    java.lang.String r0 = r4     // Catch:{ IOException -> 0x01f5 }
                                    org.apache.poi.hssf.usermodel.HSSFSheet r9 = r9.getSheet((java.lang.String) r0)     // Catch:{ IOException -> 0x01f5 }
                                    if (r9 == 0) goto L_0x01bc
                                    int r0 = r9.getPhysicalNumberOfRows()     // Catch:{ IOException -> 0x01f5 }
                                    if (r0 != 0) goto L_0x0022
                                    goto L_0x01bc
                                L_0x0022:
                                    r0 = 3
                                    r1 = r0
                                L_0x0024:
                                    int r2 = r9.getPhysicalNumberOfRows()     // Catch:{ IOException -> 0x01f5 }
                                    if (r1 >= r2) goto L_0x01fe
                                    org.apache.poi.hssf.usermodel.HSSFRow r2 = r9.getRow((int) r1)     // Catch:{ IOException -> 0x01f5 }
                                    if (r2 == 0) goto L_0x01b8
                                    java.lang.String r3 = "%.0f"
                                    r4 = 1
                                    java.lang.Object[] r4 = new java.lang.Object[r4]     // Catch:{ IOException -> 0x01f5 }
                                    r5 = 0
                                    r6 = 2
                                    org.apache.poi.hssf.usermodel.HSSFCell r6 = r2.getCell((int) r6)     // Catch:{ IOException -> 0x01f5 }
                                    double r6 = r6.getNumericCellValue()     // Catch:{ IOException -> 0x01f5 }
                                    java.lang.Double r6 = java.lang.Double.valueOf(r6)     // Catch:{ IOException -> 0x01f5 }
                                    r4[r5] = r6     // Catch:{ IOException -> 0x01f5 }
                                    java.lang.String r3 = java.lang.String.format(r3, r4)     // Catch:{ IOException -> 0x01f5 }
                                    projectapp.StudentTestMarksActivity$4 r4 = projectapp.StudentTestMarksActivity.C19674.this     // Catch:{ IOException -> 0x01f5 }
                                    java.lang.String r4 = r5     // Catch:{ IOException -> 0x01f5 }
                                    boolean r3 = r3.equals(r4)     // Catch:{ IOException -> 0x01f5 }
                                    if (r3 == 0) goto L_0x01b8
                                    projectapp.StudentTestMarksActivity$4 r9 = projectapp.StudentTestMarksActivity.C19674.this     // Catch:{ IOException -> 0x01f5 }
                                    projectapp.StudentTestMarksActivity r9 = projectapp.StudentTestMarksActivity.this     // Catch:{ IOException -> 0x01f5 }
                                    r9.rowno = r1     // Catch:{ IOException -> 0x01f5 }
                                    org.apache.poi.hssf.usermodel.HSSFCell r9 = r2.getCell((int) r0)     // Catch:{ IOException -> 0x01f5 }
                                    if (r9 != 0) goto L_0x007a
                                    org.apache.poi.hssf.usermodel.HSSFCell r9 = r2.createCell((int) r0)     // Catch:{ IOException -> 0x01f5 }
                                    java.lang.String r1 = "-"
                                    r9.setCellValue((java.lang.String) r1)     // Catch:{ IOException -> 0x01f5 }
                                    projectapp.StudentTestMarksActivity$4 r9 = projectapp.StudentTestMarksActivity.C19674.this     // Catch:{ IOException -> 0x01f5 }
                                    projectapp.StudentTestMarksActivity r9 = projectapp.StudentTestMarksActivity.this     // Catch:{ IOException -> 0x01f5 }
                                    androidx.appcompat.widget.AppCompatEditText r9 = r9.seat     // Catch:{ IOException -> 0x01f5 }
                                    org.apache.poi.hssf.usermodel.HSSFCell r0 = r2.getCell((int) r0)     // Catch:{ IOException -> 0x01f5 }
                                    java.lang.String r0 = r0.toString()     // Catch:{ IOException -> 0x01f5 }
                                    r9.setText(r0)     // Catch:{ IOException -> 0x01f5 }
                                    goto L_0x008b
                                L_0x007a:
                                    projectapp.StudentTestMarksActivity$4 r9 = projectapp.StudentTestMarksActivity.C19674.this     // Catch:{ IOException -> 0x01f5 }
                                    projectapp.StudentTestMarksActivity r9 = projectapp.StudentTestMarksActivity.this     // Catch:{ IOException -> 0x01f5 }
                                    androidx.appcompat.widget.AppCompatEditText r9 = r9.seat     // Catch:{ IOException -> 0x01f5 }
                                    org.apache.poi.hssf.usermodel.HSSFCell r0 = r2.getCell((int) r0)     // Catch:{ IOException -> 0x01f5 }
                                    java.lang.String r0 = r0.toString()     // Catch:{ IOException -> 0x01f5 }
                                    r9.setText(r0)     // Catch:{ IOException -> 0x01f5 }
                                L_0x008b:
                                    r9 = 4
                                    org.apache.poi.hssf.usermodel.HSSFCell r0 = r2.getCell((int) r9)     // Catch:{ IOException -> 0x01f5 }
                                    java.lang.String r1 = "0"
                                    if (r0 != 0) goto L_0x00a5
                                    projectapp.StudentTestMarksActivity$4 r0 = projectapp.StudentTestMarksActivity.C19674.this     // Catch:{ IOException -> 0x01f5 }
                                    projectapp.StudentTestMarksActivity r0 = projectapp.StudentTestMarksActivity.this     // Catch:{ IOException -> 0x01f5 }
                                    androidx.appcompat.widget.AppCompatEditText r0 = r0.ut1     // Catch:{ IOException -> 0x01f5 }
                                    r0.setText(r1)     // Catch:{ IOException -> 0x01f5 }
                                    org.apache.poi.hssf.usermodel.HSSFCell r9 = r2.createCell((int) r9)     // Catch:{ IOException -> 0x01f5 }
                                    r9.setCellValue((java.lang.String) r1)     // Catch:{ IOException -> 0x01f5 }
                                    goto L_0x00b6
                                L_0x00a5:
                                    projectapp.StudentTestMarksActivity$4 r0 = projectapp.StudentTestMarksActivity.C19674.this     // Catch:{ IOException -> 0x01f5 }
                                    projectapp.StudentTestMarksActivity r0 = projectapp.StudentTestMarksActivity.this     // Catch:{ IOException -> 0x01f5 }
                                    androidx.appcompat.widget.AppCompatEditText r0 = r0.ut1     // Catch:{ IOException -> 0x01f5 }
                                    org.apache.poi.hssf.usermodel.HSSFCell r9 = r2.getCell((int) r9)     // Catch:{ IOException -> 0x01f5 }
                                    java.lang.String r9 = r9.toString()     // Catch:{ IOException -> 0x01f5 }
                                    r0.setText(r9)     // Catch:{ IOException -> 0x01f5 }
                                L_0x00b6:
                                    r9 = 5
                                    org.apache.poi.hssf.usermodel.HSSFCell r0 = r2.getCell((int) r9)     // Catch:{ IOException -> 0x01f5 }
                                    if (r0 != 0) goto L_0x00ce
                                    projectapp.StudentTestMarksActivity$4 r0 = projectapp.StudentTestMarksActivity.C19674.this     // Catch:{ IOException -> 0x01f5 }
                                    projectapp.StudentTestMarksActivity r0 = projectapp.StudentTestMarksActivity.this     // Catch:{ IOException -> 0x01f5 }
                                    androidx.appcompat.widget.AppCompatEditText r0 = r0.ut2     // Catch:{ IOException -> 0x01f5 }
                                    r0.setText(r1)     // Catch:{ IOException -> 0x01f5 }
                                    org.apache.poi.hssf.usermodel.HSSFCell r9 = r2.createCell((int) r9)     // Catch:{ IOException -> 0x01f5 }
                                    r9.setCellValue((java.lang.String) r1)     // Catch:{ IOException -> 0x01f5 }
                                    goto L_0x00df
                                L_0x00ce:
                                    projectapp.StudentTestMarksActivity$4 r0 = projectapp.StudentTestMarksActivity.C19674.this     // Catch:{ IOException -> 0x01f5 }
                                    projectapp.StudentTestMarksActivity r0 = projectapp.StudentTestMarksActivity.this     // Catch:{ IOException -> 0x01f5 }
                                    androidx.appcompat.widget.AppCompatEditText r0 = r0.ut2     // Catch:{ IOException -> 0x01f5 }
                                    org.apache.poi.hssf.usermodel.HSSFCell r9 = r2.getCell((int) r9)     // Catch:{ IOException -> 0x01f5 }
                                    java.lang.String r9 = r9.toString()     // Catch:{ IOException -> 0x01f5 }
                                    r0.setText(r9)     // Catch:{ IOException -> 0x01f5 }
                                L_0x00df:
                                    r9 = 6
                                    org.apache.poi.hssf.usermodel.HSSFCell r0 = r2.getCell((int) r9)     // Catch:{ IOException -> 0x01f5 }
                                    if (r0 != 0) goto L_0x00f7
                                    projectapp.StudentTestMarksActivity$4 r0 = projectapp.StudentTestMarksActivity.C19674.this     // Catch:{ IOException -> 0x01f5 }
                                    projectapp.StudentTestMarksActivity r0 = projectapp.StudentTestMarksActivity.this     // Catch:{ IOException -> 0x01f5 }
                                    androidx.appcompat.widget.AppCompatEditText r0 = r0.avg_ut     // Catch:{ IOException -> 0x01f5 }
                                    r0.setText(r1)     // Catch:{ IOException -> 0x01f5 }
                                    org.apache.poi.hssf.usermodel.HSSFCell r9 = r2.createCell((int) r9)     // Catch:{ IOException -> 0x01f5 }
                                    r9.setCellValue((java.lang.String) r1)     // Catch:{ IOException -> 0x01f5 }
                                    goto L_0x0108
                                L_0x00f7:
                                    projectapp.StudentTestMarksActivity$4 r0 = projectapp.StudentTestMarksActivity.C19674.this     // Catch:{ IOException -> 0x01f5 }
                                    projectapp.StudentTestMarksActivity r0 = projectapp.StudentTestMarksActivity.this     // Catch:{ IOException -> 0x01f5 }
                                    androidx.appcompat.widget.AppCompatEditText r0 = r0.avg_ut     // Catch:{ IOException -> 0x01f5 }
                                    org.apache.poi.hssf.usermodel.HSSFCell r9 = r2.getCell((int) r9)     // Catch:{ IOException -> 0x01f5 }
                                    java.lang.String r9 = r9.toString()     // Catch:{ IOException -> 0x01f5 }
                                    r0.setText(r9)     // Catch:{ IOException -> 0x01f5 }
                                L_0x0108:
                                    r9 = 7
                                    org.apache.poi.hssf.usermodel.HSSFCell r0 = r2.getCell((int) r9)     // Catch:{ IOException -> 0x01f5 }
                                    if (r0 != 0) goto L_0x0120
                                    projectapp.StudentTestMarksActivity$4 r0 = projectapp.StudentTestMarksActivity.C19674.this     // Catch:{ IOException -> 0x01f5 }
                                    projectapp.StudentTestMarksActivity r0 = projectapp.StudentTestMarksActivity.this     // Catch:{ IOException -> 0x01f5 }
                                    androidx.appcompat.widget.AppCompatEditText r0 = r0.group     // Catch:{ IOException -> 0x01f5 }
                                    r0.setText(r1)     // Catch:{ IOException -> 0x01f5 }
                                    org.apache.poi.hssf.usermodel.HSSFCell r9 = r2.createCell((int) r9)     // Catch:{ IOException -> 0x01f5 }
                                    r9.setCellValue((java.lang.String) r1)     // Catch:{ IOException -> 0x01f5 }
                                    goto L_0x0131
                                L_0x0120:
                                    projectapp.StudentTestMarksActivity$4 r0 = projectapp.StudentTestMarksActivity.C19674.this     // Catch:{ IOException -> 0x01f5 }
                                    projectapp.StudentTestMarksActivity r0 = projectapp.StudentTestMarksActivity.this     // Catch:{ IOException -> 0x01f5 }
                                    androidx.appcompat.widget.AppCompatEditText r0 = r0.group     // Catch:{ IOException -> 0x01f5 }
                                    org.apache.poi.hssf.usermodel.HSSFCell r9 = r2.getCell((int) r9)     // Catch:{ IOException -> 0x01f5 }
                                    java.lang.String r9 = r9.toString()     // Catch:{ IOException -> 0x01f5 }
                                    r0.setText(r9)     // Catch:{ IOException -> 0x01f5 }
                                L_0x0131:
                                    r9 = 8
                                    org.apache.poi.hssf.usermodel.HSSFCell r0 = r2.getCell((int) r9)     // Catch:{ IOException -> 0x01f5 }
                                    if (r0 != 0) goto L_0x014a
                                    projectapp.StudentTestMarksActivity$4 r0 = projectapp.StudentTestMarksActivity.C19674.this     // Catch:{ IOException -> 0x01f5 }
                                    projectapp.StudentTestMarksActivity r0 = projectapp.StudentTestMarksActivity.this     // Catch:{ IOException -> 0x01f5 }
                                    androidx.appcompat.widget.AppCompatEditText r0 = r0.individual     // Catch:{ IOException -> 0x01f5 }
                                    r0.setText(r1)     // Catch:{ IOException -> 0x01f5 }
                                    org.apache.poi.hssf.usermodel.HSSFCell r9 = r2.createCell((int) r9)     // Catch:{ IOException -> 0x01f5 }
                                    r9.setCellValue((java.lang.String) r1)     // Catch:{ IOException -> 0x01f5 }
                                    goto L_0x015b
                                L_0x014a:
                                    projectapp.StudentTestMarksActivity$4 r0 = projectapp.StudentTestMarksActivity.C19674.this     // Catch:{ IOException -> 0x01f5 }
                                    projectapp.StudentTestMarksActivity r0 = projectapp.StudentTestMarksActivity.this     // Catch:{ IOException -> 0x01f5 }
                                    androidx.appcompat.widget.AppCompatEditText r0 = r0.individual     // Catch:{ IOException -> 0x01f5 }
                                    org.apache.poi.hssf.usermodel.HSSFCell r9 = r2.getCell((int) r9)     // Catch:{ IOException -> 0x01f5 }
                                    java.lang.String r9 = r9.toString()     // Catch:{ IOException -> 0x01f5 }
                                    r0.setText(r9)     // Catch:{ IOException -> 0x01f5 }
                                L_0x015b:
                                    r9 = 9
                                    org.apache.poi.hssf.usermodel.HSSFCell r0 = r2.getCell((int) r9)     // Catch:{ IOException -> 0x01f5 }
                                    if (r0 != 0) goto L_0x0174
                                    projectapp.StudentTestMarksActivity$4 r0 = projectapp.StudentTestMarksActivity.C19674.this     // Catch:{ IOException -> 0x01f5 }
                                    projectapp.StudentTestMarksActivity r0 = projectapp.StudentTestMarksActivity.this     // Catch:{ IOException -> 0x01f5 }
                                    androidx.appcompat.widget.AppCompatEditText r0 = r0.total     // Catch:{ IOException -> 0x01f5 }
                                    r0.setText(r1)     // Catch:{ IOException -> 0x01f5 }
                                    org.apache.poi.hssf.usermodel.HSSFCell r9 = r2.createCell((int) r9)     // Catch:{ IOException -> 0x01f5 }
                                    r9.setCellValue((java.lang.String) r1)     // Catch:{ IOException -> 0x01f5 }
                                    goto L_0x0185
                                L_0x0174:
                                    projectapp.StudentTestMarksActivity$4 r0 = projectapp.StudentTestMarksActivity.C19674.this     // Catch:{ IOException -> 0x01f5 }
                                    projectapp.StudentTestMarksActivity r0 = projectapp.StudentTestMarksActivity.this     // Catch:{ IOException -> 0x01f5 }
                                    androidx.appcompat.widget.AppCompatEditText r0 = r0.total     // Catch:{ IOException -> 0x01f5 }
                                    org.apache.poi.hssf.usermodel.HSSFCell r9 = r2.getCell((int) r9)     // Catch:{ IOException -> 0x01f5 }
                                    java.lang.String r9 = r9.toString()     // Catch:{ IOException -> 0x01f5 }
                                    r0.setText(r9)     // Catch:{ IOException -> 0x01f5 }
                                L_0x0185:
                                    projectapp.StudentTestMarksActivity$4 r9 = projectapp.StudentTestMarksActivity.C19674.this     // Catch:{ IOException -> 0x01f5 }
                                    projectapp.StudentTestMarksActivity r9 = projectapp.StudentTestMarksActivity.this     // Catch:{ IOException -> 0x01f5 }
                                    androidx.appcompat.widget.AppCompatEditText r9 = r9.group     // Catch:{ IOException -> 0x01f5 }
                                    android.text.Editable r9 = r9.getText()     // Catch:{ IOException -> 0x01f5 }
                                    java.lang.String r9 = r9.toString()     // Catch:{ IOException -> 0x01f5 }
                                    float r9 = java.lang.Float.parseFloat(r9)     // Catch:{ IOException -> 0x01f5 }
                                    projectapp.StudentTestMarksActivity$4 r0 = projectapp.StudentTestMarksActivity.C19674.this     // Catch:{ IOException -> 0x01f5 }
                                    projectapp.StudentTestMarksActivity r0 = projectapp.StudentTestMarksActivity.this     // Catch:{ IOException -> 0x01f5 }
                                    androidx.appcompat.widget.AppCompatEditText r0 = r0.individual     // Catch:{ IOException -> 0x01f5 }
                                    android.text.Editable r0 = r0.getText()     // Catch:{ IOException -> 0x01f5 }
                                    java.lang.String r0 = r0.toString()     // Catch:{ IOException -> 0x01f5 }
                                    float r0 = java.lang.Float.parseFloat(r0)     // Catch:{ IOException -> 0x01f5 }
                                    projectapp.StudentTestMarksActivity$4 r1 = projectapp.StudentTestMarksActivity.C19674.this     // Catch:{ IOException -> 0x01f5 }
                                    projectapp.StudentTestMarksActivity r1 = projectapp.StudentTestMarksActivity.this     // Catch:{ IOException -> 0x01f5 }
                                    androidx.appcompat.widget.AppCompatEditText r1 = r1.micro     // Catch:{ IOException -> 0x01f5 }
                                    float r9 = r9 + r0
                                    java.lang.String r9 = java.lang.String.valueOf(r9)     // Catch:{ IOException -> 0x01f5 }
                                    r1.setText(r9)     // Catch:{ IOException -> 0x01f5 }
                                    goto L_0x01fe
                                L_0x01b8:
                                    int r1 = r1 + 1
                                    goto L_0x0024
                                L_0x01bc:
                                    android.app.ProgressDialog r9 = r5     // Catch:{ IOException -> 0x01f5 }
                                    r9.dismiss()     // Catch:{ IOException -> 0x01f5 }
                                    androidx.appcompat.app.AlertDialog$Builder r9 = new androidx.appcompat.app.AlertDialog$Builder     // Catch:{ IOException -> 0x01f5 }
                                    projectapp.StudentTestMarksActivity$4 r0 = projectapp.StudentTestMarksActivity.C19674.this     // Catch:{ IOException -> 0x01f5 }
                                    projectapp.StudentTestMarksActivity r0 = projectapp.StudentTestMarksActivity.this     // Catch:{ IOException -> 0x01f5 }
                                    r9.<init>(r0)     // Catch:{ IOException -> 0x01f5 }
                                    java.lang.String r0 = "Alert"
                                    r9.setTitle((java.lang.CharSequence) r0)     // Catch:{ IOException -> 0x01f5 }
                                    java.lang.String r0 = "Test Marks of Current Subject Not Found.\nPossible Reasons are:\n1. New Subjects Added and Test Marks Sheet Not Updated.\nContact H.O.D"
                                    r9.setMessage((java.lang.CharSequence) r0)     // Catch:{ IOException -> 0x01f5 }
                                    java.lang.String r0 = "Ok"
                                    r1 = 0
                                    r9.setPositiveButton((java.lang.CharSequence) r0, (android.content.DialogInterface.OnClickListener) r1)     // Catch:{ IOException -> 0x01f5 }
                                    r9.create()     // Catch:{ IOException -> 0x01f5 }
                                    projectapp.StudentTestMarksActivity$4 r0 = projectapp.StudentTestMarksActivity.C19674.this     // Catch:{ IOException -> 0x01f5 }
                                    projectapp.StudentTestMarksActivity r0 = projectapp.StudentTestMarksActivity.this     // Catch:{ IOException -> 0x01f5 }
                                    androidx.lifecycle.Lifecycle r0 = r0.getLifecycle()     // Catch:{ IOException -> 0x01f5 }
                                    androidx.lifecycle.Lifecycle$State r0 = r0.getCurrentState()     // Catch:{ IOException -> 0x01f5 }
                                    androidx.lifecycle.Lifecycle$State r1 = androidx.lifecycle.Lifecycle.State.CREATED     // Catch:{ IOException -> 0x01f5 }
                                    boolean r0 = r0.isAtLeast(r1)     // Catch:{ IOException -> 0x01f5 }
                                    if (r0 == 0) goto L_0x01fe
                                    r9.show()     // Catch:{ IOException -> 0x01f5 }
                                    goto L_0x01fe
                                L_0x01f5:
                                    r9 = move-exception
                                    android.app.ProgressDialog r0 = r5
                                    r0.dismiss()
                                    r9.printStackTrace()
                                L_0x01fe:
                                    android.app.ProgressDialog r9 = r5
                                    r9.dismiss()
                                    return
                                */
                                throw new UnsupportedOperationException("Method not decompiled: projectapp.StudentTestMarksActivity.C19674.C19692.onSuccess(com.google.firebase.storage.FileDownloadTask$TaskSnapshot):void");
                            }
                        }).addOnFailureListener((OnFailureListener) new OnFailureListener() {
                            public void onFailure(@NonNull Exception exc) {
                                progressDialog.dismiss();
                                Toast.makeText(StudentTestMarksActivity.this, "F", Toast.LENGTH_SHORT).show();
                            }
                        });
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
        });
    }

    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }
}
