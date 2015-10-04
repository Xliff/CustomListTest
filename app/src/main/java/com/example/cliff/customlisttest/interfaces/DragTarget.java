package com.example.cliff.customlisttest.interfaces;

/**
 * Created by Cliff on 9/11/2015.
 */
public interface DragTarget {
    boolean m_DragTarget = false;

    public boolean getDragTarget();
    public void setDragTarget(boolean m_DragTarget);
    public void acceptTarget();
}
