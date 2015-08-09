package com.example.cliff.customlisttest.data;

/**
 * Created by Cliff on 8/9/2015.
 */
public class PlayerData {
    public String pos;
    public String firstname;
    public String lastname;
    public String team;
    public int bye;
    public int rank;

    public PlayerData(
            String pos, String firstname, String lastname, String team, int bye, int rank
    ) {
        this.pos = pos;
        this.firstname = firstname;
        this.lastname = lastname;
        this.bye = bye;
        this.rank = rank;
        this.team = team;
    }
}
