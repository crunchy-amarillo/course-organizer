package com.iu.course_organizer.ui;

import android.app.Activity;
import android.app.ActivityOptions;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.MenuCompat;

import com.google.android.material.snackbar.Snackbar;
import com.iu.course_organizer.R;
import com.iu.course_organizer.common.CsvWriter;
import com.iu.course_organizer.data.CourseRepository;
import com.iu.course_organizer.data.LearningUnitRepository;
import com.iu.course_organizer.data.LoginDataSource;
import com.iu.course_organizer.data.LoginRepository;
import com.iu.course_organizer.data.model.LoggedInUser;
import com.iu.course_organizer.database.CourseOrganizerDatabase;
import com.iu.course_organizer.ui.course.list.CourseListActivity;
import com.iu.course_organizer.ui.learning_unit.list.LearningUnitListActivity;
import com.iu.course_organizer.ui.login.LoginActivity;

import java.util.Map;

public class AppCombatDefaultActivity extends AppCompatActivity {
    private LoginRepository loginRepository;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        loginRepository = LoginRepository.getInstance(
                new LoginDataSource(CourseOrganizerDatabase.getInstance(this)));
        checkLogin();
    }

    @Override
    protected void onResume() {
        super.onResume();
        checkLogin();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.default_menu, menu);

        MenuCompat.setGroupDividerEnabled(menu, true);

        LoggedInUser user = loginRepository.getUser();
        MenuItem item = menu.findItem(R.id.menuUser);
        item.setTitle(getResources().getString(R.string.loggedin_user,
                null == user ? "" : user.getDisplayName()
        ));

        menu.findItem(R.id.btnExport).setVisible(showMenuExportItem());

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.btnLogout:
                doLogout();
                return true;
            case R.id.btnExport:
                doExport();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    protected void switchActivity(Class<? extends Activity> activityClass) {
        switchActivity(activityClass, null);
    }

    protected void switchActivity(Class<? extends Activity> activityClass,
            @Nullable Map<String, String> extras
    ) {
        Intent intent = new Intent(this, activityClass);

        if (null != extras) {
            extras.forEach(intent::putExtra);
        }

        startActivity(intent, ActivityOptions.makeSceneTransitionAnimation(this).toBundle());
    }

    public LoginRepository getLoginRepository() {
        return loginRepository;
    }

    protected String getActivityExtra(Bundle savedInstanceState, String identifier) {
        String extraValue;

        if (null == savedInstanceState) {
            Bundle extras = getIntent().getExtras();
            if (extras == null) {
                extraValue = null;
            } else {
                extraValue = extras.getString(identifier);
            }
        } else {
            extraValue = (String) savedInstanceState.getSerializable(identifier);
        }

        return extraValue;
    }

    protected void showSnackBar(String message) {
        View rootView = this.getWindow().getDecorView().findViewById(android.R.id.content);
        Snackbar.make(rootView, message, Snackbar.LENGTH_LONG).show();
    }

    protected boolean showMenuExportItem() {
        return false;
    }

    private void checkLogin() {
        if (!loginRepository.isLoggedIn()) {
            doLogout();
        }
    }

    private void doExport() {
        Thread thread = new Thread(() -> {
            try {
                CourseRepository courseRepository =
                        CourseRepository.getInstance(CourseOrganizerDatabase.getInstance(this));
                LearningUnitRepository learningUnitRepository = LearningUnitRepository.getInstance(
                        CourseOrganizerDatabase.getInstance(this));
                CsvWriter csvWriter =
                        new CsvWriter(courseRepository, learningUnitRepository, loginRepository);

                String filePath = "";
                if (this instanceof LearningUnitListActivity) {
                    filePath = csvWriter.writeLearningUnits();
                }
                if (this instanceof CourseListActivity) {
                    filePath = csvWriter.writeCourses();
                }

                showSnackBar(getResources().getString(R.string.export_succesful, filePath));
            } catch (Exception sqlEx) {
                showSnackBar(getResources().getString(R.string.error_export));
                Log.e(this.getClass().getSimpleName(), sqlEx.getMessage(), sqlEx);
            }
        });
        thread.start();
    }

    private void doLogout() {
        loginRepository.logout();

        Intent intent = new Intent(this, LoginActivity.class);
        startActivity(intent, ActivityOptions.makeSceneTransitionAnimation(this).toBundle());
    }
}
