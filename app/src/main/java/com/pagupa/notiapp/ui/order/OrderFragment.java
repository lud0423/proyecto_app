package com.pagupa.notiapp.ui.order;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.pagupa.notiapp.adapter.AdapterOrden;
import com.pagupa.notiapp.clases.Server;
import com.pagupa.notiapp.databinding.FragmentOrderBinding;
import com.pagupa.notiapp.model.Detalle;
import com.pagupa.notiapp.model.DetalleOrden;
import com.pagupa.notiapp.model.Orden;
import com.pagupa.notiapp.model.Responsable;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class OrderFragment extends Fragment {
    private FragmentOrderBinding binding;

    private RecyclerView rv;
    private Spinner spState;
    private AdapterOrden adapterOrden;
    private String TAG="OF";
    private SwipeRefreshLayout swipeRefreshLayout;

    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        OrderViewModel homeViewModel = new ViewModelProvider(this).get(OrderViewModel.class);

        binding = FragmentOrderBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        spState= binding.idHomeSpiner;
        spState.setSelection(2);

        swipeRefreshLayout= binding.idHomeSwipe;
        final RecyclerView rv=binding.idHomeRecycler;
        rv.setLayoutManager(new GridLayoutManager(getActivity(),1));
        rv.setVerticalScrollBarEnabled(true);
        adapterOrden = new AdapterOrden(getActivity());
        rv.setAdapter(adapterOrden);


        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                int i=spState.getSelectedItemPosition();
                cargarListaOrdenes(i);
            }
        });

        spState.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                cargarListaOrdenes(i);
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        return root;
    }

    private void cargarListaOrdenes(int i){
        StringRequest stringRequest = new StringRequest(Request.Method.GET, Server.dns+"/mantenimiento?orden_estado="+i,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        JSONObject jsonObject = null;
                        try {
                            adapterOrden.clear();

                            jsonObject = new JSONObject(response);
                            JSONArray jsonArray = jsonObject.getJSONArray("data");

                            for (int i = 0; i < jsonArray.length(); i++) {
                                JSONObject jsonOrden = jsonArray.getJSONObject(i);

                                Orden orden=new Orden();
                                orden.setOrden_id(jsonOrden.getInt("orden_id"));
                                orden.setCliente_id(jsonOrden.getInt("cliente_id"));


                                SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd");
                                String fecha=jsonOrden.getString("orden_fecha_inicio");
                                fecha=fecha.replace("-", "/");

                                orden.setOrden_fecha_inicio(sdf.parse(fecha));
                                orden.setOrden_prioridad(jsonOrden.getInt("orden_prioridad"));
                                orden.setOrden_lugar(jsonOrden.getString("orden_lugar"));
                                orden.setOrden_asignacion(jsonOrden.getString("orden_asignacion"));

                                if(jsonOrden.getString("orden_resultado")=="null")
                                    orden.setOrden_resultado(0);
                                else
                                    orden.setOrden_resultado(Integer.parseInt(jsonOrden.getString("orden_resultado")));

                                orden.setOrden_estado(Integer.parseInt(jsonOrden.getString("orden_estado")));
                                adapterOrden.addOrden(orden);
                                Log.e(TAG, "responable "+orden.toString());
                            }

                            if(swipeRefreshLayout.isRefreshing())  swipeRefreshLayout.setRefreshing(false);

                        } catch (JSONException | ParseException e) {
                            Log.e(TAG, "excep "+e.toString());
                            Toast.makeText(getActivity(), e.toString(), Toast.LENGTH_LONG).show();

                            if(swipeRefreshLayout.isRefreshing())  swipeRefreshLayout.setRefreshing(false);
                        }
                    }
                }, new Response.ErrorListener(){
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(getActivity(), error.toString(), Toast.LENGTH_LONG).show();
                if(swipeRefreshLayout.isRefreshing())  swipeRefreshLayout.setRefreshing(false);
            }
        }){
        };

        RequestQueue requestQueue = Volley.newRequestQueue(getActivity());
        requestQueue.add(stringRequest);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}