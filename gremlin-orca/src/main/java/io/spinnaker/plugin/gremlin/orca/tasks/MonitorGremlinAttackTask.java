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

import static io.spinnaker.plugin.gremlin.orca.GremlinStage.TERMINAL_KEY;

import com.netflix.spinnaker.orca.api.pipeline.OverridableTimeoutRetryableTask;
import com.netflix.spinnaker.orca.api.pipeline.Task;
import com.netflix.spinnaker.orca.api.pipeline.TaskResult;
import com.netflix.spinnaker.orca.api.pipeline.models.ExecutionStatus;
import com.netflix.spinnaker.orca.api.pipeline.models.StageExecution;
import io.spinnaker.plugin.gremlin.orca.AttackStatus;
import io.spinnaker.plugin.gremlin.orca.GremlinService;
import io.spinnaker.plugin.gremlin.orca.GremlinStage;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;
import javax.annotation.Nonnull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import org.pf4j.Extension;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Extension
public class MonitorGremlinAttackTask implements OverridableTimeoutRetryableTask, Task {
  @Autowired private GremlinService gremlinService;

  private final Logger log = LoggerFactory.getLogger(getClass());

  @Nonnull
  @Override
  public TaskResult execute(@Nonnull StageExecution stage) {
    final Map<String, Object> ctx = stage.getContext();

    final String apiKey = GremlinStage.getApiKey(ctx);
    final String attackGuid = GremlinStage.getAttackGuid(ctx);

    final List<AttackStatus> statuses = gremlinService.getStatus(apiKey, attackGuid);

    boolean foundFailedAttack = false;
    String failureType = "";
    String failureOutput = "";

    for (final AttackStatus status : statuses) {
      if (status.getEndTime() == null) {
        return TaskResult.builder(ExecutionStatus.RUNNING).context(ctx).build();
      }
      if (isFailure(status.getStageLifecycle())) {
        foundFailedAttack = true;
        failureType = status.getStage();
        failureOutput = status.getOutput();
      }
    }
    ctx.put(TERMINAL_KEY, "true");
    if (foundFailedAttack) {
      throw new RuntimeException(
              "Gremlin run failed (" + failureType + ") with output : " + failureOutput);
    } else {
      return TaskResult.builder(ExecutionStatus.SUCCEEDED).context(ctx).build();
    }
  }

  @Override
  public long getBackoffPeriod() {
    return TimeUnit.SECONDS.toMillis(10);
  }

  @Override
  public long getTimeout() {
    return TimeUnit.MINUTES.toMillis(15);
  }

  private boolean isFailure(final String gremlinStageName) {
    return gremlinStageName.equals("Error");
  }
}
