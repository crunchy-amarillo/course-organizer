package com.iu.course_organizer.ui.new_course;

import android.app.Activity;
import android.app.ActivityOptions;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;

import androidx.lifecycle.ViewModelProvider;

import com.google.android.material.snackbar.Snackbar;
import com.iu.course_organizer.R;
import com.iu.course_organizer.common.utils.SharedPrefValues;
import com.iu.course_organizer.data.LoginRepository;
import com.iu.course_organizer.database.CourseOrganizerDatabase;
import com.iu.course_organizer.databinding.ActivityNewCourseBinding;
import com.iu.course_organizer.databinding.ContentNewCourseBinding;
import com.iu.course_organizer.ui.AppCombatDefaultActivity;
import com.iu.course_organizer.ui.course_list.CourseListActivity;

public class NewCourseActivity extends AppCombatDefaultActivity {

    private NewCourseViewModel viewModel;
    private ActivityNewCourseBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityNewCourseBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        setSupportActionBar(binding.toolbarWrapper.toolbar);

        viewModel = new ViewModelProvider(this, new NewCourseViewModelFactory(CourseOrganizerDatabase.getInstance(this)))
                .get(NewCourseViewModel.class);

        observeFormInputs();
        observeAddResult();
        initFormInputChangeListener();
        handleSubmitButton();
        handleCancelButton();
    }

    @Override
    protected void onPostResume() {
        super.onPostResume();
        binding.includedForm.loading.setVisibility(View.INVISIBLE);
    }

    private void handleSubmitButton() {
        ContentNewCourseBinding form = binding.includedForm;
        SharedPreferences sharedPreferences = getSharedPreferences(SharedPrefValues.USER_DETAILS, MODE_PRIVATE);
        int userId = sharedPreferences.getInt(SharedPrefValues.USER_ID, -1);

        form.btnAddCourse.setOnClickListener(v -> {
            form.loading.setVisibility(View.VISIBLE);
            viewModel.add(form.courseTitle.getText().toString(),
                    form.courseDescription.getText().toString(),
                    form.courseSession.getText().toString(), userId);
        });
    }

    private void handleCancelButton() {
        ContentNewCourseBinding form = binding.includedForm;
        form.btnCancelAddCourse.setOnClickListener(v -> {
            form.loading.setVisibility(View.VISIBLE);
            Intent intent = new Intent(this, CourseListActivity.class);
            startActivity(intent, ActivityOptions.makeSceneTransitionAnimation(this).toBundle());
        });
    }

    private void initFormInputChangeListener() {
        final EditText courseTitleInput = binding.includedForm.courseTitle;
        final EditText courseDescriptionInput = binding.includedForm.courseDescription;
        final EditText courseSessionInput = binding.includedForm.courseSession;

        TextWatcher afterTextChangedListener = new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                // ignore
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                // ignore
            }

            @Override
            public void afterTextChanged(Editable s) {
                viewModel.dataChanged(courseTitleInput.getText().toString(), courseDescriptionInput.getText().toString(), courseSessionInput.getText().toString());
            }
        };
        courseTitleInput.addTextChangedListener(afterTextChangedListener);
        courseDescriptionInput.addTextChangedListener(afterTextChangedListener);
        courseSessionInput.addTextChangedListener(afterTextChangedListener);
    }

    private void observeFormInputs() {
        viewModel.getFormState().observe(this, formState -> {
            if (null == formState) {
                return;
            }

            ContentNewCourseBinding form = binding.includedForm;
            form.btnAddCourse.setEnabled(formState.isDataValid());

            if (null != formState.getCourseTitleError()) {
                form.courseTitle.setError(getString(formState.getCourseTitleError()));
            }
            if (null != formState.getCourseDescriptionError()) {
                form.courseDescription.setError(getString(formState.getCourseDescriptionError()));
            }
            if (null != formState.getCourseSessionError()) {
                form.courseSession.setError(getString(formState.getCourseSessionError()));
            }
        });
    }

    private void observeAddResult()
    {
        viewModel.getResult().observe(this, result -> {
            if (result == null) {
                return;
            }
            binding.includedForm.loading.setVisibility(View.GONE);
            if (result.getError() != null) {
                Snackbar.make(findViewById(R.id.btnAddCourse), result.getError(), Snackbar.LENGTH_LONG).show();
            }
            if (result.getSuccess() != null) {
                setResult(Activity.RESULT_OK);
                finish();
            }
        });
    }
}