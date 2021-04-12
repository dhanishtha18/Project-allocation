package org.projectapp;

import android.content.DialogInterface;
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
import androidx.appcompat.widget.AppCompatEditText;
import androidx.appcompat.widget.AppCompatSpinner;
import androidx.appcompat.widget.AppCompatTextView;
import androidx.lifecycle.Lifecycle.State;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import de.hdodenhof.circleimageview.CircleImageView;
import java.util.Calendar;
import java.util.HashMap;
import org.apache.poi.openxml4j.opc.PackageRelationship;

public class AddSubjectsActivity extends AppCompatActivity {
    String department;
    CircleImageView icon;
    String[] semArray;
    String semester;
    AppCompatButton subject_add;
    AppCompatEditText subject_code;
    AppCompatEditText subject_name;
    AppCompatSpinner subject_semester;
    AppCompatSpinner subject_type;
    AppCompatTextView title;
    String type;

    /* access modifiers changed from: protected */
    public void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        setContentView((int) R.layout.activity_add_subjects);
        this.department = getIntent().getStringExtra("department");
        this.subject_semester = (AppCompatSpinner) findViewById(R.id.subject_semester);
        this.subject_name = (AppCompatEditText) findViewById(R.id.subject_name);
        this.subject_add = (AppCompatButton) findViewById(R.id.subject_add);
        this.subject_code = (AppCompatEditText) findViewById(R.id.subject_code);
        this.title = (AppCompatTextView) findViewById(R.id.title);
        this.subject_type = (AppCompatSpinner) findViewById(R.id.subject_type);
        Calendar instance = Calendar.getInstance();
        this.title.setText("Add Subjects");
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
        this.subject_type.setAdapter((SpinnerAdapter) new ArrayAdapter(this, R.layout.spinner_dialog, new String[]{PackageRelationship.TYPE_ATTRIBUTE_NAME, "TH", "PA", "TU"}));
        this.subject_semester.setAdapter((SpinnerAdapter) new ArrayAdapter(this, R.layout.spinner_dialog, this.semArray));
        this.subject_semester.setOnItemSelectedListener(new OnItemSelectedListener() {
            public void onNothingSelected(AdapterView<?> adapterView) {
            }

            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long j) {
                if (adapterView.getItemAtPosition(i).equals("Select Semester")) {
                    AddSubjectsActivity.this.semester = null;
                    return;
                }
                AddSubjectsActivity.this.semester = adapterView.getItemAtPosition(i).toString();
            }
        });
        this.subject_type.setOnItemSelectedListener(new OnItemSelectedListener() {
            public void onNothingSelected(AdapterView<?> adapterView) {
            }

            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long j) {
                if (adapterView.getItemAtPosition(i).equals(PackageRelationship.TYPE_ATTRIBUTE_NAME)) {
                    AddSubjectsActivity.this.type = null;
                    return;
                }
                AddSubjectsActivity.this.type = adapterView.getItemAtPosition(i).toString();
            }
        });
        this.subject_add.setOnClickListener(new OnClickListener() {
            public void onClick(View view) {
                String str = "Ok";
                String str2 = "Alert";
                if (AddSubjectsActivity.this.semester == null) {
                    Builder builder = new Builder(AddSubjectsActivity.this);
                    builder.setTitle((CharSequence) str2);
                    builder.setMessage((CharSequence) "Select Semester");
                    builder.setPositiveButton((CharSequence) str, (DialogInterface.OnClickListener) new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialogInterface, int i) {
                            AddSubjectsActivity.this.subject_semester.requestFocus();
                        }
                    });
                    builder.create();
                    if (AddSubjectsActivity.this.getLifecycle().getCurrentState().isAtLeast(State.CREATED)) {
                        builder.show();
                    }
                } else if (AddSubjectsActivity.this.subject_name.getText().toString().isEmpty()) {
                    Builder builder2 = new Builder(AddSubjectsActivity.this);
                    builder2.setTitle((CharSequence) str2);
                    builder2.setMessage((CharSequence) "Enter Subject Name");
                    builder2.setPositiveButton((CharSequence) str, (DialogInterface.OnClickListener) new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialogInterface, int i) {
                            AddSubjectsActivity.this.subject_name.requestFocus();
                        }
                    });
                    builder2.create();
                    if (AddSubjectsActivity.this.getLifecycle().getCurrentState().isAtLeast(State.CREATED)) {
                        builder2.show();
                    }
                } else if (AddSubjectsActivity.this.subject_code.getText().toString().isEmpty()) {
                    Builder builder3 = new Builder(AddSubjectsActivity.this);
                    builder3.setTitle((CharSequence) str2);
                    builder3.setMessage((CharSequence) "Enter Subject Code");
                    builder3.setPositiveButton((CharSequence) str, (DialogInterface.OnClickListener) new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialogInterface, int i) {
                            AddSubjectsActivity.this.subject_code.requestFocus();
                        }
                    });
                    builder3.create();
                    if (AddSubjectsActivity.this.getLifecycle().getCurrentState().isAtLeast(State.CREATED)) {
                        builder3.show();
                    }
                } else if (AddSubjectsActivity.this.type == null) {
                    Builder builder4 = new Builder(AddSubjectsActivity.this);
                    builder4.setTitle((CharSequence) str2);
                    builder4.setMessage((CharSequence) "Select Subject Type");
                    builder4.setPositiveButton((CharSequence) str, (DialogInterface.OnClickListener) new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialogInterface, int i) {
                            AddSubjectsActivity.this.subject_type.requestFocus();
                        }
                    });
                    builder4.create();
                    if (AddSubjectsActivity.this.getLifecycle().getCurrentState().isAtLeast(State.CREATED)) {
                        builder4.show();
                    }
                } else {
                    AddSubjectsActivity addSubjectsActivity = AddSubjectsActivity.this;
                    addSubjectsActivity.onAddSubject(addSubjectsActivity.subject_name.getText().toString().trim(), AddSubjectsActivity.this.subject_code.getText().toString().trim(), AddSubjectsActivity.this.department, AddSubjectsActivity.this.semester);
                }
            }
        });
    }

    /* access modifiers changed from: private */
    public void onAddSubject(String str, String str2, String str3, String str4) {
        FirebaseDatabase instance = FirebaseDatabase.getInstance();
        StringBuilder sb = new StringBuilder();
        sb.append(str3);
        sb.append("/subjects/");
        sb.append(str4);
        DatabaseReference reference = instance.getReference(sb.toString());
        String key = reference.push().getKey();
        HashMap hashMap = new HashMap();
        StringBuilder sb2 = new StringBuilder();
        sb2.append(str);
        sb2.append(" (");
        sb2.append(str2);
        sb2.append(") ");
        sb2.append(this.type);
        hashMap.put("name", sb2.toString());
        hashMap.put("id", key);
        reference.child(key).setValue(hashMap);
        Toast.makeText(this, "Subject Added Successfully", Toast.LENGTH_SHORT).show();
        String str5 = "";
        this.subject_name.setText(str5);
        this.subject_code.setText(str5);
        this.subject_semester.setSelection(0);
        this.subject_type.setSelection(0);
    }

    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }
}
