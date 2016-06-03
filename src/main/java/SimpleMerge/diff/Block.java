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
    public void update(int offset) {
        this.update(offset, offset);
    }
    public void update(int startOffset, int endOffset) {
        this.start += startOffset;
        this.end += endOffset;
    }
}
