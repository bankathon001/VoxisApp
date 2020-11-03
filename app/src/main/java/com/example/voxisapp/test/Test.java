package com.example.myapplication.test;

import java.util.Arrays;
import java.util.List;
import java.util.PriorityQueue;

class Test {
    // Function to find the K'th largest element in the
    // array using min-heap
    public static int FindKthLargest(List<Integer> ints, int k) {
        // create a min-heap using PriorityQueue class and insert
        // first k elements of the array into the heap
        PriorityQueue<Integer> pq = new PriorityQueue<>(ints.subList(0, k));

        // do for remaining array elements
        for (int i = k; i < ints.size(); i++) {
            // if current element is more than the root of the heap
            if (ints.get(i) > pq.peek()) {
                // replace root with the current element
                pq.poll();
                pq.add(ints.get(i));
            }
        }

        // return the root of min-heap
        return pq.peek();
    }


    public static void main(String[] args) {
        List<Integer> ints = Arrays.asList(7, 4, 6, 3, 9, 1);
        int k = 2;

        System.out.println("K'th largest element in the array is " +
                FindKthLargest(ints, k));
    }
}