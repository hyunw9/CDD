package src

// 참조 투명하며 부수효과가 없는 함수

// 실무에서 순수 함수로 코드 작성이 가능할까 ? -> 겠냐?

// 지키기 어려운 사례 1 : 외부 데이터 의존
//    fun fetchUserData(userId:Int) : String {
//        return database.query("SELECT name FROM users WHERE id = $userId")
//    }

// 지키기 어려운 사례 2 : 부수 효과가 필요한 작업 ( 로그 출력, 파일 저장, 화면에 값 출력 )
//    fun logMessage(message:String){
//        println("Log : $message")
//    }

//    그럼 실무에서는 어떻게 이끌어낼 수 있을까 ?
//    실무에서는 순수함수와 부수효과를 처리하는 코드를 분리하고 관리하는 방식을 사용하자.

//    ASIS : 부수효과가 있는 함수
//    fun orderCoffee(paymentCard: PaymentCard) : Coffee {
//        val coffee = Coffee()
//        paymentCard.charge(coffee.price)
//        return coffee
//    } 어떻게 부수효과를 제거하지 ?

// -> 부수효과를 별도의 데이터로 담아보자. ( 부수효과를 처리하는 코드를 분리하자. )

// TOBE : 부수효과 제거된 함수
// -------------------------------------------------------------------------------------------

data class PaymentCard(
    val number: String,
    val name: String
)

data class Coffee(
    val price: Int
)

data class Transaction(
    val paymentCard: PaymentCard,
    val amount: Int
)

data class Order(
    val coffee: Coffee,
    val transaction: Transaction
)

fun orderCoffee(paymentCard: PaymentCard): Order {
    val coffee = Coffee(3000)
    val transaction = Transaction(paymentCard, coffee.price)
    return Order(coffee, transaction)
}

// 부수 효과 처리기
class SideEffectHandler {
    //결제 처리 (외부 API 호출)
    fun processPayment(transaction: Transaction): Result<Boolean> {
        return kotlin.runCatching {
            //실제로는 여기서 외부 결제 API 호출이 발생 .
            println("[외부 API 호출] 카드번호: ${transaction.paymentCard.number}")
            println("[외부 API 호출] 결제금액: ${transaction.amount}")
            true
        }
    }
}

fun main() {
    val myCard = PaymentCard("1234-5678", "홍길동")

    //순수함수로 주문 생성
    val order = orderCoffee(myCard)

    //부수 효과 처리
    val sideEffectHandler = SideEffectHandler()
    sideEffectHandler.processPayment(order.transaction)
        .onSuccess { success ->
            if (success) println("결제가 완료되었습니다.")
            else println("결제가 실패했습니다.")
        }
        .onFailure {
            println("결제 처리 중 오류 발생: ${it.message}")
        }
}

/* - 왜 이렇게 까지 해야하냐?
 * 1. 실제 결제 처리(부수효과)를 분리한다.
 * 2. 주문 정보와 결제 정보를 명시적으로 표현한다.
 * 3. 테스트가 용이하다 : 실제 카드 결제 없이 로직의 검증이 가능하다.
 *  - DB 연동이 부수효과로 있는 코드도 부수효과 부분 분리 시 DB 없이 테스트 가능
 * 4. 트랜잭션 정보를 모아서 처리하는 등 원하는 방식으로 제어가 가능하다.
 * 여기선 orderCoffee 순수함수가 너무 단순하기 때문에 장점이 잘 안느껴지지만, 코드베이스가 방대해지면 큰 이점으로 작용 가능
 */

/** - 부수효과의 제어
 * 1. Result<T> : 예외 처리를 위한 컨테이너
 * 2. runCatching : 예외 처리를 위한 확장 함수
 * 3. onSuccess : 성공 시 처리
 * 4. onFailure : 실패 시 처리
 */

// Result 와 같은 컨테이너를 활용해서 부수효과를 제어할 수 있다.

fun divide(a: Int, b: Int): Result<Int> {
    return if (b == 0) Result.failure(IllegalArgumentException("응 안돼"))
    else Result.success(a / b)
}

fun resultInvoker() {
    val result = divide(19, 0)
    result.onSuccess { println("Result : $it") }
        .onFailure { println("Error : $it.message") }
} //예외를 제어하여 정상적인 함수 흐름을 타게 만들고 응답을 준다. ( 어찌됐든 모두 실행되긴 한다 )

/* 실무 적용시 고려사항
* 부수효과가 필요한 작업은 공통화해서 안전하게 처리하고, 핵심 로직은 순수함수로 작성.
* 모든 부수효과를 분리하는것은 불가능하다.
* 따라서 핵심 비즈니스 로직의 순수성을 우선적을 확보하는 것이 중요하다. !
 */



