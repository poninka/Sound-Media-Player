package com.soundmediaplayer.ui;

import com.soundmediaplayer.config.ConfigManager;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 * Dialog for configuring Spotify API tokens at runtime.
 * Allows users to override the config.properties file values.
 */
public class TokenConfigDialog extends JDialog {
    private static final long serialVersionUID = 1L;
    private JTextField clientIdField;
    private JPasswordField clientSecretField;
    private boolean confirmed = false;

    public TokenConfigDialog(JFrame parent) {
        super(parent, "Configure Spotify API Tokens", true);
        setSize(400, 200);
        setLocationRelativeTo(parent);
        setLayout(new BorderLayout());

        JPanel mainPanel = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.anchor = GridBagConstraints.WEST;

        // Client ID field
        gbc.gridx = 0;
        gbc.gridy = 0;
        mainPanel.add(new JLabel("Client ID:"), gbc);
        gbc.gridx = 1;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.weightx = 1.0;
        clientIdField = new JTextField(20);
        clientIdField.setText(ConfigManager.getClientId());
        mainPanel.add(clientIdField, gbc);

        // Client Secret field
        gbc.gridx = 0;
        gbc.gridy = 1;
        gbc.fill = GridBagConstraints.NONE;
        gbc.weightx = 0;
        mainPanel.add(new JLabel("Client Secret:"), gbc);
        gbc.gridx = 1;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.weightx = 1.0;
        clientSecretField = new JPasswordField(20);
        clientSecretField.setText(ConfigManager.getClientSecret());
        mainPanel.add(clientSecretField, gbc);

        // Info label
        gbc.gridx = 0;
        gbc.gridy = 2;
        gbc.gridwidth = 2;
        gbc.fill = GridBagConstraints.NONE;
        gbc.weightx = 0;
        JLabel infoLabel = new JLabel("<html><small>These values override config.properties</small></html>");
        mainPanel.add(infoLabel, gbc);

        // Buttons
        JPanel buttonPanel = new JPanel(new FlowLayout());
        JButton saveButton = new JButton("Save");
        JButton cancelButton = new JButton("Cancel");
        JButton clearButton = new JButton("Clear Override");

        saveButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String clientId = clientIdField.getText().trim();
                String clientSecret = new String(clientSecretField.getPassword()).trim();
                
                if (clientId.isEmpty() || clientSecret.isEmpty()) {
                    JOptionPane.showMessageDialog(TokenConfigDialog.this,
                            "Both Client ID and Client Secret are required.",
                            "Validation Error", JOptionPane.ERROR_MESSAGE);
                    return;
                }
                
                ConfigManager.setClientIdOverride(clientId);
                ConfigManager.setClientSecretOverride(clientSecret);
                confirmed = true;
                dispose();
            }
        });

        cancelButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                confirmed = false;
                dispose();
            }
        });

        clearButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                ConfigManager.clearOverrides();
                clientIdField.setText(ConfigManager.getClientId());
                clientSecretField.setText(ConfigManager.getClientSecret());
                JOptionPane.showMessageDialog(TokenConfigDialog.this,
                        "Overrides cleared. Using config.properties values.",
                        "Info", JOptionPane.INFORMATION_MESSAGE);
            }
        });

        buttonPanel.add(saveButton);
        buttonPanel.add(cancelButton);
        buttonPanel.add(clearButton);

        add(mainPanel, BorderLayout.CENTER);
        add(buttonPanel, BorderLayout.SOUTH);
    }

    public boolean isConfirmed() {
        return confirmed;
    }
}

