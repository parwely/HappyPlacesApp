# **Happy Places App** - Verwalten Sie Ihre Lieblingsorte digital! 🌍✨

- **Bearbeiten**: Ort in Liste auswählen → Edit-Button (Stift-Icon)
- **Löschen**: 
  - Swipe nach links/rechts in der Liste
  - Ort auswählen → Delete-Button (Papierkorb-Icon)
- **Anzeigen**: Marker auf der Karte antippen

### Kategorien
Wählen Sie aus 7 verschiedenen Kategorien:
- **Allgemein** - Standard-Kategorie
- **Natur** - Parks, Wanderwege, Aussichtspunkte
- 🍽**Essen** - Restaurants, Cafés, Bars
- **Entspannung** - Spas, ruhige Orte
- **Unterhaltung** - Kinos, Theater, Events
- **Sport** - Fitnessstudios, Sportplätze
- **Kultur** - Museen, Denkmäler, Sehenswürdigkeiten



## App-Struktur

```
app/src/main/java/com/example/happyplacesapp/
├── data/
│   ├── database/
│   │   ├── HappyPlace.kt          # Entity-Klasse
│   │   ├── HappyPlaceDao.kt       # Data Access Object
│   │   ├── HappyPlaceDatabase.kt  # Room Database
│   │   └── Converters.kt          # Type Converters
│   └── repository/
│       └── HappyPlaceRepository.kt # Repository Pattern
├── ui/
│   ├── fragments/
│   │   ├── MapFragment.kt         # Karten-Ansicht
│   │   ├── PlacesListFragment.kt  # Listen-Ansicht
│   │   └── AddPlaceFragment.kt    # Formular
│   ├── adapters/
│   │   └── PlacesAdapter.kt       # RecyclerView Adapter
│   └── viewmodels/
│       ├── HappyPlaceViewModel.kt # ViewModel
│       └── HappyPlaceViewModelFactory.kt
└── MainActivity.kt                # Haupt-Activity
```

## Konfiguration

### OSMDroid Setup
```xml
<!-- Fügen Sie in AndroidManifest.xml hinzu -->
<uses-permission android:name="android.permission.INTERNET" />
<uses-permission android:name="android.permission.ACCESS_NETWORK_STATE" />
<uses-permission android:name="android.permission.WRITE_EXTERNAL_STORAGE" />
```
- **Foto-Integration**: Fügen Sie Bilder aus der Galerie oder Kamera hinzu
### FileProvider Setup
```xml
<!-- FileProvider für Kamera-Bilder -->
<provider
    android:name="androidx.core.content.FileProvider"
    android:authorities="${applicationId}.provider"
    android:exported="false"
    android:grantUriPermissions="true">
    <meta-data
        android:name="android.support.FILE_PROVIDER_PATHS"
        android:resource="@xml/file_paths" />
</provider>
```

### GPS-Probleme
- **Problem**: Standort wird nicht erkannt
- **Lösung**: Überprüfen Sie GPS-Einstellungen und App-Berechtigungen

### Kamera-Probleme
- **Problem**: Kamera öffnet sich nicht
- **Lösung**: Kamera-Berechtigung in App-Einstellungen aktivieren

### Performance
- **Problem**: Langsame Karten-Performance
- **Lösung**: Tile-Cache wird automatisch verwaltet

### Abhängigkeiten
```gradle
• androidx.lifecycle:lifecycle-viewmodel-ktx:2.7.0
• org.osmdroid:osmdroid-android:6.1.18
• com.google.android.gms:play-services-location:21.0.1
• com.github.bumptech.glide:glide:4.16.0
• com.google.android.material:material:1.11.0
```

## Installation & Setup

### Voraussetzungen
- Android Studio Hedgehog oder neuer
- Android SDK API Level 26+ (Android 8.0)
- Kotlin 1.8+
- Gradle 8.0+

### Projekt einrichten
```bash
1. Repository klonen:
   git clone [repository-url]

2. Projekt in Android Studio öffnen:
   File → Open → Projektordner auswählen

3. Gradle Sync ausführen:
   Gradle sync wird automatisch gestartet

4. App auf Gerät/Emulator ausführen:
   Run → Run 'app'
```

### Berechtigungen
Die App benötigt folgende Berechtigungen:
```
• ACCESS_FINE_LOCATION - Präzise Standortbestimmung
• ACCESS_COARSE_LOCATION - Grobe Standortbestimmung
• CAMERA - Foto-Aufnahme
• READ_EXTERNAL_STORAGE - Galerie-Zugriff
• WRITE_EXTERNAL_STORAGE - Bilderspeicherung (API < 29)
• INTERNET - Karten-Tiles laden
```

## Fehlerbehebung 

**Karte wird nicht angezeigt:**
- Internetverbindung prüfen
- App-Berechtigungen kontrollieren
- Neustart der App

**Standort funktioniert nicht:**
- GPS aktivieren
- App-Berechtigungen für Standort gewähren
- In den Geräteeinstellungen Standortdienste aktivieren

**Kamera funktioniert nicht:**
- Kamera-Berechtigung gewähren
- Speicherplatz prüfen
- Kamera-App schließen falls geöffnet

## Lizenz 📄

Dieses Projekt ist unter der MIT-Lizenz lizenziert. Siehe [LICENSE](LICENSE) für Details.


