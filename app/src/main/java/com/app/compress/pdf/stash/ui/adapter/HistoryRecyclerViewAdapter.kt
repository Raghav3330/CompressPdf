package com.app.compress.pdf.stash.ui.adapter

import android.content.Context
import android.content.Intent
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AlertDialog
import androidx.core.content.FileProvider
import androidx.recyclerview.widget.RecyclerView
import com.app.compress.pdf.stash.model.Pdf
import com.app.compress.pdf.stash.R
import com.app.compress.pdf.stash.util.FileUtils

class HistoryRecyclerViewAdapter(private val context: Context, private val pdfList: List<Pdf>) :
    RecyclerView.Adapter<HistoryRecyclerViewAdapter.ViewHolder>() {
    private val dialogBuilder: AlertDialog.Builder? = null
    private val dialog: AlertDialog? = null
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.historypdf_row, null)
        return ViewHolder(view, context)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val pdf = pdfList[position]
        holder.pdfname.text = pdf.filename
        holder.pdfdate.text = FileUtils.formatDate(pdf.date)
        holder.pdfsize.text = FileUtils.formatFileSize(pdf.size)
    }

    override fun getItemCount(): Int {
        return pdfList.size
    }

    inner class ViewHolder(itemView: View, ctx: Context) : RecyclerView.ViewHolder(itemView) {
        var pdfname: TextView
        var pdfsize: TextView
        var pdfdate: TextView
        var pdfOpen: TextView
        var share: Button

        init {
            pdfname = itemView.findViewById(R.id.row_name)
            pdfsize = itemView.findViewById(R.id.row_size)
            pdfdate = itemView.findViewById(R.id.row_date)
            pdfOpen = itemView.findViewById(R.id.row_open)
            share = itemView.findViewById(R.id.row_shareButton)
            pdfOpen.setOnClickListener {
//                val pdf = pdfList[getAdapterPosition()]
//                val path = pdf.sourcepath
//                Log.d("onClick: ", path.toString())
//                val intent = Intent(Intent.ACTION_VIEW)
//                intent.setDataAndType(
//                    path?.let { it1 ->
//                        FileProvider.getUriForFile(
//                            ctx,
//                            ctx.packageName + ".fileprovider",
//                            it1
//                        )
//                    }, "application/pdf"
//                )
//                intent.setFlags(Intent.FLAG_ACTIVITY_NO_HISTORY)
//                intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
//                intent.addFlags(Intent.FLAG_GRANT_WRITE_URI_PERMISSION)
//                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
//                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
//
//                //Create Viewer Intent
//                val viewerIntent = Intent.createChooser(intent, "Open PDF")
//                ctx.startActivity(viewerIntent)
            }
            share.setOnClickListener {
//                val pdf = pdfList[getAdapterPosition()]
//                val path = pdf.sourcepath
//                val outputFile =
//                    path?.let { it1 ->
//                        FileProvider.getUriForFile(ctx, ctx.packageName + ".fileprovider",
//                            it1
//                        )
//                    }
//                val share = Intent()
//                share.setAction(Intent.ACTION_SEND)
//                share.setType("application/pdf")
//                share.putExtra(Intent.EXTRA_STREAM, outputFile)
//                //                    share.setPackage("com.whatsapp");
//
//                //Share Intent
//                val shareIntent = Intent.createChooser(share, "Share PDF")
//                ctx.startActivity(shareIntent)
            }
        }
    }
}
