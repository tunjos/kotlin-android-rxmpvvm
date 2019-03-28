package co.tunjos.rxmpvvm.base.adapters.holders

import androidx.recyclerview.widget.RecyclerView

interface ItemsHolder<VM> {

    fun getItemCount(): Int

    fun getItem(position: Int): VM

    fun isEmpty() = getItemCount() <= 0

    fun setItems(adapter: RecyclerView.Adapter<*>, newItems: List<VM>)
}
