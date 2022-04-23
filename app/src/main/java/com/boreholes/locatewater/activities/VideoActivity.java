package com.boreholes.locatewater.activities;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.boreholes.locatewater.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.MediaController;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.VideoView;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Objects;

public class VideoActivity extends AppCompatActivity {
    Toolbar toolbar;
    ImageView closeBtn;
    VideoView videoView;
    TextInputEditText title, caption;
   // TextView pickCategory;
    Button postBtn;
    FloatingActionButton pickVideoFab;

    //Firebase Declarations
    FirebaseAuth mAuth;
    FirebaseDatabase mDatabase;
    FirebaseStorage mStorage;

    //Declare an instance of the Database Reference where we will save the post details
    private DatabaseReference databaseRef;

    //Declare an instance of the Database Reference where we will save the user details
    private DatabaseReference mDatabaseUsers;

    private static final int VIDEO_PICK_GALLERY_CODE = 100;
    private static final int VIDEO_PICK_CAMERA_CODE = 101;
    private static final int CAMERA_REQUEST_CODE = 102;

    private String[] cameraPermissions;

    //Uri of the picked video
    private Uri videoUri = null;

    //Progress Dialog
    private ProgressDialog progressDialog;

    private ArrayList<String> categoryTitleArrayList, categoryIdArrayList;

    private String postId, videoTitle, videoCaption;
    String post_key = null;
    String sourceType = null;
    String village = null;
    String sources =null;
    String fresh= null;
    String salty= null;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_video);
        post_key = getIntent().getExtras().getString("PostKey");
        sourceType = getIntent().getExtras().getString("sourceType");
        village =getIntent().getExtras().getString("Village");
        sources = getIntent().getExtras().getString("Sources");
        fresh = getIntent().getExtras().getString("Fresh");
        salty = getIntent().getExtras().getString("Salty");

        /*----- UI Views -----*/
        closeBtn = findViewById(R.id.closeBtn);
        videoView = findViewById(R.id.videoView);
        title = findViewById(R.id.editTxtTitle);
        caption = findViewById(R.id.editTxtCaption);
        //pickCategory = findViewById(R.id.pickCategory);
        postBtn = findViewById(R.id.postBtn);
        pickVideoFab = findViewById(R.id.pickVideoFab);


        /*----- Firebase Initialization -----*/
        mAuth = FirebaseAuth.getInstance();
        mDatabase = FirebaseDatabase.getInstance();
        mStorage = FirebaseStorage.getInstance();

        //Initialize the Database Reference
        databaseRef = FirebaseDatabase.getInstance().getReference().child(sourceType).child(post_key);

        //Get the currently logged in user
        mDatabaseUsers= FirebaseDatabase.getInstance().getReference().child("users").child(mAuth.getCurrentUser().getUid());
        /*----- Setup Progress Dialog -----*/
        progressDialog = new ProgressDialog(this);
        progressDialog.setTitle("Please Wait...");
        progressDialog.setMessage("Uploading Video");
        progressDialog.setCanceledOnTouchOutside(false);

        /*----- Toolbar -----*/
        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        //Camera permissions
        cameraPermissions = new String[]{Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE};

        /*----- Close Button -----*/
        closeBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(VideoActivity.this, SourceTypesActivity.class));
            }
        });

        /*----- Post Video Button -----*/
        postBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                videoTitle = title.getText().toString().trim();
                videoCaption = caption.getText().toString().trim();

                if(TextUtils.isEmpty(videoTitle)){
                    Toast.makeText(VideoActivity.this, "Title is required!", Toast.LENGTH_SHORT).show();
                }
                else if(TextUtils.isEmpty(videoCaption)){
                    Toast.makeText(VideoActivity.this, "Caption is required!", Toast.LENGTH_SHORT).show();
                }
                else if(videoUri == null){
                    Toast.makeText(VideoActivity.this, "Pick a video to upload!", Toast.LENGTH_SHORT).show();
                }

                else{
                    uploadVideoFirebase();
                }
            }
        });

        /*----- Pick Video Fab-----*/
        pickVideoFab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                videoPickDialog();
            }
        });

        }
    private void uploadVideoFirebase() {
        progressDialog.show();

        //Getting the DATE of the post
        java.util.Calendar calendar1 = Calendar.getInstance();
        SimpleDateFormat currentDate = new SimpleDateFormat("dd-MM-yyyy");
        String date = currentDate.format(calendar1.getTime());

        //Getting the TIME of the post
        java.util.Calendar calendar2 = Calendar.getInstance();
        SimpleDateFormat currentTime = new SimpleDateFormat("HH:mm");
        String time = currentTime.format(calendar2.getTime());

        Date myDate = new Date();
        SimpleDateFormat dateFormat = new SimpleDateFormat("E, dd MMM yyyy ");
        final String stringDate =dateFormat.format(myDate);

        Date videDate = new Date();

        SimpleDateFormat dateFor = new SimpleDateFormat("E, dd MMM yyyy HH:mm:ss z");

        String postRandomName = dateFor.format(videDate);




        StorageReference storageReference = mStorage.getReference().child("Videos").child(Objects.requireNonNull(mAuth.getUid())+postRandomName);

        storageReference.putFile(videoUri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                //If the upload of the post image was successful, get the download url
                if(taskSnapshot.getMetadata() != null){
                    if(taskSnapshot.getMetadata().getReference() != null){
                        //Get the download url from your storage using the getStorage() and getDownloadUrl() methods
                        Task<Uri> result = taskSnapshot.getStorage().getDownloadUrl();

                        //Call the addOnSuccessListener() method to determine if we got the download url
                        result.addOnSuccessListener(new OnSuccessListener<Uri>() {
                            @Override
                            public void onSuccess(Uri uri) {
                                //Convert the uri to a string
                                final String videoUrl = uri.toString();


                                //Call the push() method to publish the values of the video on the database reference
                                //final DatabaseReference newPost = databaseRef.push();

                                postId = databaseRef.getKey();

                                /*Add the post contents to the database reference
                                  Then call the addValueEventListener() method so as to set the values
                                 */

                                databaseRef.addValueEventListener(new ValueEventListener() {
                                    @Override
                                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                        databaseRef.child("videoPostId").setValue(postId);
                                        databaseRef.child("videoTitle").setValue(videoTitle);
                                        databaseRef.child("videoCaption").setValue(videoCaption);
                                        databaseRef.child("videoUrl").setValue(videoUrl);
                                        databaseRef.child("videoTime").setValue(time);
                                        databaseRef.child("videoDate").setValue(date)


                                                .addOnCompleteListener(new OnCompleteListener<Void>() {
                                                    @Override
                                                    public void onComplete(@NonNull Task<Void> task) {
                                                        if(task.isSuccessful()){

                                                            progressDialog.dismiss();
                                                            Toast.makeText(VideoActivity.this, "Video Uploaded Successfully!", Toast.LENGTH_SHORT).show();
                                                            //Launch the HomePageActivity after posting
                                                            if(sourceType.equalsIgnoreCase("Rivers")) {
                                                                Intent next = new Intent(VideoActivity.this, StepTwoRiversActivity.class);
                                                                next.putExtra("PostKey", post_key);
                                                                next.putExtra("Village", village);
                                                                next.putExtra("Sources",sources);
                                                                next.putExtra("Fresh",fresh);
                                                                next.putExtra("Salty",salty);
                                                                next.putExtra("sourceType",sourceType);
                                                                startActivity(next);
                                                                finish();
                                                            }
                                                            if(sourceType.equalsIgnoreCase("Lakes")) {
                                                                Intent next = new Intent(VideoActivity.this, StepTwoLakesActivity.class);
                                                                next.putExtra("PostKey", post_key);
                                                                next.putExtra("Village", village);
                                                                next.putExtra("Sources",sources);
                                                                next.putExtra("Fresh",fresh);
                                                                next.putExtra("Salty",salty);
                                                                next.putExtra("sourceType",sourceType);
                                                                startActivity(next);
                                                                finish();
                                                            }
                                                            if(sourceType.equalsIgnoreCase("Dams")) {
                                                                Intent next = new Intent(VideoActivity.this, StepTwoDamsActivity.class);
                                                                next.putExtra("PostKey", post_key);
                                                                next.putExtra("Village", village);
                                                                next.putExtra("Sources",sources);
                                                                next.putExtra("Fresh",fresh);
                                                                next.putExtra("Salty",salty);
                                                                next.putExtra("sourceType",sourceType);
                                                                startActivity(next);
                                                                finish();
                                                            }
                                                            if(sourceType.equalsIgnoreCase("Springs")) {
                                                                Intent next = new Intent(VideoActivity.this, StepTwoSpringsActivity.class);
                                                                next.putExtra("PostKey", post_key);
                                                                next.putExtra("Village", village);
                                                                next.putExtra("Sources",sources);
                                                                next.putExtra("Fresh",fresh);
                                                                next.putExtra("Salty",salty);
                                                                next.putExtra("sourceType",sourceType);
                                                                startActivity(next);
                                                                finish();
                                                            }
                                                            if(sourceType.equalsIgnoreCase("Boreholes")) {
                                                                Intent next = new Intent(VideoActivity.this, StepTwoBoreholesActivity.class);
                                                                next.putExtra("PostKey", post_key);
                                                                next.putExtra("Village", village);
                                                                next.putExtra("Sources",sources);
                                                                next.putExtra("Fresh",fresh);
                                                                next.putExtra("Salty",salty);
                                                                next.putExtra("sourceType",sourceType);
                                                                startActivity(next);
                                                                finish();
                                                            }
                                                            if(sourceType.equalsIgnoreCase("Taps")) {
                                                                Intent next = new Intent(VideoActivity.this, StepTwoTapsActivity.class);
                                                                next.putExtra("PostKey", post_key);
                                                                next.putExtra("Village", village);
                                                                next.putExtra("Sources",sources);
                                                                next.putExtra("Fresh",fresh);
                                                                next.putExtra("Salty",salty);
                                                                next.putExtra("sourceType",sourceType);
                                                                startActivity(next);
                                                                finish();
                                                            }
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

    }
    private void videoPickDialog() {
        //Options to display in the dialog
        String[] options = {"Camera", "Gallery"};

        //Dialog
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Pick Video From")
                .setItems(options, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        if(i == 0){
                            //Camera Clicked
                            if(!checkCameraPermissions()){
                                //If the camera permissions are not allowed, request it
                                requestCameraPermissions();
                            }
                            else{
                                //If the camera permissions are allowed, take a picture
                                videoPickCamera();
                            }
                        }
                        else if(i ==1){
                            //Gallery Clicked
                            videoPickGallery();
                        }
                        else{

                        }
                    }
                })
                .show();
    }

    private void requestCameraPermissions(){
        ActivityCompat.requestPermissions(this,cameraPermissions,CAMERA_REQUEST_CODE);
    }

    private boolean checkCameraPermissions(){
        boolean result1 = ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED;
        boolean result2 = ContextCompat.checkSelfPermission(this, Manifest.permission.WAKE_LOCK) == PackageManager.PERMISSION_GRANTED;

        return result1 && result2;
    }

    private void videoPickGallery(){
        //Pick video from gallery
        Intent gallery = new Intent(Intent.ACTION_GET_CONTENT);
        gallery.setType("video/*");
        startActivityForResult(Intent.createChooser(gallery,"Select Videos"), VIDEO_PICK_GALLERY_CODE);
    }

    private void videoPickCamera(){
        //Pick video from camera
        Intent camera = new Intent(MediaStore.ACTION_VIDEO_CAPTURE);
        startActivityForResult(camera,VIDEO_PICK_CAMERA_CODE);
    }

    private void setVideoToVideoView(){
        MediaController mediaController = new MediaController(this);
        mediaController.setAnchorView(videoView);

        //Set the MediaController to VideoView
        videoView.setMediaController(mediaController);

        videoView.setVideoURI(videoUri);
        videoView.requestFocus();
        videoView.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
            @Override
            public void onPrepared(MediaPlayer mediaPlayer) {
                videoView.start();
            }
        });
    }

    // Handling Permission Results
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode){
            case CAMERA_REQUEST_CODE:
                if(grantResults.length > 0){
                    //Checking if the camera permissions have been allowed or not
                    boolean cameraAccepted = grantResults[0] == PackageManager.PERMISSION_GRANTED;
                    boolean storageAccepted = grantResults[1] == PackageManager.PERMISSION_GRANTED;

                    if(cameraAccepted && storageAccepted){
                        //If both permissions have been allowed, take a picture
                        videoPickCamera();
                    }
                    else{
                        //If both or one of the permissions are denied
                        Toast.makeText(this, "Camera & Storage permissions are required!", Toast.LENGTH_SHORT).show();
                    }
                }
        }
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }

    //This method will get the picked video and set it in the VideoView
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(resultCode == RESULT_OK){
            if(requestCode == VIDEO_PICK_GALLERY_CODE){
                //Get the video selected
                videoUri = data.getData();
                //Show the picked video in VideoView
                setVideoToVideoView();
            }

            else if (requestCode == VIDEO_PICK_CAMERA_CODE){
                //Get the video selected
                videoUri = data.getData();
                //Show the picked video in VideoView
                setVideoToVideoView();
            }
        }
    }
}