package com.artyushin.hw121;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import androidx.appcompat.app.AppCompatActivity;

public class AddActivity extends AppCompatActivity {
    private String sample;
    private EditText editText;
    private Button saveButton;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate (savedInstanceState);
        setContentView (R.layout.activity_add);

        editText = findViewById(R.id.sample);
        saveButton = findViewById(R.id.save_setting);

        saveButton.setOnClickListener(v -> {
            Intent intent = new Intent();
            intent.putExtra("sample", editText.getText().toString());
            setResult(RESULT_OK, intent);
            finish ();
        });
    }
}
