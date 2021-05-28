package com.example.shivangibithel.beat;

/**
 * Created by SHIVANGI BITHEL on 03-07-2017.
 */
import android.app.Activity;
import android.content.DialogInterface;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.LinearLayout;

public class mood extends AppCompatActivity implements View.OnClickListener {

    private LinearLayout linLay;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.mood);

        linLay = (LinearLayout) findViewById(R.id.root1);

        findViewById(R.id.blue).setOnClickListener(this);


    }

    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.blue:
                linLay.setBackgroundColor(Color.parseColor("#ffd198"));
        }
    }

}