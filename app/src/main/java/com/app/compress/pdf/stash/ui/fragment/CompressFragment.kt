package com.app.compress.pdf.stash.ui.fragment

import android.Manifest
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.os.Environment
import android.provider.Settings
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.app.compress.pdf.stash.data.CompressViewModel
import com.app.compress.pdf.stash.data.CompressViewModelFactory
import com.app.compress.pdf.stash.databinding.FragmentCompressBinding
import com.app.compress.pdf.stash.model.Pdf
import com.app.compress.pdf.stash.ui.listener.OnNavigationClickListener
import com.app.compress.pdf.stash.ui.adapter.CompressRecyclerViewAdapter

class CompressFragment : Fragment() {

    private lateinit var binding: FragmentCompressBinding
    private lateinit var compressRecyclerViewAdapter: CompressRecyclerViewAdapter
    private val pdfList = mutableListOf<Pdf>()

    private val viewModel: CompressViewModel by viewModels {
        CompressViewModelFactory(requireContext().applicationContext)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        binding = FragmentCompressBinding.inflate(inflater)
        compressRecyclerViewAdapter = CompressRecyclerViewAdapter(requireContext(), pdfList,activity as OnNavigationClickListener,requireActivity())
        binding.recyclerView.apply {
            layoutManager = LinearLayoutManager(context)
            adapter = compressRecyclerViewAdapter
        }

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            if (hasExternalStoragePermission()) {
                viewModel.fetchAllPdfFiles()
            } else {
                requestManageExternalStoragePermission()
            }
        } else {
            requestPermissionLauncher.launch(Manifest.permission.READ_EXTERNAL_STORAGE)
        }
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        Log.d("onViewcreated","onviewcreated")

        viewModel.pdfList.observe(viewLifecycleOwner) { list ->
            pdfList.clear()
            pdfList.addAll(list)
            compressRecyclerViewAdapter.notifyDataSetChanged()

            binding.progressBar.visibility = View.GONE
            if (list.isEmpty()) {
                binding.recyclerView.visibility = View.GONE
            } else {
                binding.recyclerView.visibility = View.VISIBLE
            }
        }
    }

    private var requestPermissionLauncher =
        registerForActivityResult(ActivityResultContracts.RequestPermission()) { isGranted ->
            if (isGranted) {
                viewModel.fetchAllPdfFiles()
            } else {
                Toast.makeText(requireContext(), "Permission rejected", Toast.LENGTH_SHORT).show()
            }
        }

    private var resultLauncher =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            if (hasExternalStoragePermission()) {
                viewModel.fetchAllPdfFiles()
            } else {
                Toast.makeText(requireContext(), "Permission rejected", Toast.LENGTH_SHORT).show()
            }
        }

    private fun requestManageExternalStoragePermission() {
        if (!hasExternalStoragePermission()) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
                val intent = Intent(Settings.ACTION_MANAGE_ALL_FILES_ACCESS_PERMISSION)
                resultLauncher.launch(intent)
            } else {
                requestPermissionLauncher.launch(android.Manifest.permission.READ_EXTERNAL_STORAGE)
            }
        }
    }

    private fun hasExternalStoragePermission(): Boolean {
        return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            Environment.isExternalStorageManager()
        } else {
            false
        }
    }

}