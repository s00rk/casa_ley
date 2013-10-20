package com.techne.casa_ley;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.Toast;

import org.ksoap2.SoapEnvelope;
import org.ksoap2.serialization.SoapObject;
import org.ksoap2.serialization.SoapSerializationEnvelope;
import org.ksoap2.transport.HttpTransportSE;

import java.io.BufferedInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.List;

public class Util {

    public static Bitmap [] ofertas = null;
    public static Hashtable<Estado, ArrayList<String>> Estados = new Hashtable<Estado, ArrayList<String>>();
    public static List<Recordatorio> lista_recordatorio = new ArrayList<Recordatorio>();

    public static SoapObject obtenerSOAP(String metodo, Hashtable<String, String> params)
    {
        TareaWSConsulta ws = new TareaWSConsulta(params);
        try{
            ws.execute(metodo).get();
        }catch(Exception e){}
        return ws.getSoap();
    }

    public static void mensaje(Context ctx, String msj)
    {
        Toast.makeText(ctx, msj, Toast.LENGTH_SHORT).show();
    }

    public static Bitmap getImageBmp(String url)
    {
        Bitmap bm = null;
        InputStream is = null;
        BufferedInputStream bis = null;
        try
        {
            URLConnection conn = new URL(url).openConnection();
            conn.connect();
            is = conn.getInputStream();
            bis = new BufferedInputStream(is, 8192);
            bm = BitmapFactory.decodeStream(bis);
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
        finally {
            if (bis != null)
            {
                try
                {
                    bis.close();
                }
                catch (IOException e)
                {
                    e.printStackTrace();
                }
            }
            if (is != null)
            {
                try
                {
                    is.close();
                }
                catch (IOException e)
                {
                    e.printStackTrace();
                }
            }
        }
        return bm;
    }

}

class TareaWSConsulta extends AsyncTask<String,SoapObject,SoapObject> {

    public SoapObject resSoap;
    public Hashtable<String, String> parametros = null;
    public TareaWSConsulta(Hashtable<String, String> params)
    {
        parametros = params;
    }

    protected SoapObject doInBackground(String... params) {

        resSoap = null;

        final String NAMESPACE = "http://microsoft.com/webservices/";
        final String URLWEB="http://servicios.casaley.com.mx/WsPub/WsBridge.asmx?op=" + params[0];
        final String SOAP_ACTION = "http://tempuri.org/" + params[0];


        SoapObject request = new SoapObject(NAMESPACE, params[0]);

        if(parametros != null)
        {
            Enumeration en = parametros.keys();
            String clave;
            while( en.hasMoreElements() ){
                clave = (String)en.nextElement();
                Log.e("casa", clave + " " + parametros.get(clave));
                request.addProperty(clave, parametros.get(clave));
            }
        }
        Log.e("ley", request.toString());

        SoapSerializationEnvelope envelope =
                new SoapSerializationEnvelope(SoapEnvelope.VER11);
        envelope.dotNet = true;

        envelope.setOutputSoapObject(request);

        HttpTransportSE transporte = new HttpTransportSE(URLWEB);

        try
        {
            transporte.call(SOAP_ACTION, envelope);
            resSoap =(SoapObject)envelope.getResponse();
            //if(params[0].equals("GetCities"))
            {
                Log.e("casa_ley", envelope.getResponse().toString());
                Log.e("casa_ley", envelope.bodyIn.toString());
                Log.e("casa_ley", envelope.toString());
            }
        }
        catch (Exception e)
        {
            Log.e("casa_ley", e.toString());
            resSoap = null;
        }

        return resSoap;
    }

    public SoapObject getSoap()
    {
        return resSoap;
    }
}