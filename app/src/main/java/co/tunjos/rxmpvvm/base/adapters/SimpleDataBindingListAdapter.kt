package co.tunjos.rxmpvvm.base.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.LayoutRes
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.recyclerview.widget.RecyclerView
import co.tunjos.rxmpvvm.base.adapters.holders.ItemsHolder
import co.tunjos.rxmpvvm.base.adapters.holders.SimpleDataBindingItemsHolder
import co.tunjos.rxmpvvm.base.mvvm.uievents.OnBindPositionEvent
import co.tunjos.rxmpvvm.base.mvvm.uievents.RecyclerViewListItemClickEvent
import co.tunjos.rxmpvvm.base.mvvm.uievents.UiEvent
import co.tunjos.rxmpvvm.extensions.rx.view.click
import io.reactivex.Observable
import io.reactivex.disposables.Disposable
import io.reactivex.subjects.PublishSubject

/**
 * A simple adapter handles view models of one type only.
 *
 * @param layoutResId The id of the list item Layout to be used with this adapter.
 * @param modelVariable The model variable to be bound to the view. This is usually `BR.model`. No binding will occur if
 * it cannot be found.
 * @param uiEventsGenerator The function to be called to emit [UiEvent]s in response to events on the UI. The default
 * implementation generates  [RecyclerViewListItemClickEvent]  [UiEvent]s when the a list item's root view is clicked.
 * @param itemsHolder The holder for the items of this adapter.
 */
class SimpleDataBindingListAdapter<VM : Any>(
    @LayoutRes
    private val layoutResId: Int,
    private val modelVariable: Int,
    private val uiEventsGenerator: ((ViewDataBinding, VM) -> Observable<out UiEvent>)? = { binding, viewModel ->
        binding.root.click().map { RecyclerViewListItemClickEvent(viewModel) }
    },
    private val itemsHolder: ItemsHolder<VM> = SimpleDataBindingItemsHolder()
) : RecyclerView.Adapter<SimpleDataBindingViewHolder<VM>>() {

    val uiEvents: Observable<out UiEvent> get() = emitter.hide()
    private val emitter = PublishSubject.create<UiEvent>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SimpleDataBindingViewHolder<VM> {
        val rootView = LayoutInflater.from(parent.context).inflate(layoutResId, parent, false)
        val binding = DataBindingUtil.bind<ViewDataBinding>(rootView)!!
        return SimpleDataBindingViewHolder(rootView, binding)
    }

    override fun onBindViewHolder(holder: SimpleDataBindingViewHolder<VM>, position: Int) {
        with(holder) {
            viewModel = getItem(position)

            with(binding) {
                setVariable(modelVariable, viewModel)
                executePendingBindings()
            }

            // Automatic Garbage collection when UI component (Activity/Fragment) is destroyed.
            disposable?.dispose()
            disposable = uiEventsGenerator
                ?.invoke(binding, viewModel)
                ?.subscribe { events -> emitter.onNext(events) }

            // Emits the bound position to interested subscribed parties.
            emitter.onNext(OnBindPositionEvent(position))
        }
    }

    override fun getItemCount() = itemsHolder.getItemCount()

    fun getItem(position: Int): VM = itemsHolder.getItem(position)

    fun setItems(newItems: List<VM>) {
        itemsHolder.setItems(this, newItems)
    }

    fun isEmpty() = itemsHolder.isEmpty()
}

class SimpleDataBindingViewHolder<VM : Any>(
    view: View,
    val binding: ViewDataBinding,
    var disposable: Disposable? = null
) : RecyclerView.ViewHolder(view) {
    lateinit var viewModel: VM
}
