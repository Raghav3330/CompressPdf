package com.app.compress.pdf.stash.ui.fragment

import android.content.Intent
import android.os.Build
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.Menu
import android.view.MenuInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.widget.Toolbar
import androidx.core.content.FileProvider
import androidx.navigation.fragment.findNavController
import com.app.compress.pdf.stash.R
import com.app.compress.pdf.stash.databinding.FragmentResultBinding
import com.app.compress.pdf.stash.model.Pdf
import com.app.compress.pdf.stash.util.FileUtils
import com.google.gson.Gson
import java.io.File
import kotlin.math.roundToInt

class ResultFragment : Fragment() {

    private lateinit var binding: FragmentResultBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentResultBinding.inflate(layoutInflater)

        //TODO :
        // Reduce animation size by using total width and height and set it to 1/4 of the screen size
        val status = arguments?.getBoolean("status") ?: false
        if(status) {
            binding.animationView.setAnimation(R.raw.success)
        }else{
            binding.animationView.setAnimation(R.raw.failure)
        }

        val gson = Gson()
        val pdf = gson.fromJson(arguments?.getString("pdfJson"), Pdf::class.java)
        val compressedFile = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            arguments?.getSerializable("file", File::class.java)
        } else {
            arguments?.getSerializable("file") as File
        }

        binding.textOriginalSize.text = FileUtils.formatFileSize(pdf.size)
        if (compressedFile != null) {
            binding.textFileName.text = compressedFile.name
            val compressedSize = compressedFile.length()
            binding.textCompressedSize.append(FileUtils.formatFileSize(compressedSize))
            val compressionPercentage = (1 - (compressedSize.toDouble() / pdf.size)) * 100
            binding.textCompressionRatio.text = "${compressionPercentage.roundToInt()} % Reduced"
        }

        binding.buttonOpen.setOnClickListener {
            if (compressedFile != null) {
                FileUtils.openPdfFile(requireContext(), compressedFile.path)
            }
        }

        binding.buttonShare.setOnClickListener {
            if(compressedFile != null){
                val path = File(compressedFile.path)
                val outputFile =
                    path.let { it1 ->
                        FileProvider.getUriForFile(
                            requireContext(), requireContext().packageName + ".fileprovider",
                            it1
                        )
                    }
                val share = Intent()
                share.setAction(Intent.ACTION_SEND)
                share.setType("application/pdf")
                share.putExtra(Intent.EXTRA_STREAM, outputFile)

                //Share Intent
                val shareIntent = Intent.createChooser(share, "Share PDF")
                requireContext().startActivity(shareIntent)
            }
        }

        binding.buttonCompressAnother.setOnClickListener {
            findNavController().popBackStack()
            findNavController().navigate(R.id.compress_fragment)
        }
        return binding.root
    }
}