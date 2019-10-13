package me.jfbr0wn.ktyperesolver

import kotlin.reflect.KType


fun KType.resolveType() : KClassTree {
    return KClassTree(this)
}