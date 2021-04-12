package org.projectapp;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Build.VERSION;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AlertDialog.Builder;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;
import androidx.appcompat.widget.AppCompatEditText;
import androidx.appcompat.widget.AppCompatTextView;
import androidx.appcompat.widget.LinearLayoutCompat;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.view.GravityCompat;
import androidx.core.view.ViewCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.FragmentActivity;
import androidx.lifecycle.Lifecycle.State;
import com.bumptech.glide.Glide;
import com.github.clans.fab.FloatingActionButton;
import com.github.clans.fab.FloatingActionMenu;
import com.google.android.gms.common.Scopes;
import com.google.android.gms.common.internal.ImagesContract;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.navigation.NavigationView;
import com.google.android.material.navigation.NavigationView.OnNavigationItemSelectedListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.InstanceIdResult;
import de.hdodenhof.circleimageview.CircleImageView;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import org.projectapp.Model.Hod;
import org.projectapp.Model.Principal;
import org.projectapp.Model.Staff;
import org.projectapp.Model.Student;

public class HomeActivity extends AppCompatActivity implements OnNavigationItemSelectedListener {
    static String department = "";
    static String email = "";
    static String enrollment = "";
    static String id = "";
    static String login_id = "";
    static String loginas = "";
    static String mode = "";
    static String name = "";
    static String password = "";
    static String phone = "";
    static String profile_img = "default";
    static String roll_no = "";
    static String semester = "";
    static String shot = "";
    Calendar cal;
    GridView grid;
    View headerView;
    CircleImageView icon;
    Boolean isNotAvailable = Boolean.valueOf(true);
    LinearLayoutCompat lr;
    FloatingActionMenu menu;
    ArrayList<String> names;
    NavigationView navigationView;
    AppCompatEditText password1;
    SharedPreferences preferences;
    AppCompatTextView title;
    Toolbar toolbar;
    FloatingActionButton upload_college_notice;
    FloatingActionButton upload_department_notice;
    FloatingActionButton upload_document;
    FloatingActionButton upload_study_resources;
    FloatingActionButton uploadhodstaffnotice;
    FloatingActionButton uploadprincipalhodnotice;
    View view;

    /* access modifiers changed from: protected */
    public void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        if (VERSION.SDK_INT >= 23) {
            String[] strArr = {"android.permission.READ_EXTERNAL_STORAGE", "android.permission.WRITE_EXTERNAL_STORAGE"};
            if (!(ContextCompat.checkSelfPermission(this, strArr[0]) == 0 && ContextCompat.checkSelfPermission(this, strArr[1]) == 0)) {
                ActivityCompat.requestPermissions(this, strArr, 122);
            }
        }
        this.preferences = getSharedPreferences("rait", 0);
        loginas = this.preferences.getString("loginas", null);
        email = this.preferences.getString("email", null);
        id = this.preferences.getString("id", null);
        login_id = this.preferences.getString("login_id", null);
        profile_img = this.preferences.getString(Scopes.PROFILE, null);
        phone = this.preferences.getString("phone", null);
        name = this.preferences.getString("name", null);
        password = this.preferences.getString("password", null);
        String str = "student";
        String str2 = "Staff";
        String str3 = "hod";
        String str4 = "lecturer";
        String str5 = "principal";
        if (loginas.equals(str)) {
            setContentView((int) R.layout.activity_home_student);
            department = this.preferences.getString("branch", null);
            semester = this.preferences.getString("semester", null);
            enrollment = this.preferences.getString("enrollment", null);
            roll_no = this.preferences.getString("roll_no", null);
            FirebaseInstanceId.getInstance().getInstanceId().addOnSuccessListener(new OnSuccessListener<InstanceIdResult>() {
                public void onSuccess(InstanceIdResult instanceIdResult) {
                    final HashMap hashMap = new HashMap();
                    hashMap.put("token", instanceIdResult.getToken());
                    hashMap.put("password", HomeActivity.password);
                    hashMap.put("account", "activated");
                    FirebaseDatabase.getInstance().getReference("Students").child(HomeActivity.department).child(HomeActivity.semester).child(FirebaseAuth.getInstance().getCurrentUser().getUid()).addListenerForSingleValueEvent(new ValueEventListener() {
                        public void onCancelled(@NonNull DatabaseError databaseError) {
                        }

                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                            if (dataSnapshot.getChildrenCount() != 0) {
                                FirebaseDatabase.getInstance().getReference("Students").child(HomeActivity.department).child(HomeActivity.semester).child(FirebaseAuth.getInstance().getCurrentUser().getUid()).updateChildren(hashMap);
                                return;
                            }
                            FirebaseAuth.getInstance().signOut();
                            HomeActivity.this.preferences.edit().clear().commit();
                            Toast.makeText(HomeActivity.this, "Your Account is Deleted By Admin User.", Toast.LENGTH_SHORT).show();
                            HomeActivity.this.startActivity(new Intent(HomeActivity.this, MainActivity.class));
                        }
                    });
                }
            });
        } else {
            String str6 = "department";
            if (loginas.equals(str4)) {
                setContentView((int) R.layout.activity_home_lecturer);
                department = this.preferences.getString(str6, null);
                mode = this.preferences.getString("mode", null);
                FirebaseInstanceId.getInstance().getInstanceId().addOnSuccessListener(new OnSuccessListener<InstanceIdResult>() {
                    public void onSuccess(InstanceIdResult instanceIdResult) {
                        final HashMap hashMap = new HashMap();
                        hashMap.put("account", "activated");
                        hashMap.put("password", HomeActivity.password);
                        hashMap.put("token", instanceIdResult.getToken());
                        FirebaseDatabase instance = FirebaseDatabase.getInstance();
                        StringBuilder sb = new StringBuilder();
                        sb.append("Staff/");
                        sb.append(HomeActivity.department);
                        sb.append("/");
                        sb.append(HomeActivity.id);
                        instance.getReference(sb.toString()).addListenerForSingleValueEvent(new ValueEventListener() {
                            public void onCancelled(@NonNull DatabaseError databaseError) {
                            }

                            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                if (dataSnapshot.getChildrenCount() != 0) {
                                    FirebaseDatabase instance = FirebaseDatabase.getInstance();
                                    StringBuilder sb = new StringBuilder();
                                    sb.append("Staff/");
                                    sb.append(HomeActivity.department);
                                    sb.append("/");
                                    sb.append(HomeActivity.id);
                                    instance.getReference(sb.toString()).updateChildren(hashMap);
                                    return;
                                }
                                FirebaseAuth.getInstance().signOut();
                                HomeActivity.this.preferences.edit().clear().commit();
                                Toast.makeText(HomeActivity.this, "Your Account is Deleted By Admin User.", Toast.LENGTH_SHORT).show();
                                HomeActivity.this.startActivity(new Intent(HomeActivity.this, MainActivity.class));
                            }
                        });
                    }
                });
                FirebaseDatabase.getInstance().getReference(str2).addListenerForSingleValueEvent(new ValueEventListener() {
                    public void onCancelled(@NonNull DatabaseError databaseError) {
                    }

                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        for (DataSnapshot children : dataSnapshot.getChildren()) {
                            for (DataSnapshot value : children.getChildren()) {
                                Staff staff = (Staff) value.getValue(Staff.class);
                                if (staff.getEmail().equals(HomeActivity.email) && !staff.getDepartment().equals(HomeActivity.department)) {
                                    HashMap hashMap = new HashMap();
                                    hashMap.put("password", HomeActivity.password);
                                    FirebaseDatabase instance = FirebaseDatabase.getInstance();
                                    StringBuilder sb = new StringBuilder();
                                    sb.append("Staff/");
                                    sb.append(staff.getDepartment().trim());
                                    sb.append("/");
                                    sb.append(HomeActivity.id);
                                    instance.getReference(sb.toString()).updateChildren(hashMap);
                                }
                            }
                        }
                    }
                });
            } else if (loginas.equals(str3)) {
                setContentView((int) R.layout.activity_home_hod);
                department = this.preferences.getString(str6, null);
                FirebaseInstanceId.getInstance().getInstanceId().addOnSuccessListener(new OnSuccessListener<InstanceIdResult>() {
                    public void onSuccess(InstanceIdResult instanceIdResult) {
                        final HashMap hashMap = new HashMap();
                        hashMap.put("account", "activated");
                        hashMap.put("password", HomeActivity.password);
                        hashMap.put("token", instanceIdResult.getToken());
                        FirebaseDatabase instance = FirebaseDatabase.getInstance();
                        StringBuilder sb = new StringBuilder();
                        sb.append("HODs/");
                        sb.append(HomeActivity.department);
                        sb.append("/");
                        sb.append(HomeActivity.id);
                        instance.getReference(sb.toString()).addListenerForSingleValueEvent(new ValueEventListener() {
                            public void onCancelled(@NonNull DatabaseError databaseError) {
                            }

                            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                if (dataSnapshot.getChildrenCount() != 0) {
                                    FirebaseDatabase instance = FirebaseDatabase.getInstance();
                                    StringBuilder sb = new StringBuilder();
                                    sb.append("HODs/");
                                    sb.append(HomeActivity.department);
                                    sb.append("/");
                                    sb.append(HomeActivity.id);
                                    instance.getReference(sb.toString()).updateChildren(hashMap);
                                    return;
                                }
                                FirebaseAuth.getInstance().signOut();
                                HomeActivity.this.preferences.edit().clear().commit();
                                Toast.makeText(HomeActivity.this, "Your Account is Deleted By Admin User.", Toast.LENGTH_SHORT).show();
                                HomeActivity.this.startActivity(new Intent(HomeActivity.this, MainActivity.class));
                            }
                        });
                    }
                });
                FirebaseDatabase.getInstance().getReference("HODs").addListenerForSingleValueEvent(new ValueEventListener() {
                    public void onCancelled(@NonNull DatabaseError databaseError) {
                    }

                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        for (DataSnapshot children : dataSnapshot.getChildren()) {
                            for (DataSnapshot value : children.getChildren()) {
                                Hod hod = (Hod) value.getValue(Hod.class);
                                if (hod.getEmail().equals(HomeActivity.email) && !hod.getDepartment().equals(HomeActivity.department)) {
                                    HashMap hashMap = new HashMap();
                                    hashMap.put("password", HomeActivity.password);
                                    FirebaseDatabase instance = FirebaseDatabase.getInstance();
                                    StringBuilder sb = new StringBuilder();
                                    sb.append("HODs/");
                                    sb.append(hod.getDepartment().trim());
                                    sb.append("/");
                                    sb.append(HomeActivity.id);
                                    instance.getReference(sb.toString()).updateChildren(hashMap);
                                }
                            }
                        }
                    }
                });
            }
        }
        this.view = View.inflate(this, R.layout.layout_update_password, null);
        this.password1 = (AppCompatEditText) this.view.findViewById(R.id.password);
        this.upload_department_notice = (FloatingActionButton) findViewById(R.id.upload_department_notice);
        this.upload_college_notice = (FloatingActionButton) findViewById(R.id.upload_college_notice);
        this.upload_study_resources = (FloatingActionButton) findViewById(R.id.upload_study_resources);
        this.upload_document = (FloatingActionButton) findViewById(R.id.upload_document);
        this.menu = (FloatingActionMenu) findViewById(R.id.menu);
        this.toolbar = (Toolbar) findViewById(R.id.toolbar);
        this.title = (AppCompatTextView) findViewById(R.id.title);
        this.icon = (CircleImageView) findViewById(R.id.icon);
        this.uploadprincipalhodnotice = (FloatingActionButton) findViewById(R.id.uploadprincipalhodnotice);
        this.uploadhodstaffnotice = (FloatingActionButton) findViewById(R.id.uploadhodstaffnotice);
        this.grid = (GridView) findViewById(R.id.grid);
        this.lr = (LinearLayoutCompat) findViewById(R.id.lr);
        this.cal = Calendar.getInstance();
        setSupportActionBar(this.toolbar);
        getSupportActionBar().setTitle((CharSequence) "");
        if (loginas.equals(str5)) {
            this.title.setText("rait");
        } else {
            this.title.setText(department);
        }
        this.names = new ArrayList<>();
        this.names.clear();
        boolean equals = loginas.equals(str);
        String str7 = "Student's Complaint";
        String str8 = "Study Resources";
        String str9 = "Manual Marks";
        String str10 = "Test Marks";
        String str11 = "Department Notice";
        String str12 = "Students Score";
        String str13 = "College Notice";
        if (equals) {
            this.names.add(str13);
            this.names.add(str11);
            this.names.add("Theory Attedance");
            this.names.add("Practical Attendance");
            this.names.add(str10);
            this.names.add(str9);
            this.names.add("Discussion");
            this.names.add(str7);
            this.names.add(str8);
            this.names.add("View H.O.D");
            this.names.add("View Staff");
            this.names.add(str12);
            this.upload_study_resources.setVisibility(View.VISIBLE);
            GridView gridView = this.grid;
            HomeAdapter homeAdapter = new HomeAdapter(this, this.names, this.menu, loginas, department, name, enrollment, semester, id, profile_img, email, password);
            gridView.setAdapter(homeAdapter);
        } else if (loginas.equals(str5)) {
            this.upload_college_notice.setVisibility(View.VISIBLE);
            this.uploadprincipalhodnotice.setVisibility(View.VISIBLE);
            this.names.add(str13);
            this.names.add("H.O.D Notice");
            this.names.add("H.O.D Chats");
            this.names.add("H.O.D Discussion Portal");
            this.names.add("Active H.O.D");
            this.names.add("InActive H.O.D");
            this.names.add(str2);
            this.names.add("Students");
            this.names.add(str12);
            GridView gridView2 = this.grid;
            HomeAdapter homeAdapter3 = new HomeAdapter(this, this.names, this.menu, loginas, department, name, enrollment, semester, id, profile_img, email, password);
            gridView2.setAdapter(homeAdapter3);
        } else if (loginas.equals(str3)) {
            this.uploadhodstaffnotice.setVisibility(View.VISIBLE);
            this.upload_college_notice.setVisibility(View.VISIBLE);
            this.upload_department_notice.setVisibility(View.VISIBLE);
            this.upload_document.setVisibility(View.VISIBLE);
            this.upload_study_resources.setVisibility(View.VISIBLE);
            this.names.add(str13);
            this.names.add(str11);
            this.names.add("H.O.D Notice");
            this.names.add("Staff Notice");
            this.names.add("H.O.D Discussion Portal");
            this.names.add("Staff Discussion Portal");
            this.names.add("Staff Chats");
            this.names.add("First Year Discussion Portal");
            this.names.add("Second Year Discussion Portal");
            this.names.add("Third year Discussion Portal");
            this.names.add("Subjects");
            this.names.add("Mark Theory Attendance");
            this.names.add("View Theory Attendance");
            this.names.add("Mark Practical Attendance");
            this.names.add("View Practical Attendance");
            this.names.add(str10);
            this.names.add(str9);
            this.names.add(str7);
            this.names.add("Staff's Complaint");
            this.names.add("Active Staff");
            this.names.add("InActive Staff");
            this.names.add("Active Students");
            this.names.add("InActive Students");
            this.names.add(str8);
            this.names.add("Generate Documents");
            this.names.add(str12);
            GridView gridView3 = this.grid;
            HomeAdapter homeAdapter4 = new HomeAdapter(this, this.names, this.menu, loginas, department, name, enrollment, semester, id, profile_img, email, password);
            gridView3.setAdapter(homeAdapter4);
        } else if (loginas.equals(str4)) {
            this.upload_department_notice.setVisibility(View.VISIBLE);
            this.upload_document.setVisibility(View.VISIBLE);
            this.upload_study_resources.setVisibility(View.VISIBLE);
            this.names.add(str13);
            this.names.add(str11);
            this.names.add("Staff Notice");
            this.names.add("Staff Discussion Portal");
            this.names.add("Contact H.O.D");
            this.names.add("Staff's Complaint");
            this.names.add("Mark Theory Attendance");
            this.names.add("View Theory Attendance");
            this.names.add("Mark Practical Attendance");
            this.names.add("View Practical Attendance");
            this.names.add(str10);
            this.names.add(str9);
            this.names.add("First Year Discussion Portal");
            this.names.add("Second Year Discussion Portal");
            this.names.add("Third year Discussion Portal");
            this.names.add(str8);
            this.names.add("InActive Students");
            this.names.add("Active Students");
            this.names.add("Generate Documents");
            this.names.add("View H.O.D");
            this.names.add("View Staff");
            this.names.add(str12);
            GridView gridView4 = this.grid;
            HomeAdapter homeAdapter5 = new HomeAdapter(this, this.names, this.menu, loginas, department, name, enrollment, semester, id, profile_img, email, password);
            gridView4.setAdapter(homeAdapter5);
        }
        DrawerLayout drawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        this.navigationView = (NavigationView) findViewById(R.id.nav_view);
        ActionBarDrawerToggle actionBarDrawerToggle = new ActionBarDrawerToggle(this, drawerLayout, this.toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawerLayout.addDrawerListener(actionBarDrawerToggle);
        actionBarDrawerToggle.getDrawerArrowDrawable().setColor(ViewCompat.MEASURED_STATE_MASK);
        actionBarDrawerToggle.syncState();
        this.navigationView.setNavigationItemSelectedListener(this);
        this.headerView = this.navigationView.getHeaderView(View.VISIBLE);
        TextView textView = (TextView) this.headerView.findViewById(R.id.email);
        ((TextView) this.headerView.findViewById(R.id.name)).setText(name);
        textView.setText(email);
        if (department.equals("Computer Department")) {
            shot = "CO";
        } else if (department.equals("IT Department")) {
            shot = "IT";
        } else if (department.equals("TR Department")) {
            shot = "TR";
        } else if (department.equals("Civil I Shift Department")) {
            shot = "CEI";
        } else if (department.equals("Mechanical I Shift Department")) {
            shot = "MEI";
        } else if (department.equals("Civil II Shift Department")) {
            shot = "CEII";
        } else if (department.equals("Mechanical II Shift Department")) {
            shot = "MEII";
        } else if (department.equals("Chemical Department")) {
            shot = "CH";
        }
        this.grid.setOnTouchListener(new OnTouchListener() {
            public boolean onTouch(View view, MotionEvent motionEvent) {
                HomeActivity.this.menu.close(true);
                return false;
            }
        });
        this.upload_department_notice.setOnClickListener(new OnClickListener() {
            public void onClick(View view) {
                HomeActivity.this.menu.close(true);
                HomeActivity homeActivity = HomeActivity.this;
                Intent putExtra = new Intent(homeActivity, UploadNoticeActivity.class).putExtra("name", HomeActivity.name);
                StringBuilder sb = new StringBuilder();
                sb.append(HomeActivity.department);
                sb.append("/Notice");
                Intent putExtra2 = putExtra.putExtra("path", sb.toString());
                StringBuilder sb2 = new StringBuilder();
                sb2.append(HomeActivity.shot);
                sb2.append(" Notice");
                String str = "department";
                homeActivity.startActivity(putExtra2.putExtra("notice", sb2.toString()).putExtra(str, HomeActivity.department));
            }
        });
        this.upload_college_notice.setOnClickListener(new OnClickListener() {
            public void onClick(View view) {
                HomeActivity.this.menu.close(true);
                HomeActivity homeActivity = HomeActivity.this;
                String str = "College Notice";
                homeActivity.startActivity(new Intent(homeActivity, UploadNoticeActivity.class).putExtra("name", HomeActivity.name).putExtra("path", str).putExtra("notice", str).putExtra("department", ""));
            }
        });
        this.upload_study_resources.setOnClickListener(new OnClickListener() {
            public void onClick(View view) {
                HomeActivity.this.menu.close(true);
                HomeActivity homeActivity = HomeActivity.this;
                String str = "id";
                String str2 = "department";
                homeActivity.startActivity(new Intent(homeActivity, UploadStudyResourceActivity.class).putExtra("name", HomeActivity.name).putExtra(str, HomeActivity.id).putExtra(str2, HomeActivity.department));
            }
        });
        this.uploadhodstaffnotice.setOnClickListener(new OnClickListener() {
            public void onClick(View view) {
                HomeActivity.this.menu.close(true);
                HomeActivity homeActivity = HomeActivity.this;
                Intent putExtra = new Intent(homeActivity, UploadNoticeActivity.class).putExtra("name", HomeActivity.name);
                StringBuilder sb = new StringBuilder();
                sb.append(HomeActivity.department);
                sb.append("/HODSTAFFNOTICE");
                Intent putExtra2 = putExtra.putExtra("path", sb.toString());
                StringBuilder sb2 = new StringBuilder();
                sb2.append(HomeActivity.shot);
                sb2.append(" Staff Notice");
                String str = "department";
                homeActivity.startActivity(putExtra2.putExtra("notice", sb2.toString()).putExtra(str, HomeActivity.department));
            }
        });
        this.uploadprincipalhodnotice.setOnClickListener(new OnClickListener() {
            public void onClick(View view) {
                HomeActivity.this.menu.close(true);
                HomeActivity homeActivity = HomeActivity.this;
                String str = "department";
                homeActivity.startActivity(new Intent(homeActivity, UploadNoticeActivity.class).putExtra("name", HomeActivity.name).putExtra("path", "PRINCIPALHODNOTICE").putExtra("notice", "H.O.D Notice").putExtra(str, HomeActivity.department));
            }
        });
        this.upload_document.setOnClickListener(new OnClickListener() {
            public void onClick(View view) {
                HomeActivity.this.menu.close(true);
                HomeActivity homeActivity = HomeActivity.this;
                String str = "loginas";
                homeActivity.startActivity(new Intent(homeActivity, UploadDocumentActivity.class).putExtra("department", HomeActivity.department).putExtra(str, HomeActivity.loginas));
            }
        });
    }

    public void onBackPressed() {
        DrawerLayout drawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawerLayout.isDrawerOpen((int) GravityCompat.START)) {
            drawerLayout.closeDrawer((int) GravityCompat.START);
        } else if (this.menu.isOpened()) {
            this.menu.close(true);
        } else {
            super.onBackPressed();
            finish();
        }
    }

    public boolean onCreateOptionsMenu(Menu menu2) {
        getMenuInflater().inflate(R.menu.home, menu2);
        return true;
    }

    public boolean onOptionsItemSelected(MenuItem menuItem) {
        int itemId = menuItem.getItemId();
        String str = "Cancel";
        if (itemId == R.id.action_settings) {
            Builder builder = new Builder(this);
            builder.setTitle((CharSequence) "Delete Account");
            builder.setMessage((CharSequence) "Do you really want to delete your account?");
            builder.setPositiveButton((CharSequence) "Confirm", (DialogInterface.OnClickListener) new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialogInterface, int i) {
                    if (HomeActivity.loginas.equals("student")) {
                        FirebaseDatabase.getInstance().getReference("Students").child(HomeActivity.department).child(HomeActivity.semester).child(FirebaseAuth.getInstance().getCurrentUser().getUid()).removeValue().addOnSuccessListener(new OnSuccessListener<Void>() {
                            public void onSuccess(Void voidR) {
                                HomeActivity.this.deleteCurrentUser();
                            }
                        }).addOnFailureListener(new OnFailureListener() {
                            public void onFailure(@NonNull Exception exc) {
                                Toast.makeText(HomeActivity.this, "Account Not Deleted.", Toast.LENGTH_SHORT).show();
                            }
                        });
                    } else if (HomeActivity.loginas.equals("lecturer")) {
                        final DatabaseReference reference = FirebaseDatabase.getInstance().getReference("Staff");
                        reference.addListenerForSingleValueEvent(new ValueEventListener() {
                            public void onCancelled(@NonNull DatabaseError databaseError) {
                            }

                            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                for (DataSnapshot children : dataSnapshot.getChildren()) {
                                    for (DataSnapshot value : children.getChildren()) {
                                        Staff staff = (Staff) value.getValue(Staff.class);
                                        if (staff.getId().equals(FirebaseAuth.getInstance().getCurrentUser().getUid()) && !HomeActivity.department.equals(staff.getDepartment())) {
                                            HomeActivity.this.isNotAvailable = Boolean.valueOf(false);
                                        }
                                    }
                                }
                                if (HomeActivity.this.isNotAvailable.booleanValue()) {
                                    reference.child(HomeActivity.department).child(FirebaseAuth.getInstance().getCurrentUser().getUid()).removeValue().addOnSuccessListener(new OnSuccessListener<Void>() {
                                        public void onSuccess(Void voidR) {
                                            HomeActivity.this.deleteCurrentUser();
                                        }
                                    }).addOnFailureListener(new OnFailureListener() {
                                        public void onFailure(@NonNull Exception exc) {
                                            Toast.makeText(HomeActivity.this, "Account Not Deleted.", Toast.LENGTH_SHORT).show();
                                        }
                                    });
                                } else {
                                    reference.child(HomeActivity.department).child(FirebaseAuth.getInstance().getCurrentUser().getUid()).removeValue().addOnSuccessListener(new OnSuccessListener<Void>() {
                                        public void onSuccess(Void voidR) {
                                            HomeActivity.this.preferences.edit().clear().commit();
                                            HomeActivity.this.startActivity(new Intent(HomeActivity.this, MainActivity.class));
                                            HomeActivity.this.finishAffinity();
                                        }
                                    }).addOnFailureListener(new OnFailureListener() {
                                        public void onFailure(@NonNull Exception exc) {
                                            Toast.makeText(HomeActivity.this, "Account Not Deleted.", Toast.LENGTH_SHORT).show();
                                        }
                                    });
                                }
                            }
                        });
                    } else if (HomeActivity.loginas.equals("hod")) {
                        final DatabaseReference reference2 = FirebaseDatabase.getInstance().getReference("HODs");
                        reference2.addListenerForSingleValueEvent(new ValueEventListener() {
                            public void onCancelled(@NonNull DatabaseError databaseError) {
                            }

                            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                for (DataSnapshot children : dataSnapshot.getChildren()) {
                                    for (DataSnapshot value : children.getChildren()) {
                                        Hod hod = (Hod) value.getValue(Hod.class);
                                        if (hod.getId().equals(FirebaseAuth.getInstance().getCurrentUser().getUid()) && !HomeActivity.department.equals(hod.getDepartment())) {
                                            HomeActivity.this.isNotAvailable = Boolean.valueOf(false);
                                        }
                                    }
                                }
                                if (HomeActivity.this.isNotAvailable.booleanValue()) {
                                    reference2.child(HomeActivity.department).child(FirebaseAuth.getInstance().getCurrentUser().getUid()).removeValue().addOnSuccessListener(new OnSuccessListener<Void>() {
                                        public void onSuccess(Void voidR) {
                                            HomeActivity.this.deleteCurrentUser();
                                        }
                                    }).addOnFailureListener(new OnFailureListener() {
                                        public void onFailure(@NonNull Exception exc) {
                                            Toast.makeText(HomeActivity.this, "Account Not Deleted.", Toast.LENGTH_SHORT).show();
                                        }
                                    });
                                } else {
                                    reference2.child(HomeActivity.department).child(FirebaseAuth.getInstance().getCurrentUser().getUid()).removeValue().addOnSuccessListener(new OnSuccessListener<Void>() {
                                        public void onSuccess(Void voidR) {
                                            HomeActivity.this.preferences.edit().clear().commit();
                                            HomeActivity.this.startActivity(new Intent(HomeActivity.this, MainActivity.class));
                                            HomeActivity.this.finishAffinity();
                                        }
                                    }).addOnFailureListener(new OnFailureListener() {
                                        public void onFailure(@NonNull Exception exc) {
                                            Toast.makeText(HomeActivity.this, "Account Not Deleted.", Toast.LENGTH_SHORT).show();
                                        }
                                    });
                                }
                            }
                        });
                    }
                }
            });
            builder.setNegativeButton((CharSequence) str, (DialogInterface.OnClickListener) new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialogInterface, int i) {
                }
            });
            builder.create();
            if (getLifecycle().getCurrentState().isAtLeast(State.CREATED)) {
                builder.show();
            }
            return true;
        }
        if (itemId == R.id.update_password) {
            if (this.view.getParent() != null) {
                ((ViewGroup) this.view.getParent()).removeView(this.view);
            }
            Builder builder2 = new Builder(this);
            builder2.setTitle((CharSequence) "Update Password");
            builder2.setView(this.view);
            builder2.setPositiveButton((CharSequence) "Update", (DialogInterface.OnClickListener) new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialogInterface, int i) {
                    if (HomeActivity.this.password1.getText().toString().isEmpty()) {
                        Toast.makeText(HomeActivity.this, "Password can't be empty.", Toast.LENGTH_SHORT).show();
                    } else if (HomeActivity.this.password1.getText().toString().length() < 6) {
                        Toast.makeText(HomeActivity.this, "Password length should be more than 6.", Toast.LENGTH_SHORT).show();
                    } else {
                        if (HomeActivity.loginas.equals("student")) {
                            FirebaseAuth.getInstance().getCurrentUser().updatePassword(HomeActivity.this.password1.getText().toString().trim()).addOnSuccessListener(new OnSuccessListener<Void>() {
                                public void onSuccess(Void voidR) {
                                    HashMap hashMap = new HashMap();
                                    hashMap.put("password", HomeActivity.this.password1.getText().toString().trim());
                                    FirebaseDatabase.getInstance().getReference("Students").child(HomeActivity.department).child(HomeActivity.semester).child(FirebaseAuth.getInstance().getCurrentUser().getUid()).updateChildren(hashMap);
                                    HomeActivity.this.password1.setText("");
                                }
                            });
                        } else if (HomeActivity.loginas.equals("hod")) {
                            FirebaseAuth.getInstance().getCurrentUser().updatePassword(HomeActivity.this.password1.getText().toString().trim()).addOnSuccessListener(new OnSuccessListener<Void>() {
                                public void onSuccess(Void voidR) {
                                    final HashMap hashMap = new HashMap();
                                    hashMap.put("password", HomeActivity.this.password1.getText().toString().trim());
                                    FirebaseDatabase.getInstance().getReference("HODs").addListenerForSingleValueEvent(new ValueEventListener() {
                                        public void onCancelled(@NonNull DatabaseError databaseError) {
                                        }

                                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                            for (DataSnapshot children : dataSnapshot.getChildren()) {
                                                for (DataSnapshot value : children.getChildren()) {
                                                    Hod hod = (Hod) value.getValue(Hod.class);
                                                    if (hod.getId().equals(FirebaseAuth.getInstance().getCurrentUser().getUid())) {
                                                        FirebaseDatabase.getInstance().getReference("HODs").child(hod.getDepartment()).child(hod.getId()).updateChildren(hashMap);
                                                        HomeActivity.this.password1.setText("");
                                                    }
                                                }
                                            }
                                        }
                                    });
                                }
                            });
                        } else if (HomeActivity.loginas.equals("lecturer")) {
                            FirebaseAuth.getInstance().getCurrentUser().updatePassword(HomeActivity.this.password1.getText().toString().trim()).addOnSuccessListener(new OnSuccessListener<Void>() {
                                public void onSuccess(Void voidR) {
                                    final HashMap hashMap = new HashMap();
                                    hashMap.put("password", HomeActivity.this.password1.getText().toString().trim());
                                    FirebaseDatabase.getInstance().getReference("Staff").addListenerForSingleValueEvent(new ValueEventListener() {
                                        public void onCancelled(@NonNull DatabaseError databaseError) {
                                        }

                                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                            for (DataSnapshot children : dataSnapshot.getChildren()) {
                                                for (DataSnapshot value : children.getChildren()) {
                                                    Staff staff = (Staff) value.getValue(Staff.class);
                                                    if (staff.getId().equals(FirebaseAuth.getInstance().getCurrentUser().getUid())) {
                                                        FirebaseDatabase.getInstance().getReference("Staff").child(staff.getDepartment()).child(staff.getId()).updateChildren(hashMap);
                                                        HomeActivity.this.password1.setText("");
                                                    }
                                                }
                                            }
                                        }
                                    });
                                }
                            });
                        }
                        HomeActivity.this.getSharedPreferences("rait", 0).edit().putString("password", HomeActivity.this.password1.getText().toString().trim()).commit();
                        Toast.makeText(HomeActivity.this, "Password Updated", Toast.LENGTH_SHORT).show();
                    }
                }
            });
            builder2.setNegativeButton((CharSequence) str, (DialogInterface.OnClickListener) new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialogInterface, int i) {
                }
            });
            builder2.create();
            if (getLifecycle().getCurrentState().isAtLeast(State.CREATED)) {
                builder2.show();
            }
        } else if (itemId == R.id.about_developer) {
            startActivity(new Intent(this, AboutDeveloperActivity.class));
        }
        return super.onOptionsItemSelected(menuItem);
    }

    /* access modifiers changed from: private */
    public void deleteCurrentUser() {
        FirebaseAuth.getInstance().getCurrentUser().delete().addOnSuccessListener(new OnSuccessListener<Void>() {
            public void onSuccess(Void voidR) {
                HomeActivity.this.preferences.edit().clear().commit();
                HomeActivity homeActivity = HomeActivity.this;
                homeActivity.startActivity(new Intent(homeActivity, MainActivity.class));
                HomeActivity.this.finishAffinity();
            }
        });
    }

    public boolean onNavigationItemSelected(MenuItem menuItem) {
        int itemId = menuItem.getItemId();
        this.menu.close(true);
        String str = "admin_email";
        String str2 = "admin_password";
        /* if (itemId == R.id.register_hod) {
            startActivity(new Intent(this, HODRegistrationActivity.class).putExtra(str, FirebaseAuth.getInstance().getCurrentUser().getEmail()).putExtra(str2, password));
        } */
            String str3 = "department";
            if (itemId == R.id.add_staff) {
                startActivity(new Intent(this, StaffRegistrationActivity.class).putExtra(str, FirebaseAuth.getInstance().getCurrentUser().getEmail()).putExtra(str2, password).putExtra(str3, department));
            } else if (itemId == R.id.add_subject) {
                startActivity(new Intent(this, AddSubjectsActivity.class).putExtra(str3, department));
            } else if (itemId == R.id.logout) {
                FirebaseAuth.getInstance().signOut();
                getSharedPreferences("rait", 0).edit().clear().commit();
                startActivity(new Intent(this, MainActivity.class));
            } else if (itemId == R.id.profile) {

                String str4 = Scopes.PROFILE;
                String str5 = "loginas";
                String str6 = "id";
                if (loginas.equals("lecturer")) {
                    startActivity(new Intent(this, ViewStaffProfileActivity.class).putExtra(str6, FirebaseAuth.getInstance().getCurrentUser().getUid()).putExtra(str3, department).putExtra(str5, str4));
                } else if (loginas.equals("hod")) {
                    startActivity(new Intent(this, ViewHodProfileActivity.class).putExtra(str6, FirebaseAuth.getInstance().getCurrentUser().getUid()).putExtra(str3, department).putExtra(str5, str4));
                } else if (loginas.equals("student")) {
                    startActivity(new Intent(this, ViewStudentProfileActivity.class).putExtra(str6, FirebaseAuth.getInstance().getCurrentUser().getUid()).putExtra(str3, department).putExtra("semester", semester).putExtra(str5, str4));
                }
            } else if (itemId == R.id.send_notification) {
                startActivity(new Intent(this, SendNotificationActivity.class).putExtra(str3, department));
            } else if (itemId == R.id.add_student) {
                startActivity(new Intent(this, RegisterStudentActivity.class).putExtra("admin_login", email).putExtra(str2, password).putExtra(str3, department));
            } else if (itemId == R.id.nav_share) {
                Intent intent = new Intent("android.intent.action.SEND");
                intent.setType("text/plain");
                intent.putExtra("android.intent.extra.SUBJECT", "Download rait App");
                intent.putExtra("android.intent.extra.TEXT", "Download app form");
                startActivity(Intent.createChooser(intent, "Share via"));
            } else if (itemId == R.id.nav_send) {
                startActivity(Intent.createChooser(new Intent("android.intent.action.SENDTO").setData(Uri.parse("mailto:rait0116@gmail.com?subject=rait Android Application")), "Send Feedback"));
            }

        ((DrawerLayout) findViewById(R.id.drawer_layout)).closeDrawer((int) GravityCompat.START);
        return true;
    }

    /* access modifiers changed from: protected */
    public void onResume() {
        super.onResume();
        final CircleImageView circleImageView = (CircleImageView) this.headerView.findViewById(R.id.profile);
        SharedPreferences sharedPreferences = getSharedPreferences("rait", 0);
        loginas = sharedPreferences.getString("loginas", null);
        email = sharedPreferences.getString("email", null);
        id = sharedPreferences.getString("id", null);
        login_id = sharedPreferences.getString("login_id", null);
        profile_img = sharedPreferences.getString(Scopes.PROFILE, null);
        phone = sharedPreferences.getString("phone", null);
        name = sharedPreferences.getString("name", null);
        password = sharedPreferences.getString("password", null);
        if (loginas.equals("student")) {
            department = sharedPreferences.getString("branch", null);
            semester = sharedPreferences.getString("semester", null);
            enrollment = sharedPreferences.getString("enrollment", null);
            roll_no = sharedPreferences.getString("roll_no", null);
            FirebaseDatabase.getInstance().getReference("Students").child(department).child(semester).child(FirebaseAuth.getInstance().getCurrentUser().getUid()).addListenerForSingleValueEvent(new ValueEventListener() {
                public void onCancelled(@NonNull DatabaseError databaseError) {
                }

                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    if (dataSnapshot.getChildrenCount() != 0) {
                        Student student = (Student) dataSnapshot.getValue(Student.class);
                        HomeActivity.profile_img = student.getImage_url();
                        HomeActivity.phone = student.getPhone();
                        HomeActivity.name = student.getName();
                        HomeActivity.password = student.getPassword();
                        if (student.getImage_url() == null) {
                            return;
                        }
                        if (student.getImage_url().equals("default")) {
                            circleImageView.setImageResource(R.drawable.profile);
                        } else if (HomeActivity.this.getLifecycle().getCurrentState().isAtLeast(State.INITIALIZED)) {
                            Glide.with((FragmentActivity) HomeActivity.this).load(student.getImage_url()).into((ImageView) circleImageView);
                        }
                    }
                }
            });
        } else {
            String str = "department";
            if (loginas.equals("lecturer")) {
                department = sharedPreferences.getString(str, null);
                mode = sharedPreferences.getString("mode", null);
                FirebaseDatabase.getInstance().getReference("Staff").child(department).child(FirebaseAuth.getInstance().getCurrentUser().getUid()).addListenerForSingleValueEvent(new ValueEventListener() {
                    public void onCancelled(@NonNull DatabaseError databaseError) {
                    }

                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        if (dataSnapshot.getChildrenCount() != 0) {
                            Staff staff = (Staff) dataSnapshot.getValue(Staff.class);
                            if (staff.getProfile_url() == null) {
                                return;
                            }
                            if (staff.getProfile_url().equals("default")) {
                                circleImageView.setImageResource(R.drawable.profile);
                            } else if (HomeActivity.this.getLifecycle().getCurrentState().isAtLeast(State.INITIALIZED)) {
                                Glide.with((FragmentActivity) HomeActivity.this).load(staff.getProfile_url()).into((ImageView) circleImageView);
                            }
                        }
                    }
                });
            } else if (loginas.equals("hod")) {
                department = sharedPreferences.getString(str, null);
                FirebaseDatabase.getInstance().getReference("HODs").child(department).child(FirebaseAuth.getInstance().getCurrentUser().getUid()).addListenerForSingleValueEvent(new ValueEventListener() {
                    public void onCancelled(@NonNull DatabaseError databaseError) {
                    }

                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        if (dataSnapshot.getChildrenCount() != 0) {
                            Hod hod = (Hod) dataSnapshot.getValue(Hod.class);
                            if (hod.getProfile_url() == null) {
                                return;
                            }
                            if (hod.getProfile_url().equals("default")) {
                                circleImageView.setImageResource(R.drawable.profile);
                            } else if (HomeActivity.this.getLifecycle().getCurrentState().isAtLeast(State.INITIALIZED)) {
                                Glide.with((FragmentActivity) HomeActivity.this).load(hod.getProfile_url()).into((ImageView) circleImageView);
                            }
                        }
                    }
                });
            } else if (loginas.equals("principal")) {
                FirebaseDatabase.getInstance().getReference("Principal").child(FirebaseAuth.getInstance().getCurrentUser().getUid()).addListenerForSingleValueEvent(new ValueEventListener() {
                    public void onCancelled(@NonNull DatabaseError databaseError) {
                    }

                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        if (dataSnapshot.getChildrenCount() != 0) {
                            Principal principal = (Principal) dataSnapshot.getValue(Principal.class);
                            if (principal.getProfile_url() == null) {
                                return;
                            }
                            if (principal.getProfile_url().equals("default")) {
                                circleImageView.setImageResource(R.drawable.profile);
                            } else if (HomeActivity.this.getLifecycle().getCurrentState().isAtLeast(State.INITIALIZED)) {
                                Glide.with((FragmentActivity) HomeActivity.this).load(principal.getProfile_url()).into((ImageView) circleImageView);
                            }
                        }
                    }
                });
            }
        }
        TextView textView = (TextView) this.headerView.findViewById(R.id.email);
        ((TextView) this.headerView.findViewById(R.id.name)).setText(name);
        textView.setText(email);
        GridView gridView = this.grid;
        HomeAdapter homeAdapter = new HomeAdapter(this, this.names, this.menu, loginas, department, name, enrollment, semester, id, profile_img, email, password);
        gridView.setAdapter(homeAdapter);
    }

    class HomeAdapter extends BaseAdapter {
        Context context;
        String department;
        String email;
        String enrollment;
        String id;
        String loginas;
        FloatingActionMenu menu;
        String name;
        ArrayList<String> names;
        String password;
        String profile_img = "default";
        String[] semArray;
        String semester;
        String shot;

        public long getItemId(int i) {
            return 0;
        }

        public HomeAdapter(Context context2, ArrayList<String> arrayList, FloatingActionMenu floatingActionMenu, String str, String str2, String str3, String str4, String str5, String str6, String str7, String str8, String str9) {
            String str10 = "";
            this.loginas = str10;
            this.department = str10;
            this.name = str10;
            this.enrollment = str10;
            this.semester = str10;
            this.id = str10;
            this.email = str10;
            this.password = str10;
            this.shot = str10;
            this.context = context2;
            this.names = arrayList;
            this.menu = floatingActionMenu;
            this.loginas = str;
            this.department = str2;
            this.name = str3;
            this.enrollment = str4;
            this.semester = str5;
            this.id = str6;
            this.profile_img = str7;
            this.email = str8;
            this.password = str9;
            String str11 = "Computer Department";
            String str12 = "Chemical Department";
            String str13 = "Mechanical II Shift Department";
            String str14 = "Civil II Shift Department";
            String str15 = "Mechanical I Shift Department";
            String str16 = "Civil I Shift Department";
            String str17 = "TR Department";
            String str18 = "IT Department";
            if (str2.equals(str11)) {
                this.shot = "CO";
            } else if (str2.equals(str18)) {
                this.shot = "IT";
            } else if (str2.equals(str17)) {
                this.shot = "TR";
            } else if (str2.equals(str16)) {
                this.shot = "CEI";
            } else if (str2.equals(str15)) {
                this.shot = "MEI";
            } else if (str2.equals(str14)) {
                this.shot = "CEII";
            } else if (str2.equals(str13)) {
                this.shot = "MEII";
            } else if (str2.equals(str12)) {
                this.shot = "CH";
            }
            Calendar instance = Calendar.getInstance();
            if (str.equals("lecturer") || str.equals("hod")) {
                String str19 = "Select Semester";
                if (str2.equals(str11)) {
                    if (instance.get(2) < 5 || instance.get(2) == 11) {
                        this.semArray = new String[]{str19, "CO2I", "CO4I", "CO6I"};
                    } else {
                        this.semArray = new String[]{str19, "CO1I", "CO3I", "CO5I"};
                    }
                } else if (!str2.equals(str18)) {
                    boolean equals = str2.equals(str16);
                    String str20 = "CE4I";
                    String str21 = "CE2I";
                    if (equals) {
                        if (instance.get(2) < 5 || instance.get(2) == 11) {
                            this.semArray = new String[]{str19, str21, str20, "CE6I"};
                        } else {
                            this.semArray = new String[]{str19, "CE1I", "CE3I", "CE5I"};
                        }
                    } else if (str2.equals(str14)) {
                        if (instance.get(2) < 5 || instance.get(2) == 11) {
                            this.semArray = new String[]{str19, str21, str20, "CE6I"};
                        } else {
                            this.semArray = new String[]{str19, "CE1I", "CE3I", "CE5I"};
                        }
                    } else if (str2.equals(str15)) {
                        if (instance.get(2) < 5 || instance.get(2) == 11) {
                            this.semArray = new String[]{str19, "ME2I", "ME4I", "ME6I"};
                        } else {
                            this.semArray = new String[]{str19, "ME1I", "ME3I", "ME5I"};
                        }
                    } else if (str2.equals(str13)) {
                        if (instance.get(2) < 5 || instance.get(2) == 11) {
                            this.semArray = new String[]{str19, "ME2I", "ME4I", "ME6I"};
                        } else {
                            this.semArray = new String[]{str19, "ME1I", "ME3I", "ME5I"};
                        }
                    } else if (str2.equals(str12)) {
                        if (instance.get(2) < 5 || instance.get(2) == 11) {
                            this.semArray = new String[]{str19, "CH2I", "CH4I", "CH6I"};
                        } else {
                            this.semArray = new String[]{str19, "CH1I", "CH3I", "CH5I"};
                        }
                    } else if (!str2.equals(str17)) {
                    } else {
                        if (instance.get(2) < 5 || instance.get(2) == 11) {
                            this.semArray = new String[]{str19, "TR2G", "TR4G", "TR5G"};
                        } else {
                            this.semArray = new String[]{str19, "TR1G", "TR3G", "TR5G"};
                        }
                    }
                } else if (instance.get(2) < 5 || instance.get(2) == 11) {
                    this.semArray = new String[]{str19, "IF2I", "IF4I", "IF6I"};
                } else {
                    this.semArray = new String[]{str19, "IF1I", "IF3I", "IF5I"};
                }
            }
        }

        public int getCount() {
            return this.names.size();
        }

        public Object getItem(int i) {
            return Integer.valueOf(i);
        }

        public View getView(final int i, View view, ViewGroup viewGroup) {
            AppCompatButton appCompatButton;
            if (view == null) {
                appCompatButton = new AppCompatButton(this.context);
                appCompatButton.setBackgroundResource(R.drawable.home_btn);
                appCompatButton.setLayoutParams(new AbsListView.LayoutParams(300, 200));
            } else {
                appCompatButton = (AppCompatButton) view;
            }
            appCompatButton.setText((CharSequence) this.names.get(i));
            appCompatButton.setOnClickListener(new OnClickListener() {
                public void onClick(View view) {
                    HomeAdapter.this.menu.close(true);
                    String str = "path";
                    String str2 = "loginas";
                    if (((String) HomeAdapter.this.names.get(i)).equals("College Notice")) {
                        HomeAdapter.this.context.startActivity(new Intent(HomeAdapter.this.context, ShowNoticeActivity.class).putExtra("notice", "College Notice").putExtra(str2, HomeAdapter.this.loginas).putExtra(str, "College Notice"));
                    } else if (((String) HomeAdapter.this.names.get(i)).equals("Department Notice")) {
                        Context context = HomeAdapter.this.context;
                        Intent intent = new Intent(HomeAdapter.this.context, ShowNoticeActivity.class);
                        StringBuilder sb = new StringBuilder();
                        sb.append(HomeAdapter.this.shot);
                        sb.append(" Notice");
                        Intent putExtra = intent.putExtra("notice", sb.toString()).putExtra(str2, HomeAdapter.this.loginas);
                        StringBuilder sb2 = new StringBuilder();
                        sb2.append(HomeAdapter.this.department);
                        sb2.append("/Notice");
                        context.startActivity(putExtra.putExtra(str, sb2.toString()));
                    } else {
                        String str3 = "student";
                        String str4 = "semester";
                        String str5 = "name";
                        String str6 = "department";
                        if (((String) HomeAdapter.this.names.get(i)).equals("Theory Attedance")) {
                            HomeAdapter.this.context.startActivity(new Intent(HomeAdapter.this.context, ViewSingleStudentAttendenceActivity.class).putExtra(str5, HomeAdapter.this.name).putExtra("enrollment", HomeAdapter.this.enrollment).putExtra(str4, HomeAdapter.this.semester).putExtra(str2, str3).putExtra("attendence", "theory").putExtra(str6, HomeAdapter.this.department));
                        } else if (((String) HomeAdapter.this.names.get(i)).equals("Practical Attendance")) {
                            HomeAdapter.this.context.startActivity(new Intent(HomeAdapter.this.context, ViewSingleStudentAttendenceActivity.class).putExtra(str5, HomeAdapter.this.name).putExtra("enrollment", HomeAdapter.this.enrollment).putExtra(str4, HomeAdapter.this.semester).putExtra(str2, str3).putExtra("attendence", "practical").putExtra(str6, HomeAdapter.this.department));
                        } else {
                            String str7 = "id";
                            if (((String) HomeAdapter.this.names.get(i)).equals("Test Marks")) {
                                if (HomeAdapter.this.loginas.equals(str3)) {
                                    HomeAdapter.this.context.startActivity(new Intent(HomeAdapter.this.context, StudentTestMarksActivity.class).putExtra(str6, HomeAdapter.this.department).putExtra("enrollment", HomeAdapter.this.enrollment).putExtra(str4, HomeAdapter.this.semester));
                                    return;
                                }
                                HomeAdapter.this.context.startActivity(new Intent(HomeAdapter.this.context, TestMarksActivity.class).putExtra(str6, HomeAdapter.this.department).putExtra(str7, FirebaseAuth.getInstance().getCurrentUser().getUid()).putExtra(str2, HomeAdapter.this.loginas));
                            } else if (!((String) HomeAdapter.this.names.get(i)).equals("Manual Marks")) {
                                boolean equals = ((String) HomeAdapter.this.names.get(i)).equals("Discussion");
                                String str8 = Scopes.PROFILE;
                                String str9 = "cname";
                                String str10 = "noti";
                                if (equals) {
                                    Context context2 = HomeAdapter.this.context;
                                    Intent intent2 = new Intent(HomeAdapter.this.context, DiscussionActivity.class);
                                    StringBuilder sb3 = new StringBuilder();
                                    sb3.append(HomeAdapter.this.department);
                                    sb3.append("/Discussion/");
                                    sb3.append(HomeAdapter.this.semester);
                                    Intent putExtra2 = intent2.putExtra(str, sb3.toString()).putExtra(str6, HomeAdapter.this.department).putExtra(str4, HomeAdapter.this.semester);
                                    StringBuilder sb4 = new StringBuilder();
                                    sb4.append(HomeAdapter.this.semester);
                                    sb4.append(" Discussion Message");
                                    Intent putExtra3 = putExtra2.putExtra(str10, sb4.toString()).putExtra(str8, HomeAdapter.this.profile_img).putExtra(str9, HomeAdapter.this.name);
                                    StringBuilder sb5 = new StringBuilder();
                                    sb5.append(HomeAdapter.this.semester);
                                    sb5.append(" Discussion Portal");
                                    context2.startActivity(putExtra3.putExtra(str5, sb5.toString()));
                                } else if (((String) HomeAdapter.this.names.get(i)).equals("Student's Complaint")) {
                                    Context context3 = HomeAdapter.this.context;
                                    Intent intent3 = new Intent(HomeAdapter.this.context, ComplaintActivity.class);
                                    StringBuilder sb6 = new StringBuilder();
                                    sb6.append(HomeAdapter.this.department);
                                    sb6.append("/Student Complaints");
                                    context3.startActivity(intent3.putExtra(str, sb6.toString()).putExtra(str6, HomeAdapter.this.department).putExtra(str9, HomeAdapter.this.name).putExtra(str2, HomeAdapter.this.loginas).putExtra(str10, "Student Complaint").putExtra(str5, "Complaint Portal"));
                                } else if (((String) HomeAdapter.this.names.get(i)).equals("Study Resources")) {
                                    HomeAdapter.this.context.startActivity(new Intent(HomeAdapter.this.context, ViewStudyResourceActivity.class).putExtra(str6, HomeAdapter.this.department).putExtra(str2, HomeAdapter.this.loginas));
                                } else if (((String) HomeAdapter.this.names.get(i)).equals("H.O.D Notice")) {
                                    HomeAdapter.this.context.startActivity(new Intent(HomeAdapter.this.context, ShowNoticeActivity.class).putExtra("notice", "H.O.D Notice").putExtra(str2, HomeAdapter.this.loginas).putExtra(str, "PRINCIPALHODNOTICE"));
                                } else if (((String) HomeAdapter.this.names.get(i)).equals("H.O.D Chats")) {
                                    HomeAdapter.this.context.startActivity(new Intent(HomeAdapter.this.context, PrincipalChatActivity.class).putExtra(str5, HomeAdapter.this.name));
                                } else {
                                    String str11 = "";
                                    if (((String) HomeAdapter.this.names.get(i)).equals("H.O.D Discussion Portal")) {
                                        HomeAdapter.this.context.startActivity(new Intent(HomeAdapter.this.context, DiscussionActivity.class).putExtra(str, "Discussion/PrincipalHod").putExtra(str8, HomeAdapter.this.profile_img).putExtra(str5, "Discussion Portal").putExtra(str10, "Principal and H.O.D Discussion Message").putExtra(str9, HomeAdapter.this.name).putExtra(str6, str11));
                                        return;
                                    }
                                    String str12 = "unactive";
                                    String str13 = "admin_password";
                                    String str14 = "admin_login";
                                    if (((String) HomeAdapter.this.names.get(i)).equals("Active H.O.D")) {
                                        HomeAdapter.this.context.startActivity(new Intent(HomeAdapter.this.context, ViewHodActivity.class).putExtra(str14, HomeAdapter.this.email).putExtra(str13, HomeAdapter.this.password).putExtra(str12, false).putExtra(str2, HomeAdapter.this.loginas));
                                    } else if (((String) HomeAdapter.this.names.get(i)).equals("InActive H.O.D")) {
                                        HomeAdapter.this.context.startActivity(new Intent(HomeAdapter.this.context, ViewHodActivity.class).putExtra(str14, HomeAdapter.this.email).putExtra(str13, HomeAdapter.this.password).putExtra(str12, true).putExtra(str2, HomeAdapter.this.loginas));
                                    } else if (((String) HomeAdapter.this.names.get(i)).equals("Staff")) {
                                        HomeAdapter.this.context.startActivity(new Intent(HomeAdapter.this.context, ViewStaffActivity.class).putExtra(str14, HomeAdapter.this.email).putExtra(str13, HomeAdapter.this.password).putExtra(str2, HomeAdapter.this.loginas));
                                    } else if (((String) HomeAdapter.this.names.get(i)).equals("Students")) {
                                        HomeAdapter.this.context.startActivity(new Intent(HomeAdapter.this.context, ViewStudentActivity.class).putExtra(str14, HomeAdapter.this.email).putExtra(str13, HomeAdapter.this.password).putExtra(str2, HomeAdapter.this.loginas));
                                    } else if (((String) HomeAdapter.this.names.get(i)).equals("Staff Notice")) {
                                        Context context4 = HomeAdapter.this.context;
                                        Intent intent4 = new Intent(HomeAdapter.this.context, ShowNoticeActivity.class);
                                        StringBuilder sb7 = new StringBuilder();
                                        sb7.append(HomeAdapter.this.shot);
                                        sb7.append(" Staff Notice");
                                        Intent putExtra4 = intent4.putExtra("notice", sb7.toString()).putExtra(str2, HomeAdapter.this.loginas);
                                        StringBuilder sb8 = new StringBuilder();
                                        sb8.append(HomeAdapter.this.department);
                                        sb8.append("/HODSTAFFNOTICE");
                                        context4.startActivity(putExtra4.putExtra(str, sb8.toString()));
                                    } else if (((String) HomeAdapter.this.names.get(i)).equals("Staff Discussion Portal")) {
                                        Context context5 = HomeAdapter.this.context;
                                        Intent intent5 = new Intent(HomeAdapter.this.context, DiscussionActivity.class);
                                        StringBuilder sb9 = new StringBuilder();
                                        sb9.append(HomeAdapter.this.department);
                                        sb9.append("/Discussion/HodStaff");
                                        context5.startActivity(intent5.putExtra(str, sb9.toString()).putExtra(str8, HomeAdapter.this.profile_img).putExtra(str5, "Discussion Portal").putExtra(str10, "Principal and H.O.D Discussion Message").putExtra(str9, HomeAdapter.this.name).putExtra(str6, str11));
                                    } else if (((String) HomeAdapter.this.names.get(i)).equals("Contact Principal")) {
                                        FirebaseDatabase.getInstance().getReference("Principal").addListenerForSingleValueEvent(new ValueEventListener() {
                                            public void onCancelled(@NonNull DatabaseError databaseError) {
                                            }

                                            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                                Principal principal = null;
                                                for (DataSnapshot value : dataSnapshot.getChildren()) {
                                                    principal = (Principal) value.getValue(Principal.class);
                                                }
                                                Intent intent = new Intent(HomeAdapter.this.context, MessageActivity.class);
                                                intent.putExtra("userid", principal.getId());
                                                intent.putExtra("department", HomeAdapter.this.department);
                                                String str = "PrincipalHodChatlist";
                                                intent.putExtra(str, str);
                                                intent.putExtra("loginas", "hod");
                                                intent.putExtra(ImagesContract.URL, principal.getProfile_url());
                                                intent.putExtra("name", principal.getName());
                                                HomeAdapter.this.context.startActivity(intent);
                                            }
                                        });
                                    } else if (((String) HomeAdapter.this.names.get(i)).equals("Staff Chats")) {
                                        HomeAdapter.this.context.startActivity(new Intent(HomeAdapter.this.context, HodChatActivity.class).putExtra(str5, HomeAdapter.this.name).putExtra(str6, HomeAdapter.this.department));
                                    } else if (((String) HomeAdapter.this.names.get(i)).equals("First Year Discussion Portal")) {
                                        Context context6 = HomeAdapter.this.context;
                                        Intent intent6 = new Intent(HomeAdapter.this.context, DiscussionActivity.class);
                                        StringBuilder sb10 = new StringBuilder();
                                        sb10.append(HomeAdapter.this.department);
                                        sb10.append("/Discussion/");
                                        sb10.append(HomeAdapter.this.semArray[1]);
                                        Intent putExtra5 = intent6.putExtra(str, sb10.toString()).putExtra(str6, HomeAdapter.this.department).putExtra(str4, HomeAdapter.this.semArray[1]);
                                        StringBuilder sb11 = new StringBuilder();
                                        sb11.append(HomeAdapter.this.semArray[1]);
                                        sb11.append(" Discussion Message");
                                        Intent putExtra6 = putExtra5.putExtra(str10, sb11.toString()).putExtra(str8, HomeAdapter.this.profile_img).putExtra(str9, HomeAdapter.this.name);
                                        StringBuilder sb12 = new StringBuilder();
                                        sb12.append(HomeAdapter.this.semArray[1]);
                                        sb12.append(" Discussion Portal");
                                        context6.startActivity(putExtra6.putExtra(str5, sb12.toString()));
                                    } else if (((String) HomeAdapter.this.names.get(i)).equals("Second Year Discussion Portal")) {
                                        Context context7 = HomeAdapter.this.context;
                                        Intent intent7 = new Intent(HomeAdapter.this.context, DiscussionActivity.class);
                                        StringBuilder sb13 = new StringBuilder();
                                        sb13.append(HomeAdapter.this.department);
                                        sb13.append("/Discussion/");
                                        sb13.append(HomeAdapter.this.semArray[2]);
                                        Intent putExtra7 = intent7.putExtra(str, sb13.toString()).putExtra(str6, HomeAdapter.this.department).putExtra(str4, HomeAdapter.this.semArray[2]);
                                        StringBuilder sb14 = new StringBuilder();
                                        sb14.append(HomeAdapter.this.semArray[2]);
                                        sb14.append(" Discussion Message");
                                        Intent putExtra8 = putExtra7.putExtra(str10, sb14.toString()).putExtra(str9, HomeAdapter.this.name).putExtra(str8, HomeAdapter.this.profile_img);
                                        StringBuilder sb15 = new StringBuilder();
                                        sb15.append(HomeAdapter.this.semArray[2]);
                                        sb15.append(" Discussion Portal");
                                        context7.startActivity(putExtra8.putExtra(str5, sb15.toString()));
                                    } else if (((String) HomeAdapter.this.names.get(i)).equals("Third year Discussion Portal")) {
                                        Context context8 = HomeAdapter.this.context;
                                        Intent intent8 = new Intent(HomeAdapter.this.context, DiscussionActivity.class);
                                        StringBuilder sb16 = new StringBuilder();
                                        sb16.append(HomeAdapter.this.department);
                                        sb16.append("/Discussion/");
                                        sb16.append(HomeAdapter.this.semArray[3]);
                                        Intent putExtra9 = intent8.putExtra(str, sb16.toString()).putExtra(str6, HomeAdapter.this.department).putExtra(str9, HomeAdapter.this.name);
                                        StringBuilder sb17 = new StringBuilder();
                                        sb17.append(HomeAdapter.this.semArray[3]);
                                        sb17.append(" Discussion Message");
                                        Intent putExtra10 = putExtra9.putExtra(str10, sb17.toString()).putExtra(str4, HomeAdapter.this.semArray[3]).putExtra(str8, HomeAdapter.this.profile_img);
                                        StringBuilder sb18 = new StringBuilder();
                                        sb18.append(HomeAdapter.this.semArray[3]);
                                        sb18.append(" Discussion Portal");
                                        context8.startActivity(putExtra10.putExtra(str5, sb18.toString()));
                                    } else if (((String) HomeAdapter.this.names.get(i)).equals("Mark Theory Attendance")) {
                                        HomeAdapter.this.context.startActivity(new Intent(HomeAdapter.this.context, TheoryAttendenceActivity.class).putExtra(str6, HomeAdapter.this.department).putExtra(str7, FirebaseAuth.getInstance().getCurrentUser().getUid()).putExtra(str2, HomeAdapter.this.loginas));
                                    } else if (((String) HomeAdapter.this.names.get(i)).equals("View Theory Attendance")) {
                                        HomeAdapter.this.context.startActivity(new Intent(HomeAdapter.this.context, ViewTheoryAttendenceActivity.class).putExtra(str6, HomeAdapter.this.department));
                                    } else if (((String) HomeAdapter.this.names.get(i)).equals("Mark Practical Attendance")) {
                                        HomeAdapter.this.context.startActivity(new Intent(HomeAdapter.this.context, PracticalAttendenceActivity.class).putExtra(str6, HomeAdapter.this.department).putExtra(str7, FirebaseAuth.getInstance().getCurrentUser().getUid()).putExtra(str2, HomeAdapter.this.loginas));
                                    } else if (((String) HomeAdapter.this.names.get(i)).equals("View Practical Attendance")) {
                                        HomeAdapter.this.context.startActivity(new Intent(HomeAdapter.this.context, ViewPracticalAttendenceActivity.class).putExtra(str6, HomeAdapter.this.department));
                                    } else if (((String) HomeAdapter.this.names.get(i)).equals("Staff's Complaint")) {
                                        Context context9 = HomeAdapter.this.context;
                                        Intent intent9 = new Intent(HomeAdapter.this.context, ComplaintActivity.class);
                                        StringBuilder sb19 = new StringBuilder();
                                        sb19.append(HomeAdapter.this.department);
                                        sb19.append("/Staff Complaints");
                                        context9.startActivity(intent9.putExtra(str, sb19.toString()).putExtra(str6, HomeAdapter.this.department).putExtra(str9, HomeAdapter.this.name).putExtra(str2, HomeAdapter.this.loginas).putExtra(str10, "Staff Complaint").putExtra(str5, "Complaint Portal"));
                                    } else if (((String) HomeAdapter.this.names.get(i)).equals("Active Staff")) {
                                        HomeAdapter.this.context.startActivity(new Intent(HomeAdapter.this.context, ViewStaffActivity.class).putExtra(str14, HomeAdapter.this.email).putExtra(str13, HomeAdapter.this.password).putExtra(str2, HomeAdapter.this.loginas).putExtra(str12, false).putExtra(str6, HomeAdapter.this.department));
                                    } else if (((String) HomeAdapter.this.names.get(i)).equals("InActive Staff")) {
                                        HomeAdapter.this.context.startActivity(new Intent(HomeAdapter.this.context, ViewStaffActivity.class).putExtra(str14, HomeAdapter.this.email).putExtra(str13, HomeAdapter.this.password).putExtra(str2, HomeAdapter.this.loginas).putExtra(str12, true).putExtra(str6, HomeAdapter.this.department));
                                    } else if (((String) HomeAdapter.this.names.get(i)).equals("Active Students")) {
                                        HomeAdapter.this.context.startActivity(new Intent(HomeAdapter.this.context, ViewStudentActivity.class).putExtra(str14, HomeAdapter.this.email).putExtra(str13, HomeAdapter.this.password).putExtra(str2, HomeAdapter.this.loginas).putExtra(str12, false).putExtra(str6, HomeAdapter.this.department));
                                    } else if (((String) HomeAdapter.this.names.get(i)).equals("InActive Students")) {
                                        HomeAdapter.this.context.startActivity(new Intent(HomeAdapter.this.context, ViewStudentActivity.class).putExtra(str14, HomeAdapter.this.email).putExtra(str13, HomeAdapter.this.password).putExtra(str2, HomeAdapter.this.loginas).putExtra(str12, true).putExtra(str6, HomeAdapter.this.department));
                                    } else if (((String) HomeAdapter.this.names.get(i)).equals("Subjects")) {
                                        HomeAdapter.this.context.startActivity(new Intent(HomeAdapter.this.context, ViewSubjectsActivity.class).putExtra(str6, HomeAdapter.this.department));
                                    } else if (((String) HomeAdapter.this.names.get(i)).equals("Generate Documents")) {
                                        HomeAdapter.this.context.startActivity(new Intent(HomeAdapter.this.context, GenerateDocumentActivity.class).putExtra(str6, HomeAdapter.this.department).putExtra(str2, HomeAdapter.this.loginas));
                                    } else if (((String) HomeAdapter.this.names.get(i)).equals("Contact H.O.D")) {
                                        FirebaseDatabase.getInstance().getReference("HODs").child(HomeAdapter.this.department).addListenerForSingleValueEvent(new ValueEventListener() {
                                            public void onCancelled(@NonNull DatabaseError databaseError) {
                                            }

                                            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                                Hod hod = null;
                                                for (DataSnapshot value : dataSnapshot.getChildren()) {
                                                    hod = (Hod) value.getValue(Hod.class);
                                                }
                                                Intent intent = new Intent(HomeAdapter.this.context, MessageActivity.class);
                                                intent.putExtra("userid", hod.getId());
                                                intent.putExtra("department", HomeAdapter.this.department);
                                                intent.putExtra("PrincipalHodChatlist", "HodChatlist");
                                                intent.putExtra("loginas", "lecturer");
                                                intent.putExtra(ImagesContract.URL, hod.getProfile_url());
                                                intent.putExtra("name", hod.getName());
                                                HomeAdapter.this.context.startActivity(intent);
                                            }
                                        });
                                    } else if (((String) HomeAdapter.this.names.get(i)).equals("View Staff")) {
                                        HomeAdapter.this.context.startActivity(new Intent(HomeAdapter.this.context, ViewStaffActivity.class).putExtra(str14, str11).putExtra(str13, str11).putExtra(str2, "guest").putExtra(str12, true).putExtra(str6, HomeAdapter.this.department));
                                    } else if (((String) HomeAdapter.this.names.get(i)).equals("View H.O.D")) {
                                        HomeAdapter.this.context.startActivity(new Intent(HomeAdapter.this.context, ViewHodActivity.class).putExtra(str14, str11).putExtra(str13, str11).putExtra(str12, false).putExtra(str2, "guest"));
                                    } else if (((String) HomeAdapter.this.names.get(i)).equals("Students Score")) {
                                        HomeAdapter.this.context.startActivity(new Intent(HomeAdapter.this.context, StudentsScoreActivity.class));
                                    }
                                }
                            } else if (HomeAdapter.this.loginas.equals(str3)) {
                                HomeAdapter.this.context.startActivity(new Intent(HomeAdapter.this.context, ViewManualMarksActivity.class).putExtra(str7, HomeAdapter.this.id).putExtra("enrollment", HomeAdapter.this.enrollment).putExtra(str4, HomeAdapter.this.semester).putExtra(str6, HomeAdapter.this.department).putExtra(str5, HomeAdapter.this.name).putExtra(str2, str3));
                            } else {
                                HomeAdapter.this.context.startActivity(new Intent(HomeAdapter.this.context, ManualMarksActivity.class).putExtra(str7, HomeAdapter.this.id).putExtra(str6, HomeAdapter.this.department).putExtra(str2, HomeAdapter.this.loginas));
                            }
                        }
                    }
                }
            });
            return appCompatButton;
        }
    }

}
