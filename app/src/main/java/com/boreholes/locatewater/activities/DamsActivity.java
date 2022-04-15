package com.boreholes.locatewater.activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.IntentSender;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.location.Location;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.provider.Settings;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.boreholes.locatewater.BuildConfig;
import com.boreholes.locatewater.R;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.common.api.ResolvableApiException;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.LocationSettingsRequest;
import com.google.android.gms.location.LocationSettingsResponse;
import com.google.android.gms.location.LocationSettingsStatusCodes;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class DamsActivity extends AppCompatActivity implements FetchAddressTask.onTaskCompleted {
    private static final String TAG = MainActivity.class.getSimpleName();
    private static final int REQUEST_PERMISSIONS_REQUEST_CODE = 21;
    private static final int REPEAT_INTERVAL = 15;
    String latitude = null ;
    String longitude = null;

    //Views
    private TextView mLocationTextView, latTextView,longTextview;


    //location classes

    private FusedLocationProviderClient mFusedLocationClient;

    protected Location mLastLocation;
    // Constants
    private FirebaseAuth mAuth;
    private TextInputEditText numberFresh, numberSalty,nameOfVillage;
    private TextView numberSources;

    private ImageButton imageButton;
    private Uri event_image_uri = null;
    private final static int GALLERY_REQ = 1;
    private StorageReference mStorageRef;
    private FirebaseUser mCurrentUser;
    private DatabaseReference surveyRef,mDatabaseUsers,countyRef,subCountyRef,wardRef;
    private ProgressBar progressBar;
    private LinearLayout layoutTotals;
    private RelativeLayout layout;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dams);
        mAuth = FirebaseAuth.getInstance();

        numberSources = findViewById(R.id.source_total);
        numberFresh = findViewById(R.id.sources_fresh);

        numberSalty= findViewById(R.id.sources_salty);
        nameOfVillage = findViewById(R.id.villageName);
        imageButton = findViewById(R.id.imagebuttonRiver);
        mLocationTextView=findViewById(R.id.location_textView);
        latTextView=findViewById(R.id.lat_textView);
        longTextview=findViewById(R.id.long_textView);
        layoutTotals=findViewById(R.id.layout_total);
        layout = findViewById(R.id.display);


        mFusedLocationClient = LocationServices.getFusedLocationProviderClient(this);

        progressBar = new ProgressBar(this, null, android.R.attr.progressBarStyleLarge);
        RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(100, 100);
        params.addRule(RelativeLayout.CENTER_IN_PARENT);
        layout.addView(progressBar, params);
        progressBar.setVisibility(View.INVISIBLE);
        FirebaseUser currentUser = mAuth.getCurrentUser();
        if (currentUser == null) {

            Intent loginIntent = new Intent(this, LoginActivity.class);

            startActivity(loginIntent);
        }
        mStorageRef = FirebaseStorage.getInstance().getReference().child("event_images");

        surveyRef = FirebaseDatabase.getInstance().getReference().child("Survey");

        mCurrentUser = mAuth.getCurrentUser();

        mDatabaseUsers = FirebaseDatabase.getInstance().getReference().child("users");
        imageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent galleryIntent = new Intent(Intent.ACTION_GET_CONTENT);

                galleryIntent.setType("image/*");

                startActivityForResult(galleryIntent, GALLERY_REQ);
            }
        });

        LocationRequest locationRequest = LocationRequest.create();
        locationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
        LocationSettingsRequest.Builder builder = new LocationSettingsRequest.Builder()
                .addLocationRequest(locationRequest);
        Task<LocationSettingsResponse> result =
                LocationServices.getSettingsClient(DamsActivity.this).checkLocationSettings(builder.build());
        result.addOnCompleteListener(new OnCompleteListener<LocationSettingsResponse>() {
            @Override
            public void onComplete(@NonNull Task<LocationSettingsResponse> task) {
                try {
                    LocationSettingsResponse response = task.getResult(ApiException.class);

                } catch (ApiException exception) {
                    switch (exception.getStatusCode()) {
                        case LocationSettingsStatusCodes.RESOLUTION_REQUIRED:

                            try {

                                ResolvableApiException resolvable = (ResolvableApiException) exception;

                                resolvable.startResolutionForResult(DamsActivity.this,
                                        LocationRequest.PRIORITY_HIGH_ACCURACY);
                            } catch (IntentSender.SendIntentException e) {

                            } catch (ClassCastException e) {

                            }
                            break;
                        case LocationSettingsStatusCodes.SETTINGS_CHANGE_UNAVAILABLE:

                            break;
                    }
                }
            }
        });

    }


    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        final int index = 0;
        if (requestCode == REQUEST_PERMISSIONS_REQUEST_CODE) {
            if (grantResults[index] == PackageManager.PERMISSION_GRANTED) {
                getLocation();
            } else {
                showSnackbar(R.string.warning, R.string.settings,
                        view -> {
                            //send user to the settings so that allow location access
                            Intent settingsIntent = new Intent();
                            settingsIntent.setAction(
                                    Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
                            Uri uri = Uri.fromParts(getString(R.string.package_scheme),
                                    BuildConfig.APPLICATION_ID, null);
                            settingsIntent.setData(uri);
                            settingsIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                            startActivity(settingsIntent);
                        });
            }
        }
    }


    @SuppressLint("MissingPermission")
    private void getLocation() {


        if (ActivityCompat.checkSelfPermission( DamsActivity.this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
                && ActivityCompat.checkSelfPermission(DamsActivity.this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(DamsActivity.this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION},
                    AppConstants.LOCATION_REQUEST);
        }

        mFusedLocationClient.getLastLocation().addOnCompleteListener(this,task -> {
            if (task.isSuccessful() && task.getResult() != null) {
                mLastLocation = task.getResult();
                latitude = String.valueOf(mLastLocation.getLatitude());
                longitude = String.valueOf(mLastLocation.getLongitude());
                latTextView.setText(latitude);
                longTextview.setText(longitude);
                latTextView.setText(latitude);
                longTextview.setText(longitude);
                //   UploadLocationWork.setCoordinates(String.valueOf(mLastLocation.getLongitude()),String.valueOf(mLastLocation.getLatitude()));
                // Start the reverse geocode AsyncTask
                new FetchAddressTask(DamsActivity.this,
                        DamsActivity.this).execute(mLastLocation);
            } else {
                Log.w(TAG, task.getException());
            }
        });
        mLocationTextView.setText(getString(R.string.address_text,
                getString(R.string.loading),
                System.currentTimeMillis()));


    }
    private void showSnackbar(final int mainTextStringId, final int actionStringId,
                              View.OnClickListener listener) {
        Snackbar.make(getWindow().findViewById(android.R.id.content),
                getString(mainTextStringId),
                Snackbar.LENGTH_INDEFINITE)
                .setAction(getString(actionStringId), listener).show();
    }

    private boolean checkPermissions() {
        int permissionState = ActivityCompat.checkSelfPermission(DamsActivity.this,
                Manifest.permission.ACCESS_FINE_LOCATION);
        return permissionState == PackageManager.PERMISSION_GRANTED;
    }

    private void startLocationPermissionRequest() {
        ActivityCompat.requestPermissions(DamsActivity.this,
                new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                REQUEST_PERMISSIONS_REQUEST_CODE);
    }

    private void requestPermissions() {
        boolean shouldProvideRationale =
                ActivityCompat.shouldShowRequestPermissionRationale(this,
                        Manifest.permission.ACCESS_FINE_LOCATION);

        if (shouldProvideRationale) {
            showSnackbar(R.string.warning, android.R.string.ok,
                    view -> startLocationPermissionRequest());

        } else {
            startLocationPermissionRequest();
        }
    }

    @Override
    public void onTaskCompleted(String result) {

        mLocationTextView.setText(getString(R.string.address_text,
                result, System.currentTimeMillis()));


    }



    public void validate() {
        boolean valid = true;

        String fresh = numberFresh.getText().toString();
        String salty = numberSalty.getText().toString();
        String village = nameOfVillage.getText().toString();

        final String sourceType = "Dams";


        final String mLocation=mLocationTextView.getText().toString().trim();

        final String longitude = longTextview.getText().toString();
        final String latitude = latTextView.getText().toString();

        Date date = new Date();
        SimpleDateFormat dateFormat = new SimpleDateFormat("E, dd MMM yyyy ");
        final String stringDate =dateFormat.format(date);

        Date imageDate = new Date();

        SimpleDateFormat dateFor = new SimpleDateFormat("E, dd MMM yyyy HH:mm:ss z");

        String postRandomName = dateFor.format(imageDate);

        if (village.isEmpty()) {
            nameOfVillage.setError("Required");
            valid = false;
        } else {
            nameOfVillage.setError(null);
        }

        if (fresh.isEmpty()) {
            numberFresh.setError("Required");
            valid = false;
        } else {
            numberFresh.setError(null);
        }

        if (salty.isEmpty()) {
            numberSalty.setError("Required");
            valid = false;
        } else {
            numberSalty.setError(null);
        }

        if (valid) {
            int sFresh = Integer.parseInt(String.valueOf(fresh));
            int sSalty= Integer.parseInt(salty);
            int sum = sFresh+sSalty;
            String sources = String.valueOf(sum);
            numberSources.setText(sources);
            layoutTotals.setVisibility(View.VISIBLE);


            if (longitude != null) {

                if (event_image_uri != null) {
                    final ProgressDialog progressDialog = new ProgressDialog(this);
                    progressDialog.setTitle("Uploading...");
                    progressDialog.show();

                    //create Storage reference node, inside profile_image storage reference where you will save the profile image
                    StorageReference eventImagePath = mStorageRef.child("event_images").child(event_image_uri.getLastPathSegment() + postRandomName + ".jpg");
                    Bitmap bmp = null;
                    try {
                        bmp = MediaStore.Images.Media.getBitmap(getContentResolver(), event_image_uri);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }

                    ByteArrayOutputStream baos = new ByteArrayOutputStream();
                    assert bmp != null;
                    bmp.compress(Bitmap.CompressFormat.JPEG, 25, baos);
                    byte[] data = baos.toByteArray();

                    UploadTask uploadTask2 = eventImagePath.putBytes(data);


                    uploadTask2.addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                            //if the upload of the profile image was successful get the download url
                            if (taskSnapshot.getMetadata() != null) {
                                if (taskSnapshot.getMetadata().getReference() != null) {
                                    //get the download url from your storage use the methods getStorage() and getDownloadUrl()
                                    Task<Uri> result = taskSnapshot.getStorage().getDownloadUrl();

                                    result.addOnSuccessListener(new OnSuccessListener<Uri>() {
                                        @Override
                                        public void onSuccess(Uri uri) {

                                            final String eventImage = uri.toString();

                                            final DatabaseReference newSurvey= surveyRef.push();


                                            //call the method addValueEventListener to publish the additions in  the database reference of a specific user
                                            mDatabaseUsers.addValueEventListener(new ValueEventListener() {
                                                @Override
                                                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                                    //add the profilePhoto and displayName for the current user
                                                    newSurvey.child("village").setValue(village + " " + "Village");
                                                    newSurvey.child("Latitude").setValue(latitude);
                                                    newSurvey.child("Longitude").setValue(longitude);
                                                    newSurvey.child("sourceTotals").setValue(sources);
                                                    newSurvey.child("sourcesFresh").setValue(fresh);
                                                    newSurvey.child("sourcesSalty").setValue(salty);
                                                    newSurvey.child("type").setValue("Dams");

                                                    newSurvey.child("Dams").child("location").setValue(mLocation);
                                                    newSurvey.child("Dams").child("eventPhoto").setValue(eventImage);
                                                    newSurvey.child("Dams").child("village").setValue(village + " " + "Village");

                                                    newSurvey.child("date").setValue(stringDate);
                                                    newSurvey.child("UID").setValue(mCurrentUser.getUid());
                                                    newSurvey.child("filled by").setValue(dataSnapshot.child("username").getValue())
                                                            .addOnCompleteListener(new OnCompleteListener<Void>() {
                                                                @Override
                                                                public void onComplete(@NonNull Task<Void> task) {
                                                                    if (task.isSuccessful()) {

                                                                        progressDialog.dismiss();
                                                                        Toast.makeText(DamsActivity.this, "Upload Complete", Toast.LENGTH_SHORT).show();
                                                                        final String postKey = newSurvey.getKey();

                                                                        progressBar.setVisibility(View.GONE);
                                                                        Intent next = new Intent(DamsActivity.this, VideoActivity.class);
                                                                        next.putExtra("PostKey", postKey);
                                                                        next.putExtra("Village", village);
                                                                        next.putExtra("Sources",sources);
                                                                        next.putExtra("Fresh",fresh);
                                                                        next.putExtra("Salty",salty);
                                                                        next.putExtra("sourceType",sourceType);
                                                                        startActivity(next);
                                                                        finish();
                                                                    }
                                                                }
                                                            });
                                                }

                                                @Override
                                                public void onCancelled(@NonNull DatabaseError databaseError) {

                                                }
                                            });

                                        }
                                    });
                                }
                            }

                        }
                    });

                } else {
                    progressBar.setVisibility(View.GONE);
                    Toast.makeText(DamsActivity.this, "Please Add event photo", Toast.LENGTH_SHORT).show();
                }
            }else {
                progressBar.setVisibility(View.GONE);
                Toast.makeText(DamsActivity.this,"Switch on GPS", Toast.LENGTH_SHORT).show();
            }
        }
    }
    @Override
    public void onStart() {
        super.onStart();

        if (!checkPermissions()) {
            requestPermissions();
        } else {
            getLocation();
        }

        FirebaseUser currentUser = mAuth.getCurrentUser();

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fabQ1);
        if (currentUser  !=null) {


            fab.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    progressBar.setVisibility(View.VISIBLE);
                    validate();

                }
            });

        }
    }
    @Override

    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == GALLERY_REQ && resultCode == RESULT_OK) {

            event_image_uri = data.getData();

            imageButton.setImageURI(event_image_uri);
        }
    }

}