import SunmiV2PrinterModule from "./SunmiV2PrinterModule";

/*
 *  Types
 */

export type PaperSizeOptions = "58mm" | "80mm";

export type AlignmentValues = "left" | "center" | "right";

const alignmentTranslate = {
  left: 0,
  center: 1,
  right: 2,
} as const;

/*
 *  functions
 */

async function initBind(): Promise<void> {
  return SunmiV2PrinterModule.initBind();
}

async function getPrinterDidBind(): Promise<boolean> {
  return SunmiV2PrinterModule.getPrinterDidBind();
}

async function initPrinter(): Promise<void> {
  return SunmiV2PrinterModule.initPrinter();
}

async function getPrinterServiceDidInit(): Promise<boolean> {
  return SunmiV2PrinterModule.getPrinterServiceDidInit();
}

async function getPrinterVersion(): Promise<string> {
  return SunmiV2PrinterModule.getPrinterVersion();
}

async function getPrinterPaperSize(): Promise<PaperSizeOptions> {
  return SunmiV2PrinterModule.getPrinterPaperSize();
}

async function getPrinterSpecifications(): Promise<string> {
  return SunmiV2PrinterModule.getPrinterSpecifications();
}

async function getPrinterDensity(): Promise<number> {
  return SunmiV2PrinterModule.getPrinterDensity();
}

async function getServiceVersion(): Promise<string> {
  return SunmiV2PrinterModule.getServiceVersion();
}

async function getPrinterDistance(): Promise<string> {
  return SunmiV2PrinterModule.getPrinterDistance();
}

async function showPrinterStatusToast(): Promise<string> {
  return SunmiV2PrinterModule.showPrinterStatusToast();
}

async function sendRAWData(base64EncriptedData: string): Promise<void> {
  return SunmiV2PrinterModule.sendRAWData(base64EncriptedData);
}

async function setAlignment(alignment: AlignmentValues): Promise<void> {
  return SunmiV2PrinterModule.setAlignment(alignmentTranslate[alignment]);
}

async function printBitmap(
  base64String: string,
  height: number,
  width: number
): Promise<void> {
  return SunmiV2PrinterModule.printBitmap(base64String, height, width);
}

async function printText(text: Promise<string>): Promise<void> {
  return SunmiV2PrinterModule.printText(text);
}

async function clearBuffer(): Promise<void> {
  return SunmiV2PrinterModule.clearBuffer();
}

async function openCashDrawer(): Promise<void> {
  return SunmiV2PrinterModule.openCashDrawer();
}

async function cutPaper(): Promise<string> {
  return SunmiV2PrinterModule.cutPaper();
}

export const SunmiV2Printer = {
  // NativeModules.SunmiV2Printer
  initBind,
  getPrinterDidBind,
  initPrinter,
  getPrinterServiceDidInit,
  getPrinterVersion,
  getPrinterPaperSize,
  getPrinterSpecifications,
  getPrinterDensity,
  getServiceVersion,
  getPrinterDistance,
  showPrinterStatusToast,
  sendRAWData,
  setAlignment,
  printBitmap,
  printText,
  clearBuffer,
  cutPaper,
};

export const CashDrawer = {
  openCashDrawer,
};

export default __DEV__
  ? {
    NativeModule: SunmiV2PrinterModule,
    SunmiV2Printer,
    CashDrawer,
  }
  : { SunmiV2Printer, CashDrawer };
