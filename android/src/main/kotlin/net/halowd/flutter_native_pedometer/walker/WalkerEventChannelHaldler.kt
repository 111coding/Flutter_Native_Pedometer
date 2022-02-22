package net.halowd.flutter_native_pedometer.walker


import io.flutter.embedding.android.FlutterActivity
import io.flutter.embedding.engine.FlutterEngine
import io.flutter.embedding.engine.FlutterEngineCache
import io.flutter.embedding.engine.dart.DartExecutor
import io.flutter.plugin.common.MethodCall
import io.flutter.plugin.common.MethodChannel
import io.flutter.plugin.common.EventChannel

class WalkerEventChannelHaldler : EventChannel.StreamHandler {

    var event : EventChannel.EventSink? = null;

    override fun onListen(arguments: Any?, eventSink: EventChannel.EventSink?) {
        event = eventSink
    }

    override fun onCancel(arguments: Any?) {
        event = null
    }
}
