package com.pagupa.notiapp.ui.detailorder;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.Image;
import android.os.Bundle;
import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
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
import com.bumptech.glide.Glide;
import com.google.firebase.database.FirebaseDatabase;
import com.pagupa.notiapp.R;
import com.pagupa.notiapp.clases.Server;
import com.pagupa.notiapp.databinding.FragmentOrderBinding;
import com.pagupa.notiapp.databinding.FragmentOrderDetailBinding;
import com.pagupa.notiapp.model.Detalle;
import com.pagupa.notiapp.model.DetalleOrden;
import com.pagupa.notiapp.model.Orden;
import com.pagupa.notiapp.model.Responsable;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class DetailOrderFragment extends Fragment {
    private FragmentOrderDetailBinding binding;
    private String TAG = "DOF";

    private TextView txtOrden, txtFechaInicio, txtCliente, txtLugar, txtObservacion,
            txtFechaFinal, txtResultado, txtEstatus, txtRecibidoPor, txtPrioridad;
    private EditText txtTipo, txtEquipo, txtAsignacion, txtTecnico, txtLogistica;
    private ImageView imgFirma, btnIrAGuardar;
    private LinearLayout llPrioridadColor;
    private Button btnIrAnular;
    private Orden orden;

    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentOrderDetailBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        txtOrden = binding.idDetailOrderNumber;
        txtFechaInicio = binding.idDetailOrderDateInit;
        txtTipo = binding.idDetailOrderType;


        txtCliente = binding.idDetailOrderClient;
        txtLugar = binding.idDetailOrderPlace;
        txtPrioridad = binding.idDetailPriority;
        llPrioridadColor = binding.idDetailPriorityColor;

        txtEquipo = binding.idDetailOrderEquipment;
        txtAsignacion = binding.idDetailOrderAssignment;
        txtTecnico = binding.idDetailOrderResponsable;
        txtLogistica = binding.idDetailOrderLigistic;

        txtObservacion = binding.idDetailOrderObservation;
        txtFechaFinal = binding.idDetailOrderDateFinal;
        txtResultado = binding.idDetailOrderResult;
        txtEstatus = binding.idDetailOrderStatus;
        txtRecibidoPor = binding.idDetailOrderReceiveBy;
        imgFirma = binding.idDetailOrdenSignature;

        btnIrAGuardar = binding.idDetailOrdenGoUpdate;
        btnIrAnular = binding.idDetailOrdenGoDelete;

        txtEquipo.setKeyListener(null);
        txtAsignacion.setKeyListener(null);
        txtTecnico.setKeyListener(null);
        txtTipo.setKeyListener(null);
        txtLogistica.setKeyListener(null);
        txtObservacion.setKeyListener(null);

        Bundle arguments = getArguments();
        if(arguments.containsKey("ORDEN ID")) {
            Log.e(TAG, getArguments().toString());

            //if (getArguments().getInt("ORDEN ID") > 0) {
                cargarOrden(getArguments().getInt("ORDEN ID"));
            //}
        }
        else if(arguments.containsKey("orden")) {
            Orden orden = (Orden) arguments.get("orden");
            cargarOrden(orden.getOrden_id());
        }

        btnIrAGuardar.setVisibility(View.GONE);
        btnIrAGuardar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Bundle b = new Bundle();
                b.putSerializable("orden", orden);
                Log.e("AO", orden.toString());

                hideKeyboard();
                Navigation.findNavController(view).navigate(R.id.nav_order_update, b);
            }
        });

        if(!Server.getUser_rol(getContext()).equals("Administrador")) btnIrAnular.setVisibility(View.GONE);

        btnIrAnular.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

                builder.setMessage("Vas a Anular la Orden N°"+orden.getOrden_id())
                        .setTitle("Confirmación del Usuario");

                builder.setPositiveButton("Anular", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        anularOrden(orden);
                    }
                });

                builder.setNegativeButton("Regresar", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {

                    }
                });

                AlertDialog dialog = builder.create();
                dialog.show();
            }
        });

        /*
        if (orden != null) {
            txtOrden.setText(""+orden.getOrden_id());

            SimpleDateFormat sdf=new SimpleDateFormat("dd/MM/yyyy");
            txtFechaInicio.setText(sdf.format(orden.getOrden_fecha_inicio()));

            txtCliente.setText(orden.getCliente_nombre());
            txtLugar.setText(orden.getOrden_lugar());

            List<Responsable> rl = orden.getResponsables();
            for(Responsable resp: rl){
                txtTecnico.setText(txtTecnico.getText().toString()+resp.getResponsable_nombre_empleado()+"\n");
            }

            List<Detalle> dl = orden.getDetalles();
            for(Detalle det: dl){
                txtTipo.setText(txtTipo.getText().toString()+det.getDescripcion()+"\n");
            }

            List<DetalleOrden> dol = orden.getDetallesOrden();
            for(DetalleOrden detOrden: dol){
                txtEquipo.setText(txtEquipo.getText().toString()+detOrden.getDetalle_orden_cantidad()+" "+detOrden.getDetalle_orden_descripcion()+"\n");
            }

            imgFirma.setVisibility(View.GONE);
            if(orden.getOrden_recibido_por()!=null && !orden.getOrden_recibido_por().equals("")){
                txtRecibidoPor.setText(orden.getOrden_recibido_por());
                imgFirma.setVisibility(View.VISIBLE);

                Log.e(TAG, "orden "+orden.getOrden_id()+"  "+orden.getOrden_recibido_por());

                Glide.with(getContext())
                     .load(Server.dns+"/DocumentosMantenimiento/1/"+orden.getOrden_id()+"/firma_"+orden.getOrden_recibido_por()+".jpg")
                     .into(imgFirma);
            }
            else
                Log.e(TAG, "orden "+orden.getOrden_id()+"  no imagen");

            txtLogistica.setText(orden.getOrden_logistica());
            txtAsignacion.setText(orden.getOrden_asignacion());
            txtObservacion.setText(orden.getOrden_observacion());



            txtResultado.setText(orden.getOrden_resultado());

            if(orden.getOrden_estado()==0) {
                txtEstatus.setText("ANULADA");
                txtEstatus.setTextColor(getResources().getColor(R.color.black));
            }
            else if(orden.getOrden_estado()==1) {
                txtEstatus.setText("GENERADA");
                txtEstatus.setTextColor(getResources().getColor(R.color.normal));
            }
            else if(orden.getOrden_estado()==2) {
                txtEstatus.setText("EN PROCESO");
                txtEstatus.setTextColor(getResources().getColor(R.color.orange));
            }
            else{
                txtEstatus.setText("FINALIZADO");
                txtEstatus.setTextColor(getResources().getColor(R.color.green));
                txtFechaFinal.setText(""+orden.getOrden_fecha_finalizacion());
            }

            Log.e(TAG, "prioridad sp "+orden.getOrden_prioridad()+"   estado "+ orden.getOrden_estado());

            if(orden.getOrden_prioridad().equals("1")) spPrioridad.setSelection(0);
            if(orden.getOrden_prioridad().equals("2")) spPrioridad.setSelection(1);
            if(orden.getOrden_prioridad().equals("3")) spPrioridad.setSelection(2);

            btnIrAGuardar.setEnabled(true);
            btnIrAGuardar.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Bundle b=new Bundle();
                    b.putSerializable("orden", orden);

                    Log.e("AO", orden.toString());

                    hideKeyboard();

                    Navigation.findNavController(view).navigate(R.id.nav_order_update, b);
                }
            });
        }
        else
            btnIrAGuardar.setEnabled(false);
        */


        return root;
    }

    private void anularOrden(Orden orden){
        StringRequest stringRequest = new StringRequest(Request.Method.POST, Server.dns+"/anularOrden",
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        JSONObject jsonObject = null;
                        try {
                            jsonObject = new JSONObject(response);
                            if(jsonObject.getString("result").equals("OK")) {
                                Toast.makeText(getActivity(), "Orden Anulada Correctamente", Toast.LENGTH_SHORT).show();
                                regresarAlaOrden();
                            }
                            else {
                                Toast.makeText(getActivity(), "No se pudo anular la Orden: "+jsonObject.getString("mensaje"), Toast.LENGTH_SHORT).show();
                                Log.e(TAG, "error 0" + jsonObject.getString("mensaje"));
                            }
                        } catch (JSONException e) {
                            Toast.makeText(getActivity(), e.toString(), Toast.LENGTH_LONG).show();
                            Log.e(TAG, "error 1" + e.toString());
                        }
                    }
                }, new Response.ErrorListener(){
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(getActivity(), "Ocurrió un error: "+error.toString(), Toast.LENGTH_LONG).show();
                Log.e(TAG, "error 2"+error.toString());
            }
        }){
            protected Map<String, String> getParams() throws AuthFailureError {
                HashMap<String, String> parametro = new HashMap<>();
                parametro.put("orden_id", ""+orden.getOrden_id());
                return parametro;
            }
        };

        RequestQueue requestQueue = Volley.newRequestQueue(getActivity());
        requestQueue.add(stringRequest);
    }

    private void cargarOrden(int orden_id){
        StringRequest stringRequest = new StringRequest(Request.Method.GET, Server.dns+"/mantenimiento/"+orden_id+"/ver",
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.e(TAG, "respuesta {" + orden_id + "}/ver " + response);

                        JSONObject jsonObject;
                        try {

                            jsonObject = new JSONObject(response);
                            JSONObject jsonOrden = jsonObject.getJSONObject("data");
                            //Log.e(TAG, "tamaño "+ jsonArray.length());

                            //JSONObject jsonOrden = jsonArray.getJSONObject(i);
                            Log.e(TAG, " orden det " + jsonOrden.toString());

                            /////////////////////////////////////
                            txtOrden.setText("" + jsonOrden.getInt("orden_id"));

                            SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd");
                            String fecha=jsonOrden.getString("orden_fecha_inicio");
                            fecha=fecha.replace("-", "/");
                            Date fecha_inicio = sdf.parse(fecha);


                            SimpleDateFormat sdf2=new SimpleDateFormat("dd/MM/yyyy");

                            Log.e(TAG, "fecha "+fecha);
                            txtFechaInicio.setText(sdf2.format(fecha_inicio)+"      ");
                            JSONObject jsonCliente = jsonOrden.getJSONObject("cliente");

                            txtCliente.setText(jsonCliente.getString("cliente_nombre"));
                            txtLugar.setText(jsonOrden.getString("orden_lugar"));

                            ///////////////////////////////////orden////////////////////////////////
                            orden = new Orden();
                            orden.setOrden_id(jsonOrden.getInt("orden_id"));
                            orden.setOrden_fecha_inicio(fecha_inicio);
                            orden.setOrden_prioridad(jsonOrden.getInt("orden_prioridad"));
                            orden.setCliente_id(jsonCliente.getInt("cliente_id"));
                            orden.setCliente_nombre(jsonCliente.getString("cliente_nombre"));
                            orden.setOrden_estado(jsonOrden.getInt("orden_estado"));
                            orden.setOrden_resultado(jsonOrden.getInt("orden_resultado"));
                            orden.setOrden_estatus(jsonOrden.getInt("orden_estatus"));
                            //orden.setCliente_cedula();
                            orden.setOrden_lugar(jsonOrden.getString("orden_lugar"));
                            orden.setOrden_observacion(jsonOrden.getString("orden_observacion"));
                            ///////////////////////////////////orden////////////////////////////////

                            btnIrAGuardar.setVisibility(View.GONE);
                            Log.e(TAG, "orden estado "+orden.getOrden_estado()+"  orden_estado "+orden.getOrden_estado());


                            if (orden.getOrden_estado() == 0) {
                                txtEstatus.setText("ANULADA");
                                txtEstatus.setTextColor(getResources().getColor(R.color.black));
                                btnIrAnular.setVisibility(View.GONE);
                            } else if (orden.getOrden_estado() == 1) {
                                txtEstatus.setText("CREADA");
                                txtEstatus.setTextColor(getResources().getColor(R.color.black));
                            } else if (orden.getOrden_estado() == 2) {
                                txtEstatus.setText("GENERADA");
                                txtEstatus.setTextColor(getResources().getColor(R.color.blue));
                            } else if (orden.getOrden_estado() == 3) {
                                txtEstatus.setText("EN PROCESO");
                                txtEstatus.setTextColor(getResources().getColor(R.color.orange));
                                btnIrAGuardar.setVisibility(View.VISIBLE);
                            } else {
                                txtEstatus.setText("FINALIZADO");
                                txtEstatus.setTextColor(getResources().getColor(R.color.green));
                                btnIrAnular.setVisibility(View.GONE);

                                if(jsonOrden.has("orden_fecha_finalizacion"))
                                    txtFechaFinal.setText("" + sdf.format(jsonOrden.getString("orden_fecha_finalizacion")));
                            }

                            if (jsonOrden.has("responsables")) {
                                JSONArray arrayResponsables = jsonOrden.getJSONArray("responsables");
                                for (int j = 0; j < arrayResponsables.length(); j++) {
                                    JSONObject jsonResp = arrayResponsables.getJSONObject(j);
                                    JSONObject jsonTecnico = jsonResp.getJSONObject("tecnico");

                                    Log.e(TAG, "empleados "+jsonTecnico.getString("empleado"));
                                    if(!jsonTecnico.getString("empleado").equals("null")) {
                                        JSONObject jsonEmpleado = jsonTecnico.getJSONObject("empleado");

                                        txtTecnico.setText(txtTecnico.getText().toString()
                                                + " -"
                                                + jsonEmpleado.getString("empleado_nombre") + "\n");
                                    }
                                    else{
                                        txtTecnico.setText(txtTecnico.getText().toString()
                                                + " -"
                                                + jsonTecnico.getString("responsable_user_apellido")+" "
                                                + jsonTecnico.getString("responsable_user_nombre")+ "\n");
                                    }

                                    Log.e(TAG, "comparacion "+jsonTecnico.getInt("user_id")+"   "+Server.getUser_id(getContext()));
                                    if(j==0 && jsonTecnico.getInt("user_id")==Server.getUser_id(getContext()))
                                        btnIrAGuardar.setVisibility(View.VISIBLE);
                                }
                            }

                            if (jsonOrden.has("detalles")) {
                                JSONArray arrayDetalles = jsonOrden.getJSONArray("detalles");
                                for (int j = 0; j < arrayDetalles.length(); j++) {
                                    JSONObject jsonDet = arrayDetalles.getJSONObject(j);
                                    txtTipo.setText(txtTipo.getText().toString() + "   -"+jsonDet.getString("detalle_descripcion") + "\n");
                                }
                            }

                            if (jsonOrden.has("detalles_orden")) {
                                JSONArray arrayDetOrd = jsonOrden.getJSONArray("detalles_orden");
                                for (int j = 0; j < arrayDetOrd.length(); j++) {
                                    JSONObject jsonDetOrd = arrayDetOrd.getJSONObject(j);
                                    JSONObject jsonProd =jsonDetOrd.getJSONObject("producto");

                                    int cant=jsonDetOrd.getInt("detalle_orden_cantidad");
                                    int stock=jsonProd.getInt("producto_stock");

                                    int disponibles=0;

                                    if(stock>=cant)
                                        disponibles=cant;
                                    else
                                        disponibles=cant-stock;

                                    txtEquipo.setText(txtEquipo.getText().toString()
                                            +"("+cant+"/"+disponibles+") "
                                            +jsonProd.getString("producto_nombre")+"\n");
                                }
                            }

                            imgFirma.setVisibility(View.GONE);
                            if (jsonOrden.getString("orden_recibido_por") != null && !jsonOrden.getString("orden_recibido_por").equals("")) {
                                txtRecibidoPor.setText(jsonOrden.getString("orden_recibido_por"));
                                imgFirma.setVisibility(View.VISIBLE);

                                Glide.with(getContext())
                                        .load(Server.dns + "/DocumentosMantenimiento/1/" +orden_id+ "/firma_" + jsonOrden.getString("orden_recibido_por") + ".jpg")
                                        .into(imgFirma);
                            }

                            txtLogistica.setText(jsonOrden.getString("orden_logistica"));
                            txtAsignacion.setText(jsonOrden.getString("orden_asignacion"));
                            txtObservacion.setText(jsonOrden.getString("orden_observacion"));

                            if(jsonOrden.getInt("orden_prioridad")==1) {
                                txtPrioridad.setText("Normal");
                                llPrioridadColor.setBackgroundColor(getResources().getColor(R.color.green));
                            }
                            if(jsonOrden.getInt("orden_prioridad")==2) {
                                txtPrioridad.setText("Importante");
                                llPrioridadColor.setBackgroundColor(getResources().getColor(R.color.orange));
                            }
                            if(jsonOrden.getInt("orden_prioridad")==3) {
                                txtPrioridad.setText("Urgente");
                                llPrioridadColor.setBackgroundColor(getResources().getColor(R.color.red));
                            }

                            if(jsonOrden.getInt("orden_resultado")==2)
                                txtResultado.setText("Operativo");
                            else
                                txtResultado.setText("En Espera");

                            txtFechaFinal.setText("-");
                        } catch (JSONException | ParseException e) {
                            Log.e(TAG, "exception " + e.getMessage());
                            Toast.makeText(getActivity(), e.toString(), Toast.LENGTH_LONG).show();
                        }
                    }
                }, new Response.ErrorListener(){
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(getActivity(), error.toString(), Toast.LENGTH_LONG).show();
            }
        }){
            /*
            protected Map<String, String> getParams() throws AuthFailureError {
                HashMap<String, String> parametro = new HashMap<>();
                parametro.put("etiqueta", etiqueta);
                parametro.put("ciudad", ciudad);
                return parametro;
            }
             */
        };

        RequestQueue requestQueue = Volley.newRequestQueue(getActivity());
        requestQueue.add(stringRequest);
    }

    public void hideKeyboard () {
        View view = getActivity().getCurrentFocus();
        if (view != null) {
            InputMethodManager imm = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
        }
    }

    private void regresarAlaOrden(){
        Navigation.findNavController(getView()).navigate(R.id.nav_home);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}