public class LinkedList<T> {
    private Node<T> head;
    private Node<T> tail;
    private int size;

    public static class Node<T> {
        public T data;
        public Node<T> next;

        public Node(T data) {
            this.data = data;
            this.next = null;
        }
    }

    public LinkedList() {
        head = tail = null;
        size = 0;
    }

    public void add(T data) {
        Node<T> newNode = new Node<>(data);
        if (head == null) {
            head = tail = newNode;
        } else {
            tail.next = newNode;
            tail = newNode;
        }
        size++;
    }

    public void addSorted(T data, java.util.Comparator<T> comparator) {
        Node<T> newNode = new Node<>(data);
        
        if (head == null || comparator.compare(data, head.data) < 0) {
            newNode.next = head;
            head = newNode;
            if (tail == null) {
                tail = newNode;
            }
            size++;
            return;
        }
        
        Node<T> current = head;
        while (current.next != null && comparator.compare(data, current.next.data) >= 0) {
            current = current.next;
        }
        
        newNode.next = current.next;
        current.next = newNode;
        
        if (newNode.next == null) {
            tail = newNode;
        }
        
        size++;
    }

    public T poll() {
        if (head == null) return null;
        T data = head.data;
        head = head.next;
        if (head == null) tail = null;
        size--;
        return data;
    }

    public boolean isEmpty() {
        return size == 0;
    }

    public int size() {
        return size;
    }

    public Node<T> getHead() {
        return head;
    }
}