package com.sunmi.v2.printer;

import android.content.Context;
import android.graphics.Bitmap;
import android.os.RemoteException;
import android.util.Log;
import android.widget.Toast;


import com.sunmi.peripheral.printer.InnerLcdCallback;
import com.sunmi.peripheral.printer.InnerPrinterCallback;
import com.sunmi.peripheral.printer.InnerPrinterException;
import com.sunmi.peripheral.printer.InnerPrinterManager;
import com.sunmi.peripheral.printer.InnerResultCallback;
import com.sunmi.peripheral.printer.SunmiPrinterService;
import com.sunmi.peripheral.printer.WoyouConsts;

/**
 * <pre>
 *      This class is used to demonstrate various printing effects
 *      Developers need to repackage themselves, for details please refer to
 *      http://sunmi-ota.oss-cn-hangzhou.aliyuncs.com/DOC/resource/re_cn/Sunmiprinter%E5%BC%80%E5%8F%91%E8%80%85%E6%96%87%E6%A1%A31.1.191128.pdf
 *  </pre>
 *
 * @author kaltin
 * @since create at 2020-02-14
 */
public class SunmiPrintHelper {

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

    SunmiPrintHelper() {}

    public static SunmiPrintHelper getInstance() {
        return helper;
    }

    private InnerPrinterCallback innerPrinterCallback = new InnerPrinterCallback() {
        @Override
        protected void onConnected(SunmiPrinterService service) {
            Log.i("SDK-DEBUG", "Sunmi Printer connected");

            sunmiPrinterService = service;
            checkSunmiPrinterService(service);
        }

        @Override
        protected void onDisconnected() {
            sunmiPrinterService = null;
            sunmiPrinter = LostSunmiPrinter;
        }
    };

    public boolean printerServiceDidInit() {
        return sunmiPrinterService != null;
    }
    public boolean printerDidBind() {
        return sunmiPrinter == FoundSunmiPrinter;
    }

    /**
     * init sunmi print service
     */
    public void initSunmiPrinterService(Context context) throws Exception {
        try {
            boolean ret =  InnerPrinterManager.getInstance().bindService(context,
                    innerPrinterCallback);
            if (!ret) {
                sunmiPrinter = NoSunmiPrinter;
            } else {
                sunmiPrinter = FoundSunmiPrinter;
            }
        } catch (InnerPrinterException e) {
            e.printStackTrace();
            throw e;
        }
    }

    /**
     *  deInit sunmi print service
     */
    public void deInitSunmiPrinterService(Context context) throws Exception {
        try {
            if(sunmiPrinterService != null){
                InnerPrinterManager.getInstance().unBindService(context, innerPrinterCallback);
                sunmiPrinterService = null;
                sunmiPrinter = LostSunmiPrinter;
            }
        } catch (InnerPrinterException e) {
            e.printStackTrace();
            throw e;
        }
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
            Log.e("SDK-DEBUG", "Has no Sunmi Printer" );
            e.printStackTrace();

        }
        sunmiPrinter = ret?FoundSunmiPrinter:NoSunmiPrinter;
    }

    private void checkPrinterServiceAvailability() throws Exception {
        // TODO Service disconnection processing
        if(sunmiPrinterService == null){
            Log.e("SDK-DEBUG", "Sunmi Printer service is not initialized");
            throw new Exception("No sunmi printer service");
        }
    }

    /**
     * send esc cmd
     */
    public void sendRawData(byte[] data) throws Exception {
        checkPrinterServiceAvailability();
        sunmiPrinterService.sendRAWData(data, null);
    }

    /**
     *  Printer cuts paper and throws exception on machines without a cutter
     */
    public void cutpaper() throws Exception {
        checkPrinterServiceAvailability();
        sunmiPrinterService.cutPaper(null);
    }

    /**
     *  Initialize the printer
     *  All style settings will be restored to default
     */
    public void initPrinter() throws Exception {
        try {
            checkPrinterServiceAvailability();
            sunmiPrinterService.printerInit(null);
            Log.e("SDK-DEBUG", "Sunmi Printer is initialized");
        } catch (Exception e) {
            Log.e("SDK-DEBUG", "Exception at initPrinter");
            throw e;
        }
    }

    /**
     *  paper feed three lines
     *  Not disabled when line spacing is set to 0
     */
    public void print3Line() throws Exception {
        checkPrinterServiceAvailability();
        sunmiPrinterService.lineWrap(3, null);
    }

    /**
     * Get printer serial number
     */
    public String getPrinterSerialNo() throws Exception {
        checkPrinterServiceAvailability();
        return sunmiPrinterService.getPrinterSerialNo();
    }

    /**
     * Get device model
     */
    public String getDeviceModel() throws Exception {
        checkPrinterServiceAvailability();
        return sunmiPrinterService.getPrinterModal();
    }

    /**
     * Get firmware version
     */
    public String getPrinterVersion() throws Exception {
        checkPrinterServiceAvailability();
        return sunmiPrinterService.getPrinterVersion();
    }

    /**
     * Get paper specifications
     */
    public String getPrinterPaper() throws Exception {
        checkPrinterServiceAvailability();
        return sunmiPrinterService.getPrinterPaper() == 1? "58mm" : "80mm";
    }

    /**
     * Get printer densite
     */
    public int getPrinterDensity() throws Exception {
        checkPrinterServiceAvailability();
        return sunmiPrinterService.getPrinterDensity();
    }

    /**
     * Get service version
     */
    public String getServiceVersion() throws Exception {
        checkPrinterServiceAvailability();
        return sunmiPrinterService.getServiceVersion();
    }

    /**
     * Get paper specifications
     */
    public void getPrinterHead(InnerResultCallback callback) throws Exception {
        checkPrinterServiceAvailability();
        sunmiPrinterService.getPrinterFactory(callback);
    }

    /**
     * Get printing distance since boot
     * Get printing distance through interface callback since 1.0.8(printerlibrary)
     */
    public void getPrinterDistance(InnerResultCallback callback) throws Exception {
        checkPrinterServiceAvailability();
        sunmiPrinterService.getPrintedLength(callback);
    }

    /**
     * Set printer alignment
     */
    public void setAlign(int align) throws Exception {
        checkPrinterServiceAvailability();
        sunmiPrinterService.setAlignment(align, null);
    }

    /**
     *  Due to the distance between the paper hatch and the print head,
     *  the paper needs to be fed out automatically
     *  But if the Api does not support it, it will be replaced by printing three lines
     */
    public void feedPaper() throws Exception {
        checkPrinterServiceAvailability();
        sunmiPrinterService.autoOutPaper(null);
    }

    public void printText(String content) throws Exception {
        try {
            checkPrinterServiceAvailability();
            sunmiPrinterService.printText(content, null);
        } catch (Exception e) {
            e.printStackTrace();
            throw e;
        }

    }

    /**
     * print Bar Code
     */
    public void printBarCode(String data, int symbology, int height, int width, int textposition) throws Exception {
        try {
            checkPrinterServiceAvailability();
            sunmiPrinterService.printBarCode(data, symbology, height, width, textposition, null);
        } catch (Exception e) {
            e.printStackTrace();
            throw e;
        }
    }

    /**
     * print Qr Code
     */
    public void printQr(String data, int modulesize, int errorlevel) throws Exception {
        try {
            checkPrinterServiceAvailability();
            sunmiPrinterService.printQRCode(data, modulesize, errorlevel, null);
        } catch (Exception e) {
            e.printStackTrace();
            throw e;
        }
    }

    /**
     * Print a row of a table
     */
    public void printTable(String[] txts, int[] width, int[] align) throws Exception {
        try {
            checkPrinterServiceAvailability();
            sunmiPrinterService.printColumnsString(txts, width, align, null);
        } catch (Exception e) {
            e.printStackTrace();
            throw e;
        }
    }

    public void printBitmap(Bitmap bitmap) throws Exception {
        try {
            checkPrinterServiceAvailability();
            sunmiPrinterService.printBitmap(bitmap, null);
            sunmiPrinterService.printText("\n\n\n\n", null);
        } catch (Exception e) {
            e.printStackTrace();
            throw e;
        }
    }

    /**
     * Gets whether the current printer is in black mark mode
     */
    public boolean isBlackLabelMode(){
        try {
            checkPrinterServiceAvailability();
            return sunmiPrinterService.getPrinterMode() == 1;
        } catch (Exception e) {
            return false;
        }
    }

    /**
     * Gets whether the current printer is in label-printing mode
     */
    public boolean isLabelMode(){
        try {
            checkPrinterServiceAvailability();
            return sunmiPrinterService.getPrinterMode() == 2;
        } catch (Exception e) {
            return false;
        }
    }

    /**
     *  Open cash box
     *  This method can be used on Sunmi devices with a cash drawer interface
     *  If there is no cash box (such as V1、P1) or the call fails, an exception will be thrown
     *
     *  Reference to https://docs.sunmi.com/general-function-modules/external-device-debug/cash-box-driver/}
     */
    public void openCashBox() throws Exception {
        checkPrinterServiceAvailability();
        sunmiPrinterService.openDrawer(null);
    }

    /**
     * LCD screen control
     * @param flag 1 —— Initialization
     *             2 —— Light up screen
     *             3 —— Extinguish screen
     *             4 —— Clear screen contents
     */
    public void controlLcd(int flag) throws Exception {
        checkPrinterServiceAvailability();
        sunmiPrinterService.sendLCDCommand(flag);
    }

    /**
     * Display text SUNMI,font size is 16 and format is fill
     * sendLCDFillString(txt, size, fill, callback)
     * Since the screen pixel height is 40, the font should not exceed 40
     */
    public void sendTextToLcd() throws Exception {
        try {
            checkPrinterServiceAvailability();
            sunmiPrinterService.sendLCDFillString("SUNMI", 16, true, new InnerLcdCallback() {
                @Override
                public void onRunResult(boolean show) throws RemoteException {
                    //TODO handle result
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
            throw e;
        }

    }

    /**
     * Display two lines and one empty line in the middle
     */
    public void sendTextsToLcd() throws Exception {
        try {
            checkPrinterServiceAvailability();
            String[] texts = {"SUNMI", null, "SUNMI"};
            int[] align = {2, 1, 2};
            sunmiPrinterService.sendLCDMultiString(texts, align, new InnerLcdCallback() {
                @Override
                public void onRunResult(boolean show) throws RemoteException {
                    //TODO handle result
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
            throw e;
        }

    }

    /**
     * Display one 128x40 pixels and opaque picture
     */
    public void sendPicToLcd(Bitmap pic) throws Exception {
        try {
            checkPrinterServiceAvailability();
            sunmiPrinterService.sendLCDBitmap(pic, new InnerLcdCallback() {
                @Override
                public void onRunResult(boolean show) throws RemoteException {
                    //TODO handle result
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
            throw e;
        }

    }

    /**
     * Used to report the real-time query status of the printer, which can be used before each
     * printing
     */
    public void showPrinterStatus(Context context) throws Exception {
        String result = "Interface is too low to implement interface";
        try {
            checkPrinterServiceAvailability();
            int res = sunmiPrinterService.updatePrinterState();
            switch (res){
                case 1:
                    result = "printer is running";
                    break;
                case 2:
                    result = "printer found but still initializing";
                    break;
                case 3:
                    result = "printer hardware interface is abnormal and needs to be reprinted";
                    break;
                case 4:
                    result = "printer is out of paper";
                    break;
                case 5:
                    result = "printer is overheating";
                    break;
                case 6:
                    result = "printer's cover is not closed";
                    break;
                case 7:
                    result = "printer's cutter is abnormal";
                    break;
                case 8:
                    result = "printer's cutter is normal";
                    break;
                case 9:
                    result = "not found black mark paper";
                    break;
                case 505:
                    result = "printer does not exist";
                    break;
                default:
                    break;
            }
        } catch (Exception e) {
            e.printStackTrace();
            throw e;
        }
        Toast.makeText(context, result, Toast.LENGTH_LONG).show();
    }

    /**
     * Demo printing a label
     * After printing one label, in order to facilitate the user to tear the paper, call
     * labelOutput to push the label paper out of the paper hatch
     * 演示打印一张标签
     * 打印单张标签后为了方便用户撕纸可调用labelOutput,将标签纸推出纸舱口
     */
    public void printOneLabel() {
        try {
            checkPrinterServiceAvailability();
            sunmiPrinterService.labelLocate();
            printLabelContent();
            sunmiPrinterService.labelOutput();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Demo printing multi label
     *
     After printing multiple labels, choose whether to push the label paper to the paper hatch according to the needs
     * 演示打印多张标签
     * 打印多张标签后根据需求选择是否推出标签纸到纸舱口
     */
    public void printMultiLabel(int num) {
        try {
            checkPrinterServiceAvailability();
            for(int i = 0; i < num; i++){
                sunmiPrinterService.labelLocate();
                printLabelContent();
            }
            sunmiPrinterService.labelOutput();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     *
     *  Custom label ticket content
     *  In the example, not all labels can be applied. In actual use, please pay attention to adapting the size of the label. You can adjust the font size and content position.
     *  自定义的标签小票内容
     *  例子中并不能适用所有标签纸，实际使用时注意要自适配标签纸大小，可通过调节字体大小，内容位置等方式
     */
    private void printLabelContent() throws RemoteException {
        sunmiPrinterService.setPrinterStyle(WoyouConsts.ENABLE_BOLD, WoyouConsts.ENABLE);
        sunmiPrinterService.lineWrap(1, null);
        sunmiPrinterService.setAlignment(0, null);
        sunmiPrinterService.printText("商品         豆浆\n", null);
        sunmiPrinterService.printText("到期时间         12-13  14时\n", null);
        sunmiPrinterService.printBarCode("{C1234567890123456",  8, 90, 2, 2, null);
        sunmiPrinterService.lineWrap(1, null);
    }
}
