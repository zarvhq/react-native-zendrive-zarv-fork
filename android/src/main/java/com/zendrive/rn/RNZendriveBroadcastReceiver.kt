package com.zendrive.rn

import android.content.Context
import com.facebook.react.bridge.Arguments
import com.zendrive.sdk.AccidentInfo
import com.zendrive.sdk.AnalyzedDriveInfo
import com.zendrive.sdk.DriveResumeInfo
import com.zendrive.sdk.DriveStartInfo
import com.zendrive.sdk.EstimatedDriveInfo
import com.zendrive.sdk.ZendriveBroadcastReceiver

class RNZendriveBroadcastReceiver : ZendriveBroadcastReceiver() {

  override fun onDriveStart(context: Context?, data: DriveStartInfo?) {
    notifyJS(context, this, "com.zendrive.onDriveStart", data?.toRnObject())
  }

  override fun onZendriveSettingsConfigChanged(
    context: Context?,
    errorsFound: Boolean,
    warningsFound: Boolean
  ) {
    val map = Arguments.createMap()
    map.putBoolean("errorsFound", errorsFound)
    map.putBoolean("warningsFound", warningsFound)
    notifyJS(context, this, "com.zendrive.onSettingsChanged", map)
  }

  override fun onPotentialAccident(context: Context?, data: AccidentInfo?) {
    notifyJS(context, this, "com.zendrive.onPotentialAccident", data?.toRnObject())
  }

  override fun onAccident(context: Context?, data: AccidentInfo?) {
    notifyJS(context, this, "com.zendrive.onAccident", data?.toRnObject())
  }

  override fun onDriveAnalyzed(context: Context?, data: AnalyzedDriveInfo?) {
    notifyJS(context, this, "com.zendrive.onDriveAnalyzed", data?.toRnObject())
  }

  override fun onDriveResume(context: Context?, data: DriveResumeInfo?) {
    notifyJS(context, this, "com.zendrive.onDriveResume", data?.toRnObject())
  }

  override fun onDriveEnd(context: Context?, data: EstimatedDriveInfo?) {
    notifyJS(context, this, "com.zendrive.onDriveEnd", data?.toRnObject())
  }
}
