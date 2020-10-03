package com.example.jobs;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;

public class RecyclerViewJobs extends AppCompatActivity {

    private FirebaseFirestore firebaseFirestore;
    private RecyclerView firestoreJobs;
    private FirestoreRecyclerAdapter adapter;
    //private FirebaseAut
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recycler_view_jobs);

        firebaseFirestore = FirebaseFirestore.getInstance();
        firestoreJobs = findViewById(R.id.firestoreJobs);


        //Query
        Query query = firebaseFirestore.collection("applicants");

        //Recycler options
        FirestoreRecyclerOptions<JobsModel> options = new FirestoreRecyclerOptions.Builder<JobsModel>()
                .setQuery(query , JobsModel.class)
                .build();
        adapter = new FirestoreRecyclerAdapter<JobsModel, JobsViewHolder>(options) {
            @NonNull
            @Override
            public JobsViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_item_jobcard , parent , false);
                return new JobsViewHolder(view);
            }

            @Override
            protected void onBindViewHolder(@NonNull JobsViewHolder holder, int position, @NonNull JobsModel model) {
                holder.listSubjectName.setText(model.getSubject());
                holder.listJobMode.setText(model.getMode());
                holder.listState.setText(model.getState());
                holder.listCity.setText(model.getCity());
                holder.listPin.setText(model.getPin());
            }
        };

        //View holder
        firestoreJobs.setHasFixedSize(true);
        firestoreJobs.setLayoutManager(new LinearLayoutManager(this));
        firestoreJobs.setAdapter(adapter);
    }

    private class JobsViewHolder extends RecyclerView.ViewHolder {

        private TextView listSubjectName,listJobMode,listState,listCity,listPin;

        public JobsViewHolder(@NonNull View itemView) {
            super(itemView);

            listSubjectName = itemView.findViewById(R.id.list_subject_name);
            listJobMode = itemView.findViewById(R.id.list_job_mode);
            listState = itemView.findViewById(R.id.list_state);
            listCity = itemView.findViewById(R.id.list_city);
            listPin = itemView.findViewById(R.id.list_pin);
        }
    }

    @Override
    protected void onStart() {
        super.onStart();
        adapter.startListening();
    }

    @Override
    protected void onStop() {
        super.onStop();
        adapter.stopListening();
    }
}