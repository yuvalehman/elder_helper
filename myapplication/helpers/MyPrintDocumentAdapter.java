package com.example.yuvallehman.myapplication.helpers;

import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.pdf.PdfDocument;
import android.os.Bundle;
import android.os.CancellationSignal;
import android.os.ParcelFileDescriptor;
import android.print.PageRange;
import android.print.PrintAttributes;
import android.print.PrintDocumentAdapter;
import android.print.PrintDocumentInfo;
import android.print.pdf.PrintedPdfDocument;
import android.util.Log;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.ViewCompat;
import java.io.FileOutputStream;
import java.io.IOException;

public class MyPrintDocumentAdapter extends PrintDocumentAdapter {
    private static final String TAG = "MyPrintDocumentAdapter";
    private AppCompatActivity activity;
    private PrintedPdfDocument mPdfDocument;
    private int printItemCount;
    PageRange[] writtenPagesArray;

    public MyPrintDocumentAdapter(AppCompatActivity activity2) {
        this.activity = activity2;
    }

    public void onLayout(PrintAttributes oldAttributes, PrintAttributes newAttributes, CancellationSignal cancellationSignal, PrintDocumentAdapter.LayoutResultCallback callback, Bundle extras) {
        Log.d(TAG, "onLayout: ");
        this.mPdfDocument = new PrintedPdfDocument(this.activity, newAttributes);
        if (cancellationSignal.isCanceled()) {
            callback.onLayoutCancelled();
            return;
        }
        int pages = computePageCount(newAttributes);
        if (pages > 0) {
            callback.onLayoutFinished(new PrintDocumentInfo.Builder("print_output.pdf").setContentType(0).setPageCount(pages).build(), true);
        } else {
            callback.onLayoutFailed("Page count calculation failed.");
        }
    }

    public void onWrite(PageRange[] pages, ParcelFileDescriptor destination, CancellationSignal cancellationSignal, PrintDocumentAdapter.WriteResultCallback callback) {
        String str;
        Log.d(TAG, "onWrite: ");
        int totalPages = pages.length;
        for (int i = 0; i < totalPages; i++) {
            PdfDocument.Page page = this.mPdfDocument.startPage(i);
            if (cancellationSignal.isCanceled()) {
                callback.onWriteCancelled();
                this.mPdfDocument.close();
                this.mPdfDocument = null;
                return;
            }
            drawPage(page);
            this.mPdfDocument.finishPage(page);
        }
        try {
            Log.d(TAG, "onWrite: try");
            this.mPdfDocument.writeTo(new FileOutputStream(destination.getFileDescriptor()));
        } catch (IOException e) {
            callback.onWriteFailed(e.toString());
        } finally {
            str = "onWrite: finally";
            Log.d(TAG, str);
            this.mPdfDocument.close();
            this.mPdfDocument = null;
        }
    }

    public void onStart() {
        super.onStart();
    }

    public void onFinish() {
        super.onFinish();
    }

    private int computePageCount(PrintAttributes printAttributes) {
        int itemsPerPage = 4;
        if (!printAttributes.getMediaSize().isPortrait()) {
            itemsPerPage = 6;
        }
        return (int) Math.ceil((double) (getPrintItemCount() / itemsPerPage));
    }

    private void drawPage(PdfDocument.Page page) {
        Canvas canvas = page.getCanvas();
        Paint paint = new Paint();
        paint.setColor(ViewCompat.MEASURED_STATE_MASK);
        paint.setTextSize(36.0f);
        canvas.drawText("Test Title", (float) 54, (float) 72, paint);
        paint.setTextSize(11.0f);
        canvas.drawText("Test paragraph", (float) 54, (float) (72 + 25), paint);
        paint.setColor(-16776961);
        canvas.drawRect(100.0f, 100.0f, 172.0f, 172.0f, paint);
    }

    public int getPrintItemCount() {
        return this.printItemCount;
    }
}
