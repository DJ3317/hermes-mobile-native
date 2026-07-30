package com.hermes.mobile.di

import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

/** Room 数据库模块 — 离线缓存 */
@Module
@InstallIn(SingletonComponent::class)
object DatabaseModule {
    // Room 数据库实现后可在此添加
}
