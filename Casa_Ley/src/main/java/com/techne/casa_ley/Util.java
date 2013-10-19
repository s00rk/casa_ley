package com.techne.casa_ley;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.util.Log;

import org.ksoap2.SoapEnvelope;
import org.ksoap2.serialization.SoapObject;
import org.ksoap2.serialization.SoapSerializationEnvelope;
import org.ksoap2.transport.HttpTransportSE;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.net.URLConnection;

public class Util {

    public static SoapObject obtenerSOAP(String metodo)
    {
        TareaWSConsulta ws = new TareaWSConsulta();
        try{
            ws.execute(metodo).get();
        }catch(Exception e){}
        return ws.getSoap();
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

    protected SoapObject doInBackground(String... params) {

        resSoap = null;

        final String NAMESPACE = "http://microsoft.com/webservices/";
        final String URLWEB="http://servicios.casaley.com.mx/WsPub/WsBridge.asmx?op=" + params[0];
        final String SOAP_ACTION = "http://tempuri.org/" + params[0];

        Log.e("casa_ley", URLWEB);
        Log.e("casa_ley", NAMESPACE);
        Log.e("casa_ley", SOAP_ACTION);

        SoapObject request = new SoapObject(NAMESPACE, params[0]);

        SoapSerializationEnvelope envelope =
                new SoapSerializationEnvelope(SoapEnvelope.VER11);
        envelope.dotNet = true;

        envelope.setOutputSoapObject(request);

        HttpTransportSE transporte = new HttpTransportSE(URLWEB);

        try
        {
            transporte.call(SOAP_ACTION, envelope);
            resSoap =(SoapObject)envelope.getResponse();

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