package com.kotlin.amritakumari.animationusingrxjava

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.view.animation.OvershootInterpolator
import io.reactivex.Completable
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity(), View.OnClickListener {

    enum class ANIM_STATE{
        EXPANDED, COLLAPSED
    }

    private var animState = ANIM_STATE.COLLAPSED

    private var offset: Float = 1f

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        fab1.setOnClickListener(this)
        fab2.setOnClickListener(this)
        fab3.setOnClickListener(this)
        fab4.setOnClickListener(this)

        offset = resources.getDimension(R.dimen.offset)
    }

    override fun onClick(view: View?) {
        when(animState){
            ANIM_STATE.COLLAPSED -> {
                startExpandAnimation().subscribe()
                animState = ANIM_STATE.EXPANDED
            }
            ANIM_STATE.EXPANDED -> {
                startCollapseAnimation().subscribe()
                animState = ANIM_STATE.COLLAPSED
            }
        }
    }

    private fun startExpandAnimation(): Completable {
        return Completable.mergeArray(fab2.slideIn(offset), fab3.slideIn(2 * offset), fab4.slideIn(3 * offset))
    }

    private fun startCollapseAnimation(): Completable {
        return Completable.mergeArray(fab2.slideOut(offset), fab3.slideOut(2 * offset), fab4.slideOut(3 * offset))
    }

    fun View.slideIn(offset : Float) : Completable{
        return Completable.create {
            visibility = View.VISIBLE
            scaleX = 0f
            scaleY = 0f
            translationY = -offset
            alpha = 0f
            animate().alpha(1f)
                    .translationY(0f)
                    .scaleX(1f)
                    .scaleY(1f)
                    .setDuration(500)
                    .setInterpolator(OvershootInterpolator())
                    .withEndAction { it :: onComplete }
        }
    }

    fun View.slideOut(offset : Float) : Completable{
        return Completable.create {
            animate().alpha(0f)
                    .translationY(-offset)
                    .scaleX(0f)
                    .scaleY(0f)
                    .setDuration(500)
                    .setInterpolator(OvershootInterpolator())
                    .withEndAction {
                        visibility = View.GONE
                        it :: onComplete }
        }
    }

}
