package com.techne.casa_ley;


import android.app.ProgressDialog;
import android.content.Context;
import android.graphics.Color;
import android.os.AsyncTask;
import android.util.Log;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.*;


public class Routing extends AsyncTask<LatLng,Void,Route>
{


    private ProgressDialog spinner;
    private Context context;
    private GoogleMap map;
    private int color;
    private Boolean check=false;
    private Boolean pushPins=false;
    private LatLng startPoint;
    private LatLng destinationPoint;
    private Marker start;
    private Marker destination;


    /**
     * Initializes the context needed for the progress dialog, the map, and the color of the route.
     * @param context
     * @param map
     * @param color
     */
    public Routing(Context context,GoogleMap map,int color)
    {

        this.context = context;
        this.map = map;
        this.color = color;
    }


    /**
     * Initializes the map and the color of the route.
     * @param map
     * @param color
     */
    public Routing(GoogleMap map,int color)
    {
        this.map = map;
        this.color = color;
        check=true;
    }

    /**
     * Performs the call to the google maps API to acquie routing data and deserializes it to
     * a format the map can display.
     * @param points
     * @return
     */
    @Override
    protected Route doInBackground(LatLng... points) {
        LatLng start = points[0];
        LatLng dest = points[1];
        Parser parser;
        String jsonURL = "http://maps.googleapis.com/maps/api/directions/json?";
        final StringBuffer sBuf = new StringBuffer(jsonURL);
        sBuf.append("origin=");
        sBuf.append(start.latitude);
        sBuf.append(',');
        sBuf.append(start.longitude);
        sBuf.append("&destination=");
        sBuf.append(dest.latitude);
        sBuf.append(',');
        sBuf.append(dest.longitude);
        sBuf.append("&sensor=true&mode=walking");
        parser = new GoogleParser(sBuf.toString());
        Route r =  parser.parse();
        return r;
    }
    @Override
    protected synchronized void onPreExecute()
    {
        if(!check)
            spinner = ProgressDialog.show(context,"","Cargando ...", true,false);
    }//end onPreExecute method

    protected void onPostExecute(Route result)
    {
        if(!check)
            spinner.dismiss();

        if(result==null)
        {
            Log.e("Routing","No result was returned.");
        }
        else
        {

            PolylineOptions options = new PolylineOptions().color(color).width(5);

            for (LatLng point : result.getPoints()) {
                options.add(point);
            }
            startPoint = result.getPoints().get(2);
            destinationPoint = result.getPoints().get(result.getPoints().size()-3);

            map.addPolyline(options);

        }
    }//end onPostExecute method




}