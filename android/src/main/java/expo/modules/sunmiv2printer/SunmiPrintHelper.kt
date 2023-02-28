package expo.modules.sunmiv2printer

import android.content.Context
import android.graphics.Bitmap
import android.os.RemoteException
import android.util.Log
import android.widget.Toast

import com.sunmi.peripheral.printer.InnerLcdCallback
import com.sunmi.peripheral.printer.InnerPrinterCallback;
import com.sunmi.peripheral.printer.InnerPrinterException;
import com.sunmi.peripheral.printer.InnerPrinterManager;
import com.sunmi.peripheral.printer.InnerResultCallback;
import com.sunmi.peripheral.printer.SunmiPrinterService;
import com.sunmi.peripheral.printer.WoyouConsts;

/**
 * <pre>
 * This class is used to demonstrate various printing effects
 * Developers need to repackage themselves, for details please refer to
 * http://sunmi-ota.oss-cn-hangzhou.aliyuncs.com/DOC/resource/re_cn/Sunmiprinter%E5%BC%80%E5%8F%91%E8%80%85%E6%96%87%E6%A1%A31.1.191128.pdf
</pre> *
 *
 * @author kaltin
 * @since create at 2020-02-14
 */
class SunmiPrintHelper internal constructor() {
    /**
     * sunmiPrinter means checking the printer connection status
     */
    @JvmField
    var sunmiPrinter = CheckSunmiPrinter

    /**
     * SunmiPrinterService for API
     */
    private var sunmiPrinterService: SunmiPrinterService? = null
    private val innerPrinterCallback: InnerPrinterCallback = object : InnerPrinterCallback() {
        protected override fun onConnected(service: SunmiPrinterService?) {
            Log.i("SDK-DEBUG", "Sunmi Printer connected")
            sunmiPrinterService = service
            checkSunmiPrinterService(service)
        }

        protected override fun onDisconnected() {
            sunmiPrinterService = null
            sunmiPrinter = LostSunmiPrinter
        }
    }

    fun printerServiceDidInit(): Boolean {
        return sunmiPrinterService != null
    }

    fun printerDidBind(): Boolean {
        return sunmiPrinter == FoundSunmiPrinter
    }

    /**
     * init sunmi print service
     */
    @Throws(Exception::class)
    fun initSunmiPrinterService(context: Context?) {
        sunmiPrinter = try {
            val ret: Boolean = InnerPrinterManager.getInstance().bindService(
                context,
                innerPrinterCallback
            )
            if (!ret) {
                NoSunmiPrinter
            } else {
                FoundSunmiPrinter
            }
        } catch (e: InnerPrinterException) {
            e.printStackTrace()
            throw e
        }
    }

    /**
     * deInit sunmi print service
     */
    @Throws(Exception::class)
    fun deInitSunmiPrinterService(context: Context?) {
        try {
            if (sunmiPrinterService != null) {
                InnerPrinterManager.getInstance().unBindService(context, innerPrinterCallback)
                sunmiPrinterService = null
                sunmiPrinter = LostSunmiPrinter
            }
        } catch (e: InnerPrinterException) {
            e.printStackTrace()
            throw e
        }
    }

    /**
     * Check the printer connection,
     * like some devices do not have a printer but need to be connected to the cash drawer through a print service
     */
    private fun checkSunmiPrinterService(service: SunmiPrinterService?) {
        var ret = false
        try {
            ret = InnerPrinterManager.getInstance().hasPrinter(service)
        } catch (e: InnerPrinterException) {
            Log.e("SDK-DEBUG", "Has no Sunmi Printer")
            e.printStackTrace()
        }
        sunmiPrinter = if (ret) FoundSunmiPrinter else NoSunmiPrinter
    }

    @Throws(Exception::class)
    private fun checkPrinterServiceAvailability() {
        // TODO Service disconnection processing
        if (sunmiPrinterService == null) {
            Log.e("SDK-DEBUG", "Sunmi Printer service is not initialized")
            throw Exception("No sunmi printer service")
        }
    }

    /**
     * send esc cmd
     */
    @Throws(Exception::class)
    fun sendRawData(data: ByteArray?) {
        checkPrinterServiceAvailability()
        sunmiPrinterService?.sendRAWData(data, null)
    }

    /**
     * Printer cuts paper and throws exception on machines without a cutter
     */
    @Throws(Exception::class)
    fun cutpaper() {
        checkPrinterServiceAvailability()
        sunmiPrinterService?.cutPaper(null)
    }

    /**
     * Initialize the printer
     * All style settings will be restored to default
     */
    @Throws(Exception::class)
    fun initPrinter() {
        try {
            checkPrinterServiceAvailability()
            sunmiPrinterService?.printerInit(null)
            Log.e("SDK-DEBUG", "Sunmi Printer is initialized")
        } catch (e: Exception) {
            Log.e("SDK-DEBUG", "Exception at initPrinter")
            throw e
        }
    }

    /**
     * paper feed three lines
     * Not disabled when line spacing is set to 0
     */
    @Throws(Exception::class)
    fun print3Line() {
        checkPrinterServiceAvailability()
        sunmiPrinterService?.lineWrap(3, null)
    }

    /**
     * Get printer serial number
     */
    @get:Throws(Exception::class)
    val printerSerialNo: String?
        get() {
            checkPrinterServiceAvailability()
            return sunmiPrinterService?.getPrinterSerialNo()
        }

    /**
     * Get device model
     */
    @get:Throws(Exception::class)
    val deviceModel: String?
        get() {
            checkPrinterServiceAvailability()
            return sunmiPrinterService?.getPrinterModal()
        }

    /**
     * Get firmware version
     */
    @get:Throws(Exception::class)
    val printerVersion: String?
        get() {
            checkPrinterServiceAvailability()
            return sunmiPrinterService?.getPrinterVersion()
        }

    /**
     * Get paper specifications
     */
    @get:Throws(Exception::class)
    val printerPaper: String
        get() {
            checkPrinterServiceAvailability()
            return if (sunmiPrinterService?.getPrinterPaper() === 1) "58mm" else "80mm"
        }

    /**
     * Get printer densite
     */
    @get:Throws(Exception::class)
    val printerDensity: Int?
        get() {
            checkPrinterServiceAvailability()
            return sunmiPrinterService?.getPrinterDensity()
        }

    /**
     * Get service version
     */
    @get:Throws(Exception::class)
    val serviceVersion: String?
        get() {
            checkPrinterServiceAvailability()
            return sunmiPrinterService?.getServiceVersion()
        }

    /**
     * Get paper specifications
     */
    @Throws(Exception::class)
    fun getPrinterHead(callback: InnerResultCallback?) {
        checkPrinterServiceAvailability()
        sunmiPrinterService?.getPrinterFactory(callback)
    }

    /**
     * Get printing distance since boot
     * Get printing distance through interface callback since 1.0.8(printerlibrary)
     */
    @Throws(Exception::class)
    fun getPrinterDistance(callback: InnerResultCallback?) {
        checkPrinterServiceAvailability()
        sunmiPrinterService?.getPrintedLength(callback)
    }

    /**
     * Set printer alignment
     */
    @Throws(Exception::class)
    fun setAlign(align: Int) {
        checkPrinterServiceAvailability()
        sunmiPrinterService?.setAlignment(align, null)
    }

    /**
     * Due to the distance between the paper hatch and the print head,
     * the paper needs to be fed out automatically
     * But if the Api does not support it, it will be replaced by printing three lines
     */
    @Throws(Exception::class)
    fun feedPaper() {
        checkPrinterServiceAvailability()
        sunmiPrinterService?.autoOutPaper(null)
    }

    @Throws(Exception::class)
    fun printText(content: String?) {
        try {
            checkPrinterServiceAvailability()
            sunmiPrinterService?.printText(content, null)
        } catch (e: Exception) {
            e.printStackTrace()
            throw e
        }
    }

    /**
     * print Bar Code
     */
    @Throws(Exception::class)
    fun printBarCode(data: String?, symbology: Int, height: Int, width: Int, textposition: Int) {
        try {
            checkPrinterServiceAvailability()
            sunmiPrinterService?.printBarCode(data, symbology, height, width, textposition, null)
        } catch (e: Exception) {
            e.printStackTrace()
            throw e
        }
    }

    /**
     * print Qr Code
     */
    @Throws(Exception::class)
    fun printQr(data: String?, modulesize: Int, errorlevel: Int) {
        try {
            checkPrinterServiceAvailability()
            sunmiPrinterService?.printQRCode(data, modulesize, errorlevel, null)
        } catch (e: Exception) {
            e.printStackTrace()
            throw e
        }
    }

    /**
     * Print a row of a table
     */
    @Throws(Exception::class)
    fun printTable(txts: Array<String?>?, width: IntArray?, align: IntArray?) {
        try {
            checkPrinterServiceAvailability()
            sunmiPrinterService?.printColumnsString(txts, width, align, null)
        } catch (e: Exception) {
            e.printStackTrace()
            throw e
        }
    }

    @Throws(Exception::class)
    fun printBitmap(bitmap: Bitmap?) {
        try {
            checkPrinterServiceAvailability()
            sunmiPrinterService?.printBitmap(bitmap, null)
            sunmiPrinterService?.printText("\n\n\n\n", null)
        } catch (e: Exception) {
            e.printStackTrace()
            throw e
        }
    }

    /**
     * Gets whether the current printer is in black mark mode
     */
    val isBlackLabelMode: Boolean
        get() = try {
            checkPrinterServiceAvailability()
            sunmiPrinterService?.getPrinterMode() === 1
        } catch (e: Exception) {
            false
        }

    /**
     * Gets whether the current printer is in label-printing mode
     */
    val isLabelMode: Boolean
        get() = try {
            checkPrinterServiceAvailability()
            sunmiPrinterService?.getPrinterMode() === 2
        } catch (e: Exception) {
            false
        }

    /**
     * Open cash box
     * This method can be used on Sunmi devices with a cash drawer interface
     * If there is no cash box (such as V1、P1) or the call fails, an exception will be thrown
     *
     * Reference to https://docs.sunmi.com/general-function-modules/external-device-debug/cash-box-driver/}
     */
    @Throws(Exception::class)
    fun openCashBox() {
        checkPrinterServiceAvailability()
        sunmiPrinterService?.openDrawer(null)
    }

    /**
     * LCD screen control
     * @param flag 1 —— Initialization
     * 2 —— Light up screen
     * 3 —— Extinguish screen
     * 4 —— Clear screen contents
     */
    @Throws(Exception::class)
    fun controlLcd(flag: Int) {
        checkPrinterServiceAvailability()
        sunmiPrinterService?.sendLCDCommand(flag)
    }

    /**
     * Display text SUNMI,font size is 16 and format is fill
     * sendLCDFillString(txt, size, fill, callback)
     * Since the screen pixel height is 40, the font should not exceed 40
     */
    @Throws(Exception::class)
    fun sendTextToLcd() {
        try {
            checkPrinterServiceAvailability()
            sunmiPrinterService?.sendLCDFillString("SUNMI", 16, true, object : InnerLcdCallback() {
                @Throws(RemoteException::class)
                override fun onRunResult(show: Boolean) {
                    //TODO handle result
                }
            })
        } catch (e: Exception) {
            e.printStackTrace()
            throw e
        }
    }

    /**
     * Display two lines and one empty line in the middle
     */
    @Throws(Exception::class)
    fun sendTextsToLcd() {
        try {
            checkPrinterServiceAvailability()
            val texts = arrayOf("SUNMI", null, "SUNMI")
            val align = intArrayOf(2, 1, 2)
            sunmiPrinterService?.sendLCDMultiString(texts, align, object : InnerLcdCallback() {
                @Throws(RemoteException::class)
                override fun onRunResult(show: Boolean) {
                    //TODO handle result
                }
            })
        } catch (e: Exception) {
            e.printStackTrace()
            throw e
        }
    }

    /**
     * Display one 128x40 pixels and opaque picture
     */
    @Throws(Exception::class)
    fun sendPicToLcd(pic: Bitmap?) {
        try {
            checkPrinterServiceAvailability()
            sunmiPrinterService?.sendLCDBitmap(pic, object : InnerLcdCallback() {
                @Throws(RemoteException::class)
                override fun onRunResult(show: Boolean) {
                    //TODO handle result
                }
            })
        } catch (e: Exception) {
            e.printStackTrace()
            throw e
        }
    }

    /**
     * Used to report the real-time query status of the printer, which can be used before each
     * printing
     */
    @Throws(Exception::class)
    fun showPrinterStatus(context: Context?) {
        var result = "Interface is too low to implement interface"
        try {
            checkPrinterServiceAvailability()
            val res: Int? = sunmiPrinterService?.updatePrinterState()
            when (res) {
                1 -> result = "printer is running"
                2 -> result = "printer found but still initializing"
                3 -> result = "printer hardware interface is abnormal and needs to be reprinted"
                4 -> result = "printer is out of paper"
                5 -> result = "printer is overheating"
                6 -> result = "printer's cover is not closed"
                7 -> result = "printer's cutter is abnormal"
                8 -> result = "printer's cutter is normal"
                9 -> result = "not found black mark paper"
                505 -> result = "printer does not exist"
                else -> {}
            }
        } catch (e: Exception) {
            e.printStackTrace()
            throw e
        }
        Toast.makeText(context, result, Toast.LENGTH_LONG).show()
    }

    /**
     * Demo printing a label
     * After printing one label, in order to facilitate the user to tear the paper, call
     * labelOutput to push the label paper out of the paper hatch
     * 演示打印一张标签
     * 打印单张标签后为了方便用户撕纸可调用labelOutput,将标签纸推出纸舱口
     */
    fun printOneLabel() {
        try {
            checkPrinterServiceAvailability()
            sunmiPrinterService?.labelLocate()
            printLabelContent()
            sunmiPrinterService?.labelOutput()
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    /**
     * Demo printing multi label
     *
     * After printing multiple labels, choose whether to push the label paper to the paper hatch according to the needs
     * 演示打印多张标签
     * 打印多张标签后根据需求选择是否推出标签纸到纸舱口
     */
    fun printMultiLabel(num: Int) {
        try {
            checkPrinterServiceAvailability()
            for (i in 0 until num) {
                sunmiPrinterService?.labelLocate()
                printLabelContent()
            }
            sunmiPrinterService?.labelOutput()
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    /**
     *
     * Custom label ticket content
     * In the example, not all labels can be applied. In actual use, please pay attention to adapting the size of the label. You can adjust the font size and content position.
     * 自定义的标签小票内容
     * 例子中并不能适用所有标签纸，实际使用时注意要自适配标签纸大小，可通过调节字体大小，内容位置等方式
     */
    @Throws(RemoteException::class)
    private fun printLabelContent() {
        sunmiPrinterService?.setPrinterStyle(WoyouConsts.ENABLE_BOLD, WoyouConsts.ENABLE)
        sunmiPrinterService?.lineWrap(1, null)
        sunmiPrinterService?.setAlignment(0, null)
        sunmiPrinterService?.printText("商品         豆浆\n", null)
        sunmiPrinterService?.printText("到期时间         12-13  14时\n", null)
        sunmiPrinterService?.printBarCode("{C1234567890123456", 8, 90, 2, 2, null)
        sunmiPrinterService?.lineWrap(1, null)
    }

    companion object {
        @JvmField
        var NoSunmiPrinter = 0x00000000
        @JvmField
        var CheckSunmiPrinter = 0x00000001
        @JvmField
        var FoundSunmiPrinter = 0x00000002
        @JvmField
        var LostSunmiPrinter = 0x00000003
        val instance = SunmiPrintHelper()
    }
}