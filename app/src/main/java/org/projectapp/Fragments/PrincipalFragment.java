package org.projectapp.Fragments;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.AppCompatEditText;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Lifecycle;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import org.projectapp.Adapter.PrincipalChatAdapter;
import org.projectapp.Model.Hod;
import org.projectapp.R;

import java.util.ArrayList;
import java.util.List;


public class PrincipalFragment extends Fragment {

    private RecyclerView recyclerView;

    private PrincipalChatAdapter userAdapter;
    private List<Hod> mUsers;
    AppCompatEditText search_users;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view=inflater.inflate(R.layout.fragment_principal, container, false);
        recyclerView=view.findViewById(R.id.recycler_view);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        search_users=view.findViewById(R.id.search_users);
        mUsers=new ArrayList<>();

        readUsers();

        search_users=view.findViewById(R.id.search_users);
        search_users.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                searchUsers(s.toString().toLowerCase());
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        return view;
    }

    public void searchUsers(String s){
        final FirebaseUser fuser= FirebaseAuth.getInstance().getCurrentUser();

        final String[] branch_list = {"Select Department", "Computer Department", "IT Department", "Mechanical I Shift Department","Mechanical II Shift Department", "Civil I Shift Department","Civil II Shift Department", "TR Department", "Chemical Department"};
        mUsers.clear();

        for (String data:branch_list) {
            Query query = FirebaseDatabase.getInstance().getReference("HODs").child(data).orderByChild("search")
                    .startAt(s)
                    .endAt(s + "\uf8ff");

            query.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                        Hod user = snapshot.getValue(Hod.class);

                        assert user != null;
                        assert fuser != null;
                        boolean isShowing = true;
                        if (!user.getId().equals(fuser.getUid())) {
                            if (mUsers.size()!=0){
                                for (Hod hod:mUsers){
                                    if (user.getId().equals(hod.getId())){
                                        isShowing=false;
                                    }
                                }
                            }
                            if (isShowing) {
                                mUsers.add(user);
                            }
                        }
                    }
                    if (getLifecycle().getCurrentState().isAtLeast(Lifecycle.State.INITIALIZED)) {
                        userAdapter = new PrincipalChatAdapter(getContext(), mUsers, false);
                        recyclerView.setAdapter(userAdapter);
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            });
        }
    }

    private void readUsers() {

        final FirebaseUser firebaseUser= FirebaseAuth.getInstance().getCurrentUser();
        DatabaseReference reference= FirebaseDatabase.getInstance().getReference("HODs");

        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (search_users.getText().toString().equals("")) {
                    mUsers.clear();
                    for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                        for (DataSnapshot snapshot1:snapshot.getChildren()) {
                            if (snapshot1.getChildrenCount()!=0) {
                                Hod user = snapshot1.getValue(Hod.class);

                                boolean isShowing = true;
                                assert user != null;
                                assert firebaseUser != null;
                                if (user.getId() != null && !user.getId().equals(firebaseUser.getUid())) {
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
                    if (getLifecycle().getCurrentState().isAtLeast(Lifecycle.State.INITIALIZED)) {
                        userAdapter = new PrincipalChatAdapter(getContext(), mUsers, false);
                        recyclerView.setAdapter(userAdapter);
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }


}
