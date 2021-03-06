package org.am061.java.patterns.creational;

import lombok.Getter;
import org.am061.java.patterns.DesignPattern;

/*
    If you want to follow some steps to create an instance of the complex object
    you can go with the builder pattern with director.
 */
public class BuilderWithDirector implements DesignPattern {

    abstract class ComputerBuilder {

        @Getter
        protected Computer computer;

        abstract void buildProcessor();

        abstract void buildMotherboard();

        abstract void buildHardDisk();

        abstract void buildScreen();
    }

    public enum ComputerTyp {
        APPLE,
        DESKTOP,
        LAPTOP
    }

    public class ComputerShopDirector {
        void constructComputer(ComputerBuilder computerBuilder) {
            computerBuilder.buildMotherboard();
            computerBuilder.buildProcessor();
            computerBuilder.buildHardDisk();
            computerBuilder.buildScreen();
        }
    }

    public class Computer {

        private ComputerTyp computerType;
        private String motherBoard;
        private String processor;
        private String hardDisk;
        private String screen;

        Computer(ComputerTyp computerTyp) {
            computerType = computerTyp;
        }

        @Override
        public String toString() {
            return String.format("%s (%s, %s, %s, %s)",
                    computerType, motherBoard, processor, hardDisk, screen);
        }
    }

    public class DesktopBuilder extends ComputerBuilder {

        DesktopBuilder() {
            computer = new Computer(ComputerTyp.DESKTOP);
        }

        protected void buildHardDisk() {
            computer.hardDisk = "2TB";
        }

        protected void buildScreen() {
            computer.screen = "21 inch (1980 x 1200)";
        }

        protected void buildProcessor() {
            computer.processor = "Intel Xeon 7500";
        }

        protected void buildMotherboard() {
            computer.motherBoard = "Asus P6X58D Premium";
        }
    }

    public class LaptopBuilder extends ComputerBuilder {

        LaptopBuilder() {
            computer = new Computer(ComputerTyp.LAPTOP);
        }

        protected void buildHardDisk() {
            computer.hardDisk = "250GB";
        }

        protected void buildScreen() {
            computer.screen = "15.4-inch (1280 x 800)";
        }

        protected void buildProcessor() {
            computer.processor = "Intel Core 2 Duo";
        }

        protected void buildMotherboard() {
            computer.motherBoard = "DELL MotherBoard";
        }
    }

    public class AppleBuilder extends ComputerBuilder {

        AppleBuilder() {
            computer = new Computer(ComputerTyp.APPLE);
        }

        protected void buildHardDisk() {
            computer.hardDisk = "320GB";
        }

        protected void buildScreen() {
            computer.screen = "24 inch (1980 x 1200)";
        }

        protected void buildProcessor() {
            computer.processor = "Intel Core 2 Duo";
        }

        protected void buildMotherboard() {
            computer.motherBoard = "iMac G5 PowerPC";
        }
    }

    public void run() {
        ComputerShopDirector computerShop = new ComputerShopDirector();

        ComputerBuilder laptopBuilder = new LaptopBuilder();
        computerShop.constructComputer(laptopBuilder);
        System.out.println(laptopBuilder.getComputer());

        ComputerBuilder appleBuilder = new AppleBuilder();
        computerShop.constructComputer(appleBuilder);
        System.out.println(appleBuilder.getComputer());

        ComputerBuilder desktopBuilder = new DesktopBuilder();
        computerShop.constructComputer(desktopBuilder);
        System.out.println(desktopBuilder.getComputer());
    }
}
