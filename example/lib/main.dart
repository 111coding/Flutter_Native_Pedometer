import 'package:flutter/material.dart';
import 'package:flutter/cupertino.dart' as Cuperticno;
import 'dart:async';

import 'package:flutter_native_pedometer/flutter_native_pedometer.dart';
import 'package:flutter_native_pedometer_example/walker_gauge.dart';

void main() {
  runApp(const MyApp());
}

class MyApp extends StatefulWidget {
  const MyApp({Key? key}) : super(key: key);

  @override
  State<MyApp> createState() => _MyAppState();
}

class _MyAppState extends State<MyApp> {
  bool isWalking = false;
  int step = 0;

  @override
  void initState() {
    super.initState();
    initBridge();
  }

  Future<void> initBridge() async {
    isWalking = await FlutterNativePedometer.isRunning();

    step = await FlutterNativePedometer.getWalkData(DateTime(2022, 2, 18, 10, 0)) ?? 0;

    setState(() {});

    FlutterNativePedometer.getWalkStream().listen((event) {
      step += event as int;
      setState(() {});
    });
  }

  @override
  Widget build(BuildContext context) {
    return MaterialApp(
      home: Scaffold(
        appBar: AppBar(
          title: const Text('Plugin example app'),
        ),
        body: Center(
          child: Column(
            mainAxisAlignment: MainAxisAlignment.center,
            children: [
              Padding(
                padding: EdgeInsets.symmetric(horizontal: 24),
                child: WalkerGauge(
                  percent: 0.75,
                  width: double.infinity,
                ),
              ),
              Text("$step"),
              Cuperticno.CupertinoSwitch(
                value: isWalking,
                onChanged: (v) async {
                  if (v) {
                    step = await FlutterNativePedometer.startWalker(initStepValue: step);
                  } else {
                    step = await FlutterNativePedometer.stopWalker();
                  }
                  isWalking = v;
                  setState(() {});
                },
              )
            ],
          ),
        ),
      ),
    );
  }
}
