package com.pagupa.notiapp.clases;

import static android.content.Context.MODE_PRIVATE;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.util.Log;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.firebase.database.FirebaseDatabase;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;


public class Server {
    //public static String dns="http://172.16.14.3:8089/public";
    public static String dns="http://172.16.14.5:9000";
    //public static String dns="http://177.234.237.21:8089/public";
    //public static String dns="http://172.16.14.5";
    private static int user_id;
    private static String user_username;
    private static String user_usermail;
    private static String user_rol;

    public static int getUser_id(Context c) {
        user_id=getIntPreferences(c, "user_id");
        return user_id;
    }
    public static void setUser_id(Context c, int id) {
        user_id=id;
        saveValueIntPreferences(c,"user_id", id);
    }

    public static String getUser_rol(Context c) {
        user_rol=getStringPreferences(c, "user_rol");
        return user_rol;
    }
    public static void setUser_rol(Context c, String rol) {
        user_rol=rol;
        saveValueStringPreferences(c, "user_rol", rol);
    }

    public static String getUser_username(Context c) {
        user_username=getStringPreferences(c, "user_username");
        return user_username;
    }
    public static void setUser_username(Context c, String name) {
        user_username=name;
        saveValueStringPreferences(c, "user_username", name);
    }

    public static String getUser_usermail(Context c) {
        user_usermail=getStringPreferences(c,"user_usermail");
        return user_usermail;
    }
    public static void setUser_usermail(Context c, String mail) {
        user_usermail=mail;
        saveValueStringPreferences(c, "user_username", mail);
    }

    public static void saveAllPreferences(Context c, int id, String name, String mail, String rol){
        SharedPreferences sharedPref = c.getSharedPreferences("datos-usuarios", MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPref.edit();
        editor.putInt("user_id", id);
        editor.putString("user_username", name);
        editor.putString("user_usermail", mail);
        editor.putString("user_rol", rol);
        editor.apply();
        editor.commit();
    }

    public static void saveValueIntPreferences(Context c, String key, int valor){
        SharedPreferences sharedPref = c.getSharedPreferences("datos-usuarios", MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPref.edit();
        editor.putInt(key, valor);
        editor.commit();
    }

    public static void saveValueStringPreferences(Context c, String key, String valor){
        SharedPreferences sharedPref = c.getSharedPreferences("datos-usuarios", MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPref.edit();
        editor.putString(key, valor);
        editor.commit();
    }

    public static int getIntPreferences(Context c, String key){
        SharedPreferences sharedPref = c.getSharedPreferences("datos-usuarios", MODE_PRIVATE);
        Log.e("PREF", sharedPref.getAll().toString());
        return sharedPref.getInt(key, 0);
    }

    public static String getStringPreferences(Context c, String key){
        SharedPreferences sharedPref = c.getSharedPreferences("datos-usuarios", MODE_PRIVATE);
        return sharedPref.getString(key, "");
    }

    public static boolean borrarPreferencias(Context c){
        SharedPreferences sharedPref = c.getSharedPreferences("datos-usuarios", MODE_PRIVATE);
        sharedPref.edit().clear().commit();

        user_id=0;
        user_username="";
        user_usermail="";
        user_rol="";
        return true;
    }

    public static void guardarTokenFcm(Context c, String token){
        StringRequest stringRequest = new StringRequest(Request.Method.POST, Server.dns+"/mantenimientoguardartoken",
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        JSONObject jsonObject = null;
                        try {
                            jsonObject = new JSONObject(response);
                            if(jsonObject.getString("result").equals("OK")) {
                                Toast.makeText(c, "Credenciales Actualizadas",Toast.LENGTH_SHORT).show();
                                Log.e("Server", "se ha guardado el token correctamete");
                            }
                        } catch (JSONException e) {
                            Log.e("Server", "error "+e.getMessage());
                        }
                    }
                }, new Response.ErrorListener(){
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e("Server", "error "+error.getMessage());
            }
        }){
            protected Map<String, String> getParams() throws AuthFailureError {
                HashMap<String, String> parametro = new HashMap<>();
                parametro.put("user_id", Integer.toString(user_id));
                parametro.put("user_fcm_token", token);
                return parametro;
            }
        };

        RequestQueue requestQueue = Volley.newRequestQueue(c);
        requestQueue.add(stringRequest);
    }
}
