package org.projectapp;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatTextView;

public class ContactUsActivity extends AppCompatActivity {
    AppCompatTextView title;

    /* access modifiers changed from: protected */
    public void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        setContentView((int) R.layout.activity_contact_us);
        this.title = (AppCompatTextView) findViewById(R.id.title);
        this.title.setText("Contact Us");
    }

    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }

    public void directions(View view) {
        startActivity(new Intent("android.intent.action.VIEW", Uri.parse("shorturl.at/gkzJ0")));
    }
}
