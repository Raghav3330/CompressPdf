package com.app.compress.pdf.stash.ui.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.app.compress.pdf.stash.data.HistoryViewModel
import com.app.compress.pdf.stash.data.HistoryViewModelFactory
import com.app.compress.pdf.stash.databinding.FragmentHistoryBinding
import com.app.compress.pdf.stash.ui.adapter.HistoryRecyclerViewAdapter

class HistoryFragment : Fragment() {

    private lateinit var recyclerViewAdapter: HistoryRecyclerViewAdapter

    private lateinit var binding: FragmentHistoryBinding

    private val viewModel: HistoryViewModel by viewModels{
        HistoryViewModelFactory(requireActivity().application)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentHistoryBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.recyclerView.layoutManager = LinearLayoutManager(requireContext())
        recyclerViewAdapter = HistoryRecyclerViewAdapter(requireContext(), mutableListOf())
        binding.recyclerView.adapter = recyclerViewAdapter

        viewModel.pdfList.observe(viewLifecycleOwner) { list ->
            recyclerViewAdapter.updateList(list)
            binding.notFoundText.visibility = if (list.isEmpty()) View.VISIBLE else View.GONE
            binding.recyclerView.visibility = if (list.isEmpty()) View.GONE else View.VISIBLE
        }

        viewModel.loadCompressedPdfs()
    }

}
