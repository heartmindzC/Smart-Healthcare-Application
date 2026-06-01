import requests
import json
from datetime import datetime

# --- CẤU HÌNH ENDPOINTS ---
BASE_URL = "http://localhost:8085/api" 

HOSPITAL_API = f"{BASE_URL}/hospitals/"
DEPARTMENT_API = f"{BASE_URL}/departments/"
DOCTOR_API = f"{BASE_URL}/doctors/"
USER_API = f"{BASE_URL}/users/register"
PATIENT_API = f"{BASE_URL}/patients/"

# Danh sách lưu trữ dữ liệu để in báo cáo
doctor_report = []
patient_report = []

# --- DỮ LIỆU DANH MỤC GÁN CỨNG ---
hospitals_data = [
    {"hospitalName": "Bệnh viện Bạch Mai", "hospitalAddress": "78 Giải Phóng, Hà Nội", "hospitalPhone": "02438693731", "hospitalEmail": "bachmai@gmail.com"},
    {"hospitalName": "Bệnh viện Chợ Rẫy", "hospitalAddress": "201B Nguyễn Chí Thanh, TP.HCM", "hospitalPhone": "02838554137", "hospitalEmail": "choray@bvchoray.vn"},
    {"hospitalName": "Bệnh viện Vinmec", "hospitalAddress": "458 Minh Khai, Hà Nội", "hospitalPhone": "02439743556", "hospitalEmail": "info@vinmec.com"},
    {"hospitalName": "Bệnh viện TW Huế", "hospitalAddress": "16 Lê Lợi, Huế", "hospitalPhone": "02343822325", "hospitalEmail": "bvtwhue@vnn.vn"},
    {"hospitalName": "Bệnh viện Từ Dũ", "hospitalAddress": "284 Cống Quỳnh, TP.HCM", "hospitalPhone": "19007237", "hospitalEmail": "tudu@tudu.com.vn"}
]

dept_names = ["Khoa Nội", "Khoa Ngoại", "Khoa Nhi", "Khoa Sản", "Khoa Tim Mạch", "Khoa Tai Mũi Họng"]
first_names = ["Nguyễn", "Trần", "Lê", "Phạm", "Hoàng", "Võ", "Phan", "Đặng"]
middle_names = ["Văn", "Thị", "Minh", "Anh", "Hữu", "Quang"]
last_names = ["An", "Bình", "Chương", "Dương", "Hòa", "Linh", "Minh", "Nam", "Sơn", "Tùng"]
blood_types = ["A", "B", "AB", "O"]
jobs = ["Kỹ sư", "Giáo viên", "Kinh doanh", "Sinh viên", "Công nhân", "Tự do"]

counter = 0

def get_id_from_res(response):
    data = response.json()
    return data.get("result") if "result" in data else data

def generate_sequential_cccd(index):
    return str(index).zfill(12)

def generate_sequential_name(index):
    f = first_names[index % len(first_names)]
    m = middle_names[index % len(middle_names)]
    l = last_names[index % len(last_names)]
    return f"{f} {m} {l}"

def print_table(title, data):
    print(f"\n{title}")
    print("-" * 115)
    header = f"| {'Fullname':<25} | {'Email':<30} | {'SĐT':<15} | {'Password':<15} | {'Role':<10} |"
    print(header)
    print("-" * 115)
    for item in data:
        row = f"| {item['fullname']:<25} | {item['email']:<30} | {item['phone']:<15} | {item['password']:<15} | {item['role']:<10} |"
        print(row)
    print("-" * 115)

def run_setup():
    global counter
    headers = {'Content-Type': 'application/json'}
    
    print("=== BẮT ĐẦU THIẾT LẬP HỆ THỐNG DỮ LIỆU TỔNG HỢP (GÁN CỨNG) ===")

    # PHẦN 1: TẠO HỆ THỐNG Y TẾ
    print("\n--- PHẦN 1: KHỞI TẠO BỆNH VIỆN VÀ BÁC SĨ ---")
    for h_idx, h_info in enumerate(hospitals_data):
        try:
            h_res = requests.post(HOSPITAL_API, data=json.dumps(h_info), headers=headers)
            if h_res.status_code not in [200, 201, 409]: continue # 409 nếu đã tồn tại
            
            h_data = get_id_from_res(h_res)
            h_id = h_data.get("hospitalId") if h_data else "EXISTS"

            for d_idx in range(3):
                d_name = dept_names[d_idx]
                d_payload = {
                    "departmentName": d_name,
                    "departmentPhone": f"{h_info['hospitalPhone']}-EXT-{d_idx}",
                    "departmentEmail": f"{d_name.lower().replace(' ', '')}{d_idx}@{h_info['hospitalEmail'].split('@')[1]}",
                    "hospitalId": h_id
                }
                
                d_res = requests.post(DEPARTMENT_API, data=json.dumps(d_payload), headers=headers)
                if d_res.status_code not in [200, 201, 409]: continue
                
                d_data = get_id_from_res(d_res)
                d_id = d_data.get("departmentId") if d_data else "EXISTS"

                for doc_idx in range(2):
                    counter += 1
                    doc_cccd = generate_sequential_cccd(counter + 1000)
                    doc_name = generate_sequential_name(counter)
                    doc_pass = "password123"
                    doc_phone = f"0912000{counter:03d}"
                    doc_email = f"doctor{counter}@hospital.com"
                    
                    doc_payload = {
                        "userId": doc_cccd, "password": doc_pass, "phone": doc_phone,
                        "email": doc_email, "fullName": doc_name,
                        "address": f"Số {counter} Phố Y Tế, Hà Nội", "birth": "1980-01-01T00:00:00.000Z",
                        "gender": "MALE" if counter % 2 == 0 else "FEMALE",
                        "hospitalId": h_id, "department": d_id, "licenseId": f"BS-{counter:06d}"
                    }
                    doc_res = requests.post(DOCTOR_API, data=json.dumps(doc_payload), headers=headers)
                    
                    # Lưu vào báo cáo dù thành công hay đã tồn tại (để xem lại thông tin)
                    doctor_report.append({
                        "fullname": doc_name, "email": doc_email, "phone": doc_phone,
                        "password": doc_pass, "role": "DOCTOR"
                    })

        except Exception as e:
            print(f"!!! Lỗi xử lý {h_info['hospitalName']}: {e}")

    # PHẦN 2: TẠO 5 CẶP USER & PATIENT
    print("\n--- PHẦN 2: KHỞI TẠO 5 CẶP USER & PATIENT CỐ ĐỊNH ---")
    for i in range(1, 6):
        cccd = generate_sequential_cccd(i)
        full_name = generate_sequential_name(i + 50)
        u_email = f"patient{i}@gmail.com"
        u_pass = "password123"
        u_phone = f"038800000{i}"

        user_payload = {
            "userId": cccd, "password": u_pass, "phone": u_phone,
            "email": u_email, "fullname": full_name,
            "address": f"Số {i} Đường Bệnh Nhân, TP. HCM",
            "birth": "1990-01-01T00:00:00.000Z", "gender": "FEMALE" if i % 2 == 0 else "MALE",
            "roles": ["PATIENT"]
        }

        try:
            u_res = requests.post(USER_API, data=json.dumps(user_payload), headers=headers)
            if u_res.status_code in [200, 201, 409]:
                patient_payload = {
                    "userId": cccd, "fullName": full_name, "birth": "1990-01-01T00:00:00.000Z",
                    "gender": "FEMALE" if i % 2 == 0 else "MALE", "insuranceId": f"BH-{i:08d}",
                    "emergencyCallingNumber": f"091100000{i}", "job": jobs[i % len(jobs)],
                    "bloodType": blood_types[i % len(blood_types)], "heights": 160 + i, "weights": 50 + i
                }
                requests.post(PATIENT_API, data=json.dumps(patient_payload), headers=headers)
                
                # Lưu vào báo cáo
                patient_report.append({
                    "fullname": full_name, "email": u_email, "phone": u_phone,
                    "password": u_pass, "role": "PATIENT"
                })
        except Exception as e:
            print(f"[{i}] Lỗi kết nối: {e}")

    print("\n=== HOÀN TẤT TOÀN BỘ QUY TRÌNH SETUP ===")
    
    # IN BÁO CÁO
    print_table("BẢNG BÁO CÁO DANH SÁCH BÁC SĨ (DOCTORS)", doctor_report)
    print_table("BẢNG BÁO CÁO DANH SÁCH BỆNH NHÂN (PATIENTS)", patient_report)

if __name__ == "__main__":
    run_setup()