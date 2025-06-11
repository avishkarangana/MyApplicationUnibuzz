package lk.cmb.myapplication;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

public class MainActivity extends AppCompatActivity {

    LinearLayout navSports, navAcademic, navEvents;
    ImageView iconSports, iconAcademic, iconEvents;
    TextView labelSports, labelAcademic, labelEvents;

    // Top bar buttons
    ImageView btnDeveloperInfo, btnUserProfile;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Bottom navigation views
        navSports = findViewById(R.id.nav_sports);
        navAcademic = findViewById(R.id.nav_academic);
        navEvents = findViewById(R.id.nav_events);

        iconSports = findViewById(R.id.icon_sports);
        iconAcademic = findViewById(R.id.icon_academic);
        iconEvents = findViewById(R.id.icon_events);

        labelSports = findViewById(R.id.label_sports);
        labelAcademic = findViewById(R.id.label_academic);
        labelEvents = findViewById(R.id.label_events);

        // Top bar icon buttons
        btnDeveloperInfo = findViewById(R.id.btn_developer_info);
        btnUserProfile = findViewById(R.id.btn_user_profile);

        // Set initial fragment and highlight initial tab
        loadFragment(new SportsFragment());
        highlightSelected(navSports, iconSports, labelSports);

        // Bottom navigation click listeners
        navSports.setOnClickListener(v -> {
            loadFragment(new SportsFragment());
            highlightSelected(navSports, iconSports, labelSports);
        });

        navAcademic.setOnClickListener(v -> {
            loadFragment(new AcademicFragment());
            highlightSelected(navAcademic, iconAcademic, labelAcademic);
        });

        navEvents.setOnClickListener(v -> {
            loadFragment(new EventsFragment());
            highlightSelected(navEvents, iconEvents, labelEvents);
        });

        // Top bar Developer info icon click - opens DevInfoActivity
        btnDeveloperInfo.setOnClickListener(v -> {
            Intent intent = new Intent(MainActivity.this, DevInfoActivity.class);
            startActivity(intent);
        });

        // Top bar User profile icon click - opens UserProfileActivity
        btnUserProfile.setOnClickListener(v -> {
            Intent intent = new Intent(MainActivity.this, UserProfileActivity.class);
            startActivity(intent);
        });
    }

    // Load the fragment into the fragment container
    private void loadFragment(Fragment fragment) {
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.fragment_container, fragment)
                .commit();
    }

    // Highlight the selected bottom navigation tab
    private void highlightSelected(LinearLayout selectedNav, ImageView selectedIcon, TextView selectedLabel) {
        resetAllTabs();
        selectedNav.setBackgroundResource(R.drawable.bottom_right_yellow); // your yellow highlight drawable
        selectedIcon.setColorFilter(Color.BLACK);
        selectedLabel.setTextColor(Color.BLACK);
    }

    // Reset all tabs to default colors
    private void resetAllTabs() {
        navSports.setBackgroundColor(Color.TRANSPARENT);
        navAcademic.setBackgroundColor(Color.TRANSPARENT);
        navEvents.setBackgroundColor(Color.TRANSPARENT);

        iconSports.setColorFilter(Color.WHITE);
        iconAcademic.setColorFilter(Color.WHITE);
        iconEvents.setColorFilter(Color.WHITE);

        labelSports.setTextColor(Color.WHITE);
        labelAcademic.setTextColor(Color.WHITE);
        labelEvents.setTextColor(Color.WHITE);
    }
}
