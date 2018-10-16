package ng.com.easyworld.easyworld;

import java.util.Random;

/**
 * Created by oisrael on 27-Dec-17.
 */

public class SerialGeneratn {

    public String getNewSerialName(){
        StringBuilder serial = new StringBuilder();

        String[] Mylist = {"a","b","c","d","e","f","g","h","i","j",
                "k","l","m","n","o","p","q","r","s","t",
                "u","v","w","x","y","z","A","B","C","D","E",
                "F","G","H","I","J","K","L","M","N","O","P",
                "Q","R","S","T","U","V","W","X","Y","Z","1","2","3","4","5","6","7","8","9","0"};

        Random rand = new Random();
        for (int number = 1; number <= 35; number++)
        {
            int randomNumber = rand.nextInt(61) + 1;

            serial.append(Mylist[randomNumber]);


        }

        String result = serial.toString();

        return result;
    }


}
