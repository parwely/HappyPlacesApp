# HappyPlacesApp

HappyPlacesApp ist eine moderne Android-App, mit der du deine Lieblingsorte auf einer Karte speichern, verwalten und jederzeit wiederfinden kannst. Die App bietet eine intuitive Kartenansicht, eine übersichtliche Liste aller gespeicherten Orte und ein ansprechendes, klares Design.

## Features
- **Kartenansicht:** Setze per Tap Marker auf der Karte und speichere neue Orte mit Namen und Beschreibung.
- **Listenansicht:** Sieh alle gespeicherten Orte in einer sortierten Liste, bearbeite oder lösche sie.
- **Modernes UI:** Klare Farben, Material Design, intuitive Bedienung.
- **Google Maps Integration:** Zeigt alle Orte als Marker auf der Karte.

## Installation & Einrichtung
1. **Repository klonen:**
   ```
   git clone https://github.com/dein-github-user/HappyPlacesApp.git
   ```
2. **Google Maps API Key:**
   - Lege in deinem Projekt-Root eine Datei `local.properties` an (falls nicht vorhanden).
   - Füge folgende Zeile ein:
     ```
     MAPS_API_KEY=DEIN_API_KEY_HIER
     ```
   - Ersetze `DEIN_API_KEY_HIER` durch deinen eigenen Google Maps API Key.
   - [API Key erstellen & einschränken (Google Doku)](https://developers.google.com/maps/documentation/android-sdk/get-api-key)
3. **Projekt öffnen & bauen:**
   - Öffne das Projekt in Android Studio.
   - Führe "File > Sync Project with Gradle Files" aus.
   - Baue und starte die App auf deinem Gerät oder Emulator.

## Sicherheit & Open Source
- **Dein API Key bleibt privat:** Der Key steht nur in deiner `local.properties` und wird nicht ins Repository gepusht.
- **.gitignore schützt sensible Daten** (local.properties, Schlüsseldateien, Build-Ordner etc.).
- **Jeder Nutzer trägt seinen eigenen Key ein.**

## Lizenz
MIT

---

**Viel Spaß beim Speichern deiner Happy Places!**
