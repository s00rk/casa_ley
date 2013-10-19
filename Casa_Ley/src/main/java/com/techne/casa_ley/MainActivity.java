package com.techne.casa_ley;

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
                Intent in = null;
                switch (position)
                {
                    case 2:
                        in = new Intent(MainActivity.this, OfertasActivity.class);
                        in.putExtra("imagenes", imagenes_ofertas);
                        break;
                    case 3:
                        in = new Intent(MainActivity.this, RegistroActivity.class);
                        break;
                }
                if(in != null)
                    startActivity(in);
            }
        });

        new Thread(new Runnable() {
            @Override
            public void run() {
                cargarOfertas();
            }
        }).start();
    }

    private void cargarOfertas()
    {
        SoapObject objeto = Util.obtenerSOAP("ObtenerOfertasEspeciales");
        if(objeto == null)
            return;
        imagenes_ofertas = new String[objeto.getPropertyCount()];
        for(int i=0; i < objeto.getPropertyCount(); i++)
            imagenes_ofertas[i] = objeto.getProperty(i).toString();
    }
    
}
