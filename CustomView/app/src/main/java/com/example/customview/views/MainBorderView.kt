import android.animation.ValueAnimator
import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.LinearGradient
import android.graphics.Matrix
import android.graphics.Paint
import android.graphics.Path
import android.graphics.RectF
import android.graphics.Shader
import android.util.AttributeSet
import android.view.View
import android.view.animation.LinearInterpolator
import com.example.customview.DensityUtil
import com.example.customview.R

class MainBorderView : View {
    constructor(context: Context?) : super(context)
    constructor(context: Context?, attrs: AttributeSet?) : super(context, attrs)
    constructor(context: Context?, attrs: AttributeSet?, defStyleAttr: Int) : super(context, attrs, defStyleAttr)

    private var mRotate: Int = 0
    private var mStartAnimate: Boolean = false
    private val BORDER_RADIUS: Float = DensityUtil.dp2Px(9)

    private val BORDER_FOCUS_PADDING: Float = 2f
    private val BORDER_UN_FOCUS_PADDING: Float = 5f


    private val BORDER_FOCUS_WIDTH: Float = 3f
    private val BORDER_UN_FOCUS_WIDTH: Float = 1f

    private val mPaint: Paint = Paint()
    private val mFocusRectPath: Path = Path()
    private val mUnFocusRectPath: Path = Path()

    private var mLinearFocusGradient: LinearGradient? = null
    private var mLinearFocusMatrix: Matrix = Matrix()


    private var mLinearUnFocusGradient: LinearGradient? = null
    private var mLinearUnFocusMatrix: Matrix = Matrix()

    private var mValueAnimator: ValueAnimator? = null

    init {
        mPaint.isAntiAlias = true
        mPaint.isDither = true
        mPaint.color = Color.WHITE
        mPaint.style = Paint.Style.STROKE
    }

    override fun onSizeChanged(w: Int, h: Int, oldw: Int, oldh: Int) {
        super.onSizeChanged(w, h, oldw, oldh)

        mFocusRectPath.reset()
        mFocusRectPath.addRoundRect(
            RectF(
                BORDER_FOCUS_PADDING, BORDER_FOCUS_PADDING,
                measuredWidth - BORDER_FOCUS_PADDING,
                measuredHeight - BORDER_FOCUS_PADDING
            ),
            BORDER_RADIUS, BORDER_RADIUS, Path.Direction.CCW
        )

        mUnFocusRectPath.reset()
        mUnFocusRectPath.addRoundRect(
            RectF(
                BORDER_UN_FOCUS_PADDING, BORDER_UN_FOCUS_PADDING,
                measuredWidth - BORDER_UN_FOCUS_PADDING,
                measuredHeight - BORDER_UN_FOCUS_PADDING
            ),
            BORDER_RADIUS, BORDER_RADIUS, Path.Direction.CCW
        )

        mLinearFocusGradient = LinearGradient(
            0f, 0f, measuredWidth.toFloat(), 0f,
            intArrayOf(resources.getColor(R.color.color_F1F1F1_10), resources.getColor(R.color.color_FFFFFF), resources.getColor(R.color.color_F1F1F1_10)), floatArrayOf(0.2f, 0.5f, 0.8f),
            Shader.TileMode.CLAMP
        )

        mLinearUnFocusMatrix.setRotate(90f, measuredWidth / 2f, measuredHeight / 2f)
        mLinearUnFocusGradient = LinearGradient(
            0f, 0f, measuredWidth.toFloat(), 0f,
//            intArrayOf(resources.getColor(R.color.color_F1F1F1_80), Color.TRANSPARENT), floatArrayOf(0f, 0.5f),
            intArrayOf(Color.WHITE, Color.TRANSPARENT), floatArrayOf(0f, 0.5f),
            Shader.TileMode.CLAMP
        )
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)

        if (mStartAnimate) {
            mPaint.strokeWidth = BORDER_FOCUS_WIDTH
            mLinearFocusMatrix.setRotate(mRotate.toFloat(), measuredWidth / 2f, measuredHeight / 2f)
            mLinearFocusGradient?.setLocalMatrix(mLinearFocusMatrix)
            mPaint.shader = mLinearFocusGradient
            canvas.drawPath(mFocusRectPath, mPaint)
        } else {
            mLinearUnFocusGradient?.setLocalMatrix(mLinearUnFocusMatrix)
            mPaint.shader = mLinearUnFocusGradient
            mPaint.strokeWidth = BORDER_UN_FOCUS_WIDTH
            canvas.drawPath(mUnFocusRectPath, mPaint)
        }
    }

    public fun startAnimate() {
        mRotate = 0
        mStartAnimate = true

        mValueAnimator = ValueAnimator.ofInt(60, 240)
        mValueAnimator?.duration = 2000
        mValueAnimator?.addUpdateListener {
            mRotate = it.animatedValue as Int
            invalidate()
        }
        mValueAnimator?.interpolator = LinearInterpolator()
        mValueAnimator?.repeatMode = ValueAnimator.RESTART
        mValueAnimator?.repeatCount = ValueAnimator.INFINITE
        mValueAnimator?.start()
    }

    public fun stopAnimate() {
        mStartAnimate = false
        mValueAnimator?.cancel()
        invalidate()
    }

}