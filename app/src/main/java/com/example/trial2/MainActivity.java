package com.example.trial2;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.text.TextUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;


public class MainActivity extends AppCompatActivity {
    // creating variables for edi text,
    // button and our text views.
    private EditText pinCodeEdt;
    private TextView pinCodeDetailsTV;

    // creating a variable for our string.
    String pinCode;

    // creating a variable for request queue.
    private RequestQueue mRequestQueue;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // initializing our variables.
        pinCodeEdt = findViewById(R.id.idedtPinCode);
        Button getDataBtn = findViewById(R.id.idBtnGetCityandState);
        pinCodeDetailsTV = findViewById(R.id.idTVPinCodeDetails);

        // initializing our request que variable with request
        // queue and passing our context to it.
        mRequestQueue = Volley.newRequestQueue(MainActivity.this);

        // initialing on click listener for our button.
        getDataBtn.setOnClickListener(v -> {
            // getting string from EditText.
            pinCode = pinCodeEdt.getText().toString();

            // validating if the text is empty or not.
            if (TextUtils.isEmpty(pinCode)) {
                // displaying a toast message if the
                // text field is empty
                Toast.makeText(MainActivity.this, "Please enter valid pin code", Toast.LENGTH_SHORT).show();
            } else {
                // calling a method to display
                // our pincode details.
                getDataFromPinCode(pinCode);
            }
        });
    }

    private void getDataFromPinCode(String pinCode) {

        // clearing our cache of request queue.
        mRequestQueue.getCache().clear();

        // below is the url from where we will be getting
        // our response in the json format.
        String url = "http://www.postalpincode.in/api/pincode/" + pinCode;


        // below line is use to initialize our request queue.
        RequestQueue queue = Volley.newRequestQueue(MainActivity.this);

        // in below line we are creating a
        // object request using volley.
         @SuppressLint({"SetTextI18n", "SetTextI18n"}) JsonObjectRequest objectRequest = new JsonObjectRequest(Request.Method.GET, url, null, response -> {
             // inside this method we will get two methods
             // such as on response method
             // inside on response method we are extracting
             // data from the json format.
             try {
                 // we are getting data of post office
                 // in the form of JSON file.
                 JSONArray postOfficeArray = response.getJSONArray("PostOffice");
                if (response.getString("Status").equals("Error")) {
                     // validating if the response status is success or failure.
                     // in this method the response status is having error and
                     // we are setting text to TextView as invalid pincode.
                    pinCodeDetailsTV.setText("not valid");
                 }  else{
                     // if the status is success we are calling this method
                     // in which we are getting data from post office object
                     // here we are calling first object of our json array.
                     JSONObject obj = postOfficeArray.getJSONObject(0);

                     // inside our json array we are getting district name,
                     // state and country from our data.
                     String district = obj.getString("District");
                     String state = obj.getString("State");
                     String country = obj.getString("Country");

                     // after getting all data we are setting this data in
                     // our text view on below line.
                     pinCodeDetailsTV.setText("Details of pin code is : \n" + "District is : " + district + "\n" + "State : "
                             + state + "\n" + "Country : " + country);
                 }
             } catch (JSONException e) {
                 // if we gets any error then it
                 // will be printed in log cat.
                 e.printStackTrace();
                 pinCodeDetailsTV.setText("Pin code is not valid");
             }
         }, error -> {
            // below method is called if we get
            // any error while fetching data from API.
            // below line is use to display an error message.
            Toast.makeText(MainActivity.this, "Pin code is not valid.", Toast.LENGTH_LONG).show();
            pinCodeDetailsTV.setText("Pin code is not valid");
        });
        // below line is use for adding object
        // request to our request queue.
        queue.add(objectRequest);
    }
}
