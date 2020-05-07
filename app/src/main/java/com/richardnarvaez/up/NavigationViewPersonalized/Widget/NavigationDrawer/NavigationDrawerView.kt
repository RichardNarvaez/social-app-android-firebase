package com.richardnarvaez.up.NavigationViewPersonalized.Widget.NavigationDrawer

import android.content.Context
import android.graphics.Color
import android.os.Parcel
import android.os.Parcelable
import android.support.design.widget.NavigationView
import android.support.v4.view.AbsSavedState
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.util.AttributeSet
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import com.richardnarvaez.up.R
import com.richardnarvaez.up.NavigationViewPersonalized.Utils.Extensions.delay
import com.richardnarvaez.up.NavigationViewPersonalized.Utils.Extensions.unSafeLazy

/**
 * Created by Richard Narvaez on 30/12/2017.
 */

class NavigationDrawerView : NavigationView, ItemClickListener {

    private var itemList = mutableListOf(
            NavigationItem(NavigationId.SHOT, R.drawable.vector_home,
                    isSelected = true, itemIconColor = R.color.colorPrimary),
            NavigationItem(NavigationId.TOP_PHOTO, R.drawable.influencer,
                    itemIconColor = R.color.colorPrimaryDark),
//            NavigationItem(NavigationId.TOP_CHALLENGE, R.drawable.vector_find,
//                    itemIconColor = R.color.colorPrimary),
//            NavigationItem(NavigationId.USER_LIKES, R.drawable.vector_find,
//                    itemIconColor = R.color.colorPrimary),
            NavigationItem(NavigationId.FOLLOWING, R.drawable.vector_heart,
                    itemIconColor = R.color.green_d),
            NavigationItem(NavigationId.ABOUT, R.drawable.vector_info,
                    itemIconColor = R.color.blue_d),
            NavigationItem(NavigationId.LOG_OUT, R.drawable.vector_log_out,
                    itemIconColor = R.color.red_d))

    private var currentSelectedItem: Int = 0
    private val adapter by unSafeLazy {
        NavigationViewAdapter(itemList, this)
    }
    private val recyclerView by unSafeLazy {
        RecyclerView(context).apply {
            layoutManager = LinearLayoutManager(context)
        }
    }

    var navigationItemSelectListener: NavigationItemSelectedListener? = null
    val header: View = getHeaderView(0)

    constructor(context: Context) : super(context)

    constructor(context: Context, attrs: AttributeSet) : super(context, attrs)

    init {
        setBackgroundColor(Color.TRANSPARENT)
        val layoutParams = FrameLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.MATCH_PARENT)
        layoutParams.topMargin = context.resources.getDimension(R.dimen.nav_header_height).toInt()
        recyclerView.layoutParams = layoutParams
        recyclerView.adapter = adapter

        recyclerView.overScrollMode = View.OVER_SCROLL_NEVER
        addView(recyclerView)
    }

    override fun onSaveInstanceState(): Parcelable {
        val superState = super.onSaveInstanceState()
        val state = State(superState)
        state.currentPosition = currentSelectedItem
        return state
    }

    override fun onRestoreInstanceState(savedState: Parcelable?) {
        if (savedState !is State) {
            super.onRestoreInstanceState(savedState)
            return
        }
        super.onRestoreInstanceState(savedState.superState)
        this setCurrentSelected savedState.currentPosition
    }

    override fun invoke(item: NavigationItem, position: Int) {
        this setCurrentSelected position
        navigationItemSelectListener?.onNavigationItemSelected(item)
    }

    private infix fun setCurrentSelected(position: Int) {
        itemList[currentSelectedItem].isSelected = false
        currentSelectedItem = position
        itemList[currentSelectedItem].isSelected = true
    }

    fun setChecked(position: Int) {
        setCurrentSelected(position)
        //FIXME
        delay(250) {
            adapter.notifyDataSetChanged()
        }
    }

    private class State : AbsSavedState {
        var currentPosition: Int = 0

        private constructor(parcel: Parcel) : super(parcel) {
            currentPosition = parcel.readInt()
        }

        constructor(parcelable: Parcelable) : super(parcelable)

        override fun writeToParcel(dest: Parcel?, flags: Int) {
            super.writeToParcel(dest, flags)
            dest?.writeInt(currentPosition)
        }

        companion object CREATOR : Parcelable.Creator<State> {
            override fun createFromParcel(parcel: Parcel): State {
                return State(parcel)
            }

            override fun newArray(size: Int): Array<State?> {
                return arrayOfNulls(size)
            }
        }
    }
}