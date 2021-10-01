package com.iu.course_organizer.ui.course.list;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.snackbar.Snackbar;
import com.iu.course_organizer.R;
import com.iu.course_organizer.common.CsvExporter;
import com.iu.course_organizer.common.CsvImporter;
import com.iu.course_organizer.common.RecyclerViewTouchListener;
import com.iu.course_organizer.common.utils.ActivityExtras;
import com.iu.course_organizer.data.CourseRepository;
import com.iu.course_organizer.data.LearningUnitRepository;
import com.iu.course_organizer.data.LoginDataSource;
import com.iu.course_organizer.data.LoginRepository;
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

    private static final int PERMISSION_REQUEST_CODE_IMPORT = 2297;
    private static final int PERMISSION_REQUEST_CODE_EXPORT = 2298;

    private CourseListViewModel viewModel;
    private ActivityCourseListBinding binding;
    private CourseListEntryAdapter entryAdapter;
    private RecyclerView recyclerView;

    private static final int PICKFILE_RESULT_CODE = 42;

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

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.btnImport:
                if (!checkStoragePermission()) {
                    doRequestStoragePermission(PERMISSION_REQUEST_CODE_IMPORT);
                } else {
                    handleImport();
                }
                return true;
            case R.id.btnExport:
                if (!checkStoragePermission()) {
                    doRequestStoragePermission(PERMISSION_REQUEST_CODE_EXPORT);
                } else {
                    handleExport();
                }
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    protected int getMenuId() {
        return R.menu.course_list_menu;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        // init import
        if (requestCode == PERMISSION_REQUEST_CODE_IMPORT && resultCode == Activity.RESULT_OK) {
            handleImport();
        }
        // init export
        else if (requestCode == PERMISSION_REQUEST_CODE_EXPORT &&
                resultCode == Activity.RESULT_OK) {
            handleExport();
        }
        // handle import result
        else if (requestCode == PICKFILE_RESULT_CODE && resultCode == Activity.RESULT_OK) {
            Thread thread = new Thread(() -> {
                Uri uri = data.getData();
                CourseOrganizerDatabase database = CourseOrganizerDatabase.getInstance(this);
                CourseRepository courseRepository = CourseRepository.getInstance(database);
                LoginRepository loginRepository =
                        LoginRepository.getInstance(new LoginDataSource(database));
                CsvImporter csvImporter = new CsvImporter(courseRepository, loginRepository);
                try {
                    csvImporter.writeCourses(uri);
                    loadCourses();
                    showSnackBar(getResources().getString(R.string.import_successful));
                } catch (Exception e) {
                    showSnackBar(getResources().getString(R.string.error_import));
                }
            });
            thread.start();
        }
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

    private void doRequestStoragePermission(int requestCode) {
        requestStoragePermission(requestCode);
    }

    private void handleImport() {
        Intent chooseFile = new Intent(Intent.ACTION_GET_CONTENT);
        chooseFile.addCategory(Intent.CATEGORY_OPENABLE);
        chooseFile.setType("text/*");

        String chooseTitle = getResources().getString(R.string.choose_csv_file);
        startActivityForResult(Intent.createChooser(chooseFile, chooseTitle), PICKFILE_RESULT_CODE);
    }

    private void handleExport() {
        Thread thread = new Thread(() -> {
            try {
                CourseOrganizerDatabase database = CourseOrganizerDatabase.getInstance(this);
                CourseRepository courseRepository = CourseRepository.getInstance(database);
                LearningUnitRepository learningUnitRepository =
                        LearningUnitRepository.getInstance(database);
                LoginRepository loginRepository =
                        LoginRepository.getInstance(new LoginDataSource(database));
                CsvExporter csvExporter =
                        new CsvExporter(courseRepository, learningUnitRepository, loginRepository);

                String filePath = csvExporter.writeCourses();
                showSnackBar(getResources().getString(R.string.export_succesful, filePath));
            } catch (Exception sqlEx) {
                showSnackBar(getResources().getString(R.string.error_export));
                Log.e(this.getClass().getSimpleName(), sqlEx.getMessage(), sqlEx);
            }
        });
        thread.start();
    }

    private void initCourseListAdapter() {
        recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        entryAdapter = new CourseListEntryAdapter(new ArrayList<>());
        recyclerView.setAdapter(entryAdapter);
    }

    private void loadCourses() {
        binding.includedLayout.loading.setVisibility(View.VISIBLE);
        Integer userId = getLoginRepository().getUser().getUserId();
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