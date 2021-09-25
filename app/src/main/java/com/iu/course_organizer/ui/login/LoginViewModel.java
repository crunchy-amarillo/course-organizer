package com.iu.course_organizer.ui.login;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.iu.course_organizer.R;
import com.iu.course_organizer.common.utils.StringUtils;
import com.iu.course_organizer.data.LoginRepository;
import com.iu.course_organizer.data.Result;
import com.iu.course_organizer.data.model.LoggedInUser;

public class LoginViewModel extends ViewModel {

    private MutableLiveData<LoginFormState> loginFormState = new MutableLiveData<>();
    private MutableLiveData<LoginResult> loginResult = new MutableLiveData<>();
    private LoginRepository loginRepository;

    LoginViewModel(LoginRepository loginRepository) {
        this.loginRepository = loginRepository;
    }

    LiveData<LoginFormState> getLoginFormState() {
        return loginFormState;
    }

    LiveData<LoginResult> getLoginResult() {
        return loginResult;
    }

    public void login(String username, String password) {
        Thread thread = new Thread(() -> {
            Result<LoggedInUser> result = loginRepository.login(username, password);

            if (result instanceof Result.Success) {
                LoggedInUser data = ((Result.Success<LoggedInUser>) result).getData();
                loginResult.postValue(new LoginResult(new LoggedInUserView(data.getUserId(), data.getDisplayName())));
            } else {
                loginResult.postValue(new LoginResult(R.string.login_failed));
            }
        });
        thread.start();
    }

    public void loginDataChanged(String username, String password) {
        if (!StringUtils.isNotEmpty(username)) {
            loginFormState.setValue(new LoginFormState(R.string.invalid_username, null));
        } else if (!isPasswordValid(password)) {
            loginFormState.setValue(new LoginFormState(null, R.string.invalid_password));
        } else {
            loginFormState.setValue(new LoginFormState(true));
        }
    }

    // A placeholder password validation check
    private boolean isPasswordValid(String password) {
        return password != null && !password.isEmpty();
    }
}