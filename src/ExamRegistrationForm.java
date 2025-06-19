import javax.swing.*;
import javax.swing.border.TitledBorder;
import java.awt.*;
import java.awt.event.*;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Random;

public class ExamRegistrationForm extends JFrame implements ActionListener {
    JTextField nameField, emailField, phoneField, dobField, idField;
    JTextArea addressArea;
    JComboBox<String> genderBox, courseBox, centerBox, modeBox, idTypeBox;
    JCheckBox termsBox;
    JButton submitButton, refreshButton;
    JLabel rollLabel, marqueeLabel,headingLabel;
    String rollNo;
    Timer marqueeTimer;

    public ExamRegistrationForm() {
        setTitle("Online Exam Registration");
        setSize(720, 700);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(EXIT_ON_CLOSE);

        // Background panel with gradient
        JPanel background = new JPanel() {
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Graphics2D g2d = (Graphics2D) g;
                Color c1 = new Color(135, 206, 250);
                Color c2 = new Color(255, 182, 193);
                GradientPaint gp = new GradientPaint(0, 0, c1, getWidth(), getHeight(), c2);
                g2d.setPaint(gp);
                g2d.fillRect(0, 0, getWidth(), getHeight());
            }
        };
        background.setLayout(new BorderLayout(10, 10));
        
        JPanel topPanel = new JPanel();
        topPanel.setOpaque(false); // Make transparent so background gradient shows
        topPanel.setLayout(new BoxLayout(topPanel, BoxLayout.Y_AXIS));

        // Heading
        headingLabel = new JLabel("Online Exam Registration Form", JLabel.CENTER);
        headingLabel.setFont(new Font("SansSerif", Font.BOLD, 20));
        headingLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        headingLabel.setForeground(Color.RED);

        // Marquee
        marqueeLabel = new JLabel(" ✨ Welcome to the Online Exam Registration Portal ✨ ", JLabel.CENTER);
        marqueeLabel.setFont(new Font("Verdana", Font.BOLD, 14));
        marqueeLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        marqueeLabel.setForeground(Color.MAGENTA);
        startMarquee(); // Start moving text


        // Add both to topPanel
        topPanel.add(headingLabel);
        topPanel.add(Box.createRigidArea(new Dimension(0, 5))); // Spacing between them
        topPanel.add(marqueeLabel);

        // Now add the combined panel to the top of the frame
        background.add(topPanel, BorderLayout.NORTH);

        // Centered form panel
        JPanel formPanel = new JPanel(new GridBagLayout());
        formPanel.setBackground(new Color(120, 135, 75, 70));
        formPanel.setBorder(BorderFactory.createTitledBorder(
                BorderFactory.createLineBorder(Color.DARK_GRAY, 2),
                "Fill Your Details",
                TitledBorder.CENTER,
                TitledBorder.TOP,
                new Font("Arial", Font.BOLD, 15),
                Color.BLUE
        ));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(8, 8, 8, 8);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        String[] genders = {"Select Gender","Male", "Female", "Other"};
        String[] courses = {"Select Course","B.Sc", "BCA", "B.Tech", "MBA", "MCA", "Other"};
        String[] centers = {"Select Exam Center","Delhi", "Mumbai", "Bangalore", "Chennai", "Kolkata"};
        String[] modes = {"Select Mode Of Exam","Online", "Offline"};
        String[] idTypes = {"Select Document Type","Aadhar", "PAN", "Passport", "Driving License"};

        nameField = new JTextField(20);
        emailField = new JTextField(20);
        phoneField = new JTextField(10);
        dobField = new JTextField(10);
        genderBox = new JComboBox<>(genders);
        courseBox = new JComboBox<>(courses);
        centerBox = new JComboBox<>(centers);
        modeBox = new JComboBox<>(modes);
        idTypeBox = new JComboBox<>(idTypes);
        idField = new JTextField(15);
        addressArea = new JTextArea(3, 20);
        addressArea.setLineWrap(true);
        addressArea.setWrapStyleWord(true);
        JScrollPane addressScroll = new JScrollPane(addressArea);
        termsBox = new JCheckBox("I accept the terms and conditions");
        rollLabel = new JLabel("Roll No: Auto-generated");
        submitButton = new JButton("Register & Save");
        refreshButton = new JButton("Refresh");

        int row = 0;
        addField(formPanel, gbc, row++, "Full Name:", nameField);
        addField(formPanel, gbc, row++, "Email:", emailField);
        addField(formPanel, gbc, row++, "Phone:", phoneField);
        addField(formPanel, gbc, row++, "Date of Birth:", dobField);
        addField(formPanel, gbc, row++, "Gender:", genderBox);
        addField(formPanel, gbc, row++, "Course Name:", courseBox);
        addField(formPanel, gbc, row++, "Exam Center:", centerBox);
        addField(formPanel, gbc, row++, "Mode of Exam:", modeBox);
        addField(formPanel, gbc, row++, "ID Proof Type:", idTypeBox);
        addField(formPanel, gbc, row++, "ID Number:", idField);

        gbc.gridx = 0;
        gbc.gridy = row;
        gbc.gridwidth = 1;
        formPanel.add(new JLabel("Address:"), gbc);
        gbc.gridx = 1;
        formPanel.add(addressScroll, gbc);

        gbc.gridx = 0;
        gbc.gridy = ++row;
        gbc.gridwidth = 2;
        formPanel.add(termsBox, gbc);

        gbc.gridy++;
        formPanel.add(rollLabel, gbc);

        JPanel buttonPanel = new JPanel();
        buttonPanel.add(submitButton);
        buttonPanel.add(refreshButton);
        gbc.gridy++;
        formPanel.add(buttonPanel, gbc);

        background.add(formPanel, BorderLayout.CENTER);

        add(background);
        submitButton.addActionListener(this);
        refreshButton.addActionListener(e -> clearForm());
        
         // Live validation
        emailField.addFocusListener(new FocusAdapter() {
            public void focusLost(FocusEvent e) {
                if (!emailField.getText().matches("^[\\w.-]+@[\\w.-]+\\.[a-zA-Z]{2,}$")) {
                    JOptionPane.showMessageDialog(null, "Invalid Email Format!");
                }
            }
        });

        phoneField.addFocusListener(new FocusAdapter() {
            public void focusLost(FocusEvent e) {
                if (!phoneField.getText().matches("\\d{10}")) {
                    JOptionPane.showMessageDialog(null, "Phone number must be 10 digits!");
                }
            }
        });

        setVisible(true);
    }

    void addField(JPanel panel, GridBagConstraints gbc, int y, String label, Component input) {
        gbc.gridx = 0;
        gbc.gridy = y;
        gbc.gridwidth = 1;
        panel.add(new JLabel(label), gbc);
        gbc.gridx = 1;
        panel.add(input, gbc);
    }

    void clearForm() {
        nameField.setText("");
        emailField.setText("");
        phoneField.setText("");
        dobField.setText("");
        idField.setText("");
        addressArea.setText("");
        genderBox.setSelectedIndex(0);
        courseBox.setSelectedIndex(0);
        centerBox.setSelectedIndex(0);
        modeBox.setSelectedIndex(0);
        idTypeBox.setSelectedIndex(0);
        termsBox.setSelected(false);
        rollLabel.setText("Roll No: Auto-generated");
    }

    public void actionPerformed(ActionEvent e) {
        if (!termsBox.isSelected()) {
            JOptionPane.showMessageDialog(this, "You must accept the terms and conditions.");
            return;
        }

        if (nameField.getText().isEmpty() || emailField.getText().isEmpty()) {
            JOptionPane.showMessageDialog(this, "Please fill in the required fields.");
            return;
        }

        rollNo = "EXAM" + new Random().nextInt(999999);
        rollLabel.setText("Roll No: " + rollNo);

        try {
            FileWriter writer = new FileWriter("exam_registration_" + rollNo + ".txt");
            writer.write("===== Exam Registration Receipt =====\n");
            writer.write("Roll No       : " + rollNo + "\n");
            writer.write("Full Name     : " + nameField.getText() + "\n");
            writer.write("Email         : " + emailField.getText() + "\n");
            writer.write("Phone         : " + phoneField.getText() + "\n");
            writer.write("DOB           : " + dobField.getText() + "\n");
            writer.write("Gender        : " + genderBox.getSelectedItem() + "\n");
            writer.write("Course        : " + courseBox.getSelectedItem() + "\n");
            writer.write("Exam Center   : " + centerBox.getSelectedItem() + "\n");
            writer.write("Mode of Exam  : " + modeBox.getSelectedItem() + "\n");
            writer.write("ID Proof Type : " + idTypeBox.getSelectedItem() + "\n");
            writer.write("ID Number     : " + idField.getText() + "\n");
            writer.write("Address       : " + addressArea.getText() + "\n");
            writer.write("====================================\n");
            writer.close();

            JOptionPane.showMessageDialog(this,
                "Registration Successful!\nReceipt saved as: exam_registration_" + rollNo + ".txt",
                "Success", JOptionPane.INFORMATION_MESSAGE);
        } catch (IOException ex) {
            JOptionPane.showMessageDialog(this, "Error saving registration: " + ex.getMessage());
        }
    }

    void startMarquee() {
        Timer timer = new Timer(250, new ActionListener() {
            String text = marqueeLabel.getText();

            public void actionPerformed(ActionEvent e) {
                text = text.substring(1) + text.charAt(0);
                marqueeLabel.setText(text);
            }
        });
        timer.start();
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(ExamRegistrationForm::new);
    }
}
