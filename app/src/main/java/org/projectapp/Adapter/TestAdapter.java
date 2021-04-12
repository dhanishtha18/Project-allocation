package org.projectapp.Adapter;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.net.Uri;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.widget.AppCompatButton;
import androidx.appcompat.widget.AppCompatEditText;
import androidx.appcompat.widget.AppCompatTextView;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.projectapp.Model.Document;
import org.projectapp.Model.Test;
import org.projectapp.R;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.List;

public class TestAdapter extends RecyclerView.Adapter<TestAdapter.ViewHolder> {
    private Context context;
    private List<Test> uploads;
    private File file;
    private String subject,url,path;
    RecyclerView.Adapter adapter;
    RecyclerView student_list;
private  float ut1,ut2,ind,grp;
    public TestAdapter(RecyclerView student_list,Context context, List<Test> uploads, File file, String subject, String url, String path) {
        this.uploads = uploads;
        this.context = context;
        this.file = file;
        this.subject = subject;
        this.url = url;
        this.path = path;
        this.student_list = student_list;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.layout_test_marks, parent, false);
        ViewHolder viewHolder = new ViewHolder(v);
        return viewHolder;
    }


    public void onBindViewHolder(final ViewHolder holder, final int position) {
        final Test upload = uploads.get(position);

        holder.name.setText("Name: "+upload.getName());
        holder.roll.setText("Roll No.: "+upload.getRoll_no());
        holder.enroll.setText("Enrollment: "+upload.getEnrollment());
        holder.seat.setText("Seat No.: "+upload.getSeat_no());
        holder.ut1.setText("UT 1: "+upload.getUt1());
        holder.ut2.setText("UT 2: "+upload.getUt2());
        holder.ut_avg.setText("UT Avg: "+upload.getUt_avg());
        holder.group.setText("Group: "+upload.getGroup());
        holder.individual.setText("Individual: "+upload.getIndividual());
        holder.total.setText("Total (Out of 30): "+upload.getTotal());
        holder.micro.setText("Micro-Project: "+(Float.parseFloat(upload.getGroup())+Float.parseFloat(upload.getIndividual())));

        holder.edit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (holder.edit_seat.getVisibility()==View.VISIBLE){
                    holder.edit.setCompoundDrawablesRelativeWithIntrinsicBounds(R.drawable.edit,0,0,0);
                    holder.edit.setText("Edit");
                    if (holder.edit_seat.getText().toString().trim().equals("")){
                        AlertDialog.Builder builder = new AlertDialog.Builder(context);
                        builder.setTitle("Alert");
                        builder.setMessage("Enter Seat No.");
                        builder.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                            }
                        });
                        builder.create();
                        if (context!=null) {
                            builder.show();
                        }
                    }else if (holder.edit_ut1.getText().toString().trim().equals("")){
                        AlertDialog.Builder builder = new AlertDialog.Builder(context);
                        builder.setTitle("Alert");
                        builder.setMessage("Enter U.T.1 Marks");
                        builder.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                            }
                        });
                        builder.create();
                        if (context!=null) {
                            builder.show();
                        }
                    }else if (holder.edit_ut2.getText().toString().trim().equals("")){
                        AlertDialog.Builder builder = new AlertDialog.Builder(context);
                        builder.setTitle("Alert");
                        builder.setMessage("Enter U.T.2 Marks");
                        builder.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                            }
                        });
                        builder.create();
                        if (context!=null) {
                            builder.show();
                        }
                    }else if (holder.edit_group.getText().toString().trim().equals("")){
                        AlertDialog.Builder builder = new AlertDialog.Builder(context);
                        builder.setTitle("Alert");
                        builder.setMessage("Enter Group Marks.");
                        builder.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                            }
                        });
                        builder.create();
                        if (context!=null) {
                            builder.show();
                        }
                    }else if (holder.edit_individual.getText().toString().trim().equals("")){
                        AlertDialog.Builder builder = new AlertDialog.Builder(context);
                        builder.setTitle("Alert");
                        builder.setMessage("Enter Individual Marks");
                        builder.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                            }
                        });
                        builder.create();
                        if (context!=null) {
                            builder.show();
                        }
                    }else{
                        final ProgressDialog dialog=new ProgressDialog(context);
                        dialog.setMessage("Updating Student marks...");
                        dialog.setCancelable(false);
                        dialog.show();
                        try{
                            HSSFWorkbook workbook=new HSSFWorkbook(new FileInputStream(file));
                            HSSFSheet sheet=workbook.getSheet(subject);
                            for (int i=3;i<=sheet.getLastRowNum();i++){
                                HSSFRow row=sheet.getRow(i);
                                if (row.getCell(1).toString().equals(upload.getName()) && String.format("%.0f",(double)row.getCell(2).getNumericCellValue()).equals(upload.getEnrollment())){
                                    row.createCell(3).setCellValue(holder.edit_seat.getText().toString().trim());
                                    row.createCell(4).setCellValue(holder.edit_ut1.getText().toString().trim());
                                    row.createCell(5).setCellValue(holder.edit_ut2.getText().toString().trim());
                                    ut1=Float.parseFloat(holder.edit_ut1.getText().toString().trim());
                                    ut2=Float.parseFloat(holder.edit_ut2.getText().toString().trim());
                                    row.createCell(6).setCellValue(Math.ceil((ut1+ut2)/2));
                                    row.createCell(7).setCellValue(holder.edit_group.getText().toString().trim());
                                    row.createCell(8).setCellValue(holder.edit_individual.getText().toString().trim());
                                    ind=Float.parseFloat(holder.edit_individual.getText().toString().trim());
                                    grp=Float.parseFloat(holder.edit_group.getText().toString().trim());
                                    int tot=(int)(Math.ceil((ut1+ut2)/2)+(Math.ceil(ind+grp)));
                                    row.createCell(9).setCellValue(String.valueOf(tot));
                                    FileOutputStream stream=new FileOutputStream(file);
                                    workbook.write(stream);
                                    stream.flush();
                                    stream.close();
                                    break;
                                }
                            }

                            final StorageReference reference=FirebaseStorage.getInstance().getReference(path);
                            reference.putStream(new FileInputStream(file))
                                    .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                                        @Override
                                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                                            reference.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                                                @Override
                                                public void onSuccess(Uri downloadUrl) {
                                                    DatabaseReference mDatabase = FirebaseDatabase.getInstance().getReference(path);
                                                    mDatabase.setValue(new Document(downloadUrl.toString()));
                                                    float individual=Float.parseFloat(holder.edit_individual.getText().toString().trim());
                                                    float group=Float.parseFloat(holder.edit_group.getText().toString().trim());
                                                    Test test=new Test(upload.getRoll_no(), upload.getName(), upload.getEnrollment(), holder.edit_seat.getText().toString().trim(), holder.edit_ut1.getText().toString().trim(), holder.edit_ut2.getText().toString().trim(), String.valueOf(Math.ceil((ut1+ut2)/2)), holder.edit_group.getText().toString().trim(), holder.edit_individual.getText().toString().trim(), String.valueOf(Math.ceil((individual+group)+((ut1+ut2)/2))));
                                                    uploads.set(position,test);
                                                    dialog.dismiss();
                                                    adapter = new TestAdapter(student_list, context,uploads, file, subject, url, path);
                                                    student_list.setAdapter(adapter);
                                                }
                                            });
                                            Toast.makeText(context, "Marks Updated ", Toast.LENGTH_LONG).show();
                                        }
                                    });
                        } catch (FileNotFoundException e) {
                            dialog.dismiss();
                            e.printStackTrace();
                        } catch (IOException e) {
                            dialog.dismiss();
                            e.printStackTrace();
                        }
                        holder.edit_seat.setVisibility(View.GONE);
                        holder.edit_ut1.setVisibility(View.GONE);
                        holder.edit_ut2.setVisibility(View.GONE);
                        holder.edit_group.setVisibility(View.GONE);
                        holder.edit_individual.setVisibility(View.GONE);

                    }
                }else{
                    holder.edit_seat.setVisibility(View.VISIBLE);
                    holder.edit_ut1.setVisibility(View.VISIBLE);
                    holder.edit_ut2.setVisibility(View.VISIBLE);
                    holder.edit_group.setVisibility(View.VISIBLE);
                    holder.edit_individual.setVisibility(View.VISIBLE);

                    holder.edit_seat.setText(upload.getSeat_no());
                    holder.edit_ut1.setText(upload.getUt1());
                    holder.edit_ut2.setText(upload.getUt2());
                    holder.edit_group.setText(upload.getGroup());
                    holder.edit_individual.setText(upload.getIndividual());

                    holder.edit.setCompoundDrawablesRelativeWithIntrinsicBounds(R.drawable.done,0,0,0);
                    holder.edit.setText("Done");
                }
            }
        });



    }

    @Override
    public int getItemCount() {
        return uploads.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {

        AppCompatTextView name,roll,enroll,seat,ut1,ut2,ut_avg,group,individual,total,micro;
        AppCompatEditText edit_seat,edit_ut1,edit_ut2,edit_group,edit_individual;
        AppCompatButton edit;
        public ViewHolder(View itemView) {
            super(itemView);

            name = itemView.findViewById(R.id.name);
            roll =  itemView.findViewById(R.id.roll);
            enroll = itemView.findViewById(R.id.enroll);
            seat = itemView.findViewById(R.id.seat);
            ut1 = itemView.findViewById(R.id.ut1);
            ut2 = itemView.findViewById(R.id.ut2);
            ut_avg = itemView.findViewById(R.id.avg);
            group = itemView.findViewById(R.id.group);
            individual = itemView.findViewById(R.id.individual);
            total = itemView.findViewById(R.id.total);
            micro = itemView.findViewById(R.id.micro);
            edit_seat = itemView.findViewById(R.id.edit_seat);
            edit_ut1 = itemView.findViewById(R.id.edit_ut1);
            edit_ut2 = itemView.findViewById(R.id.edit_ut2);
            edit_group = itemView.findViewById(R.id.edit_group);
            edit_individual = itemView.findViewById(R.id.edit_individual);
            edit = itemView.findViewById(R.id.edit);
        }
    }

}
