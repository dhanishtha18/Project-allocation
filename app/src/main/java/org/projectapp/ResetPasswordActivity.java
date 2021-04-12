package org.projectapp;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;
import androidx.appcompat.widget.AppCompatEditText;
import androidx.appcompat.widget.AppCompatTextView;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;

public class ResetPasswordActivity extends AppCompatActivity {
    /* access modifiers changed from: private */
    public FirebaseAuth auth;
    private AppCompatButton btnReset;
    /* access modifiers changed from: private */
    public AppCompatEditText inputEmail;
    AppCompatTextView title;

    /* access modifiers changed from: protected */
    public void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        setContentView((int) R.layout.activity_reset_password);
        this.inputEmail = (AppCompatEditText) findViewById(R.id.email);
        this.btnReset = (AppCompatButton) findViewById(R.id.btn_reset_password);
        this.title = (AppCompatTextView) findViewById(R.id.title);
        this.title.setText("Reset Password");
        this.auth = FirebaseAuth.getInstance();
        this.btnReset.setOnClickListener(new OnClickListener() {
            public void onClick(View view) {
                String trim = ResetPasswordActivity.this.inputEmail.getText().toString().trim();
                if (TextUtils.isEmpty(trim)) {
                    Toast.makeText(ResetPasswordActivity.this.getApplication(), "Enter your registered email id", Toast.LENGTH_SHORT).show();
                } else {
                    ResetPasswordActivity.this.auth.sendPasswordResetEmail(trim).addOnCompleteListener(new OnCompleteListener<Void>() {
                        public void onComplete(@NonNull Task<Void> task) {
                            if (task.isSuccessful()) {
                                Toast.makeText(ResetPasswordActivity.this, "We have sent you instructions to reset your password!", Toast.LENGTH_SHORT).show();
                            } else {
                                Toast.makeText(ResetPasswordActivity.this, "Failed to send reset email!", Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
                }
            }
        });
    }

    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }
}
