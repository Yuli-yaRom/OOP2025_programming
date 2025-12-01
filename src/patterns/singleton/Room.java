package patterns.singleton;

public class Room {
    private String number;
    private RoomType type;
    private boolean isOccupied;

    public Room(String number, RoomType type) {
        this.number = number;
        this.type = type;
        this.isOccupied = false;
    }

    public String getNumber() { return number; }
    public boolean isOccupied() { return isOccupied; }
    public void setOccupied(boolean occupied) { isOccupied = occupied; }
}
