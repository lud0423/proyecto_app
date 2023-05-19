package com.pagupa.notiapp.ui.createorder;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.pagupa.notiapp.R;
import com.pagupa.notiapp.adapter.AdapterAutoCompleteCliente;
import com.pagupa.notiapp.adapter.AdapterAutoCompleteEmpleado;
import com.pagupa.notiapp.adapter.AdapterAutoCompleteProducto;
import com.pagupa.notiapp.clases.Server;
import com.pagupa.notiapp.databinding.FragmentOrderCreateBinding;
import com.pagupa.notiapp.model.Detalle;
import com.pagupa.notiapp.model.DetalleOrden;
import com.pagupa.notiapp.model.Responsable;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class CreateOrderFragment extends Fragment {
    private FragmentOrderCreateBinding binding;
    private String TAG="DOF";

    private AutoCompleteTextView txtCliente, txtEmpleado, txtEquipoEscribir;
    private ImageButton  btnAceptarTipo, btnAceptarEquipo;
    private LinearLayout btnAbrirTipo,btnAbrirEquipo  ;
    private TextView txtOrden, txtFechaInicio, txtLugar, txtObservacion, txtFechaFinal, txtResultado, txtEstatus, txtRecibidoPor;
    private Spinner spPrioridad, spTipoOrden;
    private EditText txtTipo, txtEquipo, txtAsignacion, txtTecnico, txtLogistica;
    private EditText txtTipoLista, txtTipoEscribir, txtEquipoLista;
    private EditText txtEquipoCantidad;

    private Button btnIrAGuardar, btnAgregarTipo, btnAgregarEquipo;
    private AdapterAutoCompleteCliente adapter;
    private AdapterAutoCompleteEmpleado adapterEmp;
    private AdapterAutoCompleteProducto adapterProd;
    private int CLIENTE_ID, EMPLEADO_ID, PRODUCTO_ID, PRODUCTO_STOCK;
    private CardView cvTipo, cvEquipo;

    List<DetalleOrden> detalleOrdenList=new ArrayList<>();
    List<Detalle> detalleList=new ArrayList<>();
    List<Responsable> responsableList=new ArrayList<>();

    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)  {
        binding = FragmentOrderCreateBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        btnAbrirTipo=binding.idOrdenCreateBtnTipo;
        btnAceptarTipo=binding.idCrearTipoBtnAceptar;
        btnAgregarTipo=binding.idCrearTipoBtnAgregar;

        btnIrAGuardar=binding.idOrdenCreateBtnGuardar;

        cvTipo=binding.idCrearOrdenTipoCardview;
        cvEquipo=binding.idCrearOrdenEquipoCardview;

        txtCliente=binding.idOrderCreateCliente;
        adapter = new AdapterAutoCompleteCliente(getContext(), android.R.layout.simple_dropdown_item_1line);
        txtCliente.setAdapter(adapter);

        txtEmpleado=binding.idOrdenCreateResponsable;
        adapterEmp = new AdapterAutoCompleteEmpleado(getContext(), android.R.layout.simple_dropdown_item_1line);
        txtEmpleado.setAdapter(adapterEmp);

        txtTipo=binding.idOrdenCreateType;
        txtTipoEscribir=binding.idCrearTipoTxtEscribir;
        txtTipoLista=binding.idCrearTipoTxtLista;

        txtEquipo=binding.idOrdenCreateEquipment;
        txtEquipoCantidad=binding.idCrearEquipoTxtCantidad;
        txtEquipoEscribir=binding.idCrearEquipoTxtEscribir;
        adapterProd = new AdapterAutoCompleteProducto(getContext(), android.R.layout.simple_dropdown_item_1line);
        txtEquipoEscribir.setAdapter(adapterProd);
        txtEquipoLista=binding.idCrearEquipoTxtLista;

        btnAbrirEquipo=binding.idOrdenCreateBtnTipo2;
        btnAceptarEquipo=binding.idCrearEquipoBtnAceptar;
        btnAgregarEquipo=binding.idCrearEquipoBtnAgregar;


        spPrioridad=binding.idOrderCreatePriority;
        spTipoOrden= binding.idOrderCreateTypeOrder;
        txtLugar=binding.idOrdenCreatePlace;
        txtAsignacion=binding.idOrdenCreateAssignment;
        txtLogistica=binding.idOrdenCreateLigistic;




        txtTipo.setKeyListener(null);
        txtEquipo.setKeyListener(null);
        //esto es una prueba para verificar que se oculte la linea del txt
        //hacemos uso de focus listener
        txtCliente.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean b) {

            }

        }); //esto es una prueba para verificar que se oculte la linea del txt
        //aqui termina

        btnAbrirTipo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                cvTipo.setVisibility(View.VISIBLE);
                txtTipoLista.setText("");
                txtTipoEscribir.setText("");

                for(Detalle det: detalleList){
                    txtTipoLista.setText(txtTipoLista.getText().toString()+""+ det.getDescripcion()+"\n");
                }
            }
        });

        btnAgregarTipo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(!txtTipoEscribir.getText().toString().equals("")) {
                    Detalle det = new Detalle();
                    det.setDescripcion(txtTipoEscribir.getText().toString());
                    det.setEstado(1);
                    detalleList.add(det);

                    txtTipoLista.setText(txtTipoLista.getText().toString() + txtTipoEscribir.getText().toString() + "\n");
                    txtTipoEscribir.setText("");
                }
            }
        });

        btnAceptarTipo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                cvTipo.setVisibility(View.GONE);

                txtTipo.setText("");

                for(Detalle det: detalleList){
                    txtTipo.setText(txtTipo.getText().toString()+""+ det.getDescripcion()+"\n");
                }
            }
        });
        btnAbrirEquipo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                cvEquipo.setVisibility(View.VISIBLE);
                txtEquipoLista.setText("");
                txtEquipoEscribir.setText("");

                for(DetalleOrden detO: detalleOrdenList){
                    int disponibles=0;

                    if(detO.getDetalle_orden_stock()>=detO.getDetalle_orden_cantidad())
                        disponibles=detO.getDetalle_orden_cantidad();
                    else
                        disponibles=detO.getDetalle_orden_cantidad()-detO.getDetalle_orden_stock();

                    txtEquipoLista.setText(txtEquipoLista.getText().toString()+
                            detO.getProducto_nombre()+"  "+detO.getDetalle_orden_cantidad()+"/"+disponibles+"\n");
                }
            }
        });
        btnAgregarEquipo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!txtEquipoCantidad.getText().toString().equals("")) {
                    int cant = Integer.parseInt(txtEquipoCantidad.getText().toString());

                    if (!txtEquipoEscribir.getText().toString().equals("")) {
                        DetalleOrden det = new DetalleOrden();
                        det.setProducto_id(PRODUCTO_ID);
                        det.setProducto_nombre(txtEquipoEscribir.getText().toString());
                        det.setDetalle_orden_cantidad(cant);
                        det.setDetalle_orden_stock(PRODUCTO_STOCK);
                        det.setDetalle_orden_estado(1);
                        detalleOrdenList.add(det);

                        int disponibles = 0;

                        if (det.getDetalle_orden_stock() >= det.getDetalle_orden_cantidad())
                            disponibles = det.getDetalle_orden_cantidad();
                        else
                            disponibles = det.getDetalle_orden_cantidad() - det.getDetalle_orden_stock();

                        txtEquipoLista.setText(txtEquipoLista.getText().toString() +
                                det.getProducto_nombre() + "  " + det.getDetalle_orden_cantidad() + "/" + disponibles + "\n");
                        txtEquipoEscribir.setText("");
                        txtEquipoCantidad.setText("1");
                        Log.e(TAG, "agregué producto");
                    }
                } else {
                    Toast.makeText(getContext(), "Digite una cantidad para el Equipo", Toast.LENGTH_LONG).show();
                    txtEquipoCantidad.requestFocus();
                }
            }
        });
        btnAceptarEquipo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                cvEquipo.setVisibility(View.GONE);

                txtEquipo.setText("");

                for(DetalleOrden det: detalleOrdenList){
                    int disponibles=0;

                    if(det.getDetalle_orden_stock()>=det.getDetalle_orden_cantidad())
                        disponibles=det.getDetalle_orden_cantidad();
                    else
                        disponibles=det.getDetalle_orden_cantidad()-det.getDetalle_orden_stock();

                    txtEquipo.setText(txtEquipo.getText().toString()+
                            det.getProducto_nombre()+"  "+det.getDetalle_orden_cantidad()+"/"+disponibles+"\n");
                }
            }
        });
        btnIrAGuardar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                guardarOrden();
            }
        });

        txtEmpleado.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Log.e(TAG, "click ");
                String cadena=txtEmpleado.getText().toString();
                String[] array=cadena.split("  -  ");

                txtEmpleado.setText(array[0]);
                EMPLEADO_ID=Integer.parseInt(array[1]);
            }
        });
        txtCliente.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Log.e(TAG, "click ");
                String cadena=txtCliente.getText().toString();
                String[] array=cadena.split("  -  ");

                txtCliente.setText(array[0]);
                CLIENTE_ID=Integer.parseInt(array[1]);
            }
        });
        txtEquipoEscribir.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                String cadena=txtEquipoEscribir.getText().toString();
                String[] array=cadena.split("  -  ");

                txtEquipoEscribir.setText(array[0]);
                PRODUCTO_ID=Integer.parseInt(array[1]);
                PRODUCTO_STOCK=Integer.parseInt(array[2]);
            }
        });

        return root;
    }

    public void guardarOrden(){
        if(CLIENTE_ID!=0){
            if(EMPLEADO_ID!=0){
                StringRequest stringRequest = new StringRequest(Request.Method.POST, Server.dns+"/guardarordenmantenimiento",
                        new Response.Listener<String>() {
                            @Override
                            public void onResponse(String response) {
                                JSONObject jsonObject = null;
                                try {
                                    jsonObject = new JSONObject(response);
                                    if(jsonObject.getString("result").equals("OK")) {
                                        Toast.makeText(getActivity(), "Orden Creada Correctamente", Toast.LENGTH_SHORT).show();
                                        regresarAlaListaOrden();
                                    }
                                    else {
                                        Toast.makeText(getActivity(), jsonObject.getString("mensaje"), Toast.LENGTH_SHORT).show();
                                        Log.e(TAG, "error 0" + jsonObject.getString("mensaje"));
                                    }
                                } catch (JSONException e) {
                                    Toast.makeText(getActivity(), e.toString(), Toast.LENGTH_LONG).show();
                                    Log.e(TAG, "error 1: " + e.getMessage());
                                }
                            }
                        }, new Response.ErrorListener(){
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(getActivity(), error.toString(), Toast.LENGTH_LONG).show();
                        Log.e(TAG, "error 2: "+error.getMessage());
                    }
                }){
                    protected Map<String, String> getParams() throws AuthFailureError {
                        Map<String, String> parametro = new HashMap<>();

                        Log.e(TAG, "prioridad "+spPrioridad.getSelectedItem().toString());

                        String prio=spPrioridad.getSelectedItem().toString();
                        if(prio.equals("Normal"))  parametro.put("prioridad", "1");
                        if(prio.equals("Importante"))  parametro.put("prioridad", "2");
                        if(prio.equals("Urgente"))  parametro.put("prioridad", "3");

                        String tipo=spTipoOrden.getSelectedItem().toString();
                        if(tipo.equals("Rutas Técnicas"))  parametro.put("tipo_id", "1");
                        if(tipo.equals("Sistemas"))  parametro.put("tipo_id", "2");

                        parametro.put("lugar", txtLugar.getText().toString());
                        parametro.put("asignacion", txtAsignacion.getText().toString());
                        parametro.put("logistica", txtLogistica.getText().toString());

                        parametro.put("cliente", Integer.toString(CLIENTE_ID));

                        parametro.put("responsable[0]", Integer.toString(EMPLEADO_ID));

                        if(detalleList.size()>0){
                            int i=0;

                            for (Detalle det: detalleList) {
                                parametro.put("detalle["+i+"]", det.getDescripcion());
                                i++;
                            }
                        }
                        if(detalleOrdenList.size()>0){
                            int j=0;

                            for (DetalleOrden det: detalleOrdenList) {
                                parametro.put("cantidad_detalle_orden["+j+"]", Integer.toString(det.getDetalle_orden_cantidad()));
                                parametro.put("descripcion_detalle_orden["+j+"]", det.getProducto_nombre());
                                parametro.put("stock_detalle_orden["+j+"]", Integer.toString(det.getDetalle_orden_stock()));
                                parametro.put("id_detalle_orden["+j+"]", Integer.toString(det.getProducto_id()));
                                j++;
                            }
                        }

                        return parametro;
                    }
                };

                RequestQueue requestQueue = Volley.newRequestQueue(getActivity());
                requestQueue.add(stringRequest);
            }
            else
                Toast.makeText(getContext(), "Seleccione un Técnico", Toast.LENGTH_LONG).show();
        }
        else
            Toast.makeText(getContext(), "Seleccione un Cliente", Toast.LENGTH_LONG).show();
    }

    public void regresarAlaListaOrden(){
        Navigation.findNavController(getView()).navigate(R.id.nav_home);
    }

    public void hideKeyboard () {
        View view = getActivity().getCurrentFocus();
        if (view != null) {
            InputMethodManager imm = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
        }
    }


    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}