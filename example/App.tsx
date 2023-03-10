import { FC, useEffect } from "react";
import {
  Pressable,
  PressableProps,
  ScrollView,
  Text,
  TextProps,
  ViewStyle,
} from "react-native";
import ReactNativeSunmiV2Printer from "react-native-sunmi-v2-printer";

interface ButtonProps extends PressableProps {
  label: string;
  style?: ViewStyle;
}

const Button: FC<ButtonProps> = ({ label, style, ...props }) => (
  <Pressable
    {...props}
    style={[
      {
        borderRadius: 25,
        padding: 10,
        backgroundColor: "#338020",
        margin: 5,
      },
      style,
    ]}
  >
    <Text style={{ color: "white" }}>{label}</Text>
  </Pressable>
);

const title: FC<TextProps> = ({ style, ...props }) => (
  <Text style={[{ fontSize: 20 }, style]} />
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

  return (
    <ScrollView
      contentContainerStyle={{
        alignItems: "center",
        justifyContent: "center",
        backgroundColor: "777",
      }}
    >
      <Text>CashDrawer actions</Text>
      {ReactNativeSunmiV2Printer?.CashDrawer &&
        Object.keys(ReactNativeSunmiV2Printer?.CashDrawer).map((key) => {
          return <Button label={key} />;
        })}

      <Text>SunmiV2Printer actions</Text>
      {ReactNativeSunmiV2Printer?.SunmiV2Printer &&
        Object.keys(ReactNativeSunmiV2Printer?.SunmiV2Printer).map((key) => {
          return <Button label={key} />;
        })}

      <Text>NativeModule actions</Text>
      {ReactNativeSunmiV2Printer?.NativeModule &&
        Object.keys(ReactNativeSunmiV2Printer?.NativeModule).map((key) => {
          return <Button label={key} />;
        })}
    </ScrollView>
  );
}
