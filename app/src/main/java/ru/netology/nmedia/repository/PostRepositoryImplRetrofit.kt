package ru.netology.nmedia.repository

import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.*
import ru.netology.nmedia.api.PostsApi
import ru.netology.nmedia.dao.PostDao
import ru.netology.nmedia.dto.Post
import ru.netology.nmedia.entity.PostEntity
import ru.netology.nmedia.entity.toDto
import ru.netology.nmedia.entity.toEntity
import ru.netology.nmedia.error.ApiError
import ru.netology.nmedia.error.NetworkError
import ru.netology.nmedia.error.UnknownError
import java.io.IOException


class PostRepositoryImplRetrofit(private val postDao: PostDao) : PostRepository {
    override val data = postDao.getVisible().map(List<PostEntity>::toDto).flowOn(Dispatchers.Default)
    override fun getNewerCount(firstId: Long): Flow<Int> = flow {
        while (true){
            try {
                //val currentCount = maxOf(firstId,postDao.getAll().first().size.toLong())
                val response = PostsApi.retrofitService.getNewer(
                    firstId
                    //currentCount
                )
                if (!response.isSuccessful) {
                    throw ApiError(response.code(), response.message())
                }
                val body = response.body() ?: throw ApiError(response.code(), response.message())
                postDao.insert(body.toEntity().map{
                    val currentCout = postDao.getAll().first().toDto().first().id
                    if (currentCout<firstId){
                        it.copy(visible = false)
                    } else
                        it

                })
                emit(body.size)
                delay(10_000L)
            } catch (e: CancellationException){
                throw e
            } catch (e: IOException) {
                throw NetworkError
            } catch (e: Exception) {
                throw UnknownError
            }
        }
    }

    override suspend fun newerUpdate(): Flow<Int> = flow{
        val count: Int? = postDao.getNewerUpdate()
        postDao.getAll().map { list ->
            list.map {
                it.copy(visible = true)
            }
        }

    }


            /*List<Post> {
        val flowList: Flow<List<Post>> = postDao.getAll().map { it.toDto() }
        return flowList.flatMapConcat { it.asFlow() }.toList()
    }*/


    override suspend fun getAllAsync() {
        try {
            val response = PostsApi.retrofitService.getAll()
            if (!response.isSuccessful) {
                throw ApiError(response.code(), response.message())
            }
            val body = response.body() ?: throw ApiError(response.code(), response.message())
            postDao.insert(body.toEntity())
        } catch (e: IOException) {
            throw NetworkError
        } catch (e: Exception) {
            throw UnknownError
        }
    }

        override suspend fun likeByIdAsync(post: Post) {
            val id = post.id
            if (!post.likedByMe) {
                try {
                    postDao.likeById(id)
                    PostsApi.retrofitService.likeById(id)

                } catch (e: IOException) {
                    throw NetworkError
                } catch (e: Exception) {
                    throw UnknownError
                }


            } else {
                try{
                    postDao.likeById(id)
                    PostsApi.retrofitService.dislikeById(id)
                } catch (e: IOException) {
                    throw NetworkError
                } catch (e: Exception) {
                    throw UnknownError
                }

            }
        }

        override suspend fun save(post: Post) {

            try {
                val response = PostsApi.retrofitService.save(post)
                if (!response.isSuccessful) {
                    throw ApiError(response.code(), response.message())
                }
                val body = response.body() ?: throw ApiError(response.code(), response.message())
                postDao.insert(PostEntity.fromDto(body))
            } catch (e: IOException) {
                throw NetworkError
            } catch (e: Exception) {
                throw UnknownError
            }

        }


        override suspend fun removeById(id: Long) {
            try {
                postDao.removeById(id)
                PostsApi.retrofitService.removeById(id)
            } catch (e: IOException) {
                throw NetworkError
            } catch (e: Exception) {
                throw UnknownError
            }

        }
    }
