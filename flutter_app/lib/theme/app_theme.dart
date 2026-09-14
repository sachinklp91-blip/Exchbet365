import 'package:flutter/material.dart';

class AppTheme {
  // Brand Colors
  static const Color primaryBlue = Color(0xFF0B63E5);
  static const Color primaryBlueDark = Color(0xFF82B1FF);

  static const Color secondaryTeal = Color(0xFF00875A);
  static const Color secondaryTealDark = Color(0xFF57DF9E);

  static const Color darkBackground = Color(0xFF0E1318);
  static const Color darkSurface = Color(0xFF161C24);
  static const Color darkSurfaceVariant = Color(0xFF262E39);

  static ThemeData lightTheme = ThemeData(
    useMaterial3: true,
    brightness: Brightness.light,
    colorScheme: ColorScheme.fromSeed(
      seedColor: primaryBlue,
      brightness: Brightness.light,
      primary: primaryBlue,
      secondary: secondaryTeal,
      surface: Colors.white,
    ),
    appBarTheme: const AppBarTheme(
      centerTitle: false,
      elevation: 0,
      scrolledUnderElevation: 2,
    ),
    navigationBarTheme: NavigationBarThemeData(
      elevation: 3,
      indicatorColor: primaryBlue.withOpacity(0.18),
      labelBehavior: NavigationDestinationLabelBehavior.alwaysShow,
    ),
  );

  static ThemeData darkTheme = ThemeData(
    useMaterial3: true,
    brightness: Brightness.dark,
    colorScheme: ColorScheme.fromSeed(
      seedColor: primaryBlue,
      brightness: Brightness.dark,
      primary: primaryBlueDark,
      secondary: secondaryTealDark,
      surface: darkSurface,
      surfaceContainerHighest: darkSurfaceVariant,
    ),
    scaffoldBackgroundColor: darkBackground,
    appBarTheme: const AppBarTheme(
      backgroundColor: darkSurface,
      centerTitle: false,
      elevation: 0,
      scrolledUnderElevation: 2,
    ),
    navigationBarTheme: NavigationBarThemeData(
      backgroundColor: darkSurface,
      elevation: 3,
      indicatorColor: primaryBlueDark.withOpacity(0.24),
      labelBehavior: NavigationDestinationLabelBehavior.alwaysShow,
    ),
  );
}
