package com.example.jobportalapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.jobportalapp.Model.Data;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.firebase.ui.database.SnapshotParser;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class AllJobActivity extends AppCompatActivity {

    private Toolbar toolbar;

    //Recycler

    private RecyclerView recyclerView;

    //Firebase

    private DatabaseReference mAllJobPost;
    private Object SnapshotParser;
    private FirebaseRecyclerAdapter adapter;
    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_all_job);

        toolbar=findViewById(R.id.all_job_post);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("All Job Post");

        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        mAuth= FirebaseAuth.getInstance();
        FirebaseUser mUser=mAuth.getCurrentUser();
        String uId=mUser.getUid();

        //Database

        mAllJobPost= FirebaseDatabase.getInstance().getReference().child("Job Post").child(uId);
        mAllJobPost.keepSynced(true);

        recyclerView=findViewById(R.id.recycler_all_job);

        LinearLayoutManager layoutManager=new LinearLayoutManager(this);
        layoutManager.setReverseLayout(true);
        layoutManager.setStackFromEnd(true);

        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(layoutManager);

        FirebaseRecyclerOptions<Data> allJobActivity=new FirebaseRecyclerOptions.Builder<Data>()
                .setQuery(mAllJobPost,Data.class).build();
        adapter = new FirebaseRecyclerAdapter<Data, AllJobPostViewHolder>(allJobActivity) {
            @NonNull
            @Override
            public AllJobPostViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                View view = LayoutInflater.from(parent.getContext())
                        .inflate(R.layout.alljobpost, parent, false);

                return new AllJobPostViewHolder(view);
            }

            @Override
            protected void onBindViewHolder(@NonNull AllJobPostViewHolder holder, int position, @NonNull final Data model) {
                holder.setJobTitle(model.getTitle());
                holder.setJobDate(model.getDate());
                holder.setJobDescription(model.getDescription());
                holder.setJobSkills(model.getSkills());
                holder.setJobSalary(model.getSalary());

                holder.myview.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        Intent intent=new Intent(getApplicationContext(),JobDetailsActivity.class);

                        intent.putExtra("title",model.getTitle());
                        intent.putExtra("date",model.getDate());
                        intent.putExtra("description",model.getDescription());
                        intent.putExtra("skills",model.getSkills());
                        intent.putExtra("salary",model.getSalary());

                        startActivity(intent);
                    }
                });
            }
        };
        recyclerView.setAdapter(adapter);
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

    public static class AllJobPostViewHolder extends RecyclerView.ViewHolder{

        View myview;

        public AllJobPostViewHolder(@NonNull View itemView) {
            super(itemView);
            myview=itemView;
        }
        public void setJobTitle(String title){
            TextView mTitle=myview.findViewById(R.id.all_job_post_title);
            mTitle.setText(title);
        }
        public void setJobDate(String date){
            TextView mDate=myview.findViewById(R.id.all_job_post_date);
            mDate.setText(date);
        }
        public void setJobDescription(String description){
            TextView mDescription=myview.findViewById(R.id.all_job_post_description);
            mDescription.setText(description);
        }
        public void setJobSkills(String skills){
            TextView mSkills=myview.findViewById(R.id.all_job_post_skills);
            mSkills.setText(skills);
        }
        public void setJobSalary(String salary){
            TextView mSalary=myview.findViewById(R.id.all_job_post_salary);
            mSalary.setText(salary);
        }
    }

}