package org.projectapp.Adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.appcompat.widget.AppCompatTextView;
import androidx.recyclerview.widget.RecyclerView;

import org.projectapp.Model.Student;
import org.projectapp.R;
import org.projectapp.ViewSingleStudentAttendenceActivity;

import java.util.ArrayList;
import java.util.List;

import az.plainpie.PieView;

public class ViewAttendenceAdapter extends RecyclerView.Adapter<ViewAttendenceAdapter.ViewHolder> {
    private Context context;
    private List<Student> uploads;
    private ArrayList<Integer> absent, present, total;
    private View v;
    private String semester, department, subject, url, path;

    public ViewAttendenceAdapter(Context context, String url, String path, List<Student> uploads, ArrayList<Integer> total, ArrayList<Integer> present, ArrayList<Integer> absent, String semester, String department, String subject) {
        this.uploads = uploads;
        this.context = context;
        this.absent = absent;
        this.present = present;
        this.total = total;
        this.semester = semester;
        this.department = department;
        this.subject = subject;
        this.url = url;
        this.path = path;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.layout_view_attendence, parent, false);
        ViewHolder viewHolder = new ViewHolder(v);
        return viewHolder;
    }


    public void onBindViewHolder(final ViewHolder holder, final int position) {
        final Student upload = uploads.get(position);

            holder.name.setText("Name: " + upload.getName());
            holder.roll.setText("Roll No.: " + upload.getRoll_no());
            holder.enroll.setText("Enrollment: " + upload.getEnrollment());
            holder.present.setText("Present: " + (present.get(position) * 100) / total.get(position) + "%");
            holder.absent.setText("Absent: " + (absent.get(position) * 100) / total.get(position) + "%");
            holder.pie.setPercentage((present.get(position) * 100) / total.get(position));
            holder.pie.setPercentageBackgroundColor(R.color.present);
            if (((present.get(position) * 100) / total.get(position)) < 75) {
                holder.status.setText("Status: DETAIN");
                holder.status.setCompoundDrawablesWithIntrinsicBounds(R.drawable.absent, 0, 0, 0);
            } else {
                holder.status.setText("Status: IN");
                holder.status.setCompoundDrawablesWithIntrinsicBounds(R.drawable.present, 0, 0, 0);
            }


        v.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                context.startActivity(new Intent(context, ViewSingleStudentAttendenceActivity.class)
                        .putExtra("department", department)
                        .putExtra("subject", subject)
                        .putExtra("enrollment", upload.getEnrollment())
                        .putExtra("name", upload.getName())
                        .putExtra("url", url)
                        .putExtra("path", path)
                        .putExtra("loginas", "notStudent")
                        .putExtra("semester", semester));
            }
        });

    }

    @Override
    public int getItemCount() {
        return uploads.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {

        AppCompatTextView name, roll, enroll, present, absent, status;
        PieView pie;

        public ViewHolder(View itemView) {
            super(itemView);

            name = itemView.findViewById(R.id.student_name);
            roll = itemView.findViewById(R.id.roll);
            enroll = itemView.findViewById(R.id.enroll);
            present = itemView.findViewById(R.id.present);
            absent = itemView.findViewById(R.id.absent);
            status = itemView.findViewById(R.id.status);
            pie = itemView.findViewById(R.id.pieView);

        }
    }

}
