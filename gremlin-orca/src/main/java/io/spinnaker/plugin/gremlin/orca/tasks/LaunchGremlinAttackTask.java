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
package io.spinnaker.plugin.gremlin.orca.tasks;

import static io.spinnaker.plugin.gremlin.orca.GremlinStage.COMMAND_TEMPLATE_ID_KEY;
import static io.spinnaker.plugin.gremlin.orca.GremlinStage.GUID_KEY;
import static io.spinnaker.plugin.gremlin.orca.GremlinStage.TARGET_TEMPLATE_ID_KEY;

import com.netflix.spinnaker.orca.api.pipeline.RetryableTask;
import com.netflix.spinnaker.orca.api.pipeline.TaskResult;
import com.netflix.spinnaker.orca.api.pipeline.models.ExecutionStatus;
import com.netflix.spinnaker.orca.api.pipeline.models.StageExecution;
import io.spinnaker.plugin.gremlin.orca.GremlinService;
import io.spinnaker.plugin.gremlin.orca.GremlinStagePlugin;
import org.pf4j.Extension;

import java.util.HashMap;
import java.util.Map;
import javax.annotation.Nonnull;
import org.springframework.beans.factory.annotation.Autowired;

@Extension
public class LaunchGremlinAttackTask implements RetryableTask {
  private static final String GREMLIN_TEMPLATE_ID_KEY = "template_id";

  @Autowired private GremlinService gremlinService;

  @Nonnull
  @Override
  public TaskResult execute(@Nonnull StageExecution stage) {
    final Map<String, Object> ctx = stage.getContext();

    final String apiKey = GremlinStage.getApiKey(ctx);

    final String commandTemplateId = (String) ctx.get(COMMAND_TEMPLATE_ID_KEY);
    if (commandTemplateId == null || commandTemplateId.isEmpty()) {
      throw new RuntimeException("No command template provided");
    }

    final String targetTemplateId = (String) ctx.get(TARGET_TEMPLATE_ID_KEY);
    if (targetTemplateId == null || targetTemplateId.isEmpty()) {
      throw new RuntimeException("No target template provided");
    }

    final Map<String, Object> commandViaTemplate = new HashMap<>();
    commandViaTemplate.put(GREMLIN_TEMPLATE_ID_KEY, commandTemplateId);

    final Map<String, Object> targetViaTemplate = new HashMap<>();
    targetViaTemplate.put(GREMLIN_TEMPLATE_ID_KEY, targetTemplateId);

    final AttackParameters newAttack = new AttackParameters(commandViaTemplate, targetViaTemplate);

    final String createdGuid = gremlinService.create(apiKey, newAttack);
    final Map<String, Object> responseMap = new HashMap<>();
    responseMap.put(GUID_KEY, createdGuid);
    return TaskResult.builder(ExecutionStatus.SUCCEEDED).context(responseMap).build();
  }
}