export declare const SunmiV2Printer: {
  initBind: () => Promise<void>;
  getPrinterDidBind: () => Promise<boolean>;
  initPrinter: () => Promise<void>;
  getPrinterServiceDidInit: () => Promise<boolean>;
  getPrinterVersion: () => Promise<string>;
  getPrinterPaperSize: () => Promise<"58mm" | "80mm">;
  sendRAWData: (base64EncriptedData: string) => Promise<void>;
  setAlignment: (alignment: number) => Promise<void>;
  printBitmap: (
    base64String: string,
    width: number,
    height: number
  ) => Promise<void>;
  printOriginalText: (text: string, typeface: string) => Promise<void>;
  clearBuffer: () => Promise<void>;
  openCashDrawer: () => Promise<void>;
  cutPaper: () => Promise<void>;
};
