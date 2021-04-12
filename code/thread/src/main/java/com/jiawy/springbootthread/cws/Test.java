package com.jiawy.springbootthread.cws;

@SuppressWarnings("all")
public class Test {
    public static void main(String[] args) {
        Node node = ListGenerator.getNodeList(new int[] {1,2,3,4,5});
        System.out.println(node);

        System.out.println(replace(node));

    }


    public static Node replace(Node node){
        if(null == node  || null == node.next) return node;
        Node node1 = node;
        Node node2 = node.next;
        while(null != node2.next){
            Node tmp = node2.next;
            node2.next = node1;
            node1 = node2;
            node2 = tmp;
        }

        node2.next = node1;
        node1.next = null;
        return node2;
    }

    public static Node reverse2(Node head) {
        if (null == head || null == head.next)
            return head;
        Node pre = head;
        Node cur = head.next;
        while (null != cur.next) {
            Node tmp = cur.next;
            cur.next= pre ;
            pre = cur;
            cur = tmp;
        }
        cur.next=pre ;
        head.next = null ;
        return cur;
    }
}
