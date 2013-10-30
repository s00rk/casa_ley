package com.techne.casa_ley;

import android.app.ProgressDialog;
import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.app.Activity;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import org.ksoap2.serialization.SoapObject;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.Hashtable;
import java.util.List;

public class RegistroActivity extends Activity {

    private Context ctx;
    private String []spinnerArray_estado;
    private String []spinnerArray_ciudad;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registro);
        ctx = this;

        if(Util.Estados.size() == 0)
        {
            final ProgressDialog pd = ProgressDialog.show(this,
                    "Casa Ley",
                    "Cargando Estados",
                    true, false);

            cargarEstados();
            pd.dismiss();
        }

        spinnerArray_estado = new String[Util.Estados.size()];
        Enumeration e = Util.Estados.keys();
        Estado clave;
        int i = 0;
        while( e.hasMoreElements() ){
            clave = (Estado)e.nextElement();
            spinnerArray_estado[i++] = clave.nombre;
        }
        for(int j=0; j < i; j++)
            for(int k = 0; k < i-1; k++)
                if(spinnerArray_estado[k].compareTo(spinnerArray_estado[k+1]) > 0)
                {
                    String tmp = spinnerArray_estado[k];
                    spinnerArray_estado[k] = spinnerArray_estado[k+1];
                    spinnerArray_estado[k+1] = tmp;
                }
        ArrayAdapter<String> spinnerArrayAdapter = new ArrayAdapter<String>(
                this, android.R.layout.simple_spinner_item, spinnerArray_estado);
        spinnerArrayAdapter.setDropDownViewResource( android.R.layout.simple_spinner_dropdown_item );
        Spinner spinner = (Spinner) findViewById( R.id.spin_estado );
        spinner.setAdapter(spinnerArrayAdapter);

        Spinner  spin_city = (Spinner)find(R.id.spin_ciudad);
        spin_city.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                ((TextView) parent.getChildAt(0)).setTextColor(Color.WHITE);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });


        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                ((TextView) parent.getChildAt(0)).setTextColor(Color.WHITE);
                Enumeration e = Util.Estados.keys();
                Estado clave;
                int i = 0;
                List<String> spinnerArray = new ArrayList<String>();
                while( e.hasMoreElements() ){
                    clave = (Estado)e.nextElement();
                    if(clave.nombre.equals(spinnerArray_estado[position]))
                        for(String ci : Util.Estados.get(clave))
                            spinnerArray.add(ci);
                }
                for(int j=0; j < spinnerArray.size(); j++)
                    for(int k = 0; k < spinnerArray.size()-1; k++)
                        if(spinnerArray.get(k).compareTo(spinnerArray.get(k+1)) > 0)
                        {
                            String tmp = spinnerArray.get(k);
                            spinnerArray.set(k, spinnerArray.get(k+1));
                            spinnerArray.set(k+1, tmp);
                        }

                ArrayAdapter<String> spinnerArrayAdapter = new ArrayAdapter<String>(
                        ctx, android.R.layout.simple_spinner_item, spinnerArray);
                spinnerArrayAdapter.setDropDownViewResource( android.R.layout.simple_spinner_dropdown_item );
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
        if(nombre.getText().toString().length() == 0 || ap_paterno.getText().toString().length() == 0 || ap_materno.getText().toString().length() == 0 ||
                domicilio.getText().toString().length() == 0 || cp.getText().toString().length() == 0 || email.getText().toString().length() == 0 || edad.getText().toString().length() == 0)
        {
            Util.mensaje(this, "No debe haber campos vacios");
            return;
        }

        RadioGroup radioSexGroup = (RadioGroup)find(R.id.radio_sexo);
        int selectedId = radioSexGroup.getCheckedRadioButtonId();
        RadioButton radioSexButton = (RadioButton) findViewById(selectedId);
        String SEXO = radioSexButton.getText().toString();


        Hashtable<String, String> params = new Hashtable<String, String>();
        params.put("nombre", nombre.getText().toString());
        params.put("apPaterno", ap_paterno.getText().toString());
        params.put("apMaterno", ap_materno.getText().toString());
        params.put("cveEstado", getcveEstado(estado.getSelectedItem().toString()));
        params.put("ciudad", ciudad.getSelectedItem().toString());
        params.put("domicilio", domicilio.getText().toString());
        params.put("codigoPostal", cp.getText().toString());
        params.put("email", email.getText().toString());
        params.put("sexo", SEXO);
        params.put("edad", edad.getText().toString());

        SoapObject obj = (SoapObject)Util.sendSOAPwithData("RegistraraUsuario", params);
        if(obj == null)
        {
            Util.mensaje(this, "Error al  enviar registro");
            return;
        }

        if(obj.getPropertyCount() > 0)
            Util.mensaje(this, obj.getProperty(1).toString());
        if(obj.getProperty(2).toString().equals("true"))
        {
            nombre.setText("");
            ap_materno.setText("");
            ap_paterno.setText("");
            domicilio.setText("");
            cp.setText("");
            email.setText("");
            edad.setText("");
        }
    }

    private void cargarEstados()
    {
        SoapObject objeto = Util.obtenerSOAP("GetStates", null);
        if(objeto == null)
            return;

        for(int i = 0; i < objeto.getPropertyCount(); i++)
        {
            SoapObject obj = (SoapObject)objeto.getProperty(i);
            Estado est = new Estado(obj.getProperty(0).toString(), obj.getProperty(1).toString());
            ArrayList<String> c = new ArrayList<String>();

            Hashtable<String, String> params = new Hashtable<String, String>();
            params.put("cve_estado", est.clvestado);
            SoapObject ciudades = Util.sendSOAPwithData("GetCities", params);
            if(ciudades != null)
            {
                for(int j = 0; j < ciudades.getPropertyCount(); j++)
                {
                    SoapObject ciu = (SoapObject)ciudades.getProperty(j);
                    for(int x = 0; x < ciu.getPropertyCount(); x++)
                        c.add(ciu.getProperty(x).toString());
                }
            }

            Util.Estados.put(est, c);
        }
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
