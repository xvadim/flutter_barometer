package org.xbasoft.barometer;

import androidx.annotation.NonNull;
import android.util.Log;
import io.flutter.embedding.android.FlutterActivity;
import io.flutter.embedding.engine.FlutterEngine;

import org.xbasoft.barometer.plugin.BarometerPlugin;

public class MainActivity extends FlutterActivity {
    @Override
    public void configureFlutterEngine(@NonNull FlutterEngine flutterEngine) {
        super.configureFlutterEngine(flutterEngine);
        flutterEngine.getPlugins().add(new BarometerPlugin());
    }
}
