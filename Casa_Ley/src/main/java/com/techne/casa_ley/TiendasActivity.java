package com.techne.casa_ley;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.app.Activity;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

public class TiendasActivity extends Activity {

    private GoogleMap googleMap;
    LatLng MiLocacion = null;
    LatLng Destino = null;
    boolean update = false;
    Routing ruta = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tiendas);

        try {
            initilizeMap();
            if(googleMap != null)
            {
                googleMap.getUiSettings().setMyLocationButtonEnabled(true);
                googleMap.setMyLocationEnabled(true);

                for(Tienda t : Util.tiendas)
                    add_Marker(Double.parseDouble(t.getLatitud()), Double.parseDouble(t.getLongitud()), t.getNombre(), t.getImagen());

                googleMap.setOnMarkerClickListener(new GoogleMap.OnMarkerClickListener() {
                    @Override
                    public boolean onMarkerClick(Marker marker) {
                        Destino = marker.getPosition();
                        if(Destino != null && MiLocacion != null)
                        {
                            googleMap.clear();
                            new Routing(TiendasActivity.this,googleMap, Color.RED).execute(MiLocacion, Destino);
                            for(Tienda t : Util.tiendas)
                                add_Marker(Double.parseDouble(t.getLatitud()), Double.parseDouble(t.getLongitud()), t.getNombre(), t.getImagen());
                        }
                        return false;
                    }
                });

                Criteria criteria = new Criteria();

                LocationManager locationManager = (LocationManager) getSystemService(LOCATION_SERVICE);
                String provider = locationManager.getBestProvider(criteria, true);
                Location location = locationManager.getLastKnownLocation(provider);

                if(location!=null){
                    locationManager.requestLocationUpdates(provider, 20000, 0, new android.location.LocationListener() {
                        @Override
                        public void onLocationChanged(Location location) {
                            double latitude = location.getLatitude();
                            double longitude = location.getLongitude();
                            MiLocacion = new LatLng(latitude, longitude);

                            if(!update)
                                update = true;
                            else
                                return;

                            googleMap.moveCamera(CameraUpdateFactory.newLatLng(MiLocacion));
                            googleMap.animateCamera(CameraUpdateFactory.zoomTo(14));
                        }

                        @Override
                        public void onStatusChanged(String provider, int status, Bundle extras) {

                        }

                        @Override
                        public void onProviderEnabled(String provider) {

                        }

                        @Override
                        public void onProviderDisabled(String provider) {

                        }
                    });
                }

            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }



    private void add_Marker(double latitude, double longitude, String nombre, int id_img)
    {
        setUpMap(latitude, longitude, nombre, id_img);
        //MarkerOptions marker = new MarkerOptions().position(new LatLng(latitude, longitude)).title(nombre);

        //marker.icon(BitmapDescriptorFactory.fromResource(id_img));
        //googleMap.addMarker(marker);
    }

    private void setUpMap(double latitude, double longitud, String nombre, int img) {

        View marker = ((LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE)).inflate(R.layout.custom_layout_marker, null);
        ImageView numTxt = (ImageView) marker.findViewById(R.id.img_ley);
        numTxt.setImageBitmap(BitmapFactory.decodeResource(getResources(),
                img));

        MarkerOptions customMarker = new MarkerOptions().position(new LatLng(latitude, longitud)).title(nombre);
        customMarker.icon(BitmapDescriptorFactory.fromBitmap(createDrawableFromView(this, marker)));
        googleMap.addMarker(customMarker);
    }

    public Bitmap createDrawableFromView(Context context, View view) {
        DisplayMetrics displayMetrics = new DisplayMetrics();
        ((Activity) context).getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        view.setLayoutParams(new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT));
        view.measure(displayMetrics.widthPixels, displayMetrics.heightPixels);
        view.layout(0, 0, displayMetrics.widthPixels, displayMetrics.heightPixels);
        view.buildDrawingCache();
        Bitmap bitmap = Bitmap.createBitmap(view.getMeasuredWidth(), view.getMeasuredHeight(), Bitmap.Config.ARGB_8888);

        Canvas canvas = new Canvas(bitmap);
        view.draw(canvas);

        return bitmap;
    }

    private void initilizeMap() {
        if (googleMap == null) {
            googleMap = ((MapFragment) getFragmentManager().findFragmentById(
                    R.id.mapa)).getMap();

            if (googleMap == null) {
                Toast.makeText(getApplicationContext(),
                        "Sorry! unable to create maps", Toast.LENGTH_SHORT)
                        .show();
            }
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        initilizeMap();
    }

    public void btnTiendas(View v)
    {
        Intent in = new Intent(TiendasActivity.this, ListaTiendaActivity.class);
        startActivity(in);
    }

    public void btnRegresarP(View v)
    {
        this.onBackPressed();
    }
    
}