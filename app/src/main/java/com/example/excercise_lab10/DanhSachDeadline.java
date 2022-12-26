package com.example.excercise_lab10;

import java.io.Serializable;
import java.util.ArrayList;

public class DanhSachDeadline implements Serializable  {
    ArrayList<deadline> listDL= new ArrayList<deadline>();
    public void add(deadline dl)
    {
        listDL.add(0,dl);
    }
    public void removeAt(int i)
    {
        listDL.remove(i);
    }
    public int size(){
        return listDL.size();
    }
    public deadline get(int i)
    {
        return listDL.get(i);
    }
    public void set(int i, deadline dl)
    {
        listDL.set(i, dl);
    }
    public void clear(){
        listDL.clear();
    }
}
