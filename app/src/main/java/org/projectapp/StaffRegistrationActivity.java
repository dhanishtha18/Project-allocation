package org.projectapp;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.util.Patterns;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.SpinnerAdapter;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;
import androidx.appcompat.widget.AppCompatEditText;
import androidx.appcompat.widget.AppCompatSpinner;
import androidx.appcompat.widget.AppCompatTextView;
import androidx.exifinterface.media.ExifInterface;
import androidx.lifecycle.Lifecycle;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.SignInMethodQueryResult;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Iterator;
import java.util.Random;
import org.projectapp.Model.Staff;
import org.projectapp.Model.Subject;

import de.hdodenhof.circleimageview.CircleImageView;

public class StaffRegistrationActivity extends AppCompatActivity {
    RadioGroup Staff_mode;
    String admin_email;
    String admin_password;
    String department;
    CircleImageView icon;
    Boolean isNotDone = true;
    String mode = null;
    ProgressDialog progressDialog;
    String[] semArray;
    String semester;
    String slots;
    AppCompatButton staff_add_subjects;
    AppCompatEditText staff_email;
    AppCompatEditText staff_name;
    AppCompatEditText staff_phone;
    AppCompatButton staff_register;
    AppCompatSpinner staff_semester;
    AppCompatSpinner staff_subject_hours;
    AppCompatSpinner staff_subjects;
    String subject;
    ArrayList<String> subjectList;
    ArrayList<String> subjects;
    AppCompatTextView title;

    /* access modifiers changed from: protected */
    public void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        setContentView((int) R.layout.activity_staff_registration);
        this.staff_name = (AppCompatEditText) findViewById(R.id.staff_name);
        this.staff_email = (AppCompatEditText) findViewById(R.id.staff_email);
        this.staff_phone = (AppCompatEditText) findViewById(R.id.staff_phone);
        this.staff_subject_hours = (AppCompatSpinner) findViewById(R.id.staff_subject_hours);
        this.staff_semester = (AppCompatSpinner) findViewById(R.id.staff_semester);
        this.staff_subjects = (AppCompatSpinner) findViewById(R.id.staff_subjects);
        this.staff_register = (AppCompatButton) findViewById(R.id.staff_register);
        this.staff_add_subjects = (AppCompatButton) findViewById(R.id.staff_add_subjects);
        this.Staff_mode = (RadioGroup) findViewById(R.id.mode);
        this.title = (AppCompatTextView) findViewById(R.id.title);
        this.subjects = new ArrayList<>();
        this.subjectList = new ArrayList<>();
        this.admin_email = getIntent().getStringExtra("admin_email");
        this.admin_password = getIntent().getStringExtra("admin_password");
        this.department = getIntent().getStringExtra("department");
        this.title.setText("Register Staff");
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
        this.staff_semester.setAdapter((SpinnerAdapter) new ArrayAdapter(this, R.layout.spinner_dialog, this.semArray));
        this.staff_subject_hours.setAdapter((SpinnerAdapter) new ArrayAdapter(this, R.layout.spinner_dialog, new String[]{"Hours", "1", ExifInterface.GPS_MEASUREMENT_2D, ExifInterface.GPS_MEASUREMENT_3D, "4", "5"}));
        this.staff_subjects.setAdapter((SpinnerAdapter) new ArrayAdapter(this, R.layout.spinner_dialog, new String[]{"Subjects"}));
        this.Staff_mode.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            public void onCheckedChanged(RadioGroup radioGroup, int i) {
                StaffRegistrationActivity.this.mode = ((RadioButton) StaffRegistrationActivity.this.findViewById(i)).getText().toString();
            }
        });
        this.staff_semester.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            public void onNothingSelected(AdapterView<?> adapterView) {
            }

            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long j) {
                if (adapterView.getItemAtPosition(i).equals("Select Semester")) {
                    StaffRegistrationActivity staffRegistrationActivity = StaffRegistrationActivity.this;
                    staffRegistrationActivity.semester = null;
                    staffRegistrationActivity.staff_subjects.setAdapter((SpinnerAdapter) new ArrayAdapter(StaffRegistrationActivity.this, R.layout.spinner_dialog, new String[]{"Subjects"}));
                    return;
                }
                StaffRegistrationActivity.this.semester = adapterView.getItemAtPosition(i).toString();
                StaffRegistrationActivity.this.subjectList.clear();
                FirebaseDatabase instance = FirebaseDatabase.getInstance();
                instance.getReference(StaffRegistrationActivity.this.department + "/subjects/" + StaffRegistrationActivity.this.semester).addValueEventListener(new ValueEventListener() {
                    public void onCancelled(@NonNull DatabaseError databaseError) {
                    }

                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        for (DataSnapshot value : dataSnapshot.getChildren()) {
                            StaffRegistrationActivity.this.subjectList.add(((Subject) value.getValue(Subject.class)).getName());
                        }
                        StaffRegistrationActivity.this.staff_subjects.setAdapter((SpinnerAdapter) new ArrayAdapter(StaffRegistrationActivity.this, R.layout.spinner_dialog, StaffRegistrationActivity.this.subjectList));
                    }
                });
            }
        });
        this.staff_subject_hours.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            public void onNothingSelected(AdapterView<?> adapterView) {
            }

            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long j) {
                if (adapterView.getItemAtPosition(i).equals("Hours")) {
                    StaffRegistrationActivity.this.slots = null;
                    return;
                }
                StaffRegistrationActivity.this.slots = adapterView.getItemAtPosition(i).toString();
            }
        });
        this.staff_subjects.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            public void onNothingSelected(AdapterView<?> adapterView) {
            }

            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long j) {
                if (adapterView.getItemAtPosition(i).equals("Subject")) {
                    StaffRegistrationActivity.this.subject = null;
                    return;
                }
                StaffRegistrationActivity.this.subject = adapterView.getItemAtPosition(i).toString();
            }
        });
        this.staff_add_subjects.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                if (StaffRegistrationActivity.this.semester == null || StaffRegistrationActivity.this.subject == null || StaffRegistrationActivity.this.slots == null) {
                    AlertDialog.Builder builder = new AlertDialog.Builder(StaffRegistrationActivity.this);
                    builder.setTitle((CharSequence) "Alert");
                    builder.setMessage((CharSequence) "Please Select Subject Fields.");
                    builder.setPositiveButton((CharSequence) "Ok", (DialogInterface.OnClickListener) null);
                    builder.create();
                    if (StaffRegistrationActivity.this.getLifecycle().getCurrentState().isAtLeast(Lifecycle.State.CREATED)) {
                        builder.show();
                        return;
                    }
                    return;
                }
                ArrayList<String> arrayList = StaffRegistrationActivity.this.subjects;
                arrayList.add(StaffRegistrationActivity.this.subject + " " + StaffRegistrationActivity.this.slots + " " + StaffRegistrationActivity.this.semester);
                StaffRegistrationActivity.this.staff_subjects.setSelection(0);
                StaffRegistrationActivity.this.staff_semester.setSelection(0);
                StaffRegistrationActivity.this.staff_subject_hours.setSelection(0);
            }
        });
        this.staff_register.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                if (!(StaffRegistrationActivity.this.semester == null || StaffRegistrationActivity.this.subject == null || StaffRegistrationActivity.this.slots == null)) {
                    ArrayList<String> arrayList = StaffRegistrationActivity.this.subjects;
                    arrayList.add(StaffRegistrationActivity.this.subject + " " + StaffRegistrationActivity.this.slots + " " + StaffRegistrationActivity.this.semester);
                    StaffRegistrationActivity.this.staff_subjects.setSelection(0);
                    StaffRegistrationActivity.this.staff_semester.setSelection(0);
                    StaffRegistrationActivity.this.staff_subject_hours.setSelection(0);
                }
                if (StaffRegistrationActivity.this.staff_name.getText().toString().isEmpty()) {
                    AlertDialog.Builder builder = new AlertDialog.Builder(StaffRegistrationActivity.this);
                    builder.setTitle((CharSequence) "Alert");
                    builder.setMessage((CharSequence) "Enter Staff Full Name");
                    builder.setPositiveButton((CharSequence) "Ok", (DialogInterface.OnClickListener) new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialogInterface, int i) {
                            StaffRegistrationActivity.this.staff_name.requestFocus();
                        }
                    });
                    builder.create();
                    if (StaffRegistrationActivity.this.getLifecycle().getCurrentState().isAtLeast(Lifecycle.State.CREATED)) {
                        builder.show();
                    }
                } else if (StaffRegistrationActivity.this.staff_phone.getText().toString().isEmpty()) {
                    AlertDialog.Builder builder2 = new AlertDialog.Builder(StaffRegistrationActivity.this);
                    builder2.setTitle((CharSequence) "Alert");
                    builder2.setMessage((CharSequence) "Enter Staff Phone No.");
                    builder2.setPositiveButton((CharSequence) "Ok", (DialogInterface.OnClickListener) new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialogInterface, int i) {
                            StaffRegistrationActivity.this.staff_phone.requestFocus();
                        }
                    });
                    builder2.create();
                    if (StaffRegistrationActivity.this.getLifecycle().getCurrentState().isAtLeast(Lifecycle.State.CREATED)) {
                        builder2.show();
                    }
                } else if (StaffRegistrationActivity.this.staff_phone.getText().toString().length() != 10) {
                    AlertDialog.Builder builder3 = new AlertDialog.Builder(StaffRegistrationActivity.this);
                    builder3.setTitle((CharSequence) "Alert");
                    builder3.setMessage((CharSequence) "Enter valid Phone No.");
                    builder3.setPositiveButton((CharSequence) "Ok", (DialogInterface.OnClickListener) new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialogInterface, int i) {
                            StaffRegistrationActivity.this.staff_phone.requestFocus();
                        }
                    });
                    builder3.create();
                    if (StaffRegistrationActivity.this.getLifecycle().getCurrentState().isAtLeast(Lifecycle.State.CREATED)) {
                        builder3.show();
                    }
                } else if (StaffRegistrationActivity.this.staff_email.getText().toString().isEmpty()) {
                    AlertDialog.Builder builder4 = new AlertDialog.Builder(StaffRegistrationActivity.this);
                    builder4.setTitle((CharSequence) "Alert");
                    builder4.setMessage((CharSequence) "Enter Staff Email Address");
                    builder4.setPositiveButton((CharSequence) "Ok", (DialogInterface.OnClickListener) new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialogInterface, int i) {
                            StaffRegistrationActivity.this.staff_email.requestFocus();
                        }
                    });
                    builder4.create();
                    if (StaffRegistrationActivity.this.getLifecycle().getCurrentState().isAtLeast(Lifecycle.State.CREATED)) {
                        builder4.show();
                    }
                } else if (!Patterns.EMAIL_ADDRESS.matcher(StaffRegistrationActivity.this.staff_email.getText().toString()).matches()) {
                    AlertDialog.Builder builder5 = new AlertDialog.Builder(StaffRegistrationActivity.this);
                    builder5.setTitle((CharSequence) "Alert");
                    builder5.setMessage((CharSequence) "Enter Valid Email Address");
                    builder5.setPositiveButton((CharSequence) "Ok", (DialogInterface.OnClickListener) new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialogInterface, int i) {
                            StaffRegistrationActivity.this.staff_email.requestFocus();
                        }
                    });
                    builder5.create();
                    if (StaffRegistrationActivity.this.getLifecycle().getCurrentState().isAtLeast(Lifecycle.State.CREATED)) {
                        builder5.show();
                    }
                } else if (StaffRegistrationActivity.this.mode == null) {
                    AlertDialog.Builder builder6 = new AlertDialog.Builder(StaffRegistrationActivity.this);
                    builder6.setTitle((CharSequence) "Alert");
                    builder6.setMessage((CharSequence) "Select Mode.");
                    builder6.setPositiveButton((CharSequence) "Ok", (DialogInterface.OnClickListener) new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialogInterface, int i) {
                            StaffRegistrationActivity.this.staff_email.requestFocus();
                        }
                    });
                    builder6.create();
                    if (StaffRegistrationActivity.this.getLifecycle().getCurrentState().isAtLeast(Lifecycle.State.CREATED)) {
                        builder6.show();
                    }
                } else {
                    if (StaffRegistrationActivity.this.subjects.size() == 0) {
                        StaffRegistrationActivity.this.subjects.add("none");
                    }
                    StaffRegistrationActivity staffRegistrationActivity = StaffRegistrationActivity.this;
                    staffRegistrationActivity.onStaffRegistration(staffRegistrationActivity.staff_name.getText().toString(), StaffRegistrationActivity.this.staff_email.getText().toString(), StaffRegistrationActivity.this.staff_phone.getText().toString(), StaffRegistrationActivity.this.department, StaffRegistrationActivity.this.subjects, StaffRegistrationActivity.this.mode);
                }
            }
        });
    }

    /* access modifiers changed from: private */
    public void onStaffRegistration(String str, String str2, String str3, String str4, ArrayList<String> arrayList, String str5) {
        this.progressDialog = new ProgressDialog(this);
        this.progressDialog.setCancelable(false);
        this.progressDialog.setMessage("Registering Staff...");
        this.progressDialog.show();
        final String str6 = str;
        final String str7 = str3;
        final String str8 = str4;
        final String generateLoginID = generateLoginID(str, str8, str7);
        final String valueOf = String.valueOf(generatePassword());
        final String str9 = str2;
        final String str10 = str5;
        final ArrayList<String> arrayList2 = arrayList;
        FirebaseAuth.getInstance().fetchSignInMethodsForEmail(str2).addOnCompleteListener(new OnCompleteListener<SignInMethodQueryResult>() {
            public void onComplete(@NonNull Task<SignInMethodQueryResult> task) {
                if (task.getResult().getSignInMethods().isEmpty()) {
                    FirebaseAuth.getInstance().signOut();
                    FirebaseAuth.getInstance().createUserWithEmailAndPassword(str9, valueOf.trim()).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if (task.isSuccessful()) {
                                FirebaseDatabase instance = FirebaseDatabase.getInstance();
                                DatabaseReference child = instance.getReference("Staff/" + str8).child(FirebaseAuth.getInstance().getCurrentUser().getUid());
                                Staff staff = new Staff(str6, str9, str7, generateLoginID.toLowerCase().trim(), valueOf.trim(), str8, str10, FirebaseAuth.getInstance().getCurrentUser().getUid(), "offline", "deactivated", str6.toLowerCase(), "default", (String) null, (String) null, (String) null, arrayList2);
                                child.setValue(staff).addOnSuccessListener(new OnSuccessListener<Void>() {
                                    public void onSuccess(Void voidR) {
                                        FirebaseAuth.getInstance().signOut();
                                        FirebaseAuth.getInstance().signInWithEmailAndPassword(StaffRegistrationActivity.this.admin_email, StaffRegistrationActivity.this.admin_password);
                                        AlertDialog.Builder builder = new AlertDialog.Builder(StaffRegistrationActivity.this);
                                        builder.setTitle((CharSequence) "Registration Successful");
                                        builder.setMessage((CharSequence) "Login ID = " + generateLoginID + "\nTemporary Password = " + valueOf);
                                        builder.setPositiveButton((CharSequence) "Ok", (DialogInterface.OnClickListener) new DialogInterface.OnClickListener() {
                                            public void onClick(DialogInterface dialogInterface, int i) {
                                                StaffRegistrationActivity.this.staff_name.setText("");
                                                StaffRegistrationActivity.this.staff_phone.setText("");
                                                StaffRegistrationActivity.this.staff_email.setText("");
                                                StaffRegistrationActivity.this.recreate();
                                            }
                                        });
                                        builder.create();
                                        if (StaffRegistrationActivity.this.getLifecycle().getCurrentState().isAtLeast(Lifecycle.State.CREATED)) {
                                            builder.show();
                                        } else {
                                            FirebaseAuth.getInstance().signInWithEmailAndPassword(StaffRegistrationActivity.this.admin_email, StaffRegistrationActivity.this.admin_password);
                                        }
                                        Toast.makeText(StaffRegistrationActivity.this, "Staff Registered Successfully", Toast.LENGTH_SHORT).show();
                                    }
                                }).addOnFailureListener(new OnFailureListener() {
                                    public void onFailure(@NonNull Exception exc) {
                                        FirebaseAuth.getInstance().signOut();
                                        FirebaseAuth.getInstance().signInWithEmailAndPassword(StaffRegistrationActivity.this.admin_email, StaffRegistrationActivity.this.admin_password);
                                    }
                                });
                                return;
                            }
                            FirebaseAuth.getInstance().signInWithEmailAndPassword(StaffRegistrationActivity.this.admin_email, StaffRegistrationActivity.this.admin_password);
                        }
                    });
                    StaffRegistrationActivity.this.progressDialog.dismiss();
                    return;
                }
                StaffRegistrationActivity.this.staffExists(str6, str9, generateLoginID, str7, str8, arrayList2, str10);
            }
        });
    }

    /* access modifiers changed from: private */
    public void staffExists(String str, String str2, String str3, String str4, String str5, ArrayList<String> arrayList, String str6) {
        final String str7 = str2;
        final String str8 = str5;
        final String str9 = str;
        final String str10 = str4;
        final String str11 = str3;
        final String str12 = str6;
        final ArrayList<String> arrayList2 = arrayList;
        FirebaseDatabase.getInstance().getReference("Staff").addValueEventListener(new ValueEventListener() {
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                Iterator<DataSnapshot> it;
                Iterator<DataSnapshot> it2 = dataSnapshot.getChildren().iterator();
                while (it2.hasNext()) {
                    for (DataSnapshot value : it2.next().getChildren()) {
                        Staff staff = (Staff) value.getValue(Staff.class);
                        if (staff.getEmail().equals(str7)) {
                            StaffRegistrationActivity.this.isNotDone = false;
                            FirebaseDatabase instance = FirebaseDatabase.getInstance();
                            DatabaseReference child = instance.getReference("Staff/" + str8).child(staff.getId());
                            it = it2;
                            Staff staff2 = new Staff(str9, str7, str10, str11.toLowerCase().trim(), staff.getPassword(), str8, str12, staff.getId(), "offline", "deactivated", str9.toLowerCase(), "default", (String) null, (String) null, (String) null, arrayList2);
                            child.setValue(staff2);
                            AlertDialog.Builder builder = new AlertDialog.Builder(StaffRegistrationActivity.this);
                            builder.setTitle((CharSequence) "Registration Successful");
                            builder.setMessage((CharSequence) "Login ID = " + str11 + "\nTemporary Password = " + staff.getPassword());
                            builder.setPositiveButton((CharSequence) "Ok", (DialogInterface.OnClickListener) new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialogInterface, int i) {
                                    StaffRegistrationActivity.this.staff_name.setText("");
                                    StaffRegistrationActivity.this.staff_phone.setText("");
                                    StaffRegistrationActivity.this.staff_email.setText("");
                                    StaffRegistrationActivity.this.recreate();
                                }
                            });
                            builder.create();
                            if (StaffRegistrationActivity.this.getLifecycle().getCurrentState().isAtLeast(Lifecycle.State.CREATED)) {
                                builder.show();
                            }
                            Toast.makeText(StaffRegistrationActivity.this, "Staff Registered Successfully", Toast.LENGTH_SHORT).show();
                            StaffRegistrationActivity.this.progressDialog.dismiss();
                        } else {
                            it = it2;
                        }
                        it2 = it;
                    }
                }
                if (StaffRegistrationActivity.this.isNotDone.booleanValue()) {
                    StaffRegistrationActivity.this.progressDialog.dismiss();
                    AlertDialog.Builder builder2 = new AlertDialog.Builder(StaffRegistrationActivity.this);
                    builder2.setTitle((CharSequence) "Registration Error");
                    builder2.setMessage((CharSequence) "H.O.D Cannot be Registered as Staff");
                    builder2.setPositiveButton((CharSequence) "Ok", (DialogInterface.OnClickListener) new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialogInterface, int i) {
                            StaffRegistrationActivity.this.onBackPressed();
                        }
                    });
                    builder2.create();
                    if (StaffRegistrationActivity.this.getLifecycle().getCurrentState().isAtLeast(Lifecycle.State.CREATED)) {
                        builder2.show();
                    }
                }
            }

            public void onCancelled(@NonNull DatabaseError databaseError) {
                StaffRegistrationActivity.this.progressDialog.dismiss();
            }
        });
    }

    private char[] generatePassword() {
        char[] cArr = new char[8];
        Random random = new Random();
        for (int i = 0; i < 8; i++) {
            cArr[i] = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz1234567890!@#$%^&*()_+-=./<>".charAt(random.nextInt(80));
        }
        return cArr;
    }

    private String generateLoginID(String str, String str2, String str3) {
        StringBuilder sb = new StringBuilder();
        if (str2.equals("Computer Department")) {
            sb.append("CO");
        } else if (str2.equals("IT Department")) {
            sb.append("IT");
        } else if (str2.equals("TR Department")) {
            sb.append("TR");
        } else if (str2.equals("Civil I Shift Department")) {
            sb.append("CEI");
        } else if (str2.equals("Mechanical I Shift Department")) {
            sb.append("MEI");
        } else if (str2.equals("Civil II Shift Department")) {
            sb.append("CEII");
        } else if (str2.equals("Mechanical II Shift Department")) {
            sb.append("MEII");
        } else if (str2.equals("Chemical Department")) {
            sb.append("CH");
        }
        sb.append(Character.toUpperCase(str.charAt(0)));
        for (int i = 0; i < str.length(); i++) {
            if (str.charAt(i) == ' ') {
                sb.append(Character.toUpperCase(str.charAt(i + 1)));
            }
        }
        sb.append(str3.charAt(6));
        sb.append(str3.charAt(7));
        sb.append(str3.charAt(8));
        sb.append(str3.charAt(9));
        return String.valueOf(sb);
    }

    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }
}
