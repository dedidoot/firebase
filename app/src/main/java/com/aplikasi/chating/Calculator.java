package com.aplikasi.chating;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

public class Calculator extends AppCompatActivity {

    private EditText editText1, editText2;
    private Button btn;
    private TextView txt;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_calculator);
        editText1 = (EditText) findViewById(R.id.editText);
        editText2 = (EditText) findViewById(R.id.editText2);
        btn = (Button) findViewById(R.id.button2);
        txt = (TextView) findViewById(R.id.textView);
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                int ed1 = Integer.valueOf(editText1.getText().toString());
                int ed2 = Integer.valueOf(editText2.getText().toString());
                int jum = ed1 + ed2;
                txt.setText(String.valueOf(jum));

            }
        });

    }
}
