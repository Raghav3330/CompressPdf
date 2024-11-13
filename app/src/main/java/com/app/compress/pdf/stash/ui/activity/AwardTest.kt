package com.app.compress.pdf.stash.ui.activity

import android.app.Activity
import android.os.Bundle
import android.util.Log
import android.view.Window
import android.view.WindowManager
import androidx.appcompat.app.AppCompatActivity
import com.app.compress.pdf.stash.BuildConfig
import com.app.compress.pdf.stash.R
import com.app.compress.pdf.stash.model.Pdf
import com.google.android.gms.ads.AdError
import com.google.android.gms.ads.AdRequest
import com.google.android.gms.ads.FullScreenContentCallback
import com.google.android.gms.ads.LoadAdError
import com.google.android.gms.ads.MobileAds
import com.google.android.gms.ads.rewarded.RewardedAd
import com.google.android.gms.ads.rewarded.RewardedAdLoadCallback

class AwardTest : AppCompatActivity() {
    private val mFeatureLanded = "Other"
    private var mRewardedAd: RewardedAd? = null
    private val seenAdvert = false
    private var pdf: Pdf? = null
    var isDebug = false
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        requestWindowFeature(Window.FEATURE_NO_TITLE)
        window.setFlags(
            WindowManager.LayoutParams.FLAG_FULLSCREEN,
            WindowManager.LayoutParams.FLAG_FULLSCREEN
        )
        setContentView(R.layout.activity_award_test)
        val bundle = intent.extras
        pdf = bundle!!["pdfObject"] as Pdf?
        if (BuildConfig.DEBUG) {
            isDebug = true
        }
        MobileAds.initialize(this) { }
        loadAds()
    }

    private fun loadAds() {
        val adRequest = AdRequest.Builder().build()
        Log.d("loadAds: ", "")
        RewardedAd.load(this, "ca-app-pub-9668830280921241/1061649352",
            adRequest, object : RewardedAdLoadCallback() {
                override fun onAdFailedToLoad(loadAdError: LoadAdError) {
                    // Handle the error.
                    mRewardedAd = null
                    Log.d("onAdFailedToLoad: ", loadAdError.message.toString())
                }

                override fun onAdLoaded(rewardedAd: RewardedAd) {
                    mRewardedAd = rewardedAd
                    Log.d(TAG, "Ad was loaded.")
                    showAds()
                }
            })
    }

    private fun showAds() {
        mRewardedAd!!.fullScreenContentCallback = object : FullScreenContentCallback() {
            override fun onAdShowedFullScreenContent() {
                // Called when ad is shown.
                Log.d(TAG, "Ad was shown.")
            }

            override fun onAdFailedToShowFullScreenContent(adError: AdError) {
                // Called when ad fails to show.
                Log.d(TAG, "Ad failed to show.")
            }

            override fun onAdDismissedFullScreenContent() {
                // Called when ad is dismissed.
                // Set the ad reference to null so you don't show the ad a second time.
                Log.d(TAG, "Ad was dismissed.")
                mRewardedAd = null
                finish()
            }
        }
        if (mRewardedAd != null) {
            val activityContext: Activity = this
            mRewardedAd!!.show(activityContext) {
                // Handle the reward.
                Log.d(TAG, "The user earned the reward.")
                //                    startCompression();
            }
        } else {
            Log.d(TAG, "The rewarded ad wasn't ready yet.")
        }
    } //    private void startCompression() {

    //        try {
    //            // Creating a PdfWriter object
    //            File fi = new File(Environment.getExternalStorageDirectory() + "/COMPRESSED_PDF/");
    //            if (!fi.mkdirs()) {
    //                fi.mkdirs();
    //            }
    ////            String[] pdfname = pdf.filename.split("\\.");
    ////            String compressedName = pdfname[0] + "-Compressed."+ pdfname[1];
    //            File dest = new File(Environment.getExternalStorageDirectory() + "/COMPRESSED_PDF/" + compressedName);
    //
    //
    ////            File src = pdf.sourcepath;
    //            /* FOR ITEXT 5*/
    //
    //            PdfReader pdfReader = new PdfReader(String.valueOf(src));
    //            int xrefSize = pdfReader.getXrefSize();
    //            int i2 = 0;
    //            while (i2 < xrefSize) {
    //                int i3 = i2 + 1;
    //                PdfObject pdfObject = pdfReader.getPdfObject(i2);
    //                if (pdfObject != null && pdfObject.isStream()) {
    //                    PRStream pRStream = (PRStream) pdfObject;
    //                    PdfName pdfName = PdfName.SUBTYPE;
    //                    PdfObject pdfObject2 = pRStream.get(pdfName);
    //                    System.out.println(pRStream.type());
    //                    if (pdfObject2 != null) {
    //                        String pdfObject3 = pdfObject2.toString();
    //                        PdfName pdfName2 = PdfName.IMAGE;
    //                        if (pdfObject3.equals(pdfName2.toString())) {
    //                            byte[] imageAsBytes = new PdfImageObject(pRStream).getImageAsBytes();
    //                            Bitmap decodeByteArray = BitmapFactory.decodeByteArray(imageAsBytes, 0, imageAsBytes.length);
    //                            if (decodeByteArray != null) {
    //                                int width = decodeByteArray.getWidth();
    //                                int height = decodeByteArray.getHeight();
    //                                Bitmap createBitmap = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888);
    //                                new Canvas(createBitmap).drawBitmap(decodeByteArray, ColumnText.GLOBAL_SPACE_CHAR_RATIO, ColumnText.GLOBAL_SPACE_CHAR_RATIO, (Paint) null);
    //                                ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
    //                                createBitmap.compress(Bitmap.CompressFormat.JPEG, 9, byteArrayOutputStream);
    //                                pRStream.clear();
    //                                pRStream.setData(byteArrayOutputStream.toByteArray(), false, 9);
    //                                pRStream.put(PdfName.TYPE, PdfName.XOBJECT);
    //                                pRStream.put(pdfName, pdfName2);
    //                                pRStream.put(PdfName.FILTER, PdfName.DCTDECODE);
    //                                pRStream.put(PdfName.WIDTH, new PdfNumber(width));
    //                                pRStream.put(PdfName.HEIGHT, new PdfNumber(height));
    //                                pRStream.put(PdfName.BITSPERCOMPONENT, new PdfNumber(8));
    //                                pRStream.put(PdfName.COLORSPACE, PdfName.DEVICERGB);
    //                                if (!decodeByteArray.isRecycled()) {
    //                                    decodeByteArray.recycle();
    //                                }
    //                                if (!createBitmap.isRecycled()) {
    //                                    createBitmap.recycle();
    //                                }
    //                            }
    //                        }
    //                    }
    //                }
    //                i2 = i3;
    //            }
    //            pdfReader.removeUnusedObjects();
    //            PdfStamper pdfStamper = new PdfStamper(pdfReader, new FileOutputStream(dest));
    //            pdfStamper.setFullCompression();
    //            pdfStamper.close();
    //            pdfReader.close();
    //
    //        }catch(FileNotFoundException e){
    //            e.printStackTrace();
    //        } catch(IOException e){
    //            e.printStackTrace();
    //        } catch(DocumentException e){
    //            e.printStackTrace();
    //        }
    //        String[] pdfname = pdf.filename.split("\\.");
    //        String compressedName = pdfname[0] + "-Compressed."+ pdfname[1];
    //        File dest = new File(Environment.getExternalStorageDirectory() + "/COMPRESSED_PDF/" + compressedName);
    //
    //        Log.d("onClick: ",dest.toString());
    //
    //        if(dest.exists()){
    //            Intent intent = new Intent(AwardTest.this, FinalResultScreen.class);
    //
    //            intent.putExtra("source", pdf.sourcepath);
    //            intent.putExtra("dest",dest);
    //            intent.putExtra("pdfname",compressedName);
    //            intent.putExtra("size_original", pdf.size);
    //            startActivity(intent);
    //            finish();
    //        }else {
    //            Intent intent = new Intent(AwardTest.this, Final_resultfail.class);
    //            intent.putExtra("size_original", pdf.size);
    //            startActivity(intent);
    //            finish();
    //        }
    //    }
    companion object {
        private const val TAG = "AWARD TEST"
    }
}