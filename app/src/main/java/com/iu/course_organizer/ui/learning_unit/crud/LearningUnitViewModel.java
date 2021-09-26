package com.iu.course_organizer.ui.learning_unit.crud;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.iu.course_organizer.R;
import com.iu.course_organizer.common.DefaultResult;
import com.iu.course_organizer.common.utils.StringUtils;
import com.iu.course_organizer.data.LearningUnitRepository;
import com.iu.course_organizer.data.Result;
import com.iu.course_organizer.database.model.LearningUnit;

public class LearningUnitViewModel extends ViewModel {
    private final MutableLiveData<LearningUnitFormState> formState = new MutableLiveData<>();
    private final MutableLiveData<DefaultResult<Boolean>> submitResult = new MutableLiveData<>();
    private final MutableLiveData<DefaultResult<LearningUnit>> learningUnit = new MutableLiveData<>();
    private final LearningUnitRepository learningUnitRepository;

    public LearningUnitViewModel(@NonNull LearningUnitRepository learningUnitRepository) {
        this.learningUnitRepository = learningUnitRepository;
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

    public void edit(String title, String description, String workingHours, Integer learningUnitId
    ) {
        Thread thread = new Thread(() -> {
            Result<Void> result =
                    learningUnitRepository.edit(title, description, workingHours, learningUnitId);
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
}
