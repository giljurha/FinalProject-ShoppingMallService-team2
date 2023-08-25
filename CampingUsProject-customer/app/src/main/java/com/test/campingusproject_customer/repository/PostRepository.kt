package com.test.campingusproject_customer.repository

import android.net.Uri
import com.google.android.gms.tasks.Task
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import com.google.firebase.storage.UploadTask
import com.test.campingusproject_customer.dataclassmodel.PostModel

class PostRepository {
    companion object {
        // 게시글 정보를 저장한다.
        fun addPostInfo(postModel: PostModel, callback1: (Task<Void>) -> Unit) {
            val database = FirebaseDatabase.getInstance()
            val postDataRef = database.getReference("PostData")
            postDataRef.push().setValue(postModel).addOnCompleteListener(callback1)
        }

        // 게시글 인덱스 번호를 가져온다.
        fun getPostIdx(callback1: (Task<DataSnapshot>) -> Unit) {
            val database = FirebaseDatabase.getInstance()
            // 게시글 인덱스 번호
            val postIdxRef = database.getReference("PostIdx")
            postIdxRef.get().addOnCompleteListener(callback1)
        }

        // 게시글 번호를 저장한다.
        fun setPostIdx(postIdx:Long, callback1: (Task<Void>) -> Unit){
            val database = FirebaseDatabase.getInstance()
            val postIdxRef = database.getReference("PostIdx")
            // 게시글 인덱스번호 저장
            postIdxRef.get().addOnCompleteListener {
                it.result.ref.setValue(postIdx).addOnCompleteListener(callback1)
            }
        }

        // 게시글 정보를 가져온다.
        fun getPostInfo(postIdx:Double, callback1 : (Task<DataSnapshot>) -> Unit){
            val database = FirebaseDatabase.getInstance()
            val postDataRef = database.getReference("PostData")
            postDataRef.orderByChild("postIdx").equalTo(postIdx).get().addOnCompleteListener(callback1)
        }

        // 게시글 이미지들을 저장한다.
        fun uploadImages(uploadUriList : MutableList<Uri>, postImagePath : String, callback1: (Task<UploadTask.TaskSnapshot>) -> Unit){
            val storage = FirebaseStorage.getInstance()

            val count = uploadUriList.size
            for(idx in 0 until count){
                val fileName = postImagePath + "${idx+1}.png"
                val imageRef = storage.reference.child(fileName)
                imageRef.putFile(uploadUriList[idx]).addOnCompleteListener(callback1)
            }
        }

        //게시글 이미지 가져오기
        fun getPostImages(fileDir: String, callback1: (StorageReference) -> Unit){
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
    }
}