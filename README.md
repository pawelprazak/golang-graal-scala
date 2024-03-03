# Golang to Scala in GraalVM interoperability experiment

This is a simple experiment to test the interoperability between Golang and Scala in GraalVM.

```bash
scala-cli --power package . \
    --suppress-experimental-feature-warning \
    --server=false \
    --jvm=graalvm-java17 \
    --graalvm-jvm-id=graalvm-java17:22 \
    --graal \
    -o lib/libscala -f \
    -- \
    --no-fallback --shared -H:Name=libscala
```

```bash
go build -o main golang/main.go
```

```bash
mkdir out
cp main out
cp lib/libscala.dylib out
cd out
./main
```
