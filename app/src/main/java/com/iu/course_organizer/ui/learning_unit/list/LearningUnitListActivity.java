package com.iu.course_organizer.ui.learning_unit.list;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.iu.course_organizer.R;
import com.iu.course_organizer.common.Timer;
import com.iu.course_organizer.common.utils.ActivityExtras;
import com.iu.course_organizer.database.CourseOrganizerDatabase;
import com.iu.course_organizer.database.model.LearningUnit;
import com.iu.course_organizer.databinding.ActivityLearningUnitListBinding;
import com.iu.course_organizer.ui.AppCombatDefaultActivity;
import com.iu.course_organizer.ui.learning_unit.crud.AddLearningUnitActivity;
import com.iu.course_organizer.ui.learning_unit.crud.EditLearningUnitActivity;

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

        handleNewButton();
        handleOnItemClick();
        handleOnStartButtonClick();
        handleOnStopButtonClick();
        handleDeleteGesture();

        observeLearningUnitList();
        observeDeleteResult();
        observeIncreaseMinutesResult();
    }

    @Override
    protected void onPostResume() {
        super.onPostResume();
        loadLearningUnits();
    }

    protected boolean showMenuExportItem()
    {
        return true;
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
        entryAdapter.setOnItemClickListener(view -> {
            int itemPosition = recyclerView.getChildAdapterPosition(view);
            LearningUnit learningUnit = entryAdapter.getByPosition(itemPosition);
            Map<String, String> extras = new HashMap<>();
            extras.put(ActivityExtras.LEARNING_UNIT_ID, String.valueOf(learningUnit.uid));
            switchActivity(EditLearningUnitActivity.class, extras);
        });
    }

    private void handleOnStartButtonClick() {
        entryAdapter.setStartButtonListener((view, position) -> {
            View containingItemView = recyclerView.findContainingItemView(view);
            containingItemView.findViewById(R.id.btnStartTimeTracking).setVisibility(View.GONE);
            containingItemView.findViewById(R.id.btnStopTimeTracking).setVisibility(View.VISIBLE);
            Timer.getInstance(position).start();
            showSnackBar(getResources().getString(R.string.start_recording));
        });
    }

    private void handleOnStopButtonClick() {
        entryAdapter.setStopButtonListener((view, position) -> {
            View containingItemView = recyclerView.findContainingItemView(view);
            binding.includedLayoutLearningUnits.loading.setVisibility(View.VISIBLE);
            containingItemView.findViewById(R.id.btnStartTimeTracking).setVisibility(View.VISIBLE);
            containingItemView.findViewById(R.id.btnStopTimeTracking).setVisibility(View.GONE);
            int minutes = Timer.getInstance(position).stop();
            LearningUnit learningUnit = entryAdapter.getByPosition(position);
            viewModel.increaseTimeTracking(learningUnit.uid, minutes);
            showSnackBar(getResources().getString(R.string.stop_recording));
            loadLearningUnits();
        });
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
                binding.includedLayoutLearningUnits.loading.setVisibility(View.VISIBLE);
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
        binding.includedLayoutLearningUnits.loading.setVisibility(View.VISIBLE);
        String courseIdStr = getActivityExtra(savedInstanceState, ActivityExtras.COURSE_ID);
        viewModel.findByCourseId(Integer.valueOf(courseIdStr));
    }

    private void observeLearningUnitList() {
        viewModel.getLearningUnits().observe(this, result -> {
            if (null == result) {
                return;
            }

            binding.includedLayoutLearningUnits.loading.setVisibility(View.GONE);
            if (result.getError() != null) {
                showSnackBar(result.getError().toString());
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

            binding.includedLayoutLearningUnits.loading.setVisibility(View.GONE);
            if (result.getError() != null) {
                showSnackBar(result.getError().toString());
            }
            if (result.getSuccess() != null) {
                setResult(Activity.RESULT_OK);
                loadLearningUnits();
            }
        });
    }

    private void observeIncreaseMinutesResult() {
        viewModel.getIncreaseMinutesResult().observe(this, result -> {
            if (result == null) {
                return;
            }

            binding.includedLayoutLearningUnits.loading.setVisibility(View.GONE);
            if (result.getError() != null) {
                showSnackBar(result.getError().toString());
            }
            if (result.getSuccess() != null) {
                setResult(Activity.RESULT_OK);
                showSnackBar(getResources().getString(R.string.finished_recording));
            }
        });
    }
}