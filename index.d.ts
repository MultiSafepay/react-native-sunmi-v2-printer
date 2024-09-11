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
  getPrinterStatus: () => Promise<
    1 | 2 | 3 | 4 | 5 | 6 | 7 | 8 | 9 | 505 | 507
  >;
};

export type PrinterStatusType =
  | "printerIsUnderNormalOperation"
  | "printerIsUnderPreparation"
  | "communicationIsAbnormal"
  | "outOfPaper"
  | "overheated"
  | "coverIsOpen"
  | "cutterError"
  | "cutterRecovered"
  | "blackMarkNotDetected"
  | "printerNotDetected"
  | "printerFirmware";

export declare const PrinterServiceAvailability: {
  1: string;
  2: string;
  3: string;
  4: string;
  5: string;
  6: string;
  7: string;
  8: string;
  9: string;
  505: string;
  507: string;
};
