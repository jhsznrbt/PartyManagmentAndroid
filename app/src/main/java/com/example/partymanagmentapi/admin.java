package com.example.partymanagmentapi;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.text.Editable;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class admin extends AppCompatActivity {
    private TextView familyName;
    private TextView givenName;
    private TextView gender;
    private TextView placeOfBirth;
    private TextView birthDate;
    private TextView beiratva;
    private FirebaseFirestore db;
    private String selected_docid;
    private Map<String, Object> individuals;
    private static final String LOG_TAG=register.class.getName();
    private static final String PREF_KEY = MainActivity.class.getPackage().toString();
    Spinner s;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        individuals = new HashMap<>();
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin);
        familyName = findViewById(R.id.familyName);
        givenName = findViewById(R.id.givenName);
        gender = findViewById(R.id.gender);
        placeOfBirth = findViewById(R.id.placeOfBirth);
        birthDate = findViewById(R.id.birthDate);
        beiratva = findViewById(R.id.beiratva);

        getIndividuals();
//        spinner = (Spinner) findViewById(R.id.gender);
//        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
//                R.array.gender_array, android.R.layout.simple_spinner_item);
//        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
//        spinner.setAdapter(adapter);



    }



    protected void loadIndividuals(){
        String[] arraySpinner = new String[individuals.size()];
        int i = 0;
        for (Map.Entry<String, Object> entry : individuals.entrySet()) {
            Individual iindiv = (Individual) entry.getValue();
            arraySpinner[i] = entry.getKey();
            i++;
            Log.i(LOG_TAG, entry.getKey());
        }
        s = (Spinner) findViewById(R.id.individual_list);
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_spinner_item, arraySpinner);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        s.setAdapter(null);
        s.setAdapter(adapter);
        s.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @SuppressLint("SetTextI18n")
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String selectedItemText = (String) parent.getItemAtPosition(position);
                // Display the selected item into the TextView
                Individual i = (Individual) individuals.get(selectedItemText);
                familyName.setText(i.familyName);
                givenName.setText(i.givenName);
                gender.setText(i.gender);

                birthDate.setText(i.birthDate);
                placeOfBirth.setText(i.placeOfBirth);
                beiratva.setText(i.title);
//                if(i.title == "Beiratva") {
//                    beiratva.setText("Be van iratva!");
//                } else {
//                    beiratva.setText("Beiratásra vár!!");
//                }
                selected_docid =  selectedItemText;
            }
//

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }

    protected void getIndividuals(){
        db = FirebaseFirestore.getInstance();
        db.collection("individuals")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                Log.d(LOG_TAG, document.getId() + " => " + document.getData());
                                Individual i = new Individual();
                                i.familyName = (String) document.getData().get("familyName");
                                i.placeOfBirth = (String) document.getData().get("placeOfBirth");
                                i.gender = (String) document.getData().get("gender");
                                i.givenName = (String) document.getData().get("givenName");

                                if(document.getData().containsKey("title")) {
                                    i.title = (String) document.getData().get("title");
                                } else {
                                    i.title = "Nincs beiratva";
                                }

                                i.birthDate = (String) document.getData().get("birthDate");;
                                individuals.put(document.getId(), i);

                            }

                        } else {
                            Log.d(LOG_TAG, "Error getting documents: ", task.getException());
                        }
                        loadIndividuals();
                    }
                });



    }

    public void decline_click(View view) {
        Individual i;
        i = (Individual) individuals.get(selected_docid);
        db.collection("individuals").document(selected_docid).delete();
        individuals.remove(selected_docid);
        getIndividuals();
//        if(i.title.equals("Beiratva")) {
//
//        } else {
//            Toast.makeText(getApplicationContext(), "A tanuló nincs beiratva", Toast.LENGTH_LONG).show();
//
//        }
    }

    public void accept_click(View view) {
        Individual i;
        i = (Individual) individuals.get(selected_docid);
        if (i.title.equals("Nincs beiratva")) {
            Map<String, Object> user = new HashMap<>();
            user.put("title", "Beiratva");
            db.collection("individuals").document(selected_docid).update(user);
            getIndividuals();
        }
        else {
            Toast.makeText(getApplicationContext(), "A tanuló be van már iratva", Toast.LENGTH_LONG).show();

        }
//        if(i.title != null) {
////            if(i.title.equals("Beiratva"))
////                beiratva.setText("Be van iratva!");
//        } else {
//            Map<String, Object> user = new HashMap<>();
//            user.put("title", "Beiratva");
//            db.collection("individuals").document(selected_docid).update(user);
//            getIndividuals();
//        }


    }

    public void back_click(View view) { finish();
    }
}