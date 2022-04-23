package com.boreholes.locatewater.activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.cardview.widget.CardView;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import android.app.Activity;
import android.content.Intent;
import android.content.IntentSender;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.boreholes.locatewater.R;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.common.api.ResolvableApiException;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.LocationSettingsRequest;
import com.google.android.gms.location.LocationSettingsResponse;
import com.google.android.gms.location.LocationSettingsStatusCodes;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class MainActivity extends AppCompatActivity  implements NavigationView.OnNavigationItemSelectedListener {
    CardView addSource;
    CardView viewMap;
    CardView listSources;
    CardView createReport;
    private FirebaseAuth mAuth;


        NavigationView navigationView;
        DrawerLayout drawer;
        @Override
        protected void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            setContentView(R.layout.activity_main);

            Toolbar toolbar = findViewById(R.id.tool_bar);
            setSupportActionBar(toolbar);
            toolbar.setNavigationIcon(R.drawable.burger);
            setToolbarTitle("");
            addSource = findViewById(R.id.cardViewSource);
            viewMap = findViewById(R.id.cardViewMap);
            listSources =findViewById(R.id.cardViewList);
            createReport=findViewById(R.id.cardViewReports);


            mAuth = FirebaseAuth.getInstance();
            FirebaseUser currentUser = mAuth.getCurrentUser();
            if (currentUser == null) {

                Intent loginIntent = new Intent(MainActivity.this, LoginActivity.class);
                loginIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(loginIntent);

            }

            navigationView = findViewById(R.id.nav_view);
            drawer = findViewById(R.id.drawer_layout);

            navigationView.setNavigationItemSelectedListener(this);
            navigationView.getMenu().getItem(0).setChecked(true);

            addSource.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    FirebaseUser currentUser = mAuth.getCurrentUser();
                    if (currentUser != null) {

                        Intent sourceType = new Intent(MainActivity.this, SourceTypesActivity.class);
                        startActivity(sourceType);
                    }
                }
            });


            viewMap.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    FirebaseUser currentUser = mAuth.getCurrentUser();
                    if (currentUser != null) {

                        Intent sourceType = new Intent(MainActivity.this, MapActivity.class);
                        startActivity(sourceType);
                    }


                }
            });
            listSources.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    FirebaseUser currentUser = mAuth.getCurrentUser();
                    if (currentUser != null) {

                        Intent sourceType = new Intent(MainActivity.this, SourceListActivity.class);
                        startActivity(sourceType);
                    }


                }
            });

            createReport.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    FirebaseUser currentUser = mAuth.getCurrentUser();
                    if (currentUser != null) {

                        Intent sourceType = new Intent(MainActivity.this, ReportActivity.class);
                        startActivity(sourceType);
                    }


                }
            });
            LocationRequest locationRequest = LocationRequest.create();
            locationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
            LocationSettingsRequest.Builder builder = new LocationSettingsRequest.Builder()
                    .addLocationRequest(locationRequest);
            Task<LocationSettingsResponse> result =
                    LocationServices.getSettingsClient(MainActivity.this).checkLocationSettings(builder.build());
            result.addOnCompleteListener(new OnCompleteListener<LocationSettingsResponse>() {
                @Override
                public void onComplete(@NonNull Task<LocationSettingsResponse> task) {
                    try {
                        LocationSettingsResponse response = task.getResult(ApiException.class);
                        // All location settings are satisfied. The client can initialize location
                        // requests here.
                    } catch (ApiException exception) {
                        switch (exception.getStatusCode()) {
                            case LocationSettingsStatusCodes.RESOLUTION_REQUIRED:
                                // Location settings are not satisfied. But could be fixed by showing the
                                // user a dialog.
                                try {
                                    // Cast to a resolvable exception.
                                    ResolvableApiException resolvable = (ResolvableApiException) exception;
                                    // Show the dialog by calling startResolutionForResult(),
                                    // and check the result in onActivityResult().
                                    resolvable.startResolutionForResult(
                                            MainActivity.this,
                                            LocationRequest.PRIORITY_HIGH_ACCURACY);
                                } catch (IntentSender.SendIntentException e) {
                                    // Ignore the error.
                                } catch (ClassCastException e) {
                                    // Ignore, should be an impossible error.
                                }
                                break;
                            case LocationSettingsStatusCodes.SETTINGS_CHANGE_UNAVAILABLE:
                                // Location settings are not satisfied. However, we have no way to fix the
                                // settings so we won't show the dialog.
                                break;
                        }
                    }
                }
            });


        }
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case LocationRequest.PRIORITY_HIGH_ACCURACY:
                switch (resultCode) {
                    case Activity.RESULT_OK:
                        // All required changes were successfully made
                        Log.i("gps", "onActivityResult: GPS Enabled by user");
                        break;
                    case Activity.RESULT_CANCELED:
                        // The user was asked to change settings, but chose not to
                        Log.i("gps", "onActivityResult: User rejected GPS request");
                        break;
                    default:
                        break;
                }
                break;
        }
    }


        private void showToast(String s) {
            Toast.makeText(getApplicationContext(),s,Toast.LENGTH_SHORT).show();
        }

        @Override
        public boolean onOptionsItemSelected(MenuItem item) {
            int id = item.getItemId();

            //noinspection SimplifiableIfStatement
            switch (item.getItemId()) {
                case android.R.id.home:
                    drawer.openDrawer(navigationView);
                    return true;
            }
            return super.onOptionsItemSelected(item);
        }

        @Override
        public boolean onNavigationItemSelected(MenuItem item) {
            // Handle navigation view item clicks here.
            int id = item.getItemId();
            switch (id) {
                case R.id.home:
                    break;

                case R.id.sourceAdd:
              startActivity(new Intent(MainActivity.this, SourceTypesActivity.class));
                    break;
                case R.id.goMap:
                    startActivity(new Intent(MainActivity.this, MapActivity.class));
                    break;

                case R.id.viewSources:
                    startActivity(new Intent(MainActivity.this, SourceListActivity.class));
            }
            drawer.closeDrawer(GravityCompat.START);
            return true;
        }
        public void setToolbarTitle(String Title) {
            if (getSupportActionBar() != null) {
                getSupportActionBar().setTitle(Title);
            }
        }




}


