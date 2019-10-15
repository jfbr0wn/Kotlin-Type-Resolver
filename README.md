# Kotlin Type Resolver
Uses Kotlin Reflection to take reified inputs, and dynamically resolve parameterized types at runtime.

## Example Usage
```val type = typeOf<Forest<Animal>>()
   val fields = type.resolveType()
   assert(fields.root.children!!.isNotEmpty())```
