package com.iu.course_organizer.ui.course.add;

import android.app.Activity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;

import androidx.lifecycle.ViewModelProvider;

import com.google.android.material.snackbar.Snackbar;
import com.iu.course_organizer.R;
import com.iu.course_organizer.database.CourseOrganizerDatabase;
import com.iu.course_organizer.databinding.ActivityAddCourseBinding;
import com.iu.course_organizer.databinding.ContentAddCourseBinding;
import com.iu.course_organizer.ui.AppCombatDefaultActivity;
import com.iu.course_organizer.ui.course.list.CourseListActivity;

public class AddCourseActivity extends AppCombatDefaultActivity {

    private AddCourseViewModel viewModel;
    private ActivityAddCourseBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityAddCourseBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        setSupportActionBar(binding.toolbarWrapper.toolbar);

        viewModel = new ViewModelProvider(this,
                new AddCourseViewModelFactory(CourseOrganizerDatabase.getInstance(this))
        ).get(AddCourseViewModel.class);

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
        ContentAddCourseBinding form = binding.includedForm;
        Integer userId = getLoginRepository().getUser().getUserId();

        form.btnAddCourse.setOnClickListener(v -> {
            form.loading.setVisibility(View.VISIBLE);
            viewModel.add(form.courseTitle.getText().toString(),
                    form.courseDescription.getText().toString(),
                    form.courseSession.getText().toString(), userId
            );
        });
    }

    private void handleCancelButton() {
        ContentAddCourseBinding form = binding.includedForm;
        form.btnCancelAddCourse.setOnClickListener(v -> {
            form.loading.setVisibility(View.VISIBLE);
            switchActivity(CourseListActivity.class);
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
                viewModel.dataChanged(courseTitleInput.getText().toString(),
                        courseDescriptionInput.getText().toString(),
                        courseSessionInput.getText().toString()
                );
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

            ContentAddCourseBinding form = binding.includedForm;
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

    private void observeAddResult() {
        viewModel.getResult().observe(this, result -> {
            if (result == null) {
                return;
            }
            binding.includedForm.loading.setVisibility(View.GONE);
            if (result.getError() != null) {
                Snackbar.make(findViewById(R.id.btnAddCourse), result.getError(),
                        Snackbar.LENGTH_LONG
                ).show();
            }
            if (result.getSuccess() != null) {
                setResult(Activity.RESULT_OK);
                finish();
            }
        });
    }
}