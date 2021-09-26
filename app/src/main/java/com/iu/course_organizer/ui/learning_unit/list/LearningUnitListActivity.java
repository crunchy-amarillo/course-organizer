package com.iu.course_organizer.ui.learning_unit.list;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.snackbar.Snackbar;
import com.iu.course_organizer.R;
import com.iu.course_organizer.common.RecyclerViewTouchListener;
import com.iu.course_organizer.common.utils.ActivityExtras;
import com.iu.course_organizer.database.CourseOrganizerDatabase;
import com.iu.course_organizer.database.model.Course;
import com.iu.course_organizer.database.model.LearningUnit;
import com.iu.course_organizer.databinding.ActivityLearningUnitListBinding;
import com.iu.course_organizer.ui.AppCombatDefaultActivity;
import com.iu.course_organizer.ui.learning_unit.add.AddLearningUnitActivity;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class LearningUnitListActivity extends AppCombatDefaultActivity {

    private LearningUnitListViewModel viewModel;
    private ActivityLearningUnitListBinding binding;
    private Bundle savedInstanceState;
    private LearningUnitListEntryAdapter entryAdapter;
    private RecyclerView recyclerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.savedInstanceState = savedInstanceState;

        binding = ActivityLearningUnitListBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        setSupportActionBar(binding.toolbarWrapper.toolbar);

        viewModel = new ViewModelProvider(this,
                new LearningUnitListViewModelFactory(CourseOrganizerDatabase.getInstance(this))
        ).get(LearningUnitListViewModel.class);

        initLearningUnitListAdapter();
        loadLearningUnits();
        observeLearningUnitList();
        observeDeleteResult();
        handleNewButton();
        handleOnItemClick();
        handleDeleteGesture();
    }

    @Override
    protected void onPostResume() {
        super.onPostResume();
        loadLearningUnits();
    }

    private void handleNewButton() {
        Map<String, String> extras = new HashMap<>();
        extras.put(ActivityExtras.COURSE_ID,
                getActivityExtra(savedInstanceState, ActivityExtras.COURSE_ID)
        );

        binding.btnAddLearningUnit.setOnClickListener(view -> {
            switchActivity(AddLearningUnitActivity.class, extras);
        });
    }

    private void handleOnItemClick() {
        recyclerView.addOnItemTouchListener(
                new RecyclerViewTouchListener(getApplicationContext(), recyclerView,
                        new RecyclerViewTouchListener.ClickListener() {
                            @Override
                            public void onClick(View view, int position) {
                                LearningUnit learningUnit = entryAdapter.getByPosition(position);
                                Map<String, String> extras = new HashMap<>();
                                extras.put(ActivityExtras.LEARNING_UNIT_ID, String.valueOf(learningUnit.uid));
                                // todo: switch to edit mode
                                //switchActivity(LearningUnitListActivity.class, extras);
                            }

                            @Override
                            public void onLongClick(View view, int position) {
                                // implement long click if necessary
                            }
                        }
                ));
    }

    private void handleDeleteGesture() {
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
                binding.includedLayout.loading.setVisibility(View.VISIBLE);
                LearningUnit learningUnit =
                        entryAdapter.getByPosition(viewHolder.getAdapterPosition());
                viewModel.delete(learningUnit.uid);
            }
        });

        itemTouchHelper.attachToRecyclerView(recyclerView);
    }

    private void initLearningUnitListAdapter() {
        recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        entryAdapter = new LearningUnitListEntryAdapter(new ArrayList<>());
        recyclerView.setAdapter(entryAdapter);
    }

    private void loadLearningUnits() {
        binding.includedLayout.loading.setVisibility(View.VISIBLE);
        String courseIdStr = getActivityExtra(savedInstanceState, ActivityExtras.COURSE_ID);
        viewModel.findByCourseId(Integer.valueOf(courseIdStr));
    }

    private void observeLearningUnitList() {
        viewModel.getLearningUnits().observe(this, result -> {
            if (null == result) {
                return;
            }

            binding.includedLayout.loading.setVisibility(View.GONE);
            if (result.getError() != null) {
                Snackbar.make(findViewById(R.id.recyclerView), result.getError(),
                        Snackbar.LENGTH_LONG
                ).show();
            }

            List<LearningUnit> learningUnits = result.getSuccess();
            if (learningUnits != null) {
                entryAdapter.setLearningUnits(learningUnits);
            }
        });
    }

    private void observeDeleteResult() {
        viewModel.getDeleteResult().observe(this, result -> {
            if (result == null) {
                return;
            }

            binding.includedLayout.loading.setVisibility(View.GONE);
            if (result.getError() != null) {
                Snackbar.make(findViewById(R.id.btnDelete), result.getError(), Snackbar.LENGTH_LONG)
                        .show();
            }
            if (result.getSuccess() != null) {
                setResult(Activity.RESULT_OK);
                loadLearningUnits();
            }
        });
    }
}