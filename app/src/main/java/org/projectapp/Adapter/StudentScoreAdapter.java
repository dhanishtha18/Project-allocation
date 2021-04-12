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
import org.projectapp.ViewStudentProfileActivity;

import java.util.List;

public class StudentScoreAdapter extends RecyclerView.Adapter<StudentScoreAdapter.ViewHolder> {

    private Context context;
    private List<Student> uploads;


    public StudentScoreAdapter(Context context, List<Student> uploads) {
        this.uploads = uploads;
        this.context = context;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.layout_unactive, parent, false);
        ViewHolder viewHolder = new ViewHolder(v);
        return viewHolder;
    }


    public void onBindViewHolder(ViewHolder holder, final int position) {
        final Student upload = uploads.get(position);

        holder.viewh.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                context.startActivity(new Intent(context, ViewStudentProfileActivity.class)
                        .putExtra("department", upload.getDepartment())
                        .putExtra("id", upload.getId())
                        .putExtra("semester", upload.getSemester())
                        .putExtra("loginas", "guest"));
            }
        });

        holder.delete.setVisibility(View.GONE);
        holder.dept.setVisibility(View.GONE);

        holder.hod_name.setText("Name: "+upload.getName());
        holder.hod_roll.setText("Roll No.: "+upload.getRoll_no());
        holder.hod_enrollment.setText("Enrollment No.: "+upload.getEnrollment());
    }

    @Override
    public int getItemCount() {
        return uploads.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {

        public AppCompatTextView hod_name, hod_enrollment,hod_roll,dept;
        public AppCompatButton viewh,delete;

        public ViewHolder(View itemView) {
            super(itemView);

            viewh = itemView.findViewById(R.id.view);
            delete = itemView.findViewById(R.id.delete);
            hod_name = itemView.findViewById(R.id.hod_name);
            hod_enrollment = itemView.findViewById(R.id.hod_password);
            hod_roll = itemView.findViewById(R.id.hod_login_id);
            dept = itemView.findViewById(R.id.hod_department);
        }
    }
}
