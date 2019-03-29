package org.civis.blockchain.signdoc.rest.config

import org.civis.blockchain.ssm.client.SsmClient
import org.civis.blockchain.ssm.client.SsmClientConfig
import org.civis.blockchain.ssm.client.domain.Signer
import org.springframework.beans.factory.annotation.Value
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

@Configuration
class SsmConfig {

    @Value("\${coop.rest.url}")
    lateinit var coopRestUrl: String

    @Bean
    fun ssmClientConfig(): SsmClientConfig {
        return SsmClientConfig(coopRestUrl);
    }

    @Bean
    fun ssmClient(ssmClientConfig: SsmClientConfig): SsmClient {
        return SsmClient.fromConfig(ssmClientConfig)
    }
}