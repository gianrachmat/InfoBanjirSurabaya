package gq.gianr.infobanjirsurabaya;

import android.os.AsyncTask;


/**
 * Created by j on 09/05/2017.
 */

public class AsyncGetWKT extends AsyncTask<String, Integer, String> {
    public AsyncResponse delegate = null;

    public interface AsyncResponse{
        void processFinish(String output);
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
    }


    @Override
    protected String doInBackground(String... params) {
        return null;
    }


    @Override
    protected void onPostExecute(String s) {
        delegate.processFinish(s);
    }


}
