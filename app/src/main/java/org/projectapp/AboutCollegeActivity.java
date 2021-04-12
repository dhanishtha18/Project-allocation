package org.projectapp;

import android.os.Bundle;
import android.os.CountDownTimer;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatImageView;
import androidx.appcompat.widget.AppCompatTextView;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import java.util.ArrayList;
import org.projectapp.Model.Principal;

public class AboutCollegeActivity extends AppCompatActivity {

    /* renamed from: i */
    static int f436i;
    AppCompatTextView desk;
    AppCompatImageView img;
    AppCompatTextView mission;
    AppCompatTextView principal_name;
    AppCompatTextView title;
    AppCompatTextView vision;

    /* access modifiers changed from: protected */
    public void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        setContentView((int) R.layout.activity_about_college);
        this.img = (AppCompatImageView) findViewById(R.id.image);
        this.vision = (AppCompatTextView) findViewById(R.id.vision);
        this.mission = (AppCompatTextView) findViewById(R.id.mission);
        this.principal_name = (AppCompatTextView) findViewById(R.id.principal_name);
        this.desk = (AppCompatTextView) findViewById(R.id.desk);
        this.title = (AppCompatTextView) findViewById(R.id.title);
        this.title.setText("About College");
        FirebaseDatabase.getInstance().getReference("Principal").addListenerForSingleValueEvent(new ValueEventListener() {
            public void onCancelled(@NonNull DatabaseError databaseError) {
            }

            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot next : dataSnapshot.getChildren()) {
                    if (next.getChildrenCount() != 0) {
                        Principal principal = (Principal) next.getValue(Principal.class);
                        if (principal.getName() != null) {
                            AppCompatTextView appCompatTextView = AboutCollegeActivity.this.principal_name;
                            appCompatTextView.setText("\t\t\t\t" + principal.getName());
                        }
                    }
                }
            }
        });
        this.desk.setText("\t\t\t\tRAIT offers a plethora of courses including Computer Engineering, Information Technology, Electronics Engineering, Electronics and Telecommunication Engineering and Instrumentation Engineering.\n\t\t\t\t RAIT is affiliated to DY Patil Deemed to be University, recognized by All India Council for Technical Education (AICTE) and accredited by National Assessment and Accreditation Council with “A” grade, paving way for a promising future.\n\t\t\t\tThe institute is much closer to the industrial area within the developing community.\n\t\t\t\tWell established infrastructure facilities are key factors for positive performances. The faculty members are well qualified, experienced & well trained & other personality development courses. Faculty members are capable of conducting continuing educational programmes for the industries and the other organizations.\n");
        this.vision.setText("\t\t\t\tTo induce technical manpower in Information Technology by adapting rapid technological advancement.");
        this.mission.setText("\t\t\t\t1) RAIT’s firm belief in a new form of engineering education that lays equal stress on academics and leadership building extracurricular skills has been a major contribution to the success of RAIT as one of the most reputed institutions of higher learning. \n\t\t\t\tRAIT’s Mission is to produce engineering and technology professionals who are innovative and inspiring thought leaders, adept at solving problems faced by our nation and world by providing quality education.\n\t\t\t\t3)To empower and motivate faculties towards building their domain expertise in technology and management verticals.\n\t\t\t\t4)To groom all round personality of students towards leadership, self-employability, and lifelong learning.\n\t\t\t\t5)To enhance educational level, consultancies & technical services for socioeconomic development.");
        ArrayList arrayList = new ArrayList();
        arrayList.add(Integer.valueOf(R.drawable.welcome));
        arrayList.add(Integer.valueOf(R.drawable.co));
        arrayList.add(Integer.valueOf(R.drawable.it));
        arrayList.add(Integer.valueOf(R.drawable.ce));
        arrayList.add(Integer.valueOf(R.drawable.me));
        arrayList.add(Integer.valueOf(R.drawable.ch));
        arrayList.add(Integer.valueOf(R.drawable.tr));
        slideshow(arrayList);
    }

    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }

    /* access modifiers changed from: private */
    public void slideshow(ArrayList<Integer> arrayList) {
        final ArrayList<Integer> arrayList2 = arrayList;
        new CountDownTimer(10000, 1000) {
            public void onTick(long j) {
                if (AboutCollegeActivity.f436i == arrayList2.size()) {
                    AboutCollegeActivity.f436i = 0;
                }
                AppCompatImageView appCompatImageView = AboutCollegeActivity.this.img;
                ArrayList arrayList = arrayList2;
                int i = AboutCollegeActivity.f436i;
                AboutCollegeActivity.f436i = i + 1;
                appCompatImageView.setImageResource(((Integer) arrayList.get(i)).intValue());
            }

            public void onFinish() {
                AboutCollegeActivity.this.slideshow(arrayList2);
            }
        }.start();
    }
}
