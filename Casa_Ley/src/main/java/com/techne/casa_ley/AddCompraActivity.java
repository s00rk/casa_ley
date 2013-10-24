package com.techne.casa_ley;

import android.content.Intent;
import android.os.Bundle;
import android.app.Activity;
import android.view.Menu;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.List;

public class AddCompraActivity extends Activity {

    DatabaseHelper db;
    List<Producto> lista;
    AutoCompleteTextView articulo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_addcompra);

        articulo = (AutoCompleteTextView)findViewById(R.id.compra_autocompletar);
        db = new DatabaseHelper(getApplicationContext());

        try{
            lista = db.getAllProductos();
            db.closeDB();

            String []articulos = new String[lista.size()];
            int i = 0;
            for(Producto r : lista)
                articulos[i++] = r.getNombre() + " " + r.getDescripcion();

            ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_dropdown_item_1line, articulos);
            articulo.setThreshold(3);
            articulo.setAdapter(adapter);
        }catch(Exception e){
        }
    }

    public void AgregarCompra(View v)
    {
        String art = articulo.getText().toString();
        if(art.length() > 0)
        {
            db = new DatabaseHelper(getApplicationContext());
            db.createCompra(art);
            db.closeDB();
            Util.mensaje(this, "Articulo Agregado a la lista! ");
            Intent intent = getIntent();
            setResult(RESULT_OK, intent);
            finish();
        }else
            Util.mensaje(this, "No deje espacio en blanco");
    }


    public void btnRegresar(View v)
    {
        this.onBackPressed();
    }
    
}
