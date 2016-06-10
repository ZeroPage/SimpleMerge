package SimpleMerge.diff;

public class Block {
    private int start, end;

    public Block(int start, int end) {
        this.start = start;
        this.end = end;
    }

    public int start() {
        return this.start;
    }

    public int end() {
        return this.end;
    }

    public int size() {
        return this.end - this.start;
    }

    public void update(int offset) {
        this.update(offset, offset);
    }

    public void update(int startOffset, int endOffset) {
        this.start += startOffset;
        this.end += endOffset;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Block block = (Block) o;

        if (start != block.start) return false;
        return end == block.end;

    }

    @Override
    public int hashCode() {
        int result = start;
        result = 31 * result + end;
        return result;
    }
}
