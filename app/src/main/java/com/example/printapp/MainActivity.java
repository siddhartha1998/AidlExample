package com.example.printapp;

import android.content.ComponentName;
import android.content.Intent;
import android.content.ServiceConnection;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.IBinder;
import android.os.RemoteException;
import android.util.Base64;
import android.util.Log;
import android.widget.Button;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.printapp.Utils.ViewUtils;
import com.example.printapp.service.PrintConnectionService;

import java.io.ByteArrayOutputStream;

import acquire.client_connection.IFewaPayService;

public class MainActivity extends AppCompatActivity {
    private Button bPrint;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        PrintConnectionService.myInstance().init(this);
        PrintConnectionService.myInstance().bindService();
        setContentView(R.layout.activity_main);
        bPrint = findViewById(R.id.btn_print);
        bPrint.setOnClickListener(view -> {
            if (ViewUtils.isFastClick())
                return;

            printCall();
        });
    }

    private void printCall() {
        CreateBitmap createBitmap = new CreateBitmap();
        Bitmap bitmap = createBitmap.getBitmap();
        String base64Image = convertBitmapToBase64(bitmap);
        try {
            PrintConnectionService.myInstance().print(base64Image);
        } catch (RemoteException e) {
            throw new RuntimeException(e);
        }
    }

    public static String convertBitmapToBase64(Bitmap bitmap) {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, baos);
        byte[] imageBytes = baos.toByteArray();
        return Base64.encodeToString(imageBytes, Base64.DEFAULT);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }
}
