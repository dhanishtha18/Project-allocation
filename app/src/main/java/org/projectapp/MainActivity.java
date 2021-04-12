package org.projectapp;

import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.webkit.DownloadListener;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatImageButton;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.core.view.ViewCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import com.google.android.material.navigation.NavigationView;
import com.google.android.material.navigation.NavigationView.OnNavigationItemSelectedListener;
import com.google.firebase.auth.FirebaseAuth;

public class MainActivity extends AppCompatActivity implements OnNavigationItemSelectedListener {
    AppCompatImageButton back;
    AppCompatImageButton forward;
    AppCompatImageButton homeButton;
    AppCompatImageButton refresh;
    WebView webView;

    /* access modifiers changed from: protected */
    public void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        setContentView((int) R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle((CharSequence) "Ramrao Adik Institute of Technology");
        FirebaseAuth instance = FirebaseAuth.getInstance();
        if (instance.getCurrentUser() != null) {
            SharedPreferences sharedPreferences = getSharedPreferences("rait", 0);
            if (instance.getCurrentUser().getUid() != null) {
                String str = "loginas";
                startActivity(new Intent(this, HomeActivity.class).putExtra(str, sharedPreferences.getString(str, null)));
                finish();
            } else {
                instance.signOut();
                sharedPreferences.edit().clear().commit();
            }
        }
        this.webView = (WebView) findViewById(R.id.web);
        this.back = (AppCompatImageButton) findViewById(R.id.back_arrow);
        this.forward = (AppCompatImageButton) findViewById(R.id.forward_arrow);
        this.refresh = (AppCompatImageButton) findViewById(R.id.refresh);
        this.homeButton = (AppCompatImageButton) findViewById(R.id.home);
        this.webView.loadUrl("http://www.dypatil.edu/mumbai/rait/");
        this.webView.getSettings().setJavaScriptEnabled(true);
        this.webView.getSettings().setUseWideViewPort(true);
        this.webView.getSettings().setDisplayZoomControls(false);
        this.webView.getSettings().setBuiltInZoomControls(true);
        this.webView.getSettings().setLoadWithOverviewMode(true);
        this.webView.getSettings().setSupportZoom(true);
        this.webView.setScrollBarStyle(0);
        this.webView.setBackgroundColor(-1);
        this.webView.setWebViewClient(new WebViewClient() {
            public boolean shouldOverrideUrlLoading(WebView webView, String str) {
                webView.loadUrl(str);
                return true;
            }
        });
        this.webView.setDownloadListener(new DownloadListener() {
            public void onDownloadStart(String str, String str2, String str3, String str4, long j) {
                Intent intent = new Intent("android.intent.action.VIEW");
                intent.setData(Uri.parse(str));
                MainActivity.this.startActivity(intent);
            }
        });
        this.back.setOnClickListener(new OnClickListener() {
            public void onClick(View view) {
                MainActivity.this.webView.goBack();
            }
        });
        this.forward.setOnClickListener(new OnClickListener() {
            public void onClick(View view) {
                MainActivity.this.webView.goForward();
            }
        });
        this.refresh.setOnClickListener(new OnClickListener() {
            public void onClick(View view) {
                MainActivity.this.webView.reload();
            }
        });
        this.homeButton.setOnClickListener(new OnClickListener() {
            public void onClick(View view) {
                MainActivity.this.webView.loadUrl("http://www.dypatil.edu/mumbai/rait/");
            }
        });
        DrawerLayout drawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        ActionBarDrawerToggle actionBarDrawerToggle = new ActionBarDrawerToggle(this, drawerLayout, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        actionBarDrawerToggle.getDrawerArrowDrawable().setColor(ViewCompat.MEASURED_STATE_MASK);
        drawerLayout.addDrawerListener(actionBarDrawerToggle);
        actionBarDrawerToggle.syncState();
        navigationView.setNavigationItemSelectedListener(this);
    }

    public void onBackPressed() {
        DrawerLayout drawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawerLayout.isDrawerOpen((int) GravityCompat.START)) {
            drawerLayout.closeDrawer((int) GravityCompat.START);
            return;
        }
        super.onBackPressed();
        finishAffinity();
    }

    public boolean onNavigationItemSelected(MenuItem menuItem) {
        int itemId = menuItem.getItemId();
        String str = "loginas";
        if (itemId == R.id.login_student) {
            startActivity(new Intent(this, LoginActivity.class).putExtra(str, "student"));
        } else if (itemId == R.id.login_lecturer) {
            startActivity(new Intent(this, LoginActivity.class).putExtra(str, "lecturer"));
        } else if (itemId == R.id.login_hod) {
            startActivity(new Intent(this, LoginActivity.class).putExtra(str, "hod"));
        } else if (itemId == R.id.nav_share) {
            Intent intent = new Intent("android.intent.action.SEND");
            intent.setType("text/plain");
            intent.putExtra("android.intent.extra.SUBJECT", "Download rait App");
            intent.putExtra("android.intent.extra.TEXT", "Download app form http://www.dypatil.edu/mumbai/rait/");
            startActivity(Intent.createChooser(intent, "Share via"));
        } else if (itemId == R.id.nav_send) {
            startActivity(Intent.createChooser(new Intent("android.intent.action.SENDTO").setData(Uri.parse("mailto:rait0116@gmail.com?subject=rait Android Application")), "Send Feedback"));
        } else {
            String str2 = "department";
            if (itemId == R.id.computer) {
                Intent intent2 = new Intent(this, StaticActivity.class);
                intent2.putExtra(str2, "Computer Department");
                startActivity(intent2);
            } else if (itemId == R.id.it) {
                Intent intent3 = new Intent(this, StaticActivity.class);
                intent3.putExtra(str2, "IT Department");
                startActivity(intent3);
            } else if (itemId == R.id.tr) {
                Intent intent4 = new Intent(this, StaticActivity.class);
                intent4.putExtra(str2, "TR Department");
                startActivity(intent4);
            } else if (itemId == R.id.chemical) {
                Intent intent5 = new Intent(this, StaticActivity.class);
                intent5.putExtra(str2, "Chemical Department");
                startActivity(intent5);
            } else if (itemId == R.id.mechanical1st) {
                Intent intent6 = new Intent(this, StaticActivity.class);
                intent6.putExtra(str2, "Mechanical I Shift Department");
                startActivity(intent6);
            } else if (itemId == R.id.mechanical2nd) {
                Intent intent7 = new Intent(this, StaticActivity.class);
                intent7.putExtra(str2, "Mechanical II Shift Department");
                startActivity(intent7);
            } else if (itemId == R.id.civil1st) {
                Intent intent8 = new Intent(this, StaticActivity.class);
                intent8.putExtra(str2, "Civil I Shift Department");
                startActivity(intent8);
            } else if (itemId == R.id.civil2nd) {
                Intent intent9 = new Intent(this, StaticActivity.class);
                intent9.putExtra(str2, "Civil II Shift Department");
                startActivity(intent9);
            } else if (itemId == R.id.about_college) {
                startActivity(new Intent(this, AboutCollegeActivity.class));
            } else if (itemId == R.id.contact_us) {
                startActivity(new Intent(this, ContactUsActivity.class));
            }
        }
        ((DrawerLayout) findViewById(R.id.drawer_layout)).closeDrawer((int) GravityCompat.START);
        return true;
    }

    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    public boolean onOptionsItemSelected(MenuItem menuItem) {
        if (menuItem.getItemId() == R.id.action_settings) {
            startActivity(new Intent(this, AboutDeveloperActivity.class));
        }
        return super.onOptionsItemSelected(menuItem);
    }
}
