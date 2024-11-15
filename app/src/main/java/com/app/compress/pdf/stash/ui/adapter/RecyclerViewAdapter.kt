package com.app.compress.pdf.stash.ui.adapter

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Canvas
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.recyclerview.widget.RecyclerView
import com.app.compress.pdf.stash.model.Pdf
import com.app.compress.pdf.stash.databinding.ConfirmationPopupBinding
import com.app.compress.pdf.stash.databinding.PdfRowBinding
import com.app.compress.pdf.stash.ui.activity.MainActivity
import com.app.compress.pdf.stash.util.FileUtils
import com.google.android.gms.ads.AdError
import com.google.android.gms.ads.AdRequest
import com.google.android.gms.ads.FullScreenContentCallback
import com.google.android.gms.ads.LoadAdError
import com.google.android.gms.ads.MobileAds
import com.google.android.gms.ads.rewarded.RewardedAd
import com.google.android.gms.ads.rewarded.RewardedAdLoadCallback
import com.itextpdf.text.DocumentException
import com.itextpdf.text.pdf.PRStream
import com.itextpdf.text.pdf.PdfName
import com.itextpdf.text.pdf.PdfNumber
import com.itextpdf.text.pdf.PdfReader
import com.itextpdf.text.pdf.PdfStamper
import com.itextpdf.text.pdf.parser.PdfImageObject
import java.io.ByteArrayOutputStream
import java.io.File
import java.io.FileNotFoundException
import java.io.FileOutputStream
import java.io.IOException

class RecyclerViewAdapter(private val context: Context, private val pdfList: MutableList<Pdf>) :
    RecyclerView.Adapter<RecyclerViewAdapter.ViewHolder>() {

    private var dialogBuilder: AlertDialog.Builder? = null
    private var dialog: AlertDialog? = null
    private lateinit var pdfRowBinding: PdfRowBinding

    companion object {
        private const val TAG = "ADS"
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        pdfRowBinding = PdfRowBinding.inflate(LayoutInflater.from(parent.context),parent,false)
        return ViewHolder(pdfRowBinding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val pdf = pdfList[position]
        pdfRowBinding.rowName.text = pdf.filename
        pdfRowBinding.rowDate.text = FileUtils.formatDate(pdf.date)
        pdfRowBinding.rowSize.text = FileUtils.formatFileSize(pdf.size)

        pdfRowBinding.rowOpen.setOnClickListener {
            FileUtils.openPdfFile(context, pdf.filePath)
        }

        pdfRowBinding.rowCompressButton.setOnClickListener {
            showConfirmationDialog(pdf)
        }
    }

    override fun getItemCount(): Int {
        return pdfList.size
    }

    inner class ViewHolder(pdfRowBinding: PdfRowBinding) : RecyclerView.ViewHolder(pdfRowBinding.root)

    private fun showConfirmationDialog(pdf: Pdf) {
        dialogBuilder = AlertDialog.Builder(context)
        val confirmationPopupBinding = ConfirmationPopupBinding.inflate(LayoutInflater.from(context))
        val confirmName = confirmationPopupBinding.confirmName
        val confirmbutton = confirmationPopupBinding.confirmButton
        confirmName.text = """${pdf.filename} ${pdf.size}"""

        dialogBuilder?.setView(confirmationPopupBinding.root)
        dialog = dialogBuilder?.create()
        dialog?.show()
        confirmbutton.setOnClickListener {
            MobileAds.initialize(context) { }
            startCompression(pdf)
//            val gson = Gson()
//            val pdfJson = gson.toJson(pdf)
//            val intent = Intent(context, AwardTest::class.java)
//            intent.putExtra("PdfJson", pdfJson)
//            context.startActivity(intent)
        }
    }

    private var mRewardedAd: RewardedAd? = null

    private fun loadAds(pdf: Pdf) {
        val adRequest = AdRequest.Builder().build()
        Log.d("loadAds: ", "")
        RewardedAd.load(context, "ca-app-pub-9668830280921241/1061649352",
            adRequest, object : RewardedAdLoadCallback() {
                override fun onAdFailedToLoad(loadAdError: LoadAdError) {
                    // Handle the error.
                    mRewardedAd = null
                    Log.d("onAdFailedToLoad: ", loadAdError.message.toString())
                }

                override fun onAdLoaded(rewardedAd: RewardedAd) {
                    mRewardedAd = rewardedAd
                    Log.d(TAG, "Ad was loaded.")
                    showAds(pdf)
                }
            })
    }

    private fun showAds(pdf: Pdf) {
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
                MainActivity.INSTANCE?.openCompressFragment()
            }
        }
        if (mRewardedAd != null) {
            MainActivity.INSTANCE?.let {
                mRewardedAd?.show(it) {
                    Log.d(TAG, "The user earned the reward.")
                    startCompression(pdf);
                }
            }
        } else {
            Log.d(TAG, "The rewarded ad wasn't ready yet.")
        }
    }
    private fun startCompression(pdf: Pdf) {
        // Create a directory for compressed PDFs if it doesn't exist
        val outputDir = File(context.filesDir, "Compressed_pdf")
        if (!outputDir.exists() && !outputDir.mkdirs()) {
            return
        }

        // Split the filename and prepare the destination file
        val pdfNameParts = pdf.filename.split(".")
        if (pdfNameParts.size < 2) return // Ensure the filename has an extension
        val compressedName = "${pdfNameParts[0]}-compressed.${pdfNameParts[1]}"
        val destFile = File(outputDir, compressedName)

        try {
            val srcFilePath = pdf.filePath

            // Open the source PDF
            val pdfReader = PdfReader(srcFilePath)

            // Iterate through the PDF objects
            for (i in 1 until pdfReader.xrefSize) {
                val pdfObject = pdfReader.getPdfObject(i)
                if (pdfObject != null && pdfObject.isStream) {
                    val pRStream = pdfObject as PRStream
                    val subtype = pRStream.get(PdfName.SUBTYPE)
                    if (PdfName.IMAGE == subtype) {
                        val imageBytes = PdfImageObject(pRStream).imageAsBytes
                        val bitmap = BitmapFactory.decodeByteArray(imageBytes, 0, imageBytes.size)
                        bitmap?.let {
                            val width = it.width
                            val height = it.height
                            val compressedBitmap = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888)
                            Canvas(compressedBitmap).drawBitmap(it, 0f, 0f, null)

                            // Compress the bitmap and update the image stream in the PDF
                            val byteArrayOutputStream = ByteArrayOutputStream()
                            compressedBitmap.compress(Bitmap.CompressFormat.JPEG, 75, byteArrayOutputStream)

                            pRStream.clear()
                            pRStream.setData(byteArrayOutputStream.toByteArray(), false, PRStream.NO_COMPRESSION)
                            pRStream.put(PdfName.TYPE, PdfName.XOBJECT)
                            pRStream.put(PdfName.SUBTYPE, PdfName.IMAGE)
                            pRStream.put(PdfName.FILTER, PdfName.DCTDECODE)
                            pRStream.put(PdfName.WIDTH, PdfNumber(width))
                            pRStream.put(PdfName.HEIGHT, PdfNumber(height))
                            pRStream.put(PdfName.BITSPERCOMPONENT, PdfNumber(8))
                            pRStream.put(PdfName.COLORSPACE, PdfName.DEVICERGB)

                            // Recycle bitmaps to free memory
                            it.recycle()
                            compressedBitmap.recycle()
                        }
                    }
                }
            }

            // Remove unused objects and write the compressed PDF to the destination
            pdfReader.removeUnusedObjects()
            val pdfStamper = PdfStamper(pdfReader, FileOutputStream(destFile))
            pdfStamper.setFullCompression()
            pdfStamper.close()
            pdfReader.close()

        } catch (e: FileNotFoundException) {
            e.printStackTrace()
        } catch (e: IOException) {
            e.printStackTrace()
        } catch (e: DocumentException) {
            e.printStackTrace()
        }

        dialog?.dismiss()

        // Check if the destination file exists
        if (!destFile.exists()) {
            Toast.makeText(context,"Something went wrong !", Toast.LENGTH_SHORT).show()
            return
        }
    }
}
