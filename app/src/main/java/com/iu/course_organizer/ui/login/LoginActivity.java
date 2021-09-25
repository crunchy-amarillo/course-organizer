package com.iu.course_organizer.ui.login;

import android.app.Activity;
import android.app.ActivityOptions;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;

import androidx.annotation.StringRes;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;

import com.google.android.material.snackbar.Snackbar;
import com.iu.course_organizer.R;
import com.iu.course_organizer.common.utils.SharedPrefValues;
import com.iu.course_organizer.database.CourseOrganizerDatabase;
import com.iu.course_organizer.databinding.ActivityLoginBinding;
import com.iu.course_organizer.ui.course_list.CourseListActivity;

public class LoginActivity extends AppCompatActivity {

    private LoginViewModel loginViewModel;
    private ActivityLoginBinding binding;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityLoginBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        loginViewModel = new ViewModelProvider(this, new LoginViewModelFactory(CourseOrganizerDatabase.getInstance(this)))
                .get(LoginViewModel.class);


        checkIfUserIsLoggedIn();

        observeFormInputs();
        observeLoginResult();

        initFormInputChangeListener();
        initFormInputPasswordEnterListener();
        handleLoginButton();
    }

    private void checkIfUserIsLoggedIn()
    {
        SharedPreferences sharedPreferences = getSharedPreferences(SharedPrefValues.USER_DETAILS, MODE_PRIVATE);
        int userId = sharedPreferences.getInt(SharedPrefValues.USER_ID, -1);

        if (userId > -1) {
            switchToCourseList();
            finish();
        }
    }

    private void handleLoginButton()
    {
        binding.login.setOnClickListener(v -> {
            binding.loading.setVisibility(View.VISIBLE);
            loginViewModel.login(binding.username.getText().toString(),
                    binding.password.getText().toString());
        });
    }

    private void initFormInputChangeListener()
    {
        final EditText usernameEditText = binding.username;
        final EditText passwordEditText = binding.password;

        TextWatcher afterTextChangedListener = new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                // ignore
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                // ignore
            }

            @Override
            public void afterTextChanged(Editable s) {
                loginViewModel.loginDataChanged(usernameEditText.getText().toString(),
                        passwordEditText.getText().toString());
            }
        };
        usernameEditText.addTextChangedListener(afterTextChangedListener);
        passwordEditText.addTextChangedListener(afterTextChangedListener);

    }

    private void initFormInputPasswordEnterListener()
    {
        final EditText usernameEditText = binding.username;
        final EditText passwordEditText = binding.password;

        passwordEditText.setOnEditorActionListener((v, actionId, event) -> {
            if (actionId == EditorInfo.IME_ACTION_DONE) {
                loginViewModel.login(usernameEditText.getText().toString(),
                        passwordEditText.getText().toString());
            }
            return false;
        });

    }

    private void observeFormInputs()
    {
        loginViewModel.getLoginFormState().observe(this, loginFormState -> {
            if (loginFormState == null) {
                return;
            }
            binding.login.setEnabled(loginFormState.isDataValid());
            if (loginFormState.getUsernameError() != null) {
                binding.username.setError(getString(loginFormState.getUsernameError()));
            }
            if (loginFormState.getPasswordError() != null) {
                binding.password.setError(getString(loginFormState.getPasswordError()));
            }
        });
    }

    private void observeLoginResult()
    {
        loginViewModel.getLoginResult().observe(this, loginResult -> {
            if (loginResult == null) {
                return;
            }
            binding.loading.setVisibility(View.GONE);
            if (loginResult.getError() != null) {
                showLoginFailed(loginResult.getError());
            }
            if (loginResult.getSuccess() != null) {
                updateUiWithUser(loginResult.getSuccess());
                setResult(Activity.RESULT_OK);

                //Complete and destroy login activity once successful
                finish();
            }
        });
    }

    private void updateUiWithUser(LoggedInUserView loggedInUserView) {

        SharedPreferences sharedPreferences = getSharedPreferences(SharedPrefValues.USER_DETAILS, MODE_PRIVATE);
        sharedPreferences.edit()
                .clear()
                .putString(SharedPrefValues.USER_NAME, loggedInUserView.getDisplayName())
                .putInt(SharedPrefValues.USER_ID, loggedInUserView.getUserId())
                .apply();

        switchToCourseList();
    }

    private void switchToCourseList()
    {
        Intent intent = new Intent(this, CourseListActivity.class);
        startActivity(intent, ActivityOptions.makeSceneTransitionAnimation(this).toBundle());
    }

    private void showLoginFailed(@StringRes Integer errorString) {
        hideKeyboard();
        Snackbar.make(findViewById(R.id.login), errorString, Snackbar.LENGTH_LONG).show();
    }

    private void hideKeyboard() {
        InputMethodManager imm = (InputMethodManager) this.getSystemService(Activity.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(this.getCurrentFocus().getWindowToken(), 0);
    }
}