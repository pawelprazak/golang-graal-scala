# Golang to Scala in GraalVM interoperability experiment

This is a simple experiment to test the interoperability between Golang and Scala in GraalVM.

## Prerequisites

- [Go](https://golang.org/doc/install)
- [Scala CLI](https://scala-cli.virtuslab.org/install)

The JVM and GraalVM are not required to be installed, as the Scala CLI will download and use the GraalVM JVM.

## Build and Run

```bash
scala-cli --power package . \
    --suppress-experimental-feature-warning \
    --server=false \
    --jvm=graalvm-java17 \
    --graalvm-jvm-id=graalvm-java17:22 \
    --native-image \
    -o lib/libscala -f \
    -- \
    --no-fallback --shared
```

```bash
go build -o bin/main golang/main.go
```

```bash
mkdir bin
cp lib/libscala.dylib bin
```

```bash
./bin/main
```

## Background

We want to reuse the existing implementations of some Scala libraries in Golang code.

We use GraalVM Native Image to compile the Scala code to a shared native library binary and the Golang/CGO code 
to an executable binary that calls the shared library binary.

### GraalVM Native Image and Polyglot Programming

GraalVM Native Image is a technology to ahead-of-time compile JVM bytecode to a native binary 
(shared library or an executable). It supports polyglot programming by allowing to include code 
from different languages in the same binary (using LLVM, WASM or Truffle) or to be linked from different binaries.

We use [`scala-cli`](https://scala-cli.virtuslab.org/) with 
[`--native-image`](https://scala-cli.virtuslab.org/docs/reference/cli-options#--native-image) 
and [`--shared`](https://www.graalvm.org/latest/reference-manual/native-image/guides/build-native-shared-library/) 
to compile the Scala code to a shared (dynamically linked) native library binary. As of the time of writing, 
GraalVM Native Image [does not support statically linked](https://github.com/oracle/graal/issues/3053) 
native library binaries.

#### Exposing Scala methods as native functions

To export any JVM method to be called from a native binary, we use the `org.graalvm.nativeimage.c.function.CEntryPoint`
from a compile time dependency on [GraalVM SDK](https://github.com/oracle/graal/blob/master/sdk/README.md).
Since the method is called from a native binary, it must be `static` and we use `@scala.annotation.static`
on a companion object method to achieve that.

### Golang and CGO

Golang has a powerful feature called CGO that allows calling C functions and C libraries from Golang code.

To use [CGO](https://go.dev/wiki/cgo) we import a pseudo-package `"C"` and use the so-called preamble with C 
and [CGO directives](https://go.dev/src/cmd/cgo/doc.go) to tell 
the compiler how to link the C shared library binary with the Golang binary:
- `CFLAGS` with `I` option to specify the compile time include path for the C header files
- `LDFLAGS` with `L` option to specify the compile time library search path for the C shared library binary; 
  `l` option to specify the library name for the C shared library binary;
  and the combination of `-Wl`, `-rpath` and [`$ORIGIN`](https://amir.rachum.com/shared-libraries/#origin) 
  to specify the runtime shared library path *relative to the executable* for the C shared library binary.
- `#include` directives to include the C header files for the C shared library binary at compile time.

### Interoperability

We use the `C` pseudo-package and the Native Image C API to call the Scala shared library binary from the Golang code.

### Distribution

We distribute the Golang binary and the Scala shared library binary together in the same directory.
The binary is linked to the shared library binary at runtime using the relative runtime shared library path.
