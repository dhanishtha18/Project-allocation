package org.projectapp;

import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.text.format.DateFormat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;
import androidx.appcompat.widget.AppCompatEditText;
import androidx.appcompat.widget.AppCompatImageButton;
import androidx.appcompat.widget.AppCompatTextView;
import androidx.lifecycle.Lifecycle;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.google.android.gms.common.Scopes;
import com.google.android.gms.common.internal.ImagesContract;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import org.apache.poi.openxml4j.opc.ContentTypes;
import org.apache.xmlbeans.impl.jam.xml.JamXmlElements;
import org.projectapp.APIs.APIService;
import org.projectapp.Adapter.DiscussionAdapter;
import org.projectapp.Model.Chat;
import org.projectapp.Model.Hod;
import org.projectapp.Model.Principal;
import org.projectapp.Model.Staff;
import org.projectapp.Model.Student;
import org.projectapp.Notifications.Client;
import org.projectapp.Notifications.Data;
import org.projectapp.Notifications.MyResponse;
import org.projectapp.Notifications.Sender;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import uk.co.senab.photoview.PhotoView;

public class DiscussionActivity extends AppCompatActivity {
    private static final int PICK_IMAGE_REQUEST = 234;
    APIService apiService;
    AppCompatImageButton btn_doc;
    AppCompatImageButton btn_send;
    AppCompatTextView cancel;
    View customview;
    String dbname;
    String department;
    Uri filePath;
    FirebaseUser fuser;
    AppCompatButton img;
    PhotoView img_view;
    Intent intent;
    LinearLayout layout;
    LayoutInflater layoutInflater;
    String loginas;
    List<Chat> mchat;
    DiscussionAdapter messageAdapter;
    String name;
    AppCompatButton pdf;
    PopupWindow popupWindow;
    String profile_url;
    RecyclerView recyclerView;
    DatabaseReference reference;
    String semester;
    AppCompatEditText text_send;
    String type;
    String uploadId;
    AppCompatTextView username;

    /* access modifiers changed from: protected */
    public void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        setContentView((int) R.layout.activity_message);
        this.recyclerView = (RecyclerView) findViewById(R.id.recycler_view);
        this.recyclerView.setHasFixedSize(true);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        linearLayoutManager.setStackFromEnd(true);
        this.recyclerView.setLayoutManager(linearLayoutManager);
        this.username = (AppCompatTextView) findViewById(R.id.title);
        this.btn_send = (AppCompatImageButton) findViewById(R.id.btn_send);
        this.text_send = (AppCompatEditText) findViewById(R.id.text_send);
        this.img_view = (PhotoView) findViewById(R.id.img_preview);
        this.btn_doc = (AppCompatImageButton) findViewById(R.id.btn_doc);
        this.apiService = (APIService) Client.getClient("https://fcm.googleapis.com/").create(APIService.class);
        this.layout = new LinearLayout(this);
        this.layout.setOrientation(LinearLayout.VERTICAL);
        this.layout.setGravity(17);
        this.layoutInflater = (LayoutInflater) getSystemService(LAYOUT_INFLATER_SERVICE);
        this.customview = this.layoutInflater.inflate(R.layout.img_doc_dialog, (ViewGroup) null);
        this.img = (AppCompatButton) this.customview.findViewById(R.id.img);
        this.pdf = (AppCompatButton) this.customview.findViewById(R.id.pdf);
        this.cancel = (AppCompatTextView) this.customview.findViewById(R.id.cancel);
        this.popupWindow = new PopupWindow(this.customview, -1, -1);
        this.recyclerView.setVisibility(View.VISIBLE);
        this.img_view.setVisibility(View.GONE);
        this.intent = getIntent();
        this.dbname = this.intent.getStringExtra("path");
        this.profile_url = this.intent.getStringExtra(Scopes.PROFILE);
        this.department = this.intent.getStringExtra("department");
        this.semester = this.intent.getStringExtra("semester");
        this.name = this.intent.getStringExtra("cname");
        this.fuser = FirebaseAuth.getInstance().getCurrentUser();
        this.username.setText(getIntent().getStringExtra("name"));
        this.btn_send.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                DiscussionActivity.this.recyclerView.setVisibility(View.VISIBLE);
                DiscussionActivity.this.img_view.setVisibility(View.GONE);
                String obj = DiscussionActivity.this.text_send.getText().toString();
                if (!obj.equals("")) {
                    DiscussionActivity discussionActivity = DiscussionActivity.this;
                    discussionActivity.sendMessage(discussionActivity.fuser.getUid(), obj);
                } else {
                    Toast.makeText(DiscussionActivity.this, "You Can't send empty message.", Toast.LENGTH_SHORT).show();
                }
                DiscussionActivity.this.text_send.setText("");
            }
        });
        this.btn_doc.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                DiscussionActivity.this.showOptions();
            }
        });
        readMessage(this.fuser.getUid());
    }

    /* access modifiers changed from: private */
    public void sendMessage(String str, final String str2) {
        final ProgressDialog progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Sending...");
        progressDialog.setCancelable(false);
        progressDialog.show();
        final DatabaseReference reference2 = FirebaseDatabase.getInstance().getReference(this.dbname);
        this.uploadId = reference2.push().getKey();
        if (this.filePath != null) {
            StorageReference reference3 = FirebaseStorage.getInstance().getReference(this.dbname);
            final StorageReference child = reference3.child(this.uploadId + ".pdf");
            final String str3 = str;
            final String str4 = str2;
            final ProgressDialog progressDialog2 = progressDialog;
            child.putFile(this.filePath).addOnSuccessListener((OnSuccessListener) new OnSuccessListener<UploadTask.TaskSnapshot>() {
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                    child.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                        public void onSuccess(Uri uri) {
                            HashMap hashMap = new HashMap();
                            hashMap.put("sender", str3);
                            hashMap.put("message", str4);
                            hashMap.put("time", DateFormat.format("dd-MM-yyyy (HH:mm:ss)", System.currentTimeMillis()).toString());
                            hashMap.put(ImagesContract.URL, uri.toString());
                            hashMap.put(JamXmlElements.TYPE, DiscussionActivity.this.type);
                            hashMap.put(Scopes.PROFILE, DiscussionActivity.this.profile_url);
                            hashMap.put("receiver", DiscussionActivity.this.name);
                            reference2.child(DiscussionActivity.this.uploadId).setValue(hashMap);
                            DiscussionActivity.this.filePath = null;
                            progressDialog2.dismiss();
                        }
                    });
                }
            }).addOnFailureListener((OnFailureListener) new OnFailureListener() {
                public void onFailure(Exception exc) {
                    progressDialog.dismiss();
                    Toast.makeText(DiscussionActivity.this, exc.getMessage(), Toast.LENGTH_SHORT).show();
                }
            });
        } else {
            HashMap hashMap = new HashMap();
            hashMap.put("sender", str);
            hashMap.put("message", str2);
            hashMap.put("time", DateFormat.format("dd-MM-yyyy (HH:mm:ss)", System.currentTimeMillis()).toString());
            hashMap.put(ImagesContract.URL, (Object) null);
            hashMap.put(JamXmlElements.TYPE, (Object) null);
            hashMap.put(Scopes.PROFILE, this.profile_url);
            hashMap.put("receiver", this.name);
            reference2.child(this.uploadId).setValue(hashMap);
            progressDialog.dismiss();
        }
        if (this.department.isEmpty()) {
            FirebaseDatabase.getInstance().getReference("Staff").addListenerForSingleValueEvent(new ValueEventListener() {
                public void onCancelled(@NonNull DatabaseError databaseError) {
                }

                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    for (DataSnapshot children : dataSnapshot.getChildren()) {
                        for (DataSnapshot next : children.getChildren()) {
                            if (next.getChildrenCount() != 0) {
                                Staff staff = (Staff) next.getValue(Staff.class);
                                if (!staff.getId().equals(FirebaseAuth.getInstance().getCurrentUser().getUid())) {
                                    DiscussionActivity.this.sendNotification(staff.getToken(), staff.getId(), DiscussionActivity.this.name, str2);
                                }
                            }
                        }
                    }
                }
            });
            FirebaseDatabase.getInstance().getReference("HODs").addListenerForSingleValueEvent(new ValueEventListener() {
                public void onCancelled(@NonNull DatabaseError databaseError) {
                }

                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    for (DataSnapshot children : dataSnapshot.getChildren()) {
                        for (DataSnapshot next : children.getChildren()) {
                            if (next.getChildrenCount() != 0) {
                                Hod hod = (Hod) next.getValue(Hod.class);
                                if (!hod.getId().equals(FirebaseAuth.getInstance().getCurrentUser().getUid())) {
                                    DiscussionActivity.this.sendNotification(hod.getToken(), hod.getId(), DiscussionActivity.this.name, str2);
                                }
                            }
                        }
                    }
                }
            });
            FirebaseDatabase.getInstance().getReference("Principal").addListenerForSingleValueEvent(new ValueEventListener() {
                public void onCancelled(@NonNull DatabaseError databaseError) {
                }

                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    for (DataSnapshot next : dataSnapshot.getChildren()) {
                        if (next.getChildrenCount() != 0) {
                            Principal principal = (Principal) next.getValue(Principal.class);
                            if (!principal.getId().equals(FirebaseAuth.getInstance().getCurrentUser().getUid())) {
                                DiscussionActivity.this.sendNotification(principal.getToken(), principal.getId(), DiscussionActivity.this.name, str2);
                            }
                        }
                    }
                }
            });
            return;
        }
        FirebaseDatabase.getInstance().getReference("Staff").child(this.department).addListenerForSingleValueEvent(new ValueEventListener() {
            public void onCancelled(@NonNull DatabaseError databaseError) {
            }

            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot next : dataSnapshot.getChildren()) {
                    if (next.getChildrenCount() != 0) {
                        Staff staff = (Staff) next.getValue(Staff.class);
                        if (staff.getId() != null && !staff.getId().equals(FirebaseAuth.getInstance().getCurrentUser().getUid())) {
                            DiscussionActivity.this.sendNotification(staff.getToken(), staff.getId(), DiscussionActivity.this.name, str2);
                        }
                    }
                }
            }
        });
        FirebaseDatabase.getInstance().getReference("HODs").child(this.department).addListenerForSingleValueEvent(new ValueEventListener() {
            public void onCancelled(@NonNull DatabaseError databaseError) {
            }

            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot next : dataSnapshot.getChildren()) {
                    if (next.getChildrenCount() != 0) {
                        Hod hod = (Hod) next.getValue(Hod.class);
                        if (!hod.getId().equals(FirebaseAuth.getInstance().getCurrentUser().getUid())) {
                            DiscussionActivity.this.sendNotification(hod.getToken(), hod.getId(), DiscussionActivity.this.name, str2);
                        }
                    }
                }
            }
        });
        FirebaseDatabase.getInstance().getReference("Students").child(this.department).child(this.semester).addListenerForSingleValueEvent(new ValueEventListener() {
            public void onCancelled(@NonNull DatabaseError databaseError) {
            }

            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot next : dataSnapshot.getChildren()) {
                    if (next.getChildrenCount() != 0) {
                        Student student = (Student) next.getValue(Student.class);
                        if (!student.getId().equals(FirebaseAuth.getInstance().getCurrentUser().getUid())) {
                            DiscussionActivity.this.sendNotification(student.getToken(), student.getId(), DiscussionActivity.this.name, str2);
                        }
                    }
                }
            }
        });
    }

    /* access modifiers changed from: private */
    public void sendNotification(String str, String str2, String str3, String str4) {
        this.apiService.sendNotification(new Sender(new Data(FirebaseAuth.getInstance().getCurrentUser().getUid(), str3 + ":" + str4, "New " + getIntent().getStringExtra("noti"), str2), str)).enqueue(new Callback<MyResponse>() {
            public void onFailure(Call<MyResponse> call, Throwable th) {
            }

            public void onResponse(Call<MyResponse> call, Response<MyResponse> response) {
                if (response.code() == 200) {
                    int i = response.body().success;
                }
            }
        });
    }

    private void readMessage(String str) {
        this.mchat = new ArrayList();
        this.reference = FirebaseDatabase.getInstance().getReference(this.dbname);
        this.reference.addValueEventListener(new ValueEventListener() {
            public void onCancelled(@NonNull DatabaseError databaseError) {
            }

            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                DiscussionActivity.this.mchat.clear();
                for (DataSnapshot value : dataSnapshot.getChildren()) {
                    DiscussionActivity.this.mchat.add((Chat) value.getValue(Chat.class));
                }
                if (DiscussionActivity.this.getLifecycle().getCurrentState().isAtLeast(Lifecycle.State.INITIALIZED)) {
                    DiscussionActivity discussionActivity = DiscussionActivity.this;
                    discussionActivity.messageAdapter = new DiscussionAdapter(discussionActivity, discussionActivity.mchat);
                    DiscussionActivity.this.recyclerView.setAdapter(DiscussionActivity.this.messageAdapter);
                }
            }
        });
    }

    /* access modifiers changed from: private */
    public void showOptions() {
        this.popupWindow.showAtLocation(this.layout, 17, 0, 0);
        this.customview.findViewById(R.id.dialog).setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                DiscussionActivity.this.popupWindow.dismiss();
            }
        });
        this.pdf.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                DiscussionActivity discussionActivity = DiscussionActivity.this;
                discussionActivity.type = "pdf";
                discussionActivity.showPdfChooser();
                DiscussionActivity.this.popupWindow.dismiss();
            }
        });
        this.img.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                DiscussionActivity discussionActivity = DiscussionActivity.this;
                discussionActivity.type = ContentTypes.EXTENSION_JPG_1;
                discussionActivity.showImageChooser();
                DiscussionActivity.this.popupWindow.dismiss();
            }
        });
        this.cancel.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                DiscussionActivity.this.popupWindow.dismiss();
            }
        });
    }

    /* access modifiers changed from: private */
    public void showImageChooser() {
        Intent intent2 = new Intent();
        intent2.setType("image/*");
        intent2.setAction("android.intent.action.GET_CONTENT");
        startActivityForResult(Intent.createChooser(intent2, "Select Picture"), PICK_IMAGE_REQUEST);
    }

    /* access modifiers changed from: private */
    public void showPdfChooser() {
        Intent intent2 = new Intent();
        intent2.setType("application/pdf");
        intent2.setAction("android.intent.action.GET_CONTENT");
        startActivityForResult(Intent.createChooser(intent2, "Select Document"), PICK_IMAGE_REQUEST);
    }

    /* access modifiers changed from: protected */
    public void onActivityResult(int i, int i2, Intent intent2) {
        super.onActivityResult(i, i2, intent2);
        if (i == PICK_IMAGE_REQUEST && i2 == -1 && intent2 != null && intent2.getData() != null) {
            this.filePath = intent2.getData();
            if (this.type.equals(ContentTypes.EXTENSION_JPG_1)) {
                this.recyclerView.setVisibility(View.GONE);
                this.img_view.setVisibility(View.VISIBLE);
                try {
                    this.img_view.setImageBitmap(MediaStore.Images.Media.getBitmap(getContentResolver(), this.filePath));
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            if (this.type.equals("pdf")) {
                this.recyclerView.setVisibility(View.GONE);
                this.img_view.setVisibility(View.VISIBLE);
                this.img_view.setImageResource(R.drawable.preview);
            }
        }
    }

    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }
}
