import java.util.Comparator;
import java.util.PriorityQueue;

/**
 * A priority queue class that supports both a min-priority-queue and a max-priority queue.
 * Min-queues prioritize lower numbers, and Max-queues prioritize higher numbers.
 * The priority queue takes two class templates: E, which is the type of objects stored in the queue,
 * and P, which is the type of priority, which must be Comparable (Integer, Double, etc).
 */
public class Priority_Q<E, P extends Comparable<P>> {
    private final PriorityQueue<PQItem<E, P>> queue;

    /**
     * Create a new min-priority queue.
     */
    public Priority_Q()
    {
        queue = new PriorityQueue<PQItem<E, P>>(Comparator.naturalOrder());
    }

    /**
     * Create a new priority queue.  Specify the parameter "true" for a min-queue,
     * and "false" for a max-queue.
     */
    public Priority_Q(boolean isMinPQ)
    {
        if (isMinPQ)
            queue = new PriorityQueue<PQItem<E, P>>(Comparator.naturalOrder());
        else
            queue = new PriorityQueue<PQItem<E, P>>(Comparator.reverseOrder());
    }

    /**
     * Add a new item into the queue with the given priority.
     */
    public void add(E item, P priority)
    {
        queue.add(new PQItem<E, P>(item, priority));
    }

    /**
     * Remove all the items from the priority queue.
     */
    public void clear() { queue.clear(); }

    /**
     * Test if the priority queue is empty.
     */
    public boolean isEmpty() { return queue.isEmpty(); }

    /**
     * Get the number of items in the priority queue.
     */
    public int size() { return queue.size(); }

    /**
     * Test if an item is in the priority queue or not.
     */
    public boolean contains(E item)
    {
        PQItem<E, P> pqitem = new PQItem<E, P>(item, null);
        return queue.contains(pqitem);
    }

    /**
     * Remove a specified item from the priority queue.
     */
    public boolean remove(E item)
    {
        PQItem<E, P> pqitem = new PQItem<E, P>(item, null);
        return queue.remove(pqitem);
    }

    /**
     * Remove and return the "highest priority" item from the queue -- meaning the greatest
     * priority for a max-queue, and lowest priority for a min-queue.
     */
    public E remove()
    {
        if (!queue.isEmpty())
        {
            return queue.remove().item;
        }
        return null;
    }

    /**
     * Change the priority of an item in the queue.
     */
    public void changePriority(E item, P newPriority)
    {
        PQItem<E, P> pqitem = new PQItem<E, P>(item, newPriority);
        queue.remove(pqitem);  // remove doesn't check priority, so removing & adding the same thing is ok
        queue.add(pqitem);
    }

    public String toString() { return queue.toString(); }

    private static class PQItem<E, P extends Comparable<P>> implements Comparable<PQItem<E, P>>
    {
        private final E item;
        private final P priority;
        public PQItem(E item, P priority)
        {
            this.item = item;
            this.priority = priority;
        }

        @Override
        public int compareTo(PQItem<E, P> otherItem) {
            return this.priority.compareTo(otherItem.priority);
        }

        @Override
        public boolean equals(Object obj) {
            if (this == obj) return true;
            if (!(obj instanceof PQItem)) return false;

            @SuppressWarnings("unchecked")
            PQItem<E, P> thisObj = (PQItem<E, P>) obj;
            return this.item.equals(thisObj.item);
        }

        public int hashCode()
        {
            return item.hashCode();
        }

        public String toString() { return "(" + item.toString() + ", " + priority.toString() + ")"; }
    }
}

