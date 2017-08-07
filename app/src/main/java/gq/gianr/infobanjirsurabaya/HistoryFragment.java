package gq.gianr.infobanjirsurabaya;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import github.vatsal.easyweather.retrofit.models.List;
import gq.gianr.infobanjirsurabaya.model.Notif;

/**
 * Created by j on 18/07/2017.
 */

public class HistoryFragment extends Fragment {
    private RecyclerView mRecycler;
    DatabaseHandler db;
    HistoryAdapter ha;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.history_fragment, container, false);
        db = new DatabaseHandler(getContext());
        mRecycler = (RecyclerView) v.findViewById(R.id.recycler_history);
        LinearLayoutManager layoutManager =
                new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false);
        mRecycler.setLayoutManager(layoutManager);
        mRecycler.setHasFixedSize(true);

        ha = new HistoryAdapter(db.getAllNotifs());

        mRecycler.setAdapter(ha);
        return v;
    }
}
