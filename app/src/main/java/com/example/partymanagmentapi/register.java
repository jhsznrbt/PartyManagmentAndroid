package com.example.partymanagmentapi;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class register extends AppCompatActivity {
    EditText familyNameEditText;
    EditText givenNameEditText;
    EditText birthPlaceEditText;
    EditText birthDateEditText;
    Spinner spinner;
    CheckBox maritalStatusCB;
    private static final String LOG_TAG=register.class.getName();
    private static final String PREF_KEY = MainActivity.class.getPackage().toString();
    private FirebaseFirestore db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        spinner = (Spinner) findViewById(R.id.gender);
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                R.array.gender_array, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);

        familyNameEditText = findViewById(R.id.editTextfamilyName);
        givenNameEditText = findViewById(R.id.editTextgivenName);
        birthPlaceEditText = findViewById(R.id.editTextplaceOfBirth);
        birthDateEditText = findViewById(R.id.editTextDateBirthdate);
        maritalStatusCB = (CheckBox)findViewById(R.id.checkBox);

        SharedPreferences preferences = getSharedPreferences(PREF_KEY, MODE_PRIVATE);

        String userName = preferences.getString("userName", "");
        String password = preferences.getString("password", "");


    }

    public void close_click(View view) {
        finish();
    }

    public void register_click(View view) {
        try {
            Log.d(LOG_TAG, "Addindividuális 1");
            Individual indiv;
            indiv = new Individual();
            Log.d(LOG_TAG, "Addindividuális 2");
            indiv.familyName = familyNameEditText.getText().toString();
            Log.d(LOG_TAG, "Addindividuális 2");
            indiv.givenName = givenNameEditText.getText().toString();
            Log.d(LOG_TAG, "Addindividuális 2");
            indiv.placeOfBirth = birthPlaceEditText.getText().toString();
            indiv.birthDate = birthDateEditText.getText().toString();
            indiv.gender = spinner.getSelectedItem().toString();
            Log.d(LOG_TAG, "Addindividuális 2");
            if (maritalStatusCB.isChecked())
                indiv.maritalStatus = "házas";
            else
                indiv.maritalStatus = "nem házas";
            Log.d(LOG_TAG, "Addindividuális 3");
            addIndividual(indiv);
        }catch (Exception e) {
            Log.d(LOG_TAG, "Oops!"+e.getMessage());
        }

        //ki kell szervezni


        //todo a regisztrációs funkció
    }
    public void addIndividual(Individual indiv) {
        Map<String, Object> user = new HashMap<>();
        Log.d(LOG_TAG, "Addindividuális");
        user.put("givenName", indiv.givenName);
        user.put("familyName", indiv.familyName);
        user.put("placeOfBirth", indiv.placeOfBirth);
        user.put("birthDate", indiv.birthDate);
        user.put("gender", indiv.gender);
        db = FirebaseFirestore.getInstance();
        db.collection("individuals")
                .add(user)
                .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                    @Override
                    public void onSuccess(DocumentReference documentReference) {
                        Log.d(LOG_TAG, "DocumentSnapshot added with ID: " + documentReference.getId());
                        Toast.makeText(getApplicationContext(), "Sikeres eltárolás", Toast.LENGTH_LONG).show();
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.w(LOG_TAG, "Error adding document", e);
                    }
                });

    }
}