/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package utils;

import java.util.Scanner;

/**
 *
 * @author Dell
 */
public class Utils {

    private static Scanner sc = new Scanner(System.in);

    public static void showIngreTitle() {
        System.out.println("\n************************************************");
        System.out.println(String.format("|%-8s|%-20s|%-7s|%4s|", "CODE", "NAME", "WEIGHT - kg", "UNIT"));
        System.out.print("************************************************");
    }

    public static String normalize(String str) {
        str = str.trim();
        String tokens[] = str.split("\\s+");
        return String.join(" ", tokens);
    }

    public static boolean confirmYesNo(String msg) {
        boolean check;
        System.out.print(msg);
        String confirm = sc.nextLine();
        confirm = confirm.trim();
        if ("y".equalsIgnoreCase(confirm)) {
            check = true;
        } else {
            check = false;
        }
        return check;
    }

    public static double updateDouble(String msg, double min, double max, double oldData) {
        boolean check = false;
        String s;
        double update = 0;
        do {
            System.out.print(msg);
            s = sc.nextLine();
            if (s.isEmpty()) {
                update = oldData;
                check = true;
                break;
            }
            update = Double.parseDouble(s);
            if (update >= min && update <= max) {
                check = true;
            } else {
                System.err.println(update + " is out of bound (" + min + ", " + max + ").");
            }

        } while (!check);

        return update;
    }

    public static int updateInteger(String msg, int min, int max, int oldData) {
        boolean check = false;
        String s;
        int update = 0;
        do {
            System.out.print(msg);
            s = sc.nextLine();
            if (s.isEmpty()) {
                update = oldData;
                check = true;
                break;
            }
            update = Integer.parseInt(s);
            if (update >= min && update <= max) {
                check = true;
            } else {
                System.err.println(update + " is out of bound (" + min + ", " + max + ").");
            }

        } while (!check);

        return update;
    }

    public static String updateString(String msg, String oldData) {
        boolean check = false;
        String update = "";
        do {
            try {
                System.out.print(msg);
                update = sc.nextLine();
                if (update.isEmpty()) {
                    update = oldData;
                }
                check = true;
            } catch (Exception e) {
                System.err.println("Error update string. Input again!!");
            }
        } while (!check);

        return update;
    }

    public static double getDouble(String msg, String errEmpty, double min, double max) {
        boolean check = false;
        String s = "";
        double input = 0;
        do {
            try {
                System.out.print(msg);
                s = sc.nextLine();
                if (s.isEmpty()) {
                    System.out.println(errEmpty);
                } else {
                    input = Double.parseDouble(s);
                    if (input < min || input > max) {
                        System.err.println(input + " is out of bound [" + min + ", " + max + "].");
                    } else {
                        check = true;
                    }
                }
            } catch (NumberFormatException e) {
                System.err.println("Error double num. Input again!!");
            }
        } while (!check);
        return input;
    }

    public static int getInteger(String msg, String errEmpty, int min, int max) {
        boolean check = false;
        String s = "";
        int input = 0;
        do {
            try {
                System.out.print(msg);
                s = sc.nextLine();
                if (s.isEmpty()) {
                    System.err.println(errEmpty);
                } else {
                    input = Integer.parseInt(s);
                    if (input < min || input > max) {
                        System.err.println(input + " is out of bound [" + min + ", " + max + "].");
                    } else {
                        check = true;
                    }
                }
            } catch (NumberFormatException e) {
                System.err.println("Error integer num. Input again!!");
            }
        } while (!check);
        return input;
    }

    public static String getStringReg(String msg, String pattern, String errEmpty) {
        boolean check = true;
        String input = "";
        do {
            try {
                System.out.print(msg);
                input = normalize(sc.nextLine());
                if (input.isEmpty()) {
                    System.err.println(errEmpty);
                } else if (!input.matches(pattern)) {
                    System.err.println("DOES NOT match the pattern (a-z or A-Z) + number.");
                } else {
                    check = false;
                }
            } catch (Exception e) {
                System.err.println("Invalid String. Input again!!");
            }
        } while (check);
        return input;
    }

    public static String getString(String welcome) {
        System.out.print(welcome);
        String result = normalize(sc.nextLine());
        return result;
    }

    public static String getString(String msg, String errEmpty) {
        boolean check = true;
        String input = "";
        do {
            try {
                System.out.print(msg);
                input = normalize(sc.nextLine());
                if (input.isEmpty()) {
                    System.err.println(errEmpty);
                } else {
                    check = false;
                }
            } catch (Exception e) {
                System.err.println("Invalid String. Input again!!");
            }
        } while (check);
        return input;
    }

    public static int getAnInteger(String welcome, String errorMsg, int min) {
        boolean check = true;
        int number = 0;
        do {
            try {
                System.out.print(welcome);
                number = Integer.parseInt(normalize(sc.nextLine()));
                if (number < min) {
                    System.err.println("Number must be large than " + min);
                } else {
                    check = false;
                }

            } catch (Exception e) {
                System.err.println(errorMsg + "\nNumber must be between " + min + " to " + Integer.MAX_VALUE);
            }
        } while (check || number < min);
        return number;
    }

    public static int getAnInteger(String welcome, String errorMsgException, int lowerBound, int upperBound) {
        int number, tmp;
        if (lowerBound > upperBound) {
            tmp = lowerBound;
            lowerBound = upperBound;
            upperBound = tmp;
        }
        while (true) {
            try {
                System.out.print(welcome);
                number = Integer.parseInt(normalize(sc.nextLine()));
                if (number < lowerBound || number > upperBound) {
                    System.err.println("Number must be between " + lowerBound + " to " + upperBound);
                } else {
                    return number;
                }

            } catch (Exception e) {
                System.err.println(errorMsgException + "\nNumber must be between " + lowerBound + " to " + upperBound);
            }
        }
    }

    public static boolean isBlank(String str) {
        return normalize(str).isEmpty();
    }

    public static int updateNumber(String welcome, String errmsg, int defaultNumber, int lowerBound, int upperBound) {
        int tmp, num;
        String upNum;
        if (lowerBound > upperBound) {
            tmp = lowerBound;
            lowerBound = upperBound;
            upperBound = tmp;
        }
        do {
            upNum = getString(welcome);
            if (upNum.isEmpty()) {
                return defaultNumber;
            }
            if (upNum.matches("[0-9]+")) {
                try {
                    num = Integer.parseInt(upNum);
                    if (num < lowerBound || num > upperBound) {
                        System.err.println("Number must be between " + lowerBound + " to " + upperBound);
                    } else {
                        return num;
                    }
                } catch (Exception e) {
                    System.err.println("Number must be between " + lowerBound + " to " + upperBound);
                }
            } else {
                System.err.println(errmsg);
            }
        } while (true);
    }

    public static int getAnInteger(String welcome, String errorMsg, String errorMsgException, int lowerBound, int upperBound) {
        int number, tmp;
        if (lowerBound > upperBound) {
            tmp = lowerBound;
            lowerBound = upperBound;
            upperBound = tmp;
        }
        while (true) {
            try {
                System.out.print(welcome);
                number = Integer.parseInt(normalize(sc.nextLine()));
                if (number < lowerBound || number > upperBound) {
                    System.err.println(errorMsg);
                } else {
                    return number;
                }

            } catch (Exception e) {
                System.err.println(errorMsgException + "\nNumber must be between " + lowerBound + " to " + upperBound);

            }
        }
    }

}
