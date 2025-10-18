package hospital;

public interface GetAppointment {
    boolean set_appointment(int date, int time);
    boolean cancel();
    boolean complete();
}

