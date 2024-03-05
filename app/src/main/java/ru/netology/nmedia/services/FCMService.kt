package ru.netology.nmedia.services

import android.Manifest
import android.annotation.SuppressLint
import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.content.pm.PackageManager
import android.os.Build
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage
import com.google.gson.Gson
import ru.netology.nmedia.R
import kotlin.random.Random

@SuppressLint("MissingFirebaseInstanceTokenRefresh")
class FCMService : FirebaseMessagingService() {
    private val action = "action"
    private val content = "content"
    private val contentPreviewLength = 40
    private val channelId = "remote"
    private val gson = Gson()

    override fun onCreate() {
        super.onCreate()
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val name = getString(R.string.channel_remote_name)
            val descriptionText = getString(R.string.channel_remote_description)
            val importance = NotificationManager.IMPORTANCE_DEFAULT
            val channel = NotificationChannel(channelId, name, importance).apply {
                description = descriptionText
            }
            val manager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            manager.createNotificationChannel(channel)
        }
    }

    override fun onMessageReceived(message: RemoteMessage) {
        message.data[action]?.let {
            if (Action.entries.count { action ->
                    action.toString() == it
                } == 1) {
                when (Action.valueOf(it)) {
                    Action.LIKE -> handleLike(gson.fromJson(message.data[content], Like::class.java))
                    Action.NEW_POST -> handleNewPost(gson.fromJson(message.data[content], NewPost::class.java))
                }
            }
        }
    }

    @SuppressLint("StringFormatInvalid")
    private fun handleLike(content: Like) {
        val notification = NotificationCompat.Builder(this, channelId)
            .setSmallIcon(R.drawable.ic_notification)
            .setContentTitle(
                getString(
                    R.string.notification_user_liked,
                    content.userName,
                    content.postAuthor,
                )
            )
            .setPriority(NotificationCompat.PRIORITY_DEFAULT)
            .build()

        notify(notification)
    }

    @SuppressLint("StringFormatInvalid")
    private fun handleNewPost(post: NewPost) {
        val notification = NotificationCompat.Builder(this, channelId)
            .setSmallIcon(R.drawable.ic_notification)
            .setContentTitle(
                getString(
                    R.string.notification_user_new_post,
                    post.authorName
                )
            )
            .setContentText(if (post.content.length > contentPreviewLength) "${post.content.substring(0, contentPreviewLength)}..." else post.content)
            .setStyle(NotificationCompat.BigTextStyle()
                .bigText(post.content))
            .setPriority(NotificationCompat.PRIORITY_DEFAULT)
            .build()

        notify(notification)
    }

    override fun onNewToken(token: String) {
            println("Token: $token")
    }

    private fun notify(notification: Notification) {
        if (
            Build.VERSION.SDK_INT < Build.VERSION_CODES.TIRAMISU ||
            checkSelfPermission(
                Manifest.permission.POST_NOTIFICATIONS
            ) == PackageManager.PERMISSION_GRANTED
        ) {
            NotificationManagerCompat.from(this)
                .notify(Random.nextInt(100_000), notification)
        }
    }
}

enum class Action {
    LIKE,
    NEW_POST,
}

data class Like(
    val userId: Long,
    val userName: String,
    val postId: Long,
    val postAuthor: String,
)

data class NewPost(
    val authorId: Long,
    val authorName: String,
    val content: String,
)