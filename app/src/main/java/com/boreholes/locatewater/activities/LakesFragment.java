package com.boreholes.locatewater.activities;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

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



public class LakesFragment extends Fragment {

    private RecyclerView recyclerView;
    private DatabaseReference likesRef, sourcesRef;
    private FloatingActionButton fab;
    // private FirebaseUser mCurrentUser;
    private FirebaseAuth.AuthStateListener mAuthListener;

    Boolean likeChecker = false;
    //  private FirebaseRecyclerAdapter adapter;
    String currentUserID = null;
    private ServicesModel servicesModel;

    public LakesFragment() {
        // Required empty public constructor
    }

    Activity context;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        context = getActivity();
        //initialize recyclerview
        View rootView = inflater.inflate(R.layout.fragment_lakes, container, false);

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
        likesRef = FirebaseDatabase.getInstance().getReference().child("Likes");
        sourcesRef = FirebaseDatabase.getInstance().getReference().child("Lakes");
        return rootView;
    }

    @Override
    public void onStart() {
        super.onStart();


        FirebaseRecyclerOptions<ServicesModel> options =
                new FirebaseRecyclerOptions.Builder<ServicesModel>()
                        .setQuery(sourcesRef, ServicesModel.class)
                        .build();

        FirebaseRecyclerAdapter<ServicesModel, LakesFragment.ProjectViewHolder> adapter =
                new FirebaseRecyclerAdapter<ServicesModel, LakesFragment.ProjectViewHolder>(options) {
                    @Override
                    protected void onBindViewHolder(@NonNull LakesFragment.ProjectViewHolder holder, int position, @NonNull ServicesModel model) {
                        final String post_key = getRef(position).getKey();
                        // final int position = getAdapterPosition();
                        // holder.setCard();
                        //eventsRef.child(post_key).addValueEventListener


                        sourcesRef.child(post_key).addValueEventListener(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot snapshot) {


                                final String source_address = snapshot.child("location").getValue().toString();
                                final String source_name = snapshot.child("videoTitle").getValue().toString();
                                final String source_county = snapshot.child("county").getValue().toString();
                                final String source_subcounty = snapshot.child("subCounty").getValue().toString();
                                final String source_ward = snapshot.child("ward").getValue().toString();
                                final String videoURL = snapshot.child("videoUrl").getValue().toString();
                                final String imageUrl = snapshot.child("eventPhoto").getValue().toString();
                                //String videoUrl = snapshot.getValue(String.class);

                                holder.sourceName.setText(source_name);
                                holder.sourceCounty.setText(source_county);
                                holder.sourceSubCounty.setText(source_subcounty);
                                holder.sourceWard.setText(source_ward);


                                final String sourceImage = snapshot.child("eventPhoto").getValue().toString();
                                Picasso.with(getContext()).load(sourceImage).into(holder.sourceImage);

                                holder.setLikeButtonStatus(post_key);

                                holder.likePostButton.setOnClickListener(new View.OnClickListener() {

                                    @Override
                                    public void onClick(View v) {
                                        // initialize the like checker to true, we are using this boolean variable to determine if a post has been liked or dislike
                                        // we declared this variable on to of our activity class
                                        likeChecker = true;
                                        //check the currently logged in user using his/her ID
                                        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
                                        if (user != null) {
                                            currentUserID = user.getUid();
                                        } else {
                                            Toast.makeText(context, R.string.please_login, Toast.LENGTH_SHORT).show();

                                        }
                                        //Listen to changes in the likes database reference
                                        likesRef.addValueEventListener(new ValueEventListener() {
                                            @Override
                                            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                                if (likeChecker.equals(true)) {
                                                    // if the current post has a like, associated to the current logged and the user clicks on it again, remove the like
                                                    //basically the user is disliking
                                                    if (dataSnapshot.child(post_key).hasChild(currentUserID)) {
                                                        likesRef.child(post_key).child(currentUserID).removeValue();
                                                        likeChecker = false;
                                                    } else {
                                                        //here the user is liking, set value on the like
                                                        likesRef.child(post_key).child(currentUserID).setValue(true);
                                                        likeChecker = false;
                                                    }
                                                }
                                            }

                                            @Override
                                            public void onCancelled(@NonNull DatabaseError error) {

                                            }
                                        });

                                    }
                                });


                                holder.videoPostButton.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {
                                        Intent playVid = new Intent(context,PlayVideoActivity.class);
                                        playVid.putExtra("videoUrl", videoURL);
                                        startActivity(playVid);
                                    }
                                });



                            }


                            @Override
                            public void onCancelled(@NonNull DatabaseError error) {

                            }
                        });
                    }

                    @NonNull
                    @Override
                    public LakesFragment.ProjectViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.source_card_item, parent, false);

                        return new LakesFragment.ProjectViewHolder(view);
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
        public final ImageButton likePostButton;
        public final ImageButton videoPostButton;
        public final TextView displayLikes;

        //Declare an int variable to hold the count  of likes
        int countLikes;
        //Declare a string variable to hold  the user ID of currently logged in user
        String currentUserID;

        final DatabaseReference likesRef;

        public ProjectViewHolder(@NonNull View itemView) {
            super(itemView);
            //Initialize the card view item objects
            //profile_image=itemView.findViewById(R.id.userImage);
            sourceName = itemView.findViewById(R.id.sourceName);
            sourceCounty = itemView.findViewById(R.id.sourceCounty);
            sourceSubCounty = itemView.findViewById(R.id.sourceSubCounty);
            sourceWard = itemView.findViewById(R.id.sourceWard);

            sourceImage = itemView.findViewById(R.id.sourceImage);

            likePostButton = itemView.findViewById(R.id.like_button);
            videoPostButton = itemView.findViewById(R.id.watchVideo);
            displayLikes = itemView.findViewById(R.id.likes_display);


            //Initialize a database reference where you will store  the likes
            likesRef = FirebaseDatabase.getInstance().getReference().child("Likes");
        }
        public void setLikeButtonStatus ( final String post_key){
            //we want to know who has like a particular post, so let's get the user using their user_ID
            FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
            if (user != null) {
                currentUserID = user.getUid();
            }
            likesRef.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    //define post_key in the in the onBindViewHolder method
                    //check if a particular post has been liked
                    if (dataSnapshot.child(post_key).hasChild(currentUserID)) {
                        //if liked get the number of likes
                        countLikes = (int) dataSnapshot.child(post_key).getChildrenCount();
                        //check the image from initiali sislike to like
                        likePostButton.setImageResource(R.drawable.like);
                        // count the like and display them in the textView for likes
                        displayLikes.setText(Integer.toString(countLikes));
                    } else {
                        //If disliked, get the current number of likes
                        countLikes = (int) dataSnapshot.child(post_key).getChildrenCount();
                        // set the image resource as disliked
                        likePostButton.setImageResource(R.drawable.dislike);
                        //display the current number of likes
                        displayLikes.setText(Integer.toString(countLikes));
                    }

                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            });


        }

    }
}