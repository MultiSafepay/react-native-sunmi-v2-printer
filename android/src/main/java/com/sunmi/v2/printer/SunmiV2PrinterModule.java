package com.sunmi.v2.printer;

import android.content.BroadcastReceiver;

import com.facebook.react.bridge.ReactApplicationContext;
import com.facebook.react.bridge.ReactContextBaseJavaModule;
import com.facebook.react.bridge.ReactMethod;
import com.facebook.react.bridge.Promise;

import java.util.Map;

import android.os.RemoteException;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.util.Base64;
import android.graphics.Bitmap;
import android.util.Log;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.content.IntentFilter;


import com.sunmi.peripheral.printer.InnerPrinterCallback;
import com.sunmi.peripheral.printer.InnerPrinterException;
import com.sunmi.peripheral.printer.InnerPrinterManager;
import com.sunmi.peripheral.printer.InnerResultCallback;
import com.sunmi.peripheral.printer.SunmiPrinterService;

class MyResultCallback extends InnerResultCallback {
    private com.facebook.react.bridge.Promise myPromise;
    private String myTag = "MyResultCallback";

    @Override
    public void onRunResult(boolean isSuccess) throws RemoteException {
        Log.i(myTag, "onRunResult: " + isSuccess);
        if (myPromise != null) {
            myPromise.resolve(isSuccess);
            myPromise = null;
        }
    }

    @Override
    public void onReturnString(String result) throws RemoteException {
        Log.i(myTag, "onReturnString: " + result);
        if (myPromise != null) {
            myPromise.resolve(result);
            myPromise = null;
        }
    }

    @Override
    public void onRaiseException(int code, String msg) throws RemoteException {
        Log.i(myTag, "onRaiseException: " + msg);
        if (myPromise != null) {
            myPromise.reject(String.valueOf(code), msg);
            myPromise = null;
        }
    }

    @Override
    public void onPrintResult(int code, String msg) throws RemoteException {
        Log.i(myTag, "onPrintResult: " + msg);
        if (myPromise != null) {
            myPromise.resolve(msg);
            myPromise = null;
        }
    }

    public void setPromise(Promise promise) {
        this.myPromise = promise;
    }
}

public class SunmiV2PrinterModule extends ReactContextBaseJavaModule {
    public static ReactApplicationContext reactApplicationContext = null;
    private BitmapUtils bitMapUtils;
    private PrinterReceiver receiver = new PrinterReceiver();

    public static String noPrinter = "NO_PRINTER_INIT";
    public static int NoSunmiPrinter = 0x00000000;
    public static int CheckSunmiPrinter = 0x00000001;
    public static int FoundSunmiPrinter = 0x00000002;
    public static int LostSunmiPrinter = 0x00000003;

    private MyResultCallback innerResultCallback = new MyResultCallback();

    /**
     *  sunmiPrinter means checking the printer connection status
     */
    public int sunmiPrinter = CheckSunmiPrinter;

    private static SunmiPrintHelper helper = new SunmiPrintHelper();

    public static SunmiPrintHelper getInstance() {
        return helper;
    }

    private ServiceConnection connService = new ServiceConnection() {
        @Override
        public void onServiceDisconnected(ComponentName name) {
            Log.i(TAG, "Service disconnected: " + name);
        }

        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            Log.i(TAG, "Service connected: " + name);

        }
    };

    private static final String TAG = "SunmiV2PrinterModule";

    public SunmiV2PrinterModule(ReactApplicationContext reactContext) {
        super(reactContext);
        reactApplicationContext = reactContext;
        bitMapUtils = new BitmapUtils(reactContext);
        IntentFilter mFilter = new IntentFilter();
        getReactApplicationContext().registerReceiver(receiver, mFilter);
    }

    @Override
    public String getName() {
        return "SunmiV2Printer";
    }

    @ReactMethod
    public void initBind(final Promise promise) {
        try {
            SunmiPrintHelper.getInstance().initSunmiPrinterService(reactApplicationContext);
            promise.resolve(null);
        } catch (Exception e) {
            Log.i(TAG, "ERROR: " + e.getMessage());
            promise.reject("0", e.getMessage());
        }
    }

    @ReactMethod
    public void getPrinterDidBind(final Promise p) {
        try {
            p.resolve(SunmiPrintHelper.getInstance().printerDidBind());
        } catch (Exception e) {
            Log.i(TAG, "ERROR: " + e.getMessage());
            p.reject("0", e.getMessage());
        }
    }

    @ReactMethod
    public void initPrinter(final Promise promise) {
        try {
            SunmiPrintHelper.getInstance().initPrinter();
            promise.resolve(null);
        } catch (Exception e) {
            Log.i(TAG, "ERROR: " + e.getMessage());
            promise.reject("0", e.getMessage());
        }
    }

    @ReactMethod
    public void getPrinterServiceDidInit(final Promise p) {
        try {
            p.resolve(SunmiPrintHelper.getInstance().printerServiceDidInit());
        } catch (Exception e) {
            Log.i(TAG, "ERROR: " + e.getMessage());
            p.reject("0", e.getMessage());
        }
    }

    @ReactMethod
    public void getPrinterStatus(final Promise p) {
         try {
             p.resolve(SunmiPrintHelper.getInstance().getPrinterStatus());
         } catch (Exception e) {
             Log.i(TAG, "ERROR: " + e.getMessage());
             p.reject("0", e.getMessage());
         }
    }

    @ReactMethod
    public void getPrinterVersion(final Promise p) {
         try {
             p.resolve(SunmiPrintHelper.getInstance().getPrinterVersion());
         } catch (Exception e) {
             Log.i(TAG, "ERROR: " + e.getMessage());
             p.reject("0", e.getMessage());
         }
    }

    @ReactMethod
    public void getPrinterPaperSize(final Promise p) {
        try {
            p.resolve(SunmiPrintHelper.getInstance().getPrinterPaper());
        } catch (Exception e) {
            Log.i(TAG, "ERROR: " + e.getMessage());
            p.reject("0", e.getMessage());
        }
    }

    @ReactMethod
    public void getPrinterSpecifications(final Promise p) {
        innerResultCallback.setPromise(p);
        try {
            SunmiPrintHelper.getInstance().getPrinterHead(innerResultCallback);
        } catch (Exception e) {
            Log.i(TAG, "ERROR: " + e.getMessage());
            p.reject("0", e.getMessage());
        }
    }

    @ReactMethod
    public void getPrinterDensity(final Promise p) {
        try {
            p.resolve(SunmiPrintHelper.getInstance().getPrinterDensity());
        } catch (Exception e) {
            Log.i(TAG, "ERROR: " + e.getMessage());
            p.reject("0", e.getMessage());
        }
    }

    @ReactMethod
    public void getServiceVersion(final Promise p) {
        try {
            p.resolve(SunmiPrintHelper.getInstance().getServiceVersion());
        } catch (Exception e) {
            Log.i(TAG, "ERROR: " + e.getMessage());
            p.reject("0", e.getMessage());
        }
    }

    @ReactMethod
    public void getPrinterDistance(final Promise p) {
        innerResultCallback.setPromise(p);
        try {
            SunmiPrintHelper.getInstance().getPrinterDistance(innerResultCallback);
        } catch (Exception e) {
            Log.i(TAG, "ERROR: " + e.getMessage());
            p.reject("0", e.getMessage());
        }
    }

    @ReactMethod
    public void showPrinterStatusToast(final Promise p) {
        try {
            SunmiPrintHelper.getInstance().showPrinterStatus(reactApplicationContext);
            p.resolve(null);
        } catch (Exception e) {
            Log.i(TAG, "ERROR: " + e.getMessage());
            p.reject("0", e.getMessage());
        }
    }

    @ReactMethod
    public void sendRAWData(String base64EncriptedData, final Promise promise) {
        try {
            final byte[] data = Base64.decode(base64EncriptedData, Base64.DEFAULT);
            SunmiPrintHelper.getInstance().sendRawData(data);
            promise.resolve(null);

        } catch (Exception e) {
            Log.i(TAG, "ERROR: " + e.getMessage());
            promise.reject("0", e.getMessage());
        }
    }

    @ReactMethod
    public void setAlignment(int alignment, final Promise promise) {
        try {
            SunmiPrintHelper.getInstance().setAlign(alignment);
            promise.resolve(null);
        } catch (Exception e) {
            Log.i(TAG, "ERROR: " + e.getMessage());
            promise.reject("0", e.getMessage());
        }
    }

    @ReactMethod
    public void printBitmap(String data, int width, int height, final Promise promise) {

        try {
            byte[] decoded = Base64.decode(data, Base64.DEFAULT);
            final Bitmap bitMap = bitMapUtils.decodeBitmap(decoded, width, height);

            SunmiPrintHelper.getInstance().printBitmap(bitMap);

            promise.resolve(null);
        } catch (Exception e) {
            e.printStackTrace();
            promise.reject("0", e.getMessage());
        }
    }

    @ReactMethod
    public void printText(String text, final Promise promise) {
        try {
            SunmiPrintHelper.getInstance().printText(text);
            promise.resolve(null);
        } catch (Exception e) {
            e.printStackTrace();
            promise.reject("0", e.getMessage());
        }
    }

    @ReactMethod
    public void clearBuffer(final Promise promise) {
        try {
            SunmiPrintHelper.getInstance().deInitSunmiPrinterService(reactApplicationContext);
            promise.resolve(null);
        } catch (Exception e) {
            e.printStackTrace();
            promise.reject("0", e.getMessage());
        }
    }

    @ReactMethod
    public void openCashDrawer(final Promise promise) {
        try {
            SunmiPrintHelper.getInstance().openCashBox();
            promise.resolve(null);

        } catch (Exception e) {
            e.printStackTrace();
            promise.reject("0", e.getMessage());

        }
    }

    @ReactMethod
    public void cutPaper(final Promise promise) {
        try {
            SunmiPrintHelper.getInstance().cutpaper();
            promise.resolve(null);
        } catch (Exception e) {
            e.printStackTrace();
            promise.reject("0", e.getMessage());
        }
    }
}
