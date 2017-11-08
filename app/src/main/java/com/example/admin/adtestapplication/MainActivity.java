package com.example.admin.adtestapplication;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.Toast;

import com.pingstart.adsdk.MRAIDBanner;
import com.pingstart.adsdk.MRAIDInterstitial;
import com.pingstart.adsdk.PingStartSDK;
import com.pingstart.adsdk.listener.MRAIDBannerListener;
import com.pingstart.adsdk.listener.MRAIDInterstitialListener;
import com.pingstart.adsdk.listener.RewardVideoListener;
import com.pingstart.adsdk.mediation.PingStartVideo;
import com.pingstart.adsdk.model.PingStartReward;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {


    private static final String TAG = MainActivity.class.getSimpleName();
    private FrameLayout mBannerContainer;
    private MRAIDBanner mBanner;
    private MRAIDInterstitial mInterstitial;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        PingStartSDK.initializeSdk(this, "5176", "5176_24");
        setContentView(R.layout.activity_main);
        findViewById(R.id.showBanner).setOnClickListener(this);
        findViewById(R.id.showInterstitial).setOnClickListener(this);
        findViewById(R.id.showVideo).setOnClickListener(this);
        mBannerContainer = (FrameLayout) findViewById(R.id.banner_container);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.showBanner:
                showBanner();
                break;
            case R.id.showInterstitial:
                showInterstitial();
                break;
            case R.id.showVideo:
                showVideo();
                break;
        }
    }

    private void showVideo() {
        PingStartVideo.initializeRewardedVideo(this);
        PingStartVideo.setRewardedVideoListener(new RewardVideoListener() {
            @Override
            public void onVideoAdClosed() {
                Log.d(TAG, "onVideoAdClosed: ");
            }

            @Override
            public void onVideoStarted() {
                Log.d(TAG, "onVideoStarted: ");
            }

            @Override
            public void onVideoLoaded() {
                Log.d(TAG, "onVideoLoaded: ");
                PingStartVideo.showRewardedVideo();
            }

            @Override
            public void onVideoCompleted(PingStartReward pingStartReward) {
                Log.d(TAG, "onVideoCompleted: currency:::"+pingStartReward.getLabel()+",amount:::"+pingStartReward.getAmount());
            }

            @Override
            public void onAdError(String s) {
                Log.d(TAG, "onAdError: " + s);
            }

            @Override
            public void onAdClicked() {
                Log.d(TAG, "onAdClicked: ");
            }
        });
        PingStartVideo.loadRewardedVideo("4");
    }

    /**
     * 插屏显示
     */
    private void showInterstitial() {
        mInterstitial = new MRAIDInterstitial(this, "51");
        mInterstitial.setMraidAdListener(
                new MRAIDInterstitialListener() {
                    @Override
                    public void mraidInterstitialLoaded() {
                        mInterstitial.show();
                    }

                    @Override
                    public void mraidInterstitialClose() {
                        mInterstitial.destroy();
                        showToast("mraidInterstitialClose");
                    }

                    @Override
                    public void mraidInterstitialClicked() {
                        showToast("mraidInterstitialClicked");
                    }

                    @Override
                    public void mraidInterstitialError(String s) {
                        showToast("mraidInterstitialError");
                    }

                }
        );
        mInterstitial.loadAd();
    }

    /**
     * banner
     */
    private void showBanner() {
        mBanner = new MRAIDBanner(this, "50");
        mBanner.setMraidAdListener(new MRAIDBannerListener() {

            @Override
            public void mraidBannerLoaded(View view) {
                mBannerContainer.removeAllViews();
                mBannerContainer.addView(view);
            }

            @Override
            public void mraidBannerClicked() {
                showToast("banner 被点击了");
            }

            @Override
            public void mraidBannerError(String s) {
                showToast("banner 加载失败");
            }
        });
        mBanner.loadAd();
    }

    @Override
    public void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        if (mBanner != null)
            mBanner.destroy();
        if (mInterstitial != null)
            mInterstitial.destroy();
        mBanner = null;
        mInterstitial = null;
    }

    private void showToast(String s) {
        Toast.makeText(this, s, Toast.LENGTH_LONG).show();
    }
}
