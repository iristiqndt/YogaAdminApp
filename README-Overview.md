# Yoga Management System

A comprehensive yoga class management system consisting of two applications: an Android admin app and a cross-platform customer app.

## 📱 Applications Overview

### 🔧 **YogaAdminApp** (Android)
**Repository**: https://github.com/iristiqndt/YogaAdminApp

An Android application for yoga studio administrators to manage classes, instructors, and schedules.

**Features:**
- Create and manage yoga courses
- Schedule class instances
- View course details and participants
- Firebase integration for real-time data sync
- Material Design UI

**Technologies:**
- Android (Java/Kotlin)
- Firebase Realtime Database
- SQLite for local storage
- Material Design Components

### 📱 **YogaCustomerApp** (.NET MAUI)
**Repository**: https://github.com/iristiqndt/YogaCustomerApp

A cross-platform mobile application for yoga customers to browse and book classes.

**Features:**
- Browse available yoga classes
- Book class sessions
- View class schedules
- User profile management
- Cross-platform compatibility (iOS, Android, Windows)

**Technologies:**
- .NET MAUI
- C# & XAML
- Firebase integration
- Cross-platform UI framework

## 🚀 Getting Started

### Prerequisites
- **For YogaAdminApp:**
  - Android Studio
  - Android SDK (API 21+)
  - Firebase account

- **For YogaCustomerApp:**
  - Visual Studio 2022 with .NET MAUI workload
  - .NET 6.0 or later
  - Firebase account

### Installation & Setup

1. **Clone the repositories:**
   ```bash
   git clone https://github.com/iristiqndt/YogaAdminApp.git
   git clone https://github.com/iristiqndt/YogaCustomerApp.git
   ```

2. **Firebase Configuration:**
   - Both apps require Firebase setup
   - See `FIREBASE_SETUP.md` in YogaCustomerApp for detailed instructions
   - Configure your Firebase project with authentication and database

3. **Build and Run:**
   - **Android App**: Open in Android Studio → Sync → Run
   - **MAUI App**: Open in Visual Studio → Restore packages → Select platform → Run

## 🔥 Firebase Setup

Both applications use Firebase for:
- Real-time database
- User authentication
- Cloud storage
- Push notifications

**Important**: Firebase credentials are required for full functionality. See individual repository documentation for setup instructions.

## 📁 Project Structure

```
Yoga Management System/
├── YogaAdminApp/          # Android admin application
│   ├── app/src/main/      # Main application code
│   ├── gradle/            # Gradle configuration
│   └── README.md          # Android app documentation
│
└── YogaCustomerApp/       # .NET MAUI customer application
    ├── YogaCustomerApp/   # Main MAUI project
    ├── Platforms/         # Platform-specific code
    ├── Resources/         # App resources
    ├── FIREBASE_SETUP.md  # Firebase setup guide
    └── README.md          # MAUI app documentation
```

## 🛠️ Development

### Architecture
- **Admin App**: MVVM pattern with Repository pattern
- **Customer App**: MVVM with .NET MAUI Shell navigation

### Database Schema
- **Courses**: Course information (name, type, price, capacity)
- **Instances**: Scheduled class sessions
- **Users**: Customer and admin user data
- **Bookings**: Class booking records

## 📝 Features Comparison

| Feature | Admin App | Customer App |
|---------|-----------|--------------|
| Course Management | ✅ Create/Edit/Delete | ❌ View Only |
| Class Scheduling | ✅ Full Control | ❌ View Only |
| Booking Management | ✅ View All Bookings | ✅ Personal Bookings |
| User Management | ✅ Admin Features | ✅ Profile Management |
| Real-time Updates | ✅ Firebase Sync | ✅ Firebase Sync |
| Offline Support | ✅ SQLite Cache | ✅ Local Storage |

## 🔐 Security & Privacy

- Firebase security rules implemented
- User authentication required
- Admin role-based access control
- Sensitive data encryption

## 📄 License

This project is for educational purposes as part of COMP1786 coursework.

## 👨‍💻 Developer

**iristiqndt**
- GitHub: https://github.com/iristiqndt
- Admin App: https://github.com/iristiqndt/YogaAdminApp
- Customer App: https://github.com/iristiqndt/YogaCustomerApp

## 📞 Support

For issues or questions:
1. Check the individual repository documentation
2. Review Firebase setup instructions
3. Create an issue in the respective repository

---

**Note**: This is a student project developed for mobile application development coursework. Both applications demonstrate modern mobile development practices and cross-platform compatibility.
