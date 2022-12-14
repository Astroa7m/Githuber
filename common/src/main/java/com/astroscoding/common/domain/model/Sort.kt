package com.astroscoding.common.domain.model

sealed class Sort(val sort: String){
    object Stars: Sort("stars")
    object Forks: Sort("forks")
    object Issues: Sort("issues")
    object EmptySort : Sort("told ya... empty")
}



