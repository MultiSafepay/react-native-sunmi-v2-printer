import ReactNativeSunmiV2PrinterModule from "./ReactNativeSunmiV2PrinterModule";

/*
 *  Types
 */

export type PaperSizeOptions = "58mm" | "80mm";

type AlignmentValues = "left" | "center" | "right";

const alignmentTranslate = {
  left: 0,
  center: 1,
  right: 2,
} as const;

/*
 *  functions
 */

export async function getTheme(): Promise<string> {
  // TODO check if could be sync
  return ReactNativeSunmiV2PrinterModule.getTheme();
}

export async function initBind(): Promise<void> {
  // TODO check if could be sync
  return ReactNativeSunmiV2PrinterModule.initBind();
}

export async function getPrinterDidBind(): Promise<boolean> {
  // TODO check if could be sync
  return ReactNativeSunmiV2PrinterModule.getPrinterDidBind();
}

export async function initPrinter(): Promise<void> {
  // TODO check if could be sync
  return ReactNativeSunmiV2PrinterModule.initPrinter();
}

export async function getPrinterServiceDidInit(): Promise<boolean> {
  // TODO check if could be sync
  return ReactNativeSunmiV2PrinterModule.getPrinterServiceDidInit();
}

export async function getPrinterVersion(): Promise<string> {
  // TODO check if could be sync
  return ReactNativeSunmiV2PrinterModule.getPrinterVersion();
}

export async function getPrinterPaperSize(): Promise<PaperSizeOptions> {
  // TODO check if could be sync
  return ReactNativeSunmiV2PrinterModule.getPrinterPaperSize();
}

export async function getPrinterSpecifications(): Promise<string> {
  // TODO check if could be sync
  return ReactNativeSunmiV2PrinterModule.getPrinterSpecifications();
}

export async function getPrinterDensity(): Promise<number> {
  // TODO check if could be sync
  return ReactNativeSunmiV2PrinterModule.getPrinterDensity();
}

export async function getServiceVersion(): Promise<string> {
  // TODO check if could be sync
  return ReactNativeSunmiV2PrinterModule.getServiceVersion();
}

export async function getPrinterDistance(): Promise<string> {
  // TODO check if could be sync
  return ReactNativeSunmiV2PrinterModule.getPrinterDistance();
}

export async function showPrinterStatusToast(): Promise<string> {
  // TODO check if could be sync
  return ReactNativeSunmiV2PrinterModule.showPrinterStatusToast();
}

export async function sendRAWData(base64EncriptedData: string): Promise<void> {
  // TODO check if could be sync
  return ReactNativeSunmiV2PrinterModule.sendRAWData(base64EncriptedData);
}

export async function setAlignment(alignment: AlignmentValues): Promise<void> {
  // TODO check if could be sync
  return ReactNativeSunmiV2PrinterModule.setAlignment(
    alignmentTranslate[alignment]
  );
}

export async function printBitmap(
  // TODO check if could be sync
  base64String: Promise<string>,
  height: number,
  width: number
): Promise<void> {
  return ReactNativeSunmiV2PrinterModule.printBitmap(
    base64String,
    height,
    width
  );
}

export async function printText(text: Promise<string>): Promise<void> {
  // TODO check if could be sync
  return ReactNativeSunmiV2PrinterModule.printText(text);
}

export async function clearBuffer(): Promise<void> {
  // TODO check if could be sync
  return ReactNativeSunmiV2PrinterModule.clearBuffer();
}

export async function openCashDrawer(): Promise<void> {
  // TODO check if could be sync
  return ReactNativeSunmiV2PrinterModule.openCashDrawer();
}

export async function cutPaper(): Promise<string> {
  // TODO check if could be sync
  return ReactNativeSunmiV2PrinterModule.cutPaper();
}

const SunmiV2Printer = {
  // NativeModules.SunmiV2Printer
  getTheme,
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
  openCashDrawer,
  cutPaper,
};
const CashDrawer = {
  // TODO was NativeModules.CashDrawer
};

export default { SunmiV2Printer, CashDrawer };

/*
 * old template
 */

// // Get the native constant value.
// export const PI = ReactNativeSunmiV2PrinterModule.PI;

// export function hello(): string {
//   return ReactNativeSunmiV2PrinterModule.hello();
// }

// export async function setValueAsync(value: string) {
//   return await ReactNativeSunmiV2PrinterModule.setValueAsync(value);
// }

// const emitter = new EventEmitter(ReactNativeSunmiV2PrinterModule ?? NativeModulesProxy.ReactNativeSunmiV2Printer);

// export function addChangeListener(listener: (event: ChangeEventPayload) => void): Subscription {
//   return emitter.addListener<ChangeEventPayload>('onChange', listener);
// }
