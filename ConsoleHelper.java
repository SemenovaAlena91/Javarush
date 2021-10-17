package com.javarush.task.task30.task3008;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

public class ConsoleHelper {
    private static BufferedReader reader  = new BufferedReader(new InputStreamReader(System.in));

    public static void writeMessage(String message){
        System.out.println(message);
    }

    public static String readString(){
        String result = null;
        try{
            result = reader.readLine();

        }catch (IOException e){
            writeMessage("Произошла ошибка при попытке ввода числа. Попробуйте еще раз.");
            result = readString();
        }
        return result;
    }

    public static int readInt(){
        int result = 0;
        try {
            result = Integer.parseInt(readString());

        }catch (NumberFormatException e){
            writeMessage("Произошла ошибка при попытке ввода числа. Попробуйте еще раз.");
            try{
                result = Integer.parseInt(readString());

            }catch (NumberFormatException e1){
                writeMessage("Произошла ошибка при попытке ввода числа. Попробуйте еще раз.");
            }
        }


        return result;
    }
    /*Добавь в него:
1. Статическое поле типа BufferedReader, инициализированное с помощью System.in.
2. Статический метод writeMessage(String message), который должен выводить сообщение message в консоль.
3. Статический метод String readString(), который должен считывать строку с консоли.
Если во время чтения произошло исключение, вывести пользователю сообщение "
Произошла ошибка при попытке ввода текста. Попробуйте еще раз."
И повторить ввод. Метод не должен пробрасывать исключения IOException наружу.
Другие исключения не должны обрабатываться.
4. Статический метод int readInt(). Он должен возвращать введенное число и использовать метод readString().
Внутри метода обработать исключение NumberFormatException.
Если оно произошло вывести сообщение
"Произошла ошибка при попытке ввода числа. Попробуйте еще раз."
И повторить ввод числа.

В этой задаче и далее, если не указано дополнительно другого, то все поля класса должны быть приватными, а методы публичными.


Requirements:
1. В классе ConsoleHelper должно быть создано и инициализировано приватное, не финальное, статическое поле типа BufferedReader.
2. В классе ConsoleHelper должен быть реализован статический метод writeMessage(String message), выводящий сообщение на консоль.
3. В классе ConsoleHelper должен быть реализован статический метод readString, возвращающий строку считанную с консоли.
4. В классе ConsoleHelper должен быть реализован статический метод readInt, возвращающий число считанное с консоли.
5. Метод readInt должен использовать метод readString для чтения с консоли.
6. Метод readString должен перехватывать IOException, выводить сообщение о некорректном вводе и повторять считывание с консоли.
7. Метод readInt должен перехватывать NumberFormatException, выводить сообщение о некорректном вводе и повторять считывание с консоли.*/
}
