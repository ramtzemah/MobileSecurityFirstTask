package com.example.securitiesles1;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.content.Intent;
import android.os.Bundle;

import com.google.android.material.button.MaterialButton;
import com.google.android.material.textview.MaterialTextView;

public class secondActivity extends AppCompatActivity {
    private MaterialButton back;
    private MaterialTextView text;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_second);
        findViews();
        back.setOnClickListener(view -> {
            Intent intent = new Intent(secondActivity.this,MainActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(intent);
            finish();
        });

    }


    private void findViews(){
        back = findViewById(R.id.back);
        text = findViewById(R.id.text);
    }
}