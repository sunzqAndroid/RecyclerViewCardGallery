package com.view.jameson.androidrecyclerviewcard;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.widget.ImageView;

import com.view.jameson.androidrecyclerviewcard.util.BlurBitmapUtils;
import com.view.jameson.androidrecyclerviewcard.util.ViewSwitchUtils;
import com.view.jameson.library.CardScaleHelper;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends Activity {

    private RecyclerView mRecyclerView;
    private ImageView mBlurView;
    private List<Integer> mList = new ArrayList<>();
    private CardScaleHelper mCardScaleHelper = null;
    private Runnable mBlurRunnable;
    private int mLastPos = -1;
    CardAdapter mAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        init();
        updateData();
    }

    private void init() {
        for (int i = 0; i < 10; i++) {
            mList.add(R.drawable.pic4);
            mList.add(R.drawable.pic5);
            mList.add(R.drawable.pic6);
        }

        mRecyclerView = (RecyclerView) findViewById(R.id.recyclerView);
        final LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false);
        mRecyclerView.setLayoutManager(linearLayoutManager);
        mAdapter = new CardAdapter(mList);
        mRecyclerView.setAdapter(mAdapter);
        // mRecyclerView绑定scale效果
        mCardScaleHelper = new CardScaleHelper();
        mCardScaleHelper.setCurrentItemPos(8);
        mCardScaleHelper.attachToRecyclerView(mRecyclerView);
        mAdapter.setCardScaleHelper(mCardScaleHelper);

        initBlurBackground();
    }

    private void initBlurBackground() {
        mBlurView = (ImageView) findViewById(R.id.blurView);
        mRecyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
                if (newState == RecyclerView.SCROLL_STATE_IDLE) {
//                    notifyBackgroundChange();
                }
            }
        });

        notifyBackgroundChange();
    }

    private void notifyBackgroundChange() {
        if (mLastPos == mCardScaleHelper.getCurrentItemPos()) return;
        mLastPos = mCardScaleHelper.getCurrentItemPos();
        final int resId = mList.get(mCardScaleHelper.getCurrentItemPos());
        mBlurView.removeCallbacks(mBlurRunnable);
        mBlurRunnable = new Runnable() {
            @Override
            public void run() {
                Bitmap bitmap = BitmapFactory.decodeResource(getResources(), resId);
                ViewSwitchUtils.startSwitchBackgroundAnim(mBlurView, BlurBitmapUtils.getBlurBitmap(mBlurView.getContext(), bitmap, 15));
            }
        };
        mBlurView.postDelayed(mBlurRunnable, 500);
    }

    private void updateData() {

        Runnable mBlurRunnable = new Runnable() {
            @Override
            public void run() {
                mLastPos = mCardScaleHelper.getCurrentItemPos();
                List<Integer> mList = new ArrayList<>();
                for (int i = 0; i < 9; i++) {
                    mList.add(R.drawable.pic4);
                    mList.add(R.drawable.pic5);
                    mList.add(R.drawable.pic6);
                }
                mAdapter = new CardAdapter(mList);
                mRecyclerView.setAdapter(mAdapter);
                mAdapter.setCardScaleHelper(mCardScaleHelper);
                if (mLastPos > mList.size() - 1) {
                    mLastPos = mList.size() - 1;
                }
                mCardScaleHelper.setCurrentItemOffset(0);
            }
        };
        mBlurView.postDelayed(mBlurRunnable, 5000);
    }
}
