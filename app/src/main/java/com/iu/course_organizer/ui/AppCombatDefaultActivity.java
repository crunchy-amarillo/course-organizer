package com.iu.course_organizer.ui;

import static android.Manifest.permission.READ_EXTERNAL_STORAGE;
import static android.Manifest.permission.WRITE_EXTERNAL_STORAGE;
import static android.os.Build.VERSION.SDK_INT;

import android.app.Activity;
import android.app.ActivityOptions;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.Settings;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.view.MenuCompat;

import com.google.android.material.snackbar.Snackbar;
import com.iu.course_organizer.R;
import com.iu.course_organizer.data.LoginDataSource;
import com.iu.course_organizer.data.LoginRepository;
import com.iu.course_organizer.data.model.LoggedInUser;
import com.iu.course_organizer.database.CourseOrganizerDatabase;
import com.iu.course_organizer.ui.course.list.CourseListActivity;
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
        getMenuInflater().inflate(getMenuId(), menu);

        MenuCompat.setGroupDividerEnabled(menu, true);

        LoggedInUser user = loginRepository.getUser();
        MenuItem item = menu.findItem(R.id.menuUser);
        item.setTitle(getResources().getString(R.string.loggedin_user,
                null == user ? "" : user.getDisplayName()
        ));

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.btnLogout:
                doLogout();
                return true;
            case R.id.btnCourseList:
                switchActivity(CourseListActivity.class);
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

    protected int getMenuId() {
        return R.menu.default_menu;
    }

    private void checkLogin() {
        if (!loginRepository.isLoggedIn()) {
            doLogout();
        }
    }

    private void doLogout() {
        loginRepository.logout();
        switchActivity(LoginActivity.class);
    }

    protected void requestStoragePermission(int requestCode) {
        if (SDK_INT >= Build.VERSION_CODES.R) {
            try {
                Intent intent = new Intent(Settings.ACTION_MANAGE_APP_ALL_FILES_ACCESS_PERMISSION);
                intent.addCategory("android.intent.category.DEFAULT");
                intent.setData(Uri.parse(
                        String.format("package:%s", getApplicationContext().getPackageName())));
                startActivityForResult(intent, requestCode);
            } catch (Exception e) {
                Intent intent = new Intent();
                intent.setAction(Settings.ACTION_MANAGE_ALL_FILES_ACCESS_PERMISSION);
                startActivityForResult(intent, requestCode);
            }
        } else {
            //below android 11
            ActivityCompat.requestPermissions(this, new String[]{WRITE_EXTERNAL_STORAGE},
                    requestCode
            );
        }
    }

    protected boolean checkStoragePermission() {
        if (SDK_INT >= Build.VERSION_CODES.R) {
            return Environment.isExternalStorageManager();
        } else {
            int resultRead = ContextCompat.checkSelfPermission(this, READ_EXTERNAL_STORAGE);
            int resultWrite = ContextCompat.checkSelfPermission(this, WRITE_EXTERNAL_STORAGE);
            return resultRead == PackageManager.PERMISSION_GRANTED &&
                    resultWrite == PackageManager.PERMISSION_GRANTED;
        }
    }
}
