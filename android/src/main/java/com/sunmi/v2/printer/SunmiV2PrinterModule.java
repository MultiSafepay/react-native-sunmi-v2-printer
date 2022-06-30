package com.sunmi.v2.printer;

import android.content.BroadcastReceiver;

import com.facebook.react.bridge.Callback;
import com.facebook.react.bridge.NativeModule;
import com.facebook.react.bridge.ReactApplicationContext;
import com.facebook.react.bridge.ReactContext;
import com.facebook.react.bridge.ReactContextBaseJavaModule;
import com.facebook.react.bridge.ReactMethod;
import com.facebook.react.bridge.ReadableArray;
import com.facebook.react.bridge.Promise;
import com.facebook.react.modules.core.DeviceEventManagerModule;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.Map;
import java.util.Map;
import java.util.HashMap;

// import woyou.aidlservice.jiuiv5.IWoyouService;
// import woyou.aidlservice.jiuiv5.ICallback;
import android.os.RemoteException;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.util.Base64;
import android.graphics.Bitmap;
import android.util.Log;
import android.content.ComponentName;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.ServiceConnection;
import android.content.IntentFilter;
import android.widget.Toast;

import com.sunmi.peripheral.printer.ExceptionConst;
import com.sunmi.peripheral.printer.InnerLcdCallback;
import com.sunmi.peripheral.printer.InnerPrinterCallback;
import com.sunmi.peripheral.printer.InnerPrinterException;
import com.sunmi.peripheral.printer.InnerPrinterManager;
import com.sunmi.peripheral.printer.InnerResultCallback;
import com.sunmi.peripheral.printer.SunmiPrinterService;
import com.sunmi.peripheral.printer.WoyouConsts;

public class SunmiV2PrinterModule extends ReactContextBaseJavaModule {
    public static ReactApplicationContext reactApplicationContext = null;
    private BitmapUtils bitMapUtils;
    private PrinterReceiver receiver = new PrinterReceiver();

    public static String noPrinter = "NO_PRINTER_INIT";
    public static int NoSunmiPrinter = 0x00000000;
    public static int CheckSunmiPrinter = 0x00000001;
    public static int FoundSunmiPrinter = 0x00000002;
    public static int LostSunmiPrinter = 0x00000003;

    /**
     *  sunmiPrinter means checking the printer connection status
     */
    public int sunmiPrinter = CheckSunmiPrinter;
    /**
     *  SunmiPrinterService for API
     */
    private SunmiPrinterService sunmiPrinterService;

    private static SunmiPrintHelper helper = new SunmiPrintHelper();

    public static SunmiPrintHelper getInstance() {
        return helper;
    }

    /*
    private InnerPrinterCallback innerPrinterCallback = new InnerPrinterCallback() {
        @Override
        protected void onConnected(SunmiPrinterService service) {
            sunmiPrinterService = service;
            checkSunmiPrinterService(service);
        }

        @Override
        protected void onDisconnected() {
            sunmiPrinterService = null;
            sunmiPrinter = LostSunmiPrinter;
        }
    };
     */

    // public final static String OUT_OF_PAPER_ACTION = "woyou.aidlservice.jiuv5.OUT_OF_PAPER_ACTION";
    // public final static String ERROR_ACTION = "woyou.aidlservice.jiuv5.ERROR_ACTION";
    // public final static String NORMAL_ACTION = "woyou.aidlservice.jiuv5.NORMAL_ACTION";
    // public final static String COVER_OPEN_ACTION = "woyou.aidlservice.jiuv5.COVER_OPEN_ACTION";
    // public final static String COVER_ERROR_ACTION = "woyou.aidlservice.jiuv5.COVER_ERROR_ACTION";
    // public final static String KNIFE_ERROR_1_ACTION = "woyou.aidlservice.jiuv5.KNIFE_ERROR_ACTION_1";
    // public final static String KNIFE_ERROR_2_ACTION = "woyou.aidlservice.jiuv5.KNIFE_ERROR_ACTION_2";
    // public final static String OVER_HEATING_ACITON = "woyou.aidlservice.jiuv5.OVER_HEATING_ACITON";
    // public final static String FIRMWARE_UPDATING_ACITON = "woyou.aidlservice.jiuv5.FIRMWARE_UPDATING_ACITON";
    
    private ServiceConnection connService = new ServiceConnection() {
        @Override
        public void onServiceDisconnected(ComponentName name) {
            Log.i(TAG, "Service disconnected: " + name);
            // woyouService = null;
        }

        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            Log.i(TAG, "Service connected: " + name);
            // woyouService = IWoyouService.Stub.asInterface(service);
        }
    };

    private static final String TAG = "SunmiV2PrinterModule";

    public SunmiV2PrinterModule(ReactApplicationContext reactContext) {
        super(reactContext);
        reactApplicationContext = reactContext;
        //Intent intent = new Intent();
        // intent.setPackage("woyou.aidlservice.jiuiv5");
        // intent.setAction("woyou.aidlservice.jiuiv5.IWoyouService");
        //reactContext.startService(intent);
        //reactContext.bindService(intent, connService, Context.BIND_AUTO_CREATE);
        bitMapUtils = new BitmapUtils(reactContext);
        IntentFilter mFilter = new IntentFilter();
        // mFilter.addAction(OUT_OF_PAPER_ACTION);
        // mFilter.addAction(ERROR_ACTION);
        // mFilter.addAction(NORMAL_ACTION);
        // mFilter.addAction(COVER_OPEN_ACTION);
        // mFilter.addAction(COVER_ERROR_ACTION);
        // mFilter.addAction(KNIFE_ERROR_1_ACTION);
        // mFilter.addAction(KNIFE_ERROR_2_ACTION);
        // mFilter.addAction(OVER_HEATING_ACITON);
        // mFilter.addAction(FIRMWARE_UPDATING_ACITON);
        getReactApplicationContext().registerReceiver(receiver, mFilter);
    }

    @Override
    public String getName() {
        return "SunmiV2Printer";
    }


    /*
    @Override
    public Map<String, Object> getConstants() {
        final Map<String, Object> constants = new HashMap<>();
        final Map<String, Object> constantsChildren = new HashMap<>();


        constants.put("Constants", constantsChildren);

        constants.put("hasPrinter", hasPrinter());

        try {
            constants.put("printerVersion", getPrinterVersion());
        } catch (Exception e) {
            // Log and ignore for it is not the madatory constants.
            Log.i(TAG, "ERROR: " + e.getMessage());
        }
        try {
            constants.put("printerSerialNo", getPrinterSerialNo());
        } catch (Exception e) {
            // Log and ignore for it is not the madatory constants.
            Log.i(TAG, "ERROR: " + e.getMessage());
        }
        try {
            constants.put("printerModal", getPrinterModal());
        } catch (Exception e) {
            // Log and ignore for it is not the madatory constants.
            Log.i(TAG, "ERROR: " + e.getMessage());
        }

        return constants;
    }
    */

    /**
     * Initialize the printer, reset the logic program of the printer, but do not clear the buffer data, so
     * Incomplete print jobs will continue after reset
     *
     * @return
     */    
    @ReactMethod
    public void printerInit(final Promise promise) {
        SunmiPrintHelper.getInstance().initSunmiPrinterService(reactApplicationContext);
        SunmiPrintHelper.getInstance().initPrinter();
        promise.resolve(null);
    }

    /**
     * Printer self-test, the printer will print a self-test page
     *
     * @param callback resault callback
     */
    // @ReactMethod
    // public void printerSelfChecking(final Promise p) {
    //     // final IWoyouService printerService = woyouService;
    //     ThreadPoolManager.getInstance().executeTask(new Runnable() {
    //         @Override
    //         public void run() {
    //             try {
    //                 printerService.printerSelfChecking(new ICallback.Stub() {
    //                     @Override
    //                     public void onPrintResult(int par1, String par2) {
    //                         Log.d(TAG, "ON PRINT RES: " + par1 + ", " + par2);
    //                     }

    //                     @Override
    //                     public void onRunResult(boolean isSuccess) {
    //                         if (isSuccess) {
    //                             p.resolve(null);
    //                         } else {
    //                             p.reject("0", isSuccess + "");
    //                         }
    //                     }

    //                     @Override
    //                     public void onReturnString(String result) {
    //                         p.resolve(result);
    //                     }

    //                     @Override
    //                     public void onRaiseException(int code, String msg) {
    //                         p.reject("" + code, msg);
    //                     }
    //                 });
    //             } catch (Exception e) {
    //                 e.printStackTrace();
    //                 Log.i(TAG, "ERROR: " + e.getMessage());
    //                 p.reject("" + 0, e.getMessage());
    //             }
    //         }
    //     });
    // }

    /**
     * Get the printer board serial number
     */
    // @ReactMethod
    // public void getPrinterSerialNo(final Promise p) {
    //     try {
    //         p.resolve(getPrinterSerialNo());
    //     } catch (Exception e) {
    //         Log.i(TAG, "ERROR: " + e.getMessage());
    //         p.reject("" + 0, e.getMessage());
    //     }
    // }

    // private String getPrinterSerialNo() throws Exception {
    //     // final IWoyouService printerService = woyouService;
    //     return printerService.getPrinterSerialNo();
    // }

    /**
     * Get the printer firmware version number
     */
     @ReactMethod
     public void getPrinterVersion(final Promise p) {
         try {
             p.resolve(SunmiPrintHelper.getInstance().getPrinterVersion());
         } catch (Exception e) {
             Log.i(TAG, "ERROR: " + e.getMessage());
             p.reject("" + 0, e.getMessage());
         }
     }

    /**
     * Get the printer paper size
     */
    @ReactMethod
    public void getPrinterPaperSize(final Promise p) {
        try {
            p.resolve(SunmiPrintHelper.getInstance().getPrinterPaper());
        } catch (Exception e) {
            Log.i(TAG, "ERROR: " + e.getMessage());
            p.reject("" + 0, e.getMessage());
        }
    }

    // private String getPrinterVersion() throws Exception {
    //     // final IWoyouService printerService = woyouService;
    //     return printerService.getPrinterVersion();
    // }

    /**
     * Get the printer model
     */
    // @ReactMethod
    // public void getPrinterModal(final Promise p) {
    //     try {
    //         p.resolve(getPrinterModal());
    //     } catch (Exception e) {
    //         Log.i(TAG, "ERROR: " + e.getMessage());
    //         p.reject("" + 0, e.getMessage());
    //     }
    // }

    // private String getPrinterModal() throws Exception {
    //     //Caution: This method is not fully test -- Januslo 2018-08-11
    //     // final IWoyouService printerService = woyouService;
    //     return printerService.getPrinterModal();
    // }

    // @ReactMethod
    // public void hasPrinter(final Promise p) {
    //     try {
    //         p.resolve(hasPrinter());
    //     } catch (Exception e) {
    //         Log.i(TAG, "ERROR: " + e.getMessage());
    //         p.reject("" + 0, e.getMessage());
    //     }
    // }

    /**
     * Is there a printer service
     * return {boolean}
     */
    // private boolean hasPrinter() {
    //     // final IWoyouService printerService = woyouService;
    //     final boolean hasPrinterService = printerService != null;
    //     return hasPrinterService;
    // }
    /**
     * Get print head print length
     */
    // @ReactMethod
    // public void getPrintedLength(final Promise p) {
    //     // final IWoyouService printerService = woyouService;
    //     ThreadPoolManager.getInstance().executeTask(new Runnable() {
    //         @Override
    //         public void run() {
    //             try {
    //                 printerService.getPrintedLength(new ICallback.Stub() {
    //                     @Override
    //                     public void onPrintResult(int par1, String par2) {
    //                         Log.d(TAG, "ON PRINT RES: " + par1 + ", " + par2);
    //                     }

    //                     @Override
    //                     public void onRunResult(boolean isSuccess) {
    //                         if (isSuccess) {
    //                             p.resolve(null);
    //                         } else {
    //                             p.reject("0", isSuccess + "");
    //                         }
    //                     }

    //                     @Override
    //                     public void onReturnString(String result) {
    //                         p.resolve(result);
    //                     }

    //                     @Override
    //                     public void onRaiseException(int code, String msg) {
    //                         p.reject("" + code, msg);
    //                     }
    //                 });
    //             } catch (Exception e) {
    //                 e.printStackTrace();
    //                 Log.i(TAG, "ERROR: " + e.getMessage());
    //                 p.reject("" + 0, e.getMessage());
    //             }
    //         }
    //     });
    // }

    // @ReactMethod
    // public void lineWrap(int n, final Promise p) {
    //     // final IWoyouService ss = woyouService;
    //     final int count = n;
    //     ThreadPoolManager.getInstance().executeTask(new Runnable() {
    //         @Override
    //         public void run() {
    //             try {
    //                 ss.lineWrap(count, new ICallback.Stub() {
    //                     @Override
    //                     public void onPrintResult(int par1, String par2) {
    //                         Log.d(TAG, "ON PRINT RES: " + par1 + ", " + par2);
    //                     }

    //                     @Override
    //                     public void onRunResult(boolean isSuccess) {
    //                         if (isSuccess) {
    //                             p.resolve(null);
    //                         } else {
    //                             p.reject("0", isSuccess + "");
    //                         }
    //                     }

    //                     @Override
    //                     public void onReturnString(String result) {
    //                         p.resolve(result);
    //                     }

    //                     @Override
    //                     public void onRaiseException(int code, String msg) {
    //                         p.reject("" + code, msg);
    //                     }
    //                 });
    //             } catch (Exception e) {
    //                 e.printStackTrace();
    //                 Log.i(TAG, "ERROR: " + e.getMessage());
    //                 p.reject("" + 0, e.getMessage());
    //             }
    //         }
    //     });
    // }

    @ReactMethod
    public void sendRAWData(String base64EncriptedData, final Promise promise) {
        try {
            final byte[] data = Base64.decode(base64EncriptedData, Base64.DEFAULT);
            SunmiPrintHelper.getInstance().sendRawData(data);
            promise.resolve(null);

        } catch (Exception e) {
            Log.i(TAG, "ERROR: " + e.getMessage());
            promise.reject("" + 0, e.getMessage());
        }
    }

    @ReactMethod
    public void setAlignment(int alignment, final Promise promise) {
        /*
        if(sunmiPrinterService == null){
            promise.reject("" + 0, noPrinter);
            return;
        }
        try {
            sunmiPrinterService.setAlignment(alignment, null);
            promise.resolve(null);
        } catch (RemoteException e) {
            handleRemoteException(e);
            promise.reject("" + 0, e.getMessage());
        }
         */
        try {
            SunmiPrintHelper.getInstance().setAlign(alignment);
            promise.resolve(null);
        } catch (Exception e) {
            promise.reject("" + 0, e.getMessage());
        }
    }

    // @ReactMethod
    // public void setFontName(String typeface, final Promise p) {
    //     // final IWoyouService ss = woyouService;
    //     final String tf = typeface;
    //     ThreadPoolManager.getInstance().executeTask(new Runnable() {
    //         @Override
    //         public void run() {
    //             try {
    //                 ss.setFontName(tf, new ICallback.Stub() {
    //                     @Override
    //                     public void onPrintResult(int par1, String par2) {
    //                         Log.d(TAG, "ON PRINT RES: " + par1 + ", " + par2);
    //                     }

    //                     @Override
    //                     public void onRunResult(boolean isSuccess) {
    //                         if (isSuccess) {
    //                             p.resolve(null);
    //                         } else {
    //                             p.reject("0", isSuccess + "");
    //                         }
    //                     }

    //                     @Override
    //                     public void onReturnString(String result) {
    //                         p.resolve(result);
    //                     }

    //                     @Override
    //                     public void onRaiseException(int code, String msg) {
    //                         p.reject("" + code, msg);
    //                     }
    //                 });
    //             } catch (Exception e) {
    //                 e.printStackTrace();
    //                 Log.i(TAG, "ERROR: " + e.getMessage());
    //                 p.reject("" + 0, e.getMessage());
    //             }
    //         }
    //     });
    // }

    // @ReactMethod
    // public void setFontSize(float fontsize, final Promise p) {
    //     // final IWoyouService ss = woyouService;
    //     final float fs = fontsize;
    //     ThreadPoolManager.getInstance().executeTask(new Runnable() {
    //         @Override
    //         public void run() {
    //             try {
    //                 ss.setFontSize(fs, new ICallback.Stub() {
    //                     @Override
    //                     public void onPrintResult(int par1, String par2) {
    //                         Log.d(TAG, "ON PRINT RES: " + par1 + ", " + par2);
    //                     }

    //                     @Override
    //                     public void onRunResult(boolean isSuccess) {
    //                         if (isSuccess) {
    //                             p.resolve(null);
    //                         } else {
    //                             p.reject("0", isSuccess + "");
    //                         }
    //                     }

    //                     @Override
    //                     public void onReturnString(String result) {
    //                         p.resolve(result);
    //                     }

    //                     @Override
    //                     public void onRaiseException(int code, String msg) {
    //                         p.reject("" + code, msg);
    //                     }
    //                 });
    //             } catch (Exception e) {
    //                 e.printStackTrace();
    //                 Log.i(TAG, "ERROR: " + e.getMessage());
    //                 p.reject("" + 0, e.getMessage());
    //             }
    //         }
    //     });
    // }


    // @ReactMethod
    // public void printTextWithFont(String text, String typeface, float fontsize, final Promise p) {
    //     // final IWoyouService ss = woyouService;
    //     final String txt = text;
    //     final String tf = typeface;
    //     final float fs = fontsize;
    //     ThreadPoolManager.getInstance().executeTask(new Runnable() {
    //         @Override
    //         public void run() {
    //             try {
    //                 ss.printTextWithFont(txt, tf, fs, new ICallback.Stub() {
    //                     @Override
    //                     public void onPrintResult(int par1, String par2) {
    //                         Log.d(TAG, "ON PRINT RES: " + par1 + ", " + par2);
    //                     }

    //                     @Override
    //                     public void onRunResult(boolean isSuccess) {
    //                         if (isSuccess) {
    //                             p.resolve(null);
    //                         } else {
    //                             p.reject("0", isSuccess + "");
    //                         }
    //                     }

    //                     @Override
    //                     public void onReturnString(String result) {
    //                         p.resolve(result);
    //                     }

    //                     @Override
    //                     public void onRaiseException(int code, String msg) {
    //                         p.reject("" + code, msg);
    //                     }
    //                 });
    //             } catch (Exception e) {
    //                 e.printStackTrace();
    //                 Log.i(TAG, "ERROR: " + e.getMessage());
    //                 p.reject("" + 0, e.getMessage());
    //             }
    //         }
    //     });
    // }

    // @ReactMethod
    // public void printColumnsText(ReadableArray colsTextArr, ReadableArray colsWidthArr, ReadableArray colsAlign, final Promise p) {
    //     // final IWoyouService ss = woyouService;
    //     final String[] clst = new String[colsTextArr.size()];
    //     for (int i = 0; i < colsTextArr.size(); i++) {
    //         clst[i] = colsTextArr.getString(i);
    //     }
    //     final int[] clsw = new int[colsWidthArr.size()];
    //     for (int i = 0; i < colsWidthArr.size(); i++) {
    //         clsw[i] = colsWidthArr.getInt(i);
    //     }
    //     final int[] clsa = new int[colsAlign.size()];
    //     for (int i = 0; i < colsAlign.size(); i++) {
    //         clsa[i] = colsAlign.getInt(i);
    //     }
    //     ThreadPoolManager.getInstance().executeTask(new Runnable() {
    //         @Override
    //         public void run() {
    //             try {
    //                 ss.printColumnsText(clst, clsw, clsa, new ICallback.Stub() {
    //                     @Override
    //                     public void onPrintResult(int par1, String par2) {
    //                         Log.d(TAG, "ON PRINT RES: " + par1 + ", " + par2);
    //                     }

    //                     @Override
    //                     public void onRunResult(boolean isSuccess) {
    //                         if (isSuccess) {
    //                             p.resolve(null);
    //                         } else {
    //                             p.reject("0", isSuccess + "");
    //                         }
    //                     }

    //                     @Override
    //                     public void onReturnString(String result) {
    //                         p.resolve(result);
    //                     }

    //                     @Override
    //                     public void onRaiseException(int code, String msg) {
    //                         p.reject("" + code, msg);
    //                     }
    //                 });
    //             } catch (Exception e) {
    //                 e.printStackTrace();
    //                 Log.i(TAG, "ERROR: " + e.getMessage());
    //                 p.reject("" + 0, e.getMessage());
    //             }
    //         }
    //     });
    // }


    @ReactMethod
    public void printBitmap(String data, int width, int height, final Promise promise) {
        /*
        if(sunmiPrinterService == null){
            promise.reject("" + 0, noPrinter);
            return;
        }

        try {
            byte[] decoded = Base64.decode(data, Base64.DEFAULT);
            final Bitmap bitMap = bitMapUtils.decodeBitmap(decoded, width, height);

            sunmiPrinterService.printBitmap(bitMap, null);
            sunmiPrinterService.printText(" \n", null);

            promise.resolve(null);
        } catch (RemoteException e) {
            e.printStackTrace();
            promise.reject("" + 0, e.getMessage());
        }
         */
        try {
            byte[] decoded = Base64.decode(data, Base64.DEFAULT);
            final Bitmap bitMap = bitMapUtils.decodeBitmap(decoded, width, height);

            SunmiPrintHelper.getInstance().printBitmap(bitMap);

            promise.resolve(null);
        } catch (Exception e) {
            e.printStackTrace();
            promise.reject("" + 0, e.getMessage());
        }
    }

    // @ReactMethod
    // public void printBarCode(String data, int symbology, int height, int width, int textposition, final Promise p) {
    //     // final IWoyouService ss = woyouService;
    //     Log.i(TAG, "come: ss:" + ss);
    //     final String d = data;
    //     final int s = symbology;
    //     final int h = height;
    //     final int w = width;
    //     final int tp = textposition;

    //     ThreadPoolManager.getInstance().executeTask(new Runnable() {
    //         @Override
    //         public void run() {
    //             try {
    //                 ss.printBarCode(d, s, h, w, tp, new ICallback.Stub() {
    //                     @Override
    //                     public void onPrintResult(int par1, String par2) {
    //                         Log.d(TAG, "ON PRINT RES: " + par1 + ", " + par2);
    //                     }

    //                     @Override
    //                     public void onRunResult(boolean isSuccess) {
    //                         if (isSuccess) {
    //                             p.resolve(null);
    //                         } else {
    //                             p.reject("0", isSuccess + "");
    //                         }
    //                     }

    //                     @Override
    //                     public void onReturnString(String result) {
    //                         p.resolve(result);
    //                     }

    //                     @Override
    //                     public void onRaiseException(int code, String msg) {
    //                         p.reject("" + code, msg);
    //                     }
    //                 });
    //             } catch (Exception e) {
    //                 e.printStackTrace();
    //                 Log.i(TAG, "ERROR: " + e.getMessage());
    //                 p.reject("" + 0, e.getMessage());
    //             }
    //         }
    //     });
    // }

    // @ReactMethod
    // public void printQRCode(String data, int modulesize, int errorlevel, final Promise p) {
    //     // final IWoyouService ss = woyouService;
    //     Log.i(TAG, "come: ss:" + ss);
    //     final String d = data;
    //     final int size = modulesize;
    //     final int level = errorlevel;
    //     ThreadPoolManager.getInstance().executeTask(new Runnable() {
    //         @Override
    //         public void run() {
    //             try {
    //                 ss.printQRCode(d, size, level, new ICallback.Stub() {
    //                     @Override
    //                     public void onPrintResult(int par1, String par2) {
    //                         Log.d(TAG, "ON PRINT RES: " + par1 + ", " + par2);
    //                     }

    //                     @Override
    //                     public void onRunResult(boolean isSuccess) {
    //                         if (isSuccess) {
    //                             p.resolve(null);
    //                         } else {
    //                             p.reject("0", isSuccess + "");
    //                         }
    //                     }

    //                     @Override
    //                     public void onReturnString(String result) {
    //                         p.resolve(result);
    //                     }

    //                     @Override
    //                     public void onRaiseException(int code, String msg) {
    //                         p.reject("" + code, msg);
    //                     }
    //                 });
    //             } catch (Exception e) {
    //                 e.printStackTrace();
    //                 Log.i(TAG, "ERROR: " + e.getMessage());
    //                 p.reject("" + 0, e.getMessage());
    //             }
    //         }
    //     });
    // }

    @ReactMethod
    public void printOriginalText(String text, final Promise promise) {
        /*
        if(sunmiPrinterService == null){
            promise.reject("" + 0, noPrinter);
            return;
        }

        try {
            final int size = text.length();
            sunmiPrinterService.printTextWithFont(text, typeface, size, null);

        } catch (Exception e) {
            e.printStackTrace();
            Log.i(TAG, "ERROR: " + e.getMessage());
            promise.reject("" + 0, e.getMessage());
        }
         */
        final int size = text.length();
        SunmiPrintHelper.getInstance().printText(text, size, false, false, null);
        Log.e("SDK-DEBUG", "Printing: "+text);
    }

    // @ReactMethod
    // public void commitPrinterBuffer() {
    //     // final IWoyouService ss = woyouService;
    //     Log.i(TAG, "come: commit buffter ss:" + ss);
    //     ThreadPoolManager.getInstance().executeTask(new Runnable() {
    //         @Override
    //         public void run() {
    //             try {
    //                 ss.commitPrinterBuffer();
    //             } catch (Exception e) {
    //                 e.printStackTrace();
    //                 Log.i(TAG, "ERROR: " + e.getMessage());
    //             }
    //         }
    //     });
    // }

    // @ReactMethod
    // public void enterPrinterBuffer(boolean clean) {
    //     // final IWoyouService ss = woyouService;
    //     Log.i(TAG, "come: " + clean + " ss:" + ss);
    //     final boolean c = clean;
    //     ThreadPoolManager.getInstance().executeTask(new Runnable() {
    //         @Override
    //         public void run() {
    //             try {
    //                 ss.enterPrinterBuffer(c);
    //             } catch (Exception e) {
    //                 e.printStackTrace();
    //                 Log.i(TAG, "ERROR: " + e.getMessage());
    //             }
    //         }
    //     });
    // }

    // @ReactMethod
    // public void exitPrinterBuffer(boolean commit) {
    //     // final IWoyouService ss = woyouService;
    //     Log.i(TAG, "come: " + commit + " ss:" + ss);
    //     final boolean com = commit;
    //     ThreadPoolManager.getInstance().executeTask(new Runnable() {
    //         @Override
    //         public void run() {
    //             try {
    //                 ss.exitPrinterBuffer(com);
    //             } catch (Exception e) {
    //                 e.printStackTrace();
    //                 Log.i(TAG, "ERROR: " + e.getMessage());
    //             }
    //         }
    //     });
    // }


    // @ReactMethod
    // public void printString(String message, final Promise p) {
    //     // final IWoyouService ss = woyouService;
    //     Log.i(TAG, "come: " + message + " ss:" + ss);
    //     final String msgs = message;
    //     ThreadPoolManager.getInstance().executeTask(new Runnable() {
    //         @Override
    //         public void run() {
    //             try {
    //                 ss.printText(msgs, new ICallback.Stub() {
    //                     @Override
    //                     public void onPrintResult(int par1, String par2) {
    //                         Log.d(TAG, "ON PRINT RES: " + par1 + ", " + par2);
    //                     }

    //                     @Override
    //                     public void onRunResult(boolean isSuccess) {
    //                         if (isSuccess) {
    //                             p.resolve(null);
    //                         } else {
    //                             p.reject("0", isSuccess + "");
    //                         }
    //                     }

    //                     @Override
    //                     public void onReturnString(String result) {
    //                         p.resolve(result);
    //                     }

    //                     @Override
    //                     public void onRaiseException(int code, String msg) {
    //                         p.reject("" + code, msg);
    //                     }
    //                 });
    //             } catch (Exception e) {
    //                 e.printStackTrace();
    //                 Log.i(TAG, "ERROR: " + e.getMessage());
    //                 p.reject("" + 0, e.getMessage());
    //             }
    //         }
    //     });
    // }

    @ReactMethod
    public void clearBuffer(final Promise promise) {
        try {
            SunmiPrintHelper.getInstance().deInitSunmiPrinterService(reactApplicationContext);
            promise.resolve(null);
        } catch (Exception e) {
            e.printStackTrace();
            promise.reject("" + 0, e.getMessage());
        }
    }

    // @ReactMethod
    // public void exitPrinterBufferWithCallback(final boolean commit, final Callback callback) {
    //     // final IWoyouService ss = woyouService;
    //     ThreadPoolManager.getInstance().executeTask(new Runnable() {
    //         @Override
    //         public void run() {
    //             try {
    //                 ss.exitPrinterBufferWithCallback(commit, new ICallback.Stub() {
    //                     @Override
    //                     public void onPrintResult(int code, String msg) {
    //                         Log.d(TAG, "ON PRINT RES: " + code + ", " + msg);
    //                         if (code == 0)
    //                             callback.invoke(true);
    //                         else
    //                             callback.invoke(false);
    //                     }

    //                     @Override
    //                     public void onRunResult(boolean isSuccess) {
    //                         callback.invoke(isSuccess);
    //                     }

    //                     @Override
    //                     public void onReturnString(String result) {
    //                         // callback.invoke(isSuccess);
    //                     }

    //                     @Override
    //                     public void onRaiseException(int code, String msg) {
    //                         callback.invoke(false);
    //                     }
    //                 });
    //             } catch (Exception e) {
    //                 e.printStackTrace();
    //                 Log.i(TAG, "ERROR: " + e.getMessage());
    //                 callback.invoke(false);
    //             }
    //         }
    //     });
    // }

    @ReactMethod
    public void openCashDrawer(final Promise promise) {
        if(sunmiPrinterService == null){
            promise.reject("" + 0, noPrinter);
            return;
        }

        try {
            sunmiPrinterService.openDrawer(null);
            promise.resolve(null);

        } catch (RemoteException e) {
            handleRemoteException(e);
            promise.reject("" + 0, e.getMessage());

        }
    }
    @ReactMethod
    public void cutPaper(final Promise promise) {
        SunmiPrintHelper.getInstance().cutpaper();
        promise.resolve(null);
    }

    private void handleRemoteException(RemoteException e){
        //TODO process when get one exception
    }

    /**
     * Check the printer connection,
     * like some devices do not have a printer but need to be connected to the cash drawer through a print service
     */
    private void checkSunmiPrinterService(SunmiPrinterService service){
        boolean ret = false;
        try {
            ret = InnerPrinterManager.getInstance().hasPrinter(service);
        } catch (InnerPrinterException e) {
            e.printStackTrace();
        }
        sunmiPrinter = ret?FoundSunmiPrinter:NoSunmiPrinter;
    }
}
