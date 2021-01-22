package io.github.zellfrey.forgevotifier.api.reward;

public class StoredPlayer {

    private String username, uuid;
    private int voteCount;

    public static int cumulativeTest = 0;

    public StoredPlayer(String username, String uuid, int voteCount){
        this.username = username;
        this.uuid = uuid;
        this.voteCount = voteCount;
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



