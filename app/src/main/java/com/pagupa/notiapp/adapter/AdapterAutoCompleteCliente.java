package com.pagupa.notiapp.adapter;


import android.app.Notification;
import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.RecyclerView;

import com.pagupa.notiapp.R;
import com.pagupa.notiapp.clases.Server;
import com.pagupa.notiapp.model.Orden;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

public class AdapterAutoCompleteCliente extends ArrayAdapter<String> implements Filterable {
    private ArrayList<String> data;

    public AdapterAutoCompleteCliente(Context context, int textViewResourceId) {
        super(context, textViewResourceId);
        data = new ArrayList<String>();
    }

    @Override
    public int getCount() {
        return data.size();
    }

    @Override
    public String getItem(int index) {
        return data.get(index);
    }

    @Override
    public Filter getFilter() {
        return new Filter() {
            @Override
            protected FilterResults performFiltering(CharSequence constraint) {
                FilterResults filterResults = new FilterResults();
                if(constraint != null) {
                    HttpURLConnection conn = null;
                    InputStream input = null;
                    try {
                        data.clear();
                        Log.e("list clie", "buscando" +constraint.toString());
                        URL url = new URL(Server.dns+"/buscarClienteNombreCedula?buscar=" + constraint.toString());
                        conn = (HttpURLConnection) url.openConnection();
                        input = conn.getInputStream();
                        InputStreamReader reader = new InputStreamReader(input, "UTF-8");
                        BufferedReader buffer = new BufferedReader(reader, 8192);
                        StringBuilder builder = new StringBuilder();
                        String line;

                        while ((line = buffer.readLine()) != null) {
                            builder.append(line);
                        }

                        Log.e("list clie", builder.toString());
                        JSONArray terms = new JSONArray (builder.toString());
                        ArrayList<String> suggestions = new ArrayList<>();

                        for (int ind = 0; ind < terms.length(); ind++) {
                            JSONObject registro = terms.getJSONObject(ind);

                            String term = registro.getString("cliente_nombre")+"  -  "+registro.getString("cliente_id");
                            suggestions.add (term);
                        }

                        filterResults.values = suggestions;
                        filterResults.count = suggestions.size();
                        data = suggestions;
                    }
                    catch (Exception ex) {
                        ex.printStackTrace();
                    }
                    finally {
                        if (input != null) {
                            try {
                                input.close();
                            }
                            catch (Exception ex) {
                                ex.printStackTrace();
                            }
                        }
                        if (conn != null) conn.disconnect();
                    }
                }
                return filterResults;
            }

            @Override
            protected void publishResults(CharSequence contraint, FilterResults results) {
                if(results != null && results.count > 0) {
                    notifyDataSetChanged();
                }
                else {
                    notifyDataSetInvalidated();
                }
            }
        };
    }
}
