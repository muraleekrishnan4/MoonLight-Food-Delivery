package com.sigma.moonlight;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.RadioButton;
import android.widget.ViewFlipper;

public class AboutUs extends Activity {

    RadioButton RB0;
    RadioButton RB1;
    RadioButton RB2;
    ViewFlipper VF;
    private View.OnClickListener radio_listener = new View.OnClickListener() {
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.radio0:
                    VF.setDisplayedChild(0);
                    break;
                case R.id.radio1:
                    VF.setDisplayedChild(1);
                    break;
                case R.id.radio2:
                    VF.setDisplayedChild(2);
                    break;
            }
        }
    };

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.aboutus);

        RB0 = findViewById(R.id.radio0);
        RB1 = findViewById(R.id.radio1);
        RB2 = findViewById(R.id.radio2);
        VF = findViewById(R.id.ViewFlipper01);


        RB0.setOnClickListener(radio_listener);
        RB1.setOnClickListener(radio_listener);
        RB2.setOnClickListener(radio_listener);
    }
}