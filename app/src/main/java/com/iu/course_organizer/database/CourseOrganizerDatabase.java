package com.iu.course_organizer.database;

import android.content.Context;

import androidx.annotation.NonNull;
import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.sqlite.db.SupportSQLiteDatabase;

import com.iu.course_organizer.database.dao.CourseDao;
import com.iu.course_organizer.database.dao.UserDao;
import com.iu.course_organizer.database.model.Course;
import com.iu.course_organizer.database.model.User;

import java.util.concurrent.Executors;

@Database(entities = {User.class, Course.class}, version = 1)
public abstract class CourseOrganizerDatabase extends RoomDatabase {
    private static CourseOrganizerDatabase instance;

    public static String databaseName = "course_organizer";

    public abstract CourseDao courseDao();

    public abstract UserDao userDao();

    public synchronized static CourseOrganizerDatabase getInstance(Context context) {
        if (instance == null) {
            instance = buildDatabase(context);
        }
        return instance;
    }

    private static CourseOrganizerDatabase buildDatabase(final Context context) {
        return Room.databaseBuilder(context,
                CourseOrganizerDatabase.class,
                databaseName)
                .addCallback(new Callback() {
                    @Override
                    public void onCreate(@NonNull SupportSQLiteDatabase db) {
                        super.onCreate(db);
                        // setup basic users
                        // todo: add user management to app
                        Executors.newSingleThreadScheduledExecutor().execute(() -> getInstance(context).userDao().insertAll(User.populateData()));
                    }
                })
                .build();
    }
}