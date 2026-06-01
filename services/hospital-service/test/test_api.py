#!/usr/bin/env python3
"""
API Integration Tests for Hospital Service
Tests all endpoints and saves results to result.json
"""

import requests
import json
import sys
from datetime import datetime
from typing import Dict, Any, Optional

# Configuration
BASE_URL = "http://localhost:8084"
TIMEOUT = 10

# Test results storage
test_results = {
    "timestamp": int(datetime.now().timestamp() * 1000),
    "baseUrl": BASE_URL,
    "tests": {}
}

# Global variables to store created IDs
created_hospital_id = None
created_department_id = None


def log_test_result(test_name: str, endpoint: str, status: str, 
                   response_code: Optional[int] = None, 
                   error: Optional[str] = None,
                   **kwargs) -> None:
    """Log test result"""
    result = {
        "endpoint": endpoint,
        "status": status,
        "timestamp": int(datetime.now().timestamp() * 1000)
    }
    
    if response_code:
        result["responseCode"] = response_code
    
    if error:
        result["error"] = str(error)
    
    # Add any additional fields
    result.update(kwargs)
    
    test_results["tests"][test_name] = result
    print(f"[{status}] {test_name}: {endpoint}")


def test_find_all_hospitals():
    """Test GET /hospitals/ - Find all hospitals"""
    try:
        response = requests.get(f"{BASE_URL}/hospitals/", timeout=TIMEOUT)
        response.raise_for_status()
        
        data = response.json()
        result_count = len(data.get("result", [])) if data.get("result") else 0
        
        log_test_result(
            "findAllHospitals",
            "GET /hospitals/",
            "PASSED",
            response_code=response.status_code,
            resultCount=result_count,
            message="Successfully retrieved hospitals"
        )
        return True
    except Exception as e:
        log_test_result(
            "findAllHospitals",
            "GET /hospitals/",
            "FAILED",
            error=str(e)
        )
        return False


def test_create_hospital():
    """Test POST /hospitals/ - Create hospital"""
    global created_hospital_id
    
    try:
        payload = {
            "hospitalName": "Test Hospital",
            "hospitalAddress": "123 Test Street, Test City",
            "hospitalPhone": "0123456789",
            "hospitalEmail": "test@hospital.com"
        }
        
        response = requests.post(
            f"{BASE_URL}/hospitals/",
            json=payload,
            timeout=TIMEOUT
        )
        response.raise_for_status()
        
        data = response.json()
        hospital = data.get("result", {})
        created_hospital_id = hospital.get("hospitalId")
        
        log_test_result(
            "createHospital",
            "POST /hospitals/",
            "PASSED",
            response_code=response.status_code,
            createdHospitalId=created_hospital_id,
            hospitalName=hospital.get("hospitalName")
        )
        return True
    except Exception as e:
        log_test_result(
            "createHospital",
            "POST /hospitals/",
            "FAILED",
            error=str(e)
        )
        return False


def test_find_hospital_by_id():
    """Test GET /hospitals/{hospitalId} - Find hospital by ID"""
    global created_hospital_id
    
    if not created_hospital_id:
        log_test_result(
            "findHospitalById",
            "GET /hospitals/{hospitalId}",
            "SKIPPED",
            reason="No hospital ID available from previous test"
        )
        return False
    
    try:
        response = requests.get(
            f"{BASE_URL}/hospitals/{created_hospital_id}",
            timeout=TIMEOUT
        )
        response.raise_for_status()
        
        data = response.json()
        hospital = data.get("result", {})
        
        log_test_result(
            "findHospitalById",
            f"GET /hospitals/{created_hospital_id}",
            "PASSED",
            response_code=response.status_code,
            hospitalId=hospital.get("hospitalId"),
            hospitalName=hospital.get("hospitalName")
        )
        return True
    except Exception as e:
        log_test_result(
            "findHospitalById",
            f"GET /hospitals/{created_hospital_id}",
            "FAILED",
            error=str(e)
        )
        return False


def test_update_hospital():
    """Test PUT /hospitals/{hospitalId} - Update hospital"""
    global created_hospital_id
    
    if not created_hospital_id:
        log_test_result(
            "updateHospital",
            "PUT /hospitals/{hospitalId}",
            "SKIPPED",
            reason="No hospital ID available from previous test"
        )
        return False
    
    try:
        payload = {
            "hospitalName": "Updated Test Hospital",
            "hospitalAddress": "456 Updated Street, Updated City",
            "hospitalPhone": "0987654321",
            "hospitalEmail": "updated@hospital.com"
        }
        
        response = requests.put(
            f"{BASE_URL}/hospitals/{created_hospital_id}",
            json=payload,
            timeout=TIMEOUT
        )
        response.raise_for_status()
        
        data = response.json()
        hospital = data.get("result", {})
        
        log_test_result(
            "updateHospital",
            f"PUT /hospitals/{created_hospital_id}",
            "PASSED",
            response_code=response.status_code,
            updatedHospitalName=hospital.get("hospitalName")
        )
        return True
    except Exception as e:
        log_test_result(
            "updateHospital",
            f"PUT /hospitals/{created_hospital_id}",
            "FAILED",
            error=str(e)
        )
        return False


def test_find_all_departments():
    """Test GET /departments/ - Find all departments"""
    try:
        response = requests.get(f"{BASE_URL}/departments/", timeout=TIMEOUT)
        response.raise_for_status()
        
        data = response.json()
        result_count = len(data.get("result", [])) if data.get("result") else 0
        
        log_test_result(
            "findAllDepartments",
            "GET /departments/",
            "PASSED",
            response_code=response.status_code,
            resultCount=result_count,
            message="Successfully retrieved departments"
        )
        return True
    except Exception as e:
        log_test_result(
            "findAllDepartments",
            "GET /departments/",
            "FAILED",
            error=str(e)
        )
        return False


def test_create_department():
    """Test POST /departments/ - Create department"""
    global created_hospital_id, created_department_id
    
    if not created_hospital_id:
        log_test_result(
            "createDepartment",
            "POST /departments/",
            "SKIPPED",
            reason="No hospital ID available"
        )
        return False
    
    try:
        payload = {
            "departmentName": "Test Department",
            "departmentPhone": "0123456789",
            "departmentEmail": "test@department.com",
            "hospitalId": created_hospital_id
        }
        
        response = requests.post(
            f"{BASE_URL}/departments/",
            json=payload,
            timeout=TIMEOUT
        )
        response.raise_for_status()
        
        data = response.json()
        department = data.get("result", {})
        created_department_id = department.get("departmentId")
        
        log_test_result(
            "createDepartment",
            "POST /departments/",
            "PASSED",
            response_code=response.status_code,
            createdDepartmentId=created_department_id,
            departmentName=department.get("departmentName")
        )
        return True
    except Exception as e:
        log_test_result(
            "createDepartment",
            "POST /departments/",
            "FAILED",
            error=str(e)
        )
        return False


def test_find_department_by_id():
    """Test GET /departments/{departmentId} - Find department by ID"""
    global created_department_id
    
    if not created_department_id:
        log_test_result(
            "findDepartmentById",
            "GET /departments/{departmentId}",
            "SKIPPED",
            reason="No department ID available from previous test"
        )
        return False
    
    try:
        response = requests.get(
            f"{BASE_URL}/departments/{created_department_id}",
            timeout=TIMEOUT
        )
        response.raise_for_status()
        
        data = response.json()
        department = data.get("result", {})
        
        log_test_result(
            "findDepartmentById",
            f"GET /departments/{created_department_id}",
            "PASSED",
            response_code=response.status_code,
            departmentId=department.get("departmentId"),
            departmentName=department.get("departmentName")
        )
        return True
    except Exception as e:
        log_test_result(
            "findDepartmentById",
            f"GET /departments/{created_department_id}",
            "FAILED",
            error=str(e)
        )
        return False


def test_find_departments_by_hospital_id():
    """Test GET /departments/hospital/{hospitalId} - Find departments by hospital ID"""
    global created_hospital_id
    
    if not created_hospital_id:
        log_test_result(
            "findDepartmentsByHospitalId",
            "GET /departments/hospital/{hospitalId}",
            "SKIPPED",
            reason="No hospital ID available"
        )
        return False
    
    try:
        response = requests.get(
            f"{BASE_URL}/departments/hospital/{created_hospital_id}",
            timeout=TIMEOUT
        )
        response.raise_for_status()
        
        data = response.json()
        result_count = len(data.get("result", [])) if data.get("result") else 0
        
        log_test_result(
            "findDepartmentsByHospitalId",
            f"GET /departments/hospital/{created_hospital_id}",
            "PASSED",
            response_code=response.status_code,
            resultCount=result_count
        )
        return True
    except Exception as e:
        log_test_result(
            "findDepartmentsByHospitalId",
            f"GET /departments/hospital/{created_hospital_id}",
            "FAILED",
            error=str(e)
        )
        return False


def test_update_department():
    """Test PUT /departments/{departmentId} - Update department"""
    global created_department_id, created_hospital_id
    
    if not created_department_id or not created_hospital_id:
        log_test_result(
            "updateDepartment",
            "PUT /departments/{departmentId}",
            "SKIPPED",
            reason="No department or hospital ID available"
        )
        return False
    
    try:
        payload = {
            "departmentName": "Updated Test Department",
            "departmentPhone": "0987654321",
            "departmentEmail": "updated@department.com",
            "hospitalId": created_hospital_id
        }
        
        response = requests.put(
            f"{BASE_URL}/departments/{created_department_id}",
            json=payload,
            timeout=TIMEOUT
        )
        response.raise_for_status()
        
        data = response.json()
        department = data.get("result", {})
        
        log_test_result(
            "updateDepartment",
            f"PUT /departments/{created_department_id}",
            "PASSED",
            response_code=response.status_code,
            updatedDepartmentName=department.get("departmentName")
        )
        return True
    except Exception as e:
        log_test_result(
            "updateDepartment",
            f"PUT /departments/{created_department_id}",
            "FAILED",
            error=str(e)
        )
        return False


def test_delete_department():
    """Test DELETE /departments/{departmentId} - Delete department"""
    global created_department_id
    
    if not created_department_id:
        log_test_result(
            "deleteDepartment",
            "DELETE /departments/{departmentId}",
            "SKIPPED",
            reason="No department ID available"
        )
        return False
    
    try:
        response = requests.delete(
            f"{BASE_URL}/departments/{created_department_id}",
            timeout=TIMEOUT
        )
        response.raise_for_status()
        
        data = response.json()
        
        log_test_result(
            "deleteDepartment",
            f"DELETE /departments/{created_department_id}",
            "PASSED",
            response_code=response.status_code,
            message=data.get("message", "Department deleted successfully")
        )
        return True
    except Exception as e:
        log_test_result(
            "deleteDepartment",
            f"DELETE /departments/{created_department_id}",
            "FAILED",
            error=str(e)
        )
        return False


def test_delete_hospital():
    """Test DELETE /hospitals/{hospitalId} - Delete hospital"""
    global created_hospital_id
    
    if not created_hospital_id:
        log_test_result(
            "deleteHospital",
            "DELETE /hospitals/{hospitalId}",
            "SKIPPED",
            reason="No hospital ID available"
        )
        return False
    
    try:
        response = requests.delete(
            f"{BASE_URL}/hospitals/{created_hospital_id}",
            timeout=TIMEOUT
        )
        response.raise_for_status()
        
        data = response.json()
        
        log_test_result(
            "deleteHospital",
            f"DELETE /hospitals/{created_hospital_id}",
            "PASSED",
            response_code=response.status_code,
            message=data.get("message", "Hospital deleted successfully")
        )
        return True
    except Exception as e:
        log_test_result(
            "deleteHospital",
            f"DELETE /hospitals/{created_hospital_id}",
            "FAILED",
            error=str(e)
        )
        return False


def generate_summary():
    """Generate test summary"""
    tests = test_results["tests"]
    total = len(tests)
    passed = sum(1 for t in tests.values() if t.get("status") == "PASSED")
    failed = sum(1 for t in tests.values() if t.get("status") == "FAILED")
    skipped = sum(1 for t in tests.values() if t.get("status") == "SKIPPED")
    
    summary = {
        "total": total,
        "passed": passed,
        "failed": failed,
        "skipped": skipped,
        "successRate": round((passed / total * 100) if total > 0 else 0, 2)
    }
    
    test_results["summary"] = summary
    test_results["timestamp"] = int(datetime.now().timestamp() * 1000)
    
    return summary


def save_results(filename: str = "result.json"):
    """Save test results to JSON file"""
    try:
        with open(filename, "w", encoding="utf-8") as f:
            json.dump(test_results, f, indent=2, ensure_ascii=False)
        print(f"\n✓ Test results saved to {filename}")
        return True
    except Exception as e:
        print(f"\n✗ Error saving results: {e}")
        return False


def main():
    """Main test execution"""
    print("=" * 60)
    print("Hospital Service API Integration Tests")
    print("=" * 60)
    print(f"Base URL: {BASE_URL}\n")
    
    # Check if server is running
    try:
        response = requests.get(f"{BASE_URL}/hospitals/", timeout=5)
        print("✓ Server is running\n")
    except Exception as e:
        print(f"✗ Cannot connect to server at {BASE_URL}")
        print(f"  Error: {e}")
        print("\nPlease make sure the Hospital Service is running on port 8084")
        sys.exit(1)
    
    # Run tests in order
    print("Running tests...\n")
    
    # Hospital tests
    test_find_all_hospitals()
    test_create_hospital()
    test_find_hospital_by_id()
    test_update_hospital()
    
    # Department tests
    test_find_all_departments()
    test_create_department()
    test_find_department_by_id()
    test_find_departments_by_hospital_id()
    test_update_department()
    
    # Cleanup tests (delete in reverse order)
    test_delete_department()
    test_delete_hospital()
    
    # Generate summary
    summary = generate_summary()
    
    print("\n" + "=" * 60)
    print("Test Summary")
    print("=" * 60)
    print(f"Total:   {summary['total']}")
    print(f"Passed:  {summary['passed']}")
    print(f"Failed:  {summary['failed']}")
    print(f"Skipped: {summary['skipped']}")
    print(f"Success Rate: {summary['successRate']}%")
    print("=" * 60)
    
    # Save results
    save_results()
    
    # Exit with error code if any tests failed
    sys.exit(0 if summary['failed'] == 0 else 1)


if __name__ == "__main__":
    main()
