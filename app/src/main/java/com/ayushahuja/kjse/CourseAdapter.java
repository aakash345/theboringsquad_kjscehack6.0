package com.ayushahuja.kjse;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QuerySnapshot;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class CourseAdapter extends RecyclerView.Adapter<CourseAdapter.Viewholder> {

    private Context context;
    private ArrayList<CourseModel> courseModelArrayList;
    FirebaseAuth mAuth;
    FirebaseFirestore fst;

    // Constructor
    public CourseAdapter(Context context, ArrayList<CourseModel> courseModelArrayList) {
        this.context = context;
        this.courseModelArrayList = courseModelArrayList;
    }

    @NonNull
    @Override
    public CourseAdapter.Viewholder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        // to inflate the layout for each item of recycler view.
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.card_layout, parent, false);
        return new Viewholder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CourseAdapter.Viewholder holder, int position) {
        // to set data to textview and imageview of each card layout
        CourseModel model = courseModelArrayList.get(position);
        holder.courseNameTV.setText(model.getCourse_name());
        holder.courseRatingTV.setText("" + model.getCourse_rating());
        holder.courseIV.setImageResource(model.getCourse_image());
        holder.points.setText("Points: "+model.getPoints());
    }

    @Override
    public int getItemCount() {
        // this method is used for showing number
        // of card items in recycler view.
        return courseModelArrayList.size();
    }

    // View holder class for initializing of
    // your views such as TextView and Imageview.
    public class Viewholder extends RecyclerView.ViewHolder implements View.OnClickListener {
        private ImageView courseIV;
        private TextView courseNameTV, courseRatingTV, points;
        private Button btn_redeem;
        @Override
        public void onClick(View view) {
            mAuth = FirebaseAuth.getInstance();
            fst = FirebaseFirestore.getInstance();
            int pos = this.getAdapterPosition();
            DocumentReference data=fst.collection("Users").document(mAuth.getCurrentUser().getUid());
            data.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                @Override
                public void onSuccess(DocumentSnapshot documentSnapshot) {
                    if(documentSnapshot.exists()){
                        String pt = documentSnapshot.getString("points");
                        anonymous(pt);
                    }
                }

                private void anonymous(String pt) {
                    CourseModel model = courseModelArrayList.get(pos);
                    int point = model.getPoints();
                    int pro_point;
                    pro_point = Integer.parseInt(pt);
                    if (point<pro_point){
                        fst.collection("Users").document(mAuth.getCurrentUser().getUid()).update("points",String.valueOf(pro_point-point));
                    }
                    else{
                        Toast.makeText(context, "Cannot redeem, required more points", Toast.LENGTH_SHORT).show();
                    }
                    Intent refresh = new Intent(context, Redeem.class);
                    refresh.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    context.startActivity(refresh);
                }
            });
//            int pos = this.getAdapterPosition();
//            CourseModel model = courseModelArrayList.get(pos);
//            int point = model.getPoints();
//            int pro_point;
//            pro_point = Integer.parseInt(pt);
//            if (point<pro_point){
//                Map<String,Object> ap = new HashMap<>();
//                ap.put("points",pro_point-point);
//                fst.collection("Users").document(mAuth.getCurrentUser().getUid()).update("points","100");
//            }
//            else{
//                Toast.makeText(context, "Cannot redeem required more points", Toast.LENGTH_SHORT).show();
//            }
        }

        public Viewholder(@NonNull View itemView) {
            super(itemView);
            courseIV = itemView.findViewById(R.id.idIVCourseImage);
            courseNameTV = itemView.findViewById(R.id.idTVCourseName);
            courseRatingTV = itemView.findViewById(R.id.idTVCourseRating);
            points = itemView.findViewById(R.id.points);
            btn_redeem = itemView.findViewById(R.id.btn_redeem);
            btn_redeem.setOnClickListener(this);
        }
    }
}

