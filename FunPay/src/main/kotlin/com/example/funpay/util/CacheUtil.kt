package com.example.funpay.util

class CacheUtil<K, V>(
    private val timeOutMillis: Long = 60_000L
) {
    private val cache = mutableMapOf<K, Pair<V, Long>>()

    fun getOrLoad(key: K, loader: () -> V): V {
        val now = System.currentTimeMillis()
        val cached: Pair<V, Long>? = cache[key]
        val cacheExpired: Boolean =
            cached?.let { now - it.second > timeOutMillis } ?: false
        //만약 데이터가 있다면, 지금 시간과 캐시된 데이터의 저장 시간의 차와 타임아웃 시간을 비교한다

        return when { //캐시 만료 시 로더를 실질적으로 소통 ( 통신 )
            cached == null || cacheExpired -> loader().also {
                newData -> cache[key] = newData to now
            }

            else -> cached.first
        }

        //기존 코드는 매번 할인률을 새로 계산했다. 이곳에 적용해볼 수 있음

        //장점
        //1. 성능 향상
        //- 자주 사용되는 값을 메모리에 캐시
        //- 불필요한 데이터 로드 방지
        //2. 재사용성
        //- 다양한 타입에 대해 캐시 적용 가능
        //- 스프링에 의존하지 않고 언제든 호출 가능
        //3. 유연성
        //- 캐시 만료 시간을 조절 가능
        //- 데이터를 가져오는 로직을 외부에서 주입받아 다양한 함수 활용 가능
        //4. 코드가독성
        //- 캐시 관련 로직이 한 곳에 모임
        //- 비즈니스 로직과 캐시처리가 명확히 구현
    }
}
