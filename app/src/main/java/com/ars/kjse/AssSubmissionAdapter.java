package com.ars.kjse;

import android.annotation.SuppressLint;
import android.app.DownloadManager;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Environment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class AssSubmissionAdapter extends RecyclerView.Adapter<AssSubmissionAdapter.myviewholder>{
    List<AssSubmissionModel> data;
    List<AssignmentModel> data2;
    public AssSubmissionAdapter( List<AssignmentModel> data2) {
        this.data2 = data2;
    }

    @NonNull
    @Override
    public myviewholder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View view = inflater.inflate(R.layout.single_row_ass,parent,false);
        return new myviewholder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull myviewholder holder,@SuppressLint("RecyclerView") int position) {
        holder.asstitle.setText(data2.get(position).getAaay());
        holder.due.setText(data2.get(position).getDue_date());
        holder.assdownloadpdf.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                downloadFile(holder.assdownloadpdf.getContext(),data2.get(position).getFilename(),".pdf", Environment.DIRECTORY_DOWNLOADS,data2.get(position).getFileurl());
            }
        });
    }
    private void downloadFile(Context context, String fileName, String fileExtension, String destinationExtension, String url) {

        DownloadManager downloadManager = (DownloadManager) context.getSystemService(Context.DOWNLOAD_SERVICE);
        Uri uri = Uri.parse(url);
        DownloadManager.Request request = new DownloadManager.Request(uri);

        request.setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED);
        request.setDestinationInExternalFilesDir(context,destinationExtension, fileName+fileExtension);

        downloadManager.enqueue(request);

    }


    @Override
    public int getItemCount() {
        return data2.size();
    }

    public class myviewholder extends RecyclerView.ViewHolder {
        CardView asscard;
        ImageView assdownloadpdf;
        TextView asstitle;
        Button asssubmit;
        TextView due;

        public myviewholder(@NonNull View itemView) {
            super(itemView);
            assdownloadpdf = itemView.findViewById(R.id.assdownloadpdf);
            asstitle = itemView.findViewById(R.id.asstitle);
            asscard = itemView.findViewById(R.id.asscard);
            asssubmit = itemView.findViewById(R.id.asssubmit);
            due = itemView.findViewById(R.id.due);
            asssubmit.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent(view.getContext(), uploadass.class);
                    System.out.println(String.valueOf(asstitle.getText()));
                    intent.putExtra(uploadass.KEY_EXTRA, String.valueOf(asstitle.getText()));
                    intent.putExtra(uploadass.KEY_XTRA, String.valueOf(due.getText()));
                    view.getContext().startActivity(intent);
                }
            });
        }

    }}
