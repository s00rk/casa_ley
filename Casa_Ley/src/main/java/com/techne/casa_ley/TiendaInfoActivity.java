package com.techne.casa_ley;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import java.text.DecimalFormat;

/**
 * Created by Victor on 23/10/13.
 */
public class TiendaInfoActivity extends Activity {


    private GoogleMap googleMap;
    LatLng MiLocacion = null;
    LatLng Destino = null;
    Routing ruta = null;
    Tienda t = null;
    TextView distanciai;
    LocationManager locationManager;
    android.location.LocationListener mlocationupdate;
    GPSTracker gps;
    Context ctx;
    boolean isOn = true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tiendainfo);
        ctx = this;

        Bundle b = getIntent().getExtras();

        String nombre = b.getString("nombre");
        String distancia = "desconocida";
        if(b.containsKey("distancia"))
        {
            distancia = b.getString("distancia");
            DecimalFormat df = new DecimalFormat("###.##");
            double d = Double.parseDouble(distancia);
            if(d > 1000)
            {
                d /= 1000;
                distancia = df.format(d) + " km";
            }else
                distancia += " metros";
        }
        for(Tienda tt : Util.tiendas)
            if(tt.getNombre().equals(nombre))
                t = tt;

        gps = new GPSTracker(TiendaInfoActivity.this);
        if(!gps.canGetLocation()){
            gps.showSettingsAlert();
        }



        TextView titulo = (TextView)findViewById(R.id.txt_tituloinfo);
        TextView nombrei = (TextView)findViewById(R.id.txt_nombreinfo);
        ImageView img = (ImageView)findViewById(R.id.img_tiendainfo);
        distanciai = (TextView)findViewById(R.id.txt_distaciainfo);
        TextView direccion = (TextView)findViewById(R.id.txt_direccioninfo);

        titulo.setText(t.getNombre());
        nombrei.setText(t.getNombre());
        direccion.setText(t.getDomicilio());
        distanciai.setText(distancia);
        img.setImageBitmap(BitmapFactory.decodeResource(getResources(), t.getImagen()));
        img.setScaleType(ImageView.ScaleType.FIT_XY);

        iniciar();

    }


    private void iniciar()
    {
        try {
            initilizeMap();
            if(googleMap != null)
            {
                googleMap.getUiSettings().setMyLocationButtonEnabled(true);
                googleMap.setMyLocationEnabled(true);

                Log.e("casa_ley", t.getLatitud());
                add_Marker(Double.parseDouble(t.getLatitud()), Double.parseDouble(t.getLongitud()), t.getNombre(), t.getImagen());


                Criteria criteria = new Criteria();

                locationManager = (LocationManager) getSystemService(LOCATION_SERVICE);
                String provider = locationManager.getBestProvider(criteria, true);
                Location location = locationManager.getLastKnownLocation(provider);

                if(location!=null){
                    new Thread(new Runnable() {
                        @Override
                        public void run() {
                            do{
                                if(gps.canGetLocation())
                                {
                                    runOnUiThread(new Runnable() {
                                        @Override
                                        public void run() {
                                            double latitude = gps.getLatitude();
                                            double longitude = gps.getLongitude();
                                            MiLocacion = new LatLng(latitude, longitude);
                                            Destino = new LatLng(Double.parseDouble(t.getLatitud()), Double.parseDouble(t.getLongitud()));

                                            float []result = new float[1];
                                            Location.distanceBetween(latitude, longitude, Double.parseDouble(t.getLatitud()), Double.parseDouble(t.getLongitud()), result);


                                            String distancia = "";
                                            DecimalFormat df = new DecimalFormat("###.##");
                                            double d = result[0];
                                            if(d > 1000)
                                            {
                                                d /= 1000;
                                                distancia = df.format(d) + " km";
                                            }else
                                                distancia += " metros";
                                            distanciai.setText(distancia);

                                            googleMap.clear();

                                            new Routing(TiendaInfoActivity.this,googleMap, Color.RED).execute(MiLocacion, Destino);
                                            add_Marker(Double.parseDouble(t.getLatitud()), Double.parseDouble(t.getLongitud()), t.getNombre(), t.getImagen());

                                            googleMap.moveCamera(CameraUpdateFactory.newLatLng(MiLocacion));
                                            googleMap.animateCamera(CameraUpdateFactory.zoomTo(14));

                                        }
                                    });
                                }
                            }while(isOn);
                        }
                    }).start();
                }

            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    private void add_Marker(double latitude, double longitud, String nombre, int img) {

        View marker = ((LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE)).inflate(R.layout.custom_layout_marker, null);
        ImageView numTxt = (ImageView) marker.findViewById(R.id.img_ley);
        numTxt.setImageBitmap(BitmapFactory.decodeResource(getResources(),
                img));

        MarkerOptions customMarker = new MarkerOptions().position(new LatLng(latitude, longitud)).title(nombre);
        customMarker.icon(BitmapDescriptorFactory.fromBitmap(createDrawableFromView(this, marker)));
        Log.e("casa_ley", "");
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
                    R.id.mapa_info)).getMap();

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

    public void btnRegresarInfoTienda(View v)
    {
        isOn = false;
        this.onBackPressed();
    }

    @Override
    protected void onDestroy() {
        gps.stopUsingGPS();
        isOn = false;
        super.onDestroy();
    }
}
