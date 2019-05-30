package com.gmail.muskankataria2408.chatwithbae;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Intent;
import android.net.sip.SipSession;
import android.support.annotation.NonNull;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseApp;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.FirebaseAuthUserCollisionException;
import com.google.firebase.auth.FirebaseAuthWeakPasswordException;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.iid.FirebaseInstanceId;

import java.util.HashMap;

import static android.app.PendingIntent.getActivity;

public class RegisterActivity extends AppCompatActivity {
     private TextInputLayout mDisplayName;
     private TextInputLayout mEmail,mPhone;
     private TextInputLayout mPassword;
    private Button mCreateButton;
    private Toolbar mtoolbar;
    private DatabaseReference mDataBase;
// Progress Dailog
  private ProgressDialog mRegProgress;
     //Firebase Auth
     private FirebaseAuth mAuth;
    @SuppressLint("WrongViewCast")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        FirebaseApp.initializeApp(this);
        mAuth = FirebaseAuth.getInstance();



        //toolbar set
        mtoolbar = (Toolbar) findViewById(R.id.register_toolbar);
        setSupportActionBar(mtoolbar);
        RegisterActivity.this.setTitle("Create Account");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        //Firebase Auth

        mRegProgress = new ProgressDialog(this);
        //Android feilds
        mDisplayName = (TextInputLayout) findViewById(R.id.reg_display_name);
        mEmail = (TextInputLayout) findViewById(R.id.reg_email);
        mPassword = (TextInputLayout) findViewById(R.id.reg_password);
        mPhone =(TextInputLayout)findViewById(R.id.mobile);
        mCreateButton = (Button) findViewById(R.id.reg_create_button);
        if(getIntent().getExtras()!=null)
        {
            mPhone.getEditText().setText(getIntent().getStringExtra("mobileno"));
        }
        mCreateButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String display_name = mDisplayName.getEditText().getText().toString();
                String email = mEmail.getEditText().getText().toString();
                String password = mPassword.getEditText().getText().toString();
                String mobile=mPhone.getEditText().getText().toString();
     if (!TextUtils.isEmpty(display_name) && !TextUtils.isEmpty(email) && !TextUtils.isEmpty(password)) {
                    mRegProgress.setTitle("Registering User");
                    mRegProgress.setMessage("Please wait while we creating your account");
                    mRegProgress.setCanceledOnTouchOutside(false);
                    mRegProgress.show();
         register_user(display_name, email, password,mobile);
                }


            }
        });


    }


    private void register_user(final String display_name, String email, String password, final String mobile) {


        mAuth.createUserWithEmailAndPassword(email, password).addOnCompleteListener( this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                                       FirebaseUser current_user=FirebaseAuth.getInstance().getCurrentUser();
                                       String uid=current_user.getUid();
                           mDataBase=FirebaseDatabase.getInstance().getReference().child("Users").child(uid);
                            String device_token = FirebaseInstanceId.getInstance().getToken();
                            HashMap<String,String> userMap=new HashMap<>();
                            userMap.put("name",display_name);
                            userMap.put("status", "Hye, I'm using HelloBae App");
                            userMap.put("image", "default");
                            userMap.put("thumb_image", "default");
                            userMap.put("device_token", device_token);
                            userMap.put("mobile_number", mobile);


                            mDataBase.setValue(userMap).addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    if(task.isSuccessful())
                                    {
                                    mRegProgress.dismiss();
                            Intent mainIntent = new Intent(RegisterActivity.this, MainActivity.class);
                            mainIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                            startActivity(mainIntent);
                            finish();
                                }}
                            });



//
// FirebaseUser user = mAuth.getCurrentUser(); //You Firebase user
//                            // user registered, start profile activity
//
                        } else {

                            String error="";
                            try{
                                throw task.getException();
                            }
                            catch(FirebaseAuthWeakPasswordException e)
                            {
                                error="Weak Password";
                            }catch(FirebaseAuthInvalidCredentialsException e)
                            {
                                error="Invalid Email";
                            }catch(FirebaseAuthUserCollisionException e)
                            {
                                error="Existing Account";
                            }
                            catch (Exception e) {
                                error="Unknown Error";
                                e.printStackTrace();
                            }

                            mRegProgress.hide();
                            Toast.makeText(getApplicationContext(), error, Toast.LENGTH_LONG).show();

                        }


                    }
                });
    }
}

