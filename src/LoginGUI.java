import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;


class LoginGUI extends JFrame {
    private List<User> users;
    private JTextField usernameField;
    private JPasswordField passwordField;

    public LoginGUI(List<User> users) {
        this.users = users;

        setTitle("Login");
        setSize(400, 200);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        JPanel panel = new JPanel();
        panel.setLayout(new GridLayout(3, 2));

        JLabel usernameLabel = new JLabel("Username:");
        JLabel passwordLabel = new JLabel("Password:");

        usernameField = new JTextField();
        passwordField = new JPasswordField();

        JButton loginButton = new JButton("Login");

        loginButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String enteredUsername = usernameField.getText();
                String enteredPassword = new String(passwordField.getPassword());
                authenticateUser(enteredUsername, enteredPassword);
            }
        });


        panel.add(usernameLabel);
        panel.add(usernameField);
        panel.add(passwordLabel);
        panel.add(passwordField);
        panel.add(loginButton);

        add(panel);
    }
    private void authenticateUser(String username, String password) {
        WestminsterShoppingManager shoppingManager = new WestminsterShoppingManager();
        User loggedUser = shoppingManager.authenticateUser(username, password);

        if (loggedUser != null) {
            // Authentication successful
            JOptionPane.showMessageDialog(LoginGUI.this, "Login Successful. Welcome, " + loggedUser.getUsername());

            // Update the ShoppingGUI login button
            ((ShoppingGUI) SwingUtilities.getWindowAncestor(LoginGUI.this)).updateLoginButton(loggedUser);

            dispose();
        } else {
            // Authentication failed
            JOptionPane.showMessageDialog(LoginGUI.this, "Invalid username or password. Please try again.", "Login Failed", JOptionPane.ERROR_MESSAGE);
        }
    }
}