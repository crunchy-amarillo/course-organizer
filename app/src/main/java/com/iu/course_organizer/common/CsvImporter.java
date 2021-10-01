package com.iu.course_organizer.common;

import android.net.Uri;
import android.os.Environment;
import android.provider.DocumentsContract;

import com.iu.course_organizer.data.CourseRepository;
import com.iu.course_organizer.data.LoginRepository;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class CsvImporter {
    public static final String COMMA_DELIMITER = ",";
    private final CourseRepository courseRepository;
    private LoginRepository loginRepository;

    public CsvImporter(CourseRepository courseRepository, LoginRepository loginRepository) {
        this.courseRepository = courseRepository;
        this.loginRepository = loginRepository;
    }

    public void writeCourses(Uri uri) throws Exception {
        File file = new File(getFilePath(uri));
        FileReader fileReader = new FileReader(file);
        List<Map<String, Object>> csvData = new ArrayList<>();
        Map<Integer, String> headerMapHelper = new HashMap<>();

        // map csv data
        int lineNumber = 0;
        try (BufferedReader br = new BufferedReader(fileReader)) {
            String line;

            while ((line = br.readLine()) != null) {
                Map<String, Object> csvDataRow = new HashMap<>();
                List<String> values = Arrays.asList(line.split(COMMA_DELIMITER));

                int column = 0;
                for (String value : values) {
                    if (0 == lineNumber) {
                        headerMapHelper.put(column, value);
                    } else {
                        String identifier = headerMapHelper.get(column);
                        csvDataRow.put(identifier, value);
                    }
                    column++;
                }

                if (lineNumber > 0) {
                    csvData.add(csvDataRow);
                }
                lineNumber++;
            }
        }

        // write to database
        if (csvData.size() > 0) {
            for (Map<String, Object> row : csvData) {
                if (!row.containsKey("title") || !row.containsKey("description") ||
                        !row.containsKey("session")) {
                    throw new Exception("invalid import data");
                }
            }
            for (Map<String, Object> row : csvData) {
                courseRepository.add((String) row.get("title"), (String) row.get("description"),
                        (String) row.get("session"), loginRepository.getUser().getUserId()
                );
            }
        }
    }

    /**
     * @see https://stackoverflow.com/questions/36128077/android-opening-a-file-with-action-get-content-results-into-different-uris
     */
    private String getFilePath(Uri uri) {
        if (isExternalStorageDocument(uri)) {
            final String docId = DocumentsContract.getDocumentId(uri);
            final String[] split = docId.split(":");
            final String type = split[0];

            if ("primary".equalsIgnoreCase(type)) {
                return Environment.getExternalStorageDirectory() + "/" + split[1];
            }
            // TODO handle non-primary volumes
        }
        // DownloadsProvider
        else if (isDownloadsDocument(uri)) {
            // todo: implement
        }
        // MediaProvider
        else if (isMediaDocument(uri)) {
            // todo: implement
        }

        return null;
    }

    private boolean isExternalStorageDocument(Uri uri) {
        return "com.android.externalstorage.documents".equals(uri.getAuthority());
    }

    private boolean isDownloadsDocument(Uri uri) {
        return "com.android.providers.downloads.documents".equals(uri.getAuthority());
    }

    private boolean isMediaDocument(Uri uri) {
        return "com.android.providers.media.documents".equals(uri.getAuthority());
    }

}
