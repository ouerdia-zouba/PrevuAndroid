package com.android.prevu.visualisation;

import android.app.ActionBar;
import android.app.Activity;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.android.prevu.prevu.R;


public class Visualisation extends Activity implements AdapterView.OnItemClickListener {
    ListView listVisualisation;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.visualisation);
        listVisualisation = (ListView) findViewById(R.id.listViewVisualisation);
        listVisualisation.setOnItemClickListener(this);
        String[] elements = getResources().getStringArray(R.array.visualisations);
        ArrayAdapter<String> adaptateur = new ArrayAdapter<String>(this,
                android.R.layout.simple_dropdown_item_1line);
        for (int i = 0; i < elements.length; i++) adaptateur.add(elements[i]);
        listVisualisation.setAdapter(adaptateur);
    }


    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        if(position == 4){
            Intent i = new Intent(this, LivresPopulaires.class);
            i.putExtra("position", position);

            startActivity(i);
        }
        else {
            Intent i = new Intent(this, VisualisationInfo.class);
            i.putExtra("position", position);
            startActivity(i);
        }

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_visualisation, menu);
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
