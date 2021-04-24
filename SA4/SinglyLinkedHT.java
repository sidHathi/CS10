public class SinglyLinkedHT<T> implements SimpleList<T> {

    private Element head;
    // Tail instance var: stores last element in list
    private Element tail;
    private int size;
    /**
     * The linked elements in the list: each has a piece of data and a next pointer
     */
    private class Element {
        private T data;
        private Element next;

        private Element(T data, Element next) {
            this.data = data;
            this.next = next;
        }
    }

    public SinglyLinkedHT(){
        this.head = null;
        // Initialize tail
        this.tail = null;
        this.size = 0;
    }

    /**
     * Helper function, advancing to the nth Element in the list and returning it
     * (exception if not that many elements)
     */
    private Element advance(int n) throws Exception {
        Element e = head;
        if (e == null) {
            throw new Exception("list is null");
        }
        while (n > 0) {
            // Just follow the next pointers
            e = e.next;
            if (e == null) throw new Exception("invalid index");
            n--;
        }
        return e;
    }

    @Override
    public void add(int idx, T item) throws Exception {
        if (idx < 0) {
            throw new Exception("invalid index");
        }
        else if (idx == 0) {
            //With one element, the tail and head are the same with pointers that refer to themselves.
            // Insert at head
            head = new Element(item, head); //new item gets next pointer set to head

            // Sets tail to last element in list
            this.tail = advance(size);
        }
        else {
            // It's the next thing after element # idx-1
            Element e = advance(idx-1);
            // Splice it in
            e.next = new Element(item, e.next);	//create new element with next pointing at prior element's next
            //and prior element's next updated to point to this item

            // Sets tail to last element in list
            this.tail = advance(size);
        }
        size++;
    }

    @Override
    public void remove(int idx) throws Exception {
        if (idx < 0) {
            throw new Exception("invalid index");
        }
        else if (idx == 0) {
            // Just pop off the head
            if (head == null) throw new Exception("invalid index");
            head = head.next;
        }
        else {
            // It's the next thing after element # idx-1
            Element e = advance(idx-1);
            if (e.next == null) throw new Exception("invalid index");
            // Splice it out
            e.next = e.next.next;  //nice!

            // Sets tail to last element in list
            this.tail = advance(size-2);
        }
        size--;
    }

    @Override
    public T get(int idx) throws Exception {
        if (idx < 0) {
            throw new Exception("invalid index");
        }
        Element e = advance(idx);
        return e.data;
    }

    @Override
    public void set(int idx, T item) throws Exception {
        if (idx < 0) {
            throw new Exception("invalid index");
        }
        Element e = advance(idx);
        e.data = item;
    }

    @Override
    public String toString() {
        String result = "";
        for (Element x = head; x != null; x = x.next)
            result += x.data + "->";
        result += "[/]";

        return result;
    }

    @Override
    public int size() {
        return size;
    }

    /**
     * Takes in a new list and adds its parameters to the end of current list.
     * @param listToAppend  List whose elements will be appended to current list.
     */
    public void append(SinglyLinkedHT<T> listToAppend){
        // If both lists have elements combine the two lists
        if(this.size() > 0 && listToAppend.size() > 0){
            // Point the list's current tail to the new list's head
            this.tail.next = listToAppend.head;
            // Set the new tail of the list to thew new tail's head
            this.tail = listToAppend.tail;
            // Update List size
            this.size += listToAppend.size();
        }
        // If only the listToAppend has elements, set the current list to listToAppend
        else if (listToAppend.size() > 0){
            // Set current list head to new list head
            this.head = listToAppend.head;
            // Update size
            this.size += listToAppend.size();
        }
        // If only the current list has elements, nothing changes
    }

    public static void main(String[] args) throws Exception {

        SinglyLinkedHT<String> list1 = new SinglyLinkedHT<String>();
        SinglyLinkedHT<String> list2 = new SinglyLinkedHT<String>();

        System.out.println(list1 + " + " + list2);
        list1.append(list2);
        System.out.println(" = " + list1);

        list2.add(0, "a");
        System.out.println(list1 + " + " + list2);
        list1.append(list2);
        System.out.println(" = " + list1);

        list1.add(1, "b");
        list1.add(2, "c");
        SinglyLinkedHT<String> list3 = new SinglyLinkedHT<String>();
        System.out.println(list1 + " + " + list3);
        list1.append(list3);
        System.out.println(" = " + list1);

        SinglyLinkedHT<String> list4 = new SinglyLinkedHT<String>();
        list4.add(0, "z");
        list4.add(0, "y");
        list4.add(0, "x");
        System.out.println(list1 + " + " + list4);
        list1.append(list4);
        System.out.println(" = " + list1);

        list1.remove(5);
        list1.remove(4);
        SinglyLinkedHT<String> list5 = new SinglyLinkedHT<String>();
        list5.add(0, "z");
        list5.add(0, "y");
        System.out.println(list1 + " + " + list5);
        list1.append(list5);
        System.out.println(" = " + list1);
    }

}



