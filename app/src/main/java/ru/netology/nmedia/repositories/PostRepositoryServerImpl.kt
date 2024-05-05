package ru.netology.nmedia.repository

import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.RequestBody.Companion.toRequestBody
import ru.netology.nmedia.dto.Post
import ru.netology.nmedia.repositories.PostRepository
import java.util.concurrent.TimeUnit


class PostRepositoryServerImpl : PostRepository {
    private val client = OkHttpClient.Builder()
        .connectTimeout(30, TimeUnit.SECONDS)
        .build()
    private val gson = Gson()
    private val typeToken = object : TypeToken<List<Post>>() {}

    companion object {
        private const val BASE_URL = "http://192.168.100.11:9999"
        private val jsonType = "application/json".toMediaType()
    }

    override fun getAll(): List<Post>  {
        val request: Request = Request.Builder()
            .url("${BASE_URL}/api/slow/posts")
            .build()

        return client.newCall(request)
            .execute()
            .let { it.body?.string() ?: throw RuntimeException("body is null") }
            .let {
                gson.fromJson(it, typeToken.type)
            }
    }

    override fun likeById(id: Long) {
        val post = getAll().firstOrNull {it.id == id}
        if (post != null) {
            val request: Request = if (!post.likedByMe) {
                Request.Builder()
                    .post(gson.toJson(null).toRequestBody(jsonType))
                    .url("${BASE_URL}/api/posts/$id/likes")
                    .build()
            } else {
                Request.Builder()
                    .delete()
                    .url("${BASE_URL}/api/posts/$id/likes")
                    .build()
            }
            client.newCall(request)
                .execute()
                .close()
        }
    }

    override fun shareById(id: Long) {
        TODO("Not yet implemented")
    }

    override fun removeById(id: Long) {
        val request: Request = Request.Builder()
            .delete()
            .url("${BASE_URL}/api/slow/posts/$id")
            .build()

        client.newCall(request)
            .execute()
            .close()
    }

    override fun save(post: Post) {
        val request: Request = Request.Builder()
            .post(gson.toJson(post).toRequestBody(jsonType))
            .url("${BASE_URL}/api/slow/posts")
            .build()

        client.newCall(request)
            .execute()
            .close()
    }

    override fun saveDraft(idUser: Long, content: String) {
        TODO("Not yet implemented")
        //-
    }

    override fun getDraft(idUser: Long): String? {
        TODO("Not yet implemented")
    }
}