package org.projectapp;

import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatTextView;

public class AboutDeveloperActivity extends AppCompatActivity {
    AppCompatTextView title;

    /* access modifiers changed from: protected */
    public void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        setContentView((int) R.layout.activity_about_developer);
        this.title = (AppCompatTextView) findViewById(R.id.title);
        this.title.setText("About Developer's");
    }
}
