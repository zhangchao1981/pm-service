//public class Maintest2 {
//
//    }
//class Solution {
//    public LinkedList<Integer> path=new LinkedList<>();
//    public  List<List<Integer>> result=new ArrayList<>();
//
//    public List<List<Integer>> combine(int n, int k) {
//        backing(n,k,1);
//        return  result;
//    }
//
//    public void  backing(int n, int k,int startIndex){
//        if(path.size()==k){
//            result.add(new ArrayList<>(path));
//            return;
//        }
//        for (int i=startIndex;i<= n - (k - path.size())+1;i++){
//            path.add(i);
//            backing(n,k,i+1);
//            path.removeLast();
//        }
//    }
//}