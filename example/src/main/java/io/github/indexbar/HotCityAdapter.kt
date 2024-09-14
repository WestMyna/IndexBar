package io.github.indexbar

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import io.github.indexbar.databinding.ItemCityDetailBinding
import io.github.indexbar.databinding.ItemHotCityBinding

class HotCityAdapter(private val dataList: MutableList<HotCityEntity>) :
    RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return HotCityViewHolder(
            ItemHotCityBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }

    override fun getItemCount(): Int {
        return dataList.size
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        if (holder is HotCityViewHolder) {
            holder.bindData(position, dataList[position])
        }
    }

    private inner class HotCityViewHolder(private val hotCityBinding: ItemHotCityBinding) :
        RecyclerView.ViewHolder(hotCityBinding.root) {
        fun bindData(position: Int, hotCityEntity: HotCityEntity) {
            val cityDetailAdapter = CityDetailAdapter(hotCityEntity.list)
            hotCityBinding.run {
                tvLetter.text = hotCityEntity.letter
                rvCityDetail.layoutManager =
                    LinearLayoutManager(hotCityBinding.rvCityDetail.context)
                rvCityDetail.adapter = cityDetailAdapter
            }
        }
    }

    private inner class CityDetailAdapter(private val detailList: MutableList<CityDetailEntity>) :
        RecyclerView.Adapter<RecyclerView.ViewHolder>() {
        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
            return CityDetailViewHolder(
                ItemCityDetailBinding.inflate(
                    LayoutInflater.from(parent.context),
                    parent,
                    false
                )
            )
        }

        override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
            if (holder is CityDetailViewHolder) {
                holder.bindData(position, detailList[position])
            }
        }

        override fun getItemCount(): Int {
            return detailList.size
        }
    }

    private inner class CityDetailViewHolder(private val cityDetailBinding: ItemCityDetailBinding) :
        RecyclerView.ViewHolder(cityDetailBinding.root) {
        fun bindData(position: Int, cityDetailEntity: CityDetailEntity) {
            cityDetailBinding.tvName.text = cityDetailEntity.name
        }
    }
}