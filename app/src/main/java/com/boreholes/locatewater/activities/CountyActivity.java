package com.boreholes.locatewater.activities;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.boreholes.locatewater.R;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.firebase.ui.database.SnapshotParser;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import jp.wasabeef.picasso.transformations.CropCircleTransformation;

public class CountyActivity extends AppCompatActivity {
    private RecyclerView recyclerView;
    DatabaseReference countyRef;
    private FirebaseAuth mAuth;
    private FirebaseRecyclerAdapter adapter;
    String currentUserID = null;
    String newCounty = null;
    private ArrayList<String> names;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_county);
        //inflate the tool bar
        Toolbar toolbar = findViewById(R.id.tool_bar);
        setSupportActionBar(toolbar);
        setToolbarTitle("");

        //initialize recyclerview
        recyclerView = findViewById(R.id.recyclerView);
       LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        //Reverse  the layout so as to display the most recent post at the top
       // linearLayoutManager.setReverseLayout(true);
       recyclerView.setLayoutManager(linearLayoutManager);
      recyclerView.setHasFixedSize(true);
        //findFriendsRecyclerList = (RecyclerView)findViewById(R.id.find_friends_recycler_list);
    //recyclerView.setLayoutManager(new LinearLayoutManager(this));
        mAuth = FirebaseAuth.getInstance();
        FirebaseUser currentUser = mAuth.getCurrentUser();
        if (currentUser == null) {

            Intent loginIntent = new Intent(CountyActivity.this, LoginActivity.class);
            loginIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(loginIntent);
        }
        countyRef = FirebaseDatabase.getInstance().getReference().child("Counties");


    }

    @Override
    protected void onStart() {
        //
        super.onStart();


        String userID = mAuth.getCurrentUser().getUid();
        //FloatingActionButton fab = (FloatingActionButton) context.findViewById(R.id.fabEvent);
        FirebaseUser currentUser = mAuth.getCurrentUser();

        names  = new ArrayList<String>();
        FirebaseRecyclerOptions<CountyModel> options =
                new FirebaseRecyclerOptions.Builder<CountyModel>()
                        .setQuery(countyRef, CountyModel.class)
                        .build();
        ChildEventListener childEventListener = new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {


              // newCounty = snapshot.getKey();

                Log.d("TAG", "ONCHILDADDED" + snapshot.getKey());

                final  String name = snapshot.getKey();
                names.add(name);



            }

            @Override
            public void onChildChanged(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {

            }

            @Override
            public void onChildRemoved(@NonNull DataSnapshot snapshot) {

            }

            @Override
            public void onChildMoved(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        };
        countyRef.addChildEventListener(childEventListener);

        FirebaseRecyclerAdapter<CountyModel, CountyModelViewHolder> adapter =
                new FirebaseRecyclerAdapter<CountyModel, CountyModelViewHolder>(options) {

                    @Override
                    protected void onBindViewHolder(@NonNull CountyModelViewHolder holder, int i, @NonNull CountyModel countyModel) {

                     //holder.county_name.setText(countyModel.getCountyName());
                        final String post_key = getRef(i).getKey();

                        final  String name = names.get(i);
                        if (name == null){ return;}
                        holder.county_name.setText(name);
                        //String cname = holder.county_name.getText().toString();
                        holder.county_opener.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {

                                Intent reportIntent = new Intent(CountyActivity.this, MyCountyActivity.class);
                                reportIntent.putExtra("CountyName", name);
                                startActivity(reportIntent);

                               // finish();
                            }
                        });


                    }

                    @NonNull
                    @Override
                    public CountyModelViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.county_card_item, parent, false);

                        return new CountyModelViewHolder(view);
                    }
                };
        recyclerView.setAdapter(adapter);
        adapter.startListening();
    }

    @Override
    protected void onStop() {
        super.onStop();
        FirebaseUser currentUser = mAuth.getCurrentUser();
        if (currentUser != null) {
//            adapter.stopListening();
        }

    }


    public void setToolbarTitle(String Title) {
        if (getSupportActionBar() != null) {
            getSupportActionBar().setTitle(Title);

        }
    }

    public class CountyModelViewHolder extends RecyclerView.ViewHolder {
        //Declare the view objects in the card view
        public TextView county_name;
        public CardView county_opener;

        public CountyModelViewHolder(@NonNull View itemView) {
            super(itemView);
            //Initialize the card view item objects
            county_name = itemView.findViewById(R.id.countyName);
            county_opener = itemView.findViewById(R.id.card_opener);

        }

    }
}