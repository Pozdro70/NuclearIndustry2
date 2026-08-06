package com.pozdro.nuclearindustry.block.tile;

public interface IHasProgress {
    int getProgress();
    int getMaxProgress();
    void setProgress(int newProgress);
}
