import { NativeModules } from "react-native";

const SunmiV2Printer = NativeModules.SunmiV2Printer;
const CashDrawer = NativeModules.CashDrawer;

/**
 * From sunmi docs in https://docs.sunmi.com/en-US/cdixeghjk491/xdideghjk524
 *   Get the printer’s latest status
 *   Function: String updatePrinterState ()
 *   Return value:
 *      1 → Printer is under normal operation
 *      2 → Printer is under preparation
 *      3 → Communication is abnormal
 *      4 → Out of paper
 *      5 → Overheated
 *      6 → Cover is open
 *      7 → Cutter error
 *      8 → Cutter recovered
 *      9 → Black mark not detected
 *      505 → Printer not detected
 *      507 → Printer firmware update failed
 *   Note 1: these return values are applicable to all SUNMI devices, but some status can’t be obtained due to hardware configuration. For example, cover open detection is not applicable to handheld devices.
 */
const PrinterServiceAvailability = {
  1: "printerIsUnderNormalOperation",
  2: "printerIsUnderPreparation",
  3: "communicationIsAbnormal",
  4: "outOfPaper",
  5: "overheated",
  6: "coverIsOpen",
  7: "cutterError",
  8: "cutterRecovered",
  9: "blackMarkNotDetected",
  505: "printerNotDetected",
  507: "printerFirmware",
};

module.exports = { SunmiV2Printer, CashDrawer, PrinterServiceAvailability };
