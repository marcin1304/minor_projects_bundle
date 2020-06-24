package com.example.lab14.Department;

import com.example.lab14.Applicant.ApplicantMXBean;
import com.example.lab14.Main;
import com.example.lab14.utils.Ticket;
import javafx.util.Pair;

import javax.management.*;
import java.lang.management.ManagementFactory;
import java.util.*;
import java.util.stream.IntStream;
import java.util.stream.Stream;

public class Department implements DepartmentMXBean {
    public static final int applicantTimeInSeconds = 15;
    private MBeanServer mBeanServer;
    private int numberOfPosts;
    private int ticketIdNumber = 0;
    private int priorityPoll = 0;

    private Map<Integer, Ticket> posts;
    private Map<String, Integer> categoryPriorityMap;
    private List<Ticket> awaitingTickets;
    private Map<Ticket, Integer> ticketApplicantMap;
    private List<Integer> applicantIds;

    private String information;

    public Department(int numberOfPosts) throws Exception {
        this.numberOfPosts = 0;

        mBeanServer = ManagementFactory.getPlatformMBeanServer();

        information = "Welcome to Some Department\n" +
                "If you need consulting choose option A\n" +
                "If you have an urgent matter choose option B";

        posts = new HashMap<>();
        awaitingTickets = new ArrayList<>();
        applicantIds = new ArrayList<>();
        categoryPriorityMap = new HashMap<>();
        ticketApplicantMap = new HashMap<>();

        categoryPriorityMap.put("A", 1);
        categoryPriorityMap.put("B", 2);

        IntStream.range(0, numberOfPosts).forEach(number -> addPost());

        Thread handleNewApplicantThread = new Thread(this::handleApplicant);
        handleNewApplicantThread.start();

        MBeanServer mBeanServer = ManagementFactory.getPlatformMBeanServer();
        mBeanServer.registerMBean(this, new ObjectName(Main.departmentJmxName));
    }

    public void addPost() {
        posts.put(++numberOfPosts, null);
    }

    @Override
    public void registerDepartmentListener(Integer applicantId) {
        applicantIds.add(applicantId);

        postsUpdate(applicantId);
        awaitingTicketsUpdate(applicantId);
    }

    @Override
    public void unregisterDepartmentListener(Integer applicantId) {
        applicantIds.remove(applicantId);
    }

    @Override
    public String issueTicket(int applicantId, String category) {
        Ticket ticket = null;
        if(categoryPriorityMap.containsKey(category)) {
            if(ticketIdNumber == Ticket.MAX_NUMBER)
                ticketIdNumber = 0;
            ticket = new Ticket(category, ticketIdNumber++);
        }
        if(ticket != null)
            registerNewTicket(applicantId, ticket);
        if(ticket == null)
            return "";
        return ticket.toString();
    }

    private void registerNewTicket(int applicantId, Ticket ticket) {
        awaitingTickets.add(ticket);
        awaitingTicketsUpdateAll();
        ticketApplicantMap.put(ticket, applicantId);
    }

    private void handleApplicant() {
        try {
            while (true) {
                if (!awaitingTickets.isEmpty() && posts.containsValue(null)) {
                    Ticket ticket = getHighestPriorityTicket();
                    awaitingTicketsUpdateAll();
                    Optional<Map.Entry<Integer, Ticket>> post = posts.entrySet()
                            .stream()
                            .filter(entry -> entry.getValue() == null)
                            .findFirst();
                    if(post.isPresent())
                        moveApplicantToPost(post.get().getKey(), ticket);
                }
                Thread.sleep(100);
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    private Ticket getHighestPriorityTicket() {
        Ticket ticket = null;
        Optional<Pair> highestPriorityOpt = awaitingTickets.stream()
                .filter(t -> categoryPriorityMap.containsKey(t.getCategory()))
                .map(t -> new Pair(t, categoryPriorityMap.get(t.getCategory())))
                .sorted((p1, p2) -> Integer.compare((Integer)p2.getValue(), (Integer)p1.getValue()))
                .findFirst();
        if(highestPriorityOpt.isPresent()) {
            String categoryOfHighestPriority = ((Ticket) highestPriorityOpt.get().getKey()).getCategory();
            Optional<Ticket> ticketOpt = awaitingTickets.stream()
                    .filter(t -> t.getCategory().equals(categoryOfHighestPriority))
                    .findFirst();
            if(ticketOpt.isPresent()) {
                ticket = ticketOpt.get();
                awaitingTickets.remove(ticket);
            }
            else {
                ticket = awaitingTickets.get(0);
                awaitingTickets.remove(0);
            }
        }
        else {
            ticket = awaitingTickets.get(0);
            awaitingTickets.remove(0);
        }
        return ticket;
    }

    private void moveApplicantToPost(int postNumber, Ticket ticket) {
        posts.put(postNumber, ticket);
        postsUpdateAll();
        int applicantId = ticketApplicantMap.get(ticket);
        try {
            ApplicantMXBean proxy = createProxy(applicantId);
            proxy.movedToPost();
        } catch(MalformedObjectNameException e) {
            e.printStackTrace();
        }
        Thread thread = new Thread(() -> applicantAtPost(postNumber, applicantId));
        thread.start();
    }

    private void applicantAtPost(int postNumber, int applicantId) {
        try {
            Thread.sleep(applicantTimeInSeconds * 1000);

            ticketApplicantMap.remove(posts.get(postNumber));
            posts.put(postNumber, null);

            postsUpdateAll();
            try {
                ApplicantMXBean proxy = createProxy(applicantId);
                proxy.leftPost();
            } catch(MalformedObjectNameException e) {
                e.printStackTrace();
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    private ApplicantMXBean createProxy(int applicantId) throws MalformedObjectNameException {
        return JMX.newMXBeanProxy(
                mBeanServer,
                new ObjectName(Main.applicantJmxBaseName + applicantId),
                ApplicantMXBean.class);
    }

    private void postsUpdateAll() {
        applicantIds.forEach(this::postsUpdate);
    }

    private void postsUpdate(Integer applicantId) {
        try {
            ApplicantMXBean proxy = createProxy(applicantId);
            proxy.postsUpdate(postsInfo());
        } catch (MalformedObjectNameException e) {
            e.printStackTrace();
        }
    }

    private void awaitingTicketsUpdateAll() {
        applicantIds.forEach(this::awaitingTicketsUpdate);
    }

    private void awaitingTicketsUpdate(Integer applicantId) {
        try {
            ApplicantMXBean proxy = createProxy(applicantId);
            proxy.awaitingTicketsUpdate(ticketInfo());
        } catch(MalformedObjectNameException e) {
            e.printStackTrace();
        }
    }

    private String postsInfo() {
        StringBuilder info = new StringBuilder();
        posts.forEach((postNumber, ticket) -> {
            info.append("Post ").append(postNumber).append("\t");
            if(ticket == null)
                info.append("free");
            else
                info.append(ticket.toString());
            info.append("\n");
        });
        return info.toString();
    }

    private String ticketInfo() {
        StringBuilder info = new StringBuilder();
        awaitingTickets.forEach(ticket -> info.append(ticket.toString()).append("  "));
        return info.toString();
    }

    public void sentGeneralInfoAll(String info) {
        applicantIds.forEach( id -> {
            sentGeneralInfo(id, info);
        });
    }

    private void sentGeneralInfo(Integer applicantId, String info) {
        try {
            ApplicantMXBean proxy = createProxy(applicantId);
            proxy.generalInfo(info);
        } catch(MalformedObjectNameException e) {
            e.printStackTrace();
        }
    }

    @Override
    public String getInfo() {
        return information;
    }

    @Override
    public boolean addCategory(String category, Integer priority) {
        if(!categoryPriorityMap.containsKey(category)) {
            categoryPriorityMap.put(category, priority);
            return true;
        }
        return false;
    }

    @Override
    public boolean updateCategory(String category, Integer priority) {
        if(categoryPriorityMap.containsKey(category)) {
            categoryPriorityMap.put(category, priority);
            return true;
        }
        return false;
    }

    @Override
    public boolean deleteCategory(String category) {
        if(categoryPriorityMap.containsKey(category)) {
            categoryPriorityMap.remove(category);
            return true;
        }
        return false;
    }
}