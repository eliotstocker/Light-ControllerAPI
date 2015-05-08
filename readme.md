Light Controller API
====================

Description
-----------
Android Client Library for [Light Controller Android App](https://github.com/eliotstocker/Light-Controller) allowing your application to control the lighting in a users home with ease and no need to write any socket connections etc.

Usage
-----
Light Controller is available on [BinTray](https://bintray.com/eliotstocker/maven/LightControllerAPI) in the JCenter Repo
The API can be added into your Gradle dependencies with the following line
```gradle
dependencies {
    compile 'Light-ControllerAPI:lightcontrollerapi:1.0.+'
}
```

API Usage is as follows:

* Add Light Controller Permission to your manifest
```xml
<uses-permission android:name="tv.piratemedia.lightcontroler.api"/>
```

* Initialize API
```java
LightControllerAPI api
try {
    api = new LightControllerAPI(this);
} catch(LightControllerException e) {
    if(e.getCode() == TYPE_APPLICATION_MISSING) {
        //Light Controller application is not installed
        //open store to download Light Controller
        LightControllerAPI.getApplicationFromPlayStore(this);
    } elseif(e.getCode() == TYPE_APPLICATION_OLD) {
        //Light Controller application too old
        //open store to download Latest Light Controller
        LightControllerAPI.getApplicationFromPlayStore(this);
    }
}
```

* Check if your application is allowed permission to control lights
```java
if(api.hasPermission()) {
            //application has permission
} else {
    //app doesnt have permission, set event for if any permissions change on the api
    api.setOnPermissionChanged(new OnPermissionChanged() {
        @Override
        public void onChange() {
            //still need to check if your app has permission as this is called on any app permission change
            if(api.hasPermission()) {
                perms.setVisibility(View.GONE);
            }
        }
    });
}
```
It is not explicityly required that an app must request permission as sending a command (such as light on) will result in a permission request being sent, however it is best use that in any app with a main activity that permissions are requested and checked on create or resume so as to avoid user confusion.
The Commandr usage for example does not explicitly request permission, thus on first use a permission requested be be sent.

* Select Zone the user wishes to control
there are multiple ways to do this

1: using the Zone List Picker
```java
api.pickZone();

//register Activity Result Listener withing your activity, required to get the return from the pick activity
@Override
protected void onActivityResult(int requestCode, int resultCode, Intent data) {
    if(requestCode == LightControllerAPI.PickRequestCode) {
        if(resultCode == RESULT_OK) {
            //save zone for later use
            Zone = (LightZone)data.getSerializableExtra("LightZone");
            //show user selected zone
            selected.setText(Character.toUpperCase(Zone.Type.charAt(0)) + Zone.Type.substring(1) + ": " + Zone.Name);
        } else if(resultCode == RESULT_CANCELED) {
            //picker was closed without picking
        }
    }
}
```

2: using LightZone List API
```java
LightZone[] zoneList = api.listZones();

for(int i = 0; i < zoneList.length; i++) {
    LightZone zone = zoneList[i];
    Log.d("Zone", "Name: "+zone.Name+" Type: "+zone.Type);
    
    //do what ever you like with the zones here (add to listView perhapse)
}
```

* Now you have the zones you can start to control the zones with like the following
```java
//zone1 is a LightZone returned from either Zone List
api.lightsOn(zone1);
```

API Documentation
-----------------
Methods:
 Return Type    | Invocation                                       | Description
 :------------- | :----------------------------------------------- | :-------------
 Void           | LightControllerAPI(Context context)              | Class constructor you must pass it your applications context for it to call the intents from, Throws a LightControllerException if The application is not installed or The Application version is to old to support this version of the API Client
 Void           | getApplicationFromPlayStore(Context context)     | (Static) Will bring the user to the play store where they can or update download light controller if needed.
 Boolean        | isLightControllerInstalled()                     | Return (Boolean) weather Light Controller Application is installed, should not need to be used as is called from the class constructor.
 LightZone[]    | listZones()                                      | Returns an array of all available Light Zones as LightZone Objects
 LightZone      | getZone(int Zone, String Type)                   | Will return the Light Zone Object for the specified zone, accepted values for Type are LightControllerAPI.LIGHT_TYPE_COLOR and LightControllerAPI.LIGHT_TYPE_WHITE
 Boolean        | hasPermission()                                  | Returns (boolean) weather your application has permission to use the Light Controller API
 Void           | RequiestPermission()                             | Request Permission to use the Light Controller API, this will create in interactive prompt for the user to allow to deny your app, use setOnPermissionChanged to listen for changes to your Permission Status
 Void           | setOnPermissionChanged(OnPermissionChanged oPC)  | Set a class that implements OnPermissionChanged where the onChange() Function is called when the application is granted/rescinded permission to access the Light Controller API
 Void           | lightsOn(LightZone lz)                           | Turn lights on for LightZone returned from listZones() or getZone(), you should not construct a LightZone Object for this.
 Void           | lightsOn(int Zone, String Type)                  | Turn on lights for the numbered zone of the type specified, accepted values for Type are LightControllerAPI.LIGHT_TYPE_COLOR and LightControllerAPI.LIGHT_TYPE_WHITE.
 Void           | lightsOff(LightZone lz)                          | Turn lights off for a LightZone Object returned from listZones() or getZone(), you should not construct a LightZone Object for this.
 Void           | lightsOff(int Zone, String Type)                 | Turn off lights for the numbered zone of the type specified, accepted values for Type are LightControllerAPI.LIGHT_TYPE_COLOR and LightControllerAPI.LIGHT_TYPE_WHITE.
 Void           | setColor(LightZone lz, int Color)                | Set Lights to specified color (For Colored Light Only Dummy!) for LightZone Object returned from listZones() or getZone(), Color Should be a color integer return from the Color class.
 Void           | setColor(int Zone, String Type, int Color)       | Set Lights to specified color (For Colored Light Only Dummy!) for the numbered zone of the type specified, Color Should be a color integer return from the Color class.
 Void           | setDefault(LightZone lz)                         | Set Colored Zone Lights to White, and White Zone Lights to Full Brightness for a LightZone Object returned from listZones() or getZone().
 Void           | setDefault(int Zone, String Type)                | Set Colored Zone Lights to White, and White Zone Lights to Full Brightness for for the numbered zone of the type specified.
 Void           | startFadeIn(LightZone lz, int duration)          | Starts the lights lowly getting brighter from the lowest setting over the duration (in seconds) specified for a LightZone Object returned from listZones() or getZone(), Vaules from 30 to 900 are allowed, throws a LightControllerException if the values are outside to the acceptable range.
 Void           | startFadeIn(int Zone, String Type, int duration) | Starts the lights lowly getting brighter from the lowest setting over the duration (in seconds) specified for a the numbered zone of the type specified, Vaules from 30 to 900 are allowed, throws a LightControllerException if the values are outside to the acceptable range.
 Void           | startFadeOut(LightZone lz, int duration)         | Starts the lights lowly getting brighter from the highest setting over the duration (in seconds) specified for a LightZone Object returned from listZones() or getZone(), Vaules from 30 to 900 are allowed, throws a LightControllerException if the values are outside to the acceptable range.
 Void           | startFadeOut(int Zone, String Type, int duration)| Starts the lights lowly getting darker from the highest setting over the duration (in seconds) specified for a the numbered zone of the type specified, Vaules from 30 to 900 are allowed, throws a LightControllerException if the values are outside to the acceptable range.
 Void           | cancelFade()                                     | Cancels and fade currently in transition on any zone
 String         | getZoneName(int Zone, String Type)               | Returns the Name saved in Light Controller for the number zone of the specified type, accepted values for Type are LightControllerAPI.LIGHT_TYPE_COLOR and LightControllerAPI.LIGHT_TYPE_WHITE.
 Void           | pickZone()                                       | Brings forward an interactive dialog withing which users may select a zone to control. The Light Zone Object can be caught from the activity response in the calling activity using onActivityResult, by getting the LightZone Extra as Follows: (LightZone)data.getSerializableExtra("LightZone")

Properties:
 Type         | Name                   | Description
 :----------- | :--------------------- | :-------------
 int          | PickRequestCode        | this is the request code of the pickZone() dialog so as you may catch this particular Activity response
 String       | LightControllerPackage | the Light Controller Package Name should you require it.
 String       | LIGHT_TYPE_COLOR       | Colored Lights LightZone type value, to be used with functions such as LightsOn etc.
 String       | LIGHT_TYPE_WHITE       | White Lights LightZone type value, to be used with functions such as LightsOn etc.

Exception:
Some Functions will throw a LightControllerException which you can catch and inspect the Code to know why the Exception was thrown, for example if the installed version of Light Controller is too old, when constructing the LightControllerAPI Class the Code will be Equal to LightControllerException.TYPE_APPLICATION_OLD the known Codes are as follows:
* TYPE_APPLICATION_MISSING: The Light Controller application is not installed, call the getApplicationFromPlayStore method to show the user the play store page.
* TYPE_APPLICATION_OLD: The Light Controller application is installed, but too old to be used with this version of the API client library, call the getApplicationFromPlayStore method to show the user the play store page.
* TYPE_ZONE_TYPE_INCORRECT: The Type String specified to a method such as lightsOn width not and accepted value
* TYPE_VALUE_OUT_OF_RANGE: The Value specified for a fade Method such as startFadeOut was outside of the accepted range


Example
-------
a simple example of the API can be found here: [API Example](https://github.com/eliotstocker/Light-ControllerAPI/tree/master/example)

License
-------
Light Controller API is free for use or modify as to allow any application to interface with Light Controller using the Apache 2.0 License