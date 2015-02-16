package tv.piratemedia.lightcontrollerapiexample;

import android.content.Intent;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.chiralcode.colorpicker.ColorPickerDialog;

import tv.piratemedia.lightcontroler.api.LightControllerAPI;
import tv.piratemedia.lightcontroler.api.LightControllerException;
import tv.piratemedia.lightcontroler.api.LightZone;


public class TestActivity extends ActionBarActivity {
    private LightZone Zone = null;
    private TextView selected;
    private LightControllerAPI api;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test);
        try {
            api = new LightControllerAPI(this);
        } catch(LightControllerException e) {
            e.printStackTrace();
            return;
        }

        if(api.hasPermission()) {
            Log.d("Permission", "The Application is given Permission");
        } else {
            Log.d("Permission", "The Application is denied Permission");
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

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if(resultCode == RESULT_OK) {
            Zone = (LightZone)data.getSerializableExtra("LightZone");
            selected.setText(Character.toUpperCase(Zone.Type.charAt(0)) + Zone.Type.substring(1) + ": " + Zone.Name);
        } else if(resultCode == RESULT_CANCELED) {

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
