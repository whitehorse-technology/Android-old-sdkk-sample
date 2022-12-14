package com.dengage.sdk.util

object Constants {

    const val PREFS_NAME = "___DEN_DEVICE_UNIQUE_ID___"
    const val DATE_FORMAT = "yyyy-MM-dd'T'HH:mm:ss.SSS'Z'"
    const val PUSH_API_URI = "https://push.dengage.com"
    const val EVENT_API_URI = "https://event.dengage.com"
    const val MESSAGE_SOURCE = "DENGAGE"

    const val PUSH_ITEM_CLICK_EVENT = "com.dengage.push.intent.ITEM_CLICK"
    const val PUSH_RECEIVE_EVENT = "com.dengage.push.intent.RECEIVE"
    const val PUSH_OPEN_EVENT = "com.dengage.push.intent.OPEN"
    const val PUSH_DELETE_EVENT = "com.dengage.push.intent.DELETE"
    const val PUSH_ACTION_CLICK_EVENT = "com.dengage.push.intent.ACTION_CLICK"
    const val NOTIFICATION_CHANNEL_ID = "3374143"
    const val NOTIFICATION_CHANNEL_NAME = "General"

     const val GEOFENCE_API_URI = "https://dev-push.dengage.com/geoapi/"
     const val BUBBLE_MOVING_GEOFENCE_REQUEST_ID = "dengage_moving"
     const val BUBBLE_STOPPED_GEOFENCE_REQUEST_ID = "dengage_stopped"
     const val SYNCED_GEOFENCES_REQUEST_ID_PREFIX = "dengage_sync"
     const val DESIRED_MOVING_UPDATE_INTERVAL = 150
     const val FASTEST_MOVING_UPDATE_INTERVAL = 30
     const val DESIRED_SYNC_INTERVAL = 20
     const val STOP_DURATION = 140
     const val STOP_DISTANCE = 70
     const val STOPPED_GEOFENCE_RADIUS = 100
     const val MOVING_GEOFENCE_RADIUS = 100

    const val LOCATION_PERMISSION_REQUEST_CODE = 19200423
    const val BACKGROUND_LOCATION_PERMISSION_REQUEST_CODE = 19231029
    const val GEOFENCE_MAX_MONITOR_COUNT = 50
    const val GEOFENCE_MAX_FETCH_INTERVAL_MILISECONDS = (1 * 1 * 1).toLong()
    const val GEOFENCE_MAX_EVENT_SIGNAL_INTERVAL_MILISECONDS = (5 * 60 * 1000).toLong()
    const val GEOFENCE_FETCH_HISTORY_MAX_COUNT = 100
    const val GEOFENCE_EVENT_HISTORY_MAX_COUNT = 100
}