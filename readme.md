Light Controller API
====================

Description
-----------
Android Client Library for [Light Controller Android App](https://github.com/eliotstocker/Light-Controller) allowing your application to control the lighting in a users home with ease and no need to write any socket collections etc.

Usage
-----
Light Controller is available on [BinTray](https://bintray.com/eliotstocker/maven/LightControllerAPI) in the JCenter Repo
The API can be added into your Gradle dependencies with the following line
```gradle
dependencies {
    compile 'Light-ControllerAPI:lightcontrollerapi:0.9.+'
}
```

API Usage is as follows:

* App Light Controller Permission to your manifest
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
All Control Functions can be called using a LightZone Object or simply a Zone and Type I.E. Zone 1, Color (api.lightsOn(1, "color");)
Full documentation coming at Version 1 for now you can have a look at the example or the API Source for a list of available functions

Example
-------
a simple example of the API can be found here: [API Example](https://github.com/eliotstocker/Light-ControllerAPI/tree/master/example)

License
-------
Light Controller API is free for use as to allow any application to interface with Light Controller using the Apache 2.0 License