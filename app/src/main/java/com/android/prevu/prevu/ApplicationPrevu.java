package com.android.prevu.prevu;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.GridView;
import android.widget.Toast;

import com.android.prevu.compte.Connection;
import com.android.prevu.rechercheLivre.InformationLivre;
import com.android.prevu.rechercheLivre.Recherche;
import com.android.prevu.scannerBarCode.IntentIntegrator;
import com.android.prevu.scannerBarCode.IntentResult;
import com.android.prevu.visualisation.Visualisation;

import java.util.ArrayList;

public class ApplicationPrevu extends Activity implements OnItemClickListener {
    /** Called when the activity is first created. */

    private GridviewAdapter mAdapter;
    private ArrayList<String> listCountry;
    private ArrayList<Integer> listFlag;

    private GridView gridView;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);

        prepareList();

        // prepared arraylist and passed it to the Adapter class
        mAdapter = new GridviewAdapter(this,listCountry, listFlag);

        // Set custom adapter to gridview
        gridView = (GridView) findViewById(R.id.gridView1);
        gridView.setAdapter(mAdapter);
        gridView.setOnItemClickListener(this);
        // Implement On Item click listener
        /*gridView.setOnItemClickListener(new OnItemClickListener()
        {
            @Override
            public void onItemClick(AdapterView<?> arg0, View arg1, int position,
                                    long arg3) {
                ;
                switch (position) {
                    case 0:
                        Intent i = new Intent(getBaseContext(), Recherche.class);
                        startActivity(i);
                        break;
                    case 1:
                        IntentIntegrator scanIntegrator = new IntentIntegrator(getParent());
                       scanIntegrator.initiateScan();
                        break;
                    case 2:
                        Intent i1 = new Intent(getBaseContext(), Visualisation.class);
                        startActivity(i1);
                        break;
                    case 3:
                        Intent i2 = new Intent(getBaseContext(), Connection.class);
                        startActivity(i2);
                        break;


                }

            }
        });*/

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

    public void prepareList()
    {
        listCountry = new ArrayList<String>();

        listCountry.add("Recherche");
        listCountry.add("Scan");
        listCountry.add("Visualisation");
        listCountry.add("Mon Compte");


        listFlag = new ArrayList<Integer>();
        listFlag.add(R.mipmap.ic_launcher_recherche);
        listFlag.add(R.mipmap.ic_launcher_scanbar);
        listFlag.add(R.mipmap.ic_launcher_visualisation);
        listFlag.add(R.mipmap.ic_launcher_utilisateur);

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

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        switch (position) {
            case 0:
                Intent i = new Intent(this, Recherche.class);
                startActivity(i);
                break;
            case 1:
                IntentIntegrator scanIntegrator = new IntentIntegrator(this);
                scanIntegrator.initiateScan();
                break;
            case 2:
                Intent i1 = new Intent(getBaseContext(), Visualisation.class);
                startActivity(i1);
                break;
            case 3:
                Intent i2 = new Intent(getBaseContext(), Connection.class);
                startActivity(i2);
                break;


        }
    }
}
