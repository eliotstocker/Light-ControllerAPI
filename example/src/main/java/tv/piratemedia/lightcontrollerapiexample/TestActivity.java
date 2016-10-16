package tv.piratemedia.lightcontrollerapiexample;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.support.annotation.NonNull;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.afollestad.materialdialogs.DialogAction;
import com.afollestad.materialdialogs.MaterialDialog;
import com.chiralcode.colorpicker.ColorPickerDialog;

import tv.piratemedia.lightcontroler.api.LightControllerAPI;
import tv.piratemedia.lightcontroler.api.LightControllerException;
import tv.piratemedia.lightcontroler.api.LightZone;
import tv.piratemedia.lightcontroler.api.OnPermissionChanged;


public class TestActivity extends ActionBarActivity {
    private LightZone Zone = null;
    private TextView selected;
    private LightControllerAPI api;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test);

        final PackageManager p = getPackageManager();
        if(p.getComponentEnabledSetting(new ComponentName(getPackageName(), "tv.piratemedia.lightcontrollerapiexample.TestLauncher")) != PackageManager.COMPONENT_ENABLED_STATE_DISABLED) {
            for (String cat : getIntent().getCategories()) {
                if (cat.equals("tv.piratemedia.lightcontroller.plugin")) {
                    new MaterialDialog.Builder(this)
                            .title("Hide Launcher Icon")
                            .content("you can launch this application from inside of Light Controller, would you like to hide the launcher icon?")
                            .positiveText("Yes")
                            .negativeText("No")
                            .onPositive(new MaterialDialog.SingleButtonCallback() {
                                @Override
                                public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                                    p.setComponentEnabledSetting(new ComponentName(getPackageName(), "tv.piratemedia.lightcontrollerapiexample.TestLauncher"), PackageManager.COMPONENT_ENABLED_STATE_DISABLED, PackageManager.DONT_KILL_APP);
                                }
                            })
                            .show();
                }
            }
        }

        try {
            api = new LightControllerAPI(this);
        } catch(LightControllerException e) {
            final Context _this = this;
            LinearLayout view = (LinearLayout)findViewById(R.id.install_app);
            Button install = (Button)findViewById(R.id.install_button);
            view.setVisibility(View.VISIBLE);
            if(e.getCode() == LightControllerException.TYPE_APPLICATION_OLD) {
                TextView desc = (TextView)findViewById(R.id.install_desc);
                desc.setText("The Installed Version of Light Controller is too old to use the required APIs for the App, Click bellow to update from the Play Store");
                install.setText("Update App");
            }
            install.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    LightControllerAPI.getApplicationFromPlayStore(_this);
                }
            });
            return;
        }

        final LinearLayout perms = (LinearLayout) findViewById(R.id.perm_request);
        
        if(api.hasPermission()) {
            perms.setVisibility(View.GONE);
        } else {
            Button permBtn = (Button) findViewById(R.id.perm_button);
            api.setOnPermissionChanged(new OnPermissionChanged() {
                @Override
                public void onChange() {
                    if(api.hasPermission()) {
                        perms.setVisibility(View.GONE);
                    }
                }
            });
            permBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    api.RequiestPermission();
                }
            });
        }

        Button zone = (Button) findViewById(R.id.zone);
        Button on = (Button) findViewById(R.id.on);
        Button off = (Button) findViewById(R.id.off);
        selected = (TextView) findViewById(R.id.selected);
        Button pickColor = (Button) findViewById(R.id.color);
        Button white = (Button) findViewById(R.id.white);

        on.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(Zone != null) {
                    api.lightsOn(Zone);
                }
            }
        });

        off.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(Zone !=  null) {
                    api.lightsOff(Zone);
                }
            }
        });

        zone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                api.pickZone();
            }
        });

        final ColorPickerDialog colorDialog = new ColorPickerDialog(this, 0xFF0000, new ColorPickerDialog.OnColorSelectedListener() {
            @Override
            public void onColorSelected(int color) {
                if(Zone != null) {
                    try {
                        api.setColor(Zone, color);
                    } catch(LightControllerException e) {
                        e.printStackTrace();
                    }
                }
            }
        });

        pickColor.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                colorDialog.show();
            }
        });

        white.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d("button", "set default");
                if(Zone != null) {
                    api.setDefault(Zone);
                }
            }
        });
    }
    
    private void checkPermission() {
        
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if(requestCode == LightControllerAPI.PickRequestCode) {
            if(resultCode == RESULT_OK) {
                Zone = (LightZone)data.getSerializableExtra("LightZone");
                selected.setText(Character.toUpperCase(Zone.Type.charAt(0)) + Zone.Type.substring(1) + ": " + Zone.Name);
            } else if(resultCode == RESULT_CANCELED) {

            }
        }
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_test, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
