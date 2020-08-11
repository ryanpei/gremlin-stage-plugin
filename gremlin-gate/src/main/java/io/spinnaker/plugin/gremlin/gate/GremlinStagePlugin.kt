/*
 * Copyright 2020 Netflix, Inc.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package io.spinnaker.plugin.gremlin.gate;

import org.slf4j.LoggerFactory
import com.netflix.spinnaker.kork.plugins.api.spring.SpringLoaderPlugin
import org.pf4j.PluginWrapper

class GremlinStagePlugin(wrapper: PluginWrapper) : SpringLoaderPlugin(wrapper) {

  private val logger = LoggerFactory.getLogger(GremlinStagePlugin::class.java)

  override fun getPackagesToScan(): List<String> {
    return listOf(
            "io.spinnaker.plugin.gremlin.gate"
    )
  }

  override fun start() {
    logger.info("GremlinStagePlugin.start()")
  }

  override fun stop() {
    logger.info("GremlinStagePlugin.stop()")
  }

}
