package com.ars.kjse;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.Manifest;
import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.format.DateFormat;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TimePicker;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.karumi.dexter.Dexter;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.PermissionDeniedResponse;
import com.karumi.dexter.listener.PermissionGrantedResponse;
import com.karumi.dexter.listener.PermissionRequest;
import com.karumi.dexter.listener.single.PermissionListener;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;

public class AssignmentActivity extends AppCompatActivity implements DatePickerDialog.OnDateSetListener, TimePickerDialog.OnTimeSetListener {

    StorageReference storageReference;
    DatabaseReference databaseReference;
    DatabaseReference databaseReference2;
    FirebaseAuth mAuth;
    FirebaseFirestore fst;

    int day, month, year_clock, hour, minute;
    int myday, myMonth, myYear, myHour, myMinute;

    EditText Tttitle,TtSubject;
    ImageView Ttbrowsepdf , Ttshowpdf, Ttcancelpdf;
    Spinner addttyearpinnerr,addttdeptspinner,addttdivspinner;
    Button addttsubmit,btnPick;
    Uri filepath;
    ArrayList<String> al = new ArrayList<String>();
    String title,year,dept,div,subject;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_assignment);
        fst = FirebaseFirestore.getInstance();
        mAuth = FirebaseAuth.getInstance();
        storageReference = FirebaseStorage.getInstance().getReference();

        databaseReference = FirebaseDatabase.getInstance().getReference("assignement");
        databaseReference2 = FirebaseDatabase.getInstance().getReference("ToDo");


        Tttitle = findViewById(R.id.TtTitle);
        TtSubject = findViewById(R.id.TtSubject);
        Ttbrowsepdf  = findViewById(R.id.TtBrowsePdf);
        Ttshowpdf  = findViewById(R.id.TtShowPdf);
        Ttcancelpdf = findViewById(R.id.TtCancel);
        addttyearpinnerr  = findViewById(R.id.addttyearpinnerr);
        addttdeptspinner = findViewById(R.id.addttdeptspinner);
        addttdivspinner = findViewById(R.id.addttdivspinner);
        addttsubmit = findViewById(R.id.addttsubmit);
        btnPick = findViewById(R.id.btnPick);

        Ttbrowsepdf.setVisibility(View.VISIBLE);
        Ttcancelpdf.setVisibility(View.GONE);
        Ttshowpdf.setVisibility(View.GONE);

        Ttcancelpdf.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Ttshowpdf.setVisibility(View.GONE);
                Ttcancelpdf.setVisibility(View.GONE);
                Ttbrowsepdf.setVisibility(View.VISIBLE);
            }

        });

        Ttbrowsepdf.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Dexter.withActivity(AssignmentActivity.this)
                        .withPermission(Manifest.permission.READ_EXTERNAL_STORAGE)
                        .withListener(new PermissionListener() {
                            @Override
                            public void onPermissionGranted(PermissionGrantedResponse response) {
                                Intent intent = new Intent();
                                intent.setType("application/pdf");
                                Log.i("Ayush","PDF liya");
                                intent.setAction(Intent.ACTION_GET_CONTENT);
                                startActivityForResult(Intent.createChooser(intent,"Select pdf files"),101);
                            }

                            @Override
                            public void onPermissionDenied(PermissionDeniedResponse response) { }

                            @Override
                            public void onPermissionRationaleShouldBeShown(PermissionRequest permission, PermissionToken token) {
                                token.continuePermissionRequest();
                            }
                        }).check();

            }
        });

        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(getApplicationContext(), R.array.year, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        addttyearpinnerr.setAdapter(adapter);

        ArrayAdapter<CharSequence> semAdapter = ArrayAdapter.createFromResource(getApplicationContext(), R.array.dept, android.R.layout.simple_spinner_item);
        semAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        addttdeptspinner.setAdapter(semAdapter);


        ArrayAdapter<CharSequence> divAdapter = ArrayAdapter.createFromResource(getApplicationContext(), R.array.div, android.R.layout.simple_spinner_item);
        semAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        addttdivspinner.setAdapter(divAdapter);


        btnPick.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Calendar calendar = Calendar.getInstance();
                year_clock = calendar.get(Calendar.YEAR);
                month = calendar.get(Calendar.MONTH);
                day = calendar.get(Calendar.DAY_OF_MONTH);
                DatePickerDialog datePickerDialog = new DatePickerDialog(AssignmentActivity.this, AssignmentActivity.this,year_clock, month,day);
                datePickerDialog.show();
                Log.i("Bhetl",""+year_clock);
                Log.i("Bhetl",""+month);
                Log.i("Bhetl",""+day);




            }
        });

        addttsubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                subject = TtSubject.getText().toString();
                title = Tttitle.getText().toString();
                year = addttyearpinnerr.getSelectedItem().toString();
                dept = addttdeptspinner.getSelectedItem().toString();
                div = addttdivspinner.getSelectedItem().toString();
                Log.i("Add",myYear+"");
                Log.i("Add",myday+"");
                Log.i("Add",myYear+"");
                Log.i("myYear",myHour+"");
                String dateva = myday+"/"+myMonth+"/"+myYear;
                Log.i("Dateva",dateva);
                String hrva = myHour+":"+myMinute;
                Log.i("Dateva",hrva);

                if(subject.isEmpty()){
                    TtSubject.setError("Empty");
                    TtSubject.requestFocus();
                }
                else if(title.isEmpty()){
                    Tttitle.setError("Empty");
                    Tttitle.requestFocus();
                }else if(year.equals("Select Year")){
                    Toast.makeText(getApplicationContext(), "Please select a Year", Toast.LENGTH_SHORT).show();
                }else if(dept.equals("Select Department")){
                    Toast.makeText(getApplicationContext(), "Please select a Department", Toast.LENGTH_SHORT).show();
                }else if(div.equals("Select Division")){
                    Toast.makeText(getApplicationContext(), "Please select a Division ", Toast.LENGTH_SHORT).show();
                }
                else {
                    Log.i("Upload ","upload");
                    fst.collection("Users").addSnapshotListener(new EventListener<QuerySnapshot>() {
                        @Override
                        public void onEvent(@Nullable QuerySnapshot value, @Nullable FirebaseFirestoreException error) {
                            al.clear();
                            String yy = year+"_"+dept+"_"+div;
                            Log.i("In",yy);
                            for(DocumentSnapshot dd:value){
                                String r= dd.getString("Roll_Num");
                                String ydd = dd.getString("Year");
                                String deptdd = dd.getString("Dept");
                                String Divdd = dd.getString("Div");
                                String aaka = ydd+"_"+deptdd+"_"+Divdd;
                                if(aaka.equals(yy)){
                                    String tcet = r+"_"+yy;
                                    Log.i("Aaaaaaa",tcet);
                                    al.add(tcet);
                                }

                            }
                            System.out.println(al);
                            HashMap<String, Object> result = new HashMap<>();
                            result.put("fileurl", "");
                            result.put("submitted","false");
                            for(int i = 0;i<al.size();i++)
                                databaseReference2.child(year+"_"+dept+"_"+div).child(title+"_"+subject).child(al.get(i)).setValue(result);

                        }
                    });

                    Log.i("Upload ","upload");
                    uploadPdf(subject,title,year,dept,div,dateva,hrva);
                }
            }
        });


    }


    private void uploadPdf(String subject,String title,String year,String dept,String div,String dateva,String hrva) {
        ProgressDialog pd = new ProgressDialog(getApplicationContext());
        pd.setTitle("Please wait");
        pd.setMessage("uploading..");

        StorageReference reference = storageReference.child("assignment/"+year+"_"+dept+"_"+div+"_"+subject+"_"+title+".pdf");
        reference.putFile(filepath).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                reference.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                    @Override
                    public void onSuccess(Uri uri) {
                        String aaay = title+"_"+subject;
                        AssignmentModel obj = new AssignmentModel(subject,title,uri.toString(),year,dept,div,dateva,hrva,aaay);
                        String pass = year+"_"+dept+"_"+div;

                        databaseReference.child(pass).child(aaay).setValue(obj);
                        DocumentReference data = fst.collection("Users").document(mAuth.getCurrentUser().getUid());
                        data.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                            @Override
                            public void onSuccess(DocumentSnapshot documentSnapshot) {
                                if(documentSnapshot.exists()){
                                    al.clear();
                                    String  am_yy = documentSnapshot.getString("Year") ;
                                    String  am_dept = documentSnapshot.getString("Dept");
                                    String  am_div = documentSnapshot.getString("Div");
                                    String  am_roll = documentSnapshot.getString("Roll_Num");
                                    String ff = am_yy+""+am_dept+""+am_div;
                                    if(ff.equals(pass)){
                                        al.add(ff);
                                    }

                                }

                            }
                        });
                        for(int i = 0;i<al.size();i++)
                            databaseReference2.child(year+""+dept+""+div).child(aaay).child(al.get(i));
                        Tttitle.setText("");
                        Ttbrowsepdf.setVisibility(View.VISIBLE);
                        Ttcancelpdf.setVisibility(View.GONE);
                        Ttshowpdf.setVisibility(View.GONE);

                        pd.dismiss();

                        Toast.makeText(getApplicationContext(), title +" Uploaded", Toast.LENGTH_SHORT).show();


                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(getApplicationContext(), "Error: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
            }
        });


    }

    @Override
    public void onDateSet(DatePicker datePicker, int i, int i1, int i2) {

        myYear = i;
        myday = i2;
        myMonth = i1;
        Log.i("myYear",myYear+"");
        Log.i("myYear",myday+"");
        Log.i("myYear",myMonth+"");

        Calendar c = Calendar.getInstance();
        hour = c.get(Calendar.HOUR);
        minute = c.get(Calendar.MINUTE);
        TimePickerDialog timePickerDialog = new TimePickerDialog(AssignmentActivity.this, AssignmentActivity.this, hour, minute, DateFormat.is24HourFormat(this));
        timePickerDialog.show();

    }

    @Override
    public void onTimeSet(TimePicker timePicker, int i, int i1) {
        myHour = i;
        myMinute = i1;

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == 101 && resultCode == RESULT_OK){
            filepath = data.getData();
            Log.i("Kam",filepath+"");
            Ttshowpdf.setVisibility(View.VISIBLE);
            Ttcancelpdf.setVisibility(View.VISIBLE);
            Ttbrowsepdf.setVisibility(View.GONE);
//
        }
    }
}