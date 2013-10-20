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
import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserFactory;


import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.StringReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.Hashtable;
import java.util.List;


public class Util {

    public static Bitmap [] ofertas = null;
    public static Hashtable<Estado, ArrayList<String>> Estados = new Hashtable<Estado, ArrayList<String>>();
    public static List<Recordatorio> lista_recordatorio = new ArrayList<Recordatorio>();
    public static List<Tienda> tiendas = new ArrayList<Tienda>();

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

    public static SoapObject sendSOAPwithData(String metodo, Hashtable<String, String> params)
    {
        try{
            String reqXML = "<?xml version=\"1.0\" encoding=\"utf-8\"?><soap:Envelope xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\" xmlns:xsd=\"http://www.w3.org/2001/XMLSchema\" xmlns:soap=\"http://schemas.xmlsoap.org/soap/envelope/\"><soap:Body><" + metodo + " xmlns=\"http://tempuri.org/\"";
            String clave;
            if(params != null)
            {
                reqXML += ">";
                Enumeration<String> keys = params.keys();
                while(keys.hasMoreElements())
                {
                    clave = keys.nextElement();
                    reqXML += "<" + clave + ">" + params.get(clave) + "</" + clave + ">";
                }
                reqXML += "</" + metodo + "></soap:Body></soap:Envelope>";
            }else
                reqXML += " /></soap:Body></soap:Envelope>";

            URL oURL = new URL("http://servicios.casaley.com.mx/WsPub/WsBridge.asmx?op=" + metodo);
            HttpURLConnection con = (HttpURLConnection) oURL.openConnection();
            con.setRequestMethod("POST");
            con.setRequestProperty("Content-type", "text/xml; charset=utf-8");
            con.setRequestProperty("SOAPAction",
                    "http://tempuri.org/" + metodo);
            con.setDoOutput(true);
            OutputStream reqStream = con.getOutputStream();
            reqStream.write(reqXML.getBytes());
            java.io.BufferedReader rd = new java.io.BufferedReader(new java.io.InputStreamReader(con.getInputStream()));
            byte[] byteBuf = new byte[10240];
            String line;
            while ((line = rd.readLine()) != null) {
                SoapObject objeto = new SoapObject();
                SoapSerializationEnvelope env = new SoapSerializationEnvelope(SoapEnvelope.VER11);
                XmlPullParserFactory factory = XmlPullParserFactory.newInstance();
                factory.setNamespaceAware(true);
                XmlPullParser xpp = factory.newPullParser();
                xpp.setInput(new StringReader(line));
                env.parse(xpp);
                return (SoapObject)env.getResponse();
            }
        }catch(Exception e){
            e.printStackTrace();
        }
        return null;
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
                request.addProperty(clave, parametros.get(clave));
            }
        }

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
            Log.e("casa_ley e", e.toString());
            resSoap = null;
        }

        return resSoap;
    }

    public SoapObject getSoap()
    {
        return resSoap;
    }
}