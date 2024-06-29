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
            curUser = ATM.mainMenuPromt(theBank, sc);

            ATM.printUserMenu(curUser, sc);

        }
    }
}

