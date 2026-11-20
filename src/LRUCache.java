import java.util.HashMap;
import java.util.Map;

public class LRUCache{
    final Map<String,Node>hashmap;
    final DoubleLinkList doubleLinkList;
    final int capacity;

    LRUCache(int capacity) {
        this.capacity=capacity;
        this.hashmap=new HashMap<>();
        this.doubleLinkList=new DoubleLinkList();
    }
    Integer get(String key) {
        if(!hashmap.containsKey(key))
        {
            return null;
        }
        // get value from hash map
        Node node = hashmap.get(key);
        // update in Double LinkList
        doubleLinkList.moveToFront(node);
        return node.value;
    }
    void put(String key, int value) {
        if(hashmap.containsKey(key))
        {
            Node node= hashmap.get(key);
            node.value=value;
            doubleLinkList.moveToFront(node);
        }
        else {
            if(hashmap.size()==capacity)
            {
                Node lru=doubleLinkList.removeLast();
                hashmap.remove(lru.key);
            }
            Node node= new Node(key,value);
            doubleLinkList.addFirst(node);
            hashmap.put(key,node);
        }
    }
}