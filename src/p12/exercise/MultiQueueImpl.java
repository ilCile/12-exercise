package p12.exercise;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class MultiQueueImpl<T, Q> implements MultiQueue<T, Q>{

    Map<Q, List<T>> map = new HashMap<>();

    @Override
    public Set<Q> availableQueues() {
        return this.map.keySet();
    }

    @Override
    public void openNewQueue(Q queue) {
       if(this.map.containsKey(queue)){
            throw new IllegalArgumentException();
       }
       this.map.put(queue, new ArrayList<>());
    }

    @Override
    public boolean isQueueEmpty(Q queue) {
        if(!this.map.containsKey(queue)){
            throw new IllegalArgumentException();
        }
        return this.map.get(queue).isEmpty();
    }

    @Override
    public void enqueue(T elem, Q queue) {
        if(!this.map.containsKey(queue)){
            throw new IllegalArgumentException();
        }
        this.map.get(queue).addLast(elem);
    }

    @Override
    public T dequeue(Q queue) {
        if(!this.map.containsKey(queue)){
            throw new IllegalArgumentException();
        }
        if(!this.map.get(queue).isEmpty()){
            T removedElement = this.map.get(queue).getFirst();
            this.map.get(queue).removeFirst();
            return removedElement;
        }else{
            return null;
        }
    }

    @Override
    public Map<Q, T> dequeueOneFromAllQueues() {
        Map<Q, T> removedElements = new HashMap<>();
        for(var queue : this.map.keySet()){
            if(!this.map.get(queue).isEmpty()){
                removedElements.put(queue, this.map.get(queue).getFirst());
                this.map.get(queue).removeFirst();
            }else{
                removedElements.put(queue, null);
            }
        }
        return removedElements;
    }

    @Override
    public Set<T> allEnqueuedElements() {
        Set<T> allEnqueuedElements = new HashSet<>();
        for(var queue : this.map.keySet()){
            for(var elem : this.map.get(queue)){
                allEnqueuedElements.add(elem);
            }
        }
        return allEnqueuedElements;
    }

    @Override
    public List<T> dequeueAllFromQueue(Q queue) {
        if(!this.map.containsKey(queue)){
            throw new IllegalArgumentException();
        }
        List<T> removedElements = new ArrayList<>();
        for(var elem : this.map.get(queue)){
            removedElements.add(elem);
        }
        this.map.get(queue).removeAll(removedElements);
        return removedElements;
    }

    @Override
    public void closeQueueAndReallocate(Q queue) {
        if(!this.map.containsKey(queue)){
            throw new IllegalArgumentException();
        }
        List<T> toReallocateElements = new ArrayList<>();
        toReallocateElements = this.map.get(queue);
        this.map.remove(queue);
        if(this.map.isEmpty()){
            throw new IllegalStateException();
        }
        while (!toReallocateElements.isEmpty()) {
            for(var q : this.map.keySet()){
                this.map.get(q).addLast(toReallocateElements.getFirst());
                toReallocateElements.removeFirst();
            }
        }
    }

}
