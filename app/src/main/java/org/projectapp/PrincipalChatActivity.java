package org.projectapp;

import android.os.Bundle;
import android.widget.ImageView;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatTextView;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.lifecycle.Lifecycle.State;
import androidx.viewpager.widget.ViewPager;
import com.bumptech.glide.Glide;
import com.google.android.material.tabs.TabLayout;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import de.hdodenhof.circleimageview.CircleImageView;
import java.util.ArrayList;
import org.projectapp.Fragments.PrincipalChatFragment;
import org.projectapp.Fragments.PrincipalFragment;
import org.projectapp.Model.Principal;

public class PrincipalChatActivity extends AppCompatActivity {
    FirebaseUser firebaseUser;
    CircleImageView icon;
    AppCompatTextView title;

    class ViewPagerAdapter extends FragmentPagerAdapter {
        private ArrayList<Fragment> fragments = new ArrayList<>();
        private ArrayList<String> titles = new ArrayList<>();

        ViewPagerAdapter(FragmentManager fragmentManager) {
            super(fragmentManager);
        }

        @NonNull
        public Fragment getItem(int i) {
            return (Fragment) this.fragments.get(i);
        }

        public int getCount() {
            return this.fragments.size();
        }

        public void addFragment(Fragment fragment, String str) {
            this.fragments.add(fragment);
            this.titles.add(str);
        }

        @Nullable
        public CharSequence getPageTitle(int i) {
            return (CharSequence) this.titles.get(i);
        }
    }

    /* access modifiers changed from: protected */
    public void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        setContentView((int) R.layout.activity_principal_chat);
        this.title = (AppCompatTextView) findViewById(R.id.title);
        this.icon = (CircleImageView) findViewById(R.id.icon);
        this.title.setText(getIntent().getStringExtra("name"));
        this.firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        FirebaseDatabase.getInstance().getReference("Principal").child(this.firebaseUser.getUid()).addListenerForSingleValueEvent(new ValueEventListener() {
            public void onCancelled(@NonNull DatabaseError databaseError) {
            }

            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                Principal principal = (Principal) dataSnapshot.getValue(Principal.class);
                if (principal.getProfile_url().equals("default")) {
                    PrincipalChatActivity.this.icon.setImageResource(R.drawable.profile);
                } else if (PrincipalChatActivity.this.getLifecycle().getCurrentState().isAtLeast(State.INITIALIZED)) {
                    Glide.with(PrincipalChatActivity.this.getApplicationContext()).load(principal.getProfile_url()).into((ImageView) PrincipalChatActivity.this.icon);
                }
            }
        });
        TabLayout tabLayout = (TabLayout) findViewById(R.id.tab_layout);
        ViewPager viewPager = (ViewPager) findViewById(R.id.view_pager);
        ViewPagerAdapter viewPagerAdapter = new ViewPagerAdapter(getSupportFragmentManager());
        viewPagerAdapter.addFragment(new PrincipalChatFragment(), "Chats");
        viewPagerAdapter.addFragment(new PrincipalFragment(), "H.O.Ds");
        viewPager.setAdapter(viewPagerAdapter);
        tabLayout.setupWithViewPager(viewPager);
    }

    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }
}
