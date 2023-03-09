package expo.modules.sunmiv2printer

// import android.content.ComponentName;
import android.content.BroadcastReceiver
import android.content.Context;
import android.content.Intent
// import android.content.Intent;
// import android.content.IntentFilter;
// import android.content.ServiceConnection;
import android.graphics.Bitmap;
// import android.os.Handler;
// import android.os.IBinder;
// import android.os.Message;
import android.os.RemoteException;
import android.util.Base64;
import android.util.Log;

import expo.modules.kotlin.modules.Module
import expo.modules.kotlin.modules.ModuleDefinition
// import expo.modules.sunmiv2printer.SunmiV2PrinterModule.reactApplicationContext

// import com.facebook.react.bridge.ReactApplicationContext;
// import com.facebook.react.bridge.ReactContextBaseJavaModule;
// import com.facebook.react.bridge.ReactMethod;
import com.facebook.react.bridge.Promise

// import java.util.Map;

// import com.sunmi.peripheral.printer.InnerPrinterCallback;
// import com.sunmi.peripheral.printer.InnerPrinterException;
// import com.sunmi.peripheral.printer.InnerPrinterManager;
import com.sunmi.peripheral.printer.InnerResultCallback;
// import com.sunmi.peripheral.printer.SunmiPrinterService;

// TODO move to another file
internal class MyResultCallback : InnerResultCallback() {
  private var myPromise: com.facebook.react.bridge.Promise? = null
  private val myTag = "MyResultCallback"
  @Throws(RemoteException::class)
  override fun onRunResult(isSuccess: Boolean) {
    Log.i(myTag, "onRunResult: $isSuccess")
    if (myPromise != null) {
      myPromise?.resolve(isSuccess)
      myPromise = null
    }
  }

  @Throws(RemoteException::class)
  override fun onReturnString(result: String) {
    Log.i(myTag, "onReturnString: $result")
    if (myPromise != null) {
      myPromise?.resolve(result)
      myPromise = null
    }
  }

  @Throws(RemoteException::class)
  override fun onRaiseException(code: Int, msg: String) {
    Log.i(myTag, "onRaiseException: $msg")
    if (myPromise != null) {
      myPromise?.reject(code.toString(), msg)
      myPromise = null
    }
  }

  @Throws(RemoteException::class)
  override fun onPrintResult(code: Int, msg: String) {
    Log.i(myTag, "onPrintResult: $msg")
    if (myPromise != null) {
      myPromise?.resolve(msg)
      myPromise = null
    }
  }

  fun setPromise(promise: Promise?) {
    myPromise = promise
  }
}

// TODO move to another file, WAS another file
class PrinterReceiver : BroadcastReceiver() {
  override fun onReceive(context: Context, data: Intent) {
    val action: String? = data.action;
    if(action != null){
      // val type = "PrinterStatus"
      Log.d("PrinterReceiver", action)
      // TODO use appContext.reactContext
      // if (SunmiV2PrinterModule.reactApplicationContext != null) {
      //   SunmiV2PrinterModule.reactApplicationContext.getJSModule(RCTDeviceEventEmitter::class.java).emit(type, action)
      // }
    }
  }
}

class ReactNativeSunmiV2PrinterModule : Module() {
  
  private val helper = SunmiPrintHelper();
  private val TAG = "SunmiV2PrinterModule"
  private val innerResultCallback = MyResultCallback()
  // private var bitMapUtils: BitmapUtils? = null


  // Each module class must implement the definition function. The definition consists of components
  // that describes the module's functionality and behavior.
  // See https://docs.expo.dev/modules/module-api for more details about available components.
  override fun definition() = ModuleDefinition {
    // Sets the name of the module that JavaScript code will use to refer to the module. Takes a string as an argument.
    // Can be inferred from module's class name, but it's recommended to set it explicitly for clarity.
    // The module will be accessible from `requireNativeModule('ReactNativeSunmiV2Printer')` in JavaScript.
    Name("ReactNativeSunmiV2Printer")
    // TODO fails here
    // bitMapUtils = BitmapUtils(appContext.reactContext);

    // // Sets constant properties on the module. Can take a dictionary or a closure that returns a dictionary.
    // Constants(
    //   "PI" to Math.PI,
    // )


    // // Defines event names that the module can send to JavaScript.
    // Events("onChange")

    // // Defines a JavaScript synchronous function that runs the native code on the JavaScript thread.
    // Function("hello") {
    //   "Hello world! 👋"
    // }

    // // Defines a JavaScript function that always returns a Promise and whose native code
    // // is by default dispatched on the different thread than the JavaScript runtime runs on.
    // AsyncFunction("setValueAsync") { value: String ->
    //   // Send an event to JavaScript.
    //   sendEvent("onChange", mapOf(
    //     "value" to value
    //   ))
    // }

    // // Enables the module to be used as a native view. Definition components that are accepted as part of
    // // the view definition: Prop, Events.
    // View(ReactNativeSunmiV2PrinterView::class) {
    //   // Defines a setter for the `name` prop.
    //   Prop("name") { view: ReactNativeSunmiV2PrinterView, prop: String ->
    //     println(prop)
    //   }
    // }

    // Function("initBind") {
    //   "Hello world! 👋"
    // }

    Function("initBind") {  ->
      print("// TODO check: initBind");

      try {
        if(appContext != null){
          return@Function helper.initSunmiPrinterService(appContext.reactContext)
        } else {
          return@Function "no context"
        }
      } catch (e: Exception) {
        print(TAG);
        print( "ERROR:");
        println(e.message);
        return@Function  e.message
      }
      // return@Function SunmiV2PrinterModule.getInstance().ini ;
    }

    Function("getPrinterDidBind") {  ->
      print("// TODO check: getPrinterDidBind");
      try {
        return@Function helper.printerDidBind()
      } catch (e: Exception) {
        print(TAG);
        print( "ERROR:");
        println(e.message);
        return@Function  e.message
      }
    }

    Function("initPrinter") {  ->
      print("// TODO check: initPrinter");
      try {
        return@Function helper.initPrinter()
      } catch (e: Exception) {
        print(TAG);
        print( "ERROR:");
        println(e.message);
        return@Function  e.message
      }
    }

    Function("getPrinterServiceDidInit") {  ->
      print("// TODO check: getPrinterServiceDidInit");
      try {
        return@Function helper.printerServiceDidInit()
      } catch (e: Exception) {
        print(TAG);
        print( "ERROR:");
        println(e.message);
        return@Function  e.message
      }
    }

    Function("getPrinterVersion") {  ->
      print("// TODO check: getPrinterVersion");
      try {
        // return@Function helper.getPrinterVersion()
        return@Function helper.printerVersion
      } catch (e: Exception) {
        print(TAG);
        print( "ERROR:");
        println(e.message);
        return@Function  e.message
      }
    }

    Function("getPrinterPaperSize") {  ->
      print("// TODO check: getPrinterPaperSize");
      try {
        //return@Function helper.getPrinterPaper()
        return@Function helper.printerPaper
      } catch (e: Exception) {
        print(TAG);
        print( "ERROR:");
        println(e.message);
        return@Function  e.message
      }
    }

    Function("getPrinterSpecifications") {  ->
      print("// TODO check: getPrinterSpecifications");
      // innerResultCallback.setPromise(p);
      try {
        return@Function helper.getPrinterHead(innerResultCallback)
      } catch (e: Exception) {
        print(TAG);
        print( "ERROR:");
        println(e.message);
        return@Function  e.message
      }
    }

    Function("getPrinterDensity") {  ->
      print("// TODO check: getPrinterDensity");
      try {
        //return@Function helper.getPrinterDensity()
        return@Function helper.printerDensity
      } catch (e: Exception) {
        print(TAG);
        print( "ERROR:");
        println(e.message);
        return@Function  e.message
      }
    }

    Function("getServiceVersion") {  ->
      print("// TODO check: getServiceVersion");
      try {
        // return@Function helper.getServiceVersion()
        return@Function helper.serviceVersion
      } catch (e: Exception) {
        print(TAG);
        print( "ERROR:");
        println(e.message);
        return@Function  e.message
      }
    }

    Function("getPrinterDistance") {  ->
      print("// TODO check: getPrinterDistance");
      try {
        return@Function helper.getPrinterDistance(innerResultCallback)
      } catch (e: Exception) {
        print(TAG);
        print( "ERROR:");
        println(e.message);
        return@Function  e.message
      }
    }

    Function("showPrinterStatusToast") {  ->
      print("// TODO check: showPrinterStatusToast");
      try {
        return@Function helper.showPrinterStatus(appContext.reactContext)
      } catch (e: Exception) {
        print(TAG);
        print( "ERROR:");
        println(e.message);
        return@Function  e.message
      }
    }

    Function("sendRAWData") { base64EncriptedData: String ->
      print("// TODO check: getServiceVersion");
      try {
        val data: ByteArray = Base64.decode(base64EncriptedData, Base64.DEFAULT)
        return@Function helper.sendRawData(data)
      } catch (e: Exception) {
        print(TAG);
        print( "ERROR:");
        println(e.message);
        return@Function  e.message
      }
    }

    Function("setAlignment") { alignment: Int ->
      print("// TODO check: setAlignment");
      try {
        return@Function helper.setAlign(alignment);
      } catch (e: Exception) {
        print(TAG);
        print( "ERROR:");
        println(e.message);
        return@Function  e.message
      }
    }

    Function("printBitmap") { data: String , width: Int,height: Int ->
      print("// TODO check: printBitmap");
      try {
        val decoded: ByteArray = Base64.decode(data, Base64.DEFAULT)
        // TODO put inside if
        if(appContext != null){
          val bitMap: Bitmap = BitmapUtils(appContext.reactContext)!!.decodeBitmap(decoded, width, height)        
          return@Function helper.printBitmap(bitMap)
        } else {
          return@Function "no context"
        }
      } catch (e: Exception) {
        print(TAG);
        print( "ERROR:");
        println(e.message);
        return@Function  e.message
      }
    }

    Function("printText") { text: String ->
      print("// TODO check: printText");
      try {
        return@Function helper.printText(text)
      } catch (e: Exception) {
        print(TAG);
        print( "ERROR:");
        println(e.message);
        return@Function  e.message
      }
    }

    Function("clearBuffer") {  ->
      print("// TODO check: getServiceVersion");
      try {
        return@Function helper.deInitSunmiPrinterService(appContext.reactContext)
      } catch (e: Exception) {
        print(TAG);
        print( "ERROR:");
        println(e.message);
        return@Function  e.message
      }
    }

    Function("openCashDrawer") {  ->
      print("// TODO check: openCashDrawer");
      try {
        return@Function helper.openCashBox()
      } catch (e: Exception) {
        print(TAG);
        print( "ERROR:");
        println(e.message);
        return@Function  e.message
      }
    }

    Function("cutPaper") {  ->
      print("// TODO check: cutPaper");
      try {
        return@Function helper.cutpaper()
      } catch (e: Exception) {
        print(TAG);
        print( "ERROR:");
        println(e.message);
        return@Function  e.message
      }
    }
  }
}


