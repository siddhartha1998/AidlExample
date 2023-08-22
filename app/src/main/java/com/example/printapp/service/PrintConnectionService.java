package com.example.printapp.service;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Handler;
import android.os.IBinder;
import android.os.RemoteException;
import android.util.Log;

import acquire.client_connection.IFewaPayService;

public class PrintConnectionService implements ServiceConnection {
    private static final String INTENT_ACTION = "acquire.client_connection.ClientConnectionService";
    private static final String PACKAGE_NAME = "com.ingenico.template";
    private static final String TAG = "ServiceCallHelper";
    private static volatile PrintConnectionService instance;

    private int retry = 0;
    private static final int MAX_RETRY_COUNT = 3;
    private static final long RETRY_INTERVALS = 3000;
    private IFewaPayService iFewaPayService;
    private Context context;

    public void init(Context context) {
        this.context = context;
    }

    public static PrintConnectionService myInstance() {
        if (instance == null) {
            synchronized (PrintConnectionService.class) {
                if (instance == null) {
                    instance = new PrintConnectionService();
                }
            }
        }
        return instance;
    }
    @Override
    public void onServiceConnected(ComponentName componentName, IBinder service) {
        Log.d(TAG, "=> onServiceConnected");
        retry = 0;
        iFewaPayService = IFewaPayService.Stub.asInterface(service);
    }

    @Override
    public void onServiceDisconnected(ComponentName componentName) {
        iFewaPayService = null;
        Log.d(TAG,"TMS Call service disconnected");
    }

    public void bindService() {
        Intent service = new Intent(INTENT_ACTION);
        service.setPackage(PACKAGE_NAME);
        boolean bindSucc = context.bindService(service, myInstance(), context.BIND_AUTO_CREATE);

        if (!bindSucc && retry++ < MAX_RETRY_COUNT) {
            Log.e(TAG, "=> bind fail, rebind (" + retry + ")");
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    bindService();
                }
            }, RETRY_INTERVALS);
        }
    }

    public boolean print(String base64Image) throws RemoteException {
        if (iFewaPayService == null) {
            bindService();
            return false;
        }
        try {
            return iFewaPayService.startPrinting(base64Image);
        } catch (RemoteException e) {
            return false;
        }
    }
}
