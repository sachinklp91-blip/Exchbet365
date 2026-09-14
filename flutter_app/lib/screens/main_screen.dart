import 'package:flutter/material.dart';
import 'package:flutter/services.dart';
import 'package:url_launcher/url_launcher.dart';
import 'package:webview_flutter/webview_flutter.dart';
import '../models/tab_config.dart';
import '../widgets/webview_tab_view.dart';

class MainScreen extends StatefulWidget {
  final VoidCallback onToggleTheme;
  final ThemeMode themeMode;

  const MainScreen({
    super.key,
    required this.onToggleTheme,
    required this.themeMode,
  });

  @override
  State<MainScreen> createState() => _MainScreenState();
}

class _MainScreenState extends State<MainScreen> {
  int _currentIndex = 0;
  final Map<int, WebViewController> _controllers = {};

  void _onControllerCreated(int index, WebViewController controller) {
    _controllers[index] = controller;
  }

  Future<void> _handlePop(bool didPop, dynamic result) async {
    if (didPop) return;

    final currentController = _controllers[_currentIndex];
    if (currentController != null && await currentController.canGoBack()) {
      await currentController.goBack();
    } else if (_currentIndex != 0) {
      setState(() {
        _currentIndex = 0;
      });
    } else {
      // Exit app cleanly
      SystemNavigator.pop();
    }
  }

  @override
  Widget build(BuildContext context) {
    final theme = Theme.of(context);
    final currentTab = TabConfig.tabs[_currentIndex];
    final currentController = _controllers[_currentIndex];

    return PopScope(
      canPop: false,
      onPopInvokedWithResult: _handlePop,
      child: Scaffold(
        appBar: AppBar(
          title: Row(
            mainAxisSize: MainAxisSize.min,
            children: [
              Text(
                currentTab.title,
                style: theme.textTheme.titleMedium?.copyWith(
                  fontWeight: FontWeight.bold,
                ),
              ),
              const SizedBox(width: 8),
              Container(
                padding: const EdgeInsets.symmetric(horizontal: 8, vertical: 3),
                decoration: BoxDecoration(
                  color: theme.colorScheme.surfaceContainerHighest,
                  borderRadius: BorderRadius.circular(12),
                ),
                child: Row(
                  mainAxisSize: MainAxisSize.min,
                  children: [
                    Icon(
                      Icons.lock_rounded,
                      size: 12,
                      color: theme.colorScheme.secondary,
                    ),
                    const SizedBox(width: 4),
                    Text(
                      currentTab.domain,
                      style: theme.textTheme.labelSmall?.copyWith(
                        color: theme.colorScheme.onSurfaceVariant,
                      ),
                    ),
                  ],
                ),
              ),
            ],
          ),
          actions: [
            IconButton(
              icon: const Icon(Icons.refresh),
              tooltip: 'Refresh',
              onPressed: () {
                currentController?.reload();
              },
            ),
            PopupMenuButton<String>(
              icon: const Icon(Icons.more_vert),
              tooltip: 'Options',
              onSelected: (value) async {
                switch (value) {
                  case 'browser':
                    final currentUrl =
                        await currentController?.currentUrl() ?? currentTab.url;
                    final uri = Uri.tryParse(currentUrl);
                    if (uri != null) {
                      launchUrl(uri, mode: LaunchMode.externalApplication);
                    }
                    break;
                  case 'clear':
                    final cookieManager = WebViewCookieManager();
                    await cookieManager.clearCookies();
                    await currentController?.clearCache();
                    await currentController?.reload();
                    if (context.mounted) {
                      ScaffoldMessenger.of(context).showSnackBar(
                        const SnackBar(
                          content: Text('Cookies and cache cleared'),
                          duration: Duration(seconds: 2),
                        ),
                      );
                    }
                    break;
                  case 'theme':
                    widget.onToggleTheme();
                    break;
                }
              },
              itemBuilder: (context) => [
                const PopupMenuItem(
                  value: 'browser',
                  child: Row(
                    children: [
                      Icon(Icons.open_in_browser, size: 20),
                      SizedBox(width: 12),
                      Text('Open in Browser'),
                    ],
                  ),
                ),
                const PopupMenuItem(
                  value: 'clear',
                  child: Row(
                    children: [
                      Icon(Icons.delete_outline, size: 20),
                      SizedBox(width: 12),
                      Text('Clear Cookies & Cache'),
                    ],
                  ),
                ),
                PopupMenuItem(
                  value: 'theme',
                  child: Row(
                    children: [
                      Icon(
                        widget.themeMode == ThemeMode.dark
                            ? Icons.light_mode_outlined
                            : Icons.dark_mode_outlined,
                        size: 20,
                      ),
                      const SizedBox(width: 12),
                      Text(
                        widget.themeMode == ThemeMode.dark
                            ? 'Switch to Light Mode'
                            : 'Switch to Dark Mode',
                      ),
                    ],
                  ),
                ),
              ],
            ),
          ],
        ),
        body: IndexedStack(
          index: _currentIndex,
          children: [
            for (int i = 0; i < TabConfig.tabs.length; i++)
              WebViewTabView(
                config: TabConfig.tabs[i],
                onControllerCreated: (controller) =>
                    _onControllerCreated(i, controller),
              ),
          ],
        ),
        bottomNavigationBar: NavigationBar(
          selectedIndex: _currentIndex,
          onDestinationSelected: (index) {
            setState(() {
              _currentIndex = index;
            });
          },
          destinations: [
            for (final tab in TabConfig.tabs)
              NavigationDestination(
                icon: Icon(tab.icon),
                selectedIcon: Icon(tab.activeIcon),
                label: tab.title,
              ),
          ],
        ),
      ),
    );
  }
}
