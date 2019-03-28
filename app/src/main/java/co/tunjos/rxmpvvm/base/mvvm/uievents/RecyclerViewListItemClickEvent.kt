package co.tunjos.rxmpvvm.base.mvvm.uievents

data class RecyclerViewListItemClickEvent<VM>(val viewModel: VM) : UiEvent
