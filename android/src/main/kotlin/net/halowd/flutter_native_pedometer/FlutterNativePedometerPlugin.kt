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

import android.util.Log

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
      result.success(count)
    } else if(call.method == "is_running") {
      result.success(isWalkerRunning())
    } else if(call.method == "start_walker") {
      val isRunning = isWalkerRunning();
      if(!isRunning){
        // for broadcast check
        saveState(true)
        WalkerService.WALKING_COUNT = call.arguments as Int
        mainActivity!!.startService(Intent(mainActivity!!, WalkerService::class.java))
      }
      result.success(WalkerService.WALKING_COUNT)
    } else if(call.method == "stop_walker") {
      val isRunning = isWalkerRunning();
      if(isRunning){
        // for broadcast check
        saveState(false)
        mainActivity!!.stopService(Intent(mainActivity!!, WalkerService::class.java))
      }
      result.success(WalkerService.WALKING_COUNT)
    } else {
      result.notImplemented()
    }
  }

  override fun onDetachedFromEngine(@NonNull binding: FlutterPlugin.FlutterPluginBinding) {
    methodChannel.setMethodCallHandler(null)
    eventChannel.setStreamHandler(null)
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

  // for broadcast check
  fun saveState(value : Boolean){
    val edit = context!!.getSharedPreferences("jadoo_walker", Context.MODE_PRIVATE)!!.edit()
    edit!!.putBoolean("jadoo_walker",value)
    edit!!.apply()
  }
}
