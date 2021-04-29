/*
 * Name: Brian Yang
 * UNI: by2289
 */

import java.io.*;
import java.util.*;




/**
 * A class that contains several sorting routines,
 * implemented as static methods.
 * Arrays are rearranged with smallest item first,
 * using compareTo.
 * @author Mark Allen Weiss
 */
public class QuickSort3Way
{
    /**
     * Quicksort algorithm.
     * @param a an array of Comparable items.
     */
    public static <AnyType extends Comparable<? super AnyType>>
    void quicksort( AnyType [ ] a )
    {
        quicksort( a, 0, a.length - 1 );
    }

    private static final int CUTOFF = 3;

    /**
     * Method to swap to elements in an array.
     * @param a an array of objects.
     * @param index1 the index of the first object.
     * @param index2 the index of the second object.
     */
    public static <AnyType> void swapReferences( AnyType [ ] a, int index1, int index2 )
    {
        AnyType tmp = a[ index1 ];
        a[ index1 ] = a[ index2 ];
        a[ index2 ] = tmp;
    }

    /**
     * Return median of left, center, and right.
     * Order these and hide the pivot.
     */
    private static <AnyType extends Comparable<? super AnyType>>
    AnyType median3( AnyType [ ] a, int left, int right )
    {
        int center = ( left + right ) / 2;
        if( a[ center ].compareTo( a[ left ] ) < 0 )
            swapReferences( a, left, center );
        if( a[ right ].compareTo( a[ left ] ) < 0 )
            swapReferences( a, left, right );
        if( a[ right ].compareTo( a[ center ] ) < 0 )
            swapReferences( a, center, right );

        // Place pivot at position right - 1
        swapReferences( a, center, right - 1 );
        return a[ right - 1 ];
    }

    /**
     * Internal quicksort method that makes recursive calls.
     * Uses median-of-three partitioning and a cutoff of 10.
     * @param a an array of Comparable items.
     * @param left the left-most index of the subarray.
     * @param right the right-most index of the subarray.
     */
    private static <AnyType extends Comparable<? super AnyType>>
    void quicksort( AnyType [ ] a, int left, int right )
    {
        if(left + CUTOFF <= right)
        {
            AnyType pivot = median3( a, left, right );
            // Begin partitioning
            int LT = left, GT = right;
            int i = left + 1, j = right - 1;

            for(; ;)
            {
                while(a[i].compareTo( pivot ) < 0) { i++; }
                while(a[j].compareTo( pivot ) > 0) { j--; }
                if(i < j)
                {
                    swapReferences(a, i, j);
                    if (a[i].compareTo(pivot) < 0)
                    {
                        i++;
                    }
                    else
                    {
                        swapReferences(a, i++, LT++);
                    }
                    if (a[j].compareTo(pivot) > 0)
                    {
                        j--;
                    }
                    else
                    {
                        swapReferences(a, j--, GT--);
                    }
                }
                else
                    break;
            }
            while(LT > left)
            {
                swapReferences(a, --LT, j--);
            }
            while(GT < right)
            {
                swapReferences(a, ++GT, i++);
            }

            quicksort(a, LT, j);
            quicksort(a, i, GT);
        }
        else  // Do an insertion sort on the subarray
        {
            insertionSort(a, left, right);
        }
    }


    /**
     * Internal insertion sort routine for subarrays
     * that is used by quicksort.
     * @param a an array of Comparable items.
     * @param left the left-most index of the subarray.
     * @param right the right-most index of the subarray.
     */
    private static <AnyType extends Comparable<? super AnyType>>
    void insertionSort( AnyType [ ] a, int left, int right )
    {
        for( int p = left + 1; p <= right; p++ )
        {
            AnyType tmp = a[ p ];
            int j;

            for( j = p; j > left && tmp.compareTo( a[ j - 1 ] ) < 0; j-- )
                a[ j ] = a[ j - 1 ];
            a[ j ] = tmp;
        }
    }





    private static final int NUM_ITEMS = 1000;
    private static int theSeed = 1;

    private static void checkSort( Integer [ ] a )
    {
        for( int i = 0; i < a.length - 1; i++ )
            if( ! (a[i] <= a[i+1]) )
                System.out.println( "Error at " + i );
        System.out.println( "Finished checksort" );
    }


    public static void main( String [ ] args )
    {
        //Integer [] a = {4,6,2,4,4,6,2,1,4,1,2};
        //Integer [] a = {9,9,8,7,7,6,6,6,5,4,3,2,2,1,1,1,1};
        Integer [] a = {3,1,4,1,5,9,2,6,5,3,5};
        for(Integer x : a)
        {
            System.out.print(x + " ");
        }
        System.out.println();
        QuickSort3Way.quicksort(a);
        for(Integer x : a)
        {
            System.out.print(x + " ");
        }
        System.out.println();
        checkSort(a);
    }
}