package com.zpedroo.voltzvote.objects;

import java.math.BigInteger;
import java.util.UUID;

public class PlayerData {

    private UUID uuid;
    private BigInteger pointsAmount;
    private int votesAmount;
    private boolean update;

    public PlayerData(UUID uuid, BigInteger pointsAmount, int votesAmount) {
        this.uuid = uuid;
        this.pointsAmount = pointsAmount;
        this.votesAmount = votesAmount;
    }

    public UUID getUUID() {
        return uuid;
    }

    public BigInteger getPointsAmount() {
        return pointsAmount;
    }

    public int getVotesAmount() {
        return votesAmount;
    }

    public boolean isQueueUpdate() {
        return update;
    }

    public void addPoints(BigInteger points) {
        setPointsAmount(this.pointsAmount.add(points));
    }

    public void removePoints(BigInteger points) {
        setPointsAmount(this.pointsAmount.subtract(points));
    }

    public void setPointsAmount(BigInteger pointsAmount) {
        this.pointsAmount = pointsAmount;
        this.update = true;
    }

    public void addVotesAmount(int votesAmount) {
        setVotesAmount(this.votesAmount + votesAmount);
    }

    public void removeVotesAmount(int votesAmount) {
        setVotesAmount(this.votesAmount - votesAmount);
    }

    public void setVotesAmount(int votesAmount) {
        this.votesAmount = votesAmount;
        this.update = true;
    }

    public void setUpdate(boolean update) {
        this.update = update;
    }
}