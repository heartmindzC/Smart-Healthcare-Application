package com.example.appointmentservice.command;

import com.example.appointmentservice.model.Appointment;
import org.springframework.stereotype.Component;

@Component
public class AppointmentInvoker{
    
    //Có thể mở rộng để lưu trữ lịch sử (Undo)
    public Appointment executeCommand(AppointmentCommand command){
        return command.execute();
    }
}
