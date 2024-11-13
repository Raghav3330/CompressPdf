package com.app.compress.pdf.stash.ui.fragment

import android.content.ContentUris
import android.content.Intent
import android.database.Cursor
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.os.Environment
import android.provider.MediaStore
import android.provider.Settings
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.app.compress.pdf.stash.R
import com.app.compress.pdf.stash.model.Pdf
import com.app.compress.pdf.stash.ui.adapter.RecyclerViewAdapter
import java.io.File
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Collections
import java.util.Date
import java.util.Locale


class CompressFragment : Fragment() {
    private lateinit var recyclerView: RecyclerView
    private var recyclerViewAdapter: RecyclerViewAdapter? = null

    private val pdfList = mutableListOf<Pdf>()
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.compress_pdf, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        recyclerView = view.findViewById(R.id.recyclerView)
        recyclerView.setHasFixedSize(true)
        recyclerView.setLayoutManager(LinearLayoutManager(context))

        if(Build.VERSION.SDK_INT>=Build.VERSION_CODES.R){
            if(hasExternalStoragePermission()){
                getAllPdf()
            }else{
                requestManageExternalStoragePermission()
            }
        }else{
            requestPermissionLauncher.launch(android.Manifest.permission.READ_EXTERNAL_STORAGE)
        }



//        pdfArrayList = checkPdfs()
        getAllPdf()
        println(pdfList.size)
        recyclerViewAdapter = RecyclerViewAdapter(requireContext(), pdfList)
        recyclerView.setAdapter(recyclerViewAdapter)
        recyclerViewAdapter!!.notifyDataSetChanged()
    }

//    private fun checkPdfs(): MutableList<Pdf>? {
//        pdfArrayList!!.clear()
//        val downloadsFolder = File(Environment.getExternalStorageDirectory().toString() + "//")
//        Log.d("checkPdfs: ", downloadsFolder.toString())
//        if (downloadsFolder.exists()) {
//            Search_Dir(downloadsFolder)
//        }
//        Log.d("checkPdfs: ", downloadsFolder.toString())
//        return pdfArrayList
//    }
    fun getAllPdf(){

        // URI for accessing the external storage files
        val uri: Uri = MediaStore.Files.getContentUri("external")

        // Columns to retrieve
        val projection = arrayOf(
            MediaStore.Files.FileColumns._ID, // PDF file name
            MediaStore.Files.FileColumns.DATA, // File path (only for Android versions before scoped storage)
            MediaStore.Files.FileColumns.MIME_TYPE, // PDF file name
            MediaStore.Files.FileColumns.DISPLAY_NAME, // PDF file name
            MediaStore.Files.FileColumns.SIZE,
            MediaStore.Files.FileColumns.DATE_MODIFIED
            )

        // Filter for PDF files
        val selection = "${MediaStore.Files.FileColumns.MIME_TYPE} = ?"
        val selectionArgs = arrayOf("application/pdf")

        // Sorting order to get the latest added files first
        val sortOrder = "${MediaStore.Files.FileColumns.DATE_ADDED} DESC"

        // Query the content resolver
        val cursor: Cursor? = requireContext().contentResolver.query(
            uri,
            projection,
            selection,
            selectionArgs,
            sortOrder
        )

        // Parse the results
        cursor?.use {
            val idColumn = it.getColumnIndexOrThrow(MediaStore.Files.FileColumns._ID)
            val nameColumn = it.getColumnIndexOrThrow(MediaStore.Files.FileColumns.DISPLAY_NAME)
            val dataColumn = it.getColumnIndexOrThrow(MediaStore.Files.FileColumns.DATA)
            val sizeColumn = it.getColumnIndexOrThrow(MediaStore.Files.FileColumns.SIZE)
            val dateColumn = it.getColumnIndexOrThrow(MediaStore.Files.FileColumns.DATE_MODIFIED)

            while (it.moveToNext()) {
                val id = it.getLong(idColumn)
                val name = it.getString(nameColumn)
                val data = it.getString(dataColumn) // Full file path
                val size = it.getLong(sizeColumn)
                val date = it.getLong(dateColumn)

                //val fileUri = ContentUris.withAppendedId(MediaStore.Files.getContentUri("external"),id )

                // Log the path or handle it
                //Log.d("PDF File", "Name: $name, Path: $data , File Uri : $fileUri ,Date : $date")

                pdfList.add(
                    Pdf(name,size,date,data.toString())
                )
            }
        }
    }

    private var requestPermissionLauncher =
        registerForActivityResult(ActivityResultContracts.RequestPermission()){isGranted ->
            if(isGranted){
                getAllPdf()
            }else{
                Toast.makeText(requireContext(),"Permission rejected",Toast.LENGTH_SHORT).show()
            }
        }

    private var resultLauncher =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()){ result ->
            if(hasExternalStoragePermission()){
                getAllPdf()
            }else{
                Toast.makeText(requireContext(),"Permission rejected",Toast.LENGTH_SHORT).show()
            }
        }

    private fun requestManageExternalStoragePermission(){
        if(!hasExternalStoragePermission()){
            if(Build.VERSION.SDK_INT>= Build.VERSION_CODES.R){
                val intent = Intent(Settings.ACTION_MANAGE_ALL_FILES_ACCESS_PERMISSION)
                resultLauncher.launch(intent)
            }else{
                requestPermissionLauncher.launch(android.Manifest.permission.READ_EXTERNAL_STORAGE)
            }
        }
    }
    private fun hasExternalStoragePermission(): Boolean {
        return if(Build.VERSION.SDK_INT>= Build.VERSION_CODES.R){
            Environment.isExternalStorageManager()
        }else{
            false
        }
    }

//    fun Search_Dir(dir: File) {
//        try {
//            val pdfPattern = ".pdf"
//            val FileList = dir.listFiles()
//            if (FileList != null) {
//                for (i in FileList.indices) {
//                    if (FileList[i].isDirectory()) {
//                        Search_Dir(FileList[i])
//                    } else {
//                        Log.d("Search_Dir: ", FileList[i].toString())
//                        if (FileList[i].getName().endsWith(pdfPattern)) {
//                            //here you have that file.
//                            val file = FileList[i]
//                            if (file.path.endsWith("pdf") && !file.path.contains("COMPRESSED_PDF")) {
//                                val pdf = Pdf()
//                                pdf.filename = file.getName()
//                                pdf.sourcepath = File(file.absolutePath)
//                                val file1 = File(file.absolutePath)
//                                val filesize1 = file1.length()
//                                pdf.size = getSizeinMBOnly(filesize1)
//                                pdf.compareDate = file.lastModified().toString()
//                                val date = Date(file.lastModified())
//                                val calendar = Calendar.getInstance()
//                                calendar.setTime(date)
//                                val timeFormat = SimpleDateFormat("hh:mm")
//                                pdf.date =
//                                    (calendar[Calendar.DATE].toString() + "/" + (calendar[Calendar.MONTH] + 1) + "/" + calendar[Calendar.YEAR] + " " +
//                                            timeFormat.format(calendar.time)).toString()
//                                pdfArrayList!!.add(pdf)
//
//                                //For Sorting based on date .
//                                Collections.sort(pdfArrayList) { o1, o2 ->
//                                    o2.compareDate!!.compareTo(
//                                        o1.compareDate!!
//                                    )
//                                }
//                                recyclerViewAdapter = RecyclerViewAdapter(requireContext(), pdfArrayList!!)
//                                recyclerView!!.setAdapter(recyclerViewAdapter)
//                                recyclerViewAdapter!!.notifyDataSetChanged()
//                            }
//                        }
//                    }
//                }
//            }
//        } catch (e: NullPointerException) {
//            e.printStackTrace()
//        }
//    }

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
