public class Practise{
  public static void main(String args[]){
    // System.out.println("hello")
    
    int number = Integer.parseInt(args[0]);
    // System.out.println(number);
    
    for(int row=0; row<number; row++)  {
        for(int column=0; column<=row; column++) {
            System.out.print("*");
        }
      System.out.println();
    }
  }
}