package com.example.niyamatmynotebook;

// UserDao.java
import androidx.room.Dao;
import androidx.room.Insert;
import com.example.niyamatmynotebook.User;


@Dao
public interface UserDao {
    @Insert
    void insert(User user);


}

