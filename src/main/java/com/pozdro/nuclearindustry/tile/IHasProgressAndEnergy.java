package com.pozdro.nuclearindustry.tile;

import ic2.api.energy.prefab.BasicSink;

public interface IHasProgressAndEnergy {
    int getProgress();
    int getMaxProgress();
    void setProgress(int newProgress);
    void setMaxProgress(int data);

    BasicSink getEnergySink();
    void setClientEnergy(int energy);
    int getClientEnergy();


}
