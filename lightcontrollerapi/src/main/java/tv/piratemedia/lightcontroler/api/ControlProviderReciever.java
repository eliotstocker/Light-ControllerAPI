package tv.piratemedia.lightcontroler.api;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import java.util.ArrayList;
import java.util.Arrays;

public abstract class ControlProviderReciever extends BroadcastReceiver {
    public static final int ZONE_TYPE_COLOR   = 0;
    public static final int ZONE_TYPE_WHITE   = 1;
    public static final int ZONE_TYPE_UNKNOWN = -1;

    public static final String LIGHT_SELECT_INTENT = "tv.piratemedia.lightcontroler.provider.Select";
    public static final String LIGHT_ON_INTENT = "tv.piratemedia.lightcontroler.provider.LightOn";
    public static final String LIGHT_OFF_INTENT = "tv.piratemedia.lightcontroler.provider.LightOff";
    public static final String LIGHT_GLOBAL_ON_INTENT = "tv.piratemedia.lightcontroler.provider.GlobalOn";
    public static final String LIGHT_GLOBAL_OFF_INTENT = "tv.piratemedia.lightcontroler.provider.GlobalOff";
    public static final String LIGHT_BRIGHTNESS_INTENT = "tv.piratemedia.lightcontroler.provider.Brightness";
    public static final String LIGHT_BRIGHTNESS_UP_INTENT = "tv.piratemedia.lightcontroler.provider.IncreaseBrightness";
    public static final String LIGHT_BRIGHTNESS_DOWN_INTENT = "tv.piratemedia.lightcontroler.provider.DecreaseBrightness";
    public static final String LIGHT_COLOR_INTENT = "tv.piratemedia.lightcontroler.provider.LightColor";
    public static final String LIGHT_TEMPERATURE_INTENT = "tv.piratemedia.lightcontroler.provider.Temperature";
    public static final String LIGHT_TEMPERATURE_UP_INTENT = "tv.piratemedia.lightcontroler.provider.IncreaseTemperature";
    public static final String LIGHT_TEMPERATURE_DOWN_INTENT = "tv.piratemedia.lightcontroler.provider.DecreaseTemperature";
    public static final String LIGHT_NIGHT_INTENT = "tv.piratemedia.lightcontroler.provider.LightNight";
    public static final String LIGHT_FULL_INTENT = "tv.piratemedia.lightcontroler.provider.LightFull";
    public static final String LIGHT_WHITE_INTENT = "tv.piratemedia.lightcontroler.provider.LightWhite";

    private appPermission defPerm = new appPermission("com.piratemedia.lightcontroler", 1321982240);

    private ArrayList<appPermission> allowedApps = new ArrayList<>();

    public ControlProviderReciever() {
        allowedApps.add(defPerm);
    }

    public ControlProviderReciever(appPermission[] permissions) {
        this(permissions, true);
    }

    public ControlProviderReciever(appPermission[] permissions, boolean addLightController) {
        allowedApps.addAll(Arrays.asList(permissions));
        if(addLightController) {
            allowedApps.add(defPerm);
        }
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        Log.d("LCAPI-ControlProviderR", "recieved intent");
        String appId = intent.getStringExtra("app_id");
        int signature = intent.getIntExtra("app_sig", -1);

        boolean callerVerified = false;
        for(appPermission app : allowedApps) {
            if(app.verifyPermission(appId, signature, context)) {
                callerVerified = true;
            }
        }

        if(!callerVerified) {
            Log.e("LCAPI-ControlProviderR", "An application that can not be verified sent a message, quitting...");
            return;
        }

        parseIntentAction(context, intent);
    }

    private void parseIntentAction(Context context, Intent intent) {
        try {
            switch (intent.getAction()) {
                case LIGHT_SELECT_INTENT:
                    onSelected(context);
                    break;
                case LIGHT_ON_INTENT:
                    onLightsOn(checkType(intent), checkZone(intent), context);
                    break;
                case LIGHT_OFF_INTENT:
                    onLightsOff(checkType(intent), checkZone(intent), context);
                    break;
                case LIGHT_GLOBAL_ON_INTENT:
                    onGlobalOn(context);
                    break;
                case LIGHT_GLOBAL_OFF_INTENT:
                    onGlobalOff(context);
                    break;
                case LIGHT_BRIGHTNESS_INTENT:
                    onSetBrightness(checkType(intent), checkZone(intent), intent.getFloatExtra("value", 1f), context);
                    break;
                case LIGHT_BRIGHTNESS_UP_INTENT:
                    onIncreaseBrightness(checkType(intent), checkZone(intent), context);
                    break;
                case LIGHT_BRIGHTNESS_DOWN_INTENT:
                    onDecreaseBrightness(checkType(intent), checkZone(intent), context);
                    break;
                case LIGHT_COLOR_INTENT:
                    if(checkType(intent) != ZONE_TYPE_COLOR) {
                        throw new Exception("Attempted to send unsupported command for bulb type");
                    }
                    onSetColor(checkZone(intent), intent.getIntExtra("value", 0), context);
                    break;
                case LIGHT_TEMPERATURE_INTENT:
                    onSetTemperature(checkType(intent), checkZone(intent), intent.getFloatExtra("value", 1f), context);
                    break;
                case LIGHT_TEMPERATURE_UP_INTENT:
                    onIncreaseTemperature(checkType(intent), checkZone(intent), context);
                    break;
                case LIGHT_TEMPERATURE_DOWN_INTENT:
                    onDecreaseTemperature(checkType(intent), checkZone(intent), context);
                    break;
                case LIGHT_NIGHT_INTENT:
                    onSetNight(checkType(intent), checkZone(intent), context);
                    break;
                case LIGHT_FULL_INTENT:
                    onSetFull(checkType(intent), checkZone(intent), context);
                    break;
                case LIGHT_WHITE_INTENT:
                    if(checkType(intent) != ZONE_TYPE_COLOR) {
                        throw new Exception("Attempted to send unsupported command for bulb type");
                    }
                    onSetWhite(checkType(intent), context);
                    break;
            }
        } catch(NullPointerException e) {
            Log.e("LCAPI-ControlProviderR", e.getMessage());
        } catch(Exception e) {
            Log.e("LCAPI-ControlProviderR", e.getMessage());
        }
    }

    private int checkZone(Intent intent) {
        int Zone = intent.getIntExtra("zone", -1);
        if(Zone < 0 || Zone > 4) {
            throw new NullPointerException("Applications didn't send a valid zone");
        }
        return Zone;
    }

    private int checkType(Intent intent) {
        return intent.getStringExtra("type").equals("color") ? ZONE_TYPE_COLOR : (intent.getStringExtra("type").equals("white") ? ZONE_TYPE_WHITE : ZONE_TYPE_UNKNOWN);
    }

    public abstract void onSelected(Context context);

    public abstract void onLightsOn(int Type, int Zone, Context context);

    public abstract void onLightsOff(int Type, int Zone, Context context);

    public abstract void onGlobalOn(Context context);

    public abstract void onGlobalOff(Context context);

    public abstract void onSetBrightness(int Type, int Zone, float Brightness, Context context);

    public abstract void onIncreaseBrightness(int Type, int Zone, Context context);

    public abstract void onDecreaseBrightness(int Type, int Zone, Context context);

    public abstract void onSetColor(int Zone, int color, Context context);

    public abstract void onSetTemperature(int Type, int Zone, float Temp, Context context);

    public abstract void onIncreaseTemperature(int Type, int Zone, Context context);

    public abstract void onDecreaseTemperature(int Type, int Zone, Context context);

    public abstract void onSetNight(int Type, int Zone, Context context);

    public abstract void onSetFull(int Type, int Zone, Context context);

    public abstract void onSetWhite(int Zone, Context context);
}
