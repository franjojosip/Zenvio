# Zenvio
Your all-in-one wellness companion — assistants, meditation, sleep, journaling, breathing, achievements & more.

## About
Zenvio is a modern mobile wellness application designed to help users enhance their mental and physical well-being through guided assistance, self-reflection tools, and direct access to health professionals.

With Zenvio, you can:
- Chat and video-call certified health assistants
- Export chat transcripts to save important advice
- Search through chats, notes, articles, and journals
- Complete personalized plans and earn achievements
- Explore meditations, breathing exercises, articles, and wellness tests
- Write thoughts in a journal or a quick notepad
- Play sleep sounds and track sleep sessions

The app is still under development, but it aims to become a complete wellness toolkit.

## 🎨 UI
<img width="1185" height="541" alt="ai_health_app" src="https://github.com/user-attachments/assets/47ecf27b-1c1a-4ddb-9079-b3aa639327ae" />

## Key Features

### Chat & Video Assistance
- Chat with qualified health assistants
- Video call support (in progress)
- View the list of available assistants
- Real-time messaging + notifications
- Export chat history (PDF / TXT / JSON)

### Plans, Search & Achievements
- Create wellness plans with goals and reminders
- Global search across the entire app
- Unlock achievements and milestones
- Share achievements with others

### Meditations & Breathing
- Guided meditations (calm, focus, sleep, stress relief)
- Breathing exercises with animations
- Background audio support
- Favorites for quick access

### Articles, Tests & Knowledge Hub
- Curated wellness articles
- Interactive mental health and personality tests
- Personalized suggestions based on results

### Journal & Notepad
- Private journal for reflection
- Unlimited entries with timestamps
- Notepad for quick ideas and reminders

### Sleep Mode & Sounds
- Relaxing sleep sound library (rain, forest, white noise, etc.)
- Sleep meditation tracks
- Auto-off timer
- Track sleep sessions

## Architecture
Zenvio uses:
- Kotlin  
- Jetpack and Navigation Compose  
- MVI
- Hilt  
- Coroutines + Flow  
- Room Database (todo)
- Retrofit / WebSocket (in progress)
- Media Playback APIs (todo)

Main modules:
- MainActivity  
- Common (models, utilities, shared UI)
- Auth  
- Chat  
- Meditation  
- Sleep  
- Journal  
- Articles  
- Achievements  

## Getting Started

1. Clone the repository
```bash
git clone https://github.com/franjojosip/Zenvio.git
```
2. Open the project in Android Studio (Arctic Fox or newer)
3. Rename the app package in app/src/main/AndroidManifest.xml and build.gradle.kts as needed
4. Update applicationId in app/build.gradle.kts
5. Replace app icons, change strings.xml for your app name
6. Run ./gradlew dependencyUpdates and verify dependencies are up to date
7. Build and run the app on an emulator or physical device

## 🔧 Status & Roadmap
### Milestone
1. Chat & video call core flow
2. Export chat transcripts
3. Search & text indexing
4. Meditation & guided sessions
5. Articles & Tests
6. Journal & Notepad
7. Sleep sounds & auto-off timer
8. Achievements & sharing

## 📚 Resources
- Official Jetpack Compose documentation  
- Hilt Dependency Injection  
- Android Media Playback Best Practices  
- Android Room Persistence

## 📝 License
Zenvio is released under the MIT License.
See the LICENSE file for more details.

❤️ Thank You
I appreciate your interest in Zenvio!
Your wellness journey starts here — stay calm, stay mindful, stay healthy.
