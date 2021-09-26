package com.iu.course_organizer.ui.learning_unit.crud;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
import android.widget.ProgressBar;

import com.iu.course_organizer.R;
import com.iu.course_organizer.common.utils.ActivityExtras;
import com.iu.course_organizer.databinding.ActivityAddLearningUnitBinding;
import com.iu.course_organizer.databinding.ContentAddLearningUnitBinding;
import com.iu.course_organizer.ui.learning_unit.list.LearningUnitListActivity;

public class AddLearningUnitActivity extends LearningUnitActivity {

    private ActivityAddLearningUnitBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityAddLearningUnitBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        setSupportActionBar(binding.toolbarWrapper.toolbar);

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
        doHandleCancelButton(form.btnCancelAddLearningUnit, form.loading);
    }

    private void initFormInputChangeListener() {
        final EditText titleInput = binding.includedForm.learningUnitTitle;
        final EditText descriptionInput = binding.includedForm.learningUnitDescription;
        final EditText workingHoursInput = binding.includedForm.learningUnitHours;

        doInitFormInputChangeListener(titleInput, descriptionInput, workingHoursInput);
    }

    private void observeFormInputs() {
        ContentAddLearningUnitBinding form = binding.includedForm;
        doObserveFormInputs(form.btnAddLearningUnit, form.learningUnitTitle,
                form.learningUnitDescription, form.learningUnitHours
        );
    }

    private void observeAddResult() {
        ProgressBar loading = binding.includedForm.loading;
        doObserveSubmitResult(R.id.btnAddLearningUnit, loading);
    }
}