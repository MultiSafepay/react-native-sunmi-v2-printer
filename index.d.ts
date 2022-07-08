export declare const SunmiV2Printer: {
  initBind: () => Promise<void>;
  getPrinterDidBind: () => Promise<boolean>;
  initPrinter: () => Promise<void>;
  getPrinterServiceDidInit: () => Promise<boolean>;
  getPrinterVersion: () => Promise<string>;
  getPrinterPaperSize: () => Promise<"58mm" | "80mm">;
  getPrinterDistance: () => Promise<string>;
  getPrinterDensity: () => Promise<number>;
  getPrinterSpecifications: () => Promise<string>;
  getServiceVersion: () => Promise<string>;
  sendRAWData: (base64EncriptedData: string) => Promise<void>;
  setAlignment: (alignment: number) => Promise<void>;
  printBitmap: (
    base64String: string,
    width: number,
    height: number
  ) => Promise<void>;
  printText: (text: string) => Promise<void>;
  clearBuffer: () => Promise<void>;
  openCashDrawer: () => Promise<void>;
  cutPaper: () => Promise<void>;
};
