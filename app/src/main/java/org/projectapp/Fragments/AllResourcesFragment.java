package org.projectapp.Fragments;

import android.content.Context;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.AppCompatSpinner;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Lifecycle;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.projectapp.Adapter.StudyResourceAdapter;
import org.projectapp.Model.StudyResources;
import org.projectapp.Model.Subject;
import org.projectapp.R;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class AllResourcesFragment extends Fragment {
    String department,subject,semester,loginas;
    Context context;
ArrayList<String> subjects;
String[] semArray;
DatabaseReference databaseReference;
    AppCompatSpinner resource_semester,resource_subject;
    RecyclerView resource_list;
    RecyclerView.Adapter adapter;
    List<StudyResources> uploads;
    LinearLayoutManager layoutManager;

    public AllResourcesFragment(String department,Context context,String loginas) {
        this.department = department;
        this.context = context;
        this.loginas = loginas;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view=inflater.inflate(R.layout.fragment_all_resources, container, false);

        resource_list=view.findViewById(R.id.resources_list);
        resource_semester=view.findViewById(R.id.resource_semester);
        resource_subject=view.findViewById(R.id.resource_subject);

        layoutManager=new LinearLayoutManager(context);
        layoutManager.setReverseLayout(true);
        resource_list.setHasFixedSize(true);
        resource_list.setLayoutManager(layoutManager);
        uploads = new ArrayList<>();
        subjects=new ArrayList<>();

         Calendar cal=Calendar.getInstance();
        if (department.equals("Computer Department")){
            if (cal.get(Calendar.MONTH)<5 || cal.get(Calendar.MONTH)==11){
                semArray= new String[]{"Select Semester","CO2I", "CO4I", "CO6I"};
            }else{
                semArray= new String[]{"Select Semester","CO1I", "CO3I", "CO5I"};
            }
        }else if (department.equals("IT Department")){
            if (cal.get(Calendar.MONTH)<5 || cal.get(Calendar.MONTH)==11){
                semArray= new String[]{"Select Semester","IF2I", "IF4I", "IF6I"};
            }else{
                semArray= new String[]{"Select Semester","IF1I", "IF3I", "IF5I"};
            }
        }else if (department.equals("Civil I Shift Department")){
            if (cal.get(Calendar.MONTH)<5 || cal.get(Calendar.MONTH)==11){
                semArray= new String[]{"Select Semester","CE2I", "CE4I", "CE6I"};
            }else{
                semArray= new String[]{"Select Semester","CE1I", "CE3I", "CE5I"};
            }
        }else if (department.equals("Civil II Shift Department")){
            if (cal.get(Calendar.MONTH)<5 || cal.get(Calendar.MONTH)==11){
                semArray= new String[]{"Select Semester","CE2I", "CE4I", "CE6I"};
            }else{
                semArray= new String[]{"Select Semester","CE1I", "CE3I", "CE5I"};
            }
        }else if (department.equals("Mechanical I Shift Department")){
            if (cal.get(Calendar.MONTH)<5 || cal.get(Calendar.MONTH)==11){
                semArray= new String[]{"Select Semester","ME2I", "ME4I", "ME6I"};
            }else{
                semArray= new String[]{"Select Semester","ME1I", "ME3I", "ME5I"};
            }
        }else if (department.equals("Mechanical II Shift Department")){
            if (cal.get(Calendar.MONTH)<5 || cal.get(Calendar.MONTH)==11){
                semArray= new String[]{"Select Semester","ME2I", "ME4I", "ME6I"};
            }else{
                semArray= new String[]{"Select Semester","ME1I", "ME3I", "ME5I"};
            }
        }else if (department.equals("Chemical Department")){
            if (cal.get(Calendar.MONTH)<5 || cal.get(Calendar.MONTH)==11){
                semArray= new String[]{"Select Semester","CH2I", "CH4I", "CH6I"};
            }else{
                semArray= new String[]{"Select Semester","CH1I", "CH3I", "CH5I"};
            }
        }else if (department.equals("TR Department")){
            if (cal.get(Calendar.MONTH)<5 || cal.get(Calendar.MONTH)==11){
                semArray= new String[]{"Select Semester","TR2G", "TR4G","TR5G"};
            }else{
                semArray= new String[]{"Select Semester","TR1G", "TR3G", "TR5G"};
            }
        }
        resource_semester.setAdapter(new ArrayAdapter<String>(context, R.layout.support_simple_spinner_dropdown_item,semArray));

        resource_semester.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (parent.getItemAtPosition(position).toString().equals("Select Semester")){
                    semester=null;
                }else{
                    semester=parent.getItemAtPosition(position).toString().trim();
                    FirebaseDatabase.getInstance().getReference(department+"/subjects/"+semester)
                            .addListenerForSingleValueEvent(new ValueEventListener() {
                                @Override
                                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                    for (DataSnapshot snapshot:dataSnapshot.getChildren()){
                                        Subject subject =snapshot.getValue(Subject.class);
                                        subjects.add(subject.getName());
                                    }
                                    resource_subject.setAdapter(new ArrayAdapter<String>(context,R.layout.spinner_dialog,subjects));
                                }

                                @Override
                                public void onCancelled(@NonNull DatabaseError databaseError) {

                                }
                            });
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        resource_subject.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                subject=parent.getItemAtPosition(position).toString();
                databaseReference = FirebaseDatabase.getInstance().getReference(department+"/Notes/"+semester+"/"+subject);
                databaseReference.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        uploads.clear();
                        for (DataSnapshot snapshot:dataSnapshot.getChildren()){
                            StudyResources studyResources=snapshot.getValue(StudyResources.class);
                            uploads.add(studyResources);
                        }
                        if (getLifecycle().getCurrentState().isAtLeast(Lifecycle.State.INITIALIZED)) {
                            adapter = new StudyResourceAdapter(context, uploads, loginas, department + "/Notes/" + semester + "/" + subject, "all");
                            resource_list.setAdapter(adapter);
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        return view;
    }
}
