package com.app.compress.pdf.stash.util

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.widget.Toast
import androidx.core.content.FileProvider
import java.io.File
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

object FileUtils {
    fun formatFileSize(size: Long): String {
        val kb = 1024
        val mb = kb * 1024
        val gb = mb * 1024

        return when {
            size >= gb -> String.format("%.2f GB", size.toFloat() / gb)
            size >= mb -> String.format("%.2f MB", size.toFloat() / mb)
            size >= kb -> String.format("%.2f KB", size.toFloat() / kb)
            else -> "$size B"
        }
    }

    fun formatDate(date: Long): String {
        val dateModified = date * 1000 // Convert to milliseconds

        return SimpleDateFormat("dd/MM/yyyy HH:mm", Locale.getDefault()).format(Date(dateModified))
    }

    fun openPdfFile(context: Context, filePath: String) {
        val file = File(filePath)
        if (file.exists()) {
            val uri: Uri = FileProvider.getUriForFile(
                context,
                "${context.packageName}.fileprovider",
                file
            )

            val intent = Intent(Intent.ACTION_VIEW).apply {
                setDataAndType(uri, "application/pdf")
                flags = Intent.FLAG_GRANT_READ_URI_PERMISSION or Intent.FLAG_ACTIVITY_NO_HISTORY
            }

            try {
                context.startActivity(intent)
            } catch (e: Exception) {
                // Handle the case where no PDF viewer is installed
                Toast.makeText(context, "No PDF viewer found", Toast.LENGTH_SHORT).show()
            }
        } else {
            Toast.makeText(context, "File not found", Toast.LENGTH_SHORT).show()
        }
    }
    fun openCompressedPdfFile(context: Context, fileName: String) {
        val file = File(context.filesDir,"/Compressed_pdf/$fileName")
        if (file.exists()) {
            val uri: Uri = FileProvider.getUriForFile(
                context,
                "${context.packageName}.fileprovider",
                file
            )

            val intent = Intent(Intent.ACTION_VIEW).apply {
                setDataAndType(uri, "application/pdf")
                flags = Intent.FLAG_GRANT_READ_URI_PERMISSION or Intent.FLAG_ACTIVITY_NO_HISTORY
            }

            try {
                context.startActivity(intent)
            } catch (e: Exception) {
                // Handle the case where no PDF viewer is installed
                Toast.makeText(context, "No PDF viewer found", Toast.LENGTH_SHORT).show()
            }
        } else {
            Toast.makeText(context, "File not found", Toast.LENGTH_SHORT).show()
        }
    }
}