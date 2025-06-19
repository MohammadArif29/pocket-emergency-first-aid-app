# Pocket - Emergency & First Aid App

A comprehensive Android application designed to provide emergency assistance and first aid guidance in critical situations.

## 🚨 Features

### 🆘 SOS Emergency System
- **Emergency SMS**: Send distress messages with GPS location to emergency contacts
- **Flashlight**: Built-in flashlight for emergency lighting
- **Emergency Siren**: Audio siren to attract attention during emergencies
- **Location Tracking**: Automatic GPS location sharing with emergency messages

### 🩹 First Aid Guide
- **Comprehensive Database**: Extensive collection of first aid procedures and emergency responses
- **Search Functionality**: Quick search through all first aid information
- **Category Filtering**: Organized by emergency types (CPR, burns, allergic reactions, etc.)
- **Step-by-Step Instructions**: Detailed guidance for various emergency situations
- **Offline Access**: All information stored locally for use without internet

### 📞 Emergency Contacts
- **Contact Management**: Store and manage emergency contact information
- **Quick Access**: Instant access to important contacts during emergencies
- **Database Storage**: Secure local storage of contact information

## 🛠 Technical Details

### Architecture
- **Platform**: Native Android (Java)
- **Minimum SDK**: Android API level 21 (Android 5.0)
- **Database**: SQLite with custom DatabaseHelper
- **UI Framework**: Material Design Components

### Key Components
- `MainActivity`: Main dashboard with navigation to all features
- `FirstAidActivity`: First aid guide with search and filtering
- `SOSActivity`: Emergency features (SMS, flashlight, siren)
- `EmergencyContactsActivity`: Contact management system
- `DatabaseHelper`: SQLite database management
- `EmergencyAdapter`: RecyclerView adapter for first aid items

### Permissions Required
- `ACCESS_FINE_LOCATION`: For GPS location in emergency messages
- `SEND_SMS`: For sending emergency SMS messages
- `CAMERA`: For flashlight functionality
- `WRITE_EXTERNAL_STORAGE`: For database storage

## 📱 Screenshots

The app features a clean, intuitive interface with:

- Material Design cards for main menu items
- ![Screenshot 2025-04-18 155103](https://github.com/user-attachments/assets/ef25d7ac-6308-4e22-886a-ea2a41c0c1c9)
- Search bars with real-time filtering
- ![Screenshot 2025-04-18 155124](https://github.com/user-attachments/assets/2d7d2ed0-322a-453e-9b2e-9aecfa5d3549)
- Category chips for easy navigation
- ![Screenshot 2025-04-18 155208](https://github.com/user-attachments/assets/df987e00-7b02-4335-8e65-0f9cf92fbe91)
- Emergency buttons with visual feedback
- ![Screenshot 2025-04-18 155230](https://github.com/user-attachments/assets/cb61e711-f424-4c27-b45f-b4c24aa3eeba)
- ![Screenshot 2025-04-18 155255](https://github.com/user-attachments/assets/8fb7be1d-5eaf-4ba2-87b7-1fd50b31a124)
- Responsive layouts for different screen sizes

## 🚀 Getting Started

### Prerequisites
- Android Studio Arctic Fox or later
- Android SDK API level 21 or higher
- Android device or emulator running Android 5.0+

### Installation
1. Clone the repository:
   ```bash
   git clone [repository-url]
   ```

2. Open the project in Android Studio

3. Sync Gradle files and install dependencies

4. Build and run the application on your device or emulator

### Configuration
1. **Emergency Contacts**: Add your emergency contacts through the app
2. **Permissions**: Grant necessary permissions when prompted
3. **Emergency Number**: Update the emergency phone number in `SOSActivity.java` (currently set to "1234567890")

## 🔧 Customization

### Adding New First Aid Procedures
1. Update the database schema in `DatabaseHelper.java`
2. Add new entries to the first aid database
3. Ensure proper categorization for filtering

### Modifying Emergency Features
- **SMS Template**: Edit the message format in `SOSActivity.java`
- **Siren Sound**: Replace `siren.mp3` in the `raw` folder
- **UI Colors**: Modify colors in `colors.xml`

## 📁 Project Structure

```
app/
├── src/main/
│   ├── java/com/example/myapplication/
│   │   ├── MainActivity.java              # Main dashboard
│   │   ├── FirstAidActivity.java          # First aid guide
│   │   ├── SOSActivity.java               # Emergency features
│   │   ├── EmergencyContactsActivity.java # Contact management
│   │   ├── DatabaseHelper.java            # Database operations
│   │   └── [Adapter classes]              # UI adapters
│   ├── res/
│   │   ├── layout/                        # UI layouts
│   │   ├── drawable/                      # Icons and graphics
│   │   ├── values/                        # Colors, strings, themes
│   │   └── raw/                           # Audio files
│   └── AndroidManifest.xml                # App configuration
```

## 🛡 Safety Features

- **Error Handling**: Comprehensive error handling throughout the app
- **Permission Management**: Proper permission requests and handling
- **Offline Functionality**: Works without internet connection
- **Battery Optimization**: Efficient resource usage
- **Data Privacy**: All data stored locally on device

## 🤝 Contributing

1. Fork the repository
2. Create a feature branch (`git checkout -b feature/AmazingFeature`)
3. Commit your changes (`git commit -m 'Add some AmazingFeature'`)
4. Push to the branch (`git push origin feature/AmazingFeature`)
5. Open a Pull Request

## 📄 License

This project is licensed under the MIT License - see the [LICENSE](LICENSE) file for details.

## ⚠️ Disclaimer

This application is designed to provide general first aid information and emergency assistance. It is not a substitute for professional medical care. In case of serious emergencies, always contact emergency services immediately.

## 🆘 Emergency Information

**Important**: This app is designed to assist in emergency situations but should not replace professional medical care. Always:
- Call emergency services (911 in the US) for serious emergencies
- Follow professional medical advice
- Use this app as a supplementary tool, not a replacement for professional care

## 📞 Support

For support or questions about the application, please open an issue in the repository or contact the development team.

---

**Version**: 1.0.0  
**Last Updated**: December 2024  
**Platform**: Android  
**Language**: Java 
