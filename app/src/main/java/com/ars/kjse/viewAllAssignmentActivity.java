package com.ars.kjse;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.FrameLayout;

public class viewAllAssignmentActivity extends AppCompatActivity {
    private FrameLayout pdfListContainer;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_all_assignment);

        pdfListContainer = findViewById(R.id.pdfListContainer);

        getSupportFragmentManager().beginTransaction().replace(R.id.pdfListContainer,new asslistfrag()).commit();
    }
}