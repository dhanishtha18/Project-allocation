package org.projectapp;

import android.app.ProgressDialog;
import android.content.DialogInterface;
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
import androidx.appcompat.widget.AppCompatSpinner;
import androidx.appcompat.widget.AppCompatTextView;
import androidx.lifecycle.Lifecycle;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FileDownloadTask;
import com.google.firebase.storage.FirebaseStorage;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Iterator;
import java.util.List;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.projectapp.Adapter.TestAdapter;
import org.projectapp.Model.Document;
import org.projectapp.Model.Hod;
import org.projectapp.Model.Staff;
import org.projectapp.Model.Test;

public class TestMarksActivity extends AppCompatActivity {
    RecyclerView.Adapter adapter;
    String department;
    Document document = null;
    String id;
    LinearLayoutManager layoutManager;
    String semester = null;
    RecyclerView student_list;
    AppCompatSpinner student_subject;
    String subject = null;
    ArrayList<String> subjects;
    AppCompatTextView title;
    List<Test> uploads;
    String year = null;

    /* access modifiers changed from: protected */
    public void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        setContentView((int) R.layout.activity_test_marks);
        Calendar instance = Calendar.getInstance();
        this.subjects = new ArrayList<>();
        this.subjects.add("Select Subject");
        this.department = getIntent().getStringExtra("department");
        this.id = getIntent().getStringExtra("id");
        this.layoutManager = new LinearLayoutManager(this);
        this.student_list = (RecyclerView) findViewById(R.id.student_list);
        this.student_list.setHasFixedSize(true);
        this.student_list.setLayoutManager(this.layoutManager);
        this.student_subject = (AppCompatSpinner) findViewById(R.id.student_subject);
        this.title = (AppCompatTextView) findViewById(R.id.title);
        this.title.setText("Test Marks");
        if (instance.get(2) < instance.get(5)) {
            this.year = (instance.get(1) - 1) + "-" + instance.get(1);
        } else if (instance.get(2) == 11) {
            this.year = instance.get(1) + "-" + (instance.get(1) + 1);
        } else {
            this.year = instance.get(1) + "-" + (instance.get(1) + 1);
        }
        this.uploads = new ArrayList();
        this.student_subject.setAdapter((SpinnerAdapter) new ArrayAdapter(this, R.layout.spinner_dialog, this.subjects));
        if (getIntent().getStringExtra("loginas").equals("lecturer")) {
            FirebaseDatabase instance2 = FirebaseDatabase.getInstance();
            instance2.getReference("Staff/" + this.department).child(this.id).addListenerForSingleValueEvent(new ValueEventListener() {
                public void onCancelled(@NonNull DatabaseError databaseError) {
                }

                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    Staff staff = (Staff) dataSnapshot.getValue(Staff.class);
                    if (!staff.getSubjects().isEmpty()) {
                        Iterator<String> it = staff.getSubjects().iterator();
                        while (it.hasNext()) {
                            String next = it.next();
                            if (next.contains("TH")) {
                                TestMarksActivity.this.subjects.add(next);
                            }
                        }
                    }
                    AppCompatSpinner appCompatSpinner = TestMarksActivity.this.student_subject;
                    TestMarksActivity testMarksActivity = TestMarksActivity.this;
                    appCompatSpinner.setAdapter((SpinnerAdapter) new ArrayAdapter(testMarksActivity, R.layout.spinner_dialog, testMarksActivity.subjects));
                }
            });
        } else {
            FirebaseDatabase instance3 = FirebaseDatabase.getInstance();
            instance3.getReference("HODs/" + this.department).child(this.id).addListenerForSingleValueEvent(new ValueEventListener() {
                public void onCancelled(@NonNull DatabaseError databaseError) {
                }

                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    Hod hod = (Hod) dataSnapshot.getValue(Hod.class);
                    if (!hod.getSubjects().isEmpty()) {
                        Iterator<String> it = hod.getSubjects().iterator();
                        while (it.hasNext()) {
                            String next = it.next();
                            if (next.contains("TH")) {
                                TestMarksActivity.this.subjects.add(next);
                            }
                        }
                    }
                    AppCompatSpinner appCompatSpinner = TestMarksActivity.this.student_subject;
                    TestMarksActivity testMarksActivity = TestMarksActivity.this;
                    appCompatSpinner.setAdapter((SpinnerAdapter) new ArrayAdapter(testMarksActivity, R.layout.spinner_dialog, testMarksActivity.subjects));
                }
            });
        }
        this.student_subject.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            public void onNothingSelected(AdapterView<?> adapterView) {
            }

            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long j) {
                TestMarksActivity.this.uploads.clear();
                if (!adapterView.getItemAtPosition(i).toString().equals("Select Subject")) {
                    String[] split = adapterView.getItemAtPosition(i).toString().split(" ");
                    TestMarksActivity testMarksActivity = TestMarksActivity.this;
                    testMarksActivity.semester = split[split.length - 1];
                    testMarksActivity.subject = split[0] + " " + split[1] + " " + split[2];
                    TestMarksActivity testMarksActivity2 = TestMarksActivity.this;
                    testMarksActivity2.startTest(testMarksActivity2.semester, TestMarksActivity.this.subject, TestMarksActivity.this.year, TestMarksActivity.this.department);
                }
            }
        });
    }

    /* access modifiers changed from: private */
    public void startTest(final String semester, final String sheetName, final String year, String department) {
        FirebaseDatabase instance = FirebaseDatabase.getInstance();
        final String str5 = semester;
        final String str6 = year;
        final String str8 = department;
        instance.getReference(department + "/Unit Test/" + semester + " " + year).addListenerForSingleValueEvent(new ValueEventListener() {
            public void onCancelled(@NonNull DatabaseError databaseError) {
            }

            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.getChildrenCount() == 0) {
                    AlertDialog.Builder builder = new AlertDialog.Builder(TestMarksActivity.this);
                    builder.setMessage((CharSequence) "Test Marks sheet not uploaded yet.");
                    builder.setTitle((CharSequence) "Alert");
                    builder.setPositiveButton((CharSequence) "Ok", (DialogInterface.OnClickListener) new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialogInterface, int i) {
                            TestMarksActivity.this.onBackPressed();
                        }
                    });
                    builder.create();
                    if (TestMarksActivity.this.getLifecycle().getCurrentState().isAtLeast(Lifecycle.State.CREATED)) {
                        builder.show();
                        return;
                    }
                    return;
                }
                final ProgressDialog progressDialog = new ProgressDialog(TestMarksActivity.this);
                progressDialog.setCancelable(false);
                progressDialog.setMessage("Fetching students marks...");
                progressDialog.show();
                TestMarksActivity.this.document = (Document) dataSnapshot.getValue(Document.class);
                try {
                    File file = new File(Environment.getExternalStorageDirectory().getPath() + "/rait/Document");
                    if (!file.exists()) {
                        file.mkdirs();
                    }
                    final File file2 = new File(Environment.getExternalStorageDirectory().getPath() + "/rait/Document", str5 + " Unit Test " + str6 + ".xls");
                    file2.createNewFile();
                    FirebaseStorage.getInstance().getReferenceFromUrl(TestMarksActivity.this.document.getUrl()).getFile(file2).addOnSuccessListener((OnSuccessListener) new OnSuccessListener<FileDownloadTask.TaskSnapshot>() {
                        public void onSuccess(FileDownloadTask.TaskSnapshot taskSnapshot) {
                            String str;
                            String str2;
                            String str3;
                            String str4;
                            String str5;
                            String str6;
                            String str7;
                            try {
                                HSSFSheet sheet = new HSSFWorkbook((InputStream) new FileInputStream(file2)).getSheet(sheetName);
                                if (sheet != null) {
                                    if (sheet.getPhysicalNumberOfRows() != 0) {
                                        for (int i = 3; i <= sheet.getLastRowNum(); i++) {
                                            HSSFRow row = sheet.getRow(i);
                                            if (!(row == null || row.getCell(1) == null || row.getCell(1).toString().equals(""))) {
                                                row.getCell(1).toString();
                                                String format = String.format("%.0f", new Object[]{Double.valueOf(row.getCell(0).getNumericCellValue())});
                                                String format2 = String.format("%.0f", new Object[]{Double.valueOf(row.getCell(2).getNumericCellValue())});
                                                if (row.getCell(3) == null) {
                                                    row.createCell(3).setCellValue("0");
                                                    str = "0";
                                                } else {
                                                    str = row.getCell(3).toString();
                                                }
                                                if (row.getCell(4) == null) {
                                                    row.createCell(4).setCellValue("0");
                                                    str2 = "0";
                                                } else {
                                                    str2 = row.getCell(4).toString();
                                                }
                                                if (row.getCell(5) == null) {
                                                    row.createCell(5).setCellValue("0");
                                                    str3 = "0";
                                                } else {
                                                    str3 = row.getCell(5).toString();
                                                }
                                                if (row.getCell(6) == null) {
                                                    String valueOf = String.valueOf((Float.parseFloat(str2.trim()) + ((float) Integer.parseInt(str3.trim()))) / 2.0f);
                                                    row.createCell(6).setCellValue("0");
                                                    str4 = valueOf;
                                                } else {
                                                    str4 = row.getCell(6).toString();
                                                }
                                                if (row.getCell(7) == null) {
                                                    row.createCell(7).setCellValue("0");
                                                    str5 = "0";
                                                } else {
                                                    str5 = row.getCell(7).toString();
                                                }
                                                if (row.getCell(8) == null) {
                                                    row.createCell(8).setCellValue("0");
                                                    str6 = "0";
                                                } else {
                                                    str6 = row.getCell(8).toString();
                                                }
                                                if (row.getCell(9) == null) {
                                                    row.createCell(9).setCellValue("0");
                                                    str7 = "0";
                                                } else {
                                                    str7 = row.getCell(9).toString();
                                                }
                                                TestMarksActivity.this.uploads.add(new Test(format, row.getCell(1).toString(), format2, str, str2, str3, str4, str5, str6, str7));
                                            }
                                        }
                                        progressDialog.dismiss();
                                        if (TestMarksActivity.this.getLifecycle().getCurrentState().isAtLeast(Lifecycle.State.INITIALIZED)) {
                                            TestMarksActivity.this.adapter = new TestAdapter(TestMarksActivity.this.student_list, TestMarksActivity.this, TestMarksActivity.this.uploads, file2, sheetName, TestMarksActivity.this.document.getUrl(), str8 + "/Unit Test/" + semester + " " + year);
                                            TestMarksActivity.this.student_list.setAdapter(TestMarksActivity.this.adapter);
                                            return;
                                        }
                                        return;
                                    }
                                }
                                progressDialog.dismiss();
                                AlertDialog.Builder builder = new AlertDialog.Builder(TestMarksActivity.this);
                                builder.setTitle((CharSequence) "Alert");
                                builder.setMessage((CharSequence) "Test Marks of Current Subject Not Found.\nPossible Reasons are:\n1. New Subjects Added and Test Marks Sheet Not Updated.\nContact H.O.D");
                                builder.setPositiveButton((CharSequence) "Ok", (DialogInterface.OnClickListener) null);
                                builder.create();
                                if (TestMarksActivity.this.getLifecycle().getCurrentState().isAtLeast(Lifecycle.State.CREATED)) {
                                    builder.show();
                                }
                            } catch (IOException e) {
                                progressDialog.dismiss();
                                e.printStackTrace();
                            }
                        }
                    }).addOnFailureListener((OnFailureListener) new OnFailureListener() {
                        public void onFailure(@NonNull Exception exc) {
                            progressDialog.dismiss();
                            Toast.makeText(TestMarksActivity.this, "F", Toast.LENGTH_SHORT).show();
                        }
                    });
                } catch (IOException e) {
                    progressDialog.dismiss();
                    e.printStackTrace();
                }
            }
        });
    }

    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }
}
