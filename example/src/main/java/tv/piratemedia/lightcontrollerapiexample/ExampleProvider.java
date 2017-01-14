package tv.piratemedia.lightcontrollerapiexample;

import android.content.Context;
import android.content.Intent;
import android.widget.Toast;
import tv.piratemedia.lightcontroler.api.ControlProviderReciever;
import tv.piratemedia.lightcontroler.api.appPermission;

/**
 * Created by Eliot Stocker on 14/10/2016.
 */
public class ExampleProvider extends ControlProviderReciever {

    private static appPermission[] ap = {new appPermission("tv.piratemedia.lightcontroler", -448508482)};

    public ExampleProvider() {
        super(ap);
    }

    @Override
    public void onDiscovery(Context context) {
        Toast.makeText(context, "Attempt Discovery", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onSelected(Context context) {
        //Toast.makeText(context, "Selected Example Provider, Yey!", Toast.LENGTH_SHORT).show();
        Intent i = new Intent(context, ProviderSelected.class);
        i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startActivity(i);
    }

    @Override
    public void onLightsOn(int Type, int Zone, Context context) {
        Toast.makeText(context, (Type == ZONE_TYPE_COLOR ? "Color" : "White")+" Lights on In Zone: "+Zone, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onLightsOff(int Type, int Zone, Context context) {
        Toast.makeText(context, (Type == ZONE_TYPE_COLOR ? "Color" : "White")+" Lights off In Zone: "+Zone, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onGlobalOn(Context context) {
        Toast.makeText(context, "All lights of all types on", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onGlobalOff(Context context) {
        Toast.makeText(context, "All lights of all types off", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onSetBrightness(int Type, int Zone, float Brightness, Context context) {
        Toast.makeText(context, (Type == ZONE_TYPE_COLOR ? "Color" : "White")+" Lights in Zone "+Zone+" to "+((int)(Brightness * 100f))+"%", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onIncreaseBrightness(int Type, int Zone, Context context) {
        Toast.makeText(context, "Increase Brightness for "+(Type == ZONE_TYPE_COLOR ? "Color" : "White")+" Lights in Zone "+Zone, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onDecreaseBrightness(int Type, int Zone, Context context) {
        Toast.makeText(context, "Decrease Brightness for "+(Type == ZONE_TYPE_COLOR ? "Color" : "White")+" Lights in Zone "+Zone, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onSetColor(int Zone, int color, Context context) {
        //must be white zone
        Toast.makeText(context, "set Color to "+ String.format("#%06X", 0xFFFFFF & color)+" in Zone "+Zone, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onSetTemperature(int Type, int Zone, float Temp, Context context) {
        Toast.makeText(context, (Type == ZONE_TYPE_COLOR ? "Color" : "White")+" Lights in Zone "+Zone+" to "+((int)(Temp * 100f))+"% Temperature", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onIncreaseTemperature(int Type, int Zone, Context context) {
        Toast.makeText(context, "Increase Temperature for "+(Type == ZONE_TYPE_COLOR ? "Color" : "White")+" Lights in Zone "+Zone, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onDecreaseTemperature(int Type, int Zone, Context context) {
        Toast.makeText(context, "Decrease Temperature for "+(Type == ZONE_TYPE_COLOR ? "Color" : "White")+" Lights in Zone "+Zone, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onSetNight(int Type, int Zone, Context context) {
        Toast.makeText(context, "Night mode for "+(Type == ZONE_TYPE_COLOR ? "Color" : "White")+" Lights in Zone "+Zone, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onSetFull(int Type, int Zone, Context context) {
        Toast.makeText(context, "Full Brightness for "+(Type == ZONE_TYPE_COLOR ? "Color" : "White")+" Lights in Zone "+Zone, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onSetWhite(int Zone, Context context) {
        //must be white zone
        Toast.makeText(context, "Lights in Zone "+Zone+" to White", Toast.LENGTH_SHORT).show();
    }
}
