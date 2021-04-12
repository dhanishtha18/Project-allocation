package org.projectapp;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.SpinnerAdapter;
import androidx.appcompat.app.AlertDialog.Builder;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatSpinner;
import androidx.appcompat.widget.AppCompatTextView;
import androidx.lifecycle.Lifecycle.State;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.RecyclerView.Adapter;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import de.hdodenhof.circleimageview.CircleImageView;
import java.util.ArrayList;
import java.util.List;
import org.projectapp.Adapter.StaffAdapter;
import org.projectapp.Model.Staff;

public class ViewStaffActivity extends AppCompatActivity {
    Adapter adapter;
    String department;
    CircleImageView icon;
    LinearLayoutManager layoutManager;
    DatabaseReference mDatabase;
    ProgressDialog progressDialog;
    AppCompatSpinner staff_department;
    AppCompatTextView title;
    Boolean unactive;
    RecyclerView unactive_list;
    List<Staff> uploads;

    /* access modifiers changed from: protected */
    public void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        setContentView((int) R.layout.activity_view_staff);
        this.layoutManager = new LinearLayoutManager(this);
        this.unactive_list = (RecyclerView) findViewById(R.id.unactive_list);
        this.title = (AppCompatTextView) findViewById(R.id.title);
        this.staff_department = (AppCompatSpinner) findViewById(R.id.staff_department);
        this.unactive_list.setHasFixedSize(true);
        this.unactive_list.setLayoutManager(this.layoutManager);
        String str = "loginas";
        String str2 = "principal";
        if (getIntent().getStringExtra(str).equals(str2) || getIntent().getStringExtra(str).equals("guest")) {
            this.title.setText("Staff");
        } else if (getIntent().getBooleanExtra("unactive", false)) {
            this.title.setText("InActive Staff");
        } else {
            this.title.setText("Active Staff");
        }
        if (getIntent().getStringExtra(str).equals(str2)) {
            this.staff_department.setAdapter((SpinnerAdapter) new ArrayAdapter(this, R.layout.spinner_dialog, new String[]{"Select Department", "Computer Department", "IT Department", "Mechanical I Shift Department", "Mechanical II Shift Department", "Civil I Shift Department", "Civil II Shift Department", "TR Department", "Chemical Department"}));
            this.staff_department.setOnItemSelectedListener(new OnItemSelectedListener() {
                public void onNothingSelected(AdapterView<?> adapterView) {
                }

                public void onItemSelected(AdapterView<?> adapterView, View view, int i, long j) {
                    if (adapterView.getItemAtPosition(i).equals("Select Department")) {
                        ViewStaffActivity.this.department = null;
                        return;
                    }
                    ViewStaffActivity.this.department = adapterView.getItemAtPosition(i).toString();
                    ViewStaffActivity.this.view();
                }
            });
            return;
        }
        this.staff_department.setVisibility(View.GONE);
        this.department = getIntent().getStringExtra("department");
        view();
    }

    /* access modifiers changed from: private */
    public void view() {
        this.progressDialog = new ProgressDialog(this);
        this.progressDialog.setCancelable(false);
        this.uploads = new ArrayList();
        this.progressDialog.setMessage("Please wait...");
        this.progressDialog.show();
        FirebaseDatabase instance = FirebaseDatabase.getInstance();
        StringBuilder sb = new StringBuilder();
        sb.append("Staff/");
        sb.append(this.department);
        this.mDatabase = instance.getReference(sb.toString());
        this.mDatabase.addValueEventListener(new ValueEventListener() {
            public void onDataChange(DataSnapshot dataSnapshot) {
                ViewStaffActivity.this.progressDialog.dismiss();
                ViewStaffActivity.this.uploads.clear();
                String str = "loginas";
                boolean equals = ViewStaffActivity.this.getIntent().getStringExtra(str).equals("hod");
                Boolean valueOf = Boolean.valueOf(false);
                if (!equals) {
                    for (DataSnapshot value : dataSnapshot.getChildren()) {
                        ViewStaffActivity.this.uploads.add((Staff) value.getValue(Staff.class));
                    }
                    ViewStaffActivity.this.unactive = valueOf;
                } else if (ViewStaffActivity.this.getIntent().getBooleanExtra("unactive", false)) {
                    for (DataSnapshot value2 : dataSnapshot.getChildren()) {
                        Staff staff = (Staff) value2.getValue(Staff.class);
                        if (staff.getAccount().equals("deactivated")) {
                            ViewStaffActivity.this.uploads.add(staff);
                        }
                    }
                    ViewStaffActivity.this.unactive = Boolean.valueOf(true);
                } else {
                    for (DataSnapshot value3 : dataSnapshot.getChildren()) {
                        Staff staff2 = (Staff) value3.getValue(Staff.class);
                        if (staff2.getAccount().equals("activated")) {
                            ViewStaffActivity.this.uploads.add(staff2);
                        }
                    }
                    ViewStaffActivity.this.unactive = valueOf;
                }
                if (ViewStaffActivity.this.uploads.size() == 0) {
                    Builder builder = new Builder(ViewStaffActivity.this);
                    builder.setTitle((CharSequence) "Alert");
                    builder.setMessage((CharSequence) "No Staff Found!!!");
                    builder.setPositiveButton((CharSequence) "Ok", (OnClickListener) new OnClickListener() {
                        public void onClick(DialogInterface dialogInterface, int i) {
                            ViewStaffActivity.this.onBackPressed();
                        }
                    });
                    builder.create();
                    if (ViewStaffActivity.this.getLifecycle().getCurrentState().isAtLeast(State.CREATED)) {
                        builder.show();
                    }
                } else if (ViewStaffActivity.this.getLifecycle().getCurrentState().isAtLeast(State.INITIALIZED)) {
                    ViewStaffActivity viewStaffActivity = ViewStaffActivity.this;
                    StaffAdapter staffAdapter = new StaffAdapter(viewStaffActivity, viewStaffActivity.uploads, ViewStaffActivity.this.unactive, ViewStaffActivity.this.getIntent().getStringExtra(str), ViewStaffActivity.this.getIntent().getStringExtra("admin_login"), ViewStaffActivity.this.getIntent().getStringExtra("admin_password"));
                    viewStaffActivity.adapter = staffAdapter;
                    ViewStaffActivity.this.unactive_list.setAdapter(ViewStaffActivity.this.adapter);
                }
            }

            public void onCancelled(DatabaseError databaseError) {
                ViewStaffActivity.this.progressDialog.dismiss();
            }
        });
    }

    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }
}
