package com.pozdro.nuclearindustry.block.tile;

import ic2.api.energy.prefab.BasicSink;

public interface IHasProgressAndEnergy {
    int getProgress();
    int getMaxProgress();
    void setProgress(int newProgress);

    BasicSink getEnergySink();
    void setClientEnergy(int energy);
    int getClientEnergy();
}
