package com.test.campingusproject_seller.repository

import android.net.Uri
import com.google.android.gms.tasks.Task
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import com.google.firebase.storage.UploadTask
import com.test.campingusproject_seller.dataclassmodel.ProductModel

class ProductRepository {
    companion object{

        //상품 ID를 가져오는 함수
        fun getProductId(callback1 : (Task<DataSnapshot>) -> Unit){
            val database = FirebaseDatabase.getInstance()

            val productId = database.getReference("ProductId")
            productId.get().addOnCompleteListener(callback1)
        }

        //상품 ID를 설정하는 함수
        fun setProductId(productId : Long, callback1 : (Task<Void>) -> Unit){
            val database = FirebaseDatabase.getInstance()
            val productIdRef = database.getReference("ProductId")

            productIdRef.get().addOnCompleteListener {
                it.result.ref.setValue(productId).addOnCompleteListener(callback1)
            }
        }

        //상품 정보를 DB에 추가하는 함수
        fun addProductInfo(productModel : ProductModel, callback1 : (Task<Void>) -> Unit){
            val database = FirebaseDatabase.getInstance()

            val productRef = database.getReference("ProductData")
            productRef.push().setValue(productModel).addOnCompleteListener(callback1)
        }

        //상품 이미지들을 업로드하는 함수
        fun uploadImages(uploadUri : MutableList<Uri>, fileDir : String, callback1: (Task<UploadTask.TaskSnapshot>) -> Unit){
            val storage = FirebaseStorage.getInstance()

            val count = uploadUri.size
            for(idx in 0 until count){
                val fileName = fileDir + "${idx+1}.png"
                val imageRef = storage.reference.child(fileName)
                imageRef.putFile(uploadUri[idx]).addOnCompleteListener(callback1)
            }
        }

        //상품의 대표이미지만 가져오는 함수
        fun getProductFirstImage(fileDir:String, callback1: (Task<Uri>) -> Unit){
            val storage = FirebaseStorage.getInstance()
            val fileName = fileDir + "1.png"

            val imageRef = storage.reference.child(fileName)
            imageRef.downloadUrl.addOnCompleteListener(callback1)
        }

        //판매자 id 받아오는 함수 - 수정필
        fun getSellerId() : String{
            return "jieun"
        }

        //모든 상품 정보 가져오는 함수
        fun getAllProductData(callback1: (Task<DataSnapshot>) -> Unit){
            val database = FirebaseDatabase.getInstance()

            val productRef = database.getReference("ProductData")
            productRef.orderByChild("productId").get().addOnCompleteListener(callback1)
        }

        //하나의 상품 정보 가져오는 함수
        fun getOneProductData(productId:Long, callback1: (Task<DataSnapshot>) -> Unit){
            val database = FirebaseDatabase.getInstance()

            val productRef = database.getReference("ProductData")
            productRef.orderByChild("productId").equalTo(productId.toDouble()).get().addOnCompleteListener(callback1)
        }

        //해당하는 상품 이미지 전부 가져오는 함수
        fun getProductImages(fileDir: String, callback1: (StorageReference) -> Unit){
            val storage = FirebaseStorage.getInstance()

            val filePath = fileDir.substring(0, fileDir.length-1)
            val fileDirRef = storage.reference.child(filePath)

            //listAll 메서드로 해당 디렉토리 하위에 있는 모든 항목을 순회
            fileDirRef.listAll()
                .addOnCompleteListener { task->
                    if(task.isSuccessful){
                        task.result.items.forEach {
                            callback1(it)
                        }
                    }
                }
        }


        //상품 삭제하는 함수
        fun removeProduct(productId: Long, callback1: (Task<Void>)-> Unit){
            val database = FirebaseDatabase.getInstance()
            val productRef = database.getReference("ProductData")

            productRef.orderByChild("productId").equalTo(productId.toDouble()).get().addOnCompleteListener {
                for(a1 in it.result.children){
                    a1.ref.removeValue().addOnCompleteListener(callback1)
                }
            }
        }

        //상품 이미지 전부 삭제하는 함수
        fun removeImages(fileDir: String, callback1: ()->Unit){
            val storage = FirebaseStorage.getInstance()
            val filePath = fileDir.substring(0, fileDir.length-1)
            val fileDirRef = storage.reference.child(filePath)

            //listAll 메서드로 해당 디렉토리 하위에 있는 모든 항목을 순회
            fileDirRef.listAll()
                .addOnCompleteListener { task->
                    if(task.isSuccessful){
                        task.result.items.forEach {
                            it.delete()
                        }
                        callback1()
                    }
                }
        }

        //등록된 상품 데이터를 수정하는 함수
        fun modifyProduct(productModel: ProductModel, callback1: (Task<Void>) -> Unit){
            val database = FirebaseDatabase.getInstance()
            val productRef = database.getReference("ProductData")

            productRef.orderByChild("productId").equalTo(productModel.productId.toDouble()).get().addOnCompleteListener {
                for(a1 in it.result.children){
                    a1.ref.child("productCount").setValue(productModel.productCount)
                    a1.ref.child("productDiscountRate").setValue(productModel.productDiscountRate)
                    a1.ref.child("productInfo").setValue(productModel.productInfo)
                    a1.ref.child("productName").setValue(productModel.productName)
                    a1.ref.child("productPrice").setValue(productModel.productPrice)
                    a1.ref.child("productSellingStatus").setValue(productModel.productSellingStatus)
                    a1.ref.child("productBrand").setValue(productModel.productBrand)
                    a1.ref.child("productKeywordList").setValue(productModel.productKeywordList)
                    a1.ref.child("productCategory").setValue(productModel.productCategory).addOnCompleteListener(callback1)
                }
            }
        }

        //수정하려는 이미지 목록 중 원래 저장된 이미지가 포함되었는지 검사하는 함수
        fun checkIsExistImage(uploadUri: MutableList<Uri>, fileDir: String){
            val storage = FirebaseStorage.getInstance()
            val filePath = fileDir.substring(0, fileDir.length-1)
            val fileDirRef = storage.reference.child(filePath)

            var count = 1
            val len = uploadUri.size
            fileDirRef.listAll()
                .addOnCompleteListener {task->
                    if(task.isSuccessful){
                        task.result.items.forEach {storageRef->
                            for(idx in 0 until len){
                                if(storageRef.downloadUrl == uploadUri[idx]){

                                }
                            }
                        }

                    }
                }
        }

        //수정된 이미지 목록 업로드하는 함수
        fun uploadModifiedImages(uploadUri : MutableList<Uri>, fileDir : String, callback1: (Task<UploadTask.TaskSnapshot>) -> Unit){
            val storage = FirebaseStorage.getInstance()

            val count = uploadUri.size
            for(idx in 0 until count){
                val fileName = fileDir + "${idx+1}.png"
                val imageRef = storage.reference.child(fileName)
                imageRef.putFile(uploadUri[idx]).addOnCompleteListener(callback1)
            }
        }
    }
}