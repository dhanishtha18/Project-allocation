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

import org.projectapp.Adapter.PrincipalChatAdapter;
import org.projectapp.Model.Chatlist;
import org.projectapp.Model.Hod;
import org.projectapp.R;

import java.util.ArrayList;
import java.util.List;


public class PrincipalChatFragment extends Fragment {
    private RecyclerView recyclerView;

    private PrincipalChatAdapter userAdapter;
    private List<Hod> mUsers;

    FirebaseUser fuser;
    DatabaseReference reference;

    private List<Chatlist> usersList;
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view=inflater.inflate(R.layout.fragment_principal_chat, container, false);
        recyclerView=view.findViewById(R.id.recycler_view);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        fuser= FirebaseAuth.getInstance().getCurrentUser();

        usersList=new ArrayList<>();

        reference= FirebaseDatabase.getInstance().getReference("PrincipalHodChatlist").child(fuser.getUid());
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
        reference =FirebaseDatabase.getInstance().getReference("HODs");
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                mUsers.clear();
                for (DataSnapshot snapshot:dataSnapshot.getChildren()){
                    for (DataSnapshot snapshot1:snapshot.getChildren()) {
                        if (snapshot1.getChildrenCount()!=0) {
                            Hod user = snapshot1.getValue(Hod.class);
                            for (Chatlist chatlist : usersList) {
                                Boolean isShowing = true;
                                if (user.getId() != null && user.getId().equals(chatlist.getId())) {
                                    if (mUsers.size() != 0) {
                                        for (Hod hod : mUsers) {
                                            if (user.getId().equals(hod.getId())) {
                                                isShowing = false;
                                            }
                                        }

                                    }
                                    if (isShowing) {
                                        mUsers.add(user);
                                    }
                                }
                            }
                        }
                    }
                }
                if (getLifecycle().getCurrentState().isAtLeast(Lifecycle.State.INITIALIZED)) {
                    userAdapter = new PrincipalChatAdapter(getContext(), mUsers, true);
                    recyclerView.setAdapter(userAdapter);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }


}
