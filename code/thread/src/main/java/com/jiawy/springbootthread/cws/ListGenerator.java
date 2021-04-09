package com.jiawy.springbootthread.cws;

import java.security.SecureRandom;
import java.util.Random;

public class ListGenerator {
    public static Node getNodeList(int size, int max) {
        if (size == 0) return null;

        SecureRandom random = new SecureRandom();
        Node head = new Node(random.nextInt(max));
        if (size == 1) return head;

        Node node = head;

        for (int i = 1; i < size; i++) {
            node.next = new Node(random.nextInt(max));
            node = node.next;
        }
        return head;
    }

    public static Node getNodeList(int[] arr) {
        Node head = null;

        if (arr == null || arr.length == 0) return head;

        Node node = null;
        for (int i : arr) {
            if (head == null) {
                head = new Node(i);
                node = head;
            } else {
                node.next = new Node(i);
                node = node.next;
            }
        }
        return head;
    }
}
