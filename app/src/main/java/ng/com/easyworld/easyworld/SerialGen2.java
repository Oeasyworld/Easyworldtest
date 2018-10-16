package ng.com.easyworld.easyworld;

import java.util.Random;

/**
 * Created by Oisrael on 8/14/2018.
 */

public class SerialGen2 {

    public String getNewSerialName(){
        StringBuilder serial = new StringBuilder();

        String[] Mylist = {"a","X","c","d","O","f","g","W","i","j",
                "Z","l","m","T","o","p","q","r","s","t",
                "u","v","w","x","y","z","A","B","C","D","E",
                "F","G","H","I","J","K","L","M","N","e","P",
                "Q","R","S","n","U","V","h","b","Y","k","6","0","3","4","5","1","7","8","9","2"};

        Random rand = new Random();
        for (int number = 1; number <= 10; number++)
        {
            int randomNumber = rand.nextInt(61) + 1;

            serial.append(Mylist[randomNumber]);


        }

        String result = serial.toString();

        return result;
    }

}
