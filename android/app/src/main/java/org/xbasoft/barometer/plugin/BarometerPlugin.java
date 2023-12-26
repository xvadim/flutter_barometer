package org.xbasoft.barometer.plugin;

import androidx.annotation.NonNull;

import android.annotation.TargetApi;
import android.content.Context;
import android.os.Build;
import android.util.Log;

import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;

import io.flutter.embedding.engine.FlutterEngine;
import io.flutter.embedding.engine.plugins.FlutterPlugin;
import io.flutter.plugin.common.BinaryMessenger;
import io.flutter.plugin.common.EventChannel;
import io.flutter.plugin.common.EventChannel.EventSink;
import io.flutter.plugin.common.MethodCall;
import io.flutter.plugin.common.MethodChannel;
import io.flutter.plugin.common.MethodChannel.MethodCallHandler;
import io.flutter.plugin.common.MethodChannel.Result;

@TargetApi(Build.VERSION_CODES.CUPCAKE)
public class BarometerPlugin implements FlutterPlugin, MethodCallHandler, EventChannel.StreamHandler,
        SensorEventListener {


    @Override
    public void onAttachedToEngine(@NonNull FlutterPluginBinding binding) {
        applicationContext = binding.getApplicationContext();
        BinaryMessenger messenger = binding.getBinaryMessenger();
        channel = new MethodChannel(messenger, prefix + "/command");
        channel.setMethodCallHandler(this);
        dataChannel = new EventChannel(messenger, prefix + "/data");
        dataChannel.setStreamHandler(this);
    }

    @Override
    public void onDetachedFromEngine(@NonNull FlutterPluginBinding binding) {
        channel.setMethodCallHandler(null);
        dataChannel.setStreamHandler(null);
    }

    @Override
    public void onMethodCall(final MethodCall call, final Result result) {
        switch (call.method) {
            case cmdIsPresent:
                boolean isBarometerPresent = true; //'checking presence'
                result.success(isBarometerPresent);
                break;
            case cmdStart:
                start();
                result.success(true);
                break;
            case cmdStop:
                stop();
                result.success(true);
                break;
            default:
                result.notImplemented();
        }
    }

    @Override
    public void onListen(final Object arguments, final EventSink eventSink) {
        Log.i(tag, "ON LISTEN: " + eventSink);
        this.eventSink = eventSink;
    }

    @Override
    public void onCancel(final Object arguments) {
        Log.i(tag, "ON CANCEL");
        eventSink = null;
    }

    private static final String tag = "XBALL";
    private static final String prefix = "org.xbasoft.barometer";
    private static final String cmdIsPresent = "isPresent";
    private static final String cmdStart = "start";
    private static final String cmdStop = "stop";

    private MethodChannel channel;
    private EventChannel dataChannel;
    private EventSink eventSink = null;

    private Context applicationContext;
    private SensorManager mSensorManager;

    private void start() {
        mSensorManager = (SensorManager) applicationContext.getSystemService(Context.SENSOR_SERVICE);
        mSensorManager.registerListener(this,
                    mSensorManager.getDefaultSensor(Sensor.TYPE_PRESSURE),
                    SensorManager.SENSOR_DELAY_UI);
    }

    private void stop() {
        mSensorManager.unregisterListener(this);
    }

    @Override
    public void onSensorChanged(SensorEvent event) {
        if (eventSink != null) {
            eventSink.success(event.values[0]);
        }
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {

    }
}
