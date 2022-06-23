package app.spring.config

import org.springframework.beans.factory.config.ConfigurableListableBeanFactory
import org.springframework.beans.factory.support.BeanDefinitionRegistry
import org.springframework.beans.factory.support.BeanDefinitionRegistryPostProcessor
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty
import org.springframework.stereotype.Component

// must add properties `disable-bean` and setup as true
@ConditionalOnProperty(name = ["disable-bean"], havingValue = "true")
@Component
class DisableBeanConfig : BeanDefinitionRegistryPostProcessor {

    private val listOfDisableBeans = listOf<String>(
        // add name of disable beans here
    )

    override fun postProcessBeanFactory(beanFactory: ConfigurableListableBeanFactory) {

    }

    override fun postProcessBeanDefinitionRegistry(registry: BeanDefinitionRegistry) {
        registry.beanDefinitionNames.forEach {
            if (it in listOfDisableBeans) registry.removeBeanDefinition(it)
        }
    }


}