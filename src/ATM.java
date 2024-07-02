import java.util.Scanner;

//TIP To <b>Run</b> code, press <shortcut actionId="Run"/> or
// click the <icon src="AllIcons.Actions.Execute"/> icon in the gutter.
public class ATM {
    public static void main(String[] args) {
        //init scanner
        Scanner sc = new Scanner(System.in);

        //init bank
        Bank theBank = new Bank("Bank of Cordoba");

        //add user which also creates a savings account
        User aUser = theBank.addUser("Jeronimo Luis", "De Cabrera", "1234");

        // add a checking account or the user
        Account newAccount = new Account("Checking", aUser, theBank);
        aUser.addAccount(newAccount);
        theBank.addAccount(newAccount);

        User curUser;
        while (true) {
            // stay in the login prompt until successful login
            curUser = ATM.mainMenuPrompt(theBank, sc);

            ATM.printUserMenu(curUser, sc);

        }
    }

    public static User mainMenuPrompt(Bank theBank, Scanner sc){
        String userID;
        String pin;
        User authUser;

        do {

            System.out.printf("\n\nWelcome to %s\n\n", theBank.getName());
            System.out.print("Enter userID: ");
            userID = sc.nextLine();
            System.out.print("Enter pin: ");
            pin = sc.nextLine();
            // find the user
            authUser = theBank.userLogin(userID, pin);
            if(authUser == null){
                System.out.println("Incorrect user ID/Pin combination. Please try again.");
            }

        } while (authUser == null);
        return authUser;
    }

    public static void printUserMenu(User theUser, Scanner sc) {
        // print a summary of the user's accounts
        theUser.printAccountsSummary();

        int choice;

        //user menu
        do {
            System.out.printf("Welcome! %s, what would you like to do?\n", theUser.getFirstName());
            System.out.println("    1) Show account transaction history");
            System.out.println("    2) Withdraw");
            System.out.println("    3) Deposit");
            System.out.println("    4) Transfer");
            System.out.println("    5) Quit");
            System.out.println();
            System.out.println("Enter choice: ");
            choice = sc.nextInt();

            if (choice < 1 || choice > 5){
                System.out.println("Invalid choice. Please choose 1-5");
            }
        } while (choice < 1 || choice > 5);

        switch (choice){
            case 1:
                ATM.showTransHistory(theUser, sc);
                break;
            case 2:
                ATM.withdrawFunds(theUser, sc);
                break;
            case 3:
                ATM.depositFunds(theUser, sc);
                break;
            case 4:
                ATM.transferFunds(theUser, sc);
                break;
        }
        if (choice != 5){
            ATM.printUserMenu(theUser, sc);
        }
    }

    public static void showTransHistory(User theUser, Scanner sc){
        int theAcct;

        //get account whose trans history to look at
        do {
            System.out.printf("Enter the number (1-%d) of the account you want to see: ", theUser.numAccounts());
            theAcct = sc.nextInt() - 1;
            if (theAcct <0 || theAcct >= theUser.numAccounts()){
                System.out.println("Invalid account. Please try again");
            }
        } while (theAcct <0 || theAcct >= theUser.numAccounts());

        theUser.printAcctTransHistory(theAcct);
    }

    public static void transferFunds(User theUser, Scanner sc){
        // init
        int fromAcct;
        int toAcct;
        double amount;
        double acctBal;

        // get the acct to transfer from
        do {
            System.out.printf("Enter the number (1-%d) of the account to transfer from: ", theUser.numAccounts());
            fromAcct = sc.nextInt()-1;
            if(fromAcct<0|| fromAcct >= theUser.numAccounts()){
                System.out.println("Invalid account. Please try again");
            }
        } while(fromAcct<0|| fromAcct >= theUser.numAccounts());

        acctBal = theUser.getAcctBalance(fromAcct);

        // get the acct to transfer to
        do {
            System.out.printf("Enter the number (1-%d) of the account to transfer to: ", theUser.numAccounts());
            toAcct = sc.nextInt()-1;
            if(toAcct<0|| toAcct >= theUser.numAccounts()){
                System.out.println("Invalid account. Please try again");
            }
        } while(toAcct<0|| toAcct >= theUser.numAccounts());


        // get the amount to transfer
        do{
            System.out.printf("Enter the amount to transfer (max $%.02f): $", acctBal);
            amount = sc.nextDouble();
            if(amount < 0){
                System.out.println("Amount must be greater than 0");
            } else if (amount > acctBal){
                System.out.printf("Amount must not be greater than balance of $%.02f", acctBal);
            }
        } while (amount <0 || amount > acctBal);

        // do the transfer
        theUser.addAcctTransaction(fromAcct, -1*amount, String.format("Transfer to account %s", theUser.getAcctUUID(toAcct)));
        theUser.addAcctTransaction(toAcct, amount, String.format("Transfer to account %s", theUser.getAcctUUID(toAcct)));
    }


    /**
     * Process a fund withdraw from an account
     * @param theUser the logged-in user object
     * @param sc the Scanner object for user input
     */
    public static void withdrawFunds(User theUser, Scanner sc){
        // init
        int fromAcct;
        double amount;
        double acctBal;
        String memo;

        // get the acct to transfer from
        do {
            System.out.printf("Enter the number (1-%d) of the account to withdraw from: ", theUser.numAccounts());
            fromAcct = sc.nextInt()-1;
            if(fromAcct<0|| fromAcct >= theUser.numAccounts()){
                System.out.println("Invalid account. Please try again");
            }
        } while(fromAcct<0|| fromAcct >= theUser.numAccounts());

        acctBal = theUser.getAcctBalance(fromAcct);

        // get the amount to transfer
        do{
            System.out.printf("Enter the amount to withdraw (max $%.02f): $", acctBal);
            amount = sc.nextDouble();
            if(amount < 0){
                System.out.println("Amount must be greater than 0");
            } else if (amount > acctBal){
                System.out.printf("Amount must not be greater than balance of $%.02f", acctBal);
            }
        } while (amount < 0 || amount > acctBal);

        // gobble up the rest of previous input
        System.out.println("Enter a memo: ");
        memo = sc.nextLine();

        theUser.addAcctTransaction(fromAcct, -1*amount, memo);
    }

    /**
     * Process a fund deposit to an account
     * @param theUser the logged-in user
     * @param sc the Scanner object for user input
     */
    public static void depositFunds(User theUser, Scanner sc){
        // init
        int toAcct;
        double amount;
        double acctBal;
        String memo;

        // get the acct to transfer from
        do {
            System.out.printf("Enter the number (1-%d) of the account to deposit to: ", theUser.numAccounts());
            toAcct = sc.nextInt()-1;
            if(toAcct < 0|| toAcct >= theUser.numAccounts()){
                System.out.println("Invalid account. Please try again");
            }
        } while(toAcct<0|| toAcct >= theUser.numAccounts());

        acctBal = theUser.getAcctBalance(toAcct);

        // get the amount to transfer
        do{
            System.out.printf("Enter the amount to deposit (max $%.02f): $", acctBal);
            amount = sc.nextDouble();
            if(amount < 0){
                System.out.println("Amount must be greater than 0");
            }
        } while (amount < 0);

        // gobble up the rest of previous input
        System.out.println("Enter a memo: ");
        memo = sc.nextLine();

        theUser.addAcctTransaction(toAcct, amount, memo);
    }
}

