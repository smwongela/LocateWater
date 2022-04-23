package com.boreholes.locatewater.activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.boreholes.locatewater.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ProgressBar;
import android.widget.TextView;

import java.util.Map;

public class MyWardReportActivity extends AppCompatActivity {
    String ward_name = null;
    private FirebaseAuth mAuth;
    private TextView textViewWard;
    private TextView totalSources,freshSources, saltySources,
            totalRivers, freshRivers,saltyRivers,
            totalLakes, freshLakes,saltyLakes,
            totalDams, freshDams,saltyDams,
            totalSprings, freshSprings,saltySprings,
            totalBoreholes, freshBoreholes,saltyBoreholes,
            totalTaps, freshTaps,saltyTaps;


    ProgressBar totalRiverProgress,freshRiverProgress,saltyRiverProgress,
            totalLakeProgress,freshLakeProgress,saltyLakeProgress,
            totalDamProgress,freshDamProgress,saltyDamProgress,
            totalSpringProgress,freshSpringProgress,saltySpringProgress,
            totalBoreholeProgress,freshBoreholeProgress,saltyBoreholeProgress,
            totalTapProgress,freshTapProgress,saltyTapProgress,visualizerProgress;


    private DatabaseReference mVillageCounts,mRiverCounts,mLakeCounts,mDamCounts, mSpringCounts,mBoreholeCounts,mTapCounts;
    Toolbar toolbar;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_ward_report);
        //inflate the tool bar
        Toolbar toolbar = findViewById(R.id.tool_bar);
        setSupportActionBar(toolbar);
        setToolbarTitle("");
        //toolbar = findViewById(R.id.toolbar);
        //setSupportActionBar(toolbar);
        mAuth = FirebaseAuth.getInstance();
        //Initialize the views
        textViewWard = findViewById(R.id.textViewWard);
        ward_name = getIntent().getExtras().getString("WardName");
        textViewWard.setText(ward_name);

        totalSources = findViewById(R.id.total_sources);
        freshSources = findViewById(R.id.sources_fresh);
        saltySources = findViewById(R.id.sources_salty);

        totalRivers = findViewById(R.id.total_rivers);
        freshRivers = findViewById(R.id.rivers_fresh);
        saltyRivers = findViewById(R.id.rivers_salty);
        totalRiverProgress=findViewById(R.id.progress_total_rivers);
        freshRiverProgress=findViewById(R.id.progress_fresh_rivers);
        saltyRiverProgress = findViewById(R.id.progress_salty_rivers);

        totalLakes = findViewById(R.id.total_lakes);
        freshLakes = findViewById(R.id.lakes_fresh);
        saltyLakes = findViewById( R.id.lakes_salty);
        totalLakeProgress = findViewById(R.id.progress_total_lakes);
        freshLakeProgress = findViewById(R.id.progress_fresh_lakes);
        saltyLakeProgress = findViewById(R.id.progress_salty_lakes);

        totalDams = findViewById(R.id.total_dams);
        freshDams = findViewById(R.id.dams_fresh);
        saltyDams = findViewById(R.id.dams_salty);
        totalDamProgress =findViewById(R.id.progress_total_dams);
        freshDamProgress = findViewById(R.id.progress_fresh_dams);
        saltyDamProgress = findViewById(R.id.progress_salty_dams);

        totalSprings = findViewById(R.id.total_springs);
        freshSprings = findViewById(R.id.springs_fresh);
        saltySprings = findViewById(R.id.springs_salty);
        totalSpringProgress= findViewById(R.id.progress_total_springs);
        freshSpringProgress = findViewById(R.id.progress_fresh_springs);
        saltySpringProgress=findViewById(R.id.progress_salty_springs);

        totalBoreholes = findViewById(R.id.total_boreholes);
        freshBoreholes = findViewById(R.id.boreholes_fresh);
        saltyBoreholes = findViewById(R.id.borholes_salty);
        totalBoreholeProgress=findViewById(R.id.progress_total_boreholes);
        freshBoreholeProgress=findViewById(R.id.progress_fresh_boreholes);
        saltyBoreholeProgress = findViewById(R.id.progress_salty_boreholes);

        totalTaps=findViewById(R.id.total_taps);
        freshTaps=findViewById(R.id.taps_fresh);
        saltyTaps= findViewById(R.id.taps_salty);
        totalTapProgress = findViewById(R.id.progress_total_taps);
        freshTapProgress = findViewById(R.id.progress_fresh_taps);
        saltyTapProgress = findViewById(R.id.progress_salty_taps);

        visualizerProgress = findViewById(R.id.progress_bar_target);







        FirebaseUser currentUser = mAuth.getCurrentUser();
        if (currentUser == null) {

            Intent loginIntent = new Intent(MyWardReportActivity.this, LoginActivity.class);
            loginIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(loginIntent);
        }

        updateCountyTotal();
        updateRiverTotal();
        updateLakeTotal();
        updateDamTotal();
        updateSpringTotal();
        updateBoreholeTotal();
        updateTapTotal();

    }

    @Override
    protected void onStart() {
        //
        super.onStart();

    }
    private void updateCountyTotal() {
        mVillageCounts = FirebaseDatabase.getInstance().getReference().child("Wards").child(ward_name);
        mVillageCounts.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                int sumTotal = 0;
                int sumFresh = 0;
                int sumSalty = 0;

                for (DataSnapshot ds : snapshot.getChildren()){

                    Map<String,Object> map;
                    map = (Map<String, Object>) ds.getValue();
                    String total = "sourceTotal";
                    String fresh = "sourcesFresh";
                    String salty = "sourcesSalty";
                    Object sourceTotal = map.get(total);
                    Object sourcesFresh = map.get(fresh);
                    Object sourcesSalty = map.get(salty);

                    if (sourceTotal!=null) {
                        int tValue = Integer.parseInt(String.valueOf(sourceTotal));
                        sumTotal += tValue;
                        totalSources.setText(String.valueOf(sumTotal));
                        visualizerProgress.setProgress(sumTotal);
                        int fValue = Integer.parseInt(String.valueOf(sourcesFresh));
                        sumFresh += fValue;
                        freshSources.setText(String.valueOf(sumFresh));

                        int sValue = Integer.parseInt(String.valueOf(sourcesSalty));
                        sumSalty += sValue;
                        saltySources.setText(String.valueOf(sumSalty));
                    }else{
                        totalSources.setText("0");
                        freshSources.setText("0");
                        saltySources.setText("0");

                    }




                }
            }


            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

    }
    private void updateRiverTotal() {
        mRiverCounts = FirebaseDatabase.getInstance().getReference().child("Wards").child(ward_name);
        mRiverCounts.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                int sumTotal = 0;
                int sumFresh = 0;
                int sumSalty = 0;

                for (DataSnapshot ds : snapshot.getChildren()){

                    Map<String,Object> map;
                    map = (Map<String, Object>) ds.getValue();
                    String total = "totalRivers";
                    String fresh = "riversFresh";
                    String salty = "riversSalty";
                    Object sourceTotal = map.get(total);
                    Object sourcesFresh = map.get(fresh);
                    Object sourcesSalty = map.get(salty);
                    if (sourceTotal!=null) {
                        int tValue = Integer.parseInt(String.valueOf(sourceTotal));
                        sumTotal += tValue;
                        totalRivers.setText(String.valueOf(sumTotal));
                        totalRiverProgress.setProgress(sumTotal);
                        int fValue = Integer.parseInt(String.valueOf(sourcesFresh));
                        sumFresh += fValue;
                        freshRivers.setText(String.valueOf(sumFresh));
                        freshRiverProgress.setProgress(sumFresh);
                        int sValue = Integer.parseInt(String.valueOf(sourcesSalty));
                        sumSalty += sValue;
                        saltyRivers.setText(String.valueOf(sumSalty));
                        saltyRiverProgress.setProgress(sumSalty);
                    }else{
                        totalRivers.setText("0");
                        freshRivers.setText("0");
                        saltyRivers.setText("0");

                    }

                }
            }


            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });


    }
    private void updateLakeTotal() {
        mLakeCounts = FirebaseDatabase.getInstance().getReference().child("Wards").child(ward_name);
        mLakeCounts.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                int sumTotal = 0;
                int sumFresh = 0;
                int sumSalty = 0;

                for (DataSnapshot ds : snapshot.getChildren()){

                    Map<String,Object> map;
                    map = (Map<String, Object>) ds.getValue();
                    String total = "totalLakes";
                    String fresh = "lakesFresh";
                    String salty = "lakesSalty";
                    Object sourceTotal = map.get(total);
                    Object sourcesFresh = map.get(fresh);
                    Object sourcesSalty = map.get(salty);
                    if (sourceTotal!=null) {
                        int tValue = Integer.parseInt(String.valueOf(sourceTotal));
                        sumTotal += tValue;
                        totalLakes.setText(String.valueOf(sumTotal));
                        totalLakeProgress.setProgress(sumTotal);
                        int fValue = Integer.parseInt(String.valueOf(sourcesFresh));
                        sumFresh += fValue;
                        freshLakes.setText(String.valueOf(sumFresh));
                        freshLakeProgress.setProgress(sumFresh);
                        int sValue = Integer.parseInt(String.valueOf(sourcesSalty));
                        sumSalty += sValue;
                        saltyLakes.setText(String.valueOf(sumSalty));
                        saltyLakeProgress.setProgress(sumSalty);
                    }else{
                        totalLakes.setText("0");
                        freshLakes.setText("0");
                        saltyLakes.setText("0");

                    }

                }
            }


            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });


    }
    private void updateDamTotal() {
        mDamCounts = FirebaseDatabase.getInstance().getReference().child("Wards").child(ward_name);
        mDamCounts.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                int sumTotal = 0;
                int sumFresh = 0;
                int sumSalty = 0;

                for (DataSnapshot ds : snapshot.getChildren()){

                    Map<String,Object> map;
                    map = (Map<String, Object>) ds.getValue();
                    String total = "totalDams";
                    String fresh = "damsFresh";
                    String salty = "damsSalty";
                    Object sourceTotal = map.get(total);
                    Object sourcesFresh = map.get(fresh);
                    Object sourcesSalty = map.get(salty);
                    if (sourceTotal!=null) {
                        int tValue = Integer.parseInt(String.valueOf(sourceTotal));
                        sumTotal += tValue;
                        totalDams.setText(String.valueOf(sumTotal));
                        totalDamProgress.setProgress(sumTotal);
                        int fValue = Integer.parseInt(String.valueOf(sourcesFresh));
                        sumFresh += fValue;
                        freshDams.setText(String.valueOf(sumFresh));
                        freshDamProgress.setProgress(sumFresh);
                        int sValue = Integer.parseInt(String.valueOf(sourcesSalty));
                        sumSalty += sValue;
                        saltyDams.setText(String.valueOf(sumSalty));
                        saltyDamProgress.setProgress(sumSalty);
                    }else{
                        totalDams.setText("0");
                        freshDams.setText("0");
                        saltyDams.setText("0");

                    }

                }
            }


            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });


    }
    private void updateSpringTotal() {
        mSpringCounts = FirebaseDatabase.getInstance().getReference().child("Wards").child(ward_name);
        mSpringCounts.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                int sumTotal = 0;
                int sumFresh = 0;
                int sumSalty = 0;

                for (DataSnapshot ds : snapshot.getChildren()){

                    Map<String,Object> map;
                    map = (Map<String, Object>) ds.getValue();
                    String total = "totalSprings";
                    String fresh = "springsFresh";
                    String salty = "springSalty";
                    Object sourceTotal = map.get(total);
                    Object sourcesFresh = map.get(fresh);
                    Object sourcesSalty = map.get(salty);
                    if (sourceTotal!=null) {
                        int tValue = Integer.parseInt(String.valueOf(sourceTotal));
                        sumTotal += tValue;
                        totalSprings.setText(String.valueOf(sumTotal));
                        totalSpringProgress.setProgress(sumTotal);
                        int fValue = Integer.parseInt(String.valueOf(sourcesFresh));
                        sumFresh += fValue;
                        freshSprings.setText(String.valueOf(sumFresh));
                        freshSpringProgress.setProgress(sumFresh);
                        int sValue = Integer.parseInt(String.valueOf(sourcesSalty));
                        sumSalty += sValue;
                        saltySprings.setText(String.valueOf(sumSalty));
                        saltySpringProgress.setProgress(sumSalty);
                    }else{
                        totalSprings.setText("0");
                        freshSprings.setText("0");
                        saltySprings.setText("0");

                    }

                }
            }


            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });


    }
    private void updateBoreholeTotal() {
        mBoreholeCounts = FirebaseDatabase.getInstance().getReference().child("Ward").child(ward_name);
        mBoreholeCounts.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                int sumTotal = 0;
                int sumFresh = 0;
                int sumSalty = 0;

                for (DataSnapshot ds : snapshot.getChildren()){

                    Map<String,Object> map;
                    map = (Map<String, Object>) ds.getValue();
                    String total = "totalBoreholes";
                    String fresh = "boreholesFresh";
                    String salty = "boreholesSalty";
                    Object sourceTotal = map.get(total);
                    Object sourcesFresh = map.get(fresh);
                    Object sourcesSalty = map.get(salty);
                    if (sourceTotal!=null) {
                        int tValue = Integer.parseInt(String.valueOf(sourceTotal));
                        sumTotal += tValue;
                        totalBoreholes.setText(String.valueOf(sumTotal));
                        totalBoreholeProgress.setProgress(sumTotal);
                        int fValue = Integer.parseInt(String.valueOf(sourcesFresh));
                        sumFresh += fValue;
                        freshBoreholes.setText(String.valueOf(sumFresh));
                        freshBoreholeProgress.setProgress(sumFresh);
                        int sValue = Integer.parseInt(String.valueOf(sourcesSalty));
                        sumSalty += sValue;
                        saltyBoreholes.setText(String.valueOf(sumSalty));
                        saltyBoreholeProgress.setProgress(sumSalty);
                    }else{
                        totalBoreholes.setText("0");
                        freshBoreholes.setText("0");
                        saltyBoreholes.setText("0");

                    }

                }
            }


            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });


    }
    private void updateTapTotal() {
        mTapCounts = FirebaseDatabase.getInstance().getReference().child("Wards").child(ward_name);
        mTapCounts.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                int sumTotal = 0;
                int sumFresh = 0;
                int sumSalty = 0;

                for (DataSnapshot ds : snapshot.getChildren()){

                    Map<String,Object> map;
                    map = (Map<String, Object>) ds.getValue();
                    String total = "totalTaps";
                    String fresh = "tapsFresh";
                    String salty = "tapsSalty";
                    Object sourceTotal = map.get(total);
                    Object sourcesFresh = map.get(fresh);
                    Object sourcesSalty = map.get(salty);
                    if (sourceTotal!=null) {
                        int tValue = Integer.parseInt(String.valueOf(sourceTotal));
                        sumTotal += tValue;
                        totalTaps.setText(String.valueOf(sumTotal));
                        totalTapProgress.setProgress(sumTotal);
                        int fValue = Integer.parseInt(String.valueOf(sourcesFresh));
                        sumFresh += fValue;
                        freshTaps.setText(String.valueOf(sumFresh));
                        freshTapProgress.setProgress(sumFresh);
                        int sValue = Integer.parseInt(String.valueOf(sourcesSalty));
                        sumSalty += sValue;
                        saltyTaps.setText(String.valueOf(sumSalty));
                        saltyTapProgress.setProgress(sumSalty);
                    }else{
                        totalTaps.setText("0");
                        freshTaps.setText("0");
                        saltyTaps.setText("0");

                    }

                }
            }


            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });


    }


    public void setToolbarTitle(String Title) {
        if (getSupportActionBar() != null) {
            getSupportActionBar().setTitle(Title);
        }
    }



}
