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
package io.spinnaker.plugin.gremlin.orca.stage;

import com.netflix.spinnaker.orca.api.pipeline.CancellableStage;
import com.netflix.spinnaker.orca.api.pipeline.graph.StageDefinitionBuilder;
import com.netflix.spinnaker.orca.api.pipeline.graph.TaskNode;
import com.netflix.spinnaker.orca.api.pipeline.models.StageExecution;
import io.spinnaker.plugin.gremlin.orca.GremlinService;
import io.spinnaker.plugin.gremlin.orca.tasks.LaunchGremlinAttackTask;
import io.spinnaker.plugin.gremlin.orca.tasks.MonitorGremlinAttackTask;
import java.util.Map;
import java.util.Optional;
import javax.annotation.Nonnull;
import org.springframework.beans.factory.annotation.Autowired;
import org.pf4j.Extension;
import org.springframework.stereotype.Component;

@Component
@Extension
public class GremlinStage implements StageDefinitionBuilder, CancellableStage {
    public static final String APIKEY_KEY = "gremlinApiKey";
    public static final String COMMAND_TEMPLATE_ID_KEY = "gremlinCommandTemplateId";
    public static final String TARGET_TEMPLATE_ID_KEY = "gremlinTargetTemplateId";
    public static final String GUID_KEY = "gremlinAttackGuid";
    public static final String TERMINAL_KEY = "isGremlinTerminal";

    @Autowired private GremlinService gremlinService;

    public String getName() {
        return "gremlinRyan";
    }

    @Override
    public void taskGraph(@Nonnull StageExecution stage, @Nonnull TaskNode.Builder builder) {
        builder
                .withTask("launchGremlinAttack", LaunchGremlinAttackTask.class)
                .withTask("monitorGremlinAttack", MonitorGremlinAttackTask.class);
    }

    @Override
    public Result cancel(StageExecution stage) {
        final Map<String, Object> ctx = stage.getContext();
        final boolean isAttackCompleted =
                Optional.ofNullable(ctx.get(TERMINAL_KEY))
                        .map(
                                s -> {
                                    try {
                                        return Boolean.parseBoolean((String) s);
                                    } catch (final Exception ex) {
                                        return false;
                                    }
                                })
                        .orElse(false);

        if (!isAttackCompleted) {
            gremlinService.haltAttack(getApiKey(ctx), getAttackGuid(ctx));
            return new CancellableStage.Result(stage, ctx);
        }
        return null;
    }

    public static String getApiKey(final Map<String, Object> ctx) {
        final String apiKey = (String) ctx.get(APIKEY_KEY);
        if (apiKey == null || apiKey.isEmpty()) {
            throw new RuntimeException("No API Key provided");
        } else {
            return "Key " + apiKey;
        }
    }

    public static String getAttackGuid(final Map<String, Object> ctx) {
        final String guid = (String) ctx.get(GUID_KEY);
        if (guid == null || guid.isEmpty()) {
            throw new RuntimeException("Could not find an active Gremlin attack GUID");
        } else {
            return guid;
        }
    }
}