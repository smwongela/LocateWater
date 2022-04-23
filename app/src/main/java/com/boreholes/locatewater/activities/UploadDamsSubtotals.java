package com.boreholes.locatewater.activities;

import android.content.Context;

import androidx.annotation.NonNull;
import androidx.work.ListenableWorker;
import androidx.work.Worker;
import androidx.work.WorkerParameters;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class UploadDamsSubtotals extends Worker {
    //private static final String TAG = UploadRiverSubtotals.class.getSimpleName();
    private static final String PATTERN_DATE_TIME = "yyyyMMdd_HHmmss";
    private static final String PATH = "/";
    private static String sources,fresh, salty, county, subCounty, ward;
    private static String mLongitude;
    private DatabaseReference surveyRef,mDatabaseUsers,countyRef,subCountyRef,wardRef,nationalRef;
    private FirebaseAuth mAuth;
    private FirebaseUser mCurrentUser;

    public UploadDamsSubtotals(@NonNull Context context, @NonNull WorkerParameters workerParams) {
        super(context, workerParams);
    }

    public static void setDamsData(String sources, String fresh,String salty,String county, String subCounty, String ward) {
        UploadDamsSubtotals.sources = sources;
        UploadDamsSubtotals.fresh = fresh;
        UploadDamsSubtotals.salty =salty;
        UploadDamsSubtotals.county =county;
        UploadDamsSubtotals.subCounty=subCounty;
        UploadDamsSubtotals.ward=ward;
    }

    @NonNull
    @Override
    public ListenableWorker.Result doWork() {
        uploadsubTotalsToFirebase();
        return ListenableWorker.Result.success();
    }

    private void uploadsubTotalsToFirebase() {
        mAuth = FirebaseAuth.getInstance();
        if (sources != null && county != null && subCounty !=null) {

            countyRef = FirebaseDatabase.getInstance().getReference().child("Counties");
            subCountyRef =FirebaseDatabase.getInstance().getReference().child("SubCounties");
            wardRef =FirebaseDatabase.getInstance().getReference().child("Wards");
            nationalRef = FirebaseDatabase.getInstance().getReference().child("Kenya");
            mDatabaseUsers= FirebaseDatabase.getInstance().getReference().child("users").child(mAuth.getCurrentUser().getUid());

            final  DatabaseReference newCounty = countyRef.child(county).push();

            countyRef.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    // countyRef.child("myCounties").setValue(county);
                    newCounty.child("subcounty").setValue(subCounty);

                    newCounty.child("sourceTotal").setValue(sources);
                    newCounty.child("sourcesFresh").setValue(fresh);
                    newCounty.child("sourcesSalty").setValue(salty);
                    newCounty.child("totalRivers").setValue("0");
                    newCounty.child("riversFresh").setValue("0");
                    newCounty.child("riversSalty").setValue("0");
                    newCounty.child("totalLakes").setValue("0");
                    newCounty.child("lakesFresh").setValue("0");
                    newCounty.child("lakesSalty").setValue("0");
                    newCounty.child("totalSprings").setValue("0");
                    newCounty.child("springsFresh").setValue("0");
                    newCounty.child("springSalty").setValue("0");
                    newCounty.child("totalDams").setValue(sources);
                    newCounty.child("damsFresh").setValue(fresh);
                    newCounty.child("damsSalty").setValue(salty);
                    newCounty.child("totalBoreholes").setValue("0");
                    newCounty.child("boreholesFresh").setValue("0");
                    newCounty.child("boreholesSalty").setValue("0");
                    newCounty.child("totalTaps").setValue("0");
                    newCounty.child("tapsFresh").setValue("0");
                    newCounty.child("tapsSalty").setValue("0");

                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }
            });

            final  DatabaseReference newSubCounty = subCountyRef.child(subCounty).push();

            subCountyRef.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    // countyRef.child("myCounties").setValue(county);
                    newSubCounty.child("subcounty").setValue(ward);

                    newSubCounty.child("sourceTotal").setValue(sources);
                    newSubCounty.child("sourcesFresh").setValue(fresh);
                    newSubCounty.child("sourcesSalty").setValue(salty);
                    newSubCounty.child("totalRivers").setValue("0");
                    newSubCounty.child("riversFresh").setValue("0");
                    newSubCounty.child("riversSalty").setValue("0");
                    newSubCounty.child("totalLakes").setValue("0");
                    newSubCounty.child("lakesFresh").setValue("0");
                    newSubCounty.child("lakesSalty").setValue("0");
                    newSubCounty.child("totalSprings").setValue("0");
                    newSubCounty.child("springsFresh").setValue("0");
                    newSubCounty.child("springSalty").setValue("0");
                    newSubCounty.child("totalDams").setValue(sources);
                    newSubCounty.child("damsFresh").setValue(fresh);
                    newSubCounty.child("damsSalty").setValue(salty);
                    newSubCounty.child("totalBoreholes").setValue("0");
                    newSubCounty.child("boreholesFresh").setValue("0");
                    newSubCounty.child("boreholesSalty").setValue("0");
                    newSubCounty.child("totalTaps").setValue("0");
                    newSubCounty.child("tapsFresh").setValue("0");
                    newSubCounty.child("tapsSalty").setValue("0");

                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }
            });
            final  DatabaseReference newWard = wardRef.child(ward).push();

            wardRef.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    // countyRef.child("myCounties").setValue(county);
                    newWard.child("subcounty").setValue(subCounty);

                    newWard.child("sourceTotal").setValue(sources);
                    newWard.child("sourcesFresh").setValue(fresh);
                    newWard.child("sourcesSalty").setValue(salty);
                    newWard.child("totalRivers").setValue("0");
                    newWard.child("riversFresh").setValue("0");
                    newWard.child("riversSalty").setValue("0");
                    newWard.child("totalLakes").setValue("0");
                    newWard.child("lakesFresh").setValue("0");
                    newWard.child("lakesSalty").setValue("0");
                    newWard.child("totalSprings").setValue("0");
                    newWard.child("springsFresh").setValue("0");
                    newWard.child("springSalty").setValue("0");
                    newWard.child("totalDams").setValue(sources);
                    newWard.child("damsFresh").setValue(fresh);
                    newWard.child("damsSalty").setValue(salty);
                    newWard.child("totalBoreholes").setValue("0");
                    newWard.child("boreholesFresh").setValue("0");
                    newWard.child("boreholesSalty").setValue("0");
                    newWard.child("totalTaps").setValue("0");
                    newWard.child("tapsFresh").setValue("0");
                    newWard.child("tapsSalty").setValue("0");

                }
                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }
            });

            final  DatabaseReference newNation = nationalRef.child("Kenya").push();

            nationalRef.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    // countyRef.child("myCounties").setValue(county);
                    newNation.child("subcounty").setValue(subCounty);

                    newNation.child("sourceTotal").setValue(sources);
                    newNation.child("sourcesFresh").setValue(fresh);
                    newNation.child("sourcesSalty").setValue(salty);
                    newNation.child("totalRivers").setValue("0");
                    newNation.child("riversFresh").setValue("0");
                    newNation.child("riversSalty").setValue("0");
                    newNation.child("totalLakes").setValue("0");
                    newNation.child("lakesFresh").setValue("0");
                    newNation.child("lakesSalty").setValue("0");
                    newNation.child("totalSprings").setValue("0");
                    newNation.child("springsFresh").setValue("0");
                    newNation.child("springSalty").setValue("0");
                    newNation.child("totalDams").setValue(sources);
                    newNation.child("damsFresh").setValue(fresh);
                    newNation.child("damsSalty").setValue(salty);
                    newNation.child("totalBoreholes").setValue("0");
                    newNation.child("boreholesFresh").setValue("0");
                    newNation.child("boreholesSalty").setValue("0");
                    newNation.child("totalTaps").setValue("0");
                    newNation.child("tapsFresh").setValue("0");
                    newNation.child("tapsSalty").setValue("0");

                }
                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }
            });

        }
    }
}
