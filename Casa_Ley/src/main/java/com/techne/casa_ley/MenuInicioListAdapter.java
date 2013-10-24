package com.techne.casa_ley;

import java.util.ArrayList;
import java.util.List;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

public class MenuInicioListAdapter extends ArrayAdapter<Menu_Inicio> {
    private int                             resource;
    private LayoutInflater  inflater;
    private Context                 context;
    private ArrayList<Menu_Inicio> original;
    private ArrayList<Menu_Inicio> fitems;
    private Filter filter;

    public MenuInicioListAdapter ( Context ctx, int resourceId, ArrayList<Menu_Inicio> objects) {
        super( ctx, resourceId, objects );
        original = new ArrayList<Menu_Inicio>(objects);
        fitems = new ArrayList<Menu_Inicio>(objects);
        resource = resourceId;
        inflater = LayoutInflater.from( ctx );
        context=ctx;
    }
    @Override
    public View getView ( int position, View convertView, ViewGroup parent ) {
        convertView = ( RelativeLayout ) inflater.inflate( resource, null );
        Menu_Inicio Legend = getItem( position );

        TextView legendName = (TextView) convertView.findViewById(R.id.legendName);
        ImageView legendImage = (ImageView) convertView.findViewById(R.id.legendImage);
        TextView legendName2 = null;

        if(resource == R.layout.list_row2)
        {
            legendName = (TextView) convertView.findViewById(R.id.legendName2);
            legendImage = (ImageView) convertView.findViewById(R.id.legendImage2);
            legendName2 = (TextView) convertView.findViewById(R.id.subname2);
        }

        legendName.setText(Legend.getName());

        if(Legend.getImage() != -1)
        {
            Drawable image = context.getResources().getDrawable(Legend.getImage());
            legendImage.setImageDrawable(image);
        }
        if(Legend.getFecha().length() != 0)
        {
            legendName2.setText(Legend.getFecha());
        }else  if(Legend.getDesc().length() != 0)
        {
            legendName.setTextColor(Color.BLACK);
            legendName2.setTextColor(Color.BLACK);
            legendImage.getLayoutParams().height = 80;
            legendImage.getLayoutParams().width = 80;
            legendName2.setText( Legend.getDesc() );
        }

        return convertView;
    }

    @Override
    public Filter getFilter()
    {
        if (filter == null)
            filter = new MenuNameFilter();

        return filter;
    }

    private class MenuNameFilter extends Filter
    {
        @Override
        protected FilterResults performFiltering(CharSequence constraint)
        {
            FilterResults results = new FilterResults();
            String prefix = constraint.toString().toLowerCase();

            if (prefix == null || prefix.length() == 0)
            {
                ArrayList<Menu_Inicio> list = new ArrayList<Menu_Inicio>(original);
                results.values = list;
                results.count = list.size();
            }
            else
            {
                final ArrayList<Menu_Inicio> list = new ArrayList<Menu_Inicio>(original);
                final ArrayList<Menu_Inicio> nlist = new ArrayList<Menu_Inicio>();
                int count = list.size();

                for (int i=0; i<count; i++)
                {
                    final Menu_Inicio pkmn = list.get(i);
                    final String value = pkmn.getName().toLowerCase();
                    final String value2 = pkmn.getDesc().toLowerCase();

                    if (value.contains(prefix))
                    {
                        nlist.add(pkmn);
                    }else if(value2.contains(prefix))
                        nlist.add(pkmn);
                }
                results.values = nlist;
                results.count = nlist.size();
            }
            return results;
        }

        @SuppressWarnings("unchecked")
        @Override
        protected void publishResults(CharSequence constraint, FilterResults results) {
            fitems = (ArrayList<Menu_Inicio>)results.values;

            clear();
            int count = fitems.size();
            for (int i=0; i<count; i++)
            {
                Menu_Inicio pkmn = (Menu_Inicio)fitems.get(i);
                add(pkmn);
            }
        }

    }

}
