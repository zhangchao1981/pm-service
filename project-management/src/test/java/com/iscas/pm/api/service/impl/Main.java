//import java.util.Scanner;
//public class Main {
//    public static void main(String args[]) {
//        Scanner cin = new Scanner(System.in);
//        while (cin.hasNext()) {
//            int n = cin.nextInt();
//            String A = cin.next();
//            String B = cin.next();
//            String aim = testSelect(n, A, B);
//            System.out.println(aim);
//        }
//    }
//    public  static String testSelect(int n, String A,String B){
//        if (n==0){
//            return  null;
//        }
//        char[] chars = new char[2*n];
//        int j=0;
//        for(int i=0;i<A.length();i++,j++){
//            chars[j]=A.charAt(i);
//            chars[j+1] = B.charAt(i);
//        }
//        return new String(chars);
//    }
//}
