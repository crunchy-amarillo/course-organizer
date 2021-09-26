package com.iu.course_organizer.ui.learning_unit.crud;

import android.app.Activity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;

import androidx.lifecycle.ViewModelProvider;

import com.google.android.material.snackbar.Snackbar;
import com.iu.course_organizer.database.CourseOrganizerDatabase;
import com.iu.course_organizer.ui.AppCombatDefaultActivity;
import com.iu.course_organizer.ui.learning_unit.list.LearningUnitListActivity;

public abstract class LearningUnitActivity extends AppCombatDefaultActivity {

    protected LearningUnitViewModel viewModel;
    protected Bundle savedInstanceState;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.savedInstanceState = savedInstanceState;

        viewModel = new ViewModelProvider(this,
                new LearningUnitViewModelFactory(CourseOrganizerDatabase.getInstance(this))
        ).get(LearningUnitViewModel.class);
    }

    protected void doInitFormInputChangeListener(EditText titleInput, EditText descriptionInput,
            EditText workingHoursInput
    ) {
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

    protected void doHandleCancelButton(Button btnCancel, ProgressBar loading) {
        btnCancel.setOnClickListener(v -> {
            loading.setVisibility(View.VISIBLE);
            switchActivity(LearningUnitListActivity.class);
        });
    }

    protected void doObserveFormInputs(Button btnSubmit, EditText txtTitle, EditText txtDescription,
            EditText txtWorkingHours
    ) {
        viewModel.getFormState().observe(this, formState -> {
            if (null == formState) {
                return;
            }

            btnSubmit.setEnabled(formState.isDataValid());

            if (null != formState.getTitleError()) {
                txtTitle.setError(getString(formState.getTitleError()));
            }
            if (null != formState.getDescriptionError()) {
                txtDescription.setError(getString(formState.getDescriptionError()));
            }
            if (null != formState.getHoursError()) {
                txtWorkingHours.setError(getString(formState.getHoursError()));
            }
        });
    }

    protected void doObserveSubmitResult(int buttonId, ProgressBar loading) {
        viewModel.getSubmitResult().observe(this, result -> {
            if (result == null) {
                return;
            }
            loading.setVisibility(View.GONE);
            if (result.getError() != null) {
                Snackbar.make(findViewById(buttonId), result.getError(), Snackbar.LENGTH_LONG)
                        .show();
            }
            if (result.getSuccess() != null) {
                setResult(Activity.RESULT_OK);
                finish();
            }
        });
    }
}
