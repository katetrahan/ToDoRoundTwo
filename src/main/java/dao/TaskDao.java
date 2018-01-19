package dao;

import models.*;

import java.util.List;



    public interface TaskDao {

        //create
        void add (Task task);
        //read
        List<Task> getAll();

        Task findById(int id);
        //update
        void update(int id, String content, int categoryId);
        //delete
        void deleteById(int id);
        void clearAllTasks();




}
