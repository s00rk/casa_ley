package com.techne.casa_ley;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.app.Activity;
import android.util.Log;
import android.view.ContextMenu;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

public class RecordatorioActivity extends Activity {

    private DatabaseHelper db;
    private Context ctx;
    ListView listViewMenuInicio;
    private List<Recordatorio> lista;

    static final int AGREGAR_RECORDATORIO = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recordatorio);
        ctx = this;

        lista = Util.lista_recordatorio;
        ArrayList<Menu_Inicio> menulista = new ArrayList<Menu_Inicio>();
        for(Recordatorio r : lista)
            menulista.add(new Menu_Inicio(r.getTitulo(), r.getFecha()));

        listViewMenuInicio = (ListView) findViewById( R.id.list_recordatorios);
        listViewMenuInicio.setAdapter( new MenuInicioListAdapter(ctx, R.layout.list_row2, menulista ) );


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

    @Override
    public boolean onContextItemSelected(MenuItem item) {
        AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo)item.getMenuInfo();
        db = new DatabaseHelper(getApplicationContext());
        int menuItemIndex = item.getItemId();
        if(menuItemIndex == 0)
        {
            Intent i = new Intent(RecordatorioActivity.this, AddRecordatorioActivity.class);
            i.putExtra("id", lista.get(info.position).get_id());
            startActivityForResult(i, AGREGAR_RECORDATORIO);
        }else{
            db.deleteRecordatorio(lista.get(info.position).get_id());
            lista.remove(info.position);
            Util.mensaje(this, "Recordatorio Eliminado");
        }
        db.closeDB();
        actualizar();
        return true;
    }

    private void actualizar()
    {
        db = new DatabaseHelper(getApplicationContext());
        lista = db.getAllRecordatorios();
        db.closeDB();
        Util.lista_recordatorio = lista;

        ArrayList<Menu_Inicio> menulista = new ArrayList<Menu_Inicio>();
        for(Recordatorio r : lista)
            menulista.add(new Menu_Inicio(r.getTitulo(), r.getFecha()));

        listViewMenuInicio = (ListView) findViewById( R.id.list_recordatorios);
        listViewMenuInicio.setAdapter( new MenuInicioListAdapter(ctx, R.layout.list_row2, menulista ) );
    }

    public void btnAdd(View v)
    {
        Intent i = new Intent(RecordatorioActivity.this, AddRecordatorioActivity.class);
        startActivityForResult(i, AGREGAR_RECORDATORIO);
    }

    public void btnBackM(View v)
    {
        this.onBackPressed();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == AGREGAR_RECORDATORIO) {
            if (resultCode == RESULT_OK) {
                actualizar();
            }
        }
    }
}
