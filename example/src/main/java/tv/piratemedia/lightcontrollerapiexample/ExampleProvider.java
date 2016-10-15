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

    private static appPermission[] ap = {new appPermission("tv.piratemedia.lightcontroler","308201dd30820146020101300d06092a864886f70d010105050030373116301406035504030c0d416e64726f69642044656275673110300e060355040a0c07416e64726f6964310b3009060355040613025553301e170d3136313031343131343330325a170d3436313030373131343330325a30373116301406035504030c0d416e64726f69642044656275673110300e060355040a0c07416e64726f6964310b300906035504061302555330819f300d06092a864886f70d010101050003818d0030818902818100c7c9c93f2ec2afefcacbf9c7f8ee371dd6b982ec569f230d0cad13685260d2654c3486bb9f01bd69b8268613535516c61a888baa7d95088b6de0268e77146c3bd36e9854f223de1c02e2f934e4569b23087b3c85c3e42727a48089314acdb10f53207342e6a842e69bc85981511cbfdeabe304a0f7df9317943a2e6453c5ad4d0203010001300d06092a864886f70d01010505000381810042a680b1733c718001f9a3a023aaeda496e48b3d86f9aad4593550a729b65a7556a7e6555efd6887fc172222d5a78877a17fb154c60876e5b067d3551aa19f3221df1f72de17b60dab4cb3e6f57d0377f2963322e86f01aeabbe41cba31993aa669255bb721f54f5bfe5a95ed19c5d14f0237578b93231751fbd88bd96d4754e")};

    public ExampleProvider() {
        super(ap);
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
        Toast.makeText(context, "All lights of all types on", Toast.LENGTH_SHORT).show();
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
