package co.tunjos.rxmpvvm.extensions

fun <E> MutableCollection<E>?.replaceWith(newList: List<E>) =
    when {
        this == null -> newList.toMutableList()
        else -> this.apply {
            clear()
            addAll(newList)
        }
    }
