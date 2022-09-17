package com.astroscoding.githuber.common.util

fun formQuery(queryString: String = "", language: String) : String{
    val languageQuery = if (language=="any") "" else "language:$language"
    return "$queryString+$languageQuery"
}

// github api returns partially irrelevant response
// when setting language to either c++ or c#
// so for data layer we store the language as the result of this function
fun languageSymbolToLanguageChar(language: String) = when (language){
    "c#" -> "csharp"
    "c++" -> "cpp"
    else -> language
}


// and for the presentation layer we use the result of this one
fun languageCharToLanguageSymbol(language: String) = when(language){
    "csharp" -> "c#"
    "cpp" -> "c++"
    else -> language
}
