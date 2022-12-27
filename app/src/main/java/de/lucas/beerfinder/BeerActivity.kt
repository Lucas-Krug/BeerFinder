package de.lucas.beerfinder

import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.runtime.SideEffect
import com.google.accompanist.systemuicontroller.rememberSystemUiController
import dagger.hilt.android.AndroidEntryPoint
import de.lucas.beerfinder.ui.Root
import de.lucas.beerfinder.ui.theme.BeerFinderTheme
import de.lucas.beerfinder.ui.theme.White

@AndroidEntryPoint
class BeerActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            val systemUiController = rememberSystemUiController()
            SideEffect {
                systemUiController.setStatusBarColor(White)
            }
            BeerFinderTheme {
                Root()
            }
        }
    }
}