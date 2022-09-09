package com.astroscoding.githuber.common.domain.model

sealed class Sort(val sort: String){
    object Stars: Sort("stars")
    object Forks: Sort("forks")
    object Issues: Sort("issues")
}
