package com.textbasedgame.skirmishes;

import com.textbasedgame.users.User;
import com.textbasedgame.utils.RandomUtils;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import dev.morphia.annotations.Entity;
import dev.morphia.annotations.Id;
import dev.morphia.annotations.Reference;
import org.bson.types.ObjectId;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;

import java.time.LocalDateTime;
import java.util.*;

@Entity("skirmishes")
public class Skirmish {
    @JsonSerialize(using = ToStringSerializer.class)
    @Id
    private ObjectId id;
    @JsonIgnore
    @Reference(idOnly = true, lazy=true)
    private User user;
    private final Map<String, SkirmishData> challenges = new HashMap<>();

    private final Dungeons dungeons = new Dungeons();
    private ChosenChallenge chosenChallenge;
    @CreatedDate
    private LocalDateTime createdAt;
    @LastModifiedDate
    private LocalDateTime updatedAt;
    public record ChosenChallenge(String id, LocalDateTime timestamp){}

    public Skirmish() {}
    public Skirmish (User user, int challengesIteration) {
        this.user = user;
        this.generateChallenges(challengesIteration);
    }

    //TODO: remove this is only for debug
    public Skirmish (User user) {
        this.user = user;

        int id = 0;
        for(EnemySkirmishDifficulty difficulty : EnemySkirmishDifficulty.values()) {
            this.challenges.put(String.valueOf(id), new SkirmishData(difficulty, "Debug skirmish"));
            id++;
        }
    }
    public void generateChallenges(int iterateCount) {
        this.challenges.clear();
        this.setChosenChallenge(null);

            int id = 0;
            for(EnemySkirmishDifficulty difficulty : EnemySkirmishDifficulty.values()){
                for(int i = 0; i < iterateCount; i++){
                    double probability = difficulty.getProbability();
                    boolean canAdd = RandomUtils.checkPercentageChance(probability);
                    if(canAdd) {
                        this.challenges.put(String.valueOf(id), new SkirmishData(difficulty, "Skirmish: " + difficulty.name() + " " + i));
                        id++;
                    }
            }
        }
    }

    public Map<String, SkirmishData> getChallenges() {
        return challenges;
    }
    public ObjectId getId() { return id; }
    public User getUser() { return user; }

    public Dungeons getDungeons() {
        return dungeons;
    }

    public LocalDateTime getCreatedAt() { return createdAt; }
    public LocalDateTime getUpdatedAt() { return updatedAt; }



    public ChosenChallenge getChosenChallenge() {
        return chosenChallenge;
    }

    public Optional<SkirmishData> getChosenChallengeData() {
        if(chosenChallenge == null) return Optional.empty();

        return Optional.ofNullable(this.getChallengeById(this.chosenChallenge.id()));
    }

    public boolean isChallengeTimeCompleted(){
        if(chosenChallenge == null) return false;
        return chosenChallenge.timestamp().isBefore(LocalDateTime.now());
    }

    public SkirmishData getChallengeById(String id){
        return this.challenges.get(id);
    }


    public void setChosenChallenge(ChosenChallenge chosenChallenge) {
        this.chosenChallenge = chosenChallenge;
    }

    @Override
    public String toString() {
        return "Skirmish{" +
                "id=" + id +
                ", user=" + user +
                ", challenges=" + challenges +
                ", createdAt=" + createdAt +
                ", updatedAt=" + updatedAt +
                '}';
    }


}
