#!/usr/bin/env bash
# Compile (if needed) then run the Water Tracker application.
set -e

ROOT="$(cd "$(dirname "$0")" && pwd)"
OUT="$ROOT/out"

mkdir -p "$OUT"

echo "🔧 Compiling sources..."
find "$ROOT/OODP" -name "*.java" | xargs javac -d "$OUT"
echo "✅ Compilation successful."
echo ""

echo "💧 Starting Smart Water Usage Tracking System..."
echo "──────────────────────────────────────────────"
java -cp "$OUT" OODP.Main
