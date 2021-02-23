package com.example.nfctest2;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import android.app.FragmentManager;
import android.app.PendingIntent;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothGatt;
import android.bluetooth.BluetoothGattCallback;
import android.bluetooth.BluetoothGattCharacteristic;
import android.bluetooth.BluetoothManager;
import android.bluetooth.BluetoothProfile;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.IntentFilter.MalformedMimeTypeException;
import android.content.pm.ActivityInfo;
import android.nfc.NdefMessage;
import android.nfc.NdefRecord;
import android.nfc.NfcAdapter;
import android.nfc.tech.NfcF;
import android.os.Bundle;
import android.os.Parcelable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
//import android.widget.Toast;
import android.widget.Toast;
import android.widget.ToggleButton;
import android.widget.VideoView;


import java.util.Arrays;
import java.util.UUID;

public class MainActivity extends AppCompatActivity {

    //----------------------------NFC-------------------------------------------
    private NfcAdapter mAdapter;
    private TextView mText;
    private TextView nText;
    private PendingIntent mPendingIntent;
    private IntentFilter[] mFilters;
    private String[][] mTechLists;
    private int mCount = 0;
    private String videoplay="meme";
    public ImageView mount1;
    public ImageView mount2;
    public ImageView mount3;
    VideoView mp;
    public static int stopPosition;


    //----------------------Bluetooth--------------------------------------------
    public static final String ADDRESS = "B8:27:EB:87:EA:0A";
    public static final String CAPSTONE_SERVICE_UUID = "13333333-3333-3333-3333-333333333337";
    public static final String CAPSTONE_CHARACTERISTICS_UUID = "13333333-3333-3333-3333-333333330001";
    public BluetoothManager bluetoothManager;
    public BluetoothAdapter mBluetoothAdapter;
    public BluetoothDevice device;
    public BluetoothGatt bluetoothGatt;
    public BluetoothGattCharacteristic charCapstone;
    private EditText editText;
    private Button ble_debugbutton;
    //private Button ble_switch;
    private ToggleButton ble_switch2;
    public boolean ble_situation=false;


    mainactivity_ble_fragment fragment= new mainactivity_ble_fragment();



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mText=(TextView)findViewById(R.id.text1);
        nText=(TextView)findViewById(R.id.text2);
       // nText.setText("Scan A");
       // mText.setText("Tag");

        ble_debugbutton=(Button)findViewById(R.id.mainactivityble);
        //ble_switch=(Button)findViewById(R.id.buttonbleswitch);
        ble_switch2=(ToggleButton)findViewById(R.id.toggleBLEButton);
        mount1=(ImageView)findViewById(R.id.imageView3);
        mount2=(ImageView)findViewById(R.id.imageView2);
        mount3=(ImageView)findViewById(R.id.imageView);

        //--------------BLE INITIAL SETUP------------------------------------------
        bluetoothManager = (BluetoothManager)this.getSystemService(Context.BLUETOOTH_SERVICE);
        mBluetoothAdapter = bluetoothManager.getAdapter();


        //---------------NFC INITIAL SETUP------------------------------------------
        mAdapter = NfcAdapter.getDefaultAdapter(this);


        //----------------NFC Availability Test-------------------------------------
        if(mAdapter != null && mAdapter.isEnabled()){
            nText.setText("NFC is");
            mText.setText("Available");
        }else{
            nText.setText("NFC is");
            mText.setText("Not Available");
            Toast.makeText(this,"Enable NFC in phone Setting to proceed",Toast.LENGTH_LONG).show();
        }

        //------------------NFC SETUP-------------------------------------------------

        mPendingIntent = PendingIntent.getActivity(this, 0,
                new Intent(this, getClass()).addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP), 0);


        IntentFilter ndef = new IntentFilter(NfcAdapter.ACTION_NDEF_DISCOVERED);
        try {
            ndef.addDataType("*/*");
            ndef.addCategory(Intent.CATEGORY_DEFAULT);
        } catch (MalformedMimeTypeException e) {
            throw new RuntimeException("fail", e);
        }
        mFilters = new IntentFilter[] {
                ndef,
        };
        mTechLists = new String[][] { new String[] { NfcF.class.getName() } };


        //-------------------ImageView.ClickToPlayVideo-----------------------------------------

        mount1.setOnClickListener(new View.OnClickListener(){
            public void onClick(View v) {
                //videoplay="bolognese";
                getSupportFragmentManager().beginTransaction().replace(android.R.id.content, Videoplay_Fragment.newInstance(R.raw.bolognese),videoplay).addToBackStack(null).commit();
            }
        });

        mount2.setOnClickListener(new View.OnClickListener(){
            public void onClick(View v) {
                //videoplay="carbonara";
                getSupportFragmentManager().beginTransaction().replace(android.R.id.content, Videoplay_Fragment.newInstance(R.raw.carbonara),videoplay).addToBackStack(null).commit();
            }
        });
        mount3.setOnClickListener(new View.OnClickListener(){
            public void onClick(View v) {
                //videoplay="marinara";
                getSupportFragmentManager().beginTransaction().replace(android.R.id.content, Videoplay_Fragment.newInstance(R.raw.marinara),videoplay).addToBackStack(null).commit();
            }
        });


        //-----------------Open BLE DEBUGGING View--------------------------------------------
        ble_debugbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ble_situation=true;
                getSupportFragmentManager().beginTransaction().replace(android.R.id.content, new mainactivity_ble_fragment()).addToBackStack(null).commit();
            }
        });



        /*-------------------BLE SWITCH--------------------------------------------------------
        ble_switch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                device = mBluetoothAdapter.getRemoteDevice(ADDRESS);
                ble_situation=true;
                makeToast("CONNECTED");
                bluetoothGatt = device.connectGatt(getApplicationContext(), true, new BluetoothGattCallback() {
                    @Override
                    public void onConnectionStateChange(BluetoothGatt gatt, int status, int newState) {
                        super.onConnectionStateChange(gatt, status, newState);
                        if (newState == BluetoothProfile.STATE_CONNECTED) {
                            gatt.discoverServices();
                        }
                    }
                    @Override
                    public void onServicesDiscovered(BluetoothGatt gatt, int status) {
                        super.onServicesDiscovered(gatt, status);
                        charCapstone = gatt.getService(UUID.fromString(CAPSTONE_SERVICE_UUID))
                                .getCharacteristic(UUID.fromString(CAPSTONE_CHARACTERISTICS_UUID));
                        bluetoothGatt = gatt;
                    }
                });
            }
        });
        */


        //-------------------BLE Toggle SWITCH--------------------------------------------------

        ble_switch2.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked){
                    device = mBluetoothAdapter.getRemoteDevice(ADDRESS);
                    ble_situation=true;
                    makeToast("CONNECTED");
                    bluetoothGatt = device.connectGatt(getApplicationContext(), true, new BluetoothGattCallback() {
                        @Override
                        public void onConnectionStateChange(BluetoothGatt gatt, int status, int newState) {
                            super.onConnectionStateChange(gatt, status, newState);
                            if (newState == BluetoothProfile.STATE_CONNECTED) {
                                gatt.discoverServices();
                            }
                        }
                        @Override
                        public void onServicesDiscovered(BluetoothGatt gatt, int status) {
                            super.onServicesDiscovered(gatt, status);
                            charCapstone = gatt.getService(UUID.fromString(CAPSTONE_SERVICE_UUID))
                                    .getCharacteristic(UUID.fromString(CAPSTONE_CHARACTERISTICS_UUID));
                            bluetoothGatt = gatt;
                        }
                    });

                }else{
                    ble_situation=false;
                    bluetoothGatt.disconnect();
                    makeToast("DISCONNECTED");
                }
            }
        });


    }


    @Override
    public void onResume() {
        super.onResume();
        mPendingIntent = PendingIntent.getActivity(this, 0, new Intent(this, getClass()), 0);
        mAdapter.enableForegroundDispatch(this, mPendingIntent, mFilters, mTechLists);
    }



    @Override
    public void onNewIntent(Intent intent) {
        //Log.i("Foreground dispatch", "Discovered tag with intent: " );  //+ intent);
        //nText.setText("Discovered tag " + ++mCount + " with intent: " );     //+ intent);
        //vid = ((Videoplay_Fragment)getFragmentManager().findFragmentById(R.raw.foe)).getvideo();

        Parcelable[] messages = intent.getParcelableArrayExtra(NfcAdapter.EXTRA_NDEF_MESSAGES);
        for (int i = 0; i < messages.length; i++) {
            NdefMessage message = (NdefMessage) messages[i];
            NdefRecord[] records = message.getRecords();
            for (int j = 0; j < records.length; j++) {
                NdefRecord record = records[j];
                if (new String(record.getType()).equals("T")) {
                    byte[] original = record.getPayload();
                    byte[] value = Arrays.copyOfRange(original, 3, original.length);
                    String payload = new String(value);
                    //mText.setText(payload);

                    //-----------First Video---------------------------------------------
                    if(payload.equals("1")){

                        if(ble_situation==false){
                            getSupportFragmentManager().beginTransaction().replace(android.R.id.content, Videoplay_Fragment.newInstance(R.raw.bolognese), videoplay).addToBackStack(null).commit();
                        }else {
                            byte values = (byte) Integer.parseInt(payload);
                            charCapstone.setValue(new byte[]{values});
                            bluetoothGatt.writeCharacteristic(charCapstone);
                        }

                    }
                    //-----------Second Video--------------------------------------------
                    else if(payload.equals("2")){

                       if(ble_situation==false){
                           getSupportFragmentManager().beginTransaction().replace(android.R.id.content, Videoplay_Fragment.newInstance(R.raw.carbonara), videoplay).addToBackStack(null).commit();
                       }else {
                           byte values = (byte) Integer.parseInt(payload);
                           charCapstone.setValue(new byte[]{values});
                           bluetoothGatt.writeCharacteristic(charCapstone);
                       }


                    }
                    //-----------Second Video--------------------------------------------
                    else if(payload.equals("3")){

                        if(ble_situation==false){
                            getSupportFragmentManager().beginTransaction().replace(android.R.id.content, Videoplay_Fragment.newInstance(R.raw.marinara), videoplay).addToBackStack(null).commit();
                        }else {
                            byte values = (byte) Integer.parseInt(payload);
                            charCapstone.setValue(new byte[]{values});
                            bluetoothGatt.writeCharacteristic(charCapstone);
                        }


                    }
                    //-----------Pause/Play Video----------------------------------------
                    else if(payload.equals("4")){
                        //((Videoplay_Fragment)getSupportFragmentManager().findFragmentByTag("meme")).videopauseplay();
                        if(ble_situation==false){
                            makeToast("Mode changing only available in monitor view. Please connect your bluetooth first");
                        }else {
                            byte values = (byte) Integer.parseInt(payload);
                            charCapstone.setValue(new byte[]{values});
                            bluetoothGatt.writeCharacteristic(charCapstone);
                        }

                    }
                    //-----------Quit Video-----------------------------------------------
                    else if(payload.equals("5")){
                        if(ble_situation==false){
                            onBackPressed();
                        }else {
                            byte values = (byte) Integer.parseInt(payload);
                            charCapstone.setValue(new byte[]{values});
                            bluetoothGatt.writeCharacteristic(charCapstone);
                        }

                    }
                    //-----------Pause/Play Video----------------------------------------
                    else if(payload.equals("6")){
                        //((Videoplay_Fragment)getSupportFragmentManager().findFragmentByTag("meme")).videopauseplay();
                        if(ble_situation==false){
                            ((Videoplay_Fragment)getSupportFragmentManager().findFragmentByTag(videoplay)).videopauseplay();
                        }else {
                            byte values = (byte) Integer.parseInt(payload);
                            charCapstone.setValue(new byte[]{values});
                            bluetoothGatt.writeCharacteristic(charCapstone);
                        }

                    }
                    else{
                        return;
                    }
                }
            }

        }
    }

    public void makeToast(String message)
    {
        Toast.makeText(this, message, Toast.LENGTH_LONG).show();
    }

    @Override
    public void onPause() {
        super.onPause();
        mAdapter.disableForegroundDispatch(this);
    }


    @Override
    public void onBackPressed() {

        if (getSupportFragmentManager().getBackStackEntryCount() > 0) {
            Intent intent = new Intent(getApplicationContext(), MainActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(intent);
            finish();
        } else {
            super.onBackPressed();
        }

        /*
        ---------------------------------------------------------------------
        int count = getSupportFragmentManager().getBackStackEntryCount();
        if (count > 0) {
            super.onBackPressed();
        }
        else{
            super.onBackPressed();
        }
        ----------------------------------------------------------------------
        Intent intent = new Intent(getApplicationContext(), MainActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);
        finish();
        */
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
    }


}
