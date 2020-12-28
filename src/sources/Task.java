/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package sources;

import java.time.LocalDate;
import java.util.LinkedList;

/**
 *
 * @author Dinh Nguyen Hai Dang - B1704721
 */
public class Task {

    private int taskID;
    private String description;
    private int duration;
    private LinkedList<Integer> predecessors = new LinkedList<>();
    private int rank = -1;
    private int earliestStart;
    private int latestStart;
    private LocalDate earliestStartDate;
    private LocalDate latestStartDate;
    private LocalDate deadline;

    public Task(int taskID, String description, int completionTime, LinkedList<Integer> predecessors) {
        this.taskID = taskID;
        this.description = description;
        this.duration = completionTime;
        this.predecessors = predecessors;
    }

    public int getTaskID() {
        return taskID;
    }

    public void setTaskID(int taskID) {
        this.taskID = taskID;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public int getDuration() {
        return duration;
    }

    public void setDuration(int duration) {
        this.duration = duration;
    }

    public LinkedList<Integer> getPredecessors() {
        return this.predecessors;
    }

    public void setPredecessors(LinkedList<Integer> previousTasks) {
        this.predecessors = new LinkedList<>(previousTasks);
    }

    public void addPredecessor(int previousTaskID) {
        predecessors.add(previousTaskID);
    }

    public boolean isPredecessorOf(Task task) {
        return task.predecessors.contains(this.taskID);
    }

    public int numberOfPredecessors() {
        return predecessors.size();
    }

    public boolean hasNoSuccessorIn(LinkedList<Task> taskList) {
        for (Task task : taskList) {
            if (task.predecessors.contains(this.taskID)) {
                return false;
            }
        }
        return true;
    }

    public int getRank() {
        return rank;
    }

    public void setRank(int rank) {
        this.rank = rank;
    }

    public int getEarliestStart() {
        return earliestStart;
    }

    public void setEarliestStart(int earliestStart) {
        this.earliestStart = earliestStart;
    }

    public int getLatestStart() {
        return latestStart;
    }

    public void setLatestStart(int latestStart) {
        this.latestStart = latestStart;
    }

    public LocalDate getEarliestStartingDate() {
        return earliestStartDate;
    }

    public void setEarliestStartingDate(LocalDate date) {
        earliestStartDate = date;
    }

    public LocalDate getLatestStartingDate() {
        return latestStartDate;
    }

    public void setLatestStartingDate(LocalDate date) {
        latestStartDate = date;
    }
    
    public LocalDate getDeadline() {
        return deadline;
    }

    public void setDeadline(LocalDate date) {
        deadline = date;
    }

    /**
     * Remove this task from taskList and adjust other tasks' connections.
     *
     * @param taskList
     */
    public void removeFrom(LinkedList<Task> taskList) {
        for (Task task : taskList) {
            // Remove this task's taskID from other tasks' previousTasks
            if (task.predecessors.contains(this.taskID)) {
                task.predecessors.remove(task.predecessors.indexOf(this.taskID));
            }

            // Shift taskIDs of tasks behind this task
            if (this.getTaskID() != -1) { // Don't adjust if this task is a virtual task
                LinkedList<Integer> adjustment = new LinkedList<>();
                for (int previousID : task.predecessors) {
                    if (previousID > taskList.indexOf(this)) {
                        adjustment.add(previousID - 1);
                    } else {
                        adjustment.add(previousID);
                    }
                }
                task.setPredecessors(new LinkedList<>(adjustment));
            }
        }

        // Remove a task form taskList
        taskList.remove(this);

        // Reset tasks' taskID = tasks' index
        for (Task task : taskList) {
            task.setTaskID(taskList.indexOf(task));
        }
    }
}
