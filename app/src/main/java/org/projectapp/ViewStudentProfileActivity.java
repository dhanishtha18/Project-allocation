package org.projectapp;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.ImageView;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;
import androidx.appcompat.widget.AppCompatEditText;
import androidx.appcompat.widget.AppCompatTextView;
import androidx.fragment.app.FragmentActivity;
import androidx.lifecycle.Lifecycle;
import com.bumptech.glide.Glide;
import com.google.android.gms.common.Scopes;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.theartofdev.edmodo.cropper.CropImage;
import com.theartofdev.edmodo.cropper.CropImageView;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import org.projectapp.Model.Student;

import de.hdodenhof.circleimageview.CircleImageView;

public class ViewStudentProfileActivity extends AppCompatActivity {
    Bitmap bitmap = null;
    String department;
    ProgressDialog dialog;

    /* renamed from: id */
    String id;
    Uri imagepath;
    Uri img;
    String loginas;
    AppCompatEditText parent_phoneno;
    AppCompatEditText sem1;
    AppCompatEditText sem2;
    AppCompatEditText sem3;
    AppCompatEditText sem4;
    AppCompatEditText sem5;
    AppCompatEditText sem6;
    String semester;
    String strsem1 = null;
    String strsem2 = null;
    String strsem3 = null;
    String strsem4 = null;
    String strsem5 = null;
    String strsem6 = null;
    Student student = null;
    AppCompatTextView title;
    AppCompatEditText user_department;
    AppCompatButton user_edit;
    AppCompatEditText user_email;
    AppCompatEditText user_enrollment;
    AppCompatEditText user_name;
    AppCompatEditText user_phoneno;
    CircleImageView user_profile;
    AppCompatEditText user_roll_no;
    AppCompatEditText user_semester;
    AppCompatEditText user_skills;

    /* access modifiers changed from: protected */
    public void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        setContentView((int) R.layout.activity_view_student_profile);
        this.user_department = (AppCompatEditText) findViewById(R.id.user_department);
        this.user_semester = (AppCompatEditText) findViewById(R.id.user_semester);
        this.user_name = (AppCompatEditText) findViewById(R.id.user_name);
        this.user_enrollment = (AppCompatEditText) findViewById(R.id.user_enrollment);
        this.user_phoneno = (AppCompatEditText) findViewById(R.id.user_phoneno);
        this.user_email = (AppCompatEditText) findViewById(R.id.user_email);
        this.user_roll_no = (AppCompatEditText) findViewById(R.id.user_roll_no);
        this.user_edit = (AppCompatButton) findViewById(R.id.user_edit);
        this.user_profile = (CircleImageView) findViewById(R.id.user_profile);
        this.user_skills = (AppCompatEditText) findViewById(R.id.user_skills);
        this.parent_phoneno = (AppCompatEditText) findViewById(R.id.parent_phoneno);
        this.title = (AppCompatTextView) findViewById(R.id.title);
        this.sem1 = (AppCompatEditText) findViewById(R.id.sem1);
        this.sem2 = (AppCompatEditText) findViewById(R.id.sem2);
        this.sem3 = (AppCompatEditText) findViewById(R.id.sem3);
        this.sem4 = (AppCompatEditText) findViewById(R.id.sem4);
        this.sem5 = (AppCompatEditText) findViewById(R.id.sem5);
        this.sem6 = (AppCompatEditText) findViewById(R.id.sem6);
        this.user_profile.setEnabled(false);
        this.id = getIntent().getStringExtra("id");
        this.semester = getIntent().getStringExtra("semester");
        this.department = getIntent().getStringExtra("department");
        this.loginas = getIntent().getStringExtra("loginas");
        if (this.loginas.equals(Scopes.PROFILE)) {
            this.user_edit.setVisibility(View.VISIBLE);
        } else {
            this.user_edit.setVisibility(View.GONE);
        }
        this.title.setText("Profile");
        FirebaseDatabase instance = FirebaseDatabase.getInstance();
        instance.getReference("Students/" + this.department + "/" + this.semester + "/" + this.id).addValueEventListener(new ValueEventListener() {
            public void onCancelled(@NonNull DatabaseError databaseError) {
            }

            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                ViewStudentProfileActivity.this.student = (Student) dataSnapshot.getValue(Student.class);
                if (ViewStudentProfileActivity.this.student.getImage_url().equals("default")) {
                    ViewStudentProfileActivity.this.user_profile.setImageResource(R.drawable.profile);
                } else if (ViewStudentProfileActivity.this.getLifecycle().getCurrentState().isAtLeast(Lifecycle.State.INITIALIZED)) {
                    Glide.with((FragmentActivity) ViewStudentProfileActivity.this).load(ViewStudentProfileActivity.this.student.getImage_url()).into((ImageView) ViewStudentProfileActivity.this.user_profile);
                }
                if (!ViewStudentProfileActivity.this.loginas.equals(Scopes.PROFILE)) {
                    ViewStudentProfileActivity.this.title.setText(ViewStudentProfileActivity.this.student.getName());
                    if (ViewStudentProfileActivity.this.student.getDepartment().isEmpty()) {
                        ViewStudentProfileActivity.this.user_department.setVisibility(View.GONE);
                    }
                    if (ViewStudentProfileActivity.this.student.getName().isEmpty()) {
                        ViewStudentProfileActivity.this.user_name.setVisibility(View.GONE);
                    }
                    if (ViewStudentProfileActivity.this.student.getPhone().isEmpty()) {
                        ViewStudentProfileActivity.this.user_phoneno.setVisibility(View.GONE);
                    }
                    if (ViewStudentProfileActivity.this.student.getEmail().isEmpty()) {
                        ViewStudentProfileActivity.this.user_email.setVisibility(View.GONE);
                    }
                    if (ViewStudentProfileActivity.this.student.getParent_phone().isEmpty()) {
                        ViewStudentProfileActivity.this.parent_phoneno.setVisibility(View.GONE);
                    }
                    if (ViewStudentProfileActivity.this.student.getSkills() == null) {
                        ViewStudentProfileActivity.this.user_skills.setVisibility(View.GONE);
                    }
                    if (ViewStudentProfileActivity.this.student.getEnrollment().isEmpty()) {
                        ViewStudentProfileActivity.this.user_enrollment.setVisibility(View.GONE);
                    }
                    if (ViewStudentProfileActivity.this.student.getSemester().isEmpty()) {
                        ViewStudentProfileActivity.this.user_semester.setVisibility(View.GONE);
                    }
                    if (ViewStudentProfileActivity.this.student.getRoll_no().isEmpty()) {
                        ViewStudentProfileActivity.this.user_roll_no.setVisibility(View.GONE);
                    }
                    if (ViewStudentProfileActivity.this.student.getSem1() == null || ViewStudentProfileActivity.this.student.getSem1().isEmpty()) {
                        ViewStudentProfileActivity.this.sem1.setVisibility(View.GONE);
                    }
                    if (ViewStudentProfileActivity.this.student.getSem2() == null || ViewStudentProfileActivity.this.student.getSem2().isEmpty()) {
                        ViewStudentProfileActivity.this.sem2.setVisibility(View.GONE);
                    }
                    if (ViewStudentProfileActivity.this.student.getSem3() == null || ViewStudentProfileActivity.this.student.getSem3().isEmpty()) {
                        ViewStudentProfileActivity.this.sem3.setVisibility(View.GONE);
                    }
                    if (ViewStudentProfileActivity.this.student.getSem4() == null || ViewStudentProfileActivity.this.student.getSem4().isEmpty()) {
                        ViewStudentProfileActivity.this.sem4.setVisibility(View.GONE);
                    }
                    if (ViewStudentProfileActivity.this.student.getSem5() == null || ViewStudentProfileActivity.this.student.getSem5().isEmpty()) {
                        ViewStudentProfileActivity.this.sem5.setVisibility(View.GONE);
                    }
                    if (ViewStudentProfileActivity.this.student.getSem6() == null || ViewStudentProfileActivity.this.student.getSem6().isEmpty()) {
                        ViewStudentProfileActivity.this.sem6.setVisibility(View.GONE);
                    }
                }
                ViewStudentProfileActivity.this.user_department.setText(ViewStudentProfileActivity.this.student.getDepartment());
                ViewStudentProfileActivity.this.user_semester.setText(ViewStudentProfileActivity.this.student.getSemester());
                ViewStudentProfileActivity.this.user_name.setText(ViewStudentProfileActivity.this.student.getName());
                ViewStudentProfileActivity.this.user_enrollment.setText(ViewStudentProfileActivity.this.student.getEnrollment());
                ViewStudentProfileActivity.this.user_phoneno.setText(ViewStudentProfileActivity.this.student.getPhone());
                ViewStudentProfileActivity.this.user_email.setText(ViewStudentProfileActivity.this.student.getEmail());
                ViewStudentProfileActivity.this.user_roll_no.setText(ViewStudentProfileActivity.this.student.getRoll_no());
                ViewStudentProfileActivity.this.user_skills.setText(ViewStudentProfileActivity.this.student.getSkills());
                ViewStudentProfileActivity.this.parent_phoneno.setText(ViewStudentProfileActivity.this.student.getParent_phone());
                ViewStudentProfileActivity.this.sem1.setText(ViewStudentProfileActivity.this.student.getSem1());
                ViewStudentProfileActivity.this.sem2.setText(ViewStudentProfileActivity.this.student.getSem2());
                ViewStudentProfileActivity.this.sem3.setText(ViewStudentProfileActivity.this.student.getSem3());
                ViewStudentProfileActivity.this.sem4.setText(ViewStudentProfileActivity.this.student.getSem4());
                ViewStudentProfileActivity.this.sem5.setText(ViewStudentProfileActivity.this.student.getSem5());
                ViewStudentProfileActivity.this.sem6.setText(ViewStudentProfileActivity.this.student.getSem6());
            }
        });
        this.user_edit.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                if (ViewStudentProfileActivity.this.user_name.isEnabled()) {
                    ViewStudentProfileActivity viewStudentProfileActivity = ViewStudentProfileActivity.this;
                    viewStudentProfileActivity.dialog = new ProgressDialog(viewStudentProfileActivity);
                    ViewStudentProfileActivity.this.dialog.setMessage("Updating...");
                    ViewStudentProfileActivity.this.dialog.setCancelable(false);
                    ViewStudentProfileActivity.this.dialog.show();
                    ViewStudentProfileActivity.this.user_edit.setText("Edit");
                    ViewStudentProfileActivity.this.user_name.setEnabled(false);
                    ViewStudentProfileActivity.this.user_phoneno.setEnabled(false);
                    ViewStudentProfileActivity.this.user_roll_no.setEnabled(false);
                    ViewStudentProfileActivity.this.user_profile.setEnabled(false);
                    ViewStudentProfileActivity.this.user_skills.setEnabled(false);
                    ViewStudentProfileActivity.this.parent_phoneno.setEnabled(false);
                    ViewStudentProfileActivity.this.sem1.setEnabled(false);
                    ViewStudentProfileActivity.this.sem2.setEnabled(false);
                    ViewStudentProfileActivity.this.sem3.setEnabled(false);
                    ViewStudentProfileActivity.this.sem4.setEnabled(false);
                    ViewStudentProfileActivity.this.sem5.setEnabled(false);
                    ViewStudentProfileActivity.this.sem6.setEnabled(false);
                    if (ViewStudentProfileActivity.this.user_name.getText().toString().equals("")) {
                        AlertDialog.Builder builder = new AlertDialog.Builder(ViewStudentProfileActivity.this);
                        builder.setMessage("Enter Your Name.");
                        builder.setTitle("Alert");
                        builder.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialogInterface, int i) {
                            }
                        });
                        builder.show();
                        ViewStudentProfileActivity.this.dialog.dismiss();
                    } else if (ViewStudentProfileActivity.this.user_enrollment.getText().toString().equals("")) {
                        AlertDialog.Builder builder2 = new AlertDialog.Builder(ViewStudentProfileActivity.this);
                        builder2.setMessage("Enter Your Enrollment No.");
                        builder2.setTitle("Alert");
                        builder2.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialogInterface, int i) {
                            }
                        });
                        builder2.show();
                        ViewStudentProfileActivity.this.dialog.dismiss();
                    } else if (ViewStudentProfileActivity.this.user_roll_no.getText().toString().equals("")) {
                        AlertDialog.Builder builder3 = new AlertDialog.Builder(ViewStudentProfileActivity.this);
                        builder3.setMessage("Enter Your Roll No.");
                        builder3.setTitle("Alert");
                        builder3.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialogInterface, int i) {
                            }
                        });
                        builder3.show();
                        ViewStudentProfileActivity.this.dialog.dismiss();
                    } else if (ViewStudentProfileActivity.this.parent_phoneno.getText().toString().equals("")) {
                        AlertDialog.Builder builder4 = new AlertDialog.Builder(ViewStudentProfileActivity.this);
                        builder4.setMessage("Enter Parent Phone No.");
                        builder4.setTitle("Alert");
                        builder4.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialogInterface, int i) {
                            }
                        });
                        builder4.show();
                        ViewStudentProfileActivity.this.dialog.dismiss();
                    } else if (ViewStudentProfileActivity.this.user_phoneno.getText().toString().equals("")) {
                        AlertDialog.Builder builder5 = new AlertDialog.Builder(ViewStudentProfileActivity.this);
                        builder5.setMessage("Enter Your Phone No.");
                        builder5.setTitle("Alert");
                        builder5.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialogInterface, int i) {
                            }
                        });
                        builder5.show();
                        ViewStudentProfileActivity.this.dialog.dismiss();
                    } else if (ViewStudentProfileActivity.this.user_phoneno.getText().toString().trim().length() != 10) {
                        AlertDialog.Builder builder6 = new AlertDialog.Builder(ViewStudentProfileActivity.this);
                        builder6.setMessage("Enter Your Phone No.");
                        builder6.setTitle("Alert");
                        builder6.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialogInterface, int i) {
                            }
                        });
                        builder6.show();
                        ViewStudentProfileActivity.this.dialog.dismiss();
                    } else if (ViewStudentProfileActivity.this.parent_phoneno.getText().toString().trim().length() != 10) {
                        AlertDialog.Builder builder7 = new AlertDialog.Builder(ViewStudentProfileActivity.this);
                        builder7.setMessage("Enter Parent Phone No.");
                        builder7.setTitle("Alert");
                        builder7.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialogInterface, int i) {
                            }
                        });
                        builder7.show();
                        ViewStudentProfileActivity.this.dialog.dismiss();
                    } else if (ViewStudentProfileActivity.this.user_skills.getText().toString().equals("")) {
                        AlertDialog.Builder builder8 = new AlertDialog.Builder(ViewStudentProfileActivity.this);
                        builder8.setMessage("Enter Your Skills.");
                        builder8.setTitle("Alert");
                        builder8.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialogInterface, int i) {
                            }
                        });
                        builder8.show();
                        ViewStudentProfileActivity.this.dialog.dismiss();
                    } else {
                        if (!ViewStudentProfileActivity.this.sem1.getText().toString().trim().isEmpty()) {
                            ViewStudentProfileActivity viewStudentProfileActivity2 = ViewStudentProfileActivity.this;
                            viewStudentProfileActivity2.strsem1 = viewStudentProfileActivity2.sem1.getText().toString().trim();
                        }
                        if (!ViewStudentProfileActivity.this.sem2.getText().toString().trim().isEmpty()) {
                            ViewStudentProfileActivity viewStudentProfileActivity3 = ViewStudentProfileActivity.this;
                            viewStudentProfileActivity3.strsem2 = viewStudentProfileActivity3.sem2.getText().toString().trim();
                        }
                        if (!ViewStudentProfileActivity.this.sem3.getText().toString().trim().isEmpty()) {
                            ViewStudentProfileActivity viewStudentProfileActivity4 = ViewStudentProfileActivity.this;
                            viewStudentProfileActivity4.strsem3 = viewStudentProfileActivity4.sem3.getText().toString().trim();
                        }
                        if (!ViewStudentProfileActivity.this.sem4.getText().toString().trim().isEmpty()) {
                            ViewStudentProfileActivity viewStudentProfileActivity5 = ViewStudentProfileActivity.this;
                            viewStudentProfileActivity5.strsem4 = viewStudentProfileActivity5.sem4.getText().toString().trim();
                        }
                        if (!ViewStudentProfileActivity.this.sem5.getText().toString().trim().isEmpty()) {
                            ViewStudentProfileActivity viewStudentProfileActivity6 = ViewStudentProfileActivity.this;
                            viewStudentProfileActivity6.strsem5 = viewStudentProfileActivity6.sem5.getText().toString().trim();
                        }
                        if (!ViewStudentProfileActivity.this.sem6.getText().toString().trim().isEmpty()) {
                            ViewStudentProfileActivity viewStudentProfileActivity7 = ViewStudentProfileActivity.this;
                            viewStudentProfileActivity7.strsem6 = viewStudentProfileActivity7.sem6.getText().toString().trim();
                        }
                        if (ViewStudentProfileActivity.this.bitmap == null) {
                            FirebaseDatabase instance = FirebaseDatabase.getInstance();
                            DatabaseReference reference = instance.getReference("Students/" + ViewStudentProfileActivity.this.department + "/" + ViewStudentProfileActivity.this.semester + "/" + ViewStudentProfileActivity.this.id);
                            Student student = new Student(ViewStudentProfileActivity.this.id, ViewStudentProfileActivity.this.user_name.getText().toString().trim(), ViewStudentProfileActivity.this.department, ViewStudentProfileActivity.this.semester, ViewStudentProfileActivity.this.user_enrollment.getText().toString().trim(), ViewStudentProfileActivity.this.user_phoneno.getText().toString(), ViewStudentProfileActivity.this.user_email.getText().toString().trim(), ViewStudentProfileActivity.this.user_roll_no.getText().toString().trim(), ViewStudentProfileActivity.this.student.getPassword(), ViewStudentProfileActivity.this.student.getImage_url(), ViewStudentProfileActivity.this.student.getAccount(), ViewStudentProfileActivity.this.user_skills.getText().toString(), ViewStudentProfileActivity.this.parent_phoneno.getText().toString().trim(), ViewStudentProfileActivity.this.student.getToken(), ViewStudentProfileActivity.this.strsem1, ViewStudentProfileActivity.this.strsem2, ViewStudentProfileActivity.this.strsem3, ViewStudentProfileActivity.this.strsem4, ViewStudentProfileActivity.this.strsem5, ViewStudentProfileActivity.this.strsem6);
                            reference.setValue(student).addOnSuccessListener(new OnSuccessListener<Void>() {
                                public void onSuccess(Void voidR) {
                                    ViewStudentProfileActivity.this.getSharedPreferences("rait", 0).edit().clear().putString("name", ViewStudentProfileActivity.this.user_name.getText().toString().trim()).putString("id", ViewStudentProfileActivity.this.student.getId()).putString("enrollment", ViewStudentProfileActivity.this.student.getEnrollment()).putString("phone", ViewStudentProfileActivity.this.user_phoneno.getText().toString().trim()).putString("branch", ViewStudentProfileActivity.this.student.getDepartment()).putString("semester", ViewStudentProfileActivity.this.student.getSemester()).putString("rollno", ViewStudentProfileActivity.this.user_roll_no.getText().toString().trim()).putString("email", ViewStudentProfileActivity.this.student.getEmail()).putString("password", ViewStudentProfileActivity.this.student.getPassword()).putString(Scopes.PROFILE, ViewStudentProfileActivity.this.student.getImage_url()).putString("loginas", "student").commit();
                                    ViewStudentProfileActivity.this.dialog.dismiss();
                                }
                            }).addOnFailureListener(new OnFailureListener() {
                                public void onFailure(@NonNull Exception exc) {
                                    ViewStudentProfileActivity.this.dialog.dismiss();
                                }
                            });
                            return;
                        }
                        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
                        ViewStudentProfileActivity.this.bitmap.compress(Bitmap.CompressFormat.JPEG, 70, byteArrayOutputStream);
                        byte[] byteArray = byteArrayOutputStream.toByteArray();
                        StorageReference reference2 = FirebaseStorage.getInstance().getReference();
                        reference2.child(ViewStudentProfileActivity.this.department + "/profile_pictures/" + ViewStudentProfileActivity.this.id).putBytes(byteArray).addOnSuccessListener((OnSuccessListener) new OnSuccessListener<UploadTask.TaskSnapshot>() {
                            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                                taskSnapshot.getStorage().getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                                    public void onSuccess(Uri uri) {
                                        FirebaseDatabase instance = FirebaseDatabase.getInstance();
                                        Student student = new Student(ViewStudentProfileActivity.this.id, ViewStudentProfileActivity.this.user_name.getText().toString().trim(), ViewStudentProfileActivity.this.department, ViewStudentProfileActivity.this.semester, ViewStudentProfileActivity.this.student.getEnrollment(), ViewStudentProfileActivity.this.user_phoneno.getText().toString(), ViewStudentProfileActivity.this.user_email.getText().toString().trim(), ViewStudentProfileActivity.this.user_roll_no.getText().toString().trim(), ViewStudentProfileActivity.this.student.getPassword(), uri.toString(), ViewStudentProfileActivity.this.student.getAccount(), ViewStudentProfileActivity.this.user_skills.getText().toString(), ViewStudentProfileActivity.this.parent_phoneno.getText().toString().trim(), ViewStudentProfileActivity.this.student.getToken(), ViewStudentProfileActivity.this.strsem1, ViewStudentProfileActivity.this.strsem2, ViewStudentProfileActivity.this.strsem3, ViewStudentProfileActivity.this.strsem4, ViewStudentProfileActivity.this.strsem5, ViewStudentProfileActivity.this.strsem6);
                                        final Uri uri2 = uri;
                                        instance.getReference("Students/" + ViewStudentProfileActivity.this.department + "/" + ViewStudentProfileActivity.this.semester + "/" + ViewStudentProfileActivity.this.id).setValue(student).addOnSuccessListener(new OnSuccessListener<Void>() {
                                            public void onSuccess(Void voidR) {
                                                ViewStudentProfileActivity.this.getSharedPreferences("rait", 0).edit().clear().putString("name", ViewStudentProfileActivity.this.user_name.getText().toString().trim()).putString("id", ViewStudentProfileActivity.this.student.getId()).putString("enrollment", ViewStudentProfileActivity.this.student.getEnrollment()).putString("phone", ViewStudentProfileActivity.this.user_phoneno.getText().toString().trim()).putString("branch", ViewStudentProfileActivity.this.student.getDepartment()).putString("semester", ViewStudentProfileActivity.this.student.getSemester()).putString("rollno", ViewStudentProfileActivity.this.user_roll_no.getText().toString().trim()).putString("email", ViewStudentProfileActivity.this.student.getEmail()).putString("password", ViewStudentProfileActivity.this.student.getPassword()).putString(Scopes.PROFILE, uri2.toString()).putString("loginas", "student").commit();
                                                ViewStudentProfileActivity.this.dialog.dismiss();
                                            }
                                        }).addOnFailureListener(new OnFailureListener() {
                                            public void onFailure(@NonNull Exception exc) {
                                                ViewStudentProfileActivity.this.dialog.dismiss();
                                            }
                                        });
                                    }
                                }).addOnFailureListener(new OnFailureListener() {
                                    public void onFailure(@NonNull Exception exc) {
                                        ViewStudentProfileActivity.this.dialog.dismiss();
                                    }
                                });
                            }
                        });
                    }
                } else {
                    ViewStudentProfileActivity.this.user_edit.setText("Update");
                    ViewStudentProfileActivity.this.user_name.setEnabled(true);
                    ViewStudentProfileActivity.this.user_phoneno.setEnabled(true);
                    ViewStudentProfileActivity.this.user_roll_no.setEnabled(true);
                    ViewStudentProfileActivity.this.user_profile.setEnabled(true);
                    ViewStudentProfileActivity.this.user_skills.setEnabled(true);
                    ViewStudentProfileActivity.this.parent_phoneno.setEnabled(true);
                    ViewStudentProfileActivity.this.sem1.setEnabled(true);
                    ViewStudentProfileActivity.this.sem2.setEnabled(true);
                    ViewStudentProfileActivity.this.sem3.setEnabled(true);
                    ViewStudentProfileActivity.this.sem4.setEnabled(true);
                    ViewStudentProfileActivity.this.sem5.setEnabled(true);
                    ViewStudentProfileActivity.this.sem6.setEnabled(true);
                }
            }
        });
        this.user_profile.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                CropImage.startPickImageActivity(ViewStudentProfileActivity.this);
            }
        });
    }

    /* access modifiers changed from: protected */
    public void onActivityResult(int i, int i2, @Nullable Intent intent) {
        super.onActivityResult(i, i2, intent);
        if (i == 200 && i2 == -1) {
            this.imagepath = CropImage.getPickImageResultUri(this, intent);
            CropImage.activity(this.imagepath).setGuidelines(CropImageView.Guidelines.ON).setMultiTouchEnabled(true).setAspectRatio(1, 1).start(this);
        }
        if (i == 203) {
            if (i2 == -1) {
                this.img = CropImage.getActivityResult(intent).getUri();
                try {
                    this.bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), this.img);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            } else {
                this.img = null;
            }
            this.user_profile.setImageURI(this.img);
        }
    }

    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }
}
