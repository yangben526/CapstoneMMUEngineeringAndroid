package com.example.nfctest2;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothGatt;
import android.bluetooth.BluetoothGattCallback;
import android.bluetooth.BluetoothGattCharacteristic;
import android.bluetooth.BluetoothManager;
import android.bluetooth.BluetoothProfile;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.net.Uri;
import android.nfc.NdefMessage;
import android.nfc.NdefRecord;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.Toast;

import java.util.UUID;



public class mainactivity_ble_fragment extends Fragment {
    // Please modify it to the correct BLE MAC Address

    public static final String ADDRESS = "B8:27:EB:87:EA:0A";

    public static final String CAPSTONE_SERVICE_UUID = "13333333-3333-3333-3333-333333333337";

    public static final String CAPSTONE_CHARACTERISTICS_UUID = "13333333-3333-3333-3333-333333330001";
    public BluetoothManager bluetoothManager;
    public BluetoothAdapter mBluetoothAdapter;
    public BluetoothDevice device;
    public BluetoothGatt bluetoothGatt;
    public BluetoothGattCharacteristic charCapstone;
    public EditText editText;



    public int i;
    public mainactivity_ble_fragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        bluetoothManager = (BluetoothManager)getActivity().getSystemService(Context.BLUETOOTH_SERVICE);
        mBluetoothAdapter = bluetoothManager.getAdapter();
        getActivity().setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        i=0;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.mainactivity_ble_fragment, container, false);
        editText = (EditText)view.findViewById(R.id.editText);
        view.findViewById(R.id.buttonConnect).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                device = mBluetoothAdapter.getRemoteDevice(ADDRESS);
                //makeToast("CONNECTED");
                bluetoothGatt = device.connectGatt(getActivity(), true, new BluetoothGattCallback() {
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
                    @Override
                    public void onCharacteristicRead(BluetoothGatt gatt, BluetoothGattCharacteristic characteristic, int status) {
                        super.onCharacteristicRead(gatt, characteristic, status);
                        final byte[] values = characteristic.getValue();
                        getActivity().runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                // only 1 byte of data is expected
                                editText.setText(""+values[0]);
                            }
                        });
                    }
                });
                makeToast("Remote Control is turned on");
            }
        });

        /*--------------------Read DEBUG Button-------------------------------------
        view.findViewById(R.id.buttonRead).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                bluetoothGatt.readCharacteristic(charCapstone);
            }
        });
        */

        /*--------------------Write DEBUG Button------------------------------------
        view.findViewById(R.id.buttonWrite).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                byte value = (byte)Integer.parseInt(editText.getText().toString());
                charCapstone.setValue(new byte[]{value});
                bluetoothGatt.writeCharacteristic(charCapstone);
            }
        });
        */

        //--------------------button1 DEBUG---------------------------------------
        view.findViewById(R.id.button1).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                byte value =1;
                charCapstone.setValue(new byte[]{value});
                bluetoothGatt.writeCharacteristic(charCapstone);
                Toast.makeText(getActivity(),"Tutorial Video Played: Spaghetti Bolognese. ",Toast.LENGTH_LONG).show();
            }
        });
        //--------------------button2 DEBUG---------------------------------------
        view.findViewById(R.id.button2).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                byte value =2;
                charCapstone.setValue(new byte[]{value});
                bluetoothGatt.writeCharacteristic(charCapstone);
                Toast.makeText(getActivity(),"Tutorial Video Played: Spaghetti Carbonara.",Toast.LENGTH_LONG).show();
            }
        });
        //--------------------button3 DEBUG---------------------------------------
        view.findViewById(R.id.button3).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                byte value =3;
                charCapstone.setValue(new byte[]{value});
                bluetoothGatt.writeCharacteristic(charCapstone);
                Toast.makeText(getActivity(),"Tutorial Video Played: Spaghetti Marinara.",Toast.LENGTH_LONG).show();
            }
        });
        //--------------------button4 DEBUG---------------------------------------
        view.findViewById(R.id.button4).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                byte value =4;
                charCapstone.setValue(new byte[]{value});
                bluetoothGatt.writeCharacteristic(charCapstone);
                if(i%2==0){
                    Toast.makeText(getActivity(),"Mode Changed to Recipe Viewing Mode ",Toast.LENGTH_LONG).show();
                }
                else {
                    Toast.makeText(getActivity(),"Mode Changed to Video Playing Mode ",Toast.LENGTH_LONG).show();
                }
                i += 1;
            }
        });

        //--------------------button5 DEBUG---------------------------------------
        view.findViewById(R.id.button5).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                byte value =5;
                charCapstone.setValue(new byte[]{value});
                bluetoothGatt.writeCharacteristic(charCapstone);
                Toast.makeText(getActivity(),"The Video has quitted ",Toast.LENGTH_LONG).show();
            }
        });

        //--------------------button6 DEBUG---------------------------------------
        view.findViewById(R.id.button6).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                byte value =6;
                charCapstone.setValue(new byte[]{value});
                bluetoothGatt.writeCharacteristic(charCapstone);
                Toast.makeText(getActivity(),"The video is being played or pause ",Toast.LENGTH_LONG).show();
            }
        });

        return view;
    }


    public void makeToast(String message)
    {
        Toast.makeText(getActivity(), message, Toast.LENGTH_LONG).show();
    }


    @Override
    public void onDestroyView() {
        super.onDestroyView();
       // bluetoothGatt.disconnect();
        i=0;
    }


}