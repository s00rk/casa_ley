package com.techne.casa_ley;

import android.content.Context;
import android.os.Bundle;
import android.app.Activity;
import android.view.Menu;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Switch;
import android.widget.Toast;

import org.ksoap2.serialization.SoapObject;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.Hashtable;
import java.util.List;

public class RegistroActivity extends Activity {

    private Context ctx;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registro);
        ctx = this;

        String []spinnerArray = new String[Util.Estados.size()];
        Enumeration e = Util.Estados.keys();
        Estado clave;
        int i = 0;
        while( e.hasMoreElements() ){
            clave = (Estado)e.nextElement();
            spinnerArray[i++] = clave.nombre;
        }
        ArrayAdapter<String> spinnerArrayAdapter = new ArrayAdapter<String>(
                this, android.R.layout.simple_spinner_item, spinnerArray);
        spinnerArrayAdapter.setDropDownViewResource( android.R.layout.simple_spinner_dropdown_item );
        Spinner spinner = (Spinner) findViewById( R.id.spin_estado );
        spinner.setAdapter(spinnerArrayAdapter);


        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                Enumeration e = Util.Estados.keys();
                Estado clave;
                int i = 0;
                List<String> spinnerArray = new ArrayList<String>();
                while( e.hasMoreElements() ){
                    clave = (Estado)e.nextElement();
                    if(i++ == position)
                        for(String ci : Util.Estados.get(clave))
                            spinnerArray.add(ci);
                }
                ArrayAdapter<String> spinnerArrayAdapter = new ArrayAdapter<String>(
                        ctx, android.R.layout.simple_spinner_item, spinnerArray);
                spinnerArrayAdapter.setDropDownViewResource( android.R.layout.simple_spinner_item );
                Spinner spinner = (Spinner) findViewById( R.id.spin_ciudad );
                spinner.setAdapter(spinnerArrayAdapter);


            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });


    }

    public void btnBack(View v)
    {
        this.onBackPressed();
    }

    public void btnEnviar(View v)
    {
        EditText nombre = (EditText)find(R.id.txt_nombre), ap_paterno = (EditText)find(R.id.txt_ap_paterno), ap_materno = (EditText)find(R.id.txt_ap_materno),
                domicilio = (EditText)find(R.id.txt_domicilio), cp = (EditText)find(R.id.txt_cp), email = (EditText)find(R.id.txt_email),
                edad = (EditText)find(R.id.txt_edad);
        Spinner estado = (Spinner)find(R.id.spin_estado), ciudad = (Spinner)find(R.id.spin_ciudad);
        Switch sexo = (Switch)find(R.id.swi_sexo);
        if(nombre.getText().toString().length() == 0 || ap_paterno.getText().toString().length() == 0 || ap_materno.getText().toString().length() == 0 ||
                domicilio.getText().toString().length() == 0 || cp.getText().toString().length() == 0 || email.getText().toString().length() == 0 || edad.getText().toString().length() == 0)
        {
            Util.mensaje(this, "No debe haber campos vacios");
            return;
        }


        Hashtable<String, String> params = new Hashtable<String, String>();
        params.put("nombre", nombre.getText().toString());
        params.put("apPaterno", ap_paterno.getText().toString());
        params.put("apMaterno", ap_materno.getText().toString());
        params.put("cveEstado", getcveEstado(estado.getSelectedItem().toString()));
        params.put("ciudad", ciudad.getSelectedItem().toString());
        params.put("domicilio", domicilio.getText().toString());
        params.put("codigoPostal", cp.getText().toString());
        params.put("email", email.getText().toString());
        params.put("sexo", sexo.getText().toString());
        params.put("edad", edad.getText().toString());

        SoapObject obj = (SoapObject)Util.obtenerSOAP("RegistraraUsuario", params);
        if(obj == null)
        {
            Util.mensaje(this, "Error al  enviar registro");
            return;
        }

        for(int i = 0; i < obj.getPropertyCount(); i++)
            Util.mensaje(this, obj.getProperty(i).toString());
    }

    private Object find(int x)
    {
        return findViewById(x);
    }
    private String getcveEstado(String nom)
    {
        Enumeration e = Util.Estados.keys();
        Estado clave;
        while( e.hasMoreElements() ){
            clave = (Estado)e.nextElement();
            if(clave.nombre.equals(nom))
                return clave.clvestado;
        }
        return null;
    }
}
