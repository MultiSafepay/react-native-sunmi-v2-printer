package expo.modules.sunmiv2printer

import SunmiResultCallback
import android.graphics.Bitmap
import android.util.Base64
import android.util.Log
import com.facebook.react.bridge.ReactApplicationContext
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

    Function("initBind") {  ->
      Log.i(TAG, "initBind");
      try {
        if(appContext != null){
          return@Function helper.initSunmiPrinterService(appContext.reactContext)
        } else {
          return@Function "no context"
        }
      } catch (e: Exception) {
        Log.i(TAG, "initBind did fail:" + e.message);
        return@Function  e.message
      }
    }

    Function("getPrinterDidBind") {  ->
      Log.i(TAG, "getPrinterDidBind");
      try {
        return@Function helper.printerDidBind()
      } catch (e: Exception) {
        Log.i(TAG, "getPrinterDidBind did fail:" + e.message);
        return@Function  e.message
      }
    }

    Function("initPrinter") {  ->
      Log.i(TAG, "initPrinter");
      try {
        return@Function helper.initPrinter()
      } catch (e: Exception) {
        Log.i(TAG, "initPrinter did fail:" + e.message);
        return@Function  e.message
      }
    }

    Function("getPrinterServiceDidInit") {  ->
      Log.i(TAG, "getPrinterServiceDidInit");
      try {
        return@Function helper.printerServiceDidInit()
      } catch (e: Exception) {
        Log.i(TAG, "getPrinterServiceDidInit did fail:" + e.message);
        return@Function  e.message
      }
    }

    Function("getPrinterVersion") {  ->
      Log.i(TAG, "getPrinterVersion");
      try {
        return@Function helper.printerVersion
      } catch (e: Exception) {
        Log.i(TAG, "getPrinterVersion did fail:" + e.message);
        return@Function  e.message
      }
    }

    Function("getPrinterPaperSize") {  ->
      Log.i(TAG, "getPrinterPaperSize");
      try {
        return@Function helper.printerPaper
      } catch (e: Exception) {
        Log.i(TAG, "getPrinterVersion did fail:" + e.message);
        return@Function  e.message
      }
    }

    Function("getPrinterSpecifications") {  ->
      Log.i(TAG, "getPrinterSpecifications");
      try {
        return@Function helper.getPrinterHead(innerResultCallback)
      } catch (e: Exception) {
        Log.i(TAG, "getPrinterSpecifications did fail:" + e.message);
        return@Function  e.message
      }
    }

    Function("getPrinterDensity") {  ->
      Log.i(TAG, "getPrinterDensity");
      try {
        return@Function helper.printerDensity
      } catch (e: Exception) {
        Log.i(TAG, "getPrinterSpecifications did fail:" + e.message);
        return@Function  e.message
      }
    }

    Function("getServiceVersion") {  ->
      Log.i(TAG, "getServiceVersion");
      try {
        // return@Function helper.getServiceVersion()
        return@Function helper.serviceVersion
      } catch (e: Exception) {
        Log.i(TAG, "getServiceVersion did fail:" + e.message);
        return@Function  e.message
      }
    }

    Function("getPrinterDistance") {  ->
      Log.i(TAG, "getPrinterDistance");
      try {
        return@Function helper.getPrinterDistance(innerResultCallback)
      } catch (e: Exception) {
        Log.i(TAG, "getPrinterDistance did fail:" + e.message);
        return@Function  e.message
      }
    }

    Function("showPrinterStatusToast") {  ->
      Log.i(TAG, "showPrinterStatusToast");
      try {
        return@Function helper.showPrinterStatus(appContext.reactContext)
      } catch (e: Exception) {
        Log.i(TAG, "showPrinterStatusToast did fail:" + e.message);
        return@Function  e.message
      }
    }

    Function("sendRAWData") { base64EncriptedData: String ->
      Log.i(TAG, "sendRAWData");
      try {
        val data: ByteArray = Base64.decode(base64EncriptedData, Base64.DEFAULT)
        return@Function helper.sendRawData(data)
      } catch (e: Exception) {
        Log.i(TAG, "sendRAWData did fail:" + e.message);
        return@Function  e.message
      }
    }

    Function("setAlignment") { alignment: Int ->
      Log.i(TAG, "sendRAWData");
      try {
        return@Function helper.setAlign(alignment);
      } catch (e: Exception) {
        Log.i(TAG, "setAlignment did fail:" + e.message);
        return@Function  e.message
      }
    }

    Function("printBitmap") { data: String , width: Int,height: Int ->
      Log.i(TAG, "printBitmap");
      try {
        val decoded: ByteArray = Base64.decode(data, Base64.DEFAULT)
        if(appContext != null){
          val bitMap: Bitmap = BitmapUtils(appContext.reactContext)!!.decodeBitmap(decoded, width, height)        
          return@Function helper.printBitmap(bitMap)
        } else {
          return@Function "no context"
        }
      } catch (e: Exception) {
        Log.i(TAG, "printBitmap did fail:" + e.message);
        return@Function  e.message
      }
    }

    Function("printText") { text: String ->
      Log.i(TAG, "printText");
      try {
        return@Function helper.printText(text)
      } catch (e: Exception) {
        Log.i(TAG, "printText did fail:" + e.message);
        return@Function  e.message
      }
    }

    Function("clearBuffer") {  ->
      Log.i(TAG, "clearBuffer");
      try {
        return@Function helper.deInitSunmiPrinterService(appContext.reactContext)
      } catch (e: Exception) {
        Log.i(TAG, "clearBuffer did fail:" + e.message);
        return@Function  e.message
      }
    }

    Function("openCashDrawer") {  ->
      Log.i(TAG, "openCashDrawer");
      try {
        return@Function helper.openCashBox()
      } catch (e: Exception) {
        Log.i(TAG, "openCashDrawer did fail:" + e.message);
        return@Function  e.message
      }
    }

    Function("cutPaper") {  ->
      Log.i(TAG, "cutPaper");
      try {
        return@Function helper.cutpaper()
      } catch (e: Exception) {
        Log.i(TAG, "cutPaper did fail:" + e.message);
        return@Function  e.message
      }
    }
  }
}


