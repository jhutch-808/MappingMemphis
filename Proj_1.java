

import java.io.InputStream;
import java.util.*;
import java.io.*;

public class Proj_1 {
    private static RoadNetwork graph;
    public static int nodes_visited = 0;

    public static boolean info= false;

    public static int user_speed_amt = 0;


    public static void main(String[] args){
        Scanner scan = new Scanner(System.in);
        graph = readGraph("memphis-medium.txt");

        System.out.print("Enter a location ID: ");
        Long st_locId = Long.parseLong(scan.nextLine());

        System.out.print("Enter ending location Id: ");
        Long end_locId = Long.parseLong(scan.nextLine());

        System.out.print("How many times are you allowed to speed?: ");
        user_speed_amt = Integer.parseInt(scan.nextLine());

        System.out.print("Do you want debugging info (y/n)? ");
        String y_n = scan.nextLine();
        if(y_n.contains("y")){info = true;}

        System.out.println(" ");

        Node goal_node = A_search(st_locId, end_locId, user_speed_amt);
        System.out.println(" ");

        ArrayList<Node> orderedlist = print_info(goal_node, user_speed_amt);
        System.out.println(" ");

        if (user_speed_amt ==0){
            print_gps(orderedlist);
        }

    }

    /*
    prints the nodes that it has travelled to
     */
    public static ArrayList<Node> print_info(Node goal_node, int speed_amt){

        System.out.println("Total time travelled: " + goal_node.getG_val());
        System.out.println("Number of nodes visited: " + nodes_visited);

        //printing the shortest path
        System.out.print(" Route Found is : \n");
        ArrayList<Node> ordered_path = new ArrayList<>();
        ordered_path.add(goal_node);
        Node curr = goal_node.getParent();
        while(curr!= null){
            ordered_path.add(curr);
            curr = curr.getParent();
        }
        Collections.reverse(ordered_path);

        for( Node node: ordered_path){
            String is_speed;
            if(node.getisSpeeding()==null || speed_amt ==0 ){is_speed = "";}
            else if(node.getisSpeeding()){is_speed = ", speeding";}
            else{is_speed =", not speeding";}

            System.out.println( " "+ node.getState().getId() + " (" + node.getState().getRoad_name() + is_speed + ")");
        }
        return ordered_path;
    }

    /*
    printing GPS directions
     */
    public static void print_gps(ArrayList<Node> ordered_path){
        System.out.print("GPS directions: \n");

        double total_distance = 0.0;
        double total_time = 0.0;

        // printing out the first one -> the N W E S stuff
        Location start_loc = graph.getLocationForId(ordered_path.getFirst().getState().getId());
        Location next_loc = graph.getLocationForId(ordered_path.get(1).getState().getId());
        String next_road_name = ordered_path.get(1).getState().getRoad_name();
        String direction = Geometry.get_cardinal_directions(start_loc,next_loc);
        System.out.printf("Head [%s] on %s\n", direction,next_road_name);

        for (int i = 0; i < ordered_path.size()-1; i++) {
            Node curr_node = ordered_path.get(i);
            String road_n = curr_node.getState().getRoad_name();
            String next_road_n = ordered_path.get(i+1).getState().getRoad_name();

            //calculating the distance between curr node and next node
            double distance = Geometry.getDistanceInMile(curr_node, ordered_path.get(i + 1), graph);
            double  time = Geometry.getDriveTimeFromNodes(curr_node, ordered_path.get(i + 1), graph);

            // If the road hasn't changed or if its the starting, sum up distance and time
            if ( next_road_n.equals(road_n) || (i ==0) ){
                total_distance += distance;
                total_time += time;
            }

            //road has changed
            else{

                // Print the accumulated distance and time for the previous road
                System.out.printf("  Drive for %.2f miles (%.2f seconds)\n", total_distance, total_time);

                //getting the prev node and next node to figure out left or right
                Node next = ordered_path.get(i + 1);
                Node prev_node = ordered_path.get(i-1);
                String turn_direction = Geometry.getTurnDirection(prev_node, curr_node, next, graph);
                System.out.printf("Turn [%s] onto %s\n", turn_direction, next.getState().getRoad_name());

                // Reset distance and time for the next new road segment
                total_distance = distance;
                total_time = time;

            }

        }

        // After the loop, print the final accumulated distance and time for the last road segment
        System.out.printf("  Out: Drive for %.2f miles (%.2f seconds)\n", total_distance, total_time);
        System.out.println("You have arrived!");
    }


    public static Node A_search(long start_id, long end_id, int speed_amt){
        int speed_limit = 65;
        if (speed_amt>0){
            speed_limit = 130;
        }

        Double h_cost = calc_h_cost(start_id, end_id, speed_limit); // calculating new nodes h_cost
        State newState = new State(start_id, speed_amt, "Starting Location");
        Node newNode = new Node(newState, null, null, h_cost,0.0); // creating the start node
        //Key, val: <node, f(n)>
        Priority_Q<Node,Double> frontier = new Priority_Q<>();
        frontier.add(newNode, newNode.getF_val());

        // initial-state, node: however because this is simple we are going to just have it as
        //  <loc_id, f(n)>      initial state is essentially location id to f(n) rather than node
        Map<State, Node> reached_map = new HashMap<>();
        reached_map.put(newState, newNode); // adding an entry

        while(!frontier.isEmpty()){
            Node node = frontier.remove();

            //formatting info for parent node state (need to handle certain exceptions)
            String p_state;
            if (node.getParent() == null){p_state = "null";}
            else{p_state = Long.toString(node.getParent().getState().getId());}

            String speed_state;
            if(node.getisSpeeding() ==null){speed_state = "null";}
            else{speed_state = Boolean.toString(node.getisSpeeding());}

            // <----printing out debugging info------------->v
            if(info && (speed_amt>0)){
                System.out.printf("Visiting [State = (%d , %d), parent = %s, speeding = %s, g=%f, h= %f , f = %f] \n",
                        node.getState().getId(), node.getState().getNum_speed(),p_state, speed_state,node.getG_val(),
                        node.getH_val(),node.getF_val());
            }
            else if(info && (speed_amt ==0)){
                System.out.printf("Visiting [State = %d , parent = %s , g=%f, h= %f , f = %f] \n", node.getState().getId(),p_state,node.getG_val(),node.getH_val(),node.getF_val());

            }
            // <------------------------------------->^

            nodes_visited+=1;

            if (node.getState().getId() == end_id) {
                return node;
            }
            // Now that you have the highest priority node you will expand that node to see what the next options are
            ArrayList<Node> child_nodes = expand(node, end_id);

            // check each node and see if we should add it to the frontier aka the priority_q or skip it cuz the f(n) is to high
            for(Node child_node: child_nodes){
                State child_state = child_node.getState();
                if ( (!reached_map.containsKey(child_state)) || (child_node.getF_val()< reached_map.get(child_state).getF_val())){

                    reached_map.put(child_state,child_node);
                    frontier.add(child_node, child_node.getF_val());
                    // <------ debugging info ----------------->v
                    if(info && (speed_amt>0)){
                        System.out.printf("     Adding[State = (%d, %d) , parent = %d, speeding = %b, g=%f, h= %f , f = %f] to frontier \n",
                                child_node.getState().getId(), child_node.getState().getNum_speed(), child_node.getParent().getState().getId(),
                                child_node.getisSpeeding(),child_node.getG_val(),child_node.getH_val(),child_node.getF_val() );
                    }
                    else if(info){
                        System.out.printf("     Adding[State = %d , parent = %d g=%f, h= %f , f = %f] to frontier \n",
                                child_node.getState().getId(), child_node.getParent().getState().getId(),
                                child_node.getG_val(),child_node.getH_val(),child_node.getF_val() );
                    }
                    // <------------------------------------------->^
                }

                // <---- printing out debugging info --------------------------------->v
                else {
                    if (info && (speed_amt>0)){
                        System.out.printf("     Skipping [State = (%d, %d), parent = %s, speeding = %b, g=%f, h= %f , f = %f] (already on frontier with lower cost).\n",
                                child_node.getState().getId(),child_node.getState().getNum_speed(), child_node.getParent().getState().getId(),child_node.getisSpeeding(),
                                child_node.getG_val(),child_node.getH_val(),child_node.getF_val() );
                    }
                    else if(info && (speed_amt ==0)){
                        System.out.printf("     Skipping [State = %d , parent = %d g=%f, h= %f , f = %f] (already on frontier with lower cost).\n",
                                child_node.getState().getId(), child_node.getParent().getState().getId(),
                                child_node.getG_val(),child_node.getH_val(),child_node.getF_val() );
                    }
                }
                // <-------------------------------------------------------------------->^

            }
            if(info){System.out.println(" ");}
        }
        return newNode;
    }

    /*
    A node gets passed in and it checks for every possibility aka the child noedes. It returns a collection of child nodes
     */
    public static ArrayList<Node> expand(Node parent_node, long end_state){
        ArrayList<Node> child_node_collection = new ArrayList<>();
        Long id = parent_node.getState().getId();
        List<Road> roads = graph.getAdjacentRoads(id);
        for (Road road: roads){
            long child_state_id = road.endId();
            double time_sec = Geometry.getDriveTimeInSeconds(road, graph);
            int remaining_speed = parent_node.getState().getNum_speed();
            //If not speeding
            if(user_speed_amt ==0){
                create_add_ChildNode(child_node_collection, parent_node,road,child_state_id,end_state,time_sec,remaining_speed,false);
            }
            //User used speeding option so will explore each state: speed or not speed
            else{

                create_add_ChildNode(child_node_collection, parent_node,road,child_state_id,end_state,time_sec,remaining_speed,false);
                // user's state is still able to speed so will check that option...
                if(remaining_speed>0){
                    double time_sec_no_speed= time_sec/2;
                    create_add_ChildNode(child_node_collection, parent_node,road,child_state_id,end_state,time_sec_no_speed,remaining_speed-1,true);
                }
            }
        }
        return child_node_collection;
    }

    /*
     helper function for expansion. This function creates and adds a node the collection of child nodes.
     */
    private static void create_add_ChildNode(ArrayList<Node> collection, Node parent_node, Road road,
                                             long child_state_id, long end_state, double time_sec,
                                             int remaining_speed, boolean isSpeeding){
        double h_cost;
        if(user_speed_amt!=0){
            if(remaining_speed!=0){
                if(isSpeeding){h_cost = calc_h_cost(child_state_id, end_state, 65);}
                else{h_cost = calc_h_cost(child_state_id, end_state, 65*2);}
            }
            // if the user runs out of speed then go back to normal heuristic
            else{h_cost = calc_h_cost(child_state_id, end_state, 65);}
        }

        // Normal Heuristic to use if user doesn't choose to speed
        else{h_cost = calc_h_cost(child_state_id, end_state, 65);}

        double g_val = time_sec + parent_node.getG_val();

        //creating the state and node
        State child_state = new State(child_state_id, remaining_speed, road.name());
        Node child = new Node(child_state, parent_node, isSpeeding, h_cost,g_val);

        collection.add(child);
    }

    /*
    Calculates the heauristic function by getting the drive time between two locations at the fastest possible time
     */
    public static Double calc_h_cost(long s, long e, int speed_limit){
        Location s_loc = graph.getLocationForId(s);
        Location e_loc = graph.getLocationForId(e);
        return Geometry.getDriveTimeInSeconds(s_loc.latitude(),s_loc.longitude(), e_loc.latitude(),e_loc.longitude(), speed_limit);
    }

    /*
    This function reads in the data from a file and creates a road netowrk comprised of roads and locations
    Returns a Roadnetwork called graph
    NOTE: Skeleton code that was given to use
     */
    public static RoadNetwork readGraph(String filename){
        InputStream is = Proj_1.class.getResourceAsStream(filename);
        if (is == null){
            System.err.println("Bad file name: "+ filename);
            System.exit(1);
        }

        Scanner scan = new Scanner(is);
        RoadNetwork graph = new RoadNetwork();

        while (scan.hasNextLine()) {
            String line = scan.nextLine();
            String[] pieces = line.split("\\|");

            if (pieces[0].equals("location")) {
                long id = Long.parseLong(pieces[1]);
                double lat = Double.parseDouble(pieces[2]);
                double longi = Double.parseDouble(pieces[3]);
                Location loc = new Location(id, lat, longi);
                graph.addLocation(loc);
            } else if (pieces[0].equals("road")) {
                long startId = Long.parseLong(pieces[1]);
                long endId = Long.parseLong(pieces[2]);
                int speed = Integer.parseInt(pieces[3]);
                String name = pieces[4];
                Road r1 = new Road(startId, endId, speed, name);
                Road r2 = new Road(endId, startId, speed, name);
                graph.addRoad(r1);
                graph.addRoad(r2);
            }
        }
        scan.close();

        return graph;

    }
}
