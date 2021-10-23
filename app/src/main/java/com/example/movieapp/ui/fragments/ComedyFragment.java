package com.example.movieapp.ui.fragments;

import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.movieapp.R;
import com.example.movieapp.adapters.MovieAdapter;
import com.example.movieapp.adapters.MovieItemClickListener;
import com.example.movieapp.adapters.ViewPagerAdapter;
import com.example.movieapp.api.ApiUtils;
import com.example.movieapp.api.MovieService;
import com.example.movieapp.models.Phim;
import com.example.movieapp.models.ResponseParser;
import com.example.movieapp.ui.MainActivity;
import com.example.movieapp.ui.MovieDetailActivity;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class ComedyFragment extends Fragment implements MovieItemClickListener {

    RecyclerView recyclerView;
    List<Phim> dataHolder;
    List<Phim> movies;

    MovieService movieService;
    Call<ResponseParser> call;
    MovieAdapter movieAdapter;

    public ComedyFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        movieService = ApiUtils.getMoiveService();

        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_comedy, container, false);
        recyclerView = view.findViewById(R.id.recyclerViewComedy);

        call = movieService.getListMovies("IwAR1k4WlQbyCdrKT7ITP-6RrfGhyIk-IFtByEE2uM_vBn_PWgXASG0mnaXF0");
        call.enqueue(new Callback<ResponseParser>() {
            @Override
            public void onResponse(Call<ResponseParser> call, Response<ResponseParser> response) {
                ResponseParser responseParser = response.body();

                if (responseParser != null) {
                    dataHolder = new ArrayList<>();
                    movies = new ArrayList<>();
                    movies = responseParser.getPhim().get("phimle");
                    for (int i = 0; i < 30; i++) {
                        if (movies.get(i).getCategory().contains("Phim hoạt hình")) {
                            dataHolder.add(movies.get(i));
                        }
                    }
                    movieAdapter = new MovieAdapter(getActivity(), dataHolder, ComedyFragment.this);
                    recyclerView.setAdapter(movieAdapter);
                    recyclerView.setLayoutManager(new LinearLayoutManager(getActivity(), LinearLayoutManager.HORIZONTAL, false));
//                    recyclerView.setLayoutManager(new GridLayoutManager(getActivity(), 3));
                }
            }

            @Override
            public void onFailure(Call<ResponseParser> call, Throwable t) {
                t.printStackTrace();
                Toast.makeText(getActivity(), "Call api error", Toast.LENGTH_SHORT).show();
            }
        });

        recyclerView.setAdapter(new MovieAdapter(this.getContext(), dataHolder));
        return view;
    }

    @Override
    public void onMovieClick(Phim movie, ImageView movieImageView) {
        Intent intent = new Intent(this.getContext(), MovieDetailActivity.class);
        intent.putExtra("title", movie.getTitle());
        intent.putExtra("imgURl", movie.getImageUrl());
        intent.putExtra("category", movie.getCategory());
        startActivity(intent);

        Toast.makeText(this.getContext(), "item clicked: " + movie.getTitle(), Toast.LENGTH_SHORT).show();
    }
}