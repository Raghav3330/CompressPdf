package com.app.compress.pdf.stash.Data;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.net.Uri;
import android.os.Environment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.core.content.FileProvider;
import androidx.recyclerview.widget.RecyclerView;

import com.app.compress.pdf.stash.Model.PDF;
import com.app.compress.pdf.stash.R;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.pdf.ColumnText;
import com.itextpdf.text.pdf.PRStream;
import com.itextpdf.text.pdf.PdfName;
import com.itextpdf.text.pdf.PdfNumber;
import com.itextpdf.text.pdf.PdfObject;
import com.itextpdf.text.pdf.PdfReader;
import com.itextpdf.text.pdf.PdfStamper;
import com.itextpdf.text.pdf.parser.PdfImageObject;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.List;

public class HistoryRecyclerViewAdapter extends RecyclerView.Adapter<HistoryRecyclerViewAdapter.ViewHolder> {
    private Context context;
    private List<PDF> pdfList;

    private AlertDialog.Builder dialogBuilder;
    private AlertDialog dialog;

    public HistoryRecyclerViewAdapter(Context context, List<PDF> pdfs) {
        this.context = context;
        pdfList = pdfs;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.historypdf_row,null);

        return new ViewHolder(view,context);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        PDF pdf = pdfList.get(position);
        holder.pdfname.setText(pdf.getFilename());
        holder.pdfdate.setText(pdf.getDate());
        holder.pdfsize.setText(pdf.getSize());

    }

    @Override
    public int getItemCount() {
        return pdfList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{
        TextView pdfname;
        TextView pdfsize;
        TextView pdfdate;
        TextView pdfOpen;
        Button share;

        public ViewHolder(@NonNull View itemView,Context ctx) {
            super(itemView);

            pdfname = itemView.findViewById(R.id.row_name);
            pdfsize = itemView.findViewById(R.id.row_size);
            pdfdate = itemView.findViewById(R.id.row_date);
            pdfOpen = itemView.findViewById(R.id.row_open);
            share = itemView.findViewById(R.id.row_shareButton);


            pdfOpen.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    PDF pdf = pdfList.get(getAdapterPosition());
                    File path = pdf.getSourcepath();
                    Log.d( "onClick: ", path.toString());
                    Intent intent = new Intent(Intent.ACTION_VIEW);
                    intent.setDataAndType(FileProvider.getUriForFile(ctx,ctx.getPackageName()+".fileprovider",path), "application/pdf");
                    intent.setFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);
                    intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
                    intent.addFlags(Intent.FLAG_GRANT_WRITE_URI_PERMISSION);
                    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);

                    //Create Viewer Intent
                    Intent viewerIntent = Intent.createChooser(intent, "Open PDF");
                    ctx.startActivity(viewerIntent);
                }
            });

            share.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    PDF pdf = pdfList.get(getAdapterPosition());
                    File path = pdf.getSourcepath();

                     Uri outputFile = FileProvider.getUriForFile(ctx,ctx.getPackageName()+".fileprovider",path);

                    Intent share = new Intent();
                    share.setAction(Intent.ACTION_SEND);
                    share.setType("application/pdf");
                    share.putExtra(Intent.EXTRA_STREAM, outputFile);
//                    share.setPackage("com.whatsapp");

                    //Share Intent
                    Intent shareIntent = Intent.createChooser(share, "Share PDF");
                    ctx.startActivity(shareIntent);
                }
            });
        }

    }
}
