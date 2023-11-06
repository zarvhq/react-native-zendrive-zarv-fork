package com.zendrive.rn

import android.view.View
import com.facebook.react.ReactPackage
import com.facebook.react.bridge.NativeModule
import com.facebook.react.bridge.ReactApplicationContext
import com.facebook.react.uimanager.ReactShadowNode
import com.facebook.react.uimanager.ViewManager

class RNZendrivePackage : ReactPackage {
  override fun createNativeModules(
    reactContext: ReactApplicationContext
  ): List<NativeModule> {
    return arrayListOf(
      RNZendriveModule(reactContext),
      RNZendriveInsuranceModule(reactContext),
      RNZendriveFeedbackModule(reactContext),
      RNZendriveDebugModule(reactContext),
      RNZendrivePermissionsModule(reactContext)
    )
  }

  override fun createViewManagers(
    reactContext: ReactApplicationContext
  ): List<ViewManager<View, ReactShadowNode<*>>> {
    return arrayListOf()
  }
}
