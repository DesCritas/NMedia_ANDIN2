package ru.netology.nmedia.repository


import androidx.lifecycle.LiveData
import ru.netology.nmedia.dto.Post

interface PostRepository {
    val data: LiveData<List<Post>>
    //fun getAll(): List<Post>
    suspend fun likeByIdAsync(post: Post)
    suspend fun save(post: Post)
    suspend fun removeById(id: Long )
    suspend fun getAllAsync(): List<Post>
}
