package org.projectapp.Adapter;

import android.content.Context;
import android.content.DialogInterface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.widget.AppCompatEditText;
import androidx.appcompat.widget.AppCompatImageButton;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import org.projectapp.Model.Subject;
import org.projectapp.R;

import java.util.List;

public class SubjectsAdapter extends RecyclerView.Adapter<SubjectsAdapter.ViewHolder> {

    private Context context;
    private List<Subject> uploads;
    private String department,semester;
private String type;

    public SubjectsAdapter(Context context, List<Subject> uploads,String department,String semester) {
        this.uploads = uploads;
        this.context = context;
        this.department = department;
        this.semester = semester;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.layout_view_subjects, parent, false);
        ViewHolder viewHolder = new ViewHolder(v);
        return viewHolder;
    }


    public void onBindViewHolder(final ViewHolder holder, final int position) {
        final Subject upload = uploads.get(position);


        holder.edit_subject.setText(upload.getName());
        /*holder.edit_mode.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (holder.edit_subject.isEnabled()){
                    if (holder.edit_subject.getText().toString().trim().equals("")){
                        AlertDialog.Builder builder = new AlertDialog.Builder(context);
                        builder.setTitle("Alert");
                        builder.setMessage("Enter Subject Name");
                        builder.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                            }
                        });
                        builder.create();
                        if (context!=null) {
                            builder.show();
                        }
                    }else if(holder.edit_code.getText().toString().trim().equals("")){
                        AlertDialog.Builder builder = new AlertDialog.Builder(context);
                        builder.setTitle("Alert");
                        builder.setMessage("Enter Subject Code");
                        builder.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                            }
                        });
                        builder.create();
                        if (context!=null) {
                            builder.show();
                        }
                    }else if(type==null){
                        AlertDialog.Builder builder = new AlertDialog.Builder(context);
                        builder.setTitle("Alert");
                        builder.setMessage("Select Subject type");
                        builder.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                            }
                        });
                        builder.create();
                        if (context!=null) {
                            builder.show();
                        }
                    }else {
                        DatabaseReference reference = FirebaseDatabase.getInstance().getReference(department + "/subjects/" + semester);
                        HashMap<String, Object> hashMap = new HashMap<>();
                        hashMap.put("name", holder.edit_subject.getText().toString().trim() + " (" + holder.edit_code.getText().toString().trim() + ") " + type);
                        reference.child(upload.getId()).updateChildren(hashMap).addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                if (task.isSuccessful()) {
                                    Toast.makeText(context, "Subject updated", Toast.LENGTH_SHORT).show();
                                    holder.edit_mode.setImageResource(R.drawable.edit);
                                    holder.edit_subject.setEnabled(false);
                                    holder.edit_type.setVisibility(View.GONE);
                                    holder.edit_code.setVisibility(View.GONE);
                                } else {
                                    Toast.makeText(context, "Subject Updation Failed", Toast.LENGTH_SHORT).show();
                                    holder.edit_mode.setImageResource(R.drawable.edit);
                                    holder.edit_subject.setEnabled(false);
                                    holder.edit_type.setVisibility(View.GONE);
                                    holder.edit_code.setVisibility(View.GONE);
                                }
                            }
                        });
                    }
                }else{
                    holder.edit_subject.setEnabled(true);
                    holder.edit_type.setVisibility(View.VISIBLE);
                    holder.edit_code.setVisibility(View.VISIBLE);
                    holder.edit_mode.setImageResource(R.drawable.done);
                    holder.edit_type.setAdapter(new ArrayAdapter<String>(context, R.layout.spinner_dialog,new String[]{"Type","TH","PA","TU"}));
                String[] sub=upload.getName().split(" ");
                holder.edit_subject.setText(sub[0]);
                String[] code=sub[1].split("");
                *//*for (int i=0;i<sub[1].length();i++){
                    code[i]=sub[1].charAt(i);
                }*//*
                String c="";
                for (int j=2;j<code.length-1;j++){
                    c+=code[j];
                }
                holder.edit_code.setText(c);
                if (sub[2].equals("TH")) {
                    holder.edit_type.setSelection(1);
                }else if (sub[2].equals("PA")) {
                    holder.edit_type.setSelection(2);
                }else if (sub[2].equals("TU")) {
                    holder.edit_type.setSelection(3);
                }else{
                    holder.edit_type.setSelection(0);
                }
                }
            }
        });*/


        holder.delete_subject.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder alert=new AlertDialog.Builder(context);
                alert.setTitle("Alert");
                alert.setMessage("Do you really wnat to delete "+upload.getName()+"?");
                alert.setPositiveButton("Confirm", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        DatabaseReference reference= FirebaseDatabase.getInstance().getReference(department+"/subjects/"+semester);
                        reference.child(upload.getId()).removeValue().addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                if (task.isSuccessful()){
                                    Toast.makeText(context, "Subject Deleted", Toast.LENGTH_SHORT).show();
                                }else{
                                    Toast.makeText(context, "Subject Deletion Failed", Toast.LENGTH_SHORT).show();
                                }
                            }
                        });
                    }
                });
                alert.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                });
                alert.create();
                if (context!=null) {
                    alert.show();
                }
            }
        });

        /*holder.edit_type.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if(parent.getItemAtPosition(position).equals("Select Type")){
                    type=null;
                }else {
                    type=parent.getItemAtPosition(position).toString();
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });*/

    }

    @Override
    public int getItemCount() {
        return uploads.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {

        AppCompatImageButton /*edit_mode,*/delete_subject;
        AppCompatEditText edit_subject,edit_code;
        //AppCompatSpinner edit_type;

        public ViewHolder(View itemView) {
            super(itemView);

            //edit_mode = itemView.findViewById(R.id.edit_mode);
            delete_subject =  itemView.findViewById(R.id.delete_subject);
            edit_subject = itemView.findViewById(R.id.edit_subject);
            //edit_code = itemView.findViewById(R.id.edit_code);
            //edit_type = itemView.findViewById(R.id.edit_type);
        }
    }
}
