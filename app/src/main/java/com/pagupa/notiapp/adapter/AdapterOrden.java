package com.pagupa.notiapp.adapter;


import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Base64;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.firebase.database.FirebaseDatabase;
import com.pagupa.notiapp.R;
import com.pagupa.notiapp.clases.Server;
import com.pagupa.notiapp.model.Orden;
import com.pagupa.notiapp.ui.detailorder.DetailOrderFragment;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class AdapterOrden extends RecyclerView.Adapter<AdapterOrden.OrdenViewHolder> {
    Context context;
    List<Orden> OrdenesList =new ArrayList<>();

    public AdapterOrden(Context c){
        this.context=c;
    }

    public void addOrden(Orden o){
        OrdenesList.add(o);
        notifyItemInserted(OrdenesList.size());
    }

    public void clear(){
        OrdenesList.clear();
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public AdapterOrden.OrdenViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.adapter_order, viewGroup, false);
        return new AdapterOrden.OrdenViewHolder(v);
    }

    public void onBindViewHolder(@NonNull AdapterOrden.OrdenViewHolder holder, int i) {
        Orden o = OrdenesList.get(i);

        SimpleDateFormat sdf=new SimpleDateFormat("dd/MM/yyyy");
        //String fecha=o.getOrden_fecha_inicio().toString();

        //Log.e("ADAPTER ORDEN", "FECHA: "+fecha);

        holder.txtOrden.setText(""+o.getOrden_id());
        holder.txtfecha.setText(sdf.format(o.getOrden_fecha_inicio())+"   ");
        holder.txtCliente.setText(o.getCliente_nombre());
        holder.txtLugar.setText(o.getOrden_lugar());
        holder.txtAsignacion.setText(o.getOrden_asignacion());
        holder.txtAsignacion.setKeyListener(null);

        if(o.getOrden_prioridad()==1)  holder.llPrioridad.setBackgroundColor(context.getResources().getColor(R.color.green));
        if(o.getOrden_prioridad()==2)  holder.llPrioridad.setBackgroundColor(context.getResources().getColor(R.color.orange));
        if(o.getOrden_prioridad()==3)  holder.llPrioridad.setBackgroundColor(context.getResources().getColor(R.color.red));

        holder.btnActivar.setVisibility(View.GONE);

        if(o.getOrden_estado()==0)holder.txtEstado.setText("  ANULADO  ");
        if(o.getOrden_estado()==1)holder.txtEstado.setText("  CREADA  ");
        if(o.getOrden_estado()==2){
            holder.txtEstado.setText("  GENERADA  ");

            //if(o.getResponsable)
            holder.btnActivar.setVisibility(View.VISIBLE);
        }
        if(o.getOrden_estado()==3)holder.txtEstado.setText("  EN PROCESO  ");
        if(o.getOrden_estado()==4)holder.txtEstado.setText("  FINALIZADO  ");

        Log.e("adapter", "prioridad "+o.getOrden_prioridad());

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Bundle b=new Bundle();
                b.putSerializable("orden", o);
                Navigation.findNavController(view).navigate(R.id.nav_order_detail, b);
                Log.e("adapter", " itenview");
            }
        });

        holder.btnActivar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder builder = new AlertDialog.Builder(context);

                builder.setMessage("Deseas Cambiar la Orden de Mantenimiento °"+o.getOrden_id()+" a \nEN PROCESO")
                        .setTitle("Confirmación del Usuario");

                builder.setPositiveButton("Activar", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        StringRequest stringRequest = new StringRequest(Request.Method.POST, Server.dns+"/activarOrdenMantenimiento",
                                new Response.Listener<String>() {
                                    @Override
                                    public void onResponse(String response) {
                                        JSONObject jsonObject = null;
                                        try {
                                            jsonObject = new JSONObject(response);
                                            if(jsonObject.getString("result").equals("OK")) {
                                                Toast.makeText(context, "Orden Actualizada Correctamente", Toast.LENGTH_SHORT).show();
                                            }
                                            else {
                                                Toast.makeText(context, jsonObject.getString("mensaje"), Toast.LENGTH_SHORT).show();
                                                Log.e("Adapter", "error 0" + jsonObject.getString("mensaje"));
                                            }
                                        } catch (JSONException e) {
                                            Toast.makeText(context, e.toString(), Toast.LENGTH_LONG).show();
                                            Log.e("Adapter", "error 1" + e.toString());
                                        }
                                    }
                                }, new Response.ErrorListener(){
                            @Override
                            public void onErrorResponse(VolleyError error) {
                                Toast.makeText(context, error.toString(), Toast.LENGTH_LONG).show();
                                Log.e("Adapter", "error 2"+error.toString());
                            }
                        }){
                            protected Map<String, String> getParams() throws AuthFailureError {
                                HashMap<String, String> parametro = new HashMap<>();
                                parametro.put("orden_id", ""+o.getOrden_id());
                                return parametro;
                            }
                        };

                        RequestQueue requestQueue = Volley.newRequestQueue(context);
                        requestQueue.add(stringRequest);
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
    }

    public int getItemCount() {
        return OrdenesList.size();
    }

    public class OrdenViewHolder extends RecyclerView.ViewHolder {
        Context context;
        TextView txtOrden, txtfecha, txtCliente, txtLugar, txtAsignacion, txtEstado;
        CardView cardView;
        LinearLayout layout, llPrioridad, llEstado;
        ImageView btnActivar;

        public OrdenViewHolder(View itemView) {
            super(itemView);

            context = itemView.getContext();
            cardView=itemView.findViewById(R.id.idAdapterCardView);
            txtOrden = itemView.findViewById(R.id.idAdapterOrderSequence);
            txtfecha = itemView.findViewById(R.id.idAdapterOrderDate);
            txtCliente = itemView.findViewById(R.id.idAdapterOrderClient);
            txtLugar = itemView.findViewById(R.id.idAdapterOrderPlace);
            txtAsignacion = itemView.findViewById(R.id.idAdapterOrderAssignment);
            layout=itemView.findViewById(R.id.idAdapterLayout);
            llPrioridad=itemView.findViewById(R.id.idAdapterLayputPriorityColor);
            llEstado=itemView.findViewById(R.id.idAdapterStateColor);
            txtEstado=itemView.findViewById(R.id.idAdapterStateText);
            btnActivar=itemView.findViewById(R.id.idAdapterBtnRunOrden);
        }
    }
}
