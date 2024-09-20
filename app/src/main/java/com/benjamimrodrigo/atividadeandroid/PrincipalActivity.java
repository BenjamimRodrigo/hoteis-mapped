package com.benjamimrodrigo.atividadeandroid;

import static android.os.Build.VERSION.SDK_INT;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.StrictMode;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.benjamimrodrigo.atividadeandroid.beans.Hotel;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public class PrincipalActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_principal);

        Button consultar = (Button) findViewById(R.id.btnConsultar);
        consultar.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        new HoteisRestAsync(PrincipalActivity.this).execute();
                    }
                });

    }
}

class HoteisRestAsync extends AsyncTask<String, String, String> {

    public static ProgressDialog dialog;
    ArrayList<Hotel> hoteis;
    Activity activity;

    String url;

    public HoteisRestAsync(Activity activity) {
        this.activity = activity;
        this.dialog = new ProgressDialog(activity, R.style.MyTheme);
        url = ((EditText) this.activity.findViewById(R.id.uri)).getText().toString();
    }

    @Override
    protected void onPreExecute() {
        this.dialog.setMessage("Conectando ao servidor...");
        this.dialog.setTitle("Aguarde!");
        this.dialog.setIndeterminate(true);
        this.dialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);

        this.dialog.setCancelable(false);

        this.dialog.show();
        super.onPreExecute();
    }

    @Override
    protected String doInBackground(String... strings) {

        HttpURLConnection conn;

        try {
            conn = (HttpURLConnection) new URL(url).openConnection();
            conn.setReadTimeout(10001);
            conn.setConnectTimeout(10001);
            conn.setDoInput(true);
            conn.setDoOutput(false);
            conn.setRequestMethod("GET");
            if (SDK_INT > 8)
            {
                StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
                StrictMode.setThreadPolicy(policy);
            }

//            Thread.sleep(3000);

            if (conn.getResponseCode() == HttpURLConnection.HTTP_OK) {
                JSONObject objJSON = new JSONObject(getStrFromStream(conn.getInputStream()));
                hoteis = new ArrayList();
                Hotel hotel;
                JSONObject result = objJSON.getJSONObject("result");
                JSONArray array = result.getJSONArray("records");
                for (int i = 0; i < array.length(); i++) {
                    hotel = new Hotel();
                    if (hotel.parseJSON(array.getJSONObject(i))) {
                        hoteis.add(hotel);
                    }
                }
            }
        } catch (Exception e) {
            Log.i("BENJAMIM", "Erro", e);
        }
        return null;
    }

    @Override
    protected void onPostExecute(String string) {
        if (this.hoteis != null) {
            Intent intent = new Intent(this.activity, MapsActivity.class);
            intent.putExtra("hoteis", this.hoteis);
            this.activity.startActivity(intent);

        } else {
            Toast.makeText(this.activity, "Falha na requisição!", Toast.LENGTH_LONG).show();
            if (dialog.isShowing()){
                dialog.dismiss();
            }
        }
    }

    public static String getStrFromStream(InputStream inputStream) {
        StringBuilder total = new StringBuilder();
        try {
            BufferedReader r = new BufferedReader(new InputStreamReader(inputStream));
            String line;
            while ((line = r.readLine()) != null) {
                total.append(line).append('\n');
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return total.toString();
    }
}