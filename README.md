# Android索引检索View
![example](https://github.com/WestMyna/IndexBar/blob/main/example.gif)

## 如何使用
    <io.github.library.IndexBar
        android:id="@+id/index_bar"
        android:layout_width="wrap_content"
        android:layout_height="wrap_content"
        android:layout_marginEnd="20dp"
        android:clickable="true"
        android:focusable="true"
        app:index_array="@array/example"
        app:layout_constraintBottom_toBottomOf="parent"
        app:layout_constraintEnd_toEndOf="parent"
        app:layout_constraintTop_toTopOf="parent"
        app:selected_bg_color="@color/teal_200"
        app:text_color="@color/white"
        app:text_padding="2dp"
        app:text_size="10sp" />

        <!--        索引文字大小-->
        <attr name="text_size" format="dimension" />
        <!--        索引文字颜色-->
        <attr name="text_color" format="color" />
        <!--        索引数组-->
        <attr name="index_array" format="string" />
        <!--        索引文字上下间距-->
        <attr name="text_padding" format="dimension" />
        <!--        索引选中背景颜色-->
        <attr name="selected_bg_color" format="color" />

## 设置数据
### 1.通过xml设置string-arrays
    app:index_array="@array/example"
### 2.通过代码设置list
    /**
     * 设置索引资源
     */
    fun setIndexResource(indexList: MutableList<String>) {
        mIndexList.clear()
        for (s in indexList) {
            mIndexList.add(IndexEntity().apply {
                text = s
            })
        }
        calculate()
        invalidate()
    }

## 设置字体大小
### 1.通过xml设置
    app:text_size="10sp"
### 2.通过代码设置
    /**
     * 设置索引字体大小
     *
     * @param textSize sp
     */
    fun setIndexTextSize(textSize: Float) {
        mIndexTextSize = textSize
        mIndexTextPaint.textSize = sp2px(context, mIndexTextSize).toFloat()
        calculate()
        invalidate()
    }

## 设置字体颜色
### 1.通过xml设置
    app:text_color="@color/white"
### 2.通过代码设置
    /**
     * 设置索引文字颜色
     *
     * @param textColor
     */
    fun setIndexTextColor(textColor: Int) {
        mIndexTextColor = textColor
        mIndexTextPaint.color = mIndexTextColor
        invalidate()
    }

## 设置文字间距
### 1.通过xml设置
    app:text_padding="2dp"
### 2.通过代码设置
    /**
     * 设置索引文字间距
     *
     * @param textPadding dp
     */
    fun setIndexTextPadding(textPadding: Float) {
        mIndexTextPadding = textPadding
        calculate()
        invalidate()
    }

## 设置选中背景颜色
### 1.通过xml设置
    app:selected_bg_color="@color/teal_200"
### 2.通过代码设置
    /**
     * 设置选中索引背景颜色
     *
     * @param selectedBgColor
     */
    fun setSelectedBgColor(selectedBgColor: Int) {
        mIndexSelectedBgColor = selectedBgColor
        mIndexSelectedBgPaint.color = mIndexSelectedBgColor
        invalidate()
    }

## 设置选中监听
    /**
     * 设置索引选中监听
     *
     * @param indexListener
     */
    fun setIndexListener(indexListener: IndexListener) {
        mIndexListener = indexListener
    }
