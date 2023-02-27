package expo.modules.sunmiv2printer

import expo.modules.kotlin.modules.Module
import expo.modules.kotlin.modules.ModuleDefinition

import com.sunmi.v2.printer.*
// import com.sunmi.peripheral.printer.InnerPrinterCallback;
// import com.sunmi.peripheral.printer.InnerPrinterException;
// import com.sunmi.peripheral.printer.InnerPrinterManager;
// import com.sunmi.peripheral.printer.InnerResultCallback;
// import com.sunmi.peripheral.printer.SunmiPrinterService;

class ReactNativeSunmiV2PrinterModule : Module() {
  
      // private val helper = SunmiPrintHelper();


  // Each module class must implement the definition function. The definition consists of components
  // that describes the module's functionality and behavior.
  // See https://docs.expo.dev/modules/module-api for more details about available components.
  override fun definition() = ModuleDefinition {
    // Sets the name of the module that JavaScript code will use to refer to the module. Takes a string as an argument.
    // Can be inferred from module's class name, but it's recommended to set it explicitly for clarity.
    // The module will be accessible from `requireNativeModule('ReactNativeSunmiV2Printer')` in JavaScript.
    Name("ReactNativeSunmiV2Printer")

    // // Sets constant properties on the module. Can take a dictionary or a closure that returns a dictionary.
    // Constants(
    //   "PI" to Math.PI
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

    // TODO call functions from old implementation
    // android/src/main/java/com/sunmi/v2/printer/SunmiV2PrinterModule.java

    Function("initBind") {  ->
      println("// TODO implement")
      // SunmiV2PrinterModule.getInstance().printerDidBind()
        //   try {
        //     helper.initSunmiPrinterService(reactApplicationContext);
        //     // return;
        // } catch (Exception e) {
        //   println()
        //     Log.i(TAG, "ERROR: " + e.getMessage());
        //     promise.reject("0", e.getMessage());
        // }
      return@Function "// TODO implement"
    }

    Function("getPrinterDidBind") {  ->
      return@Function "// TODO implement"
    }

    Function("initPrinter") {  ->
      return@Function "// TODO implement"
    }

    Function("getPrinterServiceDidInit") {  ->
      return@Function "// TODO implement"
    }

    Function("getPrinterVersion") {  ->
      return@Function "// TODO implement"
    }

    Function("getPrinterPaperSize") {  ->
      return@Function "// TODO implement"
    }

    Function("getPrinterSpecifications") {  ->
      return@Function "// TODO implement"
    }

    Function("getPrinterDensity") {  ->
      return@Function "// TODO implement"
    }

    Function("getServiceVersion") {  ->
      return@Function "// TODO implement"
    }

    Function("getPrinterDistance") {  ->
      return@Function "// TODO implement"
    }

    Function("showPrinterStatusToast") {  ->
      return@Function "// TODO implement"
    }

    Function("sendRAWData") { base64EncriptedData: String ->
      return@Function "// TODO implement"
    }

    Function("setAlignment") { alignment: Integer ->
      return@Function "// TODO implement"
    }

    Function("printBitmap") { height: Integer, width: Integer ->
      return@Function "// TODO implement"
    }

    Function("printText") { text: String ->
      return@Function "// TODO implement"
    }

    Function("clearBuffer") {  ->
      return@Function "// TODO implement"
    }

    Function("openCashDrawer") {  ->
      return@Function "// TODO implement"
    }

    Function("cutPaper") {  ->
      return@Function "// TODO implement"
    }
  }
}

