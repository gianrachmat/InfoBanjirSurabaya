package gq.gianr.infobanjirsurabaya;

import android.annotation.SuppressLint;
import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;

import github.vatsal.easyweather.Helper.TempUnitConverter;
import github.vatsal.easyweather.retrofit.models.List;
import github.vatsal.easyweather.retrofit.models.Weather;

/**
 * Created by j on 11/06/2017.
 */

public class ForecastAdapter extends RecyclerView.Adapter<ForecastAdapter.ForecastAdapterViewHolder>{
    List[] data;
    Context context;
    private static final int VIEW_TYPE_TODAY = 0;
    private static final int VIEW_TYPE_FUTURE_DAY = 1;
    private boolean mUseTodayLayout = true;

    public ForecastAdapter(List[] data, Context context) {
        this.data = data;
        this.context = context;
    }

    @Override
    public ForecastAdapterViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        int layoutId;

        switch (viewType) {
            case VIEW_TYPE_TODAY: {
                layoutId = R.layout.forecast_now;
                break;
            }

            case VIEW_TYPE_FUTURE_DAY: {
                layoutId = R.layout.forecast_list_item;
                break;
            }

            default:
                throw new IllegalArgumentException("Invalid view type, value of " + viewType);
        }

        View v = LayoutInflater.from(parent.getContext()).inflate(layoutId, parent, false);

        v.setFocusable(true);

        return new ForecastAdapterViewHolder(v);
    }

    @Override
    public int getItemViewType(int position) {
        if (mUseTodayLayout && position == 0) {
            return VIEW_TYPE_TODAY;
        } else {
            return VIEW_TYPE_FUTURE_DAY;
        }
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(ForecastAdapterViewHolder holder, int position) {
        String link = data[position].getWeather()[0].getIconLink();
        Picasso.with(context).load(link).into(holder.iconView);

        Date tgl = new Date(Long.valueOf(data[position].getDt())*1000L);
        SimpleDateFormat sdf = new SimpleDateFormat("EEEE HH:mm", Locale.US);
        sdf.setTimeZone(TimeZone.getTimeZone("GMT+7"));

        holder.dateView.setText(sdf.format(tgl));
        holder.descriptionView.setText(data[position].getWeather()[0].getDescription());
        holder.highTempView.setText(TempUnitConverter.convertToCelsius(data[position].getMain().getTemp_max()).intValue() + "°");
        holder.lowTempView.setText(TempUnitConverter.convertToCelsius(data[position].getMain().getTemp_min()).intValue() + "°");
    }

    @Override
    public int getItemCount() {
        return data.length;
    }

    class ForecastAdapterViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        final ImageView iconView;

        final TextView dateView;
        final TextView descriptionView;
        final TextView highTempView;
        final TextView lowTempView;

        ForecastAdapterViewHolder(View view) {
            super(view);

            iconView = (ImageView) view.findViewById(R.id.weather_icon);
            dateView = (TextView) view.findViewById(R.id.date);
            descriptionView = (TextView) view.findViewById(R.id.weather_description);
            highTempView = (TextView) view.findViewById(R.id.high_temperature);
            lowTempView = (TextView) view.findViewById(R.id.low_temperature);

            view.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {

        }
    }
}
