package org.projectapp;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.os.Bundle;
import android.os.Environment;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.SpinnerAdapter;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog.Builder;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatSpinner;
import androidx.appcompat.widget.AppCompatTextView;
import androidx.lifecycle.Lifecycle.State;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.RecyclerView.Adapter;
import com.google.android.gms.common.internal.ImagesContract;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FileDownloadTask.TaskSnapshot;
import com.google.firebase.storage.FirebaseStorage;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Calendar;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.projectapp.Adapter.ViewSingleStudentAttendenceAdapter;
import org.projectapp.Model.Document;
import org.projectapp.Model.Subject;

public class ViewSingleStudentAttendenceActivity extends AppCompatActivity {
    int a = 0;
    ArrayList<Integer> absent;
    Adapter adapter;
    String batch = null;
    String[] batchArray;
    String department;
    String enrollment;
    LinearLayoutManager layoutManager;
    ArrayList<String> month;
    String name;
    int p = 0;
    String path;
    ArrayList<Integer> present;
    int rowno;
    String semester;
    AppCompatSpinner student_batch;
    RecyclerView student_list;
    AppCompatSpinner student_subject;
    String subject = null;
    ArrayList<String> subjects;
    AppCompatTextView title;
    ArrayList<Integer> total;
    String url = null;
    String year;
    int z = 0;

    /* access modifiers changed from: protected */
    public void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        setContentView((int) R.layout.activity_view_single_student_attendence);
        final Calendar instance = Calendar.getInstance();
        this.student_batch = (AppCompatSpinner) findViewById(R.id.student_batch);
        this.student_subject = (AppCompatSpinner) findViewById(R.id.student_subject);
        this.title = (AppCompatTextView) findViewById(R.id.title);
        String str = "name";
        this.title.setText(getIntent().getStringExtra(str));
        this.subjects = new ArrayList<>();
        this.subjects.add("Select Subject");
        String str2 = "-";
        if (instance.get(2) < instance.get(5)) {
            StringBuilder sb = new StringBuilder();
            sb.append(instance.get(1) - 1);
            sb.append(str2);
            sb.append(instance.get(1));
            this.year = sb.toString();
        } else if (instance.get(2) == 11) {
            StringBuilder sb2 = new StringBuilder();
            sb2.append(instance.get(1));
            sb2.append(str2);
            sb2.append(instance.get(1) + 1);
            this.year = sb2.toString();
        } else {
            StringBuilder sb3 = new StringBuilder();
            sb3.append(instance.get(1));
            sb3.append(str2);
            sb3.append(instance.get(1) + 1);
            this.year = sb3.toString();
        }
        this.month = new ArrayList<>();
        this.present = new ArrayList<>();
        this.absent = new ArrayList<>();
        this.total = new ArrayList<>();
        this.layoutManager = new LinearLayoutManager(this);
        this.student_list = (RecyclerView) findViewById(R.id.student_list);
        this.student_list.setHasFixedSize(true);
        this.student_list.setLayoutManager(this.layoutManager);
        this.department = getIntent().getStringExtra("department");
        this.semester = getIntent().getStringExtra("semester");
        this.enrollment = getIntent().getStringExtra("enrollment");
        this.name = getIntent().getStringExtra(str);
        if (getIntent().getStringExtra("loginas").equals("student")) {
            String str3 = " ";
            if (getIntent().getStringExtra("attendence").equals("theory")) {
                this.title.setText("Theory Attendence");
                StringBuilder sb4 = new StringBuilder();
                sb4.append(this.semester);
                sb4.append(" Theory Attendence ");
                sb4.append(this.year);
                this.path = sb4.toString();
                this.student_subject.setVisibility(View.VISIBLE);
                this.student_batch.setVisibility(View.GONE);
                FirebaseDatabase instance2 = FirebaseDatabase.getInstance();
                StringBuilder sb5 = new StringBuilder();
                sb5.append(this.department);
                sb5.append("/Theory Attendence/");
                sb5.append(this.semester);
                sb5.append(str3);
                sb5.append(this.year);
                instance2.getReference(sb5.toString()).addListenerForSingleValueEvent(new ValueEventListener() {
                    public void onCancelled(@NonNull DatabaseError databaseError) {
                    }

                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        Document document = (Document) dataSnapshot.getValue(Document.class);
                        ViewSingleStudentAttendenceActivity.this.url = document.getUrl();
                        if (ViewSingleStudentAttendenceActivity.this.subject != null) {
                            ViewSingleStudentAttendenceActivity.this.viewAttendence(instance);
                        }
                    }
                });
                FirebaseDatabase instance3 = FirebaseDatabase.getInstance();
                StringBuilder sb6 = new StringBuilder();
                sb6.append(this.department);
                sb6.append("/subjects/");
                sb6.append(this.semester);
                instance3.getReference(sb6.toString()).addListenerForSingleValueEvent(new ValueEventListener() {
                    public void onCancelled(@NonNull DatabaseError databaseError) {
                    }

                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        ViewSingleStudentAttendenceActivity.this.subjects.clear();
                        for (DataSnapshot value : dataSnapshot.getChildren()) {
                            Subject subject = (Subject) value.getValue(Subject.class);
                            if (subject.getName().contains("TH")) {
                                ViewSingleStudentAttendenceActivity.this.subjects.add(subject.getName());
                            }
                        }
                        AppCompatSpinner appCompatSpinner = ViewSingleStudentAttendenceActivity.this.student_subject;
                        ViewSingleStudentAttendenceActivity viewSingleStudentAttendenceActivity = ViewSingleStudentAttendenceActivity.this;
                        appCompatSpinner.setAdapter((SpinnerAdapter) new ArrayAdapter(viewSingleStudentAttendenceActivity, R.layout.spinner_dialog, viewSingleStudentAttendenceActivity.subjects));
                    }
                });
            } else {
                this.title.setText("Practical Attendence");
                StringBuilder sb7 = new StringBuilder();
                sb7.append(this.semester);
                sb7.append(" Practical Attendence ");
                sb7.append(this.year);
                this.path = sb7.toString();
                this.student_subject.setVisibility(View.VISIBLE);
                this.student_batch.setVisibility(View.VISIBLE);
                FirebaseDatabase instance4 = FirebaseDatabase.getInstance();
                StringBuilder sb8 = new StringBuilder();
                sb8.append(this.department);
                sb8.append("/Practical Attendence/");
                sb8.append(this.semester);
                sb8.append(str3);
                sb8.append(this.year);
                instance4.getReference(sb8.toString()).addListenerForSingleValueEvent(new ValueEventListener() {
                    public void onCancelled(@NonNull DatabaseError databaseError) {
                    }

                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        Document document = (Document) dataSnapshot.getValue(Document.class);
                        ViewSingleStudentAttendenceActivity.this.url = document.getUrl();
                        if (ViewSingleStudentAttendenceActivity.this.subject != null && ViewSingleStudentAttendenceActivity.this.batch != null) {
                            ViewSingleStudentAttendenceActivity.this.viewAttendence(instance);
                        }
                    }
                });
                String str4 = "Batch";
                if (this.department.equals("Computer Department")) {
                    this.batchArray = new String[]{str4, "CO1", "CO2", "CO3"};
                } else if (this.department.equals("IT Department")) {
                    this.batchArray = new String[]{str4, "IF1", "IF2", "IF3"};
                } else {
                    String str5 = "CE3";
                    String str6 = "CE2";
                    String str7 = "CE1";
                    if (this.department.equals("Civil I Shift Department")) {
                        this.batchArray = new String[]{str4, str7, str6, str5};
                    } else if (this.department.equals("Civil II Shift Department")) {
                        this.batchArray = new String[]{str4, str7, str6, str5};
                    } else {
                        String str8 = "ME3";
                        String str9 = "ME2";
                        String str10 = "ME1";
                        if (this.department.equals("Mechanical I Shift Department")) {
                            this.batchArray = new String[]{str4, str10, str9, str8};
                        } else if (this.department.equals("Mechanical II Shift Department")) {
                            this.batchArray = new String[]{str4, str10, str9, str8};
                        } else if (this.department.equals("Chemical Department")) {
                            this.batchArray = new String[]{str4, "CH1", "CH2", "CH3"};
                        } else if (this.department.equals("TR Department")) {
                            this.batchArray = new String[]{str4, "TR1", "TR2", "TR3"};
                        }
                    }
                }
                this.student_batch.setAdapter((SpinnerAdapter) new ArrayAdapter(this, R.layout.spinner_dialog, this.batchArray));
            }
        } else {
            this.student_subject.setVisibility(View.GONE);
            this.student_batch.setVisibility(View.GONE);
            this.path = getIntent().getStringExtra("path");
            this.subject = getIntent().getStringExtra("subject");
            this.url = getIntent().getStringExtra(ImagesContract.URL);
            viewAttendence(instance);
        }
        this.student_subject.setOnItemSelectedListener(new OnItemSelectedListener() {
            public void onNothingSelected(AdapterView<?> adapterView) {
            }

            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long j) {
                if (ViewSingleStudentAttendenceActivity.this.getLifecycle().getCurrentState().isAtLeast(State.INITIALIZED)) {
                    ViewSingleStudentAttendenceActivity.this.student_list.setAdapter(null);
                    if (adapterView.getItemAtPosition(i).toString().equals("Select Subject")) {
                        return;
                    }
                    if (ViewSingleStudentAttendenceActivity.this.getIntent().getStringExtra("attendence").equals("practical")) {
                        ViewSingleStudentAttendenceActivity viewSingleStudentAttendenceActivity = ViewSingleStudentAttendenceActivity.this;
                        StringBuilder sb = new StringBuilder();
                        sb.append(ViewSingleStudentAttendenceActivity.this.batch);
                        sb.append(" ");
                        sb.append(adapterView.getItemAtPosition(i).toString());
                        viewSingleStudentAttendenceActivity.subject = sb.toString();
                        if (ViewSingleStudentAttendenceActivity.this.url != null) {
                            ViewSingleStudentAttendenceActivity.this.viewAttendence(instance);
                            return;
                        }
                        return;
                    }
                    ViewSingleStudentAttendenceActivity.this.subject = adapterView.getItemAtPosition(i).toString();
                    if (ViewSingleStudentAttendenceActivity.this.url != null) {
                        ViewSingleStudentAttendenceActivity.this.viewAttendence(instance);
                    }
                }
            }
        });
        this.student_batch.setOnItemSelectedListener(new OnItemSelectedListener() {
            public void onNothingSelected(AdapterView<?> adapterView) {
            }

            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long j) {
                ViewSingleStudentAttendenceActivity.this.subjects.clear();
                if (adapterView.getItemAtPosition(i).toString().equals("Batch")) {
                    ViewSingleStudentAttendenceActivity.this.batch = null;
                    return;
                }
                ViewSingleStudentAttendenceActivity.this.batch = adapterView.getItemAtPosition(i).toString();
                FirebaseDatabase instance = FirebaseDatabase.getInstance();
                StringBuilder sb = new StringBuilder();
                sb.append(ViewSingleStudentAttendenceActivity.this.department);
                sb.append("/subjects/");
                sb.append(ViewSingleStudentAttendenceActivity.this.semester);
                instance.getReference(sb.toString()).addListenerForSingleValueEvent(new ValueEventListener() {
                    public void onCancelled(@NonNull DatabaseError databaseError) {
                    }

                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        for (DataSnapshot value : dataSnapshot.getChildren()) {
                            Subject subject = (Subject) value.getValue(Subject.class);
                            if (!subject.getName().contains("TH")) {
                                ViewSingleStudentAttendenceActivity.this.subjects.add(subject.getName());
                            }
                        }
                        ViewSingleStudentAttendenceActivity.this.student_subject.setAdapter((SpinnerAdapter) new ArrayAdapter(ViewSingleStudentAttendenceActivity.this, R.layout.spinner_dialog, ViewSingleStudentAttendenceActivity.this.subjects));
                    }
                });
            }
        });
    }

    /* access modifiers changed from: private */
    public void viewAttendence(final Calendar calendar) {
        String str = "/rait/Document";
        this.total.clear();
        this.present.clear();
        this.absent.clear();
        this.month.clear();
        final ProgressDialog progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Fetching Monthly Attendence...");
        progressDialog.setCancelable(false);
        progressDialog.show();
        try {
            StringBuilder sb = new StringBuilder();
            sb.append(Environment.getExternalStorageDirectory().getPath());
            sb.append(str);
            File file = new File(sb.toString());
            if (!file.exists()) {
                file.mkdirs();
            }
            StringBuilder sb2 = new StringBuilder();
            sb2.append(Environment.getExternalStorageDirectory().getPath());
            sb2.append(str);
            String sb3 = sb2.toString();
            StringBuilder sb4 = new StringBuilder();
            sb4.append(this.path);
            sb4.append(".xls");
            final File file2 = new File(sb3, sb4.toString());
            if (!file2.exists()) {
                file2.createNewFile();
            }
            FirebaseStorage.getInstance().getReferenceFromUrl(this.url).getFile(file2).addOnSuccessListener((OnSuccessListener) new OnSuccessListener<TaskSnapshot>() {
                /* JADX WARNING: Removed duplicated region for block: B:266:0x0aa2 A[Catch:{ FileNotFoundException -> 0x0b00, IOException -> 0x0af6 }] */
                /* JADX WARNING: Removed duplicated region for block: B:331:? A[RETURN, SYNTHETIC] */
                public void onSuccess(TaskSnapshot taskSnapshot) {
                    String str = "December ";
                    try {
                        HSSFSheet sheet = new HSSFWorkbook((InputStream) new FileInputStream(file2)).getSheet(ViewSingleStudentAttendenceActivity.this.subject);
                        if (!(sheet == null || sheet.getRow(2) == null)) {
                            if (sheet.getRow(2).getCell(0) != null) {
                                int i = 2;
                                while (true) {
                                    if (i > sheet.getLastRowNum()) {
                                        break;
                                    }
                                    if (String.format("%.0f", new Object[]{Double.valueOf(sheet.getRow(i).getCell(1).getNumericCellValue())}).equals(ViewSingleStudentAttendenceActivity.this.enrollment)) {
                                        ViewSingleStudentAttendenceActivity.this.rowno = i;
                                        break;
                                    }
                                    i++;
                                }
                                HSSFRow row = sheet.getRow(ViewSingleStudentAttendenceActivity.this.rowno);
                                HSSFRow row2 = sheet.getRow(0);
                                String str2 = "Semester";
                                String str3 = "Absent";
                                String str4 = "Present";
                                String str5 = "";
                                int i2 = 3;
                                if (calendar.get(2) >= 5) {
                                    if (calendar.get(11) != 11) {
                                        if (calendar.get(2) >= 6 || calendar.get(11) != 11) {
                                            for (int i3 = 3; i3 < row.getLastCellNum(); i3++) {
                                                if (!row.getCell(i3).equals(str5)) {
                                                    if (row.getCell(i3).toString().equals(str4)) {
                                                        ViewSingleStudentAttendenceActivity.this.p++;
                                                    } else if (row.getCell(i3).toString().equals(str3)) {
                                                        ViewSingleStudentAttendenceActivity.this.a++;
                                                    }
                                                    ViewSingleStudentAttendenceActivity.this.z++;
                                                }
                                            }
                                            ViewSingleStudentAttendenceActivity.this.month.add(str2);
                                            ViewSingleStudentAttendenceActivity.this.present.add(Integer.valueOf(ViewSingleStudentAttendenceActivity.this.p));
                                            ViewSingleStudentAttendenceActivity.this.absent.add(Integer.valueOf(ViewSingleStudentAttendenceActivity.this.a));
                                            ViewSingleStudentAttendenceActivity.this.total.add(Integer.valueOf(ViewSingleStudentAttendenceActivity.this.z));
                                            ViewSingleStudentAttendenceActivity.this.p = 0;
                                            ViewSingleStudentAttendenceActivity.this.z = 0;
                                            ViewSingleStudentAttendenceActivity.this.a = 0;
                                            if (calendar.get(2) >= 6) {
                                                for (int i4 = 3; i4 < row2.getLastCellNum(); i4++) {
                                                    if (row2.getCell(i4).toString().contains("-06-") && !row.getCell(i4).toString().equals(str5)) {
                                                        if (row.getCell(i4).toString().equals(str4)) {
                                                            ViewSingleStudentAttendenceActivity.this.p++;
                                                        } else if (row.getCell(i4).toString().equals(str3)) {
                                                            ViewSingleStudentAttendenceActivity.this.a++;
                                                        }
                                                        ViewSingleStudentAttendenceActivity.this.z++;
                                                    }
                                                }
                                                ArrayList<String> arrayList = ViewSingleStudentAttendenceActivity.this.month;
                                                StringBuilder sb = new StringBuilder();
                                                sb.append("June ");
                                                sb.append(calendar.get(1));
                                                arrayList.add(sb.toString());
                                                ViewSingleStudentAttendenceActivity.this.present.add(Integer.valueOf(ViewSingleStudentAttendenceActivity.this.p));
                                                ViewSingleStudentAttendenceActivity.this.absent.add(Integer.valueOf(ViewSingleStudentAttendenceActivity.this.a));
                                                ViewSingleStudentAttendenceActivity.this.total.add(Integer.valueOf(ViewSingleStudentAttendenceActivity.this.z));
                                                ViewSingleStudentAttendenceActivity.this.p = 0;
                                                ViewSingleStudentAttendenceActivity.this.z = 0;
                                                ViewSingleStudentAttendenceActivity.this.a = 0;
                                            }
                                            if (calendar.get(2) >= 6) {
                                                for (int i5 = 3; i5 < row2.getLastCellNum(); i5++) {
                                                    if (row2.getCell(i5).toString().contains("-07-") && !row.getCell(i5).toString().equals(str5)) {
                                                        if (row.getCell(i5).toString().equals(str4)) {
                                                            ViewSingleStudentAttendenceActivity.this.p++;
                                                        } else if (row.getCell(i5).toString().equals(str3)) {
                                                            ViewSingleStudentAttendenceActivity.this.a++;
                                                        }
                                                        ViewSingleStudentAttendenceActivity.this.z++;
                                                    }
                                                }
                                                ArrayList<String> arrayList2 = ViewSingleStudentAttendenceActivity.this.month;
                                                StringBuilder sb2 = new StringBuilder();
                                                sb2.append("July ");
                                                sb2.append(calendar.get(1));
                                                arrayList2.add(sb2.toString());
                                                ViewSingleStudentAttendenceActivity.this.present.add(Integer.valueOf(ViewSingleStudentAttendenceActivity.this.p));
                                                ViewSingleStudentAttendenceActivity.this.absent.add(Integer.valueOf(ViewSingleStudentAttendenceActivity.this.a));
                                                ViewSingleStudentAttendenceActivity.this.total.add(Integer.valueOf(ViewSingleStudentAttendenceActivity.this.z));
                                                ViewSingleStudentAttendenceActivity.this.p = 0;
                                                ViewSingleStudentAttendenceActivity.this.z = 0;
                                                ViewSingleStudentAttendenceActivity.this.a = 0;
                                            }
                                            if (calendar.get(2) >= 7) {
                                                for (int i6 = 3; i6 < row2.getLastCellNum(); i6++) {
                                                    if (row2.getCell(i6).toString().contains("-08-") && !row.getCell(i6).toString().equals(str5)) {
                                                        if (row.getCell(i6).toString().equals(str4)) {
                                                            ViewSingleStudentAttendenceActivity.this.p++;
                                                        } else if (row.getCell(i6).toString().equals(str3)) {
                                                            ViewSingleStudentAttendenceActivity.this.a++;
                                                        }
                                                        ViewSingleStudentAttendenceActivity.this.z++;
                                                    }
                                                }
                                                ArrayList<String> arrayList3 = ViewSingleStudentAttendenceActivity.this.month;
                                                StringBuilder sb3 = new StringBuilder();
                                                sb3.append("August ");
                                                sb3.append(calendar.get(1));
                                                arrayList3.add(sb3.toString());
                                                ViewSingleStudentAttendenceActivity.this.present.add(Integer.valueOf(ViewSingleStudentAttendenceActivity.this.p));
                                                ViewSingleStudentAttendenceActivity.this.absent.add(Integer.valueOf(ViewSingleStudentAttendenceActivity.this.a));
                                                ViewSingleStudentAttendenceActivity.this.total.add(Integer.valueOf(ViewSingleStudentAttendenceActivity.this.z));
                                                ViewSingleStudentAttendenceActivity.this.p = 0;
                                                ViewSingleStudentAttendenceActivity.this.z = 0;
                                                ViewSingleStudentAttendenceActivity.this.a = 0;
                                            }
                                            if (calendar.get(2) >= 8) {
                                                for (int i7 = 3; i7 < row2.getLastCellNum(); i7++) {
                                                    if (row2.getCell(i7).toString().contains("-09-") && !row.getCell(i7).toString().equals(str5)) {
                                                        if (row.getCell(i7).toString().equals(str4)) {
                                                            ViewSingleStudentAttendenceActivity.this.p++;
                                                        } else if (row.getCell(i7).toString().equals(str3)) {
                                                            ViewSingleStudentAttendenceActivity.this.a++;
                                                        }
                                                        ViewSingleStudentAttendenceActivity.this.z++;
                                                    }
                                                }
                                                ArrayList<String> arrayList4 = ViewSingleStudentAttendenceActivity.this.month;
                                                StringBuilder sb4 = new StringBuilder();
                                                sb4.append("September ");
                                                sb4.append(calendar.get(1));
                                                arrayList4.add(sb4.toString());
                                                ViewSingleStudentAttendenceActivity.this.present.add(Integer.valueOf(ViewSingleStudentAttendenceActivity.this.p));
                                                ViewSingleStudentAttendenceActivity.this.absent.add(Integer.valueOf(ViewSingleStudentAttendenceActivity.this.a));
                                                ViewSingleStudentAttendenceActivity.this.total.add(Integer.valueOf(ViewSingleStudentAttendenceActivity.this.z));
                                                ViewSingleStudentAttendenceActivity.this.p = 0;
                                                ViewSingleStudentAttendenceActivity.this.z = 0;
                                                ViewSingleStudentAttendenceActivity.this.a = 0;
                                            }
                                            if (calendar.get(2) >= 9) {
                                                for (int i8 = 3; i8 < row2.getLastCellNum(); i8++) {
                                                    if (row2.getCell(i8).toString().contains("-10-") && !row.getCell(i8).toString().equals(str5)) {
                                                        if (row.getCell(i8).toString().equals(str4)) {
                                                            ViewSingleStudentAttendenceActivity.this.p++;
                                                        } else if (row.getCell(i8).toString().equals(str3)) {
                                                            ViewSingleStudentAttendenceActivity.this.a++;
                                                        }
                                                        ViewSingleStudentAttendenceActivity.this.z++;
                                                    }
                                                }
                                                ArrayList<String> arrayList5 = ViewSingleStudentAttendenceActivity.this.month;
                                                StringBuilder sb5 = new StringBuilder();
                                                sb5.append("October ");
                                                sb5.append(calendar.get(1));
                                                arrayList5.add(sb5.toString());
                                                ViewSingleStudentAttendenceActivity.this.present.add(Integer.valueOf(ViewSingleStudentAttendenceActivity.this.p));
                                                ViewSingleStudentAttendenceActivity.this.absent.add(Integer.valueOf(ViewSingleStudentAttendenceActivity.this.a));
                                                ViewSingleStudentAttendenceActivity.this.total.add(Integer.valueOf(ViewSingleStudentAttendenceActivity.this.z));
                                                ViewSingleStudentAttendenceActivity.this.p = 0;
                                                ViewSingleStudentAttendenceActivity.this.z = 0;
                                                ViewSingleStudentAttendenceActivity.this.a = 0;
                                            }
                                            if (calendar.get(2) >= 10) {
                                                while (i2 < row2.getLastCellNum()) {
                                                    if (row2.getCell(i2).toString().contains("-11-") && !row.getCell(i2).toString().equals(str5)) {
                                                        if (row.getCell(i2).toString().equals(str4)) {
                                                            ViewSingleStudentAttendenceActivity.this.p++;
                                                        } else if (row.getCell(i2).toString().equals(str3)) {
                                                            ViewSingleStudentAttendenceActivity.this.a++;
                                                        }
                                                        ViewSingleStudentAttendenceActivity.this.z++;
                                                    }
                                                    i2++;
                                                }
                                                ArrayList<String> arrayList6 = ViewSingleStudentAttendenceActivity.this.month;
                                                StringBuilder sb6 = new StringBuilder();
                                                sb6.append("November ");
                                                sb6.append(calendar.get(1));
                                                arrayList6.add(sb6.toString());
                                                ViewSingleStudentAttendenceActivity.this.present.add(Integer.valueOf(ViewSingleStudentAttendenceActivity.this.p));
                                                ViewSingleStudentAttendenceActivity.this.absent.add(Integer.valueOf(ViewSingleStudentAttendenceActivity.this.a));
                                                ViewSingleStudentAttendenceActivity.this.total.add(Integer.valueOf(ViewSingleStudentAttendenceActivity.this.z));
                                                ViewSingleStudentAttendenceActivity.this.p = 0;
                                                ViewSingleStudentAttendenceActivity.this.z = 0;
                                                ViewSingleStudentAttendenceActivity.this.a = 0;
                                            }
                                        }
                                        progressDialog.dismiss();
                                        if (!ViewSingleStudentAttendenceActivity.this.getLifecycle().getCurrentState().isAtLeast(State.INITIALIZED)) {
                                            RecyclerView recyclerView = ViewSingleStudentAttendenceActivity.this.student_list;
                                            ViewSingleStudentAttendenceAdapter viewSingleStudentAttendenceAdapter = new ViewSingleStudentAttendenceAdapter(ViewSingleStudentAttendenceActivity.this, ViewSingleStudentAttendenceActivity.this.present, ViewSingleStudentAttendenceActivity.this.absent, ViewSingleStudentAttendenceActivity.this.total, ViewSingleStudentAttendenceActivity.this.month);
                                            recyclerView.setAdapter(viewSingleStudentAttendenceAdapter);
                                            return;
                                        }
                                        return;
                                    }
                                }
                                for (int i9 = 3; i9 < row.getLastCellNum(); i9++) {
                                    if (!row.getCell(i9).equals(str5)) {
                                        if (row.getCell(i9).toString().equals(str4)) {
                                            ViewSingleStudentAttendenceActivity.this.p++;
                                        } else if (row.getCell(i9).toString().equals(str3)) {
                                            ViewSingleStudentAttendenceActivity.this.a++;
                                        }
                                        ViewSingleStudentAttendenceActivity.this.z++;
                                    }
                                }
                                ViewSingleStudentAttendenceActivity.this.month.add(str2);
                                ViewSingleStudentAttendenceActivity.this.present.add(Integer.valueOf(ViewSingleStudentAttendenceActivity.this.p));
                                ViewSingleStudentAttendenceActivity.this.absent.add(Integer.valueOf(ViewSingleStudentAttendenceActivity.this.a));
                                ViewSingleStudentAttendenceActivity.this.total.add(Integer.valueOf(ViewSingleStudentAttendenceActivity.this.z));
                                ViewSingleStudentAttendenceActivity.this.p = 0;
                                ViewSingleStudentAttendenceActivity.this.z = 0;
                                ViewSingleStudentAttendenceActivity.this.a = 0;
                                for (int i10 = 3; i10 < row2.getLastCellNum(); i10++) {
                                    if (row2.getCell(i10).toString().contains("-12-") && !row.getCell(i10).toString().equals(str5)) {
                                        if (row.getCell(i10).toString().equals(str4)) {
                                            ViewSingleStudentAttendenceActivity.this.p++;
                                        } else if (row.getCell(i10).toString().equals(str3)) {
                                            ViewSingleStudentAttendenceActivity.this.a++;
                                        }
                                        ViewSingleStudentAttendenceActivity.this.z++;
                                    }
                                }
                                ArrayList<String> arrayList7 = ViewSingleStudentAttendenceActivity.this.month;
                                StringBuilder sb7 = new StringBuilder();
                                sb7.append(str);
                                sb7.append(calendar.get(1));
                                arrayList7.add(sb7.toString());
                                ViewSingleStudentAttendenceActivity.this.present.add(Integer.valueOf(ViewSingleStudentAttendenceActivity.this.p));
                                ViewSingleStudentAttendenceActivity.this.absent.add(Integer.valueOf(ViewSingleStudentAttendenceActivity.this.a));
                                ViewSingleStudentAttendenceActivity.this.total.add(Integer.valueOf(ViewSingleStudentAttendenceActivity.this.z));
                                ViewSingleStudentAttendenceActivity.this.p = 0;
                                ViewSingleStudentAttendenceActivity.this.z = 0;
                                ViewSingleStudentAttendenceActivity.this.a = 0;
                                if (calendar.get(2) >= 0) {
                                    ArrayList<String> arrayList8 = ViewSingleStudentAttendenceActivity.this.month;
                                    ArrayList<String> arrayList9 = ViewSingleStudentAttendenceActivity.this.month;
                                    StringBuilder sb8 = new StringBuilder();
                                    sb8.append(str);
                                    sb8.append(calendar.get(1));
                                    int indexOf = arrayList9.indexOf(sb8.toString());
                                    StringBuilder sb9 = new StringBuilder();
                                    sb9.append(str);
                                    sb9.append(calendar.get(1) - 1);
                                    arrayList8.set(indexOf, sb9.toString());
                                    for (int i11 = 3; i11 < row2.getLastCellNum(); i11++) {
                                        if (row2.getCell(i11).toString().contains("-01-") && !row.getCell(i11).toString().equals(str5)) {
                                            if (row.getCell(i11).toString().equals(str4)) {
                                                ViewSingleStudentAttendenceActivity.this.p++;
                                            } else if (row.getCell(i11).toString().equals(str3)) {
                                                ViewSingleStudentAttendenceActivity.this.a++;
                                            }
                                            ViewSingleStudentAttendenceActivity.this.z++;
                                        }
                                    }
                                    ArrayList<String> arrayList10 = ViewSingleStudentAttendenceActivity.this.month;
                                    StringBuilder sb10 = new StringBuilder();
                                    sb10.append("January ");
                                    sb10.append(calendar.get(1));
                                    arrayList10.add(sb10.toString());
                                    ViewSingleStudentAttendenceActivity.this.present.add(Integer.valueOf(ViewSingleStudentAttendenceActivity.this.p));
                                    ViewSingleStudentAttendenceActivity.this.absent.add(Integer.valueOf(ViewSingleStudentAttendenceActivity.this.a));
                                    ViewSingleStudentAttendenceActivity.this.total.add(Integer.valueOf(ViewSingleStudentAttendenceActivity.this.z));
                                    ViewSingleStudentAttendenceActivity.this.p = 0;
                                    ViewSingleStudentAttendenceActivity.this.z = 0;
                                    ViewSingleStudentAttendenceActivity.this.a = 0;
                                }
                                if (calendar.get(2) >= 1) {
                                    for (int i12 = 3; i12 < row2.getLastCellNum(); i12++) {
                                        if (row2.getCell(i12).toString().contains("-02-") && !row.getCell(i12).toString().equals(str5)) {
                                            if (row.getCell(i12).toString().equals(str4)) {
                                                ViewSingleStudentAttendenceActivity.this.p++;
                                            } else if (row.getCell(i12).toString().equals(str3)) {
                                                ViewSingleStudentAttendenceActivity.this.a++;
                                            }
                                            ViewSingleStudentAttendenceActivity.this.z++;
                                        }
                                    }
                                    ArrayList<String> arrayList11 = ViewSingleStudentAttendenceActivity.this.month;
                                    StringBuilder sb11 = new StringBuilder();
                                    sb11.append("February ");
                                    sb11.append(calendar.get(1));
                                    arrayList11.add(sb11.toString());
                                    ViewSingleStudentAttendenceActivity.this.present.add(Integer.valueOf(ViewSingleStudentAttendenceActivity.this.p));
                                    ViewSingleStudentAttendenceActivity.this.absent.add(Integer.valueOf(ViewSingleStudentAttendenceActivity.this.a));
                                    ViewSingleStudentAttendenceActivity.this.total.add(Integer.valueOf(ViewSingleStudentAttendenceActivity.this.z));
                                    ViewSingleStudentAttendenceActivity.this.p = 0;
                                    ViewSingleStudentAttendenceActivity.this.z = 0;
                                    ViewSingleStudentAttendenceActivity.this.a = 0;
                                }
                                if (calendar.get(2) >= 2) {
                                    for (int i13 = 3; i13 < row2.getLastCellNum(); i13++) {
                                        if (row2.getCell(i13).toString().contains("-03-") && !row.getCell(i13).toString().equals(str5)) {
                                            if (row.getCell(i13).toString().equals(str4)) {
                                                ViewSingleStudentAttendenceActivity.this.p++;
                                            } else if (row.getCell(i13).toString().equals(str3)) {
                                                ViewSingleStudentAttendenceActivity.this.a++;
                                            }
                                            ViewSingleStudentAttendenceActivity.this.z++;
                                        }
                                    }
                                    ArrayList<String> arrayList12 = ViewSingleStudentAttendenceActivity.this.month;
                                    StringBuilder sb12 = new StringBuilder();
                                    sb12.append("March ");
                                    sb12.append(calendar.get(1));
                                    arrayList12.add(sb12.toString());
                                    ViewSingleStudentAttendenceActivity.this.present.add(Integer.valueOf(ViewSingleStudentAttendenceActivity.this.p));
                                    ViewSingleStudentAttendenceActivity.this.absent.add(Integer.valueOf(ViewSingleStudentAttendenceActivity.this.a));
                                    ViewSingleStudentAttendenceActivity.this.total.add(Integer.valueOf(ViewSingleStudentAttendenceActivity.this.z));
                                    ViewSingleStudentAttendenceActivity.this.p = 0;
                                    ViewSingleStudentAttendenceActivity.this.z = 0;
                                    ViewSingleStudentAttendenceActivity.this.a = 0;
                                }
                                if (calendar.get(2) >= 3) {
                                    for (int i14 = 3; i14 < row2.getLastCellNum(); i14++) {
                                        if (row2.getCell(i14).toString().contains("-04-") && !row.getCell(i14).toString().equals(str5)) {
                                            if (row.getCell(i14).toString().equals(str4)) {
                                                ViewSingleStudentAttendenceActivity.this.p++;
                                            } else if (row.getCell(i14).toString().equals(str3)) {
                                                ViewSingleStudentAttendenceActivity.this.a++;
                                            }
                                            ViewSingleStudentAttendenceActivity.this.z++;
                                        }
                                    }
                                    ArrayList<String> arrayList13 = ViewSingleStudentAttendenceActivity.this.month;
                                    StringBuilder sb13 = new StringBuilder();
                                    sb13.append("April ");
                                    sb13.append(calendar.get(1));
                                    arrayList13.add(sb13.toString());
                                    ViewSingleStudentAttendenceActivity.this.present.add(Integer.valueOf(ViewSingleStudentAttendenceActivity.this.p));
                                    ViewSingleStudentAttendenceActivity.this.absent.add(Integer.valueOf(ViewSingleStudentAttendenceActivity.this.a));
                                    ViewSingleStudentAttendenceActivity.this.total.add(Integer.valueOf(ViewSingleStudentAttendenceActivity.this.z));
                                    ViewSingleStudentAttendenceActivity.this.p = 0;
                                    ViewSingleStudentAttendenceActivity.this.z = 0;
                                    ViewSingleStudentAttendenceActivity.this.a = 0;
                                }
                                if (calendar.get(2) >= 4) {
                                    while (i2 < row2.getLastCellNum()) {
                                        if (row2.getCell(i2).toString().contains("-05-") && !row.getCell(i2).toString().equals(str5)) {
                                            if (row.getCell(i2).toString().equals(str4)) {
                                                ViewSingleStudentAttendenceActivity.this.p++;
                                            } else if (row.getCell(i2).toString().equals(str3)) {
                                                ViewSingleStudentAttendenceActivity.this.a++;
                                            }
                                            ViewSingleStudentAttendenceActivity.this.z++;
                                        }
                                        i2++;
                                    }
                                    ArrayList<String> arrayList14 = ViewSingleStudentAttendenceActivity.this.month;
                                    StringBuilder sb14 = new StringBuilder();
                                    sb14.append("May ");
                                    sb14.append(calendar.get(1));
                                    arrayList14.add(sb14.toString());
                                    ViewSingleStudentAttendenceActivity.this.present.add(Integer.valueOf(ViewSingleStudentAttendenceActivity.this.p));
                                    ViewSingleStudentAttendenceActivity.this.absent.add(Integer.valueOf(ViewSingleStudentAttendenceActivity.this.a));
                                    ViewSingleStudentAttendenceActivity.this.total.add(Integer.valueOf(ViewSingleStudentAttendenceActivity.this.z));
                                    ViewSingleStudentAttendenceActivity.this.p = 0;
                                    ViewSingleStudentAttendenceActivity.this.z = 0;
                                    ViewSingleStudentAttendenceActivity.this.a = 0;
                                }
                                progressDialog.dismiss();
                                if (!ViewSingleStudentAttendenceActivity.this.getLifecycle().getCurrentState().isAtLeast(State.INITIALIZED)) {
                                }
                            }
                        }
                        Builder builder = new Builder(ViewSingleStudentAttendenceActivity.this);
                        builder.setMessage((CharSequence) "Attendence not available");
                        builder.setTitle((CharSequence) "Alert");
                        builder.setPositiveButton((CharSequence) "Ok", (OnClickListener) new OnClickListener() {
                            public void onClick(DialogInterface dialogInterface, int i) {
                                ViewSingleStudentAttendenceActivity.this.onBackPressed();
                            }
                        });
                        builder.create();
                        if (ViewSingleStudentAttendenceActivity.this.getLifecycle().getCurrentState().isAtLeast(State.CREATED)) {
                            builder.show();
                        }
                    } catch (FileNotFoundException e) {
                        progressDialog.dismiss();
                        e.printStackTrace();
                    } catch (IOException e2) {
                        progressDialog.dismiss();
                        e2.printStackTrace();
                    }
                }
            });
        } catch (IOException e) {
            progressDialog.dismiss();
            e.printStackTrace();
        }
    }

    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }
}
