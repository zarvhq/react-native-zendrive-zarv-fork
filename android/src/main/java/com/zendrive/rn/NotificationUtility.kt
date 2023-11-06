package com.zendrive.rn

import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.os.Build
import android.util.Log
import androidx.core.app.NotificationCompat
import com.facebook.react.bridge.Arguments
import com.facebook.react.bridge.ReadableMap
import com.zendrive.sdk.ZendriveNotificationContainer
import java.util.Date
import org.json.JSONException
import org.json.JSONObject

const val PREFS_NAME = "zendrive-rn"
const val PREFS_KEY_NOTIFICATION_SETTINGS = "notification-settings"

fun createNotificationChannels(context: Context, channelKey: String) {
  if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
    val manager = context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
    val lowPriorityNotificationChannel = NotificationChannel(
      channelKey,
      channelKey,
      NotificationManager.IMPORTANCE_MIN
    )
    lowPriorityNotificationChannel.setShowBadge(false)
    manager.createNotificationChannel(lowPriorityNotificationChannel)
  }
}

fun createMaybeInDriveNotification(context: Context, settings: NotificationSettings): Notification {
  return NotificationCompat.Builder(context, settings.channelKey)
    .setContentTitle(settings.mayBeInDriveSettings.contentTitle)
    .setSmallIcon(iconResIdFromPath(context, settings.mayBeInDriveSettings.smallIcon))
    .setDefaults(0)
    .setPriority(NotificationCompat.PRIORITY_MIN)
    .setCategory(NotificationCompat.CATEGORY_SERVICE)
    .setContentText(settings.mayBeInDriveSettings.contentText)
    .build()
}

fun createWaitingForDriveNotification(
  context: Context,
  settings: NotificationSettings
): Notification {
  return NotificationCompat.Builder(context, settings.channelKey)
    .setSmallIcon(iconResIdFromPath(context, settings.inDriveSettings.smallIcon))
    .setCategory(NotificationCompat.CATEGORY_SERVICE)
    .setContentTitle(settings.inDriveSettings.contentTitle)
    .setContentText(settings.inDriveSettings.contentText)
    .setOnlyAlertOnce(true)
    .build()
}

fun createInDriveNotification(context: Context, settings: NotificationSettings): Notification {
  return NotificationCompat.Builder(context, settings.channelKey)
    .setSmallIcon(iconResIdFromPath(context, settings.inDriveSettings.smallIcon))
    .setCategory(NotificationCompat.CATEGORY_SERVICE)
    .setContentTitle(settings.inDriveSettings.contentTitle)
    .setContentText(settings.inDriveSettings.contentText)
    .build()
}

fun iconResIdFromPath(context: Context, name: String): Int {
  if (name.isEmpty()) {
    return context.applicationInfo.icon
  }
  val newName = name.lowercase().replace("-", "_")
  return context.resources.getIdentifier(newName, "drawable", context.packageName)
}

fun createNotification(
  context: Context,
  channelKey: String,
  title: String,
  text: String
): Notification {
  return NotificationCompat.Builder(context, channelKey)
    .setContentTitle(title)
    .setPriority(NotificationCompat.PRIORITY_MIN)
    .setCategory(NotificationCompat.CATEGORY_SERVICE)
    .setContentText(text)
    .build()
}

fun waitingForDriveNotificationContainer(context: Context): ZendriveNotificationContainer? {
  val settings = savedNotificationSettings(context)
  createNotificationChannels(context, settings.channelKey)
  if (settings.waitingForDriveSettings != null) {
    return ZendriveNotificationContainer(
      settings.waitingForDriveSettings.notificationId,
      createMaybeInDriveNotification(context, settings)
    )
  }
  return null
}

fun maybeInDriveNotificationContainer(context: Context): ZendriveNotificationContainer {
  val settings = savedNotificationSettings(context)
  createNotificationChannels(context, settings.channelKey)
  return ZendriveNotificationContainer(
    settings.mayBeInDriveSettings.notificationId,
    createMaybeInDriveNotification(context, settings)
  )
}

fun inDriveNotificationContainer(context: Context): ZendriveNotificationContainer {
  val settings = savedNotificationSettings(context)
  createNotificationChannels(context, settings.channelKey)
  return ZendriveNotificationContainer(
    settings.inDriveSettings.notificationId,
    createInDriveNotification(context, settings)
  )
}

fun zendriveNotificationContainer(
  context: Context,
  fromMap: ReadableMap?
): ZendriveNotificationContainer {
  val settings = savedNotificationSettings(context)
  val notificationConfig: NotificationConfig = notificationConfigFromRnObject(context, fromMap)
  createNotificationChannels(context, settings.channelKey)
  return ZendriveNotificationContainer(
    notificationConfig.notificationId,
    createNotification(
      context,
      settings.channelKey,
      notificationConfig.contentTitle,
      notificationConfig.contentText
    )
  )
}

data class NotificationConfig(
  val contentTitle: String = "",
  val contentText: String = "",
  val notificationId: Int = 0,
  val smallIcon: String = ""
)

data class NotificationSettings(
  val mayBeInDriveSettings: NotificationConfig,
  val waitingForDriveSettings: NotificationConfig?,
  val inDriveSettings: NotificationConfig,
  val channelKey: String
)

fun NotificationConfig.toJSONObject(): JSONObject {
  val obj = JSONObject()
  obj.put("contentTitle", this.contentTitle)
  obj.put("contentText", this.contentText)
  obj.put("notificationId", this.notificationId)
  obj.put("smallIcon", this.smallIcon)
  return obj
}

fun NotificationSettings.toJSONObject(): JSONObject {
  val obj = JSONObject()
  obj.put("mayBeInDriveSettings", this.mayBeInDriveSettings.toJSONObject())
  obj.put("inDriveSettings", this.inDriveSettings.toJSONObject())
  obj.put("waitingForDriveSettings", this.waitingForDriveSettings?.toJSONObject())
  obj.put("channelKey", this.channelKey)
  return obj
}

fun NotificationSettings.saveToPrefs(context: Context) {
  var prefs = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
  val jsonStr = this.toJSONObject().toString()
  prefs.edit().putString(PREFS_KEY_NOTIFICATION_SETTINGS, jsonStr).apply()
}

fun JSONObject.toNotificationConfig(): NotificationConfig {
  val contentTitle: String = this.get("contentTitle") as String
  val contentText: String = this.get("contentText") as String
  val smallIcon: String = this.get("smallIcon") as String
  val notificationId: Int = this.get("notificationId") as Int
  return NotificationConfig(contentTitle, contentText, notificationId, smallIcon)
}

fun JSONObject.toNotificationSettings(): NotificationSettings {
  val mayBeInDriveSettings: JSONObject = this.get("mayBeInDriveSettings") as JSONObject
  val inDriveSettings: JSONObject = this.get("inDriveSettings") as JSONObject
  val waitingForDriveSettings: JSONObject = this.get("waitingForDriveSettings") as JSONObject
  val channelKey: String = this.get("channelKey") as String
  return NotificationSettings(
    mayBeInDriveSettings = mayBeInDriveSettings.toNotificationConfig(),
    inDriveSettings = inDriveSettings.toNotificationConfig(),
    waitingForDriveSettings = waitingForDriveSettings.toNotificationConfig(),
    channelKey = channelKey
  )
}

fun String.toNotificationSettings(context: Context): NotificationSettings {
  try {
    Log.d(LOG_TAG, "Received saved notification - $this")
    val jsonObject = JSONObject(this)
    return jsonObject.toNotificationSettings()
  } catch (err: JSONException) {
    Log.d(LOG_TAG, err.toString())
  }
  Log.d(LOG_TAG, "Creating default notification setting")
  return notificationSettingsFromRnObject(context, Arguments.createMap())
}

fun savedNotificationSettings(context: Context): NotificationSettings {
  var prefs = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
  val savedSettingsStr = prefs.getString(PREFS_KEY_NOTIFICATION_SETTINGS, "")
  if (savedSettingsStr != null && savedSettingsStr != "") {
    return savedSettingsStr.toNotificationSettings(context)
  }
  return notificationSettingsFromRnObject(context, Arguments.createMap())
}

fun notificationConfigFromRnObject(context: Context, rnObject: ReadableMap?): NotificationConfig {
  var appName: String
  val applicationInfo = context.applicationInfo
  val stringId = applicationInfo.labelRes
  appName = if (stringId == 0) {
    applicationInfo.nonLocalizedLabel.toString()
  } else {
    context.getString(stringId)
  }
  var contentTitle = appName
  var contentText = ""
  var notificationId = (Date().time / 1000).toInt()
  var smallIcon = ""

  if (rnObject != null) {
    if (rnObject.hasKey("contentTitle")) {
      contentTitle = rnObject.getString("contentTitle")!!
    }
    if (rnObject.hasKey("contentText")) {
      contentText = rnObject.getString("contentText")!!
    }
    if (rnObject.hasKey("notificationId")) {
      notificationId = rnObject.getInt("notificationId")
    }
    if (rnObject.hasKey("smallIcon")) {
      smallIcon = rnObject.getString("smallIcon")!!
    }
  }
  return NotificationConfig(contentTitle, contentText, notificationId, smallIcon)
}

fun notificationSettingsFromRnObject(
  context: Context,
  rnObject: ReadableMap
): NotificationSettings {
  var mayBeInDriveSettings: ReadableMap = Arguments.createMap()
  var inDriveSettings: ReadableMap = Arguments.createMap()
  var waitingForDriveSettings: ReadableMap? = null
  var channelKey = "Zendrive"

  if (rnObject.hasKey("notificationSettings")) {
    val notificationRNObject = rnObject.getMap("notificationSettings")!!
    if (notificationRNObject.hasKey("mayBeInDriveSettings")) {
      mayBeInDriveSettings = notificationRNObject.getMap("mayBeInDriveSettings")!!
    }
    if (notificationRNObject.hasKey("inDriveSettings")) {
      inDriveSettings = notificationRNObject.getMap("inDriveSettings")!!
    }
    if (notificationRNObject.hasKey("waitingForDriveSettings")) {
      waitingForDriveSettings = notificationRNObject.getMap("waitingForDriveSettings")
    }
    if (notificationRNObject.hasKey("channelKey")) {
      channelKey = notificationRNObject.getString("channelKey")!!
    }
  }
  return NotificationSettings(
    mayBeInDriveSettings = notificationConfigFromRnObject(context, mayBeInDriveSettings),
    inDriveSettings = notificationConfigFromRnObject(context, inDriveSettings),
    waitingForDriveSettings = if (waitingForDriveSettings != null)
      notificationConfigFromRnObject(context, waitingForDriveSettings) else null,
    channelKey = channelKey
  )
}
