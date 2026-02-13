package app.services;

public class Misc {
    // Number string formatter
    public static String separator(String number) {
        if (number == null) return null;
        int lefttt = number.length() % 3 , r , l , k = -1;
        StringBuilder result = new StringBuilder("");

        if (number.length() < 3) return number;
        if (lefttt != 0) result.append(number.substring(0 , lefttt));
        for (k = 0; k < (number.length() / 3) - 1; k++) {
            l = (3 * k) + lefttt;
            r = l + 3;

            String app = (result.toString().isBlank()) ? number.substring(l , r) : "," + number.substring(l , r);
            result.append(app);
        }
        String app = (result.toString().isBlank()) ? number.substring(number.length() - 3) : "," + number.substring(number.length() - 3);
        result.append(app);
        return result.toString();
    }


    /* Encrypt / Decrypt methods cause we don't want to store passwords as text
    cause niggas can gain unauthorized access to accounts. Written by Mr. Firoozi
     */

    //Translate the encrypted text
    public static String decrypt(String s1){
        String dS1 = "";
        for (int i = 1; i < s1.length(); i += 3){
            int tmpVal = ( ( (int)s1.charAt(i) - 48) * 10) + ((int)s1.charAt(i + 2) - 48);
            if(s1.charAt(i+1) == 'a'){
                tmpVal += 48;
                dS1 += ((char)tmpVal);
            }
            else if(s1.charAt(i+1) == 'b'){
                tmpVal += 97;
                dS1 += ((char)tmpVal);
            }
            else if(s1.charAt(i+1) == 'c'){
                tmpVal += 65;
                dS1 += ((char)tmpVal);
            }
            else if(s1.charAt(i+1) == 'd'){
                tmpVal += 35;
                dS1 += ((char)tmpVal);
            }
            else if (s1.charAt(i+1) == 'e')
                dS1 += "@";
            else if (s1.charAt(i+1) == 'f')
                dS1 += "!";
        }
        return dS1;
    }
    //Encryption key Method
    public static String encrypt(String s1){
        String eS1 = "E";
        for (int i = 0; i < s1.length(); i++){
            char curChar = s1.charAt(i);
            int tempVal;
            //Character threshhold of 0 to 9 with the signiture of a
            if((int)curChar > 47 && (int)curChar < 58){
                tempVal = ((int)curChar - 48);
                eS1 += (tempVal/10) + "a" + (tempVal%10);
            }
            //Charachter threshhold of a to z with the signiture of b
            else if((int)curChar > 96 && (int)curChar < 123){
                tempVal = ((int)curChar - 97);
                eS1 += (tempVal/10) + "b" + (tempVal%10);
            }
            //Charachter threshhold of A to Z with the signiture of c
            else if((int)curChar > 64 && (int)curChar < 91){
                tempVal = ((int)curChar - 65);
                eS1 += (tempVal/10) + "c" + (tempVal%10);
            }
            //Characters of [#, %, &] with the siginiture of d
            else if(((int)curChar > 34 && (int)curChar < 39)){
                tempVal = ((int)curChar - 35);
                eS1 += (tempVal/10) + "d" + (tempVal%10);
            }
            //Character of @ with the signiture of e
            else if ((int)curChar == 64){
                eS1 += "0e0";
            }
            //Character of ! with the signiture of f
            else if ((int)curChar == 33)
                eS1 += "0f0";
        }
        return eS1;
    }

}
