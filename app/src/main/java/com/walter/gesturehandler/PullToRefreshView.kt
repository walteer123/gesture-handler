package com.walter.gesturehandler

import android.annotation.SuppressLint
import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.MotionEvent
import android.view.View
import android.view.ViewGroup
import androidx.constraintlayout.widget.ConstraintLayout
import com.walter.gesturehandler.databinding.PullToRefreshViewBinding

@SuppressLint("ClickableViewAccessibility")
class PullToRefreshView
@JvmOverloads
constructor(
    context: Context,
    attributeSet: AttributeSet,
    defStyleAttr: Int = 0,
    defStyleRes: Int = 0
) : ConstraintLayout(context, attributeSet, defStyleAttr, defStyleRes), View.OnTouchListener {

    private var binding: PullToRefreshViewBinding? = null
    private var dy: Float = 0F
    private var refreshListener: OnRefreshListener? = null

    init {
        binding = PullToRefreshViewBinding.inflate(LayoutInflater.from(context), this, true)
        //View.inflate(context, R.layout.pull_to_refresh_view, this)
        binding?.refreshContainer?.setOnTouchListener(this)
    }

    override fun addView(child: View, index: Int, params: ViewGroup.LayoutParams) {
        if (binding?.refreshContainer == null) {
            super.addView(child, index, params)
        } else {
            binding?.refreshContainer?.addView(child, index, params)
        }
    }

    override fun onTouch(view: View?, event: MotionEvent?): Boolean {
        return when(event?.action) {
            MotionEvent.ACTION_DOWN -> {
                val containerY = binding?.refreshContainer?.translationY ?: 0F
                dy = containerY - event.rawY
                true
            }
            MotionEvent.ACTION_MOVE -> {
                val newY = event.rawY + dy
                val viewHeight = binding?.pullToRefreshView?.height

                if (newY <= 0 || newY >= viewHeight?.div(2) ?: 0)
                    return true

                moveToPos(newY)
                true
            }
            MotionEvent.ACTION_UP -> {
                val progressHeight = binding?.refreshProgressIndicator?.height ?: 0
                val posToBeMoved =
                    if(event.y < progressHeight)
                        0F
                    else
                        progressHeight.toFloat()

                moveToPos(posToBeMoved)

                refreshListener?.onRefresh()
                true
            }
            else -> false
        }
    }

    private fun moveToPos(newY: Float) {
        binding?.refreshContainer?.animate()?.apply {
            y(newY)
            duration = 0
            start()
        }
    }

    fun setRefreshing(isRefreshing: Boolean) {
       // binding?.refreshProgressIndicator.set
    }

    fun setOnRefreshListener(listener: OnRefreshListener) {
        this.refreshListener = listener
    }

    interface OnRefreshListener {
        fun onRefresh()
    }


}