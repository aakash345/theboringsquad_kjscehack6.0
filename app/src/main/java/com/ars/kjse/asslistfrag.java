package com.ars.kjse;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link asslistfrag#newInstance} factory method to
 * create an instance of this fragment.
 */
public class asslistfrag extends Fragment {
    DatabaseReference reference,dbRef;
    FirebaseAuth mAuth;
    FirebaseFirestore fst;
    String usersDept,year,div,roll;
    List<AssignmentModel> list1;
    AssSubmissionAdapter adapter;
    RecyclerView fileListRecView;
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public asslistfrag() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment asslistfrag.
     */
    // TODO: Rename and change types and number of parameters
    public static asslistfrag newInstance(String param1, String param2) {
        asslistfrag fragment = new asslistfrag();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view =  inflater.inflate(R.layout.fragment_asslistfrag, container, false);
        reference = FirebaseDatabase.getInstance().getReference();
        mAuth = FirebaseAuth.getInstance();
        fst = FirebaseFirestore.getInstance();

        fileListRecView = view.findViewById(R.id.fileListRecView);

        DocumentReference data = fst.collection("Users").document(mAuth.getCurrentUser().getUid());
        data.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                if(documentSnapshot.exists()){
                    usersDept = documentSnapshot.getString("Dept");
                    year = documentSnapshot.getString("Year");
                    div = documentSnapshot.getString("Div");
                    roll = documentSnapshot.getString("Roll_Num");
                    Log.i("ayushhh","ayushhh");
                    Log.i("usersDept",usersDept);

                    if (usersDept == "COMP") {
                        usersDept = "Computer Engineering";
                    }
                }
                else Toast.makeText(getContext(), "User data not found", Toast.LENGTH_SHORT).show();
            }
        });
        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            public void run() {
                // yourMethod();
                showAss(usersDept,year,div,roll);
            }
        }, 5000);
        return view;
    }

    private void showAss(String usersDept, String year, String div, String roll) {
        String ash= year+"_"+usersDept+"_"+div;
        dbRef = reference.child("assignement").child(ash);
        dbRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                list1 = new ArrayList<AssignmentModel>();
                if (dataSnapshot.exists()){
                    for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                        AssignmentModel data = snapshot.getValue(AssignmentModel.class);
                        list1.add(data);
                    }
                    fileListRecView.setHasFixedSize(true);

                    fileListRecView.setLayoutManager(new LinearLayoutManager(getContext()));

                    adapter = new AssSubmissionAdapter(list1);
                    fileListRecView.setAdapter(adapter);
                } else {
                    Toast.makeText(getContext(), "No data exist", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(getContext(), error.getMessage(), Toast.LENGTH_LONG).show();
            }
        });
        Toast.makeText(getContext(), ""+dbRef, Toast.LENGTH_SHORT).show();
    }


}
