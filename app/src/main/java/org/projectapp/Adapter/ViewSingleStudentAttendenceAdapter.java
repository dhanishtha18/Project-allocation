package org.projectapp.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.appcompat.widget.AppCompatTextView;
import androidx.recyclerview.widget.RecyclerView;

import org.projectapp.R;

import java.util.ArrayList;

import az.plainpie.PieView;

public class ViewSingleStudentAttendenceAdapter extends RecyclerView.Adapter<ViewSingleStudentAttendenceAdapter.ViewHolder> {
    private Context context;
    private ArrayList<Integer> absent,present,total;
    private ArrayList<String> month;

    public ViewSingleStudentAttendenceAdapter(Context context,ArrayList<Integer> present,ArrayList<Integer> absent,ArrayList<Integer> total,ArrayList<String> month) {
        this.month = month;
        this.context = context;
        this.absent = absent;
        this.present = present;
        this.total = total;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.layout_view_single_student_attendence, parent, false);
        ViewHolder viewHolder = new ViewHolder(v);
        return viewHolder;
    }


    public void onBindViewHolder(final ViewHolder holder, final int position) {


        if (total.get(position)==0){
            holder.month.setText(month.get(position));
            holder.totalday.setText("Total Days: " +0);
            holder.presentday.setText("Present Days: " + 0);
            holder.absentday.setText("Absent Days: " + 0);
            holder.present.setText("Present: " + 0+ "%");
            holder.absent.setText("Absent: " + 0 + "%");
            holder.pie.setPercentage(100);
            holder.pie.setPercentageBackgroundColor(R.color.present);
        }else {
            holder.month.setText(month.get(position));
            holder.totalday.setText("Total Days: " + total.get(position));
            holder.presentday.setText("Present Days: " + present.get(position));
            holder.absentday.setText("Absent Days: " + absent.get(position));
            holder.present.setText("Present: " + (present.get(position) * 100) / total.get(position) + "%");
            holder.absent.setText("Absent: " + (absent.get(position) * 100) / total.get(position) + "%");
            holder.pie.setPercentage((present.get(position) * 100) / total.get(position));
            holder.pie.setPercentageBackgroundColor(R.color.present);
        }

        if (holder.pie.getPercentage()<75){
            holder.status.setText("Status: DETAIN");
            holder.status.setCompoundDrawablesWithIntrinsicBounds(R.drawable.absent,0,0,0);
        }else{
            holder.status.setText("Status: IN");
            holder.status.setCompoundDrawablesWithIntrinsicBounds(R.drawable.present,0,0,0);
        }
    }

    @Override
    public int getItemCount() {
        return month.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {

        AppCompatTextView present,absent,status,totalday,presentday,absentday,month;
        PieView pie;

        public ViewHolder(View itemView) {
            super(itemView);

            totalday = itemView.findViewById(R.id.totalday);
            presentday =  itemView.findViewById(R.id.presentday);
            absentday = itemView.findViewById(R.id.absentday);
            month = itemView.findViewById(R.id.month_name);
            present = itemView.findViewById(R.id.present);
            absent = itemView.findViewById(R.id.absent);
            status = itemView.findViewById(R.id.status);
            pie = itemView.findViewById(R.id.pieView);

        }
    }

}
