package com.techne.casa_ley;

import android.location.Criteria;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.app.Activity;
import android.view.View;
import android.widget.Toast;

import com.google.android.gms.location.LocationListener;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

public class TiendasActivity extends Activity {

    private GoogleMap googleMap;
    boolean update = false;

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
                    add_Marker(Double.parseDouble(t.getLatitud()), Double.parseDouble(t.getLongitud()), t.getNombre());

                Criteria criteria = new Criteria();

                LocationManager locationManager = (LocationManager) getSystemService(LOCATION_SERVICE);
                String provider = locationManager.getBestProvider(criteria, true);
                Location location = locationManager.getLastKnownLocation(provider);

                if(location!=null){
                    locationManager.requestLocationUpdates(provider, 20000, 0, new android.location.LocationListener() {
                        @Override
                        public void onLocationChanged(Location location) {
                            if(!update)
                                update = true;
                            else
                                return;
                            double latitude = location.getLatitude();
                            double longitude = location.getLongitude();
                            LatLng latLng = new LatLng(latitude, longitude);
                            googleMap.moveCamera(CameraUpdateFactory.newLatLng(latLng));
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



    private void add_Marker(double latitude, double longitude, String nombre)
    {
        MarkerOptions marker = new MarkerOptions().position(new LatLng(latitude, longitude)).title(nombre);

        marker.icon(BitmapDescriptorFactory.fromResource(R.drawable.img_pin));
        googleMap.addMarker(marker);
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

    public void btnRegresarP(View v)
    {
        this.onBackPressed();
    }
    
}
