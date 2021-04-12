package org.projectapp;

import android.app.AlertDialog.Builder;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Bitmap.CompressFormat;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore.Images.Media;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;
import androidx.appcompat.widget.AppCompatEditText;
import androidx.appcompat.widget.AppCompatTextView;
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
import org.projectapp.Model.Principal;

public class ViewPrincipalProfileActivity extends AppCompatActivity {
    Bitmap bitmap = null;
    String department;
    ProgressDialog dialog;
    String id;
    Uri imagepath;
    Uri img;
    String loginas;
    Principal principal = null;
    String semester;
    StringBuilder subject;
    AppCompatTextView title;
    AppCompatButton user_edit;
    AppCompatEditText user_email;
    AppCompatEditText user_experience;
    AppCompatEditText user_login_id;
    AppCompatEditText user_name;
    AppCompatEditText user_phoneno;
    CircleImageView user_profile;
    AppCompatEditText user_qualification;

    /* access modifiers changed from: protected */
    public void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        setContentView((int) R.layout.activity_view_principal_profile);
        this.user_name = (AppCompatEditText) findViewById(R.id.user_name);
        this.user_phoneno = (AppCompatEditText) findViewById(R.id.user_phoneno);
        this.user_email = (AppCompatEditText) findViewById(R.id.user_email);
        this.user_edit = (AppCompatButton) findViewById(R.id.user_edit);
        this.user_profile = (CircleImageView) findViewById(R.id.user_profile);
        this.user_qualification = (AppCompatEditText) findViewById(R.id.user_qualification);
        this.user_experience = (AppCompatEditText) findViewById(R.id.user_experience);
        this.user_login_id = (AppCompatEditText) findViewById(R.id.user_login_id);
        this.title = (AppCompatTextView) findViewById(R.id.title);
        this.user_profile.setEnabled(false);
        this.subject = new StringBuilder();
        this.subject.append("Subjects:");
        this.id = getIntent().getStringExtra("id");
        this.loginas = getIntent().getStringExtra("loginas");
        if (this.loginas.equals(Scopes.PROFILE)) {
            this.user_edit.setVisibility(View.VISIBLE);
        } else {
            this.user_edit.setVisibility(View.GONE);
        }
        this.title.setText("Profile");
        FirebaseDatabase instance = FirebaseDatabase.getInstance();
        StringBuilder sb = new StringBuilder();
        sb.append("Principal/");
        sb.append(this.id);
        instance.getReference(sb.toString()).addValueEventListener(new ValueEventListener() {
            public void onCancelled(@NonNull DatabaseError databaseError) {
            }

            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                ViewPrincipalProfileActivity.this.principal = (Principal) dataSnapshot.getValue(Principal.class);
                if (ViewPrincipalProfileActivity.this.principal.getProfile_url().equals("default")) {
                    ViewPrincipalProfileActivity.this.user_profile.setImageResource(R.drawable.profile);
                } else if (ViewPrincipalProfileActivity.this.getLifecycle().getCurrentState().isAtLeast(State.INITIALIZED)) {
                    Glide.with(ViewPrincipalProfileActivity.this.getApplicationContext()).load(ViewPrincipalProfileActivity.this.principal.getProfile_url()).into((ImageView) ViewPrincipalProfileActivity.this.user_profile);
                }
                if (!ViewPrincipalProfileActivity.this.loginas.equals(Scopes.PROFILE)) {
                    if (ViewPrincipalProfileActivity.this.principal.getName().isEmpty()) {
                        ViewPrincipalProfileActivity.this.user_name.setVisibility(View.GONE);
                    }
                    if (ViewPrincipalProfileActivity.this.principal.getPhone().isEmpty()) {
                        ViewPrincipalProfileActivity.this.user_phoneno.setVisibility(View.GONE);
                    }
                    if (ViewPrincipalProfileActivity.this.principal.getEmail().isEmpty()) {
                        ViewPrincipalProfileActivity.this.user_email.setVisibility(View.GONE);
                    }
                    if (ViewPrincipalProfileActivity.this.principal.getQualifiactions() == null) {
                        ViewPrincipalProfileActivity.this.user_qualification.setVisibility(View.GONE);
                    }
                    if (ViewPrincipalProfileActivity.this.principal.getExpirence() == null) {
                        ViewPrincipalProfileActivity.this.user_experience.setVisibility(View.GONE);
                    }
                    if (ViewPrincipalProfileActivity.this.principal.getLogin_id().isEmpty()) {
                        ViewPrincipalProfileActivity.this.user_login_id.setVisibility(View.GONE);
                    }
                }
                ViewPrincipalProfileActivity.this.user_name.setText(ViewPrincipalProfileActivity.this.principal.getName());
                ViewPrincipalProfileActivity.this.user_phoneno.setText(ViewPrincipalProfileActivity.this.principal.getPhone());
                ViewPrincipalProfileActivity.this.user_email.setText(ViewPrincipalProfileActivity.this.principal.getEmail());
                ViewPrincipalProfileActivity.this.user_qualification.setText(ViewPrincipalProfileActivity.this.principal.getQualifiactions());
                ViewPrincipalProfileActivity.this.user_experience.setText(ViewPrincipalProfileActivity.this.principal.getExpirence());
                ViewPrincipalProfileActivity.this.user_login_id.setText(ViewPrincipalProfileActivity.this.principal.getLogin_id());
            }
        });
        this.user_edit.setOnClickListener(new OnClickListener() {
            public void onClick(View view) {
                if (ViewPrincipalProfileActivity.this.user_name.isEnabled()) {
                    ViewPrincipalProfileActivity viewPrincipalProfileActivity = ViewPrincipalProfileActivity.this;
                    viewPrincipalProfileActivity.dialog = new ProgressDialog(viewPrincipalProfileActivity);
                    ViewPrincipalProfileActivity.this.dialog.setMessage("Updating...");
                    ViewPrincipalProfileActivity.this.dialog.setCancelable(false);
                    ViewPrincipalProfileActivity.this.dialog.show();
                    ViewPrincipalProfileActivity.this.user_edit.setText("Edit");
                    ViewPrincipalProfileActivity.this.user_name.setEnabled(false);
                    ViewPrincipalProfileActivity.this.user_phoneno.setEnabled(false);
                    ViewPrincipalProfileActivity.this.user_qualification.setEnabled(false);
                    ViewPrincipalProfileActivity.this.user_experience.setEnabled(false);
                    ViewPrincipalProfileActivity.this.user_profile.setEnabled(false);
                    String str = "";
                    String str2 = "Ok";
                    String str3 = "Alert";
                    if (ViewPrincipalProfileActivity.this.user_name.getText().toString().equals(str)) {
                        Builder builder = new Builder(ViewPrincipalProfileActivity.this);
                        builder.setMessage("Enter Your Name.");
                        builder.setTitle(str3);
                        builder.setPositiveButton(str2, new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialogInterface, int i) {
                            }
                        });
                        builder.show();
                        ViewPrincipalProfileActivity.this.dialog.dismiss();
                    } else if (ViewPrincipalProfileActivity.this.user_experience.getText().toString().equals(str)) {
                        Builder builder2 = new Builder(ViewPrincipalProfileActivity.this);
                        builder2.setMessage("Enter Your Experience.");
                        builder2.setTitle(str3);
                        builder2.setPositiveButton(str2, new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialogInterface, int i) {
                            }
                        });
                        builder2.show();
                        ViewPrincipalProfileActivity.this.dialog.dismiss();
                    } else if (ViewPrincipalProfileActivity.this.user_qualification.getText().toString().equals(str)) {
                        Builder builder3 = new Builder(ViewPrincipalProfileActivity.this);
                        builder3.setMessage("Enter Your Qualifications.");
                        builder3.setTitle(str3);
                        builder3.setPositiveButton(str2, new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialogInterface, int i) {
                            }
                        });
                        builder3.show();
                        ViewPrincipalProfileActivity.this.dialog.dismiss();
                    } else {
                        boolean equals = ViewPrincipalProfileActivity.this.user_phoneno.getText().toString().equals(str);
                        String str4 = "Enter Your Phone No.";
                        if (equals) {
                            Builder builder4 = new Builder(ViewPrincipalProfileActivity.this);
                            builder4.setMessage(str4);
                            builder4.setTitle(str3);
                            builder4.setPositiveButton(str2, new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialogInterface, int i) {
                                }
                            });
                            builder4.show();
                            ViewPrincipalProfileActivity.this.dialog.dismiss();
                        } else if (ViewPrincipalProfileActivity.this.user_phoneno.getText().toString().trim().length() != 10) {
                            Builder builder5 = new Builder(ViewPrincipalProfileActivity.this);
                            builder5.setMessage(str4);
                            builder5.setTitle(str3);
                            builder5.setPositiveButton(str2, new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialogInterface, int i) {
                                }
                            });
                            builder5.show();
                            ViewPrincipalProfileActivity.this.dialog.dismiss();
                        } else if (ViewPrincipalProfileActivity.this.bitmap == null) {
                            FirebaseDatabase instance = FirebaseDatabase.getInstance();
                            StringBuilder sb = new StringBuilder();
                            sb.append("Principal/");
                            sb.append(ViewPrincipalProfileActivity.this.id);
                            DatabaseReference reference = instance.getReference(sb.toString());
                            Principal principal = new Principal(ViewPrincipalProfileActivity.this.id, ViewPrincipalProfileActivity.this.user_name.getText().toString().trim(), ViewPrincipalProfileActivity.this.principal.getLogin_id(), ViewPrincipalProfileActivity.this.principal.getPassword(), ViewPrincipalProfileActivity.this.user_email.getText().toString().trim(), ViewPrincipalProfileActivity.this.user_phoneno.getText().toString().trim(), ViewPrincipalProfileActivity.this.user_qualification.getText().toString().trim(), ViewPrincipalProfileActivity.this.user_experience.getText().toString().trim(), ViewPrincipalProfileActivity.this.principal.getProfile_url(), ViewPrincipalProfileActivity.this.principal.getToken());
                            reference.setValue(principal).addOnSuccessListener(new OnSuccessListener<Void>() {
                                public void onSuccess(Void voidR) {
                                    String str = "id";
                                    String str2 = "login_id";
                                    String str3 = "phone";
                                    String str4 = "email";
                                    String str5 = "password";
                                    ViewPrincipalProfileActivity.this.getSharedPreferences("rait", 0).edit().clear().putString("name", ViewPrincipalProfileActivity.this.user_name.getText().toString().trim()).putString(str, ViewPrincipalProfileActivity.this.principal.getId()).putString(str2, ViewPrincipalProfileActivity.this.principal.getLogin_id()).putString(str3, ViewPrincipalProfileActivity.this.user_phoneno.getText().toString().trim()).putString(str4, ViewPrincipalProfileActivity.this.principal.getEmail()).putString(str5, ViewPrincipalProfileActivity.this.principal.getPassword()).putString(Scopes.PROFILE, ViewPrincipalProfileActivity.this.principal.getProfile_url()).putString("loginas", "principal").commit();
                                    ViewPrincipalProfileActivity.this.dialog.dismiss();
                                }
                            }).addOnFailureListener(new OnFailureListener() {
                                public void onFailure(@NonNull Exception exc) {
                                    ViewPrincipalProfileActivity.this.dialog.dismiss();
                                }
                            });
                        } else {
                            ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
                            ViewPrincipalProfileActivity.this.bitmap.compress(CompressFormat.JPEG, 70, byteArrayOutputStream);
                            byte[] byteArray = byteArrayOutputStream.toByteArray();
                            StorageReference reference2 = FirebaseStorage.getInstance().getReference();
                            StringBuilder sb2 = new StringBuilder();
                            sb2.append("gpt/profile_pictures/");
                            sb2.append(ViewPrincipalProfileActivity.this.id);
                            reference2.child(sb2.toString()).putBytes(byteArray).addOnSuccessListener((OnSuccessListener) new OnSuccessListener<TaskSnapshot>() {
                                public void onSuccess(TaskSnapshot taskSnapshot) {
                                    taskSnapshot.getStorage().getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                                        public void onSuccess(final Uri uri) {
                                            FirebaseDatabase instance = FirebaseDatabase.getInstance();
                                            StringBuilder sb = new StringBuilder();
                                            sb.append("Principal/");
                                            sb.append(ViewPrincipalProfileActivity.this.id);
                                            DatabaseReference reference = instance.getReference(sb.toString());
                                            Principal principal = new Principal(ViewPrincipalProfileActivity.this.id, ViewPrincipalProfileActivity.this.user_name.getText().toString().trim(), ViewPrincipalProfileActivity.this.principal.getLogin_id(), ViewPrincipalProfileActivity.this.principal.getPassword(), ViewPrincipalProfileActivity.this.user_email.getText().toString().trim(), ViewPrincipalProfileActivity.this.user_phoneno.getText().toString().trim(), ViewPrincipalProfileActivity.this.user_qualification.getText().toString().trim(), ViewPrincipalProfileActivity.this.user_experience.getText().toString().trim(), uri.toString(), ViewPrincipalProfileActivity.this.principal.getToken());
                                            reference.setValue(principal).addOnSuccessListener(new OnSuccessListener<Void>() {
                                                public void onSuccess(Void voidR) {
                                                    String str = "id";
                                                    String str2 = "login_id";
                                                    String str3 = "phone";
                                                    String str4 = "email";
                                                    String str5 = "password";
                                                    ViewPrincipalProfileActivity.this.getSharedPreferences("rait", 0).edit().clear().putString("name", ViewPrincipalProfileActivity.this.user_name.getText().toString().trim()).putString(str, ViewPrincipalProfileActivity.this.principal.getId()).putString(str2, ViewPrincipalProfileActivity.this.principal.getLogin_id()).putString(str3, ViewPrincipalProfileActivity.this.user_phoneno.getText().toString().trim()).putString(str4, ViewPrincipalProfileActivity.this.principal.getEmail()).putString(str5, ViewPrincipalProfileActivity.this.principal.getPassword()).putString(Scopes.PROFILE, uri.toString()).putString("loginas", "principal").commit();
                                                    ViewPrincipalProfileActivity.this.dialog.dismiss();
                                                }
                                            }).addOnFailureListener(new OnFailureListener() {
                                                public void onFailure(@NonNull Exception exc) {
                                                    ViewPrincipalProfileActivity.this.dialog.dismiss();
                                                }
                                            });
                                        }
                                    }).addOnFailureListener(new OnFailureListener() {
                                        public void onFailure(@NonNull Exception exc) {
                                            ViewPrincipalProfileActivity.this.dialog.dismiss();
                                        }
                                    });
                                }
                            });
                        }
                    }
                } else {
                    ViewPrincipalProfileActivity.this.user_edit.setText("Update");
                    ViewPrincipalProfileActivity.this.user_name.setEnabled(true);
                    ViewPrincipalProfileActivity.this.user_phoneno.setEnabled(true);
                    ViewPrincipalProfileActivity.this.user_qualification.setEnabled(true);
                    ViewPrincipalProfileActivity.this.user_experience.setEnabled(true);
                    ViewPrincipalProfileActivity.this.user_profile.setEnabled(true);
                }
            }
        });
        this.user_profile.setOnClickListener(new OnClickListener() {
            public void onClick(View view) {
                CropImage.startPickImageActivity(ViewPrincipalProfileActivity.this);
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
