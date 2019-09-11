package com.snc.farmaccount.extensions

import android.content.Context
import com.snc.farmaccount.helper.Config

val Context.config: Config get() = Config.newInstance(applicationContext)