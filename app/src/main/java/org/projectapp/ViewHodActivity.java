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
import de.hdodenhof.circleimageview.CircleImageView;
import java.util.ArrayList;
import java.util.List;
import org.projectapp.Adapter.HodAdapter;
import org.projectapp.Model.Hod;

public class ViewHodActivity extends AppCompatActivity {
    Adapter adapter;
    CircleImageView icon;
    LinearLayoutManager layoutManager;
    String loginas;
    DatabaseReference mDatabase;
    ProgressDialog progressDialog;
    AppCompatTextView title;
    RecyclerView unactive_list;
    List<Hod> uploads;

    /* access modifiers changed from: protected */
    public void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        setContentView((int) R.layout.activity_view_hod);
        this.layoutManager = new LinearLayoutManager(this);
        this.unactive_list = (RecyclerView) findViewById(R.id.unactive_list);
        this.title = (AppCompatTextView) findViewById(R.id.title);
        this.loginas = getIntent().getStringExtra("loginas");
        if (!this.loginas.equals("principal")) {
            this.title.setText("H.O.D");
        } else if (getIntent().getBooleanExtra("unactive", false)) {
            this.title.setText("InActive HOD");
        } else {
            this.title.setText("Active HOD");
        }
        this.unactive_list.setHasFixedSize(true);
        this.unactive_list.setLayoutManager(this.layoutManager);
        this.progressDialog = new ProgressDialog(this);
        this.progressDialog.setCancelable(false);
        this.uploads = new ArrayList();
        this.progressDialog.setMessage("Please wait...");
        this.progressDialog.show();
        this.mDatabase = FirebaseDatabase.getInstance().getReference("HODs");
        this.mDatabase.addValueEventListener(new ValueEventListener() {
            public void onDataChange(DataSnapshot dataSnapshot) {
                ViewHodActivity.this.progressDialog.dismiss();
                ViewHodActivity.this.uploads.clear();
                String str = "unactive";
                if (!ViewHodActivity.this.loginas.equals("principal")) {
                    for (DataSnapshot children : dataSnapshot.getChildren()) {
                        for (DataSnapshot value : children.getChildren()) {
                            ViewHodActivity.this.uploads.add((Hod) value.getValue(Hod.class));
                        }
                    }
                } else if (ViewHodActivity.this.getIntent().getBooleanExtra(str, false)) {
                    for (DataSnapshot children2 : dataSnapshot.getChildren()) {
                        for (DataSnapshot value2 : children2.getChildren()) {
                            Hod hod = (Hod) value2.getValue(Hod.class);
                            if (hod.getAccount().equals("deactivated")) {
                                ViewHodActivity.this.uploads.add(hod);
                            }
                        }
                    }
                } else {
                    for (DataSnapshot children3 : dataSnapshot.getChildren()) {
                        for (DataSnapshot value3 : children3.getChildren()) {
                            Hod hod2 = (Hod) value3.getValue(Hod.class);
                            if (hod2.getAccount().equals("activated")) {
                                ViewHodActivity.this.uploads.add(hod2);
                            }
                        }
                    }
                }
                if (ViewHodActivity.this.uploads.size() == 0) {
                    Builder builder = new Builder(ViewHodActivity.this);
                    builder.setTitle((CharSequence) "Alert");
                    builder.setMessage((CharSequence) "No H.O.D Found!!!");
                    builder.setPositiveButton((CharSequence) "Ok", (OnClickListener) new OnClickListener() {
                        public void onClick(DialogInterface dialogInterface, int i) {
                            ViewHodActivity.this.onBackPressed();
                        }
                    });
                    builder.create();
                    if (ViewHodActivity.this.getLifecycle().getCurrentState().isAtLeast(State.CREATED)) {
                        builder.show();
                    }
                } else if (ViewHodActivity.this.getLifecycle().getCurrentState().isAtLeast(State.INITIALIZED)) {
                    ViewHodActivity viewHodActivity = ViewHodActivity.this;
                    HodAdapter hodAdapter = new HodAdapter(viewHodActivity, viewHodActivity.uploads, Boolean.valueOf(ViewHodActivity.this.getIntent().getBooleanExtra(str, false)), ViewHodActivity.this.getIntent().getStringExtra("admin_login"), ViewHodActivity.this.getIntent().getStringExtra("admin_password"), ViewHodActivity.this.loginas);
                    viewHodActivity.adapter = hodAdapter;
                    ViewHodActivity.this.unactive_list.setAdapter(ViewHodActivity.this.adapter);
                }
            }

            public void onCancelled(DatabaseError databaseError) {
                ViewHodActivity.this.progressDialog.dismiss();
            }
        });
    }

    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }
}
