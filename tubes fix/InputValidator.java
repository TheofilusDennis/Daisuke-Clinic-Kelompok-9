import java.util.Scanner;
import java.util.regex.Pattern;

public class InputValidator {
    private static Scanner scanner = new Scanner(System.in);
    
    
    private static final Pattern NAME_PATTERN = Pattern.compile("^[a-zA-Z\\s]{2,50}$");
    private static final Pattern USERNAME_PATTERN = Pattern.compile("^[a-zA-Z0-9_]{3,20}$");
    private static final Pattern PASSWORD_PATTERN = Pattern.compile("^.{4,20}$");
    private static final Pattern PHONE_PATTERN = Pattern.compile("^[0-9+\\-\\s()]{8,15}$");
    private static final Pattern TIME_PATTERN = Pattern.compile("^([01]?[0-9]|2[0-3]).[0-5][0-9]$");
    private static final Pattern ADDRESS_PATTERN = Pattern.compile("^[a-zA-Z0-9\\s,./\\-]{5,100}$");
    private static final Pattern DIAGNOSIS_PATTERN = Pattern.compile("^[a-zA-Z\\s\\p{Punct}]{3,200}$");
    private static final Pattern TREATMENT_PATTERN = Pattern.compile("^[a-zA-Z\\s\\p{Punct}]{3,300}$");
    private static final Pattern ID_PATTERN = Pattern.compile("^[PDA]\\d{3}$");
   
    public static String getValidName(String prompt) {
        String input;
        while (true) {
            System.out.print(prompt);
            input = scanner.nextLine().trim();
            
            if (input.isEmpty()) {
                System.out.println("Name cannot be empty. Please try again.");
                continue;
            }
            
            if (!NAME_PATTERN.matcher(input).matches()) {
                System.out.println("    Invalid name format. Name should:");
                System.out.println("   - Only contain letters and spaces");
                System.out.println("   - Be between 2-50 characters");
                System.out.println("   - Not contain numbers or special characters");
                continue;
            }
            
            return input;
        }
    }
    
     public static String getValidId(String prompt, char type) {
        String input;
        while (true) {
            System.out.print(prompt);
            input = scanner.nextLine().trim().toUpperCase();

            if (!ID_PATTERN.matcher(input).matches()) {
                System.out.println("Format ID tidak valid. Contoh: " + type + "001");
                continue;
            }

            if (input.charAt(0) != type) {
                System.out.println("Jenis ID tidak sesuai. Harus diawali dengan '" + type + "'");
                continue;
            }

            return input;
        }
    }
  
    public static String getValidUsername(String prompt) {
        String input;
        while (true) {
            System.out.print(prompt);
            input = scanner.nextLine().trim();
            
            if (input.isEmpty()) {
                System.out.println("Username cannot be empty. Please try again.");
                continue;
            }
            
            if (!USERNAME_PATTERN.matcher(input).matches()) {
                System.out.println("    Invalid username format. Username should:");
                System.out.println("   - Only contain letters, numbers, and underscores");
                System.out.println("   - Be between 3-20 characters");
                System.out.println("   - Not contain spaces or special characters (except _)");
                continue;
            }
            
            return input;
        }
    }
    
   
    public static String getValidPassword(String prompt) {
        String input;
        while (true) {
            System.out.print(prompt);
            input = scanner.nextLine().trim();
            
            if (input.isEmpty()) {
                System.out.println("Password cannot be empty. Please try again.");
                continue;
            }
            
            if (!PASSWORD_PATTERN.matcher(input).matches()) {
                System.out.println("    Invalid password format. Password should:");
                System.out.println("   - Be between 4-20 characters");
                continue;
            }
            
            return input;
        }
    }
    
   
    public static int getValidInteger(String prompt, int min, int max) {
        int input;
        while (true) {
            System.out.print(prompt);
            try {
                String inputStr = scanner.nextLine().trim();
                if (inputStr.isEmpty()) {
                    System.out.println("Input cannot be empty. Please enter a number.");
                    continue;
                }
                
                input = Integer.parseInt(inputStr);
                
                if (input < min || input > max) {
                    System.out.println("Number must be between " + min + " and " + max + ". Please try again.");
                    continue;
                }
                
                return input;
            } catch (NumberFormatException e) {
                System.out.println("Invalid input. Please enter a valid number.");
            }
        }
    }
    
   
    public static int getValidInteger(String prompt) {
        int input;
        while (true) {
            System.out.print(prompt);
            try {
                String inputStr = scanner.nextLine().trim();
                if (inputStr.isEmpty()) {
                    System.out.println("Input cannot be empty. Please enter a number.");
                    continue;
                }
                
                input = Integer.parseInt(inputStr);
                return input;
            } catch (NumberFormatException e) {
                System.out.println("Invalid input. Please enter a valid number.");
            }
        }
    }
    
   
    public static int getValidAge(String prompt) {
        return getValidInteger(prompt, 1, 150);
    }
    
   
    public static String getValidPhoneNumber(String prompt) {
        String input;
        while (true) {
            System.out.print(prompt);
            input = scanner.nextLine().trim();
            
            if (input.isEmpty()) {
                System.out.println("Phone number cannot be empty. Please try again.");
                continue;
            }
            
            if (!PHONE_PATTERN.matcher(input).matches()) {
                System.out.println("    Invalid phone number format. Phone number should:");
                System.out.println("   - Be between 8-15 characters");
                System.out.println("   - Only contain numbers, +, -, spaces, and parentheses");
                System.out.println("   - Example: +62812345678 or (021) 1234-5678");
                continue;
            }
            
            return input;
        }
    }
    
  
    public static String getValidAddress(String prompt) {
        String input;
        while (true) {
            System.out.print(prompt);
            input = scanner.nextLine().trim();
            
            if (input.isEmpty()) {
                System.out.println("Address cannot be empty. Please try again.");
                continue;
            }
            
            if (!ADDRESS_PATTERN.matcher(input).matches()) {
                System.out.println("    Invalid address format. Address should:");
                System.out.println("   - Be between 5-100 characters");
                System.out.println("   - Only contain letters, numbers, spaces, and common punctuation (,./-)");
                continue;
            }
            
            return input;
        }
    }
    
   
    public static String getValidTime(String prompt) {
        String input;
        while (true) {
            System.out.print(prompt);
            input = scanner.nextLine().trim();
            
            if (input.isEmpty()) {
                System.out.println("Time cannot be empty. Please try again.");
                continue;
            }
            
            if (!TIME_PATTERN.matcher(input).matches()) {
                System.out.println("Invalid time format. Please use HH:mm format (24-hour)");
                System.out.println("   - Examples: 08.30, 14.45, 09.00");
                System.out.println("   - Hours: 00-23, Minutes: 00-59");
                continue;
            }
            
           
            String[] timeParts = input.split(":");
            int hour = Integer.parseInt(timeParts[0]);
            
            if (hour < 0 || hour >= 24) {
                System.out.println("time must be in format 24 hours (00.00-23.59)");
                continue;
            }
            
            return input;
        }
    }
    
   
    public static int getValidSpecialtyChoice(String prompt) {
        return getValidInteger(prompt, 1, 4);
    }
    
   
    public static int getValidMenuChoice(String prompt, int min, int max) {
        return getValidInteger(prompt, min, max);
    }
    
    
    public static String getValidString(String prompt) {
        String input;
        while (true) {
            System.out.print(prompt);
            input = scanner.nextLine().trim();
            
            if (input.isEmpty()) {
                System.out.println("Input cannot be empty. Please try again.");
                continue;
            }
            
            return input;
        }
    }
    
    
    
    public static String getValidDiagnosis(String prompt) {
        String input;
        while (true) {
            System.out.print(prompt);
            input = scanner.nextLine().trim();
            
            if (input.isEmpty()) {
                System.out.println("Diagnosis cannot be empty. Please try again.");
                continue;
            }
            
            if (input.length() < 3 || input.length() > 200) {
                System.out.println("Diagnosis should be between 3-200 characters.");
                continue;
            }
            
            if (input.matches(".*\\d.*")) {
                System.out.println("Diagnosis should not contain numbers. Please use letters only.");
                continue;
            }
            
            if (!DIAGNOSIS_PATTERN.matcher(input).matches()) {
                System.out.println("Invalid diagnosis format. Should contain only letters and spaces.");
                continue;
            }
            
            return input;
        }
    }
    
   
    public static String getValidTreatment(String prompt) {
        String input;
        while (true) {
            System.out.print(prompt);
            input = scanner.nextLine().trim();
            
            if (input.isEmpty()) {
                System.out.println("Treatment cannot be empty. Please try again.");
                continue;
            }
            
            if (input.length() < 3 || input.length() > 300) {
                System.out.println("Treatment description should be between 3-300 characters.");
                continue;
            }
            
            if (input.matches(".*\\d.*")) {
                System.out.println("Treatment should not contain numbers. Please use letters only.");
                continue;
            }
            
            if (!TREATMENT_PATTERN.matcher(input).matches()) {
                System.out.println("Invalid treatment format. Should contain only letters and spaces.");
                continue;
            }
            
            return input;
        }
    }
    
    public static boolean getValidConfirmation(String prompt) {
        String input;
        while (true) {
            System.out.print(prompt + " (y/n): ");
            input = scanner.nextLine().trim().toLowerCase();
            
            if (input.equals("y") || input.equals("yes")) {
                return true;
            } else if (input.equals("n") || input.equals("no")) {
                return false;
            } else {
                System.out.println("Please enter 'y' for yes or 'n' for no.");
            }
        }
    }
}