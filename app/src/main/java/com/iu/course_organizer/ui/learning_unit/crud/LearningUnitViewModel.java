package com.iu.course_organizer.ui.learning_unit.crud;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.iu.course_organizer.R;
import com.iu.course_organizer.common.DefaultResult;
import com.iu.course_organizer.common.utils.StringUtils;
import com.iu.course_organizer.data.LearningUnitNoteRepository;
import com.iu.course_organizer.data.LearningUnitRepository;
import com.iu.course_organizer.data.Result;
import com.iu.course_organizer.database.model.LearningUnit;
import com.iu.course_organizer.database.model.LearningUnitNote;

import java.util.ArrayList;
import java.util.List;

public class LearningUnitViewModel extends ViewModel {
    private final MutableLiveData<LearningUnitFormState> formState = new MutableLiveData<>();
    private final MutableLiveData<DefaultResult<Boolean>> submitResult = new MutableLiveData<>();
    private final MutableLiveData<DefaultResult<LearningUnit>> learningUnit =
            new MutableLiveData<>();
    private final MutableLiveData<DefaultResult<List<LearningUnitNote>>> learningUnitNotes =
            new MutableLiveData<>();
    private final LearningUnitRepository learningUnitRepository;
    private LearningUnitNoteRepository learningUnitNoteRepository;

    public LearningUnitViewModel(@NonNull LearningUnitRepository learningUnitRepository,
            @NonNull LearningUnitNoteRepository learningUnitNoteRepository
    ) {
        this.learningUnitRepository = learningUnitRepository;
        this.learningUnitNoteRepository = learningUnitNoteRepository;
    }

    LiveData<LearningUnitFormState> getFormState() {
        return formState;
    }

    LiveData<DefaultResult<Boolean>> getSubmitResult() {
        return submitResult;
    }

    public LiveData<DefaultResult<LearningUnit>> getLearningUnit() {
        return learningUnit;
    }

    public LiveData<DefaultResult<List<LearningUnitNote>>> getLearningUnitNotes() {
        return learningUnitNotes;
    }

    public void add(String title, String description, String workingHours, Integer courseId) {
        Thread thread = new Thread(() -> {
            Result<Void> result =
                    learningUnitRepository.add(title, description, workingHours, courseId);
            if (result instanceof Result.Success) {
                submitResult.postValue(new DefaultResult<>(true));
            } else {
                submitResult.postValue(new DefaultResult<>(false));
            }
        });
        thread.start();
    }

    public void edit(String title, String description, Integer workingHours, Integer spentMinutes,
            Integer learningUnitId
    ) {
        Thread thread = new Thread(() -> {
            Result<Void> result =
                    learningUnitRepository.edit(title, description, workingHours, spentMinutes,
                            learningUnitId
                    );
            if (result instanceof Result.Success) {
                submitResult.postValue(new DefaultResult<>(true));
            } else {
                submitResult.postValue(new DefaultResult<>(false));
            }
        });
        thread.start();
    }

    public void get(Integer learningUnitId) {
        Thread thread = new Thread(() -> {
            Result<LearningUnit> result = learningUnitRepository.findById(learningUnitId);
            if (result instanceof Result.Success) {
                learningUnit.postValue(
                        new DefaultResult<>((LearningUnit) ((Result.Success<?>) result).getData()));
            } else {
                learningUnit.postValue(new DefaultResult<>(0));
            }
        });
        thread.start();
    }


    public void dataChanged(String title, String description, String workingHours) {
        if (!StringUtils.isNotEmpty(title)) {
            formState.setValue(new LearningUnitFormState(R.string.invalid_empty, null, null));
        } else if (!StringUtils.isNotEmpty(workingHours)) {
            formState.setValue(new LearningUnitFormState(null, null, R.string.invalid_empty));
        } else {
            formState.setValue(new LearningUnitFormState(true));
        }
    }

    public void findNotesByLearningUnitId(Integer learningUnitId) {
        Thread thread = new Thread(() -> {
            Result<List<LearningUnitNote>> result =
                    learningUnitNoteRepository.findByLearningUnitId(learningUnitId);
            if (result instanceof Result.Success) {
                List<LearningUnitNote> data =
                        (List<LearningUnitNote>) ((Result.Success<?>) result).getData();
                learningUnitNotes.postValue(new DefaultResult<>(data));
            } else {
                learningUnitNotes.postValue(new DefaultResult<>(new ArrayList<>()));
            }
        });
        thread.start();
    }
}
