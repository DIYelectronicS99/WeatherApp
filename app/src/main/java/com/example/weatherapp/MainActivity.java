package com.example.weatherapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.Calendar;

public class MainActivity extends AppCompatActivity {

    TextView date, city, temp , desc;
    EditText inputcity;
    Button getinput;
    ProgressBar spinner;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        date = findViewById(R.id.tv1);
        city = findViewById(R.id.tv2);
        temp = findViewById(R.id.tv3);
        desc = findViewById(R.id.tv4);
        inputcity = findViewById(R.id.et1);
        getinput = findViewById(R.id.btn1);
        spinner = findViewById(R.id.pb1);

        spinner.setVisibility(View.GONE);


        getinput.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                closeKeyboard();
                weatherNow();
            }
        });

        //weatherNow();

    }

    public void weatherNow() {

        final String input_city = inputcity.getText().toString();
        //String locurl = "api.openweathermap.org/data/2.5/weather?q=" +input_city+ "&appid=562f305cbac12c6a01b39d7ca75675d0";
        String locurlprim = "http://api.openweathermap.org/data/2.5/weather?q=";
        String locurlpost = "&appid=562f305cbac12c6a01b39d7ca75675d0&units=imperial";
        String locurl = locurlprim.concat(input_city).concat(locurlpost);
        Log.i("final url:", locurl);
        //String locurl = "http://api.openweathermap.org/data/2.5/weather?q=kolkata&appid=562f305cbac12c6a01b39d7ca75675d0";
        if (input_city == "")
            Toast.makeText(getApplicationContext(), "Fill in all details ploxxx, loduxxx", Toast.LENGTH_SHORT);
        else{
            spinner.setVisibility(View.VISIBLE);
        JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, locurl, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try {
                    JSONObject mainobj = response.getJSONObject("main");
                    JSONArray array = response.getJSONArray("weather");
                    JSONObject object = array.getJSONObject(0);
                    String tempval = String.valueOf(mainobj.getDouble("temp"));
                    String description = object.getString("description");
                    String cityname = response.getString("name");


                    Calendar calendar = Calendar.getInstance();
                    SimpleDateFormat simpleDateFormat = new SimpleDateFormat("EEE, MMM d, ''yy");
                    String finaldate = simpleDateFormat.format(calendar.getTime());


                        date.setText(finaldate);
                        city.setText(cityname);
                        desc.setText(description);
                        temp.setText(tempval);
                        spinner.setVisibility(View.GONE);


                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        }
        );
        RequestQueue queue = Volley.newRequestQueue(this);
        queue.add(request);
    }
    }

    private void closeKeyboard() {
        View view = this.getCurrentFocus();
        if (view != null) {
            InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
        }
    }
}
