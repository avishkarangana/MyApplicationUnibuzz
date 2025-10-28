Unibuzz – Android News App

Unibuzz is a modern Android news application built using Java and Firebase.
It provides university and general news updates through a clean, user-friendly, and visually appealing interface designed with Material 3 principles.

Features

• Dynamic News Feed – Displays live news articles with images and descriptions.
• User Authentication – Login and sign-up using Firebase Authentication.
• User Info Management – Edit, view, or sign out easily with stylish UI dialogs.
• Notification Ready – Can integrate Firebase Cloud Messaging for alerts.
• Attractive UI Design – Gradient backgrounds, rounded corners, and smooth layouts.
• Fast & Lightweight – Optimized RecyclerViews and efficient Firebase calls.

Tech Stack

Language: Java
Backend: Firebase (Authentication, Firestore / Realtime Database, Storage)
Design Tool: Figma / Material Design 3
IDE: Android Studio
Libraries: RecyclerView, Glide, Firebase UI, Lottie Animations

UI Design Overview

Splash Screen – Centered app logo with yellow gradient background (#FBFF16 → #FFFB00)
Sign-In Screen – Login form with email, password fields, and blue button (#2210E2)
Sign-Up Screen – Card-based layout with rounded corners and yellow accent design
News Screen – Scrollable list of news cards with title, date, and description
Developer Info Screen – Displays profile image and app info with blue background
User Info Screen – Editable user details and sign-out dialog with theme-colored buttons

Project Structure

Unibuzz/
├── app/
│ ├── java/com/unibuzz/
│ │ ├── activities/ → Splash, Login, Signup, Main, User Info
│ │ ├── adapters/ → RecyclerView Adapters
│ │ ├── models/ → News and User Models
│ │ └── utils/ → Helper Classes
│ ├── res/
│ │ ├── layout/ → XML Layout Files
│ │ ├── drawable/ → Icons, Shapes, Backgrounds
│ │ └── values/ → Colors, Strings, Dimensions
│ └── google-services.json
├── build.gradle
└── README.txt

Firebase Setup
https://console.firebase.google.com/u/0/project/unibuzz-application/overview

Author
Developer: K.G.A.Rangana
Email: gamageavishka27@gmail.com

LinkedIn/Portfolio:

License
This project is licensed under the MIT License.


