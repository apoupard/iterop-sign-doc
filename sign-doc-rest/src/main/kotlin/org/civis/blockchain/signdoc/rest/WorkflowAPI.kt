package org.civis.blockchain.signdoc.rest

import org.civis.blockchain.signdoc.rest.config.SignDocConfig
import org.civis.blockchain.signdoc.rest.service.SignService
import org.civis.blockchain.ssm.client.SsmClient
import org.civis.blockchain.ssm.client.Utils.JsonUtils
import org.civis.blockchain.ssm.client.domain.Context
import org.civis.blockchain.ssm.client.repository.InvokeReturn
import org.springframework.http.MediaType
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RestController
import java.io.ByteArrayInputStream
import java.io.InputStream
import java.util.concurrent.CompletableFuture

@RestController
class WorkflowAPI(val ssmClient: SsmClient,
                  val signDocConfig: SignDocConfig) {

    @PostMapping("/sessions/{session}/update", consumes = [MediaType.APPLICATION_OCTET_STREAM_VALUE])
    fun updateFile(@PathVariable session: String, @RequestBody value: String): CompletableFuture<InvokeReturn>? {
        val context = Context(session, value, getIteration(session));
        return ssmClient.perform(signDocConfig.userSigner(), "Update", context)
    }

    private fun getIteration(session: String): Int {
        val session = ssmClient.getSession(session).get();
        return session.iteration
    }

    @PostMapping("/sessions/{session}/validate", consumes = [MediaType.APPLICATION_OCTET_STREAM_VALUE])
    fun validate(@PathVariable session: String, @RequestBody value: String): CompletableFuture<InvokeReturn>? {
        val context = Context(session, value, getIteration(session));
        return ssmClient.perform(signDocConfig.userSigner(), "Validate", context)
    }

}