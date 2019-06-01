package com.example.proovit.fragments;


import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.proovit.LoginActivity;
import com.example.proovit.MainActivity;
import com.example.proovit.R;
import com.example.proovit.data.Domain;
import com.example.proovit.utils.APIInterface;
import com.example.proovit.utils.ApiClient;
import com.example.proovit.utils.RecyclerViewAdapterAll;
import com.example.proovit.utils.RecyclerViewMyReportedAdapter;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ReportedFragment extends Fragment implements RecyclerViewMyReportedAdapter.ItemClickListener{

    private static final java.lang.String TAG = "ReportedFragment";
    private RecyclerView mRecyclerView;
    private List<String> runList;

    RecyclerViewMyReportedAdapter adapter;

    private RecyclerView mRecyclerView2;
    private List<String> runList2;

    RecyclerViewAdapterAll adapter2;
    SharedPreferences preferences;
    APIInterface apiService;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        preferences = this.getActivity().getSharedPreferences("com.example.proovit", Context.MODE_PRIVATE);
        String uuid = preferences.getString("uuid", "");
        Log.e(TAG, "onCreateView: uuid:" + uuid);

        runList = new ArrayList<>();
        runList2 = new ArrayList<>();
        apiService = ApiClient.getClient().create(APIInterface.class);
        final Context context = this.getContext();

        Call<List<String>> call = apiService.getDomains(uuid);
        call.enqueue(new Callback<List<String>>() {
            @Override
            public void onResponse(Call<List<String>> call, Response<List<String>> response) {
                runList = response.body();
                Log.e(TAG, "onResponse: domain response: " + response.code());
                for (String domain: runList) {
                    Log.e(TAG, "domain: " + domain);
                }
                adapter = new RecyclerViewMyReportedAdapter(context, runList);
                adapter.setClickListener(new RecyclerViewMyReportedAdapter.ItemClickListener() {
                    @Override
                    public void onItemClick(View view, int position) {

                    }
                });
                mRecyclerView.setAdapter(adapter);
                //mRecyclerView.setAdapter(adapter);
            }

            @Override
            public void onFailure(Call<List<String>> call, Throwable t) {

            }
        });


//


        Call<List<String>> call2 = apiService.getAllDomains(uuid);
        call2.enqueue(new Callback<List<String>>() {
            @Override
            public void onResponse(Call<List<String>> call, Response<List<String>> response) {
                runList2 = response.body();
                Log.e(TAG, "onResponse: przez innych: " + response.code());
                for (String domain: runList2) {
                    Log.e(TAG, "domain: " + domain);
                }
                adapter2 = new RecyclerViewAdapterAll(context, runList2);
                adapter2.setClickListener(new RecyclerViewAdapterAll.ItemClickListener() {
                    @Override
                    public void onItemClick(View view, int position) {

                    }
                });
                mRecyclerView2.setAdapter(adapter2);
                //mRecyclerView.setAdapter(adapter);
            }

            @Override
            public void onFailure(Call<List<String>> call, Throwable t) {

            }
        });


        //todo pobierz dane
        View v = inflater.inflate(R.layout.fragment_reported, container, false);
        mRecyclerView = v.findViewById(R.id.recyclerViewReportedByMe);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this.getContext());
        mRecyclerView.setLayoutManager(layoutManager);
        DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(mRecyclerView.getContext(),((LinearLayoutManager) layoutManager).getOrientation());
        mRecyclerView.addItemDecoration(dividerItemDecoration);

        mRecyclerView2 = v.findViewById(R.id.recyclerViewReportedByOthers);
        DividerItemDecoration dividerItemDecoration2 = new DividerItemDecoration(mRecyclerView2.getContext(),((LinearLayoutManager) layoutManager).getOrientation());
        RecyclerView.LayoutManager layoutManager2 = new LinearLayoutManager(this.getContext());

        mRecyclerView2.setLayoutManager(layoutManager2);
        mRecyclerView2.addItemDecoration(dividerItemDecoration2);
//        adapter = new RecyclerViewMyReportedAdapter(this.getContext(), runList);
//        adapter.setClickListener(this);
//        mRecyclerView.setAdapter(adapter);
        return v;
    }


    @Override
    public void onItemClick(View view, int position) {

    }
}
