package app.spring.common

class AopOrder {

    companion object {
        const val apiAuth = 0

        const val apiRequestRateLimit = 1

        const val db = 2
    }
}