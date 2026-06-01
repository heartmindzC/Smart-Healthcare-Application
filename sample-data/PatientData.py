import requests
import json
import random
from datetime import datetime

# 1. Cấu hình các Endpoint API (Thay đổi URL thực tế của bạn)
BASE_URL = "http://localhost:8085/api"
USER_API = f"{BASE_URL}/users/register"
PATIENT_API = f"{BASE_URL}/patients/"

# 2. Kho dữ liệu mẫu để tạo ngẫu nhiên
first_names = ["Nguyễn", "Trần", "Lê", "Phạm", "Phan", "Vũ", "Đặng", "Bùi"]
middle_names = ["Thị", "Văn", "Minh", "Hoàng", "Anh", "Hữu"]
last_names = ["Lan", "Hùng", "Dũng", "Hòa", "Linh", "Tuấn", "Phương", "Nam"]

blood_types = ["A", "B", "AB", "O"]
jobs = ["Kỹ sư", "Giáo viên", "Kinh doanh", "Sinh viên", "Công nhân", "Tự do"]

def generate_random_cccd():
    """Tạo số CCCD ngẫu nhiên đúng 12 số"""
    return "".join([str(random.randint(0, 9)) for _ in range(12)])

def generate_full_name():
    return f"{random.choice(first_names)} {random.choice(middle_names)} {random.choice(last_names)}"

def run_setup():
    headers = {'Content-Type': 'application/json'}
    
    print("--- BẮT ĐẦU TẠO 5 CẶP USER & PATIENT ---")

    for i in range(1, 6):
        cccd = generate_random_cccd()
        full_name = generate_full_name()
        email_name = full_name.lower().replace(" ", "")
        email = f"{email_name}{random.randint(10, 99)}@gmail.com"
        gender = random.choice(["MALE", "FEMALE"])
        birth_str = f"{random.randint(1980, 2010)}-{random.randint(1, 12):02d}-{random.randint(1, 28):02d}T00:00:00.000Z"

        # BƯỚC 1: PAYLOAD CHO USER
        user_payload = {
            "userId": cccd,
            "password": "password123",
            "phone": f"0{random.randint(300000000, 999999999)}",
            "email": email,
            "fullname": full_name,
            "address": f"Số {random.randint(1, 200)} Đường {random.randint(1, 50)}, TP. HCM",
            "birth": birth_str,
            "gender": gender,
            "roles": ["PATIENT"]
        }

        try:
            # GỌI API TẠO USER
            u_res = requests.post(USER_API, data=json.dumps(user_payload), headers=headers)
            
            if u_res.status_code in [200, 201]:
                print(f"\n[{i}] Đã tạo User thành công: {full_name} (CCCD: {cccd})")

                # BƯỚC 2: PAYLOAD CHO PATIENT (Lấy thông tin từ User vừa tạo)
                patient_payload = {
                    "userId": cccd,
                    "fullName": full_name,
                    "birth": birth_str,
                    "gender": gender,
                    "insuranceId": f"BH-{random.randint(10000000, 99999999)}",
                    "emergencyCallingNumber": f"0{random.randint(900000000, 999999999)}",
                    "job": random.choice(jobs),
                    "bloodType": random.choice(blood_types),
                    "heights": random.randint(150, 190),
                    "weights": random.randint(45, 90)
                }

                # GỌI API TẠO PATIENT
                p_res = requests.post(PATIENT_API, data=json.dumps(patient_payload), headers=headers)
                
                if p_res.status_code in [200, 201]:
                    print(f"    └── Đã tạo Patient thành công cho ID: {cccd}")
                else:
                    print(f"    └── Lỗi tạo Patient: {p_res.text}")
            else:
                print(f"[{i}] Lỗi tạo User: {u_res.text}")

        except Exception as e:
            print(f"[{i}] Lỗi kết nối: {e}")

    print("\n--- HOÀN TẤT QUY TRÌNH ---")

if __name__ == "__main__":
    run_setup()