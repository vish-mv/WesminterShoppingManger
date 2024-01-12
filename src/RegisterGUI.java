import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;

public class RegisterGUI extends JFrame {
    private List<User> userList;
    private JTextField usernameField;
    private JPasswordField passwordField;
    private JButton registerButton;
    private JTextField nameField;

    public RegisterGUI(List<User> userList) {
        this.userList = userList;

        setTitle("Register");
        setSize(400, 200);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        JPanel panel = new JPanel();
        panel.setLayout(new GridLayout(4, 2));

        JLabel nameLabel = new JLabel("Name:");
        JLabel usernameLabel = new JLabel("Username:");
        JLabel passwordLabel = new JLabel("Password:");

        nameField = new JTextField();
        usernameField = new JTextField();
        passwordField = new JPasswordField();
        registerButton = new JButton("Register");

        registerButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String enteredName = nameField.getText();
                String enteredUsername = usernameField.getText();
                String enteredPassword = new String(passwordField.getPassword());

                if (validateRegistration(enteredUsername, enteredPassword)) {
                    registerUser(enteredName,enteredUsername, enteredPassword,0,"No Purchases");
                    dispose(); // Close the registration window after successful registration
                }
            }
        });
        panel.add(nameLabel);
        panel.add(nameField);
        panel.add(usernameLabel);
        panel.add(usernameField);
        panel.add(passwordLabel);
        panel.add(passwordField);
        panel.add(registerButton);

        add(panel);
    }

    private boolean validateRegistration(String username, String password) {
        // Add your validation logic here
        if (username.isEmpty() || password.isEmpty()) {
            JOptionPane.showMessageDialog(RegisterGUI.this, "Username and password cannot be empty.", "Registration Failed", JOptionPane.ERROR_MESSAGE);
            return false;
        }

        // Check if the username is unique
        for (User user : userList) {
            if (user.getUsername().equals(username)) {
                JOptionPane.showMessageDialog(RegisterGUI.this, "Username already exists. Please choose a different username.", "Registration Failed", JOptionPane.ERROR_MESSAGE);
                return false;
            }
        }

        // Additional validation logic if needed

        return true;
    }

    private void registerUser(String name,String username, String password,int purchase_count, String bought_items) {
        // Create a new user and add it to the user list
        User newUser = new User(name,username, password,purchase_count,bought_items);
        userList.add(newUser);

        JOptionPane.showMessageDialog(RegisterGUI.this, "Registration Successful. Welcome, " + username);
    }

    public static void main(String[] args) {
        // Example usage:
        // Assuming you have a user list (List<User> userList) in your WestminsterShoppingManager
        WestminsterShoppingManager manager = new WestminsterShoppingManager();
        List<User> userList = manager.getUserList();

        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                new RegisterGUI(userList).setVisible(true);
            }
        });
    }
}
