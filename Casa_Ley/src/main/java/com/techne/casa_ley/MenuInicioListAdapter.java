package com.techne.casa_ley;

import java.util.List;
import android.content.Context;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

public class MenuInicioListAdapter extends ArrayAdapter<Menu_Inicio> {
    private int                             resource;
    private LayoutInflater  inflater;
    private Context                 context;
    public MenuInicioListAdapter ( Context ctx, int resourceId, List<Menu_Inicio> objects) {
        super( ctx, resourceId, objects );
        resource = resourceId;
        inflater = LayoutInflater.from( ctx );
        context=ctx;
    }
    @Override
    public View getView ( int position, View convertView, ViewGroup parent ) {
        convertView = ( RelativeLayout ) inflater.inflate( resource, null );
        Menu_Inicio Legend = getItem( position );
        TextView legendName = (TextView) convertView.findViewById(R.id.legendName);
        legendName.setText(Legend.getName());

        ImageView legendImage = (ImageView) convertView.findViewById(R.id.legendImage);
        if(Legend.getImage() != -1)
        {
            Drawable image = context.getResources().getDrawable(Legend.getImage());
            legendImage.setImageDrawable(image);
        }

        return convertView;
    }
}
