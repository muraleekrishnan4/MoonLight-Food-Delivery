package com.sigma.moonlight;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.FirebaseException;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthProvider;

import java.util.concurrent.TimeUnit;

public class VerificationActivity extends AppCompatActivity implements View.OnClickListener {


    String phoneNumber;
    TextView txt_number;
    Button btn_verify;
    TextInputEditText textInputEditText;

    FirebaseAuth firebaseAuth;

    String codesent;
    PhoneAuthProvider.OnVerificationStateChangedCallbacks mCallbacks = new PhoneAuthProvider.OnVerificationStateChangedCallbacks() {
        @Override
        public void onVerificationCompleted(@NonNull PhoneAuthCredential phoneAuthCredential) {

        }

        @Override
        public void onVerificationFailed(@NonNull FirebaseException e) {
            Toast.makeText(getApplicationContext(), "Error Occurred. Please try again later", Toast.LENGTH_LONG).show();
        }

        @Override
        public void onCodeSent(@NonNull String s, @NonNull PhoneAuthProvider.ForceResendingToken forceResendingToken) {
            super.onCodeSent(s, forceResendingToken);

            Toast.makeText(getApplicationContext(), "SMS request sent successfully", Toast.LENGTH_LONG).show();
            codesent = s;
        }


    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_verification);

        firebaseAuth = FirebaseAuth.getInstance();

        Intent intent = getIntent();

        phoneNumber = intent.getStringExtra("phoneNumber");
        txt_number = findViewById(R.id.txt_mobilenumber);
        btn_verify = findViewById(R.id.btn_verify);
        textInputEditText = findViewById(R.id.TIET_verification);

        txt_number.setText(phoneNumber);


        sendVerificationCode(phoneNumber);


        btn_verify.setOnClickListener(this);

    }

    private void sendVerificationCode(String phoneNumber) {

        PhoneAuthProvider.getInstance().verifyPhoneNumber(
                "+91" + phoneNumber,        // Phone number to verify
                120,                 // Timeout duration
                TimeUnit.SECONDS,   // Unit of timeout
                this,               // Activity (for callback binding)
                mCallbacks);        // OnVerificationStateChangedCallbacks

    }

    @Override
    public void onClick(View v) {
        verifySignIn();
    }

    private void verifySignIn() {

        String code = textInputEditText.getText().toString();

        if (!code.equals("")) {

            PhoneAuthCredential credential = PhoneAuthProvider.getCredential(codesent, code);
            signInWithPhoneAuthCredential(credential);

        } else {
            Toast.makeText(getApplicationContext(), "Enter the Verification Code", Toast.LENGTH_LONG).show();
        }
    }


    private void signInWithPhoneAuthCredential(PhoneAuthCredential credential) {

        firebaseAuth.signInWithCredential(credential)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            Intent intent = new Intent(getApplicationContext(), EditProfileActivity.class);
                            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                            startActivity(intent);
                            finish();
                        } else {
                            // Sign in failed, display a message and update the UI
                            if (task.getException() instanceof FirebaseAuthInvalidCredentialsException) {
                                Toast.makeText(getApplicationContext(), "Unsuccessful Login Attempt. Please try again", Toast.LENGTH_LONG).show();
                            }
                        }
                    }
                });
    }

}
