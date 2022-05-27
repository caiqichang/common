package app.spring.config.data

class ApiAuthRules {

    // add authorization methods here

    fun hasRoles(vararg str: String): Boolean {
        println(str.joinToString())
        return true
    }

}