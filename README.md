
Nice way to get tests to run every time you save.
```
fswatch src | while read; do mvn test;done
```