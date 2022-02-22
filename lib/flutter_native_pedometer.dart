import 'dart:async';
import 'dart:io';

import 'package:flutter/services.dart';

class FlutterNativePedometer {
  static const _methodChannel = MethodChannel('flutter_native_pedometer');
  static const _eventChannel = EventChannel('flutter_native_pedometer_stream');

  static Future<int?> getWalkData(DateTime beginDate) async {
    String beginDateString = Platform.isIOS ? "${beginDate.year}-${beginDate.month}-${beginDate.day} ${beginDate.hour}:${beginDate.minute}:${beginDate.second}" : "${beginDate.millisecondsSinceEpoch}";
    final result = await _methodChannel.invokeMethod('get_walk_data', beginDateString);
    if (result is int) {
      return result;
    }
  }

  static Stream<dynamic> getWalkStream() => _eventChannel.receiveBroadcastStream();
}
