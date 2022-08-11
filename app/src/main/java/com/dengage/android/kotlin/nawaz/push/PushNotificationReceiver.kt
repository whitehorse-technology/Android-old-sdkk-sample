package com.dengage.android.kotlin.nawaz.push

import android.annotation.SuppressLint
import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.media.AudioAttributes
import android.os.Build
import android.util.Log
import android.widget.RemoteViews
import android.widget.Toast
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import com.dengage.android.kotlin.nawaz.R
import com.dengage.sdk.Constants
import com.dengage.sdk.NotificationReceiver
import com.dengage.sdk.Utils
import com.dengage.sdk.callback.DengageCallback
import com.dengage.sdk.models.DengageError
import com.dengage.sdk.models.Message

/**
 * Created by Batuhan Coskun on 19 December 2020
 */



class PushNotificationReceiver : NotificationReceiver() {

    @SuppressLint("UnspecifiedImmutableFlag", "LaunchActivityFromNotification")
    override fun onCarouselRender(context: Context, intent: Intent?, message: Message?) {
        super.onCarouselRender(context, intent, message)

        val items = message?.carouselContent
        if (items.isNullOrEmpty() || intent == null) return
        val size = items.size
        val current = 0
        val left = (current - 1 + size) % size
        val right = (current + 1) % size

        val itemTitle = items[current].title
        val itemDesc = items[current].description

        // set intents (right button, left button, item click)
        val itemIntent = getItemClickIntent(intent.extras, context.packageName)
        val leftIntent = getLeftItemIntent(intent.extras, context.packageName)
        val rightIntent = getRightItemIntent(intent.extras, context.packageName)
        val deleteIntent = getDeleteIntent(intent.extras, context.packageName)
        val contentIntent = getContentIntent(intent.extras, context.packageName)

        val pendingFlags = PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
        val carouseItemIntent = PendingIntent.getBroadcast(
            context, 0,
            itemIntent,pendingFlags
        )
        val carouselLeftIntent = PendingIntent.getBroadcast(
            context, 1,
            leftIntent, pendingFlags
        )
        val carouselRightIntent = PendingIntent.getBroadcast(
            context, 2,
            rightIntent, pendingFlags
        )
        val deletePendingIntent = PendingIntent.getBroadcast(
            context, 4,
            deleteIntent, pendingFlags
        )
        val contentPendingIntent = PendingIntent.getBroadcast(
            context, 5,
            contentIntent, pendingFlags
        )

        // set views for the layout
        val collapsedView = RemoteViews(
            context.packageName,
            R.layout.den_carousel_collapsed
        )
        collapsedView.setTextViewText(R.id.den_carousel_title, message.title)
        collapsedView.setTextViewText(R.id.den_carousel_message, message.message)
        val carouselView = RemoteViews(
            context.packageName,
            R.layout.den_carousel_portrait
        )
        carouselView.setTextViewText(R.id.den_carousel_title, message.title)
        carouselView.setTextViewText(R.id.den_carousel_message, message.message)
        carouselView.setTextViewText(R.id.den_carousel_item_title, itemTitle)
        carouselView.setTextViewText(R.id.den_carousel_item_description, itemDesc)

        carouselView.setOnClickPendingIntent(R.id.den_carousel_left_arrow, carouselLeftIntent)
        carouselView.setOnClickPendingIntent(
            R.id.den_carousel_portrait_current_image,
            carouseItemIntent
        )
        carouselView.setOnClickPendingIntent(R.id.den_carousel_item_title, carouseItemIntent)
        carouselView.setOnClickPendingIntent(R.id.den_carousel_item_description, carouseItemIntent)
        carouselView.setOnClickPendingIntent(R.id.den_carousel_right_arrow, carouselRightIntent)

        val channelId = createNotificationChannel(context, message , true )

        // set views for the notification
        val notification = NotificationCompat.Builder(context, channelId)
            .setSmallIcon(R.mipmap.ic_launcher)
            .setCustomContentView(collapsedView)
            .setCustomBigContentView(carouselView)
            .setContentIntent(contentPendingIntent)
            .setDeleteIntent(deletePendingIntent)
            .build()


        // --------- Behavior-1 ---------
        /*Utils.loadCarouselImageToView(
            carouselView,
            R.id.den_carousel_portrait_left_image,
            items[left]
        )
        Utils.loadCarouselImageToView(
            carouselView,
            R.id.den_carousel_portrait_current_image,
            items[current]
        )
        Utils.loadCarouselImageToView(
            carouselView,
            R.id.den_carousel_portrait_right_image,
            items[right]
        )

        // show message
        val notificationManager = NotificationManagerCompat.from(context)
        notificationManager.notify(
            message.messageSource,
            message.messageId,
            notification
        )*/
        // --------- Behavior-1 ---------


        // You can use alternate behavior, if carousel image download bug occurs
        // Comment Behavior-1 and uncomment Behavior-2
        // --------- Behavior-2 ---------
        Utils.loadCarouselContents(
            message.carouselContent,
            object : DengageCallback<Array<Bitmap>> {
                override fun onError(error: DengageError) {
                    Toast.makeText(context, error.errorMessage ?: "", Toast.LENGTH_LONG).show()
                    Log.e("NotificationReceiver", error.errorMessage ?: "")
                }

                override fun onResult(result: Array<Bitmap>) {
                    carouselView.setImageViewBitmap(
                        R.id.den_carousel_portrait_left_image,
                        result[left]
                    )
                    carouselView.setImageViewBitmap(
                        R.id.den_carousel_portrait_current_image,
                        result[current]
                    )
                    carouselView.setImageViewBitmap(
                        R.id.den_carousel_portrait_right_image,
                        result[right]
                    )

                    // show message
                    val id = message.hashCode()

                    val notificationManager = NotificationManagerCompat.from(context)
                    notificationManager.notify(
                        message.messageSource,
                        message.messageId,
                        notification
                    )
                }
            })
        // --------- Behavior-2 ---------

    }

    override fun onCarouselReRender(context: Context, intent: Intent?, message: Message?) {
        super.onCarouselReRender(context, intent, message)

        val items = message?.carouselContent
        if (items.isNullOrEmpty() || intent == null) return
        val bundle = intent.extras
        val prevIndex = bundle?.getInt("current")
        val navigation = bundle?.getString("navigation", "right")
        val size = items.size
        val current = if (navigation.equals("right")) {
            ((prevIndex ?: 0) + 1) % size
        } else {
            ((prevIndex ?: 0) - 1 + size) % size
        }
        val right = (current + 1) % size
        val left = (current - 1 + size) % size
        intent.putExtra("current", current)

        val itemTitle = items[current].title
        val itemDesc = items[current].description

        // set intents (next button, rigth button and item click)
        val itemIntent = getItemClickIntent(intent.extras, context.packageName)
        val leftIntent = getLeftItemIntent(intent.extras, context.packageName)
        val rightIntent = getRightItemIntent(intent.extras, context.packageName)
        val deleteIntent = getDeleteIntent(intent.extras, context.packageName)
        val contentIntent = getContentIntent(intent.extras, context.packageName)

        val pendingFlags = PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
        val carouseItemIntent = PendingIntent.getBroadcast(
            context, 0,
            itemIntent, pendingFlags
        )
        val carouselLeftIntent = PendingIntent.getBroadcast(
            context, 1,
            leftIntent, pendingFlags
        )
        val carouselRightIntent = PendingIntent.getBroadcast(
            context, 2,
            rightIntent, pendingFlags
        )
        val deletePendingIntent = PendingIntent.getBroadcast(
            context, 4,
            deleteIntent, pendingFlags
        )
        val contentPendingIntent = PendingIntent.getBroadcast(
            context, 5,
            contentIntent, pendingFlags
        )

        // set views for the layout
        val collapsedView = RemoteViews(
            context.packageName,
            R.layout.den_carousel_collapsed
        )
        collapsedView.setTextViewText(R.id.den_carousel_title, message.title)
        collapsedView.setTextViewText(R.id.den_carousel_message, message.message)
        val carouselView = RemoteViews(
            context.packageName,
            R.layout.den_carousel_portrait
        )
        carouselView.setTextViewText(R.id.den_carousel_title, message.title)
        carouselView.setTextViewText(R.id.den_carousel_message, message.message)
        carouselView.setTextViewText(R.id.den_carousel_item_title, itemTitle)
        carouselView.setTextViewText(R.id.den_carousel_item_description, itemDesc)

        carouselView.setOnClickPendingIntent(R.id.den_carousel_left_arrow, carouselLeftIntent)
        carouselView.setOnClickPendingIntent(
            R.id.den_carousel_portrait_current_image,
            carouseItemIntent
        )
        carouselView.setOnClickPendingIntent(R.id.den_carousel_item_title, carouseItemIntent)
        carouselView.setOnClickPendingIntent(R.id.den_carousel_item_description, carouseItemIntent)
        carouselView.setOnClickPendingIntent(R.id.den_carousel_right_arrow, carouselRightIntent)

        val channelId = createNotificationChannel(context, message , false)

        // set your views for the notification
        val notification = NotificationCompat.Builder(context, channelId)
            .setSmallIcon(R.mipmap.ic_launcher)
            .setCustomContentView(collapsedView)
            .setCustomBigContentView(carouselView)
            .setContentIntent(contentPendingIntent)
            .setDeleteIntent(deletePendingIntent)
            .build()
        // show message again silently with next,prev and current item.
        notification.flags = Notification.FLAG_AUTO_CANCEL or Notification.FLAG_ONLY_ALERT_ONCE


        // --------- Behavior-1 ---------
        /*Utils.loadCarouselImageToView(
            carouselView,
            R.id.den_carousel_portrait_left_image,
            items[left]
        )
        Utils.loadCarouselImageToView(
            carouselView,
            R.id.den_carousel_portrait_current_image,
            items[current]
        )
        Utils.loadCarouselImageToView(
            carouselView,
            R.id.den_carousel_portrait_right_image,
            items[right]
        )

        // show message
        val notificationManager = NotificationManagerCompat.from(context)
        notificationManager.notify(
            message.messageSource,
            message.messageId,
            notification
        )*/
        // --------- Behavior-1 ---------


        // You can use alternate behavior, if carousel image download bug occurs
        // Comment Behavior-1 and uncomment Behavior-2
        // --------- Behavior-2 ---------
        Utils.loadCarouselContents(
            message.carouselContent,
            object : DengageCallback<Array<Bitmap>> {
                override fun onError(error: DengageError) {
                    Toast.makeText(context, error.errorMessage ?: "", Toast.LENGTH_LONG).show()
                    Log.e("NotificationReceiver", error.errorMessage ?: "")
                }

                override fun onResult(result: Array<Bitmap>) {
                    carouselView.setImageViewBitmap(
                        R.id.den_carousel_portrait_left_image,
                        result[left]
                    )
                    carouselView.setImageViewBitmap(
                        R.id.den_carousel_portrait_current_image,
                        result[current]
                    )
                    carouselView.setImageViewBitmap(
                        R.id.den_carousel_portrait_right_image,
                        result[right]
                    )

                    // show message

                    val id = message.hashCode()

                    val notificationManager = NotificationManagerCompat.from(context)
                    notificationManager.notify(
                        message.messageSource,
                        message.messageId,
                        notification
                    )
                }
            })
        // --------- Behavior-2 ---------
    }

    private fun createNotificationChannel(context: Context?, message: Message? , isRender :Boolean): String {
        // generate new channel id for different sounds
        val soundUri = Utils.getSound(context, message?.sound)
        val channelId = "112211"
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val notificationManager =
                context!!.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

            if(isRender){
                // delete old notification channels
                val channels = notificationManager.notificationChannels
                if (channels != null && channels.size > 0) {
                    for (channel in channels) {
                        if(!channel.id.equals(Constants.CHANNEL_ID)) {
                            notificationManager.deleteNotificationChannel(channel.id)
                        }
                    }
                }
            }
            val notificationChannel = NotificationChannel(
                channelId,
                "Channel Name",
                NotificationManager.IMPORTANCE_DEFAULT
            )
            val audioAttributes = AudioAttributes.Builder()
                .setContentType(AudioAttributes.CONTENT_TYPE_SONIFICATION)
                .setUsage(AudioAttributes.USAGE_NOTIFICATION)
                .build()
            notificationChannel.setSound(soundUri, audioAttributes)
            notificationManager.createNotificationChannel(notificationChannel)
        }
        return channelId
    }

}