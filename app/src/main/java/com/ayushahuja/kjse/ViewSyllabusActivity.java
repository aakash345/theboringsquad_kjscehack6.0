package com.ayushahuja.kjse;

import android.os.Bundle;
import android.widget.FrameLayout;

import androidx.appcompat.app.AppCompatActivity;

import com.ayushahuja.kjse.userSyllabusFrag.pdfListFragment;

public class ViewSyllabusActivity extends AppCompatActivity {

    private FrameLayout pdfListContainer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_syllabus);

        pdfListContainer = findViewById(R.id.pdfListContainer);

        getSupportFragmentManager().beginTransaction().replace(R.id.pdfListContainer,new pdfListFragment()).commit();

    }
}