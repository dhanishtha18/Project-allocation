package org.projectapp;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;
import androidx.fragment.app.FragmentActivity;
import androidx.lifecycle.Lifecycle.State;
import com.bumptech.glide.Glide;
import com.google.android.gms.common.internal.ImagesContract;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.storage.FileDownloadTask.TaskSnapshot;
import com.google.firebase.storage.FirebaseStorage;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import uk.co.senab.photoview.PhotoView;

public class ViewImageActivity extends AppCompatActivity {
    static String fullpath;
    static String id;
    static String path;
    static String title;
    static String url;
    PhotoView image_notice;
    AppCompatButton notice_download;

    /* access modifiers changed from: protected */
    public void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        setContentView((int) R.layout.activity_view_image);
        Intent intent = getIntent();
        url = intent.getStringExtra(ImagesContract.URL);
        path = intent.getStringExtra("path");
        fullpath = intent.getStringExtra("fullpath");
        title = intent.getStringExtra("title");
        id = intent.getStringExtra("id");
        this.image_notice = (PhotoView) findViewById(R.id.image_notice);
        this.notice_download = (AppCompatButton) findViewById(R.id.notice_download);
        if (new File(fullpath).exists()) {
            this.notice_download.setVisibility(View.GONE);
            if (getLifecycle().getCurrentState().isAtLeast(State.INITIALIZED)) {
                Glide.with((FragmentActivity) this).load(fullpath).into((ImageView) this.image_notice);
                return;
            }
            return;
        }
        if (getLifecycle().getCurrentState().isAtLeast(State.INITIALIZED)) {
            Glide.with((FragmentActivity) this).load(url).into((ImageView) this.image_notice);
        }
        this.notice_download.setOnClickListener(new OnClickListener() {
            public void onClick(View view) {
                ViewImageActivity.this.notice_download.setVisibility(View.GONE);
                try {
                    File file = new File(ViewImageActivity.path);
                    if (!file.exists()) {
                        file.mkdirs();
                    }
                    String str = ViewImageActivity.path;
                    StringBuilder sb = new StringBuilder();
                    sb.append(ViewImageActivity.title);
                    sb.append(ViewImageActivity.id);
                    sb.append(".jpg");
                    File file2 = new File(str, sb.toString());
                    file2.createNewFile();
                    FirebaseStorage.getInstance().getReferenceFromUrl(ViewImageActivity.url).getFile(file2).addOnSuccessListener((OnSuccessListener) new OnSuccessListener<TaskSnapshot>() {
                        public void onSuccess(TaskSnapshot taskSnapshot) {
                            Toast.makeText(ViewImageActivity.this, "File Downloaded to rait Folder", Toast.LENGTH_SHORT).show();
                        }
                    }).addOnFailureListener((OnFailureListener) new OnFailureListener() {
                        public void onFailure(@NonNull Exception exc) {
                            Toast.makeText(ViewImageActivity.this, "File Downloading Failed, Try after some time", Toast.LENGTH_SHORT).show();
                        }
                    });
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                } catch (IOException e2) {
                    e2.printStackTrace();
                }
            }
        });
    }

    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }
}
