package org.projectapp;

import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatTextView;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.viewpager.widget.ViewPager;
import com.google.android.material.tabs.TabLayout;
import java.util.ArrayList;
import org.projectapp.Fragments.AllResourcesFragment;
import org.projectapp.Fragments.ResourcesUploadedByYouFragment;

public class ViewStudyResourceActivity extends AppCompatActivity {
    String department;
    String loginas;
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
        setContentView((int) R.layout.activity_view_study_resource);
        this.title = (AppCompatTextView) findViewById(R.id.title);
        this.title.setText("Study Resources");
        this.department = getIntent().getStringExtra("department");
        this.loginas = getIntent().getStringExtra("loginas");
        TabLayout tabLayout = (TabLayout) findViewById(R.id.tab_layout);
        ViewPager viewPager = (ViewPager) findViewById(R.id.view_pager);
        ViewPagerAdapter viewPagerAdapter = new ViewPagerAdapter(getSupportFragmentManager());
        viewPagerAdapter.addFragment(new AllResourcesFragment(this.department, this, this.loginas), "Resources");
        viewPagerAdapter.addFragment(new ResourcesUploadedByYouFragment(this.department, this, this.loginas), "Uploaded By You");
        viewPager.setAdapter(viewPagerAdapter);
        tabLayout.setupWithViewPager(viewPager);
    }

    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }
}
