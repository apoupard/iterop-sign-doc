package org.civis.blockchain.signdoc.rest.service

import org.civis.blockchain.signdoc.rest.config.SignDocConfig
import org.civis.blockchain.ssm.client.SsmClient
import org.civis.blockchain.ssm.client.domain.Agent
import org.civis.blockchain.ssm.client.domain.Ssm
import org.civis.blockchain.ssm.client.repository.InvokeReturn
import org.springframework.stereotype.Service
import reactor.core.publisher.Mono

@Service
class InitService(val ssmClient: SsmClient,
                  val signDocConfig: SignDocConfig) {

    fun init(): Boolean {
        createUser()
        return createSsm()
    }

    private fun createSsm(): Boolean {
        if(!isSsmExist()) {
            initSsm();
            return true
        }
        return false
    }

    private fun isSsmExist() :Boolean {
        try {
            ssmClient.getSsm(signDocConfig.ssmName).get()
            return true;
        } catch (e: Exception) {
            return false;
        }
    }

    private fun createUser() {
       if(!getUsers().contains(signDocConfig.signerUserName)) {
           initUsers()
       }
    }

    private fun initSsm(): InvokeReturn {
        val admin = signDocConfig.adminSigner()

        val edit = Ssm.Transition(0, 0, "Signer", "Update")
        val validate = Ssm.Transition(0, 1, "Signer", "Validate")
        val ssm = Ssm(signDocConfig.ssmName, listOf(edit, validate))

        return ssmClient.create(admin, ssm).get()
    }

    private fun getUsers(): MutableList<String> {
        return ssmClient.listAgent().get()
    }

    private fun initUsers(): Mono<InvokeReturn> {
        val admin = signDocConfig.adminSigner()
        val agent = Agent.loadFromFile(signDocConfig.signerUserName, signDocConfig.signerUserFile);
        return Mono.fromFuture(ssmClient.registerUser(admin, agent))
    }
}