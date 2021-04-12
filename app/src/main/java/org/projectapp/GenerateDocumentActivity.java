package org.projectapp;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.os.Environment;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.SpinnerAdapter;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;
import androidx.appcompat.widget.AppCompatSpinner;
import androidx.appcompat.widget.AppCompatTextView;
import androidx.exifinterface.media.ExifInterface;
import androidx.lifecycle.Lifecycle;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FileDownloadTask;
import com.google.firebase.storage.FirebaseStorage;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Calendar;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.util.CellRangeAddress;
import org.apache.xmlbeans.XmlValidationError;
import org.projectapp.Model.Document;
import org.projectapp.Model.Subject;

public class GenerateDocumentActivity extends AppCompatActivity {
    String department;
    String filetype;
    AppCompatButton generate;
    AppCompatSpinner select_document;
    AppCompatSpinner select_semester;
    AppCompatSpinner select_year;
    String[] semArray;
    String semester;
    String shot;
    AppCompatTextView title;

    /* renamed from: y */
    String f439y = null;
    String year;
    ArrayList<String> yearlist;

    /* access modifiers changed from: protected */
    public void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        setContentView((int) R.layout.activity_generate_document);
        this.title = (AppCompatTextView) findViewById(R.id.title);
        this.title.setText("Generate Document");
        this.department = getIntent().getStringExtra("department");
        this.select_document = (AppCompatSpinner) findViewById(R.id.select_document);
        this.select_semester = (AppCompatSpinner) findViewById(R.id.select_semester);
        this.generate = (AppCompatButton) findViewById(R.id.generate);
        this.select_year = (AppCompatSpinner) findViewById(R.id.select_year);
        this.select_document.setAdapter((SpinnerAdapter) new ArrayAdapter(this, R.layout.spinner_dialog, new String[]{"Select File", "Empty Theory Attendence File", "Theory Attendence File", "Empty Practical Attendence File", "Practical Attendence File", "Empty Unit Test File", "Unit Test File", "Empty Manual File", "Manual File", "Empty Student List", "Student List"}));
        this.yearlist = new ArrayList<>();
        Calendar instance = Calendar.getInstance();
        if (this.department.equals("Computer Department")) {
            if (instance.get(2) < 5 || instance.get(2) == 11) {
                this.semArray = new String[]{"Select Semester", "CO2I", "CO4I", "CO6I"};
            } else {
                this.semArray = new String[]{"Select Semester", "CO1I", "CO3I", "CO5I"};
            }
        } else if (this.department.equals("IT Department")) {
            if (instance.get(2) < 5 || instance.get(2) == 11) {
                this.semArray = new String[]{"Select Semester", "IF2I", "IF4I", "IF6I"};
            } else {
                this.semArray = new String[]{"Select Semester", "IF1I", "IF3I", "IF5I"};
            }
        } else if (this.department.equals("Civil I Shift Department")) {
            if (instance.get(2) < 5 || instance.get(2) == 11) {
                this.semArray = new String[]{"Select Semester", "CE2I", "CE4I", "CE6I"};
            } else {
                this.semArray = new String[]{"Select Semester", "CE1I", "CE3I", "CE5I"};
            }
        } else if (this.department.equals("Civil II Shift Department")) {
            if (instance.get(2) < 5 || instance.get(2) == 11) {
                this.semArray = new String[]{"Select Semester", "CE2I", "CE4I", "CE6I"};
            } else {
                this.semArray = new String[]{"Select Semester", "CE1I", "CE3I", "CE5I"};
            }
        } else if (this.department.equals("Mechanical I Shift Department")) {
            if (instance.get(2) < 5 || instance.get(2) == 11) {
                this.semArray = new String[]{"Select Semester", "ME2I", "ME4I", "ME6I"};
            } else {
                this.semArray = new String[]{"Select Semester", "ME1I", "ME3I", "ME5I"};
            }
        } else if (this.department.equals("Mechanical II Shift Department")) {
            if (instance.get(2) < 5 || instance.get(2) == 11) {
                this.semArray = new String[]{"Select Semester", "ME2I", "ME4I", "ME6I"};
            } else {
                this.semArray = new String[]{"Select Semester", "ME1I", "ME3I", "ME5I"};
            }
        } else if (this.department.equals("Chemical Department")) {
            if (instance.get(2) < 5 || instance.get(2) == 11) {
                this.semArray = new String[]{"Select Semester", "CH2I", "CH4I", "CH6I"};
            } else {
                this.semArray = new String[]{"Select Semester", "CH1I", "CH3I", "CH5I"};
            }
        } else if (this.department.equals("TR Department")) {
            if (instance.get(2) < 5 || instance.get(2) == 11) {
                this.semArray = new String[]{"Select Semester", "TR2G", "TR4G"};
            } else {
                this.semArray = new String[]{"Select Semester", "TR1G", "TR3G", "TR5G"};
            }
        }
        if (instance.get(2) < instance.get(5)) {
            this.year = (instance.get(1) - 1) + "-" + instance.get(1);
        } else if (instance.get(2) == 11) {
            this.year = instance.get(1) + "-" + (instance.get(1) + 1);
        } else {
            this.year = instance.get(1) + "-" + (instance.get(1) + 1);
        }
        int i = 2019;
        while (i <= instance.get(1)) {
            ArrayList<String> arrayList = this.yearlist;
            StringBuilder sb = new StringBuilder();
            sb.append(i);
            sb.append("-");
            i++;
            sb.append(i);
            arrayList.add(sb.toString());
        }
        this.select_year.setAdapter((SpinnerAdapter) new ArrayAdapter(this, R.layout.spinner_dialog, this.yearlist));
        this.select_document.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            public void onNothingSelected(AdapterView<?> adapterView) {
            }

            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long j) {
                if (adapterView.getItemAtPosition(i).toString().equals("Select File")) {
                    GenerateDocumentActivity.this.filetype = null;
                    return;
                }
                GenerateDocumentActivity.this.filetype = adapterView.getItemAtPosition(i).toString();
                if (GenerateDocumentActivity.this.filetype.equals("Empty Theory Attendence File")) {
                    GenerateDocumentActivity.this.select_year.setVisibility(View.GONE);
                } else if (GenerateDocumentActivity.this.filetype.equals("Empty Practical Attendence File")) {
                    GenerateDocumentActivity.this.select_year.setVisibility(View.GONE);
                } else if (GenerateDocumentActivity.this.filetype.equals("Empty Unit Test File")) {
                    GenerateDocumentActivity.this.select_year.setVisibility(View.GONE);
                } else if (GenerateDocumentActivity.this.filetype.equals("Empty Manual File")) {
                    GenerateDocumentActivity.this.select_year.setVisibility(View.GONE);
                } else if (GenerateDocumentActivity.this.filetype.equals("Empty Student List")) {
                    GenerateDocumentActivity.this.select_year.setVisibility(View.GONE);
                } else if (GenerateDocumentActivity.this.filetype.equals("Theory Attendence File")) {
                    GenerateDocumentActivity.this.select_year.setVisibility(View.VISIBLE);
                } else if (GenerateDocumentActivity.this.filetype.equals("Practical Attendence File")) {
                    GenerateDocumentActivity.this.select_year.setVisibility(View.VISIBLE);
                } else if (GenerateDocumentActivity.this.filetype.equals("Unit Test File")) {
                    GenerateDocumentActivity.this.select_year.setVisibility(View.VISIBLE);
                } else if (GenerateDocumentActivity.this.filetype.equals("Manual File")) {
                    GenerateDocumentActivity.this.select_year.setVisibility(View.VISIBLE);
                } else if (GenerateDocumentActivity.this.filetype.equals("Student List")) {
                    GenerateDocumentActivity.this.select_year.setVisibility(View.VISIBLE);
                }
            }
        });
        this.select_semester.setAdapter((SpinnerAdapter) new ArrayAdapter(this, R.layout.spinner_dialog, this.semArray));
        this.select_semester.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            public void onNothingSelected(AdapterView<?> adapterView) {
            }

            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long j) {
                if (adapterView.getItemAtPosition(i).toString().equals("Select Semester")) {
                    GenerateDocumentActivity.this.semester = null;
                    return;
                }
                GenerateDocumentActivity.this.semester = adapterView.getItemAtPosition(i).toString();
            }
        });
        if (this.department.equals("Computer Department")) {
            this.shot = "CO";
        } else if (this.department.equals("IT Department")) {
            this.shot = "IF";
        } else if (this.department.equals("TR Department")) {
            this.shot = "TR";
        } else if (this.department.equals("Civil I Shift Department")) {
            this.shot = "CE";
        } else if (this.department.equals("Mechanical I Shift Department")) {
            this.shot = "ME";
        } else if (this.department.equals("Civil II Shift Department")) {
            this.shot = "CE";
        } else if (this.department.equals("Mechanical II Shift Department")) {
            this.shot = "ME";
        } else if (this.department.equals("Chemical Department")) {
            this.shot = "CH";
        }
        this.generate.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                if (GenerateDocumentActivity.this.filetype == null) {
                    AlertDialog.Builder builder = new AlertDialog.Builder(GenerateDocumentActivity.this);
                    builder.setTitle((CharSequence) "Alert");
                    builder.setMessage((CharSequence) "Select Document");
                    builder.setPositiveButton((CharSequence) "Ok", (DialogInterface.OnClickListener) new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialogInterface, int i) {
                            GenerateDocumentActivity.this.select_document.requestFocus();
                        }
                    });
                    builder.create();
                    if (GenerateDocumentActivity.this.getLifecycle().getCurrentState().isAtLeast(Lifecycle.State.CREATED)) {
                        builder.show();
                    }
                } else if (GenerateDocumentActivity.this.semester == null) {
                    AlertDialog.Builder builder2 = new AlertDialog.Builder(GenerateDocumentActivity.this);
                    builder2.setTitle((CharSequence) "Alert");
                    builder2.setMessage((CharSequence) "Select Semester");
                    builder2.setPositiveButton((CharSequence) "Ok", (DialogInterface.OnClickListener) new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialogInterface, int i) {
                            GenerateDocumentActivity.this.select_semester.requestFocus();
                        }
                    });
                    builder2.create();
                    if (GenerateDocumentActivity.this.getLifecycle().getCurrentState().isAtLeast(Lifecycle.State.CREATED)) {
                        builder2.show();
                    }
                } else {
                    if (GenerateDocumentActivity.this.filetype.equals("Empty Theory Attendence File")) {
                        GenerateDocumentActivity generateDocumentActivity = GenerateDocumentActivity.this;
                        generateDocumentActivity.generateEmptyTheoryAttedenceExcelFile(generateDocumentActivity.semester, GenerateDocumentActivity.this.department, GenerateDocumentActivity.this.year);
                    } else if (GenerateDocumentActivity.this.filetype.equals("Empty Practical Attendence File")) {
                        GenerateDocumentActivity generateDocumentActivity2 = GenerateDocumentActivity.this;
                        generateDocumentActivity2.generateEmptyPracticalAttedenceExcelFile(generateDocumentActivity2.semester, GenerateDocumentActivity.this.department, GenerateDocumentActivity.this.shot, GenerateDocumentActivity.this.year);
                    } else if (GenerateDocumentActivity.this.filetype.equals("Empty Unit Test File")) {
                        GenerateDocumentActivity generateDocumentActivity3 = GenerateDocumentActivity.this;
                        generateDocumentActivity3.generateEmptyUnitTestExcelFile(generateDocumentActivity3.semester, GenerateDocumentActivity.this.department, GenerateDocumentActivity.this.year);
                    } else if (GenerateDocumentActivity.this.filetype.equals("Empty Manual File")) {
                        GenerateDocumentActivity generateDocumentActivity4 = GenerateDocumentActivity.this;
                        generateDocumentActivity4.generateEmptyManualExcelFile(generateDocumentActivity4.semester, GenerateDocumentActivity.this.department, GenerateDocumentActivity.this.year);
                    } else if (GenerateDocumentActivity.this.filetype.equals("Empty Student List")) {
                        GenerateDocumentActivity generateDocumentActivity5 = GenerateDocumentActivity.this;
                        generateDocumentActivity5.generateEmptyStudentExcelFile(generateDocumentActivity5.semester, GenerateDocumentActivity.this.department, GenerateDocumentActivity.this.year);
                    } else if (GenerateDocumentActivity.this.filetype.equals("Theory Attendence File")) {
                        if (GenerateDocumentActivity.this.f439y == null) {
                            AlertDialog.Builder builder3 = new AlertDialog.Builder(GenerateDocumentActivity.this);
                            builder3.setTitle((CharSequence) "Alert");
                            builder3.setMessage((CharSequence) "Select Year");
                            builder3.setPositiveButton((CharSequence) "Ok", (DialogInterface.OnClickListener) null);
                            builder3.create();
                            if (GenerateDocumentActivity.this.getLifecycle().getCurrentState().isAtLeast(Lifecycle.State.CREATED)) {
                                builder3.show();
                            }
                        } else {
                            GenerateDocumentActivity generateDocumentActivity6 = GenerateDocumentActivity.this;
                            generateDocumentActivity6.generateTheoryAttedenceExcelFile(generateDocumentActivity6.semester, GenerateDocumentActivity.this.department, GenerateDocumentActivity.this.f439y);
                        }
                    } else if (GenerateDocumentActivity.this.filetype.equals("Practical Attendence File")) {
                        if (GenerateDocumentActivity.this.f439y == null) {
                            AlertDialog.Builder builder4 = new AlertDialog.Builder(GenerateDocumentActivity.this);
                            builder4.setTitle((CharSequence) "Alert");
                            builder4.setMessage((CharSequence) "Select Year");
                            builder4.setPositiveButton((CharSequence) "Ok", (DialogInterface.OnClickListener) null);
                            builder4.create();
                            if (GenerateDocumentActivity.this.getLifecycle().getCurrentState().isAtLeast(Lifecycle.State.CREATED)) {
                                builder4.show();
                            }
                        } else {
                            GenerateDocumentActivity generateDocumentActivity7 = GenerateDocumentActivity.this;
                            generateDocumentActivity7.generatePracticalAttedenceExcelFile(generateDocumentActivity7.semester, GenerateDocumentActivity.this.department, GenerateDocumentActivity.this.f439y);
                        }
                    } else if (GenerateDocumentActivity.this.filetype.equals("Unit Test File")) {
                        if (GenerateDocumentActivity.this.f439y == null) {
                            AlertDialog.Builder builder5 = new AlertDialog.Builder(GenerateDocumentActivity.this);
                            builder5.setTitle((CharSequence) "Alert");
                            builder5.setMessage((CharSequence) "Select Year");
                            builder5.setPositiveButton((CharSequence) "Ok", (DialogInterface.OnClickListener) null);
                            builder5.create();
                            if (GenerateDocumentActivity.this.getLifecycle().getCurrentState().isAtLeast(Lifecycle.State.CREATED)) {
                                builder5.show();
                            }
                        } else {
                            GenerateDocumentActivity generateDocumentActivity8 = GenerateDocumentActivity.this;
                            generateDocumentActivity8.generateUnitTestExcelFile(generateDocumentActivity8.semester, GenerateDocumentActivity.this.department, GenerateDocumentActivity.this.f439y);
                        }
                    } else if (GenerateDocumentActivity.this.filetype.equals("Manual File")) {
                        if (GenerateDocumentActivity.this.f439y == null) {
                            AlertDialog.Builder builder6 = new AlertDialog.Builder(GenerateDocumentActivity.this);
                            builder6.setTitle((CharSequence) "Alert");
                            builder6.setMessage((CharSequence) "Select Year");
                            builder6.setPositiveButton((CharSequence) "Ok", (DialogInterface.OnClickListener) null);
                            builder6.create();
                            if (GenerateDocumentActivity.this.getLifecycle().getCurrentState().isAtLeast(Lifecycle.State.CREATED)) {
                                builder6.show();
                            }
                        } else {
                            GenerateDocumentActivity generateDocumentActivity9 = GenerateDocumentActivity.this;
                            generateDocumentActivity9.generateManualExcelFile(generateDocumentActivity9.semester, GenerateDocumentActivity.this.department, GenerateDocumentActivity.this.f439y);
                        }
                    } else if (GenerateDocumentActivity.this.filetype.equals("Student List")) {
                        if (GenerateDocumentActivity.this.f439y == null) {
                            AlertDialog.Builder builder7 = new AlertDialog.Builder(GenerateDocumentActivity.this);
                            builder7.setTitle((CharSequence) "Alert");
                            builder7.setMessage((CharSequence) "Select Year");
                            builder7.setPositiveButton((CharSequence) "Ok", (DialogInterface.OnClickListener) null);
                            builder7.create();
                            if (GenerateDocumentActivity.this.getLifecycle().getCurrentState().isAtLeast(Lifecycle.State.CREATED)) {
                                builder7.show();
                            }
                        } else {
                            GenerateDocumentActivity generateDocumentActivity10 = GenerateDocumentActivity.this;
                            generateDocumentActivity10.generateStudentExcelFile(generateDocumentActivity10.semester, GenerateDocumentActivity.this.department, GenerateDocumentActivity.this.f439y);
                        }
                    }
                    GenerateDocumentActivity generateDocumentActivity11 = GenerateDocumentActivity.this;
                    generateDocumentActivity11.filetype = null;
                    generateDocumentActivity11.select_document.setSelection(0);
                    GenerateDocumentActivity.this.select_semester.setSelection(0);
                    GenerateDocumentActivity.this.select_year.setSelection(0);
                }
            }
        });
        this.select_year.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            public void onNothingSelected(AdapterView<?> adapterView) {
            }

            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long j) {
                GenerateDocumentActivity.this.f439y = adapterView.getItemAtPosition(i).toString().trim();
            }
        });
    }

    /* access modifiers changed from: private */
    public void generateEmptyStudentExcelFile(String str, String str2, String str3) {
        HSSFWorkbook hSSFWorkbook = new HSSFWorkbook();
        HSSFSheet createSheet = hSSFWorkbook.createSheet(str + " Students");
        HSSFRow createRow = createSheet.createRow(0);
        createRow.createCell(0);
        createRow.createCell(1);
        createRow.createCell(2);
        createRow.createCell(3);
        createRow.createCell(4);
        createRow.createCell(5);
        createSheet.setColumnWidth(0, XmlValidationError.LIST_INVALID);
        createSheet.setColumnWidth(1, 3500);
        createSheet.setColumnWidth(2, 5500);
        createSheet.setColumnWidth(3, 3500);
        createSheet.setColumnWidth(4, 3500);
        createSheet.setColumnWidth(5, 3500);
        createRow.getCell(0).setCellValue("Roll No.");
        createRow.getCell(1).setCellValue("Enrollment");
        createRow.getCell(2).setCellValue("Name");
        createRow.getCell(3).setCellValue("Email");
        createRow.getCell(4).setCellValue("Phone No.");
        createRow.getCell(5).setCellValue("Parents Phone No.");
        try {
            String str4 = Environment.getExternalStorageDirectory().getPath() + "/rait/Document";
            File file = new File(str4);
            if (!file.exists()) {
                file.mkdirs();
            }
            File file2 = new File(str4, str + " Students List " + str3 + ".xls");
            file2.createNewFile();
            FileOutputStream fileOutputStream = new FileOutputStream(file2);
            hSSFWorkbook.write((OutputStream) fileOutputStream);
            fileOutputStream.flush();
            fileOutputStream.close();
            Toast.makeText(this, "File Generated to rait Folder", Toast.LENGTH_SHORT).show();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e2) {
            e2.printStackTrace();
        }
    }

    /* access modifiers changed from: private */
    public void generateStudentExcelFile(final String str, String str2, final String str3) {
        final ProgressDialog progressDialog = new ProgressDialog(this);
        progressDialog.setCancelable(false);
        progressDialog.setMessage("Generating...");
        progressDialog.show();
        FirebaseDatabase instance = FirebaseDatabase.getInstance();
        instance.getReference(str2 + "/Students List/" + str + " " + str3).addListenerForSingleValueEvent(new ValueEventListener() {
            public void onCancelled(@NonNull DatabaseError databaseError) {
            }

            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.getChildrenCount() == 0) {
                    progressDialog.dismiss();
                    AlertDialog.Builder builder = new AlertDialog.Builder(GenerateDocumentActivity.this);
                    builder.setMessage((CharSequence) "Student List not found!");
                    builder.setTitle((CharSequence) "Alert");
                    builder.setPositiveButton((CharSequence) "Ok", (DialogInterface.OnClickListener) new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialogInterface, int i) {
                            GenerateDocumentActivity.this.onBackPressed();
                        }
                    });
                    builder.create();
                    if (GenerateDocumentActivity.this.getLifecycle().getCurrentState().isAtLeast(Lifecycle.State.CREATED)) {
                        builder.show();
                        return;
                    }
                    return;
                }
                Document document = (Document) dataSnapshot.getValue(Document.class);
                try {
                    String str = Environment.getExternalStorageDirectory().getPath() + "/rait/Document";
                    File file = new File(str);
                    if (!file.exists()) {
                        file.mkdirs();
                    }
                    File file2 = new File(str, str + " Students List " + str3 + ".xls");
                    file2.createNewFile();
                    FirebaseStorage.getInstance().getReferenceFromUrl(document.getUrl()).getFile(file2).addOnSuccessListener((OnSuccessListener) new OnSuccessListener<FileDownloadTask.TaskSnapshot>() {
                        public void onSuccess(FileDownloadTask.TaskSnapshot taskSnapshot) {
                            progressDialog.dismiss();
                            Toast.makeText(GenerateDocumentActivity.this, "File Generated to rait Folder", Toast.LENGTH_SHORT).show();
                        }
                    }).addOnFailureListener((OnFailureListener) new OnFailureListener() {
                        public void onFailure(@NonNull Exception exc) {
                            progressDialog.dismiss();
                            Toast.makeText(GenerateDocumentActivity.this, "File not Found!!!", Toast.LENGTH_SHORT).show();
                        }
                    });
                } catch (IOException e) {
                    progressDialog.dismiss();
                    e.printStackTrace();
                }
            }
        });
    }

    /* access modifiers changed from: private */
    public void generateManualExcelFile(final String str, String str2, final String str3) {
        final ProgressDialog progressDialog = new ProgressDialog(this);
        progressDialog.setCancelable(false);
        progressDialog.setMessage("Generating...");
        progressDialog.show();
        FirebaseDatabase instance = FirebaseDatabase.getInstance();
        instance.getReference(str2 + "/Manual Marks/" + str + " " + str3).addListenerForSingleValueEvent(new ValueEventListener() {
            public void onCancelled(@NonNull DatabaseError databaseError) {
            }

            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.getChildrenCount() == 0) {
                    progressDialog.dismiss();
                    AlertDialog.Builder builder = new AlertDialog.Builder(GenerateDocumentActivity.this);
                    builder.setMessage((CharSequence) "Manual marks sheet not found!");
                    builder.setTitle((CharSequence) "Alert");
                    builder.setPositiveButton((CharSequence) "Ok", (DialogInterface.OnClickListener) new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialogInterface, int i) {
                            GenerateDocumentActivity.this.onBackPressed();
                        }
                    });
                    builder.create();
                    if (GenerateDocumentActivity.this.getLifecycle().getCurrentState().isAtLeast(Lifecycle.State.CREATED)) {
                        builder.show();
                        return;
                    }
                    return;
                }
                Document document = (Document) dataSnapshot.getValue(Document.class);
                try {
                    String str = Environment.getExternalStorageDirectory().getPath() + "/rait/Document";
                    File file = new File(str);
                    if (!file.exists()) {
                        file.mkdirs();
                    }
                    File file2 = new File(str, str + " Manual Marks " + str3 + ".xls");
                    file2.createNewFile();
                    FirebaseStorage.getInstance().getReferenceFromUrl(document.getUrl()).getFile(file2).addOnSuccessListener((OnSuccessListener) new OnSuccessListener<FileDownloadTask.TaskSnapshot>() {
                        public void onSuccess(FileDownloadTask.TaskSnapshot taskSnapshot) {
                            progressDialog.dismiss();
                            Toast.makeText(GenerateDocumentActivity.this, "File Generated to rait Folder", Toast.LENGTH_SHORT).show();
                        }
                    }).addOnFailureListener((OnFailureListener) new OnFailureListener() {
                        public void onFailure(@NonNull Exception exc) {
                            progressDialog.dismiss();
                            Toast.makeText(GenerateDocumentActivity.this, "File not Found!!!", Toast.LENGTH_SHORT).show();
                        }
                    });
                } catch (IOException e) {
                    progressDialog.dismiss();
                    e.printStackTrace();
                }
            }
        });
    }

    /* access modifiers changed from: private */
    public void generateEmptyManualExcelFile(final String str, String str2, final String str3) {
        FirebaseDatabase instance = FirebaseDatabase.getInstance();
        DatabaseReference reference = instance.getReference(str2 + "/subjects/" + str);
        final HSSFWorkbook hSSFWorkbook = new HSSFWorkbook();
        reference.addListenerForSingleValueEvent(new ValueEventListener() {
            public void onCancelled(@NonNull DatabaseError databaseError) {
            }

            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.getChildrenCount() == 0) {
                    AlertDialog.Builder builder = new AlertDialog.Builder(GenerateDocumentActivity.this);
                    builder.setMessage((CharSequence) "Subjects not found!");
                    builder.setTitle((CharSequence) "Alert");
                    builder.setPositiveButton((CharSequence) "Ok", (DialogInterface.OnClickListener) new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialogInterface, int i) {
                            GenerateDocumentActivity.this.onBackPressed();
                        }
                    });
                    builder.create();
                    if (GenerateDocumentActivity.this.getLifecycle().getCurrentState().isAtLeast(Lifecycle.State.CREATED)) {
                        builder.show();
                        return;
                    }
                    return;
                }
                for (DataSnapshot value : dataSnapshot.getChildren()) {
                    Subject subject = (Subject) value.getValue(Subject.class);
                    if (subject.getName().contains("PA")) {
                        HSSFSheet createSheet = hSSFWorkbook.createSheet(subject.getName());
                        for (int i = 0; i < 3; i++) {
                            HSSFRow createRow = createSheet.createRow(i);
                            for (int i2 = 0; i2 < 38; i2++) {
                                createRow.createCell(i2);
                            }
                        }
                        int i3 = 2;
                        createSheet.addMergedRegion(new CellRangeAddress(0, 2, 0, 0));
                        createSheet.addMergedRegion(new CellRangeAddress(0, 2, 1, 1));
                        createSheet.addMergedRegion(new CellRangeAddress(0, 2, 2, 2));
                        createSheet.addMergedRegion(new CellRangeAddress(0, 2, 3, 3));
                        createSheet.addMergedRegion(new CellRangeAddress(0, 1, 4, 35));
                        createSheet.addMergedRegion(new CellRangeAddress(0, 2, 36, 36));
                        createSheet.addMergedRegion(new CellRangeAddress(0, 2, 37, 37));
                        createSheet.setColumnWidth(0, XmlValidationError.LIST_INVALID);
                        createSheet.setColumnWidth(1, 3500);
                        createSheet.setColumnWidth(2, 3000);
                        createSheet.setColumnWidth(3, 5500);
                        int i4 = 1;
                        int i5 = 4;
                        while (i5 < 36) {
                            createSheet.setColumnWidth(i5, 1000);
                            createSheet.getRow(i3).getCell(i5).setCellValue((double) i4);
                            i5++;
                            i4++;
                            i3 = 2;
                        }
                        createSheet.setColumnWidth(36, 3500);
                        createSheet.setColumnWidth(37, 3500);
                        HSSFRow row = createSheet.getRow(0);
                        row.getCell(0).setCellValue("Roll No.");
                        row.getCell(1).setCellValue("Enrollment No.");
                        row.getCell(2).setCellValue("Exam Seat No.");
                        row.getCell(3).setCellValue("Name");
                        row.getCell(4).setCellValue("Experiment / Assignment Marks");
                        row.getCell(36).setCellValue("Total Marks out of (10 X no of Experiments)");
                        row.getCell(37).setCellValue("PA marks of practical converted to T.E. scheme");
                    }
                }
                try {
                    String str = Environment.getExternalStorageDirectory().getPath() + "/rait/Document";
                    File file = new File(str);
                    if (!file.exists()) {
                        file.mkdirs();
                    }
                    File file2 = new File(str, str + " Manual Marks " + str3 + ".xls");
                    file2.createNewFile();
                    FileOutputStream fileOutputStream = new FileOutputStream(file2);
                    hSSFWorkbook.write((OutputStream) fileOutputStream);
                    fileOutputStream.flush();
                    fileOutputStream.close();
                    Toast.makeText(GenerateDocumentActivity.this, "File Generated to rait Folder", Toast.LENGTH_SHORT).show();
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                } catch (IOException e2) {
                    e2.printStackTrace();
                }
            }
        });
    }

    /* access modifiers changed from: private */
    public void generateUnitTestExcelFile(final String str, String str2, final String str3) {
        final ProgressDialog progressDialog = new ProgressDialog(this);
        progressDialog.setCancelable(false);
        progressDialog.setMessage("Generating...");
        progressDialog.show();
        FirebaseDatabase instance = FirebaseDatabase.getInstance();
        instance.getReference(str2 + "/Unit Test/" + str + " " + str3).addListenerForSingleValueEvent(new ValueEventListener() {
            public void onCancelled(@NonNull DatabaseError databaseError) {
            }

            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.getChildrenCount() == 0) {
                    progressDialog.dismiss();
                    AlertDialog.Builder builder = new AlertDialog.Builder(GenerateDocumentActivity.this);
                    builder.setMessage((CharSequence) "Unit Test marks sheet not found!");
                    builder.setTitle((CharSequence) "Alert");
                    builder.setPositiveButton((CharSequence) "Ok", (DialogInterface.OnClickListener) new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialogInterface, int i) {
                            GenerateDocumentActivity.this.onBackPressed();
                        }
                    });
                    builder.create();
                    if (GenerateDocumentActivity.this.getLifecycle().getCurrentState().isAtLeast(Lifecycle.State.CREATED)) {
                        builder.show();
                        return;
                    }
                    return;
                }
                Document document = (Document) dataSnapshot.getValue(Document.class);
                try {
                    String str = Environment.getExternalStorageDirectory().getPath() + "/rait/Document";
                    File file = new File(str);
                    if (!file.exists()) {
                        file.mkdirs();
                    }
                    File file2 = new File(str, str + " Unit Test " + str3 + ".xls");
                    file2.createNewFile();
                    FirebaseStorage.getInstance().getReferenceFromUrl(document.getUrl()).getFile(file2).addOnSuccessListener((OnSuccessListener) new OnSuccessListener<FileDownloadTask.TaskSnapshot>() {
                        public void onSuccess(FileDownloadTask.TaskSnapshot taskSnapshot) {
                            progressDialog.dismiss();
                            Toast.makeText(GenerateDocumentActivity.this, "File Generated to rait Folder", Toast.LENGTH_SHORT).show();
                        }
                    }).addOnFailureListener((OnFailureListener) new OnFailureListener() {
                        public void onFailure(@NonNull Exception exc) {
                            progressDialog.dismiss();
                            Toast.makeText(GenerateDocumentActivity.this, "File not Found!!!", Toast.LENGTH_SHORT).show();
                        }
                    });
                } catch (IOException e) {
                    progressDialog.dismiss();
                    e.printStackTrace();
                }
            }
        });
    }

    /* access modifiers changed from: private */
    public void generateEmptyUnitTestExcelFile(final String str, String str2, final String str3) {
        FirebaseDatabase instance = FirebaseDatabase.getInstance();
        DatabaseReference reference = instance.getReference(str2 + "/subjects/" + str);
        final HSSFWorkbook hSSFWorkbook = new HSSFWorkbook();
        reference.addListenerForSingleValueEvent(new ValueEventListener() {
            public void onCancelled(@NonNull DatabaseError databaseError) {
            }

            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.getChildrenCount() == 0) {
                    AlertDialog.Builder builder = new AlertDialog.Builder(GenerateDocumentActivity.this);
                    builder.setMessage((CharSequence) "Subjects not found!");
                    builder.setTitle((CharSequence) "Alert");
                    builder.setPositiveButton((CharSequence) "Ok", (DialogInterface.OnClickListener) new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialogInterface, int i) {
                            GenerateDocumentActivity.this.onBackPressed();
                        }
                    });
                    builder.create();
                    if (GenerateDocumentActivity.this.getLifecycle().getCurrentState().isAtLeast(Lifecycle.State.CREATED)) {
                        builder.show();
                        return;
                    }
                    return;
                }
                for (DataSnapshot value : dataSnapshot.getChildren()) {
                    Subject subject = (Subject) value.getValue(Subject.class);
                    if (subject.getName().contains("TH")) {
                        HSSFSheet createSheet = hSSFWorkbook.createSheet(subject.getName());
                        for (int i = 0; i < 3; i++) {
                            HSSFRow createRow = createSheet.createRow(i);
                            for (int i2 = 0; i2 < 10; i2++) {
                                createRow.createCell(i2);
                            }
                        }
                        createSheet.addMergedRegion(new CellRangeAddress(0, 2, 0, 0));
                        createSheet.addMergedRegion(new CellRangeAddress(0, 2, 1, 1));
                        createSheet.addMergedRegion(new CellRangeAddress(0, 0, 2, 5));
                        createSheet.addMergedRegion(new CellRangeAddress(0, 0, 7, 8));
                        createSheet.addMergedRegion(new CellRangeAddress(1, 2, 2, 2));
                        createSheet.addMergedRegion(new CellRangeAddress(1, 2, 3, 3));
                        createSheet.addMergedRegion(new CellRangeAddress(1, 2, 4, 4));
                        createSheet.addMergedRegion(new CellRangeAddress(1, 2, 5, 5));
                        createSheet.addMergedRegion(new CellRangeAddress(1, 2, 6, 6));
                        createSheet.addMergedRegion(new CellRangeAddress(1, 2, 7, 7));
                        createSheet.addMergedRegion(new CellRangeAddress(1, 2, 8, 8));
                        createSheet.addMergedRegion(new CellRangeAddress(1, 2, 9, 9));
                        createSheet.setColumnWidth(0, XmlValidationError.LIST_INVALID);
                        createSheet.setColumnWidth(1, 5500);
                        createSheet.setColumnWidth(2, 3500);
                        createSheet.setColumnWidth(3, 3500);
                        createSheet.setColumnWidth(4, 2500);
                        createSheet.setColumnWidth(5, 2500);
                        createSheet.setColumnWidth(6, 2500);
                        createSheet.setColumnWidth(7, 2500);
                        createSheet.setColumnWidth(8, 2500);
                        createSheet.setColumnWidth(9, 2500);
                        HSSFRow row = createSheet.getRow(0);
                        row.getCell(0).setCellValue("Roll No.");
                        row.getCell(1).setCellValue("Name");
                        row.getCell(2).setCellValue("Course Code and Name");
                        row.getCell(6).setCellValue("(MAX 20)");
                        row.getCell(7).setCellValue("Micro Project (Out of 10)");
                        row.getCell(9).setCellValue("Out of 30");
                        HSSFRow row2 = createSheet.getRow(1);
                        row2.getCell(2).setCellValue("Enrollment");
                        row2.getCell(3).setCellValue("Exam Seat No.");
                        row2.getCell(4).setCellValue("UT 1");
                        row2.getCell(5).setCellValue("UT 2");
                        row2.getCell(6).setCellValue("UT Average");
                        row2.getCell(7).setCellValue("Performance in Group Activity (Out of 6)");
                        row2.getCell(8).setCellValue("Individual Performance in Oral/Presentation (Out of 4)");
                        row2.getCell(9).setCellValue("Total");
                    }
                }
                try {
                    String str = Environment.getExternalStorageDirectory().getPath() + "/rait/Document";
                    File file = new File(str);
                    if (!file.exists()) {
                        file.mkdirs();
                    }
                    File file2 = new File(str, str + " Unit Test " + str3 + ".xls");
                    file2.createNewFile();
                    FileOutputStream fileOutputStream = new FileOutputStream(file2);
                    hSSFWorkbook.write((OutputStream) fileOutputStream);
                    fileOutputStream.flush();
                    fileOutputStream.close();
                    Toast.makeText(GenerateDocumentActivity.this, "File Generated to rait Folder", Toast.LENGTH_SHORT).show();
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                } catch (IOException e2) {
                    e2.printStackTrace();
                }
            }
        });
    }

    /* access modifiers changed from: private */
    public void generatePracticalAttedenceExcelFile(final String str, String str2, final String str3) {
        final ProgressDialog progressDialog = new ProgressDialog(this);
        progressDialog.setCancelable(false);
        progressDialog.setMessage("Generating...");
        progressDialog.show();
        FirebaseDatabase instance = FirebaseDatabase.getInstance();
        instance.getReference(str2 + "/Practical Attendence/" + str + " " + str3).addListenerForSingleValueEvent(new ValueEventListener() {
            public void onCancelled(@NonNull DatabaseError databaseError) {
            }

            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.getChildrenCount() == 0) {
                    progressDialog.dismiss();
                    AlertDialog.Builder builder = new AlertDialog.Builder(GenerateDocumentActivity.this);
                    builder.setMessage((CharSequence) "Practical Attendence sheet not found!");
                    builder.setTitle((CharSequence) "Alert");
                    builder.setPositiveButton((CharSequence) "Ok", (DialogInterface.OnClickListener) new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialogInterface, int i) {
                            GenerateDocumentActivity.this.onBackPressed();
                        }
                    });
                    builder.create();
                    if (GenerateDocumentActivity.this.getLifecycle().getCurrentState().isAtLeast(Lifecycle.State.CREATED)) {
                        builder.show();
                        return;
                    }
                    return;
                }
                Document document = (Document) dataSnapshot.getValue(Document.class);
                try {
                    String str = Environment.getExternalStorageDirectory().getPath() + "/rait/Document";
                    File file = new File(str);
                    if (!file.exists()) {
                        file.mkdirs();
                    }
                    File file2 = new File(str, str + " Practical Attendence " + str3 + ".xls");
                    file2.createNewFile();
                    FirebaseStorage.getInstance().getReferenceFromUrl(document.getUrl()).getFile(file2).addOnSuccessListener((OnSuccessListener) new OnSuccessListener<FileDownloadTask.TaskSnapshot>() {
                        public void onSuccess(FileDownloadTask.TaskSnapshot taskSnapshot) {
                            progressDialog.dismiss();
                            Toast.makeText(GenerateDocumentActivity.this, "File Generated to rait Folder", Toast.LENGTH_SHORT).show();
                        }
                    }).addOnFailureListener((OnFailureListener) new OnFailureListener() {
                        public void onFailure(@NonNull Exception exc) {
                            progressDialog.dismiss();
                            Toast.makeText(GenerateDocumentActivity.this, "File not Found!!!", Toast.LENGTH_SHORT).show();
                        }
                    });
                } catch (IOException e) {
                    progressDialog.dismiss();
                    e.printStackTrace();
                }
            }
        });
    }

    /* access modifiers changed from: private */
    public void generateEmptyPracticalAttedenceExcelFile(String str, String str2, String str3, String str4) {
        FirebaseDatabase instance = FirebaseDatabase.getInstance();
        DatabaseReference reference = instance.getReference(str2 + "/subjects/" + str);
        final HSSFWorkbook hSSFWorkbook = new HSSFWorkbook();
        final String str5 = str3;
        final String str6 = str;
        final String str7 = str4;
        reference.addListenerForSingleValueEvent(new ValueEventListener() {
            public void onCancelled(@NonNull DatabaseError databaseError) {
            }

            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.getChildrenCount() == 0) {
                    AlertDialog.Builder builder = new AlertDialog.Builder(GenerateDocumentActivity.this);
                    builder.setMessage((CharSequence) "Subjects not found!");
                    builder.setTitle((CharSequence) "Alert");
                    builder.setPositiveButton((CharSequence) "Ok", (DialogInterface.OnClickListener) new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialogInterface, int i) {
                            GenerateDocumentActivity.this.onBackPressed();
                        }
                    });
                    builder.create();
                    if (GenerateDocumentActivity.this.getLifecycle().getCurrentState().isAtLeast(Lifecycle.State.CREATED)) {
                        builder.show();
                        return;
                    }
                    return;
                }
                for (DataSnapshot value : dataSnapshot.getChildren()) {
                    Subject subject = (Subject) value.getValue(Subject.class);
                    if (subject.getName().contains("PA") || subject.getName().contains("TU")) {
                        for (int i = 0; i < 3; i++) {
                            String str = null;
                            if (i == 0) {
                                str = str5 + "1";
                            } else if (i == 1) {
                                str = str5 + ExifInterface.GPS_MEASUREMENT_2D;
                            } else if (i == 2) {
                                str = str5 + ExifInterface.GPS_MEASUREMENT_3D;
                            }
                            HSSFSheet createSheet = hSSFWorkbook.createSheet(str + " " + subject.getName());
                            HSSFRow createRow = createSheet.createRow(1);
                            HSSFRow createRow2 = createSheet.createRow(0);
                            createRow.createCell(0);
                            createRow.createCell(1);
                            createRow.createCell(2);
                            createRow2.createCell(0);
                            createRow2.createCell(1);
                            createRow2.createCell(2);
                            createSheet.addMergedRegion(new CellRangeAddress(0, 1, 0, 0));
                            createSheet.addMergedRegion(new CellRangeAddress(0, 1, 1, 1));
                            createSheet.addMergedRegion(new CellRangeAddress(0, 1, 2, 2));
                            createSheet.setColumnWidth(0, XmlValidationError.LIST_INVALID);
                            createSheet.setColumnWidth(1, 3500);
                            createSheet.setColumnWidth(2, 5500);
                            createRow2.getCell(0).setCellValue("Roll No.");
                            createRow2.getCell(1).setCellValue("Enrollment");
                            createRow2.getCell(2).setCellValue("Name");
                        }
                    }
                }
                try {
                    String str2 = Environment.getExternalStorageDirectory().getPath() + "/rait/Document";
                    File file = new File(str2);
                    if (!file.exists()) {
                        file.mkdirs();
                    }
                    File file2 = new File(str2, str6 + " Practical Attendence " + str7 + ".xls");
                    file2.createNewFile();
                    FileOutputStream fileOutputStream = new FileOutputStream(file2);
                    hSSFWorkbook.write((OutputStream) fileOutputStream);
                    fileOutputStream.flush();
                    fileOutputStream.close();
                    Toast.makeText(GenerateDocumentActivity.this, "File Generated to rait Folder", Toast.LENGTH_SHORT).show();
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                } catch (IOException e2) {
                    e2.printStackTrace();
                }
            }
        });
    }

    /* access modifiers changed from: private */
    public void generateTheoryAttedenceExcelFile(final String str, String str2, final String str3) {
        final ProgressDialog progressDialog = new ProgressDialog(this);
        progressDialog.setCancelable(false);
        progressDialog.setMessage("Generating...");
        progressDialog.show();
        FirebaseDatabase instance = FirebaseDatabase.getInstance();
        instance.getReference(str2 + "/Theory Attendence/" + str + " " + str3).addListenerForSingleValueEvent(new ValueEventListener() {
            public void onCancelled(@NonNull DatabaseError databaseError) {
            }

            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.getChildrenCount() == 0) {
                    progressDialog.dismiss();
                    AlertDialog.Builder builder = new AlertDialog.Builder(GenerateDocumentActivity.this);
                    builder.setMessage((CharSequence) "Theory Attendance Sheet not found!");
                    builder.setTitle((CharSequence) "Alert");
                    builder.setPositiveButton((CharSequence) "Ok", (DialogInterface.OnClickListener) new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialogInterface, int i) {
                            GenerateDocumentActivity.this.onBackPressed();
                        }
                    });
                    builder.create();
                    if (GenerateDocumentActivity.this.getLifecycle().getCurrentState().isAtLeast(Lifecycle.State.CREATED)) {
                        builder.show();
                        return;
                    }
                    return;
                }
                Document document = (Document) dataSnapshot.getValue(Document.class);
                try {
                    String str = Environment.getExternalStorageDirectory().getPath() + "/rait/Document";
                    File file = new File(str);
                    if (!file.exists()) {
                        file.mkdirs();
                    }
                    File file2 = new File(str, str + " Theory Attendence " + str3 + ".xls");
                    file2.createNewFile();
                    FirebaseStorage.getInstance().getReferenceFromUrl(document.getUrl()).getFile(file2).addOnSuccessListener((OnSuccessListener) new OnSuccessListener<FileDownloadTask.TaskSnapshot>() {
                        public void onSuccess(FileDownloadTask.TaskSnapshot taskSnapshot) {
                            progressDialog.dismiss();
                            Toast.makeText(GenerateDocumentActivity.this, "File Generated to rait Folder", Toast.LENGTH_SHORT).show();
                        }
                    }).addOnFailureListener((OnFailureListener) new OnFailureListener() {
                        public void onFailure(@NonNull Exception exc) {
                            progressDialog.dismiss();
                            Toast.makeText(GenerateDocumentActivity.this, "File not Found!!!", Toast.LENGTH_SHORT).show();
                        }
                    });
                } catch (IOException e) {
                    progressDialog.dismiss();
                    e.printStackTrace();
                }
            }
        });
    }

    /* access modifiers changed from: private */
    public void generateEmptyTheoryAttedenceExcelFile(final String str, String str2, final String str3) {
        FirebaseDatabase instance = FirebaseDatabase.getInstance();
        DatabaseReference reference = instance.getReference(str2 + "/subjects/" + str);
        final HSSFWorkbook hSSFWorkbook = new HSSFWorkbook();
        reference.addListenerForSingleValueEvent(new ValueEventListener() {
            public void onCancelled(@NonNull DatabaseError databaseError) {
            }

            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.getChildrenCount() == 0) {
                    AlertDialog.Builder builder = new AlertDialog.Builder(GenerateDocumentActivity.this);
                    builder.setMessage((CharSequence) "Subjects not found!");
                    builder.setTitle((CharSequence) "Alert");
                    builder.setPositiveButton((CharSequence) "Ok", (DialogInterface.OnClickListener) new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialogInterface, int i) {
                            GenerateDocumentActivity.this.onBackPressed();
                        }
                    });
                    builder.create();
                    if (GenerateDocumentActivity.this.getLifecycle().getCurrentState().isAtLeast(Lifecycle.State.CREATED)) {
                        builder.show();
                        return;
                    }
                    return;
                }
                for (DataSnapshot value : dataSnapshot.getChildren()) {
                    Subject subject = (Subject) value.getValue(Subject.class);
                    if (subject.getName().contains("TH")) {
                        HSSFSheet createSheet = hSSFWorkbook.createSheet(subject.getName());
                        HSSFRow createRow = createSheet.createRow(1);
                        HSSFRow createRow2 = createSheet.createRow(0);
                        createRow.createCell(0);
                        createRow.createCell(1);
                        createRow.createCell(2);
                        createRow2.createCell(0);
                        createRow2.createCell(1);
                        createRow2.createCell(2);
                        createSheet.addMergedRegion(new CellRangeAddress(0, 1, 0, 0));
                        createSheet.addMergedRegion(new CellRangeAddress(0, 1, 1, 1));
                        createSheet.addMergedRegion(new CellRangeAddress(0, 1, 2, 2));
                        createSheet.setColumnWidth(0, XmlValidationError.LIST_INVALID);
                        createSheet.setColumnWidth(1, 3500);
                        createSheet.setColumnWidth(2, 5500);
                        createRow2.getCell(0).setCellValue("Roll No.");
                        createRow2.getCell(1).setCellValue("Enrollment");
                        createRow2.getCell(2).setCellValue("Name");
                    }
                }
                try {
                    String str = Environment.getExternalStorageDirectory().getPath() + "/rait/Document";
                    File file = new File(str);
                    if (!file.exists()) {
                        file.mkdirs();
                    }
                    File file2 = new File(str, str + " Theory Attendence " + str3 + ".xls");
                    file2.createNewFile();
                    FileOutputStream fileOutputStream = new FileOutputStream(file2);
                    hSSFWorkbook.write((OutputStream) fileOutputStream);
                    fileOutputStream.flush();
                    fileOutputStream.close();
                    Toast.makeText(GenerateDocumentActivity.this, "File Generated to rait Folder", Toast.LENGTH_SHORT).show();
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
