package com.example.musicapp.tool.extensionfunction

fun <E> ArrayList<E>.addListAndRemoveDuplicate(target:List<E>?):ArrayList<E>{
              val result = ArrayList<E>()
              result.addAll(this)
              target?.let { addTarget ->result.addAll(addTarget)}
              return ArrayList(result.distinct());
}

fun <E> ArrayList<E>.addListAndRemoveDuplicate(target:List<E>?,limitCount:Int):ArrayList<E>{
    val result = ArrayList<E>()
    result.addAll(this)
    target?.let { addTarget ->
        if(addTarget.size > limitCount) result.addAll(addTarget.subList(0,limitCount-1))
        else  result.addAll(addTarget)
    }
    return ArrayList(result.distinct());
}


