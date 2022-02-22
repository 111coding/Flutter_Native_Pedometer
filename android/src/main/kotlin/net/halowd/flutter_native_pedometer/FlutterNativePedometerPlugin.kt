package net.halowd.flutter_native_pedometer

import androidx.annotation.NonNull

import io.flutter.embedding.engine.plugins.FlutterPlugin
import io.flutter.embedding.engine.plugins.activity.ActivityAware;
import io.flutter.embedding.engine.plugins.activity.ActivityPluginBinding;
import io.flutter.plugin.common.MethodCall
import io.flutter.plugin.common.MethodChannel
import io.flutter.plugin.common.MethodChannel.MethodCallHandler
import io.flutter.plugin.common.MethodChannel.Result
import io.flutter.plugin.common.EventChannel
import io.flutter.plugin.common.EventChannel.*



import androidx.appcompat.app.AppCompatActivity
import android.content.Intent
import android.content.pm.PackageManager
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat

import android.app.Activity;
import android.app.ActivityManager
import android.content.Context

import io.flutter.embedding.android.FlutterActivity
import io.flutter.embedding.engine.FlutterEngine
import io.flutter.embedding.engine.FlutterEngineCache
import io.flutter.embedding.engine.dart.DartExecutor

import net.halowd.flutter_native_pedometer.walker.room.DbInstance
import net.halowd.flutter_native_pedometer.walker.room.WalkDatabase
import net.halowd.flutter_native_pedometer.walker.WalkerEventChannelHaldler
import net.halowd.flutter_native_pedometer.walker.WalkerService

class FlutterNativePedometerPlugin: FlutterPlugin, MethodCallHandler, ActivityAware {
  
  private lateinit var methodChannel : MethodChannel
  private lateinit var eventChannel: EventChannel

  companion object {
    var context : Context? = null
    var mainActivity : Activity? = null
  }

  override fun onAttachedToActivity(binding: ActivityPluginBinding) {
    mainActivity = binding.activity;
  }

  override fun onDetachedFromActivityForConfigChanges() {
  }

  override fun onReattachedToActivityForConfigChanges(binding: ActivityPluginBinding) {
  }

  override fun onDetachedFromActivity() {
  }

  override fun onAttachedToEngine(@NonNull flutterPluginBinding: FlutterPlugin.FlutterPluginBinding) {
    context = flutterPluginBinding.getApplicationContext();

    methodChannel = MethodChannel(flutterPluginBinding.binaryMessenger, "flutter_native_pedometer")
    methodChannel.setMethodCallHandler(this)


    eventChannel = EventChannel(flutterPluginBinding.binaryMessenger, "flutter_native_pedometer_stream")
    WalkerService.eventHandler = WalkerEventChannelHaldler()
    eventChannel.setStreamHandler(WalkerService.eventHandler)
  }

  override fun onMethodCall(@NonNull call: MethodCall, @NonNull result: Result) {
    if (call.method == "get_walk_data") {
      val db = DbInstance.walkDatabase(context!!)
      val count = db!!.walkDao().getRecent((call.arguments as String).toLong())
      // TODO 따로빼기(README 참조)
      runWalkerService()
      result.success(count)
    } else {
      result.notImplemented()
    }
  }

  override fun onDetachedFromEngine(@NonNull binding: FlutterPlugin.FlutterPluginBinding) {
    methodChannel.setMethodCallHandler(null)
    eventChannel.setStreamHandler(null)
  }


  fun runWalkerService(){
    if(!isWalkerRunning()){
      mainActivity!!.startService(Intent(mainActivity!!, WalkerService::class.java))
    }
}

  fun isWalkerRunning() : Boolean {
      val manager = mainActivity!!.getSystemService(Context.ACTIVITY_SERVICE) as ActivityManager
      var isRunning = false
      for (service in manager.getRunningServices(Int.MAX_VALUE)) {
          if (WalkerService::class.java.name == service.service.className) {
              isRunning = true
          }
      }
      return isRunning;
  }
}
