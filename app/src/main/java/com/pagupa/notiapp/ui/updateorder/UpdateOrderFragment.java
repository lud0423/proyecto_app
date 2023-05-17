package com.pagupa.notiapp.ui.updateorder;

import static android.app.Activity.RESULT_OK;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.Image;
import android.os.Bundle;
import android.util.Base64;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.pagupa.notiapp.CaptureSignature;
import com.pagupa.notiapp.R;
import com.pagupa.notiapp.clases.Server;
import com.pagupa.notiapp.databinding.FragmentOrderUpdateBinding;
import com.pagupa.notiapp.model.Orden;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class UpdateOrderFragment extends Fragment {
    private FragmentOrderUpdateBinding binding;
    private String TAG="DOF";

    private TextView txtOrden, txtFechaInicio, txtCliente, txtLugar, txtPrioridad, txtFirmadoPor;
    private Spinner spResultado;
    private EditText txtObservacion;
    private RadioButton rdStatusPendiente, rdStatusFinalizado;
    private Button btnGuardar, btnGetSignature;
    private LinearLayout llPriorityColor, llFirma;
    private ImageView imgFirma;
    public static final int SIGNATURE_ACTIVITY = 1;
    private String url_firma, persona_firma;

    private DatabaseReference mDatabase;

    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentOrderUpdateBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        txtOrden=binding.idUpdateOrderNumber;
        txtFechaInicio=binding.idUpdateOrderDateInit;
        txtCliente=binding.idUpdateOrderClient;
        txtLugar=binding.idUpdateOrderPlace;
        txtFirmadoPor=binding.idUpdateOrderReceiveBy;
        llFirma=binding.idUpdateOrderll;
        imgFirma=binding.idUpdateOrdenSignature;

        spResultado=binding.idUpdateResult;
        txtPrioridad=binding.idUpdateOrderPriority;
        llPriorityColor= binding.idUpdateOrderPriorityColor;

        txtObservacion=binding.idUpdateOrderObservation;
        rdStatusPendiente=binding.idUpdateStatePendiente;
        rdStatusFinalizado=binding.idUpdateStateFinalizado;
        btnGuardar=binding.idUpdateSave;
        btnGetSignature=binding.idUpdateSaveGetSignature;

        Log.e(TAG, "cargando fragment update");
        Orden orden =(Orden)getArguments().get("orden");

        if (orden != null) {
            txtOrden.setText(""+orden.getOrden_id());

            SimpleDateFormat sdf=new SimpleDateFormat("dd/MM/yyyy");
            txtFechaInicio.setText(sdf.format(orden.getOrden_fecha_inicio()));

            txtCliente.setText(orden.getCliente_nombre());
            txtLugar.setText(orden.getOrden_lugar());
            txtObservacion.setText(orden.getOrden_observacion());

            if(orden.getOrden_prioridad()==1){
                txtPrioridad.setText("Normal");
                llPriorityColor.setBackgroundColor(getResources().getColor(R.color.green));
            }
            if(orden.getOrden_prioridad()==2){
                txtPrioridad.setText("Importante");
                llPriorityColor.setBackgroundColor(getResources().getColor(R.color.orange));
            }
            if(orden.getOrden_prioridad()==3){
                txtPrioridad.setText("Urgente");
                llPriorityColor.setBackgroundColor(getResources().getColor(R.color.red));
            }

            Log.e(TAG, "estado de la orden "+orden.getOrden_estado());

            if(orden.getOrden_resultado()==1)
                spResultado.setSelection(0);
            else
                spResultado.setSelection(1);

            if(orden.getOrden_estado()>=1) {
                btnGuardar.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        guardarOrden(orden.getOrden_id());
                    }
                });

                btnGetSignature.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        url_firma="";
                        persona_firma="";
                        Intent intent = new Intent(getActivity(), CaptureSignature.class);
                        activityResultLauncher.launch(intent);

                        Log.e(TAG," pase por abrir firma");
                        //getActivity().registerForActivityResult(intent, someActivityResultLauncher);
                    }
                });
            }

            Log.e(TAG, "estatus update "+orden.getOrden_estatus());
            Log.e(TAG, "resultado update "+orden.getOrden_resultado());

            if(orden.getOrden_estatus()==0){
                rdStatusPendiente.setChecked(true);
                rdStatusFinalizado.setChecked(false);
            }
            else{
                rdStatusPendiente.setChecked(false);
                rdStatusFinalizado.setChecked(true);
            }

            if(orden.getOrden_resultado()==1)
                spResultado.setSelection(0);
            else
                spResultado.setSelection(1);
        }
        return root;
    }



    ActivityResultLauncher<Intent> activityResultLauncher = registerForActivityResult(
    new ActivityResultContracts.StartActivityForResult(),
    new ActivityResultCallback<ActivityResult>() {
        @Override
        public void onActivityResult(ActivityResult result) {
            if (result.getResultCode() == Activity.RESULT_OK) {

                Intent data = result.getData();
                Bundle bundle = data.getExtras();
                String status  = bundle.getString("status");
                Log.e(TAG, "resultado: "+bundle.getString("path")+"  url: "+url_firma);

                if(status.equalsIgnoreCase("done")){
                    //Toast toast = Toast.makeText(getActivity(), "Capturada Correctamente en "+bundle.getString("path"), Toast.LENGTH_SHORT);
                    //toast.setGravity(Gravity.TOP, 105, 50);
                    //toast.show();

                    url_firma=bundle.getString("path");
                    persona_firma=bundle.getString("persona");
                    llFirma.setVisibility(View.VISIBLE);
                    txtFirmadoPor.setText(""+persona_firma);

                    File imgFile = new  File(url_firma);
                    if(imgFile.exists()){
                        Bitmap myBitmap = BitmapFactory.decodeFile(imgFile.getAbsolutePath());

                        imgFirma.setImageBitmap(myBitmap);
                    }

                    //convert the string to byte array
                /*
                byte[] imageAsBytes = Base64.decode(myStringImage.getBytes());
                ImageView image = (ImageView)this.findViewById(R.id.ImageView);
                image.setImageBitmap(
                        BitmapFactory.decodeByteArray(imageAsBytes, 0, imageAsBytes.length)
                );*/
                }
            }
        }
    });

    private void guardarOrden(int orden_id){
        StringRequest stringRequest = new StringRequest(Request.Method.POST, Server.dns+"/mantenimiento",
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        JSONObject jsonObject = null;
                        try {
                            jsonObject = new JSONObject(response);
                            if(jsonObject.getString("result").equals("OK")) {
                                mDatabase= FirebaseDatabase.getInstance().getReference();

                                JSONObject ordenResult = new JSONObject(jsonObject.getString("data"));

                                Map<String, Object> registro = new HashMap<>();
                                registro.put("orden_id", orden_id);
                                registro.put("fecha", ordenResult.getString("orden_fecha_inicio"));

                                mDatabase.child("ordenes").setValue(registro);

                                Toast.makeText(getActivity(), "Orden Actualizada Correctamente", Toast.LENGTH_SHORT).show();
                                regresarAlaOrden();
                            }
                            else {
                                Toast.makeText(getActivity(), jsonObject.getString("mensaje"), Toast.LENGTH_SHORT).show();
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
                Toast.makeText(getActivity(), error.toString(), Toast.LENGTH_LONG).show();
                Log.e(TAG, "error 2"+error.toString());
            }
        }){
            protected Map<String, String> getParams() throws AuthFailureError {
                HashMap<String, String> parametro = new HashMap<>();
                parametro.put("orden_id", Integer.toString(orden_id));
                parametro.put("observacion", txtObservacion.getText().toString());
                parametro.put("resultado", spResultado.getSelectedItem().toString());

                if(url_firma!=null && url_firma!="") {
                    Bitmap bm = BitmapFactory.decodeFile(url_firma);
                    ByteArrayOutputStream baos = new ByteArrayOutputStream();
                    bm.compress(Bitmap.CompressFormat.JPEG, 100, baos);
                    byte[] b = baos.toByteArray();
                    String imageB64 = Base64.encodeToString(b, Base64.DEFAULT);

                    parametro.put("imagen", imageB64);
                    parametro.put("recibido_por", persona_firma);
                }

                if(rdStatusFinalizado.isChecked())
                    parametro.put("estatus", "2");
                else
                    parametro.put("estatus", "1");

                Log.e(TAG, parametro.toString());
                return parametro;
            }
        };

        RequestQueue requestQueue = Volley.newRequestQueue(getActivity());
        requestQueue.add(stringRequest);
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