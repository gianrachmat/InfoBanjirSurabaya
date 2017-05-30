package gq.gianr.infobanjirsurabaya;

import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.MapsInitializer;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.Polygon;
import com.google.android.gms.maps.model.PolygonOptions;
import com.google.maps.android.geojson.GeoJsonLayer;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by j on 11/03/2017.
 */

public class RawanFragment extends Fragment implements OnMapReadyCallback, AsyncGet.AsyncResponse{
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
        mapView.getMapAsync(this);
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

    @Override
    public void onMapReady(GoogleMap googleMap) {
        map = googleMap;
        Log.d("Map", "onMapReady");
//        LatLng sby = new LatLng(-7.2630131, 112.7211714);
        LatLngBounds bounds = LatLngBounds.builder().include(new LatLng(-7.1943499, 112.8164465)).include(new LatLng(-7.356889, 112.6069223)).build();
//        map.addMarker(new MarkerOptions().position(sby).title("Sawahan")).showInfoWindow();
//        map.addMarker(new MarkerOptions().position(new LatLng(-7.292509, 112.708604)).title("Northeast")).showInfoWindow();
//        map.addMarker(new MarkerOptions().position(new LatLng(-7.255102099999999, 112.7343)).title("Southwest")).showInfoWindow();
        map.moveCamera(CameraUpdateFactory.newLatLngBounds(bounds, getResources().getDimensionPixelSize(R.dimen.activity_horizontal_margin)));
		// mengambil data geometry dari api
        new AsyncGet(this).execute();
		// agar kamera tidak berpindah (latlong bound)
		// lat long yang dipakai dari kecamatan sawahan
		// url mengambil bound
		// https://maps.googleapis.com/maps/api/geocode/json?&address=sawahan
		// ambil yg lat long dari id bounds
        map.setLatLngBoundsForCameraTarget(calculateMiddle(new LatLng(-7.292509, 112.708604), new LatLng(-7.255102099999999, 112.7343)));
        map.setMaxZoomPreference(13f);
        map.setMinZoomPreference(11.2f);
    }

	// calculateMiddle untuk menghitung titik tengah dari northeast (kanan atas) dan southwest (kiri bawah)
    LatLngBounds calculateMiddle(LatLng ne, LatLng sw){
        double latne = ne.latitude, lngne = ne.longitude, latsw = sw.latitude, lngsw = sw.longitude;
        double nlat = (((latne - latsw))/2)-0.0001;
        double nlng = (((lngne - lngsw))/2)-0.0001;
        LatLng ne2 = new LatLng(latne - nlat, lngne - nlng);
        LatLng sw2 = new LatLng(latsw + nlat, lngsw + nlng);
        map.addMarker(new MarkerOptions().position(ne2).title("Northeast")).showInfoWindow();
        map.addMarker(new MarkerOptions().position(sw2).title("Southwest")).showInfoWindow();
        return LatLngBounds.builder().include(ne2).include(sw2).build();
    }

    @Override
    public void processFinish(ArrayList<Kecamatan> output) {
        LatLng[] points;
        if (output!=null) {
            for (int i = 0; i < output.size(); i++) {
                points = GetPolygonPoints(output.get(i).getGeo());
                System.out.println(output.get(i).getGeo());
                Polygon p = map.addPolygon(
                        new PolygonOptions()
                                .add(points)
                                .strokeWidth(4)
                                .strokeColor(Color.RED));
//            p.setFillColor(Color.GRAY);
            }
        } else {
            Toast.makeText(getContext(), "Gagal", Toast.LENGTH_SHORT).show();
        }
    }

    public static LatLng[] GetPolygonPoints(String poligonWkt){
        ArrayList<LatLng> points = new ArrayList<>();
        Pattern p = Pattern.compile("(-\\d*\\.\\d+)\\s(\\d*\\.\\d+)");
        Matcher m = p.matcher(poligonWkt);
        String point;

        int i=0;
        while (m.find()){
            point =  poligonWkt.substring(m.start(), m.end());
            points.add(new LatLng(Double.parseDouble(m.group(1)), Double.parseDouble(m.group(2))));
//            System.out.println("points.get("+i+"):"+points.get(i));
//            System.out.println("point:"+point);
            i++;
        }
        return points.toArray(new LatLng[points.size()]);
    }
}
