package src

//커링 함수는 fun 으로 선언이 불가능하다. val이 강제
//lambda의 lambda 를 반환

fun add(a: Int, b: Int) = a + b

val curriedAdd: (Int) -> (Int) -> Int = { a: Int -> { b: Int -> a + b } } // 람다 내부에 람다를 작성.

fun main() {
    println(curriedAdd) // 메모리 위치 출력
    println(curriedAdd(3)) // 여전히 2번째 함수 메모리 출력
    println(curriedAdd(3)(5)) // 정상적인 출력

    //함수를 잘게 쪼개서 사용할 수 있음 ( 재사용성, 유연성 )

    val addFiveFunction = curriedAdd(5)

    println(addFiveFunction(5)) // 필요시에 추가할 수 있다 ( Lazy Loading )

    log()
}

// 예제 1. 단위 변환기

val convert: (Double) -> (Double) -> Double = { rate -> { value -> rate * value } }

fun convertCurrency() {
    val usdToKrw = convert(1472.0)
    val eurToKrw = convert(1504.0)

    println(usdToKrw(10.0))
    println(eurToKrw(5.0))

}

// 예제 2. 로깅함수

val createLogger: (String) -> (String) -> Unit = { tag ->
    { message ->
        println("[$tag] $message")
    }
}

fun log(){
    val userLogger = createLogger("User")
    val systemLogger = createLogger("System")

    userLogger("로그인 시도")
    systemLogger("시스템 시작")
}

// --------------

// 1. 문법적 제약이 존재.

// 커링 함수는 fun 키워드로 작성이 불가능하다. val 만 가능하다.

// 2. 더 강력한 대안 기능이 존재한다.

//1. 기본 인자 사용
fun convertToKRW(value : Double, rate : Double = 1200.0) = value * rate

//2. 고차 함수 활용 (factor를 반적용한 함수를 활용한다 : (factor) -> (n) -> n * factor)
fun createMultiplier(factor : Int) = { n : Int -> n * factor }


// 실무에서는 사용하기 힘들다.

// 1. 커링 기능을 잘 활용하지 못할 가능성이 농후하다.
// -> 모두의 맥락에 맞춰서 사용해야 한다. 혼자 튀는건 조직 생산성에 악영향
// 2. 기본 인자로 대체 가능한 경우가 많다.
// 3. 과도하게 사용할 경우 코드의 가독성이 저하될 가능성이 있다. (모든 인자를 살펴봐야함)
