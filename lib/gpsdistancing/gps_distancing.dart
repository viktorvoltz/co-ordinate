import 'package:flutter/material.dart';
import 'package:flutter/services.dart';

class GpsDistancing extends StatefulWidget {
  const GpsDistancing({ Key? key }) : super(key: key);

  @override
  State<GpsDistancing> createState() => _GpsDistancingState();
}

class _GpsDistancingState extends State<GpsDistancing> {

  static const platform = MethodChannel('com.chinonso.dev/gpsdistancing');
  String _cordinate = 'Unknown distance.';

  Future<void> _getCordinate() async{
    String cordinate;
    try {
      final int result = await platform.invokeMethod('getCordinate');
      cordinate = 'Cordinates: \n $result .';
    } on PlatformException catch (e) {
      cordinate = "Failed to get location: '${e.message}'.";
    }

    setState(() {
      _cordinate = cordinate;
    });
  }

  @override
  Widget build(BuildContext context) {
    return Material(
      child: Center(
      child: Column(
        mainAxisAlignment: MainAxisAlignment.spaceEvenly,
        children: [
          ElevatedButton(
            child: const Text('Get current cords'),
            onPressed: _getCordinate,
          ),
          Text(_cordinate),
        ],
      ),
    ),
    );
  }
}