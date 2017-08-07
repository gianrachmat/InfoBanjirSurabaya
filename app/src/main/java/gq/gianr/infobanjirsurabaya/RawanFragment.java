package gq.gianr.infobanjirsurabaya;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.MapsInitializer;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.Polygon;
import com.google.android.gms.maps.model.PolygonOptions;
import com.google.maps.android.geojson.GeoJsonLayer;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import gq.gianr.infobanjirsurabaya.model.Fuzzy;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

/**
 * Created by j on 11/03/2017.
 */

public class RawanFragment extends Fragment implements OnMapReadyCallback, AsyncGetFuzz.AsyncResponse {
    private GoogleMap map;
    MapView mapView;
    Polygon p;
    ArrayList<Fuzzy> data;
    boolean pts;
    LatLng[] points;


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
        LatLng sby = new LatLng(-7.2574719, 112.7520883);
        LatLngBounds bounds = LatLngBounds.builder().include(sby).build();
        map.moveCamera(CameraUpdateFactory.newLatLngBounds(bounds, getResources().getDimensionPixelSize(R.dimen.activity_horizontal_margin)));
        // mengambil data geometry dari api
        new AsyncGetFuzz(this).execute();
//        map.setLatLngBoundsForCameraTarget(bounds);
        map.setMaxZoomPreference(13f);
        map.setMinZoomPreference(11.3f);
        map.setOnPolygonClickListener(new GoogleMap.OnPolygonClickListener() {
            @Override
            public void onPolygonClick(Polygon polygon) {
                Toast.makeText(getContext(), data.get(Integer.parseInt(polygon.getTag().toString())).getKecamatan(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    // calculateMiddle untuk menghitung titik tengah dari northeast (kanan atas) dan southwest (kiri bawah)
    LatLngBounds calculateMiddle(LatLng ne, LatLng sw) {
        double latne = ne.latitude, lngne = ne.longitude, latsw = sw.latitude, lngsw = sw.longitude;
        double nlat = (((latne - latsw)) / 2) - 0.0001;
        double nlng = (((lngne - lngsw)) / 2) - 0.0001;
        LatLng ne2 = new LatLng(latne - nlat, lngne - nlng);
        LatLng sw2 = new LatLng(latsw + nlat, lngsw + nlng);
        return LatLngBounds.builder().include(ne2).include(sw2).build();
    }

//    @Override
//    public void processFinish(ArrayList<Banjir> output) {
//        if (output!=null) {
//            data = output;
//
//            for (int i = 0; i < output.size(); i++) {
//                points = GetPolygonPoints(output.get(i).getGeo());
//                System.out.println(output.get(i).getGeo());
//                if (pts) {
//                    new GetKelurahan(output.get(i)).execute(output.get(i).getKelurahan(),
//                            String.valueOf(points[0].latitude),
//                            String.valueOf(points[0].longitude),
//                            output.get(i).getLevel());
//                    System.out.println("points:"+points[0].toString());
//                } else {
//                    int v;
//                    switch (output.get(i).getLevel()) {
//                        case "tinggi":
//                            v = Color.RED;
//                            break;
//                        case "sedang":
//                            v = Color.YELLOW;
//                            break;
//                        default:
//                            v = Color.GREEN;
//                            break;
//                    }
//                    p = map.addPolygon(
//                            new PolygonOptions()
//                                    .add(points)
//                                    .strokeWidth(4)
//                                    .strokeColor(v));
//                }
//            }
//        } else {
//            Toast.makeText(getContext(), "Gagal", Toast.LENGTH_SHORT).show();
//        }
//    }

    @NonNull
    public LatLng[] GetPolygonPoints(String poligonWkt) {
        ArrayList<LatLng> points = new ArrayList<>();
        Pattern p = Pattern.compile("(-\\d*\\.\\d+)\\s(\\d*\\.\\d+)");
        Matcher m = p.matcher(poligonWkt);
        String point;
        pts = poligonWkt.toLowerCase().contains("point");

        int i = 0;
        while (m.find()) {
            point = poligonWkt.substring(m.start(), m.end());
            points.add(new LatLng(Double.parseDouble(m.group(1)), Double.parseDouble(m.group(2))));
            System.out.println("points.get(" + i + "):" + points.get(i));
            System.out.println("point:" + point);

            i++;
        }
        return points.toArray(new LatLng[points.size()]);
    }

//    @Override
//    public void processFinish(ArrayList<Kelurahan> output) {
//        if (output != null) {
//            data = output;
//            int j = 0;
//            for (int i = 0; i < output.size(); i++) {
//                points = GetPolygonPoints(output.get(i).getGeo());
//                System.out.println(output.get(i).getGeo());
//                int v;
//                switch (j) {
//                    case 0:
//                        v = Color.rgb(226,89,94);
//                        break;
//                    case 1:
//                        v = Color.rgb(255,115,27);
//                        break;
//                    default:
//                        v = Color.rgb(22,159,209);
//                        break;
//                }
//
//                if (j == 2) {
//                    j = 0;
//                } else j++;
//                p = map.addPolygon(
//                        new PolygonOptions()
//                                .add(points)
//                                .strokeWidth(4)
//                                .strokeColor(Color.CYAN)
//                                .fillColor(v)
//                );
//
//                }
//            }
//        } else {
//            Toast.makeText(getContext(), "Gagal", Toast.LENGTH_SHORT).show();
//        }
//    }

    @Override
    public void finFuzz(ArrayList<Fuzzy> output) {
        if (output != null) {
            data = output;
            int j = 0;
            for (int i = 0; i < output.size(); i++) {
                points = output.get(i).getGeo();
                System.out.println(output.get(i).getGeo()[0].toString());
                int v;
                switch (output.get(i).getHasil()) {
                    case "tinggi":
                        v = Color.argb(127,226,89,94);
                        break;
                    case "sedang":
                        v = Color.argb(127,255,115,27);
                        break;
                    case "rendah":
                        v = Color.argb(127,22,159,209);
                        break;
                    default:
                        v = Color.argb(70,0,0,0);
                        break;
                }

                if (j == 2) {
                    j = 0;
                } else j++;
                p = map.addPolygon(
                        new PolygonOptions()
                                .add(points)
                                .strokeWidth(4)
                                .strokeColor(Color.CYAN)
                                .fillColor(v)
                );
                p.setClickable(true);
                p.setTag(i);
            }
        } else {
            Toast.makeText(getContext(), "Gagal", Toast.LENGTH_SHORT).show();
        }
    }

    class CustomInfoWindow implements GoogleMap.InfoWindowAdapter {
        private final View v;
        private final Banjir banjir;
        private final String info;

        CustomInfoWindow(View v, Banjir banjir, String info) {
            this.v = getLayoutInflater(null).inflate(R.layout.info_window, null);
            this.banjir = banjir;
            this.info = info;
        }

        @Override
        public View getInfoWindow(Marker marker) {
            View s = v;

            TextView tvSnip = (TextView) s.findViewById(R.id.snippet);
            String body =
                    "Tipe banjir\t\t: " + banjir.getTipe() + "\n" +
                            "Jenis banjir\t\t: " + banjir.getJenis() + "\n" +
                            "Keterangan\t\t: " + banjir.getKronologi() + "\n" +
                            "Lama\t\t: " + banjir.getLama() + " menit\n" +
                            "Kedalaman\t\t: " + banjir.getKedalaman() + " cm\n" +
                            "Luas\t\t: " + banjir.getLuas() + "\n" +
                            "Level\t\t: " + banjir.getLevel() + "\n" +
                            "Rayon\t\t: " + banjir.getRayon() + "\n" +
                            "Tanggal\t\t: " + banjir.getTanggal() + "\n";
            tvSnip.setText(body);
            tvSnip = (TextView) s.findViewById(R.id.title);
            body = "Kelurahan:\t" + info;
            tvSnip.setText(body);

            return s;
        }

        @Override
        public View getInfoContents(Marker marker) {
            return null;
        }
    }

    private class GetKelurahan extends AsyncTask<String, Integer, ArrayList<Kelurahan>> {
        LatLng point;
        String level;
        Banjir banjir;

        public GetKelurahan(Banjir banjir) {
            this.banjir = banjir;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected void onPostExecute(ArrayList<Kelurahan> kelurahen) {
            super.onPostExecute(kelurahen);
            String info = "Kelurahan:\t" + kelurahen.get(0).getKelurahan();
            float v;
            switch (level) {
                case "tinggi":
                    v = BitmapDescriptorFactory.HUE_RED;
                    break;
                case "sedang":
                    v = BitmapDescriptorFactory.HUE_YELLOW;
                    break;
                default:
                    v = BitmapDescriptorFactory.HUE_GREEN;
                    break;
            }
            map.setInfoWindowAdapter(new CustomInfoWindow(getView(), banjir, info));
            map.addMarker(new MarkerOptions()
                    .position(point)
                    .title(info)
                    .icon(BitmapDescriptorFactory.defaultMarker(v))
            );
        }

        @Override
        protected ArrayList<Kelurahan> doInBackground(String... params) {
            ArrayList<Kelurahan> kel = new ArrayList<>();
            OkHttpClient client = new OkHttpClient();
            // ip address server
            Context applicationContext = MainActivity.getContextOfApplication();
            SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(applicationContext);
            System.out.println("packageName:" + applicationContext.getPackageName());

            String url = "http://" + sp.getString("ip_address", "192.168.43.167");
            Request request = new Request.Builder()
                    // url dari api yang mau diambil
                    .url(url + "/" + sp.getString("direktori", "flodizy") + "/backend/web/api/kelurahan/get-one?id=" + params[0])
                    .build();
            String response;

            point = new LatLng(Double.valueOf(params[1]), Double.valueOf(params[2]));
            level = params[3];

            try {
                Response respon = client.newCall(request).execute();
                response = respon.body().string();
                System.out.println("response:" + response);
                JSONArray array = new JSONArray(response);
                for (int i = 0; i < array.length(); i++) {
                    Kelurahan a = new Kelurahan();
                    a.setId(array.getJSONObject(i).getInt("id"));

                    a.setKecamatan(array.getJSONObject(i).getString("kecamatan"));
                    a.setKelurahan(array.getJSONObject(i).getString("kelurahan"));
                    a.setGeo(array.getJSONObject(i).getString("geometry"));
                    kel.add(a);
                }
                System.out.println(kel.toString());
                return kel;
            } catch (JSONException | IOException e) {
                e.printStackTrace();
            }
            return null;
        }
    }
}
