Light Controller API
====================

Build Status
------------
[![Build Status](https://travis-ci.org/eliotstocker/Light-ControllerAPI.svg?branch=master)](https://travis-ci.org/eliotstocker/Light-ControllerAPI)

Demo application
----------------
the demo application can be downloaded from google play here: [LC API Example](https://play.google.com/store/apps/details?id=tv.piratemedia.lightcontrollerapitest&hl=en_GB)

and the code can be fount in this repo in the [Example Directory](https://github.com/eliotstocker/Light-ControllerAPI/tree/master/example)

it provides an example API usage as well as an example provider that simply shows a toast for each command.

Description
-----------
Android Client Library for [Light Controller Android App](https://github.com/eliotstocker/Light-Controller)
allowing your application to control the lighting in a users home with ease and no need to write any socket connections etc.
along with helper classes to write providers for Light Controller to allow it to control other Light brands etc.

Installation
------------
Light Controller API is available on Bintray in the JCenter Repo [ ![Download](https://api.bintray.com/packages/eliotstocker/maven/LightControllerAPI/images/download.svg) ](https://bintray.com/eliotstocker/maven/LightControllerAPI/_latestVersion)

The API can be added into your Gradle dependencies with the following line
```gradle
dependencies {
    compile 'Light-ControllerAPI:lightcontrollerapi:1.1.+'
}
```

Parts
-----
1. [Light Controller Control API](#control-api)
2. [Light Controller Providers](#provider-api)

#Control API

Usage
-----

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
It is not explicityly required that an app must request permission as sending a command (such as light on) will result in a permission request being sent,
however it is best use that in any app with a main activity that permissions are requested and checked on create or resume so as to avoid user confusion.
The Command usage for example does not explicitly request permission, thus on first use a permission requested be be sent.

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

* Now you have the zones you can start to control the zones like the following
```java
//zone1 is a LightZone returned from either Zone List
api.lightsOn(zone1);
```

if you wish your application to show up in the navigation menu in the Light Controller
application you must register an activity with the category 'tv.piratemedia.lightcontroller.plugin'

it is also recommended that you allow the user to remove the launcher icon when accessing your plugin
from light controller, however this is only the best case if the application works mainly as a plugin
for Light Controller rather than a stand alone app.

you can see a simple method for achieving this in the example application.

API Documentation
-----------------
Methods:

| Return Type    | Invocation                                       | Description    |
| :------------- | :----------------------------------------------- | :------------- |
|                | LightControllerAPI(Context context)              | Class constructor you must pass it your applications context for it to call the intents from, Throws a LightControllerException if The application is not installed or The Application version is to old to support this version of the API Client |
| Void           | getApplicationFromPlayStore(Context context)     | (Static) Will bring the user to the play store where they can or update download light controller if needed. |
| Boolean        | isLightControllerInstalled()                     | Return (Boolean) weather Light Controller Application is installed, should not need to be used as is called from the class constructor. |
| LightZone[]    | listZones()                                      | Returns an array of all available Light Zones as LightZone Objects|
| LightZone      | getZone(int Zone, String Type)                   | Will return the Light Zone Object for the specified zone, accepted values for Type are LightControllerAPI.LIGHT_TYPE_COLOR and LightControllerAPI.LIGHT_TYPE_WHITE |
| Boolean        | hasPermission()                                  | Returns (boolean) weather your application has permission to use the Light Controller API |
| Void           | RequiestPermission()                             | Request Permission to use the Light Controller API, this will create in interactive prompt for the user to allow to deny your app, use setOnPermissionChanged to listen for changes to your Permission Status |
| Void           | setOnPermissionChanged(OnPermissionChanged oPC)  | Set a class that implements OnPermissionChanged where the onChange() Function is called when the application is granted/rescinded permission to access the Light Controller API |
| Void           | lightsOn(LightZone lz)                           | Turn lights on for LightZone returned from listZones() or getZone(), you should not construct a LightZone Object for this. |
| Void           | lightsOn(int Zone, String Type)                  | Turn on lights for the numbered zone of the type specified, accepted values for Type are LightControllerAPI.LIGHT_TYPE_COLOR and LightControllerAPI.LIGHT_TYPE_WHITE. |
| Void           | lightsOff(LightZone lz)                          | Turn lights off for a LightZone Object returned from listZones() or getZone(), you should not construct a LightZone Object for this. |
| Void           | lightsOff(int Zone, String Type)                 | Turn off lights for the numbered zone of the type specified, accepted values for Type are LightControllerAPI.LIGHT_TYPE_COLOR and LightControllerAPI.LIGHT_TYPE_WHITE. |
| Void           | setColor(LightZone lz, int Color)                | Set Lights to specified color (For Colored Light Only Dummy!) for LightZone Object returned from listZones() or getZone(), Color Should be a color integer return from the Color class. |
| Void           | setColor(int Zone, String Type, int Color)       | Set Lights to specified color (For Colored Light Only Dummy!) for the numbered zone of the type specified, Color Should be a color integer return from the Color class. |
| Void           | setDefault(LightZone lz)                         | Set Colored Zone Lights to White, and White Zone Lights to Full Brightness for a LightZone Object returned from listZones() or getZone(). |
| Void           | setDefault(int Zone, String Type)                | Set Colored Zone Lights to White, and White Zone Lights to Full Brightness for for the numbered zone of the type specified. |
| Void           | startFadeIn(LightZone lz, int duration)          | Starts the lights lowly getting brighter from the lowest setting over the duration (in seconds) specified for a LightZone Object returned from listZones() or getZone(), Vaules from 30 to 900 are allowed, throws a LightControllerException if the values are outside to the acceptable range. |
| Void           | startFadeIn(int Zone, String Type, int duration) | Starts the lights lowly getting brighter from the lowest setting over the duration (in seconds) specified for a the numbered zone of the type specified, Vaules from 30 to 900 are allowed, throws a LightControllerException if the values are outside to the acceptable range. |
| Void           | startFadeOut(LightZone lz, int duration)         | Starts the lights lowly getting brighter from the highest setting over the duration (in seconds) specified for a LightZone Object returned from listZones() or getZone(), Vaules from 30 to 900 are allowed, throws a LightControllerException if the values are outside to the acceptable range. |
| Void           | startFadeOut(int Zone, String Type, int duration)| Starts the lights lowly getting darker from the highest setting over the duration (in seconds) specified for a the numbered zone of the type specified, Vaules from 30 to 900 are allowed, throws a LightControllerException if the values are outside to the acceptable range. |
| Void           | cancelFade()                                     | Cancels and fade currently in transition on any zone |
| String         | getZoneName(int Zone, String Type)               | Returns the Name saved in Light Controller for the number zone of the specified type, accepted values for Type are LightControllerAPI.LIGHT_TYPE_COLOR and LightControllerAPI.LIGHT_TYPE_WHITE. |
| Void           | pickZone()                                       | Brings forward an interactive dialog withing which users may select a zone to control. The Light Zone Object can be caught from the activity response in the calling activity using onActivityResult, by getting the LightZone Extra as Follows: (LightZone)data.getSerializableExtra("LightZone") |

Properties:

| Type         | Name                   | Description    |
| :----------- | :--------------------- | :------------- |
| int          | PickRequestCode        | this is the request code of the pickZone() dialog so as you may catch this particular Activity response |
| String       | LightControllerPackage | the Light Controller Package Name should you require it. |
| String       | LIGHT_TYPE_COLOR       | Colored Lights LightZone type value, to be used with functions such as LightsOn etc. |
| String       | LIGHT_TYPE_WHITE       | White Lights LightZone type value, to be used with functions such as LightsOn etc. |

Exception:
Some Functions will throw a LightControllerException which you can catch and inspect the Code to know why the Exception was thrown, for example if the installed version of Light Controller is too old, when constructing the LightControllerAPI Class the Code will be Equal to LightControllerException.TYPE_APPLICATION_OLD the known Codes are as follows:
* TYPE_APPLICATION_MISSING: The Light Controller application is not installed, call the getApplicationFromPlayStore method to show the user the play store page.
* TYPE_APPLICATION_OLD: The Light Controller application is installed, but too old to be used with this version of the API client library, call the getApplicationFromPlayStore method to show the user the play store page.
* TYPE_ZONE_TYPE_INCORRECT: The Type String specified to a method such as lightsOn width not and accepted value
* TYPE_VALUE_OUT_OF_RANGE: The Value specified for a fade Method such as startFadeOut was outside of the accepted range


Example
-------
a simple example of the API can be found here: [API Example](https://github.com/eliotstocker/Light-ControllerAPI/tree/master/example)

#Provider API
The Provider API is currently in preview, and as such only works with Beta Releases of Light Controller, it should be considered a Beta and subject to some small changes or additions

Usage
-----
Inover to create a provider you must create a class extending ControlProviderReciever you will see that a number of functions are needed and they are quite self explainitory, further info on the functions required are listed bellow.

you must also register the reciever and setup intent filter as follow:
```xml
<receiver
    android:name="{class that extends ControlProviderReciever}"
    android:label="{Title that shows up fro users when in select dialog}"
    android:exported="true"
    android:enabled="true">
<intent-filter>
    <category android:name="tv.piratemedia.lightcontroler.provider"/>
    <action android:name="tv.piratemedia.lightcontroler.provider.Select"/>
</intent-filter>
<intent-filter>
    <category android:name="tv.piratemedia.lightcontroler.provider"/>
    <action android:name="tv.piratemedia.lightcontroler.provider.LightOn"/>
</intent-filter>
<intent-filter>
    <category android:name="tv.piratemedia.lightcontroler.provider"/>
    <action android:name="tv.piratemedia.lightcontroler.provider.LightOff"/>
</intent-filter>
<intent-filter>
    <category android:name="tv.piratemedia.lightcontroler.provider"/>
    <action android:name="tv.piratemedia.lightcontroler.provider.GlobalOn"/>
</intent-filter>
<intent-filter>
    <category android:name="tv.piratemedia.lightcontroler.provider"/>
    <action android:name="tv.piratemedia.lightcontroler.provider.GlobalOff"/>
</intent-filter>
<intent-filter>
    <category android:name="tv.piratemedia.lightcontroler.provider"/>
    <action android:name="tv.piratemedia.lightcontroler.provider.Brightness"/>
</intent-filter>
<intent-filter>
    <category android:name="tv.piratemedia.lightcontroler.provider"/>
    <action android:name="tv.piratemedia.lightcontroler.provider.IncreaseBrightness"/>
</intent-filter>
<intent-filter>
    <category android:name="tv.piratemedia.lightcontroler.provider"/>
    <action android:name="tv.piratemedia.lightcontroler.provider.DecreaseBrightness"/>
</intent-filter>
<intent-filter>
    <category android:name="tv.piratemedia.lightcontroler.provider"/>
    <action android:name="tv.piratemedia.lightcontroler.provider.LightColor"/>
</intent-filter>
<intent-filter>
    <category android:name="tv.piratemedia.lightcontroler.provider"/>
    <action android:name="tv.piratemedia.lightcontroler.provider.Temperature"/>
</intent-filter>
<intent-filter>
    <category android:name="tv.piratemedia.lightcontroler.provider"/>
    <action android:name="tv.piratemedia.lightcontroler.provider.IncreaseTemperature"/>
</intent-filter>
<intent-filter>
    <category android:name="tv.piratemedia.lightcontroler.provider"/>
    <action android:name="tv.piratemedia.lightcontroler.provider.DecreaseTemperature"/>
</intent-filter>
<intent-filter>
    <category android:name="tv.piratemedia.lightcontroler.provider"/>
    <action android:name="tv.piratemedia.lightcontroler.provider.LightNight"/>
</intent-filter>
<intent-filter>
    <category android:name="tv.piratemedia.lightcontroler.provider"/>
    <action android:name="tv.piratemedia.lightcontroler.provider.LightFull"/>
</intent-filter>
<intent-filter>
    <category android:name="tv.piratemedia.lightcontroler.provider"/>
    <action android:name="tv.piratemedia.lightcontroler.provider.LightWhite"/>
</intent-filter>
<meta-data
        android:name="tv.piratemedia.lightcontroler.provider"
        android:resource="@xml/provider_info" />
</receiver>
```

at the end of the reciever example adove you can see that thre is a metadata field called "tv.piratemedia.lightcontroler.provider" this provides Light Controller with some information about how your reciever works

an example of metadata XML:
```xml
<?xml version="1.0" encoding="utf-8"?>
<control-provider xmlns:android="http://schemas.android.com/apk/res/android"
                  xmlns:app="http://schemas.android.com/apk/res-auto"
                  app:ColorBrightnessStatefull="false"
                  app:WhiteBrightnessStatefull="true"
                  app:ColorHasTemperature="false"
                  app:ColorTemperatureStatefull="false"
                  app:WhiteTemperatureStatefull="false">
</control-provider>
```
this must be present for LightController to be able to load your provider

Security
---------
ControlProviderReciever has a security mechanism where by it will check the calling applications package name and signature to make sure that not just any application can use your provider.

if you wish to enable other applications to use you package you can add their signature and package name in your Providers construction class like so:
```java
private static appPermission[] ap = {new appPermission("com.example.package", 123456789)};
public ExampleProvider() {
    super(ap);
}
```
where 'com.example.package' is the package name and 123456789 is the hashCode of the applications signature, this application must of course implement calling the provider in a similar way to Light Controller

Selection
----------
most of the provider base functions are simply for the use of controlling the lights, however onSelected is a utility function that is called when a user selects your provider,
this can be left empty safely if no setup is required, however if you wish to display an activity to present the user with some options or information you can do so like so:

```java
@Override
public void onSelected(Context context) {
    Intent i = new Intent(context, ProviderSelected.class);
    i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
    context.startActivity(i);
}
```

Provider Documentation
----------------------

Methods to override in ControlProviderReciver:

| Return Type    | Method Name                                                            | Description    |
| :------------- | :--------------------------------------------------------------------- | :------------- |
| Void           | onSelected(Context context)                                            | (Optional) On Selected callback, not required, allows you to run some code when the user selects your provider |
| Void           | onLightsOn(int Type, int Zone, Context context)                        | When the user toggles a light on for a zone in the interface, run your code here to turn off the light |
| Void           | onLightsOff(int Type, int Zone, Context context)                       | When the user toggles a light off for a zone in the interface, run your code here to turn off the light |
| Void           | onGlobalOn(Context context)                                            | Turn on all Lights for All zones of all Types |
| Void           | onGlobalOff(Context context)                                           | Turn off all Lights for All zones of all Types |
| Void           | onSetBrightness(int Type, int Zone, float Brightness, Context context) | Set a Zones brightness when either 'ColorBrightnessStatefull' or 'WhiteBrightnessStatefull' are set to true the float value is between 0 and 1 where 1 is full brightness and 0 is the lowest |
| Void           | onIncreaseBrightness(int Type, int Zone, Context context)              | Increase a Zones brightness by one step when either 'ColorBrightnessStatefull' or 'WhiteBrightnessStatefull' are set to false |
| Void           | onDecreaseBrightness(int Type, int Zone, Context context)              | Decrease a Zones brightness by one step when either 'ColorBrightnessStatefull' or 'WhiteBrightnessStatefull' are set to false |
| Void           | onSetColor(int Zone, int color, Context context)                       | callback to set a zones color, the integer passed in is a standard color code, no type is sent for this as it is expected to be a Color Zone |
| Void           | onSetTemperature(int Type, int Zone, float Temp, Context context)      | Set a Zones temperature when either 'ColorTemperatureStatefull' or 'WhiteTemperatureStatefull' are set to true the float value is between -1 and 1 where 1 is warmest and -1 is the coolest |
| Void           | onIncreaseTemperature(int Type, int Zone, Context context)             | Increase a Zones color temperature by one step when either 'ColorTemperatureStatefull' or 'WhiteTemperatureStatefull' are set to false |
| Void           | onDecreaseTemperature(int Type, int Zone, Context context)             | Decrease a Zones color temperature by one step when either 'ColorTemperatureStatefull' or 'WhiteTemperatureStatefull' are set to false |
| Void           | onSetNight(int Type, int Zone, Context context)                        | Set Zone specified to night mode |
| Void           | onSetFull(int Type, int Zone, Context context)                         | Set Zone specified to full brightness |
| Void           | onSetWhite(int Zone, Context context)                                  | Set a (Color) Zone back to white |

Zones and Types
---------------
all types are sent with one of the following static values ControlProviderReceiver.ZONE_TYPE_COLOR, ControlProviderReceiver.ZONE_TYPE_WHITE or ControlProviderReceiver.ZONE_TYPE_UNKNOWN in the unlikely even the zone is not known

all zones will be integers from 0 to 4, with 0 being global (all zones of that type) and 1 to 4 being zones 1 to 4 respectively.

the global commands (on/off) should turn off oll lights of all types

Example
-------
a simple example provider of the API can be found here: [Example Provider](https://github.com/eliotstocker/Light-ControllerAPI/blob/master/example/src/main/java/tv/piratemedia/lightcontrollerapiexample/ExampleProvider.java)

License
-------
Light Controller API is free for use or modify as to allow any application to interface with Light Controller using the Apache 2.0 License
