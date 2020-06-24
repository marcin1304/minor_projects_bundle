package com.example.lab14.Applicant;

public interface ApplicantMXBean {
    void postsUpdate(String postsInfo);
    void awaitingTicketsUpdate(String ticketsInfo);
    void movedToPost();
    void leftPost();
    void generalInfo(String info);
}
