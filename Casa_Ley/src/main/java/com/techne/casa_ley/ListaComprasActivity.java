package com.techne.casa_ley;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.app.Activity;
import android.view.ContextMenu;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.List;

public class ListaComprasActivity extends Activity {

    private DatabaseHelper db;
    private Context ctx;
    ListView listViewMenuInicio;
    private List<Producto> lista;

    static final int AGREGAR_COMPRA = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_listacompras);
        ctx = this;
        listViewMenuInicio = (ListView) findViewById( R.id.list_listacompras);

        actualizar();
        registerForContextMenu(listViewMenuInicio);
    }

    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
        if (v.getId()==R.id.list_listacompras) {
            AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo)menuInfo;

            menu.setHeaderTitle(lista.get(info.position).getNombre());
            String[] menuItems = new String[2];
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
            db.deleteProducto(lista.get(info.position).get_id());
            lista.remove(info.position);
            Util.mensaje(this, "Compra Eliminada");
        }
        db.closeDB();
        actualizar();
        return true;
    }

    private void actualizar()
    {
        db = new DatabaseHelper(getApplicationContext());
        try{
            lista = db.getAllProductos(1);

            List<Menu_Inicio> menulista = new ArrayList<Menu_Inicio>();
            for(Producto r : lista)
                menulista.add(new Menu_Inicio(r.getNombre() + " " + r.getDescripcion()));

            listViewMenuInicio.setAdapter( new MenuInicioListAdapter(ctx, R.layout.list_row, menulista ) );
            db.closeDB();
        }catch(Exception e){
            try{
                db.closeDB();
            }catch(Exception ee){}
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == AGREGAR_COMPRA) {
            if (resultCode == RESULT_OK) {
                actualizar();
            }
        }
    }


    public void btnAddCompra(View v)
    {
        Intent i = new Intent(ListaComprasActivity.this, AddCompraActivity.class);
        startActivityForResult(i, AGREGAR_COMPRA);
    }

    public void btnBackmain(View v)
    {
        this.onBackPressed();
    }
    
}
