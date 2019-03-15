package ua.edu.sumdu;

public class Test {
    public static void main(String[] args) {
        Function myFunction = new FunctionVar27Impl();
        Point minOptimum = NelderMid.count(myFunction);
        System.out.println("The minimum of the function is F(" + minOptimum + ") = " + myFunction.count(minOptimum));
    }
}
