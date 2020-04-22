package com.sigma.moonlight;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;
import android.widget.Toolbar;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class FoodPreparingActivity extends Activity implements Runnable {

    TextView txtUpdate;

    FirebaseFirestore db;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_food_preparing);
        db = FirebaseFirestore.getInstance();

        Toolbar toolbar = findViewById(R.id.toolbar_main);
        toolbar.setTitle("Food Updates");
        setActionBar(toolbar);

        txtUpdate = findViewById(R.id.txt_foodUpdate);


    }

    @Override
    protected void onResume() {
        super.onResume();
        run();
    }

    @Override
    public void run() {

        //ForMoonlight Hotel
//        db.collection("orders").get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
//            @Override
//            public void onComplete(@NonNull Task<QuerySnapshot> task) {
//                if(task.isSuccessful()){
//                    Log.v("docid",""+ task.getResult().getDocuments());
//                    for(DocumentSnapshot documentSnapshot : task.getResult()){
//                        Log.v("docid1",""+ documentSnapshot.getId());
//                    }
//                }
//            }
//        });


        db.collection("orders").document(FirebaseAuth.getInstance().getCurrentUser().getUid())
                .get()
                .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                        if (task.isSuccessful()) {
                            List<String> list = new ArrayList<>();
                            Map<String, Object> map = task.getResult().getData();
                            if (map != null) {
                                for (Map.Entry<String, Object> entry : map.entrySet()) {
                                    if (entry.getKey().contains("update"))
                                        list.add(entry.getKey());
                                    Log.v("update", "" + list);
                                }
                            }
                        }
                    }
                });

    }
}