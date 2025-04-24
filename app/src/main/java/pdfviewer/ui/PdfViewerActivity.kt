package pdfviewer.ui

import PdfPageAdapter
import android.content.res.ColorStateList
import android.graphics.Color
import android.graphics.pdf.PdfRenderer
import android.os.Build
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.os.ParcelFileDescriptor
import android.view.LayoutInflater
import android.view.MenuItem
import android.view.View
import android.widget.TextView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.view.WindowInsetsControllerCompat
import androidx.recyclerview.widget.DefaultItemAnimator
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.app.compress.pdf.stash.R
import com.app.compress.pdf.stash.databinding.ActivityPdfViewerBinding
import com.app.compress.pdf.stash.databinding.DialogPdfPasswordBinding
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.tom_roush.pdfbox.pdmodel.PDDocument
import java.io.File
import kotlin.math.max
import kotlin.math.min


class PdfViewerActivity : AppCompatActivity() {

    private var pdfRenderer: PdfRenderer? = null
    private var fileDescriptor: ParcelFileDescriptor? = null

    private lateinit var pageIndicator: TextView
    private var pageIndicatorHandler = Handler(Looper.getMainLooper())
    private var hideRunnable = Runnable {
        pageIndicator.visibility = View.GONE
    }

    private lateinit var binding: ActivityPdfViewerBinding


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityPdfViewerBinding.inflate(layoutInflater)
        setContentView(binding.root)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        setSupportActionBar(binding.pdfToolbar.toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setDisplayShowTitleEnabled(false)

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.VANILLA_ICE_CREAM) { // Android 15+
            window.decorView.setOnApplyWindowInsetsListener { view, insets ->
                view.setBackgroundColor(Color.BLACK)
                WindowInsetsControllerCompat(window, window.decorView).isAppearanceLightStatusBars = false // for white icons
                insets
            }
        } else {
            // For Android 14 and below
            window.statusBarColor = Color.BLACK
        }

        val filePath = intent.getStringExtra("pdf_path") ?: return
        val file = File(filePath)

        binding.pdfToolbar.toolbar.setBackgroundColor(Color.BLACK)
        binding.pdfToolbar.toolbar.navigationIcon?.setTintList(ColorStateList.valueOf(Color.WHITE))
        binding.pdfToolbar.toolbar.setTitleTextColor(Color.WHITE)
        binding.pdfToolbar.toolbarTitle.setTextColor(Color.WHITE)
        binding.pdfToolbar.toolbarTitle.text = file.name

        val recyclerView = binding.pdfRecyclerView
        pageIndicator = binding.pageIndicator

        try{
            fileDescriptor = ParcelFileDescriptor.open(file, ParcelFileDescriptor.MODE_READ_ONLY)
            pdfRenderer = PdfRenderer(fileDescriptor!!)

            recyclerView.apply {
                isVerticalScrollBarEnabled = true
                isScrollbarFadingEnabled = true
                adapter = PdfPageAdapter(pdfRenderer!!)
                layoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
                itemAnimator = DefaultItemAnimator()
                isZoomEnabled = true
            }


        }catch (e: Exception){
            e.printStackTrace()

//          Toast.makeText(this, "This PDF is password protected and cannot be opened.", Toast.LENGTH_LONG).show()

            showPasswordDialog { password ->
                val unlockedDoc = openPdfWithPassword(file, password)
                if (unlockedDoc != null) {
                    // proceed with unlockedDoc
                    val tempFile = File.createTempFile("unlocked_pdf", ".pdf", this.cacheDir)
                    unlockedDoc.save(tempFile)
                    unlockedDoc.close()
                    val fileDescriptor = ParcelFileDescriptor.open(tempFile, ParcelFileDescriptor.MODE_READ_ONLY)
                    pdfRenderer = PdfRenderer(fileDescriptor)

                    recyclerView.apply {
                        isVerticalScrollBarEnabled = true
                        isScrollbarFadingEnabled = true
                        adapter = PdfPageAdapter(pdfRenderer!!)
                        layoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
                        itemAnimator = DefaultItemAnimator()
                        isZoomEnabled = true
                    }
                    Toast.makeText(this,"Unlocked",Toast.LENGTH_SHORT).show()
                } else {
                    Toast.makeText(this, "Invalid password", Toast.LENGTH_SHORT).show()
                    finish()
                }
            }
        }

        recyclerView.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrolled(rv: RecyclerView, dx: Int, dy: Int) {
                super.onScrolled(rv, dx, dy)

                val layoutManager = rv.layoutManager as? LinearLayoutManager ?: return

                var maxVisibleHeight = 0
                var currentPage = -1

                for (i in layoutManager.findFirstVisibleItemPosition()..layoutManager.findLastVisibleItemPosition()) {
                    val view = layoutManager.findViewByPosition(i) ?: continue

                    val visibleTop = max(view.top, 0)
                    val visibleBottom = min(view.bottom, recyclerView.height)
                    val visibleHeight = visibleBottom - visibleTop

                    if (visibleHeight > maxVisibleHeight) {
                        maxVisibleHeight = visibleHeight
                        currentPage = i
                    }
                }

                if (currentPage != -1) {
                    val pageNum = currentPage + 1
                    val totalPages = pdfRenderer?.pageCount
                    pageIndicator.text = "$pageNum/$totalPages"
                    pageIndicator.visibility = View.VISIBLE

                    pageIndicatorHandler.removeCallbacks(hideRunnable)
                    pageIndicatorHandler.postDelayed(hideRunnable, 1500)
                }
            }
        })

    }

    // Handle the "Navigate Up" button click
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            android.R.id.home -> {
                finish()
                return true
            }
        }
        return super.onOptionsItemSelected(item)
    }

    override fun onDestroy() {
        super.onDestroy()
        pdfRenderer?.close()
        fileDescriptor?.close()
    }

    private fun openPdfWithPassword(file: File, password: String): PDDocument? {
        return try {
            val document = PDDocument.load(file, password)
            if (document.isEncrypted && !document.isAllSecurityToBeRemoved) {
                document.isAllSecurityToBeRemoved = true
            }
            document
        } catch (e: Exception) {
            e.printStackTrace()
            null
        }
    }

    private fun showPasswordDialog(onPasswordEntered: (String) -> Unit) {
        val dialogPdfPasswordBinding = DialogPdfPasswordBinding.inflate(LayoutInflater.from(this))
        val passwordLayout = dialogPdfPasswordBinding.passwordLayout
        val passwordEditText = dialogPdfPasswordBinding.passwordEditText

        val dialog = MaterialAlertDialogBuilder(this,R.style.CustomAlertDialogTheme)
            .setTitle("Password required")
            .setView(dialogPdfPasswordBinding.root)
            .setCancelable(false)
            .setPositiveButton("OK", null) // We override it later
            .setNegativeButton("Cancel") { dialogInterface, _ ->
                dialogInterface.dismiss()
                finish()
            }
            .create()

        dialog.setOnShowListener {
            val okButton = dialog.getButton(AlertDialog.BUTTON_POSITIVE)
            okButton.setOnClickListener {
                val password = passwordEditText.text?.toString()
                if (!password.isNullOrEmpty()) {
                    onPasswordEntered(password)
                    dialog.dismiss()
                } else {
                    passwordLayout.error = "Password cannot be empty"
                }
            }
        }

        dialog.show()

    }


}