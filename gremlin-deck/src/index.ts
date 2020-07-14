import { IDeckPlugin } from '@spinnaker/core';
import { gremlinStage, initialize } from './GremlinStage';

export const plugin: IDeckPlugin = {
  initialize,
  stages: [gremlinStage],
};
