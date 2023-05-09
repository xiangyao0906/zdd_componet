package com.juju.core.binding.viewadpater.smart

import androidx.databinding.BindingAdapter
import com.juju.core.binding.command.BindingCommand
import com.scwang.smart.refresh.layout.SmartRefreshLayout
import com.scwang.smart.refresh.layout.api.RefreshLayout
import com.scwang.smart.refresh.layout.listener.OnRefreshLoadMoreListener

object ViewAdapter {

    @JvmStatic
    @BindingAdapter(value = ["onRefreshCommand"],requireAll = false)
    fun  onRefreshCommand(smartRefreshLayout: SmartRefreshLayout, bindingCommand: BindingCommand<Int>){
        smartRefreshLayout.setOnRefreshLoadMoreListener(object : OnRefreshLoadMoreListener {
            override fun onRefresh(refreshLayout: RefreshLayout) {
                bindingCommand.execute(0)
            }

            override fun onLoadMore(refreshLayout: RefreshLayout) {
                bindingCommand.execute(1)
            }
        })


    }
}