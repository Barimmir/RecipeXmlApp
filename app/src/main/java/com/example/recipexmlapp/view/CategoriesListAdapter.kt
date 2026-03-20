package com.example.recipexmlapp.view

import android.graphics.drawable.Drawable
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.recipexmlapp.databinding.ItemCategoryBinding
import com.example.recipexmlapp.model.Category
import java.io.InputStream

class CategoriesListAdapter(private val dataSet: List<Category>) : 
    RecyclerView.Adapter<CategoriesListAdapter.ViewHolder>() {

    class ViewHolder(private val binding: ItemCategoryBinding) : RecyclerView.ViewHolder(binding.root) {
        val ivCategory = binding.ivCategory
        val tvCategoryTitle = binding.tvCategoryTitle
        val tvCategoryDescription = binding.tvCategoryDescription
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = ItemCategoryBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val category = dataSet[position]
        
        holder.tvCategoryTitle.text = category.title
        holder.tvCategoryDescription.text = category.description
        
        try {
            val inputStream: InputStream = holder.itemView.context.assets.open(category.imageName)
            val drawable = Drawable.createFromStream(inputStream, null)
            holder.ivCategory.setImageDrawable(drawable)
            inputStream.close()
        } catch (e: Exception) {
            Log.e("CategoriesListAdapter", "Error loading image from assets: ${category.imageName}", e)
        }
    }

    override fun getItemCount() = dataSet.size
}
