package com.techne.casa_ley;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.app.Activity;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import org.ksoap2.serialization.SoapObject;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Hashtable;
import java.util.List;

public class MainActivity extends Activity {

    private ListView listViewMenuInicio;
    private Context ctx;
    private String[] imagenes_ofertas;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        overridePendingTransition(R.anim.right_in, R.anim.left_out);
        setContentView(R.layout.activity_main);

        ctx=this;
        Calendar cal = Calendar.getInstance();

        Intent intent = new Intent(this, Servicio.class);
        PendingIntent pintent = PendingIntent.getService(this, 0, intent, 0);

        AlarmManager alarm = (AlarmManager)getSystemService(Context.ALARM_SERVICE);
        alarm.setRepeating(AlarmManager.RTC_WAKEUP, cal.getTimeInMillis(), 30*1000, pintent);

        List<Menu_Inicio> menulista = new ArrayList<Menu_Inicio>();
        menulista.add(new Menu_Inicio(getString(R.string.localizanos), R.drawable.img_locate));
        menulista.add(new Menu_Inicio(getString(R.string.lista), R.drawable.img_notas));
        menulista.add(new Menu_Inicio(getString(R.string.ofertas), R.drawable.img_ofertas));
        menulista.add(new Menu_Inicio(getString(R.string.registrate), R.drawable.img_contacto));
        menulista.add(new Menu_Inicio(getString(R.string.recordatorios), R.drawable.img_recordatorio));
        menulista.add(new Menu_Inicio(getString(R.string.configuracion), R.drawable.img_settings));

        listViewMenuInicio = ( ListView ) findViewById( R.id.list_menu);
        listViewMenuInicio.setAdapter( new MenuInicioListAdapter(ctx, R.layout.list_row, menulista ) );

        listViewMenuInicio.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                overridePendingTransition(R.anim.right_in, R.anim.left_out);
                Intent in;
                switch (position)
                {
                    case 1:
                        in = new Intent(MainActivity.this, ListaComprasActivity.class);
                        break;
                    case 2:
                        in = new Intent(MainActivity.this, OfertasActivity.class);
                        in.putExtra("imagenes", imagenes_ofertas);
                        break;
                    case 3:
                        in = new Intent(MainActivity.this, RegistroActivity.class);
                        break;
                    case 4:
                        in = new Intent(MainActivity.this, RecordatorioActivity.class);
                        break;
                    default:
                        return;
                }
                startActivity(in);
            }
        });

        new Thread(new Runnable() {
            @Override
            public void run() {
                cargarEstados();
                cargarOfertas();
            }
        }).start();
        new Thread(new Runnable() {
            @Override
            public void run() {
                cargarRecordatorios();
                cargarProductos();
            }
        }).start();
    }

    private void cargarRecordatorios(){
        DatabaseHelper db = new DatabaseHelper(getApplicationContext());
        Util.lista_recordatorio = db.getAllRecordatorios();
        db.closeDB();
    }

    private void cargarProductos()
    {
        SoapObject objeto = Util.obtenerSOAP("ObtenerListadoArticulos", null);
        if(objeto == null)
            return;
        DatabaseHelper db = new DatabaseHelper(getApplicationContext());

        List<Producto> prods = db.getAllProductos(0);
        prods.addAll(db.getAllProductos(1));

        for(int i = 0; i < objeto.getPropertyCount(); i++)
        {
            SoapObject obj = (SoapObject)objeto.getProperty(i);
            boolean existe = false;
            for(int j = 0; j < prods.size() && !existe; j++)
                if(prods.get(j).getNombre().equals(obj.getProperty(1).toString()) &&
                        prods.get(j).getDescripcion().equals(obj.getProperty(0).toString()))
                    existe = true;

            if(!existe)
            {
                Producto p = new Producto(obj.getProperty(1).toString(), obj.getProperty(0).toString());
                db.createProducto(p);
            }
        }
        db.closeDB();
    }

    private void cargarOfertas()
    {
        SoapObject objeto = Util.obtenerSOAP("ObtenerOfertasEspeciales", null);
        if(objeto == null)
            return;
        imagenes_ofertas = new String[objeto.getPropertyCount()];
        Util.ofertas = new Bitmap[objeto.getPropertyCount()];
        for(int i=0; i < objeto.getPropertyCount(); i++)
        {
            Util.ofertas[i] = Util.getImageBmp(objeto.getProperty(i).toString());
            imagenes_ofertas[i] = objeto.getProperty(i).toString();
        }
    }

    private void cargarEstados()
    {
        SoapObject objeto = Util.obtenerSOAP("GetStates", null);
        if(objeto == null)
            return;
        Estado est;
        ArrayList<String> c = new ArrayList<String>();
        for(int i = 0; i < objeto.getPropertyCount(); i++)
        {
            SoapObject obj = (SoapObject)objeto.getProperty(i);
            est = new Estado(obj.getProperty(0).toString(), obj.getProperty(1).toString());

            Hashtable<String, String> params = new Hashtable<String, String>();
            params.put("cve_estado", obj.getProperty(1).toString());
            SoapObject ciudades = Util.obtenerSOAP("GetCities", null);
            if(ciudades != null)
            {
                Log.e("casa", ciudades.getPropertyCount()+"");
                for(int j = 0; j < ciudades.getPropertyCount(); j++)
                {
                    SoapObject ciu = (SoapObject)ciudades.getProperty(j);
                    Log.e("casa", ciu.getPropertyCount()+"");
                    c.add(ciu.getProperty(0).toString());
                }
            }

            Util.Estados.put(est, c);
        }
    }
    
}
