package com.boreholes.locatewater.activities;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.boreholes.locatewater.R;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

/**

 */
public class RiversFragment extends Fragment {
    private RecyclerView recyclerView;
    private DatabaseReference likesRef, sourcesRef;
    private FloatingActionButton fab;
    // private FirebaseUser mCurrentUser;
    private FirebaseAuth.AuthStateListener mAuthListener;

    Boolean likeChecker = false;
    //  private FirebaseRecyclerAdapter adapter;
    String currentUserID = null;
    private ServicesModel servicesModel;

    public RiversFragment() {
        // Required empty public constructor
    }

    Activity context;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        context = getActivity();
        //initialize recyclerview
        View rootView = inflater.inflate(R.layout.fragment_rivers, container, false);

        recyclerView = rootView.findViewById(R.id.recycler_sources);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(rootView.getContext());
        //Reverse  the layout so as to display the most recent post at the top
        linearLayoutManager.setReverseLayout(true);
        linearLayoutManager.setStackFromEnd(true);

        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(linearLayoutManager);
        // recyclerView.setLayoutManager(linearLayoutManager);

        // Inflate the layout for this fragment
        //get an instance of firebase authentication

        FirebaseAuth mAuth = FirebaseAuth.getInstance();
        FirebaseUser currentUser = mAuth.getCurrentUser();
        if (currentUser == null) {

            Intent loginIntent = new Intent(context, RegisterActivity.class);
            //loginIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(loginIntent);
        }

        sourcesRef = FirebaseDatabase.getInstance().getReference().child("Survey");
        return rootView;
    }

    @Override
    public void onStart() {
        super.onStart();




        FirebaseRecyclerOptions<ServicesModel> options =
                new FirebaseRecyclerOptions.Builder<ServicesModel>()
                        .setQuery(sourcesRef, ServicesModel.class)
                        .build();

        FirebaseRecyclerAdapter<ServicesModel, RiversFragment.ProjectViewHolder> adapter =
                new FirebaseRecyclerAdapter<ServicesModel, RiversFragment.ProjectViewHolder>(options) {
                    @Override
                    protected void onBindViewHolder(@NonNull RiversFragment.ProjectViewHolder holder, int position, @NonNull ServicesModel model) {
                        final String post_key = getRef(position).getKey();
                        // final int position = getAdapterPosition();
                        // holder.setCard();
                        //eventsRef.child(post_key).addValueEventListener


                        sourcesRef.child(post_key).addValueEventListener(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot snapshot) {
                                if (snapshot.hasChild("Rivers")) {


                                    final String source_address = snapshot.child("Rivers").child("location").getValue().toString();
                                    final String source_name = snapshot.child("Rivers").child("videoTitle").getValue().toString();
                                    final String source_county = snapshot.child("county").getValue().toString();
                                    final String source_subcounty = snapshot.child("subCounty").getValue().toString();
                                    final String source_ward = snapshot.child("ward").getValue().toString();
                                    //final String serviceCost = snapshot.child("Rivers").child("videoCaption").getValue().toString();



                                    holder.sourceAddress.setText(source_address);
                                    holder.sourceName.setText(source_name);
                                    holder.sourceCounty.setText(source_county);
                                    holder.sourceSubCounty.setText(source_subcounty);
                                    holder.sourceWard.setText(source_ward);

                                    //holder.post_image.setText(projectDate);

                                        final String sourceImage = snapshot.child("Rivers").child("eventPhoto").getValue().toString();
                                        Picasso.with(getContext()).load(sourceImage).into(holder.sourceImage);
                                    /*
                                    holder.scheduleButton.setOnClickListener(new View.OnClickListener() {
                                        @Override
                                        public void onClick(View v) {
                                            Intent reportIntent = new Intent(context, ReserveActivity.class);
                                            reportIntent.putExtra("ServiceID", post_key);
                                            reportIntent.putExtra("ServiceName",serviceTitle);
                                            startActivity(reportIntent);
                                        }
                                    });
                                    holder.inquireButton.setOnClickListener(new View.OnClickListener() {
                                        @Override
                                        public void onClick(View v) {
                                            String phone = "+254700931415";
                                            Intent intent = new Intent(Intent.ACTION_DIAL, Uri.fromParts("tel", phone, null));
                                            startActivity(intent);
                                        }
                                    });
                                    */

                                }

                            }


                            @Override
                            public void onCancelled(@NonNull DatabaseError error) {

                            }
                        });
                    }

                    @NonNull
                    @Override
                    public RiversFragment.ProjectViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.source_card_item, parent, false);

                        return new ProjectViewHolder(view);
                    }
                };
        recyclerView.setAdapter(adapter);
        adapter.startListening();
    }

    public static class ProjectViewHolder extends RecyclerView.ViewHolder {
        //Declare the view objects in the card view

        public final TextView sourceName;
        public final TextView sourceCounty;
        public final TextView sourceSubCounty;
        public final TextView sourceWard;
        public final ImageView sourceImage;
        public  final TextView sourceAddress;

        public ProjectViewHolder(@NonNull View itemView) {
            super(itemView);
            //Initialize the card view item objects
            //profile_image=itemView.findViewById(R.id.userImage);
            sourceName = itemView.findViewById(R.id.sourceName);
            sourceCounty = itemView.findViewById(R.id.sourceCounty);
            sourceSubCounty = itemView.findViewById(R.id.sourceSubCounty);
            sourceWard = itemView.findViewById(R.id.sourceWard);
            sourceAddress=itemView.findViewById(R.id.sourceAddress);
            sourceImage =itemView.findViewById(R.id.sourceImage);







        }


    }

}

