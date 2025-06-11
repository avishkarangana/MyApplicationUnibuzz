package lk.cmb.myapplication;

import android.content.Intent;
import android.os.Bundle;
import android.text.InputType;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

public class SignUpActivity extends AppCompatActivity {

    private EditText usernameField, emailField, passwordField, confirmPasswordField;
    private Button signUpButton;
    private TextView signInLink;
    private ImageView showPassword, showConfirmPassword;
    private boolean passwordVisible = false;
    private boolean confirmPasswordVisible = false;

    private FirebaseAuth mAuth;
    private FirebaseFirestore db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

        usernameField = findViewById(R.id.username);
        emailField = findViewById(R.id.email);
        passwordField = findViewById(R.id.password);
        confirmPasswordField = findViewById(R.id.confirm_password);
        signUpButton = findViewById(R.id.sign_up_btn);
        signInLink = findViewById(R.id.sign_in_link);
        showPassword = findViewById(R.id.show_password);
        showConfirmPassword = findViewById(R.id.show_confirm_password);

        mAuth = FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance();

        // Sign up button logic
        signUpButton.setOnClickListener(v -> {
            String username = usernameField.getText().toString().trim();
            String email = emailField.getText().toString().trim();
            String password = passwordField.getText().toString().trim();
            String confirmPassword = confirmPasswordField.getText().toString().trim();

            if (username.isEmpty() || email.isEmpty() || password.isEmpty() || confirmPassword.isEmpty()) {
                Toast.makeText(this, "Please fill all fields", Toast.LENGTH_SHORT).show();
                return;
            }

            if (!password.equals(confirmPassword)) {
                showAlert("Password Mismatch", "Passwords do not match.");
                return;
            }

            mAuth.createUserWithEmailAndPassword(email, password)
                    .addOnCompleteListener(task -> {
                        if (task.isSuccessful()) {
                            FirebaseUser firebaseUser = mAuth.getCurrentUser();
                            if (firebaseUser != null) {
                                String userId = firebaseUser.getUid();

                                Map<String, Object> user = new HashMap<>();
                                user.put("username", username);
                                user.put("email", email);

                                db.collection("users").document(userId).set(user)
                                        .addOnSuccessListener(aVoid -> {
                                            Toast.makeText(this, "Sign up successful! Please log in.", Toast.LENGTH_SHORT).show();
                                            mAuth.signOut();
                                            startActivity(new Intent(this, LoginActivity.class));
                                            finish();
                                        })
                                        .addOnFailureListener(e -> showAlert("Error", "Failed to save user data."));
                            }
                        } else {
                            showAlert("Sign Up Failed", task.getException() != null
                                    ? task.getException().getMessage()
                                    : "Unknown error.");
                        }
                    });
        });

        // Show/Hide password logic
        showPassword.setOnClickListener(v -> {
            passwordVisible = !passwordVisible;
            passwordField.setInputType(passwordVisible
                    ? InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD
                    : InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
            passwordField.setSelection(passwordField.getText().length());
            showPassword.setImageResource(passwordVisible ? R.drawable.ic_visibility : R.drawable.visibility_off);
        });

        showConfirmPassword.setOnClickListener(v -> {
            confirmPasswordVisible = !confirmPasswordVisible;
            confirmPasswordField.setInputType(confirmPasswordVisible
                    ? InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD
                    : InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
            confirmPasswordField.setSelection(confirmPasswordField.getText().length());
            showConfirmPassword.setImageResource(confirmPasswordVisible ? R.drawable.ic_visibility : R.drawable.visibility_off);
        });

        // Navigate to login
        signInLink.setOnClickListener(v -> {
            startActivity(new Intent(SignUpActivity.this, LoginActivity.class));
            finish();
        });
    }

    private void showAlert(String title, String message) {
        new AlertDialog.Builder(SignUpActivity.this)
                .setTitle(title)
                .setMessage(message)
                .setPositiveButton("OK", null)
                .show();
    }
}
