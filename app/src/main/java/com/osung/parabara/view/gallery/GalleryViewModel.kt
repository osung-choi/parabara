package com.osung.parabara.view.gallery

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.osung.parabara.repository.GalleryRepository
import com.osung.parabara.repository.entity.EntityGalleryImage
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.disposables.CompositeDisposable
import io.reactivex.rxjava3.schedulers.Schedulers

class GalleryViewModel(
    private val repository: GalleryRepository,
): ViewModel() {
    private val compositeDisposable = CompositeDisposable()

    //갤러리에서 읽어온 이미지 리스트 관찰
    private val _galleryImageList = MutableLiveData<List<EntityGalleryImage>>().apply {
        repository.getGalleryImageList()
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe({
                this.value = it
            }, {
                it.message
            })
            .run {
                compositeDisposable.add(this)
            }
    }

    val galleryImageList: LiveData<List<EntityGalleryImage>> = _galleryImageList

    override fun onCleared() {
        super.onCleared()

        compositeDisposable.clear()
    }
}