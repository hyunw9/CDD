package src

fun main() {
    //코틀린은 함수가 일급객체 취급을 받기 때문에, 함수의 반환을 함수형으로 설정할 수 있다.

    val calculator: (Int, Int) -> Int = getOperation("add")

    println(calculator(3, 4))


    val add : (Int, Int) -> Int = getOperation("add")
    val multiply : (Int, Int) -> Int = getOperation("multiply")

    println(chain(add,multiply)(4,5))
}

fun getAdder(x: Int): (Int) -> Int {
    return { y -> y + x }
}  // -> 반쪽짜리 함수 ( 반함수 )

fun getOperation(type: String): (Int, Int) -> (Int) { // Int,Int 를 받아서, Int를 반환하는 함수를 반환

    return when (type) {
        "add" -> { a, b -> a + b }
        "subtract" -> { a, b -> a - b }
        "multiply" -> { a, b -> a * b }
        "divide" -> { a, b ->
            if (b != 0) a / b
            else throw IllegalArgumentException("0으로 나눌 수 없습니다.")
        }

        else -> throw IllegalArgumentException("지원하지 않는 연산입니다.")
    }
}

fun chain(
    first: (Int, Int) -> Int,
    second: (Int, Int) -> Int
): (Int, Int) -> Int {
    return { x: Int, y: Int ->
        first(x, y) + second(x, y)
    }
} // -> 함수 체이닝
