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
import org.projectapp.Adapter.AttendenceAdapter;
import org.projectapp.Model.Document;
import org.projectapp.Model.Hod;
import org.projectapp.Model.Staff;
import org.projectapp.Model.Student;

public class TheoryAttendenceActivity extends AppCompatActivity {
    RecyclerView.Adapter adapter;
    String department;
    AppCompatSpinner from;
    String fromTime = null;

    /* renamed from: id */
    String f486id;
    LinearLayoutManager layoutManager;
    String semester = null;
    RecyclerView student_list;
    AppCompatSpinner student_subject;
    String subject = null;
    ArrayList<String> subjects;
    AppCompatTextView title;

    /* renamed from: to */
    AppCompatSpinner to;
    String toTime = null;
    List<Student> uploads;
    String year = null;

    /* access modifiers changed from: protected */
    public void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        setContentView((int) R.layout.activity_theory_attendence);
        Calendar instance = Calendar.getInstance();
        this.subjects = new ArrayList<>();
        this.subjects.add("Select Subject");
        this.department = getIntent().getStringExtra("department");
        this.f486id = getIntent().getStringExtra("id");
        this.layoutManager = new LinearLayoutManager(this);
        this.student_list = (RecyclerView) findViewById(R.id.student_list);
        this.student_list.setHasFixedSize(true);
        this.student_list.setLayoutManager(this.layoutManager);
        this.student_subject = (AppCompatSpinner) findViewById(R.id.student_subject);
        this.to = (AppCompatSpinner) findViewById(R.id.to);
        this.from = (AppCompatSpinner) findViewById(R.id.from);
        this.title = (AppCompatTextView) findViewById(R.id.title);
        this.title.setText("Theory Attendence");
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
            instance2.getReference("Staff/" + this.department).child(this.f486id).addListenerForSingleValueEvent(new ValueEventListener() {
                public void onCancelled(@NonNull DatabaseError databaseError) {
                }

                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    Staff staff = (Staff) dataSnapshot.getValue(Staff.class);
                    if (!staff.getSubjects().isEmpty()) {
                        Iterator<String> it = staff.getSubjects().iterator();
                        while (it.hasNext()) {
                            String next = it.next();
                            if (next.contains("TH")) {
                                TheoryAttendenceActivity.this.subjects.add(next);
                            }
                        }
                    }
                    AppCompatSpinner appCompatSpinner = TheoryAttendenceActivity.this.student_subject;
                    TheoryAttendenceActivity theoryAttendenceActivity = TheoryAttendenceActivity.this;
                    appCompatSpinner.setAdapter((SpinnerAdapter) new ArrayAdapter(theoryAttendenceActivity, R.layout.spinner_dialog, theoryAttendenceActivity.subjects));
                }
            });
        } else {
            FirebaseDatabase instance3 = FirebaseDatabase.getInstance();
            instance3.getReference("HODs/" + this.department).child(this.f486id).addListenerForSingleValueEvent(new ValueEventListener() {
                public void onCancelled(@NonNull DatabaseError databaseError) {
                }

                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    Hod hod = (Hod) dataSnapshot.getValue(Hod.class);
                    if (!hod.getSubjects().isEmpty()) {
                        Iterator<String> it = hod.getSubjects().iterator();
                        while (it.hasNext()) {
                            String next = it.next();
                            if (next.contains("TH")) {
                                TheoryAttendenceActivity.this.subjects.add(next);
                            }
                        }
                    }
                    AppCompatSpinner appCompatSpinner = TheoryAttendenceActivity.this.student_subject;
                    TheoryAttendenceActivity theoryAttendenceActivity = TheoryAttendenceActivity.this;
                    appCompatSpinner.setAdapter((SpinnerAdapter) new ArrayAdapter(theoryAttendenceActivity, R.layout.spinner_dialog, theoryAttendenceActivity.subjects));
                }
            });
        }
        this.student_subject.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            public void onNothingSelected(AdapterView<?> adapterView) {
            }

            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long j) {
                TheoryAttendenceActivity.this.uploads.clear();
                if (adapterView.getItemAtPosition(i).toString().equals("subject")) {
                    AlertDialog.Builder builder = new AlertDialog.Builder(TheoryAttendenceActivity.this);
                    builder.setMessage((CharSequence) "You dont have subjects to take attendence");
                    builder.setTitle((CharSequence) "Attendence");
                    builder.setPositiveButton((CharSequence) "Ok", (DialogInterface.OnClickListener) new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialogInterface, int i) {
                            TheoryAttendenceActivity.this.onBackPressed();
                        }
                    });
                    builder.create();
                    if (TheoryAttendenceActivity.this.getLifecycle().getCurrentState().isAtLeast(Lifecycle.State.CREATED)) {
                        builder.show();
                    }
                } else if (!adapterView.getItemAtPosition(i).toString().equals("Select Subject")) {
                    String[] split = adapterView.getItemAtPosition(i).toString().split(" ");
                    TheoryAttendenceActivity theoryAttendenceActivity = TheoryAttendenceActivity.this;
                    theoryAttendenceActivity.semester = split[split.length - 1];
                    theoryAttendenceActivity.subject = split[0] + " " + split[1] + " " + split[2];
                    if (TheoryAttendenceActivity.this.toTime != null && TheoryAttendenceActivity.this.fromTime != null) {
                        TheoryAttendenceActivity theoryAttendenceActivity2 = TheoryAttendenceActivity.this;
                        theoryAttendenceActivity2.startAttendence(theoryAttendenceActivity2.semester, TheoryAttendenceActivity.this.subject, TheoryAttendenceActivity.this.year, TheoryAttendenceActivity.this.department, TheoryAttendenceActivity.this.toTime, TheoryAttendenceActivity.this.fromTime);
                    }
                }
            }
        });
        this.to.setAdapter((SpinnerAdapter) new ArrayAdapter(this, R.layout.spinner_dialog, new String[]{"End Time", "10:00am", "11:00am", "12:00pm", "1:00pm", "2:30pm", "3:30pm", "4:30pm", "5:30pm"}));
        this.from.setAdapter((SpinnerAdapter) new ArrayAdapter(this, R.layout.spinner_dialog, new String[]{"Start Time", "9:00am", "10:00am", "11:00am", "12:00pm", "1:30pm", "2:30pm", "3:30pm", "4:30pm"}));
        this.to.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            public void onNothingSelected(AdapterView<?> adapterView) {
            }

            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long j) {
                TheoryAttendenceActivity.this.uploads.clear();
                if (adapterView.getItemAtPosition(i).toString().equals("End Time")) {
                    TheoryAttendenceActivity.this.toTime = null;
                    return;
                }
                TheoryAttendenceActivity.this.toTime = adapterView.getItemAtPosition(i).toString();
                if (TheoryAttendenceActivity.this.subject != null && TheoryAttendenceActivity.this.fromTime != null && TheoryAttendenceActivity.this.semester != null) {
                    TheoryAttendenceActivity theoryAttendenceActivity = TheoryAttendenceActivity.this;
                    theoryAttendenceActivity.startAttendence(theoryAttendenceActivity.semester, TheoryAttendenceActivity.this.subject, TheoryAttendenceActivity.this.year, TheoryAttendenceActivity.this.department, TheoryAttendenceActivity.this.toTime, TheoryAttendenceActivity.this.fromTime);
                }
            }
        });
        this.from.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            public void onNothingSelected(AdapterView<?> adapterView) {
            }

            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long j) {
                TheoryAttendenceActivity.this.uploads.clear();
                if (adapterView.getItemAtPosition(i).toString().equals("Start Time")) {
                    TheoryAttendenceActivity.this.fromTime = null;
                    return;
                }
                TheoryAttendenceActivity.this.fromTime = adapterView.getItemAtPosition(i).toString();
                if (TheoryAttendenceActivity.this.subject != null && TheoryAttendenceActivity.this.toTime != null && TheoryAttendenceActivity.this.semester != null) {
                    TheoryAttendenceActivity theoryAttendenceActivity = TheoryAttendenceActivity.this;
                    theoryAttendenceActivity.startAttendence(theoryAttendenceActivity.semester, TheoryAttendenceActivity.this.subject, TheoryAttendenceActivity.this.year, TheoryAttendenceActivity.this.department, TheoryAttendenceActivity.this.toTime, TheoryAttendenceActivity.this.fromTime);
                }
            }
        });
    }

    /* access modifiers changed from: private */
    public void startAttendence(String str, String str2, String str3, String str4, String str5, String str6) {
        FirebaseDatabase instance = FirebaseDatabase.getInstance();
        StringBuilder sb = new StringBuilder();
        final String str7 = str4;
        sb.append(str4);
        sb.append("/Theory Attendence/");
        final String str8 = str;
        sb.append(str);
        sb.append(" ");
        final String str9 = str3;
        sb.append(str3);
        final String str10 = str2;
        final String str11 = str5;
        final String str12 = str6;
        instance.getReference(sb.toString()).addListenerForSingleValueEvent(new ValueEventListener() {
            public void onCancelled(@NonNull DatabaseError databaseError) {
            }

            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                final Document document = (Document) dataSnapshot.getValue(Document.class);
                try {
                    File file = new File(Environment.getExternalStorageDirectory().getPath() + "/rait/Document");
                    if (!file.exists()) {
                        file.mkdirs();
                    }
                    final File file2 = new File(Environment.getExternalStorageDirectory().getPath() + "/rait/Document", str8 + " Theory Attendence " + str9 + ".xls");
                    file2.createNewFile();
                    if (dataSnapshot.getChildrenCount() == 0) {
                        AlertDialog.Builder builder = new AlertDialog.Builder(TheoryAttendenceActivity.this);
                        builder.setMessage((CharSequence) "Theory Attendence File not Uploaded yet");
                        builder.setTitle((CharSequence) "Attendence");
                        builder.setPositiveButton((CharSequence) "Ok", (DialogInterface.OnClickListener) new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialogInterface, int i) {
                                TheoryAttendenceActivity.this.onBackPressed();
                            }
                        });
                        builder.create();
                        if (TheoryAttendenceActivity.this.getLifecycle().getCurrentState().isAtLeast(Lifecycle.State.CREATED)) {
                            builder.show();
                            return;
                        }
                        return;
                    }
                    final ProgressDialog progressDialog = new ProgressDialog(TheoryAttendenceActivity.this);
                    progressDialog.setCancelable(false);
                    progressDialog.setMessage("Fetching students data");
                    progressDialog.show();
                    FirebaseStorage.getInstance().getReferenceFromUrl(document.getUrl()).getFile(file2).addOnSuccessListener((OnSuccessListener) new OnSuccessListener<FileDownloadTask.TaskSnapshot>() {
                        public void onSuccess(FileDownloadTask.TaskSnapshot taskSnapshot) {
                            try {
                                HSSFSheet sheet = new HSSFWorkbook((InputStream) new FileInputStream(file2)).getSheet(str10);
                                if (sheet != null) {
                                    if (sheet.getPhysicalNumberOfRows() != 0) {
                                        for (int i = 2; i <= sheet.getLastRowNum(); i++) {
                                            HSSFRow row = sheet.getRow(i);
                                            if (!(row == null || row.getCell(0) == null || row.getCell(1) == null || row.getCell(2) == null)) {
                                                String format = String.format("%.0f", new Object[]{Double.valueOf(row.getCell(0).getNumericCellValue())});
                                                String format2 = String.format("%.0f", new Object[]{Double.valueOf(row.getCell(1).getNumericCellValue())});
                                                String hSSFCell = row.getCell(2).toString();
                                                Student student = new Student();
                                                student.setName(hSSFCell);
                                                student.setRoll_no(format);
                                                student.setEnrollment(format2);
                                                if (!format.equals("") && !hSSFCell.equals("") && !format2.equals("")) {
                                                    TheoryAttendenceActivity.this.uploads.add(student);
                                                }
                                            }
                                        }
                                        progressDialog.dismiss();
                                        if (TheoryAttendenceActivity.this.getLifecycle().getCurrentState().isAtLeast(Lifecycle.State.INITIALIZED)) {
                                            TheoryAttendenceActivity.this.adapter = new AttendenceAdapter(TheoryAttendenceActivity.this, str7 + "/Theory Attendence/" + str8 + " " + str9, TheoryAttendenceActivity.this.uploads, file2, str11, str12, str10, str8, str9, str7, document.getUrl());
                                            TheoryAttendenceActivity.this.student_list.setAdapter(TheoryAttendenceActivity.this.adapter);
                                            return;
                                        }
                                        return;
                                    }
                                }
                                progressDialog.dismiss();
                                AlertDialog.Builder builder = new AlertDialog.Builder(TheoryAttendenceActivity.this);
                                builder.setTitle((CharSequence) "Alert");
                                builder.setMessage((CharSequence) "Attendance of Current Subject Not Found.\nPossible Reasons are:\n1. New Subjects Added and Attendance Sheet Not Updated.\nContact H.O.D");
                                builder.setPositiveButton((CharSequence) "Ok", (DialogInterface.OnClickListener) null);
                                builder.create();
                                if (TheoryAttendenceActivity.this.getLifecycle().getCurrentState().isAtLeast(Lifecycle.State.CREATED)) {
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
                            Toast.makeText(TheoryAttendenceActivity.this, "F", Toast.LENGTH_SHORT).show();
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
