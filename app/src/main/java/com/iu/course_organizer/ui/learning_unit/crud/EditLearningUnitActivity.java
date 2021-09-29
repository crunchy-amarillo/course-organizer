package com.iu.course_organizer.ui.learning_unit.crud;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.iu.course_organizer.R;
import com.iu.course_organizer.common.utils.ActivityExtras;
import com.iu.course_organizer.common.utils.StringUtils;
import com.iu.course_organizer.data.LearningUnitNoteRepository;
import com.iu.course_organizer.data.Result;
import com.iu.course_organizer.database.CourseOrganizerDatabase;
import com.iu.course_organizer.database.model.LearningUnitNote;
import com.iu.course_organizer.databinding.ActivityEditLearningUnitBinding;
import com.iu.course_organizer.databinding.ContentEditLearningUnitBinding;
import com.iu.course_organizer.ui.learning_unit.notes.AddLearningUnitNoteActivity;
import com.iu.course_organizer.ui.learning_unit.notes.LearningUnitNoteListEntryAdapter;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class EditLearningUnitActivity extends LearningUnitActivity {

    private ActivityEditLearningUnitBinding binding;
    private LearningUnitNoteListEntryAdapter entryAdapter;
    private RecyclerView recyclerView;
    LearningUnitNoteRepository noteRepository;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityEditLearningUnitBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        setSupportActionBar(binding.toolbarWrapper.toolbar);
         noteRepository =
                LearningUnitNoteRepository.getInstance(CourseOrganizerDatabase.getInstance(this));

        observeFormInputs();
        observeEditResult();
        initFormData();
        initFormInputChangeListener();
        handleNewButton();
        handleSubmitButton();
        handleCancelButton();
        handleTabs();

        initLearningUnitNoteListAdapter();
        loadLearningUnitNotes();
        observeLearningUnitNoteList();
        handleLearningUnitNoteDeleteGesture();
    }

    @Override
    protected void onPostResume() {
        super.onPostResume();
        binding.includedForm.loading.setVisibility(View.INVISIBLE);
        loadLearningUnitNotes();
    }

    private void loadLearningUnitNotes() {
        binding.includedForm.includedLayoutLearningUnitNotes.loading.setVisibility(View.VISIBLE);
        String learningUnitId =
                getActivityExtra(savedInstanceState, ActivityExtras.LEARNING_UNIT_ID);
        viewModel.findNotesByLearningUnitId(Integer.valueOf(learningUnitId));
    }

    private void initLearningUnitNoteListAdapter() {
        recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        entryAdapter = new LearningUnitNoteListEntryAdapter(new ArrayList<>());
        recyclerView.setAdapter(entryAdapter);
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
                Integer workingHours = learningUnit.getSuccess().workingHours;
                Integer spentMinutes = learningUnit.getSuccess().spentMinutes;
                if (null == spentMinutes) {
                    spentMinutes = 0;
                }

                form.learningUnitTitle.setText(learningUnit.getSuccess().title);
                form.learningUnitDescription.setText(learningUnit.getSuccess().description);
                form.learningUnitHours.setText(workingHours.toString());
                form.learningUnitSpentMinutes.setText(spentMinutes.toString());

                if (0 < spentMinutes) {
                    int progressPercentage =
                            Math.round(spentMinutes * 100 / ((float) workingHours * 60));
                    ProgressBar progressBar = findViewById(R.id.progressBar);
                    progressBar.setProgress(progressPercentage);
                }
            }
        });
    }

    private void handleNewButton() {
        Map<String, String> extras = new HashMap<>();
        extras.put(ActivityExtras.LEARNING_UNIT_ID,
                getActivityExtra(savedInstanceState, ActivityExtras.LEARNING_UNIT_ID)
        );

        binding.includedForm.btnAddNote.setOnClickListener(view -> {
            switchActivity(AddLearningUnitNoteActivity.class, extras);
        });
    }


    private void handleSubmitButton() {
        ContentEditLearningUnitBinding form = binding.includedForm;

        String learningUnitId =
                getActivityExtra(savedInstanceState, ActivityExtras.LEARNING_UNIT_ID);

        form.btnEditLearningUnit.setOnClickListener(v -> {
            form.loading.setVisibility(View.VISIBLE);
            String spentMinutesStr = form.learningUnitSpentMinutes.getText().toString();
            Integer spentMinutes =
                    StringUtils.isNotEmpty(spentMinutesStr) ? Integer.parseInt(spentMinutesStr) : 0;

            String workingHoursStr = form.learningUnitHours.getText().toString();
            Integer workingHours =
                    StringUtils.isNotEmpty(workingHoursStr) ? Integer.parseInt(workingHoursStr) : 0;

            viewModel.edit(form.learningUnitTitle.getText().toString(),
                    form.learningUnitDescription.getText().toString(), workingHours, spentMinutes,
                    Integer.valueOf(learningUnitId)
            );
        });
    }

    private void handleTabs() {
        ContentEditLearningUnitBinding form = binding.includedForm;
        View.OnClickListener onClickListener = view -> {
            Button tab1 = findViewById(R.id.btnTab1);
            Button tab2 = findViewById(R.id.btnTab2);
            if (view.getId() == tab1.getId()) {
                tab1.setActivated(true);
                tab1.setTextColor(getResources().getColor(R.color.primary_2, getTheme()));
                tab2.setActivated(false);
                tab2.setTextColor(getResources().getColor(R.color.muted, getTheme()));
                findViewById(R.id.tab1Wrapper).setVisibility(View.VISIBLE);
                findViewById(R.id.tab2Wrapper).setVisibility(View.GONE);
                findViewById(R.id.btnAddNote).setVisibility(View.GONE);
            } else if (view.getId() == tab2.getId()) {
                tab1.setActivated(false);
                tab1.setTextColor(getResources().getColor(R.color.muted, getTheme()));
                tab2.setActivated(true);
                tab2.setTextColor(getResources().getColor(R.color.primary_2, getTheme()));
                findViewById(R.id.tab1Wrapper).setVisibility(View.GONE);
                findViewById(R.id.tab2Wrapper).setVisibility(View.VISIBLE);
                findViewById(R.id.btnAddNote).setVisibility(View.VISIBLE);
            }
        };
        form.btnTab1.setOnClickListener(onClickListener);
        form.btnTab2.setOnClickListener(onClickListener);
    }

    private void handleCancelButton() {
        ContentEditLearningUnitBinding form = binding.includedForm;
        doHandleCancelButton(form.btnCancelEditLearningUnit, form.loading);
    }

    private void handleLearningUnitNoteDeleteGesture() {
        ItemTouchHelper itemTouchHelper = new ItemTouchHelper(new ItemTouchHelper.SimpleCallback(0,
                ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT
        ) {

            @Override
            public boolean onMove(@NonNull RecyclerView recyclerView,
                    @NonNull RecyclerView.ViewHolder viewHolder,
                    @NonNull RecyclerView.ViewHolder target
            ) {
                return false;
            }

            @Override
            public void onSwiped(RecyclerView.ViewHolder viewHolder, int swipeDir) {
                binding.includedForm.includedLayoutLearningUnitNotes.loading.setVisibility(
                        View.VISIBLE);
                LearningUnitNote learningUnitNote =
                        entryAdapter.getByPosition(viewHolder.getAdapterPosition());

                Thread thread = new Thread(() -> {
                    Result<Void> result = noteRepository.delete(learningUnitNote.uid);
                    if (result instanceof Result.Success) {
                        loadLearningUnitNotes();
                    }
                });
                thread.start();

            }
        });

        itemTouchHelper.attachToRecyclerView(recyclerView);
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

    private void observeLearningUnitNoteList() {
        viewModel.getLearningUnitNotes().observe(this, result -> {
            if (null == result) {
                return;
            }

            binding.includedForm.includedLayoutLearningUnitNotes.loading.setVisibility(View.GONE);
            if (result.getError() != null) {
                showSnackBar(result.getError().toString());
            }

            List<LearningUnitNote> learningUnitNotes = result.getSuccess();
            if (learningUnitNotes != null) {
                entryAdapter.setLearningUnitNotes(learningUnitNotes);
            }
        });
    }
}