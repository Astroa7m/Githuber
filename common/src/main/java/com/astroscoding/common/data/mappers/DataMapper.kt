package com.astroscoding.common.data.mappers

internal interface DataMapper<ThisEntity, AnotherEntity> {
    fun mapTo(): AnotherEntity
}