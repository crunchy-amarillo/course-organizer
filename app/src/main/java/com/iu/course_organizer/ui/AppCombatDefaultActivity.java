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
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.content.FileProvider;
import androidx.core.view.MenuCompat;

import com.google.android.material.snackbar.Snackbar;
import com.iu.course_organizer.BuildConfig;
import com.iu.course_organizer.R;
import com.iu.course_organizer.common.PdfWriter;
import com.iu.course_organizer.data.LoginDataSource;
import com.iu.course_organizer.data.LoginRepository;
import com.iu.course_organizer.data.model.LoggedInUser;
import com.iu.course_organizer.database.CourseOrganizerDatabase;
import com.iu.course_organizer.ui.course.list.CourseListActivity;
import com.iu.course_organizer.ui.login.LoginActivity;

import java.io.File;
import java.util.Map;
import java.util.Objects;

public class AppCombatDefaultActivity extends AppCompatActivity {

    private static final int PERMISSION_EXPORT_PDF_REQUEST_CODE = 1112;

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
            case R.id.btnExportAndMail:
                if (!checkStoragePermission()) {
                    requestStoragePermission(PERMISSION_EXPORT_PDF_REQUEST_CODE);
                } else {
                    doPdfExportAndMail();
                }
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        // init pdf export
        if (requestCode == PERMISSION_EXPORT_PDF_REQUEST_CODE && resultCode == Activity.RESULT_OK) {
            doPdfExportAndMail();
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

    protected void doPdfExportAndMail() {
        PdfWriter pdfWriter = new PdfWriter();
        Thread thread = new Thread(() -> {
            try {
                File pdf = pdfWriter.createPdf(findViewById(android.R.id.content));
                sendMail(getResources().getString(R.string.email_subject_export),
                        getResources().getString(R.string.email_text_export), pdf
                );
            } catch (Exception e) {
                showSnackBar(getResources().getString(R.string.error_export));
                Log.e(this.getClass().getSimpleName(), "Could not create pdf", e);
            }
        });
        thread.start();
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

    protected void sendMail(@NonNull String subject, @NonNull String text, @Nullable File attachment
    ) throws Exception {
        Intent emailIntent = new Intent(Intent.ACTION_SEND);
        emailIntent.setType("text/plain");
        emailIntent.putExtra(Intent.EXTRA_EMAIL, new String[]{""});
        emailIntent.putExtra(Intent.EXTRA_SUBJECT, subject);
        emailIntent.putExtra(Intent.EXTRA_TEXT, text);

        if (null != attachment) {
            if (!attachment.exists() || !attachment.canRead()) {
                throw new Exception("Invalid attachment");
            }

            Uri uri = FileProvider.getUriForFile(Objects.requireNonNull(getApplicationContext()),
                    BuildConfig.APPLICATION_ID + ".provider", attachment
            );
            emailIntent.putExtra(Intent.EXTRA_STREAM, uri);
        }

        startActivity(Intent.createChooser(emailIntent,
                getResources().getString(R.string.email_choose_provider)
        ));
    }
}
