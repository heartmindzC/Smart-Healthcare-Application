import requests
import json
import random

# 1. Cấu hình các Endpoint API
BASE_URL = "http://localhost:8085/api"
HOSPITAL_API = f"{BASE_URL}/hospitals/"
DEPARTMENT_API = f"{BASE_URL}/departments/"
DOCTOR_API = f"{BASE_URL}/doctors/"

# 2. Dữ liệu mồi cho Bệnh viện
hospitals_data = [
    {"hospitalName": "Bệnh viện Bạch Mai", "hospitalAddress": "78 Giải Phóng, Hà Nội", "hospitalPhone": "02438693731", "hospitalEmail": "bachmai@gmail.com"},
    {"hospitalName": "Bệnh viện Chợ Rẫy", "hospitalAddress": "201B Nguyễn Chí Thanh, TP.HCM", "hospitalPhone": "02838554137", "hospitalEmail": "choray@bvchoray.vn"},
    {"hospitalName": "Bệnh viện Vinmec", "hospitalAddress": "458 Minh Khai, Hà Nội", "hospitalPhone": "02439743556", "hospitalEmail": "info@vinmec.com"},
    {"hospitalName": "Bệnh viện TW Huế", "hospitalAddress": "16 Lê Lợi, Huế", "hospitalPhone": "02343822325", "hospitalEmail": "bvtwhue@vnn.vn"},
    {"hospitalName": "Bệnh viện Từ Dũ", "hospitalAddress": "284 Cống Quỳnh, TP.HCM", "hospitalPhone": "19007237", "hospitalEmail": "tudu@tudu.com.vn"}
]

# 3. Kho tài nguyên dữ liệu ngẫu nhiên cho Bác sĩ
dept_names = ["Khoa Nội", "Khoa Ngoại", "Khoa Nhi", "Khoa Sản", "Khoa Tim Mạch", "Khoa Tai Mũi Họng"]
first_names = ["Nguyễn", "Trần", "Lê", "Phạm", "Hoàng", "Võ", "Phan", "Đặng"]
middle_names = ["Văn", "Thị", "Minh", "Anh", "Hữu", "Quang"]
last_names = ["An", "Bình", "Chương", "Dương", "Hòa", "Linh", "Minh", "Nam", "Sơn", "Tùng"]

def get_id_from_res(response):
    """Hàm bổ trợ lấy ID từ cấu hình JSON bọc 'result' hoặc phẳng"""
    data = response.json()
    if "result" in data:
        return data["result"]
    return data

def generate_doctor_payload(h_id, d_id):
    """Tạo dữ liệu bác sĩ ngẫu nhiên"""
    f_name = f"{random.choice(first_names)} {random.choice(middle_names)} {random.choice(last_names)}"
    user_id = "".join([str(random.randint(0, 9)) for _ in range(12)])
    phone = f"09{random.randint(10000000, 99999999)}"
    # Email luôn có đuôi @gmail.com theo yêu cầu
    email = f"{f_name.lower().replace(' ', '')}{random.randint(100, 999)}@gmail.com"
    
    return {
        "userId": user_id,
        "password": "password123",
        "phone": phone,
        "email": email,
        "fullName": f_name,
        "address": f"Số {random.randint(1, 100)} Đường {random.randint(1, 20)}, TP. HCM",
        "birth": f"{random.randint(1970, 1995)}-{random.randint(1, 12):02d}-{random.randint(1, 28):02d}T00:00:00.000Z",
        "gender": random.choice(["MALE", "FEMALE"]),
        "hospitalId": h_id,
        "department": d_id, # departmentId lấy từ API tạo khoa
        "licenseId": f"BS-{random.randint(100000, 999999)}"
    }

def run_setup():
    headers = {'Content-Type': 'application/json'}
    
    print("--- BẮT ĐẦU THIẾT LẬP HỆ THỐNG DỮ LIỆU ---")

    for h_info in hospitals_data:
        try:
            # BƯỚC 1: TẠO BỆNH VIỆN
            h_res = requests.post(HOSPITAL_API, data=json.dumps(h_info), headers=headers)
            if h_res.status_code not in [200, 201]: continue
            
            h_data = get_id_from_res(h_res)
            h_id = h_data.get("hospitalId")
            print(f"\n[Hospital] {h_info['hospitalName']} (ID: {h_id})")

            # BƯỚC 2: TẠO 3 KHOA NGẪU NHIÊN CHO BV NÀY
            selected_depts = random.sample(dept_names, 3)
            for d_name in selected_depts:
                d_payload = {
                    "departmentName": d_name,
                    "departmentPhone": f"{h_info['hospitalPhone']}-EXT",
                    "departmentEmail": f"{d_name.lower().replace(' ', '')}@{h_info['hospitalEmail'].split('@')[1]}",
                    "hospitalId": h_id
                }
                
                d_res = requests.post(DEPARTMENT_API, data=json.dumps(d_payload), headers=headers)
                if d_res.status_code not in [200, 201]: continue
                
                d_data = get_id_from_res(d_res)
                d_id = d_data.get("departmentId")
                print(f"  └── [Dept] {d_name} (ID: {d_id})")

                # BƯỚC 3: TẠO 2-3 BÁC SĨ CHO MỖI KHOA
                num_doctors = random.randint(2, 3)
                for _ in range(num_doctors):
                    doc_payload = generate_doctor_payload(h_id, d_id)
                    doc_res = requests.post(DOCTOR_API, data=json.dumps(doc_payload), headers=headers)
                    
                    if doc_res.status_code in [200, 201]:
                        print(f"      * [Doctor] {doc_payload['fullName']} - {doc_payload['email']}")
                    else:
                        print(f"      x Lỗi tạo bác sĩ: {doc_res.text}")

        except Exception as e:
            print(f"!!! Lỗi hệ thống khi xử lý {h_info['hospitalName']}: {e}")

    print("\n--- HOÀN TẤT: Hệ thống đã sẵn sàng với dữ liệu mẫu! ---")

if __name__ == "__main__":
    run_setup()