package io.github.zellfrey.forgevotifier.api.reward;

import java.util.ArrayList;

public class StoredPlayer {

    public String username, uuid;
    public int voteCount;

    public ArrayList<StoredReward> storedRewards;

    public StoredPlayer(String username, String uuid){
        this.username = username;
        this.uuid = uuid;
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



