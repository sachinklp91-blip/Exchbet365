package com.example.model

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Sports
import androidx.compose.material.icons.filled.SportsCricket
import androidx.compose.ui.graphics.vector.ImageVector

enum class WebTab(
    val title: String,
    val initialUrl: String,
    val icon: ImageVector,
    val testTag: String
) {
    EXCHBET(
        title = "ExchBet",
        initialUrl = "https://exchbet365.com",
        icon = Icons.Default.Sports,
        testTag = "tab_exchbet"
    ),
    CREX(
        title = "Crex",
        initialUrl = "https://www.crex.com",
        icon = Icons.Default.SportsCricket,
        testTag = "tab_crex"
    )
}
