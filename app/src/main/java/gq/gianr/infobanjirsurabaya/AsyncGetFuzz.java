package gq.gianr.infobanjirsurabaya;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.preference.PreferenceManager;

import com.google.android.gms.maps.model.LatLng;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;

import gq.gianr.infobanjirsurabaya.model.Fuzzy;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

/**
 * Created by j on 19/07/2017.
 */

public class AsyncGetFuzz extends AsyncTask<String, String, ArrayList<Fuzzy>> {
    AsyncResponse delegate = null;

    public AsyncGetFuzz(AsyncResponse delegate) {
        this.delegate = delegate;
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
    }

    @Override
    protected ArrayList<Fuzzy> doInBackground(String... params) {
        ArrayList<Fuzzy> fuzzies = new ArrayList<>();
        OkHttpClient client = new OkHttpClient();
        // ip address server
        Context applicationContext = MainActivity.getContextOfApplication();
        SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(applicationContext);
        System.out.println("packageName:" + applicationContext.getPackageName());

//        if (!sp.getString("ip_address", "kosong").equals("kosong")) {
        String url = "http://" + sp.getString("ip_address", "gian.it.student.pens.ac.id");
        Request request = new Request.Builder()
                // url dari api yang mau diambil
                .url(url + "/" + sp.getString("direktori", "floodizy") + "/frontend/web/maps/fuzzy")
                .build();
        String response;

        try {
            Response respon = client.newCall(request).execute();
            response = respon.body().string();
            JSONArray array = new JSONArray(response);
            for (int i = 0; i < array.length(); i++) {
                JSONObject object = array.getJSONObject(i);
                JSONArray temp = object.getJSONArray("geometry").getJSONArray(0);
                LatLng[] latlng = new LatLng[temp.length()];
                for (int j = 0; j < temp.length(); j++) {
                    latlng[j] = new LatLng(temp.getJSONArray(j).getDouble(0), temp.getJSONArray(j).getDouble(1));

                }
                Fuzzy a = new Fuzzy();

                a.setKecamatan_id(object.getInt("kecamatan_id"));
                a.setKecamatan(object.getString("kecamatan"));
                a.setBanjir(fill(object, "banjir"));
                a.setChj(fill(object, "curah_hujan"));
                a.setKp(fill(object, "kepadatan_penduduk"));
                a.setDra(fill(object, "drainase"));
                a.setHasil(object.getString("hasil"));
                a.setGeo(latlng);
                fuzzies.add(a);
            }
            System.out.println("fuzzies.toString(): "+fuzzies.toString());
            return fuzzies;
        } catch (JSONException | IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    private Fuzzy.NilaiFuzzy fill(JSONObject object, String tipe) throws JSONException {
        Fuzzy.NilaiFuzzy n = new Fuzzy.NilaiFuzzy();
        n.setNilai(object.getJSONObject(tipe).getDouble("nilai"));
        n.setNr(object.getJSONObject(tipe).getDouble("nilai_rendah"));
        n.setNs(object.getJSONObject(tipe).getDouble("nilai_sedang"));
        n.setNt(object.getJSONObject(tipe).getDouble("nilai_tinggi"));
        n.setKri(object.getJSONObject(tipe).getString("kriteria"));
        return n;
    }

    @Override
    protected void onPostExecute(ArrayList<Fuzzy> fuzzies) {
        delegate.finFuzz(fuzzies);
    }

    public interface AsyncResponse {
        void finFuzz(ArrayList<Fuzzy> output);
    }
}
