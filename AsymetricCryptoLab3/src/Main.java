import PrimeNumber.PrimeNumber;

public class Main {

    public static void main(String[] args) {
       // System.out.println("hello");
        PrimeNumber p=new PrimeNumber();
        var v=p.generatePrime4kplus1(128);
        System.out.println(v);
    }
}
