package com.example.music_app1.View;

import static com.bumptech.glide.Glide.*;
import static com.example.music_app1.MainActivity.mViewPager2;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestOptions;
import com.bumptech.glide.request.target.SimpleTarget;
import com.bumptech.glide.request.transition.Transition;
import com.example.music_app1.Model.Music;
import com.example.music_app1.R;
import com.example.music_app1.adapter.MusicAdapter;
import com.example.music_app1.adapter.RankAdapter;
import com.google.android.play.integrity.internal.m;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.jjoe64.graphview.GraphView;
import com.jjoe64.graphview.GridLabelRenderer;
import com.jjoe64.graphview.helper.StaticLabelsFormatter;
import com.jjoe64.graphview.series.BarGraphSeries;
import com.jjoe64.graphview.series.DataPoint;
import com.jjoe64.graphview.series.DataPointInterface;
import com.jjoe64.graphview.series.LineGraphSeries;
import com.jjoe64.graphview.series.PointsGraphSeries;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Target;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class Zingchat_Fragment extends Fragment {

    private ImageButton imgbtn_search;
    private RecyclerView rcvMusic, recyclerView;
    private RankAdapter mRankAdapter;

    private GraphView mGraphView;

    List<Music> mListMusic;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_zingchat_, container, false);
        mListMusic = new ArrayList<>();

        //Biểu đồ
        mGraphView = view.findViewById(R.id.graph);

        imgbtn_search = view.findViewById(R.id.search);
        recyclerView = view.findViewById(R.id.rcv_number_rank);
        List<Integer> dataList = new ArrayList<>();
        for (int i = 1; i <= 100; i++) {
            dataList.add(i);
        }
        mRankAdapter = new RankAdapter(view.getContext(), dataList, mListMusic);

        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        DividerItemDecoration itemDecoration1 = new DividerItemDecoration(getActivity(), DividerItemDecoration.VERTICAL);
        recyclerView.addItemDecoration(itemDecoration1);
        recyclerView.setAdapter(mRankAdapter);
        imgbtn_search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mViewPager2.setCurrentItem(5,false);

            }
        });

        callApiGetMusics("");
        return view;
    }
    public void callApiGetMusics(String keyword){

        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference myRef = database.getReference("music");
        Query query = myRef.orderByChild("listens");
        query.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                Music music = snapshot.getValue(Music.class);
                if(music != null){
                    mListMusic.add(music);
                    Collections.sort(mListMusic, new Comparator<Music>() {
                        @Override
                        public int compare(Music m1, Music m2) {
                            return Integer.compare(m2.getListens(), m1.getListens());
                        }
                    });
                    mRankAdapter.notifyDataSetChanged();
                }
                if(mListMusic.size() >= 21){
                    setupGraphView();
                }

            }

            @Override
            public void onChildChanged(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                Music music = snapshot.getValue(Music.class);

                if(mListMusic == null || mListMusic.isEmpty()){
                    return;
                }
                for (int i = 0; i< mListMusic.size(); i++){
                    if(music.getId() == mListMusic.get(i).getId()){
                        mListMusic.set(i, music);

                    }
                }
                Collections.sort(mListMusic, new Comparator<Music>() {
                    @Override
                    public int compare(Music m1, Music m2) {
                        return Integer.compare(m2.getListens(), m1.getListens());
                    }
                });
                mRankAdapter.notifyDataSetChanged();
                setupGraphView();
            }

            @Override
            public void onChildRemoved(@NonNull DataSnapshot snapshot) {

            }

            @Override
            public void onChildMoved(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

    }

    private void setupGraphView() {
        String  linkhinh1 = mListMusic.get(0).getImage();
        String linkhinh2 =mListMusic.get(1).getImage();
        String linkhinh3 =mListMusic.get(2).getImage();
        // Tạo series cho đường xanh
        LineGraphSeries<DataPoint> seriesGreen = new LineGraphSeries<>(new DataPoint[]{

                new DataPoint(1, 2), // Điểm 1
                new DataPoint(2, 2.2), // Điểm 2
                new DataPoint(3, 2.4), // Điểm 3
                new DataPoint(4, 2.6), // Điểm 4
                new DataPoint(5, 2.55), // Điểm 5
                new DataPoint(6, 2.7), // Điểm 6
                new DataPoint(7, 2.64), // Điểm 7
                new DataPoint(8, 2.48), // Điểm 8
                new DataPoint(9, 2.4), // Điểm 9
                new DataPoint(10, 2.3) // Điểm 10
        });

        PointsGraphSeries<DataPoint> seriesDotGreen = new PointsGraphSeries<>(new DataPoint[]{
                new DataPoint(1, 2), // Điểm 1
                new DataPoint(2, 2.2), // Điểm 2
                new DataPoint(3, 2.4), // Điểm 3
                new DataPoint(4, 2.6), // Điểm 4
                new DataPoint(5, 2.55), // Điểm 5
                new DataPoint(6, 2.7), // Điểm 6
                new DataPoint(7, 2.64), // Điểm 7
                new DataPoint(8, 2.48), // Điểm 8
                new DataPoint(9, 2.4), // Điểm 9
                new DataPoint(10, 2.3) // Điểm 10
        });

        // Đặt màu sắc của đường xanh
        seriesGreen.setColor(Color.GREEN);
        seriesDotGreen.setSize(10);
        seriesDotGreen.setColor(Color.GREEN);


        // Tạo series cho đường vàng
        LineGraphSeries<DataPoint> seriesYellow = new LineGraphSeries<>(new DataPoint[]{
                new DataPoint(1, 1.8), // Điểm 1
                new DataPoint(2, 1.9), // Điểm 2
                new DataPoint(3, 2), // Điểm 3
                new DataPoint(4, 2.2), // Điểm 4
                new DataPoint(5, 2.35), // Điểm 5
                new DataPoint(6, 2.45), // Điểm 6
                new DataPoint(7, 2.4), // Điểm 7
                new DataPoint(8, 2.34), // Điểm 8
                new DataPoint(9, 2.3), // Điểm 9
                new DataPoint(10, 2.1) // Điểm 10
        });
        PointsGraphSeries<DataPoint> seriesDotYellow = new PointsGraphSeries<>(new DataPoint[]{
                new DataPoint(1, 1.8), // Điểm 1
                new DataPoint(2, 1.9), // Điểm 2
                new DataPoint(3, 2), // Điểm 3
                new DataPoint(4, 2.2), // Điểm 4
                new DataPoint(5, 2.35), // Điểm 5
                new DataPoint(6, 2.45), // Điểm 6
                new DataPoint(7, 2.4), // Điểm 7
                new DataPoint(8, 2.34), // Điểm 8
                new DataPoint(9, 2.3), // Điểm 9
                new DataPoint(10, 2.1) // Điểm 10
        });

        // Đặt màu sắc của đường vàng
        seriesYellow.setColor(Color.YELLOW);
        seriesDotYellow.setColor(Color.YELLOW);
        seriesDotYellow.setSize(10);


        // Tạo series cho đường đỏ
        LineGraphSeries<DataPoint> seriesRed = new LineGraphSeries<>(new DataPoint[]{
                new DataPoint(1, 1.3), // Điểm 1
                new DataPoint(2, 1.4), // Điểm 2
                new DataPoint(3, 1.4), // Điểm 3
                new DataPoint(4, 1.36), // Điểm 4
                new DataPoint(5, 1.4), // Điểm 5
                new DataPoint(6, 1.5), // Điểm 6
                new DataPoint(7, 1.6), // Điểm 7
                new DataPoint(8, 1.6), // Điểm 8
                new DataPoint(9, 1.54), // Điểm 9
                new DataPoint(10, 1.48) // Điểm 10
        });

        PointsGraphSeries<DataPoint> seriesDotRed = new PointsGraphSeries<>(new DataPoint[]{
                new DataPoint(1, 1.3), // Điểm 1
                new DataPoint(2, 1.4), // Điểm 2
                new DataPoint(3, 1.4), // Điểm 3
                new DataPoint(4, 1.36), // Điểm 4
                new DataPoint(5, 1.4), // Điểm 5
                new DataPoint(6, 1.5), // Điểm 6
                new DataPoint(7, 1.6), // Điểm 7
                new DataPoint(8, 1.6), // Điểm 8
                new DataPoint(9, 1.54), // Điểm 9
                new DataPoint(10, 1.48) // Điểm 10
        });

        seriesDotRed.setSize(10); // Kích thước của dấu chấm tròn
        seriesDotRed.setColor(Color.RED); // Màu sắc của dấu chấm tròn
        seriesRed.setColor(Color.RED);


        PointsGraphSeries<DataPoint> topPointSeries = new PointsGraphSeries<>(new DataPoint[]{
                new DataPoint(6, 2.7) //Điểm cao nhất, bài hát top 1
        });

        Glide.with(this)
                .asBitmap()
                .load(linkhinh1)
                .apply(new RequestOptions()
                        .diskCacheStrategy(DiskCacheStrategy.ALL)
                )
                .into(new SimpleTarget<Bitmap>() {
                    @Override
                    public void onResourceReady(Bitmap resource, Transition<? super Bitmap> transition) {
                        int newWidth = 100;
                        int newHeight = 100;
                        Bitmap resizedBitmap = Bitmap.createScaledBitmap(resource, newWidth, newHeight, true);
                        topPointSeries.setCustomShape(new PointsGraphSeries.CustomShape() {
                            @Override
                            public void draw(Canvas canvas, Paint paint, float x, float y, DataPointInterface dataPoint) {
                                float left = x - resizedBitmap.getWidth() / 2;
                                float top = y - resizedBitmap.getHeight();
                                canvas.drawBitmap(resizedBitmap, left, top, paint);
                            }
                        });

                    }
                });

        PointsGraphSeries<DataPoint> topPointSeries2 = new PointsGraphSeries<>(new DataPoint[]{
                new DataPoint(6, 2.45) //Điểm cao nhất, bài hát top 2
        });

        Glide.with(this)
                .asBitmap()
                .load(linkhinh2)
                .apply(new RequestOptions()
                        .diskCacheStrategy(DiskCacheStrategy.ALL)
                )
                .into(new SimpleTarget<Bitmap>() {
                    @Override
                    public void onResourceReady(Bitmap resource, Transition<? super Bitmap> transition) {
                        int newWidth = 100;
                        int newHeight = 100;
                        Bitmap resizedBitmap = Bitmap.createScaledBitmap(resource, newWidth, newHeight, true);
                        topPointSeries2.setCustomShape(new PointsGraphSeries.CustomShape() {
                            @Override
                            public void draw(Canvas canvas, Paint paint, float x, float y, DataPointInterface dataPoint) {
                                float left = x - resizedBitmap.getWidth() / 2;
                                float top = y;
                                canvas.drawBitmap(resizedBitmap, left, top, paint);
                            }
                        });

                    }
                });

        PointsGraphSeries<DataPoint> topPointSeries3 = new PointsGraphSeries<>(new DataPoint[]{
                new DataPoint(8, 1.6) //Điểm cao nhất, bài hát top 3
        });

        Glide.with(this)
                .asBitmap()
                .load(linkhinh3)
                .apply(new RequestOptions()
                        .diskCacheStrategy(DiskCacheStrategy.ALL)
                )
                .into(new SimpleTarget<Bitmap>() {
                    @Override
                    public void onResourceReady(Bitmap resource, Transition<? super Bitmap> transition) {
                        int newWidth = 100;
                        int newHeight = 100;
                        Bitmap resizedBitmap = Bitmap.createScaledBitmap(resource, newWidth, newHeight, true);
                        topPointSeries3.setCustomShape(new PointsGraphSeries.CustomShape() {
                            @Override
                            public void draw(Canvas canvas, Paint paint, float x, float y, DataPointInterface dataPoint) {
                                float left = x - resizedBitmap.getWidth() / 2;
                                float top = y - resizedBitmap.getHeight();
                                canvas.drawBitmap(resizedBitmap, left, top, paint);
                            }
                        });

                    }
                });


        // Thêm đường vào graphview
        mGraphView.addSeries(seriesGreen);
        mGraphView.addSeries(seriesYellow);
        mGraphView.addSeries(seriesRed);

        // Đặt giá trị của trục X
        mGraphView.getViewport().setMinX(1);
        mGraphView.getViewport().setMaxX(10);
        mGraphView.getGridLabelRenderer().setNumHorizontalLabels(10); // Số lượng nhãn trên trục X
        mGraphView.getGridLabelRenderer().setGridStyle(GridLabelRenderer.GridStyle.NONE);
        mGraphView.getGridLabelRenderer().setVerticalLabelsVisible(false);



        // Tạo Handler để lập lịch hiển thị các điểm
        final Handler handler = new Handler();
        final Runnable runnable = new Runnable() {
            @Override
            public void run() {
                // Hiển thị điểm màu xanh
                mGraphView.addSeries(seriesDotGreen);
                mGraphView.addSeries(topPointSeries);

                // Sau 1 giây, ẩn điểm màu xanh và hiển thị điểm màu vàng
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        mGraphView.removeSeries(seriesDotGreen);
                        mGraphView.removeSeries(topPointSeries);
                        mGraphView.addSeries(seriesDotYellow);
                        mGraphView.addSeries(topPointSeries2);

                        // Sau 1 giây, ẩn điểm màu vàng và hiển thị điểm màu đỏ
                        handler.postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                mGraphView.removeSeries(seriesDotYellow);
                                mGraphView.removeSeries(topPointSeries2);
                                mGraphView.addSeries(seriesDotRed);
                                mGraphView.addSeries(topPointSeries3);
                            }
                        }, 1500);
                    }
                }, 1500);
                mGraphView.removeSeries(seriesDotRed);
                mGraphView.removeSeries(topPointSeries3);
                // Sau 2 giây, gọi lại runnable để bắt đầu lại từ đầu
                handler.postDelayed(this, 4500);
            }
        };
        handler.post(runnable); // Bắt đầu chạy runnable
    }
}