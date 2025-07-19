Kotlin Alarm App ⏰
A modern and reliable alarm application for Android, built natively with Kotlin. This app allows users to set alarms with custom audio files from their device, ensuring a personalized and robust user experience.

Features ✨
This app is built with a focus on core Android functionalities to deliver a seamless and intuitive alarm-setting process.

1. Intuitive Time Selection
The app features a user-friendly interface for setting the alarm time.

TimePicker Integration: Utilizes the native Android TimePicker in a "spinner" mode for quick and easy time selection.

Calendar API: The selected time is processed using the Calendar API to ensure the alarm is scheduled with precision for the correct date and time.

2. Custom Audio Selection
Users can personalize their alarms by choosing any sound from their device's local storage.

Storage Access Framework: Securely browses and selects audio files without requiring broad storage permissions.

URI Handling: The app correctly handles the content URI of the selected file, making it available for playback when the alarm fires.

3. Reliable Audio Playback
The app uses the MediaPlayer class to manage audio playback effectively.

MediaPlayer Implementation: When the alarm triggers, an instance of MediaPlayer is created to play the user's chosen audio file.

Looping Audio: The player is configured to loop the alarm sound, ensuring the user wakes up. The playback is managed within a Service to run reliably in the background.

Technologies Used 🛠️
Language: Kotlin

UI Components: TimePicker, MaterialCardView, MaterialButton

Core APIs: AlarmManager, MediaPlayer, Calendar, ContentResolver

Architecture: Activity, Service, BroadcastReceiver

How To Use
Clone the repository.

Open the project in Android Studio.

Build the project and run it on an Android emulator or a physical device.

Set a time, select an audio file, and click "Set Alarm"!

![WhatsApp Image 2025-07-19 at 12 38 12_093eb1ad](https://github.com/user-attachments/assets/30443f7a-ebfc-4ecc-ab31-391faad0637f)

![WhatsApp Image 2025-07-19 at 12 38 12_e93b045f](https://github.com/user-attachments/assets/7f5d118c-d742-4641-8942-8741d3cba063)
