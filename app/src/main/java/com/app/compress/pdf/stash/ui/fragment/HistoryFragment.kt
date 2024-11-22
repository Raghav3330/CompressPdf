package com.app.compress.pdf.stash.ui.fragment

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.app.compress.pdf.stash.model.Pdf
import com.app.compress.pdf.stash.databinding.HistoryPdfBinding
import com.app.compress.pdf.stash.ui.adapter.HistoryRecyclerViewAdapter
import java.io.File
import java.util.Arrays

class HistoryFragment : Fragment() {
    private lateinit var recyclerView: RecyclerView
    private var compressedPdfs: MutableList<Pdf> = mutableListOf()
    private lateinit var recyclerViewAdapter: HistoryRecyclerViewAdapter
    private lateinit var textView: TextView

    private lateinit var binding: HistoryPdfBinding
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = HistoryPdfBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        recyclerView = binding.recyclerView
        recyclerView.setHasFixedSize(true)
        recyclerView.setLayoutManager(LinearLayoutManager(requireContext()))
        textView = binding.notFoundText

        compressedPdfs = checkPdfs()

        recyclerViewAdapter = HistoryRecyclerViewAdapter(requireContext(), compressedPdfs)
        recyclerView.setAdapter(recyclerViewAdapter)

        if (compressedPdfs.size <= 0) {
            textView.visibility = View.VISIBLE
            recyclerView.visibility = View.INVISIBLE
        }
    }

    private fun checkPdfs(): MutableList<Pdf> {
        compressedPdfs.clear()
        val downloadsFolder =
            File(requireContext().filesDir , "Compressed_pdf")
        if (downloadsFolder.exists()) {
            searchForCompressedPdfs(downloadsFolder)
            Log.d("checkPdfs: ", downloadsFolder.toString())
        }
        return compressedPdfs
    }

    private fun searchForCompressedPdfs(dir: File) {
        val pdfPattern = ".pdf"
        val fileList = dir.listFiles()
        if (fileList != null) {
            Arrays.sort(fileList) { f1, f2 ->
                f2.lastModified().compareTo(f1.lastModified())
            }
            for (file in fileList) {
                if (file.getName().endsWith(pdfPattern)) {
                    //here you have that file.
                    if (file.path.endsWith("pdf")) {
                        val pdf = Pdf(
                            0,
                            file.name,
                            file.length(),
                            file.lastModified(),
                            file.absolutePath
                        )
                        compressedPdfs.add(pdf)
                    }
                }
            }
            recyclerViewAdapter = HistoryRecyclerViewAdapter(requireContext(), compressedPdfs)
            recyclerView.setAdapter(recyclerViewAdapter)
            recyclerViewAdapter.notifyDataSetChanged()
        }
    }
}
