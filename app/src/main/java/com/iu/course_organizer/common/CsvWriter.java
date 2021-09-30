package com.iu.course_organizer.common;

import android.os.Environment;

import com.iu.course_organizer.data.LearningUnitRepository;
import com.iu.course_organizer.data.LoginRepository;
import com.iu.course_organizer.data.Result;
import com.iu.course_organizer.database.model.LearningUnit;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.List;
import java.util.StringJoiner;

public class CsvWriter {
    private LearningUnitRepository learningUnitRepository;
    private LoginRepository loginRepository;

    public CsvWriter(LearningUnitRepository learningUnitRepository, LoginRepository loginRepository
    ) {
        this.learningUnitRepository = learningUnitRepository;
        this.loginRepository = loginRepository;
    }

    public String write() throws Exception {
        File file = getFile();

        List<LearningUnit> learningUnits = loadData();
        return writeData(learningUnits, file);
    }

    private String writeData(List<LearningUnit> learningUnits, File file) throws IOException {
        FileWriter fileWriter = new FileWriter(file.getAbsoluteFile());
        BufferedWriter bufferedWriter = new BufferedWriter(fileWriter);
        String separator = ",";
        StringJoiner stringJoiner = new StringJoiner(separator);
        stringJoiner.add("uid");
        stringJoiner.add("title");
        stringJoiner.add("description");
        stringJoiner.add("workingHours");
        stringJoiner.add("spentMinutes");

        bufferedWriter.write(stringJoiner.toString() + "\n");

        for (LearningUnit learningUnit : learningUnits) {
            stringJoiner = new StringJoiner(separator);
            stringJoiner.add(learningUnit.uid.toString());
            stringJoiner.add(learningUnit.title);
            stringJoiner.add(learningUnit.description);
            stringJoiner.add(null != learningUnit.workingHours ? learningUnit.workingHours.toString() : "");
            stringJoiner.add(
                    null != learningUnit.spentMinutes ? learningUnit.spentMinutes.toString() : "");
            bufferedWriter.write(stringJoiner.toString() + "\n");
        }

        bufferedWriter.close();

        return file.getAbsoluteFile().getAbsolutePath();
    }

    private List<LearningUnit> loadData() throws Exception {
        Result<List<LearningUnit>> result =
                learningUnitRepository.findByUserId(loginRepository.getUser().getUserId());
        if (result instanceof Result.Success) {
            return (List<LearningUnit>) ((Result.Success<?>) result).getData();
        } else {
            throw new Exception("Could not export data");
        }
    }

    private File getFile() throws IOException {
        File path = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOCUMENTS);
        if (!path.exists()) {
            path.mkdirs();
        }

        File file = new File(path, "Export-CourseOrganizer.csv");
        if (file.exists()) {
            file.delete();
        }
        file.createNewFile();

        return file;
    }
}
