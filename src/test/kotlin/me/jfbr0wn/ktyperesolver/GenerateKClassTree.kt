package me.jfbr0wn.ktyperesolver

import kotlin.reflect.typeOf

@ExperimentalStdlibApi
fun main() {
    val type = typeOf<Forest<Animal>>()
    val fields = type.resolveType()
    assert(fields.root.children!!.isNotEmpty())
}

class Animal(
    val id: String? = "Penguin",
    val name: String

)

class ParkGrouping<T, U, R>(
    val group: List<T>,
    val pets: Map<R, U>,
    val mammals: Array<Animal>
)

class Tree(
    val name:String
)

class Forest<T>(
    val trees: List<Tree>?,
    val inhabitants: List<T>? = emptyList()
)


abstract class Human(
    val id: String,
    val name: String
)

class Visitor<R>(
    val friends : List<R>,
    id: String,
    name: String
): Human(id, name)

class Ranger(
    val jobTitle: String,
    id: String,
    name: String
): Human(id, name)

data class Habitat(
    val name: String = "Earth",
    var x_cor: Int,
    val y_cor: Long? = 0,
    val animals: List<Animal>
)

data class Park<R : Human, T>(
    val inhabitants: List<T>,
    val visitors: List<R>,
    val name: String,
    val managers: List<Ranger>,
    var population: Int
) {
    var counter: Int = 0
}