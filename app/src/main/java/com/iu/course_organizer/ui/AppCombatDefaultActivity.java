package com.iu.course_organizer.ui;

import android.app.ActivityOptions;
import android.content.Intent;
import android.view.Menu;
import android.view.MenuItem;

import androidx.appcompat.app.AppCompatActivity;

import com.iu.course_organizer.R;
import com.iu.course_organizer.common.utils.SharedPrefValues;
import com.iu.course_organizer.data.LoginDataSource;
import com.iu.course_organizer.data.LoginRepository;
import com.iu.course_organizer.database.CourseOrganizerDatabase;
import com.iu.course_organizer.ui.login.LoginActivity;

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
                LoginRepository loginRepository = LoginRepository.getInstance(new LoginDataSource(CourseOrganizerDatabase.getInstance(this)));
                loginRepository.logout();

                deleteSharedPreferences(SharedPrefValues.USER_DETAILS);

                Intent intent = new Intent(this, LoginActivity.class);
                startActivity(intent, ActivityOptions.makeSceneTransitionAnimation(this).toBundle());
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
}
