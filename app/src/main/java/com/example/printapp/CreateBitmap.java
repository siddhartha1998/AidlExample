package com.example.printapp;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Paint;
import android.text.TextUtils;

import com.example.printapp.constants.PrintSize;

import java.io.IOException;

public class CreateBitmap {
    public Bitmap getBitmap() {
        BitmapDraw bitmapDraw = new BitmapDraw();
        bitmapDraw.text("Print Test", PrintSize.TYPE, true, Paint.Align.CENTER);
        // bitmapDraw.text(water(), PrintSize.TYPE, true, Paint.Align.CENTER);
        bitmapDraw.text("Name: Imark", null, PrintSize.SMALL, false);
        bitmapDraw.text("Address: Dillibazar, Kathmandu", null, PrintSize.SMALL, false);
        bitmapDraw.text("Number: 01-*******",null,PrintSize.SMALL, false);

        bitmapDraw.text("-------------x----------------x-------------", 30, false, Paint.Align.CENTER);
        return bitmapDraw.getBitmap();
    }
}
