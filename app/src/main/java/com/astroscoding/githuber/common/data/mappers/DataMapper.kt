package com.astroscoding.githuber.common.data.mappers

interface DataMapper<ThisEntity, AnotherEntity> {
    fun mapTo(): AnotherEntity
}