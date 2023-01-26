package ru.netology.nmedia.repository

import androidx.lifecycle.LiveData
import androidx.lifecycle.map
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import ru.netology.nmedia.api.PostsApi
import ru.netology.nmedia.dao.PostDao
import ru.netology.nmedia.dto.Post
import ru.netology.nmedia.entity.PostEntity


class PostRepositoryImplRetrofit(private val postDao: PostDao) : PostRepository {
    override val data: LiveData<List<Post>> = postDao.getAll().map { it ->
        it.map(PostEntity::toDto)
    }


    override suspend fun getAllAsync(): List<Post> {
        val posts = PostsApi.retrofitService.getAll()
        postDao.insert(posts.map(PostEntity::fromDto))
        return posts
        //override fun onResponse(call: Call<List<Post>>, response: Response<List<Post>>) {
        //    if (!response.isSuccessful) {
        //        callback.onError(RuntimeException(response.message()))
        //        return
        //    }
//
        //    callback.onSuccess(response.body() ?: throw RuntimeException("body is null"))
        //}
//
        //override fun onFailure(call: Call<List<Post>>, t: Throwable) {
        //    callback.onError(t as Exception)
        //}
    }


    override suspend fun likeByIdAsync(post: Post) {
        val id = post.id
        if (!post.likedByMe) {

            PostsApi.retrofitService.likeById(id)
            /*override fun onResponse(call: Call<Post>, response: Response<Post>) {
                if (!response.isSuccessful) {
                    callback.onError(RuntimeException(response.message()))
                    return
                }

                callback.onSuccess(response.body() ?: throw RuntimeException("body is null"))
            }

            override fun onFailure(call: Call<Post>, t: Throwable) {
                callback.onError(t as Exception)
            }*/


        } else {
            PostsApi.retrofitService.dislikeById(id)
            /*override fun onResponse(call: Call<Post>, response: Response<Post>) {
                if (!response.isSuccessful) {
                    callback.onError(RuntimeException(response.message()))
                    return
                }

                callback.onSuccess(response.body() ?: throw RuntimeException("body is null"))
            }

            override fun onFailure(call: Call<Post>, t: Throwable) {
                callback.onError(t as Exception)
            }*/

        }
    }

    override suspend fun save(post: Post) {

        PostsApi.retrofitService.save(post)
        /*override fun onResponse(call: Call<Post>, response: Response<Post>) {
            if (!response.isSuccessful) {
                callback.onError(RuntimeException(response.message()))
                return
            }

            callback.onSuccess(response.body() ?: throw RuntimeException("body is null"))
        }

        override fun onFailure(call: Call<Post>, t: Throwable) {
            callback.onError(t as Exception)


        }*/

    }


    override suspend fun removeById(id: Long) {
        PostsApi.retrofitService.removeById(id)
            /*override fun onResponse(call: Call<Unit>, response: Response<Unit>) {
                if (!response.isSuccessful) {
                    callback.onError(RuntimeException(response.message()))
                    return
                }

                callback.onSuccess(response.body() ?: throw RuntimeException("body is null"))
            }

            override fun onFailure(call: Call<Unit>, t: Throwable) {
                callback.onError(t as Exception)
            }*/

    }
}
