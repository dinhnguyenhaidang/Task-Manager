/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package sources;

import java.util.LinkedList;

/**
 *
 * @author Dinh Nguyen Hai Dang - B1704721
 */
public class Algorithm {

    /**
     * Rank all tasks in taskList. The more previous tasks the task have, the
     * higher the rank.
     *
     * @param taskList an array list of tasks
     */
    public static void rank(LinkedList<Task> taskList) {
        // Reset tasks' rank
        for (Task task : taskList) {
            task.setRank(-1);
        }

        // Initialize variables
        LinkedList<Integer> numberOfUnrankedPredecessors = new LinkedList<>();
        for (Task task : taskList) {
            numberOfUnrankedPredecessors.add(task.numberOfPredecessors());
        }
        int rank = 0;
        boolean atLeastOneTaskIsRanked;

        // Rank
        do {
            // Reset checking variable for each interation
            atLeastOneTaskIsRanked = false;
            
            // If a task is unranked and all of its predecessors are ranked, rank it
            for (Task task : taskList) {
                if (task.getRank() == -1 && numberOfUnrankedPredecessors.get(taskList.indexOf(task)) == 0) {
                    task.setRank(rank);
                    atLeastOneTaskIsRanked = true;
                    
                    // Update numberOfUnrankedPredecessors
                    for (Task successor : taskList) {
                        if (task.isPredecessorOf(successor)) {
                            numberOfUnrankedPredecessors.set(taskList.indexOf(successor), numberOfUnrankedPredecessors.get(taskList.indexOf(successor)) - 1);
                        }
                    }
                }
            }

            // Increase variable rank
            rank++;
        } while (atLeastOneTaskIsRanked);
    }

    /**
     * Return an array list of taskIDs ordering by rank.
     *
     * @param taskList an array list of tasks
     * @return an array list
     */
    public static LinkedList<Integer> topoSort(LinkedList<Task> taskList) {
        LinkedList<Integer> topoSorted = new LinkedList<>();
        for (int rank = 0; rank < taskList.size(); rank++) {
            for (Task task : taskList) {
                if (task.getRank() == rank) {
                    topoSorted.add(taskList.indexOf(task));
                }
            }
        }
        return topoSorted;
    }

    /**
     * Calculate the earliest start possible and latest start possible of all
     * tasks in taskList. Call this function and the attributes of taskList will
     * change.
     *
     * @param taskList an array list of tasks
     */
    public static void gantt(LinkedList<Task> taskList) {
        // Add virtual tasks at "start" and "end"
        Task start = new Task(-1, "Start", 0, new LinkedList<>());
        taskList.add(0, start);

        Task end = new Task(-2, "End", 0, new LinkedList<>());
        taskList.add(end);

        // Add connections to virtual tasks
        for (Task task : taskList) {
            if (task.numberOfPredecessors() == 0 && task.getTaskID() != -1 && task.getTaskID() != -2) {
                task.addPredecessor(-1);
            }
            if (task.hasNoSuccessorIn(taskList) && task.getTaskID() != -1 && task.getTaskID() != -2) {
                end.addPredecessor(task.getTaskID());
            }
        }

        // Rank
        rank(taskList);

        // Topo sort
        LinkedList<Integer> topoSorted = new LinkedList<>(topoSort(taskList));

        // Calculate earliest start possible for each task
        for (Task task : taskList) {
            task.setEarliestStart(-9999);
        }
        start.setEarliestStart(0);

        for (int i = 0; i < topoSorted.size(); i++) {
            Task currentTask = taskList.get(topoSorted.get(i));
            for (Task task : taskList) {
                if (task.isPredecessorOf(currentTask)) {
                    if (task.getEarliestStart() + task.getDuration() > currentTask.getEarliestStart()) {
                        currentTask.setEarliestStart(task.getEarliestStart() + task.getDuration());
                    }
                }
            }
        }

        // Calculate latest start possible for each task
        for (Task task : taskList) {
            task.setLatestStart(9999);
        }
        end.setLatestStart(end.getEarliestStart());

        for (int i = topoSorted.size() - 1; i >= 0; i--) {
            Task currentTask = taskList.get(topoSorted.get(i));
            for (Task task : taskList) {
                if (currentTask.isPredecessorOf(task)) {
                    if (task.getLatestStart() - currentTask.getDuration() < currentTask.getLatestStart()) {
                        currentTask.setLatestStart(task.getLatestStart() - currentTask.getDuration());
                    }
                }
            }
        }

        // Remove virtual tasks and their connections
        start.removeFrom(taskList);
        end.removeFrom(taskList);

        for (Task task : taskList) {
            System.out.println(task.getEarliestStart() + "-" + task.getLatestStart());
        }
    }
}
