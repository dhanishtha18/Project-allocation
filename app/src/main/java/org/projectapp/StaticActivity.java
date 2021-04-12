package org.projectapp;

import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog.Builder;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;
import androidx.appcompat.widget.AppCompatImageView;
import androidx.appcompat.widget.AppCompatTextView;
import androidx.lifecycle.Lifecycle.State;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import org.projectapp.Model.Hod;

public class StaticActivity extends AppCompatActivity {
    AppCompatImageView depart_img;
    String department;
    String fid = null;
    AppCompatButton hod_details;
    AppCompatTextView id;
    AppCompatTextView labs;
    AppCompatTextView mission;
    AppCompatTextView name;
    AppCompatButton staff_details;
    AppCompatTextView vision;

    /* access modifiers changed from: protected */
    public void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        setContentView((int) R.layout.activity_static_activity);
        this.depart_img = (AppCompatImageView) findViewById(R.id.department_image);
        this.name = (AppCompatTextView) findViewById(R.id.hod_name);
        this.labs = (AppCompatTextView) findViewById(R.id.labs);
        this.vision = (AppCompatTextView) findViewById(R.id.vision);
        this.id = (AppCompatTextView) findViewById(R.id.title);
        this.mission = (AppCompatTextView) findViewById(R.id.mission);
        this.hod_details = (AppCompatButton) findViewById(R.id.hod_details);
        this.staff_details = (AppCompatButton) findViewById(R.id.staff_details);
        this.department = getIntent().getStringExtra("department");
        this.id.setText(this.department);
        FirebaseDatabase.getInstance().getReference("HODs").child(this.department).addValueEventListener(new ValueEventListener() {
            public void onCancelled(@NonNull DatabaseError databaseError) {
            }

            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.getChildrenCount() != 0) {
                    for (DataSnapshot value : dataSnapshot.getChildren()) {
                        Hod hod = (Hod) value.getValue(Hod.class);
                        String str = "\t\t\t\t224,221";
                        String str2 = "\t\t\t\t";
                        if (StaticActivity.this.department.equals("Computer Department")) {
                            StaticActivity.this.depart_img.setImageResource(R.drawable.co);
                            if (hod.getName() != null) {
                                AppCompatTextView appCompatTextView = StaticActivity.this.name;
                                StringBuilder sb = new StringBuilder();
                                sb.append(str2);
                                sb.append(hod.getName());
                                appCompatTextView.setText(sb.toString());
                                StaticActivity.this.fid = hod.getId();
                            }
                            StaticActivity.this.id.setText(StaticActivity.this.department);
                            StaticActivity.this.vision.setText("\t\t\t\tTo be a trend setting department in technical education providing highly competent, efficient manpower to meet the ever changing needs of the country, industry and the society.");
                            StaticActivity.this.mission.setText("\t\t\t\tM1. To provide an atmosphere for students and faculty to enhance problem solving skills, leadership qualities, team spirit & ethical responsibilities.\n\t\t\t\tM2. To develop technical & professional skills to face Evolving Challenges and Social Needs through Innovative Learning Process.\n\t\t\t\tM3. Establish Industry institute interaction program to enhance entrepreneurship skills.\n\t\t\t\tM4. Enabling the Students to Excel in their Professions and Careers with life-long learning keeping speedy growth with emerging technology.");
                            StaticActivity.this.labs.setText(str);
                        }
                        if (StaticActivity.this.department.equals("TR Department")) {
                            StaticActivity.this.depart_img.setImageResource(R.drawable.tr);
                            if (hod.getName() != null) {
                                AppCompatTextView appCompatTextView2 = StaticActivity.this.name;
                                StringBuilder sb2 = new StringBuilder();
                                sb2.append(str2);
                                sb2.append(hod.getName());
                                appCompatTextView2.setText(sb2.toString());
                                StaticActivity.this.fid = hod.getId();
                            }
                            StaticActivity.this.id.setText(StaticActivity.this.department);
                            StaticActivity.this.vision.setText("\t\t\t\tTo be recognized as the leading Hospitality, Travel and tourism training institute in the region, reputed for its programmes on offer and the quality of its Diploma holder.");
                            StaticActivity.this.mission.setText("\t\t\t\tTo train, develop and upgrade committed school leavers and tourism employees with the aim of ensuring a highly competent tourism workforce in order to enhance the country’s overall competitiveness towards the achievement of sustainable tourism development.");
                            StaticActivity.this.labs.setText(str);
                        }
                        String str3 = "\t\t\t\tThe Mechanical Engineering Department endeavors to be recognized globally for outstanding education and research leading to well qualified engineers, who are innovative, entrepreneurial and successful in advanced fields of mechanical engineering to cater the ever changing industrial demands and social needs.";
                        if (StaticActivity.this.department.equals("Mechanical I Shift Department")) {
                            StaticActivity.this.depart_img.setImageResource(R.drawable.me);
                            if (hod.getName() != null) {
                                AppCompatTextView appCompatTextView3 = StaticActivity.this.name;
                                StringBuilder sb3 = new StringBuilder();
                                sb3.append(str2);
                                sb3.append(hod.getName());
                                appCompatTextView3.setText(sb3.toString());
                                StaticActivity.this.fid = hod.getId();
                            }
                            StaticActivity.this.id.setText(StaticActivity.this.department);
                            StaticActivity.this.vision.setText(str3);
                            StaticActivity.this.mission.setText("\t\t\t\tM1.To imparting highest quality education to the students to build their capacity and enhancing their skills to make them globally competitive mechanical engineers.\n\t\t\t\tM2. To maintaining state of the art research facilities to provide collaborative environment that stimulates faculty, staff and students with opportunities to create, analyze, apply and disseminate knowledge.\n\t\t\t\tM3. To develop alliances with world class R&D organizations, educational institutions, industry and alumni for excellence in teaching, research and consultancy practices.\n\t\t\t\tM4. To provide the students with academic environment of excellence, leadership, ethical guidelines and lifelong learning needed for a long productive career.");
                            StaticActivity.this.labs.setText(str);
                        }
                        if (StaticActivity.this.department.equals("Mechanical II Shift Department")) {
                            StaticActivity.this.depart_img.setImageResource(R.drawable.me);
                            if (hod.getName() != null) {
                                AppCompatTextView appCompatTextView4 = StaticActivity.this.name;
                                StringBuilder sb4 = new StringBuilder();
                                sb4.append(str2);
                                sb4.append(hod.getName());
                                appCompatTextView4.setText(sb4.toString());
                                StaticActivity.this.fid = hod.getId();
                            }
                            StaticActivity.this.id.setText(StaticActivity.this.department);
                            StaticActivity.this.vision.setText(str3);
                            StaticActivity.this.mission.setText("\t\t\t\tM1. To imparting highest quality education to the students to build their capacity and enhancing their skills to make them globally competitive mechanical engineers.\n\t\t\t\tM2. To maintaining state of the art research facilities to provide collaborative environment that stimulates faculty, staff and students with opportunities to create, analyze, apply and disseminate knowledge.\n\t\t\t\tM3. To develop alliances with world class R&D organizations, educational institutions, industry and alumni for excellence in teaching, research and consultancy practices.\n\t\t\t\tM4. To provide the students with academic environment of excellence, leadership, ethical guidelines and lifelong learning needed for a long productive career.");
                            StaticActivity.this.labs.setText(str);
                        }
                        String str4 = "\t\t\t\tM1. To promote quality education, research and consultancy for industrial and societal needs.\n\t\t\t\tM2. To inculcate moral and ethical values among the students.\n\t\t\t\tM3. To impart knowledge with emphasis on the development of leadership qualities in students.\n\t\t\t\tM4. To provide state-of-the-art resources that contributes to a congenial learning environment.\n\t\t\t\tM5. To encourage students to pursue higher education and take competitive exams and various career enhancing courses.";
                        String str5 = "\t\t\t\tTo become a school of excellence that brings out civil engineers with high technical competencies and promotes high-end research to meet the current and future challenges in civil engineering.";
                        if (StaticActivity.this.department.equals("Civil II Shift Department")) {
                            StaticActivity.this.depart_img.setImageResource(R.drawable.ce);
                            if (hod.getName() != null) {
                                AppCompatTextView appCompatTextView5 = StaticActivity.this.name;
                                StringBuilder sb5 = new StringBuilder();
                                sb5.append(str2);
                                sb5.append(hod.getName());
                                appCompatTextView5.setText(sb5.toString());
                                StaticActivity.this.fid = hod.getId();
                            }
                            StaticActivity.this.id.setText(StaticActivity.this.department);
                            StaticActivity.this.vision.setText(str5);
                            StaticActivity.this.mission.setText(str4);
                            StaticActivity.this.labs.setText("224,221");
                        }
                        if (StaticActivity.this.department.equals("Civil I Shift Department")) {
                            StaticActivity.this.depart_img.setImageResource(R.drawable.ce);
                            if (hod.getName() != null) {
                                AppCompatTextView appCompatTextView6 = StaticActivity.this.name;
                                StringBuilder sb6 = new StringBuilder();
                                sb6.append(str2);
                                sb6.append(hod.getName());
                                appCompatTextView6.setText(sb6.toString());
                                StaticActivity.this.fid = hod.getId();
                            }
                            StaticActivity.this.id.setText(StaticActivity.this.department);
                            StaticActivity.this.vision.setText(str5);
                            StaticActivity.this.mission.setText(str4);
                            StaticActivity.this.labs.setText(str);
                        }
                        if (StaticActivity.this.department.equals("IT Department")) {
                            StaticActivity.this.depart_img.setImageResource(R.drawable.it);
                            if (hod.getName() != null) {
                                AppCompatTextView appCompatTextView7 = StaticActivity.this.name;
                                StringBuilder sb7 = new StringBuilder();
                                sb7.append(str2);
                                sb7.append(hod.getName());
                                appCompatTextView7.setText(sb7.toString());
                                StaticActivity.this.fid = hod.getId();
                            }
                            StaticActivity.this.id.setText(StaticActivity.this.department);
                            StaticActivity.this.vision.setText("\t\t\t\tTo serve the society and improve the lives by creating technical manpower with capabilities of accepting new challenges in Information Technology.");
                            StaticActivity.this.mission.setText("\t\t\t\tM1. Implementing outcome based curriculum effectively for quality education.\n\t\t\t\tM2. Provide professional knowledge and ethical values among students for the benefits of society.\n\t\t\t\tM3. Provide an environment for students and faculty to apply and transfer knowledge.\n\t\t\t\tM4. Encouraging the students for lifelong learning in the field of Information Technology.");
                            StaticActivity.this.labs.setText(str);
                        }
                        if (StaticActivity.this.department.equals("Chemical Department")) {
                            StaticActivity.this.depart_img.setImageResource(R.drawable.ch);
                            if (hod.getName() != null) {
                                AppCompatTextView appCompatTextView8 = StaticActivity.this.name;
                                StringBuilder sb8 = new StringBuilder();
                                sb8.append(str2);
                                sb8.append(hod.getName());
                                appCompatTextView8.setText(sb8.toString());
                                StaticActivity.this.fid = hod.getId();
                            }
                            StaticActivity.this.id.setText(StaticActivity.this.department);
                            StaticActivity.this.vision.setText("\t\t\t\tTo develop technocrat competent to work in chemical engineering environment");
                            StaticActivity.this.mission.setText("\t\t\t\tM1. To provide an environment that values and encourages knowledge acquisitions with effective implementation.\n\t\t\t\tM2. To maintain the state of the art laboratories to develop industrial competencies among the students.\n\t\t\t\tM3. To empower and motivate faculties towards building their domain expertise in technology and management verticals.\n\t\t\t\tM4. To groom all round personality of students towards leadership, self-employment and lifelong learning.\n\t\t\t\tM5. To enhance educational level , consultancies & Technical services for socio-economic development");
                            StaticActivity.this.labs.setText(str);
                        }
                    }
                    return;
                }
                Builder builder = new Builder(StaticActivity.this);
                builder.setTitle((CharSequence) "Alert");
                builder.setMessage((CharSequence) "No Data Found!!!");
                builder.setPositiveButton((CharSequence) "Ok", (OnClickListener) new OnClickListener() {
                    public void onClick(DialogInterface dialogInterface, int i) {
                        StaticActivity.this.onBackPressed();
                    }
                });
                builder.create();
                if (StaticActivity.this.getLifecycle().getCurrentState().isAtLeast(State.CREATED)) {
                    builder.show();
                }
            }
        });
        this.hod_details.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                if (StaticActivity.this.fid != null) {
                    StaticActivity staticActivity = StaticActivity.this;
                    String str = "department";
                    staticActivity.startActivity(new Intent(staticActivity, ViewHodProfileActivity.class).putExtra("id", StaticActivity.this.fid).putExtra(str, StaticActivity.this.department).putExtra("loginas", "guest"));
                }
            }
        });
        this.staff_details.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                StaticActivity staticActivity = StaticActivity.this;
                String str = "";
                String str2 = "department";
                staticActivity.startActivity(new Intent(staticActivity, ViewStaffActivity.class).putExtra("admin_login", str).putExtra("admin_password", str).putExtra(str2, StaticActivity.this.department).putExtra("loginas", "guest"));
            }
        });
    }

    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }
}
