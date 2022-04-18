import 'dart:async';
import 'dart:io';

import 'package:flutter/services.dart';

class FlutterNativePedometer {
  static const _methodChannel = MethodChannel('flutter_native_pedometer');
  static const _eventChannel = EventChannel('flutter_native_pedometer_stream');

  /// for Android
  /// 포그라운드 서비스가 돌고 있는지?
  static Future<bool> isRunning() async {
    if (Platform.isIOS) return true;
    bool result = await _methodChannel.invokeMethod('is_running');
    return result;
  }

  /// for Android
  /// 포그라운드 서비스 시작
  static Future<int> startWalker({required int initStepValue}) async {
    if (Platform.isIOS) return 0;
    int result = await _methodChannel.invokeMethod('start_walker', initStepValue);
    return result;
  }

  /// for Android
  /// 포그라운드 서비스 종료
  static Future<int> stopWalker() async {
    if (Platform.isIOS) return 0;
    int result = await _methodChannel.invokeMethod('stop_walker');
    return result;
  }

  static Future<int?> getWalkData(DateTime beginDate, {bool onlyToday = true}) async {
    final today = DateTime.now();
    DateTime date = beginDate;
    if (onlyToday) {
      if ("${today.year}${today.month}${today.day}" != "${beginDate.year}${beginDate.month}${beginDate.day}") {
        date = DateTime(today.year, today.month, today.day);
      }
    }

    String beginDateString = Platform.isIOS ? "${date.year}-${date.month}-${date.day} ${date.hour}:${date.minute}:${date.second}" : "${date.millisecondsSinceEpoch}";
    final result = await _methodChannel.invokeMethod('get_walk_data', beginDateString);
    if (result is int) {
      return result;
    }
    return null;
  }

  static Future<int?> getRecent() async {
    if (Platform.isIOS) return null;
    final result = await _methodChannel.invokeMethod('get_recent');
    if (result is int) {
      return result;
    }
    return null;
  }

  static Stream<dynamic> getWalkStream() => _eventChannel.receiveBroadcastStream();
}
