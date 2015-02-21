package tv.piratemedia.lightcontroler.api;

import android.app.Activity;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.database.ContentObserver;
import android.database.Cursor;
import android.net.Uri;
import android.os.Handler;
import android.os.Message;
import android.util.Log;

public class LightControllerAPI {
    public final static String LightControllerPackage = "tv.piratemedia.lightcontroler";
    public final static int PickRequestCode = 46598;

    private Context _ctx;
    private String _pkgName;
    private Boolean LightControllerMissing = false;
    private ContentObserver contentObserver;
    private Handler handler;
    private OnPermissionChanged onPermissionChanged = null;
    private int installedVersion = -1;

    public LightControllerAPI(Context ctx) throws LightControllerException {
        _ctx = ctx;

        //check Light Controller is installed
        if(!isLightControllerInstalled()) {
            LightControllerMissing = true;
            throw new LightControllerException(LightControllerException.TYPE_APPLICATION_MISSING);
        }
        //check Light Controller is high enough version for Using API Client
        if(ctx.getResources().getInteger(R.integer.min_versioncode) > installedVersion) {
            throw new LightControllerException(LightControllerException.TYPE_APPLICATION_OLD);
        }

        //get application package name
        _pkgName = _ctx.getPackageName();

        setupContentObserver();
        _ctx.getContentResolver().registerContentObserver(Uri.parse("content://"+LightControllerPackage+".api/permission"), true, contentObserver);
    }
    
    private void setupContentObserver() {
        handler = new Handler() {
            @Override
            public void handleMessage(Message msg) {
                super.handleMessage(msg);
                Log.d("permissions", "Perms Changed");
                if(onPermissionChanged != null) {
                    onPermissionChanged.onChange();
                }
            }
        };
        contentObserver = new ContentObserver(handler) {
            @Override
            public void onChange(boolean selfChange) {
                this.onChange(selfChange, null);
            }
            @Override
            public void onChange(boolean selfChange, Uri uri) {
                Message msg = new Message();
                msg.what = 1;
                handler.sendMessage(msg);
            }
        };
    }

    public static void getApplicationFromPlayStore(Context ctx) {
        try {
            ctx.startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("market://details?id=" + LightControllerPackage)));
        } catch (android.content.ActivityNotFoundException anfe) {
            ctx.startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("https://play.google.com/store/apps/details?id=" + LightControllerPackage)));
        }
    }

    public boolean isLightControllerInstalled() {
        PackageManager pm = _ctx.getPackageManager();
        try {
            PackageInfo p = pm.getPackageInfo(LightControllerPackage, PackageManager.GET_ACTIVITIES);
            installedVersion = p.versionCode;
            return true;
        } catch (PackageManager.NameNotFoundException e) {
            return false;
        }
    }

    public LightZone[] listZones() {
        Cursor c = _ctx.getContentResolver().query(
                Uri.parse("content://"+LightControllerPackage+".api/zones"),
                null,
                null,
                null,
                null
        );
        c.moveToFirst();
        String[] cs = c.getColumnNames();
        LightZone[] ZoneArray = new LightZone[c.getCount()];
        for(int j = 0; j < c.getCount(); j++) {
            LightZone lz = new LightZone();
            for(int i = 0; i < cs.length; i++) {
                switch(cs[i]) {
                    case "id":
                        lz.ID = c.getInt(i);
                        break;
                    case "name":
                        lz.Name = c.getString(i);
                        break;
                    case "type":
                        lz.Type = c.getString(i);
                        break;
                    case "global":
                        lz.Global = c.getInt(i) == 1;
                        break;
                }
            }
            c.moveToNext();
            ZoneArray[j] = lz;
        }

        return ZoneArray;
    }

    public LightZone getZone(int zone, String type) {
        int ID = zone;
        if(type.equals("white")) {
            ID += 4;
        }
        Cursor c = _ctx.getContentResolver().query(
                Uri.parse("content://"+LightControllerPackage+".api/zones/"+ID),
                null,
                null,
                null,
                null
        );
        c.moveToFirst();
        String[] cs = c.getColumnNames();
        LightZone lz = new LightZone();
        for(int i = 0; i < cs.length; i++) {
            switch(cs[i]) {
                case "id":
                    lz.ID = c.getInt(i);
                    break;
                case "name":
                    lz.Name = c.getString(i);
                    break;
                case "type":
                    lz.Type = c.getString(i);
                    break;
                case "global":
                    lz.Global = c.getInt(i) == 1;
                    break;
            }
        }

        return lz;
    }

    public boolean hasPermission() {
        String[] projection = new String[]{"id", "allowed"};
        Cursor c = _ctx.getContentResolver().query(
                Uri.parse("content://"+LightControllerPackage+".api/permission/"+_pkgName),
                projection,
                null,
                null,
                null
        );
        c.moveToFirst();
        String[] cs = c.getColumnNames();
        int allowedIndex = -1;
        for(int i = 0; i < cs.length; i++) {
            if(cs[i].equals("allowed")) {
                allowedIndex = i;
            }
        }

        return c.getInt(allowedIndex) == 1;
    }

    public void RequiestPermission() {
        Intent intent = new Intent();
        intent.addFlags(Intent.FLAG_INCLUDE_STOPPED_PACKAGES);
        intent.setAction(LightControllerPackage+".requestAPIPermission");
        intent.putExtra("app_id", _pkgName);
        _ctx.sendBroadcast(intent);
    }
    
    public void setOnPermissionChanged(OnPermissionChanged opc) {
        onPermissionChanged = opc;
    }

    public void lightsOn(LightZone lz) {
        lightsOn(lz.ID, lz.Type);
    }

    public void lightsOn(int zone, String type) {
        Intent intent = new Intent();
        intent.addFlags(Intent.FLAG_INCLUDE_STOPPED_PACKAGES);
        intent.setAction(LightControllerPackage+".LightOn");
        intent.putExtra("type", type);
        intent.putExtra("zone", zone);
        intent.putExtra("app_id", _pkgName);
        _ctx.sendBroadcast(intent);
    }

    public void lightsOff(LightZone lz) {
        lightsOff(lz.ID, lz.Type);
    }

    public void lightsOff(int zone, String type) {
        Intent intent = new Intent();
        intent.addFlags(Intent.FLAG_INCLUDE_STOPPED_PACKAGES);
        intent.setAction(LightControllerPackage+".LightOff");
        intent.putExtra("type", type);
        intent.putExtra("zone", zone);
        intent.putExtra("app_id", _pkgName);
        _ctx.sendBroadcast(intent);
    }

    public void setColor(LightZone lz, int color) throws LightControllerException {
        setColor(lz.ID, lz.Type, color);
    }

    public void setColor(int zone, String type, int color) throws LightControllerException {
        if(!type.equals("color")) {
            throw new LightControllerException(LightControllerException.TYPE_ZONE_TYPE_INCORRECT);
        }
        Intent intent = new Intent();
        intent.addFlags(Intent.FLAG_INCLUDE_STOPPED_PACKAGES);
        intent.setAction(LightControllerPackage+".LightColor");
        intent.putExtra("app_id", _pkgName);
        intent.putExtra("type", type);
        intent.putExtra("zone", zone);
        intent.putExtra("color", color);
        _ctx.sendBroadcast(intent);
    }

    public void setDefault(LightZone lz) {
        setDefault(lz.ID, lz.Type);
    }

    public void setDefault(int zone, String type) {
        Intent intent = new Intent();
        intent.addFlags(Intent.FLAG_INCLUDE_STOPPED_PACKAGES);
        intent.setAction(LightControllerPackage+".LightDefault");
        intent.putExtra("app_id", _pkgName);
        intent.putExtra("type", type);
        intent.putExtra("zone", zone);
        _ctx.sendBroadcast(intent);
    }

    public String getZoneName(int zone, String type) {
        LightZone lz = getZone(zone, type);
        return lz.Name;
    }

    public void pickZone() {
        final Intent i = new Intent("android.intent.action.MAIN");
        i.setComponent(ComponentName.unflattenFromString(LightControllerPackage+"/"+LightControllerPackage+".api.APIZoneSelector"));
        ((Activity)_ctx).startActivityForResult(i, PickRequestCode);
    }
}
