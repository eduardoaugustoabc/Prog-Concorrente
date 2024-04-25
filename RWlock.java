public class RWlock {
    
    private Node head;
    private int size;
    private final Object readLock;
    private final Object writeLock;

    public RWlock(){
        this.head = null;
        this.size = 0;
        readLock = new Object();
        writeLock = new Object();
    }

    public void add(int value){
        synchronized (writeLock) {
            Node newNode = new Node(value);

            if(head == null){
                head = newNode;
            }else{
                Node current = head;
                while(current.next != null){
                    current = current.next;
                }
                current.next = newNode;
            }
            size++;
        }
    }

    public int get(int index){
        synchronized (readLock) {
            while(index < 0 || index >= size){
                try{
                    wait();
                }catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                }
            }
            Node current = head;
            for (int i = 0; i < index; i++){
                current = current.next;
            }
            return current.data;
        }
    }

    public int size(){
        synchronized (readLock){
            return this.size;
        }
    }

    public void remove(int index) {
        synchronized (writeLock){
            while (index < 0 || index >= size) {
                try {
                    wait();
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                }
            }
            if (index == 0) {
                head = head.next;
            } else {
                Node prev = null;
                Node current = head;
                for (int i = 0; i < index; i++) {
                    prev = current;
                    current = current.next;
                }
                prev.next = current.next;
            }
            size--;
        }
    }

    private static class Node{
        public Node next;
        public int data;

        public Node(int data){
            this.next = null;
            this.data = data;
        }

    }

}


