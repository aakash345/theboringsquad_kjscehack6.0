package com.ayushahuja.kjse;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.card.MaterialCardView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.messaging.FirebaseMessaging;

public class AdminDashboard extends AppCompatActivity {

    FirebaseAuth mAuth;
    FirebaseFirestore fst;
    TextView logouttext;
    MaterialCardView lgt, addSyllabus, prevQuest,addtt;
    MaterialCardView pushNoti,addAssignment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_dashboard);

        mAuth = FirebaseAuth.getInstance();
        fst = FirebaseFirestore.getInstance();
        lgt = findViewById(R.id.Lgt);
        addSyllabus = findViewById(R.id.addSyllabus);
        prevQuest = findViewById(R.id.prevQuest);
        pushNoti = findViewById(R.id.pushNoti);
        addtt = findViewById(R.id.addtt);
        addAssignment = findViewById(R.id.addAssignment);

        lgt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FirebaseMessaging.getInstance().unsubscribeFromTopic("admins");
                FirebaseAuth.getInstance().signOut();
                Toast.makeText(AdminDashboard.this, "Logged Out Successfully", Toast.LENGTH_SHORT).show();
                startActivity(new Intent(AdminDashboard.this, MainActivity.class));
                finish();
            }
        });

        addSyllabus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(AdminDashboard.this,SyllabusActivity.class));
            }
        });

        pushNoti.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(AdminDashboard.this, PushNotifications.class));
            }
        });
        addAssignment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(AdminDashboard.this,AssignmentActivity.class));
            }
        });
        prevQuest.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(AdminDashboard.this,PreviosQuestPaperActivity.class));
        }
        });
        addtt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(AdminDashboard.this,AddttActivity.class));
            }
        });

    }
}
