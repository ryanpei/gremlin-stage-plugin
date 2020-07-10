/*
 * Copyright 2020 Netflix, Inc.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package io.spinnaker.plugin.gremlin.orca

import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Headers
import retrofit2.http.POST
import retrofit2.http.Path
import retrofit2.http.Query

interface GremlinService {
    @POST("/attacks/new")
    @Headers(
            "Content-Type: application/json",
            "X-Gremlin-Agent: spinnaker/0.1.0"
    )
    fun create(
            @Header("Authorization") authHeader: String,
            @Body attackParameters: AttackParameters
    ): String

    @GET("/executions")
    @Headers(
            "X-Gremlin-Agent: spinnaker/0.1.0"
    )
    fun getStatus(
            @Header("Authorization") authHeader: String,
            @Query("taskId") attackGuid: String
    ): List<AttackStatus>

    @DELETE("/attacks/{attackGuid}")
    @Headers(
            "X-Gremlin-Agent: spinnaker/0.1.0"
    )
    fun haltAttack(
            @Header("Authorization") authHeader: String,
            @Path("attackGuid") attackGuid: String
    ): Void
}

data class AttackParameters(
        val command: Map<String, Any>,
        val target: Map<String, Any>
)

data class AttackStatus(
        val guid: String,
        val stage: String,
        val stageLifecycle: String,
        val endTime: String?,
        val output: String?
)