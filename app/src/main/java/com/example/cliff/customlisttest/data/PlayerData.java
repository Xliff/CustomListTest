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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof PlayerData)) return false;

        PlayerData that = (PlayerData) o;

        if (bye != that.bye) return false;
        if (rank != that.rank) return false;
        if (!pos.equals(that.pos)) return false;
        if (firstname != null ? !firstname.equals(that.firstname) : that.firstname != null)
            return false;
        if (lastname != null ? !lastname.equals(that.lastname) : that.lastname != null)
            return false;

        return !(team != null ? !team.equals(that.team) : that.team != null);
    }

    @Override
    public int hashCode() {
        int result = pos.hashCode();
        result = 31 * result + (firstname != null ? firstname.hashCode() : 0);
        result = 31 * result + (lastname != null ? lastname.hashCode() : 0);
        result = 31 * result + (team != null ? team.hashCode() : 0);
        result = 31 * result + bye;
        result = 31 * result + rank;

        return result;
    }
}
