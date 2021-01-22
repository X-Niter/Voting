package io.github.zellfrey.forgevotifier.api.reward;

import java.util.ArrayList;

public class StoredPlayer {

    private String username, uuid;
    private int voteCount;

    private ArrayList<StoredReward> storedRewards;

    public StoredPlayer(String username, String uuid, int voteCount){
        this.username = username;
        this.uuid = uuid;
        this.voteCount = voteCount;
    }

    public int getStoredRewardsSize(){ return this.storedRewards.size(); }

    public String getUuid(){ return this.uuid; }

    public String getUsername(){ return this.username; }

    public int getVoteCount(){ return this.voteCount; }

    public void addStoredReward(StoredReward reward){ this.storedRewards.add(reward); }

    public void setUuid(String uuid) {
        this.uuid = uuid;
    }

    public void addtoVoteCount(int numOfVotes){
        this.voteCount += numOfVotes;
    }
}
//player index consists of:
//Hashmap String string + reward array
//
//{
//        "players": [
//        {
//        "playerName": "Beardedflea",
//        "uuid": "wiidwqihiwfjeiw",
//        "voteCount": "20",
//        "rewards": [
//        {
//        "service": "fjiwe",
//        "address": "england",
//        "timestamp": "112223321"
//        },
//        {
//        "service": "fjiwe",
//        "address": "england",
//        "timestamp": "112223321"
//        }
//        ]
//        },
//        {
//        "playerName": "Beardedflea",
//        "uuid": "wiidwqihiwfjeiw",
//        "voteCount": "20",
//        "rewards": []
//        }
//        ]
//}



