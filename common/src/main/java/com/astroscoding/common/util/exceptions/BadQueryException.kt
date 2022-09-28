package com.astroscoding.common.util.exceptions

class BadQueryException(message: String="Nothing matched your queries, Setting params to defaults") : ReposException(message)