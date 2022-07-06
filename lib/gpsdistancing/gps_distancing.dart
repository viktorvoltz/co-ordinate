import 'package:flutter/material.dart';
import 'package:flutter/services.dart';

class GpsDistancing extends StatefulWidget {
  const GpsDistancing({ Key? key }) : super(key: key);

  @override
  State<GpsDistancing> createState() => _GpsDistancingState();
}

class _GpsDistancingState extends State<GpsDistancing> {

  static const platform = MethodChannel('com.chinonso.dev/gpsdistancing');
  String _distance = 'Unknown distance.';

  @override
  Widget build(BuildContext context) {
    return Container(
      
    );
  }
}