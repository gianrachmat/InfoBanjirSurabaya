package gq.gianr.infobanjirsurabaya;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.MapsInitializer;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

/**
 * Created by j on 11/03/2017.
 */

public class RawanFragment extends Fragment {
    private GoogleMap map;
    MapView mapView;
    public RawanFragment() {
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.maps_fragment, container, false);

        mapView = (MapView) view.findViewById(R.id.map);
        mapView.onCreate(savedInstanceState);

        mapView.onResume();

        try {
            MapsInitializer.initialize(getActivity().getApplicationContext());
        } catch (Exception e) {
            e.printStackTrace();
        }

        mapView.getMapAsync(new OnMapReadyCallback() {
            @Override
            public void onMapReady(GoogleMap googleMap) {
                map = googleMap;

                LatLng sby = new LatLng(-34, 151);
                map.addMarker(new MarkerOptions()
                        .position(sby)
                        .title("Marker")
                );

                CameraPosition cameraPosition = new CameraPosition.Builder()
                        .target(sby)
                        .zoom(12)
                        .build();
                map.moveCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));
            }
        });
        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        mapView.onResume();
    }

    @Override
    public void onPause() {
        super.onPause();
        mapView.onPause();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mapView.onDestroy();
    }

    /*@Override
    public void onMapReady(GoogleMap googleMap) {
        map = googleMap;
        Log.d("Map", "onMapReady");
        LatLng sby = new LatLng(-34, 151);
        map.addMarker(new MarkerOptions().position(sby).title("Marker")).showInfoWindow();
        map.animateCamera(CameraUpdateFactory.newLatLngZoom(sby, 17));
    }*/
}
