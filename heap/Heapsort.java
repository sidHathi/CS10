/**
 * Heapsort.java
 * Class demo by THC.
 * Performs the heapsort algorithm to sort an array a[0..n-1].
 * 
 * Travis Peters -- updated formatting (Winter 2015)
 * Chris Bailey-Kellogg -- generics, Spring 2016
 */
public class Heapsort<E extends Comparable<E>> {

    /**
     * Sort the array a[0..n-1] by the heapsort algorithm.
     */
    public void sort(E[] a, int n) {
        heapsort(a, n - 1);
    }

    /**
     * Sort the array a[0..lastLeaf] by the heapsort algorithm.
     */
    private void heapsort(E[] a, int lastLeaf) {
        // First, turn the array a[0..lastLeaf] into a max-heap.
        buildMaxHeap(a, lastLeaf);

        // Once the array is a max-heap, repeatedly swap the root
        // with the last leaf, putting the largest remaining element
        // in the last leaf's position, declare this last leaf to no
        // longer be in the heap, and then fix up the heap.
        while (lastLeaf > 0) {
            swap(a, 0, lastLeaf);       // swap the root with the last leaf
            lastLeaf--;                 // the last leaf is no longer in the heap
            maxHeapify(a, 0, lastLeaf); // fix up what's left
        }
    }

    /**
     * Restore the max-heap property. When this method is called, the max-heap
     * property holds everywhere, except possibly at node i and its children.
     * When this method returns, the max-heap property holds everywhere.
     */
    private void maxHeapify(E[] a, int i, int lastLeaf) {
        int left = leftChild(i);      // index of node i's left child
        int right = rightChild(i);    // index of node i's right child
        int largest;                  // will hold the index of the node with the largest element among node i, left, and right

        // Is there a left child and, if so, does the left child have an element larger than node i?
        if (left <= lastLeaf && a[left].compareTo(a[i]) > 0)
            largest = left; // yes, so the left child is the largest so far
        else
            largest = i;    // no, so node i is the largest so far

        // Is there a right child and, if so, does the right child have an
        // element larger than the larger of node i and the left child?
        if (right <= lastLeaf && a[right].compareTo(a[largest]) > 0)
            largest = right; // yes, so the right child is the largest

        /* 
         * If node i holds an element larger than both the left and right
         * children, then the max-heap property already held, and we need do
         * nothing more. Otherwise, we need to swap node i with the larger
         * of the two children, and then recurse down the heap from the larger
         * child.
         */
        if (largest != i) {
            swap(a, i, largest);
            maxHeapify(a, largest, lastLeaf);
        }
    }

    /**
     * Form array a[0..lastLeaf] into a max-heap.
     */
    private void buildMaxHeap(E[] a, int lastLeaf) {
        int lastNonLeaf = (lastLeaf - 1) / 2; // nodes lastNonLeaf+1 to lastLeaf are leaves
        for (int j = lastNonLeaf; j >= 0; j--)
            maxHeapify(a, j, lastLeaf);
    }

    /**
     * Swap two locations i and j in array a.
     */
    private void swap(E[] a, int i, int j) {
        E t = a[i];
        a[i] = a[j];
        a[j] = t;
    }

    /**
     * Return the index of the left child of node i.
     */
    private int leftChild(int i) {
        return 2 * i + 1;
    }

    /**
     * Return the index of the right child of node i.
     */
    private int rightChild(int i) {
        return 2 * i + 2;
    }

    public static void main(String[] args) {
        Integer[] nums = { 5, 9, 1, 6, 10, 2, 4, 3, 8, 7 };
        new Heapsort<Integer>().sort(nums, nums.length);
        for (Integer i : nums)
            System.out.println(i);
        System.out.println();
    }
}
