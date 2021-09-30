package com.malkinfo.editingrecyclerview.view

import android.app.AlertDialog
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.recyclerview.widget.RecyclerView
import com.google.android.gms.tasks.OnFailureListener
import com.google.android.gms.tasks.OnSuccessListener
import com.google.firebase.database.FirebaseDatabase
import com.malkinfo.editingrecyclerview.R
import com.malkinfo.editingrecyclerview.model.UserData


class UserAdapter(val c:Context,val userList:ArrayList<UserData>):RecyclerView.Adapter<UserAdapter.UserViewHolder>()
{



  inner class UserViewHolder(val v:View):RecyclerView.ViewHolder(v){
      var calorie:TextView
      var foodKind:TextView
      var foodInfo:TextView
      var mMenus:ImageView
      val database : FirebaseDatabase = FirebaseDatabase.getInstance()

      init {
          calorie = v.findViewById<TextView>(R.id.mTitle)
          foodKind = v.findViewById<TextView>(R.id.mSubTitle)
          foodInfo = v.findViewById<TextView>(R.id.mSubTitle2)
          mMenus = v.findViewById(R.id.mMenus)
          mMenus.setOnClickListener { popupMenus(it) }

      }

      private fun popupMenus(v:View) {
          val position = userList[adapterPosition]
          val popupMenus = PopupMenu(c,v)
          popupMenus.inflate(R.menu.show_menu)
          popupMenus.setOnMenuItemClickListener {
              when(it.itemId){
                  R.id.editText->{
                      val v = LayoutInflater.from(c).inflate(R.layout.add_item,null)
                      val name = v.findViewById<EditText>(R.id.calorie)
                      val number = v.findViewById<EditText>(R.id.foodKind)
                              AlertDialog.Builder(c)
                                      .setView(v)
                                      .setPositiveButton("Ok"){
                                          dialog,_->
                                          position.calorie = name.text.toString()
                                          position.foodKind = number.text.toString()
                                          notifyDataSetChanged()
                                          Toast.makeText(c,"User Information is Edited",Toast.LENGTH_SHORT).show()
                                          dialog.dismiss()

                                      }
                                      .setNegativeButton("Cancel"){
                                          dialog,_->
                                          dialog.dismiss()

                                      }
                                      .create()
                                      .show()

                      true
                  }
                  R.id.delete->{
                      /**set delete*/
                      AlertDialog.Builder(c)
                              .setTitle("Delete")
                              .setIcon(R.drawable.ic_warning)
                              .setMessage("Are you sure delete this Information")
                              .setPositiveButton("Yes"){
                                  dialog,_->
                                  userList.removeAt(adapterPosition)
                                  notifyDataSetChanged()
                                  Toast.makeText(c,"Deleted this Information",Toast.LENGTH_SHORT).show()
                                  dialog.dismiss()


                                      // 데이터베이스 정보 삭제.
//                                  database.getReference().child("food")
//                                      .child(uidList.get(position)).removeValue()
//                                      .addOnSuccessListener(
//                                          OnSuccessListener<Void?> {
//                                              Toast.makeText(this, "삭제 성공", Toast.LENGTH_SHORT).show()
//                                          }).addOnFailureListener(
//                                          OnFailureListener { e ->
//                                              println("error: " + e.message)
//                                              Toast.makeText(this, "삭제 실패", Toast.LENGTH_SHORT).show()
//                                          })
                              }
                              .setNegativeButton("No"){
                                  dialog,_->
                                  dialog.dismiss()
                              }
                              .create()
                              .show()

                      true
                  }
                  else-> true
              }

          }
          popupMenus.show()
          val popup = PopupMenu::class.java.getDeclaredField("mPopup")
          popup.isAccessible = true
          val menu = popup.get(popupMenus)
          menu.javaClass.getDeclaredMethod("setForceShowIcon",Boolean::class.java)
                  .invoke(menu,true)
      }
  }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): UserViewHolder {
       val inflater = LayoutInflater.from(parent.context)
        val v  = inflater.inflate(R.layout.list_item,parent,false)
        return UserViewHolder(v)
    }

    override fun onBindViewHolder(holder: UserViewHolder, position: Int) {
       val newList = userList[position]
        holder.calorie.text = newList.calorie
        holder.foodKind.text = newList.foodKind
        holder.foodInfo.text = newList.foodInfo
    }

    override fun getItemCount(): Int {
      return  userList.size
    }
}