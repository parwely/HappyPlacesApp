package com.example.happyplacesapp.models

enum class PlaceCategory(val displayName: String) {
    RESTAURANT("Restaurant"),
    TOURIST_ATTRACTION("Touristenattraktion"),
    NATURE("Natur"),
    CULTURE("Kultur"),
    SHOPPING("Shopping"),
    ENTERTAINMENT("Unterhaltung"),
    SPORT("Sport"),
    OTHER("Sonstiges");

    companion object {
        fun fromString(value: String): PlaceCategory {
            return values().find { it.displayName == value } ?: OTHER
        }
    }
}