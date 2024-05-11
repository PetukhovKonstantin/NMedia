package ru.netology.nmedia.repository

import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import ru.netology.nmedia.api.PostsApi
import ru.netology.nmedia.dto.Post
import ru.netology.nmedia.repositories.PostRepository


class PostRepositoryServerImpl : PostRepository {
    override fun getAll(): List<Post>  {
        TODO("Not yet implemented")
    }

    override fun getAllAsync(callback: PostRepository.Callback<List<Post>>) {
        PostsApi.retrofitService.getAll().enqueue(object : Callback<List<Post>> {
            override fun onResponse(call: Call<List<Post>>, response: Response<List<Post>>) {
                if (!response.isSuccessful) {
                    callback.onError(RuntimeException(response.message()))
                    return
                }

                if (Math.random() < 0.5) {
                    callback.onError(com.bumptech.glide.load.HttpException("Internal Server Error", 500))
                    return
                }

                callback.onSuccess(response.body() ?: throw RuntimeException("body is null"))
            }

            override fun onFailure(call: Call<List<Post>>, t: Throwable) {
                    callback.onError(Exception(t.message))
            }
        })
    }

    override fun likeById(id: Long) {
        TODO("Not yet implemented")
    }

    override fun likeByIdAsync(id: Long, likedByMe: Boolean, callback: PostRepository.Callback<Post>) {
            PostsApi.retrofitService
                .let { if (likedByMe) it.dislikeById(id) else it.likeById(id) }
                .enqueue(object : Callback<Post> {
                    override fun onResponse(call: Call<Post>, response: Response<Post>) {
                        if (!response.isSuccessful) {
                            callback.onError(RuntimeException(response.message()))
                            return
                        }

                        callback.onSuccess(response.body() ?: throw RuntimeException("body is null"))
                    }

                    override fun onFailure(call: Call<Post>, t: Throwable) {
                        callback.onError(Exception(t.message))
                    }
                })
    }

    override fun shareById(id: Long) {
        TODO("Not yet implemented")
    }

    override fun removeById(id: Long) {
        TODO("Not yet implemented")
    }

    override fun removeByIdAsync(id: Long, callback: PostRepository.Callback<Unit>) {
        PostsApi.retrofitService.removeById(id).enqueue(object : Callback<Unit> {
                override fun onResponse(call: Call<Unit>, response: Response<Unit>) {
                    if (!response.isSuccessful) {
                        callback.onError(RuntimeException(response.message()))
                        return
                    }

                    callback.onSuccess(response.body() ?: throw RuntimeException("body is null"))
                }

                override fun onFailure(call: Call<Unit>, t: Throwable) {
                    callback.onError(Exception(t.message))
                }
            })
    }

    override fun save(post: Post) {
        TODO("Not yet implemented")
    }

    override fun saveAsync(post: Post, callback: PostRepository.Callback<Post>) {
        PostsApi.retrofitService.save(post).enqueue(object : Callback<Post> {
                override fun onResponse(call: Call<Post>, response: Response<Post>) {
                    if (!response.isSuccessful) {
                        callback.onError(RuntimeException(response.message()))
                        return
                    }

                    callback.onSuccess(response.body() ?: throw RuntimeException("body is null"))
                }

                override fun onFailure(call: Call<Post>, t: Throwable) {
                    callback.onError(Exception(t.message))
                }
            })
    }

    override fun saveDraft(idUser: Long, content: String) {
        TODO("Not yet implemented")
    }

    override fun getDraft(idUser: Long): String? {
        TODO("Not yet implemented")
    }
}