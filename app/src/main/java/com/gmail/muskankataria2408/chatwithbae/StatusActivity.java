package com.gmail.muskankataria2408.chatwithbae;

import android.app.ProgressDialog;
import android.support.annotation.NonNull;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import android.widget.Toolbar;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseApp;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class StatusActivity extends AppCompatActivity {

    private android.support.v7.widget.Toolbar mToolBar;
    private TextInputLayout mStatus;
    private Button mSaveStatusBtn;

    //Firebase
    private DatabaseReference mStatusDatabase;
    private FirebaseUser mCurrentUser;

    //progress
    private ProgressDialog mProgressDailog;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_status);
        FirebaseApp.initializeApp(this);
        //Firebase
        mCurrentUser= FirebaseAuth.getInstance().getCurrentUser();
        String current_uid=mCurrentUser.getUid();
        mStatusDatabase= FirebaseDatabase.getInstance().getReference().child("Users").child(current_uid);



        mToolBar= (android.support.v7.widget.Toolbar) findViewById(R.id.status_appBar);
        mStatus= (TextInputLayout) findViewById(R.id.staus_input);
        mSaveStatusBtn=(Button)findViewById(R.id.status_save_button);
        setSupportActionBar(mToolBar);
        getSupportActionBar().setTitle("Account Status");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        String status_value=getIntent().getStringExtra("status_value");
        mStatus.getEditText().setText(status_value);
        mSaveStatusBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //Progress
                mProgressDailog=new ProgressDialog(StatusActivity.this);


                //Progress
                mProgressDailog.setTitle("Saving Changes");
                mProgressDailog.setMessage("Please Wait while we save the changes");
                mProgressDailog.show();

                String status=mStatus.getEditText().getText().toString();


                mStatusDatabase.child("status").setValue(status).addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if(task.isSuccessful())
                        {

                            mProgressDailog.dismiss();
                        }

                        else
                            Toast.makeText(getApplicationContext(),"There was some errors in saving Changes", Toast.LENGTH_LONG).show();
                    }
                });


            }
        });

    }
}
