package com.example.lab14.Applicant;

import javax.swing.*;
import java.awt.*;

public class ApplicantGui extends JFrame implements ApplicantListener {
    private JLabel generalInfoLabel = new JLabel("\t");
    private JLabel ticketLabel = new JLabel("Ticket: none");
    private JLabel statusLabel = new JLabel();

    private JPanel ticketPanel = new JPanel();
    private JLabel categoryLabel = new JLabel("category: ");
    private TextField ticketCategoryTextField = new TextField();
    private JButton ticketButton = new JButton("Get ticket");

    private JPanel infoPanel = new JPanel();
    private JTextArea infoTextArea = new JTextArea("No info");

    private JPanel postsPanel = new JPanel();
    private JTextArea postsTextArea = new JTextArea("No posts");

    private JPanel awaitingTicketsPanel = new JPanel();
    private JLabel awaitingTicketsLabel = new JLabel("Awaiting tickets: ");
    private JTextArea awaitingTicketsTextArea = new JTextArea();

    private ApplicantGuiListener applicantGuiListener;

    public ApplicantGui(ApplicantGuiListener applicantGuiListener) {
        setSize(600,450);
        setLayout(new GridLayout(0,1));
        this.applicantGuiListener = applicantGuiListener;

        initInfoPanel();
        initTicketPanel();
        initPostsPanel();
        initAwaitingTickets();

        add(generalInfoLabel);
        add(ticketLabel);
        add(statusLabel);
        add(infoPanel);
        add(ticketPanel);
        add(postsPanel);
        add(awaitingTicketsPanel);

        setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
        setVisible(true);
    }

    private void initTicketPanel() {
        ticketPanel.setLayout(new FlowLayout());
        ticketPanel.add(categoryLabel);
        ticketCategoryTextField.setSize(80,30);
        ticketPanel.add(ticketCategoryTextField);

        ticketButton.addActionListener(e -> getNewTicket());
        ticketPanel.add(ticketButton);
    }

    private void initInfoPanel() {
        infoTextArea.setBackground(getBackground());
        infoTextArea.setText(applicantGuiListener.getInfo());
        infoPanel.add(infoTextArea);
    }

    private void initPostsPanel() {
        postsTextArea.setBackground(getBackground());
        postsPanel.add(postsTextArea);
    }

    private void initAwaitingTickets() {
        awaitingTicketsTextArea.setBackground(getBackground());
        awaitingTicketsPanel.add(awaitingTicketsLabel);
        awaitingTicketsPanel.add(awaitingTicketsTextArea);
    }

    private void getNewTicket() {
        applicantGuiListener.requestTicket(ticketCategoryTextField.getText());
    }

    @Override
    public void ticketUpdated(String ticket) {
        if(ticket == null)
            ticket = "None";
        ticketLabel.setText("Ticket: " + ticket);
    }

    @Override
    public void statusUpdated(String status) {
        statusLabel.setText("Status: " + status);
    }

    @Override
    public void postsUpdated(String postInfo) {
        postsTextArea.setText(postInfo);
    }

    @Override
    public void awaitingTicketsUpdated(String ticketInfo) {
        awaitingTicketsTextArea.setText(ticketInfo);
    }

    @Override
    public void generalInfoUpdated(String info) {
        generalInfoLabel.setText(info);
    }
}
