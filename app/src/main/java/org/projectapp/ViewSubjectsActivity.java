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
import org.projectapp.Adapter.SubjectsAdapter;
import org.projectapp.Model.Subject;

public class ViewSubjectsActivity extends AppCompatActivity {
    Adapter adapter;
    String department;
    CircleImageView icon;
    LinearLayoutManager layoutManager;
    DatabaseReference mDatabase;
    ProgressDialog progressDialog;
    String[] semArray;
    String semester;
    AppCompatSpinner subject_semester;
    RecyclerView subjects_list;
    AppCompatTextView title;
    List<Subject> uploads;

    /* access modifiers changed from: protected */
    public void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        setContentView((int) R.layout.activity_view_subjects);
        this.layoutManager = new LinearLayoutManager(this);
        this.subjects_list = (RecyclerView) findViewById(R.id.subjects_list);
        this.subjects_list.setHasFixedSize(true);
        this.subjects_list.setLayoutManager(this.layoutManager);
        this.subject_semester = (AppCompatSpinner) findViewById(R.id.subject_semester);
        this.title = (AppCompatTextView) findViewById(R.id.title);
        Calendar instance = Calendar.getInstance();
        this.title.setText("Subjects");
        this.department = getIntent().getStringExtra("department");
        String str = "Select Semester";
        if (this.department.equals("Computer Department")) {
            String str2 = "CO2I";
            if (instance.get(2) < instance.get(5) || instance.get(2) == 11) {
                this.semArray = new String[]{str, str2, "CO4I", "CO6I"};
            } else {
                this.semArray = new String[]{str, "CO1I", str2, "CO3I"};
            }
        } else if (this.department.equals("IT Department")) {
            String str3 = "IF2I";
            if (instance.get(2) < instance.get(5) || instance.get(2) == 11) {
                this.semArray = new String[]{str, str3, "IF4I", "IF6I"};
            } else {
                this.semArray = new String[]{str, "IF1I", str3, "IF3I"};
            }
        } else {
            String str4 = "CE3I";
            String str5 = "CE1I";
            String str6 = "CE6I";
            String str7 = "CE4I";
            String str8 = "CE2I";
            if (this.department.equals("Civil I Shift Department")) {
                if (instance.get(2) < instance.get(5) || instance.get(2) == 11) {
                    this.semArray = new String[]{str, str8, str7, str6};
                } else {
                    this.semArray = new String[]{str, str5, str8, str4};
                }
            } else if (!this.department.equals("Civil II Shift Department")) {
                String str9 = "ME1I";
                String str10 = "ME6I";
                String str11 = "ME4I";
                String str12 = "ME2I";
                if (this.department.equals("Mechanical I Shift Department")) {
                    if (instance.get(2) < instance.get(5) || instance.get(2) == 11) {
                        this.semArray = new String[]{str, str12, str11, str10};
                    } else {
                        this.semArray = new String[]{str, str9, str12, "ME3I"};
                    }
                } else if (this.department.equals("Mechanical II Shift Department")) {
                    if (instance.get(2) < instance.get(5) || instance.get(2) == 11) {
                        this.semArray = new String[]{str, str12, str11, str10};
                    } else {
                        this.semArray = new String[]{str, str9, str12, "ME3I"};
                    }
                } else if (this.department.equals("Chemical Department")) {
                    if (instance.get(2) < instance.get(5) || instance.get(2) == 11) {
                        this.semArray = new String[]{str, "CH2I", "CH4I", "CH6I"};
                    } else {
                        this.semArray = new String[]{str, "CH1I", "CH2I", "CH3I"};
                    }
                } else if (this.department.equals("TR Department")) {
                    if (instance.get(2) < instance.get(5) || instance.get(2) == 11) {
                        this.semArray = new String[]{str, "TR2G", "TR4G"};
                    } else {
                        this.semArray = new String[]{str, "TR1G", "TR2G", "TR3G"};
                    }
                }
            } else if (instance.get(2) < instance.get(5) || instance.get(2) == 11) {
                this.semArray = new String[]{str, str8, str7, str6};
            } else {
                this.semArray = new String[]{str, str5, str8, str4};
            }
        }
        this.subject_semester.setAdapter((SpinnerAdapter) new ArrayAdapter(this, R.layout.spinner_dialog, this.semArray));
        this.subject_semester.setOnItemSelectedListener(new OnItemSelectedListener() {
            public void onNothingSelected(AdapterView<?> adapterView) {
            }

            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long j) {
                if (adapterView.getItemAtPosition(i).equals("Select Semester")) {
                    ViewSubjectsActivity.this.semester = null;
                    return;
                }
                ViewSubjectsActivity.this.semester = adapterView.getItemAtPosition(i).toString();
                ViewSubjectsActivity viewSubjectsActivity = ViewSubjectsActivity.this;
                viewSubjectsActivity.progressDialog = new ProgressDialog(viewSubjectsActivity);
                ViewSubjectsActivity.this.progressDialog.setCancelable(false);
                ViewSubjectsActivity.this.uploads = new ArrayList();
                ViewSubjectsActivity.this.progressDialog.setMessage("Please wait...");
                ViewSubjectsActivity.this.progressDialog.show();
                ViewSubjectsActivity viewSubjectsActivity2 = ViewSubjectsActivity.this;
                FirebaseDatabase instance = FirebaseDatabase.getInstance();
                StringBuilder sb = new StringBuilder();
                sb.append(ViewSubjectsActivity.this.department);
                sb.append("/subjects/");
                sb.append(ViewSubjectsActivity.this.semester);
                viewSubjectsActivity2.mDatabase = instance.getReference(sb.toString());
                ViewSubjectsActivity.this.mDatabase.addValueEventListener(new ValueEventListener() {
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        ViewSubjectsActivity.this.progressDialog.dismiss();
                        ViewSubjectsActivity.this.uploads.clear();
                        for (DataSnapshot value : dataSnapshot.getChildren()) {
                            ViewSubjectsActivity.this.uploads.add((Subject) value.getValue(Subject.class));
                        }
                        if (ViewSubjectsActivity.this.uploads.size() == 0) {
                            Builder builder = new Builder(ViewSubjectsActivity.this);
                            builder.setTitle((CharSequence) "Alert");
                            builder.setMessage((CharSequence) "No Subjects Found!!!");
                            builder.setPositiveButton((CharSequence) "Ok", (OnClickListener) new OnClickListener() {
                                public void onClick(DialogInterface dialogInterface, int i) {
                                    ViewSubjectsActivity.this.onBackPressed();
                                }
                            });
                            builder.create();
                            if (ViewSubjectsActivity.this.getLifecycle().getCurrentState().isAtLeast(State.CREATED)) {
                                builder.show();
                            }
                        } else if (ViewSubjectsActivity.this.getLifecycle().getCurrentState().isAtLeast(State.INITIALIZED)) {
                            ViewSubjectsActivity.this.adapter = new SubjectsAdapter(ViewSubjectsActivity.this, ViewSubjectsActivity.this.uploads, ViewSubjectsActivity.this.department, ViewSubjectsActivity.this.semester);
                            ViewSubjectsActivity.this.subjects_list.setAdapter(ViewSubjectsActivity.this.adapter);
                        }
                    }

                    public void onCancelled(DatabaseError databaseError) {
                        ViewSubjectsActivity.this.progressDialog.dismiss();
                    }
                });
            }
        });
    }

    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }
}
