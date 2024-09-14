package io.github.indexbar

import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import io.github.indexbar.databinding.ActivityMainBinding
import io.github.library.IndexListener
import java.io.InputStream

class MainActivity : AppCompatActivity(), IndexListener {

    private lateinit var mBinding: ActivityMainBinding

    private val hotCityList = mutableListOf<HotCityEntity>()
    private lateinit var hotCityAdapter: HotCityAdapter
    private var selectedText: String? = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        mBinding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(mBinding.root)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
        mBinding.indexBar.setIndexListener(this)
        getDataFromAssets()
        hotCityAdapter = HotCityAdapter(hotCityList)
        mBinding.rv.layoutManager = LinearLayoutManager(this)
        mBinding.rv.adapter = hotCityAdapter
    }

    private fun getDataFromAssets() {
        try {
            val inputStream: InputStream = assets.open("city.json")
            val jsonString = inputStream.bufferedReader().use { it.readText() }
            val typeToken = object : TypeToken<MutableList<HotCityEntity>>() {}.type
            val gson = Gson()
            hotCityList.addAll(gson.fromJson(/* json = */ jsonString,/* typeOfT = */ typeToken))
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    override fun onIndexSelected(text: String) {
        if (hotCityList.isEmpty()) return
        if (selectedText == text) return
        selectedText = text
        val hotCityEntity = hotCityList.find {
            it.letter == selectedText
        } ?: return
        val position = hotCityList.indexOf(hotCityEntity)
        val linearLayoutManager = mBinding.rv.layoutManager as? LinearLayoutManager ?: return
        linearLayoutManager.scrollToPositionWithOffset(position, 0)
    }
}