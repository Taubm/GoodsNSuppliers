package cls;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */


/**
 *
 * @author Иван
 */
public class Validate {

    public static void checkNumber(String s) throws Exception {
        for (char c : s.toCharArray()) {
            if (!(c >= '0' && c <= '9')) {
                throw new Exception("В числовом поле недопустимые символы: ");
            }
        }
    }
    
    public static void checkCost(String s) throws Exception {
        for (char c : s.toCharArray()) {
                if (!((c >= '0' && c <= '9') || c == ',' || c == '.')) {
                    throw new Exception("В поле с ценой недопустимые символы: ");
                }
            }
    }

}
