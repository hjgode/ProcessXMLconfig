package com.demo.hsm.processxmlconfig;

import android.Manifest;
import android.app.Activity;
import android.app.Application;
import android.app.Dialog;
import android.content.ComponentName;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.database.Cursor;
import android.net.Uri;
import android.nfc.Tag;
import android.os.Build;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.obsez.android.lib.filechooser.ChooserDialog;

import java.io.File;
import java.util.List;

public class processXMLconfig extends Activity {

    EditText txtFile;
    Button btnSelect;
    Button btnProcess;
    Context ctx=this;
    String m_path="";
    final static String TAG="processXMLconfig";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_process_xmlconfig);
        txtFile = (EditText) findViewById(R.id.editFilename);
//        txtFile.setOnKeyListener(new View.OnKeyListener() {
//            @Override
//            public boolean onKey(View v, int keyCode, KeyEvent event) {
//                if (event.getAction() == KeyEvent.ACTION_DOWN)
//                {
//                    //check if the right key was pressed
//                    if (keyCode == KeyEvent.KEYCODE_BACK)
//                    {
//                        return true;
//                    }
//                }
//                return false;
//            }
//        });
        ctx = this;

        btnProcess = (Button) findViewById(R.id.btn_process);
        btnProcess.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //if file
                //send implicit broadcast intent
                Intent intent=new Intent();
                intent.setAction("com.honeywell.ezconfig.intent.action.IMPORT_XML");
                intent.putExtra("path", m_path);
                /*
                //send implicit broadcast intent
                Intent intent=new Intent();
                intent.setAction("com.honeywell.ezconfig.intent.action.IMPORT_XML");
                intent.putExtra("path", "/sdcard/honeywell/persist/DataCollectionService.xm");
                sendBroadcast(intent);
                */
                if (Build.VERSION.SDK_INT > Build.VERSION_CODES.N) {
                    sendImplicitBroadcast(ctx, intent);
                }else{
                    sendBroadcast(intent);
                }
                Log.d(TAG, "sent intent '"+"com.honeywell.ezconfig.intent.action.IMPORT_XML"+"' with path='"+m_path+"'");
                Toast.makeText(getApplicationContext(), "sent intent '"+"com.honeywell.ezconfig.intent.action.IMPORT_XML"+"' with path='"+m_path+"'", Toast.LENGTH_LONG).show();
            }
        });
        /*
        07-19 12:14:20.707 1454-2358/? W/BroadcastQueue: Permission Denial: broadcasting Intent { act=com.honeywell.ezconfig.intent.action.IMPORT_XML flg=0x10 (has extras) } from com.demo.hsm.processxmlconfig (pid=4788, uid=10125) requires android.permission.WRITE_MEDIA_STORAGE due to receiver com.intermec.bugreporter/.EZConfigReceiver
        07-19 12:14:20.724 1454-2331/? I/ActivityManager: Start proc 20550:com.intermec.webinterface:settings/u0a103 for broadcast com.intermec.webinterface/.EZConfigReceiver
        07-19 12:14:20.764 2932-2932/? D/DeviceConfigService: Start DeviceConfig Service ACTION = com.honeywell.ezconfig.intent.action.IMPORT_XML
        07-19 12:14:20.765 2932-2932/? I/ProvisionUtil: updateStatusToSetup taskid = 0, msg = Start to configure DeviceConfig
        07-19 12:14:20.779 2932-2932/? I/DeviceConfigureUpdater: process key = Lock USB Mode, value = 2
        07-19 12:14:20.779 2932-2932/? I/DeviceConfigureUpdater: processMdmOperations: key=Lock USB Mode, value=2
        07-19 12:14:20.780 2932-2932/? I/DeviceConfigureUpdater: bNeedTurnOnWifi=false
        07-19 12:14:20.780 2452-2465/? I/HoneywellProvider: configureEthernet
        07-19 12:14:20.782 2932-2932/? I/ProvisionUtil: updateStatusToSetup taskid = 0, msg = Finish configuring DeviceConfig
        07-19 12:14:20.782 2932-2932/? I/DeviceConfigureUpdater: WIFI CHANGE TO WIFI_STATE_ENABLED, mWifiOpertaionType = 0
        07-19 12:14:21.600 20565-20565/? I/TerminalEmulator: com.honeywell.ezconfig.intent.action.IMPORT_XML
        07-19 12:14:21.782 2932-2932/? I/DeviceConfigureUpdater: Device Config Finished

on.IMPORT_XML flg=0x10 (has extras) } from com.demo.hsm.processxmlconfig (pid=7899, uid=10132) requires
    com.honeywell.provisioner.ACCESS
due to receiver com.honeywell.tools.provisioner/.EZConfigReceiver
11-17 01:04:36.364  1463  2215 W BroadcastQueue: Permission Denial: broadcasting Intent { act=com.honeywell.ezconfig.intent.acti
on.IMPORT_XML flg=0x10 (has extras) } from com.demo.hsm.processxmlconfig (pid=7899, uid=10132) requires
    android.permission.WRITE_MEDIA_STORAGE
due to receiver com.intermec.bugreporter/.EZConfigReceiver

         */

        btnSelect = (Button) findViewById(R.id.btn_select);
        btnSelect.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String _path= Environment.getExternalStorageDirectory().getAbsolutePath();
                new ChooserDialog(ctx)
                        .withFilter(true,"xml")
                        .withStartFile(_path)
                        .withChosenListener(new ChooserDialog.Result() {
                            @Override
                            public void onChoosePath(String path, File pathFile) {
                                Toast.makeText(getApplicationContext(), "FILE: " + path, Toast.LENGTH_LONG).show();
                                txtFile.setText(path);
                                m_path=path;
                            }
                        })
                        .withNavigateUpTo(new ChooserDialog.CanNavigateUp() {
                            @Override
                            public boolean canUpTo(File dir) {
                                return true;
                            }
                        })
                        .withNavigateTo(new ChooserDialog.CanNavigateTo() {
                            @Override
                            public boolean canNavigate(File dir) {
                                return true;
                            }
                        })
                        .withOnCancelListener(new DialogInterface.OnCancelListener(){

                            /**
                             * This method will be invoked when the dialog is canceled.
                             *
                             * @param dialog the dialog that was canceled will be passed into the
                             *               method
                             */
                            @Override
                            public void onCancel(DialogInterface dialog) {
                                Log.d(TAG, "CANCEL");
                                txtFile.setText("");
                            }
                        })
                        .build()
                        .show();
            }
        });
        btnSelect.requestFocus();
        checkPermissions();
    }

    void checkPermissions(){
        if (ContextCompat.checkSelfPermission(processXMLconfig.this, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            // Permission is not granted
            // Should we show an explanation?
            if (ActivityCompat.shouldShowRequestPermissionRationale(processXMLconfig.this, Manifest.permission.WRITE_EXTERNAL_STORAGE)) {
                // Show an explanation to the user *asynchronously* -- don't block
                // this thread waiting for the user's response! After the user
                // sees the explanation, try again to request the permission.

            } else {
                // No explanation needed; request the permission
                // Permission is not granted
                requestPermissions(new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, 99);
            }
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        switch (requestCode) {
            case 99: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    // permission was granted, yay! Do the
                    // contacts-related task you need to do.
                } else {
                    // permission denied, boo! Disable the
                    // functionality that depends on this permission.
                }
                return;
            }

            // other 'case' lines to check for other
            // permissions this app might request.
        }
    }

    @Override
    public void onBackPressed() {
        // do something on back.
        Log.d(TAG, "onBackPressed");
        return;
    }
    @Override
    public boolean onKeyLongPress(int keyCode, KeyEvent keyEvent){
        Log.d(TAG, "onKeyLongPress: " + keyEvent.getKeyCode());
        if(keyCode==KeyEvent.KEYCODE_BACK){
            Log.d(TAG, "onKeyLongPress: "+"BACK keycode");
        }
        return true; //handled
    }

    private static void sendImplicitBroadcast(Context ctxt, Intent i) {
        PackageManager pm=ctxt.getPackageManager();
        List<ResolveInfo> matches=pm.queryBroadcastReceivers(i, 0);
        for (ResolveInfo resolveInfo : matches) {
            Intent explicit=new Intent(i);
            ComponentName cn=
                    new ComponentName(resolveInfo.activityInfo.applicationInfo.packageName,
                            resolveInfo.activityInfo.name);
            explicit.setComponent(cn);
            ctxt.sendBroadcast(explicit);
            Log.d(TAG, "sendImplicitBroadcast "+cn.toString());
        }
        /*
        09-06 12:52:09.098  1606  1622 W BroadcastQueue: Permission Denial: broadcasting Intent { act=com.honeywell.ezconfig.intent.action.IMPORT_XML flg=0x10
        cmp=com.honeywell.tools.ssclient/.EZConfigReceiver (has extras) }
        from com.demo.hsm.processxmlconfig (pid=7518, uid=10123) requires android.permission.WRITE_MEDIA_STORAGE due to receiver com.honeywell.tools.ssclient/.EZConfigReceiver
         */
    }
}
