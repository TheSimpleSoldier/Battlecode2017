package Gardner;

import battlecode.common.Clock;
import battlecode.common.Direction;
import battlecode.common.GameActionException;

public class Archon extends Unit {
    static int gardnerCount = 0;

    public static void loop() throws GameActionException {
        System.out.println("I'm an archon!");

        // The code you want your robot to perform every round should be in this loop
        while (true) {

            // Try/catch blocks stop unhandled exceptions, which cause your robot to explode
            try {

                if (gardnerCount < 5) {
                    for (int i = 0; i < 8; i++) {
                        Direction dir = Util.getDirectionForInt(i);

                        if (rc.canHireGardener(dir)) {
                            gardnerCount++;
                            rc.hireGardener(dir);
                        }
                    }
                }

                // Move randomly
                Util.tryMove(Util.randomDirection());

                // Clock.yield() makes the robot wait until the next turn, then it will perform this loop again
                Clock.yield();

            } catch (Exception e) {
                System.out.println("Archon Exception");
                e.printStackTrace();
            }
        }
    }
}
