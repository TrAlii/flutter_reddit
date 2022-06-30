import 'package:flutter/material.dart';

import 'SettingsPage.dart';

void main() {
  runApp(Settings());
}

class Settings extends StatelessWidget {
  // This widget is the root of your application.
  @override
  Widget build(BuildContext context) {
    return MaterialApp(
      title: 'Settings',
      theme: ThemeData.light(),
      darkTheme: ThemeData.dark(),
      home: SettingsPage(),
    );
  }
}
