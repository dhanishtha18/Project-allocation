package org.projectapp;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Bitmap.CompressFormat;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore.Images.Media;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.SpinnerAdapter;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog.Builder;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;
import androidx.appcompat.widget.AppCompatEditText;
import androidx.appcompat.widget.AppCompatSpinner;
import androidx.appcompat.widget.AppCompatTextView;
import androidx.core.view.ViewCompat;
import androidx.exifinterface.media.ExifInterface;
import androidx.fragment.app.FragmentActivity;
import androidx.lifecycle.Lifecycle.State;
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
import com.google.firebase.storage.UploadTask.TaskSnapshot;
import com.theartofdev.edmodo.cropper.CropImage;
import com.theartofdev.edmodo.cropper.CropImageView.Guidelines;
import de.hdodenhof.circleimageview.CircleImageView;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Iterator;
import org.projectapp.Model.Hod;
import org.projectapp.Model.Staff;
import org.projectapp.Model.Subject;

public class ViewHodProfileActivity extends AppCompatActivity {
    Bitmap bitmap = null;
    AppCompatButton delete_subjects;
    String department;
    ProgressDialog dialog;
    Hod hod = null;
    String id;
    Uri imagepath;
    Uri img;
    String loginas;
    String s = null;
    String[] semArray;
    String semester;
    String slots;
    AppCompatSpinner staff_semester;
    AppCompatSpinner staff_subject_hours;
    AppCompatSpinner staff_subjects;
    String sub;
    ArrayList<String> subList;
    StringBuilder subject;
    ArrayList<String> subjectList;
    ArrayList<String> subjects;
    AppCompatTextView title;
    AppCompatButton user_add_subject;
    AppCompatEditText user_department;
    AppCompatButton user_edit;
    AppCompatEditText user_email;
    AppCompatEditText user_experience;
    AppCompatEditText user_login_id;
    AppCompatEditText user_name;
    AppCompatEditText user_phoneno;
    CircleImageView user_profile;
    AppCompatEditText user_qualification;
    AppCompatEditText user_subject;

    /* access modifiers changed from: protected */
    public void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        setContentView((int) R.layout.activity_view_hod_profile);
        this.user_department = (AppCompatEditText) findViewById(R.id.user_department);
        this.user_name = (AppCompatEditText) findViewById(R.id.user_name);
        this.user_phoneno = (AppCompatEditText) findViewById(R.id.user_phoneno);
        this.user_email = (AppCompatEditText) findViewById(R.id.user_email);
        this.user_edit = (AppCompatButton) findViewById(R.id.user_edit);
        this.user_profile = (CircleImageView) findViewById(R.id.user_profile);
        this.user_subject = (AppCompatEditText) findViewById(R.id.user_subject);
        this.user_qualification = (AppCompatEditText) findViewById(R.id.user_qualification);
        this.user_experience = (AppCompatEditText) findViewById(R.id.user_experience);
        this.user_login_id = (AppCompatEditText) findViewById(R.id.user_login_id);
        this.user_add_subject = (AppCompatButton) findViewById(R.id.user_add_subjects);
        this.title = (AppCompatTextView) findViewById(R.id.title);
        this.delete_subjects = (AppCompatButton) findViewById(R.id.delete_subjects);
        this.user_profile.setEnabled(false);
        this.subject = new StringBuilder();
        this.subject.append("Subjects:");
        this.subList = new ArrayList<>();
        this.title.setText("Profile");
        this.id = getIntent().getStringExtra("id");
        this.department = getIntent().getStringExtra("department");
        this.loginas = getIntent().getStringExtra("loginas");
        if (this.loginas.equals(Scopes.PROFILE)) {
            this.user_edit.setVisibility(View.VISIBLE);
            this.user_add_subject.setVisibility(View.VISIBLE);
        } else {
            this.user_edit.setVisibility(View.GONE);
            this.user_add_subject.setVisibility(View.GONE);
            this.delete_subjects.setVisibility(View.GONE);
        }
        FirebaseDatabase instance = FirebaseDatabase.getInstance();
        StringBuilder sb = new StringBuilder();
        sb.append("HODs/");
        sb.append(this.department);
        instance.getReference(sb.toString()).child(this.id).addListenerForSingleValueEvent(new ValueEventListener() {
            public void onCancelled(@NonNull DatabaseError databaseError) {
            }

            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                ViewHodProfileActivity.this.hod = (Hod) dataSnapshot.getValue(Hod.class);
                if (ViewHodProfileActivity.this.hod.getProfile_url().equals("default")) {
                    ViewHodProfileActivity.this.user_profile.setImageResource(R.drawable.profile);
                } else if (ViewHodProfileActivity.this.getLifecycle().getCurrentState().isAtLeast(State.INITIALIZED)) {
                    Glide.with((FragmentActivity) ViewHodProfileActivity.this).load(ViewHodProfileActivity.this.hod.getProfile_url()).into((ImageView) ViewHodProfileActivity.this.user_profile);
                }
                if (!ViewHodProfileActivity.this.loginas.equals(Scopes.PROFILE)) {
                    ViewHodProfileActivity.this.title.setText(ViewHodProfileActivity.this.hod.getName());
                    if (ViewHodProfileActivity.this.hod.getDepartment().isEmpty()) {
                        ViewHodProfileActivity.this.user_department.setVisibility(View.GONE);
                    }
                    if (ViewHodProfileActivity.this.hod.getName().isEmpty()) {
                        ViewHodProfileActivity.this.user_name.setVisibility(View.GONE);
                    }
                    if (ViewHodProfileActivity.this.hod.getPhone().isEmpty()) {
                        ViewHodProfileActivity.this.user_phoneno.setVisibility(View.GONE);
                    }
                    if (ViewHodProfileActivity.this.hod.getEmail().isEmpty()) {
                        ViewHodProfileActivity.this.user_email.setVisibility(View.GONE);
                    }
                    if (((String) ViewHodProfileActivity.this.hod.getSubjects().get(View.VISIBLE)).equals("subject")) {
                        ViewHodProfileActivity.this.user_subject.setVisibility(View.GONE);
                    }
                    if (ViewHodProfileActivity.this.hod.getQualification() == null) {
                        ViewHodProfileActivity.this.user_qualification.setVisibility(View.GONE);
                    }
                    if (ViewHodProfileActivity.this.hod.getExperience() == null) {
                        ViewHodProfileActivity.this.user_experience.setVisibility(View.GONE);
                    }
                    if (ViewHodProfileActivity.this.hod.getLogin_id().isEmpty()) {
                        ViewHodProfileActivity.this.user_login_id.setVisibility(View.GONE);
                    }
                }
                ViewHodProfileActivity.this.user_department.setText(ViewHodProfileActivity.this.hod.getDepartment());
                ViewHodProfileActivity.this.user_name.setText(ViewHodProfileActivity.this.hod.getName());
                ViewHodProfileActivity.this.user_phoneno.setText(ViewHodProfileActivity.this.hod.getPhone());
                ViewHodProfileActivity.this.user_email.setText(ViewHodProfileActivity.this.hod.getEmail());
                if (ViewHodProfileActivity.this.hod.getSubjects() != null) {
                    Iterator it = ViewHodProfileActivity.this.hod.getSubjects().iterator();
                    while (it.hasNext()) {
                        String str = (String) it.next();
                        StringBuilder sb = ViewHodProfileActivity.this.subject;
                        StringBuilder sb2 = new StringBuilder();
                        sb2.append("\n\t");
                        sb2.append(str);
                        sb.append(sb2.toString());
                    }
                }
                ViewHodProfileActivity.this.user_subject.setText(ViewHodProfileActivity.this.subject);
                ViewHodProfileActivity.this.user_qualification.setText(ViewHodProfileActivity.this.hod.getQualification());
                ViewHodProfileActivity.this.user_experience.setText(ViewHodProfileActivity.this.hod.getExperience());
                ViewHodProfileActivity.this.user_login_id.setText(ViewHodProfileActivity.this.hod.getLogin_id());
            }
        });
        final View inflate = View.inflate(this, R.layout.activity_update_hod_subjects, null);
        this.staff_subjects = (AppCompatSpinner) inflate.findViewById(R.id.staff_subjects);
        this.staff_semester = (AppCompatSpinner) inflate.findViewById(R.id.staff_semester);
        this.staff_subject_hours = (AppCompatSpinner) inflate.findViewById(R.id.staff_subject_hours);
        this.user_add_subject.setOnClickListener(new OnClickListener() {
            public void onClick(View view) {
                Builder builder = new Builder(ViewHodProfileActivity.this);
                builder.setTitle((CharSequence) "Add Your Subjects");
                if (inflate.getParent() != null) {
                    ((ViewGroup) inflate.getParent()).removeView(inflate);
                }
                builder.setView(inflate);
                ViewHodProfileActivity viewHodProfileActivity = ViewHodProfileActivity.this;
                viewHodProfileActivity.department = viewHodProfileActivity.getIntent().getStringExtra("department");
                ViewHodProfileActivity viewHodProfileActivity2 = ViewHodProfileActivity.this;
                viewHodProfileActivity2.id = viewHodProfileActivity2.getIntent().getStringExtra("id");
                Calendar instance = Calendar.getInstance();
                ViewHodProfileActivity.this.subjectList = new ArrayList<>();
                String str = "Select Semester";
                if (ViewHodProfileActivity.this.department.equals("Computer Department")) {
                    if (instance.get(2) < 5 || instance.get(2) == 11) {
                        ViewHodProfileActivity.this.semArray = new String[]{str, "CO2I", "CO4I", "CO6I"};
                    } else {
                        ViewHodProfileActivity.this.semArray = new String[]{str, "CO1I", "CO3I", "CO5I"};
                    }
                } else if (!ViewHodProfileActivity.this.department.equals("IT Department")) {
                    String str2 = "CE5I";
                    String str3 = "CE3I";
                    String str4 = "CE1I";
                    String str5 = "CE6I";
                    String str6 = "CE4I";
                    String str7 = "CE2I";
                    if (ViewHodProfileActivity.this.department.equals("Civil I Shift Department")) {
                        if (instance.get(2) < 5 || instance.get(2) == 11) {
                            ViewHodProfileActivity.this.semArray = new String[]{str, str7, str6, str5};
                        } else {
                            ViewHodProfileActivity.this.semArray = new String[]{str, str4, str3, str2};
                        }
                    } else if (!ViewHodProfileActivity.this.department.equals("Civil II Shift Department")) {
                        String str8 = "ME1I";
                        String str9 = "ME6I";
                        String str10 = "ME4I";
                        String str11 = "ME2I";
                        if (ViewHodProfileActivity.this.department.equals("Mechanical I Shift Department")) {
                            if (instance.get(2) < 5 || instance.get(2) == 11) {
                                ViewHodProfileActivity.this.semArray = new String[]{str, str11, str10, str9};
                            } else {
                                ViewHodProfileActivity.this.semArray = new String[]{str, str8, "ME3I", "ME5I"};
                            }
                        } else if (ViewHodProfileActivity.this.department.equals("Mechanical II Shift Department")) {
                            if (instance.get(2) < 5 || instance.get(2) == 11) {
                                ViewHodProfileActivity.this.semArray = new String[]{str, str11, str10, str9};
                            } else {
                                ViewHodProfileActivity.this.semArray = new String[]{str, str8, "ME3I", "ME5I"};
                            }
                        } else if (ViewHodProfileActivity.this.department.equals("Chemical Department")) {
                            if (instance.get(2) < 5 || instance.get(2) == 11) {
                                ViewHodProfileActivity.this.semArray = new String[]{str, "CH2I", "CH4I", "CH6I"};
                            } else {
                                ViewHodProfileActivity.this.semArray = new String[]{str, "CH1I", "CH3I", "CH5I"};
                            }
                        } else if (ViewHodProfileActivity.this.department.equals("TR Department")) {
                            if (instance.get(2) < 5 || instance.get(2) == 11) {
                                ViewHodProfileActivity.this.semArray = new String[]{str, "TR2G", "TR4G"};
                            } else {
                                ViewHodProfileActivity.this.semArray = new String[]{str, "TR1G", "TR3G", "TR5G"};
                            }
                        }
                    } else if (instance.get(2) < 5 || instance.get(2) == 11) {
                        ViewHodProfileActivity.this.semArray = new String[]{str, str7, str6, str5};
                    } else {
                        ViewHodProfileActivity.this.semArray = new String[]{str, str4, str3, str2};
                    }
                } else if (instance.get(2) < 5 || instance.get(2) == 11) {
                    ViewHodProfileActivity.this.semArray = new String[]{str, "IF2I", "IF4I", "IF6I"};
                } else {
                    ViewHodProfileActivity.this.semArray = new String[]{str, "IF1I", "IF3I", "IF5I"};
                }
                AppCompatSpinner appCompatSpinner = ViewHodProfileActivity.this.staff_semester;
                ViewHodProfileActivity viewHodProfileActivity3 = ViewHodProfileActivity.this;
                appCompatSpinner.setAdapter((SpinnerAdapter) new ArrayAdapter(viewHodProfileActivity3, R.layout.spinner_dialog, viewHodProfileActivity3.semArray));
                ViewHodProfileActivity.this.staff_subject_hours.setAdapter((SpinnerAdapter) new ArrayAdapter(ViewHodProfileActivity.this, R.layout.spinner_dialog, new String[]{"Hours", "1", ExifInterface.GPS_MEASUREMENT_2D, ExifInterface.GPS_MEASUREMENT_3D, "4", "5"}));
                ViewHodProfileActivity.this.staff_subjects.setAdapter((SpinnerAdapter) new ArrayAdapter(ViewHodProfileActivity.this, R.layout.spinner_dialog, new String[]{"Subjects"}));
                builder.setPositiveButton((CharSequence) "Ok", (DialogInterface.OnClickListener) new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialogInterface, int i) {
                        if (ViewHodProfileActivity.this.semester == null || ViewHodProfileActivity.this.sub == null || ViewHodProfileActivity.this.slots == null) {
                            Toast.makeText(ViewHodProfileActivity.this, "Select all parameters.", Toast.LENGTH_SHORT).show();
                            return;
                        }
                        ViewHodProfileActivity.this.staff_subjects.setSelection(View.VISIBLE);
                        ViewHodProfileActivity.this.staff_semester.setSelection(View.VISIBLE);
                        ViewHodProfileActivity.this.staff_subject_hours.setSelection(View.VISIBLE);
                        FirebaseDatabase instance = FirebaseDatabase.getInstance();
                        StringBuilder sb = new StringBuilder();
                        sb.append("HODs/");
                        sb.append(ViewHodProfileActivity.this.department);
                        final DatabaseReference child = instance.getReference(sb.toString()).child(ViewHodProfileActivity.this.id);
                        child.addListenerForSingleValueEvent(new ValueEventListener() {
                            public void onCancelled(@NonNull DatabaseError databaseError) {
                            }

                            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                Hod hod = (Hod) dataSnapshot.getValue(Hod.class);
                                if (((String) hod.getSubjects().get(View.VISIBLE)).equals("subject")) {
                                    hod.getSubjects().clear();
                                }
                                ViewHodProfileActivity.this.subjects = hod.getSubjects();
                                ArrayList<String> arrayList = ViewHodProfileActivity.this.subjects;
                                StringBuilder sb = new StringBuilder();
                                sb.append(ViewHodProfileActivity.this.sub);
                                String str = " ";
                                sb.append(str);
                                sb.append(ViewHodProfileActivity.this.slots);
                                sb.append(str);
                                sb.append(ViewHodProfileActivity.this.semester);
                                arrayList.add(sb.toString());
                                HashMap hashMap = new HashMap();
                                hashMap.put("subjects", ViewHodProfileActivity.this.subjects);
                                child.updateChildren(hashMap).addOnSuccessListener(new OnSuccessListener<Void>() {
                                    public void onSuccess(Void voidR) {
                                        ViewHodProfileActivity.this.recreate();
                                    }
                                });
                            }
                        });
                    }
                });
                builder.setNegativeButton((CharSequence) "Cancel", (DialogInterface.OnClickListener) new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialogInterface, int i) {
                    }
                });
                builder.create();
                if (ViewHodProfileActivity.this.getLifecycle().getCurrentState().isAtLeast(State.CREATED)) {
                    builder.show();
                }
            }
        });
        this.staff_semester.setOnItemSelectedListener(new OnItemSelectedListener() {
            public void onNothingSelected(AdapterView<?> adapterView) {
            }

            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long j) {
                if (adapterView.getItemAtPosition(i).equals("Select Semester")) {
                    ViewHodProfileActivity.this.semester = null;
                    return;
                }
                ViewHodProfileActivity.this.semester = adapterView.getItemAtPosition(i).toString();
                ViewHodProfileActivity.this.subjectList.clear();
                FirebaseDatabase instance = FirebaseDatabase.getInstance();
                StringBuilder sb = new StringBuilder();
                sb.append(ViewHodProfileActivity.this.department);
                sb.append("/subjects/");
                sb.append(ViewHodProfileActivity.this.semester);
                instance.getReference(sb.toString()).addValueEventListener(new ValueEventListener() {
                    public void onCancelled(@NonNull DatabaseError databaseError) {
                    }

                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        for (DataSnapshot value : dataSnapshot.getChildren()) {
                            ViewHodProfileActivity.this.subjectList.add(((Subject) value.getValue(Subject.class)).getName());
                        }
                        ViewHodProfileActivity.this.staff_subjects.setAdapter((SpinnerAdapter) new ArrayAdapter(ViewHodProfileActivity.this, R.layout.spinner_dialog, ViewHodProfileActivity.this.subjectList));
                    }
                });
            }
        });
        this.staff_subject_hours.setOnItemSelectedListener(new OnItemSelectedListener() {
            public void onNothingSelected(AdapterView<?> adapterView) {
            }

            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long j) {
                if (adapterView.getItemAtPosition(i).equals("Hours")) {
                    ViewHodProfileActivity.this.slots = null;
                    return;
                }
                ViewHodProfileActivity.this.slots = adapterView.getItemAtPosition(i).toString();
            }
        });
        this.staff_subjects.setOnItemSelectedListener(new OnItemSelectedListener() {
            public void onNothingSelected(AdapterView<?> adapterView) {
            }

            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long j) {
                if (adapterView.getItemAtPosition(i).equals("Subjects")) {
                    ViewHodProfileActivity.this.sub = null;
                    return;
                }
                ViewHodProfileActivity.this.sub = adapterView.getItemAtPosition(i).toString();
            }
        });
        this.user_edit.setOnClickListener(new OnClickListener() {
            public void onClick(View view) {
                if (ViewHodProfileActivity.this.user_name.isEnabled()) {
                    ViewHodProfileActivity viewHodProfileActivity = ViewHodProfileActivity.this;
                    viewHodProfileActivity.dialog = new ProgressDialog(viewHodProfileActivity);
                    ViewHodProfileActivity.this.dialog.setMessage("Updating...");
                    ViewHodProfileActivity.this.dialog.setCancelable(false);
                    ViewHodProfileActivity.this.dialog.show();
                    ViewHodProfileActivity.this.user_edit.setText("Edit");
                    ViewHodProfileActivity.this.user_name.setEnabled(false);
                    ViewHodProfileActivity.this.user_phoneno.setEnabled(false);
                    ViewHodProfileActivity.this.user_qualification.setEnabled(false);
                    ViewHodProfileActivity.this.user_experience.setEnabled(false);
                    ViewHodProfileActivity.this.user_profile.setEnabled(false);
                    String str = "";
                    String str2 = "Ok";
                    String str3 = "Alert";
                    if (ViewHodProfileActivity.this.user_name.getText().toString().equals(str)) {
                        AlertDialog.Builder builder = new AlertDialog.Builder(ViewHodProfileActivity.this);
                        builder.setMessage("Enter Your Name.");
                        builder.setTitle(str3);
                        builder.setPositiveButton(str2, new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialogInterface, int i) {
                            }
                        });
                        builder.show();
                        ViewHodProfileActivity.this.dialog.dismiss();
                    } else if (ViewHodProfileActivity.this.user_experience.getText().toString().equals(str)) {
                        AlertDialog.Builder builder2 = new AlertDialog.Builder(ViewHodProfileActivity.this);
                        builder2.setMessage("Enter Your Experience.");
                        builder2.setTitle(str3);
                        builder2.setPositiveButton(str2, new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialogInterface, int i) {
                            }
                        });
                        builder2.show();
                        ViewHodProfileActivity.this.dialog.dismiss();
                    } else if (ViewHodProfileActivity.this.user_qualification.getText().toString().equals(str)) {
                        AlertDialog.Builder builder3 = new AlertDialog.Builder(ViewHodProfileActivity.this);
                        builder3.setMessage("Enter Your Qualifications.");
                        builder3.setTitle(str3);
                        builder3.setPositiveButton(str2, new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialogInterface, int i) {
                            }
                        });
                        builder3.show();
                        ViewHodProfileActivity.this.dialog.dismiss();
                    } else {
                        boolean equals = ViewHodProfileActivity.this.user_phoneno.getText().toString().equals(str);
                        String str4 = "Enter Your Phone No.";
                        if (equals) {
                            AlertDialog.Builder builder4 = new AlertDialog.Builder(ViewHodProfileActivity.this);
                            builder4.setMessage(str4);
                            builder4.setTitle(str3);
                            builder4.setPositiveButton(str2, new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialogInterface, int i) {
                                }
                            });
                            builder4.show();
                            ViewHodProfileActivity.this.dialog.dismiss();
                        } else if (ViewHodProfileActivity.this.user_phoneno.getText().toString().trim().length() != 10) {
                            AlertDialog.Builder builder5 = new AlertDialog.Builder(ViewHodProfileActivity.this);
                            builder5.setMessage(str4);
                            builder5.setTitle(str3);
                            builder5.setPositiveButton(str2, new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialogInterface, int i) {
                                }
                            });
                            builder5.show();
                            ViewHodProfileActivity.this.dialog.dismiss();
                        } else if (ViewHodProfileActivity.this.bitmap == null) {
                            FirebaseDatabase instance = FirebaseDatabase.getInstance();
                            StringBuilder sb = new StringBuilder();
                            sb.append("HODs/");
                            sb.append(ViewHodProfileActivity.this.department);
                            sb.append("/");
                            sb.append(ViewHodProfileActivity.this.id);
                            DatabaseReference reference = instance.getReference(sb.toString());
                            String trim = ViewHodProfileActivity.this.user_name.getText().toString().trim();
                            String login_id = ViewHodProfileActivity.this.hod.getLogin_id();
                            String password = ViewHodProfileActivity.this.hod.getPassword();
                            String str5 = ViewHodProfileActivity.this.department;
                            String trim2 = ViewHodProfileActivity.this.user_email.getText().toString().trim();
                            String trim3 = ViewHodProfileActivity.this.user_phoneno.getText().toString().trim();
                            String str6 = ViewHodProfileActivity.this.id;
                            String status = ViewHodProfileActivity.this.hod.getStatus();
                            String account = ViewHodProfileActivity.this.hod.getAccount();
                            String lowerCase = ViewHodProfileActivity.this.user_name.getText().toString().trim().toLowerCase();
                            String profile_url = ViewHodProfileActivity.this.hod.getProfile_url();
                            String trim4 = ViewHodProfileActivity.this.user_qualification.getText().toString().trim();
                            String trim5 = ViewHodProfileActivity.this.user_experience.getText().toString().trim();
                            String token = ViewHodProfileActivity.this.hod.getToken();
                            Hod hod = new Hod(trim, login_id, password, str5, trim2, trim3, str6, status, account, lowerCase, profile_url, trim4, trim5, token, ViewHodProfileActivity.this.hod.getSubjects());
                            reference.setValue(hod).addOnSuccessListener(new OnSuccessListener<Void>() {
                                public void onSuccess(Void voidR) {
                                    String str = "id";
                                    String str2 = "login_id";
                                    String str3 = "phone";
                                    String str4 = "email";
                                    String str5 = "password";
                                    String str6 = "department";
                                    ViewHodProfileActivity.this.getSharedPreferences("rait", 0).edit().clear().putString("name", ViewHodProfileActivity.this.user_name.getText().toString().trim()).putString(str, ViewHodProfileActivity.this.hod.getId()).putString(str2, ViewHodProfileActivity.this.hod.getLogin_id()).putString(str3, ViewHodProfileActivity.this.user_phoneno.getText().toString().trim()).putString(str4, ViewHodProfileActivity.this.hod.getEmail()).putString(str5, ViewHodProfileActivity.this.hod.getPassword()).putString(str6, ViewHodProfileActivity.this.hod.getDepartment()).putString(Scopes.PROFILE, ViewHodProfileActivity.this.hod.getProfile_url()).putString("loginas", "hod").commit();
                                    ViewHodProfileActivity.this.dialog.dismiss();
                                }
                            }).addOnFailureListener(new OnFailureListener() {
                                public void onFailure(@NonNull Exception exc) {
                                    ViewHodProfileActivity.this.dialog.dismiss();
                                }
                            });
                            return;
                        } else {
                            ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
                            ViewHodProfileActivity.this.bitmap.compress(CompressFormat.JPEG, 70, byteArrayOutputStream);
                            byte[] byteArray = byteArrayOutputStream.toByteArray();
                            StorageReference reference2 = FirebaseStorage.getInstance().getReference();
                            StringBuilder sb2 = new StringBuilder();
                            sb2.append(ViewHodProfileActivity.this.department);
                            sb2.append("/profile_pictures/");
                            sb2.append(ViewHodProfileActivity.this.id);
                            reference2.child(sb2.toString()).putBytes(byteArray).addOnSuccessListener((OnSuccessListener) new OnSuccessListener<TaskSnapshot>() {
                                public void onSuccess(TaskSnapshot taskSnapshot) {
                                    taskSnapshot.getStorage().getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                                        public void onSuccess(Uri uri) {
                                            FirebaseDatabase instance = FirebaseDatabase.getInstance();
                                            StringBuilder sb = new StringBuilder();
                                            sb.append("HODs/");
                                            sb.append(ViewHodProfileActivity.this.department);
                                            sb.append("/");
                                            sb.append(ViewHodProfileActivity.this.id);
                                            DatabaseReference reference = instance.getReference(sb.toString());
                                            String trim = ViewHodProfileActivity.this.user_name.getText().toString().trim();
                                            String login_id = ViewHodProfileActivity.this.hod.getLogin_id();
                                            String password = ViewHodProfileActivity.this.hod.getPassword();
                                            String str = ViewHodProfileActivity.this.department;
                                            String trim2 = ViewHodProfileActivity.this.user_email.getText().toString().trim();
                                            String trim3 = ViewHodProfileActivity.this.user_phoneno.getText().toString().trim();
                                            String str2 = ViewHodProfileActivity.this.id;
                                            String status = ViewHodProfileActivity.this.hod.getStatus();
                                            String account = ViewHodProfileActivity.this.hod.getAccount();
                                            String lowerCase = ViewHodProfileActivity.this.user_name.getText().toString().trim().toLowerCase();
                                            String uri2 = uri.toString();
                                            String trim4 = ViewHodProfileActivity.this.user_qualification.getText().toString().trim();
                                            String trim5 = ViewHodProfileActivity.this.user_experience.getText().toString().trim();
                                            String token = ViewHodProfileActivity.this.hod.getToken();
                                            Hod hod = new Hod(trim, login_id, password, str, trim2, trim3, str2, status, account, lowerCase, uri2, trim4, trim5, token, ViewHodProfileActivity.this.hod.getSubjects());
                                            final Uri uri3 = uri;
                                            reference.setValue(hod).addOnSuccessListener(new OnSuccessListener<Void>() {
                                                public void onSuccess(Void voidR) {
                                                    String str = "id";
                                                    String str2 = "login_id";
                                                    String str3 = "phone";
                                                    String str4 = "email";
                                                    String str5 = "password";
                                                    String str6 = "department";
                                                    ViewHodProfileActivity.this.getSharedPreferences("rait", 0).edit().clear().putString("name", ViewHodProfileActivity.this.user_name.getText().toString().trim()).putString(str, ViewHodProfileActivity.this.hod.getId()).putString(str2, ViewHodProfileActivity.this.hod.getLogin_id()).putString(str3, ViewHodProfileActivity.this.user_phoneno.getText().toString().trim()).putString(str4, ViewHodProfileActivity.this.hod.getEmail()).putString(str5, ViewHodProfileActivity.this.hod.getPassword()).putString(str6, ViewHodProfileActivity.this.hod.getDepartment()).putString(Scopes.PROFILE, uri3.toString()).putString("loginas", "hod").commit();
                                                    ViewHodProfileActivity.this.dialog.dismiss();
                                                }
                                            }).addOnFailureListener(new OnFailureListener() {
                                                public void onFailure(@NonNull Exception exc) {
                                                    ViewHodProfileActivity.this.dialog.dismiss();
                                                }
                                            });
                                        }
                                    }).addOnFailureListener(new OnFailureListener() {
                                        public void onFailure(@NonNull Exception exc) {
                                            ViewHodProfileActivity.this.dialog.dismiss();
                                        }
                                    });
                                }
                            });
                            return;
                        }
                    }
                    return;
                }
                ViewHodProfileActivity.this.user_edit.setText("Update");
                ViewHodProfileActivity.this.user_name.setEnabled(true);
                ViewHodProfileActivity.this.user_phoneno.setEnabled(true);
                ViewHodProfileActivity.this.user_qualification.setEnabled(true);
                ViewHodProfileActivity.this.user_experience.setEnabled(true);
                ViewHodProfileActivity.this.user_profile.setEnabled(true);
            }
        });
        this.user_profile.setOnClickListener(new OnClickListener() {
            public void onClick(View view) {
                CropImage.startPickImageActivity(ViewHodProfileActivity.this);
            }
        });
        this.delete_subjects.setOnClickListener(new OnClickListener() {
            public void onClick(View view) {
                LinearLayout linearLayout = new LinearLayout(ViewHodProfileActivity.this);
                linearLayout.setOrientation(LinearLayout.VERTICAL);
                linearLayout.setGravity(17);
                linearLayout.setBackgroundColor(ViewCompat.MEASURED_STATE_MASK);
                View inflate = ((LayoutInflater) ViewHodProfileActivity.this.getSystemService(LAYOUT_INFLATER_SERVICE)).inflate(R.layout.layout_list, null);
                final ListView listView = (ListView) inflate.findViewById(R.id.list);
                AppCompatButton appCompatButton = (AppCompatButton) inflate.findViewById(R.id.done);
                AppCompatButton appCompatButton2 = (AppCompatButton) inflate.findViewById(R.id.cancel);
                final PopupWindow popupWindow = new PopupWindow(inflate, -1, -1);
                popupWindow.showAtLocation(linearLayout, 17, 0, 0);
                final DatabaseReference child = FirebaseDatabase.getInstance().getReference("HODs").child(ViewHodProfileActivity.this.department).child(ViewHodProfileActivity.this.id);
                child.addListenerForSingleValueEvent(new ValueEventListener() {
                    public void onCancelled(@NonNull DatabaseError databaseError) {
                    }

                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        if (dataSnapshot.getChildrenCount() != 0) {
                            Staff staff = (Staff) dataSnapshot.getValue(Staff.class);
                            if (!staff.getSubjects().isEmpty()) {
                                ViewHodProfileActivity.this.subList = staff.getSubjects();
                                listView.setAdapter(new ArrayAdapter(ViewHodProfileActivity.this, R.layout.spinner_dialog, ViewHodProfileActivity.this.subList));
                            }
                        }
                    }
                });
                appCompatButton.setOnClickListener(new OnClickListener() {
                    public void onClick(View view) {
                        if (ViewHodProfileActivity.this.subList.size() == 0) {
                            ViewHodProfileActivity.this.subList.add("subject");
                        }
                        HashMap hashMap = new HashMap();
                        hashMap.put("subjects", ViewHodProfileActivity.this.subList);
                        child.updateChildren(hashMap).addOnSuccessListener(new OnSuccessListener<Void>() {
                            public void onSuccess(Void voidR) {
                                popupWindow.dismiss();
                                Toast.makeText(ViewHodProfileActivity.this, "Subjects Deleted", Toast.LENGTH_SHORT).show();
                                ViewHodProfileActivity.this.recreate();
                            }
                        });
                    }
                });
                appCompatButton2.setOnClickListener(new OnClickListener() {
                    public void onClick(View view) {
                        popupWindow.dismiss();
                    }
                });
                listView.setOnItemClickListener(new OnItemClickListener() {
                    public void onItemClick(AdapterView<?> adapterView, View view, int i, long j) {
                        ViewHodProfileActivity.this.s = adapterView.getItemAtPosition(i).toString();
                        ViewHodProfileActivity.this.subList.remove(ViewHodProfileActivity.this.s);
                        listView.setAdapter(new ArrayAdapter(ViewHodProfileActivity.this, R.layout.spinner_dialog, ViewHodProfileActivity.this.subList));
                    }
                });
            }
        });
    }

    /* access modifiers changed from: protected */
    public void onActivityResult(int i, int i2, @Nullable Intent intent) {
        super.onActivityResult(i, i2, intent);
        if (i == 200 && i2 == -1) {
            this.imagepath = CropImage.getPickImageResultUri(this, intent);
            CropImage.activity(this.imagepath).setGuidelines(Guidelines.ON).setMultiTouchEnabled(true).setAspectRatio(1, 1).start(this);
        }
        if (i == 203) {
            if (i2 == -1) {
                this.img = CropImage.getActivityResult(intent).getUri();
                try {
                    this.bitmap = Media.getBitmap(getContentResolver(), this.img);
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
