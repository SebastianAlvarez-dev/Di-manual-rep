package com.example.repositoriomanualmars

import android.app.Application
import com.example.repositoriomanualmars.data.AppContainer
import com.example.repositoriomanualmars.data.DefaultAppContainer

class MarsApplication : Application() {
    val container: AppContainer = DefaultAppContainer()
}
