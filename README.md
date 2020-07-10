# winston-stage

**IMPORTANT**: This stage is Netflix-internal only. 
It will not compile unless you work at Netflix.
This repo is forked to open source as an example of using the `StageDefinitionBuilder` and `Task` extension points, as well as the `HttpClient` plugin SDK.

Winston is basically (but not really) an internal Netflix version of [StackStorm](https://stackstorm.com/).
Netflix engineers can subscribe to various triggers within the Netflix platform ecosystem and run arbitary code on-instance in response to these events.
Examples of such arbitrary code could be to restart processes, terminate an instance, cleanup logs, or whatever administrative tasks that may be necessary.


## config

Config of the `HttpClient` and other configs for the plugin itself are in `winston-orca/src/main/resources/winston.yml`.
These configs could also be added into the service YAML configuration as well (but we don't):

```yaml
spinnaker:
  extensibility:
    plugins:
      netflix.winston:
        enabled: true
        config:
          baseUrl: https://winstonservice.example/
          clientLoggingLevel: BODY
```