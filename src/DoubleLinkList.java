public class DoubleLinkList {
    final Node head;
    final Node tail;

    DoubleLinkList(){
        head = new Node(null,null);
        tail = new Node(null,null);
        head.next=tail;
        tail.prev=head;
    }

    public void addFirst(Node node){
        Node currHead=head.next;
        head.next=node;
        node.prev=head;
        node.next=currHead;
        currHead.prev=node;
    }
    public void remove(Node node) {
        node.prev.next=node.next;
        node.next.prev=node.prev;
    }
    public void moveToFront(Node node) {
        remove(node);
        addFirst(node);
    }
    public Node removeLast() {
        if(tail.prev==head){
            return null;
        }
        Node last=tail.prev;
        remove(last);
        return last;
    }

}
