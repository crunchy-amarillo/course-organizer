package com.iu.course_organizer.common;

import android.os.Environment;

import java.io.File;
import java.io.IOException;

public class FileUtils {
    public static File getFile(String filename) throws IOException {
        File path = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOCUMENTS);
        if (!path.exists()) {
            path.mkdirs();
        }

        File file = new File(path, filename);
        if (file.exists()) {
            file.delete();
        }
        file.createNewFile();

        return file;
    }
}
