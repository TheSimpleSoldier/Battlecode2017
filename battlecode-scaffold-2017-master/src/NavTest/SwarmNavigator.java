package NavTest;

import battlecode.common.Direction;
import battlecode.common.MapLocation;
import battlecode.common.RobotController;

/**
 * Created by joshua on 1/12/17.
 */
public class SwarmNavigator {

    RobotController rc;

    public SwarmNavigator(RobotController rc) {
        this.rc = rc;
    }

    public Action getNextSpot(MapLocation target) {
        Action action = new Action();

        //Ideal location, will be moved around based on external stimuli
        //TODO: change 1 to be stride length
        action.moveLocation = rc.getLocation().add(new Direction(rc.getLocation(), target), 1);

        //TODO: react to trees
        //TODO: react to ally bots
        //TODO: react to enemy bots

        return action;
    }
}
