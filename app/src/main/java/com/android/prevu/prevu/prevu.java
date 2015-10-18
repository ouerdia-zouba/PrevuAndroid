

package com.android.prevu.prevu;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.gesture.Gesture;
import android.gesture.GestureLibraries;
import android.gesture.GestureLibrary;
import android.gesture.GestureOverlayView;
import android.gesture.Prediction;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;


import com.android.prevu.compte.LoginActivity;
import com.android.prevu.compte.LoginActivity1;
import com.android.prevu.rechercheLivre.Auteur;
import com.android.prevu.rechercheLivre.AuthorAdapter;
import com.android.prevu.rechercheLivre.Book;
import com.android.prevu.rechercheLivre.BookImageAdapter;
import com.android.prevu.rechercheLivre.ImageAdapter;
import com.android.prevu.rechercheLivre.InformationLivre;
import com.android.prevu.rechercheLivre.Recherche;
import com.android.prevu.scannerBarCode.IntentIntegrator;
import com.android.prevu.scannerBarCode.IntentResult;

import com.android.prevu.visualisation.LivresPopulaires;
import com.android.prevu.visualisation.Visualisation;
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
import android.view.ViewGroup.LayoutParams;

public class prevu extends Activity implements View.OnClickListener {
    private ImageView recherche;
    private ImageView scan;
    private ImageView visualisation;
    private ImageView about, compte;
    private TextView formatTxt, contentTxt;
    private GestureLibrary gestureLib;

    private int position;
    private ProgressDialog prgDialog;

    private static ObjectMapper sMapper;
    private ArrayList<Book> listBook;
    ImageAdapter imageAdapter;

    private String lienWebService;
    private String lienJavaScript;
   // private ListView listViewImageLivresPolulaires;
    private LinearLayout horizontalLayout;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_prevu);
       // recherche = (ImageView) findViewById(R.id.recherche);
       // compte = (ImageView) findViewById(R.id.recherche);
        scan = (ImageView) findViewById(R.id.scan);
        //visualisation = (ImageView) findViewById(R.id.viz);
        about = (ImageView) findViewById(R.id.about);
        compte = (ImageView) findViewById(R.id.compte);
        //recherche.setOnClickListener(this);
        scan.setOnClickListener(this);
       // about.setOnClickListener(this);
       // visualisation.setOnClickListener(this);
        //compte.setOnClickListener(this);

        // formatTxt = (TextView)findViewById(R.id.scan_format);
        // contentTxt = (TextView)findViewById(R.id.scan_content);
       // position = getIntent().getExtras().getInt("position");
       // listViewImageLivresPolulaires = (ListView) findViewById(R.id.listViewImageLivresPolulaires);
        //listViewImageLivresPolulaires.setOnItemClickListener(this);
        horizontalLayout = (LinearLayout)findViewById(R.id.mygallery);




       // lienWebService = "http://" + getResources().getString(R.string.url) + ":8888/prevu/api/issues/livre/";

        prgDialog = new ProgressDialog(this);
        prgDialog.setMessage("Please wait...");
        prgDialog.setCancelable(true);
        RequestParams params = new RequestParams();



        invokeWSLivre(params);

    }
    public void invokeWSLivre(RequestParams params){
        // Show Progress Dialog
        prgDialog.show();
        // Make RESTful webservice call using AsyncHttpClient object
        AsyncHttpClient client = new AsyncHttpClient();
        client.get("http://"+getResources().getString(R.string.url)+":8888/prevu/api/issues/livre/", params, new AsyncHttpResponseHandler() {
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

                        new GetBookThumb().execute();








                           // imageAdapter = new ImageAdapter(getBaseContext(),
                         //           prevu.this, listBook);
                         //   listViewImageLivresPolulaires.setAdapter(imageAdapter);







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

    private class GetBookThumb extends AsyncTask<String, Void, String> {
        //get thumbnail
        @Override
        protected String doInBackground(String... thumbURLs) {
//attempt to download image
            try {
//try to download
                for (Book book:
                     listBook) {
                    URL thumbURL = new URL(book.getImage());
                    URLConnection thumbConn = thumbURL.openConnection();
                    thumbConn.connect();
                    InputStream thumbIn = thumbConn.getInputStream();
                    BufferedInputStream thumbBuff = new BufferedInputStream(thumbIn);
                    book.setThumbImg(BitmapFactory.decodeStream(thumbBuff));
                    thumbBuff.close();
                    thumbIn.close();
                }


            } catch (Exception e) {
                e.printStackTrace();
            }
            return "";
        }
        protected void onPostExecute(String result) {
            for (Book book:listBook) {
                LinearLayout layout = new LinearLayout(getApplicationContext());
                layout.setLayoutParams(new ViewGroup.LayoutParams(250, 250));

                layout.setGravity(Gravity.CENTER);
                ImageView imageView = new ImageView(getApplicationContext());
                imageView.setLayoutParams(new ViewGroup.LayoutParams(200, 320));
                imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
                imageView.setImageBitmap(book.getThumbImg());
                layout.addView(imageView);
                horizontalLayout.addView(layout);
            }
        }
    }


    public void onClick(View v) {

       /* if (v.getId() == R.id.recherche) {


            Intent i = new Intent(this, Recherche.class);
            startActivity(i);

        }*/
         if (v.getId() == R.id.scan) {
            IntentIntegrator scanIntegrator = new IntentIntegrator(this);
            scanIntegrator.initiateScan();


            //Intent i = new Intent(this, ScannerBarCode.class);
            //startActivity(i);
        }

         /*else if (v.getId() == R.id.viz) {
            Intent i = new Intent(this, Visualisation.class);
            startActivity(i);
        }

         else if (v.getId() == R.id.compte) {
            Intent i = new Intent(this, FragmentCustomAnimations.class);
            startActivity(i);
        }*/
    }

    public void onActivityResult(int requestCode, int resultCode, Intent intent) {
//retrieve scan result
        IntentResult scanningResult = IntentIntegrator.parseActivityResult(requestCode, resultCode, intent);
        if (scanningResult != null) {
            String scanContent = scanningResult.getContents();

            Intent informationIntent = new Intent(getApplicationContext(), InformationLivre.class);
            informationIntent.putExtra("isbn", scanContent);
            startActivity(informationIntent);


        } else {
            Toast toast = Toast.makeText(getApplicationContext(),
                    "No scan data received!", Toast.LENGTH_SHORT);
            toast.show();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_prevu, menu);

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        switch (item.getItemId()) {

            case R.id.menu_settings:
                // Comportement du bouton "Paramètres"
                return true;
            case R.id.about:
                // Comportement du bouton "A Propos"
                Intent i = new Intent(this, About.class);
                startActivity(i);
                return true;

            case R.id.recherche:
                // Comportement du bouton "A Propos"
                Intent i1 = new Intent(this, Recherche.class);
                startActivity(i1);
                return true;

            case R.id.compte:
                // Comportement du bouton "A Propos"
                Intent i2 = new Intent(this, LoginActivity1.class);
                startActivity(i2);
                return true;

            case R.id.viz:
                // Comportement du bouton "A Propos"
                Intent i3 = new Intent(this, Visualisation.class);
                startActivity(i3);
                return true;

            default:
                return super.onOptionsItemSelected(item);
        }
    }




    /*@Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        Book book = listBook.get(position);
        Intent i = new Intent(this, InformationLivre.class);
        i.putExtra("idLivre", book.getId_notice());
        startActivity(i);
    }*/


}