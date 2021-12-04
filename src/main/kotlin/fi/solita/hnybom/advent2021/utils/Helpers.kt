package fi.solita.hnybom.advent2021.utils

object Helpers {

    fun <T> transpose(target: List<List<T>>) : List<List<T>> {
        val result = mutableListOf<MutableList<T>>()
        target.forEach { list ->
            list.forEachIndexed { index, value ->
                if (result.size <= index) {
                    result.add(mutableListOf())
                }
                result[index] = result.getOrElse(index) { mutableListOf() }.apply { add(value) }
            }
        }
        return result
    }

}