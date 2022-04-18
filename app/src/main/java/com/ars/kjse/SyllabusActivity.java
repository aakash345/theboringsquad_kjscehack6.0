package com.ars.kjse;

import android.os.Bundle;
import android.widget.FrameLayout;

import androidx.appcompat.app.AppCompatActivity;

import com.ars.kjse.syllabusFrag.AddSyllabusFragment;

public class SyllabusActivity extends AppCompatActivity {

    FrameLayout frameContainer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_syllabus);

        frameContainer = findViewById(R.id.frameContainer);

        getSupportFragmentManager().beginTransaction().replace(R.id.frameContainer,new AddSyllabusFragment()).commit();


    }
}