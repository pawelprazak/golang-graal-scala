package main

import (
	"fmt"
	"os"
)

/*
#cgo CFLAGS: -I${SRCDIR}/../lib
#cgo LDFLAGS: -L${SRCDIR}/../lib
#cgo LDFLAGS: -Wl,-rpath,"$ORIGIN"
#cgo LDFLAGS: -lscala
#include <stdlib.h>
#include <errno.h>
#include "libscala.h"
*/
import "C"

func main() {
	var isolate *C.graal_isolate_t
	var thread *C.graal_isolatethread_t

	if C.graal_create_isolate(nil, &isolate, &thread) != 0 {
		_, _ = fmt.Fprintf(os.Stderr, "initialization error\n")
		os.Exit(1)
	}
	var i = C.a(thread)
	fmt.Printf("Number of entries: %d\n", i)

	C.graal_tear_down_isolate(thread)
	fmt.Println("Go exit")
}
