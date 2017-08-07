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
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

import static android.content.Context.MODE_PRIVATE;

/**
 * Created by j on 09/05/2017.
 *
 */

public class AsyncGet extends AsyncTask<String,String,ArrayList<Kelurahan>> {
    public AsyncResponse delegate = null;

    public interface AsyncResponse{
        void processFinish(ArrayList<Kelurahan> output);
    }

    public AsyncGet(AsyncResponse delegate){
        this.delegate = delegate;
    }

    @Override
    protected ArrayList<Kelurahan> doInBackground(String... params) {
        ArrayList<Kelurahan> kec = new ArrayList<>();
        OkHttpClient client = new OkHttpClient();
		// ip address server
        Context applicationContext = MainActivity.getContextOfApplication();
        SharedPreferences sp =  PreferenceManager.getDefaultSharedPreferences(applicationContext);
        System.out.println("packageName:"+applicationContext.getPackageName());

//        if (!sp.getString("ip_address", "kosong").equals("kosong")) {
            String url = "http://"+sp.getString("ip_address", "gian.it.student.pens.ac.id");
            Request request = new Request.Builder()
                    // url dari api yang mau diambil
                    .url(url + "/"+sp.getString("direktori", "floodizy")+"/backend/web/api/kelurahan/get-all")
                    .build();
            String response;

            try {
                Response respon = client.newCall(request).execute();
                response = respon.body().string();
                JSONArray array = new JSONArray(response);
                for (int i = 0; i < array.length(); i++) {
                    Kelurahan a = new Kelurahan();
//                    a.setId(array.getJSONObject(i).getInt("id"));
//                    a.setTipe(array.getJSONObject(i).getString("tipe_banjir"));
//                    a.setJenis(array.getJSONObject(i).getString("jenis_banjir"));
//                    a.setKecamatan(array.getJSONObject(i).getString("kecamatan"));
//                    a.setKelurahan(array.getJSONObject(i).getString("kelurahan"));
//                    a.setKorban(array.getJSONObject(i).getString("korban_jiwa"));
//                    a.setFoto(array.getJSONObject(i).getString("foto"));
//                    a.setLama(array.getJSONObject(i).getString("lama"));
//                    a.setKedalaman(array.getJSONObject(i).getString("kedalaman"));
//                    a.setGeo(array.getJSONObject(i).getString("geometry"));
//                    a.setRayon(array.getJSONObject(i).getString("rayon"));
//                    a.setRayon(array.getJSONObject(i).getString("rayon"));
//                    a.setTanggal(array.getJSONObject(i).getString("tanggal"));
//                    a.setLuas(array.getJSONObject(i).getString("luas"));
//                    a.setLevel(array.getJSONObject(i).getString("level"));
//                    a.setBulan(array.getJSONObject(i).getString("bulan"));
//                    a.setTahun(array.getJSONObject(i).getInt("tahun"));
                    a.setId(array.getJSONObject(i).getInt("id"));
                    a.setKelurahan(array.getJSONObject(i).getString("kelurahan"));
                    a.setKecamatan(array.getJSONObject(i).getString("kecamatan"));
                    a.setGeo(array.getJSONObject(i).getString("geometry"));
                    kec.add(a);
                }
                System.out.println(kec.toString());
                return kec;
            } catch (JSONException | IOException e) {
                e.printStackTrace();
            }
        return null;
    }

    @Override
    protected void onPostExecute(ArrayList<Kelurahan> kelurahen) {
        delegate.processFinish(kelurahen);
    }
}
