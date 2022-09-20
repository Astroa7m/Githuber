package com.astroscoding.common.util

import com.astroscoding.common.domain.model.Sort

object Constants {
    const val BASE_URL = "https://api.github.com/search/"
    const val REPOSITORIES_ENDPOINT ="repositories"
    const val Q_PARAM = "q"
    const val SORT_PARAM = "sort"
    const val PAGE_PARAM = "page"
    const val PER_PAGE_PARAM = "per_page"
    const val DEFAULT_LANGUAGE = "kotlin"
    const val DEFAULT_QUERY = "language:$DEFAULT_LANGUAGE"
    val DEFAULT_SORT_TYPE = Sort.Stars
    const val DEFAULT_PAGE = 1
    const val DEFAULT_PER_PAGE = 30
    const val LOGGING_TAG = "API_DEBUG_MATE"
}