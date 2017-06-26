#### Base Configuration

* **file** - produces numbers basing on file bytes:
```yaml
main:
  type: rndInt
  source:
    type: file
    path: /dev/random
    bl: 16
    bs: 8192
```

* **random** - produces random numbers using standard Java algorithm:
```yaml
main:
  type: rndInt
  source:
    type: random
    secure: true
```

* **well** - produces random numbers using well algorithm:
```yaml
main:
  type: rndInt
  source:
    type: well
    # The type of well pool. Possible values:
    #  -  Well1024a 
    #  -  Well19937a
    #  -  Well19937c
    #  -  Well44497a
    #  -  Well44497b
    #  -  Well512a
    # Default: Well1024a  
    pool: Well512a
```
