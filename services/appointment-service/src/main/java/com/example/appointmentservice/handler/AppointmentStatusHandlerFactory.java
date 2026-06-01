package com.example.appointmentservice.handler;

import com.example.appointmentservice.exception.AppointmentErrorCode;
import com.example.appointmentservice.model.AppointmentStatus;
import com.example.common_exception.AppException;
import org.springframework.stereotype.Component;

import java.util.EnumMap;
import java.util.Map;

/**
 * Factory để lấy Handler phù hợp với status cần chuyển đổi
 * 
 * Sử dụng EnumMap để optimize performance khi lookup handler
 */
@Component
public class AppointmentStatusHandlerFactory {

    private final Map<AppointmentStatus, AbstractAppointmentStatusHandler> handlers;

    public AppointmentStatusHandlerFactory(
            ConfirmAppointmentHandler confirmHandler,
            CancelAppointmentHandler cancelHandler,
            CompleteAppointmentHandler completeHandler) {
        
        handlers = new EnumMap<>(AppointmentStatus.class);
        handlers.put(AppointmentStatus.CONFIRMED, confirmHandler);
        handlers.put(AppointmentStatus.CANCELLED, cancelHandler);
        handlers.put(AppointmentStatus.COMPLETED, completeHandler);
    }

    /**
     * Lấy handler phù hợp với status
     * 
     * @param status Status cần xử lý
     * @return Handler tương ứng
     * @throws AppException nếu không có handler cho status đó
     */
    public AbstractAppointmentStatusHandler getHandler(AppointmentStatus status) {
        AbstractAppointmentStatusHandler handler = handlers.get(status);
        
        if (handler == null) {
            throw new AppException(AppointmentErrorCode.STATUS_INVALID, 
                    "Không có handler cho status: " + status);
        }
        
        return handler;
    }

    /**
     * Kiểm tra xem có handler cho status này không
     */
    public boolean hasHandler(AppointmentStatus status) {
        return handlers.containsKey(status);
    }
}
