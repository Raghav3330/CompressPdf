package com.app.compress.pdf.stash.Activities;

import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Color;
import android.graphics.Rect;
import android.graphics.drawable.GradientDrawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;


import com.app.compress.pdf.stash.R;
import com.google.android.gms.ads.AdError;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.FullScreenContentCallback;
import com.google.android.gms.ads.LoadAdError;
import com.google.android.gms.ads.OnUserEarnedRewardListener;
import com.google.android.gms.ads.rewarded.RewardItem;
import com.google.android.gms.ads.rewardedinterstitial.RewardedInterstitialAd;
import com.google.android.gms.ads.rewardedinterstitial.RewardedInterstitialAdLoadCallback;
import com.google.android.material.appbar.AppBarLayout;
import com.google.android.material.appbar.CollapsingToolbarLayout;
import com.google.android.play.core.review.ReviewInfo;
import com.google.android.play.core.review.ReviewManager;
import com.google.android.play.core.review.ReviewManagerFactory;
import com.google.android.play.core.tasks.Task;

import androidx.annotation.NonNull;
import androidx.coordinatorlayout.widget.CoordinatorLayout;
import androidx.core.content.ContextCompat;
import androidx.core.content.FileProvider;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.util.DisplayMetrics;
import android.util.Log;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.DecelerateInterpolator;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;



import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.util.Calendar;
import java.util.concurrent.TimeUnit;



public class FinalResultScreen extends AppCompatActivity implements AppBarLayout.OnOffsetChangedListener {

    private static final String LOG_TAG = FinalResultScreen.class.getSimpleName();
    public static String FINAL_ACTIVITY_FINISHED = "final.activity.finished";
    private static int size = 4;
    final int APP_HINT_CARD_ADVANCE = 0;
    final int APP_HINT_CARD = 6;
    final int SHARE_APP_CARD = 1;
    final int FEEDBACK_CARD = 2;
    final int NEGATIVE_FEEDBACK_CARD = 4;
    final int POSITIVE_FEEDBACK_CARD = 5;
    final int FEEDBACK_THANK_YOU_CARD = 7;
    final int STASH_INSTALL_CARD = 8;
    final int BLANK_CARD = 9;

    TextView no_of_compressed_apps, size_saved_unit, app_size_saved, cache_size_saved, ram_size_saved;
    TextView size_of_compressed_apps, size_saved_no;
    ImageView back_btn, process_signifying_image;
    RelativeLayout final_result_image;
    CollapsingToolbarLayout collapse_toolbar;
    int feedbackFlag = 0;
    boolean advance_compression, clickedShareCard, clickedRatingCard, clickedInstallStashCard;

    //button click status
    boolean difficult_clicked = false, app_crashed_clicked = false, data_lost_clicked = false, slow_process_clicked = false, send_button_clicked = false, negative_feedback_cancel_pressed = false, clickedCancelInstall = false, mSelectedMayBLater = false;
    boolean abort = false;
    EditText negative_feedback_editText;
    private Bundle mGAParams;
    //colors
    private int blue, transparent, stroke, mAppTextColor;
    private int mStokeEditTextColor;
    //stroke of textview
    private int mStrokePxText;
    String mAppCompressed;
    androidx.appcompat.widget.Toolbar mToolBar;
    private int retryAttempt = 0;

    private TextView original,compressed,pdfFilename,pdfsize , percentagesaved;

    private Button openButton,shareButton;
    private RewardedInterstitialAd mInterstitialAd;
    private ReviewManager reviewManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.final_result_layout);

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                loadAd();
            }
        },5000);

        original = findViewById(R.id.original_size_saved);
        compressed = findViewById(R.id.compressed_size_saved);

        percentagesaved = findViewById(R.id.size_saved_no);

        pdfFilename = findViewById(R.id.finalpdfname);
        pdfsize = findViewById(R.id.finalpdfsize);

        openButton = findViewById(R.id.openButtonFinal);
        shareButton = findViewById(R.id.shareButtonFinal);

        reviewManager = ReviewManagerFactory.create(this);

        Bundle bundle = getIntent().getExtras();
        String destinationCheck = String.valueOf(bundle.get("dest"));

        String originalSize = bundle.getString("size_original");
        original.setText("Original Size - "+originalSize);

        File destinationFile = new File(destinationCheck);
        long compressedSize = destinationFile.length();

        compressed.setText("Compressed Size - "+ getSizeinMBOnly(compressedSize));

        String sourceCheck = String.valueOf(bundle.get("source"));
        File sourceFile = new File(sourceCheck);
        long sourceSize = sourceFile.length();

        double spaceSavdouble = ((sourceSize - compressedSize));
        double spaceSaved = (spaceSavdouble/(Long) sourceSize)*100;
        long l = (new Double(spaceSaved)).longValue();

        percentagesaved.setText(String.valueOf(l) + " %");

        String pdfname = bundle.getString("pdfname");
        pdfFilename.setText(pdfname);

        pdfsize.setText(getSizeinMBOnly(compressedSize));

        openButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d( "onClick: ", destinationFile.toString());
                Intent intent = new Intent(Intent.ACTION_VIEW);
                intent.setDataAndType(FileProvider.getUriForFile(FinalResultScreen.this,getApplicationContext().getPackageName()+".fileprovider",destinationFile), "application/pdf");
                intent.setFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);
                intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
                intent.addFlags(Intent.FLAG_GRANT_WRITE_URI_PERMISSION);
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);

                //Create Viewer Intent
                Intent viewerIntent = Intent.createChooser(intent, "Open PDF");
                startActivity(viewerIntent);
            }
        });

        shareButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Uri outputFile = FileProvider.getUriForFile(FinalResultScreen.this,getApplicationContext().getPackageName()+".fileprovider",destinationFile);

                Intent share = new Intent();

                share.setAction(Intent.ACTION_SEND);
                share.setType("application/pdf");
                share.putExtra(Intent.EXTRA_STREAM, outputFile);
//                    share.setPackage("com.whatsapp");

            //Share Intent
                Intent shareIntent = Intent.createChooser(share, "Share PDF");
                startActivity(shareIntent);
            }
        });


        mToolBar = findViewById(R.id.toolbar);
        mToolBar.setTitle(" PDF Compressed");
        setSupportActionBar(mToolBar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        size = 4;
//        blue = ContextCompat.getColor(FinalResultScreen.this, R.color.blue);
//        stroke = ContextCompat.getColor(FinalResultScreen.this, R.color.statusbar);
//        transparent = Color.TRANSPARENT;
//        mAppTextColor = ContextCompat.getColor(FinalResultScreen.this, R.color.app_text_color);
//        mStokeEditTextColor = ContextCompat.getColor(FinalResultScreen.this, R.color.fortyBlack);
        collapse_toolbar = findViewById(R.id.collapse_toolbar);

        Intent intent = getIntent();
        int apps_compressed = 0;
        long size_compressed_apps = 0;


        advance_compression = intent.getBooleanExtra("advance_compression", false);

        if (intent.getBooleanExtra("process_abort", false)) {

            no_of_compressed_apps.setText("Process Aborted!");
            try {
                apps_compressed = Integer.parseInt(mAppCompressed);
                size_compressed_apps = Long.parseLong(intent.getStringExtra("size_saved"));
            } catch (NumberFormatException e) {
                Log.d(LOG_TAG, e.getMessage(), e);
            }
            process_signifying_image.setVisibility(View.VISIBLE);
            final_result_image.setVisibility(View.GONE);
            abort = true;
            Log.d("finish", "onCreate: value of abort " + abort);

        } else {
            abort = false;
            Log.d("finish", "onCreate: value of abort " + abort);
            /*process_signifying_image.setVisibility(View.GONE);
            final_result_image.setVisibility(View.VISIBLE);*/
            try {
                apps_compressed = Integer.parseInt(mAppCompressed);
                size_compressed_apps = Long.parseLong(intent.getStringExtra("size_saved"));
            } catch (NumberFormatException e) {
                Log.d(LOG_TAG, e.getMessage(), e);
            }
            if (getIntent() != null) {

            }


        }
        if (apps_compressed == 0) {

        } else if (apps_compressed == 1) {

        } else {

        }

        AppBarLayout appbar = (AppBarLayout) findViewById(R.id.appbar);

        appbar.addOnOffsetChangedListener(this);

        RecyclerView recyclerView = (RecyclerView) findViewById(R.id.recycler_view);

        int space = (int) dipToPixels(this, 12);
        CoordinatorLayout.LayoutParams params =
                (CoordinatorLayout.LayoutParams) recyclerView.getLayoutParams();
        AppBarLayout.ScrollingViewBehavior behavior =
                (AppBarLayout.ScrollingViewBehavior) params.getBehavior();
        CompressedAdapter mAdapter = new CompressedAdapter();
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.addItemDecoration(new SpacesItemDecoration(space));
        recyclerView.setAdapter(mAdapter);

    }
    private void loadAd() {
        RewardedInterstitialAd.load(FinalResultScreen.this, "ca-app-pub-9668830280921241/4374208404",
                new AdRequest.Builder().build(),  new RewardedInterstitialAdLoadCallback() {
                    @Override
                    public void onAdLoaded(RewardedInterstitialAd ad) {
                        mInterstitialAd = ad;
                        Log.d("TAG == rk", "onAdLoaded");
                        mInterstitialAd.show(FinalResultScreen.this, new OnUserEarnedRewardListener() {
                            @Override
                            public void onUserEarnedReward(@NonNull RewardItem rewardItem) {
                            }
                        });
                        mInterstitialAd.setFullScreenContentCallback(new FullScreenContentCallback() {
                            /** Called when the ad failed to show full screen content. */
                            @Override
                            public void onAdFailedToShowFullScreenContent(AdError adError) {
                                Log.d("TAG == rk", "onAdFailedToShowFullScreenContent");
                                Log.d( "onAdFailedToShow : ",adError.getMessage().toString());
                            }
                            /** Called when ad showed the full screen content. */
                            @Override
                            public void onAdShowedFullScreenContent() {
                                Log.d("TAG == rk", "onAdShowedFullScreenContent");
                            }

                            /** Called when full screen content is dismissed. */
                            @Override
                            public void onAdDismissedFullScreenContent() {
                                Log.d("TAG == rk", "onAdDismissedFullScreenContent");
                            }
                        });
                    }
                    @Override
                    public void onAdFailedToLoad(LoadAdError loadAdError) {
                        Log.e("TAG", "onAdFailedToLoad");
                    }
                });
    }
    public static String getSizeinMBOnly(long prev_size) {
        int unit = 1000;
        if (prev_size > unit) {
            prev_size = prev_size / unit;
            if (prev_size > unit) {
                prev_size = prev_size / unit;

                return prev_size + " MB";
            }
            return prev_size + " KB";

        }
        return prev_size + " B";
    }







    @Override
    protected void onPause() {
        super.onPause();
        boolean isSeenTutorial = true;
        if (!isSeenTutorial) {

            if (!clickedRatingCard && !clickedShareCard && !clickedInstallStashCard) {
            }
        }
    }

    public float dipToPixels(Context context, float dipValue) {
        DisplayMetrics metrics = context.getResources().getDisplayMetrics();
        return TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dipValue, metrics);
    }

    @Override
    protected void onResume() {
        clickedRatingCard = false;
        clickedShareCard = false;
        super.onResume();
        Window window = getWindow();
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);

        window.getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_STABLE |
                View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN);

    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        return super.onCreateOptionsMenu(menu);
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        onBackPressed();
        return false;
    }
    @Override
    public void onBackPressed() {
        startActivity(new Intent(this, MainActivity.class));
        finish();
        //super.onBackPressed();

    }

    int mVerticalOffsetGlobal = -1;

    @Override
    public void onOffsetChanged(AppBarLayout appBarLayout, int verticalOffset) {

    }

    protected void setStatusBarTranslucent(boolean makeTranslucent) {
        if (makeTranslucent) {
            getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        } else {
            getWindow().clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        }
    }


    public class CompressedAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> implements View.OnClickListener {

        private static final int ANIMATED_ITEMS_COUNT = 2;
        public TextView send, cancel, difficult, app_crashed, data_lost, slow_process, sure, maybe;
        boolean animateItems = true;
        int lastAnimatedPosition = -1;

        @Override
        public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            switch (viewType) {
                case APP_HINT_CARD_ADVANCE:

                case APP_HINT_CARD:

                case STASH_INSTALL_CARD:

                case BLANK_CARD:
                    View blank = LayoutInflater.from(parent.getContext())
                            .inflate(R.layout.blank_row, parent, false);

                    return new BlankHolder(blank);

                case SHARE_APP_CARD:
                    View shareAppCard = LayoutInflater.from(parent.getContext())
                            .inflate(R.layout.share_app_card, parent, false);
                    return new ShareAppCardHolder(shareAppCard);

                case FEEDBACK_CARD:

                    View finalScreenRateCard = LayoutInflater.from(parent.getContext())
                            .inflate(R.layout.final_screen_rate_card, parent, false);

                    return new RatingCard(finalScreenRateCard);

                case NEGATIVE_FEEDBACK_CARD:

                    View finalScreenNegativeRateCard = LayoutInflater.from(parent.getContext())
                            .inflate(R.layout.negative_feedback_layout, parent, false);

                    return new FourthCard(finalScreenNegativeRateCard);

                case POSITIVE_FEEDBACK_CARD:

                    View finalScreenPositiveRateCard = LayoutInflater.from(parent.getContext())
                            .inflate(R.layout.positive_feedback_card, parent, false);

                    return new FifthCard(finalScreenPositiveRateCard);
                case FEEDBACK_THANK_YOU_CARD:
                    View feedBackThankYouCard = LayoutInflater.from(parent.getContext())
                            .inflate(R.layout.feedback_thank_you_card, parent, false);

                    return new FeedBackThankYouCard(feedBackThankYouCard);
            }
            return null;
        }

        private void runEnterAnimation(View view, int position) {

            if (position > lastAnimatedPosition) {
                Log.d("animation", "" + lastAnimatedPosition);
                lastAnimatedPosition = position;
                view.animate()
                        .translationY(0)
                        .setInterpolator(new DecelerateInterpolator(3.f))
                        .setDuration(2000)
                        .start();
            }
        }


        @Override
        public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {


            if (holder instanceof RatingCard) {

                RatingCard viewHolder = (RatingCard) holder;
                runEnterAnimation(viewHolder.card, position);
                viewHolder.negative.setOnClickListener(this);
                viewHolder.positive.setOnClickListener(this);
                return;
            }

            if (holder instanceof ShareAppCardHolder) {
                ShareAppCardHolder shareAppCardHolder = (ShareAppCardHolder) holder;
                runEnterAnimation(shareAppCardHolder.shareCard, position);
                shareAppCardHolder.shareButton.setOnClickListener(this);
                return;
            }

            if (holder instanceof FifthCard) {
                FifthCard viewHolder = (FifthCard) holder;
                sure.setOnClickListener(this);
                maybe.setOnClickListener(this);
                return;
            }

            if (holder instanceof FourthCard) {
                FourthCard viewHolder = (FourthCard) holder;
                send.setOnClickListener(this);
                cancel.setOnClickListener(this);
                difficult.setOnClickListener(this);
                app_crashed.setOnClickListener(this);
                data_lost.setOnClickListener(this);
                slow_process.setOnClickListener(this);

                negative_feedback_editText.setOnFocusChangeListener(new View.OnFocusChangeListener() {
                    @Override
                    public void onFocusChange(View v, boolean hasFocus) {
                        if (hasFocus) {
                            GradientDrawable drawable = (GradientDrawable) negative_feedback_editText.getBackground();
                            drawable.setStroke(mStrokePxText, mStokeEditTextColor);
                        } else {
                            GradientDrawable drawable = (GradientDrawable) negative_feedback_editText.getBackground();
                            //remove border
                            drawable.setStroke(0, transparent);
                        }
                    }
                });
            }

            if (holder instanceof AppHintAdvanceCard) {

                AppHintAdvanceCard appHintAdvanceCard = (AppHintAdvanceCard) holder;
                runEnterAnimation(appHintAdvanceCard.hintCard, position);
            }

            if (holder instanceof AppHintNormalCard) {

                AppHintNormalCard appHintNormalCard = (AppHintNormalCard) holder;
                runEnterAnimation(appHintNormalCard.hintCard, position);
            }

        }

        @Override
        public int getItemViewType(int position) {

            Log.d("pos", "" + position);
            if (abort) {
//Position 0 Compress Card
                if (position == 0) {
                    if (advance_compression)
                        return APP_HINT_CARD_ADVANCE;
                    else
                        return APP_HINT_CARD;
                }

                if (position == 1) {
                    if (!clickedCancelInstall && !false) {
                        return STASH_INSTALL_CARD;
                    } else {
                        return BLANK_CARD;
                    }

                }

                if (position == 2 && (negative_feedback_cancel_pressed || send_button_clicked)) {
                    if (send_button_clicked) {
                        return FEEDBACK_THANK_YOU_CARD;
                    }
                    return SHARE_APP_CARD;
                } else if (position == 2) {
                    return NEGATIVE_FEEDBACK_CARD;
                }

                if (position == 3) {
                    return SHARE_APP_CARD;
                }


            } else {
                //Position 0 Compress Card
                if (position == 1) {
                    if (advance_compression)
                        return APP_HINT_CARD_ADVANCE;
                    else
                        return APP_HINT_CARD;
                }
                //Position 1 Stash Install Card
                if (position == 0) {
                    if (!clickedCancelInstall && !false) {
                        return STASH_INSTALL_CARD;
                    } else {
                        return BLANK_CARD;

                    }
                }

                //Position 2 LIKE CARD

                if (position == 2) {
                    if (!mSelectedMayBLater) {
                        if (feedbackFlag == 0) {
                            return FEEDBACK_CARD;
                        } else if (feedbackFlag == 1) {
                            return NEGATIVE_FEEDBACK_CARD;
                        } else if (feedbackFlag == 2) {
//                            return POSITIVE_FEEDBACK_CARD;
                        } else if (feedbackFlag == 3) {
                            return FEEDBACK_THANK_YOU_CARD;
                        }
                    } else {
                        return BLANK_CARD;
                    }
                }
                //Position 3 SHARE CARD

                if (position == 3) {
                    return SHARE_APP_CARD;
                }
            }


            return position;
        }

        @Override
        public int getItemCount() {

            Log.d("FinalScreen", size + "");
            return size;
        }

        @Override
        public void onClick(View v) {
            switch (v.getId()) {

                case R.id.positive:

//                    feedbackFlag = 2;
//                    notifyItemChanged(FEEDBACK_CARD);

                    launchMarket();
                    clickedRatingCard = true;
                    notifyItemRemoved(2);
                    notifyItemRangeChanged(0, size);

                    break;
                case R.id.negative:

                    feedbackFlag = 1;
                    notifyItemChanged(FEEDBACK_CARD);
                    break;

                case R.id.send:
                    send_button_clicked = true;



                    if (difficult_clicked || app_crashed_clicked || data_lost_clicked || slow_process_clicked || (negative_feedback_editText.getText().length() > 0)) {


                        if (difficult_clicked) {

                        }

                        if (app_crashed_clicked) {

                        }

                        if (data_lost_clicked) {

                        }
                        if (slow_process_clicked) {

                        }

                        if (negative_feedback_editText.getText().length() > 0) {

                        }
                        if (abort) {
                            notifyItemRemoved(1);
                        } else {
                            notifyItemRemoved(2);
                        }
                        notifyItemRangeChanged(0, size);
                        feedbackFlag = 3;
                    } else {
                        Toast.makeText(FinalResultScreen.this, "Please choose a cause", Toast.LENGTH_SHORT).show();
                    }

                    try {
                        InputMethodManager inputManager =
                                (InputMethodManager) FinalResultScreen.this.
                                        getSystemService(Context.INPUT_METHOD_SERVICE);
                        inputManager.hideSoftInputFromWindow(
                                FinalResultScreen.this.getCurrentFocus().getWindowToken(),
                                InputMethodManager.HIDE_NOT_ALWAYS);
                    } catch (Exception e) {
                        Log.d("FinalResult", Log.getStackTraceString(e));
                    }
                    mSelectedMayBLater = true;

                    break;


                case R.id.maybe:

                    mSelectedMayBLater = true;

                case R.id.cancel:
                    mSelectedMayBLater = true;
                    if (abort) {
                        negative_feedback_cancel_pressed = true;
                        notifyItemRemoved(1);
                    } else {
                        notifyItemRemoved(2);
                    }
                    notifyItemRangeChanged(0, size);
                    break;


                case R.id.difficult:
                    if (difficult_clicked) {
                        //unselected
                        difficult_clicked = false;
                        GradientDrawable drawable = (GradientDrawable) difficult.getBackground();
                        drawable.setColor(transparent); // set solid color
                        drawable.setStroke(mStrokePxText, stroke);
                        difficult.setTextColor(mAppTextColor);

                    } else {
                        //selected
                        difficult_clicked = true;
                        GradientDrawable drawable = (GradientDrawable) difficult.getBackground();
                        drawable.setColor(blue); // set solid color
                        drawable.setStroke(0, transparent);
                        difficult.setTextColor(Color.WHITE);
                    }

                    break;
                case R.id.app_crashed:
                    if (app_crashed_clicked) {
                        //unselected
                        app_crashed_clicked = false;
                        GradientDrawable drawable = (GradientDrawable) app_crashed.getBackground();
                        drawable.setColor(transparent); // set solid color
                        drawable.setStroke(mStrokePxText, stroke);
                        app_crashed.setTextColor(mAppTextColor);

                    } else {
                        //selected
                        app_crashed_clicked = true;
                        GradientDrawable drawable = (GradientDrawable) app_crashed.getBackground();
                        drawable.setColor(blue); // set solid color
                        drawable.setStroke(0, transparent);
                        app_crashed.setTextColor(Color.WHITE);
                    }
                    break;
                case R.id.data_lost:
                    if (data_lost_clicked) {
                        //unselected
                        data_lost_clicked = false;
                        GradientDrawable drawable = (GradientDrawable) data_lost.getBackground();
                        drawable.setColor(transparent); // set solid color
                        drawable.setStroke(mStrokePxText, stroke);
                        data_lost.setTextColor(mAppTextColor);
                    } else {
                        //selected
                        data_lost_clicked = true;
                        GradientDrawable drawable = (GradientDrawable) data_lost.getBackground();
                        drawable.setColor(blue); // set solid color
                        drawable.setStroke(0, transparent);
                        data_lost.setTextColor(Color.WHITE);
                    }
                    break;
                case R.id.slow_process:
                    if (slow_process_clicked) {
                        //unselected
                        slow_process_clicked = false;
                        GradientDrawable drawable = (GradientDrawable) slow_process.getBackground();
                        drawable.setColor(transparent); // set solid color
                        drawable.setStroke(mStrokePxText, stroke);
                        slow_process.setTextColor(mAppTextColor);
                    } else {
                        //selected
                        slow_process_clicked = true;
                        GradientDrawable drawable = (GradientDrawable) slow_process.getBackground();
                        drawable.setColor(blue); // set solid color
                        drawable.setStroke(0, transparent);
                        slow_process.setTextColor(Color.WHITE);
                    }
                    break;
                case R.id.negative_feedback_editText:

                    break;

                case R.id.sure:
                    launchMarket();
                    clickedRatingCard = true;
                    notifyItemRemoved(2);
                    notifyItemRangeChanged(0, size);

                    break;


                case R.id.share_button:

                    clickedShareCard = true;
                    Intent intent = new Intent(Intent.ACTION_SEND);
                    intent.putExtra(Intent.EXTRA_TEXT, "https://play.google.com/store/apps/details?id=com.app.compress.pdf.stash&referrer=utm_source%3Dshare%26utm_medium%3Dreferral%26utm_campaign%3Dapp%2520sharing");
                    intent.setType("text/plain");
                    FinalResultScreen.this.startActivity(Intent.createChooser(intent,"Help your friends save storage"));
                    break;

            }

        }


        private void launchMarket() {
//            Uri uri = Uri.parse("market://details?id=" + getPackageName());
//            Intent myAppLinkToMarket = new Intent(Intent.ACTION_VIEW, uri);
//            try {
//                startActivity(myAppLinkToMarket);
//            } catch (ActivityNotFoundException e) {
//                Log.d("FinalResultScreen", "play store redirection failed ");
//            }

            Task<ReviewInfo> request = reviewManager.requestReviewFlow();
            request.addOnCompleteListener(task -> {
                if (task.isSuccessful()) {
                    // Getting the ReviewInfo object
                    ReviewInfo reviewInfo = task.getResult();

                    Task<Void> flow = reviewManager.launchReviewFlow(FinalResultScreen.this, reviewInfo);
                    flow.addOnCompleteListener(task1 -> {
                        // The flow has finished. The API does not indicate whether the user
                        // reviewed or not, or even whether the review dialog was shown.
                    });
                }
            });
        }

        class AppHintAdvanceCard extends RecyclerView.ViewHolder {

            CardView hintCard;

            public AppHintAdvanceCard(View itemView) {
                super(itemView);
//                hintCard = (CardView) itemView.findViewById(R.id.hint_card);
            }
        }

        class AppHintNormalCard extends RecyclerView.ViewHolder {

            CardView hintCard;

            public AppHintNormalCard(View itemView) {
                super(itemView);
//                hintCard = (CardView) itemView.findViewById(R.id.hint_card);
            }
        }

        private class FeedBackThankYouCard extends RecyclerView.ViewHolder {

            public FeedBackThankYouCard(View itemView) {
                super(itemView);
            }
        }

        private class ShareAppCardHolder extends RecyclerView.ViewHolder {

            TextView shareButton;
            CardView shareCard;

            public ShareAppCardHolder(View itemView) {
                super(itemView);
                shareCard = (CardView) itemView.findViewById(R.id.share_card);
                shareButton = (TextView) itemView.findViewById(R.id.share_button);
            }
        }

        private class StashCardHolder extends RecyclerView.ViewHolder {

//            TextView shareButton;
//            CardView shareCard;

            public StashCardHolder(View itemView) {
                super(itemView);
//                shareCard = (CardView) itemView.findViewById(R.id.share_card);
//                shareButton = (TextView) itemView.findViewById(R.id.share_button);
            }
        }

        private class BlankHolder extends RecyclerView.ViewHolder {

            public BlankHolder(View itemView) {
                super(itemView);
            }
        }

        public class SecondCard extends RecyclerView.ViewHolder {
            public TextView title, year, genre;

            public SecondCard(View view) {
                super(view);
                /*title = (TextView) view.findViewById(R.id.title);
                genre = (TextView) view.findViewById(R.id.genre);
                year = (TextView) view.findViewById(R.id.year);*/
            }
        }

        public class RatingCard extends RecyclerView.ViewHolder {
            public TextView negative, positive;
            public CardView card;

            public RatingCard(View view) {
                super(view);
                card = (CardView) view.findViewById(R.id.card_view);
                negative = (TextView) view.findViewById(R.id.negative);
                positive = (TextView) view.findViewById(R.id.positive);
                //year = (TextView) view.findViewById(R.id.year);
            }
        }

        public class FourthCard extends RecyclerView.ViewHolder {


            public FourthCard(View view) {
                super(view);
                send = (TextView) view.findViewById(R.id.send);
                cancel = (TextView) view.findViewById(R.id.cancel);
                difficult = (TextView) view.findViewById(R.id.difficult);
                app_crashed = (TextView) view.findViewById(R.id.app_crashed);
                data_lost = (TextView) view.findViewById(R.id.data_lost);
                slow_process = (TextView) view.findViewById(R.id.slow_process);
                negative_feedback_editText = (EditText) view.findViewById(R.id.negative_feedback_editText);
            }
        }

        public class FifthCard extends RecyclerView.ViewHolder {

            public FifthCard(View view) {
                super(view);
                sure = (TextView) view.findViewById(R.id.sure);
                maybe = (TextView) view.findViewById(R.id.maybe);

            }
        }
    }

//    private void showCompressedAppTutorial() {
//        boolean isSeenTutorial = PrefManager.getInstance(getApplicationContext()).getBoolean(PrefManager.SEEN_MANAGE_APPS_TUTORIAL);
//        if (isSeenTutorial) {
//
//            startActivity(new Intent(FinalResultScreen.this, ManageApps.class));
//        } else {
//
//            finish();
//            UtilityMethods.getInstance().addShortcut(getApplicationContext());
//
//            PrefManager.getInstance(getApplicationContext()).putBoolean(PrefManager.SEEN_MANAGE_APPS_TUTORIAL, true);
//            Intent startMain = new Intent(Intent.ACTION_MAIN);
//            startMain.addCategory(Intent.CATEGORY_HOME);
//            startMain.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//            startActivity(startMain);
//            new Handler().postDelayed(new Runnable() {
//                @Override
//                public void run() {
//                    Intent i = new Intent(FinalResultScreen.this, FolderDiscoveryPopup.class);
//                    i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//                    startActivity(i);
//
//                }
//            }, 700);
//        }
//    }


    private class SpacesItemDecoration extends RecyclerView.ItemDecoration {
        private int space = 12;

        public SpacesItemDecoration(int space) {
            this.space = space;
        }

        @Override
        public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
            if (parent.getChildAdapterPosition(view) == state.getItemCount() - 1) {
                outRect.bottom = space;
                outRect.top = 0; //don't forget about recycling...
            }
        }
    }

}
