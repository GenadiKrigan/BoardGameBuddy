package com.example.boardgamebuddt.fragments;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.boardgamebuddt.MainActivity;
import com.example.boardgamebuddt.R;
import com.google.android.material.textfield.TextInputEditText;

public class RegisterFragment extends Fragment {

    private TextInputEditText etEmail, etPassword, etConfirmPassword, etUsername;
    private TextView tvLoginRedirect;

    public RegisterFragment() {super(R.layout.fragment_register);}

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        Button btnCreateAccount;
        etEmail = view.findViewById(R.id.etRegisterEmailText);
        etPassword = view.findViewById(R.id.etRegisterPassword);
        etConfirmPassword = view.findViewById(R.id.etConfromPassword);
        etUsername = view.findViewById(R.id.etRegisterUsername);
        tvLoginRedirect = view.findViewById(R.id.tvLoginRedirect);
        btnCreateAccount = view.findViewById(R.id.btnCreateAccount);
        btnCreateAccount.setOnClickListener(v -> {
            String email = etEmail.getText().toString().trim();
            String password = etPassword.getText().toString().trim();
            String confirmPassword = etConfirmPassword.getText().toString().trim();
            String username = etUsername.getText().toString().trim();
            //Check for empty fields
            if (email.isEmpty() || password.isEmpty() || confirmPassword.isEmpty() || username.isEmpty()) {
                Toast.makeText(getContext(), "Please fill all fields", Toast.LENGTH_SHORT).show();
                return;
            }
            //Check if passwords match
            else if (!password.equals(confirmPassword)) {
                Toast.makeText(getContext(), "Passwords do not match", Toast.LENGTH_SHORT).show();
                return;
            }
            else {
                MainActivity mainActivity = (MainActivity) getActivity();
                if(mainActivity!=null){
                    mainActivity.register(email, password, username);
                }
            }
        });
        tvLoginRedirect.setOnClickListener(v -> {
            Navigation.findNavController(v).navigate(R.id.action_registerFragment_to_loginFragment);
        });
    }
}