package com.artyushin.hw121;

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

        editText = (EditText) findViewById(R.id.sample);
        saveButton = (Button) findViewById(R.id.save_setting);
        saveButton.setOnClickListener(new View.OnClickListener () {
            @Override
            public void onClick(View v) {
                sample = String.valueOf (editText.getText ());
                Sample.setSample (sample);
                finish ();
            }
        });
    }
}
