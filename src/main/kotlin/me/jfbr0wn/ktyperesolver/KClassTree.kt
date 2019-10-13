package me.jfbr0wn.ktyperesolver

import kotlin.reflect.KClass
import kotlin.reflect.KProperty
import kotlin.reflect.KType
import kotlin.reflect.KTypeProjection

class KClassTree(
    val ktype: KType
) {
    val root: KClassNode

    init {
        root = resolveType(ktype)
    }

    /**
     * Parse the Object Graph, resolving Generics
     */
    private fun resolveType(kType: KType): KClassNode {
        val kClass = kType.resolveTypeClass()
        val resolvers = kType.arguments.mapNotNull { it -> it.type }

        // Map Generics to their Resolved Types. For Example List<Animal> will result in a mapping of { "E" : Animal }
        if(resolvers.size != kClass.typeParameters.size) throw MismatchedResolverException(kType, resolvers)
        val typeMap = kClass.typeParameters.associateBy(
            {it.name},
            {kType.arguments[kClass.typeParameters.indexOf(it)].type}
        )

        //Map over KClass attributes, resolving generics as needed
        val projections = kClass.members.filterIsInstance<KProperty<Any>>().map { kProperty ->
            val resolvers = kProperty.returnType.arguments.mapNotNull{projection ->

                // Try to find our resolved type, else try and work with what we have
                val type = typeMap.get(projection.type.toString()) ?: projection.type ?: throw UnrecognizedResolverTypeException()
                resolveType(type)
            }
            KPropertyNode(kProperty, kProperty.returnType.resolveTypeClass(), resolvers
            )
        }
        return KClassNode(kClass, projections)
    }

    /**
     * Extension function to resolve the class of a given KType via the [KTypeProjection.invariant]
     */
    private fun KType.resolveTypeClass(): KClass<*> {
        val invariant = KTypeProjection.invariant(this)
        return invariant.type?.let { it.classifier as KClass<*> } ?: throw UnrecognizedInvariantException(invariant)
    }
}

class KClassNode(
    val kClass: KClass<*>,
    val children: List<KPropertyNode>?
)

class KPropertyNode(
    val kProperty: KProperty<Any>,
    val type: KClass<*>,
    val children: List<KClassNode>
)

private fun UnrecognizedInvariantException(invariant: KTypeProjection) =
    Exception("Unable to Parse Type Tree for Invariant: ${invariant}")

private fun MismatchedResolverException(kType: KType, resolvers: List<KType>) =
    Exception("Resolved Parameter List Does Not Match Parameter List for Type:$kType \n Resolvers: $resolvers")

private fun UnrecognizedResolverTypeException() =
    Exception("Did not recognize resolver type, this could be because a Star Type was returned?")
