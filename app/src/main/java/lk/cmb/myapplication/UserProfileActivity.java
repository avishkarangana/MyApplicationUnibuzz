package lk.cmb.myapplication;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
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

public class UserProfileActivity extends AppCompatActivity {

    Button signOutButton, editInfoButton;
    ImageView backArrow;
    TextView usernameText, emailText;

    private FirebaseAuth mAuth;
    private FirebaseFirestore db;
    private FirebaseUser currentUser;

    private String currentUsername = "";
    private String currentEmail = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_profile);

        signOutButton = findViewById(R.id.signOutButton);
        editInfoButton = findViewById(R.id.editInfoButton);
        backArrow = findViewById(R.id.backArrow);
        usernameText = findViewById(R.id.usernameText);
        emailText = findViewById(R.id.emailText);

        mAuth = FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance();
        currentUser = mAuth.getCurrentUser();

        if (currentUser != null) {
            db.collection("users").document(currentUser.getUid()).get()
                    .addOnSuccessListener(snapshot -> {
                        if (snapshot.exists()) {
                            currentUsername = snapshot.getString("username");
                            currentEmail = snapshot.getString("email");

                            usernameText.setText("User name: " + currentUsername);
                            emailText.setText("Email: " + currentEmail);
                        }
                    })
                    .addOnFailureListener(e -> {
                        Toast.makeText(this, "Failed to load profile info.", Toast.LENGTH_SHORT).show();
                    });
        }

        backArrow.setOnClickListener(v -> {
            startActivity(new Intent(this, MainActivity.class));
            finish();
        });

        signOutButton.setOnClickListener(v -> showSignOutDialog());
        editInfoButton.setOnClickListener(v -> showEditInfoDialog());
    }

    private void showSignOutDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        View dialogView = LayoutInflater.from(this).inflate(R.layout.dialog_signout, null);
        builder.setView(dialogView);
        AlertDialog dialog = builder.create();
        dialog.show();

        dialogView.findViewById(R.id.btnOk).setOnClickListener(v -> {
            mAuth.signOut();
            startActivity(new Intent(UserProfileActivity.this, LoginActivity.class));
            finish();
        });

        dialogView.findViewById(R.id.btnCancel).setOnClickListener(v -> dialog.dismiss());
    }

    private void showEditInfoDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        View dialogView = LayoutInflater.from(this).inflate(R.layout.dialog_edit_info, null);
        builder.setView(dialogView);

        EditText editUsername = dialogView.findViewById(R.id.editUsername);
        EditText editEmail = dialogView.findViewById(R.id.editEmail);
        Button btnOk = dialogView.findViewById(R.id.btnOk);
        Button btnCancel = dialogView.findViewById(R.id.btnCancel);

        editUsername.setText(currentUsername);
        editEmail.setText(currentEmail);

        AlertDialog dialog = builder.create();
        dialog.show();

        btnOk.setOnClickListener(v -> {
            String newUsername = editUsername.getText().toString().trim();
            String newEmail = editEmail.getText().toString().trim();

            if (!newUsername.isEmpty() && !newEmail.isEmpty()) {
                // Update Firebase Authentication email first
                currentUser.updateEmail(newEmail)
                        .addOnSuccessListener(aVoid -> {
                            // Update Firestore document after email update success
                            HashMap<String, Object> updates = new HashMap<>();
                            updates.put("username", newUsername);
                            updates.put("email", newEmail);

                            db.collection("users").document(currentUser.getUid()).update(updates)
                                    .addOnSuccessListener(aVoid2 -> {
                                        usernameText.setText("User name: " + newUsername);
                                        emailText.setText("Email: " + newEmail);
                                        currentUsername = newUsername;
                                        currentEmail = newEmail;
                                        Toast.makeText(this, "Updated successfully", Toast.LENGTH_SHORT).show();
                                        dialog.dismiss();
                                    })
                                    .addOnFailureListener(e -> {
                                        Toast.makeText(this, "Failed to update profile info. Try again.", Toast.LENGTH_SHORT).show();
                                    });
                        })
                        .addOnFailureListener(e -> {
                            Toast.makeText(this, "Failed to update email: " + e.getMessage(), Toast.LENGTH_LONG).show();
                        });
            } else {
                Toast.makeText(this, "Please fill in all fields", Toast.LENGTH_SHORT).show();
            }
        });

        btnCancel.setOnClickListener(v -> dialog.dismiss());
    }
}
