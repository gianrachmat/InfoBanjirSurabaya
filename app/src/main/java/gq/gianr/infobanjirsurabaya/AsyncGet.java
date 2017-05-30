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

public class AsyncGet extends AsyncTask<String,String,ArrayList<Kecamatan>> {
    public AsyncResponse delegate = null;

    public interface AsyncResponse{
        void processFinish(ArrayList<Kecamatan> output);
    }

    public AsyncGet(AsyncResponse delegate){
        this.delegate = delegate;
    }

    @Override
    protected ArrayList<Kecamatan> doInBackground(String... params) {
        ArrayList<Kecamatan> kec = new ArrayList<>();
        OkHttpClient client = new OkHttpClient();
		// ip address server
        Context applicationContext = MainActivity.getContextOfApplication();
        SharedPreferences sp =  PreferenceManager.getDefaultSharedPreferences(applicationContext);
        System.out.println("packageName:"+applicationContext.getPackageName());

//        if (!sp.getString("ip_address", "kosong").equals("kosong")) {
            String url = "http://"+sp.getString("ip_address", "192.168.43.167");
            Request request = new Request.Builder()
                    // url dari api yang mau diambil
                    .url(url + "/floodizy/backend/web/api/kecamatan/get-all")
                    .build();
            String response;

            try {
                Response respon = client.newCall(request).execute();
                response = respon.body().string();
                JSONArray array = new JSONArray(response);
                for (int i = 0; i < array.length(); i++) {
                    Kecamatan a = new Kecamatan();
                    a.setId(String.valueOf(array.getJSONObject(i).getInt("id")));
                    a.setKecamatan(array.getJSONObject(i).getString("kecamatan"));
                    a.setGeo(array.getJSONObject(i).getString("geometry"));
                    kec.add(a);
                }
                System.out.println(kec.toString());
                return kec;
            } catch (JSONException | IOException e) {
                e.printStackTrace();
            }
//        } else {
//            return null;
//        }
        return null;
    }

    @Override
    protected void onPostExecute(ArrayList<Kecamatan> kecamatan) {
        delegate.processFinish(kecamatan);
    }


}
