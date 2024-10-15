//Node will contain info about the state,
// parent pointer: the node that generated this node)
// f(n) g(n) and h(n) values (path cost, heuristic, the edge weight)


/*
These are notes for myself
h(n)  = get the lat and long of the curr node and end location. Get the distance
        between the 2 and calculate the time it'll take going at 65mph.
        this will the heuristic value of that node. Because you don't know
        what it'll look like in the future you will have to calculate it each time
        you are checking the next node. ( to check the h(n) val should drop as you are
        getting closer to the end location)
g(n)  = the edge cost which will be the actual time from the curr node to the next node.
        this will just do the normal distant calculations between the two nodes, and then
        calculating the time it'll take to cross that distance using the actual MPH
 */


public class Node {

    //state will just be the location ID
    private final State state;

    private Node parent;
    private Boolean isSpeeding;

    //h(n) val
    private Double h_val; // heuristic val
    private Double g_val; //  total path cost
    private Double f_val; // G + N

    public Node(State state, Node parent, Boolean isSpeeding, Double h_val, Double g_val){
        this.state = state;
        this.parent = parent;
        this.isSpeeding = isSpeeding;
        this.h_val = h_val; // heauristic
        this.g_val = g_val; // path cost
        this.f_val = g_val + h_val;

    }

    public Double getH_val(){
        return h_val;
    }

    public Double getG_val() {
        return g_val;
    }

    public void set_gVal(Double val){
        this.g_val = val;
    }

    public Double getF_val() {
        return f_val;
    }

    public Node getParent() {
        return parent;
    }

    public Boolean getisSpeeding() {
        return isSpeeding;
    }

    public State getState(){
        return state;
    }
}
