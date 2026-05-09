package net.bobbacon.ritual;

public abstract class Phase <T extends Ritual> {
    public final T ritual;
    public Phase(T ritual) {
        this.ritual=ritual;
    }

    public abstract void start();
    /**Called every ticks on server-side.
     * This should return if this phase should end after the current tick.
     * There should always be at least one case where this methode returns true.
     * Parameter time represents the number of ticks since the beginning of the current phase*/
    public abstract boolean tick(int time);
    public interface Factory<R extends Phase<?>>{
        //TODO creer une class context
        R create(Ritual ritual);
    }
}
