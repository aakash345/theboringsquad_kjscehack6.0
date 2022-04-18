package com.ars.kjse;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.karumi.dexter.Dexter;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.PermissionDeniedResponse;
import com.karumi.dexter.listener.PermissionGrantedResponse;
import com.karumi.dexter.listener.PermissionRequest;
import com.karumi.dexter.listener.single.PermissionListener;


public class uploadass extends AppCompatActivity {
    StorageReference storageReference;
    DatabaseReference databaseReference;
    FirebaseAuth mAuth;
    FirebaseFirestore fst;
    ImageView assbrowsepdf, asshowpdf, asscancel;
    Button addassbtn;
    Uri filepath;
    String am_yy, am_dept, am_div, am_roll, ff;
    public static final String KEY_EXTRA = "ass_submit";
    public static final String KEY_XTRA = "ass_due";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_uploadass);
        mAuth = FirebaseAuth.getInstance();
        System.out.println(getIntent().getStringExtra(KEY_XTRA).toString());
        storageReference = FirebaseStorage.getInstance().getReference();

        databaseReference = FirebaseDatabase.getInstance().getReference("submissions");
        assbrowsepdf = findViewById(R.id.assbrowsepdf);
        asshowpdf = findViewById(R.id.assshowpdf);
        asscancel = findViewById(R.id.asscancel);
        addassbtn = findViewById(R.id.addassbtn);

        assbrowsepdf.setVisibility(View.VISIBLE);
        asscancel.setVisibility(View.GONE);
        asshowpdf.setVisibility(View.GONE);
        asscancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                asshowpdf.setVisibility(View.GONE);
                asscancel.setVisibility(View.GONE);
                assbrowsepdf.setVisibility(View.VISIBLE);
            }

        });

        assbrowsepdf.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Dexter.withActivity(uploadass.this)
                        .withPermission(Manifest.permission.READ_EXTERNAL_STORAGE)
                        .withListener(new PermissionListener() {
                            @Override
                            public void onPermissionGranted(PermissionGrantedResponse response) {
                                Intent intent = new Intent();
                                intent.setType("application/pdf");
                                Log.i("Kam", "PDF liya");
                                intent.setAction(Intent.ACTION_GET_CONTENT);
                                startActivityForResult(Intent.createChooser(intent, "Select pdf files"), 101);
                            }

                            @Override
                            public void onPermissionDenied(PermissionDeniedResponse response) {
                            }

                            @Override
                            public void onPermissionRationaleShouldBeShown(PermissionRequest permission, PermissionToken token) {
                                token.continuePermissionRequest();
                            }
                        }).check();

            }
        });

        addassbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DatabaseReference mRef = FirebaseDatabase.getInstance().getReference();
                String auths = mAuth.getCurrentUser().getUid();
                DocumentReference data = FirebaseFirestore.getInstance().collection("Users").document(auths);


                data.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                    @Override
                    public void onSuccess(DocumentSnapshot documentSnapshot) {
                        if (documentSnapshot.exists()) {
                            am_yy = documentSnapshot.getString("Year");
                            am_dept = documentSnapshot.getString("Dept");
                            am_div = documentSnapshot.getString("Div");
                            am_roll = documentSnapshot.getString("Roll_Num");
                            String am_points = documentSnapshot.getString("points");
                            ff = am_yy + "" + am_dept + "" + am_div;

                        }

                    }

                });
                uploadPdf(ff + "_" + am_roll);

            }

        private void uploadPdf (String title){
            ProgressDialog pd = new ProgressDialog(getApplicationContext());
            pd.setTitle("Please wait");
            pd.setMessage("uploading..");

            StorageReference reference = storageReference.child("submissions/" + title + ".pdf");
            reference.putFile(filepath).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                    reference.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                        @Override
                        public void onSuccess(Uri uri) {
                            AssSubmissionModel obj = new AssSubmissionModel(uri.toString(), "true");
                            assbrowsepdf.setVisibility(View.VISIBLE);
                            asscancel.setVisibility(View.GONE);
                            asshowpdf.setVisibility(View.GONE);
                            updateassignmentdata(obj);
                            pd.dismiss();

                            Toast.makeText(getApplicationContext(), title + " Uploaded" + " 10 points credited", Toast.LENGTH_SHORT).show();


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
        private void updateassignmentdata (AssSubmissionModel obj){
            DatabaseReference mRef = FirebaseDatabase.getInstance().getReference();
            String auths = mAuth.getCurrentUser().getUid();
            DocumentReference data = FirebaseFirestore.getInstance().collection("Users").document(auths);
            data.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                @Override
                public void onSuccess(DocumentSnapshot documentSnapshot) {
                    if (documentSnapshot.exists()) {
                        String am_yy = documentSnapshot.getString("Year");
                        String am_dept = documentSnapshot.getString("Dept");
                        String am_div = documentSnapshot.getString("Div");
                        String am_roll = documentSnapshot.getString("Roll_Num");
                        String am_points = documentSnapshot.getString("points");
                        String ff = am_yy + "" + am_dept + "" + am_div;

                        annoy(am_points);
                        mRef.child("ToDo/" + am_yy + "_" + am_dept + "_" + am_div + "/" + getIntent().getStringExtra(KEY_EXTRA).toString() + "/" + am_roll + "_" + am_yy + "_" + am_dept + "_" + am_div).setValue(obj);

                    }

                }

                private void annoy(String pt) {
                    int pts = Integer.parseInt(pt);
//                String dateStr = getIntent().getStringExtra(KEY_XTRA);
//                L
//                System.out.println(dateStr);
//                System.out.println(dateObj);
//
//                SimpleDateFormat sdFormat = new SimpleDateFormat("yyyy-MM-dd");
//
//                String d1 = sdFormat.format(dateStr.toString());
//                String  d2 = sdFormat.format(dateObj);
//                System.out.println(d2);
//
//                if(d1.compareTo(d2)>0){
                    FirebaseFirestore.getInstance().collection("Users").document(mAuth.getCurrentUser().getUid()).update("points", String.valueOf(pts + 10));

//                }
                }
            });

        }

    });
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == 101 && resultCode == RESULT_OK){
            filepath = data.getData();
            Log.i("Kam",filepath+"");
            asshowpdf.setVisibility(View.VISIBLE);
            asscancel.setVisibility(View.VISIBLE);
            assbrowsepdf.setVisibility(View.GONE);
//
        }
    }
}
