package test.sort;

import java.util.*;

public class StackAndQueue {


    static int[] numbers = {1,2,3,4,5};


    public static void main(String[] args) {
        Stack<Integer> s = new Stack<>();
        Queue<Integer> q  = new LinkedList<>();
        Deque<Integer> d = new ArrayDeque<Integer>();
        for (int i : numbers) {
            s.push(i);
            q.offer(i);
            d.push(i);
        }


        System.out.println("Stack: ");
        for(int i : s){
            System.out.println(i);
        }

        System.out.println();
        System.out.println("Queue:");
        for(Integer i : q) {
            System.out.println(i);
        }

        System.out.println("Deque:");
        for(Integer i : d) {
            System.out.println(i);
        }
    }
}
