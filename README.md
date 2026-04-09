# Smart-Healthcare-Application--
Applying Design Pattern for Optimization coding structure --
--
--
--

1. Singleton: Log
2. Strategy: Authen & User
3. Template: from (user-service, patient-service, doctor-service => user-service(authen,patient,doctor domain))
----- New variant for Template Pattern:
Appointment Status Transition:
Mô tả: Các chuyển trạng thái appointment (confirm, cancel, complete) có cấu trúc tương tự.
Template Method áp dụng:
Base: AbstractAppointmentStatusHandler
Template method: changeStatus()
.....
Các bước chung:
Validate appointment exists
Validate current status allows transition
Get old status
Update status
Update related fields (notes, updatedAt)
Log to history (nếu có)
Update related resources (TimeSlot availability)
Send notification (nếu có)
Các bước khác nhau:
Confirm: Mark time slot unavailable, send confirmation email
Cancel: Restore time slot, send cancellation email, record reason
Complete: Create EHR entry, send completion notification




----------
4. Reformat output endpoints: ( code, messages, results[])
    - User --
    - Patient --
    - Hospital -- 
    - appointment --
    - Doctor --
    - Ehr
5. Simple Factory: CRUD user/patient/doctor
6. Commmand Pattern: Pending Solution
