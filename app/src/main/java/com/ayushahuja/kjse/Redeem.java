package com.ayushahuja.kjse;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.sql.SQLOutput;
import java.util.ArrayList;

public class Redeem extends AppCompatActivity {

    private RecyclerView courseRV;
    private TextView current_points;
    FirebaseAuth mAuth;
    FirebaseFirestore fst;

    // Arraylist for storing data
    private ArrayList<CourseModel> courseModelArrayList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_redeem);
        courseRV = findViewById(R.id.idRVCourse);
        current_points = findViewById(R.id.current_points);
        mAuth = FirebaseAuth.getInstance();
        fst = FirebaseFirestore.getInstance();

        // here we have created new array list and added data to it.
        courseModelArrayList = new ArrayList<>();
        courseModelArrayList.add(new CourseModel("boAt Rockerz 550", "BLUETOOTH HEADPHONES", R.drawable.headphone,1199));
        courseModelArrayList.add(new CourseModel("DIZO Watch 2", "Smart Watch", R.drawable.watch,1499));
        courseModelArrayList.add(new CourseModel("OPPO Enco Air 2", "Earbuds", R.drawable.tws,1299));

        // we are initializing our adapter class and passing our arraylist to it.
        CourseAdapter courseAdapter = new CourseAdapter(this, courseModelArrayList);

        // below line is for setting a layout manager for our recycler view.
        // here we are creating vertical list so we will provide orientation as vertical
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);

        // in below two lines we are setting layoutmanager and adapter to our recycler view.
        courseRV.setLayoutManager(linearLayoutManager);
        courseRV.setAdapter(courseAdapter);

        DocumentReference data = fst.collection("Users").document(mAuth.getCurrentUser().getUid());
        data.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                if(documentSnapshot.exists()){
                    String pts = (documentSnapshot.getString("points"));
                    current_points.setText("Remaining points: "+pts);
                }
                else Toast.makeText(Redeem.this, "User data not found", Toast.LENGTH_SHORT).show();
            }
        });
    }
}