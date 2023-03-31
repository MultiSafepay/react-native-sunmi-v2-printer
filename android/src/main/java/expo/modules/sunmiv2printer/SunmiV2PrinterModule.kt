package expo.modules.sunmiv2printer

import SunmiResultCallback
import android.graphics.Bitmap
import android.util.Base64
import android.util.Log
import com.facebook.react.bridge.ReactApplicationContext
import expo.modules.kotlin.Promise
import expo.modules.kotlin.modules.Module
import expo.modules.kotlin.modules.ModuleDefinition

class SunmiV2PrinterModule : Module() {
    public companion object {
        @kotlin.jvm.JvmField
        public var reactApplicationContext: ReactApplicationContext? = null
    }

    private val helper = SunmiPrintHelper();
    private val TAG = "SunmiV2PrinterModule"
    private val innerResultCallback = SunmiResultCallback()
    // private var bitMapUtils: BitmapUtils? = null

    // Each module class must implement the definition function. The definition consists of components
    // that describes the module's functionality and behavior.
    // See https://docs.expo.dev/modules/module-api for more details about available components.
    override fun definition() = ModuleDefinition {
        // Sets the name of the module that JavaScript code will use to refer to the module. Takes a string as an argument.
        // Can be inferred from module's class name, but it's recommended to set it explicitly for clarity.
        // The module will be accessible from `requireNativeModule('SunmiV2PrinterModule')` in JavaScript.
        Name(TAG)

        AsyncFunction("initBind") { promise: Promise ->
            Log.i(TAG, "initBind");
            try {
                if (appContext != null) {
                    promise.resolve(helper.initSunmiPrinterService(appContext.reactContext))
                } else {
                    promise.reject("no context")
                }
            } catch (e: Exception) {
                Log.i(TAG, "initBind did fail:" + e.message);
                promise.reject(e.message ?: "Error")
            }
        }

        AsyncFunction("getPrinterDidBind") { promise: Promise ->
            Log.i(TAG, "getPrinterDidBind");
            try {
                promise.resolve(helper.printerDidBind())
            } catch (e: Exception) {
                Log.i(TAG, "getPrinterDidBind did fail:" + e.message);
                promise.reject(e.message ?: "Error")
            }
        }

        AsyncFunction("initPrinter") { promise: Promise ->
            Log.i(TAG, "initPrinter");
            try {
                promise.resolve(helper.initPrinter())
            } catch (e: Exception) {
                Log.i(TAG, "initPrinter did fail:" + e.message);
                promise.reject(e.message ?: "initPrinterError")
            }
        }

        AsyncFunction("getPrinterServiceDidInit") { promise: Promise ->
            Log.i(TAG, "getPrinterServiceDidInit");
            try {
                promise.resolve(helper.printerServiceDidInit())
            } catch (e: Exception) {
                Log.i(TAG, "getPrinterServiceDidInit did fail:" + e.message);
                promise.reject(e.message ?: "Error")
            }
        }


        AsyncFunction("getPrinterVersion") { promise: Promise ->
            Log.i(TAG, "getPrinterVersion");
            try {
                promise.resolve(helper.printerVersion)
            } catch (e: Exception) {
                Log.i(TAG, "getPrinterVersion did fail:" + e.message);
                promise.reject(e.message ?: "Error")
            }
        }


        AsyncFunction("getPrinterPaperSize") { promise: Promise ->
            Log.i(TAG, "getPrinterPaperSize");
            try {
                promise.resolve(helper.printerPaper)
            } catch (e: Exception) {
                Log.i(TAG, "getPrinterVersion did fail:" + e.message);
                promise.reject(e.message ?: "Error")
            }
        }

        AsyncFunction("getPrinterSpecifications") { promise: Promise ->
            Log.i(TAG, "getPrinterSpecifications");
            try {
                promise.resolve(helper.getPrinterHead(innerResultCallback))
            } catch (e: Exception) {
                Log.i(TAG, "getPrinterSpecifications did fail:" + e.message);
                promise.reject(e.message ?: "Error")
            }
        }

        AsyncFunction("getPrinterDensity") { promise: Promise ->
            Log.i(TAG, "getPrinterDensity");
            try {
                promise.resolve(helper.printerDensity)
            } catch (e: Exception) {
                Log.i(TAG, "getPrinterSpecifications did fail:" + e.message);
                promise.reject(e.message ?: "Error")
            }
        }

        AsyncFunction("getServiceVersion") { promise: Promise ->
            Log.i(TAG, "getServiceVersion");
            try {
                promise.resolve(helper.serviceVersion)
            } catch (e: Exception) {
                Log.i(TAG, "getServiceVersion did fail:" + e.message);
                promise.reject(e.message ?: "Error")
            }
        }

        AsyncFunction("getPrinterDistance") { promise: Promise ->
            Log.i(TAG, "getPrinterDistance");
            try {
                promise.resolve(helper.getPrinterDistance(innerResultCallback))
            } catch (e: Exception) {
                Log.i(TAG, "getPrinterDistance did fail:" + e.message);
                promise.reject(e.message ?: "Error")
            }
        }

        AsyncFunction("showPrinterStatusToast") { promise: Promise ->
            Log.i(TAG, "showPrinterStatusToast");
            try {
                promise.resolve(helper.showPrinterStatus(appContext.reactContext))
            } catch (e: Exception) {
                Log.i(TAG, "showPrinterStatusToast did fail:" + e.message);
                promise.reject(e.message ?: "Error")
            }
        }

        AsyncFunction("sendRAWData") { base64EncriptedData: String, promise: Promise ->
            Log.i(TAG, "sendRAWData");
            try {
                val data: ByteArray = Base64.decode(base64EncriptedData, Base64.DEFAULT)
                promise.resolve(helper.sendRawData(data))
            } catch (e: Exception) {
                Log.i(TAG, "sendRAWData did fail:" + e.message);
                promise.reject(e.message ?: "Error")
            }
        }

        AsyncFunction("setAlignment") { alignment: Int, promise: Promise ->
            Log.i(TAG, "sendRAWData");
            try {
                promise.resolve(helper.setAlign(alignment))
            } catch (e: Exception) {
                Log.i(TAG, "setAlignment did fail:" + e.message);
                promise.reject(e.message ?: "Error")
            }
        }

        AsyncFunction("printBitmap") { data: String, width: Int, height: Int, promise: Promise ->
            Log.i(TAG, "printBitmap");
            try {
                val decoded: ByteArray = Base64.decode(data, Base64.DEFAULT)
                if (appContext != null) {
                    val bitMap: Bitmap =
                        BitmapUtils(appContext.reactContext)!!.decodeBitmap(decoded, width, height)
                    promise.resolve(helper.printBitmap(bitMap))
                } else {
                    promise.reject("no context")
                }
            } catch (e: Exception) {
                Log.i(TAG, "printBitmap did fail:" + e.message);
                promise.reject(e.message ?: "Error")
            }
        }

        AsyncFunction("printText") { text: String, promise: Promise ->
            Log.i(TAG, "printText");
            try {
                promise.resolve(helper.printText(text))
            } catch (e: Exception) {
                Log.i(TAG, "printText did fail:" + e.message);
                promise.reject(e.message ?: "Error")
            }
        }

        AsyncFunction("clearBuffer") { promise: Promise ->
            Log.i(TAG, "clearBuffer");
            try {
                promise.resolve(helper.deInitSunmiPrinterService(appContext.reactContext))
            } catch (e: Exception) {
                Log.i(TAG, "clearBuffer did fail:" + e.message);
                promise.reject(e.message ?: "Error")
            }
        }

        AsyncFunction("openCashDrawer") { promise: Promise ->
            Log.i(TAG, "openCashDrawer");
            try {
                promise.resolve(helper.openCashBox())
            } catch (e: Exception) {
                Log.i(TAG, "openCashDrawer did fail:" + e.message);
                promise.reject(e.message ?: "Error")
            }
        }

        AsyncFunction("cutPaper") { promise: Promise ->
            Log.i(TAG, "cutPaper");
            try {
                promise.resolve(helper.cutpaper())
            } catch (e: Exception) {
                Log.i(TAG, "cutPaper did fail:" + e.message);
                promise.reject(e.message ?: "Error")
            }
        }
    }
}
