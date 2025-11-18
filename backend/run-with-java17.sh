#!/bin/bash
# Script para ejecutar Maven con Java 17
export JAVA_HOME=$(/usr/libexec/java_home -v 17)
echo "✅ Usando Java 17: $JAVA_HOME"
exec "$@"
