# Sistema Funcional de Recomendaciones en Kotlin

## Descripción

Este proyecto implementa un sistema de recomendaciones personalizado utilizando programación funcional en Kotlin.

El sistema analiza información de usuarios, productos e interacciones para generar recomendaciones basadas en preferencias, historial de comportamiento, puntuaciones, similitud entre usuarios y reglas de negocio.

La solución utiliza principios de programación funcional como:

* Funciones puras.
* Inmutabilidad mediante `data class` con propiedades `val`.
* Funciones de orden superior.
* Composición de funciones.
* Transformaciones funcionales de colecciones.
* Tipos sellados.
* Uso de `map`, `filter`, `fold`, `groupBy`, `associate`, `Sequence`.
* Recursividad.
* Separación de efectos secundarios.

---

# Tecnologías utilizadas

* Kotlin
* Android Studio
* Jetpack Compose
* JUnit para pruebas unitarias
* Gradle

---

# Estructura del proyecto

```
com.example.sistemarecomendaciones

├── domain
│   ├── Models.kt
│
├── infrastructure
│   ├── DataGenerator.kt
│   └── PerformanceBenchMark.kt
│
├── validation
│   ├── AppResult.kt
│   └── Validation.kt
│
├── normalization
│   └── ProductNormalization.kt
│
├── scoring
│   ├── ScoringRules.kt
│   └── ProfileScoringRules.kt
│
├── profile
│   └── UserProfileGenerator.kt
│
├── similarity
│   └── ProfileScoringRules.kt
│   └── ScoringRules.kt
│
├── recommendation
│   ├── RecommendationEngine.kt
│   ├── AdvancedRecommendationEngine.kt
│   └── CategoryRecursion.kt
│
├── reporting
│   ├── RecommendationReporting.kt
│   └── FileReportGenerator.kt
│
└── test
    ├── ValidationTest.kt
    ├── ProductNormalizationTest.kt
    ├── ScoringTest.kt
    ├── ProfileTest.kt
    ├── RecommendationTest.kt
    └── ReportingTest.kt
```

---

# Funcionamiento del sistema

El flujo principal del sistema es:

```
Usuarios + Productos + Interacciones

          ↓

Validación funcional

          ↓

Normalización de productos

          ↓

Generación del perfil del usuario

          ↓

Aplicación de reglas de puntuación

          ↓

Cálculo de similitud

          ↓

Generación de recomendaciones

          ↓

Ordenamiento y filtrado

          ↓

Acumulación mediante fold

          ↓

Generación de reporte
```

---

# Modelado del dominio

El sistema utiliza modelos inmutables:

## Usuario

Representa las preferencias y categorías bloqueadas.

Incluye:

* Identificador.
* Nombre.
* Categorías favoritas.
* Categorías bloqueadas.

---

## Producto

Representa los elementos disponibles para recomendar.

Incluye:

* Nombre.
* Categoría.
* Precio.
* Calificación.
* Stock.
* Etiquetas.

---

## Interacción

Registra acciones realizadas por usuarios:

Tipos disponibles:

* VIEW.
* FAVORITE.
* PURCHASE.
* REMOVE_FROM_CART.

---

# Manejo funcional de resultados

El sistema implementa un tipo sellado:

```kotlin
sealed class AppResult<T>
```

Permite representar:

* Operaciones exitosas.
* Errores acumulados.

Incluye funciones:

* `map`
* `flatMap`
* `fold`
* `getOrElse`

Esto permite encadenar operaciones sin depender de múltiples condiciones.

---

# Validación funcional

Las validaciones utilizan reglas independientes:

```kotlin
typealias ValidationRule<T> = (T) -> List<String>
```

El sistema valida:

## Usuarios

* ID válido.
* Nombre obligatorio.
* Categorías preferidas.
* Conflictos entre categorías.

## Productos

* Precio válido.
* Rating correcto.
* Stock disponible.
* Categoría válida.

## Interacciones

* Usuario existente.
* Producto existente.
* Timestamp válido.
* Compatibilidad de interacción.

Los errores se acumulan sin detener la ejecución.

---

# Normalización

Los productos pasan por un pipeline funcional:

```kotlin
normalizeName
    then
normalizeCategory
    then
normalizeTags
    then
roundPrice
```

Transformaciones realizadas:

* Eliminación de espacios innecesarios.
* Conversión de nombres.
* Normalización de categorías.
* Limpieza de etiquetas.
* Redondeo de precios.

---

# Sistema de puntuación

Las recomendaciones se calculan mediante reglas:

* Coincidencia de categoría preferida.
* Penalización por categorías bloqueadas.
* Calificación del producto.
* Historial de compras.
* Productos vistos.
* Productos eliminados del carrito.
* Etiquetas relacionadas.
* Stock.
* Popularidad.
* Precio promedio.

Las reglas se almacenan en listas y se procesan mediante:

```kotlin
map()
fold()
```

---

# Perfil del usuario

El perfil se genera usando transformaciones funcionales:

Operaciones utilizadas:

* filter
* map
* flatMap
* groupingBy
* eachCount
* sortedByDescending
* take

El perfil contiene:

* Categorías favoritas.
* Etiquetas favoritas.
* Precio promedio.
* Productos vistos.
* Productos comprados.
* Frecuencia de interacciones.

---

# Generación de recomendaciones

El motor realiza:

1. Validación del usuario.
2. Normalización.
3. Eliminación de productos sin stock.
4. Exclusión de categorías bloqueadas.
5. Exclusión de productos comprados.
6. Cálculo de puntuación.
7. Eliminación de resultados negativos.
8. Ordenamiento.
9. Aplicación del límite solicitado.

---

# Reportes

El sistema genera reportes con:

* Productos analizados.
* Recomendaciones generadas.
* Promedio de puntuación.
* Categorías recomendadas.
* Productos rechazados.
* Razones de rechazo.
* Productos populares.

Los reportes pueden almacenarse en:

* TXT.
* JSON.
* CSV.

---

# Pruebas unitarias

El proyecto incluye más de 20 pruebas unitarias.

Se prueban:

* Validaciones.
* Acumulación de errores.
* Normalización.
* Composición.
* Reglas de puntuación.
* Perfiles.
* Recomendaciones.
* Exclusión de productos.
* Ordenamiento.
* Límites.
* Recursividad.
* Reportes.
* Acumuladores inmutables.

Ejecutar pruebas:

```
Run → Tests
```

o mediante Gradle:

```
./gradlew test
```

---

# Generación de datos

El sistema utiliza datos de prueba para simular:

* Usuarios.
* Productos.
* Interacciones.

Estos datos permiten evaluar el comportamiento del motor de recomendaciones.

---

# Comparación List vs Sequence

El procesamiento de recomendaciones utiliza `Sequence` para reducir colecciones intermedias.

Ejemplo:

```kotlin
products
    .asSequence()
    .filter()
    .map()
    .sortedByDescending()
    .take()
    .toList()
```

Se compara contra una implementación basada en `List` midiendo:

* Tiempo de ejecución.
* Uso de memoria.
* Cantidad de transformaciones.

---

# Ejecución del proyecto

## Requisitos

* Android Studio actualizado.
* JDK 11 o superior.
* Gradle configurado.

---

## Pasos

1. Clonar el repositorio.

```
git clone URL_DEL_REPOSITORIO
```

2. Abrir el proyecto en Android Studio.

3. Esperar la sincronización de Gradle.

4. Ejecutar la aplicación.

---

# Autores

Alumno:
Samuel Dominguez Lopez

Proyecto realizado para la materia:

**Programación Funcional**

Unidad 3: Lógica Funcional

Ingeniería en Desarrollo de Software

Año 2026
