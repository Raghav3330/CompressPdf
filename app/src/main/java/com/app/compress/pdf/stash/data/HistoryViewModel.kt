package com.app.compress.pdf.stash.data

import android.app.Application
import android.content.Context
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.app.compress.pdf.stash.model.Pdf
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.io.File

class HistoryViewModel(private val application: Application) : ViewModel() {
    private var _pdfList = MutableLiveData<List<Pdf>>()
    val pdfList: LiveData<List<Pdf>> = _pdfList

    fun loadCompressedPdfs() {
        viewModelScope.launch(Dispatchers.IO) {
            val list = mutableListOf<Pdf>()
            val dir = File(application.filesDir, "Compressed_pdf")
            if (dir.exists()) {
                val files = dir.listFiles()?.sortedByDescending { it.lastModified() }
                files?.forEach { file ->
                    if (file.name.endsWith(".pdf")) {
                        list.add(
                            Pdf(
                                id = 0,
                                filename = file.name,
                                size = file.length(),
                                date = file.lastModified(),
                                filePath = file.absolutePath
                            )
                        )
                    }
                }
            }
            _pdfList.postValue(list)
        }
    }
}

class HistoryViewModelFactory(private val application: Application) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(HistoryViewModel::class.java)) {
            return HistoryViewModel(application) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}
