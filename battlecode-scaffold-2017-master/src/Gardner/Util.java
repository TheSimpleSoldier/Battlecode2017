package Gardner;

import battlecode.common.*;
import scala.Int;

public strictfp class Util {
    public static boolean notFoundCorners = true;


    public static MapLocation nextTargetLocation() throws GameActionException {
        MapLocation us = Unit.rc.getLocation();

        // look for archons in distress nearby
        if (Unit.rc.getRoundNum() - Unit.rc.readBroadcast(Constants.ArchonInDestressTimeStamp) <= Constants.roundCutOff) {
            MapLocation loc = decodeMapLocationFromInt(Unit.rc.readBroadcast(Constants.ArchonInDestress));

            if (loc.distanceSquaredTo(us) <= Constants.farthestDistToRespondToAttack) {
                return loc;
            }
        }

        // look for gardners in distress nearby
        for (int i = Constants.gardnerInDestressTimeStamps.length; --i>=0; ) {
            if (Unit.rc.getRoundNum() - Unit.rc.readBroadcast(Constants.gardnerInDestressTimeStamps[i]) <= Constants.roundCutOff) {
                MapLocation loc = decodeMapLocationFromInt(Unit.rc.readBroadcast(Constants.gardnerInDistressLocs[i]));

                if (loc.distanceSquaredTo(us) <= Constants.farthestDistToRespondToAttack) {
                    return loc;
                }
            }
        }

        // look for enemy archons nearby
        if (Unit.rc.getRoundNum() - Unit.rc.readBroadcast(Constants.enemyArchon1TimeStamp) <= Constants.roundCutOff) {
            MapLocation loc = decodeMapLocationFromInt(Unit.rc.readBroadcast(Constants.enemyArchon1Loc));

            if (loc.distanceSquaredTo(us) <= Constants.farthestDistToRespondToAttack) {
                return loc;
            }
        }

        if (Unit.rc.getRoundNum() - Unit.rc.readBroadcast(Constants.enemyArchon2TimeStamp) <= Constants.roundCutOff) {
            MapLocation loc = decodeMapLocationFromInt(Unit.rc.readBroadcast(Constants.enemyArchon2Loc));

            if (loc.distanceSquaredTo(us) <= Constants.farthestDistToRespondToAttack) {
                return loc;
            }
        }

        if (Unit.rc.getRoundNum() - Unit.rc.readBroadcast(Constants.enemyArchon3TimeStamp) <= Constants.roundCutOff) {
            MapLocation loc = decodeMapLocationFromInt(Unit.rc.readBroadcast(Constants.enemyArchon3Loc));

            if (loc.distanceSquaredTo(us) <= Constants.farthestDistToRespondToAttack) {
                return loc;
            }
        }

        // look for enemy gardners nearby
        for (int i = Constants.enemyGardnerPositions.length; --i>=0; ) {
            if (Unit.rc.getRoundNum() - Unit.rc.readBroadcast(Constants.gardnerInDestressTimeStamps[i]) <= Constants.roundCutOff) {
                MapLocation loc = decodeMapLocationFromInt(Unit.rc.readBroadcast(Constants.gardnerInDistressLocs[i]));

                if (loc.distanceSquaredTo(us) <= Constants.farthestDistToRespondToAttack) {
                    return loc;
                }
            }
        }

        // look for enemy units to kill

        // head towards enemy archon starting points

        return null;
    }

    /**
     * This method sends out a distress signal for gardners
     *
     * @param enemies
     * @param allies
     * @throws GameActionException
     */
    public static void GardnerDistressSignal(RobotInfo[] enemies, RobotInfo[] allies) throws GameActionException {
        // if there are no enemies do not send out distress call
        if (enemies.length == 0) return;

        // if our army is bigger then don't send out distress call
        if (ArmyFirePower(enemies) <= ArmyFirePower(allies)) return;

        int oldestTimeStamp = Int.MaxValue();
        int oldestChannel = 0;

        for (int i = Constants.gardnerInDestressTimeStamps.length; --i>=0; ) {
            int val = Unit.rc.readBroadcast(Constants.gardnerInDestressTimeStamps[i]);

            if (val < oldestTimeStamp) {
                oldestTimeStamp = val;
                oldestChannel = i;
            }
        }

        Unit.rc.broadcast(Constants.gardnerInDestressTimeStamps[oldestChannel], Unit.rc.getRoundNum());
        Unit.rc.broadcast(Constants.gardnerInDistressLocs[oldestChannel], encodeMapLocationIntoInt(Unit.rc.getLocation()));
    }

    /**
     * This method sends out a distress signal for archons
     *
     * @param enemies
     * @param allies
     * @throws GameActionException
     */
    public static void ArchonDistressSignal(RobotInfo[] enemies, RobotInfo[] allies) throws GameActionException {
        // if there are no enemies do not send out distress call
        if (enemies.length == 0) return;

        // if our army is bigger then don't send out distress call
        if (ArmyFirePower(enemies) <= ArmyFirePower(allies)) return;

        Unit.rc.broadcast(Constants.ArchonInDestressTimeStamp, Unit.rc.getRoundNum());
        Unit.rc.broadcast(Constants.ArchonInDestress, encodeMapLocationIntoInt(Unit.rc.getLocation()));
    }

    /**
     * This method calculates the power of an army of units
     *
     * @param units
     * @return
     */
    public static int ArmyFirePower(RobotInfo[] units) {
        int power = 0;
        for (int i = units.length; --i>=0; ) {
            power += units[i].type.attackPower * units[i].health;
        }

        return power;
    }

    /**
     * This method updates enemy positions
     *
     * @param enemies
     * @param trees
     * @throws GameActionException
     */
    public static void scanEnemyInformation(RobotInfo[] enemies, TreeInfo[] trees) throws GameActionException {
        for (int i = enemies.length; --i>=0; ) {
            switch (enemies[i].type) {
                case ARCHON:
                    updateForEnemyArchon(enemies[i]);
                    break;
                case GARDENER:
                    updateForList(enemies[i].location, Constants.enemyGardnerPositions);
                    break;
                default:
                    break;
            }
        }

        for (int i = trees.length; --i>=0; ) {
            if (trees[i].team == Unit.enemy) {
                updateForList(trees[i].location, Constants.enemyTreePositions);
            }
        }
    }

    /**
     * This method takes a list and updates it for a unit (it updates an existing location if they are close)
     *
     * @param loc
     * @throws GameActionException
     */
    public static void updateForList(MapLocation loc, int[] channels) throws GameActionException {
        int openChannel = 0;

        for (int i = channels.length; --i>=0; ) {
            int val = Unit.rc.readBroadcast(channels[i]);

            if (val < 1) {
                openChannel = i;
                continue;
            }

            if (decodeMapLocationFromInt(val).distanceSquaredTo(loc) <= 36) {
                Unit.rc.broadcast(channels[i], encodeMapLocationIntoInt(loc));
                return;
            }
        }

        Unit.rc.broadcast(openChannel, encodeMapLocationIntoInt(loc));
    }

    /**
     * This method updates the broadcasted location of enemy archons
     *
     * @param enemyArchon
     * @throws GameActionException
     */
    public static void updateForEnemyArchon(RobotInfo enemyArchon) throws GameActionException {
        int id = enemyArchon.ID;

        int firstArchonID = Unit.rc.readBroadcast(Constants.enemyArchon1ID);
        if (firstArchonID == 0 || firstArchonID == id) {
            Unit.rc.broadcast(Constants.enemyArchon1ID, id);
            int loc = encodeMapLocationIntoInt(enemyArchon.location);
            Unit.rc.broadcast(Constants.enemyArchon1Loc, loc);\
            Unit.rc.broadcast(Constants.enemyArchon1TimeStamp, Unit.rc.getRoundNum());
        } else {
            int secondArchonID = Unit.rc.readBroadcast(Constants.enemyArchon2ID);
            if (secondArchonID == 0 || secondArchonID == id) {
                Unit.rc.broadcast(Constants.enemyArchon2ID, id);
                int loc = encodeMapLocationIntoInt(enemyArchon.location);
                Unit.rc.broadcast(Constants.enemyArchon2Loc, loc);
                Unit.rc.broadcast(Constants.enemyArchon2TimeStamp, Unit.rc.getRoundNum());
            } else {
                int thirdArchonID = Unit.rc.readBroadcast(Constants.enemyArchon3ID);
                if (thirdArchonID == 0 || thirdArchonID == id) {
                    Unit.rc.broadcast(Constants.enemyArchon3ID, id);
                    int loc = encodeMapLocationIntoInt(enemyArchon.location);
                    Unit.rc.broadcast(Constants.enemyArchon3Loc, loc);
                    Unit.rc.broadcast(Constants.enemyArchon3TimeStamp, Unit.rc.getRoundNum());
                }
            }
        }
    }

    /**
     * This method takes an int and converts it to a MapLocation
     *
     * @param val
     * @return
     */
    public static MapLocation decodeMapLocationFromInt(int val) {
        int x = val % 1000;
        int y = val / 1000;
        return new MapLocation(x, y);
    }

    /**
     * This method takes a mapLocation and converts it to an int
     *
     * @param loc
     * @return
     */
    public static int encodeMapLocationIntoInt(MapLocation loc) {
        int x = (int) loc.x;
        int y = ((int) loc.y) * 1000;
        return x + y;
    }

    /**
     * This method determines if we still need to scout out the boundries of the map
     *
     * @return
     * @throws GameActionException
     */
    public static boolean needScout() throws GameActionException {
        if (!notFoundCorners) return false;

        notFoundCorners = false;
        if (Unit.rc.readBroadcast(Constants.foundHighestMapX) < 1) notFoundCorners = true;
        else if (Unit.rc.readBroadcast(Constants.foundHighestMaxY) < 1) notFoundCorners = true;
        else if (Unit.rc.readBroadcast(Constants.foundLowestMapX) < 1) notFoundCorners = true;
        else if (Unit.rc.readBroadcast(Constants.foundLowestMapY) < 1) notFoundCorners = true;

        return notFoundCorners;
    }



    /**
     * This method updates a counter in a channel
     *
     * @param channel
     * @throws GameActionException
     */
    public static void updateChannel(int channel) throws GameActionException {
        int val = Unit.rc.readBroadcast(channel);
        val++;
        Unit.rc.broadcast(channel, val);
    }

    /**
     * This method gets the value for a channel that keeps increasing
     * @param channel1
     * @param channel2
     * @return
     * @throws GameActionException
     */
    public static int getUpdatingChannelCount(int channel1, int channel2) throws GameActionException {
        if (Unit.rc.getRoundNum() % 2 == 0) {
            return Unit.rc.readBroadcast(channel1);
        } else {
            return Unit.rc.readBroadcast(channel2);
        }
    }

    /**
     * This method keeps the amount of a particular unit count up to date
     *
     * @param channel1
     * @param channel2
     * @throws GameActionException
     */
    public static void updateUnitCountChannel(int channel1, int channel2) throws GameActionException {
        if (Unit.rc.getRoundNum() % 2 == 0) {
            Unit.rc.broadcast(channel1, 0);
            updateChannel(channel2);
        } else {
            Unit.rc.broadcast(channel2, 0);
            updateChannel(channel1);
        }
    }

    /**
     * This method causes a gardner to run away from enemies
     *
     * @param enemies
     * @throws GameActionException
     */
    public static boolean runAwayFromEnemies(RobotInfo[] enemies) throws GameActionException {
        if (enemies.length > 0) {
            MapLocation com = unitCOM(enemies);

            tryMove(Unit.rc.getLocation().directionTo(com).opposite());
            return true;
        }

        return false;
    }

    /**
     * THis method has a lumberjack attack
     *
     * @throws GameActionException
     */
    public static void LumberJackAttack() throws GameActionException {
        RobotInfo[] enemies = Unit.rc.senseNearbyRobots(GameConstants.LUMBERJACK_STRIKE_RADIUS, Unit.enemy);
        RobotInfo[] allies = Unit.rc.senseNearbyRobots(GameConstants.LUMBERJACK_STRIKE_RADIUS, Unit.us);
        if (enemies.length >= allies.length) {
            Unit.rc.strike();
        }
    }

    /**
     * This method plants a tree around a gardner
     *
     * @throws GameActionException
     */
    public static void plantTree() throws GameActionException {
        if (Unit.rc.hasTreeBuildRequirements()) {
            Direction dir = Direction.getNorth();
            int counter = 0;

            while (!Unit.rc.canPlantTree(dir) && counter < 18) {
                counter++;
                dir = dir.rotateLeftDegrees(20);
            }

            if (Unit.rc.canPlantTree(dir)) {
                Unit.rc.plantTree(dir);
            }
        }
    }

    /**
     * This method builds a unit in any direction
     *
     * @param unit
     * @throws GameActionException
     */
    public static void createUnit(RobotType unit) throws GameActionException {
        if (Unit.rc.hasRobotBuildRequirements(unit)) {
            Direction dir = Direction.getNorth();
            int counter = 0;

            while (!Unit.rc.canBuildRobot(unit, dir) && counter < 18) {
                counter++;
                dir = dir.rotateLeftDegrees(20);
            }

            if (Unit.rc.canBuildRobot(unit, dir)) {
                Unit.rc.buildRobot(unit, dir);
            }
        }
    }

    /**
     * This method creates a lumberjack to clear out trees around us so we can
     * @param trees
     */
    public static boolean createLumberJackIfNeeded(TreeInfo[] trees, RobotInfo[] allies) {
        MapLocation us = Unit.rc.getLocation();

        int lumberJackCount = 0;

        for (int i = allies.length; --i>=0; ) {
            RobotInfo ally = allies[i];

            if (ally.type == RobotType.LUMBERJACK && ally.location.distanceSquaredTo(us) <= 36) {
                lumberJackCount++;
            }
        }

        if (lumberJackCount >= 2) {
            return false;
        }

        for (int i = trees.length; --i>=0; ) {
            TreeInfo tree = trees[i];

            if (tree.team == Team.NEUTRAL) {
                if (tree.location.distanceSquaredTo(us) <= 36) {
                    return true;
                }
            }
        }

        return false;
    }
    /**
     * This method returns the direction to move in to find a good tree farm spot
     * If it returns null then the current location is good
     * @param allies
     * @return
     */
    public static Direction getClosestLocForTreeFarm(RobotInfo[] allies) throws GameActionException {
        MapLocation current = Unit.rc.getLocation();
        if (willLocationWorkForTreeFarm(Unit.rc.getLocation(), allies)) {
            return null;
        }

        Direction dir = directionFromEdge(current);

        if (dir != null) {
            return dir;
        }

        return dirAwayFromGardnerCOM(allies, current);
    }

    /**
     * Returns direction to move away from wall
     *
     * @param spot
     * @return
     * @throws GameActionException
     */
    public static Direction directionFromEdge(MapLocation spot) throws GameActionException {
        if (!Unit.rc.onTheMap(spot.add(Direction.getNorth(), 5))) {
            return Direction.getSouth();
        }

        if (!Unit.rc.onTheMap(spot.add(Direction.getSouth(), 5))) {
            return Direction.getNorth();
        }

        if (!Unit.rc.onTheMap(spot.add(Direction.getEast(), 5))) {
            return Direction.getWest();
        }

        if (!Unit.rc.onTheMap(spot.add(Direction.getWest(), 5))) {
            return Direction.getEast();
        }

        return null;
    }


    /**
     * This method will point the gardner in a direction away from all the other gardners
     *
     * @param allies
     * @return
     */
    public static Direction dirAwayFromGardnerCOM(RobotInfo[] allies, MapLocation spot) {
        int x = 0;
        int y = 0;
        int gardnerCount = 0;

        for (int i = allies.length; --i>=0; ) {
            RobotInfo ally = allies[i];

            if (ally.type == RobotType.GARDENER) {
                x += ally.location.x;
                y += ally.location.y;
                gardnerCount++;
            }
        }

        if (gardnerCount <= 0) {
            return null;
        }

        MapLocation com = new MapLocation(x / gardnerCount, y / gardnerCount);

        return com.directionTo(spot);
    }


    /**
     * This method determines if a location would make a good tree farm
     *
     * @param spot
     * @param allies
     * @return
     */
    public static boolean willLocationWorkForTreeFarm(MapLocation spot, RobotInfo[] allies) {
        try {
            if (!Unit.rc.onTheMap(spot, 5)) {
                return false;
            }
        } catch (GameActionException e) {
            return false;
        }


        for (int i = allies.length; --i >=0; ) {
            RobotInfo ally = allies[i];
            // we don't want to build next to another gardner
            if (ally.type == RobotType.GARDENER && ally.location.distanceSquaredTo(spot) <= 36) {
                return false;
            }
        }

        return true;
    }

    /**
     * This method returns the corner furthest from the enemies initial com
     * and null if we haven't found the edges of the map yet
     *
     * @return
     * @throws GameActionException
     */
    public static MapLocation bestCorner() throws GameActionException {
        if (Unit.rc.readBroadcast(Constants.foundHighestMapX) < 1) return null;
        if (Unit.rc.readBroadcast(Constants.foundHighestMaxY) < 1) return null;
        if (Unit.rc.readBroadcast(Constants.foundLowestMapX) < 1) return null;
        if (Unit.rc.readBroadcast(Constants.foundLowestMapY) < 1) return null;

        int minX = Unit.rc.readBroadcast(Constants.lowestMapX);
        int minY = Unit.rc.readBroadcast(Constants.lowestMapY);

        int maxX = Unit.rc.readBroadcast(Constants.highestMapX);
        int maxY = Unit.rc.readBroadcast(Constants.highestMapY);

        MapLocation enemyCOM = com(Unit.rc.getInitialArchonLocations(Unit.rc.getTeam().opponent()));

        double distToLeftTopCorner = Math.pow((enemyCOM.x - minX), 2) + Math.pow((enemyCOM.y - maxY), 2);
        double distToTopRightCorner = Math.pow((enemyCOM.x - maxX), 2) + Math.pow((enemyCOM.y - maxY), 2);
        double distToBottomLeftCorner = Math.pow((enemyCOM.x - minX), 2) + Math.pow((enemyCOM.y - minY), 2);
        double distToBottomRightCorner = Math.pow((enemyCOM.x - maxX), 2) + Math.pow((enemyCOM.y - minY), 2);

        if (distToLeftTopCorner <= distToBottomLeftCorner && distToLeftTopCorner <= distToBottomRightCorner && distToLeftTopCorner <= distToTopRightCorner) {
            return new MapLocation(minX, maxY);
        }

        if (distToBottomLeftCorner <= distToTopRightCorner && distToBottomLeftCorner <= distToBottomRightCorner) {
            return new MapLocation(minX, minY);
        }

        if (distToTopRightCorner <= distToBottomRightCorner) {
            return new MapLocation(maxX, maxY);
        }

        return new MapLocation(maxX, minY);
    }

    public static Direction closestDefenselessEnemy(RobotInfo[] enemies) {
        Direction dir = null;
        float dist = Float.MAX_VALUE;


        for (int i = enemies.length; --i>=0; ) {
            RobotType type = enemies[i].type;

            if (type == RobotType.ARCHON || type == RobotType.GARDENER) {
                float currentDist = enemies[i].location.distanceSquaredTo(Unit.rc.getLocation());

                if (currentDist < dist) {
                    dir = Unit.rc.getLocation().directionTo(enemies[i].location);
                    dist = currentDist;
                }
            }
        }

        return dir;
    }

    /**
     * This method moves scouts toward their destination while avoiding enemy units and Bullets
     *
     * @param direction
     * @throws GameActionException
     */
    public static void scoutMove(Direction direction, BulletInfo[] bullets, RobotInfo[] enemies) throws GameActionException {
        Direction toMove = direction;
        float degreesToMove = 15;
        int counter = 0;

        float leastDamage = Float.MAX_VALUE;
        Direction bestDir = null;

        if (bullets.length > 0 || enemies.length > 0) {
            for (int i = 0; i < 8; i++) {
                float degrees = (float)Math.PI * i / 4;
                Direction dir = new Direction(degrees);

                if (!Unit.rc.canMove(dir)) continue;

                MapLocation next = Unit.rc.getLocation().add(dir, Unit.myType.strideRadius);

                float damage = PotentialDamageFromLocation(next, bullets, enemies, 1);

                if (damage > 0) {
                    Unit.rc.setIndicatorLine(Unit.rc.getLocation(), next, 255, 0 ,0);
                } else {
                    Unit.rc.setIndicatorLine(Unit.rc.getLocation(), next, 0, 255, 0);
                }

                if (damage < leastDamage) {
                    leastDamage = damage;
                    bestDir = dir;
                } else if (damage == leastDamage && Math.abs(bestDir.getAngleDegrees() - direction.getAngleDegrees()) > Math.abs(dir.getAngleDegrees() - direction.getAngleDegrees())) {
                    bestDir = dir;
                }
            }
        }

        if (bestDir != null) {
            if (Unit.rc.canMove(bestDir)) {
                Unit.rc.move(bestDir);
                return;
            }
        }

        while (!Unit.rc.canMove(toMove) && degreesToMove < 180) {
            if (counter % 2 == 0) {
                toMove = direction.rotateRightDegrees(degreesToMove);
            } else {
                toMove = direction.rotateLeftDegrees(degreesToMove);
                degreesToMove += 15;
            }

            counter++;
        }

        if (Unit.rc.canMove(toMove)) {
            Unit.rc.move(toMove);
        }
    }

    /**
     * This method scans for the edges of the maps
     *
     * @return
     * @throws GameActionException
     */
    public static boolean scanForMapEdges() throws GameActionException {
        boolean scan = false;
        Direction dir;
        MapLocation spot;

        if (Unit.rc.readBroadcast(Constants.lowestMapX) == 0) {
            scan = true;
            dir = new Direction((float)Math.PI);
            spot = Unit.rc.getLocation().add(dir, Unit.rc.getType().sensorRadius - 1);

            if (!Unit.rc.onTheMap(spot)) {
                Unit.rc.broadcast(Constants.foundLowestMapX, 1);
                Unit.rc.broadcast(Constants.lowestMapX, (int) spot.add(dir).x);
            }
        }

        if (Unit.rc.readBroadcast(Constants.highestMapX) == 0) {
            scan = true;
            dir = new Direction(0);
            spot = Unit.rc.getLocation().add(dir, Unit.rc.getType().sensorRadius - 1);

            if (!Unit.rc.onTheMap(spot)) {
                Unit.rc.broadcast(Constants.highestMapX, (int) spot.add(dir).x);
                Unit.rc.broadcast(Constants.foundHighestMapX, 1);

            }
        }

        if (Unit.rc.readBroadcast(Constants.lowestMapY) == 0) {
            scan = true;
            dir = new Direction((float)Math.PI * 3 / 2);
            spot = Unit.rc.getLocation().add(dir, Unit.rc.getType().sensorRadius - 1);

            if (!Unit.rc.onTheMap(spot)) {
                Unit.rc.broadcast(Constants.lowestMapY, (int) spot.add(dir).y);
                Unit.rc.broadcast(Constants.foundLowestMapY, 1);
            }
        }

        if (Unit.rc.readBroadcast(Constants.highestMapY) == 0) {
            scan = true;
            dir = new Direction((float)Math.PI / 2);
            spot = Unit.rc.getLocation().add(dir, Unit.rc.getType().sensorRadius - 1);

            if (!Unit.rc.onTheMap(spot)) {
                Unit.rc.broadcast(Constants.highestMapY, (int) spot.add(dir).y);
                Unit.rc.broadcast(Constants.foundHighestMaxY, 1);
            }
        }


        return scan;
    }


    /**
     * This method seeks to shoot at the enemy with the least health so that it won't hit an allied unit
     *
     * @param enemies
     * @param allies
     * @throws GameActionException
     */
    public static void shootAtEnemies(RobotInfo[] enemies, RobotInfo[] allies) throws GameActionException {
        Direction target = null;
        MapLocation us = Unit.rc.getLocation();
        float closestEnemy = Float.MAX_VALUE;


        for (int i = enemies.length; --i>=0; ) {
            RobotInfo enemy = enemies[i];

            float currentDist = enemy.location.distanceSquaredTo(us);

            if (currentDist < closestEnemy) {
                boolean friendlyFire = false;
                Direction targetDir = us.directionTo(enemy.location);

                for (int j = allies.length; --j>=0; ) {
                    if (turnBulletWillHitLocation(us, allies[j].location, 1, targetDir, (int) allies[j].getType().bodyRadius) >= 0) {
                        friendlyFire = true;
                        break;
                    }
                }

                if (!friendlyFire) {
                    closestEnemy = currentDist;
                    target = targetDir;
                }
            }
        }

        if (target != null) {
            if (Unit.rc.canFireSingleShot()) {
                Unit.rc.fireSingleShot(target);
            }
        }
    }

    public static float PotentialDamageFromLocation(MapLocation loc, BulletInfo[] bullets, RobotInfo[] enemies, int radius) throws GameActionException {
        float damage = 0;

        for (int j = 0; j < bullets.length; j++) {
            BulletInfo bullet = bullets[j];
            float velocity = bullet.getSpeed();
            Direction bulletDir = bullet.getDir();

            int turn = Util.turnBulletWillHitLocation(bullet.location, loc, velocity, bulletDir, radius);

            if (turn >= 0 && turn < 2) {
                damage += bullet.damage;
            }
        }

        for (int i = enemies.length; --i>=0; ) {
            RobotInfo enemy = enemies[i];

            if (enemy.type == RobotType.LUMBERJACK) {
                float dist = enemy.getRadius() + GameConstants.LUMBERJACK_STRIKE_RADIUS + enemy.type.strideRadius + radius;
                if (loc.distanceSquaredTo(enemy.location) <= (dist * dist)) {
                    damage += enemy.type.attackPower;
                    Unit.rc.setIndicatorDot(enemy.location, 255, 0, 0);
                } else {
                    Unit.rc.setIndicatorDot(enemy.location, 0, 255, 0);
                }
            } else if (enemy.type == RobotType.SCOUT || enemy.type == RobotType.SOLDIER || enemy.type == RobotType.TANK) {
                float dist = enemy.getRadius() + enemy.type.bulletSpeed + enemy.type.strideRadius + radius;
                if (loc.distanceSquaredTo(enemy.location) <= (dist * dist)) {
                    damage += enemy.type.attackPower;
                }
            }
        }

        return damage;
    }

    /**
     * This method finds the furthest corner of a list of map locations from a point
     *
     * @param current
     * @param locs
     * @return
     */
    public static MapLocation furthestCorner(MapLocation current,MapLocation[] locs) {
        float x = 0;
        float y = 0;

        for (int i = locs.length; --i>=0; ) {
            if (Math.abs(locs[i].x - current.x) > x) {
                x = locs[i].x;
            }

            if (Math.abs(locs[i].y - current.y) > y) {
                y = locs[i].y;
            }
        }

        return new MapLocation(x, y);
    }

    public static MapLocation combinedCOM(MapLocation[] locs1, MapLocation[] locs2) {
        float x = 0;
        float y = 0;

        for (int i = locs1.length; --i >=0; ) {
            x += locs1[i].x;
            y += locs1[i].y;
        }

        for (int i = locs2.length; --i >=0; ) {
            x += locs2[i].x;
            y += locs2[i].y;
        }

        x /= (locs1.length + locs2.length);
        y /= (locs1.length + locs2.length);

        return new MapLocation(x,y);
    }

    /**
     * This method finds the com of some units
     *
     * @param bots
     * @return
     */
    public static MapLocation unitCOM(RobotInfo[] bots) {
        float x = 0;
        float y = 0;

        for (int i = bots.length; --i >=0; ) {
            x += bots[i].location.x;
            y += bots[i].location.y;
        }

        x /= bots.length;
        y /= bots.length;

        return new MapLocation(x,y);
    }

    /**
     * This method gets the center of mass of a list of map locations
     *
     * @param locs
     * @return
     */
    public static MapLocation com(MapLocation[] locs) {
        float x = 0;
        float y = 0;

        for (int i = locs.length; --i >=0; ) {
            x += locs[i].x;
            y += locs[i].y;
        }

        x /= locs.length;
        y /= locs.length;

        return new MapLocation(x,y);
    }

    /**
     * This method calculates the distance of a point from a line
     *
     * @param line1
     * @param line2
     * @param point
     * @return
     */
    public static float distFromPointToLine(MapLocation line1, MapLocation line2, MapLocation point) {
        float y1 = line1.y;
        float y2 = line2.y;
        float y3 = point.y;
        float x1 = line1.x;
        float x2 = line2.x;
        float x3 = point.x;

        float dist = Math.abs((y2 - y1) * x3 - (x2 - x1) * y3 + x2 * y1 - y2 * x1);
        dist /= Math.sqrt(Math.pow((y2 - y1), 2) + Math.pow((x2 - x1), 2));
        return dist;
    }

    /**
     * This function calculates if a unit will "run over" a bullet
     *
     * @param target
     * @param bulletSpot
     * @param radius
     * @return
     */
    public static int willUnitPathGoOverBullet(MapLocation target, MapLocation bulletSpot, int radius) {
        if (target.distanceSquaredTo(bulletSpot) > Unit.myType.strideRadius) return -1;

        MapLocation next = target.add(Unit.rc.getLocation().directionTo(target));
        float dist = distFromPointToLine(bulletSpot, next, target);

        if (dist > radius) {
            return -1;
        }

        return 1;
    }

    /**
     * This method determines which round a bullet will hit a unit if it moves to the target location
     * and stops there
     *
     * It returns a -1 if the bullet will not hit that location
     *
     * @param bulletSpot
     * @param target
     * @param speed
     * @param direction
     * @param radius
     * @return
     */
    public static int turnBulletWillHitLocation(MapLocation bulletSpot, MapLocation target, float speed, Direction direction, int radius) {
        if (bulletSpot.distanceSquaredTo(target) <= radius) return 1;

        float degrees = target.directionTo(bulletSpot).getAngleDegrees();

        if (Math.abs(degrees - direction.getAngleDegrees()) < 180) {
            return willUnitPathGoOverBullet(target, bulletSpot, radius);
        }

        MapLocation next = bulletSpot.add(direction, 5);

        float dist = distFromPointToLine(bulletSpot, next, target);

        if (dist > radius) {
            return -1;
        }

        return (int) (dist / speed);
    }

    /**
     * Causes a unit to water any trees around it that need it
     *
     * @throws GameActionException
     */
    public static void waterTrees() throws GameActionException{
        TreeInfo[] trees = Unit.rc.senseNearbyTrees();
        for (int i = trees.length; --i >=0; ) {
            TreeInfo tree = trees[i];


            if (tree.getTeam() == Unit.rc.getTeam()) {
                if (tree.getHealth() < 40) {
                    if (Unit.rc.canWater(tree.getID())) {
                        Unit.rc.water(tree.getID());
                    } else {
                        System.out.println("cant water tree");
                    }
                }
            }
        }
    }

    public static void moveToWaterTrees() throws GameActionException {
        TreeInfo[] trees = Unit.rc.senseNearbyTrees();

        float weakestTreeHealth = 40;
        TreeInfo weakestTree = null;

        for (int i = trees.length; --i >=0; ) {
            TreeInfo tree = trees[i];

            if (tree.getTeam() == Unit.rc.getTeam()) {// && Unit.rc.getLocation().distanceSquaredTo(tree.location) < 5) {
                if (tree.getHealth() < weakestTreeHealth) {
                    weakestTree = tree;
                    weakestTreeHealth = tree.getHealth();
                }
            }
        }

        if (weakestTree != null) {
            System.out.println("moving towards weakest tree");
            if (Unit.rc.canWater(weakestTree.getID())) {
                Unit.rc.water(weakestTree.getID());
            } else {
                tryMove(Unit.rc.getLocation().directionTo(weakestTree.location));
            }
        }
    }

    /**
     * Returns a random Direction
     * @return a random Direction
     */
    static Direction randomDirection() {
        return new Direction((float)Math.random() * 2 * (float)Math.PI);
    }

    /**
     * Returns a direction
     */
    static Direction getDirectionForInt(int dir) {
        return new Direction((float)dir / 8 * 2 * (float)Math.PI);
    }

    /**
     * Attempts to move in a given direction, while avoiding small obstacles directly in the path.
     *
     * @param dir The intended direction of movement
     * @return true if a move was performed
     * @throws GameActionException
     */
    static boolean tryMove(Direction dir) throws GameActionException {
        return tryMove(dir,20,9);
    }

    /**
     * Attempts to move in a given direction, while avoiding small obstacles direction in the path.
     *
     * @param dir The intended direction of movement
     * @param degreeOffset Spacing between checked directions (degrees)
     * @param checksPerSide Number of extra directions checked on each side, if intended direction was unavailable
     * @return true if a move was performed
     * @throws GameActionException
     */
    static boolean tryMove(Direction dir, float degreeOffset, int checksPerSide) throws GameActionException {

        // First, try intended direction
        if (Unit.rc.canMove(dir)) {
            Unit.rc.move(dir);
            return true;
        }

        // Now try a bunch of similar angles
        boolean moved = false;
        int currentCheck = 1;

        while(currentCheck<=checksPerSide) {
            // Try the offset of the left side
            if(Unit.rc.canMove(dir.rotateLeftDegrees(degreeOffset*currentCheck))) {
                Unit.rc.move(dir.rotateLeftDegrees(degreeOffset*currentCheck));
                return true;
            }
            // Try the offset on the right side
            if(Unit.rc.canMove(dir.rotateRightDegrees(degreeOffset*currentCheck))) {
                Unit.rc.move(dir.rotateRightDegrees(degreeOffset*currentCheck));
                return true;
            }
            // No move performed, try slightly further
            currentCheck++;
        }

        // A move never happened, so return false.
        return false;
    }

    /**
     * A slightly more complicated example function, this returns true if the given bullet is on a collision
     * course with the current robot. Doesn't take into account objects between the bullet and this robot.
     *
     * @param bullet The bullet in question
     * @return True if the line of the bullet's path intersects with this robot's current position.
     */
    static boolean willCollideWithMe(BulletInfo bullet) {
        MapLocation myLocation = Unit.rc.getLocation();

        // Get relevant bullet information
        Direction propagationDirection = bullet.dir;
        MapLocation bulletLocation = bullet.location;

        // Calculate bullet relations to this robot
        Direction directionToRobot = bulletLocation.directionTo(myLocation);
        float distToRobot = bulletLocation.distanceTo(myLocation);
        float theta = propagationDirection.radiansBetween(directionToRobot);

        // If theta > 90 degrees, then the bullet is traveling away from us and we can break early
        if (Math.abs(theta) > Math.PI/2) {
            return false;
        }

        // distToRobot is our hypotenuse, theta is our angle, and we want to know this length of the opposite leg.
        // This is the distance of a line that goes from myLocation and intersects perpendicularly with propagationDirection.
        // This corresponds to the smallest radius circle centered at our location that would intersect with the
        // line that is the path of the bullet.
        float perpendicularDist = (float)Math.abs(distToRobot * Math.sin(theta)); // soh cah toa :)

        return (perpendicularDist <= Unit.rc.getType().bodyRadius);
    }
}
