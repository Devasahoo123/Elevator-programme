import java.util.*;

// Enum representing the direction of the elevator
enum Direction {
    UP,
    DOWN,
    IDLE
}

// Main Elevator class
class Elevator {
    private static final int MIN_FLOOR = 0;
    private static final int MAX_FLOOR = 10;
    private static int processingTime = 500; // ms
    private int currentFloor;
    private Direction currentDirection;
    private Map<Integer, List<Integer>> floorRequestsMap; // Keeps track of floor requests
    private Boolean[] scheduledFloors; // Keeps track of scheduled floors

    // Constructor initializes elevator state
    public Elevator() {
        this.currentFloor = 0;
        this.currentDirection = Direction.UP;
        this.floorRequestsMap = new HashMap<>();
        this.scheduledFloors = new Boolean[MAX_FLOOR + 1];
        Arrays.fill(this.scheduledFloors, Boolean.FALSE);
    }

    // Set processing time for elevator movements
    public void setProcessingTime(int processingTime) {
        Elevator.processingTime = processingTime;
    }

    // Getter for current floor
    public int getCurrentFloor() {
        return this.currentFloor;
    }

    // Getter for floor requests map
    public Map<Integer, List<Integer>> getFloorRequestsMap() {
        return this.floorRequestsMap;
    }

    // Getter for scheduled floors array
    public Boolean[] getScheduledFloors() {
        return this.scheduledFloors;
    }

    // Main elevator loop
    public void start() throws InterruptedException {
        currentDirection = Direction.UP; // Elevator starts by moving up
        do {
            System.out.println("--------");
            processFloor(currentFloor); // Process the current floor
            System.out.println("--------");
        } while (currentDirection != Direction.IDLE); // Continue until idle
        System.out.println("No one is waiting, and no one is looking to go anywhere.");
        System.out.println("Elevator is idle now.");
    }

    // Simulate a rush of elevator requests
    public void simulateElevatorRush() {
        Random random = new Random();
        for (int i = 0; i < 30; i++) {
            requestElevator(random.nextInt(11), random.nextInt(10) + 1);
        }
    }

    // Request the elevator to pick someone up and take them to a destination
    public void requestElevator(int startFloor, int destinationFloor) {
        if (isInvalidFloor(startFloor) || isInvalidFloor(destinationFloor) || startFloor == destinationFloor) {
            System.out.println("INVALID FLOORS. Try again.");
            return;
        }
        if (floorRequestsMap.containsKey(startFloor)) {
            floorRequestsMap.get(startFloor).add(destinationFloor);
        } else {
            floorRequestsMap.put(startFloor, new ArrayList<>() {{ add(destinationFloor); }});
        }
    }

    // Process the current floor - boarding and unboarding
    private void processFloor(int currentFloor) throws InterruptedException {
        if (scheduledFloors[currentFloor]) {
            System.out.println("UN-BOARDING at Floor: " + currentFloor);
        }
        if (floorRequestsMap.containsKey(currentFloor)) {
            System.out.println("BOARDING at Floor: " + currentFloor);
            floorRequestsMap.get(currentFloor).forEach(destinationFloor -> scheduledFloors[destinationFloor] = true);
            floorRequestsMap.remove(currentFloor);
        }
        scheduledFloors[currentFloor] = false;
        moveElevator();
    }

    // Move the elevator based on its current direction
    private void moveElevator() throws InterruptedException {
        if (!Arrays.asList(scheduledFloors).contains(true) && floorRequestsMap.isEmpty()) {
            currentDirection = Direction.IDLE;
            return;
        } else if (isInvalidFloor(currentFloor + 1)) {
            currentDirection = Direction.DOWN;
        } else if (isInvalidFloor(currentFloor - 1)) {
            currentDirection = Direction.UP;
        }
        switch (currentDirection) {
            case UP: {
                moveUp();
                break;
            }
            case DOWN: {
                moveDown();
                break;
            }
            default: {
                System.out.println("Elevator Malfunctioned");
            }
        }
    }

    // Move the elevator up and update its current floor
    private void moveUp() throws InterruptedException {
        currentFloor++;
        System.out.println("GOING UP TO " + currentFloor);
        Thread.sleep(processingTime);
    }

    // Move the elevator down and update its current floor
    private void moveDown() throws InterruptedException {
        currentFloor--;
        System.out.println("GOING DOWN TO " + currentFloor);
        Thread.sleep(processingTime);
    }

    // Check if a given floor is invalid
    private boolean isInvalidFloor(int floor) {
        return floor < MIN_FLOOR || floor > MAX_FLOOR;
    }
}

public class ElevatorChallenge {
    // Simulate elevator behavior with automatic requests
    static void automaticElevator() throws InterruptedException {
        Elevator elevator = new Elevator();
        elevator.simulateElevatorRush();
        elevator.start();
    }

    // Simulate elevator behavior with manual request
    static void manualElevator() throws InterruptedException {
        Elevator elevator = new Elevator();
        Scanner scanner = new Scanner(System.in);
        System.out.println("Enter a starting floor (0 - 10)");
        int startFloor = scanner.nextInt();
        System.out.println("Enter a destination floor (0 - 10)");
        int endFloor = scanner.nextInt();
        elevator.requestElevator(startFloor, endFloor);
        elevator.start();
    }

    public static void main(String[] args) throws InterruptedException {
        manualElevator();
        // automaticElevator();
    }
}
