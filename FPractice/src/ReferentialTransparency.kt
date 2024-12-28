package src

//    동일한 입력에 대해서 항상 동일한 결과를 반환하는 성질

//     a -> fun() -> A 라는 함수가 있다면,
//     a -> fun() -> B 가 발생해서는 안된다. ( a가 같다면, 결과는 항상 같아야 한다. )

/*
** 1. 참조투명성의 특징
*  결과의 일관성 --> 동일한 입력에 대해 항상 동일한 응답.
*  함수 호출의 결과를 *미리 계산*(캐시) 하고 대체할 수 있다. --> (memoization)
*  순수 함수의 핵심 조건
*  외부의 상태에 영향을 받거나 영향을 주지 않는다.
*
 */

//참조 불투명한 코드

var counter = 0

fun incrementWithSideEffect(): Int {
    counter += 1 // 외부 값에 접근해서 활용
    return counter
} // : 무엇이 문제인가 ? 외부 상태에 의존하고 있다. 외부 상태에 영향을 준다.
/*
** 1. 함수가 외부 변수 counter 를 변경하므로, 호출 시 마다 결과가 달라진다.
* 2. 결과 예측이 불가능하기 때문에, 병렬 처리나 캐시가 어렵다.
* 3. 외부 상태에 따라 결과가 달라지므로, 디버깅과 테스트가 어렵다.
 */

fun main(){
    println(incrementWithSideEffect()) // -> 1
    println(incrementWithSideEffect()) // -> 2
}

