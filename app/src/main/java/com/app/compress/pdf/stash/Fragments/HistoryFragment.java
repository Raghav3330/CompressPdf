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
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.app.compress.pdf.stash.Data.HistoryRecyclerViewAdapter;
import com.app.compress.pdf.stash.Data.RecyclerViewAdapter;
import com.app.compress.pdf.stash.Model.PDF;
import com.app.compress.pdf.stash.R;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Comparator;
import java.util.Date;
import java.util.List;

public class HistoryFragment extends BaseContentFragment {
    private RecyclerView recyclerView;
    private List<PDF> pdfArrayList;
    private HistoryRecyclerViewAdapter recyclerViewAdapter;
    private TextView textView;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.history_pdf,container,false);
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);


        recyclerView = view.findViewById(R.id.recyclerView);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        textView = view.findViewById(R.id.notFoundText);

        pdfArrayList = new ArrayList<>();
        pdfArrayList = checkPdfs();

        recyclerViewAdapter = new HistoryRecyclerViewAdapter(getContext(), pdfArrayList);
        recyclerView.setAdapter(recyclerViewAdapter);
        recyclerViewAdapter.notifyDataSetChanged();

        if(pdfArrayList.size() <= 0){
            textView.setVisibility(View.VISIBLE);
            recyclerView.setVisibility(View.INVISIBLE);
        }
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
    }

    private List<PDF> checkPdfs() {
        pdfArrayList.clear();
        File downloadsFolder = new File(Environment.getExternalStorageDirectory() + "/COMPRESSED_PDF/");
        if(downloadsFolder.exists()) {
            Search_Dir(downloadsFolder);
        Log.d("checkPdfs: ", String.valueOf(downloadsFolder));
        }
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
        String pdfPattern = ".pdf";

        File FileList[] = dir.listFiles();

        if (FileList != null) {
            Arrays.sort(FileList, new Comparator<File>() {
                public int compare(File f1, File f2) {
                    return Long.compare(f2.lastModified(), f1.lastModified());
                }
            });
            for (int i = 0; i < FileList.length; i++) {

                if (FileList[i].isDirectory()) {
                    Search_Dir(FileList[i]);
                } else {

                    if (FileList[i].getName().endsWith(pdfPattern)) {
                        //here you have that file.
                        File file = FileList[i];


                        if (file.getPath().endsWith("pdf")) {
                            PDF pdf = new PDF();
                            pdf.setFilename(file.getName());
                            pdf.setSourcepath(new File(file.getAbsolutePath()));
                            File file1 = new File(file.getAbsolutePath());
                            long filesize1 = file1.length();
                            pdf.setSize(getSizeinMBOnly(filesize1));
                            Date date = new Date(file.lastModified());
                            Calendar calendar = Calendar.getInstance();
                            calendar.setTime(date);
                            SimpleDateFormat timeFormat = new SimpleDateFormat("hh:mm");


                            pdf.setDate(String.valueOf(calendar.get(Calendar.DATE) + "/" + calendar.get(Calendar.MONTH) + "/" + calendar.get(Calendar.YEAR) + " " +
                                    timeFormat.format(calendar.getTime())));

                            pdfArrayList.add(pdf);
                            recyclerViewAdapter = new HistoryRecyclerViewAdapter(getContext(), pdfArrayList);
                            recyclerView.setAdapter(recyclerViewAdapter);
                            recyclerViewAdapter.notifyDataSetChanged();
                        }
                    }
                }
            }
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
