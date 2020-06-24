package com.example.lab14.Applicant;

public interface ApplicantListener {
    void ticketUpdated(String ticket);
    void statusUpdated(String status);
    void postsUpdated(String postInfo);
    void awaitingTicketsUpdated(String ticketInfo);
    void generalInfoUpdated(String info);
}
