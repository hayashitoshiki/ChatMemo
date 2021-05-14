package com.myapp.chatmemo.ui.utils.transition

import android.animation.*
import android.content.Context
import android.graphics.*
import android.graphics.drawable.BitmapDrawable
import android.graphics.drawable.ColorDrawable
import android.graphics.drawable.Drawable
import android.graphics.drawable.ShapeDrawable
import android.graphics.drawable.shapes.OvalShape
import android.util.ArrayMap
import android.util.AttributeSet
import android.view.View
import android.view.ViewAnimationUtils
import android.view.ViewGroup
import androidx.annotation.ColorInt
import androidx.transition.Transition
import androidx.transition.TransitionValues
import kotlin.math.sqrt

class PlayTransition : Transition {
    var color = Color.parseColor("#6c1622")
    private var context: Context

    constructor(
        context: Context,
        @ColorInt fabColor: Int
    ) {
        this.context = context
        color = fabColor
    }

    constructor(
        context: Context,
        attrs: AttributeSet?
    ) : super(context, attrs) {
        this.context = context
        // val a = context.obtainStyledAttributes(attrs, R.styleable.PlayTransition)
        // color = a.getColor(R.styleable.PlayTransition_colorCT, color)
        // a.recycle()
    }

    override fun getTransitionProperties(): Array<String> {
        return TRANSITION_PROPERTIES
    }

    private fun captureValues(transitionValues: TransitionValues) {
        val view = transitionValues.view
        transitionValues.values[PROPERTY_BOUNDS] = Rect(view.left, view.top, view.right, view.bottom)
        val position = IntArray(2)
        transitionValues.view.getLocationInWindow(position)
        transitionValues.values[PROPERTY_POSITION] = position
    }

    override fun captureEndValues(transitionValues: TransitionValues) {
        val view = transitionValues.view
        if (view.width <= 0 || view.height <= 0) {
            return
        }
        captureValues(transitionValues)
    }

    override fun captureStartValues(transitionValues: TransitionValues) {
        val view = transitionValues.view
        if (view.width <= 0 || view.height <= 0) {
            return
        }
        captureValues(transitionValues)
        val bitmap = Bitmap.createBitmap(view.width, view.height, Bitmap.Config.ARGB_8888)
        val canvas = Canvas(bitmap)
        view.draw(canvas)
        transitionValues.values[PROPERTY_IMAGE] = bitmap
    }

    override fun createAnimator(
        sceneRoot: ViewGroup,
        startValues: TransitionValues?,
        endValues: TransitionValues?
    ): Animator? {
        if (startValues == null || endValues == null) {
            return null
        }
        val startBounds = startValues.values[PROPERTY_BOUNDS] as Rect?
        val endBounds = endValues.values[PROPERTY_BOUNDS] as Rect?
        if (startBounds == null || endBounds == null || startBounds == endBounds) {
            return null
        }
        val startImage = startValues.values[PROPERTY_IMAGE] as Bitmap?
        val startBackground: Drawable = BitmapDrawable(context.resources, startImage)
        val startView = addViewToOverlay(sceneRoot, startImage!!.width, startImage.height, startBackground)
        val shrinkingBackground: Drawable = ColorDrawable(color)
        val shrinkingView = addViewToOverlay(sceneRoot, startImage.width, startImage.height, shrinkingBackground)
        val sceneRootLoc = IntArray(2)
        sceneRoot.getLocationInWindow(sceneRootLoc)
        val startLoc = startValues.values[PROPERTY_POSITION] as IntArray?
        val startTranslationX = startLoc!![0] - sceneRootLoc[0]
        val startTranslationY = startLoc[1] - sceneRootLoc[1]
        startView.translationX = startTranslationX.toFloat()
        startView.translationY = startTranslationY.toFloat()
        shrinkingView.translationX = startTranslationX.toFloat()
        shrinkingView.translationY = startTranslationY.toFloat()
        val endView = endValues.view
        val startRadius = calculateMaxRadius(shrinkingView)
        val minRadius = calculateMinRadius(shrinkingView).coerceAtMost(calculateMinRadius(endView))
        val circleBackground = ShapeDrawable(OvalShape())
        circleBackground.paint.color = color
        val circleView = addViewToOverlay(sceneRoot, minRadius * 2, minRadius * 2, circleBackground)
        val circleStartX = startLoc[0] - sceneRootLoc[0] + ((startView.width - circleView.width) / 2).toFloat()
        val circleStartY = startLoc[1] - sceneRootLoc[1] + ((startView.height - circleView.height) / 2).toFloat()
        circleView.translationX = circleStartX
        circleView.translationY = circleStartY
        circleView.visibility = View.INVISIBLE
        shrinkingView.alpha = 0f
        endView.alpha = 0f
        val shrinkingAnimator = createCircularReveal(shrinkingView, startRadius, minRadius.toFloat())
        shrinkingAnimator.addListener(object : AnimatorListenerAdapter() {
            override fun onAnimationEnd(animation: Animator) {
                shrinkingView.visibility = View.INVISIBLE
                startView.visibility = View.INVISIBLE
                circleView.visibility = View.VISIBLE
            }
        })
        val startAnimator = createCircularReveal(startView, startRadius, minRadius.toFloat())
        val fadeInAnimator: Animator = ObjectAnimator.ofFloat(shrinkingView, View.ALPHA, 0f, 1f)
        val shrinkFadeSet = AnimatorSet()
        shrinkFadeSet.playTogether(shrinkingAnimator, startAnimator, fadeInAnimator)
        val endLoc = endValues.values[PROPERTY_POSITION] as IntArray?
        val circleEndX = endLoc!![0] - sceneRootLoc[0] + ((endView.width - circleView.width) / 2).toFloat()
        val circleEndY = endLoc[1] - sceneRootLoc[1] + ((endView.height - circleView.height) / 2).toFloat()
        val circlePath: Path = pathMotion.getPath(circleStartX, circleStartY, circleEndX, circleEndY)
        val circleAnimator: Animator = ObjectAnimator.ofFloat(circleView, View.TRANSLATION_X, View.TRANSLATION_Y, circlePath)
        val growingView = addViewToOverlay(sceneRoot, endView.width, endView.height, shrinkingBackground)
        growingView.visibility = View.INVISIBLE
        val endTranslationX = endLoc[0] - sceneRootLoc[0].toFloat()
        val endTranslationY = endLoc[1] - sceneRootLoc[1].toFloat()
        growingView.translationX = endTranslationX
        growingView.translationY = endTranslationY
        val endRadius = calculateMaxRadius(endView)
        circleAnimator.addListener(object : AnimatorListenerAdapter() {
            override fun onAnimationEnd(animation: Animator) {
                circleView.visibility = View.INVISIBLE
                growingView.visibility = View.VISIBLE
                endView.alpha = 1f
            }
        })
        val fadeOutAnimator: Animator = ObjectAnimator.ofFloat(growingView, View.ALPHA, 1f, 0f)
        val endAnimator = createCircularReveal(endView, minRadius.toFloat(), endRadius)
        val growingAnimator = createCircularReveal(growingView, minRadius.toFloat(), endRadius)
        growingAnimator.addListener(object : AnimatorListenerAdapter() {
            override fun onAnimationEnd(animation: Animator) {
                sceneRoot.overlay.remove(startView)
                sceneRoot.overlay.remove(shrinkingView)
                sceneRoot.overlay.remove(circleView)
                sceneRoot.overlay.remove(growingView)
            }
        })
        val growingFadeSet = AnimatorSet()
        growingFadeSet.playTogether(fadeOutAnimator, endAnimator, growingAnimator)
        val animatorSet = AnimatorSet()
        animatorSet.playSequentially(shrinkFadeSet, circleAnimator, growingFadeSet)
        return animatorSet
    }

    private fun addViewToOverlay(
        sceneRoot: ViewGroup,
        width: Int,
        height: Int,
        background: Drawable
    ): View {
        val view: View = NoOverlapView(sceneRoot.context)
        view.background = background
        val widthSpec = View.MeasureSpec.makeMeasureSpec(width, View.MeasureSpec.EXACTLY)
        val heightSpec = View.MeasureSpec.makeMeasureSpec(height, View.MeasureSpec.EXACTLY)
        view.measure(widthSpec, heightSpec)
        view.layout(0, 0, width, height)
        sceneRoot.overlay.add(view)
        return view
    }

    private fun createCircularReveal(
        view: View,
        startRadius: Float,
        endRadius: Float
    ): Animator {
        val centerX = view.width / 2
        val centerY = view.height / 2
        // 円形アニメーション
        val reveal = ViewAnimationUtils.createCircularReveal(view, centerX, centerY, startRadius, endRadius)
        return NoPauseAnimator(reveal)
    }

    private class NoPauseAnimator(private val mAnimator: Animator) : Animator() {
        private val mListeners = ArrayMap<AnimatorListener, AnimatorListener>()
        override fun addListener(listener: AnimatorListener) {
            val wrapper: AnimatorListener = AnimatorListenerWrapper(this, listener)
            if (!mListeners.containsKey(listener)) {
                mListeners[listener] = wrapper
                mAnimator.addListener(wrapper)
            }
        }

        override fun cancel() {
            mAnimator.cancel()
        }

        override fun end() {
            mAnimator.end()
        }

        override fun getDuration(): Long {
            return mAnimator.duration
        }

        override fun getInterpolator(): TimeInterpolator {
            return mAnimator.interpolator
        }

        override fun getListeners(): ArrayList<AnimatorListener> {
            return ArrayList(mListeners.keys)
        }

        override fun getStartDelay(): Long {
            return mAnimator.startDelay
        }

        override fun isPaused(): Boolean {
            return mAnimator.isPaused
        }

        override fun isRunning(): Boolean {
            return mAnimator.isRunning
        }

        override fun isStarted(): Boolean {
            return mAnimator.isStarted
        }

        override fun removeAllListeners() {
            mListeners.clear()
            mAnimator.removeAllListeners()
        }

        override fun removeListener(listener: AnimatorListener) {
            val wrapper = mListeners[listener]
            if (wrapper != null) {
                mListeners.remove(listener)
                mAnimator.removeListener(wrapper)
            }
        }

        override fun setDuration(durationMS: Long): Animator {
            mAnimator.duration = durationMS
            return this
        }

        override fun setInterpolator(timeInterpolator: TimeInterpolator) {
            mAnimator.interpolator = timeInterpolator
        }

        override fun setStartDelay(delayMS: Long) {
            mAnimator.startDelay = delayMS
        }

        override fun setTarget(target: Any?) {
            mAnimator.setTarget(target)
        }

        override fun setupEndValues() {
            mAnimator.setupEndValues()
        }

        override fun setupStartValues() {
            mAnimator.setupStartValues()
        }

        override fun start() {
            mAnimator.start()
        }
    }

    private class AnimatorListenerWrapper(
        private val mAnimator: Animator,
        private val mListener: Animator.AnimatorListener
    ) : Animator.AnimatorListener {
        override fun onAnimationStart(animator: Animator) {
            mListener.onAnimationStart(mAnimator)
        }

        override fun onAnimationEnd(animator: Animator) {
            mListener.onAnimationEnd(mAnimator)
        }

        override fun onAnimationCancel(animator: Animator) {
            mListener.onAnimationCancel(mAnimator)
        }

        override fun onAnimationRepeat(animator: Animator) {
            mListener.onAnimationRepeat(mAnimator)
        }
    }

    private class NoOverlapView(context: Context?) : View(context) {
        override fun hasOverlappingRendering(): Boolean {
            return false
        }
    }

    companion object {
        private const val PROPERTY_BOUNDS = "circleTransition:bounds"
        private const val PROPERTY_POSITION = "circleTransition:position"
        private const val PROPERTY_IMAGE = "circleTransition:image"
        private val TRANSITION_PROPERTIES = arrayOf(PROPERTY_BOUNDS, PROPERTY_POSITION)

        fun calculateMaxRadius(view: View): Float {
            val widthSquared = view.width * view.width.toFloat()
            val heightSquared = view.height * view.height.toFloat()
            return sqrt(widthSquared + heightSquared) / 2
        }

        fun calculateMinRadius(view: View): Int {
            return (view.width / 2).coerceAtMost(view.height / 2)
        }
    }
}
