package com.hermes.mobile.di

import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

/** 全局单例模块 — Application 级别依赖 */
@Module
@InstallIn(SingletonComponent::class)
object AppModule {
    // 全局单例可在此添加
}
