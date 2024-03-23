package com.example.leaderboards;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import dev.morphia.annotations.Entity;
import dev.morphia.annotations.Id;
import org.bson.types.ObjectId;

import java.util.List;

@Entity("leaderboards")
public class Leaderboard {
    @JsonSerialize(using = ToStringSerializer.class)
    @Id
    private ObjectId id;
    private LeaderboardCategory category;
    private List<LeaderboardData> data;
    public enum LeaderboardCategory {
        LEVELS,
        DUNGEONS,
        //TODO: make others leaderboards (GOLD, ACHIEVEMENTS, FIGHTS etc)
    };

    public Leaderboard(){};

    public Leaderboard(LeaderboardCategory category, List<LeaderboardData> data){
        this.category = category;
        this.data = data;
    }

    public record LeaderboardData(int place, String userId, String username, double value){}


    public ObjectId getId() {
        return this.id;
    }

    public LeaderboardCategory getCategory() {
        return this.category;
    }

    public List<LeaderboardData> getData() {
        return this.data;
    }

    public void setData(List<LeaderboardData> data) {
        this.data = data;
    }
}
