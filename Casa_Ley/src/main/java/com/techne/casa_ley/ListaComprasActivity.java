package com.techne.casa_ley;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.app.Activity;
import android.util.Log;
import android.view.ContextMenu;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

public class ListaComprasActivity extends Activity {

    private DatabaseHelper db;
    private Context ctx;
    ListView listViewMenuInicio;
    private ArrayList<Compra> lista;

    static final int AGREGAR_COMPRA = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_listacompras);
        ctx = this;
        listViewMenuInicio = (ListView) findViewById( R.id.list_listacompras);
        Button trash = (Button)findViewById(R.id.btn_listacompradelete);
        Button recordatorio = (Button)findViewById(R.id.btn_listacomprarecordatorio);
        Typeface font = Typeface.createFromAsset(getAssets(), "pulsarjs.ttf");
        trash.setTypeface(font);
        recordatorio.setTypeface(font);

        actualizar();
        registerForContextMenu(listViewMenuInicio);
    }

    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
        if (v.getId()==R.id.list_listacompras) {
            AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo)menuInfo;

            menu.setHeaderTitle(lista.get(info.position).getNombre());
            String[] menuItems = new String[1];
            menuItems[0] = "Eliminar";
            for (int i = 0; i<menuItems.length; i++) {
                menu.add(Menu.NONE, i, i, menuItems[i]);
            }
        }
    }

    @Override
    public boolean onContextItemSelected(MenuItem item) {
        AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo)item.getMenuInfo();
        db = new DatabaseHelper(getApplicationContext());
            db.deleteCompra(lista.get(info.position).get_id());
            lista.remove(info.position);
            Util.mensaje(this, "Compra Eliminada");
        db.closeDB();
        actualizar();
        return true;
    }

    private void actualizar()
    {
        db = new DatabaseHelper(getApplicationContext());
        try{
            lista = db.getAllCompras();
            ArrayList<Menu_Inicio> menulista = new ArrayList<Menu_Inicio>();
            for(Compra r : lista)
            {
                Log.e("casa", r.get_id()+" "+r.getNombre());
                menulista.add(new Menu_Inicio(r.getNombre()));
            }

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


    public void BotonListaCompras(View v)
    {
        Intent i;
        switch(v.getId())
        {
            case R.id.btn_listacompraagregar:
                i = new Intent(ListaComprasActivity.this, AddCompraActivity.class);
                startActivityForResult(i, AGREGAR_COMPRA);
                break;
            case R.id.btn_listacomprarecordatorio:
                i = new Intent(ListaComprasActivity.this, RecordatorioActivity.class);
                startActivity(i);
                break;
            case R.id.btn_listacompradelete:
                AlertDialog.Builder builder = new AlertDialog.Builder(this);
                builder.setMessage(getString(R.string.pregunta_eliminar))
                        .setPositiveButton("Si", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                DatabaseHelper db = new DatabaseHelper(ListaComprasActivity.this);
                                db.deleteCompraAll();
                                db.closeDB();
                                actualizar();
                                Toast.makeText(ListaComprasActivity.this, "Lista Eliminada", Toast.LENGTH_SHORT).show();
                            }
                        })
                        .setNegativeButton("No", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                            }
                        });
                builder.create();
                builder.show();
                break;
            default:
                Log.e("casa_ley", "WTF!! XD " + v.getId() + " " + R.id.btn_listacompraagregar);
        }
    }
    
}
