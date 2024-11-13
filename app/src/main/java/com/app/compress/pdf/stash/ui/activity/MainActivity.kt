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

class MainActivity: AppCompatActivity() {
    private val REQUEST_CODE: Int = 1234
    private lateinit var mInterstitialAd: RewardedInterstitialAd
    private lateinit var navHostFragment: NavHostFragment
    private lateinit var navController: NavController
    private lateinit var bottomNavigationView: BottomNavigationView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        navHostFragment = supportFragmentManager.findFragmentById(R.id.fragment_container_view) as NavHostFragment
        navController = navHostFragment.navController

        bottomNavigationView = findViewById(R.id.bottom_nav_view);
        bottomNavigationView.setupWithNavController(navController);

        bottomNavigationView.setOnItemSelectedListener { item ->
            if (item.itemId == R.id.compress_fragment) {
                navController.popBackStack(R.id.compress_fragment,false)
            }else if(item.itemId == R.id.history_fragment){
                navController.navigate(R.id.history_fragment)
            }
            true
        }

        if (ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE)
            != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, arrayOf(Manifest.permission.READ_EXTERNAL_STORAGE), REQUEST_CODE)
        }

    }
    private fun showPermissionDialog() {
        if (SDK_INT >= Build.VERSION_CODES.R) {

            try {
                val intent = Intent(Settings.ACTION_MANAGE_ALL_FILES_ACCESS_PERMISSION);
                intent.addCategory("android.intent.category.DEFAULT");
                intent.setData(Uri.parse(String.format("package:%s",  objectListOf(getApplicationContext().getPackageName()))))
                this.startActivityForResult(intent,34)
            } catch (e: Exception) {
                val intent =  Intent(Settings.ACTION_MANAGE_ALL_FILES_ACCESS_PERMISSION);
                startActivity(intent);

            }

        } else
            ActivityCompat.requestPermissions(this,
                listOf(WRITE_EXTERNAL_STORAGE, READ_EXTERNAL_STORAGE).toTypedArray(), 1);
    }

    private fun checkPermission(): Boolean {
        if (SDK_INT >= Build.VERSION_CODES.R) {
            return Environment.isExternalStorageManager();
        } else {
            val write = ContextCompat.checkSelfPermission(getApplicationContext(),
                    WRITE_EXTERNAL_STORAGE);
            val read = ContextCompat.checkSelfPermission(getApplicationContext(),
                    READ_EXTERNAL_STORAGE);

            return write == PackageManager.PERMISSION_GRANTED &&
                    read == PackageManager.PERMISSION_GRANTED;
        }
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