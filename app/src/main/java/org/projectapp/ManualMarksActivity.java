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
import org.projectapp.Adapter.ManualAdapter;
import org.projectapp.Model.Document;
import org.projectapp.Model.Hod;
import org.projectapp.Model.Staff;
import org.projectapp.Model.Student;

public class ManualMarksActivity extends AppCompatActivity {
    RecyclerView.Adapter adapter;
    Calendar cal;
    String department;

    /* renamed from: id */
    String f443id;
    LinearLayoutManager layoutManager;
    String loginas;
    String semester;
    RecyclerView student_list;
    AppCompatSpinner student_subject;
    String subject;
    ArrayList<String> subjects;
    AppCompatTextView title;
    List<Student> uploads;
    String year;

    /* access modifiers changed from: protected */
    public void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        setContentView((int) R.layout.activity_manual_marks);
        this.department = getIntent().getStringExtra("department");
        this.loginas = getIntent().getStringExtra("loginas");
        this.f443id = getIntent().getStringExtra("id");
        this.student_subject = (AppCompatSpinner) findViewById(R.id.student_subject);
        this.title = (AppCompatTextView) findViewById(R.id.title);
        this.title.setText("Manual Marks");
        this.layoutManager = new LinearLayoutManager(this);
        this.student_list = (RecyclerView) findViewById(R.id.student_list);
        this.student_list.setHasFixedSize(true);
        this.student_list.setLayoutManager(this.layoutManager);
        this.uploads = new ArrayList();
        this.subjects = new ArrayList<>();
        this.subjects.add("Select Subject");
        this.cal = Calendar.getInstance();
        if (this.cal.get(2) < this.cal.get(5)) {
            this.year = (this.cal.get(1) - 1) + "-" + this.cal.get(1);
        } else if (this.cal.get(2) == 11) {
            this.year = this.cal.get(1) + "-" + (this.cal.get(1) + 1);
        } else {
            this.year = this.cal.get(1) + "-" + (this.cal.get(1) + 1);
        }
        if (this.loginas.equals("hod")) {
            FirebaseDatabase.getInstance().getReference("HODs").child(this.department).child(this.f443id).addListenerForSingleValueEvent(new ValueEventListener() {
                public void onCancelled(@NonNull DatabaseError databaseError) {
                }

                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    Hod hod = (Hod) dataSnapshot.getValue(Hod.class);
                    if (!hod.getSubjects().isEmpty()) {
                        Iterator<String> it = hod.getSubjects().iterator();
                        while (it.hasNext()) {
                            String next = it.next();
                            if (next.contains("PA")) {
                                ManualMarksActivity.this.subjects.add(next);
                            }
                        }
                    }
                    AppCompatSpinner appCompatSpinner = ManualMarksActivity.this.student_subject;
                    ManualMarksActivity manualMarksActivity = ManualMarksActivity.this;
                    appCompatSpinner.setAdapter((SpinnerAdapter) new ArrayAdapter(manualMarksActivity, R.layout.spinner_dialog, manualMarksActivity.subjects));
                }
            });
        } else {
            FirebaseDatabase.getInstance().getReference("Staff").child(this.department).child(this.f443id).addListenerForSingleValueEvent(new ValueEventListener() {
                public void onCancelled(@NonNull DatabaseError databaseError) {
                }

                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    Staff staff = (Staff) dataSnapshot.getValue(Staff.class);
                    if (!staff.getSubjects().isEmpty()) {
                        Iterator<String> it = staff.getSubjects().iterator();
                        while (it.hasNext()) {
                            String next = it.next();
                            if (next.contains("PA")) {
                                ManualMarksActivity.this.subjects.add(next);
                            }
                        }
                    }
                    AppCompatSpinner appCompatSpinner = ManualMarksActivity.this.student_subject;
                    ManualMarksActivity manualMarksActivity = ManualMarksActivity.this;
                    appCompatSpinner.setAdapter((SpinnerAdapter) new ArrayAdapter(manualMarksActivity, R.layout.spinner_dialog, manualMarksActivity.subjects));
                }
            });
        }
        this.student_subject.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            public void onNothingSelected(AdapterView<?> adapterView) {
            }

            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long j) {
                ManualMarksActivity.this.subject = adapterView.getItemAtPosition(i).toString().trim();
                if (adapterView.getItemAtPosition(i).toString().equals("subject")) {
                    AlertDialog.Builder builder = new AlertDialog.Builder(ManualMarksActivity.this);
                    builder.setMessage((CharSequence) "You dont have subjects.");
                    builder.setTitle((CharSequence) "Manaual marks");
                    builder.setPositiveButton((CharSequence) "Ok", (DialogInterface.OnClickListener) new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialogInterface, int i) {
                            ManualMarksActivity.this.onBackPressed();
                        }
                    });
                    builder.create();
                    if (ManualMarksActivity.this.getLifecycle().getCurrentState().isAtLeast(Lifecycle.State.CREATED)) {
                        builder.show();
                    }
                } else if (!adapterView.getItemAtPosition(i).toString().equals("Select Subject")) {
                    String[] split = adapterView.getItemAtPosition(i).toString().split(" ");
                    ManualMarksActivity manualMarksActivity = ManualMarksActivity.this;
                    manualMarksActivity.semester = split[split.length - 1];
                    manualMarksActivity.subject = split[0] + " " + split[1] + " " + split[2];
                    ManualMarksActivity manualMarksActivity2 = ManualMarksActivity.this;
                    manualMarksActivity2.Manual(manualMarksActivity2.semester, ManualMarksActivity.this.subject, ManualMarksActivity.this.year, ManualMarksActivity.this.department);
                }
            }
        });
    }

    /* access modifiers changed from: private */
    public void Manual(String str, String str2, String str3, String str4) {
        FirebaseDatabase instance = FirebaseDatabase.getInstance();
        final String str5 = str;
        final String str6 = str3;
        final String str7 = str2;
        final String str8 = str4;
        instance.getReference(str4 + "/Manual Marks/" + str + " " + str3).addListenerForSingleValueEvent(new ValueEventListener() {
            public void onCancelled(@NonNull DatabaseError databaseError) {
            }

            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                final Document document = (Document) dataSnapshot.getValue(Document.class);
                try {
                    File file = new File(Environment.getExternalStorageDirectory().getPath() + "/rait/Document");
                    if (!file.exists()) {
                        file.mkdirs();
                    }
                    final File file2 = new File(Environment.getExternalStorageDirectory().getPath() + "/rait/Document", str5 + " Manual Marks " + str6 + ".xls");
                    file2.createNewFile();
                    if (dataSnapshot.getChildrenCount() == 0) {
                        AlertDialog.Builder builder = new AlertDialog.Builder(ManualMarksActivity.this);
                        builder.setMessage((CharSequence) "Manual Marks Sheet File not Uploaded yet");
                        builder.setTitle((CharSequence) "Manual Marks");
                        builder.setPositiveButton((CharSequence) "Ok", (DialogInterface.OnClickListener) new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialogInterface, int i) {
                                ManualMarksActivity.this.onBackPressed();
                            }
                        });
                        builder.create();
                        if (ManualMarksActivity.this.getLifecycle().getCurrentState().isAtLeast(Lifecycle.State.CREATED)) {
                            builder.show();
                            return;
                        }
                        return;
                    }
                    final ProgressDialog progressDialog = new ProgressDialog(ManualMarksActivity.this);
                    progressDialog.setCancelable(false);
                    progressDialog.setMessage("Fetching students data");
                    progressDialog.show();
                    FirebaseStorage.getInstance().getReferenceFromUrl(document.getUrl()).getFile(file2).addOnSuccessListener((OnSuccessListener) new OnSuccessListener<FileDownloadTask.TaskSnapshot>() {
                        public void onSuccess(FileDownloadTask.TaskSnapshot taskSnapshot) {
                            try {
                                HSSFSheet sheet = new HSSFWorkbook((InputStream) new FileInputStream(file2)).getSheet(str7);
                                if (sheet != null) {
                                    if (sheet.getPhysicalNumberOfRows() != 0) {
                                        for (int i = 2; i <= sheet.getLastRowNum(); i++) {
                                            HSSFRow row = sheet.getRow(i);
                                            if (!(row == null || ((row.getCell(0) == null || row.getCell(1) == null) && row.getCell(3) == null))) {
                                                String format = String.format("%.0f", new Object[]{Double.valueOf(row.getCell(0).getNumericCellValue())});
                                                String format2 = String.format("%.0f", new Object[]{Double.valueOf(row.getCell(1).getNumericCellValue())});
                                                String hSSFCell = row.getCell(3).toString();
                                                Student student = new Student();
                                                student.setName(hSSFCell);
                                                student.setRoll_no(format);
                                                student.setEnrollment(format2);
                                                if (!format.equals("") && !hSSFCell.equals("") && !format2.equals("")) {
                                                    ManualMarksActivity.this.uploads.add(student);
                                                }
                                            }
                                        }
                                        progressDialog.dismiss();
                                        if (ManualMarksActivity.this.getLifecycle().getCurrentState().isAtLeast(Lifecycle.State.INITIALIZED)) {
                                            ManualMarksActivity manualMarksActivity = ManualMarksActivity.this;
                                            ManualMarksActivity manualMarksActivity2 = ManualMarksActivity.this;
                                            manualMarksActivity.adapter = new ManualAdapter(manualMarksActivity2, str8 + "/Manual Marks/" + str5 + " " + str6, ManualMarksActivity.this.uploads, file2, str7, str5, str6, str8, document.getUrl());
                                            ManualMarksActivity.this.student_list.setAdapter(ManualMarksActivity.this.adapter);
                                            return;
                                        }
                                        return;
                                    }
                                }
                                progressDialog.dismiss();
                                AlertDialog.Builder builder = new AlertDialog.Builder(ManualMarksActivity.this);
                                builder.setTitle((CharSequence) "Alert");
                                builder.setMessage((CharSequence) "Manual Marks of Current Subject Not Found.\nPossible Reasons are:\n1. New Subjects Added and Manual Marks Sheet Not Updated.\nContact H.O.D");
                                builder.setPositiveButton((CharSequence) "Ok", (DialogInterface.OnClickListener) null);
                                builder.create();
                                if (ManualMarksActivity.this.getLifecycle().getCurrentState().isAtLeast(Lifecycle.State.CREATED)) {
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
                            Toast.makeText(ManualMarksActivity.this, "F", Toast.LENGTH_SHORT).show();
                        }
                    });
                } catch (IOException e) {
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
