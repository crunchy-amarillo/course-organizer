package com.iu.course_organizer.ui.learning_unit.notes;

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
import com.iu.course_organizer.databinding.ActivityAddLearningUnitNoteBinding;
import com.iu.course_organizer.databinding.ContentAddLearningUnitNoteBinding;
import com.iu.course_organizer.ui.AppCombatDefaultActivity;
import com.iu.course_organizer.ui.learning_unit.crud.EditLearningUnitActivity;
import com.iu.course_organizer.ui.learning_unit.crud.LearningUnitActivity;

import java.util.HashMap;
import java.util.Map;

public class AddLearningUnitNoteActivity extends AppCombatDefaultActivity {

    private AddLearningUnitNoteViewModel viewModel;
    private ActivityAddLearningUnitNoteBinding binding;
    private Bundle savedInstanceState;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.savedInstanceState = savedInstanceState;

        binding = ActivityAddLearningUnitNoteBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        setSupportActionBar(binding.toolbarWrapper.toolbar);

        viewModel = new ViewModelProvider(this,
                new AddLearningUnitNoteViewModelFactory(CourseOrganizerDatabase.getInstance(this))
        ).get(AddLearningUnitNoteViewModel.class);

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
        ContentAddLearningUnitNoteBinding form = binding.includedForm;
        String learningUnitId =
                getActivityExtra(savedInstanceState, ActivityExtras.LEARNING_UNIT_ID);

        form.btnAddNote.setOnClickListener(v -> {
            form.loading.setVisibility(View.VISIBLE);
            viewModel.add(form.note.getText().toString(), Integer.valueOf(learningUnitId));
        });
    }

    private void handleCancelButton() {
        ContentAddLearningUnitNoteBinding form = binding.includedForm;
        String learningUnitId =
                getActivityExtra(savedInstanceState, ActivityExtras.LEARNING_UNIT_ID);
        Map<String, String> extras = new HashMap<>();
        extras.put(ActivityExtras.LEARNING_UNIT_ID, learningUnitId);

        form.btnCancelNote.setOnClickListener(v -> {
            form.loading.setVisibility(View.VISIBLE);
            switchActivity(EditLearningUnitActivity.class, extras);
        });
    }

    private void initFormInputChangeListener() {
        final EditText note = binding.includedForm.note;

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
                viewModel.dataChanged(note.getText().toString());
            }
        };
        note.addTextChangedListener(afterTextChangedListener);
    }

    private void observeFormInputs() {
        viewModel.getFormState().observe(this, formState -> {
            if (null == formState) {
                return;
            }

            ContentAddLearningUnitNoteBinding form = binding.includedForm;
            form.btnAddNote.setEnabled(formState.isDataValid());

            if (null != formState.getNoteError()) {
                form.note.setError(getString(formState.getNoteError()));
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
                Snackbar.make(findViewById(R.id.btnAddNote), result.getError(),
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