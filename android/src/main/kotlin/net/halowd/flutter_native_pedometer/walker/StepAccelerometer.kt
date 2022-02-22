package net.halowd.flutter_native_pedometer.walker

import android.hardware.Sensor
import android.hardware.SensorEvent

import android.hardware.SensorManager

import android.hardware.SensorEventListener
import kotlin.math.abs


class StepAccelerometer : SensorEventListener {
    private var mLimit = 10f
    private val mLastValues = FloatArray(3 * 2)
    private val mScale = FloatArray(2)
    private val mYOffset: Float
    private val mLastDirections = FloatArray(3 * 2)
    private val mLastExtremes = arrayOf(FloatArray(3 * 2), FloatArray(3 * 2))
    private val mLastDiff = FloatArray(3 * 2)
    private var mLastMatch = -1
    private var mListener: StepListener? = null
    fun setSensitivity(sensitivity: Float) {
        mLimit = sensitivity
    }

    fun setStepListener(sl: StepListener?) {
        mListener = sl
    }

    override fun onSensorChanged(event: SensorEvent) {
        val sensor: Sensor = event.sensor
        synchronized(this) {
            if (sensor.type == Sensor.TYPE_ACCELEROMETER) {
                var vSum = 0f
                for (i in 0..2) {
                    val v = mYOffset + event.values[i] * mScale[1]
                    vSum += v
                }
                val k = 0
                val v = vSum / 3
                val direction =
                    (if (v > mLastValues[k]) 1 else if (v < mLastValues[k]) -1 else 0).toFloat()
                if (direction == -mLastDirections[k]) {
                    val extType = if (direction > 0) 0 else 1
                    mLastExtremes[extType][k] = mLastValues[k]
                    val diff = abs(
                        mLastExtremes[extType][k] - mLastExtremes[1 - extType][k]
                    )
                    if (diff > mLimit) {
                        val isAlmostAsLargeAsPrevious =
                            diff > mLastDiff[k] * 2 / 3
                        val isPreviousLargeEnough =
                            mLastDiff[k] > diff / 3
                        val isNotContra = mLastMatch != 1 - extType
                        mLastMatch =
                            if (isAlmostAsLargeAsPrevious && isPreviousLargeEnough && isNotContra) {
                                mListener?.onStep(1)
                                extType
                            } else {
                                -1
                            }
                    }
                    mLastDiff[k] = diff
                }
                mLastDirections[k] = direction
                mLastValues[k] = v
            }
        }
    }

    override fun onAccuracyChanged(sensor: Sensor?, accuracy: Int) {}

    init {
        val h = 480
        mYOffset = h * 0.5f
        mScale[0] = -(h * 0.5f * (1.0f / (SensorManager.STANDARD_GRAVITY * 2)))
        mScale[1] = -(h * 0.5f * (1.0f / SensorManager.MAGNETIC_FIELD_EARTH_MAX))
    }
}