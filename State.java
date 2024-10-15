
import java.util.Objects;

public class State {
    private final long id;
    private int num_speed;

    private final String road_name;

    public State (long id, int num_speed, String road_name){
        this.id = id;
        this.num_speed = num_speed;
        this.road_name = road_name;
    }

    public long getId() {
        return id;
    }

    public int getNum_speed() {
        return num_speed;
    }

    public String getRoad_name(){
        return road_name;
    }

    public void setNum_speed(int num_speed) {
        this.num_speed = num_speed;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        State state = (State) o;
        return id == state.id && num_speed == state.num_speed;
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, num_speed);
    }
}
