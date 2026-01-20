package net.bobbacon.ritual;

public interface Phase {
    public void start();
    /**Called every ticks on server-side.
     * This should return if this phase should end after the current tick.
     * There should always be at least one case where this methode returns true.
     * Parameter time represents the number of ticks since the beginning of the current phase*/
    public boolean tick(int time);
}
