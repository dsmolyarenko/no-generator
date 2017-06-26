# Data Generator Tool

In the age of big data analysis and other buzzwords one of the hottest things is an estimation of production environment. One of the biggest issue that can be faced on a performance stage is an absence of real source data. Not only financial organization but many others are not wiling to provide even obfuscated production data to be used outside establishment. So the proper modeling of production data is an important part of development workflow.

NO provides a stream based DG framework which having a flexible configuration for any unconditioned data structures can produce more than half a billion characters per second. When the base extension provides an implementation to cover the vast majority of cases the modular SPI based architecture allows to easily integrate any specific additional module.

## Architecture

![DGT Architecture](doc/img1.png)

There are two functional phases to the architecture:

1. Configuration;
2. Streaming;

### Configuration
The root appender is being configured via YAML configuration file. See a simple SQL example below:

```yaml
main:
  type: join
  delimiter: "\n"
  number: 1000000
  sample:
    type: template
    template: insert into `User` (id, uid, fn, ln, age) values ({0}, "{1}", "{2}", "{3}", "{4}");
    samples:
      - type: seqInt
      - type: rndStr
      - fn
      - ln
      - age
generators:
  - id: fn
    type: rndSet
    samples:[ Alexander, Andreas, Benjamin, John ]
  - id: ln
    type: rndSet
    samples: [ Smith, Johnson, Williams, Jones, Brown ]
  - id: age
    type: rndInt
    shift: 21
    count: 20
sources:
```

The configuration should contain main appender which can be described inline or via references to others generators or number sources described in corresponding sections. See full description of appenders [here](doc/help.yaml).

### Streaming
The tool can be run as part of existing application or as a command line tool. See dev example below: 
```
java -jar generator-cli/target/generator-cli-1.0-SNAPSHOT-jar-with-dependencies.jar -c generator-cli/configuration.yaml -o /dev/stdout
```
That produces a sql based random data:
```
...
insert into `User` (id, uid, fn, ln, age) values (84208, "QU2aZWglTOsxdKOF", "John", "Jones", "48");
insert into `User` (id, uid, fn, ln, age) values (84209, "deHeKX60ZlzeQAQy", "John", "Brown", "49");
...
```
