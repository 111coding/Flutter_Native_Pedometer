import 'package:flutter/material.dart';
import 'dart:async';

import 'package:flutter/services.dart';
import 'package:flutter_native_pedometer/flutter_native_pedometer.dart';

void main() {
  runApp(const MyApp());
}

class MyApp extends StatefulWidget {
  const MyApp({Key? key}) : super(key: key);

  @override
  State<MyApp> createState() => _MyAppState();
}

class _MyAppState extends State<MyApp> {
  @override
  void initState() {
    super.initState();
    initBridge();
  }

  Future<void> initBridge() async {
    int? lastCount = await FlutterNativePedometer.getWalkData(DateTime(2022, 2, 18, 10, 0));

    print("lastCount $lastCount");
    FlutterNativePedometer.getWalkStream().listen((event) {
      print("event $event");
    });
  }

  @override
  Widget build(BuildContext context) {
    return MaterialApp(
      home: Scaffold(
        appBar: AppBar(
          title: const Text('Plugin example app'),
        ),
        body: const Center(
          child: Text('test'),
        ),
      ),
    );
  }
}
