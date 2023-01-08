package com.example.chatapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.example.chatapp.databinding.ActivityOtpactivityBinding;
import com.example.chatapp.databinding.ActivityPhoneNumberBinding;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseException;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthOptions;
import com.google.firebase.auth.PhoneAuthProvider;

import java.util.concurrent.TimeUnit;

public class OTPActivity extends AppCompatActivity {
    ActivityOtpactivityBinding binding;
    FirebaseAuth auth;
    String verificationId;
    String PhoneNumber;
    ProgressDialog dailog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding=ActivityOtpactivityBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        dailog=new ProgressDialog(this);
        dailog.setMessage("Sending OTP...😇😇");
        dailog.setCancelable(false);
        dailog.show();
        auth=FirebaseAuth.getInstance();
        PhoneNumber=getIntent().getStringExtra("PhoneNumber");
        binding.phoneLbl.setText("Verify "+PhoneNumber);

        PhoneAuthOptions options = PhoneAuthOptions.newBuilder(auth)
                .setPhoneNumber(PhoneNumber)
                .setTimeout(60L , TimeUnit.SECONDS)
                .setActivity(OTPActivity.this)
                .setCallbacks(new PhoneAuthProvider.OnVerificationStateChangedCallbacks() {
                    @Override
                    public void onVerificationCompleted(@NonNull PhoneAuthCredential phoneAuthCredential) {

                    }

                    @Override
                    public void onVerificationFailed(@NonNull FirebaseException e) {

                    }

                    @Override
                    public void onCodeSent(@NonNull String verifyId, @NonNull PhoneAuthProvider.ForceResendingToken forceResendingToken) {
                        super.onCodeSent(verifyId, forceResendingToken);
                        dailog.dismiss();
                        verificationId=verifyId;
                    }
                }).build();
        PhoneAuthProvider.verifyPhoneNumber(options);
        binding.loginBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String otp = binding.otpbox.getText().toString();
                PhoneAuthCredential credential = PhoneAuthProvider.getCredential(verificationId , otp);
                auth.signInWithCredential(credential).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                       if(task.isSuccessful())
                       {
                           Intent intent=new Intent(OTPActivity.this,SetupProfileActivity.class);
                           startActivity(intent);
                           finishAffinity();
                       }
                       else{
                           Toast.makeText(OTPActivity.this,"Failed",Toast.LENGTH_SHORT).show();
                       }
                    }
                });

                }
        });

    }
}