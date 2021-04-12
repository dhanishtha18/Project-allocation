package org.projectapp.Adapter;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface.OnClickListener;
import android.net.Uri;
import android.text.format.DateFormat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RadioGroup.OnCheckedChangeListener;
import android.widget.Toast;
import androidx.appcompat.app.AlertDialog.Builder;
import androidx.appcompat.widget.AppCompatButton;
import androidx.appcompat.widget.AppCompatTextView;
import androidx.recyclerview.widget.RecyclerView.Adapter;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask.TaskSnapshot;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;
import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.projectapp.Model.Document;
import org.projectapp.Model.Student;
import org.projectapp.R;

public class AttendenceAdapter extends Adapter<AttendenceAdapter.ViewHolder> {
    /* access modifiers changed from: private */
    public ArrayList<String> attendence = new ArrayList<>();
    /* access modifiers changed from: private */
    public Context context;
    private String department;
    /* access modifiers changed from: private */
    public File file;
    /* access modifiers changed from: private */
    public String fromTime;
    /* access modifiers changed from: private */
    public String path;
    private String semester;
    /* access modifiers changed from: private */
    public String subject;
    /* access modifiers changed from: private */
    public String toTime;
    /* access modifiers changed from: private */
    public List<Student> uploads;
    /* access modifiers changed from: private */
    public String url;
    private String year;

    class ViewHolder extends androidx.recyclerview.widget.RecyclerView.ViewHolder {
        AppCompatTextView enroll;
        AppCompatTextView name;
        RadioGroup rg;
        AppCompatTextView roll;
        AppCompatButton submit;

        public ViewHolder(View view) {
            super(view);
            this.name = (AppCompatTextView) view.findViewById(R.id.student_name);
            this.roll = (AppCompatTextView) view.findViewById(R.id.roll);
            this.enroll = (AppCompatTextView) view.findViewById(R.id.enroll);
            this.rg = (RadioGroup) view.findViewById(R.id.rg);
            this.submit = (AppCompatButton) view.findViewById(R.id.submit);
        }
    }

    public AttendenceAdapter(Context context2, String str, List<Student> list, File file2, String str2, String str3, String str4, String str5, String str6, String str7, String str8) {
        this.uploads = list;
        this.context = context2;
        this.path = str;
        this.file = file2;
        this.fromTime = str3;
        this.toTime = str2;
        this.subject = str4;
        this.semester = str5;
        this.year = str6;
        this.department = str7;
        this.url = str8;
    }

    public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        return new ViewHolder(LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.layout_attendence, viewGroup, false));
    }

    public void onBindViewHolder(ViewHolder viewHolder, final int i) {
        Student student = (Student) this.uploads.get(i);
        AppCompatTextView appCompatTextView = viewHolder.name;
        StringBuilder sb = new StringBuilder();
        sb.append("Name: ");
        sb.append(student.getName());
        appCompatTextView.setText(sb.toString());
        AppCompatTextView appCompatTextView2 = viewHolder.roll;
        StringBuilder sb2 = new StringBuilder();
        sb2.append("Roll No.: ");
        sb2.append(student.getRoll_no());
        appCompatTextView2.setText(sb2.toString());
        AppCompatTextView appCompatTextView3 = viewHolder.enroll;
        StringBuilder sb3 = new StringBuilder();
        sb3.append("Enrollment: ");
        sb3.append(student.getEnrollment());
        appCompatTextView3.setText(sb3.toString());
        if (this.uploads.size() - 1 == i) {
            viewHolder.submit.setVisibility(View.VISIBLE);
        }
        viewHolder.rg.setOnCheckedChangeListener(new OnCheckedChangeListener() {
            public void onCheckedChanged(RadioGroup radioGroup, int i) {
                RadioButton radioButton = (RadioButton) radioGroup.findViewById(radioGroup.getCheckedRadioButtonId());
                int j = i;
                if (attendence.size() < j) {
                    radioButton.setChecked(false);
                    Builder builder = new Builder(AttendenceAdapter.this.context);
                    builder.setTitle((CharSequence) "Alert");
                    builder.setMessage((CharSequence) "Please Mark Attendence of above students first.");
                    builder.setPositiveButton((CharSequence) "Ok", (OnClickListener) null);
                    builder.create();
                    if (AttendenceAdapter.this.context != null) {
                        builder.show();
                    }
                } else if (AttendenceAdapter.this.attendence.size() == i) {
                    AttendenceAdapter.this.attendence.add(i, radioButton.getText().toString());
                } else {
                    AttendenceAdapter.this.attendence.set(i, radioButton.getText().toString());
                }
            }
        });
        viewHolder.submit.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                if (AttendenceAdapter.this.uploads.size() == AttendenceAdapter.this.attendence.size()) {
                    final ProgressDialog progressDialog = new ProgressDialog(AttendenceAdapter.this.context);
                    progressDialog.setMessage("Uploading Students Attendence...");
                    progressDialog.setCancelable(false);
                    progressDialog.show();
                    try {
                        HSSFWorkbook hSSFWorkbook = new HSSFWorkbook((InputStream) new FileInputStream(AttendenceAdapter.this.file));
                        HSSFSheet sheet = hSSFWorkbook.getSheet(AttendenceAdapter.this.subject);
                        HSSFRow row = sheet.getRow(0);
                        sheet.setColumnWidth(row.getLastCellNum(), 5000);
                        row.createCell((int) row.getLastCellNum()).setCellValue(DateFormat.format("dd-MM-yyyy (HH:mm:ss)", System.currentTimeMillis()).toString());
                        HSSFRow row2 = sheet.getRow(1);
                        HSSFCell createCell = row2.createCell((int) row2.getLastCellNum());
                        StringBuilder sb = new StringBuilder();
                        sb.append(AttendenceAdapter.this.fromTime);
                        sb.append("-");
                        sb.append(AttendenceAdapter.this.toTime);
                        createCell.setCellValue(sb.toString());
                        sheet.getRow(2);
                        for (int i = 2; i < AttendenceAdapter.this.uploads.size() + 2; i++) {
                            HSSFRow row3 = sheet.getRow(i);
                            row3.createCell((int) row3.getLastCellNum()).setCellValue((String) AttendenceAdapter.this.attendence.get(i - 2));
                        }
                        FileOutputStream fileOutputStream = new FileOutputStream(AttendenceAdapter.this.file);
                        hSSFWorkbook.write((OutputStream) fileOutputStream);
                        fileOutputStream.flush();
                        fileOutputStream.close();
                        final StorageReference referenceFromUrl = FirebaseStorage.getInstance().getReferenceFromUrl(AttendenceAdapter.this.url);
                        referenceFromUrl.putFile(Uri.fromFile(AttendenceAdapter.this.file)).addOnSuccessListener((OnSuccessListener) new OnSuccessListener<TaskSnapshot>() {
                            public void onSuccess(TaskSnapshot taskSnapshot) {
                                referenceFromUrl.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                                    public void onSuccess(Uri uri) {
                                        FirebaseDatabase.getInstance().getReference(AttendenceAdapter.this.path).setValue(new Document(uri.toString()));
                                    }
                                });
                                Toast.makeText(AttendenceAdapter.this.context, "Attendence Marked ", Toast.LENGTH_SHORT).show();
                                progressDialog.dismiss();
                            }
                        }).addOnFailureListener((OnFailureListener) new OnFailureListener() {
                            public void onFailure(Exception exc) {
                                progressDialog.dismiss();
                                Toast.makeText(AttendenceAdapter.this.context, exc.getMessage(), Toast.LENGTH_SHORT).show();
                            }
                        });
                    } catch (IOException e) {
                        progressDialog.dismiss();
                        e.printStackTrace();
                    }
                } else {
                    Toast.makeText(AttendenceAdapter.this.context, "Mark attendence of all students", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    public int getItemCount() {
        return this.uploads.size();
    }
}
