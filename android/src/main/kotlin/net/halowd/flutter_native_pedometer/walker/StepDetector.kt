package net.halowd.flutter_native_pedometer.walker

import android.hardware.Sensor
import android.hardware.SensorEvent
import android.hardware.SensorEventListener

class StepDetector : SensorEventListener {

    private var mListener: StepListener? = null

    fun setStepListener(sl: StepListener?) {
        mListener = sl
    }



    override fun onSensorChanged(event: SensorEvent?) {
        if(event!=null){
            if (event!!.sensor.type == Sensor.TYPE_STEP_DETECTOR) {
                // STEP DETECTOR
                val nextCount : Int = event.values[0].toInt()
                mListener?.onStep(nextCount)
            }
        }
    }

    override fun onAccuracyChanged(sensor: Sensor?, accuracy: Int) {}
}