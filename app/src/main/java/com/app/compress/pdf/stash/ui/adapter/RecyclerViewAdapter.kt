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
import com.app.compress.pdf.stash.ui.activity.AwardTest
import com.app.compress.pdf.stash.util.FileUtils
import java.io.File

class RecyclerViewAdapter(private val context: Context, private val pdfList: List<Pdf>) :
    RecyclerView.Adapter<RecyclerViewAdapter.ViewHolder>() {
    private var dialogBuilder: AlertDialog.Builder? = null
    private var dialog: AlertDialog? = null
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.pdf_row, null)
        return ViewHolder(view, context)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val pdf = pdfList[position]
        holder.pdfname.text = pdf.filename
        holder.pdfdate.text = FileUtils.formatDate(pdf.date)
        holder.pdfsize.text = FileUtils.formatFileSize(pdf.size)

        holder.pdfOpen.setOnClickListener {
            FileUtils.openPdfFile(context,pdf.filePath)
        }

        holder.itemView.setOnClickListener {
        }
    }

    override fun getItemCount(): Int {
        return pdfList.size
    }

    inner class ViewHolder(itemView: View, ctx: Context) : RecyclerView.ViewHolder(itemView) {
        var pdfname: TextView
        var pdfsize: TextView
        var pdfdate: TextView
        var pdfOpen: TextView
        var compress: Button

        init {
            pdfname = itemView.findViewById(R.id.row_name)
            pdfsize = itemView.findViewById(R.id.row_size)
            pdfdate = itemView.findViewById(R.id.row_date)
            pdfOpen = itemView.findViewById(R.id.row_open)
            compress = itemView.findViewById(R.id.row_compressButton)

            compress.setOnClickListener { showConfirmationDialog() }
            itemView.setOnClickListener { showConfirmationDialog() }
        }

        private fun showConfirmationDialog() {
//            dialogBuilder = AlertDialog.Builder(context)
//            val view = LayoutInflater.from(context).inflate(R.layout.confirmation_popup, null)
//            val confirmName = view.findViewById<TextView>(R.id.confirmName)
//            val confirmbutton = view.findViewById<Button>(R.id.confirmButton)
//            val pdf = pdfList[getAdapterPosition()]
//            confirmName.text = """${pdf.filename} ${pdf.size}"""
//            dialogBuilder!!.setView(view)
//            dialog = dialogBuilder!!.create()
//            dialog!!.show()
//            confirmbutton.setOnClickListener {
//                val intent = Intent(context, AwardTest::class.java)
//                intent.putExtra("pdfObject", pdf)
//                context.startActivity(intent)
//            }
        }
    }
}
