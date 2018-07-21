package com.example.szarathkumar.gamechangemob;

import android.util.Log;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.Charset;

/**
 * Created by Szarath on 2016/08/13.
 */
public class Json {

    public Json()
    {

    }

    private static final String Currentbrowser = "Mozilla/5.0";
   /* public static JSONObject getserverinfo(String get) throws IOException{
        try {
            //URL url = new URL("http://twogame.somee.com/WcfServices/"+ post);
            URL url = new URL("http://twogameswcf.azurewebsites.net/" + get );
          // URL url = new URL("http://192.168.2.50:5000/"+ get);
            HttpURLConnection ucon = (HttpURLConnection) url.openConnection();

            Log.d("ucon",ucon.toString());
            String response = "";
            if (ucon.getResponseCode() == HttpURLConnection.HTTP_OK) {
                Scanner sc = new Scanner(ucon.getInputStream());

               while (sc.hasNextLine()) {
                    response += sc.nextLine();


                }
               // ucon.setDoOutput(true);

            } else {
                Scanner sc = new Scanner(ucon.getErrorStream());
                response = null;

                while (sc.hasNextLine()) {
                    response += sc.nextLine();
                }

            }

            Log.d("Json", response.toString());
            return new JSONObject(response);

        }
        catch (JSONException jo)
        {
            jo.printStackTrace();
        }
        catch (Exception ex)
        {ex.printStackTrace();}

        return null;
    }*/
   private static String readAll(BufferedReader rd) throws IOException {
       StringBuilder sb = new StringBuilder();
       int cp;
       while ((cp = rd.read()) != -1) {
           sb.append((char) cp);
       }
       return sb.toString();
   }

    public static JSONObject getserverinfo(String get) throws IOException, JSONException {

        //String url = new String("http://twogameswcf.azurewebsites.net/" + get );
         String url = new String("http://192.168.43.239:5000/Service2.svc/"+ get);
        InputStream is = new URL(url).openStream();
        try {
            Log.d("ucon",url.toString());
            BufferedReader rd = new BufferedReader(new InputStreamReader(is, Charset.forName("UTF-8")));
            String jsonText = readAll(rd);
            JSONObject json = new JSONObject(jsonText);
            return json;
        } finally {
            is.close();
        }
    }
    public static JSONObject getserver(String get) throws IOException, JSONException {

        //String url = new String("http://twogameswcf.azurewebsites.net/" + get );
        String url = new String( get);
        InputStream is = new URL(url).openStream();
        try {
            Log.d("ucon",url.toString());
            BufferedReader rd = new BufferedReader(new InputStreamReader(is, Charset.forName("UTF-8")));
            String jsonText = readAll(rd);
            JSONObject json = new JSONObject(jsonText);
            return json;
        } finally {
            is.close();
        }
    }
    public static void setserverinfo(String post, JSONObject param) throws Exception {


        //URL obj = new URL("http://twogameswcf.azurewebsites.net/" + post );


        URL obj = new URL("http://localhost:50834/"+ post);

        String data =param.toString();
        HttpURLConnection conn = (HttpURLConnection) obj.openConnection();


        conn.setRequestProperty("Content-Type","application/json; charset=utf-8");
        conn.setRequestProperty("Accept", "application/json");
        conn.setRequestProperty("User-Agent", "Pigeon");
        conn.setChunkedStreamingMode(0);

        conn.setDoInput(true);
        conn.setDoOutput(true);
        conn.setRequestMethod("POST");


        Log.d("obj",obj.toString());
        Log.d("param",param.toString());

        OutputStream out = new BufferedOutputStream(conn.getOutputStream());
        BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(out, "UTF-8"));
        writer.write(data);

        writer.flush();
        writer.close();
        out.close();
        conn.connect();


        InputStream in = new BufferedInputStream(conn.getInputStream());
        BufferedReader reader = new BufferedReader(new InputStreamReader(
                in, "UTF-8"));
        StringBuilder sb = new StringBuilder();
        String line = null;
        while ((line = reader.readLine()) != null) {
            sb.append(line);
        }
        in.close();
        String result = sb.toString();
        Log.d("server", "Server response = " + result);

        conn.disconnect();







    }




}
