# Happy Places App 🌟

Eine moderne Android-App zum Speichern und Verwalten Ihrer Lieblingsorte mit interaktiver Karte und Kamera-Funktionalität.

## Features ✨

### Kernfunktionen
- **Orte speichern**: Titel, Beschreibung, Standort und Fotos
- **Interaktive Karte**: OpenStreetMap-Integration mit Markern
- **Kamera-Integration**: Fotos direkt aufnehmen oder aus Galerie wählen
- **Kategorien**: Natur, Essen, Entspannung, Unterhaltung, Sport, Kultur
- **Notizen**: Persönliche Notizen zu jedem Ort
- **Standort-Services**: Aktueller Standort oder manuell auf Karte wählen

### Benutzeroberfläche
- **Zwei Hauptansichten**: Liste und Karte
- **Intuitive Navigation**: Bottom Navigation zwischen Ansichten
- **Material Design**: Moderne UI mit Material Components
- **Swipe-to-Delete**: Orte einfach durch Wischen löschen
- **FAB-Buttons**: Schneller Zugriff auf Hauptfunktionen

## Technologie Stack 🛠️

### Android
- **Kotlin** - Programmiersprache
- **Android Jetpack** - Architecture Components
- **Room Database** - Lokale Datenspeicherung
- **Navigation Component** - Fragment-Navigation
- **View Binding** - Type-safe View-Zugriff

### Karten & Standort
- **OpenStreetMap (OSMDroid)** - Kartenanzeige
- **Google Play Services Location** - Standortdienste
- **Geocoder** - Adressauflösung

### Kamera & Bilder
- **CameraX** - Kamera-Integration
- **Glide** - Bildverarbeitung und -anzeige
- **FileProvider** - Sichere Dateifreigabe

### UI/UX
- **Material Design Components** - UI-Komponenten
- **RecyclerView** - Effiziente Listen
- **ConstraintLayout** - Responsive Layouts

## Projektstruktur 📁

```
app/
├── src/main/java/com/example/happyplacesapp/
│   ├── data/
│   │   ├── database/
│   │   │   ├── HappyPlace.kt          # Entity-Klasse
│   │   │   ├── HappyPlaceDao.kt       # Database Access Object
│   │   │   ├── HappyPlaceDatabase.kt  # Room Database
│   │   │   └── TypeConverters.kt      # Datentyp-Konverter
│   │   └── repository/
│   │       └── HappyPlaceRepository.kt # Repository Pattern
│   ├── ui/
│   │   ├── adapters/
│   │   │   └── PlacesAdapter.kt       # RecyclerView Adapter
│   │   ├── fragments/
│   │   │   ├── AddPlaceFragment.kt    # Ort hinzufügen/bearbeiten
│   │   │   ├── MapFragment.kt         # Kartenansicht
│   │   │   └── PlacesListFragment.kt  # Listenansicht
│   │   └── viewmodels/
│   │       ├── HappyPlaceViewModel.kt # ViewModel
│   │       └── HappyPlaceViewModelFactory.kt
│   ├── utils/                         # Hilfsfunktionen
│   └── MainActivity.kt                # Haupt-Activity
└── src/main/res/
    ├── layout/                        # Layout-Dateien
    ├── navigation/                    # Navigation Graph
    ├── drawable/                      # Icons und Grafiken
    ├── values/                        # Strings, Colors, etc.
    └── xml/                          # File Provider Paths
```

## Installation & Setup 🚀

### Voraussetzungen
- Android Studio Arctic Fox oder neuer
- Android SDK 26 (API Level 26) oder höher
- Kotlin 1.8.0 oder neuer

### Installation
1. Repository klonen:
   ```bash
   git clone https://github.com/yourusername/HappyPlacesApp.git
   ```

2. Projekt in Android Studio öffnen

3. Gradle Sync durchführen

4. App auf Gerät oder Emulator ausführen

### Berechtigungen
Die App benötigt folgende Berechtigungen:
- **Kamera**: Für Fotoaufnahmen
- **Standort**: Für aktuelle Position
- **Speicher**: Für Bildspeicherung
- **Internet**: Für Kartendaten

## Verwendung 📱

### Neuen Ort hinzufügen
1. Auf **+**-Button tippen (in Liste oder auf Karte)
2. Foto aufnehmen oder aus Galerie wählen (optional)
3. Titel und Beschreibung eingeben
4. Kategorie auswählen
5. Standort automatisch setzen oder manuell wählen
6. Notizen hinzufügen (optional)
7. **Speichern** antippen

### Orte verwalten
- **Bearbeiten**: Ort in Liste auswählen → Bearbeiten-Button
- **Löschen**: Ort in Liste nach links/rechts wischen
- **Anzeigen**: Zwischen Listen- und Kartenansicht wechseln

### Karten-Features
- **Marker**: Alle gespeicherten Orte als Marker
- **Aktueller Standort**: Eigene Position anzeigen
- **Navigation**: Zu eigenem Standort navigieren
- **Hinzufügen**: Auf Karte tippen um neuen Ort hinzuzufügen

## Sicherheit & Datenschutz 🔒

- **Lokale Speicherung**: Alle Daten bleiben auf dem Gerät
- **Keine Cloud-Sync**: Keine Datenübertragung an externe Server
- **Sichere Bildverwaltung**: FileProvider für sichere Dateifreigabe
- **Berechtigungsanfragen**: Nur bei Bedarf angefordert

## Erweiterte Features 🎯

### Geplante Features
- **Suchfunktion**: Orte nach Name oder Kategorie suchen
- **Export/Import**: Daten sichern und wiederherstellen
- **Kategorien-Filter**: Orte nach Kategorien filtern
- **Navigation**: Integration mit externen Navigations-Apps
- **Sharing**: Orte mit anderen teilen

## Fehlerbehebung 🔧

### Häufige Probleme

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

## Beitragen 🤝

Beiträge sind willkommen! Bitte:
1. Fork das Repository
2. Feature Branch erstellen
3. Änderungen commiten
4. Pull Request erstellen

## Kontakt 📧

Bei Fragen oder Problemen erstellen Sie bitte ein Issue im Repository.

---

**Happy Places App** - Bewahren Sie Ihre schönsten Erinnerungen! 🌍✨
