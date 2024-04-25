public class Main{
    public static void main(String[] args) {
            final LinkedList list = new LinkedList();

            Thread thread1 = new Thread(() -> {
                for (int i = 0; i < 5; i++) {
                    list.add(i);
                    System.out.println("Thread 1 adicionou: " + i);
                    try {
                        Thread.sleep(100);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            });

            Thread thread2 = new Thread(() -> {
                for (int i = 5; i < 10; i++) {
                    list.add(i);
                    System.out.println("Thread 2 adicionou: " + i);
                    try {
                        Thread.sleep(100);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            });

            Thread thread3 = new Thread(() -> {
                for (int i = 0; i < 5; i++) {
                    int value = list.get(i);
                    System.out.println("Thread 3 obteve: " + value + " na posição " + i);
                    try {
                        Thread.sleep(100);
                    }catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            });

            Thread thread4 = new Thread(() -> {
                for (int i = 0; i < 3; i++) {
                    list.remove(i);
                    System.out.println("Thread 4 removeu elemento na posição " + i);
                    try {
                        Thread.sleep(100);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            });

            thread1.start();
            thread2.start();
            thread3.start();
            thread4.start();
    }


    private static class Node{
        public Node next;
        public int data;

        public Node(int data){
            this.next = null;
            this.data = data;
        }
    }

    static class LinkedList{
        private Node head;
        private int size;

        public LinkedList(){
            this.head = null;
            this.size = 0;
        }

        public synchronized void add(int value){
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
            notify();
        }

        public synchronized int get(int index){
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

        public synchronized int size(){
            return this.size;
        }

        public synchronized void remove(int index) {
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
}
