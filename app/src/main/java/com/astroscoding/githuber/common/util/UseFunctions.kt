package com.astroscoding.githuber.common.util

fun formQuery(queryString: String = "", language: String) =
    "$queryString+language:$language"
