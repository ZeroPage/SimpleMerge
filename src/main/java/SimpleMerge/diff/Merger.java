package SimpleMerge.diff;

import SimpleMerge.util.Pair;

import java.util.List;

public class Merger<T extends Comparable<T>> {
    private List<T> firstItems, secondItems;
    private List<Block> firstBlocks, secondBlocks;
    private int focusedBlock;
    private MergeEventListener mergeEventListener;
    private UpdateBlockStyleEventListener updateBlockStyleFirstEventListener, updateBlockStyleSecondEventListener;
    private UpdateItemsEventListener<T> updateItemsFirstEventListener, updateItemsSecondEventListener;

    public Merger(List<T> firstItems, List<T> secondItems, Pair<List<Block>> diffBlocks) {
        this.firstItems = firstItems;
        this.secondItems = secondItems;
        this.firstBlocks = diffBlocks.first;
        this.secondBlocks = diffBlocks.second;
        this.focusedBlock = 0;
    }

    public void start() {
        /*
        emitUpdateItemsFirst(firstItems, new Block(0, firstItems.size() - 1));
        emitUpdateItemsSecond(firstItems, new Block(0, firstItems.size() - 1));
        */
        emitUpdateBlockStyleFirst(firstBlocks.get(0), BlockState.FOCUSED);
        emitUpdateBlockStyleSecond(secondBlocks.get(0), BlockState.FOCUSED);
        for (int i = 1; i < firstBlocks.size(); i++) {
            emitUpdateBlockStyleFirst(firstBlocks.get(i), BlockState.DIFF);
            emitUpdateBlockStyleSecond(secondBlocks.get(i), BlockState.DIFF);
        }
        emitMergeStart();
    }

    public void moveToNext() {
        // TODO: Not implemented

    }

    public void moveToPrev() {
        // TODO: Not implemented
    }

    public void mergeWithFirstItem() {
        emitUpdateBlockStyleFirst(firstBlocks.get(focusedBlock), BlockState.IDENTICAL);
        emitUpdateBlockStyleSecond(secondBlocks.get(focusedBlock), BlockState.IDENTICAL);
        mergeFocusedItem(
            firstItems,
            secondItems,
            firstBlocks.get(focusedBlock),
            secondBlocks.get(focusedBlock),
            updateItemsSecondEventListener);
        mergeFocusedBlock(firstBlocks, secondBlocks, updateBlockStyleSecondEventListener);
        updateFocus();
    }

    public void mergeWithSecondItem() {
        emitUpdateBlockStyleFirst(firstBlocks.get(focusedBlock), BlockState.IDENTICAL);
        emitUpdateBlockStyleSecond(secondBlocks.get(focusedBlock), BlockState.IDENTICAL);
        mergeFocusedItem(
            secondItems,
            firstItems,
            secondBlocks.get(focusedBlock),
            firstBlocks.get(focusedBlock),
            updateItemsFirstEventListener);
        mergeFocusedBlock(secondBlocks, firstBlocks, updateBlockStyleFirstEventListener);
        updateFocus();
    }

    private void updateFocus() {
        if (focusedBlock == firstBlocks.size()) {
            focusedBlock--;
        }
        if (focusedBlock < 0) {
            emitMergeEnd();
        } else {
            emitUpdateBlockStyleFirst(firstBlocks.get(focusedBlock), BlockState.FOCUSED);
            emitUpdateBlockStyleSecond(secondBlocks.get(focusedBlock), BlockState.FOCUSED);
        }
    }

    private void mergeFocusedBlock(List<Block> from, List<Block> to, UpdateBlockStyleEventListener eventListener) {
        int fromBlockSize = from.get(focusedBlock).size(), toBlockSize = to.get(focusedBlock).size();
        for (int i = focusedBlock + 1; i < to.size(); i++) {
            to.get(i).update(fromBlockSize - toBlockSize);
        }
        Block mergedBlock = new Block(to.get(focusedBlock).start(), to.get(focusedBlock).start() + fromBlockSize);
        eventListener.onUpdateBlockStyle(mergedBlock, BlockState.MERGED);
        from.remove(focusedBlock);
        to.remove(focusedBlock);
    }

    private void mergeFocusedItem(List<T> fromItems, List<T> toItems, Block fromBlock, Block toBlock, UpdateItemsEventListener<T> eventListener) {
        for (int i = toBlock.end() - 1; i >= toBlock.start(); i--) {
            toItems.remove(i);
        }
        toItems.addAll(toBlock.start(), fromItems.subList(fromBlock.start(), fromBlock.end()));
        if (eventListener != null) {
            eventListener.onUpdateItems(toBlock, fromItems.subList(fromBlock.start(), fromBlock.end()), fromBlock.end() == fromItems.size());
        }
    }

    public void setMergeEventListener(MergeEventListener eventListener) {
        mergeEventListener = eventListener;
    }

    private void emitMergeStart() {
        if (mergeEventListener != null) {
            mergeEventListener.onStart();
        }
    }

    private void emitMergeEnd() {
        if (mergeEventListener != null) {
            mergeEventListener.onEnd();
        }
    }

    public void setOnUpdateBlockStyleFirst(UpdateBlockStyleEventListener eventListener) {
        updateBlockStyleFirstEventListener = eventListener;
    }

    private void emitUpdateBlockStyleFirst(Block block, BlockState blockState) {
        if (updateBlockStyleFirstEventListener != null) {
            updateBlockStyleFirstEventListener.onUpdateBlockStyle(block, blockState);
        }
    }

    public void setOnUpdateBlockStyleSecond(UpdateBlockStyleEventListener eventListener) {
        updateBlockStyleSecondEventListener = eventListener;
    }

    private void emitUpdateBlockStyleSecond(Block block, BlockState blockState) {
        if (updateBlockStyleSecondEventListener != null) {
            updateBlockStyleSecondEventListener.onUpdateBlockStyle(block, blockState);
        }
    }

    public void setOnUpdateItemsFirst(UpdateItemsEventListener<T> eventListener) {
        updateItemsFirstEventListener = eventListener;
    }

    public void emitUpdateItemsFirst(Block block, List<T> items, boolean includeLastItem) {
        if (updateItemsFirstEventListener != null) {
            updateItemsFirstEventListener.onUpdateItems(block, items, includeLastItem);
        }
    }

    public void setOnUpdateItemsSecond(UpdateItemsEventListener<T> eventListener) {
        updateItemsSecondEventListener = eventListener;
    }

    public void emitUpdateItemsSecond(Block block, List<T> items, boolean includeLastItem) {
        if (updateItemsSecondEventListener != null) {
            updateItemsSecondEventListener.onUpdateItems(block, items, includeLastItem);
        }
    }

    public enum BlockState {
        IDENTICAL, MERGED, DIFF, FOCUSED;
    }

    public interface MergeEventListener {
        void onStart();

        void onEnd();
    }

    public interface UpdateBlockStyleEventListener {
        void onUpdateBlockStyle(Block block, BlockState style);
    }

    public interface UpdateItemsEventListener<T> {
        void onUpdateItems(Block block, List<T> items, boolean includeLastItem);
    }

}
