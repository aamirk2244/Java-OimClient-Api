package com.example.gui;

import com.example.Login;
import com.example.Users;

import com.example.gui.components.PopupBox;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import java.util.HashMap;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

public class CreateUserScreen {
    JTextField userLogin = new JTextField(10);
    JTextField userFirstName = new JTextField(10);
    JTextField userLastName = new JTextField(10);
    JTextField userPassword = new JTextField(10);
    JFrame frame;
    public CreateUserScreen() {
        super();
    }
    
    public JFrame create() {
        frame = new JFrame("Create User");

        JLabel label1 = new JLabel("User Login:");
        JLabel label2 = new JLabel("First Name:");
        JLabel label3 = new JLabel("Last Name:");
        JLabel label4 = new JLabel("Password:");

        JPanel inputPanel = new JPanel(new GridLayout(4, 2, 5, 5));
        inputPanel.add(label1);
        inputPanel.add(userLogin);
        inputPanel.add(label2);
        inputPanel.add(userFirstName);
        inputPanel.add(label3);
        inputPanel.add(userLastName);
        inputPanel.add(label4);
        inputPanel.add(userPassword);

        JButton submitButton = new JButton("Submit");
        submitButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                submitUser();
            }
        });

        frame.setSize(560, 200);

        frame.getContentPane().add(inputPanel, BorderLayout.CENTER);
        frame.getContentPane().add(submitButton, BorderLayout.SOUTH);

        frame.setLocationRelativeTo(null); // Center the create user screen

        return frame;
    }
    
    private void submitUser(){
        HashMap<String, Object> userParams = new HashMap<String, Object>();
        System.out.println(  userLogin.getText());
        userParams.put("User Login",(String) userLogin.getText());
        userParams.put("First Name", (String) userFirstName.getText());
        userParams.put("Last Name", (String) userLastName.getText());
        userParams.put(oracle.iam.identity.utils.Constants.PASSWORD, (String) userPassword.getText());
        System.out.println(userParams);
        String errorMessage  = "An error occurred while creating user: ";
        try {
          new Users(new Login().getSession()).create(userParams);
            PopupBox.showSuccessDialog(frame, "User Created Successfully");
        } catch (Exception e) {
          System.out.println(errorMessage +  e.getMessage());
          PopupBox.showErrorDialog(frame, "User Creation Error",  e.getMessage());
        }
    }
}
