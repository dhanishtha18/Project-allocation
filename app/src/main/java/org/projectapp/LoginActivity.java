package org.projectapp;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Build.VERSION;
import android.os.Bundle;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog.Builder;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;
import androidx.appcompat.widget.AppCompatCheckBox;
import androidx.appcompat.widget.AppCompatEditText;
import androidx.appcompat.widget.AppCompatTextView;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.lifecycle.Lifecycle.State;
import com.google.android.gms.common.Scopes;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import org.projectapp.Model.Hod;
import org.projectapp.Model.Principal;
import org.projectapp.Model.Staff;
import org.projectapp.Model.Student;

public class LoginActivity extends AppCompatActivity {
    int data = 0;
    AppCompatTextView forget_password_btn, registerhod;
    int invalid = 0;
    AppCompatButton login_btn;
    String loginas = null;
    ProgressDialog progressDialog;
    AppCompatCheckBox show_password;
    AppCompatEditText user_id;
    AppCompatEditText user_password;

    /* access modifiers changed from: protected */
    public void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        setContentView((int) R.layout.activity_login);
        this.loginas = getIntent().getStringExtra("loginas");
        if (VERSION.SDK_INT >= 23) {
            String[] strArr = {"android.permission.READ_EXTERNAL_STORAGE", "android.permission.WRITE_EXTERNAL_STORAGE"};
            if (!(ContextCompat.checkSelfPermission(this, strArr[0]) == 0 && ContextCompat.checkSelfPermission(this, strArr[1]) == 0)) {
                ActivityCompat.requestPermissions(this, strArr, 122);
            }
        }
        this.user_id = (AppCompatEditText) findViewById(R.id.user_id);
        this.registerhod = (AppCompatTextView) findViewById(R.id.registerhod);
        this.user_password = (AppCompatEditText) findViewById(R.id.user_password);
        this.show_password = (AppCompatCheckBox) findViewById(R.id.show_password);
        this.login_btn = (AppCompatButton) findViewById(R.id.login_btn);
        this.forget_password_btn = (AppCompatTextView) findViewById(R.id.forget_password);
        if(LoginActivity.this.loginas.equals("hod")){
            registerhod.setVisibility(View.VISIBLE);
        }
        registerhod.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(LoginActivity.this, HODRegistrationActivity.class));
            }
        });
        this.show_password.setOnClickListener(new OnClickListener() {
            public void onClick(View view) {
                AppCompatCheckBox appCompatCheckBox = (AppCompatCheckBox) view;
                if (appCompatCheckBox.isChecked()) {
                    LoginActivity.this.user_password.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
                }
                if (!appCompatCheckBox.isChecked()) {
                    LoginActivity.this.user_password.setTransformationMethod(PasswordTransformationMethod.getInstance());
                }
            }
        });
        this.forget_password_btn.setOnClickListener(new OnClickListener() {
            public void onClick(View view) {
                LoginActivity loginActivity = LoginActivity.this;
                loginActivity.startActivity(new Intent(loginActivity, ResetPasswordActivity.class));
                LoginActivity.this.finish();
            }
        });
        this.login_btn.setOnClickListener(new OnClickListener() {
            public void onClick(View view) {
                String str = "Ok";
                String str2 = "Alert";
                if (!LoginActivity.this.isNetworkAvailable()) {
                    Builder builder = new Builder(LoginActivity.this);
                    builder.setTitle((CharSequence) str2);
                    builder.setMessage((CharSequence) "No Internet Connection...");
                    builder.setPositiveButton((CharSequence) str, (DialogInterface.OnClickListener) new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialogInterface, int i) {
                        }
                    });
                    builder.create();
                    if (LoginActivity.this.getLifecycle().getCurrentState().isAtLeast(State.CREATED)) {
                        builder.show();
                    }
                } else if (LoginActivity.this.user_id.getText().toString().isEmpty()) {
                    Builder builder2 = new Builder(LoginActivity.this);
                    builder2.setTitle((CharSequence) str2);
                    builder2.setMessage((CharSequence) "Enter Login ID");
                    builder2.setPositiveButton((CharSequence) str, (DialogInterface.OnClickListener) new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialogInterface, int i) {
                        }
                    });
                    builder2.create();
                    if (LoginActivity.this.getLifecycle().getCurrentState().isAtLeast(State.CREATED)) {
                        builder2.show();
                    }
                } else if (LoginActivity.this.user_password.getText().toString().isEmpty()) {
                    Builder builder3 = new Builder(LoginActivity.this);
                    builder3.setTitle((CharSequence) str2);
                    builder3.setMessage((CharSequence) "Enter Login Password");
                    builder3.setPositiveButton((CharSequence) str, (DialogInterface.OnClickListener) new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialogInterface, int i) {
                        }
                    });
                    builder3.create();
                    if (LoginActivity.this.getLifecycle().getCurrentState().isAtLeast(State.CREATED)) {
                        builder3.show();
                    }
                } else if (LoginActivity.this.loginas.equals("lecturer")) {
                    LoginActivity loginActivity2 = LoginActivity.this;
                    loginActivity2.onStaffLogin(loginActivity2.user_id.getText().toString().toLowerCase(), LoginActivity.this.user_password.getText().toString());
                } else if (LoginActivity.this.loginas.equals("hod")) {
                    LoginActivity loginActivity3 = LoginActivity.this;
                    loginActivity3.onHodLogin(loginActivity3.user_id.getText().toString().toLowerCase(), LoginActivity.this.user_password.getText().toString());
                } else if (LoginActivity.this.loginas.equals("student")) {
                    LoginActivity loginActivity4 = LoginActivity.this;
                    loginActivity4.onStudentLogin(loginActivity4.user_id.getText().toString(), LoginActivity.this.user_password.getText().toString());
                }
            }
        });
    }

    /* access modifiers changed from: private */
    public void onStudentLogin(final String str, final String str2) {
        this.progressDialog = new ProgressDialog(this);
        this.progressDialog.setCancelable(false);
        this.progressDialog.setMessage("Authenticating....");
        this.progressDialog.show();
        FirebaseDatabase.getInstance().getReference("Students").addListenerForSingleValueEvent(new ValueEventListener() {
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot children : dataSnapshot.getChildren()) {
                    for (DataSnapshot children2 : children.getChildren()) {
                        for (DataSnapshot dataSnapshot2 : children2.getChildren()) {
                            LoginActivity.this.data++;
                            final Student student = (Student) dataSnapshot2.getValue(Student.class);
                            if (student.getEnrollment().equals(str)) {
                                LoginActivity.this.invalid--;
                                FirebaseAuth.getInstance().signInWithEmailAndPassword(student.getEmail(), str2).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                                    public void onComplete(@NonNull Task<AuthResult> task) {
                                        if (task.isSuccessful()) {
                                            String str = "id";
                                            String str2 = "enrollment";
                                            String str3 = "phone";
                                            String str4 = "branch";
                                            String str5 = "semester";
                                            String str6 = "rollno";
                                            String str7 = "email";
                                            String str8 = "password";
                                            LoginActivity.this.getSharedPreferences("rait", 0).edit().clear().putString("name", student.getName()).putString(str, student.getId()).putString(str2, student.getEnrollment()).putString(str3, student.getPhone()).putString(str4, student.getDepartment()).putString(str5, student.getSemester()).putString(str6, student.getRoll_no()).putString(str7, student.getEmail()).putString(str8, student.getPassword()).putString(Scopes.PROFILE, student.getImage_url()).putString("loginas", "student").commit();
                                            Intent intent = new Intent(LoginActivity.this, HomeActivity.class);
                                            LoginActivity.this.finishAffinity();
                                            LoginActivity.this.progressDialog.dismiss();
                                            LoginActivity.this.startActivity(intent);
                                            Toast.makeText(LoginActivity.this, "Login Successful", Toast.LENGTH_SHORT).show();
                                        } else if (task.getException().getMessage().equals("The password is invalid or the user does not have a password.")) {
                                            Builder builder = new Builder(LoginActivity.this);
                                            builder.setTitle((CharSequence) "Alert");
                                            builder.setMessage((CharSequence) "Invalid Password");
                                            builder.setPositiveButton((CharSequence) "Ok", (DialogInterface.OnClickListener) new DialogInterface.OnClickListener() {
                                                public void onClick(DialogInterface dialogInterface, int i) {
                                                }
                                            });
                                            builder.create();
                                            if (LoginActivity.this.getLifecycle().getCurrentState().isAtLeast(State.CREATED)) {
                                                builder.show();
                                            }
                                            LoginActivity.this.progressDialog.dismiss();
                                        }
                                    }
                                });
                            } else {
                                LoginActivity.this.invalid++;
                            }
                        }
                    }
                }
                if (LoginActivity.this.invalid == LoginActivity.this.data) {
                    LoginActivity loginActivity = LoginActivity.this;
                    loginActivity.invalid = 0;
                    loginActivity.data = 0;
                    Builder builder = new Builder(loginActivity);
                    builder.setTitle((CharSequence) "Alert");
                    builder.setMessage((CharSequence) "Please enter valid enrollment no.");
                    builder.setPositiveButton((CharSequence) "Ok", (DialogInterface.OnClickListener) new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialogInterface, int i) {
                        }
                    });
                    builder.create();
                    if (LoginActivity.this.getLifecycle().getCurrentState().isAtLeast(State.CREATED)) {
                        builder.show();
                    }
                    LoginActivity.this.progressDialog.dismiss();
                }
            }

            public void onCancelled(@NonNull DatabaseError databaseError) {
                LoginActivity.this.progressDialog.dismiss();
            }
        });
    }

    /* access modifiers changed from: private */
    public void onHodLogin(final String str, final String str2) {
        this.progressDialog = new ProgressDialog(this);
        this.progressDialog.setCancelable(false);
        this.progressDialog.setMessage("Authenticating....");
        this.progressDialog.show();
        FirebaseDatabase.getInstance().getReference("HODs").addListenerForSingleValueEvent(new ValueEventListener() {
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot children : dataSnapshot.getChildren()) {
                    for (DataSnapshot dataSnapshot2 : children.getChildren()) {
                        LoginActivity.this.data++;
                        final Hod hod = (Hod) dataSnapshot2.getValue(Hod.class);
                        if (hod.getLogin_id().equals(str)) {
                            LoginActivity.this.invalid--;
                            FirebaseAuth.getInstance().signInWithEmailAndPassword(hod.getEmail(), str2).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                                public void onComplete(@NonNull Task<AuthResult> task) {
                                    if (task.isSuccessful()) {
                                        String str = "id";
                                        String str2 = "login_id";
                                        String str3 = "phone";
                                        String str4 = "email";
                                        String str5 = "password";
                                        String str6 = "department";
                                        LoginActivity.this.getSharedPreferences("rait", 0).edit().clear().putString("name", hod.getName()).putString(str, hod.getId()).putString(str2, hod.getLogin_id()).putString(str3, hod.getPhone()).putString(str4, hod.getEmail()).putString(str5, hod.getPassword()).putString(str6, hod.getDepartment()).putString(Scopes.PROFILE, hod.getProfile_url()).putString("loginas", "hod").commit();
                                        Intent intent = new Intent(LoginActivity.this, HomeActivity.class);
                                        LoginActivity.this.finishAffinity();
                                        LoginActivity.this.progressDialog.dismiss();
                                        LoginActivity.this.startActivity(intent);
                                        Toast.makeText(LoginActivity.this, "Login Successful", Toast.LENGTH_SHORT).show();
                                    } else if (task.getException().getMessage().equals("The password is invalid or the user does not have a password.")) {
                                        Builder builder = new Builder(LoginActivity.this);
                                        builder.setTitle((CharSequence) "Alert");
                                        builder.setMessage((CharSequence) "Invalid Password");
                                        builder.setPositiveButton((CharSequence) "Ok", (DialogInterface.OnClickListener) new DialogInterface.OnClickListener() {
                                            public void onClick(DialogInterface dialogInterface, int i) {
                                            }
                                        });
                                        builder.create();
                                        if (LoginActivity.this.getLifecycle().getCurrentState().isAtLeast(State.CREATED)) {
                                            builder.show();
                                        }
                                        LoginActivity.this.progressDialog.dismiss();
                                    }
                                }
                            });
                        } else {
                            LoginActivity.this.invalid++;
                        }
                    }
                }
                if (LoginActivity.this.invalid == LoginActivity.this.data) {
                    LoginActivity loginActivity = LoginActivity.this;
                    loginActivity.invalid = 0;
                    loginActivity.data = 0;
                    Builder builder = new Builder(loginActivity);
                    builder.setTitle((CharSequence) "Alert");
                    builder.setMessage((CharSequence) "Please enter valid Login ID");
                    builder.setPositiveButton((CharSequence) "Ok", (DialogInterface.OnClickListener) new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialogInterface, int i) {
                        }
                    });
                    builder.create();
                    if (LoginActivity.this.getLifecycle().getCurrentState().isAtLeast(State.CREATED)) {
                        builder.show();
                    }
                    LoginActivity.this.progressDialog.dismiss();
                }
            }

            public void onCancelled(@NonNull DatabaseError databaseError) {
                LoginActivity.this.progressDialog.dismiss();
            }
        });
    }

    /* access modifiers changed from: private */
    public void onStaffLogin(final String str, final String str2) {
        this.progressDialog = new ProgressDialog(this);
        this.progressDialog.setCancelable(false);
        this.progressDialog.setMessage("Authenticating....");
        this.progressDialog.show();
        FirebaseDatabase.getInstance().getReference("Staff").addListenerForSingleValueEvent(new ValueEventListener() {
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot children : dataSnapshot.getChildren()) {
                    for (DataSnapshot dataSnapshot2 : children.getChildren()) {
                        LoginActivity.this.data++;
                        final Staff staff = (Staff) dataSnapshot2.getValue(Staff.class);
                        if (staff.getLogin_id().equals(str)) {
                            LoginActivity.this.invalid--;
                            FirebaseAuth.getInstance().signInWithEmailAndPassword(staff.getEmail(), str2).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                                public void onComplete(@NonNull Task<AuthResult> task) {
                                    if (task.isSuccessful()) {
                                        String str = "id";
                                        String str2 = "login_id";
                                        String str3 = "phone";
                                        String str4 = "email";
                                        String str5 = "password";
                                        String str6 = "department";
                                        String str7 = "mode";
                                        LoginActivity.this.getSharedPreferences("rait", 0).edit().clear().putString("name", staff.getName()).putString(str, staff.getId()).putString(str2, staff.getLogin_id()).putString(str3, staff.getPhone()).putString(str4, staff.getEmail()).putString(str5, staff.getPassword()).putString(str6, staff.getDepartment()).putString(str7, staff.getMode()).putString(Scopes.PROFILE, staff.getProfile_url()).putString("loginas", "lecturer").commit();
                                        Intent intent = new Intent(LoginActivity.this, HomeActivity.class);
                                        LoginActivity.this.finishAffinity();
                                        LoginActivity.this.progressDialog.dismiss();
                                        LoginActivity.this.startActivity(intent);
                                        Toast.makeText(LoginActivity.this, "Login Successful", Toast.LENGTH_SHORT).show();
                                    } else if (task.getException().getMessage().equals("The password is invalid or the user does not have a password.")) {
                                        Builder builder = new Builder(LoginActivity.this);
                                        builder.setTitle((CharSequence) "Alert");
                                        builder.setMessage((CharSequence) "Invalid Password");
                                        builder.setPositiveButton((CharSequence) "Ok", (DialogInterface.OnClickListener) new DialogInterface.OnClickListener() {
                                            public void onClick(DialogInterface dialogInterface, int i) {
                                            }
                                        });
                                        builder.create();
                                        if (LoginActivity.this.getLifecycle().getCurrentState().isAtLeast(State.CREATED)) {
                                            builder.show();
                                        }
                                        LoginActivity.this.progressDialog.dismiss();
                                    }
                                }
                            });
                        } else {
                            LoginActivity.this.invalid++;
                        }
                    }
                }
                if (LoginActivity.this.invalid == LoginActivity.this.data) {
                    LoginActivity loginActivity = LoginActivity.this;
                    loginActivity.invalid = 0;
                    loginActivity.data = 0;
                    Builder builder = new Builder(loginActivity);
                    builder.setTitle((CharSequence) "Alert");
                    builder.setMessage((CharSequence) "Please enter valid Login ID");
                    builder.setPositiveButton((CharSequence) "Ok", (DialogInterface.OnClickListener) new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialogInterface, int i) {
                        }
                    });
                    builder.create();
                    if (LoginActivity.this.getLifecycle().getCurrentState().isAtLeast(State.CREATED)) {
                        builder.show();
                    }
                    LoginActivity.this.progressDialog.dismiss();
                }
            }

            public void onCancelled(@NonNull DatabaseError databaseError) {
                LoginActivity.this.progressDialog.dismiss();
            }
        });
    }

    /* access modifiers changed from: private */

    /* access modifiers changed from: private */
    public boolean isNetworkAvailable() {
        NetworkInfo activeNetworkInfo = ((ConnectivityManager) getSystemService("connectivity")).getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }

    public void onBackPressed() {
        super.onBackPressed();
        finishAffinity();
    }
}
