package ru.netology.nmedia.repository

import kotlinx.coroutines.flow.Flow
import ru.netology.nmedia.dto.Post

interface PostRepository {
    val data: Flow<List<Post>>
    fun getNewerCount(firstId: Long): Flow<Int>
    suspend fun likeByIdAsync(post: Post)
    suspend fun save(post: Post)
    suspend fun removeById(id: Long )
    suspend fun getAllAsync()
}
