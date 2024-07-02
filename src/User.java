import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.security.MessageDigest;

public class User {
    private String firstName;
    private String lastName;
    private final String uuid;

    // the MD5 has of the user's pin.
    private byte pinHash[];

    private ArrayList<Account> accounts;

    /*
    * Create a new User
    * @param firstName the user's first name
    * @param lastName the user's last name
    * @param pin the user's account pin
    * @param theBank the Bank object that the user is a costumer of
    */

    public User(String firstName, String lastName, String pin, Bank theBank){
        this.firstName = firstName;
        this.lastName = lastName;

        // store the pin's MD5 hash, for security reasons
        try {
            MessageDigest md = MessageDigest.getInstance("MD5");
            this.pinHash = md.digest(pin.getBytes());
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException(e);
        }

        // get a new unique universal id for the user
        this.uuid = theBank.getNewUserUUID();

        // list of accounts
        this.accounts = new ArrayList<Account>();

        // print log message
        System.out.printf("New user %s, %s with ID %s created, \n", lastName, firstName, this.uuid);
    }

    public void addAccount(Account anAcct){
        this.accounts.add(anAcct);
    }

    public String getUUID(){
        return this.uuid;
    }

    public boolean validatePin(String aPin){
        try {
            MessageDigest md = MessageDigest.getInstance("MD5");
            return MessageDigest.isEqual(md.digest(aPin.getBytes()), this.pinHash);
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException(e);
        }
    }

    public String getFirstName(){
        return this.firstName;
    }

    public void printAccountsSummary(){
        System.out.printf("\n\n%s's accounts summary\n", this.firstName);
        for(int a=0;a<this.accounts.size();a++){
            System.out.printf("%d) %s\n", a + 1, this.accounts.get(a).getSummaryLine());
        }
        System.out.println();
    }

    public int numAccounts(){
        return this.accounts.size();
    }

    public void printAcctTransHistory(int accIdx){
        this.accounts.get(accIdx).printTransHistory();
    }

    public double getAcctBalance(int accIdx){
        return this.accounts.get(accIdx).getBalance();
    }

    public String getAcctUUID(int acctIdx){
        return this.accounts.get(acctIdx).getUUID();
    }

    public void addAcctTransaction(int acctIdx, double amount, String memo){
        this.accounts.get(acctIdx).addTransaction(amount, memo);
    }
}
