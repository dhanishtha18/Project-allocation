package org.projectapp.Adapter;

import android.content.Context;

import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.appcompat.widget.AppCompatButton;
import androidx.appcompat.widget.AppCompatTextView;
import androidx.recyclerview.widget.RecyclerView;

import org.projectapp.Model.Student;
import org.projectapp.R;
import org.projectapp.ViewManualMarksActivity;

import java.io.File;

import java.util.ArrayList;
import java.util.List;

public class ManualAdapter extends RecyclerView.Adapter<ManualAdapter.ViewHolder> {
    private Context context;
    private List<Student> uploads;
    private File file;
    private ArrayList<String> attendence=new ArrayList<>();
    private String subject,semester,year,department,url,path;

    public ManualAdapter(Context context,String path, List<Student> uploads, File file,String subject,String semester,String year,String department,String url) {
        this.uploads = uploads;
        this.context = context;
        this.path = path;
        this.file = file;
        this.subject = subject;
        this.semester = semester;
        this.year = year;
        this.department = department;
        this.url = url;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.layout_manual, parent, false);
        ViewHolder viewHolder = new ViewHolder(v);
        return viewHolder;
    }


    public void onBindViewHolder(final ViewHolder holder, final int position) {
        final Student upload = uploads.get(position);


        holder.name.setText("Name: "+upload.getName());
        holder.roll.setText("Roll No.: "+upload.getRoll_no());
        holder.enroll.setText("Enrollment: "+upload.getEnrollment());


        holder.view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                context.startActivity(new Intent(context, ViewManualMarksActivity.class)
                .putExtra("subject",subject)
                .putExtra("enrollment",upload.getEnrollment())
                .putExtra("semester",semester)
                .putExtra("department",department)
                .putExtra("name",upload.getName())
                .putExtra("loginas","higher"));
            }
        });

    }

    @Override
    public int getItemCount() {
        return uploads.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {

        AppCompatTextView name,roll,enroll;
        AppCompatButton view;
        public ViewHolder(View itemView) {
            super(itemView);

            name = itemView.findViewById(R.id.name);
            roll =  itemView.findViewById(R.id.roll);
            enroll = itemView.findViewById(R.id.enroll);
            view = itemView.findViewById(R.id.view);
        }
    }

}
