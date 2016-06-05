package com.company;

import java.util.*;

/**
 * Created by Женя on 03.06.2016.
 */
public class MyLinkedList<T> implements Queue<T>{
    private Node<T> first;
    private Node<T> last;
    private int size;
    private int modCount;

    class Node<T> {
        private Node<T> next;
        private Node<T> previous;
        private T value;

        public Node(T value, Node<T> previous, Node<T> next){
            this.value = value;
            this.previous = previous;
            this.next = next;
        }
        public Node(T value){
            this.value = value;
        }
    }

    public void addFirst(T value){
        Node<T> f = first;
        Node<T> newNode = new Node<>(value, null, f);
        first = newNode;
        if (f == null){
            last = newNode;
        }else{
            f.previous = newNode;
        }
        size++;
        modCount++;
    }

    public void addLast(T value){
        Node<T> l = last;
        Node<T> newNode = new Node<>(value, l, null);
        last = newNode;
        if (l == null){
            first = newNode;
        }else{
            l.next = newNode;
        }
        size++;
        modCount++;
    }

    public T removeFirst(){
        Node<T> f = first;
        if (f == null){
            throw new NoSuchElementException("List is empty");
        }
        T element = f.value;
        Node<T> next = f.next;
        f.value = null;
        f.next = null;
        first = next;
        if (next == null){
            last = null;
        }else{
            next.previous = null;
        }
        size--;
        modCount++;
        return element;
    }

    public T removeLast(){
        Node<T> l = last;
        if (l == null){
            throw new NoSuchElementException("List is empty");
        }
        T element = l.value;
        Node<T> previous = l.previous;
        l.value = null;
        first.previous = null;
        last = previous;
        if (previous == null){
            first = null;
        }else{
            previous.next = null;
        }
        size--;
        modCount++;
        return element;
    }

    public T getFirst(){
        return (T) first.value;
    }

    public T getLast(){
        return (T) last.value;
    }

    public T get(int index){
        checkIndex(index);
        Node temp = getNode(index);
        return (T) temp.value;
    }

    private Node getNode(int index){
        int i;
        Node temp;
        if (index < size/2){
            i = 0;
            temp = first;
            while(i != index){
                temp = temp.next;
                i++;
            }
        }else{
            i  = size - 1;
            temp = last;
            while(i != index){
                temp = temp.previous;
                i--;
            }
        }
        return temp;
    }

    public void add(int index, T value){
        checkIndex(index);
        if (index == 0){
            addFirst(value);
            return;
        }
        if (index == size){
            add(value);
            return;
        }
        modCount++;
        Node temp = getNode(index);
        Node node = new Node(value);
        temp.next.previous = node;
        node.next = temp.next;
        node.previous = temp;
        temp.next = node;
        size++;
    }

    @Override
    public boolean add(T t) {
        addLast(t);
        return true;
    }

    @Override
    public boolean contains(Object o) {
        Node temp = first;
        while (temp != null) {
            if (temp.value.equals(o)){
                return true;
            }
            temp = temp.next;
        }
        return false;
    }

    private void checkIndex(int index){
        if (index < 0 || index >= size()){
            throw new ArrayIndexOutOfBoundsException("Out of bounds: " + index);
        }
    }



    @Override
    public boolean remove(Object o) {
        return false;
    }

    @Override
    public Iterator<T> iterator() {
        return new ListIterator<T>() {
            int modCount = 0;
            int currentIndex = 0;
            Node<T> justReturned = null;
            Node<T> current = first;
            @Override
            public boolean hasNext() {
                return (current != null && current.next!= null);
            }

            @Override
            public T next() {
                justReturned = current;
                currentIndex++;
                return (T) justReturned.value;
            }

            @Override
            public boolean hasPrevious() {
                return (current != null && current.previous != null);
            }

            @Override
            public T previous() {
                justReturned = current;
                currentIndex--;
                return (T) justReturned.value;
            }

            @Override
            public int nextIndex() {
                return currentIndex++;
            }

            @Override
            public int previousIndex() {
                return currentIndex--;
            }

            @Override
            public void remove() {
                checkMods();
                modCount++;
                if (justReturned == null){
                    throw new IllegalArgumentException();
                }
                if (justReturned == first){
                    MyLinkedList.this.removeFirst();
                }else if (justReturned == last){
                    MyLinkedList.this.removeLast();
                }else{
                    justReturned.next.previous = justReturned.previous;
                    justReturned.previous.next = justReturned.next;
                    justReturned = null;
                }
            }

            public void set(int index, T value){
                checkMods();
                checkIndex(index);
                modCount++;
                Node temp = getNode(index);
                temp.value = value;
            }

            @Override
            public void set(T t) {
                checkMods();
                modCount++;
                justReturned.value = t;
            }

            @Override
            public void add(T t) {
                checkMods();
                modCount++;
                if (justReturned == first){
                    MyLinkedList.this.addFirst(t);
                }else if (justReturned == last){
                    MyLinkedList.this.addLast(t);
                }else{
                    Node node = new Node(t);
                    justReturned.next.previous = node;
                    node.next = justReturned.next;
                    justReturned.next = node;
                    node.previous = justReturned;
                }
            }

            private void checkMods(){
                if (modCount != MyLinkedList.this.modCount){
                    throw new ConcurrentModificationException();
                }
            }
        };

    }

    @Override
    public Object[] toArray() {
        return new Object[0];
    }

    @Override
    public <T1> T1[] toArray(T1[] a) {
        return null;
    }

    @Override
    public int size() {
        return 0;
    }

    @Override
    public boolean isEmpty() {
        return false;
    }

    @Override
    public boolean containsAll(Collection<?> c) {
        return false;
    }

    @Override
    public boolean addAll(Collection<? extends T> c) {
        return false;
    }

    @Override
    public boolean removeAll(Collection<?> c) {
        return false;
    }

    @Override
    public boolean retainAll(Collection<?> c) {
        return false;
    }

    @Override
    public void clear() {

    }

    @Override
    public boolean offer(T t) {
        return false;
    }

    @Override
    public T remove() {
        return null;
    }

    @Override
    public T poll() {
        return null;
    }

    @Override
    public T element() {
        return null;
    }

    @Override
    public T peek() {
        return null;
    }

}
