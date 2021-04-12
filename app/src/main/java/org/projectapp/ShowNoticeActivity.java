package org.projectapp;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.os.Bundle;
import androidx.appcompat.app.AlertDialog.Builder;
import androidx.appcompat.app.AppCompatActivity;
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
import java.util.ArrayList;
import java.util.List;
import org.projectapp.Adapter.NoticeAdapter;
import org.projectapp.Model.Notice;

public class ShowNoticeActivity extends AppCompatActivity {
    Adapter adapter;
    LinearLayoutManager layoutManager;
    DatabaseReference mDatabase;
    String notice;
    RecyclerView notice_list;
    String path;
    ProgressDialog progressDialog;
    AppCompatTextView title;
    List<Notice> uploads;

    /* access modifiers changed from: protected */
    public void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        setContentView((int) R.layout.activity_show_notice);
        this.layoutManager = new LinearLayoutManager(this);
        this.layoutManager.setReverseLayout(true);
        this.notice_list = (RecyclerView) findViewById(R.id.notice_list);
        this.notice_list.setHasFixedSize(true);
        this.notice_list.setLayoutManager(this.layoutManager);
        this.path = getIntent().getStringExtra("path");
        this.notice = getIntent().getStringExtra("notice");
        this.title = (AppCompatTextView) findViewById(R.id.title);
        this.title.setText(this.notice);
        this.progressDialog = new ProgressDialog(this);
        this.progressDialog.setCancelable(false);
        this.uploads = new ArrayList();
        this.progressDialog.setMessage("Please wait...");
        this.progressDialog.show();
        this.mDatabase = FirebaseDatabase.getInstance().getReference(this.path);
        this.mDatabase.addValueEventListener(new ValueEventListener() {
            public void onDataChange(DataSnapshot dataSnapshot) {
                ShowNoticeActivity.this.progressDialog.dismiss();
                ShowNoticeActivity.this.uploads.clear();
                for (DataSnapshot value : dataSnapshot.getChildren()) {
                    ShowNoticeActivity.this.uploads.add((Notice) value.getValue(Notice.class));
                }
                if (ShowNoticeActivity.this.uploads.size() == 0) {
                    Builder builder = new Builder(ShowNoticeActivity.this);
                    builder.setTitle((CharSequence) "Alert");
                    builder.setMessage((CharSequence) "No Notice Found!!!");
                    builder.setPositiveButton((CharSequence) "Ok", (OnClickListener) new OnClickListener() {
                        public void onClick(DialogInterface dialogInterface, int i) {
                            ShowNoticeActivity.this.onBackPressed();
                        }
                    });
                    builder.create();
                    if (ShowNoticeActivity.this.getLifecycle().getCurrentState().isAtLeast(State.CREATED)) {
                        builder.show();
                    }
                } else if (ShowNoticeActivity.this.getLifecycle().getCurrentState().isAtLeast(State.INITIALIZED)) {
                    ShowNoticeActivity showNoticeActivity = ShowNoticeActivity.this;
                    NoticeAdapter noticeAdapter = new NoticeAdapter(showNoticeActivity, showNoticeActivity.uploads, ShowNoticeActivity.this.getIntent().getStringExtra("loginas"), ShowNoticeActivity.this.path, ShowNoticeActivity.this.notice);
                    showNoticeActivity.adapter = noticeAdapter;
                    ShowNoticeActivity.this.notice_list.setAdapter(ShowNoticeActivity.this.adapter);
                }
            }

            public void onCancelled(DatabaseError databaseError) {
                ShowNoticeActivity.this.progressDialog.dismiss();
            }
        });
    }

    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }
}
