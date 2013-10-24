package com.techne.casa_ley;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Toast;

import com.google.android.gms.maps.model.MarkerOptions;

import java.util.ArrayList;
import java.util.List;


public class ListaTiendaActivity extends Activity {

    List<Tienda> lista;
    Context ctx;
    ListView listViewMenuInicio;
    ArrayList<Menu_Inicio> menulista;
    MenuInicioListAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_listatienda);
        ctx = this;

        EditText search = (EditText)findViewById(R.id.txt_buscarley);

        search.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                adapter.getFilter().filter(s.toString());
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        lista = Util.tiendas;
        menulista = new ArrayList<Menu_Inicio>();
        for(Tienda r : lista)
            menulista.add(new Menu_Inicio(r.getImagen(), r.getNombre(), r.getDomicilio()));

        listViewMenuInicio = (ListView) findViewById( R.id.list_listatiendas);
        adapter  = new MenuInicioListAdapter(ctx, R.layout.list_row2, menulista );
        listViewMenuInicio.setAdapter( adapter );
        listViewMenuInicio.setBackgroundColor(Color.WHITE);

        listViewMenuInicio.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent i = new Intent(ListaTiendaActivity.this, TiendaInfoActivity.class);
                i.putExtra("nombre", ((Menu_Inicio)listViewMenuInicio.getItemAtPosition(position)).getName());
                startActivity(i);
            }
        });
    }

    public void btnReturnTienda(View v)
    {
        this.onBackPressed();
    }
}
