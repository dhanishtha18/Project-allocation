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
import java.util.Collections;
import java.util.List;
import org.projectapp.Adapter.StudentScoreAdapter;
import org.projectapp.Model.Student;

public class StudentsScoreActivity extends AppCompatActivity {
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
    RecyclerView unactive_list;
    List<Student> uploads;

    /* access modifiers changed from: protected */
    public void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        setContentView((int) R.layout.activity_students_score);
        this.layoutManager = new LinearLayoutManager(this);
        this.unactive_list = (RecyclerView) findViewById(R.id.unactive_list);
        this.student_department = (AppCompatSpinner) findViewById(R.id.student_department);
        this.student_semester = (AppCompatSpinner) findViewById(R.id.student_semester);
        this.unactive_list.setHasFixedSize(true);
        this.unactive_list.setLayoutManager(this.layoutManager);
        this.title = (AppCompatTextView) findViewById(R.id.title);
        this.title.setText("Students Score");
        this.student_department.setAdapter((SpinnerAdapter) new ArrayAdapter(this, R.layout.spinner_dialog, new String[]{"Select Department", "Computer Department", "IT Department", "Mechanical I Shift Department", "Mechanical II Shift Department", "Civil I Shift Department", "Civil II Shift Department", "TR Department", "Chemical Department"}));
        this.student_department.setOnItemSelectedListener(new OnItemSelectedListener() {
            public void onNothingSelected(AdapterView<?> adapterView) {
            }

            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long j) {
                if (adapterView.getItemAtPosition(i).equals("Select Department")) {
                    StudentsScoreActivity.this.department = null;
                    return;
                }
                StudentsScoreActivity.this.department = adapterView.getItemAtPosition(i).toString();
                StudentsScoreActivity.this.view();
            }
        });
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
                    StudentsScoreActivity.this.semester = null;
                    return;
                }
                StudentsScoreActivity.this.semester = adapterView.getItemAtPosition(i).toString();
                StudentsScoreActivity studentsScoreActivity = StudentsScoreActivity.this;
                studentsScoreActivity.progressDialog = new ProgressDialog(studentsScoreActivity);
                StudentsScoreActivity.this.progressDialog.setCancelable(false);
                StudentsScoreActivity.this.uploads = new ArrayList();
                StudentsScoreActivity.this.progressDialog.setMessage("Please wait...");
                StudentsScoreActivity.this.progressDialog.show();
                StudentsScoreActivity studentsScoreActivity2 = StudentsScoreActivity.this;
                FirebaseDatabase instance = FirebaseDatabase.getInstance();
                StringBuilder sb = new StringBuilder();
                sb.append("Students/");
                sb.append(StudentsScoreActivity.this.department);
                studentsScoreActivity2.mDatabase = instance.getReference(sb.toString());
                StudentsScoreActivity.this.mDatabase.child(StudentsScoreActivity.this.semester).addValueEventListener(new ValueEventListener() {
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        StudentsScoreActivity.this.progressDialog.dismiss();
                        StudentsScoreActivity.this.uploads.clear();
                        for (DataSnapshot value : dataSnapshot.getChildren()) {
                            StudentsScoreActivity.this.uploads.add((Student) value.getValue(Student.class));
                        }
                        if (StudentsScoreActivity.this.uploads.size() == 0) {
                            Builder builder = new Builder(StudentsScoreActivity.this);
                            builder.setTitle((CharSequence) "Alert");
                            builder.setMessage((CharSequence) "No Students Found!!!");
                            builder.setPositiveButton((CharSequence) "Ok", (OnClickListener) new OnClickListener() {
                                public void onClick(DialogInterface dialogInterface, int i) {
                                    StudentsScoreActivity.this.onBackPressed();
                                }
                            });
                            builder.create();
                            if (StudentsScoreActivity.this.getLifecycle().getCurrentState().isAtLeast(State.CREATED)) {
                                builder.show();
                                return;
                            }
                            return;
                        }
                        StudentsScoreActivity.this.sort(StudentsScoreActivity.this.uploads, 0, StudentsScoreActivity.this.uploads.size() - 1);
                        Collections.reverse(StudentsScoreActivity.this.uploads);
                        if (StudentsScoreActivity.this.getLifecycle().getCurrentState().isAtLeast(State.INITIALIZED)) {
                            StudentsScoreActivity.this.adapter = new StudentScoreAdapter(StudentsScoreActivity.this, StudentsScoreActivity.this.uploads);
                            StudentsScoreActivity.this.unactive_list.setAdapter(StudentsScoreActivity.this.adapter);
                        }
                    }

                    public void onCancelled(DatabaseError databaseError) {
                        StudentsScoreActivity.this.progressDialog.dismiss();
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
        if (((Student) list.get(i2)).getSem1() != null) {
            float parseFloat = Float.parseFloat(((Student) list.get(i2)).getSem1().trim());
            int i3 = i - 1;
            while (i < i2) {
                if (((Student) list.get(i)).getSem1() != null && Float.parseFloat(((Student) list.get(i)).getSem1().trim()) < parseFloat) {
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
        } else if (((Student) list.get(i2)).getSem2() != null) {
            float parseFloat2 = Float.parseFloat(((Student) list.get(i2)).getSem2().trim());
            int i5 = i - 1;
            while (i < i2) {
                if (((Student) list.get(i)).getSem2() != null && Float.parseFloat(((Student) list.get(i)).getSem2().trim()) < parseFloat2) {
                    i5++;
                    Student student3 = (Student) list.get(i5);
                    list.set(i5, list.get(i));
                    list.set(i, student3);
                }
                i++;
            }
            int i6 = i5 + 1;
            Student student4 = (Student) list.get(i6);
            list.set(i6, list.get(i2));
            list.set(i2, student4);
            return i6;
        } else if (((Student) list.get(i2)).getSem3() != null) {
            float parseFloat3 = Float.parseFloat(((Student) list.get(i2)).getSem3().trim());
            int i7 = i - 1;
            while (i < i2) {
                if (((Student) list.get(i)).getSem3() != null && Float.parseFloat(((Student) list.get(i)).getSem3().trim()) < parseFloat3) {
                    i7++;
                    Student student5 = (Student) list.get(i7);
                    list.set(i7, list.get(i));
                    list.set(i, student5);
                }
                i++;
            }
            int i8 = i7 + 1;
            Student student6 = (Student) list.get(i8);
            list.set(i8, list.get(i2));
            list.set(i2, student6);
            return i8;
        } else if (((Student) list.get(i2)).getSem4() != null) {
            float parseFloat4 = Float.parseFloat(((Student) list.get(i2)).getSem4().trim());
            int i9 = i - 1;
            while (i < i2) {
                if (((Student) list.get(i)).getSem4() != null && Float.parseFloat(((Student) list.get(i)).getSem4().trim()) < parseFloat4) {
                    i9++;
                    Student student7 = (Student) list.get(i9);
                    list.set(i9, list.get(i));
                    list.set(i, student7);
                }
                i++;
            }
            int i10 = i9 + 1;
            Student student8 = (Student) list.get(i10);
            list.set(i10, list.get(i2));
            list.set(i2, student8);
            return i10;
        } else if (((Student) list.get(i2)).getSem5() != null) {
            float parseFloat5 = Float.parseFloat(((Student) list.get(i2)).getSem5().trim());
            int i11 = i - 1;
            while (i < i2) {
                if (((Student) list.get(i)).getSem5() != null && Float.parseFloat(((Student) list.get(i)).getSem5().trim()) < parseFloat5) {
                    i11++;
                    Student student9 = (Student) list.get(i11);
                    list.set(i11, list.get(i));
                    list.set(i, student9);
                }
                i++;
            }
            int i12 = i11 + 1;
            Student student10 = (Student) list.get(i12);
            list.set(i12, list.get(i2));
            list.set(i2, student10);
            return i12;
        } else if (((Student) list.get(i2)).getSem6() != null) {
            float parseFloat6 = Float.parseFloat(((Student) list.get(i2)).getSem6().trim());
            int i13 = i - 1;
            while (i < i2) {
                if (((Student) list.get(i)).getSem6() != null && Float.parseFloat(((Student) list.get(i)).getSem6().trim()) < parseFloat6) {
                    i13++;
                    Student student11 = (Student) list.get(i13);
                    list.set(i13, list.get(i));
                    list.set(i, student11);
                }
                i++;
            }
            int i14 = i13 + 1;
            Student student12 = (Student) list.get(i14);
            list.set(i14, list.get(i2));
            list.set(i2, student12);
            return i14;
        } else {
            float parseFloat7 = Float.parseFloat(((Student) list.get(i2)).getRoll_no().trim());
            int i15 = i - 1;
            while (i < i2) {
                if (((Student) list.get(i)).getRoll_no() != null && Float.parseFloat(((Student) list.get(i)).getRoll_no().trim()) < parseFloat7) {
                    i15++;
                    Student student13 = (Student) list.get(i15);
                    list.set(i15, list.get(i));
                    list.set(i, student13);
                }
                i++;
            }
            int i16 = i15 + 1;
            Student student14 = (Student) list.get(i16);
            list.set(i16, list.get(i2));
            list.set(i2, student14);
            return i16;
        }
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
