package com.sigma.moonlight;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.Toolbar;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.Objects;

public class EditProfileActivity extends AppCompatActivity implements View.OnClickListener {

    TextInputEditText profileName, profileAddress;
    TextView profileNumber;
    Button btnProfileSave;

    FirebaseUser user;
    UserProfileChangeRequest profileUpdates;
    FirebaseFirestore db;

    Profile profile;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_profile);

        profileName = findViewById(R.id.edittxt_profilename);
        profileAddress = findViewById(R.id.edittxt_profileaddress);
        profileNumber = findViewById(R.id.edittx_profilenumber);
        btnProfileSave = findViewById(R.id.btn_saveprofile);
        user = FirebaseAuth.getInstance().getCurrentUser();
        db = FirebaseFirestore.getInstance();

        Toolbar toolbar = findViewById(R.id.toolbar_main);
        toolbar.setTitle("Edit Profile");
        setActionBar(toolbar);

        if (user != null) {
            profileName.setText(user.getDisplayName());
            profileNumber.setText(user.getPhoneNumber());
            setprofileAddress();
        }


        btnProfileSave.setOnClickListener(this);
    }


    private void setprofileAddress() {
        db.collection("users")
                .document(Objects.requireNonNull(FirebaseAuth.getInstance().getUid()))
                .get()
                .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                        DocumentSnapshot documentSnapshot = task.getResult();
                        if (documentSnapshot.exists()) {
                            String address = documentSnapshot.getString("address");
                            profileAddress.setText(address);
                        } else
                            Toast.makeText(getApplicationContext(), "No Document found", Toast.LENGTH_LONG).show();
                    }
                });
    }


    @Override
    public void onClick(View v) {

        String name = profileName.getText().toString().trim();
        String address = profileAddress.getText().toString().trim();


        if ((!name.equals("")) && (!address.equals(""))) {

            profile = new Profile(name, address);

            db.collection("users")
                    .document(Objects.requireNonNull(FirebaseAuth.getInstance().getCurrentUser().getUid()))
                    .set(profile)
                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if (task.isSuccessful()) {
                                Toast.makeText(getApplicationContext(), "Profile Saved", Toast.LENGTH_LONG).show();
                                Intent intent = new Intent(getApplicationContext(), CatalogActivity.class);
                                startActivity(intent);
                                finish();
                            } else
                                Toast.makeText(getApplicationContext(), "Failed to Save", Toast.LENGTH_LONG).show();

                        }
                    });

            profileUpdates = new UserProfileChangeRequest.Builder()
                    .setDisplayName(name)
                    .build();

            user.updateProfile(profileUpdates)
                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if (task.isSuccessful()) {
                                Log.v("temp", " Firebase Profile Updated");
                            }
                        }
                    });

        } else
            Toast.makeText(getApplicationContext(), "Add neccessary details", Toast.LENGTH_LONG).show();


    }
}
