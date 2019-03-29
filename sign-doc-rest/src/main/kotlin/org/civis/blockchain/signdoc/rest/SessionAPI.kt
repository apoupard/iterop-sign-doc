package org.civis.blockchain.signdoc.rest

import org.civis.blockchain.signdoc.rest.config.SignDocConfig
import org.civis.blockchain.signdoc.rest.service.InitService
import org.civis.blockchain.ssm.client.SsmClient
import org.civis.blockchain.ssm.client.domain.*
import org.civis.blockchain.ssm.client.repository.InvokeReturn
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RestController
import reactor.core.publisher.Mono

@RestController
class SessionAPI(val ssmClient: SsmClient,
                 val signDocConfig: SignDocConfig,
                 val initService: InitService) {

    @GetMapping("/ssm")
    fun getSsm(): Mono<Ssm> {
        return Mono.fromFuture(
                ssmClient.getSsm(signDocConfig.ssmName)
        )
    }

    @PostMapping("/ssm")
    fun init(): Boolean {
        return initService.init();
    }

    @GetMapping("/sessions")
    fun getAllSessions(): List<String> {
        return ssmClient.listSession().get().filter {
            val session = ssmClient.getSession(it).get()
            session.ssm.equals(signDocConfig.ssmName)
        }
    }

    @GetMapping("/sessions/{name}")
    fun getSession(@PathVariable name: String): Mono<Session> {
        return Mono.fromFuture(
                ssmClient.getSession(name)
        )
    }

    @PostMapping("/session/{name}")
    fun createSession(@PathVariable name: String): Mono<InvokeReturn> {
        val admin = signDocConfig.adminSigner()
        val roles = hashMapOf(signDocConfig.userSigner().name to "Signer")
        val session = Session(signDocConfig.ssmName, name, "", roles)

        return Mono.fromFuture(
                ssmClient.start(admin, session)
        )
    }


}