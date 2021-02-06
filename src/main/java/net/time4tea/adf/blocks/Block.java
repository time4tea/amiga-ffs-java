package net.time4tea.adf.blocks;

public interface Block extends Describable {
    int size();

    byte[] bytes();
}
