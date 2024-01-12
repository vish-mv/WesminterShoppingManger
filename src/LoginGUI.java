import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;


class LoginGUI extends JFrame {
    private List<User> users;
    private JTextField usernameField;
    private JPasswordField passwordField;

    private ShoppingGUI shoppingGUI;
    private JLabel login;
    public LoginGUI(ShoppingGUI shoppingGUI, List<User> users) {
        this.shoppingGUI = shoppingGUI;
        this.users = users;


        setTitle("Login");
        setSize(500, 200);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);
        setMinimumSize(new Dimension(700, 500));

        // Main panel with BorderLayout
        JPanel mainPanel = new JPanel(new BorderLayout());

        // Create a panel for the form
        JPanel formPanel = new JPanel(new GridLayout(3, 2));

        // Center-aligned title label
        JLabel login = new JLabel("Login", SwingConstants.CENTER);
        login.setFont(new Font("Arial", Font.BOLD, 24));
        login.setBorder(BorderFactory.createEmptyBorder(10, 0, 20, 0));

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

        // Add components to the form panel
        formPanel.add(usernameLabel);
        formPanel.add(usernameField);
        formPanel.add(passwordLabel);
        formPanel.add(passwordField);


        // Add the title label to the main panel
        mainPanel.add(login, BorderLayout.NORTH);

        // Add the form panel to the main panel
        mainPanel.add(formPanel, BorderLayout.CENTER);
        mainPanel.add(loginButton,BorderLayout.SOUTH);

        // Add the main panel to the frame
        add(mainPanel);
    }
    private void authenticateUser(String username, String password) {
        WestminsterShoppingManager shoppingManager = new WestminsterShoppingManager();
        User loggedUser = shoppingManager.authenticateUser(username, password);

        if (loggedUser != null) {
            // Authentication successful
            JOptionPane.showMessageDialog(LoginGUI.this, "Login Successful. Welcome, " + loggedUser.getName());

            // Update the ShoppingGUI login button

            shoppingGUI.updateLoginButton(loggedUser);

            dispose();
        } else {
            // Authentication failed
            JOptionPane.showMessageDialog(LoginGUI.this, "Invalid username or password. Please try again.", "Login Failed", JOptionPane.ERROR_MESSAGE);
        }
    }
}