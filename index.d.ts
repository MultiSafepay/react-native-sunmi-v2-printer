// declare const SunmiV2Printer: {
//   printerInit: (p: () => Promise<void>) => void;
//   printerSelfChecking: (p: () => Promise<void>) => void;
//   getPrinterSerialNo: (p: () => Promise<void>) => void;
//   getPrinterVersion: (p: () => Promise<void>) => void;
//   getPrinterModal: (p: () => Promise<void>) => void;
//   hasPrinter: (p: () => Promise<void>) => void;
//   getPrintedLength: (p: () => Promise<void>) => void;
//   lineWrap: (n: number, p: () => Promise<void>) => void;
//   sendRAWData: (base64EncriptedData: string, p: () => Promise<void>) => void;
//   setAlignment: (alignment: number, p: () => Promise<void>) => void;
//   setFontName: (typeface: string, p: () => Promise<void>) => void;
//   setFontSize: (fontsize: number, p: () => Promise<void>) => void;
//   printTextWithFont: (
//     text: string,
//     typeface: string,
//     fontsize: number,
//     p: () => Promise<void>
//   ) => void;
//   printColumnsText: (
//     colsTextArr: string[],
//     colsWidthArr: number[],
//     colsAlign: number[],
//     p: () => Promise<void>
//   ) => void;
//   printBitmap: (
//     data: string,
//     width: number,
//     height: number,
//     p: () => Promise<void>
//   ) => void;
//   printBarCode: (
//     data: string,
//     symbology: number,
//     height: number,
//     width: number,
//     textposition: number,
//     p: () => Promise<void>
//   ) => void;
//   printQRCode: (
//     data: string,
//     modulesize: number,
//     errorlevel: number,
//     p: () => Promise<void>
//   ) => void;
//   printOriginalText: (text: string, p: () => Promise<void>) => void;
//   commitPrinterBuffer: () => void;
//   enterPrinterBuffer: (clean: boolean) => void;
//   exitPrinterBuffer: (commit: boolean) => void;
//   printString: (message: string, p: () => Promise<void>) => void;
//   clearBuffer: () => void;
//   exitPrinterBufferWithCallback: (
//     commit: boolean,
//     callback: () => void
//   ) => void;
// };

// declare const CashDrawer: {
//   open: (callBack: () => void) => void;
//   cutPaper: (callBack: () => void) => void;
// };

declare const SunmiV2Printer: {
  printerInit: () => void;
  printBitmap: (
    base64String: string,
    width: number,
    height: number,
    orientation: 0 | 1 | 2
  ) => void;
  printOriginalText: (text: string, typeface: string) => void;
  clearBuffer: () => void;
  openCashDrawer: () => void;
  cutPaper: () => void;
};
