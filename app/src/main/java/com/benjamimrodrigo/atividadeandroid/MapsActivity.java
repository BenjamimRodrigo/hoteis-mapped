package com.benjamimrodrigo.atividadeandroid;

import android.os.Bundle;
import android.util.Log;
import com.benjamimrodrigo.atividadeandroid.beans.Hotel;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import java.util.ArrayList;
import static com.benjamimrodrigo.atividadeandroid.HoteisRestAsync.dialog;
import androidx.fragment.app.FragmentActivity;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback {

    private GoogleMap mMap;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        ArrayList<Hotel> hoteis = (ArrayList<Hotel>) getIntent().getSerializableExtra("hoteis");
        LatLng local;
        if (hoteis != null) {
            if (hoteis.size() > 0) {
                for (Hotel hotel : hoteis) {
                    local = new LatLng(hotel.getLatitude(), hotel.getLongitude());
                    mMap.addMarker(new MarkerOptions().position(local).title(hotel.getNome()));
                    mMap.moveCamera(CameraUpdateFactory.newLatLng(local));
                }

                if (dialog.isShowing()) {
                    dialog.dismiss();
                }

                mMap.animateCamera(CameraUpdateFactory.zoomIn());
                mMap.animateCamera(CameraUpdateFactory.zoomTo(13), 1000, null);
            }
        } else {
            Log.e("BENJAMIM", "Hoteis não encontrados!");
        }
    }
}
