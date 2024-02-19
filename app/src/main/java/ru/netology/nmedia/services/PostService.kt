package ru.netology.nmedia.services

object PostService {
    fun ConvertCountToShortString(count: Int): String {
        val result = when(count) {
            in 0..999 -> count.toString()
            in 1_000..1_099 -> "${count.toString()[0]}К"
            in 1_100..9_999 -> "${count.toString()[0]}.${count.toString()[1]}К"
            in 10_000..999_999 -> "${count.toString().substring(0, (count / 1_000).toString().length)}К"
            in 1_000_000..1_099_999 -> "1М"
            in 1_100_000..Int.MAX_VALUE -> {
                val length = (count / 1_000_000).toString().length
                "${count.toString().substring(0, length)}.${count.toString()[length]}М"
            }
            else -> count.toString()
        }
        return result
    }
}