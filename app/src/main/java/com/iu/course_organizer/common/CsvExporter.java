package com.iu.course_organizer.common;

import android.os.Environment;

import com.iu.course_organizer.data.CourseRepository;
import com.iu.course_organizer.data.LearningUnitRepository;
import com.iu.course_organizer.data.LoginRepository;
import com.iu.course_organizer.data.Result;
import com.iu.course_organizer.database.model.Course;
import com.iu.course_organizer.database.model.LearningUnit;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.List;
import java.util.StringJoiner;

public class CsvExporter {
    private final CourseRepository courseRepository;
    private final LearningUnitRepository learningUnitRepository;
    private final LoginRepository loginRepository;

    public CsvExporter(CourseRepository courseRepository,
            LearningUnitRepository learningUnitRepository, LoginRepository loginRepository
    ) {
        this.courseRepository = courseRepository;
        this.learningUnitRepository = learningUnitRepository;
        this.loginRepository = loginRepository;
    }

    public String writeCourses() throws Exception {
        File file = FileUtils.getFile("Export-CourseOrganizer-Courses.csv");

        List<Course> courses = loadCourses();

        FileWriter fileWriter = new FileWriter(file.getAbsoluteFile());
        BufferedWriter bufferedWriter = new BufferedWriter(fileWriter);
        String delimiter = CsvImporter.COMMA_DELIMITER;
        StringJoiner stringJoiner = new StringJoiner(delimiter);
        stringJoiner.add("uid");
        stringJoiner.add("title");
        stringJoiner.add("description");
        stringJoiner.add("session");

        bufferedWriter.write(stringJoiner.toString() + "\n");

        for (Course course : courses) {
            stringJoiner = new StringJoiner(delimiter);
            stringJoiner.add(course.uid.toString());
            stringJoiner.add(course.title);
            stringJoiner.add(course.description);
            stringJoiner.add(course.session.toString());
            bufferedWriter.write(stringJoiner.toString() + "\n");
        }

        bufferedWriter.close();

        return file.getAbsoluteFile().getAbsolutePath();
    }

    public String writeLearningUnits() throws Exception {
        File file = FileUtils.getFile("Export-CourseOrganizer-LearningUnits.csv");

        List<LearningUnit> learningUnits = loadLearningUnits();

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
            stringJoiner.add(learningUnit.workingHours.toString());
            stringJoiner.add(
                    null != learningUnit.spentMinutes ? learningUnit.spentMinutes.toString() : "");
            bufferedWriter.write(stringJoiner.toString() + "\n");
        }

        bufferedWriter.close();

        return file.getAbsoluteFile().getAbsolutePath();
    }

    private List<Course> loadCourses() throws Exception {
        Result<List<Course>> result =
                courseRepository.findByUserId(loginRepository.getUser().getUserId());
        if (result instanceof Result.Success) {
            return (List<Course>) ((Result.Success<?>) result).getData();
        } else {
            throw new Exception("Could not export data");
        }
    }

    private List<LearningUnit> loadLearningUnits() throws Exception {
        Result<List<LearningUnit>> result =
                learningUnitRepository.findByUserId(loginRepository.getUser().getUserId());
        if (result instanceof Result.Success) {
            return (List<LearningUnit>) ((Result.Success<?>) result).getData();
        } else {
            throw new Exception("Could not export data");
        }
    }
}
