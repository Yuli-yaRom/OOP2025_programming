package Main.patterns.observer;


import java.util.ArrayList;
import java.util.List;

public class AppointmentNotifier implements Subject {
    private List<Observer> observers = new ArrayList<>();

    @Override
    public void addObserver(Observer observer) {
        observers.add(observer);
    }

    @Override
    public void removeObserver(Observer observer) {
        observers.remove(observer);
    }

    @Override
    public void notifyObservers(String message) {
        for (Observer observer : observers) {
            observer.update(message);
        }
    }

    public void newSlotAvailable() {
        notifyObservers("A new Main.appointment slot is available!");
    }
}

