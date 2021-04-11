[app](../../index.md) / [com.example.chatmemo.ui.utils.transition](../index.md) / [PlayTransition](./index.md)

# PlayTransition

`class PlayTransition : Transition`

### Constructors

| Name | Summary |
|---|---|
| [&lt;init&gt;](-init-.md) | `PlayTransition(fabColor: `[`Int`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-int/index.html)`)`<br>`PlayTransition(context: `[`Context`](https://developer.android.com/reference/android/content/Context.html)`, attrs: `[`AttributeSet`](https://developer.android.com/reference/android/util/AttributeSet.html)`?)` |

### Properties

| Name | Summary |
|---|---|
| [color](color.md) | `var color: `[`Int`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-int/index.html) |

### Functions

| Name | Summary |
|---|---|
| [captureEndValues](capture-end-values.md) | `fun captureEndValues(transitionValues: TransitionValues): `[`Unit`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-unit/index.html) |
| [captureStartValues](capture-start-values.md) | `fun captureStartValues(transitionValues: TransitionValues): `[`Unit`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-unit/index.html) |
| [createAnimator](create-animator.md) | `fun createAnimator(sceneRoot: `[`ViewGroup`](https://developer.android.com/reference/android/view/ViewGroup.html)`, startValues: TransitionValues?, endValues: TransitionValues?): `[`Animator`](https://developer.android.com/reference/android/animation/Animator.html)`?` |
| [getTransitionProperties](get-transition-properties.md) | `fun getTransitionProperties(): `[`Array`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-array/index.html)`<`[`String`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html)`>?` |

### Companion Object Functions

| Name | Summary |
|---|---|
| [calculateMaxRadius](calculate-max-radius.md) | `fun calculateMaxRadius(view: `[`View`](https://developer.android.com/reference/android/view/View.html)`): `[`Float`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-float/index.html) |
| [calculateMinRadius](calculate-min-radius.md) | `fun calculateMinRadius(view: `[`View`](https://developer.android.com/reference/android/view/View.html)`): `[`Int`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-int/index.html) |
