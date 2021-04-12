package org.projectapp.Fragments;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Lifecycle;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.projectapp.Adapter.HodChatAdapter;
import org.projectapp.Model.Chatlist;
import org.projectapp.Model.Staff;
import org.projectapp.R;

import java.util.ArrayList;
import java.util.List;


public class HodChatFragment extends Fragment {
    private RecyclerView recyclerView;

    private HodChatAdapter userAdapter;
    private List<Staff> mUsers;

    FirebaseUser fuser;
    DatabaseReference reference;

    private List<Chatlist> usersList;

    String department;
    public HodChatFragment(String department) {
        this.department=department;
    }

    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view=inflater.inflate(R.layout.fragment_hod_chat, container, false);
        recyclerView=view.findViewById(R.id.recycler_view);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        fuser= FirebaseAuth.getInstance().getCurrentUser();

        usersList=new ArrayList<>();

        reference= FirebaseDatabase.getInstance().getReference("HodChatlist").child(fuser.getUid());
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                usersList.clear();
                for (DataSnapshot snapshot:dataSnapshot.getChildren()){
                    Chatlist chatlist=snapshot.getValue(Chatlist.class);
                    usersList.add(chatlist);
                }

                chatList();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
        return  view;
    }

    private void chatList() {
        mUsers=new ArrayList<>();
        mUsers=new ArrayList<>();
        reference =FirebaseDatabase.getInstance().getReference("Staff/"+department);
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                mUsers.clear();
                for (DataSnapshot snapshot:dataSnapshot.getChildren()){
if (snapshot.getChildrenCount()!=0) {
    Staff user = snapshot.getValue(Staff.class);
    for (Chatlist chatlist : usersList) {
        if (user.getId() != null && user.getId().equals(chatlist.getId())) {
            mUsers.add(user);
        }
    }
}

                }
                if (getLifecycle().getCurrentState().isAtLeast(Lifecycle.State.INITIALIZED)) {
                    userAdapter = new HodChatAdapter(getContext(), mUsers, true);
                    recyclerView.setAdapter(userAdapter);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }


}
