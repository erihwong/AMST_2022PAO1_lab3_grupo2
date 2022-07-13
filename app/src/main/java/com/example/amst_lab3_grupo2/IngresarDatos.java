package com.example.amst_lab3_grupo2;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class IngresarDatos extends AppCompatActivity {
    String token = "";
    Button btn_envio;
    private RequestQueue mQueue;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ingresar_datos);

        mQueue = Volley.newRequestQueue(this);
        Intent menu = getIntent();
        this.token = (String)menu.getExtras().get("token");
        btn_envio = (Button) findViewById(R.id.btn_enviarDatos);
        btn_envio.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                enviarDatos();
            }
        });

    }
    public void enviarDatos (){
        final EditText tempValue = (EditText) findViewById(R.id.tempValor);
        final EditText humedadValue = (EditText) findViewById(R.id.humedadValor);
        final EditText pesoValue = (EditText) findViewById(R.id.pesoValor);
        String temperatura = tempValue.getText().toString();
        String humedad = humedadValue.getText().toString();
        String peso = pesoValue.getText().toString();
        Map<String,String> params = new HashMap<String,String>();
        params.put("temperatura",temperatura);
        params.put("humedad",humedad);
        params.put("peso",peso);
        JSONObject parametros = new JSONObject(params);
        System.out.println(parametros);
        String post_url = "https://amst-labx.herokuapp.com/api/sensores";
        JsonObjectRequest request = new JsonObjectRequest(Request.Method.POST, post_url, parametros, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                System.out.println("Este es mi response"+response);
                try {
                    Toast toast = Toast.makeText(getApplicationContext(),"Se ha realizado el POST con exito",Toast.LENGTH_SHORT);
                    toast.show();
                } catch (Exception e) {
                    e.printStackTrace();
                    System.out.println("Algo salio mal aqui");
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                AlertDialog alertDialog = new AlertDialog.Builder(IngresarDatos.this).create();
                alertDialog.setTitle("Alerta");
                alertDialog.setMessage("Error al enviar los datos");
                alertDialog.setButton(AlertDialog.BUTTON_NEUTRAL, "OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });
                alertDialog.show();
            }
        }){
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String,String> params = new HashMap<String,String>();
                params.put("Authorization","JWT "+token);
                System.out.println(token);
                return params;
            }
        };
        mQueue.add(request);

    }
}