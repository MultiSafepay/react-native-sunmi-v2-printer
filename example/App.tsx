import { FC, useCallback, useEffect } from "react";
import {
  Pressable,
  PressableProps,
  ScrollView,
  Text,
  TextProps,
  ViewStyle,
  View,
  Alert,
} from "react-native";
import ReactNativeSunmiV2Printer, {
  SunmiV2Printer,
} from "react-native-sunmi-v2-printer";

interface ButtonProps extends PressableProps {
  label: string;
  style?: ViewStyle;
}

const multisafepayLogo = {
  base64:
    "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",
  width: 480,
  height: 854,
};

const Button: FC<ButtonProps> = ({ label, style, ...props }) => (
  <Pressable
    {...props}
    style={({ pressed }) => [
      {
        borderRadius: 8,
        padding: 5,
        backgroundColor: "#338020",
        margin: 5,
      },
      pressed && { opacity: 0.6 },
      style,
    ]}
  >
    <Text style={{ color: "white" }}>{label}</Text>
  </Pressable>
);

const Title: FC<TextProps> = ({ style, ...props }) => (
  <Text
    {...props}
    style={[{ fontSize: 20, fontWeight: "bold", marginVertical: 10 }, style]}
  />
);

export default function App() {
  useEffect(() => {
    console.log(
      "ReactNativeSunmiV2Printer module ",
      ReactNativeSunmiV2Printer?.NativeModule &&
        Object.keys(ReactNativeSunmiV2Printer?.NativeModule)
    );
    console.log(
      "ReactNativeSunmiV2Printer CashDrawer",
      ReactNativeSunmiV2Printer?.CashDrawer &&
        Object.keys(ReactNativeSunmiV2Printer?.CashDrawer)
    );
    console.log(
      "ReactNativeSunmiV2Printer SunmiV2Printer",
      ReactNativeSunmiV2Printer?.SunmiV2Printer &&
        Object.keys(ReactNativeSunmiV2Printer?.SunmiV2Printer)
    );
  }, []);

  const setupSunmiPrinter = useCallback(async () => {
    if (SunmiV2Printer) {
      const isBinded = await SunmiV2Printer.getPrinterDidBind();
      if (!isBinded) {
        await SunmiV2Printer.initBind();
      }
      const serviceDidInit = await SunmiV2Printer.getPrinterServiceDidInit();
      if (!serviceDidInit) {
        await SunmiV2Printer.initPrinter();
      }
    } else {
      console.warn("No Sunmi device");
    }
  }, []);

  useEffect(() => {
    void setupSunmiPrinter();
  }, [setupSunmiPrinter]);

  return (
    <ScrollView
      contentContainerStyle={{
        alignItems: "center",
        justifyContent: "center",
        backgroundColor: "777",
      }}
    >
      <Title>NATIVE METHODS</Title>
      <View>
        <Button
          label="Get printer version"
          onPress={async () => {
            try {
              const printerVersion = await SunmiV2Printer.getPrinterVersion();
              Alert.alert("Printer version", printerVersion);
            } catch (e) {
              console.error(e);
            }
          }}
        />
        <Button
          label="Print logo"
          onPress={async () => {
            try {
              await SunmiV2Printer.printBitmap(
                multisafepayLogo.base64,
                multisafepayLogo.width,
                multisafepayLogo.height
              );
            } catch (e) {
              console.error(e);
            }
          }}
        />
        <Button
          label="Cut paper"
          onPress={async () => {
            try {
              await SunmiV2Printer.cutPaper();
            } catch (e) {
              console.error(e);
            }
          }}
        />
      </View>
      <Title>ACTIONS</Title>
      <View>
        <Text>CashDrawer actions</Text>
        {ReactNativeSunmiV2Printer?.CashDrawer &&
          Object.keys(ReactNativeSunmiV2Printer?.CashDrawer).map((key) => {
            return <Button label={key} key={key} />;
          })}

        <Text>SunmiV2Printer actions</Text>
        {ReactNativeSunmiV2Printer?.SunmiV2Printer &&
          Object.keys(ReactNativeSunmiV2Printer?.SunmiV2Printer).map((key) => {
            return <Button label={key} key={key} />;
          })}

        <Text>NativeModule actions</Text>
        {ReactNativeSunmiV2Printer?.NativeModule &&
          Object.keys(ReactNativeSunmiV2Printer?.NativeModule).map((key) => {
            return <Button label={key} key={key} />;
          })}
      </View>
    </ScrollView>
  );
}
