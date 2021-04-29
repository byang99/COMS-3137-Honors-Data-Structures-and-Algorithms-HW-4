/*
 * Name: Brian Yang
 * UNI: by2289
 */

import java.util.PriorityQueue;
import java.util.List;
public class KBestCounter<T extends Comparable<? super T>>
{
    PriorityQueue<T> heap;
    int k;

    public KBestCounter(int k)
    {
        heap = new PriorityQueue<>();
        this.k = k;
    }

    public void count(T x)
    {
        //if x isn't null, we simply add x into the priority queue
        //increasing the queue's size by 1
        if(x != null)
        {
            heap.add(x); //O(log N)
        }

        //if, by adding x, we increased the queue size to k+1, then
        //we call remove once to bring the queue size back to k
        //since remove method removes the minimum in the queue,
        //we maintain that the queue stores the k largest elements
        //we've seen so far
        if(heap.size() > k)
        {
            heap.remove(); //O(log N)
        }
    }

    public List<T> kbest()
    {
        //we will call remove k times. Each call to remove
        //returns the next smallest element in the priority queue,
        //so we add each element to the front of a list to have
        //the list be sorted in decreasing order

        //perform the removes on a copy of the heap, so the original
        //heap isn't changed
        PriorityQueue<T> copy = new PriorityQueue<>(heap);

        List<T> KLargest = new java.util.LinkedList<>();

        while(copy.size() > 0)
        {
            KLargest.add(0, copy.remove());
        }

        return KLargest;
    }
}
