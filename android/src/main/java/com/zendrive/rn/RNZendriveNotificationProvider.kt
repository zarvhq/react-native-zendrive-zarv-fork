package com.zendrive.rn

import android.content.Context
import com.zendrive.sdk.ZendriveNotificationContainer
import com.zendrive.sdk.ZendriveNotificationProvider

class RNZendriveNotificationProvider : ZendriveNotificationProvider {
  override fun getWaitingForDriveNotificationContainer(
    context: Context
  ): ZendriveNotificationContainer? {
    return waitingForDriveNotificationContainer(context)
  }

  override fun getMaybeInDriveNotificationContainer(
    context: Context
  ): ZendriveNotificationContainer {
    return maybeInDriveNotificationContainer(context)
  }

  override fun getInDriveNotificationContainer(
    context: Context
  ): ZendriveNotificationContainer {
    return inDriveNotificationContainer(context)
  }
}
