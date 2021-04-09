package com.jiawy.springbootthread.cws;

public class Node {

    int value;
    Node next;

    public Node(int value) {
        this.value = value;
    }

    public Node() {}

    @Override
    public String toString() {

        StringBuilder sb = new StringBuilder();

        Node cur = this;

        while (cur != null) {
            sb.append(cur.value);
            sb.append("\t");
            cur = cur.next;
        }

        return sb.toString();
    }

}
