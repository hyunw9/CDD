package src

//반환값 외에 외부 상태를 변경하거나 외부 상태에 의존하는 현상
// 순수성에 굉장히 심혈을 기울이는 것 같다.

/* 부수효과가 있는 함수 */
//var count = 0
//
//fun incrementAndPrint(value : Int) {
//    count += value // 외부 상태 변경
//    println("$count") //출력은 부수효과다.
//
//    if(count > 10)
//        throw IllegalArgumentException("Count is greater than 10") // 예외도 부수효과 .
//}

// 부수효과의 문제점 ?
/*
** 1. 테스트가 어려워진다.
* 외부 상태 따라 결과가 달라질 수 있다.
* 외부 상태를 초기화하고 관리하기 위해 mocking이 필요해진다.
* 2. 디버깅이 어려워진다.
* 동일한 입력에 다른 응답이 발생할 수 있기 때문에 문제 파악이 어려워진다.
* 3. 병렬 처리에 어렵다.
* 공유된 외부 자원을 활용시 동시성 이슈 발생 가능성
* 4. 함수 합성이 어렵다.
* 부수효과가 있는 함수들을 합성하는 경우 각자 함수의 부수효과 발생으로 테스트나 디버깅이 어렵다.
* 순수함수는 합성하기 적합하다 ( 부수효과가 없다 -> output 예측이 정확하고 신뢰도가 높음 )
 */

/**
 * 예외도 부수효과인가?
 * -> 정상적인 함수의 흐름을 방해한다. 코드가 진행되다, 예외처리 발생 시 코드의 흐름이 바뀌게 된다.
 * 또한, 숨겨진 제어 흐름을 가지고 있다.
 */

//해결책
fun incrementAndCheck(value : Int, currentCount : Int): Result<Int>{
    val newCount = currentCount + value

    //Optional<?> 와 같이, 내부에 상태값을 저장하고 예외를 도중에 터뜨리지 않는다.
    return if( newCount > 10){
        Result.failure(IllegalStateException("Count is greater than 10"))
    } else{
        Result.success(newCount)
    }

//    외부 상태를 변경하지 않고, 반환값에 모든 정보를 포함, 한번에 상태값 검증을 진행하는 것이 부수효과를 줄이는 방법 중 하나다.
}
