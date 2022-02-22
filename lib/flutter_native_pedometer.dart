
import 'dart:async';

import 'package:flutter/services.dart';

class FlutterNativePedometer {
  static const MethodChannel _channel = MethodChannel('flutter_native_pedometer');

  static Future<String?> get platformVersion async {
    final String? version = await _channel.invokeMethod('getPlatformVersion');
    return version;
  }
}
