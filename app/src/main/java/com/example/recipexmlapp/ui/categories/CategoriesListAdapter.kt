package com.example.recipexmlapp.ui.categories

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.example.recipexmlapp.R
import com.example.recipexmlapp.databinding.ItemCategoryBinding
import com.example.recipexmlapp.data.Category
import com.example.recipexmlapp.data.ApiConstants

class CategoriesListAdapter(private var dataSet: List<Category>) : 
    RecyclerView.Adapter<CategoriesListAdapter.ViewHolder>() {

    private var itemClickListener: OnItemClickListener? = null

    interface OnItemClickListener {
        fun onItemClick(categoryId: Int)
    }

    fun setOnItemClickListener(listener: OnItemClickListener) {
        itemClickListener = listener
    }

    fun updateCategories(newCategories: List<Category>) {
        dataSet = newCategories
        notifyDataSetChanged()
    }

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
        
        val imageUrl = "${ApiConstants.IMAGE_BASE_URL}${category.imageName}"
        
        Glide.with(holder.itemView.context)
            .load(imageUrl)
            .placeholder(R.drawable.img_placeholder)
            .error(R.drawable.img_error)
            .diskCacheStrategy(DiskCacheStrategy.ALL)
            .into(holder.ivCategory)
        
        holder.itemView.setOnClickListener {
            itemClickListener?.onItemClick(category.id)
        }
    }

    override fun getItemCount() = dataSet.size
}
