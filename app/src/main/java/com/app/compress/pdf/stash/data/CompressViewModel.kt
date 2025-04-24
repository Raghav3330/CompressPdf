package com.app.compress.pdf.stash.data

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.app.compress.pdf.stash.model.Pdf
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class CompressViewModel(private val repository: PdfRepository): ViewModel() {
    private var _pdfList = MutableLiveData<List<Pdf>>()
    val pdfList: LiveData<List<Pdf>> = _pdfList

    fun fetchAllPdfFiles(){
        viewModelScope.launch(Dispatchers.IO) {
            val files = repository.getAllPdfFiles()
            _pdfList.postValue(files)
        }
    }
}

class CompressViewModelFactory(private val context: Context) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        val repository = PdfRepository(context)
        if (modelClass.isAssignableFrom(CompressViewModel::class.java)) {
            return CompressViewModel(repository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}