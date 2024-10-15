
import java.util.List;

public class Geometry {
    public static double getDistanceInMiles(double lat1, double long1, double lat2, double long2) {

        if (lat1 == lat2 && long1 == long2) return 0;

        /* Convert latitude and longitude to
        # spherical coordinates in radians. */
        double degrees_to_radians = Math.PI / 180.0;

        // phi = 90 - latitude
        double phi1 = (90.0 - lat1) * degrees_to_radians;
        double phi2 = (90.0 - lat2) * degrees_to_radians;

        // theta = longitude
        double theta1 = long1 * degrees_to_radians;
        double theta2 = long2 * degrees_to_radians;

        // Compute spherical distance from spherical coordinates.

        /* For two locations in spherical coordinates
        # (1, theta, phi) and (1, theta, phi)
        # cosine( arc length ) =
        #    sin phi sin phi' cos(theta-theta') + cos phi cos phi'
        # distance = rho * arc length */

        double cos = (Math.sin(phi1) * Math.sin(phi2) * Math.cos(theta1 - theta2) +
                Math.cos(phi1) * Math.cos(phi2));

        double arc = Math.acos(cos);

        /* Remember to multiply arc by the radius of the earth
        # in your favorite set of units to get length. */
        return arc * 3960;
    }
    public static double getDistanceInMile(Node start_n, Node end_n, RoadNetwork graph){
        Location start_l = graph.getLocationForId(start_n.getState().getId());
        Location end_l= graph.getLocationForId(end_n.getState().getId());

        double s_lat = start_l.latitude();
        double s_lon = start_l.longitude();

        double e_lat = end_l.latitude();
        double e_lon = end_l.longitude();

        return getDistanceInMiles(s_lat,s_lon,e_lat,e_lon);
    }

    public static double getDriveTimeFromNodes(Node start_n, Node end_n, RoadNetwork graph){
        Location start_l = graph.getLocationForId(start_n.getState().getId());
        Location end_l= graph.getLocationForId(end_n.getState().getId());

        double s_lat = start_l.latitude();
        double s_lon = start_l.longitude();

        double e_lat = end_l.latitude();
        double e_lon = end_l.longitude();

        int speed = 65;

        List<Road> roads = graph.getAdjacentRoads(start_n.getState().getId());
        for (Road road : roads) {
            if (road.endId() == end_n.getState().getId()) {
                speed = road.speedLimit();
                break;
            }
        }

        return getDriveTimeInSeconds(s_lat,s_lon,e_lat,e_lon, speed);
    }

    public static double getDriveTimeInSeconds(double lat1, double long1, double lat2, double long2, int speedLimit)
    {
        return getDistanceInMiles(lat1, long1, lat2, long2) / speedLimit * 60 * 60;
    }

    public static double getDistanceInMiles(Road road, RoadNetwork graph)
    {
        Location start = graph.getLocationForId(road.startId());
        Location end = graph.getLocationForId(road.endId());
        return getDistanceInMiles(start.latitude(), start.longitude(), end.latitude(), end.longitude());
    }

    public static double getDriveTimeInSeconds(Road road, RoadNetwork graph)
    {
        Location start = graph.getLocationForId(road.startId());
        Location end = graph.getLocationForId(road.endId());

        return getDriveTimeInSeconds(start.latitude(), start.longitude(), end.latitude(), end.longitude(), road.speedLimit());
    }


    public static String get_cardinal_directions(Location start, Location end){
        double lat_diff = end.latitude() - start.latitude(); // determins North or south
        double lon_diff = end.longitude() - start.longitude(); // determins east or west

        String nsDirection = "";
        String ewDirection = "";

        if(lat_diff >0){nsDirection = "north";}
        else if( lat_diff<0){nsDirection = "south";}

        if(lon_diff >0){ ewDirection = "east";}
        else if (lon_diff <0 ){ ewDirection = "west";}

        if (!nsDirection.isEmpty() && !ewDirection.isEmpty()) {
            if(Math.abs(lat_diff)> Math.abs(lon_diff)){
                return nsDirection;
            }
            else{
                return ewDirection;
            }
        }
        return " ";
    }

    /*
    Useing vetor math to see what is left and right
    I had to use this page to figure it out:
    https://en.wikipedia.org/wiki/Cross_product
     */
    private static String getTurnDirection(Location loc_1, Location loc_2, Location loc_3){
        double v1x = loc_2.longitude() - loc_1.longitude();
        double v1y = loc_2.latitude() - loc_1.latitude();

        double v2x = loc_3.longitude() - loc_2.longitude();
        double v2y = loc_3.latitude()- loc_2.latitude();

        double product = v1x * v2y - v1y *v2x;
        if (product > 0){
            return "left";
        }
        else if(product<0){
            return "right";
        }
        else{return " ";}
    }
    /*
    helper function to the real getTurndirection code that will take in nodes instead of the
    individual locations of each val
     */
    public static String getTurnDirection(Node prev_n, Node curr_n, Node next_n, RoadNetwork graph) {
        Location prev_l = graph.getLocationForId(prev_n.getState().getId());
        Location cur_l = graph.getLocationForId(curr_n.getState().getId());
        Location next_l = graph.getLocationForId(next_n.getState().getId());


        return getTurnDirection(prev_l,cur_l, next_l);
    }




}
