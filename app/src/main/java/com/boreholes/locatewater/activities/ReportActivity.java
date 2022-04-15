package com.boreholes.locatewater.activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.cardview.widget.CardView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.boreholes.locatewater.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;


public class ReportActivity extends AppCompatActivity {

    CardView nationalReport;
    CardView countyReport;
    CardView subCountyReport;
    CardView wardReport;
    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_report);

        //inflate the tool bar
        Toolbar toolbar = findViewById(R.id.tool_bar);
        setSupportActionBar(toolbar);
        setToolbarTitle("");
        //Initialize the views
       nationalReport= findViewById(R.id.cardViewNational);
        countyReport = findViewById(R.id.cardViewCounty);
        subCountyReport =findViewById(R.id.cardViewSubCounty);
        wardReport=findViewById(R.id.cardViewWard);
        mAuth = FirebaseAuth.getInstance();
        FirebaseUser currentUser = mAuth.getCurrentUser();
        if (currentUser == null) {

            Intent loginIntent = new Intent(ReportActivity.this, LoginActivity.class);
            loginIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(loginIntent);
        }
        nationalReport.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FirebaseUser currentUser = mAuth.getCurrentUser();
                if (currentUser != null) {

                    Intent national = new Intent(ReportActivity.this, MyNationalReportActivity.class);
                    startActivity(national);
                }
            }
        });


        countyReport.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FirebaseUser currentUser = mAuth.getCurrentUser();
                if (currentUser != null) {

                    Intent county= new Intent(ReportActivity.this, CountyActivity.class);
                    startActivity(county);
                }


            }
        });
        subCountyReport.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FirebaseUser currentUser = mAuth.getCurrentUser();
                if (currentUser != null) {

                    Intent sub = new Intent(ReportActivity.this, SubCountyActivity.class);
                    startActivity(sub);
                }


            }
        });

        wardReport.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FirebaseUser currentUser = mAuth.getCurrentUser();
                if (currentUser != null) {

                    Intent ward = new Intent(ReportActivity.this, WardActivity.class);
                    startActivity(ward);
                }


            }
        });

    }
    public void setToolbarTitle(String Title) {
        if (getSupportActionBar() != null) {
            getSupportActionBar().setTitle(Title);
        }
    }
}