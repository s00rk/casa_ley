package com.techne.casa_ley;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.Parcelable;
import android.provider.MediaStore;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import java.util.ArrayList;

public class OfertasActivity extends Activity {

    Bitmap [] imagenes = null;
    Context ctx;
    ViewPager viewPager;
    PageImageAdapter adapter;

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuItem item = menu.add ("Guardar Imagen");
        item.setOnMenuItemClickListener (new MenuItem.OnMenuItemClickListener(){
            @Override
            public boolean onMenuItemClick (MenuItem item){
                Bitmap bmp = null;
                bmp = (Bitmap)adapter.imagenes[viewPager.getCurrentItem()];
                MediaStore.Images.Media.insertImage(getContentResolver(), bmp, "Promocion Casa Ley" , "");
                Toast.makeText(ctx, "Imagen Guardada", Toast.LENGTH_LONG).show();
                return true;
            }
        });
        return true;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_ofertas);
        ctx = this;
        viewPager = (ViewPager) findViewById(R.id.view_pager);

        Bundle extras = getIntent().getExtras();
        String [] images = extras.getStringArray("imagenes");
        imagenes = new Bitmap[images.length];
        for(int i = 0; i < images.length; i++)
            imagenes[i] = Util.getImageBmp(images[i]);

        adapter = new PageImageAdapter(ctx, imagenes);
        viewPager.setAdapter(adapter);

    }

    @Override
    public void onBackPressed() {
        //super.onBackPressed();
        finish();
        overridePendingTransition(R.anim.bottom_in, R.anim.top_out);
    }
}
