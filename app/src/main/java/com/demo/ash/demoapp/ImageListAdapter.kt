package com.demo.ash.demoapp

import android.graphics.Bitmap
import android.graphics.drawable.BitmapDrawable
import android.net.Uri
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.bumptech.glide.Glide
import com.firebase.ui.storage.images.FirebaseImageLoader
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import kotlinx.android.synthetic.main.treatment_image.view.*
import android.provider.MediaStore.Images.Media.getBitmap
import com.google.android.gms.tasks.OnSuccessListener


/**
 * Created by ashwin on 3/27/2018.
 */
class ImageListAdapter(var imageSrcList: MutableList<String>, var client: String, var patient: String, var tid: String): RecyclerView.Adapter<ImageViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup?, viewType: Int): ImageViewHolder{
        val layoutInflator = LayoutInflater.from(parent?.context)
        val cellForRow = layoutInflator.inflate(R.layout.treatment_image, parent, false)
        return ImageViewHolder(cellForRow)
    }

    override fun getItemCount(): Int {
        return imageSrcList.size
    }

    override fun onBindViewHolder(holder: ImageViewHolder, position: Int) {
        val row = imageSrcList.get(position)

        Glide.with(holder?.view?.context).load(row).into(holder?.view?.image_treat)

    }

}

class ImageViewHolder(var view: View): RecyclerView.ViewHolder(view){

}
