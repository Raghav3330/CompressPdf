package com.app.compress.pdf.stash.ui.activity

import android.content.Context
import android.content.Intent
import android.graphics.Rect
import android.graphics.drawable.GradientDrawable
import android.os.Bundle
import android.os.Handler
import android.util.Log
import android.util.TypedValue
import android.view.LayoutInflater
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.view.View.OnFocusChangeListener
import android.view.ViewGroup
import android.view.WindowManager
import android.view.animation.DecelerateInterpolator
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.RelativeLayout
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.cardview.widget.CardView
import androidx.coordinatorlayout.widget.CoordinatorLayout
import androidx.core.content.FileProvider
import androidx.recyclerview.widget.DefaultItemAnimator
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.ItemDecoration
import com.app.compress.pdf.stash.R
import com.google.android.gms.ads.AdError
import com.google.android.gms.ads.AdRequest
import com.google.android.gms.ads.FullScreenContentCallback
import com.google.android.gms.ads.LoadAdError
import com.google.android.gms.ads.rewardedinterstitial.RewardedInterstitialAd
import com.google.android.gms.ads.rewardedinterstitial.RewardedInterstitialAdLoadCallback
import com.google.android.material.appbar.AppBarLayout
import com.google.android.material.appbar.AppBarLayout.OnOffsetChangedListener
import com.google.android.material.appbar.CollapsingToolbarLayout
import com.google.android.play.core.review.ReviewInfo
import com.google.android.play.core.review.ReviewManager
import com.google.android.play.core.review.ReviewManagerFactory
import com.google.android.play.core.tasks.Task
import java.io.File

class FinalResultScreen : AppCompatActivity(), OnOffsetChangedListener {
    val APP_HINT_CARD_ADVANCE = 0
    val APP_HINT_CARD = 6
    val SHARE_APP_CARD = 1
    val FEEDBACK_CARD = 2
    val NEGATIVE_FEEDBACK_CARD = 4
    val POSITIVE_FEEDBACK_CARD = 5
    val FEEDBACK_THANK_YOU_CARD = 7
    val STASH_INSTALL_CARD = 8
    val BLANK_CARD = 9
    var no_of_compressed_apps: TextView? = null
    var size_saved_unit: TextView? = null
    var app_size_saved: TextView? = null
    var cache_size_saved: TextView? = null
    var ram_size_saved: TextView? = null
    var size_of_compressed_apps: TextView? = null
    var size_saved_no: TextView? = null
    var back_btn: ImageView? = null
    var process_signifying_image: ImageView? = null
    var final_result_image: RelativeLayout? = null
    var collapse_toolbar: CollapsingToolbarLayout? = null
    var feedbackFlag = 0
    var advance_compression = false
    var clickedShareCard = false
    var clickedRatingCard = false
    var clickedInstallStashCard = false

    //button click status
    var difficult_clicked = false
    var app_crashed_clicked = false
    var data_lost_clicked = false
    var slow_process_clicked = false
    var send_button_clicked = false
    var negative_feedback_cancel_pressed = false
    var clickedCancelInstall = false
    var mSelectedMayBLater = false
    var abort = false
    lateinit var negative_feedback_editText: EditText
    private val mGAParams: Bundle? = null

    //colors
    private val blue = 0
    private val transparent = 0
    private val stroke = 0
    private val mAppTextColor = 0
    private val mStokeEditTextColor = 0

    //stroke of textview
    private val mStrokePxText = 0
    var mAppCompressed: String? = null
    lateinit var mToolBar: Toolbar
    private val retryAttempt = 0
    private lateinit var original: TextView
    private lateinit var compressed: TextView
    private lateinit var pdfFilename: TextView
    private lateinit var pdfsize: TextView
    private lateinit var percentagesaved: TextView
    private lateinit var openButton: Button
    private lateinit var shareButton: Button
    private var mInterstitialAd: RewardedInterstitialAd? = null
    private var reviewManager: ReviewManager? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.final_result_layout)
        Handler().postDelayed({ loadAd() }, 5000)
        original = findViewById(R.id.original_size_saved)
        compressed = findViewById(R.id.compressed_size_saved)
        percentagesaved = findViewById(R.id.size_saved_no)
        pdfFilename = findViewById(R.id.finalpdfname)
        pdfsize = findViewById(R.id.finalpdfsize)
        openButton = findViewById(R.id.openButtonFinal)
        shareButton = findViewById(R.id.shareButtonFinal)
        reviewManager = ReviewManagerFactory.create(this)
        val bundle = intent.extras
        val destinationCheck = bundle!!["dest"].toString()
        val originalSize = bundle.getString("size_original")
        original.setText("Original Size - $originalSize")
        val destinationFile = File(destinationCheck)
        val compressedSize = destinationFile.length()
        compressed.setText("Compressed Size - " + getSizeinMBOnly(compressedSize))
        val sourceCheck = bundle["source"].toString()
        val sourceFile = File(sourceCheck)
        val sourceSize = sourceFile.length()
        val spaceSavdouble = (sourceSize - compressedSize).toDouble()
        val spaceSaved = spaceSavdouble / sourceSize * 100
        val l = spaceSaved.toLong()
        percentagesaved.setText("$l %")
        val pdfname = bundle.getString("pdfname")
        pdfFilename.setText(pdfname)
        pdfsize.setText(getSizeinMBOnly(compressedSize))
        openButton.setOnClickListener(View.OnClickListener {
            Log.d("onClick: ", destinationFile.toString())
            val intent = Intent(Intent.ACTION_VIEW)
            intent.setDataAndType(
                FileProvider.getUriForFile(
                    this@FinalResultScreen,
                    applicationContext.packageName + ".fileprovider",
                    destinationFile
                ), "application/pdf"
            )
            intent.setFlags(Intent.FLAG_ACTIVITY_NO_HISTORY)
            intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
            intent.addFlags(Intent.FLAG_GRANT_WRITE_URI_PERMISSION)
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)

            //Create Viewer Intent
            val viewerIntent = Intent.createChooser(intent, "Open PDF")
            startActivity(viewerIntent)
        })
        shareButton.setOnClickListener(View.OnClickListener {
            val outputFile = FileProvider.getUriForFile(
                this@FinalResultScreen,
                applicationContext.packageName + ".fileprovider",
                destinationFile
            )
            val share = Intent()
            share.setAction(Intent.ACTION_SEND)
            share.setType("application/pdf")
            share.putExtra(Intent.EXTRA_STREAM, outputFile)
            //                    share.setPackage("com.whatsapp");

            //Share Intent
            val shareIntent = Intent.createChooser(share, "Share PDF")
            startActivity(shareIntent)
        })
        mToolBar = findViewById(R.id.toolbar)
        mToolBar.setTitle(" PDF Compressed")
        setSupportActionBar(mToolBar)
        supportActionBar!!.setDisplayHomeAsUpEnabled(true)
        size = 4
        //        blue = ContextCompat.getColor(FinalResultScreen.this, R.color.blue);
//        stroke = ContextCompat.getColor(FinalResultScreen.this, R.color.statusbar);
//        transparent = Color.TRANSPARENT;
//        mAppTextColor = ContextCompat.getColor(FinalResultScreen.this, R.color.app_text_color);
//        mStokeEditTextColor = ContextCompat.getColor(FinalResultScreen.this, R.color.fortyBlack);
        collapse_toolbar = findViewById(R.id.collapse_toolbar)
        val intent = intent
        var apps_compressed = 0
        var size_compressed_apps: Long = 0
        advance_compression = intent.getBooleanExtra("advance_compression", false)
        if (intent.getBooleanExtra("process_abort", false)) {
            no_of_compressed_apps!!.text = "Process Aborted!"
            try {
                apps_compressed = mAppCompressed!!.toInt()
                size_compressed_apps = intent.getStringExtra("size_saved")!!.toLong()
            } catch (e: NumberFormatException) {
                Log.d(LOG_TAG, e.message, e)
            }
            process_signifying_image!!.setVisibility(View.VISIBLE)
            final_result_image!!.visibility = View.GONE
            abort = true
            Log.d("finish", "onCreate: value of abort $abort")
        } else {
            abort = false
            Log.d("finish", "onCreate: value of abort $abort")
            /*process_signifying_image.setVisibility(View.GONE);
            final_result_image.setVisibility(View.VISIBLE);*/try {
                apps_compressed = mAppCompressed!!.toInt()
                size_compressed_apps = intent.getStringExtra("size_saved")!!.toLong()
            } catch (e: NumberFormatException) {
                Log.d(LOG_TAG, e.message, e)
            }
            if (getIntent() != null) {
            }
        }
        if (apps_compressed == 0) {
        } else if (apps_compressed == 1) {
        } else {
        }
        val appbar = findViewById<View>(R.id.appbar) as AppBarLayout
        appbar.addOnOffsetChangedListener(this)
        val recyclerView = findViewById<View>(R.id.recycler_view) as RecyclerView
        val space = dipToPixels(this, 12f).toInt()
        val params = recyclerView.layoutParams as CoordinatorLayout.LayoutParams
        val behavior = params.behavior as AppBarLayout.ScrollingViewBehavior?
        val mAdapter: CompressedAdapter = CompressedAdapter()
        val mLayoutManager: RecyclerView.LayoutManager = LinearLayoutManager(this)
        recyclerView.setLayoutManager(mLayoutManager)
        recyclerView.setItemAnimator(DefaultItemAnimator())
        recyclerView.addItemDecoration(SpacesItemDecoration(space))
        recyclerView.setAdapter(mAdapter)
    }

    private fun loadAd() {
        RewardedInterstitialAd.load(this@FinalResultScreen,
            "ca-app-pub-9668830280921241/4374208404",
            AdRequest.Builder().build(),
            object : RewardedInterstitialAdLoadCallback() {
                override fun onAdLoaded(ad: RewardedInterstitialAd) {
                    mInterstitialAd = ad
                    Log.d("TAG == rk", "onAdLoaded")
                    mInterstitialAd!!.show(this@FinalResultScreen) { }
                    mInterstitialAd!!.fullScreenContentCallback =
                        object : FullScreenContentCallback() {
                            /** Called when the ad failed to show full screen content.  */
                            override fun onAdFailedToShowFullScreenContent(adError: AdError) {
                                Log.d("TAG == rk", "onAdFailedToShowFullScreenContent")
                                Log.d("onAdFailedToShow : ", adError.message.toString())
                            }

                            /** Called when ad showed the full screen content.  */
                            override fun onAdShowedFullScreenContent() {
                                Log.d("TAG == rk", "onAdShowedFullScreenContent")
                            }

                            /** Called when full screen content is dismissed.  */
                            override fun onAdDismissedFullScreenContent() {
                                Log.d("TAG == rk", "onAdDismissedFullScreenContent")
                            }
                        }
                }

                override fun onAdFailedToLoad(loadAdError: LoadAdError) {
                    Log.e("TAG", "onAdFailedToLoad")
                }
            })
    }

    override fun onPause() {
        super.onPause()
        val isSeenTutorial = true
        if (!isSeenTutorial) {
            if (!clickedRatingCard && !clickedShareCard && !clickedInstallStashCard) {
            }
        }
    }

    fun dipToPixels(context: Context, dipValue: Float): Float {
        val metrics = context.resources.displayMetrics
        return TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dipValue, metrics)
    }

    override fun onResume() {
        clickedRatingCard = false
        clickedShareCard = false
        super.onResume()
        val window = window
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS)
        window.decorView.systemUiVisibility = View.SYSTEM_UI_FLAG_LAYOUT_STABLE or
                View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        onBackPressed()
        return false
    }

    override fun onBackPressed() {
        super.onBackPressed()
        startActivity(Intent(this, MainActivity::class.java))
        finish()
        //super.onBackPressed();
    }

    var mVerticalOffsetGlobal = -1
    override fun onOffsetChanged(appBarLayout: AppBarLayout, verticalOffset: Int) {}
    protected fun setStatusBarTranslucent(makeTranslucent: Boolean) {
        if (makeTranslucent) {
            window.addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS)
        } else {
            window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS)
        }
    }

    inner class CompressedAdapter : RecyclerView.Adapter<RecyclerView.ViewHolder?>(),
        View.OnClickListener {
        lateinit var send: TextView
        lateinit var cancel: TextView
        lateinit var difficult: TextView
        lateinit var app_crashed: TextView
        lateinit var data_lost: TextView
        lateinit var slow_process: TextView
        lateinit var sure: TextView
        lateinit var maybe: TextView
        var animateItems = true
        var lastAnimatedPosition = -1
        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
            when (viewType) {
                APP_HINT_CARD_ADVANCE, APP_HINT_CARD, STASH_INSTALL_CARD, BLANK_CARD -> {
                    val blank = LayoutInflater.from(parent.context)
                        .inflate(R.layout.blank_row, parent, false)
                    return BlankHolder(blank)
                }

                SHARE_APP_CARD -> {
                    val shareAppCard = LayoutInflater.from(parent.context)
                        .inflate(R.layout.share_app_card, parent, false)
                    return ShareAppCardHolder(shareAppCard)
                }

                FEEDBACK_CARD -> {
                    val finalScreenRateCard = LayoutInflater.from(parent.context)
                        .inflate(R.layout.final_screen_rate_card, parent, false)
                    return RatingCard(finalScreenRateCard)
                }

                NEGATIVE_FEEDBACK_CARD -> {
                    val finalScreenNegativeRateCard = LayoutInflater.from(parent.context)
                        .inflate(R.layout.negative_feedback_layout, parent, false)
                    return FourthCard(finalScreenNegativeRateCard)
                }

                POSITIVE_FEEDBACK_CARD -> {
                    val finalScreenPositiveRateCard = LayoutInflater.from(parent.context)
                        .inflate(R.layout.positive_feedback_card, parent, false)
                    return FifthCard(finalScreenPositiveRateCard)
                }

                FEEDBACK_THANK_YOU_CARD -> {
                    val feedBackThankYouCard = LayoutInflater.from(parent.context)
                        .inflate(R.layout.feedback_thank_you_card, parent, false)
                    return FeedBackThankYouCard(feedBackThankYouCard)
                }
            }
            val feedBackThankYouCard = LayoutInflater.from(parent.context)
                .inflate(R.layout.feedback_thank_you_card, parent, false)
            return FeedBackThankYouCard(feedBackThankYouCard)
        }

        private fun runEnterAnimation(view: View?, position: Int) {
            if (position > lastAnimatedPosition) {
                Log.d("animation", "" + lastAnimatedPosition)
                lastAnimatedPosition = position
                view!!.animate()
                    .translationY(0f)
                    .setInterpolator(DecelerateInterpolator(3f))
                    .setDuration(2000)
                    .start()
            }
        }

        override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
            if (holder is RatingCard) {
                val viewHolder = holder
                runEnterAnimation(viewHolder.card, position)
                viewHolder.negative.setOnClickListener(this)
                viewHolder.positive.setOnClickListener(this)
                return
            }
            if (holder is ShareAppCardHolder) {
                val shareAppCardHolder = holder
                runEnterAnimation(shareAppCardHolder.shareCard, position)
                shareAppCardHolder.shareButton.setOnClickListener(this)
                return
            }
            if (holder is FifthCard) {
                val viewHolder = holder
                sure.setOnClickListener(this)
                maybe.setOnClickListener(this)
                return
            }
            if (holder is FourthCard) {
                val viewHolder = holder
                send.setOnClickListener(this)
                cancel.setOnClickListener(this)
                difficult.setOnClickListener(this)
                app_crashed.setOnClickListener(this)
                data_lost.setOnClickListener(this)
                slow_process.setOnClickListener(this)
                negative_feedback_editText.onFocusChangeListener =
                    OnFocusChangeListener { v, hasFocus ->
                        if (hasFocus) {
                            val drawable = negative_feedback_editText.background as GradientDrawable
                            drawable.setStroke(mStrokePxText, mStokeEditTextColor)
                        } else {
                            val drawable = negative_feedback_editText.background as GradientDrawable
                            //remove border
                            drawable.setStroke(0, transparent)
                        }
                    }
            }
            if (holder is AppHintAdvanceCard) {
                runEnterAnimation(holder.hintCard, position)
            }
            if (holder is AppHintNormalCard) {
                runEnterAnimation(holder.hintCard, position)
            }
        }

        override fun getItemViewType(position: Int): Int {
            Log.d("pos", "" + position)
            if (abort) {
//Position 0 Compress Card
                if (position == 0) {
                    return if (advance_compression) APP_HINT_CARD_ADVANCE else APP_HINT_CARD
                }
                if (position == 1) {
                    return if (!clickedCancelInstall && !false) {
                        STASH_INSTALL_CARD
                    } else {
                        BLANK_CARD
                    }
                }
                if (position == 2 && (negative_feedback_cancel_pressed || send_button_clicked)) {
                    return if (send_button_clicked) {
                        FEEDBACK_THANK_YOU_CARD
                    } else SHARE_APP_CARD
                } else if (position == 2) {
                    return NEGATIVE_FEEDBACK_CARD
                }
                if (position == 3) {
                    return SHARE_APP_CARD
                }
            } else {
                //Position 0 Compress Card
                if (position == 1) {
                    return if (advance_compression) APP_HINT_CARD_ADVANCE else APP_HINT_CARD
                }
                //Position 1 Stash Install Card
                if (position == 0) {
                    return if (!clickedCancelInstall && !false) {
                        STASH_INSTALL_CARD
                    } else {
                        BLANK_CARD
                    }
                }

                //Position 2 LIKE CARD
                if (position == 2) {
                    if (!mSelectedMayBLater) {
                        if (feedbackFlag == 0) {
                            return FEEDBACK_CARD
                        } else if (feedbackFlag == 1) {
                            return NEGATIVE_FEEDBACK_CARD
                        } else if (feedbackFlag == 2) {
//                            return POSITIVE_FEEDBACK_CARD;
                        } else if (feedbackFlag == 3) {
                            return FEEDBACK_THANK_YOU_CARD
                        }
                    } else {
                        return BLANK_CARD
                    }
                }
                //Position 3 SHARE CARD
                if (position == 3) {
                    return SHARE_APP_CARD
                }
            }
            return position
        }

        override fun getItemCount(): Int {
            Log.d("FinalScreen", size.toString() + "")
            return size
        }

        override fun onClick(v: View) {
//            switch (v.getId()) {
//
//                case R.id.positive:
//
////                    feedbackFlag = 2;
////                    notifyItemChanged(FEEDBACK_CARD);
//
//                    launchMarket();
//                    clickedRatingCard = true;
//                    notifyItemRemoved(2);
//                    notifyItemRangeChanged(0, size);
//
//                    break;
//                case R.id.negative:
//
//                    feedbackFlag = 1;
//                    notifyItemChanged(FEEDBACK_CARD);
//                    break;
//
//                case R.id.send:
//                    send_button_clicked = true;
//
//
//
//                    if (difficult_clicked || app_crashed_clicked || data_lost_clicked || slow_process_clicked || (negative_feedback_editText.getText().length() > 0)) {
//
//
//                        if (difficult_clicked) {
//
//                        }
//
//                        if (app_crashed_clicked) {
//
//                        }
//
//                        if (data_lost_clicked) {
//
//                        }
//                        if (slow_process_clicked) {
//
//                        }
//
//                        if (negative_feedback_editText.getText().length() > 0) {
//
//                        }
//                        if (abort) {
//                            notifyItemRemoved(1);
//                        } else {
//                            notifyItemRemoved(2);
//                        }
//                        notifyItemRangeChanged(0, size);
//                        feedbackFlag = 3;
//                    } else {
//                        Toast.makeText(FinalResultScreen.this, "Please choose a cause", Toast.LENGTH_SHORT).show();
//                    }
//
//                    try {
//                        InputMethodManager inputManager =
//                                (InputMethodManager) FinalResultScreen.this.
//                                        getSystemService(Context.INPUT_METHOD_SERVICE);
//                        inputManager.hideSoftInputFromWindow(
//                                FinalResultScreen.this.getCurrentFocus().getWindowToken(),
//                                InputMethodManager.HIDE_NOT_ALWAYS);
//                    } catch (Exception e) {
//                        Log.d("FinalResult", Log.getStackTraceString(e));
//                    }
//                    mSelectedMayBLater = true;
//
//                    break;
//
//
//                case R.id.maybe:
//
//                    mSelectedMayBLater = true;
//
//                case R.id.cancel:
//                    mSelectedMayBLater = true;
//                    if (abort) {
//                        negative_feedback_cancel_pressed = true;
//                        notifyItemRemoved(1);
//                    } else {
//                        notifyItemRemoved(2);
//                    }
//                    notifyItemRangeChanged(0, size);
//                    break;
//
//
//                case R.id.difficult:
//                    if (difficult_clicked) {
//                        //unselected
//                        difficult_clicked = false;
//                        GradientDrawable drawable = (GradientDrawable) difficult.getBackground();
//                        drawable.setColor(transparent); // set solid color
//                        drawable.setStroke(mStrokePxText, stroke);
//                        difficult.setTextColor(mAppTextColor);
//
//                    } else {
//                        //selected
//                        difficult_clicked = true;
//                        GradientDrawable drawable = (GradientDrawable) difficult.getBackground();
//                        drawable.setColor(blue); // set solid color
//                        drawable.setStroke(0, transparent);
//                        difficult.setTextColor(Color.WHITE);
//                    }
//
//                    break;
//                case R.id.app_crashed:
//                    if (app_crashed_clicked) {
//                        //unselected
//                        app_crashed_clicked = false;
//                        GradientDrawable drawable = (GradientDrawable) app_crashed.getBackground();
//                        drawable.setColor(transparent); // set solid color
//                        drawable.setStroke(mStrokePxText, stroke);
//                        app_crashed.setTextColor(mAppTextColor);
//
//                    } else {
//                        //selected
//                        app_crashed_clicked = true;
//                        GradientDrawable drawable = (GradientDrawable) app_crashed.getBackground();
//                        drawable.setColor(blue); // set solid color
//                        drawable.setStroke(0, transparent);
//                        app_crashed.setTextColor(Color.WHITE);
//                    }
//                    break;
//                case R.id.data_lost:
//                    if (data_lost_clicked) {
//                        //unselected
//                        data_lost_clicked = false;
//                        GradientDrawable drawable = (GradientDrawable) data_lost.getBackground();
//                        drawable.setColor(transparent); // set solid color
//                        drawable.setStroke(mStrokePxText, stroke);
//                        data_lost.setTextColor(mAppTextColor);
//                    } else {
//                        //selected
//                        data_lost_clicked = true;
//                        GradientDrawable drawable = (GradientDrawable) data_lost.getBackground();
//                        drawable.setColor(blue); // set solid color
//                        drawable.setStroke(0, transparent);
//                        data_lost.setTextColor(Color.WHITE);
//                    }
//                    break;
//                case R.id.slow_process:
//                    if (slow_process_clicked) {
//                        //unselected
//                        slow_process_clicked = false;
//                        GradientDrawable drawable = (GradientDrawable) slow_process.getBackground();
//                        drawable.setColor(transparent); // set solid color
//                        drawable.setStroke(mStrokePxText, stroke);
//                        slow_process.setTextColor(mAppTextColor);
//                    } else {
//                        //selected
//                        slow_process_clicked = true;
//                        GradientDrawable drawable = (GradientDrawable) slow_process.getBackground();
//                        drawable.setColor(blue); // set solid color
//                        drawable.setStroke(0, transparent);
//                        slow_process.setTextColor(Color.WHITE);
//                    }
//                    break;
//                case R.id.negative_feedback_editText:
//
//                    break;
//
//                case R.id.sure:
//                    launchMarket();
//                    clickedRatingCard = true;
//                    notifyItemRemoved(2);
//                    notifyItemRangeChanged(0, size);
//
//                    break;
//
//
//                case R.id.share_button:
//
//                    clickedShareCard = true;
//                    Intent intent = new Intent(Intent.ACTION_SEND);
//                    intent.putExtra(Intent.EXTRA_TEXT, "https://play.google.com/store/apps/details?id=com.app.compress.pdf.stash&referrer=utm_source%3Dshare%26utm_medium%3Dreferral%26utm_campaign%3Dapp%2520sharing");
//                    intent.setType("text/plain");
//                    FinalResultScreen.this.startActivity(Intent.createChooser(intent,"Help your friends save storage"));
//                    break;
//
//            }
        }

        private fun launchMarket() {
//            Uri uri = Uri.parse("market://details?id=" + getPackageName());
//            Intent myAppLinkToMarket = new Intent(Intent.ACTION_VIEW, uri);
//            try {
//                startActivity(myAppLinkToMarket);
//            } catch (ActivityNotFoundException e) {
//                Log.d("FinalResultScreen", "play store redirection failed ");
//            }
            val request = reviewManager!!.requestReviewFlow()
            request.addOnCompleteListener { task: Task<ReviewInfo?> ->
                if (task.isSuccessful) {
                    // Getting the ReviewInfo object
                    val reviewInfo = task.result
                    val flow = reviewManager!!.launchReviewFlow(this@FinalResultScreen, reviewInfo)
                    flow.addOnCompleteListener { task1: Task<Void?>? -> }
                }
            }
        }

        internal inner class AppHintAdvanceCard(itemView: View?) : RecyclerView.ViewHolder(
            itemView!!
        ) {
            var hintCard: CardView? = null
        }

        internal inner class AppHintNormalCard(itemView: View?) : RecyclerView.ViewHolder(
            itemView!!
        ) {
            var hintCard: CardView? = null
        }

        private inner class FeedBackThankYouCard(itemView: View?) : RecyclerView.ViewHolder(
            itemView!!
        )

        private inner class ShareAppCardHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
            var shareButton: TextView
            var shareCard: CardView

            init {
                shareCard = itemView.findViewById<View>(R.id.share_card) as CardView
                shareButton = itemView.findViewById<View>(R.id.share_button) as TextView
            }
        }

        private inner class StashCardHolder  //            TextView shareButton;
        //            CardView shareCard;
            (itemView: View?) : RecyclerView.ViewHolder(itemView!!)

        private inner class BlankHolder(itemView: View?) : RecyclerView.ViewHolder(
            itemView!!
        )

        inner class SecondCard(view: View?) : RecyclerView.ViewHolder(view!!) {
            var title: TextView? = null
            var year: TextView? = null
            var genre: TextView? = null
        }

        inner class RatingCard(view: View) : RecyclerView.ViewHolder(view) {
            var negative: TextView
            var positive: TextView
            var card: CardView

            init {
                card = view.findViewById<View>(R.id.card_view) as CardView
                negative = view.findViewById<View>(R.id.negative) as TextView
                positive = view.findViewById<View>(R.id.positive) as TextView
                //year = (TextView) view.findViewById(R.id.year);
            }
        }

        inner class FourthCard(view: View) : RecyclerView.ViewHolder(view) {
            init {
                send = view.findViewById<View>(R.id.send) as TextView
                cancel = view.findViewById<View>(R.id.cancel) as TextView
                difficult = view.findViewById<View>(R.id.difficult) as TextView
                app_crashed = view.findViewById<View>(R.id.app_crashed) as TextView
                data_lost = view.findViewById<View>(R.id.data_lost) as TextView
                slow_process = view.findViewById<View>(R.id.slow_process) as TextView
                negative_feedback_editText =
                    view.findViewById<View>(R.id.negative_feedback_editText) as EditText
            }
        }

        inner class FifthCard(view: View) : RecyclerView.ViewHolder(view) {
            init {
                sure = view.findViewById<View>(R.id.sure) as TextView
                maybe = view.findViewById<View>(R.id.maybe) as TextView
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
    private inner class SpacesItemDecoration(space: Int) : ItemDecoration() {
        private var space = 12

        init {
            this.space = space
        }

        override fun getItemOffsets(
            outRect: Rect,
            view: View,
            parent: RecyclerView,
            state: RecyclerView.State
        ) {
            if (parent.getChildAdapterPosition(view) == state.itemCount - 1) {
                outRect.bottom = space
                outRect.top = 0 //don't forget about recycling...
            }
        }
    }

    companion object {
        private val LOG_TAG = FinalResultScreen::class.java.getSimpleName()
        var FINAL_ACTIVITY_FINISHED = "final.activity.finished"
        private var size = 4
        fun getSizeinMBOnly(prev_size: Long): String {
            var prev_size = prev_size
            val unit = 1000
            if (prev_size > unit) {
                prev_size = prev_size / unit
                if (prev_size > unit) {
                    prev_size = prev_size / unit
                    return "$prev_size MB"
                }
                return "$prev_size KB"
            }
            return "$prev_size B"
        }
    }
}
