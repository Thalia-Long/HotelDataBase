/*
 * This is the Data Access Object class for the Hotel class
 */
package hoteldatabase;

import java.sql.PreparedStatement;
import java.sql.SQLException;

/**
 *
 * @author Hien Long
 */
public class HotelDAO {
    
    
    /**
     * Checking if the hotel is full
     *
     * @return true if it's full. False otherwise
     */
    public boolean isFull() {
        /*
        Loop thru the records in the database and check if ever
        */
        boolean full = false;
        if (numOfRooms == occupiedCnt) {
            full = true;
        }
        return false;
    }

    /**
     * Checking if the hotel is empty
     *
     * @return true if it's empty. False otherwise
     */
    public boolean isEmpty() {
        boolean empty = false;
        if (occupiedCnt == 0) {
            empty = true;
        }
        return empty;
    }

    
    /**
     * Adding new room with specified characters to the room array
     *
     * @param num : the room number
     * @param type : the bed type
     * @param s : the smoking or non-smoking type
     * @param rate : the room rate
     * Modify this add method so that it can add room to the table in the database
     */
    public void addRoom(int num, String type, char s, double rate) throws SQLException {
        Room room = new Room(num, type, s, rate);
        theRooms[numOfRooms] = room;
        room.setOccupied(false);
        numOfRooms++;
        String sql = "INSERT INTO tblRoom(num, type, s, rate) VALUES(?,?,?,?)";
        try{
            PreparedStatement ps = connection.prepareStatement(sql);
            ps.setInt(1, num);
            ps.setString(2, type);
            ps.setString(3, String.valueOf(s));
            ps.setDouble(4, rate);
            ps.executeUpdate();
        } catch(SQLException e){
            System.out.println(e.getMessage());
        }
    }

    /**
     * Add the reservation for the requested room
     *
     * @param name: occupant name
     * @param s: smoking or non-smoking type
     * @param type : bed type
     */
    public void addReservation(String name, char s, String type) {
        /*
        Go thru the rooms in the database and find the one with the requirements,
        then set the reserved to true
        */
        boolean reserved = false;
        for (Room r : theRooms) {
            if (r.isOccupied() == false && s == r.getSmoking() && type.equals(r.getBedType())) {
                r.setOccupant(name);
                r.setOccupied(true);
                occupiedCnt++;
                reserved = true;
                System.out.println("Your reservation was made.");
                break;
            }
        }
        if (reserved == false) {
            System.out.println("Sorry, there is no room available!");
        }

    }

    /**
     * Cancel the existing reservation if found
     *
     * @param name : occupant name
     */
    public void cancelReservation(String name) {
        int index = findReservation(name);
        if (index == NOT_FOUND) {
            System.out.println("Your reservation cannot be found.");
        } else {
            System.out.println("Your reservation is canceled.");
            occupiedCnt--;
        }

    }

    /**
     * Finding the reservation
     *
     * @param name : occupant name
     * @return the index room if found. Return NOT_FOUND otherwise
     */
    private int findReservation(String name) {
        int roomIndex = 0;
        for (int i = 0; i < 10; i++) {
            if (theRooms[i] != null && theRooms[i].equals(theRooms[i].getOccupant())) {
                theRooms[i].setOccupied(false);
                roomIndex = i;
            } else {
                roomIndex = NOT_FOUND;
            }
        }
        return roomIndex;
    }

    /**
     * Print the reservation list
     */
    public void printReservationList() {
        for (Room r : theRooms) {
            if (r != null && r.isOccupied() == true) {
                System.out.println("Room Number: " + r.getRoomNum());
                System.out.println("Occupant Name: " + r.getOccupant());
                System.out.println("Smoking room: " + r.getSmoking());
                System.out.println("Bed Type: " + r.getBedType());
                System.out.println("Rate: " + r.getRoomRate());
                System.out.println("\n");
            }
        }
    }

    /**
     * Calculate the daily sale
     *
     * @return the daily sale amount
     */
    public double getDailySales() {
        double amount = 0;
        for (Room r : theRooms) {
            if (r != null && r.isOccupied() == true) {
                amount = amount + r.getRoomRate();
            }
        }
        return amount;
    }

    /**
     * Calculate occupancy percentage
     *
     * @return occupancy percentage
     */
    public double occupancyPercentage() {

        double percentage;
        for (Room r : theRooms) {
            if (r != null && r.isOccupied() == true) {
                occupiedCnt++;
            }
        }
        percentage = (double) occupiedCnt / (double) numOfRooms;
        return percentage;

    }

    /**
     * Prepare the hotel details
     *
     * @return the hotel details
     */
    public String toString() {
        String details;
        details = "Hotel Name: " + name + "\nNumber of Rooms: " + numOfRooms + "\nNumber of Occupied Rooms: " + occupiedCnt + "\n\n\nRoomDetails are: " + theRooms.toString();
        return details;
    }

}
