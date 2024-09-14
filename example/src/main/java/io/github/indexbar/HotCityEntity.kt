package io.github.indexbar

data class HotCityEntity(
    val letter: String,
    val list: MutableList<CityDetailEntity>
)

data class CityDetailEntity(
    val name: String,
    val adCode: String,
    val cityCode: String
)