

package com.android.prevu.prevu;

import android.app.Activity;
import android.content.Intent;
import android.gesture.Gesture;
import android.gesture.GestureLibraries;
import android.gesture.GestureLibrary;
import android.gesture.GestureOverlayView;
import android.gesture.Prediction;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;


import com.android.prevu.compte.LoginActivity;
import com.android.prevu.compte.LoginActivity1;
import com.android.prevu.rechercheLivre.InformationLivre;
import com.android.prevu.rechercheLivre.Recherche;
import com.android.prevu.scannerBarCode.IntentIntegrator;
import com.android.prevu.scannerBarCode.IntentResult;

import com.android.prevu.visualisation.Visualisation;

import java.util.ArrayList;

public class prevu extends Activity implements View.OnClickListener{
    private ImageView recherche;
    private ImageView scan;
    private ImageView visualisation;
    private ImageView about, compte;
    private TextView formatTxt, contentTxt;
    private GestureLibrary gestureLib;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_prevu);
        recherche = (ImageView) findViewById(R.id.recherche);
        compte = (ImageView) findViewById(R.id.recherche);
        scan = (ImageView) findViewById(R.id.scan);
        visualisation = (ImageView) findViewById(R.id.viz);
        about = (ImageView) findViewById(R.id.about);
        compte = (ImageView) findViewById(R.id.compte);
        recherche.setOnClickListener(this);
        scan.setOnClickListener(this);
       // about.setOnClickListener(this);
        visualisation.setOnClickListener(this);
        compte.setOnClickListener(this);

        // formatTxt = (TextView)findViewById(R.id.scan_format);
        // contentTxt = (TextView)findViewById(R.id.scan_content);


    }

    public void onClick(View v) {

        if (v.getId() == R.id.recherche) {


            Intent i = new Intent(this, Recherche.class);
            startActivity(i);

        } else if (v.getId() == R.id.scan) {
            IntentIntegrator scanIntegrator = new IntentIntegrator(this);
            scanIntegrator.initiateScan();


            //Intent i = new Intent(this, ScannerBarCode.class);
            //startActivity(i);
        } else if (v.getId() == R.id.viz) {
            Intent i = new Intent(this, Visualisation.class);
            startActivity(i);
        }

         else if (v.getId() == R.id.compte) {
            Intent i = new Intent(this, LoginActivity1.class);
            startActivity(i);
        }
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
            default:
                return super.onOptionsItemSelected(item);
        }
    }


}