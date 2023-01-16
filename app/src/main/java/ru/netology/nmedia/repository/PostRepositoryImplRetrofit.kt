package ru.netology.nmedia.repository

import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import ru.netology.nmedia.api.PostsApi
import ru.netology.nmedia.dto.Post


class PostRepositoryImplRetrofit : PostRepository {


    //override fun getAll(): List<Post> {
    //    val request: Request = Request.Builder()
    //        .url("${BASE_URL}/api/slow/posts")
    //        .build()
//
    //    val response = client.newCall(request)
    //        .execute()
    //        .let { it.body?.string() ?: throw RuntimeException("body is null") }
//
    //    return response.let {
    //        gson.fromJson(it, typeToken.type)
    //    }
    //}

    //fun getById(id: Long): Post {
    //    val request: Request = Request.Builder()
    //        .url("${BASE_URL}/api/slow/posts/$id")
    //        .build()
    //    return client.newCall(request)
    //        .execute()
    //        .let {
    //            it.body?.string() ?: throw RuntimeException("body is null") }
    //        .let {
    //            gson.fromJson(it, typeTokenPost.type)
    //        }
//
    //}

    override fun likeByIdAsync(post: Post, callback: PostRepository.AsyncCallback<Post>) {
        val id = post.id
        if (!post.likedByMe) {

            PostsApi.retrofitService.likeById(id).enqueue(object : Callback<Post> {
                override fun onResponse(call: Call<Post>, response: Response<Post>) {
                    if (!response.isSuccessful) {
                        callback.onError(RuntimeException(response.message()))
                        return
                    }

                    callback.onSuccess(response.body() ?: throw RuntimeException("body is null"))
                }

                override fun onFailure(call: Call<Post>, t: Throwable) {
                    callback.onError(t as Exception)
                }

            })
        } else {
            PostsApi.retrofitService.dislikeById(id).enqueue(object : Callback<Post> {
                override fun onResponse(call: Call<Post>, response: Response<Post>) {
                    if (!response.isSuccessful) {
                        callback.onError(RuntimeException(response.message()))
                        return
                    }

                    callback.onSuccess(response.body() ?: throw RuntimeException("body is null"))
                }

                override fun onFailure(call: Call<Post>, t: Throwable) {
                    callback.onError(t as Exception)
                }

            })
        }
        //val post = getById(id)
        //val id = post.id
//
        //if (!post.likedByMe) {
        //    val request: Request = Request.Builder()
        //        .post(gson.toJson(EMPTY_REQUEST).toRequestBody(jsonType))
        //        .url("${BASE_URL}/api/slow/posts/$id/likes")
        //        .build()
//
        //    client.newCall(request)
        //        .enqueue(object : Callback {
        //            override fun onResponse(call: Call, response: Response) {
        //                val body = response.body?.string() ?: throw RuntimeException("Null body")
        //                try {
        //                    callback.onSuccess(gson.fromJson(body, typeTokenPost.type))
        //                } catch (e: Exception) {
        //                    callback.onError(e)
        //                }
        //            }
//
        //            override fun onFailure(call: Call, e: IOException) {
        //                callback.onError(e)
        //            }
        //        })
        //} else {
        //    val request: Request = Request.Builder()
        //        .delete()
        //        .url("${BASE_URL}/api/slow/posts/$id/likes")
        //        .build()
        //    client.newCall(request)
        //        .enqueue(object : Callback {
        //            override fun onResponse(call: Call, response: Response) {
        //                val body = response.body?.string() ?: throw RuntimeException("Null body")
        //                try {
        //                    callback.onSuccess(gson.fromJson(body, typeTokenPost.type))
        //                } catch (e: Exception) {
        //                    callback.onError(e)
        //                }
        //            }
//
        //            override fun onFailure(call: Call, e: IOException) {
        //                callback.onError(e)
        //            }
        //        })
//
        //}

    }

    override fun save(post: Post, callback: PostRepository.AsyncCallback<Post>) {

        PostsApi.retrofitService.save(post).enqueue(object : Callback<Post> {
            override fun onResponse(call: Call<Post>, response: Response<Post>) {
                if (!response.isSuccessful) {
                    callback.onError(RuntimeException(response.message()))
                    return
                }

                callback.onSuccess(response.body() ?: throw RuntimeException("body is null"))
            }

            override fun onFailure(call: Call<Post>, t: Throwable) {
                callback.onError(t as Exception)


            }

        }

        )
        //val request: Request = Request.Builder()
        //    .post(gson.toJson(post).toRequestBody(jsonType))
        //    .url("${BASE_URL}/api/slow/posts")
        //    .build()
//
        //client.newCall(request)
        //    .enqueue(object : Callback{
        //        override fun onResponse(call: Call, response: Response) {
        //            val body = response.body?.string() ?: throw RuntimeException("Null body")
        //            try {
        //                callback.onSuccess(gson.fromJson(body, typeTokenPost.type))
        //            } catch (e: Exception) {
        //                callback.onError(e)
        //            }
        //        }
        //        override fun onFailure(call: Call, e: IOException) {
        //            callback.onError(e)
        //        }
        //    })
    }

    override fun removeById(id: Long, callback: PostRepository.AsyncCallback<Unit>) {
        PostsApi.retrofitService.removeById(id).enqueue(object : Callback<Unit> {
            override fun onResponse(call: Call<Unit>, response: Response<Unit>) {
                if (!response.isSuccessful) {
                    callback.onError(RuntimeException(response.message()))
                    return
                }

                callback.onSuccess(response.body() ?: throw RuntimeException("body is null"))
            }

            override fun onFailure(call: Call<Unit>, t: Throwable) {
                callback.onError(t as Exception)
            }

        })


        //val request: Request = Request.Builder()
        //    .delete()
        //    .url("${BASE_URL}/api/slow/posts/$id")
        //    .build()
//
        //client.newCall(request)
        //    .enqueue(object  : Callback{
        //        override fun onResponse(call: Call, response: Response) {
        //            response.body?.string() ?: throw RuntimeException("Null body")
        //            try {
        //                callback.onSuccess(Unit)
        //            } catch (e: Exception) {
        //                callback.onError(e)
        //            }
//
        //        }
//
        //        override fun onFailure(call: Call, e: IOException) {
        //            callback.onError(e)
        //        }
//
        //    })

    }

    override fun getAllAsync(callback: PostRepository.AsyncCallback<List<Post>>) {
        PostsApi.retrofitService.getAll().enqueue(object : Callback<List<Post>> {
            override fun onResponse(call: Call<List<Post>>, response: Response<List<Post>>) {
                if (!response.isSuccessful) {
                    callback.onError(RuntimeException(response.message()))
                    return
                }

                callback.onSuccess(response.body() ?: throw RuntimeException("body is null"))
            }

            override fun onFailure(call: Call<List<Post>>, t: Throwable) {
                callback.onError(t as Exception)
            }
        })
    }
}
