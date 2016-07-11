package com.teckemeyer.recipecostcalculatorv2;

import android.graphics.Typeface;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;


public class Credits extends AppCompatActivity {

    Typeface typeFace;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_credits);

        TextView txt1 = (TextView) findViewById(R.id.txtCredit1);
        TextView txt2 = (TextView) findViewById(R.id.txtCredit2);
        TextView txtThanks = (TextView) findViewById(R.id.txtThanks);
        Button btnCredit = (Button) findViewById(R.id.btnCreditsOK);

        typeFace = Typeface.createFromAsset(getAssets(), "fonts/AlegreyaSans-Medium.ttf");

        txt1.setTypeface(typeFace);
        txt2.setTypeface(typeFace);
        txtThanks.setTypeface(typeFace);
        btnCredit.setTypeface(typeFace);

        btnCredit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

    }
}
