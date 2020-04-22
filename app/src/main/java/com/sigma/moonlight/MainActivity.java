package com.sigma.moonlight;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {


    TextInputEditText textInputEditText;
    Button btnProceed;


    FirebaseUser user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        textInputEditText = findViewById(R.id.TIET_number);
        btnProceed = findViewById(R.id.BLogin);
        user = FirebaseAuth.getInstance().getCurrentUser();

        if (user != null) {
            Intent intent = new Intent(this, CatalogActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(intent);
            finish();
        } else
            btnProceed.setOnClickListener(this);

    }


    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.BLogin) {

            if (textInputEditText.length() == 10) {
                final String phoneNumber = textInputEditText.getText().toString().trim();

                Intent intent = new Intent(MainActivity.this, VerificationActivity.class);
                intent.putExtra("phoneNumber", phoneNumber);
                startActivity(intent);
            } else
                Toast.makeText(MainActivity.this, "Enter valid Mobile Number", Toast.LENGTH_LONG).show();
        }

    }


}
