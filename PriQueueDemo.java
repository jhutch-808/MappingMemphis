// Demo of using the PriQueue class.

public class PriQueueDemo
{
    public static void main(String[] args) {
        Priority_Q<String, Integer> q = new Priority_Q<>();
        q.add("is", 5);
        q.add("computer", 3);
        q.add("rhodes", 1);
        q.add("awesome", 6);
        q.add("college", 2);
        q.add("science", 4);

        while (!q.isEmpty()) {
            String s = q.remove();
            System.out.println("Dequeued: " + s);
        }
    }
}
