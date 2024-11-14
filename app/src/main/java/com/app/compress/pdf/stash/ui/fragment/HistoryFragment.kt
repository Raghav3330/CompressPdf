package com.app.compress.pdf.stash.ui.fragment

import android.os.Bundle
import android.os.Environment
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.app.compress.pdf.stash.model.Pdf
import com.app.compress.pdf.stash.R
import com.app.compress.pdf.stash.ui.adapter.HistoryRecyclerViewAdapter
import java.io.File
import java.text.SimpleDateFormat
import java.util.Arrays
import java.util.Calendar
import java.util.Date

class HistoryFragment : Fragment() {
    private lateinit var recyclerView: RecyclerView
    private var pdfArrayList: MutableList<Pdf>? = null
    private lateinit var recyclerViewAdapter: HistoryRecyclerViewAdapter
    private lateinit var textView: TextView
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.history_pdf, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        recyclerView = view.findViewById(R.id.recyclerView)
        recyclerView.setHasFixedSize(true)
        recyclerView.setLayoutManager(LinearLayoutManager(requireContext()))
        textView = view.findViewById(R.id.notFoundText)

        pdfArrayList = ArrayList()
        pdfArrayList = checkPdfs()

        recyclerViewAdapter = HistoryRecyclerViewAdapter(requireContext(), pdfArrayList!!)
        recyclerView.setAdapter(recyclerViewAdapter)

        if (pdfArrayList!!.size <= 0) {
            textView.visibility = View.VISIBLE
            recyclerView.visibility = View.INVISIBLE
        }
    }

    private fun checkPdfs(): MutableList<Pdf>? {
        pdfArrayList!!.clear()
        val downloadsFolder =
            File(Environment.getExternalStorageDirectory().toString() + "/COMPRESSED_PDF/")
        if (downloadsFolder.exists()) {
//            Search_Dir(downloadsFolder)
            Log.d("checkPdfs: ", downloadsFolder.toString())
        }
        return pdfArrayList
    }

//    fun Search_Dir(dir: File) {
//        val pdfPattern = ".pdf"
//        val FileList = dir.listFiles()
//        if (FileList != null) {
//            Arrays.sort(FileList) { f1, f2 ->
//                java.lang.Long.compare(
//                    f2.lastModified(),
//                    f1.lastModified()
//                )
//            }
//            for (i in FileList.indices) {
//                if (FileList[i].isDirectory()) {
//                    Search_Dir(FileList[i])
//                } else {
//                    if (FileList[i].getName().endsWith(pdfPattern)) {
//                        //here you have that file.
//                        val file = FileList[i]
//                        if (file.path.endsWith("pdf")) {
//                            val pdf = Pdf()
//                            pdf.filename = file.getName()
//                            pdf.sourcepath = File(file.absolutePath)
//                            val file1 = File(file.absolutePath)
//                            val filesize1 = file1.length()
//                            pdf.size = getSizeinMBOnly(filesize1)
//                            val date = Date(file.lastModified())
//                            val calendar = Calendar.getInstance()
//                            calendar.setTime(date)
//                            val timeFormat = SimpleDateFormat("hh:mm")
//                            pdf.date =
//                                (calendar[Calendar.DATE].toString() + "/" + calendar[Calendar.MONTH] + "/" + calendar[Calendar.YEAR] + " " +
//                                        timeFormat.format(calendar.time)).toString()
//                            pdfArrayList!!.add(pdf)
//                            recyclerViewAdapter = HistoryRecyclerViewAdapter(requireContext(), pdfArrayList!!)
//                            recyclerView!!.setAdapter(recyclerViewAdapter)
//                            recyclerViewAdapter!!.notifyDataSetChanged()
//                        }
//                    }
//                }
//            }
//        }
//    }

    fun refreshPosts(tag: Int) {}
    fun setAccessibilityRunning() {}
    fun setCurrentNotification() {}
    fun onClick(v: View?) {}

    companion object {
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
