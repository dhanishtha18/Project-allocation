package org.projectapp;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.util.Patterns;
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

import org.projectapp.Model.Hod;

import java.util.ArrayList;
import java.util.Random;

import de.hdodenhof.circleimageview.CircleImageView;

public class HODRegistrationActivity extends AppCompatActivity {
    String department;
    AppCompatSpinner hod_department;
    AppCompatEditText hod_email;
    AppCompatEditText hod_name;
    AppCompatEditText hod_phone;
    AppCompatButton hod_register;
    CircleImageView icon;
    Boolean isNotDone = true;
    ProgressDialog progressDialog;
    ArrayList<String> subjects;
    AppCompatTextView title;

    /* access modifiers changed from: protected */
    public void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        setContentView((int) R.layout.activity_hodregistration);
        this.hod_name = (AppCompatEditText) findViewById(R.id.hod_name);
        this.hod_email = (AppCompatEditText) findViewById(R.id.hod_email);
        this.hod_phone = (AppCompatEditText) findViewById(R.id.hod_phone);
        this.hod_department = (AppCompatSpinner) findViewById(R.id.hod_department);
        this.hod_register = (AppCompatButton) findViewById(R.id.hod_register);
        this.title = (AppCompatTextView) findViewById(R.id.title);
        this.title.setText("Register H.O.D");
        this.subjects = new ArrayList<>();
        this.subjects.add("subject");
        this.hod_department.setAdapter((SpinnerAdapter) new ArrayAdapter(this, R.layout.spinner_dialog, new String[]{"Select Department", "Computer Department", "IT Department", "Mechanical I Shift Department", "Mechanical II Shift Department", "Civil I Shift Department", "Civil II Shift Department", "TR Department", "Chemical Department"}));
        this.hod_department.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            public void onNothingSelected(AdapterView<?> adapterView) {
            }

            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long j) {
                if (adapterView.getItemAtPosition(i).equals("Select Department")) {
                    HODRegistrationActivity.this.department = null;
                    return;
                }
                HODRegistrationActivity.this.department = adapterView.getItemAtPosition(i).toString();
            }
        });
        this.hod_register.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                if (HODRegistrationActivity.this.hod_name.getText().toString().isEmpty()) {
                    AlertDialog.Builder builder = new AlertDialog.Builder(HODRegistrationActivity.this);
                    builder.setTitle((CharSequence) "Alert");
                    builder.setMessage((CharSequence) "Enter H.O.D Full Name");
                    builder.setPositiveButton((CharSequence) "Ok", (DialogInterface.OnClickListener) new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialogInterface, int i) {
                            HODRegistrationActivity.this.hod_name.requestFocus();
                        }
                    });
                    builder.create();
                    if (HODRegistrationActivity.this.getLifecycle().getCurrentState().isAtLeast(Lifecycle.State.CREATED)) {
                        builder.show();
                    }
                } else if (HODRegistrationActivity.this.hod_phone.getText().toString().isEmpty()) {
                    AlertDialog.Builder builder2 = new AlertDialog.Builder(HODRegistrationActivity.this);
                    builder2.setTitle((CharSequence) "Alert");
                    builder2.setMessage((CharSequence) "Enter H.O.D Phone No.");
                    builder2.setPositiveButton((CharSequence) "Ok", (DialogInterface.OnClickListener) new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialogInterface, int i) {
                            HODRegistrationActivity.this.hod_phone.requestFocus();
                        }
                    });
                    builder2.create();
                    if (HODRegistrationActivity.this.getLifecycle().getCurrentState().isAtLeast(Lifecycle.State.CREATED)) {
                        builder2.show();
                    }
                } else if (HODRegistrationActivity.this.hod_phone.getText().toString().length() != 10) {
                    AlertDialog.Builder builder3 = new AlertDialog.Builder(HODRegistrationActivity.this);
                    builder3.setTitle((CharSequence) "Alert");
                    builder3.setMessage((CharSequence) "Enter valid Phone no.");
                    builder3.setPositiveButton((CharSequence) "Ok", (DialogInterface.OnClickListener) new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialogInterface, int i) {
                            HODRegistrationActivity.this.hod_phone.requestFocus();
                        }
                    });
                    builder3.create();
                    if (HODRegistrationActivity.this.getLifecycle().getCurrentState().isAtLeast(Lifecycle.State.CREATED)) {
                        builder3.show();
                    }
                } else if (HODRegistrationActivity.this.hod_email.getText().toString().isEmpty()) {
                    AlertDialog.Builder builder4 = new AlertDialog.Builder(HODRegistrationActivity.this);
                    builder4.setTitle((CharSequence) "Alert");
                    builder4.setMessage((CharSequence) "Enter H.O.D Email Address");
                    builder4.setPositiveButton((CharSequence) "Ok", (DialogInterface.OnClickListener) new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialogInterface, int i) {
                            HODRegistrationActivity.this.hod_email.requestFocus();
                        }
                    });
                    builder4.create();
                    if (HODRegistrationActivity.this.getLifecycle().getCurrentState().isAtLeast(Lifecycle.State.CREATED)) {
                        builder4.show();
                    }
                } else if (!Patterns.EMAIL_ADDRESS.matcher(HODRegistrationActivity.this.hod_email.getText().toString()).matches()) {
                    AlertDialog.Builder builder5 = new AlertDialog.Builder(HODRegistrationActivity.this);
                    builder5.setTitle((CharSequence) "Alert");
                    builder5.setMessage((CharSequence) "Enter Valid Email Address");
                    builder5.setPositiveButton((CharSequence) "Ok", (DialogInterface.OnClickListener) new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialogInterface, int i) {
                            HODRegistrationActivity.this.hod_email.requestFocus();
                        }
                    });
                    builder5.create();
                    if (HODRegistrationActivity.this.getLifecycle().getCurrentState().isAtLeast(Lifecycle.State.CREATED)) {
                        builder5.show();
                    }
                } else if (HODRegistrationActivity.this.department == null) {
                    AlertDialog.Builder builder6 = new AlertDialog.Builder(HODRegistrationActivity.this);
                    builder6.setTitle((CharSequence) "Alert");
                    builder6.setMessage((CharSequence) "Select Department");
                    builder6.setPositiveButton((CharSequence) "Ok", (DialogInterface.OnClickListener) new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialogInterface, int i) {
                            HODRegistrationActivity.this.hod_department.requestFocus();
                        }
                    });
                    builder6.create();
                    if (HODRegistrationActivity.this.getLifecycle().getCurrentState().isAtLeast(Lifecycle.State.CREATED)) {
                        builder6.show();
                    }
                } else {
                    HODRegistrationActivity hODRegistrationActivity = HODRegistrationActivity.this;
                    hODRegistrationActivity.onHodRegistration(hODRegistrationActivity.hod_name.getText().toString().trim(), HODRegistrationActivity.this.hod_email.getText().toString().trim(), HODRegistrationActivity.this.hod_phone.getText().toString().trim(), HODRegistrationActivity.this.department);
                }
            }
        });
    }

    /* access modifiers changed from: private */
    public void onHodRegistration(String str, String str2, String str3, String str4) {
        this.progressDialog = new ProgressDialog(this);
        this.progressDialog.setCancelable(false);
        this.progressDialog.setMessage("Registering H.O.D");
        this.progressDialog.show();
        final String generateLoginID = generateLoginID(str, str4, str3);
        final String valueOf = String.valueOf(generatePassword());
        final String str5 = str2;
        final String str6 = str4;
        final String str7 = str;
        final String str8 = str3;
        FirebaseDatabase.getInstance().getReference("HODs").child(str4).addListenerForSingleValueEvent(new ValueEventListener() {
            public void onCancelled(@NonNull DatabaseError databaseError) {
            }

            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.getChildrenCount() == 0) {
                    FirebaseAuth.getInstance().fetchSignInMethodsForEmail(str5).addOnCompleteListener(new OnCompleteListener<SignInMethodQueryResult>() {
                        public void onComplete(@NonNull Task<SignInMethodQueryResult> task) {
                            if (task.getResult().getSignInMethods().isEmpty()) {
                                FirebaseAuth.getInstance().createUserWithEmailAndPassword(str5, valueOf).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                                    public void onComplete(@NonNull Task<AuthResult> task) {
                                        if (task.isSuccessful()) {
                                            FirebaseDatabase instance = FirebaseDatabase.getInstance();
                                            DatabaseReference child = instance.getReference("HODs/" + str6).child(FirebaseAuth.getInstance().getCurrentUser().getUid());
                                            Hod hod = new Hod(str7, generateLoginID.toLowerCase().trim(), valueOf, str6, str5, str8, FirebaseAuth.getInstance().getCurrentUser().getUid(), "offline", "deactivated", str7.toLowerCase(), "default", (String) null, (String) null, (String) null, HODRegistrationActivity.this.subjects);
                                            child.setValue(hod).addOnSuccessListener(new OnSuccessListener<Void>() {
                                                public void onSuccess(Void voidR) {
                                                    FirebaseAuth.getInstance().signOut();
                                                    AlertDialog.Builder builder = new AlertDialog.Builder(HODRegistrationActivity.this);
                                                    builder.setTitle((CharSequence) "Registration Successful");
                                                    builder.setMessage((CharSequence) "Login ID = " + generateLoginID + "\nTemporary Password = " + valueOf);
                                                    builder.setPositiveButton((CharSequence) "Ok", (DialogInterface.OnClickListener) new DialogInterface.OnClickListener() {
                                                        public void onClick(DialogInterface dialogInterface, int i) {
                                                            HODRegistrationActivity.this.hod_name.setText("");
                                                            HODRegistrationActivity.this.hod_phone.setText("");
                                                            HODRegistrationActivity.this.hod_department.setSelection(0);
                                                            HODRegistrationActivity.this.hod_email.setText("");
                                                        }
                                                    });
                                                    builder.create();
                                                    if (HODRegistrationActivity.this.getLifecycle().getCurrentState().isAtLeast(Lifecycle.State.CREATED)) {
                                                        builder.show();
                                                    }
                                                    Toast.makeText(HODRegistrationActivity.this, "H.O.D Registered Successfully", Toast.LENGTH_SHORT).show();
                                                }
                                            }).addOnFailureListener(new OnFailureListener() {
                                                public void onFailure(@NonNull Exception exc) {
                                                    FirebaseAuth.getInstance().signOut();
                                                }
                                            });
                                        }
                                        HODRegistrationActivity.this.progressDialog.dismiss();
                                    }
                                });
                                return;
                            }
                            HODRegistrationActivity.this.hodExists(str7, str5, generateLoginID, str8, str6);
                        }
                    });
                    return;
                }
                HODRegistrationActivity.this.progressDialog.dismiss();
                AlertDialog.Builder builder = new AlertDialog.Builder(HODRegistrationActivity.this);
                builder.setTitle((CharSequence) "Alert");
                builder.setMessage((CharSequence) "H.O.D is already Registered. If any problem contact Admin");
                builder.setPositiveButton((CharSequence) "Ok", (DialogInterface.OnClickListener) new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialogInterface, int i) {
                        HODRegistrationActivity.this.onBackPressed();
                    }
                });
                builder.create();
                if (HODRegistrationActivity.this.getLifecycle().getCurrentState().isAtLeast(Lifecycle.State.CREATED)) {
                    builder.show();
                }
            }
        });
    }

    /* access modifiers changed from: private */
    public void hodExists(String str, String str2, String str3, String str4, String str5) {
        final String str6 = str2;
        final String str7 = str5;
        final String str8 = str;
        final String str9 = str3;
        final String str10 = str4;
        FirebaseDatabase.getInstance().getReference("HODs").addListenerForSingleValueEvent(new ValueEventListener() {
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot children : dataSnapshot.getChildren()) {
                    for (DataSnapshot value : children.getChildren()) {
                        Hod hod = (Hod) value.getValue(Hod.class);
                        if (hod.getEmail().equals(str6)) {
                            HODRegistrationActivity.this.isNotDone = false;
                            FirebaseDatabase instance = FirebaseDatabase.getInstance();
                            DatabaseReference child = instance.getReference("HODs/" + str7).child(hod.getId());
                            Hod hod3 = new Hod(str8, str9.toLowerCase().trim(), hod.getPassword(), str7, str6, str10, hod.getId(), "offline", "deactivated", str8.toLowerCase(), "default", (String) null, (String) null, (String) null, HODRegistrationActivity.this.subjects);
                            child.setValue(hod3);
                            AlertDialog.Builder builder = new AlertDialog.Builder(HODRegistrationActivity.this);
                            builder.setTitle((CharSequence) "Registration Successful");
                            builder.setMessage((CharSequence) "Login ID = " + str9 + "\nTemporary Password = " + hod.getPassword());
                            builder.setPositiveButton((CharSequence) "Ok", (DialogInterface.OnClickListener) new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialogInterface, int i) {
                                    HODRegistrationActivity.this.hod_name.setText("");
                                    HODRegistrationActivity.this.hod_phone.setText("");
                                    HODRegistrationActivity.this.hod_department.setSelection(0);
                                    HODRegistrationActivity.this.hod_email.setText("");
                                }
                            });
                            builder.create();
                            if (HODRegistrationActivity.this.getLifecycle().getCurrentState().isAtLeast(Lifecycle.State.CREATED)) {
                                builder.show();
                            }
                            Toast.makeText(HODRegistrationActivity.this, "H.O.D Registered Successfully", Toast.LENGTH_SHORT).show();
                            HODRegistrationActivity.this.progressDialog.dismiss();
                        }
                    }
                }
                if (HODRegistrationActivity.this.isNotDone.booleanValue()) {
                    HODRegistrationActivity.this.progressDialog.dismiss();
                    AlertDialog.Builder builder2 = new AlertDialog.Builder(HODRegistrationActivity.this);
                    builder2.setTitle((CharSequence) "Registration Error");
                    builder2.setMessage((CharSequence) "Staff Cannot be Registered as H.O.D");
                    builder2.setPositiveButton((CharSequence) "Ok", (DialogInterface.OnClickListener) new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialogInterface, int i) {
                            HODRegistrationActivity.this.onBackPressed();
                        }
                    });
                    builder2.create();
                    if (HODRegistrationActivity.this.getLifecycle().getCurrentState().isAtLeast(Lifecycle.State.CREATED)) {
                        builder2.show();
                    }
                }
            }

            public void onCancelled(@NonNull DatabaseError databaseError) {
                HODRegistrationActivity.this.progressDialog.dismiss();
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
