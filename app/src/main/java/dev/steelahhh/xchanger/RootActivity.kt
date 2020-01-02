package dev.steelahhh.xchanger

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import dev.steelahhh.rates.RatesListFragment

class RootActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        setTheme(R.style.AppTheme)
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_root)

        if (savedInstanceState == null)
            supportFragmentManager.beginTransaction()
                .replace(R.id.container, RatesListFragment.newInstance())
                .commit()
    }
}
