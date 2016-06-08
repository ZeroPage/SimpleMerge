package SimpleMerge.diff;

import SimpleMerge.util.Pair;
import org.junit.BeforeClass;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class MergerTest {
    private static List<String> first;
    private static List<String> second;
    private static Pair<List<Block>> diffBlocks;

    @BeforeClass
    public static void setup() {
        first = new ArrayList<>();
        first.add("1");
        first.add("2");
        first.add("3");
        first.add("4");
        first.add("5");
        second = new ArrayList<>();
        second.add("1");
        second.add("a");
        second.add("b");
        second.add("c");
        second.add("3");
        second.add("d");
        second.add("e");
        second.add("5");

        Diff<String> diff = new Diff<>();
        diff.compare(first, second);
        diffBlocks = diff.getDiffBlocks();
    }

    @Test
    public void shouldUpdateStyleToFocusedBlockOnStart() {
        Merger<String> stringMerger = new Merger<>(first, second, diffBlocks);
        Merger.UpdateBlockStyleEventListener updateBlockStyleEventListenerFirst = mock(Merger.UpdateBlockStyleEventListener.class);
        Merger.UpdateBlockStyleEventListener updateBlockStyleEventListenerSecond = mock(Merger.UpdateBlockStyleEventListener.class);
        stringMerger.setOnUpdateBlockStyleFirst(updateBlockStyleEventListenerFirst);
        stringMerger.setOnUpdateBlockStyleSecond(updateBlockStyleEventListenerSecond);
        stringMerger.start();
        verify(updateBlockStyleEventListenerFirst).onUpdateBlockStyle(new Block(1, 2), Merger.BlockStyleFocused);
        verify(updateBlockStyleEventListenerSecond).onUpdateBlockStyle(new Block(1, 4), Merger.BlockStyleFocused);
    }

    @Test
    public void shouldBeAbleToMerge() {
        Merger<String> stringMerger = new Merger<>(first, second, diffBlocks);
        Merger.UpdateBlockStyleEventListener updateBlockStyleEventListenerFirst = mock(Merger.UpdateBlockStyleEventListener.class);
        Merger.UpdateBlockStyleEventListener updateBlockStyleEventListenerSecond = mock(Merger.UpdateBlockStyleEventListener.class);
        Merger.UpdateItemsEventListener updateItemsEventListenerFirst = mock(Merger.UpdateItemsEventListener.class);
        Merger.UpdateItemsEventListener updateItemsEventListenerSecond = mock(Merger.UpdateItemsEventListener.class);
        stringMerger.setOnUpdateBlockStyleFirst(updateBlockStyleEventListenerFirst);
        stringMerger.setOnUpdateBlockStyleSecond(updateBlockStyleEventListenerSecond);
        stringMerger.setOnUpdateItemsFirst(updateItemsEventListenerFirst);
        stringMerger.setOnUpdateItemsSecond(updateItemsEventListenerSecond);
        stringMerger.setOnUpdateBlockStyleSecond(updateBlockStyleEventListenerSecond);

        stringMerger.start();
        verify(updateBlockStyleEventListenerFirst).onUpdateBlockStyle(new Block(1, 2), Merger.BlockStyleFocused);
        verify(updateBlockStyleEventListenerSecond).onUpdateBlockStyle(new Block(1, 4), Merger.BlockStyleFocused);

        stringMerger.mergeWithFirstItem();
        verify(updateBlockStyleEventListenerFirst).onUpdateBlockStyle(new Block(1, 2), Merger.BlockStyleIdentical);
        verify(updateBlockStyleEventListenerFirst).onUpdateBlockStyle(new Block(3, 4), Merger.BlockStyleFocused);

        verify(updateBlockStyleEventListenerSecond).onUpdateBlockStyle(new Block(1, 4), Merger.BlockStyleIdentical);
        verify(updateItemsEventListenerSecond).onUpdateItems(new Block(1, 4), new ArrayList<String>(){{
            add("2");
        }}, false);
        verify(updateBlockStyleEventListenerSecond).onUpdateBlockStyle(new Block(1, 2), Merger.BlockStyleMerged);
        verify(updateBlockStyleEventListenerSecond).onUpdateBlockStyle(new Block(3, 5), Merger.BlockStyleFocused);
    }
}
