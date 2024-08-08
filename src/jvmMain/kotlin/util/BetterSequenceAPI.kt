package garden.ephemeral.clipninja.util

fun <T : Any> Sequence<T>.reject(predicate: (T) -> Boolean): Sequence<T> {
    return this.filter { thing ->
        !predicate(thing)
    }
}

fun <T> Sequence<T>.accept(predicate: (T) -> Boolean): Sequence<T> {
    return this.filter(predicate)
}

fun <K, V> Sequence<Map.Entry<K, V>>.toMap(): Map<K, V> {
    return this
        .map { (k: K, v: V) -> k to v }
        .toMap()
}
