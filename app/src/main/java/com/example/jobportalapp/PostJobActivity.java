package com.example.jobportalapp;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.jobportalapp.Model.Data;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class PostJobActivity extends AppCompatActivity {

    private FloatingActionButton fabBtn;
    private Toolbar toolbar;
    private FirebaseRecyclerAdapter adapter;

    //Recycler View
    private RecyclerView recyclerView;
    //Firebase
    private FirebaseAuth mAuth;
    private DatabaseReference JobPostDatabase;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_post_job);

        toolbar=findViewById(R.id.toolbar_post_job);
        setSupportActionBar(toolbar);

        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        fabBtn=findViewById(R.id.fab_add);

        mAuth=FirebaseAuth.getInstance();
        FirebaseUser mUser=mAuth.getCurrentUser();
        String uId=mUser.getUid();

        JobPostDatabase= FirebaseDatabase.getInstance().getReference().child("Job Post").child(uId);

        recyclerView=findViewById(R.id.recycler_job_post_id);
        LinearLayoutManager layoutManager=new LinearLayoutManager(this);
        layoutManager.setStackFromEnd(true);
        layoutManager.setReverseLayout(true);

        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(layoutManager);

        FirebaseRecyclerOptions<Data> allJobActivity=new FirebaseRecyclerOptions.Builder<Data>()
                .setQuery(JobPostDatabase,Data.class).build();
        adapter = new FirebaseRecyclerAdapter<Data, MyViewHolder>(allJobActivity) {
            @NonNull
            @Override
            public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                View view = LayoutInflater.from(parent.getContext())
                        .inflate(R.layout.job_post_item, parent, false);

                return new MyViewHolder(view);
            }

            @Override
            protected void onBindViewHolder(@NonNull MyViewHolder holder, int position, @NonNull final Data model) {
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

        fabBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(), InsertPostJobActivity.class));
            }
        });
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

    public static class MyViewHolder extends RecyclerView.ViewHolder{

        View myview;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            myview=itemView;
        }

        public void setJobTitle(String title){
            TextView mTitle=myview.findViewById(R.id.job_title);
            mTitle.setText(title);
        }

        public void setJobDate(String date){
            TextView mDate=myview.findViewById(R.id.job_date);
            mDate.setText(date);
        }

        public void setJobDescription(String description){
            TextView mDescription=myview.findViewById(R.id.job_description);
            mDescription.setText(description);
        }

        public void setJobSkills(String skills){
            TextView mSkills=myview.findViewById(R.id.job_skills);
            mSkills.setText(skills);
        }

        public void setJobSalary(String salary){
            TextView mSalary=myview.findViewById(R.id.job_salary);
            mSalary.setText(salary);
        }
    }
}