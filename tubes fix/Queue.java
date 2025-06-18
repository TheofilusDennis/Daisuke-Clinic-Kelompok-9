public class Queue<T> {
    private LinkedList<T> list = new LinkedList<>();

    public void enqueue(T data) {
        list.add(data);
    }

    public T dequeue() {
        return list.poll();
    }

    public boolean isEmpty() {
        return list.isEmpty();
    }

    public int size() {
        return list.size();
    }
}