package com.voyager.lcars;

import android.app.Activity;
import android.content.ComponentName;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.LauncherActivityInfo;
import android.content.pm.LauncherApps;
import android.net.Uri;
import android.os.BatteryManager;
import android.os.Build;
import android.os.Bundle;
import android.os.Process;
import android.os.VibrationEffect;
import android.os.Vibrator;
import android.provider.MediaStore;
import android.provider.Settings;
import android.util.DisplayMetrics;
import android.webkit.JavascriptInterface;
import android.webkit.WebChromeClient;
import android.webkit.WebResourceRequest;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Locale;

/**
 * Voyager LCARS: standalone home-screen replacement.
 * No network, contacts, microphone, camera, or location permissions requested.
 * HTML is bundled in the app. The native bridge is only available on that asset.
 */
public class MainActivity extends Activity {
    private WebView web;

    @Override public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setStatusBarColor(0xff000000);
        getWindow().setNavigationBarColor(0xff000000);
        web = new WebView(this);
        web.setBackgroundColor(0xff000000);
        WebSettings settings = web.getSettings();
        settings.setJavaScriptEnabled(true);
        settings.setDomStorageEnabled(true);
        settings.setAllowFileAccess(true);
        settings.setAllowContentAccess(false);
        settings.setAllowFileAccessFromFileURLs(false);
        settings.setAllowUniversalAccessFromFileURLs(false);
        settings.setJavaScriptCanOpenWindowsAutomatically(false);
        settings.setSupportMultipleWindows(false);
        settings.setMixedContentMode(WebSettings.MIXED_CONTENT_NEVER_ALLOW);
        settings.setCacheMode(WebSettings.LOAD_DEFAULT);
        web.setWebChromeClient(new WebChromeClient());
        web.addJavascriptInterface(new DeviceBridge(), "Android");
        web.setWebViewClient(new WebViewClient() {
            @Override public boolean shouldOverrideUrlLoading(WebView view, WebResourceRequest request) {
                Uri uri = request.getUrl();
                String target = uri.toString();
                // Only the packaged, local interface may remain inside this WebView.
                // All external destinations must open in a separate Android app so
                // untrusted pages cannot inherit the JavaScript device bridge.
                if (target.equals("file:///android_asset/index.html")) return false;
                String scheme = uri.getScheme();
                if (scheme != null && (scheme.equals("https") || scheme.equals("http")
                        || scheme.equals("tel") || scheme.equals("sms")
                        || scheme.equals("smsto") || scheme.equals("mailto")
                        || scheme.equals("geo"))) {
                    try { startActivity(new Intent(Intent.ACTION_VIEW, uri)); }
                    catch (Exception e) {
                        Toast.makeText(MainActivity.this, "No app available for this link", Toast.LENGTH_SHORT).show();
                    }
                }
                return true;
            }
        });
        setContentView(web);
        web.loadUrl("file:///android_asset/index.html");
    }

    private void launchIntent(Intent intent) {
        if (intent == null) { Toast.makeText(this, "No compatible app installed", Toast.LENGTH_SHORT).show(); return; }
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        try { startActivity(intent); }
        catch (Exception e) { Toast.makeText(this, "No compatible app installed", Toast.LENGTH_SHORT).show(); }
    }

    public final class DeviceBridge {
        @JavascriptInterface public String listApps() {
            JSONArray out = new JSONArray();
            try {
                LauncherApps la = (LauncherApps) getSystemService(LAUNCHER_APPS_SERVICE);
                List<LauncherActivityInfo> appList = la.getActivityList(null, Process.myUserHandle());
                List<LauncherActivityInfo> sorted = new ArrayList<>(appList);
                Collections.sort(sorted, (a,b) -> String.CASE_INSENSITIVE_ORDER.compare(a.getLabel().toString(),b.getLabel().toString()));
                for (LauncherActivityInfo app : sorted) {
                    JSONObject j = new JSONObject();
                    j.put("label", app.getLabel().toString());
                    j.put("component", app.getComponentName().flattenToString());
                    out.put(j);
                }
            } catch (Exception ignored) { }
            return out.toString();
        }

        @JavascriptInterface public void launchApp(String name) {
            if (name == null || name.length()>300) return;
            final ComponentName component = ComponentName.unflattenFromString(name);
            if (component==null) return;
            runOnUiThread(() -> {
                try {
                    LauncherApps la = (LauncherApps) getSystemService(LAUNCHER_APPS_SERVICE);
                    la.startMainActivity(component, Process.myUserHandle(), null, null);
                } catch (Exception e) {
                    try { launchIntent(getPackageManager().getLaunchIntentForPackage(component.getPackageName())); }
                    catch (Exception ignored) { Toast.makeText(MainActivity.this,"Unable to launch application",Toast.LENGTH_SHORT).show(); }
                }
            });
        }

        @JavascriptInterface public void openAction(String action) {
            if (action == null) return;
            runOnUiThread(() -> {
                Intent i;
                switch(action) {
                    case "phone": i=new Intent(Intent.ACTION_DIAL, Uri.parse("tel:")); break;
                    case "messages": i=new Intent(Intent.ACTION_SENDTO, Uri.parse("smsto:")); break;
                    case "browser": i=new Intent(Intent.ACTION_VIEW, Uri.parse("https://www.google.com/")); break;
                    case "maps": i=new Intent(Intent.ACTION_VIEW, Uri.parse("geo:0,0?q=")); break;
                    case "camera": i=new Intent(MediaStore.INTENT_ACTION_STILL_IMAGE_CAMERA); break;
                    case "settings": i=new Intent(Settings.ACTION_SETTINGS); break;
                    case "files":
                        i=new Intent(Intent.ACTION_OPEN_DOCUMENT);
                        i.setType("*/*"); i.addCategory(Intent.CATEGORY_OPENABLE);
                        break;
                    case "calendar":
                        i=new Intent(Intent.ACTION_MAIN);
                        i.addCategory(Intent.CATEGORY_APP_CALENDAR);
                        break;
                    default: return;
                }
                launchIntent(i);
            });
        }

        @JavascriptInterface public void vibrate(int durationMs) {
            if (durationMs < 1 || durationMs > 120) return;
            runOnUiThread(() -> {
                try {
                    Vibrator vibrator = (Vibrator) getSystemService(VIBRATOR_SERVICE);
                    if (vibrator == null || !vibrator.hasVibrator()) return;
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                        vibrator.vibrate(VibrationEffect.createOneShot(durationMs, VibrationEffect.DEFAULT_AMPLITUDE));
                    } else {
                        vibrator.vibrate(durationMs);
                    }
                } catch (Exception ignored) { }
            });
        }

        @JavascriptInterface public String getDeviceInfo() {
            JSONObject info = new JSONObject();
            Intent battery = registerReceiver(null, new IntentFilter(Intent.ACTION_BATTERY_CHANGED));
            int pct = -1; boolean charging = false;
            if (battery != null) {
                int level = battery.getIntExtra(BatteryManager.EXTRA_LEVEL,-1);
                int scale = battery.getIntExtra(BatteryManager.EXTRA_SCALE,-1);
                if (level >= 0 && scale > 0) pct = Math.round(level*100f/scale);
                charging = battery.getIntExtra(BatteryManager.EXTRA_PLUGGED,0)!=0;
            }
            DisplayMetrics dm = getResources().getDisplayMetrics();
            try {
                info.put("battery",pct);
                info.put("charging",charging);
                info.put("model", Build.MODEL);
                info.put("version", Build.VERSION.RELEASE);
                info.put("width",dm.widthPixels);
                info.put("height",dm.heightPixels);
            } catch (JSONException ignored) { }
            return info.toString();
        }

        @JavascriptInterface public void openHomeSettings() {
            runOnUiThread(() -> launchIntent(new Intent(Settings.ACTION_HOME_SETTINGS)));
        }

        @JavascriptInterface public void shareText(String text) {
            if (text==null || text.length()>500000) return;
            runOnUiThread(() -> {
                Intent i=new Intent(Intent.ACTION_SEND);
                i.setType("text/plain");
                i.putExtra(Intent.EXTRA_TEXT,text);
                try { startActivity(Intent.createChooser(i,"Export Captain's Log")); }
                catch (Exception e) { Toast.makeText(MainActivity.this,"Sharing unavailable",Toast.LENGTH_SHORT).show(); }
            });
        }
    }

    @Override public void onBackPressed() {
        if (web != null) web.evaluateJavascript("window.LCARS&&window.LCARS.home()",null);
        else super.onBackPressed();
    }

    @Override protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        setIntent(intent);
        if (web != null) web.evaluateJavascript("window.LCARS&&window.LCARS.home()", null);
    }

    @Override protected void onResume() {
        super.onResume();
        if (web != null) web.evaluateJavascript("window.LCARS&&window.LCARS.resume()", null);
    }

    @Override protected void onDestroy() {
        if (web != null) { web.removeJavascriptInterface("Android"); web.destroy(); web=null; }
        super.onDestroy();
    }
}
