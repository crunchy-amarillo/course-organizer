package com.iu.course_organizer.ui.learning_unit.list;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.iu.course_organizer.common.DefaultResult;
import com.iu.course_organizer.data.LearningUnitRepository;
import com.iu.course_organizer.data.Result;
import com.iu.course_organizer.database.model.LearningUnit;

import java.util.ArrayList;
import java.util.List;

public class LearningUnitListViewModel extends ViewModel {
    private final MutableLiveData<DefaultResult<List<LearningUnit>>> learningUnits =
            new MutableLiveData<>();
    private final MutableLiveData<DefaultResult<Boolean>> deleteResult = new MutableLiveData<>();
    private final MutableLiveData<DefaultResult<Boolean>> increaseMinutesResult =
            new MutableLiveData<>();
    private final LearningUnitRepository learningUnitRepository;

    public LearningUnitListViewModel(@NonNull LearningUnitRepository learningUnitRepository) {
        this.learningUnitRepository = learningUnitRepository;
    }

    public LiveData<DefaultResult<List<LearningUnit>>> getLearningUnits() {
        return learningUnits;
    }

    public MutableLiveData<DefaultResult<Boolean>> getDeleteResult() {
        return deleteResult;
    }

    public MutableLiveData<DefaultResult<Boolean>> getIncreaseMinutesResult() {
        return increaseMinutesResult;
    }

    public void findByCourseId(Integer courseId) {
        Thread thread = new Thread(() -> {
            Result<List<LearningUnit>> result = learningUnitRepository.findByCourseId(courseId);
            if (result instanceof Result.Success) {
                List<LearningUnit> data =
                        (List<LearningUnit>) ((Result.Success<?>) result).getData();
                learningUnits.postValue(new DefaultResult<>(data));
            } else {
                learningUnits.postValue(new DefaultResult<>(new ArrayList<>()));
            }
        });
        thread.start();
    }

    public void increaseTimeTracking(Integer learningUnitId, int minutes) {
        Thread thread = new Thread(() -> {
            Result<Void> result =
                    learningUnitRepository.increaseSpentMinutes(learningUnitId, minutes);
            if (result instanceof Result.Success) {
                increaseMinutesResult.postValue(new DefaultResult<>(true));
            } else {
                increaseMinutesResult.postValue(new DefaultResult<>(false));
            }
        });
        thread.start();
    }

    public void delete(Integer learningUnitId) {
        Thread thread = new Thread(() -> {
            Result<Void> result = learningUnitRepository.delete(learningUnitId);
            if (result instanceof Result.Success) {
                deleteResult.postValue(new DefaultResult<>(true));
            } else {
                deleteResult.postValue(new DefaultResult<>(false));
            }
        });
        thread.start();
    }
}
