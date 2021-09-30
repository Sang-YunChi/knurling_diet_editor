package com.malkinfo.editingrecyclerview

import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import android.view.LayoutInflater
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.ktx.Firebase
import com.malkinfo.editingrecyclerview.model.UserData
import com.malkinfo.editingrecyclerview.view.UserAdapter
import java.text.SimpleDateFormat
import java.util.*
import java.util.jar.Manifest
import kotlin.collections.ArrayList

class MainActivity : AppCompatActivity() {
    private lateinit var addsBtn:FloatingActionButton
    private lateinit var recv:RecyclerView
    private lateinit var userList:ArrayList<UserData>
    private lateinit var userAdapter:UserAdapter
    private val OPEN_GALLERY=1
    val db = FirebaseFirestore.getInstance()
    val database : FirebaseDatabase = FirebaseDatabase.getInstance()


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        /**set List*/
        userList = ArrayList()
        /**set find Id*/
        addsBtn = findViewById(R.id.addingBtn)
        recv = findViewById(R.id.mRecycler)
        /**set Adapter*/
        userAdapter = UserAdapter(this,userList)
        /**setRecycler view Adapter*/
        recv.layoutManager = LinearLayoutManager(this)
        recv.adapter = userAdapter
        /**set Dialog*/
        addsBtn.setOnClickListener { addInfo() }


    }


    private fun addInfo() {
        val inflter = LayoutInflater.from(this)
        val v = inflter.inflate(R.layout.add_item,null)
        /**set view*/
        val foodName = v.findViewById<EditText>(R.id.calorie)
        val foodKind = v.findViewById<EditText>(R.id.foodKind)
        val foodInfo = v.findViewById<EditText>(R.id.foodInfo)
//        val openGallery = v.findViewById<Button>(R.id.openGallery_button)

        val addDialog = AlertDialog.Builder(this)

        val myRef : DatabaseReference = database.getReference("food")

//        openGallery.setOnClickListener{
//            openGallery()} // openGallery 버튼 누르면 갤러리가 열림.



        addDialog.setView(v)
        addDialog.setPositiveButton("Ok"){
            dialog,_->
            val calories = foodName.text.toString().toInt() ?: 0
            val foodKinds = foodKind.text.toString()
            val foodInfos = foodInfo.text.toString()
            val timeStamp = SimpleDateFormat("yyyyMMdd_HHmm").format(Date())
            userList.add(UserData("열량: $calories"+"kcal","식단 : $foodKinds","음식 정보 : \n $foodInfos","시간 : $timeStamp"))
            //수정해야 부분
            userAdapter.notifyDataSetChanged()

            val data = hashMapOf(
                "calories" to calories,
                "foodKinds" to foodKinds,
                "foodInfos" to foodInfos,
                "time" to timeStamp.toString()
            )

            myRef.push().setValue(data) // 리얼타임 데이터베이스에 식단 데이터 추가.

            Toast.makeText(this,"Adding Food Information Success",Toast.LENGTH_SHORT).show()
            dialog.dismiss()


        }
        addDialog.setNegativeButton("Cancel"){
            dialog,_->
            dialog.dismiss()
            Toast.makeText(this,"Cancel",Toast.LENGTH_SHORT).show()
        }
        addDialog.create()
        addDialog.show()
    }

//    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
//        super.onActivityResult(requestCode, resultCode, data)
//
//        val inflter = LayoutInflater.from(this)
//        val v = inflter.inflate(R.layout.add_item,null)
//
//        if (requestCode == OPEN_GALLERY && resultCode == Activity.RESULT_OK) {
//            val currentImageUrl : Uri? = data?.data
//            try{
//                val imageView = v.findViewById<ImageView>(R.id.showImage_imageView)
//                val bitmap = MediaStore.Images.Media.getBitmap(contentResolver, currentImageUrl)
//                imageView.setImageBitmap(bitmap)
//            }catch (e: Exception){
//                e.printStackTrace()
//            }
//        }
//    }

//    private fun openGallery(){
//        val intent: Intent = Intent(Intent.ACTION_GET_CONTENT)
//        intent.setType("image/*")
//        startActivityForResult(intent,OPEN_GALLERY)
//        Log.d("확인용","MainActivity - openGallery() called")
//    }
//


}