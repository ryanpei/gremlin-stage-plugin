import React from 'react';

import {
  ExecutionDetailsSection,
  ExecutionDetailsTasks,
  FormikFormField,
  FormikStageConfig,
  FormValidator,
  HelpContentsRegistry,
  HelpField,
  IExecutionDetailsSectionProps,
  IStage,
  IStageConfigProps,
  IStageTypeConfig,
  NumberInput,
  Validators,
  StageFailureMessage,
} from '@spinnaker/core';

import { GremlinStageConfig } from './GremlinStageConfig';

export function GremlinExecutionDetails(props: IExecutionDetailsSectionProps) {
  const { stage } = props; //stage.context

    return (
      <ExecutionDetailsSection name={props.name} current={props.current}>
        <StageFailureMessage stage={stage} message={stage.failureMessage} />
      </ExecutionDetailsSection>
  );
}

/*
  IStageConfigProps defines properties passed to all Spinnaker Stages.
  See IStageConfigProps.ts (https://github.com/spinnaker/deck/blob/master/app/scripts/modules/core/src/pipeline/config/stages/common/IStageConfigProps.ts) for a complete list of properties.
  Pass a JSON object to the `updateStageField` method to add the `maxWaitTime` to the Stage.

  This method returns JSX (https://reactjs.org/docs/introducing-jsx.html) that gets displayed in the Spinnaker UI.

function GremlinStageConfig(props: IStageConfigProps) {
  return (
    <div className="GremlinStageConfig">
      <FormikStageConfig
        {...props}
//        validate={validate}
        onChange={props.updateStage}
        render={(props) => (
          <FormikFormField
            name="maxWaitTime"
            label="Max Time To Wait"
            help={<HelpField id="armory.randomWaitStage.maxWaitTime" />}
            input={(props) => <NumberInput {...props} />}
          />
        )}
      />
    </div>
  );
}
*/
/*
  This is a contrived example of how to use an `initialize` function to hook into arbitrary Deck services. 
  This `initialize` function provides the help field text for the `RandomWaitStageConfig` stage form defined above.

  You can hook into any service exported by the `@spinnaker/core` NPM module, e.g.:
   - CloudProviderRegistry
   - DeploymentStrategyRegistry

  When you use a registry, you are diving into Deck's implementation to add functionality. 
  These registries and their methods may change without warning.
*/
export const initialize = () => {
//  HelpContentsRegistry.register('armory.gremlinStage.maxWaitTime', 'The maximum time, in seconds, that this stage should wait before continuing.');
};
/*
function validate(stageConfig: IStage) {
  const validator = new FormValidator(stageConfig);

  validator
    .field('gremlinApiKey')
    .required()
    .withValidators((value, label) => (value < 0 ? `${label} must be non-negative` : undefined));

  return validator.validateForm();
}
*/
export namespace GremlinExecutionDetails {
  export const title = 'gremlinRyan';
}

/*
  Define Spinnaker Stages with IStageTypeConfig.
  Required options: https://github.com/spinnaker/deck/master/app/scripts/modules/core/src/domain/IStageTypeConfig.ts
  - label -> The name of the Stage
  - description -> Long form that describes what the Stage actually does
  - key -> A unique name for the Stage in the UI; ties to Orca backend
  - component -> The rendered React component
  - validateFn -> A validation function for the stage config form.
 */
export const gremlinStage: IStageTypeConfig = {
  key: 'gremlinRyan',
  label: `Ryan - Gremlin`,
  description: 'Runs a chaos experiment using Gremlin',
  component: GremlinStageConfig, // stage config
  executionDetailsSections: [GremlinExecutionDetails, ExecutionDetailsTasks],
  strategy: true,
  validators: [
    { type: 'requiredField', fieldName: 'gremlinCommandTemplateId' },
    { type: 'requiredField', fieldName: 'gremlinTargetTemplateId' },
    { type: 'requiredField', fieldName: 'gremlinApiKey' },
  ],
};
