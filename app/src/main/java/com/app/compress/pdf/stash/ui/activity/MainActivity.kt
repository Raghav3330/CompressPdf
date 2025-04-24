package com.app.compress.pdf.stash.ui.activity;

import android.content.res.ColorStateList
import android.content.res.Configuration
import android.graphics.Color
import android.os.Build
import androidx.navigation.ui.setupWithNavController

import androidx.appcompat.app.AppCompatActivity;
import androidx.navigation.NavController;
import androidx.navigation.fragment.NavHostFragment;

import android.os.Bundle;
import android.view.Menu
import android.view.MenuItem
import android.view.WindowInsets.CONSUMED
import androidx.activity.OnBackPressedCallback
import androidx.activity.enableEdgeToEdge
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.view.WindowInsetsControllerCompat
import androidx.core.view.updatePadding

import com.app.compress.pdf.stash.R;
import com.google.android.gms.ads.rewardedinterstitial.RewardedInterstitialAd;
import com.app.compress.pdf.stash.databinding.ActivityMainBinding
import com.app.compress.pdf.stash.model.Pdf
import com.app.compress.pdf.stash.ui.listener.OnNavigationClickListener
import com.google.gson.Gson
import java.io.File

class MainActivity: AppCompatActivity(), OnNavigationClickListener {
    private val REQUEST_CODE: Int = 1234
    private lateinit var mInterstitialAd: RewardedInterstitialAd
    private lateinit var navHostFragment: NavHostFragment
    private lateinit var navController: NavController

    private lateinit var binding: ActivityMainBinding

    companion object{
        val INSTANCE: MainActivity? = null
    }

     private val isDarkMode by lazy {
         when (resources.configuration.uiMode and Configuration.UI_MODE_NIGHT_MASK) {
             Configuration.UI_MODE_NIGHT_YES -> true
             else -> false
         }
     }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        ViewCompat.setOnApplyWindowInsetsListener(binding.root) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
        ViewCompat.setOnApplyWindowInsetsListener(binding.bottomNavView) { v, insets ->
            val bars = insets.getInsets(
                WindowInsetsCompat.Type.systemBars()
                        or WindowInsetsCompat.Type.displayCutout()
            )
            v.updatePadding(
                bottom = bars.bottom,
            )
            WindowInsetsCompat.CONSUMED
        }
        setSupportActionBar(binding.clToolbar.toolbar)

        // Disable the default title to prevent duplicate text
        supportActionBar?.setDisplayShowTitleEnabled(false)
        binding.clToolbar.toolbarTitle.text = "Compress Pdf"

        if(isDarkMode){
            WindowInsetsControllerCompat(window, window.decorView).isAppearanceLightStatusBars = false // for white icons
        }else{
            WindowInsetsControllerCompat(window, window.decorView).isAppearanceLightStatusBars = true // for black icons
        }

        navHostFragment = supportFragmentManager.findFragmentById(R.id.fragment_container_view) as NavHostFragment
        navController = navHostFragment.navController

        binding.bottomNavView.setupWithNavController(navController)

        onBackPressedDispatcher.addCallback(this, object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                if (!navController.popBackStack()) {
                    finish()
                }
            }
        })

        binding.bottomNavView.setOnItemSelectedListener { item ->
            if (item.itemId == R.id.compress_fragment) {
                openCompressFragment()
            } else if (item.itemId == R.id.history_fragment) {
                openHistoryFragment()
            }
            true
        }

        navController.addOnDestinationChangedListener { _, destination, _ ->
            when (destination.id) {
                R.id.compress_fragment -> {
                    supportActionBar?.setDisplayHomeAsUpEnabled(false)
                    binding.clToolbar.toolbarTitle.text = "Compress Pdf"
                }
                R.id.history_fragment -> {
                    supportActionBar?.setDisplayHomeAsUpEnabled(false)
                    binding.clToolbar.toolbarTitle.text = "History"
                }

                R.id.result_fragment -> {
                    supportActionBar?.setDisplayHomeAsUpEnabled(true)
                    binding.clToolbar.toolbarTitle.text = "Result"
                }
            }
        }

    }

    override fun onSupportNavigateUp(): Boolean {
        return navController.navigateUp() || super.onSupportNavigateUp()
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return super.onOptionsItemSelected(item)
    }

    override fun openCompressFragment(){
        supportActionBar?.setDisplayHomeAsUpEnabled(false)
        binding.clToolbar.toolbarTitle.text = "Compress Pdf"
        navController.popBackStack(R.id.compress_fragment,false)
    }

    override fun openHistoryFragment(){
        supportActionBar?.setDisplayHomeAsUpEnabled(false)
        binding.clToolbar.toolbarTitle.text = "History"
        navController.navigate(R.id.history_fragment)
    }

    override fun openResultFragment(status: Boolean, pdf: Pdf?, destFile: File?) {
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        if (isDarkMode){
            binding.clToolbar.toolbar.navigationIcon?.setTintList(ColorStateList.valueOf(Color.WHITE))
        } else {
            binding.clToolbar.toolbar.navigationIcon?.setTintList(ColorStateList.valueOf(Color.BLACK))
        }
        binding.clToolbar.toolbarTitle.text = "Result"
        for (i in 0 until binding.clToolbar.toolbar.menu.size()) {
            binding.clToolbar.toolbar.menu.getItem(i).isVisible = false
        }
        val bundle = Bundle()
        bundle.putBoolean("status",status)
        bundle.putString("pdfJson", Gson().toJson(pdf))
        bundle.putSerializable("file", destFile)
        navController.navigate(R.id.result_fragment,bundle)
    }
/**
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
*/
}