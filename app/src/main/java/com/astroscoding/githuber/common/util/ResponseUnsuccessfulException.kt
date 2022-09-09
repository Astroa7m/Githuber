package com.astroscoding.githuber.common.util

class ResponseUnsuccessfulException(override val message: String?=null) : Exception(message?:"Connecting to the network was unsuccessful")