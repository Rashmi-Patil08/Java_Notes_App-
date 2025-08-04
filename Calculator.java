import java.util.Scanner;
public class Calculator {
    
    // Method for addition
    public static double add(double num1, double num2) {
        return num1 + num2;
    }
    
    // Method for subtraction
    public static double subtract(double num1, double num2) {
        return num1 - num2;
    }
    
    // Method for multiplication
    public static double multiply(double num1, double num2) {
        return num1 * num2;
    }
    
    // Method for division with zero check
    public static double divide(double num1, double num2) {
        if (num2 == 0) {
            throw new ArithmeticException("Cannot divide by zero!");
        }
        return num1 / num2;
    }
    
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        boolean continueCalculating = true;
        
        System.out.println("Java Console Calculator");
        System.out.println("----------------------");
        
        while (continueCalculating) {
            System.out.println("\nChoose an operation:");
            System.out.println("1. Addition (+)");
            System.out.println("2. Subtraction (-)");
            System.out.println("3. Multiplication (*)");
            System.out.println("4. Division (/)");
            System.out.println("5. Exit");
            System.out.print("Enter your choice (1-5): ");
            
            int choice = scanner.nextInt();
            
            if (choice == 5) {
                continueCalculating = false;
                System.out.println("Thank you for using the calculator. Goodbye!");
                break;
            }
            
            System.out.print("Enter first number: ");
            double num1 = scanner.nextDouble();
            
            System.out.print("Enter second number: ");
            double num2 = scanner.nextDouble();
            
            double result = 0;
            String operation = "";
            
            try {
                switch (choice) {
                    case 1:
                        result = add(num1, num2);
                        operation = "+";
                        break;
                    case 2:
                        result = subtract(num1, num2);
                        operation = "-";
                        break;
                    case 3:
                        result = multiply(num1, num2);
                        operation = "*";
                        break;
                    case 4:
                        result = divide(num1, num2);
                        operation = "/";
                        break;
                    default:
                        System.out.println("Invalid choice! Please try again.");
                        continue;
                }
                
                System.out.printf("\nResult: %.2f %s %.2f = %.2f%n", num1, operation, num2, result);
                
            } catch (ArithmeticException e) {
                System.out.println("Error: " + e.getMessage());
            }
        }
        
        scanner.close();
    }
}

