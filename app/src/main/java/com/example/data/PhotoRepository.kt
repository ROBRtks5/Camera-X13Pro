package com.example.data

import kotlinx.coroutines.flow.Flow

class PhotoRepository(private val photoDao: PhotoDao) {
    val allPhotos: Flow<List<PhotoEntity>> = photoDao.getAllPhotos()
    val favoritePhotos: Flow<List<PhotoEntity>> = photoDao.getFavoritePhotos()

    suspend fun insert(photo: PhotoEntity): Long = photoDao.insertPhoto(photo)
    suspend fun update(photo: PhotoEntity) = photoDao.updatePhoto(photo)
    suspend fun deleteById(id: Long) = photoDao.deletePhotoById(id)
    suspend fun getPhotoById(id: Long): PhotoEntity? = photoDao.getPhotoById(id)
}
