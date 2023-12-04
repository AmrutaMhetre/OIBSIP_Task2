package oasis_4;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

class BankAccount {
    String name;
    String user_Name;
    String password;
    String accNo;
    float balance = 10000f;
    int transactions = 0;
    StringBuilder transaction_History = new StringBuilder();

    public void register(String name, String user_Name, String password, String accNo) {
        this.name = name;
        this.user_Name = user_Name;
        this.password = password;
        this.accNo = accNo;
    }

    public boolean login(String enteredUsername, String enteredPassword) {
        return user_Name != null && user_Name.equals(enteredUsername) && password.equals(enteredPassword);
    }

    public void withdraw(float amount) {
        if (balance >= amount) {
            transactions++;
            balance -= amount;
            String str = amount + " Rs Withdrawn\n";
            transaction_History.append(str);
        } else {
            JOptionPane.showMessageDialog(null, "Insufficient Balance!", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    public void deposit(float amount) {
        if (amount <= 10000f) {
            transactions++;
            balance += amount;
            String str = amount + " Rs Deposited\n";
            transaction_History.append(str);
        } else {
            JOptionPane.showMessageDialog(null, "Sorry...Limit is 10000.00", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    public void transfer(String recipient, float amount) {
        if (balance >= amount && amount <= 5000f) {
            transactions++;
            balance -= amount;
            String str = amount + " Rs Transferred to " + recipient + "\n";
            transaction_History.append(str);
        } else if (amount > 5000f) {
            JOptionPane.showMessageDialog(null, "Sorry...Limit is 5000.00", "Error", JOptionPane.ERROR_MESSAGE);
        } else {
            JOptionPane.showMessageDialog(null, "Insufficient Balance!", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    public String checkBalance() {
        return String.valueOf(balance);
    }

    public String getTransactionHistory() {
        if (transactions == 0) {
            return "Transaction History is Empty";
        } else {
            return transaction_History.toString();
        }
    }
}

class RegistrationPanel extends JPanel {
    private JTextField nameField;
    private JTextField usernameField;
    private JPasswordField passwordField;
    private JTextField accNoField;
    private ATMFrame parentFrame;

    public RegistrationPanel(ATMFrame frame) {
        parentFrame = frame;
        setLayout(new GridLayout(5, 2, 10, 10));

        nameField = new JTextField();
        usernameField = new JTextField();
        passwordField = new JPasswordField();
        accNoField = new JTextField();

        add(new JLabel("Enter Your Name:"));
        add(nameField);
        add(new JLabel("Enter Your Username:"));
        add(usernameField);
        add(new JLabel("Enter Your Password:"));
        add(passwordField);
        add(new JLabel("Enter Your Account Number:"));
        add(accNoField);

        JButton registerButton = new JButton("Register");
        add(registerButton);

        registerButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String name = nameField.getText();
                String username = usernameField.getText();
                String password = new String(passwordField.getPassword());
                String accNo = accNoField.getText();

                if (parentFrame.getBankAccount() == null) {
                    parentFrame.setBankAccount(new BankAccount());
                }

                parentFrame.getBankAccount().register(name, username, password, accNo);

                parentFrame.showLogin();
            }
        });
    }
}

class LoginPanel extends JPanel {
    private JTextField usernameField;
    private JPasswordField passwordField;
    private ATMFrame parentFrame;

    public LoginPanel(ATMFrame frame) {
        parentFrame = frame;
        setLayout(new GridLayout(3, 2, 10, 10));

        usernameField = new JTextField();
        passwordField = new JPasswordField();

        add(new JLabel("Enter Your Username:"));
        add(usernameField);
        add(new JLabel("Enter Your Password:"));
        add(passwordField);

        JButton loginButton = new JButton("Login");
        add(loginButton);

        loginButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String username = usernameField.getText();
                String password = new String(passwordField.getPassword());

                BankAccount bankAccount = parentFrame.getBankAccount();

                if (bankAccount != null && bankAccount.login(username, password)) {
                    parentFrame.showMainMenu();
                } else {
                    JOptionPane.showMessageDialog(null, "Login failed. Please try again.");
                }
            }
        });
    }
}

class MainMenuPanel extends JPanel {
    private ATMFrame parentFrame;

    public MainMenuPanel(ATMFrame frame) {
        parentFrame = frame;
        setLayout(new GridLayout(7, 1, 10, 10));

        JButton withdrawButton = new JButton("Withdraw");
        JButton depositButton = new JButton("Deposit");
        JButton transferButton = new JButton("Transfer");
        JButton checkBalanceButton = new JButton("Check Balance");
        JButton transactionHistoryButton = new JButton("Transaction History");
        JButton exitButton = new JButton("Exit");

        String welcomeMessage = parentFrame.getBankAccount() != null ?
                "WELCOME BACK " + parentFrame.getBankAccount().name :
                "Welcome to the ATM";

        add(new JLabel(welcomeMessage));
        add(withdrawButton);
        add(depositButton);
        add(transferButton);
        add(checkBalanceButton);
        add(transactionHistoryButton);
        add(exitButton);

        withdrawButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                float amount = parentFrame.takeFloatInput("Enter amount to withdraw:");
                parentFrame.getBankAccount().withdraw(amount);
            }
        });

        depositButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                float amount = parentFrame.takeFloatInput("Enter amount to deposit:");
                parentFrame.getBankAccount().deposit(amount);
            }
        });

        transferButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String recipient = parentFrame.takeStringInput("Enter Recipient's Name:");
                float amount = parentFrame.takeFloatInput("Enter amount to transfer:");
                parentFrame.getBankAccount().transfer(recipient, amount);
            }
        });

        checkBalanceButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                parentFrame.showMessage("Balance: " + parentFrame.getBankAccount().checkBalance() + " Rs");
            }
        });

        transactionHistoryButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                parentFrame.showMessage(parentFrame.getBankAccount().getTransactionHistory());
            }
        });

        exitButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                System.exit(0);
            }
        });
    }
}

class ATMFrame extends JFrame {
    private BankAccount bankAccount;
    private CardLayout cardLayout;
    private JPanel cardPanel;

    public ATMFrame() {
        super("ATM Interface");
        setSize(400, 300);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        cardLayout = new CardLayout();
        cardPanel = new JPanel(cardLayout);

        cardPanel.add(new RegistrationPanel(this), "registration");
        cardPanel.add(new LoginPanel(this), "login");
        cardPanel.add(new MainMenuPanel(this), "mainMenu");

        add(cardPanel);

        cardLayout.show(cardPanel, "registration");  // Show registration panel initially
    }

    public void showLogin() {
        cardLayout.show(cardPanel, "login");
    }

    public void showMainMenu() {
        cardLayout.show(cardPanel, "mainMenu");
    }

    public BankAccount getBankAccount() {
        return bankAccount;
    }

    public void setBankAccount(BankAccount bankAccount) {
        this.bankAccount = bankAccount;
    }

    public float takeFloatInput(String message) {
        String input = JOptionPane.showInputDialog(null, message);
        try {
            return Float.parseFloat(input);
        } catch (NumberFormatException e) {
            showMessage("Invalid input! Please enter a valid number.");
            return takeFloatInput(message);
        }
    }

    public String takeStringInput(String message) {
        return JOptionPane.showInputDialog(null, message);
    }

    public void showMessage(String message) {
        JOptionPane.showMessageDialog(null, message);
    }
}

public class ATMInterfaceGUI {
    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                new ATMFrame().setVisible(true);
            }
        });
    }
}
