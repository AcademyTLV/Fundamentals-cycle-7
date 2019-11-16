package com.android.academy.threads

import android.os.Bundle

import android.widget.Toast

import androidx.appcompat.app.AppCompatActivity

import com.android.academy.R

private const val FRAGMENT_TAG_THREAD = "fragment_tag_thread"

class ThreadsActivity : AppCompatActivity(), IAsyncTaskEvents {

    private lateinit var threadsFragment: CounterFragment
    private var simpleAsyncTask: MySimpleAsyncTask? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_threads)

        savedInstanceState?.let {
            threadsFragment =
                supportFragmentManager.findFragmentByTag(FRAGMENT_TAG_THREAD) as CounterFragment
        } ?: run {
            threadsFragment = CounterFragment()//Get Fragment Instance
            val data = Bundle()//Use bundle to pass data
            data.putString(
                CounterFragment.FRAGMENT_TYPE,
                getString(R.string.handler_exe_activity)
            )//put string, int, etc in bundle with a key value
            threadsFragment.arguments = data//Finally set argument bundle to fragment
            supportFragmentManager.beginTransaction()
                .replace(R.id.fragment, threadsFragment, FRAGMENT_TAG_THREAD)
                .commit()//now replace the argument fragment
        }
    }

    /***
     * // IAsyncTaskEvent's methods - start:
     */

    override fun createAsyncTask() {
        Toast.makeText(this, getString(R.string.msg_thread_oncreate), Toast.LENGTH_SHORT).show()
        simpleAsyncTask = MySimpleAsyncTask(this)

    }

    override fun startAsyncTask() {
        if (simpleAsyncTask == null || simpleAsyncTask!!.isCancelled) {
            Toast.makeText(this, R.string.msg_should_create_task, Toast.LENGTH_SHORT).show()
        } else {
            Toast.makeText(this, getString(R.string.msg_thread_onstart), Toast.LENGTH_SHORT).show()
            simpleAsyncTask!!.execute()
        }
    }

    override fun cancelAsyncTask() {
        simpleAsyncTask?.cancel() ?: run {
            Toast.makeText(this, R.string.msg_should_create_task, Toast.LENGTH_SHORT).show()
        }
    }

    override fun onPreExecute() {
        Toast.makeText(this, getString(R.string.msg_preexecute), Toast.LENGTH_SHORT).show()
    }

    override fun onPostExecute() {
        Toast.makeText(this, getString(R.string.msg_postexecute), Toast.LENGTH_SHORT).show()
        threadsFragment.updateFragmentText(getString(R.string.done))
        simpleAsyncTask = null
    }

    override fun onProgressUpdate(num: Int) {
        threadsFragment.updateFragmentText(num.toString())
    }

    override fun onCancel() {
        Toast.makeText(this, getString(R.string.msg_thread_oncancel), Toast.LENGTH_SHORT).show()
    }

    /***
     * //  IAsyncTaskEvent's methods - end
     */

    override fun onDestroy() {
        simpleAsyncTask?.let {
            it.cancel()
            simpleAsyncTask = null
        }

        super.onDestroy()
    }

}
