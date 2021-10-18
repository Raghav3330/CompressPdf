package com.app.compress.pdf.stash.Fragments;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.app.compress.pdf.stash.Data.RecyclerViewAdapter;
import com.app.compress.pdf.stash.Model.PDF;
import com.app.compress.pdf.stash.R;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.List;

public class CompressFragment extends BaseContentFragment {

    private RecyclerView recyclerView;
    private List<PDF> pdfArrayList;
    private RecyclerViewAdapter recyclerViewAdapter;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.compress_pdf, container, false);
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        recyclerView = view.findViewById(R.id.recyclerView);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        pdfArrayList = new ArrayList<>();
        pdfArrayList = checkPdfs();

        recyclerViewAdapter = new RecyclerViewAdapter(getContext(), pdfArrayList);
        recyclerView.setAdapter(recyclerViewAdapter);
        recyclerViewAdapter.notifyDataSetChanged();
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
    }

    private List<PDF> checkPdfs() {
        pdfArrayList.clear();
        File downloadsFolder = new File(Environment.getExternalStorageDirectory() + "//");
        Log.d("checkPdfs: ",downloadsFolder.toString());
        if(downloadsFolder.exists()) {
            Search_Dir(downloadsFolder);
        }
        Log.d("checkPdfs: ", String.valueOf(downloadsFolder));

        return pdfArrayList;
    }
    public static String getSizeinMBOnly(long prev_size) {
        int unit = 1000;
        if (prev_size > unit) {
            prev_size = prev_size / unit;
            if (prev_size > unit) {
                prev_size = prev_size / unit;

                return prev_size + " MB";
            }
            return prev_size + " KB";

        }
        return prev_size + " B";
    }

    public void Search_Dir(File dir) {
        try {
            String pdfPattern = ".pdf";

            File FileList[] = dir.listFiles();

            if (FileList != null) {

                for (int i = 0; i < FileList.length; i++) {

                    if (FileList[i].isDirectory()) {
                        Search_Dir(FileList[i]);
                    } else {
                        Log.d("Search_Dir: ",FileList[i].toString());
                        if (FileList[i].getName().endsWith(pdfPattern)) {
                            //here you have that file.
                            File file = FileList[i];

                            if (file.getPath().endsWith("pdf") && !file.getPath().contains("COMPRESSED_PDF")) {
                                PDF pdf = new PDF();
                                pdf.setFilename(file.getName());
                                pdf.setSourcepath(new File(file.getAbsolutePath()));
                                File file1 = new File(file.getAbsolutePath());
                                long filesize1 = file1.length();
                                pdf.setSize(getSizeinMBOnly(filesize1));

                                pdf.setCompareDate(String.valueOf(file.lastModified()));

                                Date date = new Date(file.lastModified());
                                Calendar calendar = Calendar.getInstance();
                                calendar.setTime(date);

                                SimpleDateFormat timeFormat = new SimpleDateFormat("hh:mm");

                                pdf.setDate(String.valueOf(calendar.get(Calendar.DATE) + "/" + calendar.get(Calendar.MONTH) + "/" + calendar.get(Calendar.YEAR) + " " +
                                             timeFormat.format(calendar.getTime())));
                                pdfArrayList.add(pdf);

                                //For Sorting based on date .
                                Collections.sort(pdfArrayList, new Comparator<PDF>() {
                                    @Override
                                    public int compare(PDF o1, PDF o2) {
                                        return o2.getCompareDate().compareTo(o1.getCompareDate());
                                    }
                                });
                                recyclerViewAdapter = new RecyclerViewAdapter(getContext(), pdfArrayList);
                                recyclerView.setAdapter(recyclerViewAdapter);
                                recyclerViewAdapter.notifyDataSetChanged();
                            }
                        }
                    }
                }
            }
        }catch (NullPointerException e){
            e.printStackTrace();
        }
    }

    @Override
    public void refreshPosts(int tag) {

    }

    @Override
    public void setAccessibilityRunning() {

    }

    @Override
    public void setCurrentNotification() {

    }

    @Override
    public void onClick(View v) {

    }
}
