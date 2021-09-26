package com.iu.course_organizer.ui.learning_unit.crud;

import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ProgressBar;

import com.iu.course_organizer.R;
import com.iu.course_organizer.common.utils.ActivityExtras;
import com.iu.course_organizer.databinding.ActivityEditLearningUnitBinding;
import com.iu.course_organizer.databinding.ContentEditLearningUnitBinding;

public class EditLearningUnitActivity extends LearningUnitActivity {

    private ActivityEditLearningUnitBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityEditLearningUnitBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        setSupportActionBar(binding.toolbarWrapper.toolbar);

        observeFormInputs();
        observeEditResult();
        initFormData();
        initFormInputChangeListener();
        handleSubmitButton();
        handleCancelButton();
    }

    @Override
    protected void onPostResume() {
        super.onPostResume();
        binding.includedForm.loading.setVisibility(View.INVISIBLE);
    }

    private void initFormData() {
        String learningUnitId =
                getActivityExtra(savedInstanceState, ActivityExtras.LEARNING_UNIT_ID);
        viewModel.get(Integer.valueOf(learningUnitId));
        viewModel.getLearningUnit().observe(this, learningUnit -> {
            if (null == learningUnit) {
                return;
            }
            ContentEditLearningUnitBinding form = binding.includedForm;
            if (null != learningUnit.getSuccess()) {
                form.learningUnitTitle.setText(learningUnit.getSuccess().title);
                form.learningUnitDescription.setText(learningUnit.getSuccess().description);
                form.learningUnitHours.setText(learningUnit.getSuccess().workingHours.toString());
            }
        });
    }

    private void handleSubmitButton() {
        ContentEditLearningUnitBinding form = binding.includedForm;

        String learningUnitId =
                getActivityExtra(savedInstanceState, ActivityExtras.LEARNING_UNIT_ID);

        form.btnEditLearningUnit.setOnClickListener(v -> {
            form.loading.setVisibility(View.VISIBLE);
            viewModel.edit(form.learningUnitTitle.getText().toString(),
                    form.learningUnitDescription.getText().toString(),
                    form.learningUnitHours.getText().toString(), Integer.valueOf(learningUnitId)
            );
        });
    }

    private void handleCancelButton() {
        ContentEditLearningUnitBinding form = binding.includedForm;
        doHandleCancelButton(form.btnCancelEditLearningUnit, form.loading);
    }

    private void initFormInputChangeListener() {
        final EditText titleInput = binding.includedForm.learningUnitTitle;
        final EditText descriptionInput = binding.includedForm.learningUnitDescription;
        final EditText workingHoursInput = binding.includedForm.learningUnitHours;

        doInitFormInputChangeListener(titleInput, descriptionInput, workingHoursInput);
    }

    private void observeFormInputs() {
        ContentEditLearningUnitBinding form = binding.includedForm;
        doObserveFormInputs(form.btnEditLearningUnit, form.learningUnitTitle,
                form.learningUnitDescription, form.learningUnitHours
        );
    }

    private void observeEditResult() {
        ProgressBar loading = binding.includedForm.loading;
        doObserveSubmitResult(R.id.btnEditLearningUnit, loading);
    }
}