import { useEffect } from "react";
import { Text, View } from "react-native";
import ReactNativeSunmiV2Printer from "react-native-sunmi-v2-printer";

export default function App() {
  useEffect(() => {
    console.log(
      "SunmiV2Printer",
      ReactNativeSunmiV2Printer.SunmiV2Printer
        ? Object.keys(ReactNativeSunmiV2Printer.SunmiV2Printer)
        : undefined
    );
    console.log(
      "CashDrawer",
      ReactNativeSunmiV2Printer.CashDrawer
        ? Object.keys(ReactNativeSunmiV2Printer.CashDrawer)
        : undefined
    );
  }, []);
  return (
    <View style={{ flex: 1, alignItems: "center", justifyContent: "center" }}>
      <Text>Theme</Text>
    </View>
  );
}

// import { StyleSheet, Text, View } from 'react-native';

// import * as ReactNativeSunmiV2Printer from 'react-native-sunmi-v2-printer';

// export default function App() {
//   return (
//     <View style={styles.container}>
//       <Text>{ReactNativeSunmiV2Printer.hello()}</Text>
//     </View>
//   );
// }

// const styles = StyleSheet.create({
//   container: {
//     flex: 1,
//     backgroundColor: '#fff',
//     alignItems: 'center',
//     justifyContent: 'center',
//   },
// });
