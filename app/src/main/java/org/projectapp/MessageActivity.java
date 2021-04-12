package org.projectapp;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.text.format.DateFormat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;
import androidx.appcompat.widget.AppCompatEditText;
import androidx.appcompat.widget.AppCompatImageButton;
import androidx.appcompat.widget.AppCompatTextView;
import androidx.core.app.NotificationCompat;
import androidx.fragment.app.FragmentActivity;
import androidx.lifecycle.Lifecycle;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.bumptech.glide.Glide;
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
import org.projectapp.Adapter.MessageAdapter;
import org.projectapp.Model.Chat;
import org.projectapp.Model.Hod;
import org.projectapp.Model.Principal;
import org.projectapp.Model.Staff;
import org.projectapp.Notifications.Client;
import org.projectapp.Notifications.Data;
import org.projectapp.Notifications.MyResponse;
import org.projectapp.Notifications.Sender;

import de.hdodenhof.circleimageview.CircleImageView;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import uk.co.senab.photoview.PhotoView;

public class MessageActivity extends AppCompatActivity {
    private static final int PICK_IMAGE_REQUEST = 234;
    APIService apiService;
    AppCompatImageButton btn_doc;
    AppCompatImageButton btn_send;
    AppCompatTextView cancel;
    String currentname;
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
    MessageAdapter messageAdapter;
    String name = "abc";
    AppCompatButton pdf;
    PopupWindow popupWindow;
    CircleImageView profile_image;
    RecyclerView recyclerView;
    DatabaseReference reference;
    ValueEventListener seenListener;
    AppCompatEditText text_send;
    String type;
    String uploadId;
    String url;
    String userid;
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
        this.apiService = (APIService) Client.getClient("https://fcm.googleapis.com/").create(APIService.class);
        this.profile_image = (CircleImageView) findViewById(R.id.icon);
        this.username = (AppCompatTextView) findViewById(R.id.title);
        this.btn_send = (AppCompatImageButton) findViewById(R.id.btn_send);
        this.text_send = (AppCompatEditText) findViewById(R.id.text_send);
        this.img_view = (PhotoView) findViewById(R.id.img_preview);
        this.btn_doc = (AppCompatImageButton) findViewById(R.id.btn_doc);
        this.layout = new LinearLayout(this);
        this.layout.setOrientation(LinearLayout.VERTICAL);
        this.layout.setGravity(17);
        this.layoutInflater = (LayoutInflater) getSystemService(LAYOUT_INFLATER_SERVICE);
        this.customview = this.layoutInflater.inflate(R.layout.img_doc_dialog, (ViewGroup) null);
        this.img = (AppCompatButton) this.customview.findViewById(R.id.img);
        this.pdf = (AppCompatButton) this.customview.findViewById(R.id.pdf);
        this.cancel = (AppCompatTextView) this.customview.findViewById(R.id.cancel);
        this.popupWindow = new PopupWindow(this.customview, -1, -1);
        this.apiService = (APIService) Client.getClient("https://fcm.googleapis.com/").create(APIService.class);
        this.recyclerView.setVisibility(View.VISIBLE);
        this.img_view.setVisibility(View.GONE);
        this.intent = getIntent();
        this.userid = this.intent.getStringExtra("userid");
        this.department = this.intent.getStringExtra("department");
        this.dbname = this.intent.getStringExtra("PrincipalHodChatlist");
        this.loginas = this.intent.getStringExtra("loginas");
        this.name = this.intent.getStringExtra("name");
        this.url = this.intent.getStringExtra(ImagesContract.URL);
        this.fuser = FirebaseAuth.getInstance().getCurrentUser();
        this.btn_send.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                MessageActivity.this.recyclerView.setVisibility(View.VISIBLE);
                MessageActivity.this.img_view.setVisibility(View.GONE);
                String obj = MessageActivity.this.text_send.getText().toString();
                if (!obj.equals("")) {
                    MessageActivity messageActivity = MessageActivity.this;
                    messageActivity.sendMessage(messageActivity.fuser.getUid(), MessageActivity.this.userid, obj);
                } else {
                    Toast.makeText(MessageActivity.this, "You Can't send empty message.", Toast.LENGTH_SHORT).show();
                }
                MessageActivity.this.text_send.setText("");
            }
        });
        this.btn_doc.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                MessageActivity.this.showOptions();
            }
        });
        this.username.setText(this.name);
        if (this.url.equals("default")) {
            this.profile_image.setImageResource(R.drawable.profile);
        } else if (getLifecycle().getCurrentState().isAtLeast(Lifecycle.State.INITIALIZED)) {
            Glide.with((FragmentActivity) this).load(this.url).into((ImageView) this.profile_image);
        }
        if (this.loginas.equals("hod")) {
            FirebaseDatabase.getInstance().getReference("HODs").child(this.department).child(FirebaseAuth.getInstance().getCurrentUser().getUid()).addListenerForSingleValueEvent(new ValueEventListener() {
                public void onCancelled(@NonNull DatabaseError databaseError) {
                }

                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    MessageActivity.this.currentname = ((Hod) dataSnapshot.getValue(Hod.class)).getName();
                }
            });
        } else if (this.loginas.equals("lecturer")) {
            FirebaseDatabase.getInstance().getReference("Staff").child(this.department).child(FirebaseAuth.getInstance().getCurrentUser().getUid()).addListenerForSingleValueEvent(new ValueEventListener() {
                public void onCancelled(@NonNull DatabaseError databaseError) {
                }

                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    MessageActivity.this.currentname = ((Staff) dataSnapshot.getValue(Staff.class)).getName();
                }
            });
        } else if (this.loginas.equals("principal")) {
            FirebaseDatabase.getInstance().getReference("Principal").child(FirebaseAuth.getInstance().getCurrentUser().getUid()).addListenerForSingleValueEvent(new ValueEventListener() {
                public void onCancelled(@NonNull DatabaseError databaseError) {
                }

                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    MessageActivity.this.currentname = ((Principal) dataSnapshot.getValue(Principal.class)).getName();
                }
            });
        }
        readMessage(this.fuser.getUid(), this.userid, this.url);
        seenMessage(this.userid);
    }

    private void seenMessage(final String str) {
        this.reference = FirebaseDatabase.getInstance().getReference("Chats");
        this.seenListener = this.reference.addValueEventListener(new ValueEventListener() {
            public void onCancelled(@NonNull DatabaseError databaseError) {
            }

            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot next : dataSnapshot.getChildren()) {
                    Chat chat = (Chat) next.getValue(Chat.class);
                    if (chat.getReceiver().equals(MessageActivity.this.fuser.getUid()) && chat.getSender().equals(str)) {
                        HashMap hashMap = new HashMap();
                        hashMap.put("isseen", true);
                        next.getRef().updateChildren(hashMap);
                    }
                }
            }
        });
    }

    /* access modifiers changed from: private */
    public void sendMessage(String str, String str2, final String str3) {
        final ProgressDialog progressDialog = new ProgressDialog(this);
        progressDialog.setCancelable(false);
        progressDialog.setMessage("Sending...");
        progressDialog.show();
        final DatabaseReference reference2 = FirebaseDatabase.getInstance().getReference("Chats");
        this.uploadId = reference2.push().getKey();
        if (this.filePath != null) {
            StorageReference reference3 = FirebaseStorage.getInstance().getReference("Chats");
            final StorageReference child = reference3.child(this.uploadId + ".pdf");
            final String str4 = str;
            final String str5 = str2;
            final String str6 = str3;
            final ProgressDialog progressDialog2 = progressDialog;
            child.putFile(this.filePath).addOnSuccessListener((OnSuccessListener) new OnSuccessListener<UploadTask.TaskSnapshot>() {
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                    child.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                        public void onSuccess(Uri uri) {
                            HashMap hashMap = new HashMap();
                            hashMap.put("sender", str4);
                            hashMap.put("receiver", str5);
                            hashMap.put("message", str6);
                            hashMap.put("isseen", false);
                            hashMap.put("time", DateFormat.format("dd-MM-yyyy (HH:mm:ss)", System.currentTimeMillis()).toString());
                            hashMap.put(ImagesContract.URL, uri.toString());
                            hashMap.put(JamXmlElements.TYPE, MessageActivity.this.type);
                            reference2.child(MessageActivity.this.uploadId).setValue(hashMap);
                            MessageActivity.this.filePath = null;
                            final DatabaseReference child = FirebaseDatabase.getInstance().getReference(MessageActivity.this.dbname).child(MessageActivity.this.fuser.getUid()).child(MessageActivity.this.userid);
                            child.addValueEventListener(new ValueEventListener() {
                                public void onCancelled(@NonNull DatabaseError databaseError) {
                                }

                                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                    if (!dataSnapshot.exists()) {
                                        child.child("id").setValue(MessageActivity.this.userid);
                                    }
                                }
                            });
                        }
                    });
                    progressDialog2.dismiss();
                }
            }).addOnFailureListener((OnFailureListener) new OnFailureListener() {
                public void onFailure(Exception exc) {
                    progressDialog.dismiss();
                    Toast.makeText(MessageActivity.this, exc.getMessage(), Toast.LENGTH_SHORT).show();
                }
            });
        } else {
            HashMap hashMap = new HashMap();
            hashMap.put("sender", str);
            hashMap.put("receiver", str2);
            hashMap.put("message", str3);
            hashMap.put("isseen", false);
            hashMap.put("time", DateFormat.format("dd-MM-yyyy (HH:mm:ss)", System.currentTimeMillis()).toString());
            hashMap.put(ImagesContract.URL, (Object) null);
            hashMap.put(JamXmlElements.TYPE, (Object) null);
            reference2.child(this.uploadId).setValue(hashMap);
            final DatabaseReference child2 = FirebaseDatabase.getInstance().getReference(this.dbname).child(this.fuser.getUid()).child(this.userid);
            child2.addValueEventListener(new ValueEventListener() {
                public void onCancelled(@NonNull DatabaseError databaseError) {
                }

                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    if (!dataSnapshot.exists()) {
                        child2.child("id").setValue(MessageActivity.this.userid);
                    }
                }
            });
            progressDialog.dismiss();
        }
        if (this.dbname.equals("HodChatlist")) {
            FirebaseDatabase.getInstance().getReference("Staff").child(this.department).child(this.userid).addListenerForSingleValueEvent(new ValueEventListener() {
                public void onCancelled(@NonNull DatabaseError databaseError) {
                }

                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    if (dataSnapshot.getChildrenCount() != 0) {
                        Staff staff = (Staff) dataSnapshot.getValue(Staff.class);
                        MessageActivity.this.sendNotification(staff.getToken(), staff.getId(), MessageActivity.this.currentname, str3);
                    }
                }
            });
        }
        FirebaseDatabase.getInstance().getReference("HODs").child(this.department).child(this.userid).addListenerForSingleValueEvent(new ValueEventListener() {
            public void onCancelled(@NonNull DatabaseError databaseError) {
            }

            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.getChildrenCount() != 0) {
                    Hod hod = (Hod) dataSnapshot.getValue(Hod.class);
                    if (!hod.getId().equals(FirebaseAuth.getInstance().getCurrentUser().getUid())) {
                        MessageActivity.this.sendNotification(hod.getToken(), hod.getId(), MessageActivity.this.currentname, str3);
                    }
                }
            }
        });
        if (this.dbname.equals("PrincipalHodChatlist")) {
            FirebaseDatabase.getInstance().getReference("Principal").child(this.userid).addListenerForSingleValueEvent(new ValueEventListener() {
                public void onCancelled(@NonNull DatabaseError databaseError) {
                }

                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    if (dataSnapshot.getChildrenCount() != 0) {
                        Principal principal = (Principal) dataSnapshot.getValue(Principal.class);
                        if (!principal.getId().equals(FirebaseAuth.getInstance().getCurrentUser().getUid())) {
                            MessageActivity.this.sendNotification(principal.getToken(), principal.getId(), MessageActivity.this.currentname, str3);
                        }
                    }
                }
            });
        }
    }

    /* access modifiers changed from: private */
    public void sendNotification(String str, String str2, String str3, String str4) {
        String uid = FirebaseAuth.getInstance().getCurrentUser().getUid();
        this.apiService.sendNotification(new Sender(new Data(uid, str3 + ":" + str4, "New Chat Message", str2), str)).enqueue(new Callback<MyResponse>() {
            public void onFailure(Call<MyResponse> call, Throwable th) {
            }

            public void onResponse(Call<MyResponse> call, Response<MyResponse> response) {
                if (response.code() == 200) {
                    int i = response.body().success;
                }
            }
        });
    }

    private void readMessage(final String str, final String str2, final String str3) {
        this.mchat = new ArrayList();
        this.reference = FirebaseDatabase.getInstance().getReference("Chats");
        this.reference.addValueEventListener(new ValueEventListener() {
            public void onCancelled(@NonNull DatabaseError databaseError) {
            }

            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                MessageActivity.this.mchat.clear();
                for (DataSnapshot value : dataSnapshot.getChildren()) {
                    Chat chat = (Chat) value.getValue(Chat.class);
                    if ((chat.getReceiver().equals(str) && chat.getSender().equals(str2)) || (chat.getReceiver().equals(str2) && chat.getSender().equals(str))) {
                        MessageActivity.this.mchat.add(chat);
                    }
                    if (MessageActivity.this.getLifecycle().getCurrentState().isAtLeast(Lifecycle.State.INITIALIZED)) {
                        MessageActivity messageActivity = MessageActivity.this;
                        messageActivity.messageAdapter = new MessageAdapter(messageActivity, messageActivity.mchat, str3);
                        MessageActivity.this.recyclerView.setAdapter(MessageActivity.this.messageAdapter);
                    }
                }
            }
        });
    }

    private void currentUser(String str) {
        SharedPreferences.Editor edit = getSharedPreferences("PREFS", 0).edit();
        edit.putString("currentuser", str);
        edit.apply();
    }

    private void status(String str) {
        if (this.loginas.equals("principal")) {
            this.reference = FirebaseDatabase.getInstance().getReference("Principal").child(this.fuser.getUid());
            HashMap hashMap = new HashMap();
            hashMap.put(NotificationCompat.CATEGORY_STATUS, str);
            this.reference.updateChildren(hashMap);
        } else if (this.loginas.equals("lecturer")) {
            this.reference = FirebaseDatabase.getInstance().getReference("Staff").child(this.department).child(this.fuser.getUid());
            HashMap hashMap2 = new HashMap();
            hashMap2.put(NotificationCompat.CATEGORY_STATUS, str);
            this.reference.updateChildren(hashMap2);
        } else if (this.loginas.equals("hod")) {
            this.reference = FirebaseDatabase.getInstance().getReference("HODs").child(this.department).child(this.fuser.getUid());
            HashMap hashMap3 = new HashMap();
            hashMap3.put(NotificationCompat.CATEGORY_STATUS, str);
            this.reference.updateChildren(hashMap3);
        }
    }

    /* access modifiers changed from: protected */
    public void onResume() {
        super.onResume();
        status("online");
        currentUser(this.userid);
    }

    /* access modifiers changed from: protected */
    public void onPause() {
        super.onPause();
        this.reference.removeEventListener(this.seenListener);
        status("offline");
        currentUser("none");
    }

    /* access modifiers changed from: private */
    public void showOptions() {
        this.popupWindow.showAtLocation(this.layout, 17, 0, 0);
        this.customview.findViewById(R.id.dialog).setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                MessageActivity.this.popupWindow.dismiss();
            }
        });
        this.pdf.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                MessageActivity messageActivity = MessageActivity.this;
                messageActivity.type = "pdf";
                messageActivity.showPdfChooser();
                MessageActivity.this.popupWindow.dismiss();
            }
        });
        this.img.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                MessageActivity messageActivity = MessageActivity.this;
                messageActivity.type = ContentTypes.EXTENSION_JPG_1;
                messageActivity.showImageChooser();
                MessageActivity.this.popupWindow.dismiss();
            }
        });
        this.cancel.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                MessageActivity.this.popupWindow.dismiss();
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
