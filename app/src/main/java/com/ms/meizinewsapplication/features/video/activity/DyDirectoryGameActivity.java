package com.ms.meizinewsapplication.features.video.activity;

import android.os.Bundle;
import android.support.v7.widget.RecyclerView;

import com.ms.meizinewsapplication.features.base.activity.BaseActivityPresenterImpl;
import com.ms.meizinewsapplication.features.video.iview.DyDirectoryGameIView;
import com.ms.meizinewsapplication.features.video.model.DouYeDirectoryGameWebModel;
import com.ms.meizinewsapplication.features.video.pojo.VideoItem;

import org.loader.model.OnModelListener;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by 啟成 on 2016/5/4.
 */
public class DyDirectoryGameActivity extends BaseActivityPresenterImpl<DyDirectoryGameIView> {

    private DouYeDirectoryGameWebModel douYeDirectoryGameWebModel;

    @Override
    public void created(Bundle savedInstance) {
        super.created(savedInstance);
        mView.init(DyDirectoryGameActivity.this);
        mView.addOnScrollListener(onScrollListener);
        initDouYeDirectoryGameWebModel();
        douYeDirectoryGameWebModelLoadWeb();
    }

    @Override
    public void onBackPressed() {
        mView.onBackPressed(DyDirectoryGameActivity.this);
    }

    //TODO Model==================================================

    private void initDouYeDirectoryGameWebModel() {
        douYeDirectoryGameWebModel = new DouYeDirectoryGameWebModel();
    }

    private void douYeDirectoryGameWebModelLoadWeb() {

        addSubscription(
                douYeDirectoryGameWebModel.loadWeb(
                        DyDirectoryGameActivity.this,
                        douYeDirectory,
                        getIntent().getStringExtra("directory_game"),
                        page + ""
                )
        );

    }

    //TODO Listener===================================================

    OnModelListener<List<VideoItem>> douYeDirectory = new OnModelListener<List<VideoItem>>() {
        @Override
        public void onSuccess(List<VideoItem> douYeDirectories) {
            page++;
            mView.addDatas2QuickAdapter((ArrayList<VideoItem>) douYeDirectories);
        }

        @Override
        public void onError(String err) {

        }

        @Override
        public void onCompleted() {

        }
    };


    private int page = 1;


    /**
     * 到底监听
     */
    RecyclerView.OnScrollListener onScrollListener = new RecyclerView.OnScrollListener() {
        @Override
        public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
            super.onScrollStateChanged(recyclerView, newState);


            if (newState != RecyclerView.SCROLL_STATE_IDLE) {
                return;
            }
//            unsubscribe();

            douYeDirectoryGameWebModelLoadWeb();
        }
    };

}
