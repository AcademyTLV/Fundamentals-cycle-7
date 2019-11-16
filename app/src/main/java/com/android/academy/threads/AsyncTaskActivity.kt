package com.android.academy.threads

import android.os.Bundle

import android.widget.Toast

import androidx.appcompat.app.AppCompatActivity

import com.android.academy.R

class AsyncTaskActivity : AppCompatActivity(), IAsyncTaskEvents {
    private lateinit var threadsFragment: CounterFragment
    private var asyncTask: CounterAsyncTask? = null

    companion object {
        const val FRAGMENT_TAG = "fragment_tag"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.asynctask_activity)

        savedInstanceState?.let {
            threadsFragment =
                supportFragmentManager.findFragmentByTag(FRAGMENT_TAG) as CounterFragment
        } ?: run {
            threadsFragment = CounterFragment()//Get Fragment Instance
            val data = Bundle()//Use bundle to pass data
            data.putString(
                CounterFragment.FRAGMENT_TYPE,
                getString(R.string.async_task_activity)
            )//put string, int, etc in bundle with a key value
            threadsFragment.arguments = data//Finally set argument bundle to fragment
            supportFragmentManager.beginTransaction()
                .replace(R.id.fragment, threadsFragment, FRAGMENT_TAG)
                .commit()//now replace the argument fragment
        }
    }

    /***
     * // IAsyncTaskEvent's methods - start:
     */
    override fun createAsyncTask() {
        Toast.makeText(this, getString(R.string.msg_oncreate), Toast.LENGTH_SHORT).show()
        asyncTask = CounterAsyncTask(this)
    }

    override fun startAsyncTask() {
        if (asyncTask == null || asyncTask !!.isCancelled) {
            Toast.makeText(this, R.string.msg_should_create_task, Toast.LENGTH_SHORT).show()
        } else {
            Toast.makeText(this, getString(R.string.msg_onstart), Toast.LENGTH_SHORT).show()
            asyncTask!!.execute(10)
        }
    }

    override fun cancelAsyncTask() {
        asyncTask?.cancel(true) ?: run {
            Toast.makeText(this, R.string.msg_should_create_task, Toast.LENGTH_SHORT).show()
        }
    }

    override fun onPreExecute() {
        Toast.makeText(this, getString(R.string.msg_preexecute), Toast.LENGTH_SHORT).show()
    }

    override fun onPostExecute() {
        Toast.makeText(this, getString(R.string.msg_postexecute), Toast.LENGTH_SHORT).show()
        threadsFragment.updateFragmentText(getString(R.string.done))
        asyncTask = null
    }

    override fun onProgressUpdate(num: Int) {
        threadsFragment.updateFragmentText(num.toString())
    }

    override fun onCancel() {
        Toast.makeText(this, getString(R.string.msg_oncancel), Toast.LENGTH_SHORT).show()
    }

    /***
     * //  IAsyncTaskEvent's methods - end
     */
    override fun onDestroy() {
        asyncTask?.let {
            it.cancel(false)
            asyncTask = null
        }
        super.onDestroy()
    }

}
