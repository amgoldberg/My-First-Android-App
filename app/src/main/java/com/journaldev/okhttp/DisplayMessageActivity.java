package com.journaldev.okhttp;

import android.content.Intent;
import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import nl.dionsegijn.konfetti.KonfettiView;
import nl.dionsegijn.konfetti.models.Shape;
import nl.dionsegijn.konfetti.models.Size;
//import com.surveymonkey.surveymonkeyandroidsdk.SurveyMonkey;
//import com.surveymonkey.surveymonkeyandroidsdk.utils.SMError;

import org.json.JSONObject;


public class DisplayMessageActivity extends AppCompatActivity {

    //private SurveyMonkey sdkInstance = new SurveyMonkey();



        public KonfettiView konfettiView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // startActivityForResult(Intent, int)
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_display_message);
        konfettiView = (KonfettiView)findViewById(R.id.viewKonfetti);


        // Get the Intent that started this activity and extract the string
        Intent intent = getIntent();
        String message = intent.getStringExtra(MainActivity.EXTRA_MESSAGE);

        // Capture the layout's TextView and set the string as its text
        TextView textView = (TextView) findViewById(R.id.textView);
        textView.setText(message);

        textView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                konfettiView.build()
                        .addColors(Color.YELLOW, Color.GREEN, Color.MAGENTA)
                        .setDirection(0.0, 359.0)
                        .setSpeed(1f, 5f)
                        .setFadeOutEnabled(true)
                        .setTimeToLive(2000L)
                        .addShapes(Shape.RECT, Shape.CIRCLE)
                        .addSizes(new Size(12, 5f))
                        .setPosition(-50f, konfettiView.getWidth() + 50f, -50f, -50f)
                        .stream(300, 5000L);

            }
        });

    }

    // protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        // super.onActivityResult(requestCode, resultCode, data);
        //String respondent = data.getStringExtra("smRespondent");
        // JSONObject surveyResponse = new JSONObject(respondent);
        // if (resultCode != RESULT_OK){
        // SMError e = (SMError) data.getSerializableExtra(SMError);
        //}

    }

