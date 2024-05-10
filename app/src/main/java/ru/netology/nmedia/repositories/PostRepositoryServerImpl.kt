package ru.netology.nmedia.repository

import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import okhttp3.Call
import okhttp3.Callback
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.RequestBody.Companion.toRequestBody
import okhttp3.Response
import ru.netology.nmedia.config.AppConfig
import ru.netology.nmedia.dto.Post
import ru.netology.nmedia.repositories.PostRepository
import java.io.IOException
import java.util.concurrent.TimeUnit


class PostRepositoryServerImpl : PostRepository {
    private val client = OkHttpClient.Builder()
        .connectTimeout(30, TimeUnit.SECONDS)
        .build()
    private val gson = Gson()
    private val typeToken = object : TypeToken<List<Post>>() {}

    companion object {
        private const val BASE_URL = AppConfig.BASE_URL
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

    override fun getAllAsync(callback: PostRepository.GetAllCallback) {
        val request: Request = Request.Builder()
            .url("${BASE_URL}/api/slow/posts")
            .build()

        client.newCall(request)
            .enqueue(object : Callback {
                override fun onResponse(call: Call, response: Response) {
                    try {
                        val body = response.body?.string() ?: throw RuntimeException("body is null")
                        if (!response.isSuccessful) {
                            callback.onError(RuntimeException(body))
                            return
                        }
                        callback.onSuccess(gson.fromJson(body, typeToken.type))
                    } catch (e: Exception) {
                        callback.onError(e)
                    }
                }

                override fun onFailure(call: Call, e: IOException) {
                    callback.onError(e)
                }
            })
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

    override fun likeByIdAsync(id: Long, likedByMe: Boolean, callback: PostRepository.CallbackWithOutArgs) {
        val request: Request = if (!likedByMe) {
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
        callRequestWithOutArgs(request, callback);
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

    override fun removeByIdAsync(id: Long, callback: PostRepository.CallbackWithOutArgs) {
        val request: Request = Request.Builder()
            .delete()
            .url("${BASE_URL}/api/slow/posts/$id")
            .build()

        callRequestWithOutArgs(request, callback);
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

    override fun saveAsync(post: Post, callback: PostRepository.CallbackWithOutArgs) {
        val request: Request = Request.Builder()
            .post(gson.toJson(post).toRequestBody(jsonType))
            .url("${BASE_URL}/api/slow/posts")
            .build()

        callRequestWithOutArgs(request, callback);
    }

    override fun saveDraft(idUser: Long, content: String) {
        TODO("Not yet implemented")
    }

    override fun getDraft(idUser: Long): String? {
        TODO("Not yet implemented")
    }

    private fun callRequestWithOutArgs(request: Request, callback: PostRepository.CallbackWithOutArgs) {
        client.newCall(request)
            .enqueue(object : Callback {
                override fun onResponse(call: Call, response: Response) {
                    try {
                        if (!response.isSuccessful) {
                            callback.onError(RuntimeException())
                            return
                        }
                        callback.onSuccess()
                    } catch (e: Exception) {
                        callback.onError(e)
                    }
                }

                override fun onFailure(call: Call, e: IOException) {
                    callback.onError(e)
                }
            })
    }
}