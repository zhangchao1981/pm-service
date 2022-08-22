//package com.iscas.pm.api.service.impl;
//
///**
// * @author by  lichang
// * @date 2022/8/20.
// */
//import org.checkerframework.checker.units.qual.A;
//
//import java.util.Scanner;
//public class Main {
//    public static void main(String args[]) {
//        Scanner cin = new Scanner(System.in);
//        while (cin.hasNext()) {
//            int n,m ;
//            n= cin.nextInt();
//            m= cin.nextInt();
//            String  first = cin.nextLine();
//            String second = cin.nextLine();
//            String[] Alist=first.split(" ");
//            int[] A=new int[Alist.length];
//            for (int i = 0; i <A.length ; i++) {
//                A[i]=Integer.parseInt(Alist[i]);
//            }
//            String[] Blist=second.split(" ");
//            int[] B=new int[Blist.length];
//            for (int i = 0; i <B.length ; i++) {
//                B[i]=Integer.parseInt(Blist[i]);
//            }
//
//            System.out.println(testSelect(n,m, A, B));
//        }
//    }
//    public  static Integer testSelect(int n, int m, int[]A, int[] B) {
//        Integer pointA = new Integer(0);
//        Integer pointB = new Integer(0);
//        if (n == m) {
//            for (int i = 0; i < A.length; i++) {
//                pointA+= Math.abs(A[i])+Math.abs(B[i]);
//            }
//            return  pointA;
//        }
//
//        for (int i = 0; i < A.length; i++) {
//            if (A[i]!=B[i]){
//                A[i]=B[i];
//                pointA+=A[i]+B[i];
//            }
//        }
//
//        for (int i = 0; i < A.length; i++) {
//            if (A[i]!=B[i]){
//                A[i]=B[i];
//                pointA+=A[i]+B[i];
//            }
//        }
//
//
//        return null;
//    }
//
//}