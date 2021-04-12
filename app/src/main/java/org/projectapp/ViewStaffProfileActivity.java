package org.projectapp;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.SpinnerAdapter;
import android.widget.TableLayout;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;
import androidx.appcompat.widget.AppCompatEditText;
import androidx.appcompat.widget.AppCompatSpinner;
import androidx.appcompat.widget.AppCompatTextView;
import androidx.exifinterface.media.ExifInterface;
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
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Iterator;
import org.projectapp.Model.Staff;
import org.projectapp.Model.Subject;

import de.hdodenhof.circleimageview.CircleImageView;

public class ViewStaffProfileActivity extends AppCompatActivity {
    Bitmap bitmap = null;
    String department;
    ProgressDialog dialog;

    /* renamed from: id */
    String id;
    Uri imagepath;
    Uri img;
    String loginas;

    /* renamed from: s */
    String s = null;
    String[] semArray;
    String semester;
    String slots;
    Staff staff = null;
    AppCompatSpinner staff_semester;
    AppCompatSpinner staff_subject_hours;
    AppCompatSpinner staff_subjects;
    String sub;
    ArrayList<String> subList;
    StringBuilder subject;
    ArrayList<String> subjectList;
    AppCompatButton subject_add;
    AppCompatButton subject_delete;
    ArrayList<String> subjects;
    AppCompatTextView title;

    /* renamed from: tl */
    TableLayout tl;
    AppCompatEditText user_department;
    AppCompatButton user_edit;
    AppCompatEditText user_email;
    AppCompatEditText user_experience;
    AppCompatEditText user_login_id;
    AppCompatEditText user_mode;
    AppCompatEditText user_name;
    AppCompatEditText user_phoneno;
    CircleImageView user_profile;
    AppCompatEditText user_qualification;
    AppCompatEditText user_subject;

    /* access modifiers changed from: protected */
    public void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        setContentView((int) R.layout.activity_view_staff_profile);
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
        this.user_mode = (AppCompatEditText) findViewById(R.id.user_mode);
        this.title = (AppCompatTextView) findViewById(R.id.title);
        this.tl = (TableLayout) findViewById(R.id.tl);
        this.subject_add = (AppCompatButton) findViewById(R.id.subject_add);
        this.subject_delete = (AppCompatButton) findViewById(R.id.subject_delete);
        this.user_profile.setEnabled(false);
        this.subject = new StringBuilder();
        this.subject.append("Subjects:");
        this.title.setText("Profile");
        this.id = getIntent().getStringExtra("id");
        this.department = getIntent().getStringExtra("department");
        this.loginas = getIntent().getStringExtra("loginas");
        if (this.loginas.equals(Scopes.PROFILE)) {
            this.user_edit.setVisibility(View.VISIBLE);
            this.tl.setVisibility(View.GONE);
        } else if (this.loginas.equals("hod")) {
            this.user_edit.setVisibility(View.GONE);
            this.tl.setVisibility(View.VISIBLE);
        } else {
            this.user_edit.setVisibility(View.GONE);
            this.tl.setVisibility(View.GONE);
        }
        this.subList = new ArrayList<>();
        FirebaseDatabase instance = FirebaseDatabase.getInstance();
        instance.getReference("Staff/" + this.department + "/" + this.id).addValueEventListener(new ValueEventListener() {
            public void onCancelled(@NonNull DatabaseError databaseError) {
            }

            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                ViewStaffProfileActivity.this.staff = (Staff) dataSnapshot.getValue(Staff.class);
                if (ViewStaffProfileActivity.this.staff.getProfile_url().equals("default")) {
                    ViewStaffProfileActivity.this.user_profile.setImageResource(R.drawable.profile);
                } else if (ViewStaffProfileActivity.this.getLifecycle().getCurrentState().isAtLeast(Lifecycle.State.INITIALIZED)) {
                    Glide.with((FragmentActivity) ViewStaffProfileActivity.this).load(ViewStaffProfileActivity.this.staff.getProfile_url()).into((ImageView) ViewStaffProfileActivity.this.user_profile);
                }
                if (!ViewStaffProfileActivity.this.loginas.equals(Scopes.PROFILE)) {
                    ViewStaffProfileActivity.this.title.setText(ViewStaffProfileActivity.this.staff.getName());
                    if (ViewStaffProfileActivity.this.staff.getDepartment().isEmpty()) {
                        ViewStaffProfileActivity.this.user_department.setVisibility(View.GONE);
                    }
                    if (ViewStaffProfileActivity.this.staff.getName().isEmpty()) {
                        ViewStaffProfileActivity.this.user_name.setVisibility(View.GONE);
                    }
                    if (ViewStaffProfileActivity.this.staff.getPhone().isEmpty()) {
                        ViewStaffProfileActivity.this.user_phoneno.setVisibility(View.GONE);
                    }
                    if (ViewStaffProfileActivity.this.staff.getEmail().isEmpty()) {
                        ViewStaffProfileActivity.this.user_email.setVisibility(View.GONE);
                    }
                    if (ViewStaffProfileActivity.this.staff.getSubjects().get(0).equals("none")) {
                        ViewStaffProfileActivity.this.user_subject.setVisibility(View.GONE);
                    }
                    if (ViewStaffProfileActivity.this.staff.getQualification() == null) {
                        ViewStaffProfileActivity.this.user_qualification.setVisibility(View.GONE);
                    }
                    if (ViewStaffProfileActivity.this.staff.getExperience() == null) {
                        ViewStaffProfileActivity.this.user_experience.setVisibility(View.GONE);
                    }
                    if (ViewStaffProfileActivity.this.staff.getLogin_id().isEmpty()) {
                        ViewStaffProfileActivity.this.user_login_id.setVisibility(View.GONE);
                    }
                }
                ViewStaffProfileActivity.this.user_department.setText(ViewStaffProfileActivity.this.staff.getDepartment());
                ViewStaffProfileActivity.this.user_name.setText(ViewStaffProfileActivity.this.staff.getName());
                ViewStaffProfileActivity.this.user_phoneno.setText(ViewStaffProfileActivity.this.staff.getPhone());
                ViewStaffProfileActivity.this.user_email.setText(ViewStaffProfileActivity.this.staff.getEmail());
                if (ViewStaffProfileActivity.this.staff.getSubjects() != null) {
                    Iterator<String> it = ViewStaffProfileActivity.this.staff.getSubjects().iterator();
                    while (it.hasNext()) {
                        StringBuilder sb = ViewStaffProfileActivity.this.subject;
                        sb.append("\n\t" + it.next());
                    }
                }
                ViewStaffProfileActivity.this.user_subject.setText(ViewStaffProfileActivity.this.subject);
                ViewStaffProfileActivity.this.user_qualification.setText(ViewStaffProfileActivity.this.staff.getQualification());
                ViewStaffProfileActivity.this.user_experience.setText(ViewStaffProfileActivity.this.staff.getExperience());
                ViewStaffProfileActivity.this.user_login_id.setText(ViewStaffProfileActivity.this.staff.getLogin_id());
                AppCompatEditText appCompatEditText = ViewStaffProfileActivity.this.user_mode;
                appCompatEditText.setText("Mode: " + ViewStaffProfileActivity.this.staff.getMode());
            }
        });
        this.user_edit.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                if (ViewStaffProfileActivity.this.user_name.isEnabled()) {
                    ViewStaffProfileActivity viewStaffProfileActivity = ViewStaffProfileActivity.this;
                    viewStaffProfileActivity.dialog = new ProgressDialog(viewStaffProfileActivity);
                    ViewStaffProfileActivity.this.dialog.setMessage("Updating...");
                    ViewStaffProfileActivity.this.dialog.setCancelable(false);
                    ViewStaffProfileActivity.this.dialog.show();
                    ViewStaffProfileActivity.this.user_edit.setText("Edit");
                    ViewStaffProfileActivity.this.user_name.setEnabled(false);
                    ViewStaffProfileActivity.this.user_phoneno.setEnabled(false);
                    ViewStaffProfileActivity.this.user_qualification.setEnabled(false);
                    ViewStaffProfileActivity.this.user_experience.setEnabled(false);
                    ViewStaffProfileActivity.this.user_profile.setEnabled(false);
                    if (ViewStaffProfileActivity.this.user_name.getText().toString().equals("")) {
                        AlertDialog.Builder builder = new AlertDialog.Builder(ViewStaffProfileActivity.this);
                        builder.setMessage("Enter Your Name.");
                        builder.setTitle("Alert");
                        builder.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialogInterface, int i) {
                            }
                        });
                        builder.show();
                        ViewStaffProfileActivity.this.dialog.dismiss();
                    } else if (ViewStaffProfileActivity.this.user_experience.getText().toString().equals("")) {
                        AlertDialog.Builder builder2 = new AlertDialog.Builder(ViewStaffProfileActivity.this);
                        builder2.setMessage("Enter Your Experience.");
                        builder2.setTitle("Alert");
                        builder2.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialogInterface, int i) {
                            }
                        });
                        builder2.show();
                        ViewStaffProfileActivity.this.dialog.dismiss();
                    } else if (ViewStaffProfileActivity.this.user_qualification.getText().toString().equals("")) {
                        AlertDialog.Builder builder3 = new AlertDialog.Builder(ViewStaffProfileActivity.this);
                        builder3.setMessage("Enter Your Qualifications.");
                        builder3.setTitle("Alert");
                        builder3.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialogInterface, int i) {
                            }
                        });
                        builder3.show();
                        ViewStaffProfileActivity.this.dialog.dismiss();
                    } else if (ViewStaffProfileActivity.this.user_phoneno.getText().toString().equals("")) {
                        AlertDialog.Builder builder4 = new AlertDialog.Builder(ViewStaffProfileActivity.this);
                        builder4.setMessage("Enter Your Phone No.");
                        builder4.setTitle("Alert");
                        builder4.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialogInterface, int i) {
                            }
                        });
                        builder4.show();
                        ViewStaffProfileActivity.this.dialog.dismiss();
                    } else if (ViewStaffProfileActivity.this.user_phoneno.getText().toString().trim().length() != 10) {
                        AlertDialog.Builder builder5 = new AlertDialog.Builder(ViewStaffProfileActivity.this);
                        builder5.setMessage("Enter Your Phone No.");
                        builder5.setTitle("Alert");
                        builder5.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialogInterface, int i) {
                            }
                        });
                        builder5.show();
                        ViewStaffProfileActivity.this.dialog.dismiss();
                    } else if (ViewStaffProfileActivity.this.bitmap == null) {
                        FirebaseDatabase instance = FirebaseDatabase.getInstance();
                        DatabaseReference reference = instance.getReference("Staff/" + ViewStaffProfileActivity.this.department + "/" + ViewStaffProfileActivity.this.id);
                        Staff staff = new Staff(ViewStaffProfileActivity.this.user_name.getText().toString().trim(), ViewStaffProfileActivity.this.user_email.getText().toString().trim(), ViewStaffProfileActivity.this.staff.getPhone(), ViewStaffProfileActivity.this.staff.getLogin_id(), ViewStaffProfileActivity.this.staff.getPassword(), ViewStaffProfileActivity.this.department, ViewStaffProfileActivity.this.staff.getMode(), ViewStaffProfileActivity.this.id, ViewStaffProfileActivity.this.staff.getStatus(), ViewStaffProfileActivity.this.staff.getAccount(), ViewStaffProfileActivity.this.user_name.getText().toString().trim().toLowerCase(), ViewStaffProfileActivity.this.staff.getProfile_url(), ViewStaffProfileActivity.this.user_qualification.getText().toString().trim(), ViewStaffProfileActivity.this.user_experience.getText().toString().trim(), ViewStaffProfileActivity.this.staff.getToken(), ViewStaffProfileActivity.this.staff.getSubjects());
                        reference.setValue(staff).addOnSuccessListener(new OnSuccessListener<Void>() {
                            public void onSuccess(Void voidR) {
                                ViewStaffProfileActivity.this.getSharedPreferences("rait", 0).edit().clear().putString("name", ViewStaffProfileActivity.this.user_name.getText().toString().trim()).putString("id", ViewStaffProfileActivity.this.staff.getId()).putString("login_id", ViewStaffProfileActivity.this.staff.getLogin_id()).putString("phone", ViewStaffProfileActivity.this.user_phoneno.getText().toString().trim()).putString("email", ViewStaffProfileActivity.this.staff.getEmail()).putString("password", ViewStaffProfileActivity.this.staff.getPassword()).putString("department", ViewStaffProfileActivity.this.staff.getDepartment()).putString("mode", ViewStaffProfileActivity.this.staff.getMode()).putString(Scopes.PROFILE, ViewStaffProfileActivity.this.staff.getProfile_url()).putString("loginas", "lecturer").commit();
                                ViewStaffProfileActivity.this.dialog.dismiss();
                            }
                        }).addOnFailureListener(new OnFailureListener() {
                            public void onFailure(@NonNull Exception exc) {
                                ViewStaffProfileActivity.this.dialog.dismiss();
                            }
                        });
                    } else {
                        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
                        ViewStaffProfileActivity.this.bitmap.compress(Bitmap.CompressFormat.JPEG, 70, byteArrayOutputStream);
                        byte[] byteArray = byteArrayOutputStream.toByteArray();
                        StorageReference reference2 = FirebaseStorage.getInstance().getReference();
                        reference2.child(ViewStaffProfileActivity.this.department + "/profile_pictures/" + ViewStaffProfileActivity.this.id).putBytes(byteArray).addOnSuccessListener((OnSuccessListener) new OnSuccessListener<UploadTask.TaskSnapshot>() {
                            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                                taskSnapshot.getStorage().getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                                    public void onSuccess(final Uri uri) {
                                        FirebaseDatabase instance = FirebaseDatabase.getInstance();
                                        Staff staff = new Staff(ViewStaffProfileActivity.this.user_name.getText().toString().trim(), ViewStaffProfileActivity.this.user_email.getText().toString().trim(), ViewStaffProfileActivity.this.staff.getPhone(), ViewStaffProfileActivity.this.staff.getLogin_id(), ViewStaffProfileActivity.this.staff.getPassword(), ViewStaffProfileActivity.this.department, ViewStaffProfileActivity.this.staff.getMode(), ViewStaffProfileActivity.this.id, ViewStaffProfileActivity.this.staff.getStatus(), ViewStaffProfileActivity.this.staff.getAccount(), ViewStaffProfileActivity.this.user_name.getText().toString().trim().toLowerCase(), uri.toString(), ViewStaffProfileActivity.this.user_qualification.getText().toString().trim(), ViewStaffProfileActivity.this.user_experience.getText().toString().trim(), ViewStaffProfileActivity.this.staff.getToken(), ViewStaffProfileActivity.this.staff.getSubjects());
                                        instance.getReference("Staff/" + ViewStaffProfileActivity.this.department + "/" + ViewStaffProfileActivity.this.id).setValue(staff).addOnSuccessListener(new OnSuccessListener<Void>() {
                                            public void onSuccess(Void voidR) {
                                                ViewStaffProfileActivity.this.getSharedPreferences("rait", 0).edit().clear().putString("name", ViewStaffProfileActivity.this.user_name.getText().toString().trim()).putString("id", ViewStaffProfileActivity.this.staff.getId()).putString("login_id", ViewStaffProfileActivity.this.staff.getLogin_id()).putString("phone", ViewStaffProfileActivity.this.user_phoneno.getText().toString().trim()).putString("email", ViewStaffProfileActivity.this.staff.getEmail()).putString("password", ViewStaffProfileActivity.this.staff.getPassword()).putString("department", ViewStaffProfileActivity.this.staff.getDepartment()).putString("mode", ViewStaffProfileActivity.this.staff.getMode()).putString(Scopes.PROFILE, uri.toString()).putString("loginas", "lecturer").commit();
                                                ViewStaffProfileActivity.this.dialog.dismiss();
                                            }
                                        }).addOnFailureListener(new OnFailureListener() {
                                            public void onFailure(@NonNull Exception exc) {
                                                ViewStaffProfileActivity.this.dialog.dismiss();
                                            }
                                        });
                                    }
                                }).addOnFailureListener(new OnFailureListener() {
                                    public void onFailure(@NonNull Exception exc) {
                                        ViewStaffProfileActivity.this.dialog.dismiss();
                                    }
                                });
                            }
                        });
                    }
                } else {
                    ViewStaffProfileActivity.this.user_edit.setText("Update");
                    ViewStaffProfileActivity.this.user_name.setEnabled(true);
                    ViewStaffProfileActivity.this.user_phoneno.setEnabled(true);
                    ViewStaffProfileActivity.this.user_qualification.setEnabled(true);
                    ViewStaffProfileActivity.this.user_experience.setEnabled(true);
                    ViewStaffProfileActivity.this.user_profile.setEnabled(true);
                }
            }
        });
        this.user_profile.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                CropImage.startPickImageActivity(ViewStaffProfileActivity.this);
            }
        });
        final View inflate = View.inflate(this, R.layout.activity_update_hod_subjects, (ViewGroup) null);
        this.staff_subjects = (AppCompatSpinner) inflate.findViewById(R.id.staff_subjects);
        this.staff_semester = (AppCompatSpinner) inflate.findViewById(R.id.staff_semester);
        this.staff_subject_hours = (AppCompatSpinner) inflate.findViewById(R.id.staff_subject_hours);
        this.subject_add.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                AlertDialog.Builder builder = new AlertDialog.Builder(ViewStaffProfileActivity.this);
                builder.setTitle((CharSequence) "Add Subjects");
                if (inflate.getParent() != null) {
                    ((ViewGroup) inflate.getParent()).removeView(inflate);
                }
                builder.setView(inflate);
                ViewStaffProfileActivity viewStaffProfileActivity = ViewStaffProfileActivity.this;
                viewStaffProfileActivity.department = viewStaffProfileActivity.getIntent().getStringExtra("department");
                ViewStaffProfileActivity viewStaffProfileActivity2 = ViewStaffProfileActivity.this;
                viewStaffProfileActivity2.id = viewStaffProfileActivity2.getIntent().getStringExtra("id");
                Calendar instance = Calendar.getInstance();
                ViewStaffProfileActivity.this.subjectList = new ArrayList<>();
                if (ViewStaffProfileActivity.this.department.equals("Computer Department")) {
                    if (instance.get(2) < 5 || instance.get(2) == 11) {
                        ViewStaffProfileActivity.this.semArray = new String[]{"Select Semester", "CO2I", "CO4I", "CO6I"};
                    } else {
                        ViewStaffProfileActivity.this.semArray = new String[]{"Select Semester", "CO1I", "CO3I", "CO5I"};
                    }
                } else if (ViewStaffProfileActivity.this.department.equals("IT Department")) {
                    if (instance.get(2) < 5 || instance.get(2) == 11) {
                        ViewStaffProfileActivity.this.semArray = new String[]{"Select Semester", "IF2I", "IF4I", "IF6I"};
                    } else {
                        ViewStaffProfileActivity.this.semArray = new String[]{"Select Semester", "IF1I", "IF3I", "IF5I"};
                    }
                } else if (ViewStaffProfileActivity.this.department.equals("Civil I Shift Department")) {
                    if (instance.get(2) < 5 || instance.get(2) == 11) {
                        ViewStaffProfileActivity.this.semArray = new String[]{"Select Semester", "CE2I", "CE4I", "CE6I"};
                    } else {
                        ViewStaffProfileActivity.this.semArray = new String[]{"Select Semester", "CE1I", "CE3I", "CE5I"};
                    }
                } else if (ViewStaffProfileActivity.this.department.equals("Civil II Shift Department")) {
                    if (instance.get(2) < 5 || instance.get(2) == 11) {
                        ViewStaffProfileActivity.this.semArray = new String[]{"Select Semester", "CE2I", "CE4I", "CE6I"};
                    } else {
                        ViewStaffProfileActivity.this.semArray = new String[]{"Select Semester", "CE1I", "CE3I", "CE5I"};
                    }
                } else if (ViewStaffProfileActivity.this.department.equals("Mechanical I Shift Department")) {
                    if (instance.get(2) < 5 || instance.get(2) == 11) {
                        ViewStaffProfileActivity.this.semArray = new String[]{"Select Semester", "ME2I", "ME4I", "ME6I"};
                    } else {
                        ViewStaffProfileActivity.this.semArray = new String[]{"Select Semester", "ME1I", "ME3I", "ME5I"};
                    }
                } else if (ViewStaffProfileActivity.this.department.equals("Mechanical II Shift Department")) {
                    if (instance.get(2) < 5 || instance.get(2) == 11) {
                        ViewStaffProfileActivity.this.semArray = new String[]{"Select Semester", "ME2I", "ME4I", "ME6I"};
                    } else {
                        ViewStaffProfileActivity.this.semArray = new String[]{"Select Semester", "ME1I", "ME3I", "ME5I"};
                    }
                } else if (ViewStaffProfileActivity.this.department.equals("Chemical Department")) {
                    if (instance.get(2) < 5 || instance.get(2) == 11) {
                        ViewStaffProfileActivity.this.semArray = new String[]{"Select Semester", "CH2I", "CH4I", "CH6I"};
                    } else {
                        ViewStaffProfileActivity.this.semArray = new String[]{"Select Semester", "CH1I", "CH3I", "CH5I"};
                    }
                } else if (ViewStaffProfileActivity.this.department.equals("TR Department")) {
                    if (instance.get(2) < 5 || instance.get(2) == 11) {
                        ViewStaffProfileActivity.this.semArray = new String[]{"Select Semester", "TR2G", "TR4G"};
                    } else {
                        ViewStaffProfileActivity.this.semArray = new String[]{"Select Semester", "TR1G", "TR3G", "TR5G"};
                    }
                }
                AppCompatSpinner appCompatSpinner = ViewStaffProfileActivity.this.staff_semester;
                ViewStaffProfileActivity viewStaffProfileActivity3 = ViewStaffProfileActivity.this;
                appCompatSpinner.setAdapter((SpinnerAdapter) new ArrayAdapter(viewStaffProfileActivity3, R.layout.spinner_dialog, viewStaffProfileActivity3.semArray));
                ViewStaffProfileActivity.this.staff_subject_hours.setAdapter((SpinnerAdapter) new ArrayAdapter(ViewStaffProfileActivity.this, R.layout.spinner_dialog, new String[]{"Hours", "1", ExifInterface.GPS_MEASUREMENT_2D, ExifInterface.GPS_MEASUREMENT_3D, "4", "5"}));
                ViewStaffProfileActivity.this.staff_subjects.setAdapter((SpinnerAdapter) new ArrayAdapter(ViewStaffProfileActivity.this, R.layout.spinner_dialog, new String[]{"Subjects"}));
                builder.setPositiveButton((CharSequence) "Ok", (DialogInterface.OnClickListener) new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialogInterface, int i) {
                        if (ViewStaffProfileActivity.this.semester == null || ViewStaffProfileActivity.this.sub == null || ViewStaffProfileActivity.this.slots == null) {
                            Toast.makeText(ViewStaffProfileActivity.this, "Select all parameters.", Toast.LENGTH_SHORT).show();
                            return;
                        }
                        ViewStaffProfileActivity.this.staff_subjects.setSelection(0);
                        ViewStaffProfileActivity.this.staff_semester.setSelection(0);
                        ViewStaffProfileActivity.this.staff_subject_hours.setSelection(0);
                        FirebaseDatabase instance = FirebaseDatabase.getInstance();
                        final DatabaseReference child = instance.getReference("Staff/" + ViewStaffProfileActivity.this.department).child(ViewStaffProfileActivity.this.id);
                        child.addListenerForSingleValueEvent(new ValueEventListener() {
                            public void onCancelled(@NonNull DatabaseError databaseError) {
                            }

                            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                Staff staff = (Staff) dataSnapshot.getValue(Staff.class);
                                if (staff.getSubjects().get(0).equals("none")) {
                                    staff.getSubjects().clear();
                                }
                                ViewStaffProfileActivity.this.subjects = staff.getSubjects();
                                ArrayList<String> arrayList = ViewStaffProfileActivity.this.subjects;
                                arrayList.add(ViewStaffProfileActivity.this.sub + " " + ViewStaffProfileActivity.this.slots + " " + ViewStaffProfileActivity.this.semester);
                                HashMap hashMap = new HashMap();
                                hashMap.put("subjects", ViewStaffProfileActivity.this.subjects);
                                child.updateChildren(hashMap).addOnSuccessListener(new OnSuccessListener<Void>() {
                                    public void onSuccess(Void voidR) {
                                        ViewStaffProfileActivity.this.recreate();
                                        Toast.makeText(ViewStaffProfileActivity.this, "Subject Added", Toast.LENGTH_SHORT).show();
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
                if (ViewStaffProfileActivity.this.getLifecycle().getCurrentState().isAtLeast(Lifecycle.State.CREATED)) {
                    builder.show();
                }
            }
        });
        this.staff_semester.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            public void onNothingSelected(AdapterView<?> adapterView) {
            }

            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long j) {
                if (adapterView.getItemAtPosition(i).equals("Select Semester")) {
                    ViewStaffProfileActivity.this.semester = null;
                    return;
                }
                ViewStaffProfileActivity.this.semester = adapterView.getItemAtPosition(i).toString();
                ViewStaffProfileActivity.this.subjectList.clear();
                FirebaseDatabase instance = FirebaseDatabase.getInstance();
                instance.getReference(ViewStaffProfileActivity.this.department + "/subjects/" + ViewStaffProfileActivity.this.semester).addValueEventListener(new ValueEventListener() {
                    public void onCancelled(@NonNull DatabaseError databaseError) {
                    }

                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        for (DataSnapshot value : dataSnapshot.getChildren()) {
                            ViewStaffProfileActivity.this.subjectList.add(((Subject) value.getValue(Subject.class)).getName());
                        }
                        ViewStaffProfileActivity.this.staff_subjects.setAdapter((SpinnerAdapter) new ArrayAdapter(ViewStaffProfileActivity.this, R.layout.spinner_dialog, ViewStaffProfileActivity.this.subjectList));
                    }
                });
            }
        });
        this.staff_subject_hours.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            public void onNothingSelected(AdapterView<?> adapterView) {
            }

            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long j) {
                if (adapterView.getItemAtPosition(i).equals("Hours")) {
                    ViewStaffProfileActivity.this.slots = null;
                    return;
                }
                ViewStaffProfileActivity.this.slots = adapterView.getItemAtPosition(i).toString();
            }
        });
        this.staff_subjects.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            public void onNothingSelected(AdapterView<?> adapterView) {
            }

            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long j) {
                if (adapterView.getItemAtPosition(i).equals("Subjects")) {
                    ViewStaffProfileActivity.this.sub = null;
                    return;
                }
                ViewStaffProfileActivity.this.sub = adapterView.getItemAtPosition(i).toString();
            }
        });
        this.subject_delete.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                LinearLayout linearLayout = new LinearLayout(ViewStaffProfileActivity.this);
                linearLayout.setOrientation(LinearLayout.VERTICAL);
                linearLayout.setGravity(17);
                View inflate = ((LayoutInflater) ViewStaffProfileActivity.this.getSystemService(LAYOUT_INFLATER_SERVICE)).inflate(R.layout.layout_list, (ViewGroup) null);
                final ListView listView = (ListView) inflate.findViewById(R.id.list);
                final PopupWindow popupWindow = new PopupWindow(inflate, -1, -1);
                popupWindow.showAtLocation(linearLayout, 17, 0, 0);
                final DatabaseReference child = FirebaseDatabase.getInstance().getReference("Staff").child(ViewStaffProfileActivity.this.department).child(ViewStaffProfileActivity.this.id);
                child.addListenerForSingleValueEvent(new ValueEventListener() {
                    public void onCancelled(@NonNull DatabaseError databaseError) {
                    }

                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        if (dataSnapshot.getChildrenCount() != 0) {
                            Staff staff = (Staff) dataSnapshot.getValue(Staff.class);
                            if (!staff.getSubjects().isEmpty()) {
                                ViewStaffProfileActivity.this.subList = staff.getSubjects();
                                listView.setAdapter(new ArrayAdapter(ViewStaffProfileActivity.this, R.layout.spinner_dialog, ViewStaffProfileActivity.this.subList));
                            }
                        }
                    }
                });
                ((AppCompatButton) inflate.findViewById(R.id.done)).setOnClickListener(new View.OnClickListener() {
                    public void onClick(View view) {
                        if (ViewStaffProfileActivity.this.subList.size() == 0) {
                            ViewStaffProfileActivity.this.subList.add("none");
                        }
                        HashMap hashMap = new HashMap();
                        hashMap.put("subjects", ViewStaffProfileActivity.this.subList);
                        child.updateChildren(hashMap).addOnSuccessListener(new OnSuccessListener<Void>() {
                            public void onSuccess(Void voidR) {
                                popupWindow.dismiss();
                                Toast.makeText(ViewStaffProfileActivity.this, "Subjects Deleted", Toast.LENGTH_SHORT).show();
                                ViewStaffProfileActivity.this.recreate();
                            }
                        });
                    }
                });
                ((AppCompatButton) inflate.findViewById(R.id.cancel)).setOnClickListener(new View.OnClickListener() {
                    public void onClick(View view) {
                        popupWindow.dismiss();
                    }
                });
                listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    public void onItemClick(AdapterView<?> adapterView, View view, int i, long j) {
                        ViewStaffProfileActivity.this.s = adapterView.getItemAtPosition(i).toString();
                        ViewStaffProfileActivity.this.subList.remove(ViewStaffProfileActivity.this.s);
                        listView.setAdapter(new ArrayAdapter(ViewStaffProfileActivity.this, R.layout.spinner_dialog, ViewStaffProfileActivity.this.subList));
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
