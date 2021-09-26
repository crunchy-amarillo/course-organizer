package com.iu.course_organizer.ui.course.list;

import android.app.Activity;
import android.content.SharedPreferences;
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
import com.iu.course_organizer.common.utils.SharedPrefValues;
import com.iu.course_organizer.database.CourseOrganizerDatabase;
import com.iu.course_organizer.database.model.Course;
import com.iu.course_organizer.databinding.ActivityCourseListBinding;
import com.iu.course_organizer.ui.AppCombatDefaultActivity;
import com.iu.course_organizer.ui.course.add.AddCourseActivity;
import com.iu.course_organizer.ui.learning_unit.list.LearningUnitListActivity;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class CourseListActivity extends AppCombatDefaultActivity {

    private CourseListViewModel viewModel;
    private ActivityCourseListBinding binding;
    private CourseListEntryAdapter entryAdapter;
    private RecyclerView recyclerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityCourseListBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        setSupportActionBar(binding.toolbarWrapper.toolbar);

        viewModel = new ViewModelProvider(this,
                new CourseListViewModelFactory(CourseOrganizerDatabase.getInstance(this))
        ).get(CourseListViewModel.class);

        initCourseListAdapter();
        loadCourses();
        observeCourseList();
        observeDeleteResult();
        handleNewButton();
        handleOnItemClick();
        handleDeleteGesture();
    }

    @Override
    protected void onPostResume() {
        super.onPostResume();
        loadCourses();
    }

    private void handleNewButton() {
        binding.btnAddCourse.setOnClickListener(view -> {
            switchActivity(AddCourseActivity.class);
        });
    }

    private void handleOnItemClick() {
        recyclerView.addOnItemTouchListener(
                new RecyclerViewTouchListener(getApplicationContext(), recyclerView,
                        new RecyclerViewTouchListener.ClickListener() {
                            @Override
                            public void onClick(View view, int position) {
                                Course course = entryAdapter.getByPosition(position);
                                Map<String, String> extras = new HashMap<>();
                                extras.put(ActivityExtras.COURSE_ID, String.valueOf(course.uid));
                                switchActivity(LearningUnitListActivity.class, extras);
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
                View buttonDelete = viewHolder.itemView.findViewById(R.id.btnDelete);
                buttonDelete.setVisibility(View.VISIBLE);
                return false;
            }

            @Override
            public void onSwiped(RecyclerView.ViewHolder viewHolder, int swipeDir) {
                binding.includedLayout.loading.setVisibility(View.VISIBLE);
                Course course = entryAdapter.getByPosition(viewHolder.getAdapterPosition());
                viewModel.delete(course.uid);
            }
        });

        itemTouchHelper.attachToRecyclerView(recyclerView);
    }

    private void initCourseListAdapter() {
        recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        entryAdapter = new CourseListEntryAdapter(new ArrayList<>());
        recyclerView.setAdapter(entryAdapter);
    }

    private void loadCourses() {
        binding.includedLayout.loading.setVisibility(View.VISIBLE);
        SharedPreferences sharedPreferences =
                getSharedPreferences(SharedPrefValues.USER_DETAILS, MODE_PRIVATE);
        int userId = sharedPreferences.getInt(SharedPrefValues.USER_ID, -1);
        viewModel.findByUserId(userId);
    }

    private void observeCourseList() {
        viewModel.getCourses().observe(this, result -> {
            if (null == result) {
                return;
            }

            binding.includedLayout.loading.setVisibility(View.GONE);
            if (result.getError() != null) {
                Snackbar.make(findViewById(R.id.recyclerView), result.getError(),
                        Snackbar.LENGTH_LONG
                ).show();
            }

            List<Course> courses = result.getSuccess();
            if (courses != null) {
                entryAdapter.setCourses(courses);
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
                loadCourses();
            }
        });
    }
}