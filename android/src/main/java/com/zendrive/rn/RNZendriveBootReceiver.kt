package com.zendrive.rn

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import com.facebook.react.bridge.Arguments

class RNZendriveBootReceiver : BroadcastReceiver() {
  override fun onReceive(context: Context?, intent: Intent?) {
    var isBootReceivedAction = false
    var isPackageReplacedAction = false
    intent?.let {
      if (it.action == Intent.ACTION_MY_PACKAGE_REPLACED) {
        isPackageReplacedAction = true
      }
      if (it.action == Intent.ACTION_BOOT_COMPLETED) {
        isBootReceivedAction = true
      }
    }
    if (isBootReceivedAction) {
      val map = Arguments.createMap()
      notifyJS(context, this, "com.zendrive.onBootCompleted", map)
    }
    if (isPackageReplacedAction) {
      val map = Arguments.createMap()
      notifyJS(context, this, "com.zendrive.onMyPackageReplaced", map)
    }
  }
}
