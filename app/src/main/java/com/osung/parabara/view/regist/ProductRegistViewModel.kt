package com.osung.parabara.view.regist

import android.app.Application
import android.net.Uri
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.osung.parabara.repository.ProductRepository
import com.osung.parabara.repository.entity.EntityGalleryImage
import com.osung.parabara.repository.entity.EntityProduct
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.core.Observable
import io.reactivex.rxjava3.disposables.CompositeDisposable
import io.reactivex.rxjava3.schedulers.Schedulers
import io.reactivex.rxjava3.subjects.PublishSubject
import java.io.File
import java.io.FileOutputStream
import java.io.OutputStream

class ProductRegistViewModel(
    application: Application,
    private val repository: ProductRepository
) : AndroidViewModel(application) {
    private val context = application
    private val tempFileList = mutableListOf<String>() //이미지 파일 경로 저장

    private val compositeDisposable = CompositeDisposable()

    //상품 이미지 리스트 관찰
    private val _productImageList = MutableLiveData<List<EntityGalleryImage>>()
    val productImageList: LiveData<List<EntityGalleryImage>> = _productImageList

    //저장 버튼(상품 등록 및 수정) 활성화 여부 관찰
    private val _enableProductSaveButton = MutableLiveData<Boolean>()
    val enableProductSaveButton: LiveData<Boolean> = _enableProductSaveButton

    //상품 저장 여부 관찰
    private val _completeSaveProduct = MutableLiveData<EntityProduct>()
    val completeSaveProduct: LiveData<EntityProduct> = _completeSaveProduct

    //전달된 상품 ID가 있다면 상품 수정 모드, -1이면 상품 추가 모드
    private var editProductId: Long = -1

    private val productTitleSubject = PublishSubject.create<String>()
    private val productPriceSubject = PublishSubject.create<String>()
    private val productContentSubject = PublishSubject.create<String>()

    var productTitle = ""
        set(value) {
            productTitleSubject.onNext(value)
            field = value
        }

    var productPrice = ""
        set(value) {
            productPriceSubject.onNext(value)
            field = value
        }

    var productContent = ""
        set(value) {
            productContentSubject.onNext(value)
            field = value
        }

    init {
        setProductInputState()
    }

    /**
     * 상품 수정 모드
     *
     * @param productItem 수정할 상품
     */
    fun setProductEditMode(productItem: EntityProduct) {
        with(productItem) {
            editProductId = id
            _productImageList.value = images.map { EntityGalleryImage(imagePath = it) }
            productTitle = title
            productPrice = price.toString()
            productContent = content
        }
    }

    /**
     * 선택한 갤러리 이미지 추가
     *
     * @param selectImageList
     */
    fun addProductImageList(selectImageList: List<EntityGalleryImage>) {
        val images = _productImageList.value?.toMutableList() ?: mutableListOf()
        images.addAll(selectImageList)

        _productImageList.value = images
    }

    /**
     * 상품 정보 저장
     *
     */
    fun onSaveProductInfo() {
        if (_enableProductSaveButton.value == true) {
            if(editProductId > -1) //ID 값을 비교하여 수정 또는 추가
                updateProduct()
            else
                insertProduct()
        }
    }

    /**
     * 선택한 상품(갤러리) 이미지 삭제
     *
     * @param image
     */
    fun removeProductImage(image: EntityGalleryImage) {
        val imageList = _productImageList.value?.toMutableList()?: mutableListOf()

        if(imageList.indexOf(image) > -1) {
            imageList.remove(image)

            _productImageList.value = imageList
        }
    }

    /**
     * 상품 등록
     *
     */
    private fun insertProduct() {
        val imageList = _productImageList.value ?: listOf()

        Observable.fromIterable(imageList)
            .map { getNewImageRealPath(it) }
            .filter { it.imagePath.isNotBlank() }
            .toList()
            .flatMap {
                repository.insertProduct(productTitle, productContent, productPrice.toLong(), it)
            }.subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe({
                _completeSaveProduct.value = it
            }, {})
            .run {
                compositeDisposable.add(this)
            }
    }

    /**
     * 상품 수정
     *
     */
    private fun updateProduct() {
        repository.updateProduct(editProductId, productTitle, productContent, productPrice.toLong())
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe({
                _completeSaveProduct.value = it
            },{
                it.message
            })
            .run {
                compositeDisposable.add(this)
            }
    }

    /**
     * 상품정보 입력 상태 관찰
     *
     */
    private fun setProductInputState() {
        val titleObservable = productTitleSubject.map { it.isNotBlank() }
        val priceObservable = productPriceSubject.map { it.isNotBlank() }
        val contentObservable = productContentSubject.map { it.isNotBlank() }

        Observable.combineLatest(
            titleObservable,
            priceObservable,
            contentObservable,
            { name, price, comment ->
                name && price && comment
            })
            .subscribe {
                _enableProductSaveButton.value = it
            }
            .run {
                compositeDisposable.add(this)
            }
    }

    /**
     * 내부저장소에 복사한 이미지 정보를 인스턴스에 담는다
     *
     * @param galleryImage 선택한 갤러리 이미지
     * @return
     */
    private fun getNewImageRealPath(galleryImage: EntityGalleryImage): EntityGalleryImage {
        val uri = Uri.parse(galleryImage.imagePath)
        val newPath = copyImageAndReturnNewPath(uri, galleryImage.fileName)

        return EntityGalleryImage(galleryImage.id, galleryImage.fileName, newPath)
    }

    /**
     * 갤러리 이미지를 내부저장소에 복사하고 파일 경로를 반환한다.
     *
     * @param uri 갤러리 상대주소 (content://...)
     * @param fileName 이미지 명
     * @return
     */
    private fun copyImageAndReturnNewPath(uri: Uri, fileName: String): String {
        val filePath: String =
            context.applicationInfo.dataDir + File.separator + System.currentTimeMillis()
                .toString() + "." + fileName.substring(fileName.lastIndexOf(".") + 1)
        val file = File(filePath)

        tempFileList.add(filePath)

        context.contentResolver.openInputStream(uri)?.let { inputStream ->
            val outputStream: OutputStream = FileOutputStream(file)
            val buf = ByteArray(1024)
            var len: Int
            while (inputStream.read(buf).also { len = it } > 0)
                outputStream.write(buf, 0, len)

            outputStream.close()
            inputStream.close()

            return file.absolutePath
        }

        return ""
    }

    /**
     * 내부저장소에 복사한 이미지를 삭제한다.
     *
     */
    private fun removeCopyImage() {
        tempFileList.forEach {
            val file = File(it)

            if(file.exists()) {
                file.delete()
            }
        }
    }
    override fun onCleared() {
        super.onCleared()

        removeCopyImage()
        compositeDisposable.clear()
    }
}