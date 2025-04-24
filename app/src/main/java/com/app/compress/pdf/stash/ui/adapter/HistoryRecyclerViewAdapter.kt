package com.app.compress.pdf.stash.ui.adapter

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.content.FileProvider
import androidx.recyclerview.widget.RecyclerView
import pdfviewer.ui.PdfViewerActivity
import com.app.compress.pdf.stash.model.Pdf
import com.app.compress.pdf.stash.databinding.HistorypdfRowBinding
import com.app.compress.pdf.stash.util.FileUtils
import java.io.File
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class HistoryRecyclerViewAdapter(private val context: Context, private var pdfList: MutableList<Pdf>) :
    RecyclerView.Adapter<HistoryRecyclerViewAdapter.ViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = HistorypdfRowBinding.inflate(LayoutInflater.from(parent.context), parent,false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val pdf = pdfList[position]
        holder.binding.rowName.text = pdf.filename
        val sdf = SimpleDateFormat("dd/MM/yyyy HH:mm", Locale.getDefault())
        val formattedDate = sdf.format(Date(pdf.date))
        holder.binding.rowDate.text = formattedDate
        holder.binding.rowSize.text = FileUtils.formatFileSize(pdf.size)

        holder.binding.root.setOnClickListener {
            val intent = Intent(context, PdfViewerActivity::class.java)
            intent.putExtra("pdf_path", pdf.filePath)
            context.startActivity(intent)
//            FileUtils.openCompressedPdfFile(context, pdf.filename)
        }

        holder.binding.rowShareButton.setOnClickListener {
            val path = File(pdf.filePath)
            val outputFile =
                path.let { it1 ->
                    FileProvider.getUriForFile(
                        context, context.packageName + ".fileprovider",
                        it1
                    )
                }
            val share = Intent()
            share.setAction(Intent.ACTION_SEND)
            share.setType("application/pdf")
            share.putExtra(Intent.EXTRA_STREAM, outputFile)
            //                    share.setPackage("com.whatsapp");

            //Share Intent
            val shareIntent = Intent.createChooser(share, "Share PDF")
            context.startActivity(shareIntent)
        }
    }

    override fun getItemCount(): Int {
        return pdfList.size
    }

    fun updateList(newList: List<Pdf>) {
        pdfList.clear()
        pdfList.addAll(newList)
        notifyDataSetChanged()
    }

    class ViewHolder(val binding: HistorypdfRowBinding) : RecyclerView.ViewHolder(binding.root)
}
