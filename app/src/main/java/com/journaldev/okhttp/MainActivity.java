package com.journaldev.okhttp;

import android.content.Intent;
import android.graphics.Color;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;

import nl.dionsegijn.konfetti.Confetti;
import nl.dionsegijn.konfetti.KonfettiView;
import nl.dionsegijn.konfetti.models.Shape;
import nl.dionsegijn.konfetti.models.Size;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import com.newrelic.agent.android.NewRelic;
import com.newrelic.agent.android.logging.AgentLog;


import static android.provider.AlarmClock.EXTRA_MESSAGE;


public class MainActivity extends AppCompatActivity implements View.OnClickListener {



    TextView txtString;
    Button asynchronousGet, synchronousGet, asynchronousPOST;
    public KonfettiView konfettiView;
    public String url = "https://reqres.in/api/users/2";
    //public String url = "notrealurl";
    public String postUrl = "https://reqres.in/api/users/";
    public String postBody = "{\n" +
            "    \"name\": \"morpheus\",\n" +
            "    \"job\": \"leader\"\n" +
            "}";

    public static final MediaType JSON = MediaType.parse("application/json; charset=utf-8");
    public static final String EXTRA_MESSAGE = "com.example.myfirstapp.MESSAGE";
    private String text;
    private static final String LOG_TAG = MainActivity.class.getSimpleName();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        NewRelic.withApplicationToken("AAbabf66540b356e317c7e0ce2679f72644cf4b074")
                .withLogLevel(AgentLog.DEBUG)
                .start(this.getApplication());
        asynchronousGet = (Button) findViewById(R.id.asynchronousGet);
        synchronousGet = (Button) findViewById(R.id.synchronousGet);
        asynchronousPOST = (Button) findViewById(R.id.asynchronousPost);


        asynchronousGet.setOnClickListener(this);
        synchronousGet.setOnClickListener(this);
        asynchronousPOST.setOnClickListener(this);

        konfettiView = (KonfettiView)findViewById(R.id.viewKonfetti);

        txtString = (TextView) findViewById(R.id.txtString);
        // session attribute
        NewRelic.setAttribute("plantype", "Platinum");

    }


    /** Called when the user taps the Send button */
    public void sendMessage(View view) {
        Intent intent = new Intent(this, DisplayMessageActivity.class);
        EditText editText = (EditText) findViewById(R.id.editText);
        String message = editText.getText().toString();
        intent.putExtra(EXTRA_MESSAGE, message);
        startActivity(intent);
        Map textbox = new HashMap();
        textbox.put("textbox1", message);

        // NewRelic.recordCustomEvent("usersubmittedtext", textbox);
        NewRelic.recordBreadcrumb("usersubmittedtext", textbox);

    }


    public void buttonOnClick(View v) {
        Button button = (Button) v;
        ((Button) v).setText("hooray!");
        Map attributes = new HashMap();
        attributes.put("make", "townhouse");
        NewRelic.recordCustomEvent("House", attributes);
    }

     public void conditionalButton (View v) {
        Button button;
         button = (Button) v;
         Random rnd = new Random();

        if (button.getText() == "happy") {
            button.setText("new");
            NewRelic.recordMetric("randomlong","buttonclick", rnd.nextLong());
            NewRelic.incrementAttribute("happy_clicked");
        } else if (button.getText() == "new") {
            button.setText("year!");
            NewRelic.recordMetric("randomfloat","buttonclick", rnd.nextFloat());
        } else {
            button.setText("happy");
            NewRelic.recordMetric("randomint","buttonclick", rnd.nextInt());
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
            NewRelic.recordMetric("If Else button click","buttonclick", rnd.nextInt());
             }


    void postRequest(String postUrl, String postBody) throws IOException {

        OkHttpClient client = new OkHttpClient();

        RequestBody body = RequestBody.create(JSON, postBody);

        Request request = new Request.Builder()
                .url(postUrl)
                .post(body)
                .build();

        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                call.cancel();
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {

                Log.d("TAG", response.body().string());
            }
        });
    }


    void run() throws IOException {

        OkHttpClient client = new OkHttpClient();

        Request request = new Request.Builder()
                .url(url)
                .build();

        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                call.cancel();
                NewRelic.recordHandledException(e);
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {

                final String myResponse = response.body().string();

                MainActivity.this.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        try {

                            JSONObject json = new JSONObject(myResponse);
                            txtString.setText("First Name: "+json.getJSONObject("data").getString("first_name") + "\nLast Name: " + json.getJSONObject("data").getString("last_name"));
                        } catch (JSONException e) {
                            e.printStackTrace();
                            NewRelic.recordHandledException(e);
                        }
                    }
                });

            }
        });
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.asynchronousGet:
                Map attributes = new HashMap();
                attributes.put("make", "Ford");
                attributes.put("model", "ModelT");
                attributes.put("color", "Black");
                attributes.put("VIN", "123XYZ");
                attributes.put("maxSpeed", 12);

                NewRelic.recordCustomEvent("Car", attributes);
                try {
                    run();
                } catch (Exception e) {
                    e.printStackTrace();
                    NewRelic.recordHandledException(e);
                }
                break;
            case R.id.synchronousGet:
                String strAnimal = new String();
                strAnimal = "dog";

                Map pet = new HashMap();
                pet.put ("animal",strAnimal);
                pet.put ( "breed", "german separd");
                pet.put ("age", 3);
                pet.put ( "weight", "90 pounds");

                NewRelic.recordCustomEvent("Woody", pet);
                OkHttpHandler okHttpHandler = new OkHttpHandler();
                okHttpHandler.execute(url);
                break;
            case R.id.asynchronousPost:
                // NewRelic.crashNow("test 123");
                try {
                    postRequest(postUrl, postBody);
                } catch (Exception e) {
                    e.printStackTrace();
                    NewRelic.recordHandledException(e);
                }
                break;

        }
    }

    public void setText(String text) {
        this.text = text;
    }

    public void launchPartyActivity(View view) {
        //Log.d (LOG_TAG, "Button clicked!");
        Intent intent = new Intent(this, ConfettiActivity.class);
        startActivity(intent);

    }

    public class OkHttpHandler extends AsyncTask<String, Void, String> {

        OkHttpClient client = new OkHttpClient();

        @Override
        protected String doInBackground(String... params) {

            Request.Builder builder = new Request.Builder();
            builder.url(params[0]);
            Request request = builder.build();

            try {
                Response response = client.newCall(request).execute();
                return response.body().string();
            } catch (Exception e) {
                e.printStackTrace();
                NewRelic.recordHandledException(e);
            }
            return null;
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            txtString.setText(s);
        }
    }

}



