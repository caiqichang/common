package app.spring.config

import org.springframework.beans.factory.config.ConfigurableListableBeanFactory
import org.springframework.beans.factory.support.BeanDefinitionRegistry
import org.springframework.beans.factory.support.BeanDefinitionRegistryPostProcessor
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty
import org.springframework.stereotype.Component

@ConditionalOnProperty(name = ["disable-bean"], havingValue = "true")
@Component
class DisableBeanConfig : BeanDefinitionRegistryPostProcessor {

    private val listOfDisableBeans = listOf<String>()

    override fun postProcessBeanFactory(beanFactory: ConfigurableListableBeanFactory) {

    }

    override fun postProcessBeanDefinitionRegistry(registry: BeanDefinitionRegistry) {
        registry.beanDefinitionNames.forEach {
            if (listOfDisableBeans.contains(it)) registry.removeBeanDefinition(it)
        }
    }


}