package com.example.repositoriomanualmars

import com.example.repositoriomanualmars.data.MarsApiService
import com.example.repositoriomanualmars.data.MarsPhoto
import com.example.repositoriomanualmars.data.NetworkMarsPhotosRepository
import kotlinx.coroutines.runBlocking
import org.junit.Assert.assertEquals
import org.junit.Test

class MarsPhotosRepositoryTest {
    @Test
    fun repositoryReturnsPhotosFromService() = runBlocking {
        val photos = listOf(MarsPhoto("1", "https://example.com/mars.jpg"))
        val service = object : MarsApiService {
            override suspend fun getPhotos() = photos
        }
        assertEquals(photos, NetworkMarsPhotosRepository(service).getMarsPhotos())
    }
}
