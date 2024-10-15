import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class RoadNetwork {
    private Map<Long, Location> locations;
    private Map<Long, List<Road>> roads;

    public RoadNetwork() {
        locations = new HashMap<>();
        roads = new HashMap<>();
    }
    public Map get_locations_map(){
        return locations;
    }

    public void addLocation(Location loc) {
        locations.put(loc.id(), loc);
        roads.put(loc.id(), new ArrayList<>());
    }

    public void addRoad(Road road) {
        roads.get(road.startId()).add(road);
    }

    public Location getLocationForId(long id) {
        Location loc = locations.get(id);
        if (loc == null) throw new IllegalArgumentException("Location " + id + " doesn't exist in graph.");
        return loc;
    }

    public List<Road> getAdjacentRoads(Location loc) {
        List<Road> r = roads.get(loc.id());
        if (r == null) throw new IllegalArgumentException("Location " + loc + " doesn't exist in graph.");
        return r;
    }

    public List<Road> getAdjacentRoads(long id) {
        return getAdjacentRoads(getLocationForId(id));
    }
}
