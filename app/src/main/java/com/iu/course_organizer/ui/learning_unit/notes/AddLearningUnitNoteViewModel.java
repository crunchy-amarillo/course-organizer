package com.iu.course_organizer.ui.learning_unit.notes;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.iu.course_organizer.R;
import com.iu.course_organizer.common.DefaultResult;
import com.iu.course_organizer.common.utils.StringUtils;
import com.iu.course_organizer.data.LearningUnitNoteRepository;
import com.iu.course_organizer.data.Result;


public class AddLearningUnitNoteViewModel extends ViewModel {
    private final MutableLiveData<AddLearningUnitNoteFormState> formState = new MutableLiveData<>();
    private final MutableLiveData<DefaultResult<Boolean>> addResult = new MutableLiveData<>();
    private final LearningUnitNoteRepository noteRepository;

    public AddLearningUnitNoteViewModel(@NonNull LearningUnitNoteRepository noteRepository) {
        this.noteRepository = noteRepository;
    }

    LiveData<AddLearningUnitNoteFormState> getFormState() {
        return formState;
    }

    LiveData<DefaultResult<Boolean>> getResult() {
        return addResult;
    }

    public void add(String note, Integer learningUnitId) {
        Thread thread = new Thread(() -> {
            Result<Void> result = noteRepository.add(note, learningUnitId);
            if (result instanceof Result.Success) {
                addResult.postValue(new DefaultResult<>(true));
            } else {
                addResult.postValue(new DefaultResult<>(false));
            }
        });
        thread.start();
    }

    public void dataChanged(String note) {
        if (!StringUtils.isNotEmpty(note)) {
            formState.setValue(new AddLearningUnitNoteFormState(R.string.invalid_empty));
        } else {
            formState.setValue(new AddLearningUnitNoteFormState(true));
        }
    }
}
