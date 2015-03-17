package com.example.nico.earthquake.Activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import com.example.nico.earthquake.R;

/**
 * Created by nico on 17/03/2015.
 */
public class SettingsActivity extends ActionBarActivity implements View.OnClickListener {

    private static String TAG = "EARTHQUAKE";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);
        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            int magnitude = extras.getInt("magnitude");
            Log.d(TAG, "magnitude : " + magnitude);
            this.selectRadio(magnitude);
        }
        else{
            selectRadio(0);
        }

        Button valid = (Button) findViewById(R.id.OK_button);
        valid.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        Intent intent;
        if(v.getId() == R.id.OK_button){
            RadioGroup radioGroupEQ = (RadioGroup) findViewById(R.id.radioGroupEQ);
            int indexBtn = radioGroupEQ.getCheckedRadioButtonId();

            intent = new Intent(SettingsActivity.this, MainActivity.class);

            View radioButtonEQ = radioGroupEQ.findViewById(indexBtn);
            int magnitude = radioGroupEQ.indexOfChild(radioButtonEQ);
            intent.putExtra("magnitude", magnitude);

            startActivity(intent);
            Log.d(TAG, "Parameters OK");
        } // else nothing to do
    }

    private void selectRadio(int earthquake) {
        RadioButton radioButton;
        switch (earthquake){
            case 0:
                radioButton = (RadioButton) findViewById(R.id.radioSignificant);
                radioButton.setChecked(true);
                break;
            case 1:
                radioButton = (RadioButton) findViewById(R.id.radioAll);
                radioButton.setChecked(true);
                break;
            case 2:
                radioButton = (RadioButton) findViewById(R.id.radioM4);
                radioButton.setChecked(true);
                break;
            case 3:
                radioButton = (RadioButton) findViewById(R.id.radioM2);
                radioButton.setChecked(true);
                break;
            case 4:
                radioButton = (RadioButton) findViewById(R.id.radioM1);
                radioButton.setChecked(true);
                break;
        }
    }
}
