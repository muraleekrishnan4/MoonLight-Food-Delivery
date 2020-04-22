package com.sigma.moonlight;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.Toolbar;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.Objects;

public class ProfileActivity extends AppCompatActivity implements View.OnClickListener {


    String address;

    TextView profileName, profileAddress, profileNumber;
    Button btnSignOut, btnProfileEdit;

    FirebaseUser user;
    FirebaseFirestore db;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        profileName = findViewById(R.id.txt_profilename);
        profileAddress = findViewById(R.id.txt_profileaddress);
        profileNumber = findViewById(R.id.txt_profilenumber);
        btnProfileEdit = findViewById(R.id.btn_profileedit);
        btnSignOut = findViewById(R.id.btn_Signout);
        db = FirebaseFirestore.getInstance();
        user = FirebaseAuth.getInstance().getCurrentUser();

        Toolbar toolbar = findViewById(R.id.toolbar_main);
        toolbar.setTitle("Profile");
        setActionBar(toolbar);


        setupProfile();

        btnProfileEdit.setOnClickListener(this);
        btnSignOut.setOnClickListener(this);


    }

    private void setupProfile() {


        profileName.setText(user.getDisplayName());
        profileNumber.setText(user.getPhoneNumber());

        db.collection("users")
                .document(Objects.requireNonNull(FirebaseAuth.getInstance().getUid()))
                .get()
                .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                        DocumentSnapshot documentSnapshot = task.getResult();
                        if (documentSnapshot.exists()) {
                            address = documentSnapshot.getString("address");
                            profileAddress.setText(address);
                        } else
                            Toast.makeText(getApplicationContext(), "No Document found", Toast.LENGTH_LONG).show();
                    }
                });


    }

    @Override
    public void onClick(View v) {
        if (v == btnProfileEdit) {
            Intent intent = new Intent(getApplicationContext(), EditProfileActivity.class);
            startActivity(intent);
        } else if (v == btnSignOut) {
            FirebaseAuth.getInstance().signOut();
            Intent intent = new Intent(getApplicationContext(), MainActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(intent);
            finish();
        }
    }
}
