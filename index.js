import { NativeModules } from "react-native";

const SunmiV2Printer = NativeModules.SunmiV2Printer;
const CashDrawer = NativeModules.CashDrawer;

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
