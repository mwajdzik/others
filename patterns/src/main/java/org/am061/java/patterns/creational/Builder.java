package org.am061.java.patterns.creational;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.am061.java.patterns.DesignPattern;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;

/*
    Helps to avoid complex constructors. Provides flexibility when creating objects.
 */
public class Builder implements DesignPattern {

    @Getter
    @Setter
    @ToString
    @AllArgsConstructor
    private class Room {
        private Dimension dimensions;
        private Color wallColor;
        private int floorNumber;
        private int ceilingHeight;
        private int numberOfDoors;
        private int numberOfWindows;
    }

    @Getter
    @Setter
    @ToString
    @AllArgsConstructor
    private class House {
        private List<Room> rooms;
    }

    private class RoomBuilder {

        private Dimension dimensions;
        private int ceilingHeight;
        private int floorNumber;
        private Color wallColor;
        private int numberOfWindows;
        private int numberOfDoors;

        private RoomListBuilder roomListBuilder;

        RoomBuilder(RoomListBuilder roomListBuilder) {
            this.roomListBuilder = roomListBuilder;
        }

        RoomBuilder setDimensions(Dimension dimensions) {
            this.dimensions = dimensions;
            return this;
        }

        RoomBuilder setCeilingHeight(int ceilingHeight) {
            this.ceilingHeight = ceilingHeight;
            return this;
        }

        RoomBuilder setFloorNumber(int floorNumber) {
            this.floorNumber = floorNumber;
            return this;
        }

        RoomBuilder setWallColor(Color wallColor) {
            this.wallColor = wallColor;
            return this;
        }

        RoomBuilder setNumberOfWindows(int numberOfWindows) {
            this.numberOfWindows = numberOfWindows;
            return this;
        }

        RoomBuilder setNumberOfDoors(int numberOfDoors) {
            this.numberOfDoors = numberOfDoors;
            return this;
        }

        Room createRoom() {
            return new Room(dimensions, wallColor, floorNumber, ceilingHeight, numberOfDoors, numberOfWindows);
        }

        RoomListBuilder addRoomToList() {
            Room room = createRoom();
            this.roomListBuilder.addRoom(room);
            return this.roomListBuilder;
        }
    }

    private class RoomListBuilder {

        private List<Room> listOfRooms;

        RoomListBuilder addList() {
            this.listOfRooms = new ArrayList<>();
            return this;
        }

        RoomListBuilder addRoom(Room room) {
            listOfRooms.add(room);
            return this;
        }

        RoomBuilder addRoom() {
            return new RoomBuilder(this);
        }

        List<Room> buildList() {
            return listOfRooms;
        }
    }

    @Override
    public void run() {
        List<Room> rooms = new RoomListBuilder().addList()
                .addRoom().setFloorNumber(2).setWallColor(Color.BLUE).setDimensions(new Dimension(100, 200)).addRoomToList()
                .addRoom().setFloorNumber(1).setCeilingHeight(250).setNumberOfWindows(2).setNumberOfDoors(1).addRoomToList()
                .buildList();

        House house = new House(rooms);
        System.out.println(house);
    }
}
