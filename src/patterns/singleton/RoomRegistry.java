package patterns.singleton;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import java.util.ArrayList;
import java.util.List;

public class RoomRegistry {
    private final List<String> availableRooms;

    private RoomRegistry() {
        availableRooms = new ArrayList<>();
        // Add some initial rooms
        availableRooms.add("101");
        availableRooms.add("102");
        availableRooms.add("201");
        availableRooms.add("202");
    }

    private static class RoomRegistryHolder {
        private static final RoomRegistry INSTANCE = new RoomRegistry();
    }

    public static RoomRegistry getInstance() {

        return RoomRegistryHolder.INSTANCE;
    }

    public List<String> getAvailableRooms() {

        return new ArrayList<>(availableRooms);
    }

    public synchronized void bookRoom(String roomNumber) {

        availableRooms.remove(roomNumber);
    }

    public synchronized void releaseRoom(String roomNumber) {

        availableRooms.add(roomNumber);
    }
}

