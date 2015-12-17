package com.android.prevu.visualisation;

import android.annotation.SuppressLint;
import android.app.ActionBar;
import android.app.Activity;
import android.app.ProgressDialog;
import android.os.Build;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.webkit.JavascriptInterface;
import android.webkit.WebView;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.Toast;

import com.android.prevu.prevu.R;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestHandle;
import com.loopj.android.http.RequestParams;

import org.codehaus.jackson.map.ObjectMapper;

import java.util.Arrays;
import java.util.List;

@SuppressLint("SetJavaScriptEnabled")
public class VisualisationInfo extends Activity implements AdapterView.OnItemSelectedListener {
    private int position;
    private ProgressDialog prgDialog;
    private String jsonResponse;
    private static ObjectMapper sMapper;
    String annee;
    private WebView webView;
    private Spinner spinner;


    private String lienWebService;
    private String lienJavaScript;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_visualisation_info);
        position = getIntent().getExtras().getInt("position");

        prgDialog = new ProgressDialog(this);
        prgDialog.setMessage("Please wait...");
        prgDialog.setCancelable(true);
        webView = (WebView) findViewById(R.id.web);
        spinner = (Spinner) findViewById(R.id.spinner);
        List<String> listeAnnees = Arrays.asList("2012", "2013", "2014", "2015");
        ArrayAdapter anneeAdapter = new ArrayAdapter(
                this,
                android.R.layout.simple_spinner_item,
                listeAnnees
        );
            /* On definit une présentation du spinner quand il est déroulé         (android.R.layout.simple_spinner_dropdown_item) */
        anneeAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        //Enfin on passe l'adapter au Spinner et c'est tout
        spinner.setAdapter(anneeAdapter);
        spinner.setVisibility(View.INVISIBLE);

        switch (position) {
            case 0:
                spinner.setOnItemSelectedListener(this);
                spinner.setVisibility(View.VISIBLE);
                lienJavaScript = "file:///android_asset/chartCountIssueByMonth.html";
                lienWebService = getResources().getString(R.string.url) + "api/android/stats/issues/years/month/";
                break;
            case 1:
                lienWebService = getResources().getString(R.string.url) + "api/android/stats/issues/years/semestre";
                lienJavaScript = "file:///android_asset/chartCountIssueBySemestre.html";
                invokeWS(new RequestParams(),lienWebService , lienJavaScript);
                break;
            case 2:
                lienWebService = getResources().getString(R.string.url) + "api/android/stats/issues/years/dayofweek";
                lienJavaScript = "file:///android_asset/empruntsJourSemaine.html";
                invokeWS(new RequestParams(),lienWebService , lienJavaScript);
                break;
            case 3:
                lienWebService = getResources().getString(R.string.url) + "api/android/issues/matiere/";
                spinner.setOnItemSelectedListener(this);
                spinner.setVisibility(View.VISIBLE);
                lienJavaScript = "file:///android_asset/matierePlusEmprunteesAnnee.html";
                break;
            case 5:
                lienWebService = getResources().getString(R.string.url) + "api/android/stats/issues/ufr";
                lienJavaScript = "file:///android_asset/chartCountIssueByUFR.html";
                invokeWS(new RequestParams(),lienWebService , lienJavaScript);
                break;

            case 6:
                lienWebService = getResources().getString(R.string.url) + "api/android/stats/issues/categorie";
                lienJavaScript = "file:///android_asset/chartCategorieIssues.html";
                invokeWS(new RequestParams(),lienWebService , lienJavaScript);
                break;


        }

    }

    public void invokeWS(RequestParams params, String lienWebService, final String lienJavascript) {
        // Show Progress Dialog
        prgDialog.show();
        // Make RESTful webservice call using AsyncHttpClient object
        AsyncHttpClient client = new AsyncHttpClient();
        RequestHandle requestHandle = client.get(lienWebService, params, new AsyncHttpResponseHandler() {
            // When the response returned by REST has Http response code '200'
            @Override
            public void onSuccess(String response) {

                // Hide Progress Dialog
                prgDialog.hide();
                jsonResponse = response;

                webView = (WebView) findViewById(R.id.web);
                webView.addJavascriptInterface(new WebAppInterface(), "Android");
                webView.getSettings().setJavaScriptEnabled(true);
                webView.loadUrl(lienJavascript);
            }

            // When the response returned by REST has Http response code other than '200'
            @Override
            public void onFailure(int statusCode, Throwable error, String content) {
                // Hide Progress Dialog
                prgDialog.hide();
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
        });
    }


    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        switch (position) {
            case 0:
                annee = "2012";
                invokeWS(new RequestParams(),lienWebService +annee,lienJavaScript );
                break;
            case 1:
                annee = "2013";
                invokeWS(new RequestParams(),lienWebService +annee,lienJavaScript );
                break;
            case 2:
                annee = "2014";
                invokeWS(new RequestParams(),lienWebService +annee,lienJavaScript );
                break;
            case 3:
                annee = "2015";
                invokeWS(new RequestParams(),lienWebService +annee,lienJavaScript );
                break;

        }

    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {
    }

    public class WebAppInterface {


        @JavascriptInterface
        public String getJsonResponse() {

            return jsonResponse;
        }

        @JavascriptInterface
        public String getAnnee() {

            return annee;
        }


    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_visualisation_info, menu);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
            ActionBar actionBar = getActionBar();
            actionBar.setDisplayHomeAsUpEnabled(true);
        }
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {

            case android.R.id.home:
                onBackPressed();
        }
        return true;
    }
}
