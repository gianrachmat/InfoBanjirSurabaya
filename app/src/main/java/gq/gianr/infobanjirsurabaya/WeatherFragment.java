package gq.gianr.infobanjirsurabaya;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.CardView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.picasso.Picasso;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import github.vatsal.easyweather.Helper.ForecastCallback;
import github.vatsal.easyweather.Helper.TempUnitConverter;
import github.vatsal.easyweather.Helper.WeatherCallback;
import github.vatsal.easyweather.WeatherMap;
import github.vatsal.easyweather.retrofit.models.ForecastResponseModel;
import github.vatsal.easyweather.retrofit.models.Weather;
import github.vatsal.easyweather.retrofit.models.WeatherResponseModel;

/**
 * Created by j on 02/03/2017.
 */
public class WeatherFragment extends Fragment {
    public final String APP_ID = "062e8ee34715b1ebdc5ddf46a8f645d0";
    String city = "Surabaya";
    ForecastAdapter adapter;

    @Bind(R.id.recycler_weather) RecyclerView recyclerView;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView =  inflater.inflate(R.layout.weather_layout, null);
        ButterKnife.bind(this, rootView);
        loadWeather(city);
        LinearLayoutManager layoutManager =
                new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setHasFixedSize(true);
        recyclerView.setVisibility(View.GONE);
        return rootView;
    }

    private void loadWeather(String city) {
        WeatherMap weatherMap = new WeatherMap(getContext(), APP_ID);

        weatherMap.getCityForecast(city, new ForecastCallback() {
            @Override
            public void success(ForecastResponseModel response) {
                System.out.println("getList:"+ response.getList().length);
                populateForecast(response);
            }

            @Override
            public void failure(String message) {

            }
        });
    }

    private void populateForecast(ForecastResponseModel response){
        adapter = new ForecastAdapter(response.getList(), getContext());
        recyclerView.setAdapter(adapter);
        recyclerView.setVisibility(View.VISIBLE);
    }

}
