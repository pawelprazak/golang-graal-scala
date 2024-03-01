# Golang to Scala in GraalVM interoperability experiment

This is a simple experiment to test the interoperability between Golang and Scala in GraalVM.

```bash
scala-cli --power package . \
    --suppress-experimental-feature-warning \
    --server=false \
    --jvm=graalvm-java17 \
    --graalvm-jvm-id=graalvm-java17:22 \
    -o lib/libscala.dylib -f
```

```bash
go run golang/main.go
```