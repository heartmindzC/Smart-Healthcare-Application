package com.example.notificationservice.command;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class NotificationInvoker {

    public void executeCommand(NotificationCommand command) {
        try {
            log.info("Bat dau thuc thi NotificationCommand: {}", command.getClass().getSimpleName());
            command.execute();
            log.info("Thuc thi NotificationCommand thanh cong.");
        } catch (Exception e) {
            log.error("Loi khi thuc thi NotificationCommand: ", e);
            //Có thể thêm logic lưu lại command lỗi để retry
        }
    }
}