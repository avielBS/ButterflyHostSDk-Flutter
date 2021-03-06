package sdk.butterfly_sdk;

import android.app.Activity;
import android.util.Log;

import androidx.annotation.NonNull;

import com.butterfly.sdk.ButterflyHost;

import io.flutter.embedding.engine.plugins.FlutterPlugin;
import io.flutter.plugin.common.MethodCall;
import io.flutter.plugin.common.MethodChannel;
import io.flutter.plugin.common.MethodChannel.MethodCallHandler;
import io.flutter.plugin.common.MethodChannel.Result;

/**
 * ButterflySdkPlugin
 */
public class ButterflySdkPlugin implements FlutterPlugin, MethodCallHandler {
    /// The MethodChannel that will the communication between Flutter and native Android
    ///
    /// This local reference serves to register the plugin with the Flutter Engine and unregister it
    /// when the Flutter Engine is detached from the Activity
    private MethodChannel channel;



    @Override
    public void onAttachedToEngine(@NonNull FlutterPluginBinding flutterPluginBinding) {
        channel = new MethodChannel(flutterPluginBinding.getBinaryMessenger(), "butterfly_sdk");
        channel.setMethodCallHandler(this);
    }

    @Override
    public void onMethodCall(@NonNull MethodCall call, @NonNull Result result) {
        if (call.method.equals("getPlatformVersion")) {
            result.success("Android " + android.os.Build.VERSION.RELEASE);
        } else if (call.method.equals("butterflyHostSDK")) {
            Log.d("butterflyHostSDK", "in butterflyHostSDK function");
            String key = call.argument("key");
            Activity activity = ActivityProvider.Companion.getPresentedActivity();
            if(activity != null) {
                startButterflySDK(activity, key);
            }
        } else {
               result.notImplemented();
        }
    }

    @Override
    public void onDetachedFromEngine(@NonNull FlutterPluginBinding binding) {
        channel.setMethodCallHandler(null);
    }


    private void startButterflySDK(Activity activity, String key) {
        ButterflyHost butterflyHost = ButterflyHost.getInstance();
        butterflyHost.OnGrabReportRequested(activity, key);
    }


}