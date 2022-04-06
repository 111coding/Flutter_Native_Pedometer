package net.halowd.flutter_native_pedometer.walker


import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.os.Build
import android.util.Log



class BootReceiver : BroadcastReceiver() {

    override fun onReceive(context: Context,intent: Intent) {
        if (Intent.ACTION_BOOT_COMPLETED.equals(intent.getAction())) {

            // 직전에 서비스를 종료하지 않았을 때에만
            Log.wtf("ttt123","${context.getSharedPreferences("jadoo_walker",Context.MODE_PRIVATE).getBoolean("jadoo_walker",false)}")
            if(context.getSharedPreferences("jadoo_walker",Context.MODE_PRIVATE).getBoolean("jadoo_walker",false)){
                val serviceIntent = Intent(context, WalkerService::class.java)
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                    context.startForegroundService(serviceIntent)
                } else {
                    context.startService(serviceIntent);
                }
            }
            
        }
    }

}

