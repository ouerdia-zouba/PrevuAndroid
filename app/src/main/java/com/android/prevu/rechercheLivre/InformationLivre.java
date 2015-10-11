package com.android.prevu.rechercheLivre;

import android.annotation.SuppressLint;
import android.app.ActionBar;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;

import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.webkit.JavascriptInterface;
import android.webkit.WebView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.android.prevu.prevu.R;
import com.android.prevu.prevu.prevu;
import com.android.prevu.visualisation.VisualisationInfo;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestHandle;
import com.loopj.android.http.RequestParams;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.StatusLine;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.codehaus.jackson.map.ObjectMapper;
import org.codehaus.jackson.type.TypeReference;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;

@SuppressLint("SetJavaScriptEnabled")
public class InformationLivre extends Activity {
    private TextView bookTitle, author_prenom, author_nom, nombre_emprunt, dateText, ratingCountText;
    private String titre;
    private int idLivre;
    private String isbn;
    //private  String titreisbn;
    private String bookSearchString;
    private ProgressDialog prgDialog;
    private static ObjectMapper sMapper;
    private ArrayList<Book> listBook;
    private Book book;
    private String url;
    private ImageView thumbView;
    private ImageView[] starViews;
    private Bitmap thumbImg;
    private LinearLayout starLayout;
    private int position;

    private String jsonResponse;

    String annee;
    private WebView webView;


    private String lienJavaScript;
    private String lienWebService;
    private String retourUfr;
    private String chaine = "";
    private String chaine1 = "";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_information__livre);
        bookTitle = (TextView) findViewById(R.id.bookTitle);
        author_prenom = (TextView) findViewById(R.id.authorPrenom);
        author_nom = (TextView) findViewById(R.id.authorNom);
        nombre_emprunt = (TextView) findViewById(R.id.nombre_emprunt);
        dateText = (TextView) findViewById(R.id.book_date);
        starLayout = (LinearLayout) findViewById(R.id.star_layout);
        ratingCountText = (TextView) findViewById(R.id.book_rating_count);
        thumbView = (ImageView) findViewById(R.id.thumb);
        starViews = new ImageView[5];
        for (int s = 0; s < starViews.length; s++) {
            starViews[s] = new ImageView(this);


        }
        webView = (WebView) findViewById(R.id.web);
        prgDialog = new ProgressDialog(this);
        prgDialog.setMessage("Please wait...");
        prgDialog.setCancelable(true);
        //titre = getIntent().getExtras().getString("titre");
        // isbn = getIntent().getExtras().getString("isbn");
        lienJavaScript = "file:///android_asset/noticeIssuesByUfr.html";

        if (getIntent().getExtras().getInt("idLivre") != 0) {
            idLivre = getIntent().getExtras().getInt("idLivre");


            RequestParams params = new RequestParams();

            params.put("idLivre", idLivre);
            url = "http://" + getResources().getString(R.string.url) + ":8888/prevu/api/book/info/" + idLivre;

            invokeWS(params);

        } else if (getIntent().getExtras().getString("isbn") != null) {
            isbn = getIntent().getExtras().getString("isbn");
            url = "http://" + getResources().getString(R.string.url) + ":8888/prevu/api/book/isbn/"+isbn;

            RequestParams params = new RequestParams();

            invokeWS(params);

        }

    }

    public void invokeWS(RequestParams params) {
        // Show Progress Dialog
        prgDialog.show();
        // Make RESTful webservice call using AsyncHttpClient object
        AsyncHttpClient client = new AsyncHttpClient();

        client.get(url, params, new AsyncHttpResponseHandler() {
            // When the response returned by REST has Http response code '200'
            @Override
            public void onSuccess(String response) {
                // Hide Progress Dialog
                prgDialog.hide();
                // JSON Object
                sMapper = new ObjectMapper();
                try {
                    book = sMapper.readValue(response, new TypeReference<Book>() {

                    });
                    // listBook = sMapper.readValue(response, new TypeReference<ArrayList<Book>>() {
// Hide Progress Dialog

                    jsonResponse = response;

                    // });
                    bookTitle.setText("TITLE: " + book.getTitle());
                    author_prenom.setText("Author: " + book.getAuthor_prenom());
                    author_nom.setText(" " + book.getAuthor_nom());
                    nombre_emprunt.setText(("Nombre Emprunt: " + book.getIssues()));
                    dateText.setText("PUBLISHED: " + book.getPublicationyear());

                    try {
//set stars
                        double decNumStars = Double.parseDouble("3");
                        int numStars = (int) decNumStars;
                        starLayout.setTag(numStars);
                        starLayout.removeAllViews();
                        for (int s = 0; s < numStars; s++) {
                            starViews[s].setImageResource(R.drawable.star);
                            starLayout.addView(starViews[s]);
                        }
                    } catch (Exception e) {
                        starLayout.removeAllViews();
                        e.printStackTrace();
                    }
                    try {
                        ratingCountText.setText(" - " + book.getIssues() + " ratings");
                    } catch (Exception e) {
                        ratingCountText.setText("");
                        e.printStackTrace();
                    }
                    try {
                        // JSONObject imageInfo = volumeObject.getJSONObject("imageLinks");
                        new GetBookThumb().execute(book.getImage());
                    } catch (Exception jse) {
                        thumbView.setImageBitmap(null);
                        jse.printStackTrace();
                    }
                    // cas recherche par ISBN
                    idLivre = book.getId_notice();
                    invokeWSCharts();
                } catch (Exception e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }

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

    private class GetBookThumb extends AsyncTask<String, Void, String> {
        //get thumbnail
        @Override
        protected String doInBackground(String... thumbURLs) {
//attempt to download image
            try {
//try to download
                URL thumbURL = new URL(thumbURLs[0]);
                URLConnection thumbConn = thumbURL.openConnection();
                thumbConn.connect();
                InputStream thumbIn = thumbConn.getInputStream();
                BufferedInputStream thumbBuff = new BufferedInputStream(thumbIn);
                thumbImg = BitmapFactory.decodeStream(thumbBuff);
                thumbBuff.close();
                thumbIn.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
            return "";
        }

        protected void onPostExecute(String result) {
            thumbView.setImageBitmap(thumbImg);
        }
    }
    public class WebAppInterface {

        @JavascriptInterface
        public String getRetourUfr() {

            return retourUfr;
        }
        @JavascriptInterface
        public String getChaine() {

            return chaine;
        }
        @JavascriptInterface
        public String getChaine1() {

            return chaine1;
        }


    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_information__livre, menu);
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




    public void invokeWSCharts() {
        // Show Progress Dialog
        prgDialog.show();

        // Make RESTful webservice call using AsyncHttpClient object
        AsyncHttpClient client = new AsyncHttpClient();
        RequestHandle requestHandle = client.get("http://" + getResources().getString(R.string.url) + ":8888/prevu/api/ufr/book/" + idLivre, new AsyncHttpResponseHandler() {
            // When the response returned by REST has Http response code '200'
            @Override
            public void onSuccess(String response) {
                retourUfr = response;
                lienWebService = "http://" + getResources().getString(R.string.url) + ":8888/prevu/api/niveau/book/" + idLivre;
                AsyncHttpClient client = new AsyncHttpClient();
                RequestHandle requestHandle = client.get(lienWebService, new RequestParams(), new AsyncHttpResponseHandler() {
                    @Override
                    public void onSuccess(String response) {
                        chaine = response;
                        lienWebService = "http://" + getResources().getString(R.string.url) + ":8888/prevu/api/categorie/book/" + idLivre;
                        AsyncHttpClient client = new AsyncHttpClient();
                        RequestHandle requestHandle = client.get(lienWebService, new RequestParams(), new AsyncHttpResponseHandler() {
                            @Override
                            public void onSuccess(String response) {
                                prgDialog.hide();
                                chaine1 = response;

                                webView.addJavascriptInterface(new WebAppInterface(), "Android");
                                webView.getSettings().setJavaScriptEnabled(true);
                                webView.loadUrl(lienJavaScript);

                               // webView.addJavascriptInterface(new WebAppInterface(), "Android");
                               // webView.getSettings().setJavaScriptEnabled(true);
                               // webView.loadUrl("file:///android_asset/noticeIssuesByNiveau.html");




                            }});


                    }});
            }});




        // Make RESTful webservice call using AsyncHttpClient object
       /* AsyncHttpClient client = new AsyncHttpClient();

        client.get("http://" + getResources().getString(R.string.url) + ":8888/prevu/api/ufr/book/" + idLivre, new RequestParams(), new AsyncHttpResponseHandler() {
            // When the response returned by REST has Http response code '200'
            @Override
            public void onSuccess(String response) {
                    prgDialog.hide();
                    retourUfr = response;


                    webView = (WebView) findViewById(R.id.web);
                    webView.addJavascriptInterface(new WebAppInterface(), "Android");
                    webView.getSettings().setJavaScriptEnabled(true);
                    webView.loadUrl(lienJavaScript);

            }*/

                            // When the response returned by REST has Http response code other than '200'



                            }





}