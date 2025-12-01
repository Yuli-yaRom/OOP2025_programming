package appointment;

import java.time.LocalDateTime;

//
public interface AppointmentActions {
    // Перенести прийом на інший час
    void reschedule(LocalDateTime newDateTime);

    // Скасувати прийом
    boolean cancel();

    // Завершити прийом (лікар виконав роботу)
    boolean complete();
}

