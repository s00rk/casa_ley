package com.techne.casa_ley;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.app.Activity;
import android.view.ContextMenu;
import android.view.Menu;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.List;

public class RecordatorioActivity extends Activity {

    private DatabaseHelper db;
    private Context ctx;
    private List<Recordatorio> lista;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recordatorio);
        ctx = this;

        db = new DatabaseHelper(getApplicationContext());
        lista = db.getAllRecordatorios();

        List<Menu_Inicio> menulista = new ArrayList<Menu_Inicio>();
        for(Recordatorio r : lista)
            menulista.add(new Menu_Inicio(r.getTitulo()));

        ListView listViewMenuInicio = (ListView) findViewById( R.id.list_recordatorios);
        listViewMenuInicio.setAdapter( new MenuInicioListAdapter(ctx, R.layout.list_row, menulista ) );
        registerForContextMenu(listViewMenuInicio);
    }

    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
        if (v.getId()==R.id.list_recordatorios) {
            AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo)menuInfo;

            menu.setHeaderTitle(lista.get(info.position).getTitulo());
            String[] menuItems = new String[2];
            menuItems[0] = "Editar";
            menuItems[1] = "Eliminar";
            for (int i = 0; i<menuItems.length; i++) {
                menu.add(Menu.NONE, i, i, menuItems[i]);
            }
        }
    }

    public void btnAdd(View v)
    {
        Intent i = new Intent(RecordatorioActivity.this, AddRecordatorioActivity.class);
        startActivity(i);
    }

    public void btnBackM(View v)
    {
        this.onBackPressed();
    }
}
