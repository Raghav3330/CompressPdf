package com.app.compress.pdf.stash.Activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.cardview.widget.CardView;
import androidx.coordinatorlayout.widget.CoordinatorLayout;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Rect;
import android.graphics.drawable.GradientDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.util.DisplayMetrics;
import android.util.Log;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.DecelerateInterpolator;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

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
import com.google.android.play.core.review.ReviewInfo;
import com.google.android.play.core.review.ReviewManager;
import com.google.android.play.core.review.ReviewManagerFactory;
import com.google.android.play.core.tasks.Task;

public class Final_resultfail extends AppCompatActivity implements AppBarLayout.OnOffsetChangedListener {

    private TextView original,compressed;

    private Toolbar mToolbar;

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

    boolean advance_compression, clickedShareCard, clickedRatingCard, clickedInstallStashCard;
    int feedbackFlag = 0;

    private int blue, transparent, stroke, mAppTextColor;
    private int mStokeEditTextColor;
    //stroke of textview
    private int mStrokePxText;

    //button click status
    boolean difficult_clicked = false, app_crashed_clicked = false, data_lost_clicked = false, slow_process_clicked = false, send_button_clicked = false, negative_feedback_cancel_pressed = false, clickedCancelInstall = false, mSelectedMayBLater = false;
    boolean abort = false;
    EditText negative_feedback_editText;
    private Toolbar mToolBar;
    private RewardedInterstitialAd mInterstitialAd;
    private ReviewManager reviewManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_final_result_fail);

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                loadAd();
            }
        },5000);

        reviewManager = ReviewManagerFactory.create(this);

        mToolbar = findViewById(R.id.toolbar);
        mToolbar.setTitle(" PDF Compressed");
        setSupportActionBar(mToolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        original = findViewById(R.id.original_size_saved_fail);
        compressed = findViewById(R.id.compressed_size_saved_fail);

        Bundle bundle = getIntent().getExtras();
        String originalSize = bundle.getString("size_original");
        original.setText("Original Size - "+originalSize);

        compressed.setText("Compressed Size - N/A");

        mToolBar = findViewById(R.id.toolbar);
        mToolBar.setTitle(" PDF Compressed");
        setSupportActionBar(mToolBar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        size = 4;

        Intent intent = getIntent();
        int apps_compressed = 0;
        long size_compressed_apps = 0;


        advance_compression = intent.getBooleanExtra("advance_compression", false);

        if (intent.getBooleanExtra("process_abort", false)) {
        } else {
            abort = false;
            Log.d("finish", "onCreate: value of abort " + abort);
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
        Final_resultfail.CompressedAdapter mAdapter = new Final_resultfail.CompressedAdapter();
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.addItemDecoration(new Final_resultfail.SpacesItemDecoration(space));
        recyclerView.setAdapter(mAdapter);
    }
    private void loadAd() {
        RewardedInterstitialAd.load(Final_resultfail.this, "ca-app-pub-9668830280921241/4374208404",
                new AdRequest.Builder().build(),  new RewardedInterstitialAdLoadCallback() {
                    @Override
                    public void onAdLoaded(RewardedInterstitialAd ad) {
                        mInterstitialAd = ad;
                        Log.d("TAG == rk", "onAdLoaded");
                        mInterstitialAd.show(Final_resultfail.this, new OnUserEarnedRewardListener() {
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


    public float dipToPixels(Context context, float dipValue) {
        DisplayMetrics metrics = context.getResources().getDisplayMetrics();
        return TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dipValue, metrics);
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

    @Override
    public void onOffsetChanged(AppBarLayout appBarLayout, int verticalOffset) {

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

                    return new Final_resultfail.CompressedAdapter.BlankHolder(blank);

                case SHARE_APP_CARD:
                    View shareAppCard = LayoutInflater.from(parent.getContext())
                            .inflate(R.layout.share_app_card, parent, false);
                    return new Final_resultfail.CompressedAdapter.ShareAppCardHolder(shareAppCard);

                case FEEDBACK_CARD:

                    View finalScreenRateCard = LayoutInflater.from(parent.getContext())
                            .inflate(R.layout.final_screen_rate_card, parent, false);

                    return new Final_resultfail.CompressedAdapter.RatingCard(finalScreenRateCard);

                case NEGATIVE_FEEDBACK_CARD:

                    View finalScreenNegativeRateCard = LayoutInflater.from(parent.getContext())
                            .inflate(R.layout.negative_feedback_layout, parent, false);

                    return new Final_resultfail.CompressedAdapter.FourthCard(finalScreenNegativeRateCard);

                case POSITIVE_FEEDBACK_CARD:

                    View finalScreenPositiveRateCard = LayoutInflater.from(parent.getContext())
                            .inflate(R.layout.positive_feedback_card, parent, false);

                    return new Final_resultfail.CompressedAdapter.FifthCard(finalScreenPositiveRateCard);
                case FEEDBACK_THANK_YOU_CARD:
                    View feedBackThankYouCard = LayoutInflater.from(parent.getContext())
                            .inflate(R.layout.feedback_thank_you_card, parent, false);

                    return new Final_resultfail.CompressedAdapter.FeedBackThankYouCard(feedBackThankYouCard);
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


            if (holder instanceof Final_resultfail.CompressedAdapter.RatingCard) {

                Final_resultfail.CompressedAdapter.RatingCard viewHolder = (Final_resultfail.CompressedAdapter.RatingCard) holder;
                runEnterAnimation(viewHolder.card, position);
                viewHolder.negative.setOnClickListener(this);
                viewHolder.positive.setOnClickListener(this);
                return;
            }

            if (holder instanceof Final_resultfail.CompressedAdapter.ShareAppCardHolder) {
                Final_resultfail.CompressedAdapter.ShareAppCardHolder shareAppCardHolder = (Final_resultfail.CompressedAdapter.ShareAppCardHolder) holder;
                runEnterAnimation(shareAppCardHolder.shareCard, position);
                shareAppCardHolder.shareButton.setOnClickListener(this);
                return;
            }

            if (holder instanceof Final_resultfail.CompressedAdapter.FifthCard) {
                Final_resultfail.CompressedAdapter.FifthCard viewHolder = (Final_resultfail.CompressedAdapter.FifthCard) holder;
                sure.setOnClickListener(this);
                maybe.setOnClickListener(this);
                return;
            }

            if (holder instanceof Final_resultfail.CompressedAdapter.FourthCard) {
                Final_resultfail.CompressedAdapter.FourthCard viewHolder = (Final_resultfail.CompressedAdapter.FourthCard) holder;
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

            if (holder instanceof Final_resultfail.CompressedAdapter.AppHintAdvanceCard) {

                Final_resultfail.CompressedAdapter.AppHintAdvanceCard appHintAdvanceCard = (Final_resultfail.CompressedAdapter.AppHintAdvanceCard) holder;
                runEnterAnimation(appHintAdvanceCard.hintCard, position);
            }

            if (holder instanceof Final_resultfail.CompressedAdapter.AppHintNormalCard) {

                Final_resultfail.CompressedAdapter.AppHintNormalCard appHintNormalCard = (Final_resultfail.CompressedAdapter.AppHintNormalCard) holder;
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
                        Toast.makeText(Final_resultfail.this, "Please choose a cause", Toast.LENGTH_SHORT).show();
                    }

                    try {
                        InputMethodManager inputManager =
                                (InputMethodManager) Final_resultfail.this.
                                        getSystemService(Context.INPUT_METHOD_SERVICE);
                        inputManager.hideSoftInputFromWindow(
                                Final_resultfail.this.getCurrentFocus().getWindowToken(),
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
                    Final_resultfail.this.startActivity(Intent.createChooser(intent,"Help your friends save storage"));
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

                    Task<Void> flow = reviewManager.launchReviewFlow(Final_resultfail.this, reviewInfo);
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