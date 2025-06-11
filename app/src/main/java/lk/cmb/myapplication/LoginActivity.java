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
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

public class LoginActivity extends AppCompatActivity {

    private FirebaseAuth mAuth;
    private FirebaseFirestore db;
    private boolean passwordVisible = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        EditText usernameField = findViewById(R.id.username); // Username input
        EditText passwordField = findViewById(R.id.password); // Password input
        Button signInButton = findViewById(R.id.btn_signin);   // Login button
        ImageView showPassword = findViewById(R.id.show_password); // Eye icon
        TextView signUpLink = findViewById(R.id.sign_up_link);     // SignUp link

        mAuth = FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance();

        signInButton.setOnClickListener(v -> {
            String username = usernameField.getText().toString().trim();
            String password = passwordField.getText().toString().trim();

            if (username.isEmpty() || password.isEmpty()) {
                Toast.makeText(this, "Please enter username and password", Toast.LENGTH_SHORT).show();
                return;
            }

            // 🔍 Query Firestore where username equals input
            db.collection("users").whereEqualTo("username", username).get()
                    .addOnSuccessListener(querySnapshot -> {
                        if (!querySnapshot.isEmpty()) {
                            DocumentSnapshot userDoc = querySnapshot.getDocuments().get(0);
                            String email = userDoc.getString("email");

                            if (email != null) {
                                // ✅ Try login with found email and password
                                mAuth.signInWithEmailAndPassword(email, password)
                                        .addOnCompleteListener(task -> {
                                            if (task.isSuccessful()) {
                                                FirebaseUser user = mAuth.getCurrentUser();
                                                if (user != null) {
                                                    Toast.makeText(this, "Login successful!", Toast.LENGTH_SHORT).show();
                                                    startActivity(new Intent(this, MainActivity.class));
                                                    finish();
                                                }
                                            } else {
                                                showAlert("Login Failed", task.getException() != null
                                                        ? task.getException().getMessage()
                                                        : "Unknown error.");
                                            }
                                        });
                            } else {
                                showAlert("Login Failed", "Email not found for this username.");
                            }
                        } else {
                            showAlert("Login Failed", "Username not found.");
                        }
                    })
                    .addOnFailureListener(e -> showAlert("Error", "Failed to fetch user data. Try again."));
        });

        // 👁️ Toggle password visibility
        showPassword.setOnClickListener(v -> {
            passwordVisible = !passwordVisible;
            passwordField.setInputType(passwordVisible
                    ? InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD
                    : InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
            passwordField.setSelection(passwordField.getText().length());
            showPassword.setImageResource(passwordVisible ? R.drawable.ic_visibility : R.drawable.visibility_off);
        });

        // 🔗 Go to SignUp page
        signUpLink.setOnClickListener(v -> {
            startActivity(new Intent(LoginActivity.this, SignUpActivity.class));
            finish();
        });
    }

    // 🔔 Show alert dialog
    private void showAlert(String title, String message) {
        new AlertDialog.Builder(LoginActivity.this)
                .setTitle(title)
                .setMessage(message)
                .setPositiveButton("OK", null)
                .show();
    }
}
