package com.example.niyamatmynotebook;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.biometric.BiometricManager;
import androidx.biometric.BiometricPrompt;
import androidx.core.content.ContextCompat;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.util.concurrent.Executor;

public class LoginActivity extends AppCompatActivity {
    private EditText emailEditText;
    private EditText passwordEditText;
    BiometricPrompt biometricPrompt;
    BiometricPrompt.PromptInfo promptInfo;
    LinearLayout mMainLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        mMainLayout = findViewById(R.id.main_layout);
        BiometricManager biometricManager = BiometricManager.from(this);
        switch (biometricManager.canAuthenticate()){
            case BiometricManager.BIOMETRIC_ERROR_NO_HARDWARE:
                Toast.makeText(getApplicationContext(), "Device Doesn't have Fingerprint", Toast.LENGTH_SHORT).show();
                break;
            case BiometricManager.BIOMETRIC_ERROR_HW_UNAVAILABLE:
                Toast.makeText(getApplicationContext(), "Device not working fingerprint", Toast.LENGTH_SHORT).show();
                break; // Add break statement
            case BiometricManager.BIOMETRIC_ERROR_NONE_ENROLLED:
                Toast.makeText(getApplicationContext(), "No Fingerprint", Toast.LENGTH_SHORT).show();
                break; // Add break statement
        }

        Executor executor = ContextCompat.getMainExecutor(this);

        biometricPrompt = new BiometricPrompt(LoginActivity.this, executor, new BiometricPrompt.AuthenticationCallback() {


            public void onAuthenticationError(CharSequence errString, int errorCode) {
                super.onAuthenticationError(errorCode, errString);
                // Handle authentication error if needed
                String errorMessage = "Authentication Error: " + errString;
                switch (errorCode) {
                    case BiometricPrompt.ERROR_LOCKOUT:
                        errorMessage = "Authentication failed due to too many attempts. Please try again later.";
                        break;
                    case BiometricPrompt.ERROR_CANCELED:
                        errorMessage = "Authentication was canceled by the system.";
                        break;
                    case BiometricPrompt.ERROR_HW_NOT_PRESENT:
                        errorMessage = "Biometric hardware is not available on this device.";
                        break;
                    case BiometricPrompt.ERROR_HW_UNAVAILABLE:
                        errorMessage = "Biometric hardware is currently unavailable.";
                        break;
                    case BiometricPrompt.ERROR_TIMEOUT:
                        errorMessage = "Authentication timed out. Please try again.";
                        break;
                    case BiometricPrompt.ERROR_NO_BIOMETRICS:
                        errorMessage = "No biometric features are enrolled on this device.";
                        break;
                    case BiometricPrompt.ERROR_USER_CANCELED:
                        // This is not necessarily an error; it can occur if the user cancels the authentication process
                        errorMessage = "Authentication was canceled by the user.";
                        break;
                    case BiometricPrompt.ERROR_VENDOR:
                        errorMessage = "A vendor-specific error occurred during authentication.";
                        // You may need to handle vendor-specific errors differently based on your application's requirements
                        break;
                    case BiometricPrompt.ERROR_NO_DEVICE_CREDENTIAL:
                        errorMessage = "No device credential is set up on this device.";
                        // This error may occur if the user hasn't set up a device PIN, pattern, or password
                        // You can prompt the user to set up a device credential for fallback authentication
                        break;
                    // You can add more cases to handle other error codes as needed
                    default:
                        // Handle unknown errors or provide a generic error message for unhandled error codes
                        errorMessage = "An unknown error occurred during authentication.";
                        break;
                }
                Toast.makeText(getApplicationContext(), errorMessage, Toast.LENGTH_SHORT).show();
            }


            @Override
            public void onAuthenticationSucceeded(@NonNull BiometricPrompt.AuthenticationResult result) {
                super.onAuthenticationSucceeded(result);
                Toast.makeText(getApplicationContext(), "Fingerprint Authentication Successful", Toast.LENGTH_SHORT).show();
                // Navigate to MainActivity or perform any other actions upon successful fingerprint authentication
                Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                startActivity(intent);
                finish(); // Finish LoginActivity so that the user cannot navigate back to it using the back button
            }
            @Override
            public void onAuthenticationFailed() {
                super.onAuthenticationFailed();
                // Handle authentication failure if needed
                Toast.makeText(getApplicationContext(), "Fingerprint Authentication Failed", Toast.LENGTH_SHORT).show();
                // You may choose to retry authentication or fall back to other authentication methods (e.g., password)
                showRetryOrFallbackDialog();
            }

            public void onAuthenticationError(int errorCode, @NonNull CharSequence errString) {
                super.onAuthenticationError(errorCode, errString);
                // Handle authentication error if needed
                String errorMessage = "Authentication Error: " + errString;
                switch (errorCode) {
                    case BiometricPrompt.ERROR_LOCKOUT:
                        errorMessage = "Authentication failed due to too many attempts. Please try again later.";
                        break;
                    case BiometricPrompt.ERROR_CANCELED:
                        errorMessage = "Authentication was canceled by the system.";
                        break;
                    case BiometricPrompt.ERROR_HW_NOT_PRESENT:
                        errorMessage = "Biometric hardware is not available on this device.";
                        break;
                    case BiometricPrompt.ERROR_HW_UNAVAILABLE:
                        errorMessage = "Biometric hardware is currently unavailable.";
                        break;
                    case BiometricPrompt.ERROR_TIMEOUT:
                        errorMessage = "Authentication timed out. Please try again.";
                        break;
                    case BiometricPrompt.ERROR_NO_BIOMETRICS:
                        errorMessage = "No biometric features are enrolled on this device.";
                        break;
                    case BiometricPrompt.ERROR_USER_CANCELED:
                        // This is not necessarily an error; it occurs if the user cancels the authentication process
                        errorMessage = "Authentication was canceled by the user.";
                        // Perform any necessary cleanup or handle the cancellation gracefully
                        // For example, dismiss any ongoing dialogs
                        dismissAuthenticationDialog();
                        break;
                    case BiometricPrompt.ERROR_VENDOR:
                    case BiometricPrompt.ERROR_NO_DEVICE_CREDENTIAL:
                        // These errors may require special handling based on your app's requirements
                        errorMessage = "Authentication failed due to a vendor-specific error.";
                        break;
                }
                Toast.makeText(getApplicationContext(), errorMessage, Toast.LENGTH_SHORT).show();
            }
            private void showRetryOrFallbackDialog() {
                AlertDialog.Builder builder = new AlertDialog.Builder(LoginActivity.this);
                builder.setTitle("Authentication Failed")
                        .setMessage("Please Set the Pattern / Pin")
                        .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                // Do something when the positive button is clicked
                            }
                        })
                        .setNegativeButton("Registration", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                // Handle switching to password login activity
                                Intent intent = new Intent(LoginActivity.this, RegistrationActivity.class);
                                startActivity(intent);
                                finish(); // Finish LoginActivity to prevent the user from navigating back to it using the back button
                            }
                        })
                        .setNeutralButton("Cancel", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();
                            }
                        })
                        .show();
            }

        });

        promptInfo = new BiometricPrompt.PromptInfo.Builder()
                .setTitle("Visit Project")
                .setDescription("Use Fingerprint to Login")
                .setDeviceCredentialAllowed(true)
                .build();
        biometricPrompt.authenticate(promptInfo);



        emailEditText = findViewById(R.id.login_email);
        passwordEditText = findViewById(R.id.login_password);

        Button loginButton = findViewById(R.id.login_btn);
        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                loginUser();
            }
        });

        TextView registerLink = findViewById(R.id.need_new_account_link);
        registerLink.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(LoginActivity.this, RegistrationActivity.class));
            }
        });
    }

    private void dismissAuthenticationDialog() {
        if (biometricPrompt != null) {
            biometricPrompt.cancelAuthentication();
        }
        Intent intent = new Intent(LoginActivity.this, RegistrationActivity.class);
        startActivity(intent);
    }

    private void loginUser() {
        String email = emailEditText.getText().toString().trim();
        String password = passwordEditText.getText().toString().trim();

        // Perform validation
        if (email.isEmpty() || !android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            emailEditText.setError("Enter a valid email address");
            emailEditText.requestFocus();
            return;
        }
        if (password.isEmpty()) {
            passwordEditText.setError("Enter your password");
            passwordEditText.requestFocus();
            return;
        }

        // Check email and password against your authentication mechanism
        // For simplicity, I'm just showing a toast message here
        Toast.makeText(this, "Login successful", Toast.LENGTH_SHORT).show();

        Intent intent = new Intent(LoginActivity.this, MainActivity.class);
        startActivity(intent);
        finish();
        // You can add further logic to handle the login process, such as navigating to another activity
    }
}
