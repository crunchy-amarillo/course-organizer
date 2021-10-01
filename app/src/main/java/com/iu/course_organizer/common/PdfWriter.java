package com.iu.course_organizer.common;

import android.graphics.pdf.PdfDocument;
import android.view.View;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

/**
 * @see https://developer.android.com/reference/android/graphics/pdf/PdfDocument
 */
public class PdfWriter {
    public File createPdf(View view) throws IOException {
        File file = FileUtils.getFile("CourseOrganizer.pdf");
        FileOutputStream fileOutputStream = new FileOutputStream(file);

        PdfDocument document = new PdfDocument();
        PdfDocument.PageInfo pageInfo =
                new PdfDocument.PageInfo.Builder(view.getWidth(), view.getHeight(), 1).create();
        PdfDocument.Page page = document.startPage(pageInfo);

        view.draw(page.getCanvas());

        document.finishPage(page);
        document.writeTo(fileOutputStream);
        document.close();
        fileOutputStream.close();

        return file;
    }
}
