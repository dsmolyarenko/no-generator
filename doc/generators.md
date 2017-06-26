#### Base Configuration

* **join** - joins sample a specified number of times using prefix, delimiter and suffix. See additional properties:
```yaml
main:
  type: join
  # The number of samples.
  # Default: 16
  number: 1000
  # A string sample to preppend the samples sequence.
  # Default: null
  prefix: '['
  # A string sample to finalize the samples sequence.
  # Default: null
  suffix: ']'
  # A string sample to use as delimited of the sample sequence.
  # Default: null
  delimiter: ', '
  # The sample reference or inplace definition.
  # Mandatory
  sample: CurrentDate
```
* **loop** - Sequently select a sample from the sample sequence. Starts from begin if the sequence is exhausted.
```yaml
main:
  type: loop
  # The samples references or inplace definitions.
  # Mandatory
  samples:
    - type: text
      text: a
    - type: text
      text: b
    - type: text
      text: c
```
* **text** - Append static text.
```yaml
main:
  type: text
  # The text sample
  # Mandatory
  text: >
    The long long chapter of the text.
```
* **rndAny** - Append random sample from the sample sequence.
```yaml
main:
  type: rndAny
  samples:
    - type: text
      text: a
    - type: text
      text: b
```
* **rndInt** - Append random integer.
```yaml
main:
  type: rndInt
  shift: 0
  count: 32768
  # Default: default
  source:
    type: well
```
* **rndNum** - Append random number. See [decimalFormat](https://docs.oracle.com/javase/tutorial/i18n/format/decimalFormat.html) for the formatting rules.
```yaml
main:
  type: rndNum
  # Default: 0.0
  shift: 0.0
  # Default: 1.0
  scale: 2.0
  # Default: null
  format: '#.##'
  # Default: null
  locale: en_US
  # Default: default
  source:
    type: well
```
* **rndSet** - Append random text sample from specified text samples sequence.
```yaml
main:
  type: rndSet
  # Mandatory
  samples: [ a, b, c ]
  # Default: default
  source:
    type: well
```