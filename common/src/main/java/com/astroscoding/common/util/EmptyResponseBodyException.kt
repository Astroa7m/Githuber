package com.astroscoding.common.util

class EmptyResponseBodyException(message: String="Connection was successful but there's no data received") : ReposException(message)