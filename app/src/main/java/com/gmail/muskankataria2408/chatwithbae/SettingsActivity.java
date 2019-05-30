//package com.gmail.muskankataria2408.chatwithbae;
//
//import android.app.ProgressDialog;
//import android.content.Intent;
//import android.graphics.Bitmap;
//import android.net.Uri;
//import android.support.annotation.NonNull;
//import android.support.v7.app.AppCompatActivity;
//import android.os.Bundle;
//import android.view.View;
//import android.widget.Button;
//import android.widget.TextView;
//import android.widget.Toast;
//
//import com.google.android.gms.tasks.OnCompleteListener;
//import com.google.android.gms.tasks.Task;
//import com.google.firebase.auth.FirebaseAuth;
//import com.google.firebase.auth.FirebaseUser;
//import com.google.firebase.database.DataSnapshot;
//import com.google.firebase.database.DatabaseError;
//import com.google.firebase.database.DatabaseReference;
//import com.google.firebase.database.FirebaseDatabase;
//import com.google.firebase.database.ValueEventListener;
//import com.google.firebase.storage.FirebaseStorage;
//import com.google.firebase.storage.StorageReference;
//import com.google.firebase.storage.StorageTask;
//import com.google.firebase.storage.UploadTask;
//import com.squareup.picasso.Picasso;
//import com.theartofdev.edmodo.cropper.CropImage;
//
//import java.io.ByteArrayOutputStream;
//import java.io.File;
//import java.io.IOException;
//import java.util.HashMap;
//import java.util.Map;
//
//import de.hdodenhof.circleimageview.CircleImageView;
//import id.zelory.compressor.Compressor;
//
//public class SettingsActivity extends AppCompatActivity {
//
//
//    private DatabaseReference mUserDatabase;
//    private FirebaseUser mCurrentUser;
//    private CircleImageView mDisplayImage;
//    private TextView mName;
//    private TextView mStatus;
//    private final static int GALLERY_PICK=1;
//    String status_value;
//
//    //Storage Firebase
//    private StorageReference mImageStorage;
//
//    private ProgressDialog mProgressDailog;
//
//
//    @Override
//    protected void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_settings);
//
//        mDisplayImage=(CircleImageView)findViewById(R.id.settings_image);
//        mName=(TextView)findViewById(R.id.settings_name);
//        mStatus=(TextView)findViewById(R.id.settings_status);
//        Button mImageBtn = (Button) findViewById(R.id.settings_image_btn);
//        Button mStatusBtn = (Button) findViewById(R.id.settings_status_btn);
//        mImageStorage= FirebaseStorage.getInstance().getReference();
//        mCurrentUser= FirebaseAuth.getInstance().getCurrentUser();
//        String current_user_id=mCurrentUser.getUid();
//        //String current_uid= mCurrentUser.getUid();
//       mUserDatabase= FirebaseDatabase.getInstance().getReference().child("Users").child(current_user_id);
//       mUserDatabase.keepSynced(true);
//        mUserDatabase.addValueEventListener(new ValueEventListener() {
//            @Override
//            public void onDataChange(DataSnapshot dataSnapshot) {
//               //checking
//                // Toast.makeText(SettingsActivity.this, dataSnapshot.toString(), Toast.LENGTH_LONG).show();
//                String name=dataSnapshot.child("name").getValue().toString();
//                String image=dataSnapshot.child("image").getValue().toString();
//                String status=dataSnapshot.child("status").getValue().toString();
//                String thumb_image=dataSnapshot.child("thumb_image").getValue().toString();
//
//                mName.setText(name.toUpperCase());
//                mStatus.setText(status);
//                    if(!image.equals("default")) {
//                        Picasso.with(SettingsActivity.this).load(image).placeholder(R.drawable.default_avatar).into(mDisplayImage);
//                    }
//
//                Picasso.with(SettingsActivity.this).load(image).placeholder(R.drawable.default_avatar).into(mDisplayImage);
//
//            }
//
//            @Override
//            public void onCancelled(DatabaseError databaseError) {
//
//            }
//        });
//
//        mStatusBtn.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//
//                status_value=mStatus.getText().toString();
//                Intent status_Intent=new Intent(SettingsActivity.this, StatusActivity.class);
//                status_Intent.putExtra("status_value", status_value);
//                startActivity(status_Intent);
//            }
//        });
//
//        mImageBtn.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                Intent gallery_intent=new Intent();
//                gallery_intent.setType("image/*");
//                gallery_intent.setAction(Intent.ACTION_GET_CONTENT);
//                startActivityForResult(Intent.createChooser(gallery_intent,"SELECT IMAGE"),GALLERY_PICK);
//
////                CropImage.activity()
////                        .setGuidelines(CropImageView.Guidelines.ON)
////                        .start(SettingsActivity.this);
//
//            }
//        });
//    }
//
//    @Override
//    protected void onActivityResult(int requestCode, final int resultCode, final Intent data) {
//        super.onActivityResult(requestCode, resultCode, data);
//
//        if(requestCode==GALLERY_PICK && resultCode==RESULT_OK)
//        {
//           Uri imageUri=data.getData();
//            CropImage.activity(imageUri)
//                    .setAspectRatio(1,1)
//                    .start(this);
//
//            // Toast.makeText(SettingsActivity.this, imageUri,Toast.LENGTH_LONG).show();
//        }
//        if (requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE) {
//            final CropImage.ActivityResult result = CropImage.getActivityResult(data);
//            if (resultCode == RESULT_OK) {
//                 mProgressDailog=new ProgressDialog(SettingsActivity.this);
//                 mProgressDailog.setTitle("Uploadig Image...");
//                 mProgressDailog.setMessage("please wait while we upload and process the image.");
//                 mProgressDailog.setCanceledOnTouchOutside(false);
//                 mProgressDailog.show();
//
//
//                Uri resultUri = result.getUri();
//
//                final File thumb_filePath=new File(resultUri.getPath());
//
//                String current_user_id=mCurrentUser.getUid();
//
//                StorageReference filepath=mImageStorage.child("profile_images").child(current_user_id+".jpg");
//
//                final StorageReference thumb_filepath=mImageStorage.child("profile_images").child("thumbs").child(current_user_id + ".jpg");
//
//
//                final StorageTask<UploadTask.TaskSnapshot> thumb_image = filepath.putFile(resultUri).addOnCompleteListener(new OnCompleteListener<UploadTask.TaskSnapshot>() {
//                    @Override
//                    public void onComplete(@NonNull Task<UploadTask.TaskSnapshot> task) {
//
//                        if (task.isSuccessful()) {
//                            //Toast.makeText(SettingsActivity.this, "Working",Toast.LENGTH_LONG).show();
//                            final String download_url=task.getResult().toString();
//                            Bitmap thumb_bitmap = null;
//                            try {
//                                thumb_bitmap = new Compressor(getApplicationContext())
//                                        .setMaxWidth(200)
//                                        .setMaxHeight(200)
//                                        .setQuality(75)
//                                        .compressToBitmap(thumb_filePath);
//                            } catch (IOException e) {
//                                e.printStackTrace();
//                            }
//                            ByteArrayOutputStream baos = new ByteArrayOutputStream();
//                            thumb_bitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos);
//                            byte[] thumb_byte = baos.toByteArray();
//
//
//                            UploadTask uploadTask = thumb_filepath.putBytes(thumb_byte);
//                            final Bitmap finalThumb_bitmap = thumb_bitmap;
//                            final StorageTask<UploadTask.TaskSnapshot> taskSnapshotStorageTask = uploadTask.addOnCompleteListener(new OnCompleteListener<UploadTask.TaskSnapshot>() {
//                                @Override
//                                public void onComplete(@NonNull Task<UploadTask.TaskSnapshot> thumb_task) {
//                                    String thumb_downloadUrl = thumb_task.getResult().toString();
//                                    final HashMap<String,String> update_Hashmap;
//                                    update_Hashmap = new HashMap<>();
//                                    update_Hashmap.put("name",mName.getText().toString().toUpperCase());
//                                    update_Hashmap.put("status",mStatus.getText().toString());
//                                    update_Hashmap.put("image", download_url);
//                                    update_Hashmap.put("thumb_image", thumb_downloadUrl);
//
//                                    mUserDatabase.setValue(update_Hashmap).addOnCompleteListener(new OnCompleteListener<Void>() {
//                                        @Override
//                                        public void onComplete(@NonNull Task<Void> task) {
//                                            if (task.isSuccessful()) {
//                                                mProgressDailog.dismiss();
//                                                Toast.makeText(SettingsActivity.this, "Success Uploading", Toast.LENGTH_LONG).show();
//                                                mDisplayImage.setImageBitmap(finalThumb_bitmap);
//
//                                            } else {
//                                                Toast.makeText(SettingsActivity.this, "Error in Uploading thumbnail", Toast.LENGTH_LONG).show();
//                                                mProgressDailog.dismiss();
//                                            }
//                                        }
//                                    });
//                                }
//
//                            });
//                        } else if (resultCode == CropImage.CROP_IMAGE_ACTIVITY_RESULT_ERROR_CODE) {
//                            result.getError();
//                        }
//                    }
//                });
//            }
//        }
//    }
//}






package com.gmail.muskankataria2408.chatwithbae;

        import android.app.ProgressDialog;
        import android.content.Intent;
        import android.graphics.Bitmap;
        import android.net.Uri;
        import android.support.annotation.NonNull;
        import android.support.v7.app.AppCompatActivity;
        import android.os.Bundle;
        import android.view.View;
        import android.widget.Button;
        import android.widget.TextView;
        import android.widget.Toast;

        import com.google.android.gms.tasks.OnCompleteListener;
        import com.google.android.gms.tasks.OnFailureListener;
        import com.google.android.gms.tasks.OnSuccessListener;
        import com.google.android.gms.tasks.Task;
        import com.google.firebase.auth.FirebaseAuth;
        import com.google.firebase.auth.FirebaseUser;
        import com.google.firebase.database.DataSnapshot;
        import com.google.firebase.database.DatabaseError;
        import com.google.firebase.database.DatabaseReference;
        import com.google.firebase.database.FirebaseDatabase;
        import com.google.firebase.database.ValueEventListener;
        import com.google.firebase.storage.FirebaseStorage;
        import com.google.firebase.storage.StorageReference;
        import com.google.firebase.storage.StorageTask;
        import com.google.firebase.storage.UploadTask;
        import com.squareup.picasso.Callback;
        import com.squareup.picasso.NetworkPolicy;
        import com.squareup.picasso.Picasso;
        import com.theartofdev.edmodo.cropper.CropImage;

        import java.io.ByteArrayOutputStream;
        import java.io.File;
        import java.io.IOException;
        import java.util.HashMap;
        import java.util.Map;
        import java.util.Random;

        import de.hdodenhof.circleimageview.CircleImageView;
        import id.zelory.compressor.Compressor;

public class SettingsActivity extends AppCompatActivity {


    private DatabaseReference mUserDatabase;
    private FirebaseUser mCurrentUser;


    //Android Layout

    private CircleImageView mDisplayImage;
    private TextView mName;
    private TextView mStatus;

    private Button mStatusBtn;
    private Button mImageBtn;

    String  download_url;
    private static final int GALLERY_PICK = 1;

    // Storage Firebase
    private StorageReference mImageStorage;

    private ProgressDialog mProgressDialog;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        mDisplayImage = (CircleImageView) findViewById(R.id.settings_image);
        mName = (TextView) findViewById(R.id.settings_name);
        mStatus = (TextView) findViewById(R.id.settings_status);

        mStatusBtn = (Button) findViewById(R.id.settings_status_btn);
        mImageBtn = (Button) findViewById(R.id.settings_image_btn);

        mImageStorage = FirebaseStorage.getInstance().getReference();

        mCurrentUser = FirebaseAuth.getInstance().getCurrentUser();

        String current_uid = mCurrentUser.getUid();



        mUserDatabase = FirebaseDatabase.getInstance().getReference().child("Users").child(current_uid);
        mUserDatabase.keepSynced(true);

        mUserDatabase.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                String name = dataSnapshot.child("name").getValue().toString();
                final String image = dataSnapshot.child("image").getValue().toString();
                String status = dataSnapshot.child("status").getValue().toString();
                String thumb_image = dataSnapshot.child("thumb_image").getValue().toString();

                mName.setText(name);
                mStatus.setText(status);

                if(!image.equals("default")) {

                    //Picasso.with(SettingsActivity.this).load(image).placeholder(R.drawable.default_avatar).into(mDisplayImage);

                    Picasso.with(SettingsActivity.this).load(image).networkPolicy(NetworkPolicy.OFFLINE)
                            .placeholder(R.drawable.default_avatar).into(mDisplayImage, new Callback() {
                        @Override
                        public void onSuccess() {

                        }

                        @Override
                        public void onError() {

                            Picasso.with(SettingsActivity.this).load(image).placeholder(R.drawable.default_avatar).into(mDisplayImage);

                        }
                    });

                }


            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });


        mStatusBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String status_value = mStatus.getText().toString();

                Intent status_intent = new Intent(SettingsActivity.this, StatusActivity.class);
                status_intent.putExtra("status_value", status_value);
                startActivity(status_intent);

            }
        });


        mImageBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                Intent galleryIntent = new Intent();
                galleryIntent.setType("image/*");
                galleryIntent.setAction(Intent.ACTION_GET_CONTENT);

                startActivityForResult(Intent.createChooser(galleryIntent, "SELECT IMAGE"), GALLERY_PICK);


                /*
                CropImage.activity()
                        .setGuidelines(CropImageView.Guidelines.ON)
                        .start(SettingsActivity.this);
                        */

            }
        });


    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode == GALLERY_PICK && resultCode == RESULT_OK){

            Uri imageUri = data.getData();

            CropImage.activity(imageUri)
                    .setAspectRatio(1, 1)
                    .setMinCropWindowSize(500, 500)
                    .start(this);

            //Toast.makeText(SettingsActivity.this, imageUri, Toast.LENGTH_LONG).show();

        }


        if (requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE) {

            CropImage.ActivityResult result = CropImage.getActivityResult(data);

            if (resultCode == RESULT_OK) {


                mProgressDialog = new ProgressDialog(SettingsActivity.this);
                mProgressDialog.setTitle("Uploading Image...");
                mProgressDialog.setMessage("Please wait while we upload and process the image.");
                mProgressDialog.setCanceledOnTouchOutside(false);
                mProgressDialog.show();


                Uri resultUri = result.getUri();

                File thumb_filePath = new File(resultUri.getPath());

                String current_user_id = mCurrentUser.getUid();


                Bitmap thumb_bitmap = null;
                try {
                    thumb_bitmap = new Compressor(this)
                            .setMaxWidth(200)
                            .setMaxHeight(200)
                            .setQuality(75)
                            .compressToBitmap(thumb_filePath);
                } catch (IOException e) {
                    e.printStackTrace();
                }

                ByteArrayOutputStream baos = new ByteArrayOutputStream();
                thumb_bitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos);
                final byte[] thumb_byte = baos.toByteArray();


                StorageReference filepath = mImageStorage.child("profile_images").child(current_user_id + ".jpg");
                final StorageReference thumb_filepath = mImageStorage.child("profile_images").child("thumbs").child(current_user_id + ".jpg");



                filepath.putFile(resultUri).addOnCompleteListener(new OnCompleteListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<UploadTask.TaskSnapshot> task) {

                        if(task.isSuccessful()){

                            task.getResult().getStorage().getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                                @Override
                                public void onSuccess(Uri uri) {
                                    // Got the uri
                                    download_url= String.valueOf(uri);
                                }
                            }).addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception exception) {
                                    // Handle any errors
                                }
                            });

                       //     final  @SuppressWarnings("VisibleForTests")String download_url = task.getResult().getStorage().getDownloadUrl().toString();

                            UploadTask uploadTask = thumb_filepath.putBytes(thumb_byte);
                            uploadTask.addOnCompleteListener(new OnCompleteListener<UploadTask.TaskSnapshot>() {
                                @Override
                                public void onComplete(@NonNull Task<UploadTask.TaskSnapshot> thumb_task) {


                                   String thumb_downloadUrl = download_url;

                                    if(thumb_task.isSuccessful()){

                                        Map update_hashMap = new HashMap();
                                        update_hashMap.put("image", download_url);
                                        update_hashMap.put("thumb_image", thumb_downloadUrl);

                                        mUserDatabase.updateChildren(update_hashMap).addOnCompleteListener(new OnCompleteListener<Void>() {
                                            @Override
                                            public void onComplete(@NonNull Task<Void> task) {

                                                if(task.isSuccessful()){

                                                    mProgressDialog.dismiss();
                                                    Toast.makeText(SettingsActivity.this, "Success Uploading.", Toast.LENGTH_LONG).show();

                                                }

                                            }
                                        });


                                    } else {

                                        Toast.makeText(SettingsActivity.this, "Error in uploading thumbnail.", Toast.LENGTH_LONG).show();
                                        mProgressDialog.dismiss();

                                    }


                                }
                            });



                        } else {

                            Toast.makeText(SettingsActivity.this, "Error in uploading.", Toast.LENGTH_LONG).show();
                            mProgressDialog.dismiss();

                        }

                    }
                });



            } else if (resultCode == CropImage.CROP_IMAGE_ACTIVITY_RESULT_ERROR_CODE) {

                Exception error = result.getError();

            }
        }


    }


    public static String random() {
        Random generator = new Random();
        StringBuilder randomStringBuilder = new StringBuilder();
        int randomLength = generator.nextInt(20);
        char tempChar;
        for (int i = 0; i < randomLength; i++){
            tempChar = (char) (generator.nextInt(96) + 32);
            randomStringBuilder.append(tempChar);
        }
        return randomStringBuilder.toString();
    }

}