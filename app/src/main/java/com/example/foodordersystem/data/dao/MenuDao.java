package com.example.foodordersystem.data.dao;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Delete;
import androidx.room.Update;

import com.example.foodordersystem.data.entity.MenuItem;

import java.util.List;

@Dao
public interface MenuDao {
    @Insert
    void insertMenuItem(MenuItem menuItem);

    @Update
    void updateMenuItem(MenuItem menuItem);

    @Delete
    void deleteMenuItem(MenuItem menuItem);

    @Query("SELECT * FROM menu_items")
    List<MenuItem> getAllMenuItems();

    @Query("SELECT * FROM menu_items WHERE itemId = :itemId LIMIT 1")
    MenuItem getMenuItemById(int itemId);
}
