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
            lista = db.getAllProductos(0);
            String []articulos = new String[lista.size()];
            int i = 0;
            for(Producto r : lista)
                articulos[i++] = r.getNombre() + " " + r.getDescripcion();
            db.closeDB();

            ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_dropdown_item_1line, articulos);
            articulo.setThreshold(3);
            articulo.setAdapter(adapter);
        }catch(Exception e){
            try{
                db.closeDB();
            }catch(Exception ee){}
        }
    }

    public void AgregarCompra(View v)
    {
        String art;
        Producto resp = null;
        for(Producto r : lista)
        {
            art = r.getNombre() + " " + r.getDescripcion();
            if(art.equals(articulo.getText().toString()))
            {
                resp = r;
                resp.setComprar(1);
            }
        }

        if(resp != null)
        {
            db = new DatabaseHelper(getApplicationContext());
            db.updateProducto(resp);
            db.closeDB();
            Util.mensaje(this, "Articulo Agregado a la lista! " + resp.getNombre() + " " + resp.get_id());
            Intent intent = getIntent();
            setResult(RESULT_OK, intent);
            finish();
        }else
            Util.mensaje(this, "El Articulo No Existe en la Lista");

    }


    public void btnRegresar(View v)
    {
        this.onBackPressed();
    }
    
}
