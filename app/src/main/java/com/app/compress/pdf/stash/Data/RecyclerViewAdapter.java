package com.app.compress.pdf.stash.Data;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
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


import com.app.compress.pdf.stash.Activities.AwardTest;
import com.app.compress.pdf.stash.Activities.FinalResultScreen;
import com.app.compress.pdf.stash.Activities.Final_resultfail;
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

public class RecyclerViewAdapter extends RecyclerView.Adapter<RecyclerViewAdapter.ViewHolder> {
    private Context context;
    private List<PDF> pdfList;

    private AlertDialog.Builder dialogBuilder;
    private AlertDialog dialog;

    public RecyclerViewAdapter(Context context,List<PDF> pdfs) {
        this.context = context;
        pdfList = pdfs;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.pdf_row,null);

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
        Button compress;

        public ViewHolder(@NonNull View itemView,Context ctx) {
            super(itemView);

            pdfname = itemView.findViewById(R.id.row_name);
            pdfsize = itemView.findViewById(R.id.row_size);
            pdfdate = itemView.findViewById(R.id.row_date);
            pdfOpen = itemView.findViewById(R.id.row_open);
            compress = itemView.findViewById(R.id.row_compressButton);


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

            compress.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    showConfirmationDialog();
                }
            });

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    showConfirmationDialog();
                }
            });
        }

        private void showConfirmationDialog() {
            dialogBuilder = new AlertDialog.Builder(context);
            View view = LayoutInflater.from(context).inflate(R.layout.confirmation_popup, null);

            TextView confirmName = view.findViewById(R.id.confirmName);
            Button confirmbutton = view.findViewById(R.id.confirmButton);

            PDF pdf = pdfList.get(getAdapterPosition());
            confirmName.setText(pdf.getFilename() +"\n \n"+
                    pdf.getSize());

            dialogBuilder.setView(view);
            dialog = dialogBuilder.create();
            dialog.show();

            confirmbutton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    Intent intent = new Intent(context, AwardTest.class);
                    intent.putExtra("pdfObject",pdf);
                    context.startActivity(intent);
                }
            });
        }
    }
}
