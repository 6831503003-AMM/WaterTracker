#!/usr/bin/env bash
# Compile all Java sources into the out/ directory.
set -e

ROOT="$(cd "$(dirname "$0")" && pwd)"
OUT="$ROOT/out"

mkdir -p "$OUT"

echo "🔧 Compiling sources..."
find "$ROOT/OODP" -name "*.java" | xargs javac -d "$OUT"
echo "✅ Compilation successful. Classes written to out/"
