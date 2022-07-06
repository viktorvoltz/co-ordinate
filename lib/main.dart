import 'package:flutter/material.dart';
import 'package:gpsdistancing/gpsdistancing/gps_distancing.dart';

void main() {
  runApp(const MyApp());
}

class MyApp extends StatelessWidget {
  const MyApp({Key? key}) : super(key: key);
  @override
  Widget build(BuildContext context) {
    return MaterialApp(
      title: 'Cordinates',
      theme: ThemeData(
        primarySwatch: Colors.blue,
      ),
      home: const GpsDistancing(),
    );
  }
}