package com.iu.course_organizer.ui.learning_unit.add;

import android.app.Activity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;

import androidx.lifecycle.ViewModelProvider;

import com.google.android.material.snackbar.Snackbar;
import com.iu.course_organizer.R;
import com.iu.course_organizer.common.utils.ActivityExtras;
import com.iu.course_organizer.database.CourseOrganizerDatabase;
import com.iu.course_organizer.databinding.ActivityAddLearningUnitBinding;
import com.iu.course_organizer.databinding.ContentAddLearningUnitBinding;
import com.iu.course_organizer.ui.AppCombatDefaultActivity;
import com.iu.course_organizer.ui.learning_unit.list.LearningUnitListActivity;

public class AddLearningUnitActivity extends AppCombatDefaultActivity {

    private AddLearningUnitViewModel viewModel;
    private ActivityAddLearningUnitBinding binding;
    private Bundle savedInstanceState;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.savedInstanceState = savedInstanceState;

        binding = ActivityAddLearningUnitBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        setSupportActionBar(binding.toolbarWrapper.toolbar);

        viewModel = new ViewModelProvider(this,
                new AddLearningUnitViewModelFactory(CourseOrganizerDatabase.getInstance(this))
        ).get(AddLearningUnitViewModel.class);

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
        ContentAddLearningUnitBinding form = binding.includedForm;

        String courseIdStr = getActivityExtra(savedInstanceState, ActivityExtras.COURSE_ID);

        form.btnAddLearningUnit.setOnClickListener(v -> {
            form.loading.setVisibility(View.VISIBLE);
            viewModel.add(form.learningUnitTitle.getText().toString(),
                    form.learningUnitDescription.getText().toString(),
                    form.learningUnitHours.getText().toString(), Integer.valueOf(courseIdStr)
            );
        });
    }

    private void handleCancelButton() {
        ContentAddLearningUnitBinding form = binding.includedForm;
        form.btnCancelAddLearningUnit.setOnClickListener(v -> {
            form.loading.setVisibility(View.VISIBLE);
            switchActivity(LearningUnitListActivity.class);
        });
    }

    private void initFormInputChangeListener() {
        final EditText titleInput = binding.includedForm.learningUnitTitle;
        final EditText descriptionInput = binding.includedForm.learningUnitDescription;
        final EditText workingHoursInput = binding.includedForm.learningUnitHours;

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
                viewModel.dataChanged(titleInput.getText().toString(),
                        descriptionInput.getText().toString(),
                        workingHoursInput.getText().toString()
                );
            }
        };
        titleInput.addTextChangedListener(afterTextChangedListener);
        descriptionInput.addTextChangedListener(afterTextChangedListener);
        workingHoursInput.addTextChangedListener(afterTextChangedListener);
    }

    private void observeFormInputs() {
        viewModel.getFormState().observe(this, formState -> {
            if (null == formState) {
                return;
            }

            ContentAddLearningUnitBinding form = binding.includedForm;
            form.btnAddLearningUnit.setEnabled(formState.isDataValid());

            if (null != formState.getTitleError()) {
                form.learningUnitTitle.setError(getString(formState.getTitleError()));
            }
            if (null != formState.getDescriptionError()) {
                form.learningUnitDescription.setError(getString(formState.getDescriptionError()));
            }
            if (null != formState.getHoursError()) {
                form.learningUnitHours.setError(getString(formState.getHoursError()));
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
                Snackbar.make(findViewById(R.id.btnAddLearningUnit), result.getError(),
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