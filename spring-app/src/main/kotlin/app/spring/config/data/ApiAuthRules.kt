package app.spring.config.data

class ApiAuthRules {

    // add authorization methods here

    fun hasRole(vararg roles: String): Boolean {
        return true
    }

    fun hasAnyRole(vararg roles: String): Boolean {
        return true
    }

    fun hasPermission(vararg permissions: String): Boolean {
        return true
    }

    fun hasAnyPermission(vararg permissions: String): Boolean {
        return true
    }
}