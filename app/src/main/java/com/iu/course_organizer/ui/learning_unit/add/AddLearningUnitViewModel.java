package com.iu.course_organizer.ui.learning_unit.add;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.iu.course_organizer.R;
import com.iu.course_organizer.common.DefaultResult;
import com.iu.course_organizer.common.utils.StringUtils;
import com.iu.course_organizer.data.LearningUnitRepository;
import com.iu.course_organizer.data.Result;

public class AddLearningUnitViewModel extends ViewModel {
    private final MutableLiveData<AddLearningUnitFormState> formState = new MutableLiveData<>();
    private final MutableLiveData<DefaultResult<Boolean>> addResult = new MutableLiveData<>();
    private final LearningUnitRepository learningUnitRepository;

    public AddLearningUnitViewModel(@NonNull LearningUnitRepository learningUnitRepository) {
        this.learningUnitRepository = learningUnitRepository;
    }

    LiveData<AddLearningUnitFormState> getFormState() {
        return formState;
    }

    LiveData<DefaultResult<Boolean>> getResult() {
        return addResult;
    }

    public void add(String title, String description, String workingHours, Integer courseId) {
        Thread thread = new Thread(() -> {
            Result<Void> result =
                    learningUnitRepository.add(title, description, workingHours, courseId);
            if (result instanceof Result.Success) {
                addResult.postValue(new DefaultResult<>(true));
            } else {
                addResult.postValue(new DefaultResult<>(false));
            }
        });
        thread.start();
    }

    public void dataChanged(String title, String description, String workingHours) {
        if (!StringUtils.isNotEmpty(title)) {
            formState.setValue(new AddLearningUnitFormState(R.string.invalid_empty, null, null));
        } else if (!StringUtils.isNotEmpty(workingHours)) {
            formState.setValue(new AddLearningUnitFormState(null, null, R.string.invalid_empty));
        } else {
            formState.setValue(new AddLearningUnitFormState(true));
        }
    }
}
