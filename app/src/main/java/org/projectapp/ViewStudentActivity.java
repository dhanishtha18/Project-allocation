package org.projectapp;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.SpinnerAdapter;
import androidx.appcompat.app.AlertDialog.Builder;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatSpinner;
import androidx.appcompat.widget.AppCompatTextView;
import androidx.lifecycle.Lifecycle.State;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.RecyclerView.Adapter;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import de.hdodenhof.circleimageview.CircleImageView;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import org.projectapp.Adapter.StudentAdapter;
import org.projectapp.Model.Student;

public class ViewStudentActivity extends AppCompatActivity {
    Adapter adapter;
    String department;
    CircleImageView icon;
    LinearLayoutManager layoutManager;
    DatabaseReference mDatabase;
    ProgressDialog progressDialog;
    String[] semArray;
    String semester;
    AppCompatSpinner student_department;
    AppCompatSpinner student_semester;
    AppCompatTextView title;
    Boolean unactive;
    RecyclerView unactive_list;
    List<Student> uploads;

    /* access modifiers changed from: protected */
    public void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        setContentView((int) R.layout.activity_view_student);
        this.layoutManager = new LinearLayoutManager(this);
        this.unactive_list = (RecyclerView) findViewById(R.id.unactive_list);
        this.student_department = (AppCompatSpinner) findViewById(R.id.student_department);
        this.student_semester = (AppCompatSpinner) findViewById(R.id.student_semester);
        this.unactive_list.setHasFixedSize(true);
        this.unactive_list.setLayoutManager(this.layoutManager);
        this.title = (AppCompatTextView) findViewById(R.id.title);
        String str = "loginas";
        String str2 = "principal";
        if (getIntent().getStringExtra(str).equals(str2)) {
            this.title.setText("Students");
        } else if (getIntent().getBooleanExtra("unactive", false)) {
            this.title.setText("InActive Students");
        } else {
            this.title.setText("Active Students");
        }
        if (getIntent().getStringExtra(str).equals(str2)) {
            this.student_department.setAdapter((SpinnerAdapter) new ArrayAdapter(this, R.layout.spinner_dialog, new String[]{"Select Department", "Computer Department", "IT Department", "Mechanical I Shift Department", "Mechanical II Shift Department", "Civil I Shift Department", "Civil II Shift Department", "TR Department", "Chemical Department"}));
            this.student_department.setOnItemSelectedListener(new OnItemSelectedListener() {
                public void onNothingSelected(AdapterView<?> adapterView) {
                }

                public void onItemSelected(AdapterView<?> adapterView, View view, int i, long j) {
                    if (adapterView.getItemAtPosition(i).equals("Select Department")) {
                        ViewStudentActivity.this.department = null;
                        return;
                    }
                    ViewStudentActivity.this.department = adapterView.getItemAtPosition(i).toString();
                    ViewStudentActivity.this.view();
                }
            });
            return;
        }
        this.student_department.setVisibility(View.GONE);
        this.department = getIntent().getStringExtra("department");
        view();
    }

    /* access modifiers changed from: private */
    public void view() {
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
                String str8 = "ME3I";
                String str9 = "ME1I";
                String str10 = "ME6I";
                String str11 = "ME4I";
                String str12 = "ME2I";
                if (this.department.equals("Mechanical I Shift Department")) {
                    if (instance.get(2) < 5 || instance.get(2) == 11) {
                        this.semArray = new String[]{str, str12, str11, str10};
                    } else {
                        this.semArray = new String[]{str, str9, str8, "ME5I"};
                    }
                } else if (this.department.equals("Mechanical II Shift Department")) {
                    if (instance.get(2) < 5 || instance.get(2) == 11) {
                        this.semArray = new String[]{str, str12, str11, str10};
                    } else {
                        this.semArray = new String[]{str, str9, str8, "ME5I"};
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
        this.student_semester.setAdapter((SpinnerAdapter) new ArrayAdapter(this, R.layout.spinner_dialog, this.semArray));
        this.student_semester.setOnItemSelectedListener(new OnItemSelectedListener() {
            public void onNothingSelected(AdapterView<?> adapterView) {
            }

            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long j) {
                if (adapterView.getItemAtPosition(i).toString().equals("Select Semester")) {
                    ViewStudentActivity.this.semester = null;
                    return;
                }
                ViewStudentActivity.this.semester = adapterView.getItemAtPosition(i).toString();
                ViewStudentActivity viewStudentActivity = ViewStudentActivity.this;
                viewStudentActivity.progressDialog = new ProgressDialog(viewStudentActivity);
                ViewStudentActivity.this.progressDialog.setCancelable(false);
                ViewStudentActivity.this.uploads = new ArrayList();
                ViewStudentActivity.this.progressDialog.setMessage("Please wait...");
                ViewStudentActivity.this.progressDialog.show();
                ViewStudentActivity viewStudentActivity2 = ViewStudentActivity.this;
                FirebaseDatabase instance = FirebaseDatabase.getInstance();
                StringBuilder sb = new StringBuilder();
                sb.append("Students/");
                sb.append(ViewStudentActivity.this.department);
                viewStudentActivity2.mDatabase = instance.getReference(sb.toString());
                ViewStudentActivity.this.mDatabase.child(ViewStudentActivity.this.semester).addValueEventListener(new ValueEventListener() {
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        ViewStudentActivity.this.progressDialog.dismiss();
                        ViewStudentActivity.this.uploads.clear();
                        String str = "loginas";
                        boolean equals = ViewStudentActivity.this.getIntent().getStringExtra(str).equals("principal");
                        Boolean valueOf = Boolean.valueOf(false);
                        if (equals) {
                            for (DataSnapshot value : dataSnapshot.getChildren()) {
                                ViewStudentActivity.this.uploads.add((Student) value.getValue(Student.class));
                            }
                            ViewStudentActivity.this.unactive = valueOf;
                        } else if (ViewStudentActivity.this.getIntent().getBooleanExtra("unactive", false)) {
                            for (DataSnapshot value2 : dataSnapshot.getChildren()) {
                                Student student = (Student) value2.getValue(Student.class);
                                if (student.getAccount().equals("deactivated")) {
                                    ViewStudentActivity.this.uploads.add(student);
                                }
                            }
                            ViewStudentActivity.this.unactive = Boolean.valueOf(true);
                        } else {
                            for (DataSnapshot value3 : dataSnapshot.getChildren()) {
                                Student student2 = (Student) value3.getValue(Student.class);
                                if (student2.getAccount().equals("activated")) {
                                    ViewStudentActivity.this.uploads.add(student2);
                                }
                            }
                            ViewStudentActivity.this.unactive = valueOf;
                        }
                        if (ViewStudentActivity.this.uploads.size() == 0) {
                            Builder builder = new Builder(ViewStudentActivity.this);
                            builder.setTitle((CharSequence) "Alert");
                            builder.setMessage((CharSequence) "No Students Found!!!");
                            builder.setPositiveButton((CharSequence) "Ok", (OnClickListener) new OnClickListener() {
                                public void onClick(DialogInterface dialogInterface, int i) {
                                    ViewStudentActivity.this.onBackPressed();
                                }
                            });
                            builder.create();
                            if (ViewStudentActivity.this.getLifecycle().getCurrentState().isAtLeast(State.CREATED)) {
                                builder.show();
                                return;
                            }
                            return;
                        }
                        ViewStudentActivity.this.sort(ViewStudentActivity.this.uploads, 0, ViewStudentActivity.this.uploads.size() - 1);
                        if (ViewStudentActivity.this.getLifecycle().getCurrentState().isAtLeast(State.INITIALIZED)) {
                            ViewStudentActivity viewStudentActivity = ViewStudentActivity.this;
                            StudentAdapter studentAdapter = new StudentAdapter(ViewStudentActivity.this, ViewStudentActivity.this.uploads, ViewStudentActivity.this.unactive, ViewStudentActivity.this.getIntent().getStringExtra(str), ViewStudentActivity.this.getIntent().getStringExtra("admin_login"), ViewStudentActivity.this.getIntent().getStringExtra("admin_password"));
                            viewStudentActivity.adapter = studentAdapter;
                            ViewStudentActivity.this.unactive_list.setAdapter(ViewStudentActivity.this.adapter);
                        }
                    }

                    public void onCancelled(DatabaseError databaseError) {
                        ViewStudentActivity.this.progressDialog.dismiss();
                    }
                });
            }
        });
    }

    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }

    private int partition(List<Student> list, int i, int i2) {
        float parseFloat = Float.parseFloat(((Student) list.get(i2)).getRoll_no());
        int i3 = i - 1;
        while (i < i2) {
            if (Float.parseFloat(((Student) list.get(i)).getRoll_no()) < parseFloat) {
                i3++;
                Student student = (Student) list.get(i3);
                list.set(i3, list.get(i));
                list.set(i, student);
            }
            i++;
        }
        int i4 = i3 + 1;
        Student student2 = (Student) list.get(i4);
        list.set(i4, list.get(i2));
        list.set(i2, student2);
        return i4;
    }

    /* access modifiers changed from: private */
    public void sort(List<Student> list, int i, int i2) {
        if (i < i2) {
            int partition = partition(list, i, i2);
            sort(list, i, partition - 1);
            sort(list, partition + 1, i2);
        }
    }
}
