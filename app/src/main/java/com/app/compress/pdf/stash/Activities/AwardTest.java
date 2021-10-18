package com.app.compress.pdf.stash.Activities;


import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.Window;
import android.view.WindowManager;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.app.compress.pdf.stash.BuildConfig;
import com.app.compress.pdf.stash.Model.PDF;
import com.app.compress.pdf.stash.R;
import com.google.android.gms.ads.AdError;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.FullScreenContentCallback;
import com.google.android.gms.ads.LoadAdError;
import com.google.android.gms.ads.MobileAds;
import com.google.android.gms.ads.OnUserEarnedRewardListener;
import com.google.android.gms.ads.initialization.InitializationStatus;
import com.google.android.gms.ads.initialization.OnInitializationCompleteListener;
import com.google.android.gms.ads.interstitial.InterstitialAd;
import com.google.android.gms.ads.interstitial.InterstitialAdLoadCallback;
import com.google.android.gms.ads.rewarded.RewardItem;
import com.google.android.gms.ads.rewarded.RewardedAd;
import com.google.android.gms.ads.rewarded.RewardedAdLoadCallback;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.pdf.ColumnText;
import com.itextpdf.text.pdf.PRStream;
import com.itextpdf.text.pdf.PdfName;
import com.itextpdf.text.pdf.PdfNumber;
import com.itextpdf.text.pdf.PdfObject;
import com.itextpdf.text.pdf.PdfReader;
import com.itextpdf.text.pdf.PdfStamper;
import com.itextpdf.text.pdf.parser.PdfImageObject;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;


public class AwardTest extends AppCompatActivity  {
    private static final String TAG = "AWARD TEST";
    private String mFeatureLanded = "Other";
    private RewardedAd mRewardedAd;
    private boolean seenAdvert = false;

    private PDF pdf;
    boolean isDebug = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_award_test);

        Bundle bundle = getIntent().getExtras();

        pdf = (PDF) bundle.get("pdfObject");

        if (BuildConfig.DEBUG) {
            isDebug = true;
        }

        MobileAds.initialize(this, new OnInitializationCompleteListener() {
            @Override
            public void onInitializationComplete(@NonNull InitializationStatus initializationStatus) {
            }
        });

        loadAds();
    }

    private void loadAds() {
        AdRequest adRequest = new AdRequest.Builder().build();
        Log.d("loadAds: ","");
        RewardedAd.load(this, "ca-app-pub-9668830280921241/1061649352",
                adRequest, new RewardedAdLoadCallback() {
                    @Override
                    public void onAdFailedToLoad(@NonNull LoadAdError loadAdError) {
                        // Handle the error.
                        mRewardedAd = null;
                        Log.d("onAdFailedToLoad: ",loadAdError.getMessage().toString());
                    }

                    @Override
                    public void onAdLoaded(@NonNull RewardedAd rewardedAd) {
                        mRewardedAd = rewardedAd;
                        Log.d(TAG, "Ad was loaded.");
                        showAds();
                    }
                });
    }

    private void showAds() {

        mRewardedAd.setFullScreenContentCallback(new FullScreenContentCallback() {
            @Override
            public void onAdShowedFullScreenContent() {
                // Called when ad is shown.
                Log.d(TAG, "Ad was shown.");
            }

            @Override
            public void onAdFailedToShowFullScreenContent(AdError adError) {
                // Called when ad fails to show.
                Log.d(TAG, "Ad failed to show.");
            }

            @Override
            public void onAdDismissedFullScreenContent() {
                // Called when ad is dismissed.
                // Set the ad reference to null so you don't show the ad a second time.
                Log.d(TAG, "Ad was dismissed.");
                mRewardedAd = null;
                finish();
            }
        });
        if (mRewardedAd != null) {
            Activity activityContext = this;
            mRewardedAd.show(activityContext, new OnUserEarnedRewardListener() {
                @Override
                public void onUserEarnedReward(@NonNull RewardItem rewardItem) {
                    // Handle the reward.
                    Log.d(TAG, "The user earned the reward.");
                    startCompression();
                }
            });
        } else {
            Log.d(TAG, "The rewarded ad wasn't ready yet.");
        }
    }

    private void startCompression() {
        try {
            // Creating a PdfWriter object
            File fi = new File(Environment.getExternalStorageDirectory() + "/COMPRESSED_PDF/");
            if (!fi.mkdirs()) {
                fi.mkdirs();
            }
            String[] pdfname = pdf.getFilename().split("\\.");
            String compressedName = pdfname[0] + "-Compressed."+ pdfname[1];
            File dest = new File(Environment.getExternalStorageDirectory() + "/COMPRESSED_PDF/" + compressedName);


            File src = pdf.getSourcepath();
            /* FOR ITEXT 5*/

            PdfReader pdfReader = new PdfReader(String.valueOf(src));
            int xrefSize = pdfReader.getXrefSize();
            int i2 = 0;
            while (i2 < xrefSize) {
                int i3 = i2 + 1;
                PdfObject pdfObject = pdfReader.getPdfObject(i2);
                if (pdfObject != null && pdfObject.isStream()) {
                    PRStream pRStream = (PRStream) pdfObject;
                    PdfName pdfName = PdfName.SUBTYPE;
                    PdfObject pdfObject2 = pRStream.get(pdfName);
                    System.out.println(pRStream.type());
                    if (pdfObject2 != null) {
                        String pdfObject3 = pdfObject2.toString();
                        PdfName pdfName2 = PdfName.IMAGE;
                        if (pdfObject3.equals(pdfName2.toString())) {
                            byte[] imageAsBytes = new PdfImageObject(pRStream).getImageAsBytes();
                            Bitmap decodeByteArray = BitmapFactory.decodeByteArray(imageAsBytes, 0, imageAsBytes.length);
                            if (decodeByteArray != null) {
                                int width = decodeByteArray.getWidth();
                                int height = decodeByteArray.getHeight();
                                Bitmap createBitmap = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888);
                                new Canvas(createBitmap).drawBitmap(decodeByteArray, ColumnText.GLOBAL_SPACE_CHAR_RATIO, ColumnText.GLOBAL_SPACE_CHAR_RATIO, (Paint) null);
                                ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
                                createBitmap.compress(Bitmap.CompressFormat.JPEG, 9, byteArrayOutputStream);
                                pRStream.clear();
                                pRStream.setData(byteArrayOutputStream.toByteArray(), false, 9);
                                pRStream.put(PdfName.TYPE, PdfName.XOBJECT);
                                pRStream.put(pdfName, pdfName2);
                                pRStream.put(PdfName.FILTER, PdfName.DCTDECODE);
                                pRStream.put(PdfName.WIDTH, new PdfNumber(width));
                                pRStream.put(PdfName.HEIGHT, new PdfNumber(height));
                                pRStream.put(PdfName.BITSPERCOMPONENT, new PdfNumber(8));
                                pRStream.put(PdfName.COLORSPACE, PdfName.DEVICERGB);
                                if (!decodeByteArray.isRecycled()) {
                                    decodeByteArray.recycle();
                                }
                                if (!createBitmap.isRecycled()) {
                                    createBitmap.recycle();
                                }
                            }
                        }
                    }
                }
                i2 = i3;
            }
            pdfReader.removeUnusedObjects();
            PdfStamper pdfStamper = new PdfStamper(pdfReader, new FileOutputStream(dest));
            pdfStamper.setFullCompression();
            pdfStamper.close();
            pdfReader.close();

        }catch(FileNotFoundException e){
            e.printStackTrace();
        } catch(IOException e){
            e.printStackTrace();
        } catch(DocumentException e){
            e.printStackTrace();
        }
        String[] pdfname = pdf.getFilename().split("\\.");
        String compressedName = pdfname[0] + "-Compressed."+ pdfname[1];
        File dest = new File(Environment.getExternalStorageDirectory() + "/COMPRESSED_PDF/" + compressedName);

        Log.d("onClick: ",dest.toString());

        if(dest.exists()){
            Intent intent = new Intent(AwardTest.this, FinalResultScreen.class);

            intent.putExtra("source",pdf.getSourcepath());
            intent.putExtra("dest",dest);
            intent.putExtra("pdfname",compressedName);
            intent.putExtra("size_original",pdf.getSize());
            startActivity(intent);
            finish();
        }else {
            Intent intent = new Intent(AwardTest.this, Final_resultfail.class);
            intent.putExtra("size_original",pdf.getSize());
            startActivity(intent);
            finish();
        }
    }

}