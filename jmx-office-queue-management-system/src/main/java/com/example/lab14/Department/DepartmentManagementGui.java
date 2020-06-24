package com.example.lab14.Department;

import javax.swing.*;
import java.awt.*;

public class DepartmentManagementGui extends JFrame {
    private JLabel categoryLabel = new JLabel("Category: ");
    private TextField categoryTextField = new TextField();
    private JLabel priorityLabel = new JLabel("Priority: ");
    private TextField priorityTextField = new TextField();
    private JButton addCategoryButton = new JButton("add");
    private JButton updateCategoryButton = new JButton("update");
    private JButton removeCategoryButton = new JButton("remove");

    private Department department;

    public DepartmentManagementGui(Department department) {
        this.department = department;
        setLayout(new FlowLayout());
        setSize(240,180);

        init();

        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        setVisible(true);
    }

    private void init() {
        add(categoryLabel);
        add(categoryTextField);
        add(priorityLabel);
        add(priorityTextField);

        addCategoryButton.addActionListener(e -> addCategory());
        add(addCategoryButton);

        updateCategoryButton.addActionListener(e -> updateCategory());
        add(updateCategoryButton);

        removeCategoryButton.addActionListener(e -> removeCategory());
        add(removeCategoryButton);
    }

    private void addCategory() {
        if(department.addCategory(categoryTextField.getText(), Integer.parseInt(priorityTextField.getText())))
            department.sentGeneralInfoAll("New category '" + categoryTextField.getText() + "' of priority " + priorityTextField.getText() + " added");
    }

    private void updateCategory() {
        if(department.updateCategory(categoryTextField.getText(), Integer.parseInt(priorityTextField.getText())))
            department.sentGeneralInfoAll("Category '" + categoryTextField.getText() + "' priority updated to " + priorityTextField.getText());
    }

    private void removeCategory() {
        if(department.deleteCategory(categoryTextField.getText()))
            department.sentGeneralInfoAll("Category '" + categoryTextField.getText() + "' removed");
    }


}
