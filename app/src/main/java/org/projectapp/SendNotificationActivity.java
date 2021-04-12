package org.projectapp;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.SpinnerAdapter;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog.Builder;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;
import androidx.appcompat.widget.AppCompatEditText;
import androidx.appcompat.widget.AppCompatSpinner;
import androidx.lifecycle.Lifecycle.State;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import java.util.Calendar;
import org.projectapp.APIs.APIService;
import org.projectapp.Model.Student;
import org.projectapp.Notifications.Client;
import org.projectapp.Notifications.Data;
import org.projectapp.Notifications.MyResponse;
import org.projectapp.Notifications.Sender;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SendNotificationActivity extends AppCompatActivity {
    APIService apiService;
    String department;
    AppCompatEditText notification_message;
    AppCompatButton notification_send;
    AppCompatEditText notification_title;
    String[] semArray;
    String semester;
    AppCompatSpinner student_semester;

    /* access modifiers changed from: protected */
    public void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        setContentView((int) R.layout.activity_send_notification);
        this.department = getIntent().getStringExtra("department");
        this.notification_title = (AppCompatEditText) findViewById(R.id.notification_title);
        this.notification_message = (AppCompatEditText) findViewById(R.id.notification_message);
        this.notification_send = (AppCompatButton) findViewById(R.id.notification_send);
        this.student_semester = (AppCompatSpinner) findViewById(R.id.student_semester);
        this.apiService = (APIService) Client.getClient("https://fcm.googleapis.com/").create(APIService.class);
        Calendar instance = Calendar.getInstance();
        String str = "All Semester";
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
                String str8 = "ME3I";
                String str9 = "ME1I";
                String str10 = "ME6I";
                String str11 = "ME4I";
                String str12 = "ME2I";
                if (this.department.equals("Mechanical I Shift Department")) {
                    if (instance.get(2) < 5 || instance.get(2) == 11) {
                        this.semArray = new String[]{str, str12, str11, str10};
                    } else {
                        this.semArray = new String[]{str, str9, str8, "ME5I"};
                    }
                } else if (this.department.equals("Mechanical II Shift Department")) {
                    if (instance.get(2) < 5 || instance.get(2) == 11) {
                        this.semArray = new String[]{str, str12, str11, str10};
                    } else {
                        this.semArray = new String[]{str, str9, str8, "ME5I"};
                    }
                } else if (this.department.equals("Chemical Department")) {
                    if (instance.get(2) < 5 || instance.get(2) == 11) {
                        this.semArray = new String[]{str, "CH2I", "CH4I", "CH6I"};
                    } else {
                        this.semArray = new String[]{str, "CH1I", "CH3I", "CH5I"};
                    }
                } else if (this.department.equals("TR Department")) {
                    if (instance.get(2) < 5 || instance.get(2) == 11) {
                        this.semArray = new String[]{str, "TR2G", "TR4G", "TR5G"};
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
        this.student_semester.setAdapter((SpinnerAdapter) new ArrayAdapter(this, R.layout.spinner_dialog, this.semArray));
        this.student_semester.setOnItemSelectedListener(new OnItemSelectedListener() {
            public void onNothingSelected(AdapterView<?> adapterView) {
            }

            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long j) {
                SendNotificationActivity.this.semester = adapterView.getItemAtPosition(i).toString();
            }
        });
        this.notification_send.setOnClickListener(new OnClickListener() {
            public void onClick(View view) {
                String str = "";
                String str2 = "Alert";
                String str3 = "OK";
                if (SendNotificationActivity.this.notification_message.getText().toString().equals(str)) {
                    Builder builder = new Builder(SendNotificationActivity.this);
                    builder.setPositiveButton((CharSequence) str3, (DialogInterface.OnClickListener) new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialogInterface, int i) {
                            SendNotificationActivity.this.notification_message.requestFocus();
                        }
                    });
                    builder.setTitle((CharSequence) str2);
                    builder.setMessage((CharSequence) "Enter Message");
                    builder.create();
                    if (SendNotificationActivity.this.getLifecycle().getCurrentState().isAtLeast(State.CREATED)) {
                        builder.show();
                    }
                } else if (SendNotificationActivity.this.notification_title.getText().toString().equals(str)) {
                    Builder builder2 = new Builder(SendNotificationActivity.this);
                    builder2.setPositiveButton((CharSequence) str3, (DialogInterface.OnClickListener) new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialogInterface, int i) {
                            SendNotificationActivity.this.notification_title.requestFocus();
                        }
                    });
                    builder2.setTitle((CharSequence) str2);
                    builder2.setMessage((CharSequence) "Enter Title");
                    builder2.create();
                    if (SendNotificationActivity.this.getLifecycle().getCurrentState().isAtLeast(State.CREATED)) {
                        builder2.show();
                    }
                } else {
                    final ProgressDialog progressDialog = new ProgressDialog(SendNotificationActivity.this);
                    progressDialog.setCancelable(false);
                    progressDialog.setMessage("Sending...");
                    progressDialog.show();
                    String str4 = "Students";
                    if (SendNotificationActivity.this.semester.equals("All Semester")) {
                        FirebaseDatabase.getInstance().getReference(str4).child(SendNotificationActivity.this.department).addListenerForSingleValueEvent(new ValueEventListener() {
                            public void onCancelled(@NonNull DatabaseError databaseError) {
                            }

                            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                for (DataSnapshot children : dataSnapshot.getChildren()) {
                                    for (DataSnapshot value : children.getChildren()) {
                                        Student student = (Student) value.getValue(Student.class);
                                        SendNotificationActivity.this.sendNotification(student.getToken(), student.getId(), SendNotificationActivity.this.notification_title.getText().toString().trim(), SendNotificationActivity.this.notification_message.getText().toString().trim());
                                    }
                                }
                                progressDialog.dismiss();
                                Toast.makeText(SendNotificationActivity.this, "Notification Sended.", Toast.LENGTH_SHORT).show();
                            }
                        });
                    } else {
                        FirebaseDatabase.getInstance().getReference(str4).child(SendNotificationActivity.this.department).child(SendNotificationActivity.this.semester).addListenerForSingleValueEvent(new ValueEventListener() {
                            public void onCancelled(@NonNull DatabaseError databaseError) {
                            }

                            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                for (DataSnapshot value : dataSnapshot.getChildren()) {
                                    Student student = (Student) value.getValue(Student.class);
                                    SendNotificationActivity.this.sendNotification(student.getToken(), student.getId(), SendNotificationActivity.this.notification_title.getText().toString().trim(), SendNotificationActivity.this.notification_message.getText().toString().trim());
                                }
                                progressDialog.dismiss();
                                Toast.makeText(SendNotificationActivity.this, "Notification Sended.", Toast.LENGTH_SHORT).show();
                            }
                        });
                    }
                    SendNotificationActivity.this.notification_title.setText(str);
                    SendNotificationActivity.this.notification_message.setText(str);
                }
            }
        });
    }

    /* access modifiers changed from: private */
    public void sendNotification(String str, String str2, String str3, String str4) {
        this.apiService.sendNotification(new Sender(new Data(FirebaseAuth.getInstance().getCurrentUser().getUid(), str4, str3, str2), str)).enqueue(new Callback<MyResponse>() {
            public void onFailure(Call<MyResponse> call, Throwable th) {
            }

            public void onResponse(Call<MyResponse> call, Response<MyResponse> response) {
                if (response.code() == 200 && ((MyResponse) response.body()).success != 1) {
                    Toast.makeText(SendNotificationActivity.this, "Failed!", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }
}
