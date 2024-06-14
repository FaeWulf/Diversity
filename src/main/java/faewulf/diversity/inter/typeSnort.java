package faewulf.diversity.inter;

public enum typeSnort {

    SUGAR(0),
    BLAZE_POWDER(1),
    GUN_POWDER(2),
    REDSTONE(3),
    GLOW_DUST(4),
    BONE_MEAL(5);

    private final int index;

    typeSnort(final int index) {
        this.index = index;
    }

    public int getIndex() {
        return index;
    }
}
