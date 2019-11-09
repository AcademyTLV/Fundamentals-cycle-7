package com.android.academy.Threads

import android.os.Bundle

import android.widget.Toast

import androidx.appcompat.app.AppCompatActivity

import com.android.academy.R

class AsyncTaskActivity : AppCompatActivity(), IAsyncTaskEvents {
    private lateinit var mThreadsFragment: CounterFragment
    private var mAsyncTask: CounterAsyncTask? = null

    companion object {
        const val FRAGMENT_TAG = "fragment_tag"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.asynctask_activity)

        savedInstanceState?.let {
            mThreadsFragment =
                supportFragmentManager.findFragmentByTag(FRAGMENT_TAG) as CounterFragment
        } ?: run {
            mThreadsFragment = CounterFragment()//Get Fragment Instance
            val data = Bundle()//Use bundle to pass data
            data.putString(
                CounterFragment.FRAGMENT_TYPE,
                getString(R.string.async_task_activity)
            )//put string, int, etc in bundle with a key value
            mThreadsFragment.arguments = data//Finally set argument bundle to fragment
            supportFragmentManager.beginTransaction()
                .replace(R.id.fragment, mThreadsFragment, FRAGMENT_TAG)
                .commit()//now replace the argument fragment
        }
    }

    /***
     * // IAsyncTaskEvent's methods - start:
     */
    override fun createAsyncTask() {
        Toast.makeText(this, getString(R.string.msg_oncreate), Toast.LENGTH_SHORT).show()
        mAsyncTask = CounterAsyncTask(this)
    }

    override fun startAsyncTask() {
        if (mAsyncTask == null || mAsyncTask!!.isCancelled) {
            Toast.makeText(this, R.string.msg_should_create_task, Toast.LENGTH_SHORT).show()
        } else {
            Toast.makeText(this, getString(R.string.msg_onstart), Toast.LENGTH_SHORT).show()
            mAsyncTask!!.execute(10)
        }
    }

    override fun cancelAsyncTask() {
        mAsyncTask?.cancel(true) ?: run {
            Toast.makeText(this, R.string.msg_should_create_task, Toast.LENGTH_SHORT).show()
        }
    }

    override fun onPreExecute() {
        Toast.makeText(this, getString(R.string.msg_preexecute), Toast.LENGTH_SHORT).show()
    }

    override fun onPostExecute() {
        Toast.makeText(this, getString(R.string.msg_postexecute), Toast.LENGTH_SHORT).show()
        mThreadsFragment.updateFragmentText(getString(R.string.done))
        mAsyncTask = null
    }

    override fun onProgressUpdate(num: Int) {
        mThreadsFragment.updateFragmentText(num.toString())
    }

    override fun onCancel() {
        Toast.makeText(this, getString(R.string.msg_oncancel), Toast.LENGTH_SHORT).show()
    }

    /***
     * //  IAsyncTaskEvent's methods - end
     */
    override fun onDestroy() {
        mAsyncTask?.let {
            it.cancel(false)
            mAsyncTask = null
        }
        super.onDestroy()
    }

}
