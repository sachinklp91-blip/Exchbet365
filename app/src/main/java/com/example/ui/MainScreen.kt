package com.example.ui

import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.webkit.CookieManager
import android.webkit.WebStorage
import android.webkit.WebView
import android.widget.Toast
import androidx.activity.compose.BackHandler
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material.icons.filled.OpenInBrowser
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.zIndex
import com.example.model.WebTab
import com.example.ui.components.WebViewContainer

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MainScreen() {
    val context = LocalContext.current
    val activity = context as? Activity

    var currentTab by remember { mutableStateOf(WebTab.EXCHBET) }

    var exchWebView by remember { mutableStateOf<WebView?>(null) }
    var crexWebView by remember { mutableStateOf<WebView?>(null) }

    var showMenu by remember { mutableStateOf(false) }

    val activeWebView = if (currentTab == WebTab.EXCHBET) exchWebView else crexWebView

    // Back button navigates within WebView history first before switching tabs or exiting
    BackHandler(enabled = true) {
        if (activeWebView?.canGoBack() == true) {
            activeWebView.goBack()
        } else if (currentTab != WebTab.EXCHBET) {
            currentTab = WebTab.EXCHBET
        } else {
            activity?.finish()
        }
    }

    Scaffold(
        modifier = Modifier
            .fillMaxSize()
            .testTag("main_scaffold"),
        topBar = {
            TopAppBar(
                title = {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier.padding(end = 8.dp)
                    ) {
                        Text(
                            text = currentTab.title,
                            style = MaterialTheme.typography.titleMedium,
                            fontWeight = FontWeight.Bold,
                            color = MaterialTheme.colorScheme.onSurface
                        )

                        Spacer(modifier = Modifier.width(8.dp))

                        // Domain & SSL security badge
                        Surface(
                            shape = RoundedCornerShape(12.dp),
                            color = MaterialTheme.colorScheme.surfaceVariant,
                            modifier = Modifier.padding(horizontal = 4.dp)
                        ) {
                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                                modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
                            ) {
                                Icon(
                                    imageVector = Icons.Default.Lock,
                                    contentDescription = "SSL Secure",
                                    tint = MaterialTheme.colorScheme.secondary,
                                    modifier = Modifier.size(12.dp)
                                )
                                Spacer(modifier = Modifier.width(4.dp))
                                Text(
                                    text = if (currentTab == WebTab.EXCHBET) "exchbet365.com" else "crex.com",
                                    fontSize = 11.sp,
                                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                                    maxLines = 1,
                                    overflow = TextOverflow.Ellipsis
                                )
                            }
                        }
                    }
                },
                actions = {
                    IconButton(
                        onClick = { activeWebView?.reload() },
                        modifier = Modifier.testTag("action_refresh")
                    ) {
                        Icon(
                            imageVector = Icons.Default.Refresh,
                            contentDescription = "Refresh Page"
                        )
                    }

                    IconButton(
                        onClick = { showMenu = !showMenu },
                        modifier = Modifier.testTag("action_more")
                    ) {
                        Icon(
                            imageVector = Icons.Default.MoreVert,
                            contentDescription = "More Options"
                        )
                    }

                    DropdownMenu(
                        expanded = showMenu,
                        onDismissRequest = { showMenu = false }
                    ) {
                        DropdownMenuItem(
                            text = { Text("Open in External Browser") },
                            leadingIcon = {
                                Icon(
                                    imageVector = Icons.Default.OpenInBrowser,
                                    contentDescription = null
                                )
                            },
                            onClick = {
                                showMenu = false
                                try {
                                    val currentUrl = activeWebView?.url ?: currentTab.initialUrl
                                    val intent = Intent(Intent.ACTION_VIEW, Uri.parse(currentUrl))
                                    context.startActivity(intent)
                                } catch (_: Exception) {
                                    Toast.makeText(context, "Cannot open browser", Toast.LENGTH_SHORT).show()
                                }
                            }
                        )

                        DropdownMenuItem(
                            text = { Text("Clear Cookies & Cache") },
                            onClick = {
                                showMenu = false
                                CookieManager.getInstance().removeAllCookies(null)
                                CookieManager.getInstance().flush()
                                WebStorage.getInstance().deleteAllData()
                                activeWebView?.clearCache(true)
                                activeWebView?.reload()
                                Toast.makeText(context, "Session cleared", Toast.LENGTH_SHORT).show()
                            }
                        )
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.surface,
                    titleContentColor = MaterialTheme.colorScheme.onSurface
                )
            )
        },
        bottomBar = {
            NavigationBar(
                modifier = Modifier.testTag("bottom_navigation_bar"),
                containerColor = MaterialTheme.colorScheme.surface,
                tonalElevation = 8.dp
            ) {
                WebTab.entries.forEach { tab ->
                    val isSelected = currentTab == tab
                    NavigationBarItem(
                        selected = isSelected,
                        onClick = { currentTab = tab },
                        icon = {
                            Icon(
                                imageVector = tab.icon,
                                contentDescription = tab.title
                            )
                        },
                        label = {
                            Text(
                                text = tab.title,
                                fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal
                            )
                        },
                        modifier = Modifier.testTag(tab.testTag)
                    )
                }
            }
        }
    ) { innerPadding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
        ) {
            // Tab 1: ExchBet - kept alive in layout so switching tabs doesn't reset state
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .then(
                        if (currentTab == WebTab.EXCHBET) {
                            Modifier.zIndex(1f)
                        } else {
                            Modifier
                                .zIndex(0f)
                                .alpha(0f)
                        }
                    )
            ) {
                WebViewContainer(
                    initialUrl = WebTab.EXCHBET.initialUrl,
                    onWebViewCreated = { exchWebView = it }
                )
            }

            // Tab 2: Crex - kept alive in layout so live cricket updates remain active
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .then(
                        if (currentTab == WebTab.CREX) {
                            Modifier.zIndex(1f)
                        } else {
                            Modifier
                                .zIndex(0f)
                                .alpha(0f)
                        }
                    )
            ) {
                WebViewContainer(
                    initialUrl = WebTab.CREX.initialUrl,
                    onWebViewCreated = { crexWebView = it }
                )
            }
        }
    }
}
