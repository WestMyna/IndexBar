package io.github.library

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.Rect
import android.graphics.RectF
import android.util.AttributeSet
import android.util.Log
import android.view.MotionEvent
import android.view.View
import androidx.core.content.res.getTextArrayOrThrow
import io.github.library.Utils.dip2px
import io.github.library.Utils.sp2px

class IndexBar : View {

    private var mWidth: Int = 0
    private var mHeight: Int = 0

    //索引画笔
    private val mIndexTextPaint = Paint().apply {
        isAntiAlias = true
        isDither = true
        textAlign = Paint.Align.CENTER
    }

    //选中背景画笔
    private val mIndexSelectedBgPaint = Paint().apply {
        isAntiAlias = true
        isDither = true
    }

    //索引字体大小
    private var mIndexTextSize = DEFAULT_INDEX_TEXT_SIZE

    //索引字体颜色
    private var mIndexTextColor = DEFAULT_INDEX_TEXT_COLOR

    //索引选中背景颜色
    private var mIndexSelectedBgColor = DEFAULT_INDEX_SELECTED_BG_COLOR

    //索引字体之间的边距
    private var mIndexTextPadding = DEFAULT_INDEX_TEXT_PADDING

    //索引列表
    private var mIndexList = mutableListOf<IndexEntity>()

    //选中的索引
    private var mSelectedIndexEntity: IndexEntity? = null

    private var mIndexListener: IndexListener? = null

    companion object {
        private const val TAG = "IndexBar"
        private const val DEFAULT_INDEX_TEXT_SIZE = 10f//默认字体大小
        private const val DEFAULT_INDEX_TEXT_COLOR = Color.WHITE//默认字体颜色
        private const val DEFAULT_INDEX_SELECTED_BG_COLOR = Color.GREEN//默认选中背景颜色
        private const val DEFAULT_INDEX_TEXT_PADDING = 2f//文字默认边距
    }

    constructor(context: Context) : this(context, null)
    constructor(context: Context, attributeSet: AttributeSet?) : this(context, attributeSet, 0)
    constructor(context: Context, attributeSet: AttributeSet?, defStyleAttr: Int) : super(
        context,
        attributeSet,
        defStyleAttr
    ) {
        val typedArray = context.obtainStyledAttributes(attributeSet, R.styleable.IndexBar)

        mIndexTextSize =
            typedArray.getDimension(R.styleable.IndexBar_text_size, DEFAULT_INDEX_TEXT_SIZE)
        mIndexTextColor =
            typedArray.getColor(R.styleable.IndexBar_text_color, DEFAULT_INDEX_TEXT_COLOR)
        mIndexTextPaint.run {
            this.color = mIndexTextColor
            this.textSize = mIndexTextSize
        }

        mIndexSelectedBgColor =
            typedArray.getColor(
                R.styleable.IndexBar_selected_bg_color,
                DEFAULT_INDEX_SELECTED_BG_COLOR
            )
        mIndexSelectedBgPaint.color = mIndexSelectedBgColor

        mIndexTextPadding =
            typedArray.getDimension(R.styleable.IndexBar_text_padding, DEFAULT_INDEX_TEXT_PADDING)

        val indexArray = typedArray.getTextArray(R.styleable.IndexBar_index_array)
        if (!indexArray.isNullOrEmpty()) {
            fillIndexList(indexArray)
        }
        calculate()
        typedArray.recycle()
    }

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

    fun setSelectedBgColor(selectedBgColor: Int) {
        mIndexSelectedBgColor = selectedBgColor
        mIndexSelectedBgPaint.color = mIndexSelectedBgColor
        invalidate()
    }

    /**
     * 设置索引选中监听
     *
     * @param indexListener
     */
    fun setIndexListener(indexListener: IndexListener) {
        mIndexListener = indexListener
    }

    /**
     * 填充indexList
     * @param indexArray
     */
    private fun fillIndexList(indexArray: Array<CharSequence>) {
        mIndexList.clear()
        val charMutableList = indexArray.toMutableList()
        charMutableList.forEach { char ->
            mIndexList.add(IndexEntity().apply {
                this.text = char.toString()
            })
        }
    }

    //根据索引个数计算出view的宽高以及索引的位置
    private fun calculate() {
        if (mIndexList.isEmpty()) return
        val indexWidthList = mutableListOf<Int>()
        var indexTotalHeight = 0
        val rect = Rect()
        mIndexList.forEachIndexed { index, indexEntity ->
            val text = indexEntity.text
            //计算出每个索引文字的宽度
            mIndexTextPaint.getTextBounds(text, 0, text.length, rect)
            indexWidthList.add(rect.width())
            //总高度
            indexTotalHeight += rect.height() + dip2px(context, mIndexTextPadding) * 2
            //设置每个索引的pointF的y
            indexEntity.pointF.set(0f, indexTotalHeight.toFloat())
        }

        indexWidthList.sortDescending()
        //获取索引中最大的宽度
        val maxWidth = indexWidthList.first()
        mWidth = maxWidth + dip2px(context, mIndexTextPadding * 2)
        mHeight = indexTotalHeight
        for (indexEntity in mIndexList) {
            val pointF = indexEntity.pointF
            //设置每个索引的pointF的x
            pointF.set(mWidth.toFloat() / 2, pointF.y)
        }
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec)
        //因为是根据索引列表以及是否显示气泡去计算view的大小，所以不判断测量模式
        //设置最终的宽度和高度
        setMeasuredDimension(mWidth, mHeight)
    }

    @SuppressLint("DrawAllocation")
    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
        mSelectedIndexEntity?.let { entity ->
            val index = mIndexList.indexOf(entity)
            val pointF = entity.pointF
            val centerY = if (index == 0) {
                pointF.y / 2
            } else {
                pointF.y - ((pointF.y - mIndexList[index - 1].pointF.y) / 2)
            }
            val radius = pointF.x - dip2px(context, mIndexTextPadding / 2) / 2
            canvas.drawCircle(pointF.x, centerY, radius, mIndexSelectedBgPaint)
        }

        mIndexList.forEachIndexed { index, indexEntity ->
            val text = indexEntity.text
            val pointF = indexEntity.pointF
            val fontMetrics = mIndexTextPaint.getFontMetrics()
            val centerY = if (index == 0) {
                pointF.y / 2
            } else {
                pointF.y - ((pointF.y - mIndexList[index - 1].pointF.y) / 2)
            }
            val baseline = centerY + (fontMetrics.bottom - fontMetrics.top) / 2 - fontMetrics.bottom
            canvas.drawText(text, pointF.x, baseline, mIndexTextPaint)
            Log.d(TAG, "DRAW:${mIndexTextPaint.textSize}")
        }
    }

    override fun onTouchEvent(event: MotionEvent): Boolean {
        when (event.action) {
            MotionEvent.ACTION_DOWN, MotionEvent.ACTION_MOVE -> {
                Log.d(TAG, "x:${event.x}--y:${event.y}")
                mSelectedIndexEntity = findIndexByEventY(event.x, event.y)
                if (mSelectedIndexEntity == null) {
                    Log.d(TAG, "没有匹配到")
                } else {
                    Log.d(TAG, "IndexEntity:${mSelectedIndexEntity?.text}")
                    mIndexListener?.onIndexSelected(mSelectedIndexEntity!!.text)
                }
                invalidate()
            }

            else -> {
                mSelectedIndexEntity = null
                invalidate()
            }
        }
        return super.onTouchEvent(event)
    }

    /**
     * 根据坐标寻找满足的索引
     *
     * @param x
     * @param y
     *
     */
    private fun findIndexByEventY(x: Float, y: Float): IndexEntity? {
        val rect = RectF(0f, 0f, x, y)
        for (indexEntity in mIndexList) {
            val pointF = indexEntity.pointF
            val indexRect = RectF(0f, 0f, pointF.x, pointF.y)
            if (indexRect.contains(rect)) {
                return indexEntity
            }
        }
        return null
    }
}