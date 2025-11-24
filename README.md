<p align="center">
  <img src="https://github.com/user-attachments/assets/a018493a-1588-4e31-ab10-bd9650306750" alt="Zenvio Banner" width="100%">
</p>

<br>

# 🌿 Zenvio — AI Wellness, Meditation & Assistant App
Your all-in-one companion for mental clarity, healthy habits and peaceful sleep.

![Android](https://img.shields.io/badge/Android-Compose-brightgreen)
![Kotlin](https://img.shields.io/badge/Kotlin-1.9+-blueviolet)
![Architecture-MVI](https://img.shields.io/badge/Architecture-MVI-blue)
![License: MIT](https://img.shields.io/badge/License-MIT-yellow)
![Status](https://img.shields.io/badge/Status-In_Development-orange)

---

## ✨ Overview
Zenvio is a modern wellness application built with **Jetpack Compose**, **MVI**, and **Kotlin**, designed to help users take control of their mental and physical well-being.

It combines **AI-powered assistance**, **journaling**, **meditation**, **sleep tools**, **articles**, and **achievements** into one beautifully structured mobile experience.

This project also demonstrates real-world, scalable Android engineering techniques valued by European hiring teams.

---

## 🌟 Features

### 👩‍⚕️ Chat & Video Assistance
- Real-time chat with certified wellness assistants  
- Video calls *(in progress)*  
- Assistant directory with availability  
- Export chat history (PDF / TXT / JSON)  
- Notifications & presence tracking  

### 📅 Plans, Search & Achievements
- Personalized wellness goals  
- Global search (chats, journal, articles…)  
- Progress tracking & achievements  
- Easily share achievements  

### 🧘 Meditations & Breathing
- Guided meditation sessions  
- Breathing exercises with animations  
- Background audio  
- Favorites for quick access  

### 📝 Journal & Quick Notes
- Private reflection journal  
- Unlimited entries with timestamps  
- Minimalistic notepad for ideas  

### 😴 Sleep Tools
- Sleep sound library (rain, forest, white noise…)  
- Auto-off timer  
- Sleep meditations  
- Sleep session tracking *(planned)*  

### 📚 Articles & Wellness Tests
- Curated wellness articles  
- Interactive self-tests  
- Personalized recommendations *(future)*  

---

## 🛠 Tech Stack

| Layer | Technology |
|-------|------------|
| UI | Jetpack Compose |
| Architecture | MVI + Modular Structure |
| DI | Hilt |
| Async | Kotlin Coroutines + Flow |
| Networking | Retrofit / WebSocket *(WIP)* |
| Local Storage | Room *(upcoming)* |
| Media | Android Media APIs *(upcoming)* |
| Build | Kotlin DSL + Version Catalog |

---

## 🧩 Architecture
```
app/
├── MainActivity.kt
├── feature/
│   ├── auth/
│   ├── chat/
│   ├── meditation/
│   ├── sleep/
│   ├── journal/
│   ├── articles/
│   └── achievements/
└── common/
    ├── ui/
    ├── models/
    └── utils/
```

---

## 🎨 UI & UX Principles
- 100% Jetpack Compose  
- Smooth animations  
- Consistent typography  
- Reusable components  
- Light & dark mode  

---

## 🚀 Getting Started

### Clone the repository
```bash
git clone https://github.com/franjojosip/Zenvio.git
```

### Open project in Android Studio  
Android Studio 2024+ recommended

### Update package info  
- `applicationId` in `app/build.gradle.kts`  
- Edit `strings.xml` for the app name  
- Replace icons  

### Check dependencies
```bash
./gradlew dependencyUpdates
```

### Build & run 🎉

---

## 📈 Roadmap
- [ ] Chat & Video Calling  
- [ ] Chat Export  
- [ ] Global Search System  
- [ ] Meditation Engine  
- [ ] Article & Test Hub  
- [ ] Journal Rich Text  
- [ ] Sleep Tracking  
- [ ] Achievement Sharing  

---

## ❤️ About the Developer
Hi! I'm **Franjo**, an Android developer passionate about:

- Scalable architecture (MVI / MVVM / Clean Architecture)  
- Beautiful Compose UI  
- Wellness-focused apps  
- AI-driven experiences  
- Building modern Android applications  

---

## ⭐ Enjoying Zenvio?
If you like the project, consider giving it a **star** ⭐  
Stay mindful. Stay healthy. Stay inspired. 🌱
