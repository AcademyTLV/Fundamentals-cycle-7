package com.android.academy.Threads

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView

import androidx.fragment.app.Fragment

import com.android.academy.R


class CounterFragment : Fragment(), View.OnClickListener {
    private lateinit var mBtnCreate: Button
    private lateinit var mBtnStart: Button
    private lateinit var mBtnCancel: Button
    private lateinit var mTxtValue: TextView

    private var callbackListener: IAsyncTaskEvents? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        val rootView = inflater.inflate(R.layout.fragment_threads, container, false)

        mBtnCreate = rootView.findViewById(R.id.btnAsyncCreate)
        mBtnStart = rootView.findViewById(R.id.btnAsyncStart)
        mBtnCancel = rootView.findViewById(R.id.btnAsyncCancel)
        mTxtValue = rootView.findViewById(R.id.fullscreen_content)

        mBtnCreate.setOnClickListener(this)
        mBtnStart.setOnClickListener(this)
        mBtnCancel.setOnClickListener(this)

        //UNPACK OUR DATA FROM OUR BUNDLE
        val bundle = this.arguments
        if (bundle != null) {
            this.arguments?.getString(FRAGMENT_TYPE)?.let {
                mTxtValue.text = it
            }
        }
        return rootView
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        if (context is IAsyncTaskEvents) {
            callbackListener = context
        }

    }

    override fun onDetach() {
        super.onDetach()
        callbackListener = null
    }


    override fun onClick(v: View) {
        callbackListener?.let {
            if (isAdded ){
                when (v.id) {
                    R.id.btnAsyncCreate -> it.createAsyncTask()
                    R.id.btnAsyncStart -> it.startAsyncTask()
                    R.id.btnAsyncCancel -> it.cancelAsyncTask()
                }
            }
        }
    }

    fun updateFragmentText(text: String) {
            mTxtValue.text = text
    }

    companion object {

        val FRAGMENT_TYPE = "fragment_type"
    }
}
