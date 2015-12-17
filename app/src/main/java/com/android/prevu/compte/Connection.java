package com.android.prevu.compte;

import android.app.ActionBar;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.android.prevu.prevu.ApplicationPrevu;
import com.android.prevu.prevu.R;
import com.android.prevu.prevu.prevu;
import com.android.prevu.rechercheLivre.Recherche;
import com.android.prevu.rechercheLivre.Utility;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestHandle;

import org.apache.http.entity.StringEntity;
import org.json.JSONObject;

public class Connection extends Activity implements View.OnClickListener{

    // Email, password edittext
    EditText txtUsername, txtPassword;

    // login button
    Button btnLogin;

    // Alert Dialog Manager
    AlertDialogManager alert = new AlertDialogManager();

    // Session Manager Class
    SessionManagement session;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login1);

        // Session Manager
        session = new SessionManagement(getApplicationContext());

        // Email, Password input text
        txtUsername = (EditText) findViewById(R.id.txtUsername);
        txtPassword = (EditText) findViewById(R.id.txtPassword);

        // Login button
        btnLogin = (Button) findViewById(R.id.btnLogin);


        // Login button click event
        btnLogin.setOnClickListener(this);
        ActionBar actionBar = getActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
    }

    @Override
    public void onClick(View arg0) {
        // Get username, password from EditText
        String username = txtUsername.getText().toString();
        String password = txtPassword.getText().toString();

        // Check if username, password is filled
        if(username.trim().length() > 0 && password.trim().length() > 0 && Utility.validate(username)) {
            invokeWS();
        }else
        {
            // user didn't entered username or password
            // Show alert asking him to enter the details
            alert.showAlertDialog(Connection.this, "Login failed..", "Please enter valid email and password", false);
        }

    }
    public void invokeWS(){
        try{
            final  boolean connectionOk= true;
            AsyncHttpClient client = new AsyncHttpClient();
            JSONObject jsonParams = new JSONObject();
            jsonParams.put("mail", txtUsername.getText().toString());
            jsonParams.put("password", txtPassword.getText().toString());
            StringEntity entity = new StringEntity(jsonParams.toString());


            RequestHandle requestHandle = client.post(getApplicationContext(), getResources().getString(R.string.url) + "api/android/utilisateur/connection",
                    entity, "application/json", new AsyncHttpResponseHandler() {


                        // When the response returned by REST has Http response code '200'
                        @Override
                        public void onSuccess(String response) {
                            // JSON Object

                            try {


                                JSONObject reponseJson = new JSONObject(response);
                                String result = reponseJson.get("result").toString();
                                // String message = reponseJson.get("message").toString();
                                //Toast.makeText(getApplicationContext(), message, Toast.LENGTH_LONG).show();
                                if (result.equals("OK")) {

                                    // Creating user login session
                                    // For testing i am stroing name, email as follow
                                    // Use user real data
                                    session.createLoginSession(txtUsername.getText().toString());
                                    // Staring MainActivity
                                    ///Intent i = new Intent(getApplicationContext(), MainActivity.class);


                                    Intent i = new Intent(getApplicationContext(), Deconnection.class);
                                    startActivity(i);
                                    finish();
                                    //getApplicationContext().startActivity(i);



                                }
                                else {

                                    // username / password doesn't match
                                    alert.showAlertDialog(Connection.this, "Login failed..", "Username/Password is incorrect", false);

                                }

                            }




                            catch (Exception e) {
                                // TODO Auto-generated catch block
                                e.printStackTrace();
                            }

                        }

                        // When the response returned by REST has Http response code other than '200'
                        @Override
                        public void onFailure(int statusCode, Throwable error, String content) {
                            // When Http response code is '404'
                            if (statusCode == 404) {
                                Toast.makeText(getApplicationContext(), "Requested resource not found", Toast.LENGTH_LONG).show();
                            }
                            // When Http response code is '500'
                            else if (statusCode == 500) {
                                Toast.makeText(getApplicationContext(), "Something went wrong at server end", Toast.LENGTH_LONG).show();
                            }
                            // When Http response code other than 404, 500
                            else {
                                Toast.makeText(getApplicationContext(), "Unexpected Error occcured! [Most common Error: Device might not be connected to Internet or remote server is not up and running]", Toast.LENGTH_LONG).show();
                            }
                        }
                    });} catch(Exception e){
            e.printStackTrace();
        }
    }
    /**
     * Method which navigates from Login Activity to Home Activity
     */
    public void navigatetoHomeActivity(){
        Intent homeIntent = new Intent(getApplicationContext(),Deconnection.class);
        //homeIntent.putExtra("loginString",username);
        homeIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(homeIntent);
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        Intent intent = new Intent(this, ApplicationPrevu.class); intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);
        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}