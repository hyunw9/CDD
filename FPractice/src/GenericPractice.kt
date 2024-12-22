package src


class Box<T>(
    val content: T
) {
    fun getContent2(): T = content
}

fun <T> swap(pair: Pair<T, T>): Pair<T, T> {
    return Pair(pair.second, pair.first)
} // -> 타입을 무한으로 열어버리면 위함하다.

fun <T : Number> doubleValue(value: T): Double {
    return value.toDouble() * 2
} // -> Number를 상속받은 타입만 받겠다. 제네릭 앞쪽에 정의

fun <T> T?.toNotNull(defaultValue:T) = this ?: defaultValue
//확장함수 -> null이면 defaultValue를 반환

fun main() {
    println(swap(Pair(10,100)))
    println(doubleValue(3))
    println(doubleValue(3.3))
    val str : String? = null
    println(str.toNotNull("Default"))

    //List, Set, Map 모두 제네릭으로 구성되어 있다.
}

sealed class ApiResponse<out T> // <- abstract, out -> 반공변성, 공변성, 무공변성 등 출력받는 위치에서만 사용 가능
data class Success<out T>(val data:T): ApiResponse<T>()
data class Error(val message:String): ApiResponse<Nothing>()

//공변성은 제네릭 타입이 클래스 계층 구조에서 같은 방향으로 동작하도록 한다.
//공변성은 상위 모듈 -> 하위 모듈로 타입을 전달할 수 있다.
//공변성은 out 키워드로 표시한다.

//반공변성은 제네릭 타입이 클래스 계층 구조에서 반대 방향으로 동작하도록 한다.
//반공변성은 하위 모듈 -> 상위 모듈로 타입을 전달할 수 있다.
//반공변성은 in 키워드로 표시한다.

//무공변성은 제네릭 타입이 클래스 계층 구조에서 동작하지 않도록 한다.
//무공변성은 아무런 키워드 없이 표시한다.
//코틀린 제네릭은 기본적으로 무공변성이다.
