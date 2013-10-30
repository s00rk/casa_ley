package com.techne.casa_ley;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.graphics.Typeface;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.ToggleButton;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Victor on 23/10/13.
 */
public class ConfigActivity extends Activity {

    Config c;
    Context ctx;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_config);
        ctx = this;

        Typeface font = Typeface.createFromAsset(getAssets(), "pulsarjs.ttf");

        ToggleButton btnLocal = (ToggleButton)findViewById(R.id.btn_noti_local);
        ToggleButton btnPush = (ToggleButton)findViewById(R.id.btn_noti_push);
        TextView privacidad = (TextView)findViewById(R.id.txt_privacidad);
        TextView version = (TextView)findViewById(R.id.txt_version);
        Spinner tiendas = (Spinner)findViewById(R.id.spin_tiendas);

        final String [] items = new String[] {"100", "300", "500", "1000"};

        ArrayAdapter<String> spinnerArrayAdapter = new ArrayAdapter<String>(
                this, android.R.layout.simple_spinner_item, items);
        spinnerArrayAdapter.setDropDownViewResource( android.R.layout.simple_spinner_dropdown_item );
        tiendas.setAdapter(spinnerArrayAdapter);

        privacidad.setTypeface(font);
        DatabaseHelper db = new DatabaseHelper(this);
        c = db.getConfig();
        db.closeDB();

        if(c.getLocal() == 1)
            btnLocal.setChecked(true);
        if(c.getPush() == 1)
            btnPush.setChecked(true);
        int i = 0;
        if(c.getTiendas() == 300)
            i = 1;
        else if(c.getTiendas() == 500)
            i = 2;
        else if(c.getTiendas() == 1000)
            i = 3;
        tiendas.setSelection(i);


        try{
            PackageInfo pInfo = getPackageManager().getPackageInfo(getPackageName(), 0);
            version.setText(pInfo.versionName);
        }catch(Exception E){}

        tiendas.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                c.setTiendas( Integer.parseInt(items[position]) );
                DatabaseHelper db = new DatabaseHelper(ctx);
                db.updateConfig(c);
                db.closeDB();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        btnLocal.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int l = 0;
                if(c.getLocal() == l)
                    l = 1;
                c.setLocal(l);
                DatabaseHelper db = new DatabaseHelper(ctx);
                db.updateConfig(c);
                db.closeDB();
            }
        });
        btnPush.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int l = 0;
                if(c.getPush() == l)
                    l = 1;
                c.setPush(l);
                DatabaseHelper db = new DatabaseHelper(ctx);
                db.updateConfig(c);
                db.closeDB();
            }
        });

    }

    public void btnConfig(View v)
    {
        Intent i = null;
        Log.e("casa_ley", v.getId() + "");
        switch (v.getId())
        {
            case R.id.linear_privacidad:
                i = new Intent(ConfigActivity.this, PrivacidadActivity.class);
                startActivity(i);
                return;
        }
        this.onBackPressed();
    }

    public void btnConfigura(View vv)
    {
        this.onBackPressed();
    }

}
