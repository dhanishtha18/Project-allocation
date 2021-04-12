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

import org.projectapp.Adapter.HodChatAdapter;
import org.projectapp.Model.Staff;
import org.projectapp.R;

import java.util.ArrayList;
import java.util.List;


public class HodFragment extends Fragment {

    private RecyclerView recyclerView;

    private HodChatAdapter userAdapter;
    private List<Staff> mUsers;
    AppCompatEditText search_users;

    String department;
    public HodFragment(String department) {
        this.department=department;
    }

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

    public void searchUsers(final String s){
        final FirebaseUser fuser= FirebaseAuth.getInstance().getCurrentUser();

        mUsers.clear();

            Query query = FirebaseDatabase.getInstance().getReference("Staff").child(department).orderByChild("search")
                    .startAt(s)
                    .endAt(s + "\uf8ff");

            query.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                        Staff user = snapshot.getValue(Staff.class);

                        assert user != null;
                        assert fuser != null;
                        if (!user.getId().equals(fuser.getUid())) {
                            mUsers.add(user);
                        }
                    }
                    if (getLifecycle().getCurrentState().isAtLeast(Lifecycle.State.INITIALIZED)) {
                        userAdapter = new HodChatAdapter(getContext(), mUsers, false);
                        recyclerView.setAdapter(userAdapter);
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            });
    }

    private void readUsers() {

        final FirebaseUser firebaseUser= FirebaseAuth.getInstance().getCurrentUser();
        DatabaseReference reference= FirebaseDatabase.getInstance().getReference("Staff").child(department);

        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (search_users.getText().toString().equals("")) {
                    mUsers.clear();
                    for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                        if (snapshot.getChildrenCount()!=0) {
                            Staff user = snapshot.getValue(Staff.class);

                            assert user != null;
                            assert firebaseUser != null;
                            if (user.getId() != null && !user.getId().equals(firebaseUser.getUid())) {
                                mUsers.add(user);
                            }
                        }

                    }
                    if (getLifecycle().getCurrentState().isAtLeast(Lifecycle.State.INITIALIZED)) {
                        userAdapter = new HodChatAdapter(getContext(), mUsers, false);
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
