package gq.gianr.infobanjirsurabaya;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import gq.gianr.infobanjirsurabaya.model.Notif;

/**
 * Created by j on 19/07/2017.
 */

public class HistoryAdapter extends RecyclerView.Adapter<HistoryAdapter.HistoryAdapterViewHolder>{
    List<Notif> notifList = new ArrayList<>();

    public HistoryAdapter(List<Notif> notifList) {
        Collections.reverse(notifList);
        this.notifList = notifList;
    }

    @Override
    public HistoryAdapterViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.history_list, parent, false);
        v.setFocusable(true);
        return new HistoryAdapterViewHolder(v);
    }

    @Override
    public void onBindViewHolder(HistoryAdapterViewHolder holder, int position) {
        holder.chj.setText(notifList.get(position).get_tgl());
        holder.tgl.setText(notifList.get(position).get_chj());
    }

    @Override
    public int getItemCount() {
        return notifList.size();
    }

    class HistoryAdapterViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        final TextView tgl;
        final TextView chj;

        HistoryAdapterViewHolder(View view) {
            super(view);

            tgl = (TextView) view.findViewById(R.id.tanggal_history);
            chj = (TextView) view.findViewById(R.id.chj_history);

            view.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {

        }
    }
}
