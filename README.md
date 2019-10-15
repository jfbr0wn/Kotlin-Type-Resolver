# Kotlin Type Resolver
Uses Kotlin Reflection to and reified inputs to dynamically resolve parameterized types at runtime.

## Example Usage
```val type = typeOf<Forest<Animal>>()
    val fields = type.resolveType()
    assert(fields.root.children!!.isNotEmpty())```
