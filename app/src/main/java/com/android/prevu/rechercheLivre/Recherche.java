
package com.android.prevu.rechercheLivre;

import android.app.ActionBar;
import android.app.Activity;
import android.os.Bundle;

import android.text.Editable;
import android.text.TextWatcher;
import android.view.Menu;
import android.view.MenuItem;

import android.content.Intent;


//import java.util.ArrayList;


import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.RadioButton;

import android.widget.Toast;


import com.android.prevu.prevu.R;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestHandle;
import com.loopj.android.http.RequestParams;

import java.util.ArrayList;
import java.lang.String;
import java.util.List;

import org.codehaus.jackson.map.ObjectMapper;
import org.codehaus.jackson.type.TypeReference;


public class Recherche extends Activity implements View.OnClickListener, TextWatcher, OnItemClickListener {


    private String titre;
    private String auteur;
    private int idLivre;
    private int idAuteur;


    private RadioButton radioTitre;
    private RadioButton radioAuteur;


    private static ObjectMapper sMapper;
    private ArrayList<Book> listBook;
    private ArrayList<Auteur> listAuteur;
    BookAdapter bookAdapter;
    AuthorAdapter authorAdapter;


    // List view
    private ListView lv;

    // Search EditText
    private EditText inputSearch;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recherche);
        ActionBar actionBar = getActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);

        ImageButton recherche = (ImageButton) findViewById(R.id.recherche);

        radioAuteur = (RadioButton) findViewById(R.id.radio2);
        radioTitre = (RadioButton) findViewById(R.id.radio1);


        lv = (ListView) findViewById(R.id.list_view);
        lv.setOnItemClickListener(this);
        inputSearch = (EditText) findViewById(R.id.inputSearch);
        inputSearch.addTextChangedListener(this);

        recherche.setOnClickListener(this);
        radioAuteur.setOnClickListener(this);
        radioTitre.setOnClickListener(this);

    }

    /**
     * Method that performs RESTful webservice invocations
     *
     * @param params
     */

    public void invokeWSLivre(RequestParams params) {
        // Make RESTful webservice call using AsyncHttpClient object
        AsyncHttpClient client = new AsyncHttpClient();


        RequestHandle requestHandle = client.get(getResources().getString(R.string.url) + "api/android/book/search/" + titre, params,
                new AsyncHttpResponseHandler() {


                    // When the response returned by REST has Http response code '200'
                    @Override
                    public void onSuccess(String response) {
                        // JSON Object
                        sMapper = new ObjectMapper();
                        try {

                            listBook = sMapper.readValue(response, new TypeReference<ArrayList<Book>>() {
                                    }
                            );
                            bookAdapter = new BookAdapter(getBaseContext(),
                                    Recherche.this, listBook);
                            lv.setAdapter(bookAdapter);


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
                });
    }

    public void invokeWSAuteur(RequestParams params) {
        // Make RESTful webservice call using AsyncHttpClient object
        AsyncHttpClient client = new AsyncHttpClient();

        RequestHandle requestHandle = client.get(getResources().getString(R.string.url) + "api/android/author/search/" + auteur, params, new AsyncHttpResponseHandler() {
            // When the response returned by REST has Http response code '200'
            @Override
            public void onSuccess(String response) {
                // JSON Object
                sMapper = new ObjectMapper();
                try {

                    listAuteur = sMapper.readValue(response, new TypeReference<ArrayList<Auteur>>() {
                            }
                    );

                    authorAdapter = new AuthorAdapter(getBaseContext(),
                            Recherche.this, listAuteur);
                    lv.setAdapter(authorAdapter);


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
        });
    }


    public void onItemClick(AdapterView<?> parent, View view, int position,
                            long id) {

        if (radioTitre.isChecked()) {
            Book book = listBook.get(position);
            inputSearch.setText(book.getTitle() + ", (" + book.getAuthor_prenom() + "," + book.getAuthor_nom() + ")");
            idLivre = book.getId_notice();
        } else if (radioAuteur.isChecked()) {
            Auteur auteur = listAuteur.get(position);
            inputSearch.setText(auteur.getAuthor_prenom() + " " + auteur.getAuthor_nom() + " (" + auteur.getDatenaissance() + ", " + auteur.getDatedeces() + " )");
            idAuteur = auteur.getId_author();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_recherche, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        if (id == android.R.id.home)
            onBackPressed();


        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onClick(View v) {

        // else{

        //Toast.makeText(getApplicationContext(), "Veillez tapper au moin trois lettre", Toast.LENGTH_LONG).show();
        // }
        if (v.getId() == R.id.recherche && radioTitre.isChecked()) {
            Intent informationIntent = new Intent(getApplicationContext(), InformationLivre.class);
            informationIntent.putExtra("idLivre", idLivre);
            // informationIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(informationIntent);
            //RequestParams params = new RequestParams();
            //invokeWSInformationLivre(params);


            //Toast.makeText(RechercheActivity.this, autoComplete.getText(), 2).show();
        } else if (v.getId() == R.id.recherche && radioAuteur.isChecked()) {
            Intent informationIntent = new Intent(getApplicationContext(), InformationAuteur.class);
            String[] tab = inputSearch.getText().toString().split(",");

            informationIntent.putExtra("idAuteur", idAuteur);
            // informationIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(informationIntent);
        }


    }

    @Override
    public void onTextChanged(CharSequence cs, int arg1, int arg2, int arg3) {
        if (radioTitre.isChecked()) {

            titre = inputSearch.getText().toString();
            if (titre.length() >= 3) {
                RequestParams params = new RequestParams();
                invokeWSLivre(params);

            }
            //else if(titre.length() > 3){
            // bookAdapter.getFilter().filter(titre);
            // }
        } else if (radioAuteur.isChecked()) {
            auteur = inputSearch.getText().toString();
            if (auteur.length() >= 3) {
                RequestParams params = new RequestParams();
                invokeWSAuteur(params);
            }
        }

    }

    @Override
    public void beforeTextChanged(CharSequence arg0, int arg1, int arg2,
                                  int arg3) {
        // TODO Auto-generated method stub

    }

    @Override
    public void afterTextChanged(Editable arg0) {
        // TODO Auto-generated method stub
    }

}
