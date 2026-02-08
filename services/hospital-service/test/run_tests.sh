#!/bin/bash

# Script to run API integration tests using Python

echo "=========================================="
echo "Running Hospital Service API Tests"
echo "=========================================="

# Navigate to project root
cd "$(dirname "$0")/.."

# Check if Python is available
if ! command -v python3 &> /dev/null; then
    echo "Error: python3 is not installed"
    exit 1
fi

# Check if requests library is installed
if ! python3 -c "import requests" 2>/dev/null; then
    echo "Installing required dependencies..."
    pip3 install -r test/requirements.txt
fi

# Run tests
echo ""
python3 test/test_api.py

# Check if result.json was created
if [ -f "result.json" ]; then
    echo ""
    echo "Full results available in: $(pwd)/result.json"
else
    echo "Warning: result.json was not created. Check test output for errors."
    exit 1
fi
