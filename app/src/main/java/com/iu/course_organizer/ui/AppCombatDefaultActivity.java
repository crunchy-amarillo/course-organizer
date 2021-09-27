package com.iu.course_organizer.ui;

import android.app.Activity;
import android.app.ActivityOptions;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.snackbar.Snackbar;
import com.iu.course_organizer.R;
import com.iu.course_organizer.common.utils.SharedPrefValues;
import com.iu.course_organizer.data.LoginDataSource;
import com.iu.course_organizer.data.LoginRepository;
import com.iu.course_organizer.database.CourseOrganizerDatabase;
import com.iu.course_organizer.ui.login.LoginActivity;

import java.util.Map;

public class AppCombatDefaultActivity extends AppCompatActivity {

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.default_menu, menu);

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.btnLogout:
                LoginRepository loginRepository = LoginRepository.getInstance(
                        new LoginDataSource(CourseOrganizerDatabase.getInstance(this)));
                loginRepository.logout();

                deleteSharedPreferences(SharedPrefValues.USER_DETAILS);

                Intent intent = new Intent(this, LoginActivity.class);
                startActivity(intent,
                        ActivityOptions.makeSceneTransitionAnimation(this).toBundle()
                );
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
}
