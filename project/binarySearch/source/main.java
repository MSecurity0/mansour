

import java.io.*;
import java.util.Scanner;

class NodeBST {
    int jobId;
    String title;
    String company;
    String datePosted;
    String companyIndustry;
    String jobRole;
    String degree;
    String jobCity;
    int maxAge;
    int monthlySalaryMaxRange;
    NodeBST left, right;

    public NodeBST(int jobId, String title, String company, String datePosted, String companyIndustry,
                   String jobRole, String degree, String jobCity, int maxAge, int monthlySalaryMaxRange) {
        this.jobId = jobId;
        this.title = title;
        this.company = company;
        this.datePosted = datePosted;
        this.companyIndustry = companyIndustry;
        this.jobRole = jobRole;
        this.degree = degree;
        this.jobCity = jobCity;
        this.maxAge = maxAge;
        this.monthlySalaryMaxRange = monthlySalaryMaxRange;
        this.left = null;
        this.right = null;
    }

    @Override
    public String toString() {
        return "Job ID: " + jobId + ", Title: " + title + ", Company: " + company + 
               ", Date Posted: " + datePosted + ", Company Industry: " + companyIndustry + 
               ", Job Role: " + jobRole + ", Degree: " + degree + 
               ", Job City: " + jobCity + ", Max Age: " + maxAge + 
               ", Monthly Salary Max Range: " + monthlySalaryMaxRange;
    }
}


class BST {
    private NodeBST root;

    public BST() {
        root = null;
    }

    public void insert(NodeBST node) {
    root = insertRec(root, node);
}

    private NodeBST insertRec(NodeBST current, NodeBST node) {
        if (current == null) {
            return node;
        }

        if (node.title.compareTo(current.title) < 0) {
            current.left = insertRec(current.left, node);
        } else if (node.title.compareTo(current.title) > 0) {
            current.right = insertRec(current.right, node);
        }
        return current;
}


    public NodeBST search(String title) {
        return searchRec(root, title);
    }

        private NodeBST searchRec(NodeBST current, String title) {
        if (current == null || current.title.equals(title)) {
            return current;
        }

        if (title.compareTo(current.title) < 0) {
            return searchRec(current.left, title);
        } else {
            return searchRec(current.right, title);
        }
}
    

    // Method to remove a node
    public void remove(String title) {
    root = removeRec(root, title);
}

    private NodeBST removeRec(NodeBST current, String title) {
        if (current == null) {
            return null;
        }

        if (title.equals(current.title)) {
            // Node with only one child or no child
            if (current.left == null) {
                return current.right;
            } else if (current.right == null) {
                return current.left;
            }

            // Node with two children: Get the inorder successor (smallest in the right subtree)
            current.title = minValue(current.right);

            // Delete the inorder successor
            current.right = removeRec(current.right, current.title);
        } else if (title.compareTo(current.title) < 0) {
            current.left = removeRec(current.left, title);
        } else {
            current.right = removeRec(current.right, title);
        }

        return current;
    }

    private String minValue(NodeBST root) {
        String minv = root.title;
        while (root.left != null) {
            minv = root.left.title;
            root = root.left;
        }
        return minv;
    }

}

public class main {
    public static void main(String[] args) {
        // Check for the correct number of command-line arguments
        if (args.length < 2) {
            System.out.println("Usage: java Main <operation> <keyword>");
            System.out.println("<operation> 0 for search, 1 for remove");
            return;
        }

        // Parse the operation and keyword from command-line arguments
        int operation = Integer.parseInt(args[1]);
        String titleKeyword = args[0];

        BST bst = new BST();
        System.out.print("DataSet File: ");
        String csvFile = in.next(); 

        // Time measurement for reading and populating the BST
        long startTime = System.currentTimeMillis();
        System.out.println("Start time: " + startTime);

        try (BufferedReader br = new BufferedReader(new FileReader(csvFile))) {
            System.out.println("Started reading dataset file");
            String line;
            while ((line = br.readLine()) != null) {
                String[] data = line.split(",");
                if (data[0].equals("Job ID")) continue; // Skip header

                // Parse data and populate BST
                int jobId = Integer.parseInt(data[0]);
                String title = data[1];
                String company = data[2];
                String datePosted = data[3];
                String companyIndustry = data[4];
                String jobRole = data[5];
                String degree = data[6];
                String jobCity = data[7];
                int maxAge = isNumeric(data[8]) ? Integer.parseInt(data[8]) : 0;
                int monthlySalaryMaxRange = isNumeric(data[9]) ? Integer.parseInt(data[9]) : 0;

                NodeBST node = new NodeBST(jobId, title, company, datePosted, companyIndustry, 
                                           jobRole, degree, jobCity, maxAge, monthlySalaryMaxRange);
                bst.insert(node);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        long fileReadEndTime = System.currentTimeMillis();
        System.out.println("Loaded file in BST");
        System.out.println("End time: " + fileReadEndTime);
        System.out.println("Time taken: " + (fileReadEndTime - startTime) / 1000.0 + " milliseconds");

        // Time measurement for search/remove operation
        long operationStartTime = System.currentTimeMillis();
        System.out.println("\nStart time: " + operationStartTime);
        System.out.println("Operation keyword: " + titleKeyword);

        if (operation == 0) {
            // Search operation
            NodeBST result = bst.search(titleKeyword);
            long operationEndTime = System.currentTimeMillis();
            System.out.println("End time: " + operationEndTime);
            System.out.println("Time taken: " + (operationEndTime - operationStartTime) / 1000.0 + " milliseconds");
            
            if (result != null) {
                System.out.println("\nFound: " + result);
            } else {
                System.out.println("\nNo job found with title: " + titleKeyword);
            }
        } else if (operation == 1) {
            // Remove operation
            bst.remove(titleKeyword);
            long operationEndTime = System.currentTimeMillis();
            System.out.println("End time: " + operationEndTime);
            System.out.println("Time taken: " + (operationEndTime - operationStartTime) / 1000.0 + " milliseconds");
            
            System.out.println("\nJob with title '" + titleKeyword + "' removed from the BST.");
        } else {
            System.out.println("Invalid operation choice. Please enter 0 for search or 1 for remove.");
        }
    }

    // Helper method to check if a string is numeric
    private static boolean isNumeric(String str) {
        try {
            Integer.parseInt(str);
            return true;
        } catch (NumberFormatException e) {
            return false;
        }
    }
}
