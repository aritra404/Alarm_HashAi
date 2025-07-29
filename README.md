# Java Alarm App ⏰  
A modern and reliable alarm application for Android, built natively with **Kotlin**.  
This app allows users to set alarms with **custom audio files** from their device, ensuring a **personalized and robust experience**.

---

## ✨ Features

### **1. Intuitive Time Selection**  
- **TimePicker Integration** – Uses Android’s native `TimePicker` in **spinner mode** for quick and easy time selection.  
- **Calendar API** – Ensures precise scheduling of alarms using the `Calendar` class.

---

### **2. Custom Audio Selection**  
- **Personalized Alarm Sounds** – Choose any sound from the device’s local storage.  
- **Storage Access Framework** – Securely browse and select audio files without requiring unnecessary storage permissions.  
- **URI Handling** – Properly manages the content URI to ensure smooth playback when the alarm rings.

---

### **3. Reliable Audio Playback**  
- **MediaPlayer** – Plays the user’s chosen audio file when the alarm is triggered.  
- **Looping Audio** – The alarm sound loops until the user dismisses it.  
- **Background Service** – Ensures the alarm works reliably even if the app is closed.

---

## 🛠 Technologies Used
- **Language:** Java  
- **UI Components:** `TimePicker`, `MaterialCardView`, `MaterialButton`  
- **Core APIs:** `AlarmManager`, `MediaPlayer`, `Calendar`, `ContentResolver`  

---

## 🚀 How to Use
1. Clone this repository.  
2. Open the project in **Android Studio**.  
3. Build and run it on an **emulator or physical device**.  
4. Set a time, select an audio file, and tap **"Set Alarm"!**

---

## 📸 Screenshots
<p align="center">
  <img src="https://github.com/user-attachments/assets/30443f7a-ebfc-4ecc-ab31-391faad0637f" width="300" style="margin: 10px;">
  <img src="https://github.com/user-attachments/assets/7f5d118c-d742-4641-8942-8741d3cba063" width="300" style="margin: 10px;">
</p>
