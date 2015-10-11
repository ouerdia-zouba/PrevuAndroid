package com.android.prevu.rechercheLivre;

/**
 * Created by Ouerdia on 13/08/2015.
 */

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.TextView;

import com.android.prevu.prevu.R;

import java.util.ArrayList;
import java.util.List;

public class BookAdapter extends ArrayAdapter<Book> implements Filterable {


        private List<Book> arrList;
        private Context context;



        public BookAdapter(Context ctx, Activity activity,
                           ArrayList<Book> arrList) {
            super(ctx, android.R.layout.simple_dropdown_item_1line, arrList);
            this.arrList = arrList;
            this.context = ctx;
        }
        public View getView(int position, View convertView, ViewGroup parent) {



            // First let's verify the convertView is not null
            if (convertView == null) {
                // This a new view we inflate the new layout
                LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                convertView = inflater.inflate(R.layout.book, parent, false);
            }
            // Now we can fill the layout with the right values
            TextView tv = (TextView) convertView.findViewById(R.id.item1);
            if(arrList.size()>0 && position < arrList.size()) {
                Book book = arrList.get(position);

                tv.setText(book.getTitle() + "," + book.getAuthor_prenom() + "," + book.getAuthor_nom() + "," +
                        book.getPublicationyear() + "," + book.getDatenaissance() + "," + book.getDatedeces());

            }
            return convertView;

        }
    }




