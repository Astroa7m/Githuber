package com.astroscoding.common.util.exceptions

class EmptyResponseBodyException(message: String="Connection was successful but there's no data received") : ReposException(message)