package co.tunjos.rxmpvvm.base.databinding;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.BindingAdapter;
import androidx.recyclerview.widget.RecyclerView;
import co.tunjos.rxmpvvm.base.adapters.SimpleDataBindingListAdapter;

import java.util.ArrayList;
import java.util.List;

//TODO convert to Kotlin, issues with adapter.setItems(items) when Kotlinized
public final class RecyclerViewItemsBindingAdapter {

    private RecyclerViewItemsBindingAdapter() {
    }

    @BindingAdapter("items")
    public static void setRecyclerAdapterItems(
            @NonNull final RecyclerView recyclerView,
            @Nullable final List<?> items
    ) {
        final RecyclerView.Adapter adapter = recyclerView.getAdapter();

        if (adapter == null) {
            throw new IllegalStateException(
                    "SimpleDataBindingListAdapter must be set on the RecyclerView before binding");
        }

        if (adapter instanceof SimpleDataBindingListAdapter) {
            handleSimpleDataBindingAdapter((SimpleDataBindingListAdapter) adapter, items);
        } else {
            throw new IllegalStateException(
                    "The adapter must be an instance of SimpleDataBindingListAdapter"
            );
        }
    }

    private static void handleSimpleDataBindingAdapter(
            @NonNull final SimpleDataBindingListAdapter adapter,
            @Nullable final List<?> items
    ) {
        if (items != null && !items.isEmpty()) {
            //noinspection unchecked
            adapter.setItems(items);
        } else {
            //noinspection unchecked
            adapter.setItems(new ArrayList());
        }
    }
}
