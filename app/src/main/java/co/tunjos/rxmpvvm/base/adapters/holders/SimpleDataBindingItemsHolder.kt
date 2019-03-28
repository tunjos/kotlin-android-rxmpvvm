package co.tunjos.rxmpvvm.base.adapters.holders

import androidx.recyclerview.widget.RecyclerView
import co.tunjos.rxmpvvm.extensions.replaceWith

/**
 * A simple implementation of an [ItemsHolder] that calls [RecyclerView.Adapter.notifyDataSetChanged]  when it's items
 * are set.
 */
class SimpleDataBindingItemsHolder<VM> : ItemsHolder<VM> {

    private val items = mutableListOf<VM>()

    override fun getItemCount() = items.size

    override fun getItem(position: Int) =
        if (position < getItemCount()) items[position] else throw IndexOutOfBoundsException()

    override fun setItems(adapter: RecyclerView.Adapter<*>, newItems: List<VM>) {
        items.replaceWith(newItems)
        adapter.notifyDataSetChanged()
    }
}
