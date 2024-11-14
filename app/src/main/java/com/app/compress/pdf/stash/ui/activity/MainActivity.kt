package com.app.compress.pdf.stash.ui.activity;

import android.Manifest.permission.READ_EXTERNAL_STORAGE
import android.Manifest.permission.WRITE_EXTERNAL_STORAGE
import android.os.Build.VERSION.SDK_INT
import androidx.collection.objectListOf
import androidx.navigation.ui.setupWithNavController

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.navigation.NavController;
import androidx.navigation.fragment.NavHostFragment;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.Settings;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.app.compress.pdf.stash.R;
import com.google.android.gms.ads.AdError;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.FullScreenContentCallback;
import com.google.android.gms.ads.LoadAdError;
import com.google.android.gms.ads.OnUserEarnedRewardListener;
import com.google.android.gms.ads.rewarded.RewardItem;
import com.google.android.gms.ads.rewardedinterstitial.RewardedInterstitialAd;
import com.google.android.gms.ads.rewardedinterstitial.RewardedInterstitialAdLoadCallback;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationBarView;
import androidx.navigation.ui.NavigationUI;
import com.app.compress.pdf.stash.databinding.ActivityMainBinding

class MainActivity: AppCompatActivity() {
    private val REQUEST_CODE: Int = 1234
    private lateinit var mInterstitialAd: RewardedInterstitialAd
    private lateinit var navHostFragment: NavHostFragment
    private lateinit var navController: NavController

    private lateinit var binding: ActivityMainBinding

    companion object{
        var INSTANCE: MainActivity ?= null
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        INSTANCE = this

        navHostFragment =
            supportFragmentManager.findFragmentById(R.id.fragment_container_view) as NavHostFragment
        navController = navHostFragment.navController

        binding.bottomNavView.setupWithNavController(navController);

        binding.bottomNavView.setOnItemSelectedListener { item ->
            if (item.itemId == R.id.compress_fragment) {
                openCompressFragment()
            } else if (item.itemId == R.id.history_fragment) {
                openHistoryFragment()
            }
            true
        }

    }

    fun openCompressFragment(){
        navController.popBackStack(R.id.compress_fragment,false)
    }

    fun openHistoryFragment(){
        navController.navigate(R.id.history_fragment)
    }

    fun openFinalFragment(status: Boolean){
        val bundle = Bundle()
        bundle.putBoolean("status",status)
//        navController.navigate(R.id.final_fragment,bundle)
    }

//    private void loadAd() {
//        RewardedInterstitialAd.load(MainActivity.this, "ca-app-pub-9668830280921241/7627057702",
//                new AdRequest.Builder().build(),  new RewardedInterstitialAdLoadCallback() {
//                    @Override
//                    public void onAdLoaded(RewardedInterstitialAd ad) {
//                        mInterstitialAd = ad;
//                        Log.d("TAG == rk", "onAdLoaded");
//                        mInterstitialAd.show(MainActivity.this, new OnUserEarnedRewardListener() {
//                            @Override
//                            public void onUserEarnedReward(@NonNull RewardItem rewardItem) {
//                            }
//                        });
//                        mInterstitialAd.setFullScreenContentCallback(new FullScreenContentCallback() {
//                            /** Called when the ad failed to show full screen content. */
//                            @Override
//                            public void onAdFailedToShowFullScreenContent(AdError adError) {
//                                Log.d("TAG == rk", "onAdFailedToShowFullScreenContent");
//                                Log.d( "onAdFailedToShow : ",adError.getMessage().toString());
//                            }
//                            /** Called when ad showed the full screen content. */
//                            @Override
//                            public void onAdShowedFullScreenContent() {
//                                Log.d("TAG == rk", "onAdShowedFullScreenContent");
//                            }
//
//                            /** Called when full screen content is dismissed. */
//                            @Override
//                            public void onAdDismissedFullScreenContent() {
//                                Log.d("TAG == rk", "onAdDismissedFullScreenContent");
//                            }
//                        });
//                    }
//                    @Override
//                    public void onAdFailedToLoad(LoadAdError loadAdError) {
//                        Log.e("TAG", "onAdFailedToLoad");
//                    }
//                });
//    }
}