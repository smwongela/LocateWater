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

public class UploadBoreholeSubtotals  extends Worker {
    //private static final String TAG = UploadRiverSubtotals.class.getSimpleName();
    private static final String PATTERN_DATE_TIME = "yyyyMMdd_HHmmss";
    private static final String PATH = "/";
    private static String sources,fresh, salty, county, subCounty, ward;
    private static String mLongitude;
    private DatabaseReference surveyRef,mDatabaseUsers,countyRef,subCountyRef,wardRef;
    private FirebaseAuth mAuth;
    private FirebaseUser mCurrentUser;

    public UploadBoreholeSubtotals(@NonNull Context context, @NonNull WorkerParameters workerParams) {
        super(context, workerParams);
    }

    public static void setBoreholesData(String sources, String fresh,String salty,String county, String subCounty, String ward) {
        UploadBoreholeSubtotals.sources = sources;
        UploadBoreholeSubtotals.fresh = fresh;
        UploadBoreholeSubtotals.salty =salty;
        UploadBoreholeSubtotals.county =county;
        UploadBoreholeSubtotals.subCounty=subCounty;
        UploadBoreholeSubtotals.ward=ward;
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
                    newCounty.child("totalDams").setValue("0");
                    newCounty.child("damsFresh").setValue("0");
                    newCounty.child("damsSalty").setValue("0");
                    newCounty.child("totalBoreholes").setValue(sources);
                    newCounty.child("boreholesFresh").setValue(fresh);
                    newCounty.child("boreholesSalty").setValue(salty);
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
                    newSubCounty.child("totalDams").setValue("0");
                    newSubCounty.child("damsFresh").setValue("0");
                    newSubCounty.child("damsSalty").setValue("0");
                    newSubCounty.child("totalBoreholes").setValue(sources);
                    newSubCounty.child("boreholesFresh").setValue(fresh);
                    newSubCounty.child("boreholesSalty").setValue(salty);
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
                    newWard.child("totalDams").setValue("0");
                    newWard.child("damsFresh").setValue("0");
                    newWard.child("damsSalty").setValue("0");
                    newWard.child("totalBoreholes").setValue(sources);
                    newWard.child("boreholesFresh").setValue(fresh);
                    newWard.child("boreholesSalty").setValue(salty);
                    newWard.child("totalTaps").setValue("0");
                    newWard.child("tapsFresh").setValue("0");
                    newWard.child("tapsSalty").setValue("0");

                }
                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }
            });



        }
    }
}


