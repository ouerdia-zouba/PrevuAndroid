package com.android.prevu.rechercheLivre;

import android.app.ActionBar;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.prevu.prevu.R;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import org.codehaus.jackson.map.DeserializationConfig;
import org.codehaus.jackson.map.ObjectMapper;
import org.codehaus.jackson.type.TypeReference;

import java.util.ArrayList;
import java.util.List;

public class InformationAuteur extends Activity implements AdapterView.OnItemClickListener {

    private  int id_auteur;
    private TextView  bookTitle, author_prenom,author_nom, nombre_emprunt ;
    private ProgressDialog prgDialog;
    private static ObjectMapper sMapper;
    private ArrayList<Book> listBook;
    private Auteur auteur;
    private Book book;
    private  ListView listViewBooks;
    private ImageView thumbView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_information__auteur);
        author_nom = (TextView)findViewById(R.id.nomAuteur);
       // bookTitle = (TextView)findViewById(R.id.book_title);
        listViewBooks = (ListView) findViewById(R.id.listViewFiltreBooks);
        listViewBooks.setOnItemClickListener(this);
       // thumbView = (ImageView)findViewById(R.id.thumb);
        prgDialog = new ProgressDialog(this);
        prgDialog.setMessage("Please wait...");
        prgDialog.setCancelable(true);
        id_auteur = getIntent().getExtras().getInt("idAuteur");
        RequestParams params = new RequestParams();


       invokeWS(params);
        invokeWSLivreAuteur(params);
        ActionBar actionBar = getActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);

    }
    public void invokeWS(RequestParams params){
        // Show Progress Dialog
        prgDialog.show();
        // Make RESTful webservice call using AsyncHttpClient object
        AsyncHttpClient client = new AsyncHttpClient();
        client.get("http://"+getResources().getString(R.string.url)+":8888/prevu/api/author/search/id/" + id_auteur, params, new AsyncHttpResponseHandler() {
            // When the response returned by REST has Http response code '200'
            @Override
            public void onSuccess(String response) {
                // Hide Progress Dialog
                prgDialog.hide();
                // JSON Object
                sMapper = new ObjectMapper();

                try {
                    auteur = sMapper.readValue(response, new TypeReference<Auteur>() {

                    });
                    // listBook = sMapper.readValue(response, new TypeReference<ArrayList<Book>>() {

                    // });
                    author_nom.setText("auteur: " + auteur.getAuthor_prenom() + " " + auteur.getAuthor_nom());
                    // bookTitle.setText("livre auteur:"+ book.getTitle());
                    //author_prenom.setText("Author: "+book.getAuthor_prenom());
                    // author_nom.setText(" "+book.getAuthor_nom());
                    // nombre_emprunt.setText("Nombre Emprunt: "+book.getIssues());

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
    public void invokeWSLivreAuteur(RequestParams params){
        // Show Progress Dialog
        prgDialog.show();
        // Make RESTful webservice call using AsyncHttpClient object
        AsyncHttpClient client = new AsyncHttpClient();
        client.get("http://"+getResources().getString(R.string.url)+":8888/prevu/api/book/search/title/"+id_auteur, params, new AsyncHttpResponseHandler() {
            // When the response returned by REST has Http response code '200'
            @Override
            public void onSuccess(String response) {
                // Hide Progress Dialog
                prgDialog.hide();
                // JSON Object
                sMapper = new ObjectMapper();

                try {
                    //book = sMapper.readValue(response, new TypeReference<Book>() {

                   // });
                     listBook = sMapper.readValue(response, new TypeReference<ArrayList<Book>>() {

                    });
                   // BookAdapter bookAdapter = new BookAdapter(getBaseContext(),
                         //   InformationAuteur.this, listBook);//il faut rajouter l'objet parsé
                 //  listViewBooks.setAdapter(bookAdapter);
                   String[] tableauString = new String[listBook.size()];

                    int i=0;
                   for (Book book:listBook) {
                       tableauString[i] = book.getTitle();
                       i++;
                    }

                    ArrayAdapter<String> adapter = new ArrayAdapter<String>(getBaseContext(),
                           android.R.layout.simple_dropdown_item_1line, tableauString);

                    listViewBooks.setAdapter(adapter);

                    // bookTitle.setText("livre auteur:"+ book.getTitle());
                    //author_prenom.setText("Author: "+book.getAuthor_prenom());
                    // author_nom.setText(" "+book.getAuthor_nom());
                    // nombre_emprunt.setText("Nombre Emprunt: "+book.getIssues());

                } catch (Exception e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                    author_nom.setText(e.getMessage());

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
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_information__auteur, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        Intent intent = new Intent(this, Recherche.class); intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);
        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
    Book book = listBook.get(position);
        Intent i = new Intent(this, InformationLivre.class);
        i.putExtra("idLivre", book.getId_notice());
        startActivity(i);
    }
}
