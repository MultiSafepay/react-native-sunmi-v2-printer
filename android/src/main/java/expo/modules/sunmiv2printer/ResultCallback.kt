import android.os.RemoteException
import android.util.Log
import com.facebook.react.bridge.Promise
import com.sunmi.peripheral.printer.InnerResultCallback

class SunmiResultCallback : InnerResultCallback() {
  private var myPromise: Promise? = null
  private val myTag = "MyResultCallback"
  @Throws(RemoteException::class)
  override fun onRunResult(isSuccess: Boolean) {
    Log.i(myTag, "onRunResult: $isSuccess")
    if (myPromise != null) {
      myPromise?.resolve(isSuccess)
      myPromise = null
    }
  }

  @Throws(RemoteException::class)
  override fun onReturnString(result: String) {
    Log.i(myTag, "onReturnString: $result")
    if (myPromise != null) {
      myPromise?.resolve(result)
      myPromise = null
    }
  }

  @Throws(RemoteException::class)
  override fun onRaiseException(code: Int, msg: String) {
    Log.i(myTag, "onRaiseException: $msg")
    if (myPromise != null) {
      myPromise?.reject(code.toString(), msg)
      myPromise = null
    }
  }

  @Throws(RemoteException::class)
  override fun onPrintResult(code: Int, msg: String) {
    Log.i(myTag, "onPrintResult: $msg")
    if (myPromise != null) {
      myPromise?.resolve(msg)
      myPromise = null
    }
  }
}