package com.base.web;


public class testMain {
    public static void main(String[] args) {
//        for(int i=0; i<10; i++){
//            for(int j=0; j<3; j++){
//
//                if(j  > 1){
//                    continue;
//                }
//                System.out.println(j+" ");
//            }
//        }
        for (int i = 0; i < args.length; i++)
            System.out.println(i == 0 ? args[i] : " " + args[i]  +"\n");
        System.out.println();
    }
}
