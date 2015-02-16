Light Controller API
====================

Description
-----------
Android Client Library for [Light Controller Android App](https://github.com/eliotstocker/Light-Controller) allowing your application to control the lighting in a users home with ease and no need to write any socket collections etc.

Usage
-----
The API can be added into your Gradle dependencies with the following line
```gradle
Coming soon
```

API Usage is as follows:

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
    //your application has permission
} else {
    //your application does not yet have permission
    api.RequiestPermission();
}
```
(I will soon add a content observer that you can use to known once the application has permission)

* Select Zone the user wishes to control
there are multiple ways to do this

1: using the Zone List Picker
```java
api.pickZone();

//register Activity Result Listener withing your activity
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
Full documentation coming at Version 1

Example
-------
a simple example of the API can be found here: [API Example](https://github.com/eliotstocker/Light-ControllerAPI/tree/master/example)

License
-------
Light Controller API is free for use as to allow any application to interface with Light Controller