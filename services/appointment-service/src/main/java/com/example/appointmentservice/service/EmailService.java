package com.example.appointmentservice.service;

import com.example.appointmentservice.model.Appointment;
import jakarta.mail.internet.MimeMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

@Service
public class EmailService {

    @Autowired
    private JavaMailSender mailSender;

    /**
     * Gửi email xác nhận đặt lịch thành công.
     * FE sẽ chịu trách nhiệm gọi user-service để lấy email bệnh nhân
     * và truyền email đó xuống endpoint của appointment-service.
     * @param toEmail Email người nhận
     * @param appointment Thông tin lịch hẹn
     * @param hospitalAddress Địa chỉ bệnh viện (nullable - không bắt buộc)
     */
    public void sendAppointmentConfirmationEmail(String toEmail, Appointment appointment, String hospitalAddress) {
        try {
            MimeMessage mimeMessage = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(mimeMessage, true, "UTF-8");

            helper.setTo(toEmail);
            helper.setSubject("Xác nhận lịch khám - Smart Healthcare System");

            String htmlTemplate = """
                <html>
                <head>
                   <style>
                                    body {
                                        font-family: Arial, sans-serif;
                                        background-color: #f0f4f8;
                                        margin: 0;
                                        padding: 20px;
                                    }
                                    .container {
                                        max-width: 600px;
                                        margin: auto;
                                        background: #ffffff;
                                        border-radius: 12px;
                                        overflow: hidden;
                    
                                        /* 🌟 Đổ bóng đẹp – cao cấp */
                                        box-shadow:\s
                                            0 6px 20px rgba(0,0,0,0.12),\s
                                            0 2px 6px rgba(0,0,0,0.08);
                                    }
                                    .header {
                                        background: #1E88E5;
                                        color: white;
                                        padding: 24px;
                                        text-align: center;
                                    }
                                    .title {
                                        font-size: 24px;
                                        font-weight: bold;
                                        margin-bottom: 5px;
                                    }
                                    .content {
                                        padding: 25px;
                                        color: #333;
                                        font-size: 16px;
                                        line-height: 1.6;
                                    }
                    
                                    /* 🎯 Thêm bóng nhẹ cho info-box */
                                    .info-box {
                                        background: #E3F2FD;
                                        border-left: 4px solid #1E88E5;
                                        padding: 18px;
                                        margin: 20px 0;
                                        border-radius: 8px;
                    
                                        box-shadow:\s
                                            0 3px 10px rgba(30,136,229,0.15);
                                    }
                    
                                    .footer {
                                        text-align: center;
                                        padding: 15px;
                                        font-size: 13px;
                                        color: #777;
                                        background: #f1f1f1;
                                    }
                                    .label {
                                        font-weight: bold;
                                        color: #1E88E5;
                                    }
                                </style>
                    
                </head>
                <body>
                    <div class="container">
                        <div class="header">
                            <div class="title">Smart Healthcare System</div>
                            <div>Xác nhận đặt lịch khám thành công</div>
                        </div>
                        <div class="content">
                            <p>Xin chào <strong>%s</strong>,</p>
                            <p>Lịch khám của bạn đã được đặt thành công với thông tin như sau:</p>

                            <div class="info-box">
                                <p><span class="label"> Patient Name:</span> %s</p>
                                <p><span class="label"> Doctor:</span> %s</p>
                                <p><span class="label"> Time at:</span> %s</p>
                                <p><span class="label"> Hospital:</span> %s</p>
                                %s
                                <p><span class="label"> Department:</span> %s</p>
                            </div>

                            <p>Vui lòng đến đúng giờ và mang theo giấy tờ cần thiết.</p>
                            <p>Chúc bạn một ngày tốt lành!</p>

                            <p>Trân trọng,<br><strong>Smart Healthcare System</strong></p>
                        </div>
                        <div class="footer">
                            Email được gửi tự động. Vui lòng không phản hồi email này.
                        </div>
                    </div>
                </body>
                </html>
                """;
            
            // Build address section with Google Maps link
            String addressSection = "";
            if (hospitalAddress != null && !hospitalAddress.trim().isEmpty()) {
                try {
                    String encodedAddress = java.net.URLEncoder.encode(hospitalAddress, "UTF-8");
                    String mapsUrl = "https://www.google.com/maps/search/?api=1&query=" + encodedAddress;
                    addressSection = "<p><span class=\"label\"> Address:</span> " + hospitalAddress + 
                                   " <a href=\"" + mapsUrl + "\" style=\"color: #1E88E5; text-decoration: none;\">[Xem trên Google Maps 🗺️]</a></p>";
                } catch (Exception e) {
                    // Fallback without link if encoding fails
                    addressSection = "<p><span class=\"label\"> Address:</span> " + hospitalAddress + "</p>";
                }
            }
            
            String htmlContent = String.format(htmlTemplate,
                    appointment.getPatientName(),
                    appointment.getPatientName(),
                    appointment.getDoctorName(),
                    appointment.getAppointmentDateTime(),
                    appointment.getHospitalName(),
                    addressSection,
                    appointment.getDepartmentName()
            );

            helper.setText(htmlContent, true);
            mailSender.send(mimeMessage);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
