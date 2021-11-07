package com.dhruv.randomdogs

import android.content.Context
import com.bumptech.glide.GlideBuilder
import com.bumptech.glide.annotation.GlideModule
import com.bumptech.glide.module.AppGlideModule

@GlideModule
class GlideApp: AppGlideModule() {

    override fun applyOptions(context: Context, builder: GlideBuilder) {
        super.applyOptions(context, builder)
    }

}