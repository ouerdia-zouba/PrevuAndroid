package com.android.prevu.visualisation;

import android.app.ActionBar;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Build;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.webkit.WebView;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.Toast;

import com.android.prevu.prevu.R;
import com.android.prevu.rechercheLivre.Auteur;
import com.android.prevu.rechercheLivre.AuthorAdapter;
import com.android.prevu.rechercheLivre.Book;
import com.android.prevu.rechercheLivre.BookAdapter;
import com.android.prevu.rechercheLivre.BookImageAdapter;
import com.android.prevu.rechercheLivre.InformationLivre;
import com.android.prevu.rechercheLivre.Recherche;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestHandle;
import com.loopj.android.http.RequestParams;

import org.codehaus.jackson.map.ObjectMapper;
import org.codehaus.jackson.type.TypeReference;

import java.io.BufferedInputStream;
import java.io.InputStream;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class LivresPopulaires extends Activity   implements AdapterView.OnItemSelectedListener, AdapterView.OnItemClickListener  {
    private int position;
    private ProgressDialog prgDialog;

    private static ObjectMapper sMapper;
    private ArrayList<Book> listBook;
    private ArrayList<Auteur> listAuteur;
    BookImageAdapter bookAdapter;
    AuthorAdapter authorAdapter;
    private String lienWebService;
    private String lienJavaScript;
    private ListView listVisualisationLivre;
    private Spinner spinner;
    String annee;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_livres_populaires);
        position = getIntent().getExtras().getInt("position");
        listVisualisationLivre = (ListView) findViewById(R.id.listViewLivresPolulaires);
        listVisualisationLivre.setOnItemClickListener(this);
        spinner = (Spinner) findViewById(R.id.spinnerLivre);
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


        lienWebService = "http://" + getResources().getString(R.string.url) + ":8888/prevu/api/issues/livre/";
        spinner.setOnItemSelectedListener(this);
        prgDialog = new ProgressDialog(this);
        prgDialog.setMessage("Please wait...");
        prgDialog.setCancelable(true);

       }


    public void invokeWSLivre(RequestParams params, String lienWebService) {
        // Make RESTful webservice call using AsyncHttpClient object
        AsyncHttpClient client = new AsyncHttpClient();
        prgDialog.show();

        RequestHandle requestHandle = client.get(lienWebService, params,
                new AsyncHttpResponseHandler() {


                    // When the response returned by REST has Http response code '200'
                    @Override
                    public void onSuccess(String response) {
                        prgDialog.hide();
                        // JSON Object
                        sMapper = new ObjectMapper();
                        try {

                            listBook = sMapper.readValue(response, new TypeReference<ArrayList<Book>>() {
                                    }
                            );

                            bookAdapter = new BookImageAdapter(getBaseContext(),
                                    LivresPopulaires.this, listBook);
                            listVisualisationLivre.setAdapter(bookAdapter);






                        } catch (Exception e) {
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
                });}

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



    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        switch (position) {
            case 0:
                annee = "2012";
                invokeWSLivre(new RequestParams(), lienWebService + annee);
                break;
            case 1:
                annee = "2013";
                invokeWSLivre(new RequestParams(), lienWebService + annee);
                break;
            case 2:
                annee = "2014";
                invokeWSLivre(new RequestParams(), lienWebService + annee);
                break;
            case 3:
                annee = "2015";
                invokeWSLivre(new RequestParams(), lienWebService + annee );
                break;

        }

    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        Book book = listBook.get(position);
        Intent i = new Intent(this, InformationLivre.class);
        i.putExtra("idLivre", book.getId_notice());
        startActivity(i);
    }


}
