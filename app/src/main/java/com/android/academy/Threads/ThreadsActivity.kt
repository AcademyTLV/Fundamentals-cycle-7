package com.android.academy.Threads

import android.os.Bundle

import android.widget.Toast

import androidx.appcompat.app.AppCompatActivity

import com.android.academy.R

private val FRAGMENT_TAG_THREAD = "fragment_tag_thread"

class ThreadsActivity : AppCompatActivity(), IAsyncTaskEvents {

    private lateinit var mThreadsFragment: CounterFragment
    private var mSimpleAsyncTask: MySimpleAsyncTask? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_threads)

        savedInstanceState?.let {
            mThreadsFragment =
                supportFragmentManager.findFragmentByTag(FRAGMENT_TAG_THREAD) as CounterFragment
        } ?: run {
            mThreadsFragment = CounterFragment()//Get Fragment Instance
            val data = Bundle()//Use bundle to pass data
            data.putString(
                CounterFragment.FRAGMENT_TYPE,
                getString(R.string.handler_exe_activity)
            )//put string, int, etc in bundle with a key value
            mThreadsFragment.arguments = data//Finally set argument bundle to fragment
            supportFragmentManager.beginTransaction()
                .replace(R.id.fragment, mThreadsFragment, FRAGMENT_TAG_THREAD)
                .commit()//now replace the argument fragment
        }
    }

    /***
     * // IAsyncTaskEvent's methods - start:
     */

    override fun createAsyncTask() {
        Toast.makeText(this, getString(R.string.msg_thread_oncreate), Toast.LENGTH_SHORT).show()
        mSimpleAsyncTask = MySimpleAsyncTask(this)

    }

    override fun startAsyncTask() {
        if (mSimpleAsyncTask == null || mSimpleAsyncTask!!.isCancelled) {
            Toast.makeText(this, R.string.msg_should_create_task, Toast.LENGTH_SHORT).show()
        } else {
            Toast.makeText(this, getString(R.string.msg_thread_onstart), Toast.LENGTH_SHORT).show()
            mSimpleAsyncTask!!.execute()
        }
    }

    override fun cancelAsyncTask() {
        mSimpleAsyncTask?.cancel() ?: run {
            Toast.makeText(this, R.string.msg_should_create_task, Toast.LENGTH_SHORT).show()
        }
    }

    override fun onPreExecute() {
        Toast.makeText(this, getString(R.string.msg_preexecute), Toast.LENGTH_SHORT).show()
    }

    override fun onPostExecute() {
        Toast.makeText(this, getString(R.string.msg_postexecute), Toast.LENGTH_SHORT).show()
        mThreadsFragment.updateFragmentText(getString(R.string.done))
        mSimpleAsyncTask = null
    }

    override fun onProgressUpdate(num: Int) {
        mThreadsFragment.updateFragmentText(num.toString())
    }

    override fun onCancel() {
        Toast.makeText(this, getString(R.string.msg_thread_oncancel), Toast.LENGTH_SHORT).show()
    }

    /***
     * //  IAsyncTaskEvent's methods - end
     */

    override fun onDestroy() {
        mSimpleAsyncTask?.let {
            it.cancel()
            mSimpleAsyncTask = null
        }

        super.onDestroy()
    }

}
